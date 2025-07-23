package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * 提供常用的数据验证功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class ValidationUtils {

    /**
     * 邮箱正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    /**
     * 手机号正则表达式
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    /**
     * 身份证号码正则表达式（18位中国身份证）
     */
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );

    /**
     * 用户名正则表达式
     */
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );

    /**
     * 密码正则表达式：8-20位，包含大小写字母、数字、特殊字符
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );

    /**
     * URL正则表达式
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );

    /**
     * IPv4地址正则表达式
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    );

    /**
     * Mac地址正则表达式
     */
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile(
        "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    );

    /**
     * 纯中文正则表达式
     */
    private static final Pattern CHINESE_PATTERN = Pattern.compile(
        "^[\u4e00-\u9fa5]+$"
    );

    /**
     * 数字正则表达式
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile(
        "^-?\\d+(\\.\\d+)?$"
    );

    /**
     * 整数正则表达式
     */
    private static final Pattern INTEGER_PATTERN = Pattern.compile(
        "^-?\\d+$"
    );

    /**
     * 正数整数正则表达式
     */
    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile(
        "^[1-9]\\d*$"
    );

    /**
     * 非负整数正则表达式
     */
    private static final Pattern NON_NEGATIVE_INTEGER_PATTERN = Pattern.compile(
        "^(0|[1-9]\\d*)$"
    );

    private ValidationUtils() {
        // 工具类，禁止实例化
    }

    // ==================== 基础验证 ====================

    /**
     * 验证对象是否为null
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 验证对象是否不为null
     *
     * @param obj 对象
     * @return 是否不为null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 验证字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return !StringUtils.hasText(str);
    }

    /**
     * 验证字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.hasText(str);
    }

    /**
     * 验证集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 验证集合是否不为空
     *
     * @param collection 集合
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 验证Map是否为空
     *
     * @param map Map对象
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 验证Map是否不为空
     *
     * @param map Map对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 验证数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 验证数组是否不为空
     *
     * @param array 数组
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    // ==================== 字符串验证 ====================

    /**
     * 验证字符串长度是否在指定范围内
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 是否在范围内
     */
    public static boolean isLengthBetween(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * 验证字符串是否匹配正则表达式
     *
     * @param str     字符串
     * @param pattern 正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String str, String pattern) {
        if (isEmpty(str) || isEmpty(pattern)) {
            return false;
        }
        try {
            return Pattern.matches(pattern, str);
        } catch (Exception e) {
            log.error("正则表达式匹配失败: str={}, pattern={}", str, pattern, e);
            return false;
        }
    }

    /**
     * 验证字符串是否匹配正则表达式
     *
     * @param str     字符串
     * @param pattern 编译后的正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String str, Pattern pattern) {
        if (isEmpty(str) || pattern == null) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    // ==================== 格式验证 ====================

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否为有效邮箱
     */
    public static boolean isValidEmail(String email) {
        return matches(email, EMAIL_PATTERN);
    }

    /**
     * 验证手机号格式（中国大陆）
     *
     * @param phone 手机号
     * @return 是否为有效手机号
     */
    public static boolean isValidPhone(String phone) {
        return matches(phone, PHONE_PATTERN);
    }

    /**
     * 验证身份证号格式（中国大陆18位）
     *
     * @param idCard 身份证号
     * @return 是否为有效身份证号
     */
    public static boolean isValidIdCard(String idCard) {
        if (!matches(idCard, ID_CARD_PATTERN)) {
            return false;
        }
        
        // 验证校验码
        return validateIdCardChecksum(idCard);
    }

    /**
     * 验证用户名格式（3-20位字母、数字、下划线）
     *
     * @param username 用户名
     * @return 是否为有效用户名
     */
    public static boolean isValidUsername(String username) {
        return matches(username, USERNAME_PATTERN);
    }

    /**
     * 验证密码强度（至少8位，包含大小写字母和数字）
     *
     * @param password 密码
     * @return 是否为强密码
     */
    public static boolean isStrongPassword(String password) {
        return matches(password, PASSWORD_PATTERN);
    }

    /**
     * 验证URL格式
     *
     * @param url URL地址
     * @return 是否为有效URL
     */
    public static boolean isValidUrl(String url) {
        return matches(url, URL_PATTERN);
    }

    /**
     * 验证IPv4地址格式
     *
     * @param ip IP地址
     * @return 是否为有效IPv4地址
     */
    public static boolean isValidIPv4(String ip) {
        return matches(ip, IPV4_PATTERN);
    }

    /**
     * 验证MAC地址格式
     *
     * @param mac MAC地址
     * @return 是否为有效MAC地址
     */
    public static boolean isValidMacAddress(String mac) {
        return matches(mac, MAC_ADDRESS_PATTERN);
    }

    /**
     * 验证是否为纯中文
     *
     * @param str 字符串
     * @return 是否为纯中文
     */
    public static boolean isChinese(String str) {
        return matches(str, CHINESE_PATTERN);
    }

    // ==================== 数字验证 ====================

    /**
     * 验证是否为数字
     *
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isNumber(String str) {
        return matches(str, NUMBER_PATTERN);
    }

    /**
     * 验证是否为整数
     *
     * @param str 字符串
     * @return 是否为整数
     */
    public static boolean isInteger(String str) {
        return matches(str, INTEGER_PATTERN);
    }

    /**
     * 验证是否为正整数
     *
     * @param str 字符串
     * @return 是否为正整数
     */
    public static boolean isPositiveInteger(String str) {
        return matches(str, POSITIVE_INTEGER_PATTERN);
    }

    /**
     * 验证是否为非负整数
     *
     * @param str 字符串
     * @return 是否为非负整数
     */
    public static boolean isNonNegativeInteger(String str) {
        return matches(str, NON_NEGATIVE_INTEGER_PATTERN);
    }

    /**
     * 验证数字是否在指定范围内
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * 验证数字是否在指定范围内
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(long value, long min, long max) {
        return value >= min && value <= max;
    }

    /**
     * 验证数字是否在指定范围内
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * 验证BigDecimal是否在指定范围内
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isInRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    /**
     * 验证是否为正数
     *
     * @param value 数值
     * @return 是否为正数
     */
    public static boolean isPositive(Number value) {
        if (value == null) {
            return false;
        }
        return value.doubleValue() > 0;
    }

    /**
     * 验证是否为非负数
     *
     * @param value 数值
     * @return 是否为非负数
     */
    public static boolean isNonNegative(Number value) {
        if (value == null) {
            return false;
        }
        return value.doubleValue() >= 0;
    }

    // ==================== 日期验证 ====================

    /**
     * 验证日期字符串格式
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return 是否为有效日期
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        if (isEmpty(dateStr) || isEmpty(pattern)) {
            return false;
        }
        
        try {
            DateTimeUtils.parseDate(dateStr, pattern);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 验证日期时间字符串格式
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     日期时间格式
     * @return 是否为有效日期时间
     */
    public static boolean isValidDateTime(String dateTimeStr, String pattern) {
        if (isEmpty(dateTimeStr) || isEmpty(pattern)) {
            return false;
        }
        
        try {
            DateTimeUtils.parseDateTime(dateTimeStr, pattern);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 验证日期是否在指定范围内
     *
     * @param date      日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 是否在范围内
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * 验证日期时间是否在指定范围内
     *
     * @param dateTime      日期时间
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 是否在范围内
     */
    public static boolean isDateTimeInRange(LocalDateTime dateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (dateTime == null || startDateTime == null || endDateTime == null) {
            return false;
        }
        return !dateTime.isBefore(startDateTime) && !dateTime.isAfter(endDateTime);
    }

    /**
     * 验证是否为成年人（18岁以上）
     *
     * @param birthDate 出生日期
     * @return 是否为成年人
     */
    public static boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        return DateTimeUtils.getAge(birthDate) >= 18;
    }

    // ==================== 业务验证 ====================

    /**
     * 验证银行卡号格式（Luhn算法）
     *
     * @param cardNumber 银行卡号
     * @return 是否为有效银行卡号
     */
    public static boolean isValidBankCard(String cardNumber) {
        if (isEmpty(cardNumber)) {
            return false;
        }
        
        // 移除空格和连字符
        String cleanNumber = cardNumber.replaceAll("[\\s-]", "");
        
        // 检查是否只包含数字
        if (!isInteger(cleanNumber)) {
            return false;
        }
        
        // 检查长度（通常为13-19位）
        if (cleanNumber.length() < 13 || cleanNumber.length() > 19) {
            return false;
        }
        
        // Luhn算法验证
        return validateLuhn(cleanNumber);
    }

    /**
     * 验证统一社会信用代码
     *
     * @param creditCode 统一社会信用代码
     * @return 是否为有效统一社会信用代码
     */
    public static boolean isValidCreditCode(String creditCode) {
        if (isEmpty(creditCode) || creditCode.length() != 18) {
            return false;
        }
        
        // 统一社会信用代码格式验证
        Pattern pattern = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");
        return pattern.matcher(creditCode).matches();
    }

    /**
     * 验证车牌号格式
     *
     * @param plateNumber 车牌号
     * @return 是否为有效车牌号
     */
    public static boolean isValidPlateNumber(String plateNumber) {
        if (isEmpty(plateNumber)) {
            return false;
        }
        
        // 普通车牌号格式
        Pattern normalPattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$");
        
        // 新能源车牌号格式
        Pattern newEnergyPattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{5}$");
        
        return normalPattern.matcher(plateNumber).matches() || newEnergyPattern.matcher(plateNumber).matches();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 验证身份证号校验码
     *
     * @param idCard 身份证号
     * @return 校验码是否正确
     */
    private static boolean validateIdCardChecksum(String idCard) {
        if (idCard.length() != 18) {
            return false;
        }
        
        try {
            // 权重因子
            int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            
            // 校验码对应表
            char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Character.getNumericValue(idCard.charAt(i)) * weights[i];
            }
            
            int mod = sum % 11;
            char expectedCheckCode = checkCodes[mod];
            char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
            
            return expectedCheckCode == actualCheckCode;
        } catch (Exception e) {
            log.error("身份证号校验码验证失败: {}", idCard, e);
            return false;
        }
    }

    /**
     * Luhn算法验证
     *
     * @param number 数字字符串
     * @return 是否通过Luhn算法验证
     */
    private static boolean validateLuhn(String number) {
        try {
            int sum = 0;
            boolean alternate = false;
            
            for (int i = number.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(number.charAt(i));
                
                if (alternate) {
                    digit *= 2;
                    if (digit > 9) {
                        digit = (digit % 10) + 1;
                    }
                }
                
                sum += digit;
                alternate = !alternate;
            }
            
            return sum % 10 == 0;
        } catch (Exception e) {
            log.error("Luhn算法验证失败: {}", number, e);
            return false;
        }
    }

    // ==================== 组合验证 ====================

    /**
     * 验证所有条件是否都为真
     *
     * @param conditions 条件数组
     * @return 是否所有条件都为真
     */
    public static boolean allTrue(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        
        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 验证是否有任意条件为真
     *
     * @param conditions 条件数组
     * @return 是否有任意条件为真
     */
    public static boolean anyTrue(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 验证所有条件是否都为假
     *
     * @param conditions 条件数组
     * @return 是否所有条件都为假
     */
    public static boolean allFalse(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return true;
        }
        
        for (boolean condition : conditions) {
            if (condition) {
                return false;
            }
        }
        
        return true;
    }
}