package com.example.demo.component.vehicle;

import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.component.user.User;
import com.example.demo.component.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Autowired
    private EntityManager entityManager;

    public List<Vehicle> getAllVehicle(int pagNumber, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vehicle> criteriaQuery = builder.createQuery(Vehicle.class);
        Root<Vehicle> from = criteriaQuery.from(Vehicle.class);
        CriteriaQuery<Vehicle> select = criteriaQuery.select(from);
        TypedQuery<Vehicle> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Vehicle> vehicleList = typedQuery.getResultList();
        for (Vehicle vehicle : vehicleList) {
            Optional<User> owner = userRepository.findByVehicleNumber(vehicle.getVehicleNumber());
            if (owner.isPresent()) {
                vehicle.setOwner(owner.get());
            }
        }
        return vehicleList;
    }

    public Optional<Vehicle> getVehicle(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumber(vehicleNumber);
    }

    public Optional<Vehicle> verifyVehicle(Vehicle vehicle) {
        Optional<Vehicle> vehicleDB = vehicleRepository.findByVehicleNumber(vehicle.getVehicleNumber());
        if (vehicleDB.isPresent()) {
            vehicleDB.get().setBrand(vehicle.getBrand());
            vehicleDB.get().setSize(vehicle.getSize());
            vehicleDB.get().setExpireDate(vehicle.getExpireDate());
            vehicleDB.get().setVehicleTypeId(vehicle.getVehicleTypeId());
            vehicleDB.get().setVerified(true);
            vehicleRepository.save(vehicleDB.get());
        }
        return vehicleDB;
    }

    public Optional<Vehicle> saveVehicle(Vehicle vehicle) {
        Optional<Vehicle> vehicleDB = vehicleRepository.findByVehicleNumber(vehicle.getVehicleNumber());
        if (vehicleDB.isPresent()) {
            vehicleDB.get().setLicensePlateId(vehicle.getLicensePlateId());
            vehicleDB.get().setBrand(vehicle.getBrand());
            vehicleDB.get().setSize(vehicle.getSize());
            vehicleDB.get().setExpireDate(vehicle.getExpireDate());
            vehicleDB.get().setVehicleTypeId(vehicle.getVehicleTypeId());
            vehicleRepository.save(vehicleDB.get());
        } else {
            vehicle.setVerified(true);
            vehicleRepository.save(vehicle);
            vehicleDB = Optional.of(vehicle);
        }
        return vehicleDB;
    }

    public Long getTotalVehicles(int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(
                countQuery.from(Vehicle.class)));
        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();
        return (long) Math.ceil((double) count / pageSize);
    }

    public ResponseObject searchVehicle(SearchCriteria param, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vehicle> query = builder.createQuery(Vehicle.class);
        Root r = query.from(Vehicle.class);

        Predicate predicate = builder.conjunction();

        if (param.getOperation().equalsIgnoreCase(">")) {
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(r.get(param.getKey()),
                            param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate,
                    builder.lessThanOrEqualTo(r.get(param.getKey()),
                            param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (r.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate,
                        builder.like(r.get(param.getKey()),
                                "%" + param.getValue() + "%"));
            } else {
                predicate = builder.and(predicate,
                        builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }
        query.where(predicate);
        TypedQuery<Vehicle> typedQuery = entityManager.createQuery(query);
        List<Vehicle> result = typedQuery.getResultList();
        for (Vehicle vehicle : result) {
            Optional<User> owner = userRepository.findByVehicleNumber(vehicle.getVehicleNumber());
            if (owner.isPresent()) {
                vehicle.setOwner(owner.get());
            }
        }
        int totalPages = (int) Math.ceil((double) result.size() / pageSize);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Vehicle> userList = typedQuery.getResultList();
        responseObject.setData(userList);
        responseObject.setTotalPages(totalPages);
        responseObject.setPageNumber(pagNumber);
        responseObject.setPageSize(pageSize);
        return responseObject;
    }

    public boolean deleteVehicle(String vehicleNumber) {
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);
        if (vehicle.isPresent()) {
            vehicleRepository.delete(vehicle.get());
            return true;
        }
        return false;
    }
}
