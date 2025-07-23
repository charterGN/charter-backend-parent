package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期时间工具类
 * 提供常用的日期时间处理功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class DateTimeUtils {

    // 常用日期时间格式
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";
    public static final String PATTERN_TIME_COMPACT = "HHmmss";
    public static final String PATTERN_DATETIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_ISO_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PATTERN_ISO_DATETIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    
    // 常用格式化器
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_DATETIME_COMPACT = DateTimeFormatter.ofPattern(PATTERN_DATETIME_COMPACT);
    public static final DateTimeFormatter FORMATTER_DATE_COMPACT = DateTimeFormatter.ofPattern(PATTERN_DATE_COMPACT);
    public static final DateTimeFormatter FORMATTER_TIME_COMPACT = DateTimeFormatter.ofPattern(PATTERN_TIME_COMPACT);
    public static final DateTimeFormatter FORMATTER_DATETIME_MILLISECOND = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MILLISECOND);
    
    // 默认时区
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");

    private DateTimeUtils() {
        // 工具类，禁止实例化
    }

    // ==================== 当前时间获取 ====================

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     *
     * @return 当前时间戳（秒）
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前LocalDateTime
     *
     * @return 当前LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(DEFAULT_ZONE_ID);
    }

    /**
     * 获取当前LocalDate
     *
     * @return 当前LocalDate
     */
    public static LocalDate today() {
        return LocalDate.now(DEFAULT_ZONE_ID);
    }

    /**
     * 获取当前LocalTime
     *
     * @return 当前LocalTime
     */
    public static LocalTime nowTime() {
        return LocalTime.now(DEFAULT_ZONE_ID);
    }

    // ==================== 格式化 ====================

    /**
     * 格式化LocalDateTime为字符串
     *
     * @param dateTime LocalDateTime对象
     * @param pattern  格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return dateTime.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("日期格式化失败: dateTime={}, pattern={}", dateTime, pattern, e);
            return null;
        }
    }

    /**
     * 格式化LocalDateTime为默认格式字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, PATTERN_DATETIME);
    }

    /**
     * 格式化LocalDate为字符串
     *
     * @param date    LocalDate对象
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return date.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("日期格式化失败: date={}, pattern={}", date, pattern, e);
            return null;
        }
    }

    /**
     * 格式化LocalDate为默认格式字符串（yyyy-MM-dd）
     *
     * @param date LocalDate对象
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date) {
        return format(date, PATTERN_DATE);
    }

    /**
     * 格式化LocalTime为字符串
     *
     * @param time    LocalTime对象
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return time.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("时间格式化失败: time={}, pattern={}", time, pattern, e);
            return null;
        }
    }

    /**
     * 格式化LocalTime为默认格式字符串（HH:mm:ss）
     *
     * @param time LocalTime对象
     * @return 格式化后的字符串
     */
    public static String format(LocalTime time) {
        return format(time, PATTERN_TIME);
    }

    // ==================== 解析 ====================

    /**
     * 解析字符串为LocalDateTime
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     格式模式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (!StringUtils.hasText(dateTimeStr) || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            log.error("日期时间解析失败: dateTimeStr={}, pattern={}", dateTimeStr, pattern, e);
            return null;
        }
    }

    /**
     * 解析字符串为LocalDateTime（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, PATTERN_DATETIME);
    }

    /**
     * 解析字符串为LocalDate
     *
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (!StringUtils.hasText(dateStr) || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            log.error("日期解析失败: dateStr={}, pattern={}", dateStr, pattern, e);
            return null;
        }
    }

    /**
     * 解析字符串为LocalDate（默认格式：yyyy-MM-dd）
     *
     * @param dateStr 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, PATTERN_DATE);
    }

    /**
     * 解析字符串为LocalTime
     *
     * @param timeStr 时间字符串
     * @param pattern 格式模式
     * @return LocalTime对象
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (!StringUtils.hasText(timeStr) || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            log.error("时间解析失败: timeStr={}, pattern={}", timeStr, pattern, e);
            return null;
        }
    }

    /**
     * 解析字符串为LocalTime（默认格式：HH:mm:ss）
     *
     * @param timeStr 时间字符串
     * @return LocalTime对象
     */
    public static LocalTime parseTime(String timeStr) {
        return parseTime(timeStr, PATTERN_TIME);
    }

    // ==================== 转换 ====================

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp 时间戳（毫秒）
     * @return LocalDateTime对象
     */
    public static LocalDateTime timestampToDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID);
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param dateTime LocalDateTime对象
     * @return 时间戳（毫秒）
     */
    public static long dateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0L;
        }
        return dateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime dateToDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID);
    }

    /**
     * LocalDateTime转Date
     *
     * @param dateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date dateTimeToDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * LocalDate转Date
     *
     * @param date LocalDate对象
     * @return Date对象
     */
    public static Date localDateToDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    // ==================== 计算 ====================

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差（endDate - startDate）
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个时间之间的小时差
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 小时差（endDateTime - startDateTime）
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0L;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个时间之间的分钟差
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 分钟差（endDateTime - startDateTime）
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0L;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个时间之间的秒数差
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 秒数差（endDateTime - startDateTime）
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0L;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    // ==================== 日期操作 ====================

    /**
     * 获取指定日期的开始时间（00:00:00）
     *
     * @param date 日期
     * @return 开始时间
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间（23:59:59.999）
     *
     * @param date 日期
     * @return 结束时间
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 获取指定日期所在月份的第一天
     *
     * @param date 日期
     * @return 月份第一天
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期所在月份的最后一天
     *
     * @param date 日期
     * @return 月份最后一天
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取指定日期所在年份的第一天
     *
     * @param date 日期
     * @return 年份第一天
     */
    public static LocalDate firstDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取指定日期所在年份的最后一天
     *
     * @param date 日期
     * @return 年份最后一天
     */
    public static LocalDate lastDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 判断是否为闰年
     *
     * @param year 年份
     * @return 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * 判断日期是否为今天
     *
     * @param date 日期
     * @return 是否为今天
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(today());
    }

    /**
     * 判断日期是否为昨天
     *
     * @param date 日期
     * @return 是否为昨天
     */
    public static boolean isYesterday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(today().minusDays(1));
    }

    /**
     * 判断日期是否为明天
     *
     * @param date 日期
     * @return 是否为明天
     */
    public static boolean isTomorrow(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(today().plusDays(1));
    }

    /**
     * 判断日期是否为工作日（周一到周五）
     *
     * @param date 日期
     * @return 是否为工作日
     */
    public static boolean isWorkday(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * 判断日期是否为周末（周六或周日）
     *
     * @param date 日期
     * @return 是否为周末
     */
    public static boolean isWeekend(LocalDate date) {
        return !isWorkday(date);
    }

    /**
     * 获取年龄
     *
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, today()).getYears();
    }

    /**
     * 获取友好的时间描述（如：刚刚、1分钟前、1小时前等）
     *
     * @param dateTime 时间
     * @return 友好的时间描述
     */
    public static String getFriendlyTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "未知";
        }
        
        LocalDateTime now = now();
        long seconds = secondsBetween(dateTime, now);
        
        if (seconds < 0) {
            return "未来时间";
        }
        
        if (seconds < 60) {
            return "刚刚";
        }
        
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "分钟前";
        }
        
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "小时前";
        }
        
        long days = hours / 24;
        if (days < 30) {
            return days + "天前";
        }
        
        long months = days / 30;
        if (months < 12) {
            return months + "个月前";
        }
        
        long years = months / 12;
        return years + "年前";
    }
}