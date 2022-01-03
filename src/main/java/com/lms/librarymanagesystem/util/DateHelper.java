package com.lms.librarymanagesystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    //获取上传图片名
    public static String getImageName(){
        SimpleDateFormat  sdf= new SimpleDateFormat("yyyyMMddHHmmssS");
        return sdf.format(new Date());
    }

    //对日期进行加减操作
    public static java.sql.Date getNewDate(java.sql.Date date , long amount){
        long mills = date.getTime();
        mills += amount*24*60*60*1000;
        java.sql.Date backDate = new java.sql.Date(mills);
        return backDate;
    }

    //计算两个时间差
    public static int getSpan(Date  date01,Date date02){
        long span = date01.getTime()-date02.getTime();
        int day = (int)(span/1000/60/60/24);
        return Math.abs(day);

    }

    //测试
    public static void main(String[] args) {
        System.out.println(getImageName());
        java.sql.Date  date01 =  java.sql.Date.valueOf("2021-10-01");
        java.sql.Date  date02 = getNewDate(date01,31);
        System.out.println(date01+","+date02);

    }
}
