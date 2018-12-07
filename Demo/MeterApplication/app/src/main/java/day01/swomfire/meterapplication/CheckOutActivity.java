package day01.swomfire.meterapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Util.RmaAPIUtils;
import model.HourHasPrice;
import model.Location;
import model.Order;
import model.OrderPricing;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.TimeDuration;
import service.UserService;

public class CheckOutActivity extends Activity {

    TextView txtParkingFrom, txtParkingTo, txtUserVehicleNumber, txtUserVehicleType, txtWallet, txtMinHour;

    private User user;
    private Order order;

    ProgressDialog pd;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        context = this;
        pd = new ProgressDialog(context);
        pd.setTitle("Đang xử lý...");
        pd.setMessage("Xin chờ.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        user = new Gson().fromJson(getIntent().getStringExtra("user"), User.class);
        order = new Gson().fromJson(getIntent().getStringExtra("order"), Order.class);
        setUpInfo(order, user);
    }

    public void confirmCheckOut(View view) {

        Intent currentIntent = getIntent();
        String userId = user.getId();
        String token = currentIntent.getStringExtra("token");

        confirmPayment(userId, token);
    }

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
                    pd.cancel();
                    if (result != null) {
                        setUpFinalView(result);
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


    public void setUpInfo(Order order, User user) {
        txtParkingFrom = findViewById(R.id.txtParkingFrom);
        txtParkingTo = findViewById(R.id.txtParkingTo);
        txtUserVehicleNumber = findViewById(R.id.txtVehicleNumber);
        txtUserVehicleType = findViewById(R.id.txtVehicleType);
        txtWallet = findViewById(R.id.txtWallet);
        txtMinHour = findViewById(R.id.txtMinHour);
        TextView txtDuration = findViewById(R.id.txtDuration);
        TextView txtTotal = findViewById(R.id.txtTotal);
        TextView txtLeft = findViewById(R.id.txtLeft);

        txtUserVehicleNumber.setText(user.getVehicle().getVehicleNumber());
        txtUserVehicleType.setText(user.getVehicle().getVehicleTypeId().getName());
        txtWallet.setText(UserService.convertMoney(Double.parseDouble(user.getMoney())));


        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        TimeDuration duration;
        duration = TimeDuration.compareTwoDates(order.getCheckInDate(), new Date().getTime());
        if (duration.getHour() < order.getMinHour()) {
            findViewById(R.id.panelMinHour).setVisibility(View.VISIBLE);
            txtMinHour.setText(order.getMinHour()+" giờ");
            duration.setHour(order.getMinHour());
            duration.setMinute(0);
        }

        txtParkingFrom.setText(simpleDateFormat.format(new Date(
                order.getCheckInDate()
        )));
        txtParkingTo.setText(simpleDateFormat.format(new Date()));
        txtDuration.setText(duration.getHour() + " giờ " + duration.getMinute() + " phút");

        double totalPrice = getTotalPrice(order);
        double left = Double.parseDouble(user.getMoney()) - totalPrice;
        txtTotal.setText(UserService.convertMoney(totalPrice));
        txtLeft.setText(UserService.convertMoney(left));
    }


    public void setUpFinalView(Order order) {
        setContentView(R.layout.activity_payment_complete);
        txtParkingFrom = findViewById(R.id.txtParkingFrom);
        txtParkingTo = findViewById(R.id.txtParkingTo);
        txtUserVehicleNumber = findViewById(R.id.txtVehicleNumber);
        txtUserVehicleType = findViewById(R.id.txtVehicleType);
        TextView txtDuration = findViewById(R.id.txtDuration);
        TextView txtTotal = findViewById(R.id.txtTotal);
        TextView txtLeft = findViewById(R.id.txtLeft);
        TextView txtUsername = findViewById(R.id.txtUsername);
        TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);

        txtUserVehicleNumber.setText(order.getVehicle().getVehicleNumber());
        txtUserVehicleType.setText(order.getVehicle().getVehicleTypeId().getName());
        txtUsername.setText(order.getUser().getLastName() + " " + order.getUser().getFirstName());
        txtPhoneNumber.setText(order.getUser().getPhone());


        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        TimeDuration duration = TimeDuration.compareTwoDates(order.getCheckInDate(), order.getCheckOutDate());

        txtParkingFrom.setText(simpleDateFormat.format(new Date(
                order.getCheckInDate()
        )));
        txtParkingTo.setText(simpleDateFormat.format(new Date(order.getCheckOutDate())));
        txtDuration.setText(duration.getHour() + " giờ " + duration.getMinute() + " phút");

        double totalPrice = getTotalPrice(order);
        double left = Double.parseDouble(order.getUser().getMoney());
        txtTotal.setText(UserService.convertMoney(totalPrice));
        txtLeft.setText(UserService.convertMoney(left));
    }

    public double getTotalPrice(Order order) {
        List<HourHasPrice> hourHasPrices = UserService.composeHourPrice(new Date().getTime() - order.getCheckInDate()
                , order.getCheckInDate(), order.getAllowedParkingFrom(), order.getAllowedParkingTo(), order.getMinHour(), order.getOrderPricings());

        double totalPrice = 0;
        for (HourHasPrice hourHasPrice : hourHasPrices) {
            double money = (hourHasPrice.isLate()) ? hourHasPrice.getFine() : hourHasPrice.getPrice();
            if (hourHasPrice.isFullHour()) {
                totalPrice += money;
            } else {
                totalPrice += Math.ceil(money * ((double) hourHasPrice.getMinutes() / 60));
            }
        }
        return totalPrice;
    }

    public void cancel(View view) {
        finish();
    }
}
