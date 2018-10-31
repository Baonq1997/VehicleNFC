package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;

        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String userPhoneNumber = prefs.getString("phoneNumberSignIn", "Non");
        if (!userPhoneNumber.equals("Non")){
            Intent intent = new Intent(context, NFCActivity.class);
            startActivity(intent);
        }
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void toSignIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
