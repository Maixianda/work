package io.ganguo.library.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期时间类
 * yyyy-MM-dd HH:mm:ss
 * <p/>
 * Created by Tony on 1/5/15.
 */
public class DateTime extends BaseDate {
    public final static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat FORMATTER_CN = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public final static SimpleDateFormat FORMATTER_M = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public final static SimpleDateFormat FORMATTER_I = new SimpleDateFormat("yy年MM月dd日");//利率
    public final static SimpleDateFormat FORMATTER_LIANGCE = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * string to date
     *
     * @param string
     * @return
     */
    public static DateTime parseFor(String string) {
        java.util.Date date = null;
        try {
            date = FORMATTER.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DateTime(date);
    }

    /**
     * date to string
     *
     * @param date
     * @return
     */
    public static String formatFor(BaseDate date) {
        return FORMATTER.format(date);
    }

    /**
     * date to string
     *
     * @param date
     * @return
     */
    public static String formatLiangCeFor(BaseDate date) {
        return FORMATTER_LIANGCE.format(date);
    }

    /**
     * date to string
     *
     * @param date
     * @return
     */
    public static String formatForM(long date) {
        return FORMATTER_M.format(new BaseDate(date));
    }

    /**
     * date to string
     *
     * @param date
     * @return
     */
    public static String formatForI(long date) {
        return FORMATTER_I.format(new BaseDate(date));
    }

    // 构造方法
    public DateTime() {
        super();
    }

    public DateTime(long time) {
        super(time);
    }

    public DateTime(java.util.Date date) {
        super(date.getTime());
    }

}
