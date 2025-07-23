package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 * 提供常用的字符串处理功能
 * 注意：避免与Spring的StringUtils重复，使用不同的类名或方法名
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class StringUtils {

    // 常用正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile(
        "[\u4e00-\u9fa5]"
    );
    
    private static final Pattern NUMBER_PATTERN = Pattern.compile(
        "^-?\\d+(\\.\\d+)?$"
    );
    
    private static final Pattern INTEGER_PATTERN = Pattern.compile(
        "^-?\\d+$"
    );

    // 常用字符集合
    private static final String DIGITS = "0123456789";
    private static final String LETTERS_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS = LETTERS_LOWER + LETTERS_UPPER;
    private static final String ALPHANUMERIC = LETTERS + DIGITS;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private StringUtils() {
        // 工具类，禁止实例化
    }

    // ==================== 基础判断 ====================

    /**
     * 判断字符串是否为空（null或空字符串）
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白（null、空字符串或只包含空白字符）
     *
     * @param str 字符串
     * @return 是否为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 字符串
     * @return 是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断所有字符串是否都不为空
     *
     * @param strs 字符串数组
     * @return 是否都不为空
     */
    public static boolean isAllNotEmpty(String... strs) {
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有任意字符串为空
     *
     * @param strs 字符串数组
     * @return 是否有任意字符串为空
     */
    public static boolean isAnyEmpty(String... strs) {
        if (strs == null || strs.length == 0) {
            return true;
        }
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    // ==================== 字符串处理 ====================

    /**
     * 安全的trim操作
     *
     * @param str 字符串
     * @return trim后的字符串，如果输入为null则返回null
     */
    public static String safeTrim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 默认值处理
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 如果字符串为空则返回默认值，否则返回原字符串
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 默认值处理（空白）
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 如果字符串为空白则返回默认值，否则返回原字符串
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * 截取字符串，超出长度用省略号表示
     *
     * @param str       字符串
     * @param maxLength 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    /**
     * 截取字符串，超出长度用指定后缀表示
     *
     * @param str       字符串
     * @param maxLength 最大长度
     * @param suffix    后缀
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (isEmpty(str) || maxLength <= 0) {
            return str;
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        if (isEmpty(suffix)) {
            return str.substring(0, maxLength);
        }
        
        int suffixLength = suffix.length();
        if (maxLength <= suffixLength) {
            return suffix.substring(0, maxLength);
        }
        
        return str.substring(0, maxLength - suffixLength) + suffix;
    }

    /**
     * 重复字符串
     *
     * @param str   字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int count) {
        if (isEmpty(str) || count <= 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 左填充
     *
     * @param str    字符串
     * @param length 目标长度
     * @param padStr 填充字符串
     * @return 填充后的字符串
     */
    public static String leftPad(String str, int length, String padStr) {
        if (str == null) {
            str = "";
        }
        
        if (isEmpty(padStr) || length <= str.length()) {
            return str;
        }
        
        int padLength = length - str.length();
        StringBuilder sb = new StringBuilder(length);
        
        while (sb.length() < padLength) {
            sb.append(padStr);
        }
        
        if (sb.length() > padLength) {
            sb.setLength(padLength);
        }
        
        sb.append(str);
        return sb.toString();
    }

    /**
     * 右填充
     *
     * @param str    字符串
     * @param length 目标长度
     * @param padStr 填充字符串
     * @return 填充后的字符串
     */
    public static String rightPad(String str, int length, String padStr) {
        if (str == null) {
            str = "";
        }
        
        if (isEmpty(padStr) || length <= str.length()) {
            return str;
        }
        
        int padLength = length - str.length();
        StringBuilder sb = new StringBuilder(str);
        
        while (sb.length() < length) {
            sb.append(padStr);
        }
        
        if (sb.length() > length) {
            sb.setLength(length);
        }
        
        return sb.toString();
    }

    /**
     * 反转字符串
     *
     * @param str 字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    // ==================== 大小写转换 ====================

    /**
     * 转换为驼峰命名（首字母小写）
     *
     * @param str 字符串
     * @return 驼峰命名字符串
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        String[] words = str.split("[_\\-\\s]+");
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                sb.append(word);
            } else {
                sb.append(capitalize(word));
            }
        }
        
        return sb.toString();
    }

    /**
     * 转换为帕斯卡命名（首字母大写）
     *
     * @param str 字符串
     * @return 帕斯卡命名字符串
     */
    public static String toPascalCase(String str) {
        String camelCase = toCamelCase(str);
        return capitalize(camelCase);
    }

    /**
     * 转换为下划线命名
     *
     * @param str 字符串
     * @return 下划线命名字符串
     */
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                  .replaceAll("[\\s\\-]+", "_")
                  .toLowerCase();
    }

    /**
     * 转换为短横线命名
     *
     * @param str 字符串
     * @return 短横线命名字符串
     */
    public static String toKebabCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("([a-z])([A-Z])", "$1-$2")
                  .replaceAll("[\\s_]+", "-")
                  .toLowerCase();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return Character.toUpperCase(firstChar) + str.substring(1);
        }
        
        return str;
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return Character.toLowerCase(firstChar) + str.substring(1);
        }
        
        return str;
    }

    // ==================== 验证 ====================

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否为有效邮箱
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式（中国大陆）
     *
     * @param phone 手机号
     * @return 是否为有效手机号
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证身份证号格式（中国大陆18位）
     *
     * @param idCard 身份证号
     * @return 是否为有效身份证号
     */
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        return ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 判断字符串是否包含中文
     *
     * @param str 字符串
     * @return 是否包含中文
     */
    public static boolean containsChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return CHINESE_PATTERN.matcher(str).find();
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符串是否为整数
     *
     * @param str 字符串
     * @return 是否为整数
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return INTEGER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符串是否只包含字母
     *
     * @param str 字符串
     * @return 是否只包含字母
     */
    public static boolean isAlpha(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否只包含字母和数字
     *
     * @param str 字符串
     * @return 是否只包含字母和数字
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // ==================== 字符串生成 ====================

    /**
     * 生成随机字符串
     *
     * @param length 长度
     * @param chars  字符集合
     * @return 随机字符串
     */
    public static String randomString(int length, String chars) {
        if (length <= 0 || isEmpty(chars)) {
            return "";
        }
        
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        return randomString(length, DIGITS);
    }

    /**
     * 生成随机字母字符串
     *
     * @param length 长度
     * @return 随机字母字符串
     */
    public static String randomAlpha(int length) {
        return randomString(length, LETTERS);
    }

    /**
     * 生成随机字母数字字符串
     *
     * @param length 长度
     * @return 随机字母数字字符串
     */
    public static String randomAlphanumeric(int length) {
        return randomString(length, ALPHANUMERIC);
    }

    // ==================== 字符串分割和连接 ====================

    /**
     * 安全的字符串分割
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @return 分割后的数组，如果输入为空则返回空数组
     */
    public static String[] safeSplit(String str, String delimiter) {
        if (isEmpty(str)) {
            return new String[0];
        }
        return str.split(Pattern.quote(delimiter));
    }

    /**
     * 连接字符串数组
     *
     * @param delimiter 分隔符
     * @param elements  字符串数组
     * @return 连接后的字符串
     */
    public static String join(String delimiter, String... elements) {
        if (elements == null || elements.length == 0) {
            return "";
        }
        
        return Arrays.stream(elements)
                     .filter(Objects::nonNull)
                     .collect(Collectors.joining(delimiter));
    }

    /**
     * 连接字符串集合
     *
     * @param delimiter 分隔符
     * @param elements  字符串集合
     * @return 连接后的字符串
     */
    public static String join(String delimiter, Collection<String> elements) {
        if (elements == null || elements.isEmpty()) {
            return "";
        }
        
        return elements.stream()
                      .filter(Objects::nonNull)
                      .collect(Collectors.joining(delimiter));
    }

    // ==================== 字符串比较 ====================

    /**
     * 安全的字符串比较
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 比较结果
     */
    public static boolean safeEquals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    /**
     * 忽略大小写的字符串比较
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 比较结果
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    // ==================== 字符串编码 ====================

    /**
     * 获取字符串的UTF-8字节数组
     *
     * @param str 字符串
     * @return UTF-8字节数组
     */
    public static byte[] getUtf8Bytes(String str) {
        if (str == null) {
            return new byte[0];
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 从UTF-8字节数组创建字符串
     *
     * @param bytes UTF-8字节数组
     * @return 字符串
     */
    public static String fromUtf8Bytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ==================== 字符串清理 ====================

    /**
     * 移除字符串中的HTML标签
     *
     * @param str 字符串
     * @return 移除HTML标签后的字符串
     */
    public static String removeHtmlTags(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("<[^>]+>", "");
    }

    /**
     * 移除字符串中的特殊字符
     *
     * @param str 字符串
     * @return 移除特殊字符后的字符串
     */
    public static String removeSpecialChars(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5\\s]", "");
    }

    /**
     * 移除字符串中的空白字符
     *
     * @param str 字符串
     * @return 移除空白字符后的字符串
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * 标准化空白字符（将多个连续空白字符替换为单个空格）
     *
     * @param str 字符串
     * @return 标准化后的字符串
     */
    public static String normalizeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    /**
     * 计算字符串的显示宽度（中文字符算2个宽度，英文字符算1个宽度）
     *
     * @param str 字符串
     * @return 显示宽度
     */
    public static int getDisplayWidth(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        
        int width = 0;
        for (char c : str.toCharArray()) {
            if (c >= 0x4e00 && c <= 0x9fa5) {
                // 中文字符
                width += 2;
            } else {
                // 其他字符
                width += 1;
            }
        }
        return width;
    }
}