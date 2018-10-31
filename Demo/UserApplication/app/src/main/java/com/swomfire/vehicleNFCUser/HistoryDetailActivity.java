package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import service.UserService;
import sqliteModel.History;

public class HistoryDetailActivity extends Activity {

    TextView lbl_toolbar,txtName, txtVehicalName, txtVehicalID, txtLocation, txtFrom, txtTo, txtDuration, txtTotal;
    History item;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        myDialog = new Dialog(this);

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Thông Tin Chi Tiết");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);


        item = (History) getIntent().getExtras().get("hisItem");

        txtName = findViewById(R.id.txtName);
        txtVehicalName = findViewById(R.id.txtVehicalName);
        txtVehicalID = findViewById(R.id.txtVehicalID);
        txtLocation = findViewById(R.id.txtLocation);
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);
        txtDuration = findViewById(R.id.txtDuration);
        txtTotal = findViewById(R.id.txtTotal);
        Date date = new Date(item.getCheck_in_date());

        txtVehicalName.setText(item.getVehical_name());
        txtVehicalID.setText(item.getVehical_id());
        txtLocation.setText(item.getTbl_location_id());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        txtFrom.setText(sdf.format(item.getCheck_in_date()) + " -> " + sdf.format(item.getCheck_out_date()) + "   ");

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

        txtTo.setText(sdf2.format(item.getCheck_in_date()));

        long time = item.getDuration();
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        txtDuration.setText(h + "giờ "  + m+ " phút");

        txtTotal.setText(UserService.convertMoney((double)item.getTotal()));
    }

    public void clickMore(View view) {
        Intent intent = new Intent(this, TransparentActivity.class);
        intent.putExtra("switcher", TransparentActivity.POP_PRICE_LIST);
        intent.putExtra("extra", item.getId());
        startActivity(intent);
    }

    public void onBackButton(View view) {
        finish();
    }


}
