package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;
    private final PolicyRepository policyRepository;

    public PricingService(PricingRepository pricingRepository, PolicyRepository policyRepository) {
        this.pricingRepository = pricingRepository;
        this.policyRepository = policyRepository;
    }

    public List<Pricing> findByPolicyHasVehicleTypeId(Integer policyHasVehicleTypeId) {
        Optional<Policy> policy =
                policyRepository.findById(policyHasVehicleTypeId);
        if (policy.isPresent()) {
            Policy policyDB = policy.get();
            //TODO
//            Pricing pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
            List<Pricing> pricing = policyDB.getPricings();
            return pricing;
        }
        return null;
    }


    public Pricing  save(Pricing pricing, Integer policyId) {
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
              pricing.setPolicyId(policyId);
            return pricingRepository.save(pricing);
        }
//        return pricingRepository.save(pricing);

    public Pricing save(Pricing pricing) {
        return pricingRepository.save(pricing);
    }

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
