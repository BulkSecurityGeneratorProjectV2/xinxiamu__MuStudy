package co.jufeng.util.date;
import java.text.ParsePosition;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
public class DateUtil {  
  
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();  
      
    private static final Object object = new Object();  
      
    //获取SimpleDateFormat
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {  
        SimpleDateFormat dateFormat = threadLocal.get();  
        if (dateFormat == null) {  
            synchronized (object) {  
                if (dateFormat == null) {  
                    dateFormat = new SimpleDateFormat(pattern);  
                    dateFormat.setLenient(false);  
                    threadLocal.set(dateFormat);  
                }  
            }  
        }  
        dateFormat.applyPattern(pattern);  
        return dateFormat;  
    }  
  
    //获取日期中的某数值。如获取月份 
    private static int getInteger(Date date, int dateType) {  
        int num = 0;  
        Calendar calendar = Calendar.getInstance();  
        if (date != null) {  
            calendar.setTime(date);  
            num = calendar.get(dateType);  
        }  
        return num;  
    }  
  
    //增加日期中某类型的某数值。如增加日期
    private static String addInteger(String date, int dateType, int amount) {  
        String dateString = null;  
        DateStyle dateStyle = getDateStyle(date);  
        if (dateStyle != null) {  
            Date myDate = StringToDate(date, dateStyle);  
            myDate = addInteger(myDate, dateType, amount);  
            dateString = toString(myDate, dateStyle);  
        }  
        return dateString;  
    }  
  
    // 增加日期中某类型的某数值。如增加日期
    private static Date addInteger(Date date, int dateType, int amount) {  
        Date myDate = null;  
        if (date != null) {  
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(date);  
            calendar.add(dateType, amount);  
            myDate = calendar.getTime();  
        }  
        return myDate;  
    }  
  
