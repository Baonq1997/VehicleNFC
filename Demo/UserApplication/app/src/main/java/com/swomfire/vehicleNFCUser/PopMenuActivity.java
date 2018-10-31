package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import Util.RmaAPIUtils;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.DBHelper;
import service.UserService;

public class PopMenuActivity extends Activity {

    TextView txtName, txtPhoneNumber;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.popup_menu_enter, R.anim.popup_menu_exit);
        setContentView(R.layout.activity_pop_menu);
        context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setGravity(Gravity.LEFT);
        getWindow().setLayout((int) (width * .8), height);

        txtName = findViewById(R.id.txtUserName);
        txtPhoneNumber = findViewById(R.id.txtUserPhone);

        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String userName = prefs.getString("userName", "1");
        String userPhoneNumber = prefs.getString("phoneNumberSignIn", "1");
        txtName.setText(userName);
        txtPhoneNumber.setText(userPhoneNumber);

//        getWindow().setLayout(width, height);
    }

    public void closePopup(View view) {
        finish();
    }

    public void toUserProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void viewHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.popup_menu_enter, R.anim.popup_menu_exit);
    }

    public void logOut(View view) {
        DBHelper db = new DBHelper(context);
        //TODO clear all records
        db.deleteAllContact();
        //Clear old id
        SharedPreferences.Editor a = getSharedPreferences("localData", MODE_PRIVATE).edit();
        a.clear().commit();
        //clear sqlite db
        context.deleteDatabase("ParkingWithNFC.db");

        Intent intent = new Intent(this, WelcomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    ProgressDialog progressDialog;

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

}
