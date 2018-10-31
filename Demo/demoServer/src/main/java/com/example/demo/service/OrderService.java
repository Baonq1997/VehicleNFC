package com.example.demo.service;

import com.example.demo.Config.*;
import com.example.demo.entity.*;
import com.example.demo.entity.Order;
import com.example.demo.model.HourHasPrice;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PricingRepository pricingRepository;
    private final VehicleRepository vehicleRepository;
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;
    private final LocationService locationService;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, UserRepository userRepository, LocationRepository locationRepository, OrderPricingRepository orderPricingRepository, PricingRepository pricingRepository, VehicleRepository vehicleRepository, PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository, LocationService locationService) {
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.orderPricingRepository = orderPricingRepository;
        this.pricingRepository = pricingRepository;
        this.vehicleRepository = vehicleRepository;
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
        this.locationService = locationService;
    }

    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            if (order.get().getCheckOutDate() != null) {
                TimeDuration duration = TimeService.compareTwoDates(order.get().getCheckInDate(), order.get().getCheckOutDate());

                int totalHour = duration.getHour();
                int totalMinute = duration.getMinute();

                List<HourHasPrice> hourHasPrices = new ArrayList<>();

                // duration < getMInHour
                if (totalHour < order.get().getMinHour()) {
                    totalHour = order.get().getMinHour();
                    totalMinute = 0;
                }

                // Add hour
                while (totalHour > 0) {
                    hourHasPrices.add(new HourHasPrice(totalHour, null));
                    totalHour--;
                }

                List<OrderPricing> orderPricings = orderPricingRepository.findByOrderId(order.get().getId());
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
                List<HourHasPrice> hourHasPriceList = new ArrayList<>();
                for (HourHasPrice hourHasPrice : hourHasPrices) {
                    if (hourHasPriceList.size() < 1) {
                        hourHasPrice.setTotal(hourHasPrice.getPrice());
                        hourHasPriceList.add(hourHasPrice);
                    } else {
                        HourHasPrice tmp = hourHasPriceList.get(hourHasPriceList.size() - 1);
                        if ((Double.compare(tmp.getPrice(), hourHasPrice.getPrice()) == 0)) {
                            if (tmp.getTotal() == null) {
                                tmp.setTotal(hourHasPrice.getPrice());
                            } else {
                                tmp.setTotal(tmp.getTotal() + hourHasPrice.getPrice());
                            }
                        } else {
                            hourHasPrice.setTotal(hourHasPrice.getPrice());
                            hourHasPriceList.add(hourHasPrice);
                        }
                    }
                }
                hourHasPriceList = HourHasPrice.sort(hourHasPriceList);
                hourHasPriceList.get(hourHasPriceList.size() - 1).setMinutes(totalMinute);
                order.get().setHourHasPrices(hourHasPriceList);
            }
        }
        return order;
    }

    public Optional<Order> getOpenOrderByUserId(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Optional<Order> order = orderRepository.findFirstByUserIdAndOrderStatusId(user.get()
                    , orderStatusRepository.findByName(OrderStatusEnum.Open.getName()).get());
            if (order.isPresent()) {
                order.get().getUserId().setVehicle(
                        vehicleRepository.findByVehicleNumber(order.get().getUserId().getVehicleNumber()).get()
                );
                order.get().setOrderPricingList(orderPricingRepository.findByOrderId(order.get().getId()));
            }
            return order;
        }
        return null;
    }

    @Transactional
    public Optional<Order> createOrder(User checkInUser, Location location, Map<String, String> userTokenList) {
        checkInUser = userRepository.findById(checkInUser.getId()).get();
        location = locationService.getMeterById((location.getId())).get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Open.getName()).get();

        Order order = null;
        try {
            order = orderRepository.findByUserIdAndLocationIdAndOrderStatusId(checkInUser,
                    location, orderStatus).get();
        } catch (NoSuchElementException e) {
        }

        if (order != null) {
            checkOutOrder(order, userTokenList, checkInUser);

            return Optional.of(order);
        }

        order = new Order();
        order.setOrderStatusId(orderStatus);

        Vehicle vehicle = vehicleRepository.findByVehicleNumber(checkInUser.getVehicleNumber()).get();
        order.setVehicleTypeId(vehicle.getVehicleTypeId());

        order.setUserId(checkInUser);
        order.setLocationId(locationRepository.findById(location.getId()).get());

        order.setCheckInDate(new Date().getTime());

        //TODO kiểm tra thời điểm hiện tại để chọn policy
        PolicyInstance policy = order.getLocationId().getPolicyInstanceList().get(0);

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
        sendNotification(checkInUser, order, userTokenList, orderPricings, NotificationEnum.CHECK_IN);
        return Optional.of(order);
    }

    public List<Pricing> getPricingList(Order order, User user) {
        List<PolicyInstance> policies = order.getLocationId().getPolicyInstanceList();
        List<PolicyInstance> matchPolicies = new ArrayList<>();
        for (PolicyInstance policy : policies) {
            if (policy.getAllowedParkingFrom() < order.getCheckInDate()
                    && policy.getAllowedParkingTo() > order.getCheckInDate()) {
                matchPolicies.add(policy);
            }
        }
        PolicyInstance choosedPolicy = null;
        PolicyInstanceHasTblVehicleType policyHasTblVehicleType = null;
        for (PolicyInstance policy : matchPolicies) {
            while (choosedPolicy == null) {
                Vehicle vehicle = vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get();
                policyHasTblVehicleType = policyInstanceHasVehicleTypeRepository
                        .findByPolicyInstanceIdAndVehicleTypeId(policy.getId(), vehicle.getVehicleTypeId()).get();
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
            List<Pricing> pricings = policyHasTblVehicleType.getPricingList();
            return pricings;
        }
        return null;
    }

    @Transactional
    public Optional<Order> checkOutOrder(Order order, Map<String, String> userToken, User user) {
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

        order.setDuration(order.getCheckOutDate() - order.getCheckInDate());
        order.setTotal(round(totalPrice, 0));
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Close.getName()).get();
        order.setOrderStatusId(orderStatus);
        orderRepository.save(order);
        user.setMoney(user.getMoney() - totalPrice);
        userRepository.save(user);
        sendNotification(user, order, userToken, orderPricings, NotificationEnum.CHECK_OUT);

        return Optional.of(order);
    }


    public void sendNotification(User user, Order
            order, Map<String, String> userToken, List<OrderPricing> orderPricings, NotificationEnum notification) {
        order.setOrderPricingList(orderPricings);
        if (user.getSmsNoti()) {
            PushNotificationService.sendNotificationToSendSms(NFCServerProperties.getSmsHostToken(), notification, order);
        } else {
            if (userToken != null) {
                PushNotificationService.sendNotification(userToken.get(user.getPhoneNumber()), notification, order.getId());
            }
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
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            User user = order.getUserId();
            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get());
            order.setUserId(user);
            result.add(order);
        }
        responseObject.setData(result);
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

    public ResponseObject filterOrders(List<SearchCriteria> params, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root r = query.from(Order.class);

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
                if (param.getKey().equalsIgnoreCase("vehicleNumber")) {
                    // do vehicleNumber la object nam trong User
                    type = User.class;
                } else {
                    type = r.get(param.getKey()).getJavaType();
                }
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
                } else if (type == long.class) {
                    // search in range between SearchDay 0h0p0s and searchDay 23h59p
                    Long endOfDay = Long.parseLong("86340000"); // 23h59p
                    predicate = builder.and(predicate, builder.between(r.get(param.getKey()), (long) param.getValue(), (long) param.getValue() + endOfDay));
                } else if (type == User.class) {
                    if (param.getKey().equalsIgnoreCase("vehicleNumber")) {
                        Join<Order, User> join = r.join("userId");
                        Predicate vehiclePredicate = builder.equal(join.get("vehicleNumber"), param.getValue());
                        predicate = builder.and(predicate, vehiclePredicate);
                    }


                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
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
//        List<Order> result = new ArrayList<>();
        for (Order order : orderList) {
            User user = order.getUserId();
            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get());
            order.setUserId(user);
//            result.add(order);
        }
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
            order.setOrderPricingList(orderPricingRepository.findByOrderId(order.getId()));
        }
        return orders;
    }

    @Transactional
    public void refundOrder(Order order, User user, Double refundMoney) {
        double lastRefund = 0;
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Refund.getName()).get();
        Order orderDB = orderRepository.findById(order.getId()).get();
        orderDB.setOrderStatusId(orderStatus);
        //Todo
//        if (orderDB.getRefund() != null) {
//            lastRefund = orderDB.getRefund();s
//        }
//        orderDB.setRefund(refundMoney);
        orderRepository.save(orderDB);

        User userDB = userRepository.findById(user.getId()).get();
        double currentMoney = userDB.getMoney();
        userDB.setMoney(currentMoney + refundMoney - lastRefund);
        userRepository.save(userDB);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
