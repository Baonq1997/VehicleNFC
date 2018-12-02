package com.example.demo.component.order;

import com.example.demo.component.location.Location;
import com.example.demo.component.location.LocationRepository;
import com.example.demo.component.policy.*;
import com.example.demo.component.user.User;
import com.example.demo.component.user.UserRepository;
import com.example.demo.component.vehicle.Vehicle;
import com.example.demo.component.vehicle.VehicleRepository;
import com.example.demo.config.*;
import com.example.demo.model.HourHasPrice;
import com.example.demo.model.TimeDuration;
import com.example.demo.component.location.LocationService;
import com.example.demo.service.PushNotificationService;
import com.example.demo.service.TimeService;
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
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;
    private final LocationService locationService;

    public OrderService(OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, UserRepository userRepository, LocationRepository locationRepository, OrderPricingRepository orderPricingRepository, PricingRepository pricingRepository, VehicleRepository vehicleRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository, LocationService locationService) {
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.orderPricingRepository = orderPricingRepository;
        this.pricingRepository = pricingRepository;
        this.vehicleRepository = vehicleRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
        this.locationService = locationService;
    }

    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
//            order.get().setLocation(locationRepository.findById(order.get().getLocationId()).get());
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
                order.get().setOrderPricingList(orderPricings);
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
        if (id != null) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                Optional<Order> order = orderRepository.findFirstByUserIdAndOrderStatusId(user.get()
                        , orderStatusRepository.findByName(OrderStatusEnum.Open.getName()).get());
                if (order.isPresent()) {
//                    order.get().setLocation(locationRepository.findById(order.get().getLocationId()).get());
                    order.get().setOrderPricingList(orderPricingRepository.findByOrderId(order.get().getId()));
                }
                return order;
            }
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
                    location.getId(), orderStatus).get();
        } catch (NoSuchElementException e) {
        }

        if (order != null) {
            order.setLocation(location);
            checkOutOrder(order, userTokenList, checkInUser);
            return Optional.of(order);
        }

        order = new Order();
        order.setLocation(location);
        order.setOrderStatusId(orderStatus);

//        Vehicle vehicle = vehicleRepository.findByVehicleNumber(checkInUser.getVehicleNumber()).get();
        Vehicle vehicle = checkInUser.getVehicle();
        order.setVehicle(vehicle);

        order.setUserId(checkInUser);
//        order.setLocationId(locationRepository.findById(location.getId()).get().getId());
        order.setLocation(locationRepository.findById(location.getId()).get());

        order.setCheckInDate(new Date().getTime());

        //TODO kiểm tra thời điểm hiện tại để chọn policy
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
        List<Policy> policies = order.getLocation().getPolicyList();
        List<Policy> matchPolicies = new ArrayList<>();
        for (Policy policy : policies) {
            if (!isOutOfTheLine(order.getCheckInDate(), policy.getAllowedParkingFrom(), policy.getAllowedParkingTo())) {
                matchPolicies.add(policy);
            }
        }
        Policy choosedPolicy = null;
