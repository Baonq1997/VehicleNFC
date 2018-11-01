package com.example.demo.service;

import com.example.demo.component.order.Order;
import com.example.demo.component.order.OrderPricing;
import com.example.demo.config.NotificationEnum;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;;

import org.json.JSONObject;

@Service
public class PushNotificationService {

    private static final String FIREBASE_SERVER_KEY = "AAAALpE5CE0:APA91bHrRbW6K-dM9-ZdHvr2N2i7fGDNf0o-WDyXOY5NiJNNP0U7z5nusQ_2fmM-tGeMud-0AosXx9yIWPB_lEZIlVKvcFgvNSbaBIu3r4njl0CIhrgcsP9aBqhVKAce5O38j1MeQSkx";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    public static void sendNotification(String appToken, NotificationEnum notificationEnum, int orderId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
            httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
            JSONObject msg = new JSONObject();
            JSONObject json = new JSONObject();


            msg.put("title", notificationEnum.getTitle());
            msg.put("body", notificationEnum.getBody());
            msg.put("orderId", orderId);

            json.put("data", msg);

            json.put("to", appToken);

            try {
                HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
                String response = restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
                System.out.println(response);
                System.err.println("Send noti to: " + appToken);
            } catch (Exception e) {
                System.err.println("Can not send notification");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationToSendSms(String serverMachineToken, NotificationEnum notificationEnum, Order order) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
            httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
            JSONObject msg = new JSONObject();
            JSONObject json = new JSONObject();


            msg.put("title", notificationEnum.getTitle());
            msg.put("body", composeOrderPrice(order, notificationEnum.getTitle()));
            msg.put("phoneNumber", order.getUserId().getPhoneNumber());

            json.put("data", msg);

            json.put("to", "dBlmGBRMPtk:APA91bGUDwRK_1ZIIeh-e9X6bfrs3o-AXVxoK_e8K95YDM73gSr_lgx_fa8XEGZLbftx3-JP5aegE1Y93A7a2HXgneoOl4u2Qs77ZeOdmyDZ7czeuASweUlJUVMD_DE95FOKK0e6WhnF");

            try {
                HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
                String response = restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
            } catch (Exception e) {
                System.err.println("Can not send notification");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendPhoneConfirmNotification(String appToken, String phoneNumber, String confirmKey) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
            httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
            JSONObject msg = new JSONObject();
            JSONObject json = new JSONObject();

            msg.put("title", "Send confirm phone sms");
            msg.put("body", "Mã xác nhận là: " + confirmKey);
            msg.put("phoneNumber", phoneNumber);

            json.put("data", msg);

            json.put("to", "dBlmGBRMPtk:APA91bGUDwRK_1ZIIeh-e9X6bfrs3o-AXVxoK_e8K95YDM73gSr_lgx_fa8XEGZLbftx3-JP5aegE1Y93A7a2HXgneoOl4u2Qs77ZeOdmyDZ7czeuASweUlJUVMD_DE95FOKK0e6WhnF");


            HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
            String response = restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
            System.out.println(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String composeOrderPrice(Order order, String title) {
        String body = "";
        if (order != null) {
            if (title.equals(NotificationEnum.CHECK_IN.getTitle())) {
                String pattern = "HH:mm dd-MM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date(order.getCheckInDate()));

                body += "Bắt đầu đậu xe tại: " + order.getLocationId().getLocation() + "\n";
                body += "Vào lúc: " + date + "\n";
                body += "Bảng giá cho loại xe: " + order.getVehicleTypeId().getName() + "\n";
                for (OrderPricing orderPricing : order.getOrderPricingList()) {
                    body += (orderPricing.getFromHour() == 0) ? "Từ Giờ đầu: " + convertMoneyNoVND(orderPricing.getPricePerHour()) + "VNĐ/h\n"
                            : "Từ giờ thứ " + orderPricing.getFromHour() + ": " + convertMoneyNoVND(orderPricing.getPricePerHour()) + "VNĐ/h\n";
                }
            } else if (title.equals(NotificationEnum.CHECK_OUT.getTitle())) {
                String pattern = "HH:mm dd-MM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String checkInDate = simpleDateFormat.format(new Date(order.getCheckInDate()));
                String checkOutDate = simpleDateFormat.format(new Date(order.getCheckOutDate()));

                body += "Rời nơi đậu xe: " + order.getLocationId().getLocation() + "\n";
                body += "Thời gian đậu Từ: " + checkInDate + " đến: " + checkOutDate + "\n";
                body += "Phí đậu xe: " + convertMoneyNoVND(order.getTotal()) + "VNĐ\n";
            }
        }
        return body;
    }

    public static void sendUserNeedVerifyNotification(String appToken, String phoneNumber) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
            httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
            JSONObject msg = new JSONObject();
            JSONObject json = new JSONObject();


            msg.put("title", "User need verify");
            msg.put("body", "User need verify");
            msg.put("phoneNumber", phoneNumber);

            json.put("data", msg);

            json.put("to", "fhRoDKtJR4Q:APA91bFRKKjR2GydlMD0akn71EluhoayB7YXe3a9M5MVat1IRPGo-59onV4VmI-KLj3b-e0zQ2k55brMCxTGJPIcZK2eNslJMnTdq8BNecpqJwsDO5InyL-ALvF0ojQEb_PMtX_xtYsf");

            HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
            String response = restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
            System.out.println(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String convertMoneyNoVND(double money) {
        String base = (long) money * 1000 + "";
        String[] strings = base.split("");
        String result = "";
        int count = 0;
        for (int i = strings.length - 1; i > 0; i--) {
            count++;
            result = strings[i] + result;
            if (count == 3) {
                if (i > 1) {
                    result = "," + result;
                    count = 0;
                }
            }
        }
        return result;
    }
}
