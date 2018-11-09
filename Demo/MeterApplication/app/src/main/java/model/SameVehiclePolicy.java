package model;

import java.util.ArrayList;
import java.util.List;

public class SameVehiclePolicy {
    List<Policy> policies;
    VehicleType vehicleType;

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public static List<SameVehiclePolicy> mergePolicyList(List<Policy> policies) {
//        if (policies != null) {
//            List<SameVehiclePolicy> sameVehiclePolicies = new ArrayList<>();
//            for (Policy policy : policies) {
//                if (sameVehiclePolicies.size() > 0) {
//                    boolean found = false;
//                    for (SameVehiclePolicy sameVehiclePolicy : sameVehiclePolicies) {
//                        if (sameVehiclePolicy.getVehicleType().getId() == policy.getVehicleType().getId()) {
//                            sameVehiclePolicy.getPolicies().add(policy);
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        SameVehiclePolicy sameVehiclePolicy = new SameVehiclePolicy();
//                        sameVehiclePolicy.setVehicleType(policy.getVehicleType());
//                        List<Policy> policyList = new ArrayList<>();
//                        policyList.add(policy);
//                        sameVehiclePolicy.setPolicies(policyList);
//                        sameVehiclePolicies.add(sameVehiclePolicy);
//                    }
//                } else {
//                    SameVehiclePolicy sameVehiclePolicy = new SameVehiclePolicy();
//                    sameVehiclePolicy.setVehicleType(policy.getVehicleType());
//                    List<Policy> policyList = new ArrayList<>();
//                    policyList.add(policy);
//                    sameVehiclePolicy.setPolicies(policyList);
//                    sameVehiclePolicies.add(sameVehiclePolicy);
//                }
//            }
//            return sameVehiclePolicies;
//        }
        return null;
    }
}
