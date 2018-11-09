package day01.swomfire.meterapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import adapter.PricingAdapter;
import model.Pricing;

public class PricingPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing_popup);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        List<LinkedTreeMap> values = new Gson().fromJson(getIntent().getStringExtra("Pricing"), List.class);
        List<Pricing> pricings = new ArrayList<>();
        for (LinkedTreeMap s : values
                ) {
            pricings.add(new Gson().fromJson(s.toString(), Pricing.class));
        }
        if (pricings != null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listPricing);
            PricingAdapter pricingAdapter = new PricingAdapter(pricings);
            GridLayoutManager gLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(pricingAdapter);
        }
    }

    public void cancel(View view) {
        finish();
    }
}
