package com.swomfire.vehicleNFCUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Util.RmaAPIUtils;
import adapter.OrderPricingAdapter;
import model.HourHasPrice;
import model.NotificationSerial;
import model.Order;
import model.OrderPricing;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;


public class NFCActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {
    Context context;
    String token = null;

    Chronometer parkingChorno;
    boolean blink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ImageView gifImageView = (ImageView) findViewById(R.id.gifHahah);
        Glide.with(this).load(R.raw.holding).into(gifImageView);
        //TODO check internet connection
        checkInternetConnection();

        NotificationSerial notificationSerial = (NotificationSerial) getIntent().getSerializableExtra("Notification");
        if (notificationSerial != null) {
            setContentView(R.layout.activity_checkout);
            parkingChorno = findViewById(R.id.chroParking);
            getOrderInfo(notificationSerial.getOrderId());
        }

        context = this;
        token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumberSignIn", "1");
        sendTokenToServer(token,phoneNumber);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Thiết bị không hỗ trợ NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Vui lòng bật chức năng NFC trong cài đặt.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    /**
     * Ndef Record that will be sent over via NFC
     *
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
        String restoredText = prefs.getString("userId", "1");
        String message = "{'userId':" + restoredText + ",'token':'" + token + "'}";
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }


    public void popUpMenu(View view) {
        Intent intent = new Intent(NFCActivity.this, TransparentActivity.class);
        intent.putExtra("switcher", TransparentActivity.POP_MENU);
        startActivity(intent);
    }

    public void updatePrice(int h, int m, Order order) {
        //TODO update price after every chrono tick.
        List<OrderPricing> orderPricings = order.getOrderPricings();
        lblTotal = findViewById(R.id.lblTotal);
        double totalPrice = 0;
        List<HourHasPrice> hourHasPrices = new ArrayList<>();
        if (h < order.getMinHour()) {
            h = order.getMinHour();
            m = 0;
        }

        while (h > 0) {
            hourHasPrices.add(new HourHasPrice(h, null));
            h--;
        }

        double lastPrice = 0;
        for (OrderPricing orderPricing : orderPricings
                ) {
            if (orderPricing.getPricePerHour() > lastPrice) {
                lastPrice = orderPricing.getPricePerHour();
            }
            for (HourHasPrice hourHasPrice : hourHasPrices) {
                if (orderPricing.getFromHour() < hourHasPrice.getHour()) {
                    hourHasPrice.setPrice(orderPricing.getPricePerHour());
                }
            }
        }

        for (
                HourHasPrice hourHasPrice : hourHasPrices
                ) {
            totalPrice += hourHasPrice.getPrice();
        }

        totalPrice += lastPrice * ((double) m / 60);
        try {
            lblTotal.setText(UserService.convertMoney(totalPrice));
        } catch (NullPointerException e) {}
    }

    TextView lblTotal;

    private void setUpChrono(Order order) {
        System.out.println(new Date().getTime());
        parkingChorno.setBase(order.getCheckInDate());
        parkingChorno.setOnChronometerTickListener(chronometer -> {
            long time = new Date().getTime() - chronometer.getBase();
            int h = (int) (time / 3600000);
            int m = (int) (time - h * 3600000) / 60000;
            String timeString = (blink) ? String.format("%02d %02d", h, m) : String.format("%02d:%02d", h, m);
            blink = !blink;
            chronometer.setText(timeString);
            updatePrice(h, m, order);
        });
        parkingChorno.start();
    }

    private boolean hasCurrentOrder = false;
    private boolean pause = false;

    public void getOrderInfo(String orderId) {
        if (orderId == null) {
            //TODO get user info
            User user = null;
//            if (user == null) {
//                return;
//            }
            SharedPreferences prefs = getSharedPreferences("localData", MODE_PRIVATE);
            String userId = prefs.getString("userId", "1");

            RmaAPIService mService = RmaAPIUtils.getAPIService();
            mService.getOpenOrderByUserId(Integer.parseInt(userId)).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        Order result = response.body();
                        if (result != null) {
                            if (!hasCurrentOrder) {
                                setContentView(R.layout.activity_checkout);
                                parkingChorno = findViewById(R.id.chroParking);
                                setUpChrono(result);
                                setUpOrderInfo(result);
                                hasCurrentOrder = true;
                            }
                        } else if (result == null && hasCurrentOrder) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                        if (!pause) {
                            getOrderInfo(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    System.out.println(t.getMessage());
                    toast.show();
                }
            });
            return;
        }
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getOrderById(Integer.parseInt(orderId)).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order result = response.body();
                    if (result != null) {
                        setUpChrono(result);
                        setUpOrderInfo(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void setUpOrderInfo(Order order) {
        TextView lblLocation, lblVehicleType, lblVehicleNumber, lblCheckInDate;
        lblLocation = findViewById(R.id.lblOrderLocation);
        lblVehicleNumber = findViewById(R.id.lblUserVehicleNumber);
        lblVehicleType = findViewById(R.id.lblUserVehicleType);
        lblCheckInDate = findViewById(R.id.lblCheckin);

        String pattern = "HH:mm dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date(order.getCheckInDate()));

        lblCheckInDate.setText(date);
        lblLocation.setText(order.getLocation().getLocation());

        lblVehicleNumber.setText(order.getUser().getVehicle().getVehicleNumber());
        lblVehicleType.setText(order.getUser().getVehicle().getVehicleTypeId().getName());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOrderPricing);
        OrderPricingAdapter orderPricingAdapter = new OrderPricingAdapter(order.getOrderPricings());
        GridLayoutManager gLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderPricingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();
        pause = false;
    }

    public void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //get all networks information
        NetworkInfo networkInfo[] = cm.getAllNetworkInfo();
        int i;
        //checking internet connectivity
        for (i = 0; i < networkInfo.length; ++i) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                getOrderInfo(null);
                return;
            }
        }

        if (i == networkInfo.length) {
            Toast.makeText(getApplicationContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    public void sendTokenToServer(String token, String phoneNumber){
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.sendDeviceTokenToServer(token,phoneNumber).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
//                    Order result = response.body();
//                    if (result != null) {
//                        setUpChrono(result);
//                        setUpOrderInfo(result);
//                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
