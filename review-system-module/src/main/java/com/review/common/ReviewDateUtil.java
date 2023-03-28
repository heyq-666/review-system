package com.review.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/3/24
 */
public class ReviewDateUtil {

    private static final String formatDateSign    = "-";

    private static final String formatTimeSign    = ":";

    private static final String simpleDateFormat  = "yyyy" + formatDateSign
            + "MM"
            + formatDateSign
            + "dd";

    private static final String simpleTimeFormat  = simpleDateFormat + " HH"
            + formatTimeSign
            + "mm"
            + formatTimeSign
            + "ss";

    public static String getCurrentDateStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }

    public static java.sql.Date getCurrentDate()
    {
        Calendar newcalendar = null;
        newcalendar = Calendar.getInstance();

        String year = String.valueOf(newcalendar.get(Calendar.YEAR));
        String month = String.valueOf(newcalendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(newcalendar.get(Calendar.DAY_OF_MONTH));

        return stringToSqlDate(year + formatDateSign + month + formatDateSign
                + day);
    }

    public static java.sql.Date stringToSqlDate(String str)
    {
        if (stringToUtilDate(str) == null || str.length() < 1)
            return null;
        else
            return new java.sql.Date(stringToUtilDate(str).getTime());
    }

    public static java.util.Date stringToUtilDate(String str)
    {
        if (null != str && str.length() > 0)
        {
            try
            {
                // 对两种时间格式进行转化。
                if (str.length() <= simpleDateFormat.length())
                { // 只包含日期。
                    return (new SimpleDateFormat(simpleDateFormat)).parse(str);
                } else
                { // 日期和时间都有
                    return (new SimpleDateFormat(simpleTimeFormat)).parse(str);
                }
            } catch (ParseException ex)
            {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }
}
