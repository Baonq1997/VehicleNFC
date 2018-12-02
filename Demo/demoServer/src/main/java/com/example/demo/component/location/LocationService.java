package com.example.demo.component.location;

import com.example.demo.component.policy.*;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;

import com.example.demo.config.SearchCriteria;
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
                for(PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes){
                    policyHasTblVehicleType.setPricingList(pricingRepository.findByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId()));
                }
                policy.setPolicyHasTblVehicleTypes(policyHasTblVehicleTypes);
            }
            location.get().setPolicyList(policyList);
        }
        return location;
    }

    public boolean checkExistedLocation(String location) {
        if (locationRepository.findByLocation(location) != null) {
            return true;
        }
        return false;
    }

    public Location saveLocation(Location location) {
        if (location.getId() == null) {
            // create
            locationRepository.save(location);
            return location;
        } else {
            Optional<Location> locationOpt = locationRepository.findById(location.getId());
            if (locationOpt.isPresent()) {
                Location locationDB = locationOpt.get();
                locationDB.setLocation(location.getLocation());
                locationDB.setActivated(location.getActivated());
                locationDB.setDescription(location.getDescription());
                locationRepository.save(locationDB);
                return locationDB;
            }
        }
        return null;
    }

    @Transactional
    public void deleteLocation(Integer locationId) {
        locationRepository.deleteById(locationId);
    }

    public ResponseObject getAllLocations() {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> criteriaQuery = builder.createQuery(Location.class);
        Root<Location> from = criteriaQuery.from(Location.class);
        CriteriaQuery<Location> select = criteriaQuery.select(from);
        TypedQuery<Location> typedQuery = entityManager.createQuery(select);
        List<Location> locationList = typedQuery.getResultList();

        responseObject.setData(locationList);
        return responseObject;
    }

    public ResponseObject filterLocation(List<SearchCriteria> params
            , int pageNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = builder.createQuery(Location.class);
        Root r = query.from(Location.class);

        Predicate predicate = builder.conjunction();
        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                               param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                Object type = r.get(param.getKey()).getJavaType();
                if (type == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        query.where(predicate);
        TypedQuery<Location> typedQuery = entityManager.createQuery(query);
        List<Location> locations = typedQuery.getResultList();
        int totalPages = locations.size() / pageSize;
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Location> resultList = typedQuery.getResultList();
        responseObject.setData(resultList);
        responseObject.setTotalPages(totalPages + 1);
        responseObject.setPageNumber(pageNumber);
        responseObject.setPageSize(PaginationEnum.locationPageSize.getNumberOfRows());
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
//                for (Location existedLocation : existedLocations) {
//                    if (existedLocation.getId() != location.getId()) {
//                        if (location.getIsDelete().equalsIgnoreCase("true")) {
//                            policyRepository.deleteById(policyDB.getId());
//                        } else {
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
                                    List<Pricing> pricingList = pricingRepository.findByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
                                    if (pricingList != null) {
                                        for (Pricing pricing : pricingList) {
                                            Pricing duplicatePricing = new Pricing();
                                            duplicatePricing.setPolicyHasTblVehicleTypeId(duplicatePolicyVehicle.getId());
                                            duplicatePricing.setPricePerHour(pricing.getPricePerHour());
                                            duplicatePricing.setLateFeePerHour(pricing.getLateFeePerHour());
                                            duplicatePricing.setFromHour(pricing.getFromHour());
                                            pricingRepository.save(duplicatePricing);
                                        }
                                        duplicatePolicyVehicle.setPricingList(policyHasTblVehicleType.getPricingList());
                                    }
                                }
                            }
//                        }
//                    }
//                }
            }
        }

    }

    public List<VehicleType> getLocationHasVehicleTypes(Integer locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        List<VehicleType> vehicleTypeList = new ArrayList<>();
        //Todo
        if (location.isPresent()) {
            List<Policy> policyList = location.get().getPolicyList();
            if (policyList != null) {
                for (Policy policy : policyList) {
                    List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policy.getId());
                    if (policyHasTblVehicleTypes != null) {
                        for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
                            VehicleType vehicleType = policyHasTblVehicleType.getVehicleTypeId();
                            if (!vehicleTypeList.contains(vehicleType)) {
                                vehicleTypeList.add(vehicleType);
                            }
                        }
                    }

                }
            }

        }
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
