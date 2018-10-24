package com.example.demo.service;

import com.example.demo.Config.NFCServerProperties;
import com.example.demo.Config.ResponseObject;
import com.example.demo.Config.SearchCriteria;
import com.example.demo.entities.User;
import com.example.demo.entities.Vehicle;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
        if (user.isPresent()) {
            user.get().setVehicle(vehicleRepository.findByVehicleNumber(user.get().getVehicleNumber()).get());
        }
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

    public void createUser(User user, String confirmCode) {
        if (user.getVehicle() != null) {
            userRepository.save(user);
//            requestNewConfirmCode(user.getPhoneNumber(), confirmCode);
        }
    }

    public void requestNewConfirmCode(String phoneNumber, String confirmCode) {
        PushNotificationService pushNotificationService = new PushNotificationService();
        pushNotificationService.sendPhoneConfirmNotification(NFCServerProperties.getSmsHostToken(), phoneNumber, confirmCode);
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone);

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

    public ResponseObject searchUser(SearchCriteria param, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root r = query.from(User.class);

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
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        List<User> result = typedQuery.getResultList();
        for (User user : result) {
            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get());
        }
        int totalPages = result.size() / pageSize;
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<User> userList = typedQuery.getResultList();
        responseObject.setData(userList);
        responseObject.setTotalPages(totalPages + 1);
        responseObject.setPageNumber(pagNumber);
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
        for (User user : listUsers) {
            Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNumber(user.getVehicleNumber());
            if (vehicle.isPresent()) {
                user.setVehicle(vehicle.get());
            }
        }
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
        return (long) (count / pageSize) + 1;
    }

    public Optional<User> login(String phone, String password) {
        return userRepository.findByPhoneNumberAndPassword(phone, password);
    }

    public void updateUserSmsNoti(User user) {
        User userDB = userRepository.findByPhoneNumber(user.getPhoneNumber()).get();
        if (userDB != null) {
            userDB.setSmsNoti(user.getSmsNoti());
            userRepository.save(userDB);
        }
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
}
