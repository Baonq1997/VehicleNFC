package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;

    public PricingService(PricingRepository pricingRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository) {
        this.pricingRepository = pricingRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
    }

    public Pricing findByPolicyHasVehicleTypeId(Integer policyHasVehicleTypeId) {
        Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
            //TODO
//            Pricing pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
            Pricing pricing = null;
            return pricing;
        }
        return null;
    }

    //Todo
    public List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId) {
        Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasTblVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
//            List<Pricing> pricings = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
//            return pricings;
            return null;
        }
        return null;
    }

    public Pricing  save(Pricing pricing, Integer policyInstanceHasTblVehicleType) {
//        PolicyInstanceHasTblVehicleType instance = policyInstanceHasVehicleTypeRepository.findById(policyInstanceHasTblVehicleType).get();
//        boolean existed = false;
//        if (instance != null) {
//            List<Pricing> pricingList = pricingRepository.findPricingByPolicyInstanceVehicle(instance.getId());
//            for (Pricing item : pricingList) {
//                if (item.getId() == pricing.getId()) {
//                    existed = true;
//                    item.setFromHour(pricing.getFromHour());
//                    item.setLateFeePerHour(pricing.getLateFeePerHour());
//                    item.setPricePerHour(pricing.getPricePerHour());
//                }
//            }
//            if (!existed) {
//                pricingList.add(pricing);
//                instance.setPricingList(pricingList);
//            }
//            policyInstanceHasVehicleTypeRepository.save(instance);
//              pricing.setId(policyInstanceHasTblVehicleType);
            return pricingRepository.save(pricing);
        }
//        return pricingRepository.save(pricing);


    @Transactional
    public void deletePricing(Integer id) {
        Optional<Pricing> pricing = pricingRepository.findById(id);
        if (pricing.isPresent()) {
            pricingRepository.deletePricingById(pricing.get().getId());
        }

    }


    public void deleteByPolicyHasTblVehicleTypeId(Integer policyHasVehicleTypeId) {
//        pricingRepository.deleteByPolicyHasTblVehicleTypeId(policyHasVehicleTypeId);
    }

    public Pricing findById(Integer pricingId) {
        return pricingRepository.findById(pricingId).get();
    }
}
