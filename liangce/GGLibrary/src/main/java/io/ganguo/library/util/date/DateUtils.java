package io.ganguo.library.util.date;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Tony on 10/27/14.
 */
public class DateUtils {
    public static final String TAG = DateUtils.class.getName();
    public static String dayNames[] = {"日", "一", "二", "三", "四", "五", "六"};

    /**
     * <pre>
     * 判断date和当前日期是否在同一周内
     * 注:
     * Calendar类提供了一个获取日期在所属年份中是第几周的方法，对于上一年末的某一天
     * 和新年初的某一天在同一周内也一样可以处理，例如2012-12-31和2013-01-01虽然在
     * 不同的年份中，但是使用此方法依然判断二者属于同一周内
     * </pre>
     *
     * @param date
     * @return
     */
    public static boolean isSameWeekWithToday(Date date) {

        if (date == null) {
            return false;
        }

        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();

        todayCal.setTime(new Date());
        dateCal.setTime(date);

        // 1.比较当前日期在年份中的周数是否相同
        if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化时间
     * pattern HH:mm 20:10
     *
     * @param pattern yyyy-MM-dd
     * @param date    日期
     * @return 格式化后时间字符串
     */
    public static String format(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 本周六 20:00
     *
     * @param date
     * @return
     */
    public static String getTimeOnWeek(Date date) {
        String friendTime = "";
        if (isSameWeekWithToday(date)) {
            friendTime += "本周";
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            friendTime += dayNames[dayOfWeek - 1];
            friendTime += format("HH:mm", date);
        } else {
            friendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        }
        return friendTime;
    }

    /**
     * 今天是否在两个时间之内
     *
     * @param begin
     * @param end
     * @return
     */
    public static boolean isBetweenWithToday(Date begin, Date end) {
        Date today = new Date();
        return today.getTime() > begin.getTime() && today.getTime() <= end.getTime();
    }

    /**
     * 距离现在还有多少时间
     * dd天hh小时mm分钟
     * 11小时23分钟
     *
     * @param date
     * @return
     */
    public static String hasTimeWithNow(Date date, String[] format) {
        if (date == null) return null;

        String dateFormatted = "";
        long has = date.getTime() - System.currentTimeMillis();
        long day = has / (24 * 60 * 60 * 1000);
        long hh = has / (1000 * 60 * 60) - day * 24;
        long mm = has / (1000 * 60) - hh * 60 - day * 24 * 60;
        long ss = has / (1000 * 60) - mm * 60 - hh * 60 - day * 24 * 60;
        if (day > 0) {
            dateFormatted += day + format[0];
        }
        if (hh > 0) {
            dateFormatted += hh + format[1];
        }
        if (mm > 0) {
            dateFormatted += mm + format[2];
        }
        if (ss > 0) {
            dateFormatted += mm + format[3];
        }
        Log.d(TAG, "dateFormatted: " + dateFormatted);
        return dateFormatted;
    }

    /**
     * 距离现在还有多少时间
     * hh小时mm分钟
     * 11小时23分钟
     *
     * @param date
     * @return
     */
    public static String getTimeWithNow(Date date, String[] format) {
        if (date == null) return null;

        String dateFormatted = "";
        long has = System.currentTimeMillis() - date.getTime();

        long hh = has / (60 * 60 * 1000);
        long mm = has / (60 * 1000);
        long ss = has / 1000;
        if (mm > 0) {
            ss -= mm * 60;
        }
        if (hh > 0) {
            mm -= hh * 60;
        }

        if (hh > 0) {
            dateFormatted += hh + format[0];
        }
        if (mm > 0) {
            dateFormatted += mm + format[1];
        }
        if (ss > 0) {
            dateFormatted += ss + format[2];
        }
        Log.d(TAG, has + " dateFormatted: " + dateFormatted);
        return dateFormatted;
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }
}
