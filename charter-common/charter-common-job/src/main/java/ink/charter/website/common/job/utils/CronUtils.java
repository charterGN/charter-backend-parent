package ink.charter.website.common.job.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cron表达式工具类
 * 提供Cron表达式的验证、解析和计算功能
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
public class CronUtils {
    
    /**
     * 验证Cron表达式是否有效
     *
     * @param cronExpression Cron表达式
     * @return true-有效，false-无效
     */
    public static boolean isValid(String cronExpression) {
        if (cronExpression == null || cronExpression.trim().isEmpty()) {
            return false;
        }
        
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (ParseException e) {
            log.error("无效的Cron表达式: {}, 错误: {}", cronExpression, e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取Cron表达式的下次执行时间
     *
     * @param cronExpression Cron表达式
     * @return 下次执行时间
     */
    public static Date getNextExecuteTime(String cronExpression) {
        return getNextExecuteTime(cronExpression, new Date());
    }
    
    /**
     * 获取Cron表达式在指定时间后的下次执行时间
     *
     * @param cronExpression Cron表达式
     * @param afterTime 指定时间
     * @return 下次执行时间
     */
    public static Date getNextExecuteTime(String cronExpression, Date afterTime) {
        if (!isValid(cronExpression)) {
            throw new IllegalArgumentException("无效的Cron表达式: " + cronExpression);
        }
        
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(afterTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("解析Cron表达式失败: " + cronExpression, e);
        }
    }
    
    /**
     * 获取Cron表达式的前N次执行时间
     *
     * @param cronExpression Cron表达式
     * @param count 获取次数
     * @return 执行时间列表
     */
    public static List<Date> getNextExecuteTimes(String cronExpression, int count) {
        return getNextExecuteTimes(cronExpression, new Date(), count);
    }
    
    /**
     * 获取Cron表达式在指定时间后的前N次执行时间
     *
     * @param cronExpression Cron表达式
     * @param afterTime 指定时间
     * @param count 获取次数
     * @return 执行时间列表
     */
    public static List<Date> getNextExecuteTimes(String cronExpression, Date afterTime, int count) {
        if (!isValid(cronExpression)) {
            throw new IllegalArgumentException("无效的Cron表达式: " + cronExpression);
        }
        
        if (count <= 0) {
            throw new IllegalArgumentException("获取次数必须大于0");
        }
        
        List<Date> times = new ArrayList<>();
        
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date nextTime = afterTime;
            
            for (int i = 0; i < count; i++) {
                nextTime = cron.getNextValidTimeAfter(nextTime);
                if (nextTime == null) {
                    break;
                }
                times.add(nextTime);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("解析Cron表达式失败: " + cronExpression, e);
        }
        
        return times;
    }
    
    /**
     * 检查指定时间是否满足Cron表达式
     *
     * @param cronExpression Cron表达式
     * @param time 指定时间
     * @return true-满足，false-不满足
     */
    public static boolean isSatisfiedBy(String cronExpression, Date time) {
        if (!isValid(cronExpression)) {
            throw new IllegalArgumentException("无效的Cron表达式: " + cronExpression);
        }
        
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.isSatisfiedBy(time);
        } catch (ParseException e) {
            throw new IllegalArgumentException("解析Cron表达式失败: " + cronExpression, e);
        }
    }
    
    /**
     * 获取Cron表达式的描述信息
     *
     * @param cronExpression Cron表达式
     * @return 描述信息
     */
    public static String getDescription(String cronExpression) {
        if (!isValid(cronExpression)) {
            return "无效的Cron表达式";
        }
        
        // 简单的Cron表达式描述
        String[] parts = cronExpression.split("\\s+");
        if (parts.length < 6) {
            return "格式错误的Cron表达式";
        }
        
        StringBuilder desc = new StringBuilder();
        
        // 秒
        if ("*".equals(parts[0])) {
            desc.append("每秒");
        } else if (parts[0].contains("/")) {
            String[] secParts = parts[0].split("/");
            desc.append("每").append(secParts[1]).append("秒");
        } else {
            desc.append("第").append(parts[0]).append("秒");
        }
        
        // 分钟
        if ("*".equals(parts[1])) {
            desc.append("每分钟");
        } else if (parts[1].contains("/")) {
            String[] minParts = parts[1].split("/");
            desc.append("每").append(minParts[1]).append("分钟");
        } else {
            desc.append("第").append(parts[1]).append("分钟");
        }
        
        // 小时
        if ("*".equals(parts[2])) {
            desc.append("每小时");
        } else if (parts[2].contains("/")) {
            String[] hourParts = parts[2].split("/");
            desc.append("每").append(hourParts[1]).append("小时");
        } else {
            desc.append("第").append(parts[2]).append("小时");
        }
        
        return desc.toString();
    }
    
    /**
     * 常用的Cron表达式模板
     */
    public static class Templates {
        /** 每秒执行 */
        public static final String EVERY_SECOND = "* * * * * ?";
        
        /** 每分钟执行 */
        public static final String EVERY_MINUTE = "0 * * * * ?";
        
        /** 每小时执行 */
        public static final String EVERY_HOUR = "0 0 * * * ?";
        
        /** 每天执行 */
        public static final String EVERY_DAY = "0 0 0 * * ?";
        
        /** 每周执行 */
        public static final String EVERY_WEEK = "0 0 0 ? * MON";
        
        /** 每月执行 */
        public static final String EVERY_MONTH = "0 0 0 1 * ?";
        
        /** 每年执行 */
        public static final String EVERY_YEAR = "0 0 0 1 1 ?";
        
        /** 工作日每天9点执行 */
        public static final String WORKDAY_9AM = "0 0 9 ? * MON-FRI";
        
        /** 每天凌晨2点执行 */
        public static final String DAILY_2AM = "0 0 2 * * ?";
        
        /** 每5分钟执行 */
        public static final String EVERY_5_MINUTES = "0 */5 * * * ?";
        
        /** 每10分钟执行 */
        public static final String EVERY_10_MINUTES = "0 */10 * * * ?";
        
        /** 每30分钟执行 */
        public static final String EVERY_30_MINUTES = "0 */30 * * * ?";
    }
    
    /**
     * 将LocalDateTime转换为Date
     *
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 将Date转换为LocalDateTime
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}