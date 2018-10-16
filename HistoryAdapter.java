package adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swomfire.vehicleNFCUser.HistoryDetail;
import com.swomfire.vehicleNFCUser.LoginActivity;
import com.swomfire.vehicleNFCUser.R;
import com.swomfire.vehicleNFCUser.WelcomeActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import Sqlite.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<History> hislist;

    public HistoryAdapter(List<History> his) {
        this.hislist = his;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_tb_row, parent, false);

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm DD/MM/YYYY ", Locale.US);

        History history = hislist.get(position);

        holder.txtDatetime.setText("Từ   : " + sdf.format(history.getCheck_in_date()));

        holder.txtToDate.setText("Đến : " + sdf.format(history.getCheck_out_date()));

        holder.txtAddress.setText("Thời gian đỗ : " + history.getTbl_location_id());

        holder.txtPrice.setText("Chi phí : " + history.getTotal() + " vnd");

        holder.txtPos.setText(""+position);

    }

    @Override
    public int getItemCount() {
        return hislist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDatetime, txtAddress, txtPrice, txtToDate, txtPos;
        private LinearLayout layoutMain;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtDatetime = itemView.findViewById(R.id.txtDatetime);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtToDate = itemView.findViewById(R.id.txtToDate);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            txtPos = itemView.findViewById(R.id.txtPos);

        }
    }

}
