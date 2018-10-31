package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import Util.RmaAPIUtils;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;

public class ProfileActivity extends Activity {

    TextView lbl_toolbar, txtMoney, txtName, txtPhone, txtVehicalID, txtVehicalName, txtDangKiem;
    ImageView imageXe;
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Thông Tin Tài Khoản");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

        context = this;
        progressDialog = UserService.setUpProcessDialog(this);
        progressDialog.show();

        txtMoney = findViewById(R.id.txtMoney);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtVehicalID = findViewById(R.id.txtVehicalID);
        txtVehicalName = findViewById(R.id.txtVehicalName);
        txtDangKiem = findViewById(R.id.txtDangKiem);
        imageXe = findViewById(R.id.imageXe);

        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String restoredText = prefs.getString("phoneNumberSignIn", "1");
        //String name = "1324658";
        getUserInfo(restoredText);

    }

    public void getUserInfo(String phone) {

        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getUserByPhone(phone).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    User user = response.body();
                    if (user != null) {

                        txtMoney.setText(UserService.convertMoney(Double.parseDouble(user.getMoney())));
                        txtName.setText(user.getFirstName() + " " + user.getLastName());
                        txtPhone.setText(user.getPhone());
                        txtVehicalID.setText(user.getVehicle().getVehicleNumber());

                        String count = user.getVehicle().getVehicleTypeId().getName();
                        txtVehicalName.setText(count);
                        if (count.matches("16 chỗ")) {
                            imageXe.setImageDrawable(getResources().getDrawable(R.drawable.cartypeicobig));
                        }

                        txtDangKiem.setText(user.getVehicle().getLicensePlateId());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void topUp(View view) {
        progressDialog = UserService.setUpProcessDialog(context);
        progressDialog.show();
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getUSD("http://v3.exchangerate-api.com/bulk/3d78ccdddf5bd1c43a6587ff/USD").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject obj = response.body();
                    obj = obj.getAsJsonObject("rates");
                    String s = obj.get("VND").toString();
                    Intent intent = new Intent(getApplicationContext(), TransparentActivity.class);
                    intent.putExtra("switcher", TransparentActivity.POP_TOP_UP);
                    intent.putExtra("extra", true);
                    intent.putExtra("USD", s);
                    startActivity(intent);
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.cancel();
                System.err.println(t);
            }
        });
    }


    public void onClickChangePass(View v) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String restoredText = prefs.getString("phoneNumberSignIn", "1");
        //String name = "1324658";
        getUserInfo(restoredText);
    }

    public void onBackButton(View view) {
        finish();
    }
}
