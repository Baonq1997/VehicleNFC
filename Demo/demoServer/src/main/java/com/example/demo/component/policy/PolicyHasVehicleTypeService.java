package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyRepository;
import com.example.demo.component.policy.PricingRepository;
import com.example.demo.component.vehicleType.VehicleTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PolicyHasVehicleTypeService {
    private final PolicyRepository policyRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final PricingRepository pricingRepository;

    public PolicyHasVehicleTypeService(PolicyRepository policyRepository, VehicleTypeRepository vehicleTypeRepository, PricingRepository pricingRepository) {
        this.policyRepository = policyRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.pricingRepository = pricingRepository;
    }

    //Todo
//    public List<PolicyHasTblVehicleType> findByPolicyId(Integer policyId) {
//        Policy policy = policyRepository.findById(policyId).get();
//        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findByPolicyId(policy.getId());
////        for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
////            policyHasTblVehicleType.setPricings(pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleType));
////        }
//        for (int i = 0; i < policyHasTblVehicleTypes.size(); i++) {
//            PolicyHasTblVehicleType policyHasTblVehicleType = policyHasTblVehicleTypes.get(i);
//            policyHasTblVehicleType.setPricings(pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId()));
//            policyHasTblVehicleTypes.set(i,policyHasTblVehicleType);
//        }
//        return policyHasTblVehicleTypes;
//    }

//    public Optional<PolicyHasTblVehicleType> findByPolicyAndVehicleType(Integer policyId, Integer vehicleTypeId) {
//        Policy policy = policyRepository.findById(policyId).get();
//        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId).get();
//
//        PolicyHasTblVehicleType policyHasTblVehicleType=  policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policy.getId(), vehicleType).get();
//        List<Pricing> pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
//        policyHasTblVehicleType.setPricings(pricing);
//        return Optional.of(policyHasTblVehicleType);
//    }
        //Todo
//    public List<PolicyHasTblVehicleType> findByPolicyList(List<Policy> policyList) {
//        return policyHasVehicleTypeRepository.findByPolicyIdIn(policyList);
//    }

    //Todo
//    public void save(PolicyHasTblVehicleType policyHasTblVehicleType) {
//        Policy policy = policyRepository.getOne(policyHasTblVehicleType.getPolicyId());
//        VehicleType vehicleType = policyHasTblVehicleType.getVehicleTypeId();
//        policyRepository.save(policy);
//        vehicleTypeRepository.save(vehicleType);
//        policyHasVehicleTypeRepository.save(policyHasTblVehicleType);
//    }

    //Todo
//    @Transactional
//    public void delete(PolicyHasTblVehicleType policyHasTblVehicleType) {
//        Policy policy = policyRepository.getOne(policyHasTblVehicleType.getPolicyId());
//        VehicleType vehicleType = policyHasTblVehicleType.getVehicleTypeId();
//        pricingRepository.deleteByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
//        policyHasVehicleTypeRepository.deleteById(policyHasTblVehicleType.getId());
////        policyRepository.delete(policy);
////        vehicleTypeRepository.delete(vehicleType);
//    }

    //Todo
//    public List<PolicyHasTblVehicleType> findListByPolicyId(Integer policyId) {
//        Policy policy = policyRepository.findById(policyId).get();
//        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findByPolicyId(policyId);
//        List<PolicyHasTblVehicleType> policyHasTblVehicleTypeList = new ArrayList<>();
//        for (PolicyHasTblVehicleType policyHasTblVehicleType: policyHasTblVehicleTypes) {
//            List<Pricing> pricings = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
//            policyHasTblVehicleType.setPricings(pricings);
//            policyHasTblVehicleTypeList.add(policyHasTblVehicleType);
//        }
//        return policyHasTblVehicleTypeList;
//    }
}
