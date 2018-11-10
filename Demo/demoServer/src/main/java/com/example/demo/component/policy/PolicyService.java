package com.example.demo.component.policy;

import com.example.demo.component.location.Location;
import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.vehicleType.VehicleType;
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
        if (policy.getId() == 0) {
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
                    PolicyHasTblVehicleType instance = policyHasVehicleTypeRepository.findByPolicyIdAndVehicleTypeId(policyDB.getId(), vehicleTypeList.get(i)).get();
                    pricingRepository.deleteByPolicyHasVehicleTypeId(instance.getId());
//                    if (null != pricings || !pricings.isEmpty()) {
//                        for (Pricing pricing : pricings) {
//                            pricingRepository.delete(pricing);
//                        }
//                    }
                    policyHasVehicleTypeRepository.delete(instance);
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
    public void deleteBylocationIdAndPolicyInstanceId(Integer policyInstanceId) {
//        Optional<Location> locationOpt = locationRepository.findById(locationId);
        Optional<Policy> policyInstanceOptional = policyRepository.findById(policyInstanceId);
        if ( policyInstanceOptional.isPresent()) {
//            Location location = locationOpt.get();
            Policy policy = policyInstanceOptional.get();
            List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeRepository.findAllByPolicyId(policy.getId());
            for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
                pricingRepository.deleteByPolicyHasVehicleTypeId(policyHasTblVehicleType.getId());
                policyHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyHasTblVehicleType.getId());
            }
//            policyInstance.setPolicyInstanceHasTblVehicleTypes(policyInstanceHasTblVehicleTypes);
            policyRepository.delete(policy);
        }

    }

    @Transactional
    public void deletePolicyInstance(Integer locationId, Policy policy, List<Integer> policyHasVehicleTypeId) {
        if (!policyHasVehicleTypeId.isEmpty()) {
            for (Integer id : policyHasVehicleTypeId) {
                pricingRepository.deleteByPolicyHasVehicleTypeId(id);
            }
        }
//
        for (int i = 0; i < policyHasVehicleTypeId.size(); i++) {
            Optional<PolicyHasTblVehicleType> policyInstanceHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId.get(i));
            if (policyInstanceHasTblVehicleType.isPresent()) {
                policyHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyInstanceHasTblVehicleType.get().getId());
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
                } else {
                    type = r.get(param.getKey()).getJavaType();
                }
                if (type == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else if(type == VehicleType.class){
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<PolicyHasTblVehicleType> vehicleQuery = builder.createQuery(PolicyHasTblVehicleType.class);
                    Root<PolicyHasTblVehicleType> policyInstanceVehicleRoot = vehicleQuery.from(PolicyHasTblVehicleType.class);
                    Join<PolicyHasTblVehicleType, VehicleType> vehicleTypeJoin = policyInstanceVehicleRoot.join("vehicleTypeId");
                    ArrayList<String> vehicleTypes = (ArrayList<String>) param.getValue();
                    Expression<String> vehicleExpression = vehicleTypeJoin.get("name");
                    Predicate vehiclePredicate = vehicleExpression.in(vehicleTypes);

                    vehicleQuery.where(vehiclePredicate);
                    TypedQuery<PolicyHasTblVehicleType> vehicleTypeTypedQuery = entityManager.createQuery(vehicleQuery);
                    List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = vehicleTypeTypedQuery.getResultList();

                    Join<Policy, PolicyHasTblVehicleType> policyJoin = r.join("policyInstanceHasTblVehicleTypes");
                    List<Integer> ids = new ArrayList<>();
                    for (PolicyHasTblVehicleType instance : policyHasTblVehicleTypes) {
                        ids.add(instance.getPolicyId());
                    }
                    Expression<String> policyExpression = policyJoin.get("policyInstanceId");
                    Predicate policyPredicate = policyExpression.in(ids);
                    predicate = builder.and(predicate,policyPredicate);
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        predicate = builder.and(predicate, builder.equal(r.get("locationId"), locationId));
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
        return responseObject;
    }
}
