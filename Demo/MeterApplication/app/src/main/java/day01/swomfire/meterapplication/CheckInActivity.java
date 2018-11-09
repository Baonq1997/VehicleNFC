package day01.swomfire.meterapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Util.RmaAPIUtils;
import adapter.PricingAdapter;
import model.Location;
import model.Order;
import model.Policy;
import model.PolicyHasVehicleType;
import model.Pricing;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;

public class CheckInActivity extends Activity {
    TextView txtLocation, txtAllowedFrom, txtAllowedTo, txtUserVehicleNumber, txtUserVehicleType, txtUserWallet;

    private User user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        pd = new ProgressDialog(context);
        pd.setTitle("Đang xử lý...");
        pd.setMessage("Xin chờ.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        Location location = new Gson().fromJson(getIntent().getStringExtra("location"), Location.class);
        user = new Gson().fromJson(getIntent().getStringExtra("user"), User.class);
        setUpLocationInfo(location, user);
        System.out.println();
    }

    public void cancel(View view) {
        finish();
    }

    public void confirmCheckin(View view) {
        Intent currentIntent = getIntent();
        String userId = user.getId();
        String token = currentIntent.getStringExtra("token");

        confirmPayment(userId, token);
    }

    ProgressDialog pd;

    public void confirmPayment(String userId, String token) {
        Order order = new Order();

        User user = new User();
        user.setId(userId);
        user.setDeviceToken(token);

        Location location = new Location();
        location.setId(1);

        order.setUser(user);

        order.setLocation(location);
        pd.show();
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.sendTransactionToServer(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order result = response.body();
                    if (result != null) {
                        pd.cancel();
                        setContentView(R.layout.activity_check_in_complete);
                        setUpUserInfo(result.getUser(), result);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                pd.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpLocationInfo(Location location, User user) {
        //Check if user allowed to parking here
        if (Double.parseDouble(user.getMoney()) <= 0) {
            Toast.makeText(this, "Số dư tài khoản quý khách không đủ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Policy policy = UserService.findMatchedPolicy(user, location);
        if (policy != null) {
            readyToCheckIn(user, location, policy);
            return;
        }
        Toast.makeText(this, "Quý khách không được đậu xe tại đây", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setUpUserInfo(User user, Order order) {
        txtUserVehicleNumber = findViewById(R.id.txtVehicleNumber);
        txtUserVehicleType = findViewById(R.id.txtVehicleType);
        TextView txtUsername = findViewById(R.id.txtUsername);
        TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        TextView txtCheckInDate = findViewById(R.id.txtCheckInDate);
        txtLocation = findViewById(R.id.txtLocation);


        txtUsername.setText(user.getLastName() + " " + user.getFirstName());
        txtUserVehicleNumber.setText(user.getVehicle().getVehicleNumber());
        txtUserVehicleType.setText(order.getVehicleType().getName());
        txtPhoneNumber.setText(user.getPhone());
        String pattern = "HH:mm dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        txtCheckInDate.setText(simpleDateFormat.format(new Date(order.getCheckInDate())));
        txtLocation.setText(order.getLocation().getLocation());

    }

    public void setUpFinalInfo(Order order) {
        //TODO replace info
    }

    public void readyToCheckIn(User user, Location location, Policy policy) {
        PolicyHasVehicleType policyHasVehicleType = UserService.findMatchedPolicyHasVehicleType(user, policy);
        if (policyHasVehicleType == null) {
            Toast.makeText(this, "Xe của Quý khách không được đậu tại đây", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_check_in);
        txtLocation = findViewById(R.id.txtLocation);
        txtAllowedFrom = findViewById(R.id.txtAllowedFrom);
        txtAllowedTo = findViewById(R.id.txtAllowedTo);
        txtUserVehicleNumber = findViewById(R.id.txtVehicleNumber);
        txtUserVehicleType = findViewById(R.id.txtVehicleType);
        txtUserWallet = findViewById(R.id.txtWallet);

        txtUserVehicleNumber.setText(user.getVehicle().getVehicleNumber());
        txtUserVehicleType.setText(user.getVehicle().getVehicleTypeId().getName());
        txtUserWallet.setText(UserService.convertMoney(Double.parseDouble(user.getMoney())));


        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        txtLocation.setText(location.getLocation());
        txtAllowedFrom.setText(simpleDateFormat.format(new Date(
                policy.getAllowedParkingFrom()
        )));

        txtLocation.setText(location.getLocation());
        txtAllowedTo.setText(simpleDateFormat.format(new Date(
                policy.getAllowedParkingTo()
        )));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listPricing);
        PricingAdapter pricingAdapter = new PricingAdapter(policyHasVehicleType.getPricings());
        GridLayoutManager gLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pricingAdapter);
    }
}


