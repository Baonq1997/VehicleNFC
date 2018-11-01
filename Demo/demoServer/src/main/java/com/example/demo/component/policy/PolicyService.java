package com.example.demo.component.policy;

import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.policy.Policy;
import com.example.demo.component.policy.PolicyInstanceRepository;
import com.example.demo.component.policy.PolicyRepository;
import com.example.demo.component.policy.PricingRepository;
import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
//    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;
    private final LocationRepository locationRepository;
    private final PricingRepository pricingRepository;
    private final PolicyInstanceRepository policyInstanceRepository;

    public PolicyService(PolicyRepository policyRepository, LocationRepository locationRepository, PricingRepository pricingRepository, PolicyInstanceRepository policyInstanceRepository) {
        this.policyRepository = policyRepository;
        this.locationRepository = locationRepository;
        this.pricingRepository = pricingRepository;
        this.policyInstanceRepository = policyInstanceRepository;
    }

    public Optional<Policy> getPolicyById(Integer policyId) {
        return policyRepository.findById(policyId);
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    //Todo
    @Transactional
    public Policy savePolicy(Policy policy, List<VehicleType> vehicleTypeList, Integer locationId) {

//        Policy policyDB = new Policy();
//        if (policy.getId() == 0) {
//            // create
//            policyDB = policyRepository.save(policy);
//            Location location = locationRepository.findById(locationId).get();
//            List<Policy> policyList = location.getPolicyList();
//            policyList.add(policyDB);
//            location.setPolicyList(policyList);
//            locationRepository.save(location);
//
//        } else {
//            policyDB = policyRepository.findById(policy.getId()).get();
//            if (policyDB != null) {
//                policyDB.setAllowedParkingFrom(policy.getAllowedParkingFrom());
//                policyDB.setAllowedParkingTo(policy.getAllowedParkingTo());
//                policyRepository.save(policyDB);
//            }
//        }
//        List<PolicyHasTblVehicleType> policyHasTblVehicleTypeList = new ArrayList<>();
//        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findByPolicyId(policyDB.getId());
//        for (int i = 0; i < vehicleTypeList.size(); i++) {
//            if (policyHasTblVehicleTypes.size() == 0) {
//                PolicyHasTblVehicleType policyHasTblVehicleType1 = new PolicyHasTblVehicleType();
//                policyHasTblVehicleType1.setPolicyId(policyDB.getId());
//                policyHasTblVehicleType1.setVehicleTypeId(vehicleTypeList.get(i));
//                policyHasVehicleTypeRepository.saveAndFlush(policyHasTblVehicleType1);
//            } else {
//                if (vehicleTypeList.get(i).getIsDelete().equalsIgnoreCase("true")) {
//                    PolicyHasTblVehicleType instance = policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policyDB.getId(), vehicleTypeList.get(i)).get();
//                    List<Pricing> pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(instance.getId());
//                    if (!pricing.isEmpty()) {
//                        pricingRepository.deleteByPolicyHasTblVehicleTypeId(instance.getId());
//                    }
//                    policyHasVehicleTypeRepository.deletePolicyHasVehicleById(instance.getId());
////                    break;
//                } else {
//                    Optional<PolicyHasTblVehicleType> policyHasTblVehicleType1 = policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policyDB.getId(), vehicleTypeList.get(i));
//                    if (!policyHasTblVehicleType1.isPresent()) {
//                        PolicyHasTblVehicleType dto =  new PolicyHasTblVehicleType();
//                        dto.setPolicyId(policyDB.getId());
//                        dto.setVehicleTypeId(vehicleTypeList.get(i));
//                        policyHasVehicleTypeRepository.saveAndFlush(dto);
//                    }
//
//                }
//            }
//        }
//        return policyDB;
        return null;
    }

    //Todo
    @Transactional
    public void deletePolicy(Integer locationId, Policy policy, List<Integer> policyHasVehicleTypeId) {
//        if (!policyHasVehicleTypeId.isEmpty()) {
//            pricingRepository.deleteByPolicyHasTblVehicleTypeIdIn(policyHasVehicleTypeId);
//        }
//
//        for (int i = 0; i < policyHasVehicleTypeId.size(); i++) {
//            Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId.get(i));
//            if (policyHasTblVehicleType.isPresent()) {
//                policyHasVehicleTypeRepository.delete(policyHasTblVehicleType.get());
//            }
//
//        }
//        if (locationId != null) {
//            Location location = locationRepository.findById(locationId).get();
//            locationRepository.deleteLocationPolicyByPolicyId(policy.getId());
//        }
//
//        policyRepository.delete(policy);
    }

    //Todo
    @Transactional
    public void deleteByIdAndLocationId (Integer locationId, Integer policyId) {
//        Optional<Location> locationOpt = locationRepository.findById(locationId);
//        Optional<Policy> policyOpt = policyRepository.findById(policyId);
//        if (locationOpt.isPresent() && policyOpt.isPresent()) {
//            Location location = locationOpt.get();
//            Policy policy = policyOpt.get();
//            List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findByPolicyId(policy.getId());
//            for (PolicyHasTblVehicleType policyHasTblVehicleType: policyHasTblVehicleTypes) {
//                pricingRepository.deleteByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
//                policyHasVehicleTypeRepository.deleteById(policyHasTblVehicleType.getId());
//            }
//            locationRepository.deleteLocationPolicyByPolicyId(policy.getId());
//            policyRepository.delete(policy);
//        }
    }

    public List<Policy> getPolicies() {
        List<Policy> policyList = policyRepository.findAll();
    return policyList;
    }
}
