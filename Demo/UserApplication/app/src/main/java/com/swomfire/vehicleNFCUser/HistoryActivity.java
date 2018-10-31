package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Util.RmaAPIUtils;
import adapter.HistoryAdapter;
import model.Order;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.DBHelper;
import service.UserService;
import sqliteModel.History;
import sqliteModel.HistoryDetail;

public class HistoryActivity extends Activity {
    private Context context;
    TextView txtPos,lbl_toolbar;
    List<History> historyList = new ArrayList<History>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = this;

        lbl_toolbar = findViewById(R.id.lbl_toolbar);
        lbl_toolbar.setText("Lịch Sử");
        lbl_toolbar.setTypeface(null, Typeface.BOLD);


        progressDialog = UserService.setUpProcessDialog(context);
        progressDialog.show();
        DBHelper db = new DBHelper(context);
        historyList = db.getAllOrder();
        if (historyList != null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyList);
            HistoryAdapter historyAdapter = new HistoryAdapter(historyList);
            GridLayoutManager gLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(historyAdapter);
            progressDialog.cancel();
        }
        loadOrderByUserId();

    }

    public void loadOrderByUserId() {
        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String restoredText = prefs.getString("userId", "1");

        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getOrderByUserId(Integer.parseInt(restoredText)).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    List<Order> resultList = response.body();
                    if (resultList != null) {
                        DBHelper db = new DBHelper(context);
                        db.deleteAllContact();
                        for (Order order : resultList) {
                            db.insertOrder(order);
                        }
                        historyList = db.getAllOrder();
                        if (historyList != null) {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyList);
                            HistoryAdapter historyAdapter = new HistoryAdapter(historyList);
                            GridLayoutManager gLayoutManager = new GridLayoutManager(context, 1);
                            recyclerView.setLayoutManager(gLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(historyAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void onClickCard(View view) {
        txtPos = view.findViewById(R.id.txtPos);
        int i = Integer.parseInt((String) txtPos.getText());

        Intent newActivity1 = new Intent(context, HistoryDetailActivity.class);
        newActivity1.putExtra("hisItem", historyList.get(i));
        startActivity(newActivity1);

    }

    public void onBackButton(View view) {
        finish();
    }
}
