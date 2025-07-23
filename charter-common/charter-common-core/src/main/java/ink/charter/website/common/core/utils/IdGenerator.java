package ink.charter.website.common.core.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成器工具类
 * 提供多种ID生成策略
 *
 * @author charter
 * @create 2025/07/19
 */
public final class IdGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String NUMBERS = "0123456789";
    
    // 机器ID（可以通过配置文件或环境变量设置）
    private static final long MACHINE_ID = 1L;
    
    // 雪花算法相关常量
    private static final long EPOCH = 1640995200000L; // 2022-01-01 00:00:00
    private static final long MACHINE_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    
    private static long lastTimestamp = -1L;
    private static long sequence = 0L;

    private IdGenerator() {
        // 工具类，禁止实例化
    }

    // ==================== UUID相关 ====================

    /**
     * 生成标准UUID
     *
     * @return UUID字符串（包含连字符）
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成简化UUID（去除连字符）
     *
     * @return 32位UUID字符串
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成大写UUID（去除连字符）
     *
     * @return 32位大写UUID字符串
     */
    public static String upperUuid() {
        return simpleUuid().toUpperCase();
    }

    // ==================== 雪花算法 ====================

    /**
     * 生成雪花ID
     *
     * @return 雪花ID
     */
    public static synchronized long snowflakeId() {
        long timestamp = System.currentTimeMillis();
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，拒绝生成ID");
        }
        
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT) |
               (MACHINE_ID << MACHINE_ID_SHIFT) |
               sequence;
    }

    /**
     * 等待下一毫秒
     *
     * @param lastTimestamp 上次时间戳
     * @return 下一毫秒时间戳
     */
    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // ==================== 序列号生成 ====================

    /**
     * 生成递增序列号
     *
     * @return 递增序列号
     */
    public static long nextSequence() {
        return SEQUENCE.incrementAndGet();
    }

    /**
     * 生成指定长度的序列号（左补零）
     *
     * @param length 长度
     * @return 序列号字符串
     */
    public static String nextSequence(int length) {
        long seq = nextSequence();
        return String.format("%0" + length + "d", seq);
    }

    // ==================== 时间戳ID ====================

    /**
     * 生成时间戳ID
     *
     * @return 时间戳ID
     */
    public static long timestampId() {
        return System.currentTimeMillis();
    }

    /**
     * 生成带序列号的时间戳ID
     *
     * @return 时间戳+序列号ID
     */
    public static String timestampSequenceId() {
        return System.currentTimeMillis() + String.format("%04d", nextSequence() % 10000);
    }

    // ==================== 日期格式ID ====================

    /**
     * 生成日期格式ID（yyyyMMddHHmmss）
     *
     * @return 日期格式ID
     */
    public static String dateId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 生成日期格式ID（yyyyMMddHHmmssSSS）
     *
     * @return 带毫秒的日期格式ID
     */
    public static String dateIdWithMillis() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    /**
     * 生成日期+序列号ID
     *
     * @return 日期+序列号ID
     */
    public static String dateSequenceId() {
        return dateId() + String.format("%04d", nextSequence() % 10000);
    }

    // ==================== 随机ID ====================

    /**
     * 生成随机数字ID
     *
     * @param length 长度
     * @return 随机数字ID
     */
    public static String randomNumericId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机字母数字ID
     *
     * @param length 长度
     * @return 随机字母数字ID
     */
    public static String randomAlphanumericId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // ==================== 业务ID生成 ====================

    /**
     * 生成订单号
     * 格式：yyyyMMdd + 8位随机数字
     *
     * @return 订单号
     */
    public static String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = randomNumericId(8);
        return dateStr + randomStr;
    }

    /**
     * 生成用户编号
     * 格式：U + 时间戳后8位 + 4位随机数字
     *
     * @return 用户编号
     */
    public static String generateUserNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String timestampSuffix = timestamp.substring(timestamp.length() - 8);
        String randomStr = randomNumericId(4);
        return "U" + timestampSuffix + randomStr;
    }

    /**
     * 生成商品编号
     * 格式：P + yyyyMMdd + 6位序列号
     *
     * @return 商品编号
     */
    public static String generateProductNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String seqStr = String.format("%06d", nextSequence() % 1000000);
        return "P" + dateStr + seqStr;
    }

    /**
     * 生成流水号
     * 格式：前缀 + yyyyMMddHHmmss + 4位序列号
     *
     * @param prefix 前缀
     * @return 流水号
     */
    public static String generateSerialNo(String prefix) {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String seqStr = String.format("%04d", nextSequence() % 10000);
        return (prefix != null ? prefix : "") + dateStr + seqStr;
    }

    // ==================== 验证码生成 ====================

    /**
     * 生成数字验证码
     *
     * @param length 长度
     * @return 数字验证码
     */
    public static String generateNumericCode(int length) {
        return randomNumericId(length);
    }

    /**
     * 生成字母数字验证码
     *
     * @param length 长度
     * @return 字母数字验证码
     */
    public static String generateAlphanumericCode(int length) {
        return randomAlphanumericId(length);
    }

    /**
     * 生成6位数字验证码
     *
     * @return 6位数字验证码
     */
    public static String generateSmsCode() {
        return generateNumericCode(6);
    }

    /**
     * 生成4位数字验证码
     *
     * @return 4位数字验证码
     */
    public static String generateImageCode() {
        return generateNumericCode(4);
    }
}