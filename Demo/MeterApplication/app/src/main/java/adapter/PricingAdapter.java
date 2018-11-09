package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import day01.swomfire.meterapplication.R;
import model.OrderPricing;
import model.Pricing;
import service.UserService;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.MyViewHolder> {
    private List<Pricing> pricings;
    private List<OrderPricing> orderPricings;

    public PricingAdapter(List<Pricing> pricings) {
        this.pricings = pricings;
    }

    public PricingAdapter(List<OrderPricing> orderPricings, int tmp) {
        this.pricings = OrderPricing.convert(orderPricings);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pricing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (pricings != null) {
            final Pricing pricing = pricings.get(position);

            String fromHousr = (pricing.getFromHour() == 0) ? "Giờ đầu" : "Giờ thứ " + pricing.getFromHour();
            holder.txtFrom.setText(fromHousr);
            holder.txtPrice.setText(UserService.convertMoney(pricing.getPricePerHour()));
            holder.txtFee.setText(UserService.convertMoney(pricing.getLateFeePerHour()));
        }
    }

    @Override
    public int getItemCount() {
        if (pricings != null) {
            return pricings.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFrom, txtPrice, txtFee;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtFrom = itemView.findViewById(R.id.txtFrom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtFee = itemView.findViewById(R.id.txtFee);
        }
    }


}
