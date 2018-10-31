package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TransparentActivity extends Activity {

    boolean show = false;
    public static final String POP_MENU = "PopMenuActivity";
    public static final String POP_TOP_UP = "PopTopUp";
    public static final String POP_PRICE_LIST = "PriceList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        String switcher = getIntent().getStringExtra("switcher");
        Intent intent = null;
        switch (switcher) {
            case POP_MENU:
                intent = new Intent(this, PopMenuActivity.class);
                break;
            case POP_TOP_UP:
                intent = new Intent(this, ActivityTopUpExtras.class);
                intent.putExtra("isFromProfile", getIntent().getBooleanExtra("extra", false));
                intent.putExtra("USD", getIntent().getStringExtra("USD"));
                break;
            case POP_PRICE_LIST:
                intent = new Intent(this, ShowPopupPrice.class);
                intent.putExtra("itemID", getIntent().getIntExtra("extra", 0));
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        show = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (show) {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.popup_hide);
    }
}
