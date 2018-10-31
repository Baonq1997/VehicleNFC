package sqliteModel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.swomfire.vehicleNFCUser.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HistoryDetail extends Activity {

    TextView txtName, txtVehicalName, txtVehicalID, txtLocation, txtFrom, txtTo, txtDuration, txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        History item = (History)getIntent().getExtras().get("hisItem");

        txtName = findViewById(R.id.txtName);
        txtVehicalName = findViewById(R.id.txtVehicalName);
        txtVehicalID = findViewById(R.id.txtVehicalID);
        txtLocation = findViewById(R.id.txtLocation);
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);
        txtDuration = findViewById(R.id.txtDuration);
        txtTotal = findViewById(R.id.txtTotal);

        txtName.setText(item.getUsername());
        txtVehicalName.setText(item.getVehical_name());
        txtVehicalID.setText(item.getVehical_id());
        txtLocation.setText(item.getTbl_location_id());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm DD/MM/YYYY ");
        txtFrom.setText(sdf.format(item.getCheck_in_date()));
        txtTo.setText(sdf.format(item.getCheck_in_date()));
        txtDuration.setText(item.getDuration() + " Phút");
        txtTotal.setText(item.getTotal() + " VND");

    }
}
