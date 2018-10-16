package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Sqlite.History;
import adapter.HistoryAdapter;

public class HistoryActivity extends Activity {
    private Context context;
    TextView txtPos;
    List<History> historyList = new ArrayList<History>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = this;

        new HttpAsyncTask().execute("http://25.27.125.196:8080/order/orders?userId=1");

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void onClickCard(View view) {

        //Intent i = new Intent(context, HistoryDetail.class);
        //startActivity(i);

        txtPos = findViewById(R.id.txtPos);
        int i = Integer.parseInt((String)txtPos.getText());

        Intent newActivity1 = new Intent(context, HistoryDetail.class);
        newActivity1.putExtra("hisItem", historyList.get(i));
        startActivity(newActivity1);

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray list = new JSONArray(result);

                for (int i = 0; i < list.length(); i++) {

                    JSONObject json = list.getJSONObject(i);

                    int id = json.getInt("id");
                    double total = 0;
                    int checkInDate = 0;
                    int checkOutDate = 0;
                    int duration = 0;
                    int allowedParkingFrom = 0;
                    int allowedParkingTo = 0;
                    try {
                        total = json.getDouble("total");
                    } catch (Exception e) {

                    }
                    try {
                        checkInDate = json.getInt("checkInDate");
                    } catch (Exception e) {

                    }
                    try {
                        checkOutDate = json.getInt("checkOutDate");
                    } catch (Exception e) {

                    }
                    try {
                        duration = json.getInt("duration");
                    } catch (Exception e) {

                    }
                    try {
                        allowedParkingFrom = json.getInt("allowedParkingFrom");
                    } catch (Exception e) {

                    }
                    try {
                        allowedParkingTo = json.getInt("allowedParkingTo");
                    } catch (Exception e) {

                    }
                    JSONObject location = json.getJSONObject("locationId");
                    String locationname = location.getString("location");
                    JSONObject orderstatus = json.getJSONObject("orderStatusId");
                    String orderstatusname = orderstatus.getString("name");
                    JSONObject user = json.getJSONObject("userId");
                    String username = user.getString("firstName") + " " + user.getString("lastName");
                    String vehicalid = user.getString("vehicleNumber");
                    JSONObject vehical = user.getJSONObject("vehicleTypeId");
                    String vehicalname = vehical.getString("name");
                    History c = new History(id, total, checkInDate, checkOutDate, duration, allowedParkingFrom, allowedParkingTo, orderstatusname, username, vehicalname, vehicalid, locationname);
                    historyList.add(c);

                    //DBHelper db = new DBHelper(context);
                    //db.insertOrder(total, checkInDate, checkOutDate, duration, allowedParkingFrom, allowedParkingTo, orderstatusname, username, vehicalname, vehicalid, locationname);


                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyList);
                HistoryAdapter his = new HistoryAdapter(historyList);
                GridLayoutManager gLayoutManager = new GridLayoutManager(context, 1);
                recyclerView.setLayoutManager(gLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(his);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
