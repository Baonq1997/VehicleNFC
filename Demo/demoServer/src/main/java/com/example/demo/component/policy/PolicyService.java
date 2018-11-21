package com.example.demo.component.policy;

import com.example.demo.component.location.Location;
import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
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
public class PolicyService {
    private PolicyRepository policyRepository;
    private LocationRepository locationRepository;
    private PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;
    private PricingRepository pricingRepository;

    public PolicyService(PolicyRepository policyRepository, PricingRepository pricingRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository, LocationRepository locationRepository) {
        this.policyRepository = policyRepository;
        this.locationRepository = locationRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
        this.pricingRepository = pricingRepository;
    }

    public List<Policy> getAll() {
        return policyRepository.findAll();
    }

    public List<Policy> getByLocationId(Integer locationId) {
        List<Policy> policies = policyRepository.findAllByLocationId(locationId);
        for (Policy policy : policies) {
            List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policy.getId());
            policy.setPolicyHasTblVehicleTypes(policyHasTblVehicleTypes);
        }
        return policies;
    }


    public Policy getById(Integer id) {
        return policyRepository.findById(id).get();
    }

    @Transactional
    public Policy savePolicy(Policy policy, List<VehicleType> vehicleTypeList, Integer locationId) {

        Policy policyDB;
        if (policy.getId() == null) {
//            // create
            Location location = locationRepository.findById(locationId).get();
            policy.setLocationId(locationId);
            policyDB = policyRepository.save(policy);
            List<Policy> policies = location.getPolicyList();
            policies.add(policyDB);
            location.setPolicyList(policies);
            locationRepository.save(location);
//
        } else {
            policyDB = policyRepository.findById(policy.getId()).get();
            if (policyDB != null) {
                policyDB.setAllowedParkingFrom(policy.getAllowedParkingFrom());
                policyDB.setAllowedParkingTo(policy.getAllowedParkingTo());
                policyRepository.save(policyDB);
            }
        }
        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policyDB.getId());
        for (int i = 0; i < vehicleTypeList.size(); i++) {
            if (policyHasTblVehicleTypes.size() == 0) {
                PolicyHasTblVehicleType policyHasTblVehicleType = new PolicyHasTblVehicleType();
                policyHasTblVehicleType.setPolicyId(policyDB.getId());
                policyHasTblVehicleType.setVehicleTypeId(vehicleTypeList.get(i));
                policyHasVehicleTypeRepository.saveAndFlush(policyHasTblVehicleType);
            } else {
                if (vehicleTypeList.get(i).getIsDelete().equalsIgnoreCase("true")) {
                    Optional<PolicyHasTblVehicleType> policyHasTblVehicleTypeOptional = policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policyDB.getId(), vehicleTypeList.get(i));
                    if (policyHasTblVehicleTypeOptional.isPresent()) {
                        PolicyHasTblVehicleType instance = policyHasTblVehicleTypeOptional.get();
                        pricingRepository.deleteByPolicyHasTblVehicleTypeId(instance.getId());
                        policyHasVehicleTypeRepository.delete(instance);
                    }

//                    if (null != pricings || !pricings.isEmpty()) {
//                        for (Pricing pricing : pricings) {
//                            pricingRepository.delete(pricing);
//                        }
//                    }

//                    break;
                } else {
                    Optional<PolicyHasTblVehicleType> instance = policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policyDB.getId(), vehicleTypeList.get(i));
                    if (!instance.isPresent()) {
                        PolicyHasTblVehicleType dto = new PolicyHasTblVehicleType();
                        dto.setPolicyId(policyDB.getId());
                        dto.setVehicleTypeId(vehicleTypeList.get(i));
                        policyHasVehicleTypeRepository.saveAndFlush(dto);
                    }

                }
            }
        }
        return policyDB;
    }

    @Transactional
    public void deleteBylocationIdAndPolicyId(Integer policyId) {
//        Optional<Location> locationOpt = locationRepository.findById(locationId);
        Optional<Policy> policyOptional = policyRepository.findById(policyId);
        if ( policyOptional.isPresent()) {
//            Location location = locationOpt.get();
            Policy policy = policyOptional.get();
            List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policy.getId());
            for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
                pricingRepository.deleteByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
                policyHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyHasTblVehicleType.getId());
            }
