package com.example.demo.component.user;

import com.example.demo.config.NFCServerProperties;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.component.user.User;
import com.example.demo.component.vehicle.Vehicle;
import com.example.demo.component.user.UserRepository;
import com.example.demo.component.vehicle.VehicleRepository;
import com.example.demo.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public UserService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<User> getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    public void updateUser(User user) {
        Optional<User> userDB = userRepository.findById(user.getId());
        if (userDB.isPresent()) {
            User existedUser = userDB.get();
            user.setPassword(existedUser.getPassword());
            userRepository.save(user);
        }
    }

    @Transactional
    public void createUser(User user) {
        if (user.getVehicle() != null) {
            boolean needVerify = false;
            //TODO check if phoneNumber exist
            Vehicle vehicle = null;
            if (user.getId() != null) {
//                vehicle = vehicleRepository.findByVehicleNumber(userRepository.findById(user.getId()).get().getVehicleNumber()).get();
                vehicle = user.getVehicle();
            }
            if (user.getId() == null || !userRepository.findById(user.getId()).isPresent()
                    || !vehicle.getVehicleNumber().equals(user.getVehicle().getVehicleNumber())
                    || !vehicle.getLicensePlateId().equals(user.getVehicle().getLicensePlateId())) {
                needVerify = true;
                user.getVehicle().setVerified(false);
                user.setActivated(false);
                vehicleRepository.save(user.getVehicle());
//                user.setVehicleNumber(user.getVehicle().getVehicleNumber());
            }
//            user.setVehicleNumber(user.getVehicle().getVehicleNumber());
            userRepository.save(user);
            if (needVerify) {
                try {
                    PushNotificationService.sendUserNeedVerifyNotification(
                            "fhRoDKtJR4Q:APA91bFRKKjR2GydlMD0akn71EluhoayB7YXe3a9M5MVat1IRPGo-59onV4VmI-KLj3b-e0zQ2k55brMCxTGJPIcZK2eNslJMnTdq8BNecpqJwsDO5InyL-ALvF0ojQEb_PMtX_xtYsf",
                            user.getPhoneNumber()
                    );
                } catch (Exception e) {
                    System.err.println("Cannot connect to firebase");
                }
            }
        }
    }

    public void requestNewConfirmCode(String phoneNumber, String confirmCode) {
        PushNotificationService.sendPhoneConfirmNotification(NFCServerProperties.getSmsHostToken(), phoneNumber, confirmCode);
    }

    public Optional<User> getUserByPhone(String phone) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
//        if (user.isPresent()) {
//            user.get().setVehicle(vehicleRepository.findByVehicleNumber(user.get().getVehicleNumber()).get());
//        }
        return user;

    }

    public Page<User> getAllUser(Integer page, Integer numOfRows) {
        return userRepository.findAll(new PageRequest(page, numOfRows));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public String hashID(Integer id) {
        ByteBuffer b = ByteBuffer.allocate(4);
        //b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
        b.putInt(id);
        byte[] result = b.array();
        return Base64.getEncoder().withoutPadding().encodeToString(result);
    }

    @Autowired
    private EntityManager entityManager;

    public ResponseObject searchUser(List<SearchCriteria> params, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root r = query.from(User.class);

        Predicate predicate = builder.conjunction();
        for (SearchCriteria param : params) {

            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                Object type = param.getType();
                if (type == null) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else if (type.equals("vehicle")) {
                    Join<User, Vehicle> join = r.join("vehicle");
                    predicate = builder.and(predicate,
                            builder.like(join.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                    predicate = builder.and(predicate, predicate);
                }
            } else {
                Object type = param.getType();
                if (type == null) {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                } else if (type.equals("vehicle")) {
                    Join<User, Vehicle> join = r.join("vehicle");
                    predicate = builder.and(predicate,
                            builder.equal(join.get(param.getKey()),
                                    param.getValue()));
                    predicate = builder.and(predicate, predicate);
                }
            }
        }
        query.where(predicate);
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        List<User> result = typedQuery.getResultList();
//        for (User user : result) {
//            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicle()).get());
//      1  }
        int totalPages = (int) Math.ceil((double) result.size() / pageSize);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<User> userList = typedQuery.getResultList();
        responseObject.setData(userList);
        responseObject.setTotalPages(totalPages);
        responseObject.setPageNumber(pagNumber);
        responseObject.setPageSize(pageSize);
        return responseObject;
    }


    public List<User> getUsers(int pagNumber, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        CriteriaQuery<User> select = criteriaQuery.select(from);
        TypedQuery<User> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<User> listUsers = typedQuery.getResultList();
//        for (User user : listUsers) {
//            Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(user.getVehicleNumber());
//            if (vehicle.isPresent()) {
//                user.setVehicle(vehicle.get());
//            }
//        }
        return listUsers;
    }

    public Long getTotalUsers(int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(
                countQuery.from(User.class)));
        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();
        return (long) Math.ceil((double) count / pageSize);
    }

    public Optional<User> login(String phone, String password) {
        Optional<User> user = userRepository.findByPhoneNumberAndPassword(phone, password);
//        if (user.isPresent()) {
//            user.get().setVehicle(vehicleRepository.findByVehicleNumber(user.get().getVehicleNumber()).get());
//        }
        return user;
    }

    public Optional<User> updateUserSmsNoti(User user) {
        Optional<User> userDB = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (userDB.isPresent()) {
            userDB.get().setSmsNoti(user.getSmsNoti());
            userRepository.save(userDB.get());
        }
        return userDB;
    }

    public void activateUser(String phoneNumber) {
        Optional<User> userDB = userRepository.findByPhoneNumber(phoneNumber);
        if (userDB.isPresent()) {
            User existedUser = userDB.get();
            existedUser.setActivated(true);
            userRepository.save(existedUser);
        }
    }

    public Optional<User> topUp(String userId, double amount) {
        Optional<User> userDB = userRepository.findById(Integer.parseInt(userId));
        if (userDB.isPresent()) {
            userDB.get().setMoney(userDB.get().getMoney() + amount);
            userRepository.save(userDB.get());
        }
        return userDB;
    }

    public Optional<User> changePassword(String phoneNumber, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findByPhoneNumberAndPassword(phoneNumber, oldPassword);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            userRepository.save(user.get());
        }
        return user;
    }

    public Optional<User> resetPassword(String phoneNumber, String newPassword) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            userRepository.save(user.get());
        }
        return user;
    }

    public Optional<User> getUserByVehicleNumber(String vehicleNumber) {
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);
        if (vehicle.isPresent()) {
            return userRepository.findByVehicle(vehicle.get());
        }
        return null;
    }

    public Optional<User> saveUser(User user){
        return Optional.of(userRepository.save(user));
    }
}
