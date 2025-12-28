package com.example.demo.util;

import java.time.Duration;
import java.time.LocalTime;

public class TimeUtils {

    public static long minutesBetween(LocalTime start, LocalTime end) {
        return Duration.between(start, end).toMinutes();
    }
}
