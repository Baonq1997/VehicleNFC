package service;

import android.app.ProgressDialog;
import android.content.Context;

import com.paypal.android.sdk.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.HourHasPrice;
import model.OrderPricing;

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

    public static String convertMoneyNoVND(double money) {
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
        return result + "K";
    }

    //public static double convertVNDToUSD(double money) {
    //    return money / 23;
    //}

    public static String convertMoneyUSD(double money) {
        money = round(money, 2);
        String base = money + "";
        String tail = (base.split("\\.").length > 1) ? base.split("\\.")[1] : "";
        String[] strings = (base.split("\\.").length > 1) ? base.split("\\.")[0].split("") : base.split("");
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
        result = result + "." + tail + " USD";
        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static ProgressDialog setUpProcessDialog(Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Đang xử lý...");
        pd.setMessage("Xin chờ.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        return pd;
    }

    public static long convertToMilliseconds(int value, boolean isHour) {
        if (isHour) {
            return value * 3600000;
        } else {
            return value * 60000;
        }
    }

    public static boolean compareTwoDate(long a, long b) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
        return simpleDateFormat.format(a).equals(simpleDateFormat.format(b));
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
        String a = cur.get(Calendar.HOUR_OF_DAY) + ":" + cur.get(Calendar.MINUTE);
        String b = from.get(Calendar.HOUR_OF_DAY) + ":" + from.get(Calendar.MINUTE);
        String c = to.get(Calendar.HOUR_OF_DAY) + ":" + to.get(Calendar.MINUTE);
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
