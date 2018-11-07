package com.example.demo.component.policy;

import com.example.demo.component.location.Location;
import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.policy.PolicyInstanceRepository;
import com.example.demo.component.vehicle.Vehicle;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public List<PolicyInstance> getAll() {
        return policyInstanceRepository.findAll();
    }

    public List<PolicyInstance> getByLocationId(Integer locationId) {
        List<PolicyInstance> policyInstances = policyInstanceRepository.findAllByLocationId(locationId);
        for (PolicyInstance policyInstance : policyInstances) {
            List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes = policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policyInstance.getId());
            policyInstance.setPolicyInstanceHasTblVehicleTypes(policyInstanceHasTblVehicleTypes);
        }
        return policyInstances;
    }


    public PolicyInstance getById(Integer id) {
        return policyInstanceRepository.findById(id).get();
    }

    @Transactional
    public PolicyInstance savePolicy(PolicyInstance policyInstance, List<VehicleType> vehicleTypeList, Integer locationId) {

        PolicyInstance policyInstanceDB ;
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
                    pricingRepository.deleteByPolicyInstanceHasTblVehicleTypeId(instance.getId());
//                    if (null != pricings || !pricings.isEmpty()) {
//                        for (Pricing pricing : pricings) {
//                            pricingRepository.delete(pricing);
//                        }
//                    }
                    policyInstanceHasVehicleTypeRepository.delete(instance);
//                    break;
                } else {
                    Optional<PolicyInstanceHasTblVehicleType> instance = policyInstanceHasVehicleTypeRepository.findByPolicyInstanceIdAndVehicleTypeId(policyInstanceDB.getId(), vehicleTypeList.get(i));
                    if (!instance.isPresent()) {
                        PolicyInstanceHasTblVehicleType dto = new PolicyInstanceHasTblVehicleType();
                        dto.setPolicyInstanceId(policyInstanceDB.getId());
                        dto.setVehicleTypeId(vehicleTypeList.get(i));
                        policyInstanceHasVehicleTypeRepository.saveAndFlush(dto);
                    }

                }
            }
        }
        return policyInstanceDB;
    }

    @Transactional
    public void deleteBylocationIdAndPolicyInstanceId(Integer policyInstanceId) {
//        Optional<Location> locationOpt = locationRepository.findById(locationId);
        Optional<PolicyInstance> policyInstanceOptional = policyInstanceRepository.findById(policyInstanceId);
        if ( policyInstanceOptional.isPresent()) {
//            Location location = locationOpt.get();
            PolicyInstance policyInstance = policyInstanceOptional.get();
            List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes = policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policyInstance.getId());
            for (PolicyInstanceHasTblVehicleType policyInstanceHasTblVehicleType : policyInstanceHasTblVehicleTypes) {
                pricingRepository.deleteByPolicyInstanceHasTblVehicleTypeId(policyInstanceHasTblVehicleType.getId());
                policyInstanceHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyInstanceHasTblVehicleType.getId());
            }
//            policyInstance.setPolicyInstanceHasTblVehicleTypes(policyInstanceHasTblVehicleTypes);
            policyInstanceRepository.delete(policyInstance);
        }

    }

    @Transactional
    public void deletePolicyInstance(Integer locationId, PolicyInstance policyInstance, List<Integer> policyHasVehicleTypeId) {
        if (!policyHasVehicleTypeId.isEmpty()) {
            for (Integer id : policyHasVehicleTypeId) {
                pricingRepository.deleteByPolicyInstanceHasTblVehicleTypeId(id);
            }
        }
//
        for (int i = 0; i < policyHasVehicleTypeId.size(); i++) {
            Optional<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleType = policyInstanceHasVehicleTypeRepository.findById(policyHasVehicleTypeId.get(i));
            if (policyInstanceHasTblVehicleType.isPresent()) {
                policyInstanceHasVehicleTypeRepository.deletePolicyHasVehicleTypeById(policyInstanceHasTblVehicleType.get().getId());
            }

        }
        policyInstanceRepository.delete(policyInstance);
    }

    @Autowired
    private EntityManager entityManager;

    public ResponseObject filterPoliciesByLocation(Integer locationId, List<SearchCriteria> params
            , int pageNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PolicyInstance> query = builder.createQuery(PolicyInstance.class);
        Root r = query.from(PolicyInstance.class);

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
                    CriteriaQuery<PolicyInstanceHasTblVehicleType> vehicleQuery = builder.createQuery(PolicyInstanceHasTblVehicleType.class);
                    Root<PolicyInstanceHasTblVehicleType> policyInstanceVehicleRoot = vehicleQuery.from(PolicyInstanceHasTblVehicleType.class);
                    Join<PolicyInstanceHasTblVehicleType, VehicleType> vehicleTypeJoin = policyInstanceVehicleRoot.join("vehicleTypeId");
                    ArrayList<String> vehicleTypes = (ArrayList<String>) param.getValue();
                    Expression<String> vehicleExpression = vehicleTypeJoin.get("name");
                    Predicate vehiclePredicate = vehicleExpression.in(vehicleTypes);

                    vehicleQuery.where(vehiclePredicate);
                    TypedQuery<PolicyInstanceHasTblVehicleType> vehicleTypeTypedQuery = entityManager.createQuery(vehicleQuery);
                    List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes = vehicleTypeTypedQuery.getResultList();

                    Join<PolicyInstance, PolicyInstanceHasTblVehicleType> policyJoin = r.join("policyInstanceHasTblVehicleTypes");
                    List<Integer> ids = new ArrayList<>();
                    for (PolicyInstanceHasTblVehicleType instance : policyInstanceHasTblVehicleTypes) {
                        ids.add(instance.getPolicyInstanceId());
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
        TypedQuery<PolicyInstance> typedQuery = entityManager.createQuery(query);
        List<PolicyInstance> policyInstances = typedQuery.getResultList();
        int totalPages = policyInstances.size() / pageSize;
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<PolicyInstance> resultList = typedQuery.getResultList();
        responseObject.setData(resultList);
        responseObject.setTotalPages(totalPages + 1);
        responseObject.setPageNumber(pageNumber);
        return responseObject;
    }
}
