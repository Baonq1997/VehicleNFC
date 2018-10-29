package com.example.demo.service;

import com.example.demo.Config.*;
import com.example.demo.entities.*;
import com.example.demo.entities.Order;
import com.example.demo.model.HourHasPrice;
import com.example.demo.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Notification;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final OrderPricingRepository orderPricingRepository;
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;
    private final PricingRepository pricingRepository;
    private final VehicleRepository vehicleRepository;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, UserRepository userRepository, LocationRepository locationRepository, OrderPricingRepository orderPricingRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository, PricingRepository pricingRepository, VehicleRepository vehicleRepository) {
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.orderPricingRepository = orderPricingRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
        this.pricingRepository = pricingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().getUserId().setVehicle(
                    vehicleRepository.findByVehicleNumber(order.get().getUserId().getVehicleNumber()).get()
            );
            order.get().setOrderPricings(orderPricingRepository.findByOrderId(order.get().getId()));
        }
        return order;
    }

    public Optional<Order> getOpenOrderByUserId(Integer id) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndOrderStatusId(userRepository.findById(id).get()
                , orderStatusRepository.findByName(OrderStatusEnum.Open.getName()).get());
        if (order.isPresent()) {
            order.get().getUserId().setVehicle(
                    vehicleRepository.findByVehicleNumber(order.get().getUserId().getVehicleNumber()).get()
            );
            order.get().setOrderPricings(orderPricingRepository.findByOrderId(order.get().getId()));
        }
        return order;
    }

    @Transactional
    public Optional<Order> createOrder(User checkInUser, Location location) {
        String userToken = checkInUser.getDeviceToken();
        checkInUser = userRepository.findById(checkInUser.getId()).get();
        location = locationRepository.findById(location.getId()).get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Open.getName()).get();

        Order order = null;
        try {
            order = orderRepository.findByUserIdAndLocationIdAndOrderStatusId(checkInUser,
                    location, orderStatus).get();
        } catch (NoSuchElementException e) {
        }

        if (order != null) {
            checkOutOrder(order, userToken, checkInUser);

            return Optional.of(order);
        }

        order = new Order();
        order.setOrderStatusId(orderStatus);

        Vehicle vehicle = vehicleRepository.findByVehicleNumber(checkInUser.getVehicleNumber()).get();
        order.setVehicleType(vehicle.getVehicleTypeId());
        order.setUserId(checkInUser);
        order.setLocationId(locationRepository.findById(location.getId()).get());

        order.setCheckInDate(new Date().getTime());

        //TODO kiểm tra thời điểm hiện tại để chọn policy
