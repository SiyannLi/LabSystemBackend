package com.example.LabSystemBackend.jwt;

import java.util.Calendar;
import java.util.Date;
/**
 * @version 1.0
 * @author Cong Liu
 *
 * Date util
 */
public class DateUtil {
    public static Date getNextMinute(Date date, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date getNextSecond(Date date, int second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }
}
