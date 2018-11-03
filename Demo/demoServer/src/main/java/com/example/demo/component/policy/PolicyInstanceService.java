package com.example.demo.component.policy;

import com.example.demo.component.location.Location;
import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.policy.PolicyInstanceRepository;
import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PolicyInstanceService {
    private PolicyInstanceRepository policyInstanceRepository;
    private LocationRepository locationRepository;
    private PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;
    private PricingRepository pricingRepository;

    public PolicyInstanceService(PolicyInstanceRepository policyInstanceRepository, PricingRepository pricingRepository, PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository, LocationRepository locationRepository) {
        this.policyInstanceRepository = policyInstanceRepository;
        this.locationRepository = locationRepository;
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
        this.pricingRepository = pricingRepository;
    }

    public PolicyInstance getById(Integer id) {
        return policyInstanceRepository.findById(id).get();
    }
    @Transactional
    public PolicyInstance savePolicy(PolicyInstance policyInstance, List<VehicleType> vehicleTypeList, Integer locationId) {

        PolicyInstance policyInstanceDB = new PolicyInstance();
        if (policyInstance.getId() == 0) {
//            // create

            Location location = locationRepository.findById(locationId).get();
            policyInstance.setLocationId(locationId);
            policyInstanceDB = policyInstanceRepository.save(policyInstance);
            List<PolicyInstance> policyInstances = location.getPolicyInstanceList();
            policyInstances.add(policyInstanceDB);
            location.setPolicyInstanceList(policyInstances);
            locationRepository.save(location);
//
        } else {
            policyInstanceDB = policyInstanceRepository.findById(policyInstance.getId()).get();
            if (policyInstanceDB != null) {
                policyInstanceDB.setAllowedParkingFrom(policyInstance.getAllowedParkingFrom());
                policyInstanceDB.setAllowedParkingTo(policyInstance.getAllowedParkingTo());
                policyInstanceRepository.save(policyInstanceDB);
            }
        }
        List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes = policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policyInstanceDB.getId());
        for (int i = 0; i < vehicleTypeList.size(); i++) {
            if (policyInstanceHasTblVehicleTypes.size() == 0) {
                PolicyInstanceHasTblVehicleType policyInstanceHasTblVehicleType = new PolicyInstanceHasTblVehicleType();
                policyInstanceHasTblVehicleType.setPolicyInstanceId(policyInstanceDB.getId());
                policyInstanceHasTblVehicleType.setVehicleTypeId(vehicleTypeList.get(i));
                policyInstanceHasVehicleTypeRepository.saveAndFlush(policyInstanceHasTblVehicleType);
            } else {
                if (vehicleTypeList.get(i).getIsDelete().equalsIgnoreCase("true")) {
                    PolicyInstanceHasTblVehicleType instance = policyInstanceHasVehicleTypeRepository.findByPolicyInstanceIdAndVehicleTypeId(policyInstanceDB.getId(), vehicleTypeList.get(i)).get();
                    List<Pricing> pricings = instance.getPricingList();
                    if (!pricings.isEmpty()) {
                        for (Pricing pricing: pricings) {
                            pricingRepository.delete(pricing);
                        }
                    }
                    policyInstanceHasVehicleTypeRepository.delete(instance);
//                    break;
                } else {
                    Optional<PolicyInstanceHasTblVehicleType> instance = policyInstanceHasVehicleTypeRepository.findByPolicyInstanceIdAndVehicleTypeId(policyInstanceDB.getId(), vehicleTypeList.get(i));
                    if (!instance.isPresent()) {
                        PolicyInstanceHasTblVehicleType dto =  new PolicyInstanceHasTblVehicleType();
                        dto.setPolicyInstanceId(policyInstanceDB.getId());
                        dto.setVehicleTypeId(vehicleTypeList.get(i));
                        policyInstanceHasVehicleTypeRepository.saveAndFlush(dto);
                    }

                }
            }
        }
        return policyInstanceDB;
    }
}
