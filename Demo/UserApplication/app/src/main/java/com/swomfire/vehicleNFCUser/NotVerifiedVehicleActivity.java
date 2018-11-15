package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class NotVerifiedVehicleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified_vehicle);
    }

    public void onBackButton(View view) {
        finish();
    }
}
