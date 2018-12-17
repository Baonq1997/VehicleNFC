package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import Util.RmaAPIUtils;
import model.ResponseObject;
import model.User;
import model.Vehicle;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;

public class SignUpActivity extends Activity {
    EditText txtFirstname, txtLastname, txtPhone, txtPassword, txtVehicalID, txtVehicalLicenceId;
    private Context context;
    TextView lbl_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Đăng Kí Tài  Khoản");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);

        context = this;
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtVehicalID = findViewById(R.id.txtVehicalID);
        txtVehicalLicenceId = findViewById(R.id.txtVehicalLicenceId);
    }

    ProgressDialog progressDialog;

    public void signUpUser(View view) {

        progressDialog = UserService.setUpProcessDialog(context);
        progressDialog.show();

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
        String vechungnhan = txtVehicalLicenceId.getText().toString();

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

//        if (veid.matches("[0-9]{2}[A-Z]{1}-[0-9]{5}") || veid.matches("[0-9]{2}[A-Z]{1}-[0-9]{3}.[0-9]{2}[A-Z]{")
//                || veid.matches("[A-Z]{2}[0-9]{2}-[0-9]{2}") || veid.matches("[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{5}")
//                || veid.matches("[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{3}.[0-9]{2}")) {
//            txtVehicalID.setBackgroundResource(R.drawable.signupedt);
//        } else {
//            flag4 = false;
//            txtVehicalID.setBackgroundResource(R.drawable.signuperror);
//        }
//
//        if (!vechungnhan.matches("[0-9]{5,10}")) {
//            flag5 = false;
//            txtVehicalLicenceId.setBackgroundResource(R.drawable.signuperror);
//        } else {
//            txtVehicalLicenceId.setBackgroundResource(R.drawable.signupedt);
//        }

//        if ((flag == true) && (flag1 == true) && (flag2 == true) && (flag3 == true) && (flag4 == true) && (flag5 == true)) {

        user.setFirstName(fistname);
        user.setLastName(lastname);
        user.setPhone(phone);
        user.setPassword(pass);
        //user. setVehicleNumber(veid);
        //user.setLicensePlateId(vechungnhan);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(veid);
        vehicle.setLicensePlateId(vechungnhan);
        user.setVehicle(vehicle);

        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.sendUserToServer(user).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    ResponseObject responseObject = response.body();
//                        (response.isSuccessful()) ? response.body() : response.errorBody();
                    if (responseObject != null) {
                        String msg = responseObject.getData().toString();
                        if (responseObject.getStatus() == 200) {
                            Intent intent = new Intent(getApplicationContext(), TransparentActivity.class);
                            intent.putExtra("switcher", TransparentActivity.SIGN_UP);
                            intent.putExtra("extra", true);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (response.code() == 409) {
                        try {
                            JSONObject errMsg = new JSONObject(response.errorBody().string());
                            String msg = errMsg.getString("data");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                progressDialog.cancel();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
//                    Intent intent = new Intent(context, CreateSuccessActivity.class);
//                    startActivity(intent);
                progressDialog.cancel();
            }
        });

    }

    public void haveAccount(View v){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    public void onBackButton(View view) {
        finish();
    }
}
