package com.zhao.fanweather.unit;

/**
 * Created by Zhao on 2016/5/6.
 */
public class WeekTool {
    public static String transform(String str) {
        if (str.contains("天"))
            return "周日";
        if (str.contains("一"))
            return "周一";
        if (str.contains("二"))
            return "周二";
        if (str.contains("三"))
            return "周三";
        if (str.contains("四"))
            return "周四";
        if (str.contains("五"))
            return "周五";
        if (str.contains("六"))
            return "周六";
        return null;
    }
}
