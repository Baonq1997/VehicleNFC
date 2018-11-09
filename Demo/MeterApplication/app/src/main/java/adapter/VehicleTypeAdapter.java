package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.List;

import day01.swomfire.meterapplication.PricingPopupActivity;
import day01.swomfire.meterapplication.R;
import model.Policy;
import model.PolicyHasVehicleType;
import model.VehicleType;

public class VehicleTypeAdapter extends RecyclerView.Adapter<VehicleTypeAdapter.MyViewHolder> {
    private List<PolicyHasVehicleType> vehicleTypes;
    private Context context;

    public VehicleTypeAdapter(List<PolicyHasVehicleType> vehicleTypes, Context context) {
        this.vehicleTypes = vehicleTypes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vehicle_type, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PolicyHasVehicleType policyHasTblVehicleType = vehicleTypes.get(position);
        holder.txtVehicleType.setText(policyHasTblVehicleType.getVehicleType().getName());
        holder.btnPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PricingPopupActivity.class);
                intent.putExtra("Pricing", (new Gson()).toJson(policyHasTblVehicleType.getPricings()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleTypes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtVehicleType;
        private Button btnPricing;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtVehicleType = itemView.findViewById(R.id.txtVehicleType);
            btnPricing = itemView.findViewById(R.id.btnPricing);
//            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }


}
