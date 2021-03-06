package com.example.Bama.util;

import android.net.Uri;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.Base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.regex.Pattern;

public class StringUtil {

    // 几分钟前，几小时前，昨天，几天前，一年前，仿照微信的展现形式
    public static String getTimeLineTime(String time) {
        StringBuilder sb = new StringBuilder();
        long mss = System.currentTimeMillis() - Long.parseLong(time);
        long oneMinute = 60 * 1000;
        long oneHour = oneMinute * 60;
        Calendar calendar = Calendar.getInstance();
        int intHour = calendar.get(Calendar.HOUR_OF_DAY);
        int intMinute = calendar.get(Calendar.MINUTE);
        int intSec = calendar.get(Calendar.SECOND);
        long yesterday = intHour * oneHour + intMinute * oneMinute + intSec * 1000;
        long oneDay = oneHour * 24;
        long oneYear = oneDay * 365;
        if (mss < oneMinute) {
            sb.append(HCApplication.getInstance().getString(R.string.oneMinAgo));
        } else if (mss < oneHour) {
            sb.append(mss / oneMinute).append(HCApplication.getInstance().getString(R.string.minsAgo));
        } else if (mss < yesterday) {
            sb.append(mss / oneHour).append(HCApplication.getInstance().getString(R.string.hoursAgo));
        } else if ((mss - yesterday) < oneDay) {
            sb.append(HCApplication.getInstance().getString(R.string.yestaday));
        } else if ((mss - yesterday) < oneYear) {
            sb.append((mss - yesterday) / oneDay).append(HCApplication.getInstance().getString(R.string.daysAgo));
        } else {
            sb.append((mss - yesterday) / oneYear).append(HCApplication.getInstance().getString(R.string.yearsAgo));
        }
        return sb.toString();
    }

    // 将时间转换成2012年08月12日 这种形式的字符串
    public static String getDateToString3(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append(HCApplication.getInstance().getString(R.string.year));
            sb.append(month).append(HCApplication.getInstance().getString(R.string.month)).append(day).append(HCApplication.getInstance().getString(R.string.day));
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }

    //return such as 2015/06/13
    public static String getDateToString1(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append("/");
            if (month >= 10) {
                sb.append(month);
            } else {
                sb.append("0").append(month);
            }
            sb.append("/");
            if (day >= 10) {
                sb.append(day);
            } else {
                sb.append("0").append(day);
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }

    //return such as 2015-06-13 09:40:40
    public static String getDateToString2(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append("-");
            if (month >= 10) {
                sb.append(month);
            } else {
                sb.append("0").append(month);
            }
            sb.append("-");
            if (day >= 10) {
                sb.append(day);
            } else {
                sb.append("0").append(day);
            }
            sb.append(" ");
            if (hour >= 10) {
                sb.append(hour);
            } else {
                sb.append("0").append(hour);
            }
            sb.append(":");
            if (min >= 10) {
                sb.append(min);
            } else {
                sb.append("0").append(min);
            }
            sb.append(":");
            if (sec >= 10) {
                sb.append(sec);
            } else {
                sb.append("0").append(sec);
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }

    public static String getUrlInfo(String url){
        String urlStr = null;
        try {
            urlStr = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Uri.parse(urlStr).getQueryParameter("ClientOperation");

    }
}
//http://dev.8mbang.com/app/?ClientOperation={"jump":"group","break":1,"jumpindex":0}