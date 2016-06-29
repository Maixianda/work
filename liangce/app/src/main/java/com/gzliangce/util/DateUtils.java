package com.gzliangce.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by leo on 16/3/4.
 */
public class DateUtils {
    private static Calendar mCalendar;

    public static Calendar getCalendar() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        return mCalendar;
    }

    // 获取昨天时间的最小值：
    public static long getYesterdayMinTimeMillis(long mHourInMillis) {
        long currTime = System.currentTimeMillis();
        getCalendar().setTime(new Date(currTime));
        int year = getCalendar().get(Calendar.YEAR);
        int month = getCalendar().get(Calendar.MONTH);
        int day = getCalendar().get(Calendar.DAY_OF_MONTH);
        getCalendar().set(year, month, day, 0, 0, 0);
        long minToday = getCalendar().getTimeInMillis() - mHourInMillis;
        return minToday;
    }

    //  获取昨天时间的最大值：
    public static long getYesterdayMaxTimeMillis(long mHourInMillis) {
        long currTime = System.currentTimeMillis();
        getCalendar().setTime(new Date(currTime));

        int year = getCalendar().get(Calendar.YEAR);
        int month = getCalendar().get(Calendar.MONTH);
        int day = getCalendar().get(Calendar.DAY_OF_MONTH);
        getCalendar().set(year, month, day, 23, 59, 59);
        long minToday = mCalendar.getTimeInMillis() - mHourInMillis;

        return minToday;
    }

}
