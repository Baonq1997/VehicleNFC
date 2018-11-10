package com.example.demo.component.location;

import com.example.demo.component.policy.*;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.config.ResponseObject;

import com.example.demo.view.AddLocationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final PolicyRepository policyRepository;
    private final PricingRepository pricingRepository;
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;

    @Autowired
    private EntityManager entityManager;

    public LocationService(LocationRepository locationRepository, PolicyRepository policyRepository, PricingRepository pricingRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository) {
        this.locationRepository = locationRepository;
        this.policyRepository = policyRepository;
        this.pricingRepository = pricingRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
    }

    public Optional<Location> getMeterById(Integer id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            List<Policy> policyList = policyRepository.findAllByLocationId(location.get().getId());
            for (Policy policy : policyList) {
                List<PolicyHasTblVehicleType> policyHasTblVehicleTypes =
                        policyHasVehicleTypeRepository.findAllByPolicyId(policy.getId());
                policy.setPolicyHasTblVehicleTypes(policyHasTblVehicleTypes);
            }
            location.get().setPolicyList(policyList);
        }
        return location;
    }

    public ResponseObject getAllLocations() {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> criteriaQuery = builder.createQuery(Location.class);
        Root<Location> from = criteriaQuery.from(Location.class);
        CriteriaQuery<Location> select = criteriaQuery.select(from);
        TypedQuery<Location> typedQuery = entityManager.createQuery(select);
//        typedQuery.setFirstResult(pagNumber * pageSize);
//        typedQuery.setMaxResults(pageSize);
        List<Location> locationList = typedQuery.getResultList();
//        for (int i = 0; i <locationList.size()-1; i++) {
//            Location location = locationList.get(i);
//            List<Policy> policyList = location.getPolicyList();
//            for (Policy policy : policyList) {
//                List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeService.findByPolicyId(policy);
//                policy.setPolicyHasTblVehicleTypeList(policyHasTblVehicleTypes);
//            }
//            location.setPolicyList(policyList);
//            locationList.set(i, location);
//        }
        responseObject.setData(locationList);
        return responseObject;
    }

    @Transactional
    public void addPolicy(AddLocationObject addLocationObject) {

        Optional<Policy> policy = policyRepository.findById(addLocationObject.getPolicyId());
        List<Location> locationList = addLocationObject.getLocationArr();
        List<Location> existedLocations = addLocationObject.getCurrentLocationId();
        if (!locationList.isEmpty() && policy.isPresent()) {
            Policy policyDB = policy.get();
            for (Location location : locationList) {
                for (Location existedLocation:existedLocations) {
                    if (existedLocation.getId() != location.getId()) {
                        if (location.getIsDelete().equalsIgnoreCase("true")) {
                            policyRepository.deleteById(policyDB.getId());
                        } else {
                            Policy policyInstance = new Policy();
                            policyInstance.setAllowedParkingFrom(policyDB.getAllowedParkingFrom());
                            policyInstance.setAllowedParkingTo(policyDB.getAllowedParkingTo());
                            policyInstance.setLocationId(location.getId());
                            List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policyDB.getId());
                            policyRepository.save(policyInstance);
                            if (policyHasTblVehicleTypes != null) {
                                List<PolicyHasTblVehicleType> policyHasTblVehicleTypeList = policyHasTblVehicleTypes;
                                for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
                                    PolicyHasTblVehicleType duplicatePolicyVehicle = new PolicyHasTblVehicleType();

                                    duplicatePolicyVehicle.setPolicyId(policyInstance.getId());

                                    if (policyHasTblVehicleType.getMinHour() != null) {
                                        duplicatePolicyVehicle.setMinHour(policyHasTblVehicleType.getMinHour());
                                    }
                                    if (policyHasTblVehicleType.getVehicleTypeId() != null) {
                                        duplicatePolicyVehicle.setVehicleTypeId(policyHasTblVehicleType.getVehicleTypeId());
                                    }
                                    policyHasVehicleTypeRepository.save(duplicatePolicyVehicle);
                                    List<Pricing> pricingList = pricingRepository.findByPolicyHasVehicleTypeId(policyHasTblVehicleType.getId());
                                    if (pricingList != null) {
                                        for (Pricing pricing : pricingList) {
                                            Pricing duplicatePricing = new Pricing();
                                            duplicatePricing.setPolicyHasVehicleTypeId(duplicatePolicyVehicle.getId());
                                            duplicatePricing.setPricePerHour(pricing.getPricePerHour());
                                            duplicatePricing.setLateFeePerHour(pricing.getLateFeePerHour());
                                            duplicatePricing.setFromHour(pricing.getFromHour());
                                            pricingRepository.save(duplicatePricing);
                                        }
                                        duplicatePolicyVehicle.setPricingList(policyHasTblVehicleType.getPricingList());
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    public List<VehicleType> getLocationHasVehicleTypes(Integer locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        List<VehicleType> vehicleTypeList = new ArrayList<>();
        //Todo
//        if (location.isPresent()) {
//            List<Policy> policyList = location.get().getPolicyList();
//            if (policyList != null) {
//                for (Policy policy : policyList) {
//                    List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findByPolicyId(policy.getId());
//                    if (policyHasTblVehicleTypes != null) {
//                        for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
//                            VehicleType vehicleType = policyHasTblVehicleType.getVehicleTypeId();
//                            if (!vehicleTypeList.contains(vehicleType)) {
//                                vehicleTypeList.add(vehicleType);
//                            }
//                        }
//                    }
//
//                }
//            }
//
//        }
        return vehicleTypeList;
    }

    public List<Location> getLocationsByPolicyId(Integer policyInstanceId) {
//        Optional<Policy> policyOpt = policyRepository.findById(policyId);
        Optional<Policy> policyInstanceOptional = policyRepository.findById(policyInstanceId);
        if (policyInstanceOptional.isPresent()) {
            List<Policy> policyList = new ArrayList<>();
            policyList.add(policyInstanceOptional.get());
            return locationRepository.findByPolicyList(policyList);
        }
        return null;
    }
}
