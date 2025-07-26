package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 加密工具类
 * 提供常用的加密解密功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class CryptoUtils {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String MD5_ALGORITHM = "MD5";
    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String SHA512_ALGORITHM = "SHA-512";

    private CryptoUtils() {
        // 工具类，禁止实例化
    }

    // ==================== MD5加密 ====================

    /**
     * MD5加密
     *
     * @param data 原始数据
     * @return MD5加密后的十六进制字符串
     */
    public static String md5(String data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5加密失败", e);
            return null;
        }
    }

    /**
     * MD5加密（加盐）
     *
     * @param data 原始数据
     * @param salt 盐值
     * @return MD5加密后的十六进制字符串
     */
    public static String md5WithSalt(String data, String salt) {
        if (data == null || salt == null) {
            return null;
        }
        return md5(data + salt);
    }

    // ==================== SHA加密 ====================

    /**
     * SHA-256加密
     *
     * @param data 原始数据
     * @return SHA-256加密后的十六进制字符串
     */
    public static String sha256(String data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM);
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256加密失败", e);
            return null;
        }
    }

    /**
     * SHA-512加密
     *
     * @param data 原始数据
     * @return SHA-512加密后的十六进制字符串
     */
    public static String sha512(String data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA512_ALGORITHM);
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-512加密失败", e);
            return null;
        }
    }

    // ==================== AES加密解密 ====================

    /**
     * AES解密密码
     *
     * @param encryptedPassword 前端加密后的密码（Base64编码）
     * @param secretKey 密钥
     * @return 解密后的原始密码
     */
    public static String decryptPassword(String encryptedPassword, String secretKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("密码解密失败", e);
            return null;
        }
    }

    /**
     * 生成AES密钥
     *
     * @return Base64编码的AES密钥
     */
    public static String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            log.error("生成AES密钥失败", e);
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param data 原始数据
     * @param key  Base64编码的密钥
     * @return Base64编码的加密数据
     */
    public static String aesEncrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param encryptedData Base64编码的加密数据
     * @param key           Base64编码的密钥
     * @return 解密后的原始数据
     */
    public static String aesDecrypt(String encryptedData, String key) {
        if (encryptedData == null || key == null) {
            return null;
        }
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密失败", e);
            return null;
        }
    }

    // ==================== Base64编码解码 ====================

    /**
     * Base64编码
     *
     * @param data 原始数据
     * @return Base64编码后的字符串
     */
    public static String base64Encode(String data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解码
     *
     * @param encodedData Base64编码的数据
     * @return 解码后的原始字符串
     */
    public static String base64Decode(String encodedData) {
        if (encodedData == null) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.error("Base64解码失败", e);
            return null;
        }
    }

    // ==================== 随机数生成 ====================

    /**
     * 生成随机盐值
     *
     * @param length 盐值长度
     * @return 随机盐值
     */
    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串（包含字母和数字）
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // ==================== 工具方法 ====================

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * 验证MD5
     *
     * @param data     原始数据
     * @param md5Hash  MD5哈希值
     * @return 是否匹配
     */
    public static boolean verifyMd5(String data, String md5Hash) {
        if (data == null || md5Hash == null) {
            return false;
        }
        String calculatedHash = md5(data);
        return md5Hash.equalsIgnoreCase(calculatedHash);
    }

    /**
     * 验证SHA-256
     *
     * @param data       原始数据
     * @param sha256Hash SHA-256哈希值
     * @return 是否匹配
     */
    public static boolean verifySha256(String data, String sha256Hash) {
        if (data == null || sha256Hash == null) {
            return false;
        }
        String calculatedHash = sha256(data);
        return sha256Hash.equalsIgnoreCase(calculatedHash);
    }
}