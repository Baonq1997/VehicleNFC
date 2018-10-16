package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Util.RmaAPIUtils;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends Activity {

    EditText txtFirstname, txtLastname, txtPhone, txtPassword, txtVehicalID, txtVehicalChungNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtVehicalID = findViewById(R.id.txtVehicalID);
        txtVehicalChungNhan = findViewById(R.id.txtVehicalChungNhan);
    }

    public void onclickAdd(View view) {


        boolean flag = true;

        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        boolean flag5 = true;
        User user = new User();

        String fistname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();
        String phone = txtPhone.getText().toString();
        String pass = txtPassword.getText().toString();
        String veid = txtVehicalID.getText().toString();
        String vechungnhan = txtVehicalChungNhan.getText().toString();

        if (fistname.length() < 1) {
            flag = false;
            txtFirstname.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtFirstname.setBackgroundResource(R.drawable.signupedt);
        }

        if (lastname.length() < 1) {
            flag1 = false;
            txtLastname.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtLastname.setBackgroundResource(R.drawable.signupedt);
        }

        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            flag2 = false;
            txtPhone.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtPhone.setBackgroundResource(R.drawable.signupedt);
        }

        if (pass.length() < 6) {
            flag3 = false;
            txtPassword.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtPassword.setBackgroundResource(R.drawable.signupedt);
        }

        if (!veid.matches("[0-9]{2}[A-Z]{1}-[0-9]{5}")) {
            flag4 = false;
            txtVehicalID.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtVehicalID.setBackgroundResource(R.drawable.signupedt);
        }

        if (!vechungnhan.matches("[0-9]{8}")) {
            flag5 = false;
            txtVehicalChungNhan.setBackgroundResource(R.drawable.signuperror);
        } else {
            txtVehicalChungNhan.setBackgroundResource(R.drawable.signupedt);
        }

        if ((flag == true) && (flag1 == true) && (flag2 == true) && (flag3 == true) && (flag4 == true) && (flag5 == true)) {

            user.setFirstName(fistname);
            user.setLastName(lastname);
            user.setPhone(phone);
            user.setPassword(pass);
            user.setVehicleNumber(veid);
            user.setLicensePlateId(vechungnhan);

            Toast.makeText(getBaseContext(), "Thanh cong!", Toast.LENGTH_LONG).show();

            RmaAPIService mService = RmaAPIUtils.getAPIService();
            mService.sendUserToServer(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "Thanh cong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }


    }

    public void haveAccount(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

    }
}
