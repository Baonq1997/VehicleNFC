package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import service.UserService;

public class SignInActivity extends Activity {
    Context context;
    EditText txtPhone, txtPassword;
    ProgressDialog progressDialog;
    TextView lbl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Đăng Nhập");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

        context = this;
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        progressDialog = UserService.setUpProcessDialog(context);

    }

    public void signIn(View view) {
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        String phone = txtPhone.getText().toString();
        String password = txtPassword.getText().toString();
        progressDialog.show();
        mService.login(phone, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    User result = response.body();
                    if (result != null) {
                        if (result.isActivated()) {
                            SharedPreferences.Editor a = getSharedPreferences("localData", MODE_PRIVATE).edit();
                            a.clear();
                            SharedPreferences.Editor editor = getSharedPreferences("localData", MODE_PRIVATE).edit();
                            editor.putString("phoneNumberSignIn", phone);
                            editor.putString("userId", result.getId());
                            editor.putString("userName", result.getLastName() + " " + result.getFirstName());
                            editor.commit();

                            Intent intent = new Intent(context, NFCActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, VerifyActivity.class);
                            intent.putExtra("phoneNumber", phone);
                            intent.putExtra("type", "create-account");
                            intent.putExtra("userId", result.getId());
                            intent.putExtra("userName", result.getLastName() + " " + result.getFirstName());
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Số điện thoại hoặc mật khẩu không đúng!", Toast.LENGTH_LONG).show();
                        txtPhone.setText("");
                        txtPassword.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//        Intent intent = new Intent(this, NFCActivity.class);
//        startActivity(intent);

    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void toResetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void onBackButton(View view) {
        finish();
    }
}
