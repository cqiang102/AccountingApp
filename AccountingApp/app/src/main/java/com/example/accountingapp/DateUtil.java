package com.example.accountingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//时间日期转换工具类
public class DateUtil {

    //unix time -> 11:11

    public static String getFormattedTime(long timeStamp){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(timeStamp));
    }

    public static  String getFormattedDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
    public static  Date srt2Date(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getWeekDay(String date){
        String[] weekdays = {"星期一","星期二","星期三","星期四","星期五","星期六","星期天"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(srt2Date(date));
        int index = calendar.get(Calendar.DAY_OF_WEEK) -1;
        return weekdays[index];
    }
    public static String getDateTitle(String date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(srt2Date(date));
        int index = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return  index + " 月 " +day +" 日";
    }
}
