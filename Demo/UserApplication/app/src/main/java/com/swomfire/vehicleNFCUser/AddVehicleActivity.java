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
import model.Vehicle;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleActivity extends Activity {

    TextView lbl_toolbar;
    EditText edtNumber, edtPlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Thêm Phương Tiện");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);
    }

    public void onClickXacNhan(View v){

        edtNumber = findViewById(R.id.edtNumber);
        edtPlate = findViewById(R.id.edtPlate);

        String veid = edtNumber.getText().toString();
        String vechungnhan = edtPlate.getText().toString();
        boolean flag1 = true;
        boolean flag2 = true;

        if (!veid.matches("[0-9]{2}[A-Z]{1}-[0-9]{5}") || veid.matches("[0-9]{2}[A-Z]{1}-[0-9]{3}.[0-9]{2}")
                || veid.matches("[A-Z]{2}[0-9]{2}-[0-9]{2}") || veid.matches("[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{5}")
                || veid.matches("[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{3}.[0-9]{2}")) {
            edtNumber.setBackgroundResource(R.drawable.signupedt);
        } else {
            flag1 = false;
            edtNumber.setBackgroundResource(R.drawable.signuperror);
        }

        if (!vechungnhan.matches("[0-9]{5,15}")) {
            flag2 = false;
            edtPlate.setBackgroundResource(R.drawable.signuperror);
        } else {
            edtPlate.setBackgroundResource(R.drawable.signupedt);
        }

        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String phone = prefs.getString("phoneNumberSignIn", "1");

        if (flag1 && flag2 ) {

            RmaAPIService mService = RmaAPIUtils.getAPIService();
            mService.changeVehicle(phone,veid,vechungnhan).enqueue(new Callback<Vehicle>() {
                @Override
                public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {

                    Vehicle result = response.body();
                    if (result != null) {

                        if (result.isVerified()) {
                            Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddVehicleSuccessActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Không thể sử dụng phương tiện này!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Vehicle> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }

    }

    public void onBackButton(View v){
        finish();
    }

}
