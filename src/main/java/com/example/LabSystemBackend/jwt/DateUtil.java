package com.example.LabSystemBackend.jwt;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date getNextMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
