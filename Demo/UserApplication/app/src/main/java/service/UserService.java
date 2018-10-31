package service;

import android.app.ProgressDialog;
import android.content.Context;

import java.text.SimpleDateFormat;

public class UserService {

    public static String convertMoney(double money) {
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
        result = result + " vnđ";
        return result;
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
}