//        PolicyHasTblVehicleType policyHasTblVehicleType = null;
        for (Policy policy : matchPolicies) {
            while (choosedPolicy == null) {
                Vehicle vehicle = user.getVehicle();
                List<PolicyHasTblVehicleType> policyHasTblVehicleTypes =
                        policy.getPolicyHasTblVehicleTypes();
                if (vehicle == null) {
                    break;
                }
                for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
                    if (policyHasTblVehicleType.getVehicleTypeId().getId() == user.getVehicle().getVehicleTypeId().getId()) {
                        order.setAllowedParkingFrom(policy.getAllowedParkingFrom());
                        order.setAllowedParkingTo(policy.getAllowedParkingTo());
                        order.setMinHour(policyHasTblVehicleType.getMinHour());
                        List<Pricing> pricings = policyHasTblVehicleType.getPricingList();
                        return pricings;
                    }
                }
            }
        }
        return null;
    }

    @Transactional
    public Optional<Order> checkOutOrder(Order order, Map<String, String> userToken, User user) {
        order.setCheckOutDate(new Date().getTime());

//        List<OrderPricing> orderPricings = orderPricingRepository.findByOrderId(order.getId());
        List<OrderPricing> orderPricings = order.getOrderPricingList();
        List<HourHasPrice> hourHasPrices = composeHourPrice(new Date().getTime() - order.getCheckInDate()
                , order.getCheckInDate(), order.getAllowedParkingFrom(), order.getAllowedParkingTo(), order.getMinHour(), orderPricings);

        double totalPrice = 0;
        for (HourHasPrice hourHasPrice : hourHasPrices) {
            double money = (hourHasPrice.isLate()) ? hourHasPrice.getFine() : hourHasPrice.getPrice();
            if (hourHasPrice.isFullHour()) {
                totalPrice += money;
            } else {
                totalPrice += Math.ceil(money * ((double) hourHasPrice.getMinutes() / 60));
            }
        }

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
        if (user.getSmsNoti() != null) {
            if (user.getSmsNoti()) {
                PushNotificationService.sendNotificationToSendSms(NFCServerProperties.getSmsHostToken(), notification, order);
            } else {
                if (userToken != null) {
                    PushNotificationService.sendNotification(userToken.get(user.getPhoneNumber()), notification, order.getId());
                }
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
//            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get());
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
                    Join<Order, OrderStatus> join = r.join("orderStatusId");
                    Predicate locationNamePredicate = builder.like(join.get("name"), param.getValue() + "%");
                    predicate = builder.and(predicate, locationNamePredicate);
                } else if (type == long.class) {
                    // search in range between SearchDay 0h0p0s and searchDay 23h59p
                    Long endOfDay = Long.parseLong("86340000"); // 23h59p
                    predicate = builder.and(predicate, builder.between(r.get(param.getKey()), (long) param.getValue(), (long) param.getValue() + endOfDay));
                } else if (type == User.class) {
                    if (param.getKey().equalsIgnoreCase("vehicleNumber")) {
                        Join<Order, Vehicle> join = r.join("vehicle");
                        Predicate vehiclePredicate = builder.like(join.get("vehicleNumber"), "%" + param.getValue() + "%");
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
        int totalPages = (int) Math.ceil((double) orders.size() / pageSize);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Order> orderList = typedQuery.getResultList();
//        List<Order> result = new ArrayList<>();
        for (Order order : orderList) {
            User user = order.getUserId();
//            user.setVehicle(vehicleRepository.findByVehicleNumber(user.getVehicleNumber()).get());
            order.setUserId(user);
//            order.setLocation(locationRepository.findById(order.getLocationId()).get());
//            result.add(order);
        }
        responseObject.setData(orderList);
        responseObject.setPageSize(pageSize);
        responseObject.setTotalPages(totalPages);
        responseObject.setPageNumber(pagNumber);
        return responseObject;
    }

    public List<Order> findOrdersByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (user.get().getActivated() && user.get().getVehicle() != null) {
                List<Order> orders = orderRepository.findByUserIdAndVehicleOrderByCheckInDateDesc(user.get(), user.get().getVehicle());
                for (Order order : orders) {
//            order.getUserId().setVehicle(
//                    vehicleRepository.findByVehicleNumber(order.getUserId().getVehicle()).get()
//            );
//                    order.setLocation(locationRepository.findById(order.getLocationId()).get());
                    order.setOrderPricingList(orderPricingRepository.findByOrderId(order.getId()));
                }
                return orders;
            }
        }
        return null;

    }

//    @Transactional
//    public void refundOrder(Order order, User user, Double refundMoney) {
//        double lastRefund = 0;
//        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.Refund.getName()).get();
//        Order orderDB = orderRepository.findById(order.getId()).get();
//        orderDB.setOrderStatusId(orderStatus);
//        //Todo
////        if (orderDB.getRefund() != null) {
////            lastRefund = orderDB.getRefund();s
////        }
////        orderDB.setRefund(refundMoney);
//        orderRepository.save(orderDB);
//
//        User userDB = userRepository.findById(user.getId()).get();
//        double currentMoney = userDB.getMoney();
//        userDB.setMoney(currentMoney + refundMoney - lastRefund);
//        userRepository.save(userDB);
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static List<HourHasPrice> composeHourPrice(long duration, long startTime
            , long limitFromTime, long limitToTime, int minHour, List<OrderPricing> pricings) {
        int totalHour = (int) (duration / 3600000);
        int totalMinute = (int) (duration - totalHour * 3600000) / 60000;
        List<HourHasPrice> hourHasPrices = new ArrayList<>();
//        startTime += duration;
        if (totalHour < minHour) {
            totalHour = minHour;
            totalMinute = 0;
        }

        boolean foreverLate = false;
        for (int i = 1; i <= totalHour; i++) {
            if (i == totalHour && totalMinute != 0) {
                startTime += totalMinute * 60000;
                HourHasPrice notFull = new HourHasPrice(i, null);
                notFull.setFullHour(false);
                notFull.setMinutes(totalMinute);
                if (isOutOfTheLine(startTime, limitFromTime, limitToTime)) {
                    foreverLate = true;
                }
                notFull.setLate(foreverLate);
                hourHasPrices.add(notFull);
                totalMinute = 0;
            }
            startTime += 3600000;
            HourHasPrice hourHasPrice = new HourHasPrice(i, null);
            if (isOutOfTheLine(startTime, limitFromTime, limitToTime)) {
                foreverLate = true;
            }
            hourHasPrice.setLate(foreverLate);
            hourHasPrices.add(hourHasPrice);
        }

        for (
                OrderPricing orderPricing : pricings)

        {
            for (HourHasPrice hourHasPrice : hourHasPrices) {
                if (orderPricing.getFromHour() < hourHasPrice.getHour()) {
                    hourHasPrice.setPrice(orderPricing.getPricePerHour());
                    if (hourHasPrice.isLate()) {
                        hourHasPrice.setFine(orderPricing.getLateFeePerHour());
                    }
                }
            }
        }

        return hourHasPrices;
    }

    public static boolean isOutOfTheLine(long current, long limitFrom, long limitTo) {
        Calendar cur = Calendar.getInstance(), from = Calendar.getInstance(), to = Calendar.getInstance();
        cur.setTimeInMillis(current);
        from.setTimeInMillis(limitFrom);
        to.setTimeInMillis(limitTo);
//        String a = cur.get(Calendar.HOUR_OF_DAY)+":"+cur.get(Calendar.MINUTE);
//        String b = from.get(Calendar.HOUR_OF_DAY)+":"+from.get(Calendar.MINUTE);
//        String c = to.get(Calendar.HOUR_OF_DAY)+":"+to.get(Calendar.MINUTE);
        if (cur.get(Calendar.HOUR_OF_DAY) < from.get(Calendar.HOUR_OF_DAY)
                || cur.get(Calendar.HOUR_OF_DAY) > to.get(Calendar.HOUR_OF_DAY)) {
            return true;
        }
        if (cur.get(Calendar.HOUR_OF_DAY) == from.get(Calendar.HOUR_OF_DAY)
                || cur.get(Calendar.HOUR_OF_DAY) == to.get(Calendar.HOUR_OF_DAY)) {
            if (cur.get(Calendar.MINUTE) < from.get(Calendar.MINUTE)
                    || cur.get(Calendar.MINUTE) > to.get(Calendar.MINUTE)) {
                return true;
            }
        }
        return false;
    }
}


