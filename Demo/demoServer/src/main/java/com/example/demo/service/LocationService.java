package com.example.demo.service;

import com.example.demo.Config.ResponseObject;
import com.example.demo.entity.*;

import com.example.demo.repository.*;
import com.example.demo.view.AddLocationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final PolicyRepository policyRepository;
    private final PricingRepository pricingRepository;
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;
    private final PolicyInstanceRepository policyInstanceRepository;

    @Autowired
    private EntityManager entityManager;

    public LocationService(LocationRepository locationRepository, PolicyRepository policyRepository, PricingRepository pricingRepository, PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository, PolicyInstanceRepository policyInstanceRepository) {
        this.locationRepository = locationRepository;
        this.policyRepository = policyRepository;
        this.pricingRepository = pricingRepository;
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
        this.policyInstanceRepository = policyInstanceRepository;
    }

    public Optional<Location> getMeterById(Integer id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            List<PolicyInstance> policyInstances = policyInstanceRepository.findAllByLocationId(location.get().getId());
            for (PolicyInstance policy : policyInstances) {
                List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes =
                        policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policy.getId());
                policy.setPolicyInstanceHasTblVehicleTypes(policyInstanceHasTblVehicleTypes);
            }
            location.get().setPolicyInstanceList(policyInstances);
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

//        Optional<Policy> policy = policyRepository.findById(addLocationObject.getPolicyId());
//        List<Location> locationList = addLocationObject.getLocationArr();
//        if (!locationList.isEmpty() && policy.isPresent()) {
//            Policy policyDB = policy.get();
//            for (Location location : locationList) {
//                if (location.getIsDelete().equalsIgnoreCase("true")) {
//                    locationRepository.deleteLocationPolicyByPolicyIdAndLocationId(policyDB.getId(), location.getId());
//                } else {
//                    List<Policy> policyList = new ArrayList<>();
//                    policyList.add(policyDB);
//                    Location temp = locationRepository.findByIdAndPolicyList(location.getId(), policyList);
//                    if (temp == null) {
//                        locationRepository.insertLocationAndPolicy(location.getId(), policyDB.getId());
//                    }
//                }
//            }
//        }

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

    public List<Location> getLocationsByPolicyId(Integer policyId) {
//        Optional<Policy> policyOpt = policyRepository.findById(policyId);
//        if (policyOpt.isPresent()) {
//            List<Policy> policyList = new ArrayList<>();
//            policyList.add(policyOpt.get());
//            return locationRepository.findByPolicyList(policyList);
//        }
        return null;
    }
}
