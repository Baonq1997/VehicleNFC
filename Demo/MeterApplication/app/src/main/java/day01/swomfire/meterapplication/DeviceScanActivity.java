package day01.swomfire.meterapplication;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Util.RmaAPIUtils;
import adapter.VehicleTypeAdapter;
import model.Location;
import model.Order;
import model.SameVehiclePolicy;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceScanActivity extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    Tag myTag;
    Context context;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_scan);
        context = this;
        pd = new ProgressDialog(context);
        pd.setTitle("Đang xử lý...");
        pd.setMessage("Xin chờ.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        ImageView gifImageView = (ImageView) findViewById(R.id.gifHaha);
        Glide.with(this).load(R.raw.holding).into(gifImageView);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "Thiết bị không hỗ trợ NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        setUpMeterInfo();
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                buildTagViews(msgs);
            } else {
                Toast.makeText(context, "Thiết bị không hợp lệ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding

        try {
            // Get the Text
            text = new String(payload, 0, payload.length, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        } catch (StringIndexOutOfBoundsException e) {
            text = new String(payload);
        }
        try {
            JSONObject jsonObj = new JSONObject(text);
            String userId = (text.contains("userId")) ? jsonObj.getString("userId") : "";
            String token = (text.contains("token")) ? jsonObj.getString("token") : "";
            checkUserInfo(userId, token);

        } catch (JSONException e) {
            //TODO change
            Toast.makeText(context, "Thẻ hoặc thiết bị không hợp lệ", Toast.LENGTH_SHORT).show();
        }

    }

    TextView txtLocation, txtAllowedFrom, txtAllowedTo;
    ProgressDialog pd;

    public void setUpMeterInfo() {

        pd.show();
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getLocationById(1).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    Location result = response.body();
                    setUpLocationInfo(result);
                    //TODO
//                    checkUserInfo("36", null);
                    pd.cancel();
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.cancel();
            }
        });
    }

    private Location location;

    public void setUpLocationInfo(Location location) {
        this.location = location;
        txtLocation = findViewById(R.id.txtLocation);
        txtAllowedFrom = findViewById(R.id.txtAllowedFrom);
        txtAllowedTo = findViewById(R.id.txtAllowedTo);
//        List<SameVehiclePolicy> sameVehiclePolicies = SameVehiclePolicy.mergePolicyList(location.getPolicies());
        PolicyListFragment policyListFragment = new PolicyListFragment();
        policyListFragment.setPolicyList(location.getPolicies());
        policyListFragment.setContext(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, policyListFragment);
        transaction.commit();
    }

    public void checkUserInfo(final String userId, final String token) {
        pd.show();
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getUserById(Integer.parseInt(userId)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    pd.cancel();
                    User result = response.body();
                    if (result != null) {
                        checkOpenOrderInfo(result, token);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                pd.cancel();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkOpenOrderInfo(final User user, final String token) {
        pd.show();
        RmaAPIService mService = RmaAPIUtils.getAPIService();
        mService.getOpenOrderByUserId(Integer.parseInt(user.getId())).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order result = response.body();
                    pd.cancel();
                    if (result == null) {
                        Intent intent = new Intent(context, CheckInActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("location", new Gson().toJson(location));
                        intent.putExtra("user", new Gson().toJson(user));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, CheckOutActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("order", new Gson().toJson(result));
                        intent.putExtra("user", new Gson().toJson(user));
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
}
