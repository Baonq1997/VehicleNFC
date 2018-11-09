package day01.swomfire.meterapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import adapter.PolicyAdapter;
import adapter.VehicleTypeAdapter;
import model.Policy;

public class PolicyListFragment extends Fragment implements View.OnClickListener {
    Button btnNext, btnPre;
    TextView txtFrom,txtTo;
    List<Policy> policyList;
    int position;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_policy_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtFrom = view.findViewById(R.id.txtFrom);
        txtTo = view.findViewById(R.id.txtTo);
        btnNext = view.findViewById(R.id.btnNext);
        btnPre = view.findViewById(R.id.btnPre);
        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        if (policyList != null) {
            setUpViewForPolicy(policyList, 0, view);
        }
    }

    public void setPolicyList(List<Policy> policyList) {
        this.policyList = policyList;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == v.getRootView().findViewById(R.id.btnNext).getId()) {
            setUpViewForPolicy(policyList, ++position, v.getRootView());
        } else if (v.getId() == v.getRootView().findViewById(R.id.btnPre).getId()) {
            setUpViewForPolicy(policyList, --position, v.getRootView());
        }

    }

    public void setUpViewForPolicy(List<Policy> policyList, int position, View view) {
        if (position == 0) {
            btnPre.setTextColor(getResources().getColor(R.color.colorTransparent));
            btnPre.setEnabled(false);
        } else {
            btnPre.setEnabled(true);
            btnPre.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (position == policyList.size() - 1) {
            btnNext.setTextColor(getResources().getColor(R.color.colorTransparent));
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
            btnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        txtFrom.setText(simpleDateFormat.format(new Date(policyList.get(position).getAllowedParkingFrom())));
        txtTo.setText(simpleDateFormat.format(new Date(policyList.get(position).getAllowedParkingTo())));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listPricing);
//        PolicyAdapter pricingAdapter = new PolicyAdapter(policyList.get(position).getPolicies(), context);
        VehicleTypeAdapter vehicleTypeAdapter = new VehicleTypeAdapter(policyList.get(position).getPolicyHasVehicleTypes(),context);
        GridLayoutManager gLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vehicleTypeAdapter);
    }
}

