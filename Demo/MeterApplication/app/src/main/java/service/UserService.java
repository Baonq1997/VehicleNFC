package service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Util.RmaAPIUtils;
import model.HourHasPrice;
import model.Location;
import model.OrderPricing;
import model.Policy;
import model.PolicyHasVehicleType;
import model.User;
import remote.RmaAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    public static String convertMoney(double money) {
        String base = (long) money + "";
        String[] strings = base.split("");
        String result = "";
        int count = 0;
        for (int i = strings.length - 1; i >= 0; i--) {
            count++;
            result = strings[i] + result;
            if (count == 3) {
                if (i > 1) {
                    result = "," + result;
                    count = 0;
                }
            }
        }
        result = result + "K VNĐ";
        return result;
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
        int bonus = 0;
        if (to.get(Calendar.HOUR_OF_DAY) < from.get(Calendar.HOUR_OF_DAY)
                || (to.get(Calendar.HOUR_OF_DAY) == from.get(Calendar.HOUR_OF_DAY))
                && to.get(Calendar.MINUTE) < from.get(Calendar.MINUTE)) {
            bonus = 24;
        }
        
        if (cur.get(Calendar.HOUR_OF_DAY) < from.get(Calendar.HOUR_OF_DAY)
                || cur.get(Calendar.HOUR_OF_DAY) > to.get(Calendar.HOUR_OF_DAY)) {
            return true;
        }
        if ((cur.get(Calendar.HOUR_OF_DAY) == from.get(Calendar.HOUR_OF_DAY) && cur.get(Calendar.MINUTE) < from.get(Calendar.MINUTE))
                || (cur.get(Calendar.HOUR_OF_DAY) == to.get(Calendar.HOUR_OF_DAY) + bonus && cur.get(Calendar.MINUTE) > to.get(Calendar.MINUTE))) {
            return true;
        }
        return false;
    }

    public static Policy findMatchedPolicy(User user, Location location) {
        long currentTime = new Date().getTime();
        List<Policy> policies = location.getPolicies();
        for (Policy policy : policies) {
            if (!UserService.isOutOfTheLine(currentTime, policy.getAllowedParkingFrom(), policy.getAllowedParkingTo())) {
                return policy;
            }
        }
        return null;
    }

    public static PolicyHasVehicleType findMatchedPolicyHasVehicleType(User user, Policy policy) {
        for (PolicyHasVehicleType policyHasVehicleType : policy.getPolicyHasVehicleTypes()) {
            if (policyHasVehicleType.getVehicleType().getId() == user.getVehicle().getVehicleTypeId().getId()) {
                return policyHasVehicleType;
            }
        }
        return null;
    }
}
