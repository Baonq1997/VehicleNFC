package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Context;
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

public class ForgetPasswordActivity extends Activity {

    EditText txtPhone;
    Context context;
    TextView lbl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        context = this;
        txtPhone = findViewById(R.id.txtPhone);
        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Quên Mật Khẩu");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

    }

    public void onClickXacNhan(View v) {

        String phone = txtPhone.getText().toString();
        boolean flag = true;

        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            flag = false;
            txtPhone.setBackgroundResource(R.drawable.signuperror);
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_LONG).show();
        } else {
            txtPhone.setBackgroundResource(R.drawable.signupedt);
        }

        if (flag) {
            RmaAPIService mService = RmaAPIUtils.getAPIService();
            mService.requestResetPassword(phone).enqueue(new Callback<Boolean>() {

                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        if (response.body()) {
                            Intent intent = new Intent(context, VerifyActivity.class);
                            intent.putExtra("phoneNumber", phone);
                            intent.putExtra("type", "change-pass");
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

    public void onBackButton(View view) {
        finish();
    }
}