    //获取精确的日期 
    private static Date getAccurateDate(List<Long> timestamps) {  
        Date date = null;  
        long timestamp = 0;  
        Map<Long, long[]> map = new HashMap<Long, long[]>();  
        List<Long> absoluteValues = new ArrayList<Long>();  
  
        if (timestamps != null && timestamps.size() > 0) {  
            if (timestamps.size() > 1) {  
                for (int i = 0; i < timestamps.size(); i++) {  
                    for (int j = i + 1; j < timestamps.size(); j++) {  
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));  
                        absoluteValues.add(absoluteValue);  
                        long[] timestampTmp = { timestamps.get(i), timestamps.get(j) };  
                        map.put(absoluteValue, timestampTmp);  
                    }  
                }  
  
                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0  
                // 因此不能将minAbsoluteValue取默认值0  
                long minAbsoluteValue = -1;  
                if (!absoluteValues.isEmpty()) {  
                    minAbsoluteValue = absoluteValues.get(0);  
                    for (int i = 1; i < absoluteValues.size(); i++) {  
                        if (minAbsoluteValue > absoluteValues.get(i)) {  
                            minAbsoluteValue = absoluteValues.get(i);  
                        }  
                    }  
                }  
  
                if (minAbsoluteValue != -1) {  
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);  
  
                    long dateOne = timestampsLastTmp[0];  
                    long dateTwo = timestampsLastTmp[1];  
                    if (absoluteValues.size() > 1) {  
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;  
                    }  
                }  
            } else {  
                timestamp = timestamps.get(0);  
            }  
        }  
  
        if (timestamp != 0) {  
            date = new Date(timestamp);  
        }  
        return date;  
    }  

    //判断字符串是否为日期字符串 
    public static boolean isDate(String date) {  
        boolean isDate = false;  
        if (date != null) {  
            if (getDateStyle(date) != null) {  
                isDate = true;  
            }  
        }  
        return isDate;  
    }  
  
    //获取日期字符串的日期风格。失敗返回null 
    public static DateStyle getDateStyle(String date) {  
        DateStyle dateStyle = null;  
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();  
        List<Long> timestamps = new ArrayList<Long>();  
        for (DateStyle style : DateStyle.values()) {  
            if (style.isShowOnly()) {  
                continue;  
            }  
            Date dateTmp = null;  
            if (date != null) {  
                try {  
                    ParsePosition pos = new ParsePosition(0);  
                    dateTmp = getDateFormat(style.getValue()).parse(date, pos);  
                    if (pos.getIndex() != date.length()) {  
                        dateTmp = null;  
                    }  
                } catch (Exception e) {  
                }  
            }  
            if (dateTmp != null) {  
                timestamps.add(dateTmp.getTime());  
                map.put(dateTmp.getTime(), style);  
            }  
        }  
        Date accurateDate = getAccurateDate(timestamps);  
        if (accurateDate != null) {  
            dateStyle = map.get(accurateDate.getTime());  
        }  
        return dateStyle;  
    }  
  
    //将日期字符串转化为日期。失败返回null
    public static Date StringToDate(String date) {  
        DateStyle dateStyle = getDateStyle(date);  
        return StringToDate(date, dateStyle);  
    }  
  
    //将日期字符串转化为日期。失败返回null
    public static Date StringToDate(String date, String pattern) {  
        Date myDate = null;  
        if (date != null) {  
            try {  
                myDate = getDateFormat(pattern).parse(date);  
            } catch (Exception e) {  
            }  
        }  
        return myDate;  
    }  
  
    //将日期字符串转化为日期。失败返回null
    public static Date StringToDate(String date, DateStyle dateStyle) {  
        Date myDate = null;  
        if (dateStyle != null) {  
            myDate = StringToDate(date, dateStyle.getValue());  
        }  
        return myDate;  
    }  
  
    //将日期转化为日期字符串。失败返回null
    public static String DateToString(Date date, String pattern) {  
        String dateString = null;  
        if (date != null) {  
            try {  
                dateString = getDateFormat(pattern).format(date);  
            } catch (Exception e) {  
            }  
        }  
        return dateString;  
    }  
  
    //将日期转化为日期字符串。失败返回null。 
    public static String toString(Date date, DateStyle dateStyle) {  
        String dateString = null;  
        if (dateStyle != null) {  
            dateString = DateToString(date, dateStyle.getValue());  
        }  
        return dateString;  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null
    public static String toString(String date, String newPattern) {  
        DateStyle oldDateStyle = getDateStyle(date);  
        return StringToString(date, oldDateStyle, newPattern);  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null
    public static String StringToString(String date, DateStyle newDateStyle) {  
        DateStyle oldDateStyle = getDateStyle(date);  
        return StringToString(date, oldDateStyle, newDateStyle);  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null
    public static String StringToString(String date, String olddPattern, String newPattern) {  
        return DateToString(StringToDate(date, olddPattern), newPattern);  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null 
    public static String StringToString(String date, DateStyle olddDteStyle, String newParttern) {  
        String dateString = null;  
        if (olddDteStyle != null) {  
            dateString = StringToString(date, olddDteStyle.getValue(), newParttern);  
        }  
        return dateString;  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null
    public static String StringToString(String date, String olddPattern, DateStyle newDateStyle) {  
        String dateString = null;  
        if (newDateStyle != null) {  
            dateString = StringToString(date, olddPattern, newDateStyle.getValue());  
        }  
        return dateString;  
    }  
  
    //将日期字符串转化为另一日期字符串。失败返回null
    public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {  
        String dateString = null;  
        if (olddDteStyle != null && newDateStyle != null) {  
            dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());  
        }  
        return dateString;  
    }  
  
    //增加日期的年份。失败返回null 
    public static String addYear(String date, int yearAmount) {  
        return addInteger(date, Calendar.YEAR, yearAmount);  
    }  
  
    //增加日期的年份。失败返回null
    public static Date addYear(Date date, int yearAmount) {  
        return addInteger(date, Calendar.YEAR, yearAmount);  
    }  
  
    //增加日期的月份。失败返回null  
    public static String addMonth(String date, int monthAmount) {  
        return addInteger(date, Calendar.MONTH, monthAmount);  
    }  
  
    //增加日期的月份。失败返回null
    public static Date addMonth(Date date, int monthAmount) {  
        return addInteger(date, Calendar.MONTH, monthAmount);  
    }  
  
    //增加日期的天数。失败返回null
    public static String addDay(String date, int dayAmount) {  
        return addInteger(date, Calendar.DATE, dayAmount);  
    }  
  
    //增加日期的天数。失败返回null 
    public static Date addDay(Date date, int dayAmount) {  
        return addInteger(date, Calendar.DATE, dayAmount);  
    }  
  
    //增加日期的小时。失败返回null 
    public static String addHour(String date, int hourAmount) {  
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);  
    }  
  
    //增加日期的小时。失败返回null
    public static Date addHour(Date date, int hourAmount) {  
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);  
    }  
  
    //增加日期的分钟。失败返回null
    public static String addMinute(String date, int minuteAmount) {  
        return addInteger(date, Calendar.MINUTE, minuteAmount);  
    }  
  
    //增加日期的分钟。失败返回null 
    public static Date addMinute(Date date, int minuteAmount) {  
        return addInteger(date, Calendar.MINUTE, minuteAmount);  
    }  
  
    //增加日期的秒钟。失败返回null  
    public static String addSecond(String date, int secondAmount) {  
        return addInteger(date, Calendar.SECOND, secondAmount);  
    }  
  
    //增加日期的秒钟。失败返回null
    public static Date addSecond(Date date, int secondAmount) {  
        return addInteger(date, Calendar.SECOND, secondAmount);  
    }  
  
    //获取日期的年份。失败返回0 
    public static int getYear(String date) {  
        return getYear(StringToDate(date));  
    }  
  
    //获取日期的年份。失败返回0 
    public static int getYear(Date date) {  
        return getInteger(date, Calendar.YEAR);  
    }  
  
    //获取日期的月份。失败返回0 
    public static int getMonth(String date) {  
        return getMonth(StringToDate(date));  
    }  
  
    //获取日期的月份。失败返回0  
    public static int getMonth(Date date) {  
        return getInteger(date, Calendar.MONTH) + 1;  
    }  
  
    //获取日期的天数。失败返回0  
    public static int getDay(String date) {  
        return getDay(StringToDate(date));  
    }  
  
    //获取日期的天数。失败返回0 
    public static int getDay(Date date) {  
        return getInteger(date, Calendar.DATE);  
    }  
  
    //获取日期的小时。失败返回0 
    public static int getHour(String date) {  
        return getHour(StringToDate(date));  
    }  
  
    //获取日期的小时。失败返回0
    public static int getHour(Date date) {  
        return getInteger(date, Calendar.HOUR_OF_DAY);  
    }  
  
    //获取日期的分钟。失败返回0
    public static int getMinute(String date) {  
        return getMinute(StringToDate(date));  
    }  
  
    //获取日期的分钟。失败返回0 
    public static int getMinute(Date date) {  
        return getInteger(date, Calendar.MINUTE);  
    }  
  
    //获取日期的秒钟。失败返回0
    public static int getSecond(String date) {  
        return getSecond(StringToDate(date));  
    }  
  
    //获取日期的秒钟。失败返回0 
    public static int getSecond(Date date) {  
        return getInteger(date, Calendar.SECOND);  
    }  
  
    //获取日期 。默认yyyy-MM-dd格式。失败返回null 
    public static String getDate(String date) {  
        return StringToString(date, DateStyle.YYYY_MM_DD);  
    }  
  
    //获取日期。默认yyyy-MM-dd格式。失败返回null 
    public static String getDate(Date date) {  
        return toString(date, DateStyle.YYYY_MM_DD);  
    }  
    
    //获取日期的时间。默认HH:mm:ss格式。失败返回null
    public static String getTime(String date) {  
        return StringToString(date, DateStyle.HH_MM_SS);  
    }  
  
    //获取日期的时间。默认HH:mm:ss格式。失败返回null 
    public static String getTime(Date date) {  
        return toString(date, DateStyle.HH_MM_SS);  
    }  
  
    //获取日期的星期。失败返回null
    public static Week getWeek(String date) {  
        Week week = null;  
        DateStyle dateStyle = getDateStyle(date);  
        if (dateStyle != null) {  
            Date myDate = StringToDate(date, dateStyle);  
            week = getWeek(myDate);  
        }  
        return week;  
    }  
  
    //获取日期的星期。失败返回null 
    public static Week getWeek(Date date) {  
        Week week = null;  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;  
        switch (weekNumber) {  
        case 0:  
            week = Week.SUNDAY;  
            break;  
        case 1:  
            week = Week.MONDAY;  
            break;  
        case 2:  
            week = Week.TUESDAY;  
            break;  
        case 3:  
            week = Week.WEDNESDAY;  
            break;  
        case 4:  
            week = Week.THURSDAY;  
            break;  
        case 5:  
            week = Week.FRIDAY;  
            break;  
        case 6:  
            week = Week.SATURDAY;  
            break;  
        }  
        return week;  
    }  
  
    //获取两个日期相差的天数
    public static int getIntervalDays(String date, String otherDate) {  
        return getIntervalDays(StringToDate(date), StringToDate(otherDate));  
    }  
  
    //获取两个日期相差的天数  
    public static int getIntervalDays(Date date, Date otherDate) {  
        int num = -1;  
        Date dateTmp = DateUtil.StringToDate(DateUtil.getDate(date), DateStyle.YYYY_MM_DD);  
        Date otherDateTmp = DateUtil.StringToDate(DateUtil.getDate(otherDate), DateStyle.YYYY_MM_DD);  
        if (dateTmp != null && otherDateTmp != null) {  
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());  
            num = (int) (time / (24 * 60 * 60 * 1000));  
        }  
        return num;  
    }  
}  
