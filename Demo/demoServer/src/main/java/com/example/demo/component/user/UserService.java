package com.example.demo.component.user;

import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.component.vehicle.Vehicle;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;
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
        if (userId == null) {
            userId = -1;
        }
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    public boolean updateUser(User user) {
        Optional<User> userDB = userRepository.findById(user.getId());
        if (userDB.isPresent()) {
            if (userDB.get().getPhoneNumber().equals(user.getPhoneNumber()) || !userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
                User existedUser = userDB.get();
                existedUser.setPassword(user.getPassword());
                existedUser.setFirstName(user.getFirstName());
                existedUser.setLastName(user.getLastName());
                existedUser.setPhoneNumber(user.getPhoneNumber());
                existedUser.setMoney(user.getMoney());
                existedUser.setSmsNoti(user.getSmsNoti());
                existedUser.setActivated(user.getActivated());
                userRepository.save(existedUser);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Integer createUser(User user, Map<String, String> tokenList) {
        if (user.getVehicle() != null) {
            boolean needVerify = true;

            //TODO check if any vehicle avaiable
            Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(user.getVehicle().getVehicleNumber());
            if (vehicle.isPresent()) {
                if (vehicle.get().isActive()) {
                    return null;
                } else {
                    vehicle.get().setLicensePlateId(user.getVehicle().getLicensePlateId());
                    vehicle.get().setActive(true);
                    needVerify = vehicle.get().isVerified();
                    user.setVehicle(vehicle.get());
                }
            }

            user.getVehicle().setVerified(!needVerify);
            user.setActivated(false);
            vehicleRepository.save(user.getVehicle());
            userRepository.save(user);

            user.getVehicle().setOwnerId(user.getId());
            vehicleRepository.save(user.getVehicle());

            if (needVerify) {
                for (Map.Entry<String, String> entry : tokenList.entrySet()) {
                    try {
                        PushNotificationService.sendUserNeedVerifyNotification(
                                entry.getValue(),
                                user.getPhoneNumber()
                        );
                    } catch (Exception e) {
                        System.err.println("Cannot connect to firebase");
                    }
                }

            }
            return user.getId();
        }
        return null;
    }

    public boolean checkExistedPhoneNumber(String phoneNumber) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isPresent()) {
            return true;
        }
        return false;
    }

    public void requestNewConfirmCode(String phoneNumber, String confirmCode) {
        PushNotificationService.sendPhoneConfirmNotification(null, phoneNumber, confirmCode);
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

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Integer id) {

        userRepository.updateUserStatus(0, id);
    }

    public static String encodeId(Integer id) {
        if (id != null) {
            try {
                byte[] plaintTextByteArray = id.toString().getBytes("UTF8");
                return Base64.getEncoder().encodeToString(plaintTextByteArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Integer decodeId(String encodedKey) {
        try {
            byte[] plaintTextByteArray = Base64.getDecoder().decode(encodedKey);
            return Integer.parseInt(new String(plaintTextByteArray));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    @Autowired
    private EntityManager entityManager;

    public ResponseObject searchUser(List<SearchCriteria> params, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root r = query.from(User.class);
        boolean isFilterActive = true;
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
                Object type = new Object();
                if (param.getKey().equalsIgnoreCase("vehicle")) {
                    // do vehicleNumber la object nam trong User
                    type = User.class;
                } else if (param.getKey().equalsIgnoreCase("isVerified")) {
                    type = Vehicle.class;
                } else if (param.getKey().equalsIgnoreCase("isActivated")) {
                    type = User.class;
                } else {
                    type = r.get(param.getKey()).getJavaType();
                }
                if (type == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else if (type == Vehicle.class) {
                    if (param.getKey().equalsIgnoreCase("isVerified")) {
                        Join<User, Vehicle> join = r.join("vehicle");
                        Predicate vehiclePredicate = builder.equal(join.get("isVerified"), Boolean.parseBoolean(param.getValue().toString()));
                        predicate = builder.and(predicate, vehiclePredicate);
                    }
                } else if (type == User.class) {
                    if (param.getKey().equalsIgnoreCase("vehicle")) {
                        Join<User, Vehicle> join = r.join("vehicle");
                        Predicate vehiclePredicate = builder.like(join.get("vehicleNumber"), "%" + param.getValue() + "%");
                        predicate = builder.and(predicate, vehiclePredicate);
                    } else {
                        isFilterActive = false;
                    }
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        if (isFilterActive) {
            predicate = builder.and(predicate, builder.equal(r.get("isActivated"), true));
        } else {
            predicate = builder.and(predicate, builder.equal(r.get("isActivated"), false));
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

    public Optional<User> topUp(Integer userId, double amount) {
        Optional<User> userDB = userRepository.findById(userId);
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

    public Optional<User> saveUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    public boolean unbindVehicle(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(user.get().getVehicle().getVehicleNumber());
            if (vehicle.isPresent()) {
                vehicle.get().setActive(false);
                vehicle.get().setOwnerId(null);
                vehicleRepository.save(vehicle.get());
                user.get().setVehicle(null);
                user.get().setActivated(true);
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

    public Vehicle changeVehicle(String phoneNumber, String vehicleNumber, String licenseId) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);
            if (vehicle.isPresent()) {
                if (userRepository.findByVehicle(vehicle.get()).isPresent()) {
                    return null;
                }
            } else {
                vehicle = Optional.of(new Vehicle());
                vehicle.get().setVehicleNumber(vehicleNumber);
            }
            vehicle.get().setLicensePlateId(licenseId);
            vehicleRepository.save(vehicle.get());
            if (!vehicle.get().isVerified()) {
                try {
                    PushNotificationService.sendUserNeedVerifyNotification(
                            "fhRoDKtJR4Q:APA91bFRKKjR2GydlMD0akn71EluhoayB7YXe3a9M5MVat1IRPGo-59onV4VmI-KLj3b-e0zQ2k55brMCxTGJPIcZK2eNslJMnTdq8BNecpqJwsDO5InyL-ALvF0ojQEb_PMtX_xtYsf",
                            user.get().getPhoneNumber()
                    );
                } catch (Exception e) {
                    System.err.println("Cannot connect to firebase");
                }
            }
            user.get().setVehicle(vehicle.get());
            saveUser(user.get());
            return vehicle.get();
        }
        return null;
    }

    public Boolean addVehicleToUser(String vehicleNumber, String phoneNumber) {
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);
        if (vehicle.isPresent()) {
            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setVehicle(vehicle.get());
                userRepository.save(user);
                Vehicle vehicleDB = vehicle.get();
                vehicleDB.setOwnerId(user.getId());
                vehicleRepository.save(vehicleDB);
                return true;
            }
        }
        return false;
    }

    public void updateUserStatus(boolean status, Integer userId) {
        userRepository.updateUserStatus(0, userId);
    }

}
