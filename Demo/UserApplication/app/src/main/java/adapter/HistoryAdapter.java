package adapter;

import android.app.Service;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swomfire.vehicleNFCUser.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import service.UserService;
import sqliteModel.History;


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

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat full = new SimpleDateFormat("dd-MM-yyyy");

        History history = hislist.get(position);

        holder.txtDate.setText(full.format(new Date(history.getCheck_in_date())));

        holder.txtDatetime.setText("Từ   : " + sdf.format(new Date(history.getCheck_in_date())));

        holder.txtToDate.setText("Đến : " + sdf.format(new Date(history.getCheck_out_date())));

        holder.txtAddress.setText("Địa điểm : " + history.getTbl_location_id());

        holder.txtPrice.setText("Chi phí : " +  UserService.convertMoney((double)history.getTotal()));

        holder.txtPos.setText("" + position);

    }

    @Override
    public int getItemCount() {
        return hislist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDatetime, txtAddress, txtPrice, txtToDate, txtPos, txtDate;
        private LinearLayout layoutMain;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtDatetime = itemView.findViewById(R.id.txtDatetime);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtToDate = itemView.findViewById(R.id.txtToDate);
            txtDate = itemView.findViewById(R.id.txtDate);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            txtPos = itemView.findViewById(R.id.txtPos);

        }
    }

}