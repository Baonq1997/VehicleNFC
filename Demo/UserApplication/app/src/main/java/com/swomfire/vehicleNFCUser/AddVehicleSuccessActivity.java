package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import service.DBHelper;

public class AddVehicleSuccessActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_success);
    }

    public void onClickXacNhan(View v) {

        DBHelper db = new DBHelper(getApplicationContext());
        //TODO clear all records
        db.deleteAllContact();
        //Clear old id
        SharedPreferences.Editor a = getSharedPreferences("localData", MODE_PRIVATE).edit();
        a.clear().commit();
        //clear sqlite db
        getApplicationContext().deleteDatabase("ParkingWithNFC.db");

        Intent intent = new Intent(this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
