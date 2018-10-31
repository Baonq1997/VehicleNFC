package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Util.RmaAPIUtils;
import adapter.HistoryPricingAdapter;
import adapter.OrderPricingAdapter;
import model.HourHasPrice;
import model.Order;
import model.OrderPricing;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;

public class ShowPopupPrice extends Activity {

    TextView txtTime, txtTotal,txtThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_popup_price);
        txtTime = findViewById(R.id.txtTime);
        txtTotal = findViewById(R.id.txtTotal);
        txtThongBao = findViewById(R.id.txtThongBao);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        int id = (int) getIntent().getExtras().get("itemID");

        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getOrderById(id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order result = response.body();
                    if (result != null) {

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM");

                        txtTime.setText(sdf.format(result.getCheckInDate()) + " đến " + sdf.format(result.getCheckOutDate()));
                        txtTotal.setText("Tổng tiền: "+ UserService.convertMoney(result.getTotal()));

                        List<OrderPricing> orderPricings = result.getOrderPricings();
                        int h = (int) (result.getDuration() / 3600000);
                        int h2 = h;

                        int m = (int) (result.getDuration() - h * 3600000) / 60000;

                        if (h < result.getMinHour()) {
                            txtThongBao.setVisibility(View.VISIBLE);
                            txtThongBao.setText("Thời gian đỗ tối thiểu quy đinh là: "+ result.getMinHour() + " giờ");
                            h = result.getMinHour();
                        } else {

                            txtThongBao.setVisibility(View.GONE);
                        }

                        List<HourHasPrice> hourHasPrices = new ArrayList<>();

                        while (h > 0) {
                            hourHasPrices.add(new HourHasPrice(h, null));
                            h--;
                        }

                        double lastPrice = 0;
                        for (OrderPricing orderPricing : orderPricings) {
                            if (orderPricing.getPricePerHour() > lastPrice) {
                                lastPrice = orderPricing.getPricePerHour();
                            }
                            for (HourHasPrice hourHasPrice : hourHasPrices) {
                                if (orderPricing.getFromHour() < hourHasPrice.getHour()) {
                                    hourHasPrice.setPrice(orderPricing.getPricePerHour());
                                }
                            }
                        }
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listHistoryPricing);
                        HistoryPricingAdapter historyPricingAdapter = new HistoryPricingAdapter(hourHasPrices, m, result.getCheckInDate(), result.getCheckOutDate());
                        GridLayoutManager gLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        recyclerView.setLayoutManager(gLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(historyPricingAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.8));
    }
}
