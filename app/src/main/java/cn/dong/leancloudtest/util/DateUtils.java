package cn.dong.leancloudtest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具
 *
 * @author dong 2014-12-3
 */
public class DateUtils {
    private SimpleDateFormat mFormat;

    private static SimpleDateFormat mFullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private static SimpleDateFormat mCurFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private static SimpleDateFormat mBeforFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    public DateUtils(String format) {
        mFormat = new SimpleDateFormat(format, Locale.CHINA);
    }

    public String format(Date date) {
        try {
            return mFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFullTimeBySeconds(long time) {
        return mFullFormat.format(new Date(time * 1000));
    }

    public static String getTimeByDate(Date date) {
        return getTimeByMillis(date.getTime());
    }

    public static String getTimeBySeconds(long time) {
        return getTimeByMillis(time * 1000);
    }

    public static String getTimeByMillis(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date date = new Date(time);

        Calendar curCalendar = Calendar.getInstance();
        long curTime = curCalendar.getTimeInMillis();
        long distance = Math.abs(curTime - time);

        // 个人认为可以优先1小时以内的格式，大于1小时再区分当天和昨天
        if (cal.get(Calendar.YEAR) == curCalendar.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == curCalendar.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) == curCalendar.get(Calendar.DAY_OF_MONTH)) {
            // 当天
            if (distance <= 10 * 1000) {
                // 小于10秒
                return "刚刚";
            } else if (distance <= 60 * 1000) {
                // 小于一分钟
                return String.format("%s秒前", distance / 1000);
            } else if (distance <= 60 * 60 * 1000) {
                // 小于一小时
                return String.format("%s分钟前", distance / 60 / 1000);
            } else {
                // 大于一小时
                return mCurFormat.format(date);
            }
        } else if (curCalendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                && curCalendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                && curCalendar.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) == 1) {
            // 昨天
            return "昨天";
        } else {
            // 前天及以前
            return mBeforFormat.format(date);
        }
    }

}
