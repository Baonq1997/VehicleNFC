package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import day01.swomfire.meterapplication.PricingPopupActivity;
import day01.swomfire.meterapplication.R;
import model.Policy;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.MyViewHolder> {
    private List<Policy> policies;
    private Context context;

    public PolicyAdapter(List<Policy> policies, Context context) {
        this.policies = policies;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_policy, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        final Policy policy = policies.get(position);
//        String pattern = "HH:mm";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//
//
//        holder.txtFrom.setText(simpleDateFormat.format(new Date(policy.getAllowedParkingFrom())));
//        holder.txtTo.setText(simpleDateFormat.format(new Date(policy.getAllowedParkingTo())));
//        holder.btnPricing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, PricingPopupActivity.class);
//                intent.putExtra("Pricing", (new Gson()).toJson(policy.getPricings()));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return policies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFrom, txtTo;
        private Button btnPricing;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtFrom = itemView.findViewById(R.id.txtFrom);
            txtTo = itemView.findViewById(R.id.txtTo);
            btnPricing = itemView.findViewById(R.id.btnPricing);
//            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }


}
