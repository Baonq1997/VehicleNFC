package com.example.demo.service;

public class UtilityService {
    public static String encodeGenerator() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (int) (Math.random() * (9)) + "";
        }
        return result;
    }
}
