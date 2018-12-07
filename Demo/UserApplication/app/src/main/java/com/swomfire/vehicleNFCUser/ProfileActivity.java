package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import service.DBHelper;
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
                        if (user.getVehicle() != null) {
                            txtVehicalID.setText(user.getVehicle().getVehicleNumber());
                            String count = user.getVehicle().getVehicleTypeId().getName();
                            txtVehicalName.setText(count);
                            if (count.matches("16 chỗ")) {
                                imageXe.setImageDrawable(getResources().getDrawable(R.drawable.cartypeicobig));
                            }

                            txtDangKiem.setText(user.getVehicle().getLicensePlateId());
                        }else {
                            txtVehicalID.setText("Không có xe");
                            txtVehicalName.setText("Không có xe");
                            txtDangKiem.setText("Không có xe");
                        }
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

    public void onClickChangeVehicle(View v) {
        //Intent intent = new Intent(this, ChangeVehicleActivity.class);
        //startActivity(intent);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
                        String userid = prefs.getString("userId", "1");

                        RmaAPIService mService = RmaAPIUtils.getAPIService();
                        mService.unbindVehicle(userid).enqueue(new Callback<Boolean>() {

                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.body()) {

                                        Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                                        intent.putExtra("type", "unbindVehicle");
                                        intent.putExtra("userID", userid);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc sẽ gỡ bỏ phương tiện khỏi thiết bị hiện tại?").setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
    }

    public void onBackButton(View view) {
        finish();
    }
}