//            policy.setPolicyHasTblVehicleTypes(policyHasTblVehicleTypes);
            policyRepository.delete(policy);
        }

    }

    @Transactional
    public void deletePolicy(Integer locationId, Policy policy, List<Integer> policyHasVehicleTypeId) {
        if (!policyHasVehicleTypeId.isEmpty()) {
            for (Integer id : policyHasVehicleTypeId) {
                pricingRepository.deleteByPolicyHasTblVehicleTypeId(id);
            }
        }
//
        for (int i = 0; i < policyHasVehicleTypeId.size(); i++) {
            Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId.get(i));
            if (policyHasTblVehicleType.isPresent()) {
                policyHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyHasTblVehicleType.get().getId());
            }

        }
        policyRepository.delete(policy);
    }

    @Autowired
    private EntityManager entityManager;

    public ResponseObject filterPoliciesByLocation(Integer locationId, List<SearchCriteria> params
            , int pageNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Policy> query = builder.createQuery(Policy.class);
        Root r = query.from(Policy.class);

        Predicate predicate = builder.conjunction();
        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.le(r.get(param.getKey()),
                               Long.parseLong(param.getValue().toString())));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.ge(r.get(param.getKey()),
                                Long.parseLong(param.getValue().toString())));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                Object type = new Object();
                if (param.getKey().equalsIgnoreCase("vehicleTypes")) {
                    type = VehicleType.class;
                } else if (param.getKey().equalsIgnoreCase("locationId")) {
                    type = Location.class;
                }else {
                    type = r.get(param.getKey()).getJavaType();
                }
                if (type == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else if(type == VehicleType.class){
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<PolicyHasTblVehicleType> vehicleQuery = builder.createQuery(PolicyHasTblVehicleType.class);
                    Root<PolicyHasTblVehicleType> policyVehicleRoot = vehicleQuery.from(PolicyHasTblVehicleType.class);
                    Join<PolicyHasTblVehicleType, VehicleType> vehicleTypeJoin = policyVehicleRoot.join("vehicleTypeId");
                    ArrayList<String> vehicleTypes = (ArrayList<String>) param.getValue();
                    Expression<String> vehicleExpression = vehicleTypeJoin.get("name");
                    Predicate vehiclePredicate = vehicleExpression.in(vehicleTypes);

                    vehicleQuery.where(vehiclePredicate);
                    TypedQuery<PolicyHasTblVehicleType> vehicleTypeTypedQuery = entityManager.createQuery(vehicleQuery);
                    List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = vehicleTypeTypedQuery.getResultList();

                    Join<Policy, PolicyHasTblVehicleType> policyJoin = r.join("policyHasTblVehicleTypes");
                    List<Integer> ids = new ArrayList<>();
                    for (PolicyHasTblVehicleType instance : policyHasTblVehicleTypes) {
                        ids.add(instance.getPolicyId());
                    }
                    if (ids.size() == 0) {
                        responseObject.setData(null);
                        return responseObject;
                    }
                    Expression<String> policyExpression = policyJoin.get("policyId");
                    Predicate policyPredicate = policyExpression.in(ids);
                    predicate = builder.and(predicate,policyPredicate);
                } else if (type == Location.class) {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Location> locationQuery = builder.createQuery(Location.class);
                    Root<Location> locationRoot = locationQuery.from(Location.class);
                    Predicate locationPredicate1 = cb.like(locationRoot.get("location"),
                            "%" + param.getValue() + "%");
                    locationQuery.where(locationPredicate1);
                    TypedQuery<Location> locationTypedQuery = entityManager.createQuery(locationQuery);
                    List<Location> locations = locationTypedQuery.getResultList();
                    List<Integer> locationIds = new ArrayList<>();
                    if (locations != null) {
                        for (Location location : locations) {
                            locationIds.add(location.getId());
                        }
                        Expression<String> policyExpression = r.get("locationId");
                        Predicate policyPredicate = policyExpression.in(locationIds);
                        predicate = builder.and(predicate,policyPredicate);
                    }
                }else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        if (locationId != 0) {
            predicate = builder.and(predicate, builder.equal(r.get("locationId"), locationId));
        }
        query.where(predicate);
        query.groupBy(r.get("id"));
        TypedQuery<Policy> typedQuery = entityManager.createQuery(query);
        List<Policy> policies = typedQuery.getResultList();
        int totalPages = policies.size() / pageSize;
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Policy> resultList = typedQuery.getResultList();
        responseObject.setData(resultList);
        responseObject.setTotalPages(totalPages + 1);
        responseObject.setPageNumber(pageNumber);
        responseObject.setPageSize(PaginationEnum.locationPageSize.getNumberOfRows());
        return responseObject;
    }
    public boolean isExistedPolicy(Policy policy, Integer locationId) {
        Long allowedParkingFrom = policy.getAllowedParkingFrom();
        Long allowedParkingTo = policy.getAllowedParkingTo();
        List<Policy> policies = policyRepository.findByAllowedParkingFromAndAllowedParkingToAndLocationId(allowedParkingFrom,allowedParkingTo,locationId);
        if (policies.size() != 0) {
            return true;
        }
        return false;
    }
}
