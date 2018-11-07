package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swomfire.vehicleNFCUser.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.HourHasPrice;
import model.OrderPricing;
import service.UserService;

public class HistoryPricingAdapter extends RecyclerView.Adapter<HistoryPricingAdapter.MyViewHolder> {
    private List<HourHasPrice> hourHasPrices;
    private long checkInDate, checkOutDate;
    private int layoutId;

    public HistoryPricingAdapter(List<HourHasPrice> hourHasPrices, long checkInDate, long checkOutDate, int layoutId) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.layoutId = layoutId;
        this.hourHasPrices = new ArrayList<>();

        for (HourHasPrice hourHasPrice : hourHasPrices) {
            if (this.hourHasPrices.size() < 1) {
                double money = (hourHasPrice.isLate()) ? hourHasPrice.getFine() : hourHasPrice.getPrice();
                if (hourHasPrice.isFullHour()) {
                    hourHasPrice.setTotal(money);
                } else {
                    hourHasPrice.setFullHour(false);
                    hourHasPrice.setTotal(Math.ceil((double) hourHasPrice.getMinutes() / 60 * money));
                }
                this.hourHasPrices.add(hourHasPrice);
            } else {
                boolean different = true;
                HourHasPrice tmp = this.hourHasPrices.get(this.hourHasPrices.size() - 1);
                if (tmp.isLate() == hourHasPrice.isLate()) {
                    boolean tmpBoolean = (tmp.isLate()) ? (Double.compare(tmp.getFine(), hourHasPrice.getFine()) == 0) : (Double.compare(tmp.getPrice(), hourHasPrice.getPrice()) == 0);
                    if (tmpBoolean) {
                        different = false;
                        double money = (hourHasPrice.isLate()) ? hourHasPrice.getFine() : hourHasPrice.getPrice();
                        if (hourHasPrice.isFullHour()) {
                            tmp.setHour(tmp.getHour() + 1);
                            tmp.setTotal(tmp.getTotal() + money);
                        } else {
                            tmp.setFullHour(false);
                            tmp.setMinutes(hourHasPrice.getMinutes());
                            tmp.setTotal(tmp.getTotal() + Math.ceil(((double) hourHasPrice.getMinutes() / 60) * money));
                        }
                    }
                }
                if (different) {
                    double money = (hourHasPrice.isLate()) ? hourHasPrice.getFine() : hourHasPrice.getPrice();
                    if (hourHasPrice.isFullHour()) {
                        hourHasPrice.setTotal(money);
                    } else {
                        hourHasPrice.setFullHour(false);
                        hourHasPrice.setTotal(Math.ceil((double) hourHasPrice.getMinutes() / 60 * money));
                    }
                    this.hourHasPrices.add(hourHasPrice);
                }
            }
        }
        this.hourHasPrices = HourHasPrice.sortList(this.hourHasPrices);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MyViewHolder(itemView);
    }

    private int passHour = 0;
    private int isLate = 0;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HourHasPrice hourHasPrice = hourHasPrices.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        long mili = UserService.convertToMilliseconds(hourHasPrice.getHour() - passHour, true);

        if (position == hourHasPrices.size() - 1 && !hourHasPrice.isFullHour()) {
            mili += UserService.convertToMilliseconds(hourHasPrice.getMinutes(), false);
        }

        String toHour = "";
        if (!UserService.compareTwoDate(checkInDate, checkInDate + mili)) {
            SimpleDateFormat extra = new SimpleDateFormat("HH:mm dd/MM");
            toHour = extra.format(checkInDate + mili);
        } else {
            toHour = sdf.format(checkInDate + mili);
        }

        holder.txtFrom.setText(sdf.format(checkInDate));
        holder.txtTo.setText(toHour + ": ");

        if (!hourHasPrice.isLate()) {
            if (position != hourHasPrices.size() - 1) {
                holder.txtTotal.setText(UserService.convertMoneyNoVND(hourHasPrice.getPrice()) + " VNĐ x " + (hourHasPrice.getHour() - passHour) + " giờ");
                holder.txtEnd.setText(UserService.convertMoney(hourHasPrice.getTotal()));
            } else {
                int min = hourHasPrice.getMinutes();
                holder.txtTotal.setText(UserService.convertMoneyNoVND(hourHasPrice.getPrice()) + " VNĐ x " + (hourHasPrice.getHour() - passHour + " giờ " + min + " p"));
                holder.txtEnd.setText(UserService.convertMoney(hourHasPrice.getTotal()));
            }
        } else {
            isLate++;
            if (position != hourHasPrices.size() - 1) {
                holder.txtTotal.setText(UserService.convertMoneyNoVND(hourHasPrice.getFine()) + " VNĐ x " + (hourHasPrice.getHour() - passHour) + " giờ");
                holder.txtEnd.setText(UserService.convertMoney(hourHasPrice.getTotal()));
            } else {
                int min = hourHasPrice.getMinutes();
                holder.txtTotal.setText(UserService.convertMoneyNoVND(hourHasPrice.getFine()) + " VNĐ x " + (hourHasPrice.getHour() - passHour + " giờ " + min + " p"));
                holder.txtEnd.setText(UserService.convertMoney(hourHasPrice.getTotal()));
            }
        }
        if (isLate == 1) {
            holder.txtIsLate.setVisibility(View.VISIBLE);
        }
        checkInDate += mili;
        passHour = hourHasPrice.getHour();
    }

    @Override
    public int getItemCount() {
        return hourHasPrices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFrom, txtTo, txtTotal, txtEnd, txtThongBao, txtIsLate;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtFrom = itemView.findViewById(R.id.txtFrom);
            txtTo = itemView.findViewById(R.id.txtTo);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtEnd = itemView.findViewById(R.id.txtEnd);
            txtThongBao = itemView.findViewById(R.id.txtThongBao);
            txtIsLate = itemView.findViewById(R.id.isLate);
        }
    }
}
