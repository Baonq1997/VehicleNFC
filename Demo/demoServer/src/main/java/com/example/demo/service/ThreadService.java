package com.example.demo.service;

import com.example.demo.component.order.Order;
import com.example.demo.component.order.OrderService;
import com.example.demo.component.user.User;
import com.example.demo.component.user.UserService;
import com.example.demo.config.NotificationEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.util.*;

public class ThreadService extends Thread {
    private final int[] limit = {1800000, 3600000};
    private final int passHour = 5;

    private OrderService orderService;
    private UserService userService;

    private static Map<String, String> registerTokenList;

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static Map<String, String> getRegisterTokenList() {
        return registerTokenList;
    }

    public static void setRegisterTokenList(Map<String, String> registerTokenList) {
        ThreadService.registerTokenList = registerTokenList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<Order> openingOrders = new ArrayList<>();
                List<User> users = userService.getAllUser();
                for (User user : users) {
                    Optional<Order> order = orderService.getOpenOrderByUserId(user.getId());
                    if (order.isPresent()) {
                        openingOrders.add(order.get());
                    }
                }
                if (openingOrders.size() > 0) {
                    System.err.println("Number of Order: " + openingOrders.size());
                    for (Order order : openingOrders) {
                        Calendar cur = Calendar.getInstance(), to = Calendar.getInstance(), in = Calendar.getInstance();
                        cur.setTimeInMillis(new Date().getTime());
                        in.setTimeInMillis(order.getCheckInDate());
                        to.setTimeInMillis(order.getAllowedParkingTo());

                        int differenceDates = cur.get(Calendar.DAY_OF_YEAR) - in.get(Calendar.DAY_OF_YEAR);
                        int bonus = 86400000 * differenceDates;

                        int curMili = cur.get(Calendar.HOUR_OF_DAY) * 3600000 + cur.get(Calendar.MINUTE) * 60000 + bonus;
                        int toMili = to.get(Calendar.HOUR_OF_DAY) * 3600000 + to.get(Calendar.MINUTE) * 60000;

                        if ((curMili - toMili) % (passHour * 3600000) == 0) {
                            System.err.println("Order: " + order.getId() + ", user phone: " + order.getUserId().getPhoneNumber() + ", is late");
                            OrderService.sendNotification(order.getUserId(), order, registerTokenList, null, NotificationEnum.LATE_5_HOUR);
                        } else if (toMili - curMili < 0) {
                            System.err.println("Order: " + order.getId() + ", user phone: " + order.getUserId().getPhoneNumber() + ", is " + TimeService.miliSecondToHour(curMili - toMili) + "h late");
                            OrderService.sendNotification(order.getUserId(), order, registerTokenList, null, NotificationEnum.LATE_5_HOUR);
                        } else if (toMili - curMili < limit[0]) {
                            System.err.println("Order: " + order.getId() + ", user phone: " + order.getUserId().getPhoneNumber() + ", 30m to deadline");
                            OrderService.sendNotification(order.getUserId(), order, registerTokenList, null, NotificationEnum.LATE_FULL_HOUR);
                        } else if (toMili - curMili < limit[1]) {
                            System.err.println("Order: " + order.getId() + ", user phone: " + order.getUserId().getPhoneNumber() + ", 1h to deadline");
                            OrderService.sendNotification(order.getUserId(), order, registerTokenList, null, NotificationEnum.LATE_HALF_HOUR);

                        }
                    }
                }
                this.sleep(30000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
