package com.example.demo.service;

import com.example.demo.model.TimeDuration;

import java.time.Duration;
import java.time.Instant;

public class TimeService {

    public static TimeDuration compareTwoDates(long dateA, long dateB){
        Instant start = Instant.ofEpochMilli(dateA);
        Instant end = Instant.ofEpochMilli(dateB);

        Duration duration = Duration.between(start, end);
        Integer hours = ((Long) duration.toHours()).intValue();
        int minutes = (int) (duration.getSeconds() % 60);

        return new TimeDuration(hours,minutes,null);
    }
}

