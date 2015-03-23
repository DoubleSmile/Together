package com.whiteblue.beetl;

import com.jfinal.kit.StringKit;
import org.beetl.core.Context;
import org.beetl.core.Function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WhiteBlue on 15/1/29.
 */
public class PrintTimeFunction implements Function {
    private final static SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat MY_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    public static String getNiceDate(Object o) {
        if (null == o) return "";
        String dateString = o.toString();
        String result = null;
        try {
            Date date = TIME_STAMP_FORMAT.parse(dateString);
            long currentTime = new Date().getTime() - date.getTime();
            int time = (int) (currentTime / 1000);
            if (time < 60) {
                result = "刚刚";
            } else if (time >= 60 && time < 3600) {
                result = time / 60 + "分钟前";
            } else if (time >= 3600 && time < 86400) {
                result = time / 3600 + "小时前";
            } else if (time >= 86400 && time < 864000) {
                result = time / 86400 + "天前";
            } else {
                result = MY_DATE_FORMAT.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isOverTime(Object o, int day) {
        String dateString = o.toString();
        try {
            Date date = TIME_STAMP_FORMAT.parse(dateString);
            long between = new Date().getTime() - date.getTime();
            int time = (int) (between / 1000);
            if ((time / 86400) >= day) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;


    }

    @Override
    public String call(Object[] params, Context context) {
        if (params.length != 1) {
            throw new RuntimeException("length of params must be 1 !");
        }
        if (StringKit.isBlank(params[0].toString())) {
            return null;
        }
        String dateString = params[0].toString();
        return getNiceDate(dateString);
    }
}
