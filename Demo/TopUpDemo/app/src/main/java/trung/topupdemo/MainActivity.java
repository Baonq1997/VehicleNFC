package trung.topupdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;

public class MainActivity extends Activity {

    Button btnRent;
    EditText edtBalance;
    EditText edtAmount;
    ProductObj item;
    int Balance = 0;
    int num;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("ARZV5aC-CZnIsiEp4S8PVWcSDY0TbEQx0tCNZs_NHBuYdsFsctaPnvhNLtdAYQhw8Dzn6p7S0SUNpG9i");

    public void onBuyPressed(ProductObj obj){

        PayPalPayment payment = new PayPalPayment(obj.getPrice(),obj.getCurrency(), obj.getName(), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null){
                try{

                    Log.i("Payment Example", confirm.toJSONObject().toString(4));
                    Balance += num;
                    edtBalance.setText(""+Balance + " USD");
                    Toast.makeText(getApplicationContext(), "Succesful!", Toast.LENGTH_SHORT).show();

                } catch (JSONException e){
                    Log.e("Payment Example", "Error:" ,e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample","Canceled");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample","Valid payment");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRent = (Button)findViewById(R.id.btnPay);
        edtBalance = (EditText) findViewById(R.id.edtBalance);
        edtAmount = (EditText) findViewById(R.id.edtAmount);

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    num = Integer.valueOf(edtAmount.getText().toString());
                    if (num > 0) {
                        item = new ProductObj("Nap ", num, "USD");
                        onBuyPressed(item);
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid number!",Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Invalid number!",Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
