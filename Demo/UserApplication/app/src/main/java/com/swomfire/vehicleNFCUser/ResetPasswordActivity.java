package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Util.RmaAPIUtils;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends Activity {

    EditText edtOldPass, edtNewPass;
    TextView lbl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Đổi mật khẩu");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

        edtNewPass = findViewById(R.id.edtNewPass);
        edtOldPass = findViewById(R.id.edtOldPass);
    }

    public void onClickXacNhan(View v) {

        String pass1 = edtOldPass.getText().toString();
        String pass2 = edtNewPass.getText().toString();
        String phone = (String) getIntent().getExtras().get("phone");

        boolean flag = true;
        boolean flag2 = true;

        if (pass1.length() < 6) {
            flag = false;
            edtOldPass.setBackgroundResource(R.drawable.signuperror);
            Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_LONG).show();
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

        if (flag && flag2) {

            RmaAPIService mService = RmaAPIUtils.getAPIService();
            mService.updatePasswordByPhone(phone, pass1).enqueue(new Callback<Boolean>() {

                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    // DO STH HERE !! show toast + chuyen ve activity profile
                    if (response.isSuccessful()) {
                        if (response.body()) {
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                        }
                    }

                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });

        }
    }
        public void onBackButton(View v){
            finish();
        }
}
