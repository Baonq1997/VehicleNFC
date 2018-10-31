package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
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

public class VerifyActivity extends Activity {

    EditText edtConfirm;
    TextView lbl_toolbar,txtChangPass, txtThongBao;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphone);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Verify Phone Number");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

        phone = (String) getIntent().getExtras().get("phoneNumber");
        edtConfirm = findViewById(R.id.edtConfirm);
        txtChangPass = findViewById(R.id.txtChangpass);
        txtThongBao = findViewById(R.id.txtThongBao);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtChangPass.setText("Chưa nhận được tin nhắn? (" + millisUntilFinished / 1000 + ")");
                txtChangPass.setEnabled(false);
                txtChangPass.setTextColor(Color.parseColor("#CCCCCC"));
            }

            public void onFinish() {
                txtChangPass.setText(Html.fromHtml("<p><u>Chưa nhận được tin nhắn? </u></p>"));
                txtChangPass.setTextColor(Color.parseColor("#006699"));
                txtChangPass.setEnabled(true);
            }
        }.start();

    }

    public void onClickConfirm(View v) {


        String code = edtConfirm.getText().toString();
        String type = (String) getIntent().getExtras().get("type");
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        if (type.matches("create-account")) {
            mService.verifyNumber(phone, code).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result) {

                            SharedPreferences.Editor a = getSharedPreferences("localData", MODE_PRIVATE).edit();
                            a.clear();
                            SharedPreferences.Editor editor = getSharedPreferences("localData", MODE_PRIVATE).edit();
                            editor.putString("phoneNumberSignIn", phone);
                            editor.putString("userId", (String) getIntent().getExtras().get("userId"));
                            editor.putString("userName", (String) getIntent().getExtras().get("userName"));
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Mã xác nhận không đúng! Vui lòng nhập lại.", Toast.LENGTH_LONG).show();
                            edtConfirm.setText("");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else if (type.matches("change-pass")) {
            mService.verifyResetPassword(phone, code).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result) {
                            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Mã xác nhận không đúng! Vui lòng nhập lại.", Toast.LENGTH_LONG).show();
                            edtConfirm.setText("");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    public void onClickResend(View v) {

        //String phone = (String) getIntent().getExtras().get("phonenumber");
//        String phone = "+841224093586";
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.resendCode(phone).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                txtThongBao.setText("Một mã xác nhận đã được gửi lại số điện thoải của bạn. Vui lòng nhập mã mới và xác nhận.");
                Toast.makeText(getApplicationContext(), "Một mã mới đã được gửi lại số điện thoại bạn!", Toast.LENGTH_SHORT).show();

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txtChangPass.setText("Chưa nhận được tin nhắn? (" + millisUntilFinished / 1000 + ")");
                        txtChangPass.setEnabled(false);
                        txtChangPass.setTextColor(Color.parseColor("#CCCCCC"));
                    }

                    public void onFinish() {
                        txtChangPass.setText(Html.fromHtml("<p><u>Chưa nhận được tin nhắn? </u></p>"));
                        txtChangPass.setTextColor(Color.parseColor("#006699"));
                        txtChangPass.setEnabled(true);
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Vào failure rồi nè!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBackButton(View view) {
        finish();
    }
}
