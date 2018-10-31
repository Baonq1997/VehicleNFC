package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Util.RmaAPIUtils;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.DBHelper;
import service.UserService;

public class ChangePasswordActivity extends Activity {

    EditText edtCurPass, edtOldPass, edtNewPass;
    TextView lbl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtCurPass = findViewById(R.id.edtCurPass);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Đổi Mật Khẩu");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

    }

    public void onClickXacNhan(View v) {

        String pass0 = edtCurPass.getText().toString();
        String pass1 = edtOldPass.getText().toString();
        String pass2 = edtNewPass.getText().toString();
        //String phone = (String) getIntent().getExtras().get("phone");

        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String phone = prefs.getString("phoneNumberSignIn", "1");

        boolean fl = true;
        boolean flag = true;
        boolean flag2 = true;

        if (pass0.length() < 6) {
            fl = false;
            edtCurPass.setBackgroundResource(R.drawable.signuperror);
            Toast.makeText(getApplicationContext(), "Mật khẩu cũ có ít nhất 6 kí tự", Toast.LENGTH_LONG).show();
        } else {
            edtCurPass.setBackgroundResource(R.drawable.signupedt);
            if (pass1.length() < 6) {
                flag = false;
                edtOldPass.setBackgroundResource(R.drawable.signuperror);
                Toast.makeText(getApplicationContext(), "Mật khẩu mới phải có ít nhất 6 kí tự", Toast.LENGTH_LONG).show();
            } else {
                edtOldPass.setBackgroundResource(R.drawable.signupedt);
                if (!pass1.matches(pass2)) {
                    edtOldPass.setBackgroundResource(R.drawable.signuperror);
                    edtNewPass.setBackgroundResource(R.drawable.signuperror);
                    Toast.makeText(getApplicationContext(), "Mật khẩu xác nhận không khớp!", Toast.LENGTH_LONG).show();
                    edtOldPass.setText("");
                    edtNewPass.setText("");
                    flag2 = false;
                } else {
                    edtOldPass.setBackgroundResource(R.drawable.signupedt);
                    edtNewPass.setBackgroundResource(R.drawable.signupedt);
                }
            }

            if (fl && flag && flag2) {

                RmaAPIService mService = RmaAPIUtils.getAPIService();
                mService.changePassword(phone, pass0, pass1).enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        // DO STH HERE !! show toast + chuyen ve activity profile
                        if (response.isSuccessful()) {
                            if (response.body()) {

                                DBHelper db = new DBHelper(getApplicationContext());
                                //TODO clear all records
                                db.deleteAllContact();
                                //Clear old id
                                SharedPreferences.Editor a = getSharedPreferences("localData", MODE_PRIVATE).edit();
                                a.clear().commit();
                                //clear sqlite db
                                getApplicationContext().deleteDatabase("ParkingWithNFC.db");

                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

            }

        }
    }

    public void onBackButton(View view) {
        finish();
    }
}
