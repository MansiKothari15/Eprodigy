package com.bacancy.eprodigy.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SCUtils {

    public static String validateStr(String str)
    {
        return TextUtils.isEmpty(str)?"":str.trim();
    }

    public static boolean isContainLatLng(String str) {


        Matcher matcher = null;
        try {
            Pattern pattern;
            final String STR_PATTERN = "([0-9]*\\.?[0-9]*°[0-9]*\\.?[0-9]*'[0-9]*\\.?[0-9]*\"[A-Z]\\s)*";
             //final String STR_PATTERN = "[0-9]*\\.?[0-9]*°[0-9]*\\.?[0-9]*'[0-9]*\\.?[0-9]*''[A-Z]";
            pattern = Pattern.compile(STR_PATTERN);
            matcher = pattern.matcher(str);
            Log.e("ad","isContainLatLng>"+(matcher.matches()?"TRUE":"FALSE"));
            return matcher.matches();
        } catch (Exception e) {



            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }public static boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }
        Log.e("ad","containsDigit>"+(containsDigit?"TRUE":"FALSE"));
        return containsDigit;
    }
    //use either formatted_date or time_ago (below)
    public static String formatted_date(String timestamp) {
        if (TextUtils.isEmpty(timestamp))
        {
            return "";
        }
//        timestamp = "2013-11-14 13:00:00";

        SimpleDateFormat sdfformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        // Calendar cal = Calendar.getInstance();
        // TimeZone tz = cal.getTimeZone();//get your local time zone.
        DateFormat sdfoutput = new SimpleDateFormat("hh:mma", Locale.getDefault()); //dd MMM yyyy KK:mma
        // sdfoutput.setTimeZone(tz);//set time zone.
        String localTime = "";
        try {
            Date date = sdfformate.parse(timestamp);
            localTime = sdfoutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

            return localTime.toLowerCase();
    }
    public  static  String getCurrentTimeStamp()
    {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(new Date());
    }


    //use either formatted_date or time_ago (below)
    public static String formatted_date_only(String timestamp) {

        SimpleDateFormat sdfformate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdfoutput = new SimpleDateFormat("dd MMM yyyy",Locale.getDefault()); //dd MMM yyyy KK:mma
        sdfoutput.setTimeZone(tz);//set time zone.
        String localTime = "";
        try {
            Date date = sdfformate.parse(timestamp);
            localTime = sdfoutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return localTime.toLowerCase();
    }

    public static long getDatetoLong(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }

    public static Date getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            return calendar.getTime();
        } catch (Exception e) {
        }
        return null;
    }

    public static String getNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String time_ago(long message_timestamp) {

        CharSequence xx = DateUtils.getRelativeTimeSpanString(message_timestamp * 1000L, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        return xx.toString();
    }
}