//        Policy policy = order.getLocationId().getPolicyList().get(0);
        List<Pricing> pricings = getPricingList(order, checkInUser);
        if (pricings == null) {
            return null;
        }
        orderRepository.save(order);

        List<OrderPricing> orderPricings = OrderPricing.convertListPricingToOrderPricing(pricings);
        for (OrderPricing orderPricing : orderPricings
                ) {
            orderPricing.setOrderId(order.getId());
            orderPricingRepository.save(orderPricing);
        }

        //TODO kiểm tra user đó xài gì để gửi SMS hay Noti
        sendNotification(checkInUser, order, userToken, orderPricings, NotificationEnum.CHECK_IN);

        return Optional.of(order);
    }

    public List<Pricing> getPricingList(Order order, User user) {
        List<Policy> policies = order.getLocationId().getPolicyList();
        List<Policy> matchPolicies = new ArrayList<>();
        for (Policy policy : policies) {
            if (policy.getAllowedParkingFrom() < order.getCheckInDate()
                    && policy.getAllowedParkingTo() > order.getCheckInDate()) {
                matchPolicies.add(policy);
            }
        }
        Policy choosedPolicy = null;
        PolicyHasTblVehicleType policyHasTblVehicleType = null;
        for (Policy policy : matchPolicies) {
            while (choosedPolicy == null) {
                Vehicle vehicle = vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get();
                policyHasTblVehicleType = policyHasVehicleTypeRepository
                        .findByPolicyIdAndVehicleTypeId(policy.getId(), vehicle.getVehicleTypeId()).get();
                if (policyHasTblVehicleType != null) {
                    choosedPolicy = policy;
                    break;
                }
            }
        }
        if (policyHasTblVehicleType != null) {
            order.setAllowedParkingFrom(choosedPolicy.getAllowedParkingFrom());
            order.setAllowedParkingTo(choosedPolicy.getAllowedParkingTo());
            order.setMinHour(policyHasTblVehicleType.getMinHour());
            List<Pricing> pricings = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleType.getId());
            return pricings;
        }
        return null;
    }

    @Transactional
    public Optional<Order> checkOutOrder(Order order, String userToken, User user) {
        order.setCheckOutDate(new Date().getTime());
        TimeDuration duration = TimeService.compareTwoDates(order.getCheckInDate(), order.getCheckOutDate());
        //TODO kiểm tra có bị lố giờ để phạt tiền thêm
        //code here

        double totalPrice = 0;
        int totalHour = duration.getHour();
        int totalMinute = duration.getMinute();
        List<HourHasPrice> hourHasPrices = new ArrayList<>();

        if (totalHour < order.getMinHour()) {
            totalHour = order.getMinHour();
            totalMinute = 0;
        }

        while (totalHour > 0) {
            hourHasPrices.add(new HourHasPrice(totalHour, null));
            totalHour--;
        }

        List<OrderPricing> orderPricings = orderPricingRepository.findByOrderId(order.getId());
        double lastPrice = 0;
        for (OrderPricing orderPricing : orderPricings) {
            if (orderPricing.getPricePerHour() > lastPrice) {
                lastPrice = orderPricing.getPricePerHour();
            }
            for (HourHasPrice hourHasPrice : hourHasPrices) {
                if (orderPricing.getFromHour() < hourHasPrice.getHour()) {
                    hourHasPrice.setPrice(orderPricing.getPricePerHour());
                }
            }
        }

        for (HourHasPrice hourHasPrice : hourHasPrices) {
            totalPrice += hourHasPrice.getPrice();
        }
        totalPrice += lastPrice * ((double) totalMinute / 60);

        order.setDuration(duration.toMilisecond() / 1000);
        order.setTotal(round(totalPrice, 0));
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Close.getName()).get();
        order.setOrderStatusId(orderStatus);
        orderRepository.save(order);
        user.setMoney(user.getMoney() - totalPrice);
        userRepository.save(user);
        sendNotification(user, order, userToken, orderPricings, NotificationEnum.CHECK_OUT);

        return Optional.of(order);
    }

    public void sendNotification(User user, Order order, String userToken, List<OrderPricing> orderPricings, NotificationEnum notification) {
        if (user.getSmsNoti()) {
            order.setOrderPricings(orderPricings);
            PushNotificationService.sendNotificationToSendSms(NFCServerProperties.getSmsHostToken(), notification, order);
        } else {
            order.setOrderPricings(orderPricings);
            PushNotificationService.sendNotification(userToken, notification, order.getId());
        }
    }

    @Autowired
    private EntityManager entityManager;

    public ResponseObject getOrders(int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from);
        select.orderBy(builder.desc(from.get("checkInDate")), builder.desc(from.get("checkOutDate")));
        TypedQuery<Order> typedQuery = entityManager.createQuery(select);

        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<Order> orders = typedQuery.getResultList();

        responseObject.setData(orders);
        responseObject.setPageNumber(pagNumber);
        int totalPages = getTotalOrders(pageSize).intValue();
        responseObject.setTotalPages(totalPages);
        return responseObject;
    }

    public Long getTotalOrders(int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(
                countQuery.from(Order.class)));
        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();
        return (long) (count / pageSize) + 1;
    }

    public ResponseObject filterOrders(SearchCriteria param, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root r = query.from(Order.class);

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
            Object type = r.get(param.getKey()).getJavaType();
            if (type == String.class) {
                predicate = builder.and(predicate,
                        builder.like(r.get(param.getKey()),
                                "%" + param.getValue() + "%"));
            } else if (type == Location.class) {
                Join<Order, Location> join = r.join("locationId");
                Predicate locationNamePredicate = builder.like(join.get("location"), "%" + param.getValue() + "%");
                predicate = builder.and(predicate, locationNamePredicate);
            } else if (type == OrderStatus.class) {
                Join<Order, Location> join = r.join("orderStatusId");
                Predicate locationNamePredicate = builder.like(join.get("name"), param.getValue() + "%");
                predicate = builder.and(predicate, locationNamePredicate);
            } else {
                predicate = builder.and(predicate,
                        builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }
        query.where(predicate);
        query.orderBy(builder.desc(r.get("checkInDate")), builder.desc(r.get("checkOutDate")));
        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        List<Order> orders = typedQuery.getResultList();
        int totalPages = orders.size() / pageSize;
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Order> orderList = typedQuery.getResultList();
        responseObject.setData(orderList);
        responseObject.setTotalPages(totalPages + 1);
        responseObject.setPageNumber(pagNumber);
        return responseObject;
    }

    public List<Order> findOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCheckInDateDesc(userRepository.findById(userId).get());

        for (Order order : orders) {
            order.getUserId().setVehicle(
                    vehicleRepository.findByVehicleNumber(order.getUserId().getVehicleNumber()).get()
            );
            order.setOrderPricings(orderPricingRepository.findByOrderId(order.getId()));
        }
        return orders;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
