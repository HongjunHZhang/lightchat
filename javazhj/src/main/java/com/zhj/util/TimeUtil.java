package com.zhj.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 789
 */
public class TimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 获取日期（精确到毫秒）
     * @return 日期
     */
    public static String getSimpleTime(){
        return DATE_TIME_FORMATTER.format(getLocalDateTimeOfNow());
    }

    /**
     * 获取日期（精确到天）
     * @return 日期
     */
    public static String getSimpleDay(){
        return DATE_TIME_FORMATTER_DAY.format(getLocalDateTimeOfNow());
    }

    public static LocalDateTime getLocalDateTimeOfNow(){
        return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime getLocalDateTimeOfDate(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static String getZeroSimpleTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date zero = calendar.getTime();
        return DATE_TIME_FORMATTER.format(getLocalDateTimeOfDate(zero));
    }

    public static String getDay(String time,Long simpleTime){
        Long aLong = Long.parseLong(time);
        if (simpleTime - aLong > 1000000000){
            return time.substring(4,6)+"-"+time.substring(6,8);
        }else {
            return time.substring(8,10)+":"+time.substring(10,12);
        }
    }

    public static String getDayTime(String time,String simpleTime,Long lastTime){
        Long aLong = Long.parseLong(time);
        if (aLong - lastTime < 300000){
            return "0";
        }
        if (time.compareTo(simpleTime) < 0){
            return time.substring(4,6)+"-"+time.substring(6,8)+"  "+time.substring(8,10)+":"+time.substring(10,12);
        }else {
            return time.substring(8,10)+":"+time.substring(10,12);
        }
    }

    public static String getFormatDay(String simpleTime){
     return simpleTime.substring(0,4)+"-"+simpleTime.substring(4,6)+"-"+simpleTime.substring(6,8);
    }
    public static String getDayTime(String time,String simpleTime){
        if (time.compareTo(simpleTime) < 0){
            return time.substring(4,6)+"-"+time.substring(6,8)+"  "+time.substring(8,10)+":"+time.substring(10,12);
        }else {
            return time.substring(8,10)+":"+time.substring(10,12);
        }
    }

    public static String getDayTimeCurrent(String time,String simpleTime){
        if (time.compareTo(simpleTime) < 0){
            return time.substring(4,6)+"-"+time.substring(6,8);
        }else {
            return time.substring(8,10)+":"+time.substring(10,12);
        }
    }


}
