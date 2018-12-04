package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoVehicleMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_vehicle_main);
    }

    public void onClickAddVehicle(View view) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    public void popUpMenu(View view) {
        Intent intent = new Intent(this, TransparentActivity.class);
        intent.putExtra("switcher", TransparentActivity.POP_MENU);
        startActivity(intent);
    }
}
