package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import com.github.jarod.qqwry.QQWry;
import com.github.jarod.qqwry.IPZone;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * IP工具类
 * 提供IP地址相关的实用功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";
    
    // IPv4地址正则表达式
    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    );
    
    // 内网IP地址正则表达式
    private static final Pattern INTERNAL_IP_PATTERN = Pattern.compile(
        "^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$"
    );
    
    // 纯真IP数据库实例（懒加载）
    private static volatile QQWry qqWry;

    private IpUtils() {
        // 工具类，禁止实例化
    }
    
    /**
     * 获取QQWry实例（懒加载，线程安全）
     *
     * @return QQWry实例
     */
    private static QQWry getQQWry() {
        if (qqWry == null) {
            synchronized (IpUtils.class) {
                if (qqWry == null) {
                    try {
                        qqWry = new QQWry();
                        log.info("纯真IP数据库初始化成功");
                    } catch (Exception e) {
                        log.error("纯真IP数据库初始化失败", e);
                    }
                }
            }
        }
        return qqWry;
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String ip = null;
        
        // 1. 检查X-Forwarded-For头（代理服务器传递的原始客户端IP）
        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // X-Forwarded-For可能包含多个IP，取第一个
            if (ip.contains(SEPARATOR)) {
                ip = ip.split(SEPARATOR)[0].trim();
            }
            return ip;
        }
        
        // 2. 检查X-Real-IP头（Nginx代理传递的真实IP）
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 3. 检查Proxy-Client-IP头
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 4. 检查WL-Proxy-Client-IP头（WebLogic代理）
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 5. 检查HTTP_CLIENT_IP头
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 6. 检查HTTP_X_FORWARDED_FOR头
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 7. 最后使用request.getRemoteAddr()
        ip = request.getRemoteAddr();
        
        // 如果是本地回环地址，尝试获取本机真实IP
        if (LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip)) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ip = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                log.warn("获取本机IP失败", e);
            }
        }
        
        return ip;
    }

    /**
     * 判断IP是否有效
     *
     * @param ip IP地址
     * @return 是否有效
     */
    private static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 验证IPv4地址格式
     *
     * @param ip IP地址
     * @return 是否为有效的IPv4地址
     */
    public static boolean isValidIPv4(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * 判断是否为内网IP
     *
     * @param ip IP地址
     * @return 是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }
        return INTERNAL_IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 判断是否为外网IP
     *
     * @param ip IP地址
     * @return 是否为外网IP
     */
    public static boolean isExternalIp(String ip) {
        return isValidIPv4(ip) && !isInternalIp(ip);
    }

    /**
     * 获取本机IP地址
     *
     * @return 本机IP地址
     */
    public static String getLocalIp() {
        try {
            // 优先获取非回环地址的网卡IP
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                
                // 跳过回环网卡和未启用的网卡
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    
                    // 跳过回环地址和IPv6地址
                    if (inetAddress.isLoopbackAddress() || !inetAddress.isSiteLocalAddress()) {
                        continue;
                    }
                    
                    String ip = inetAddress.getHostAddress();
                    if (isValidIPv4(ip)) {
                        return ip;
                    }
                }
            }
            
            // 如果没有找到合适的IP，返回默认的本机IP
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
            
        } catch (SocketException | UnknownHostException e) {
            log.error("获取本机IP失败", e);
            return LOCALHOST_IPV4;
        }
    }

    /**
     * 获取本机主机名
     *
     * @return 本机主机名
     */
    public static String getLocalHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            log.error("获取本机主机名失败", e);
            return "unknown";
        }
    }

    /**
     * 将IP地址转换为长整型
     *
     * @param ip IP地址
     * @return 长整型IP
     */
    public static long ipToLong(String ip) {
        if (!isValidIPv4(ip)) {
            throw new IllegalArgumentException("无效的IP地址: " + ip);
        }
        
        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = result * 256 + Integer.parseInt(parts[i]);
        }
        return result;
    }

    /**
     * 将长整型转换为IP地址
     *
     * @param longIp 长整型IP
     * @return IP地址
     */
    public static String longToIp(long longIp) {
        return ((longIp >> 24) & 0xFF) + "." +
               ((longIp >> 16) & 0xFF) + "." +
               ((longIp >> 8) & 0xFF) + "." +
               (longIp & 0xFF);
    }

    /**
     * 判断IP是否在指定网段内
     *
     * @param ip     要检查的IP
     * @param subnet 网段（如：192.168.1.0/24）
     * @return 是否在网段内
     */
    public static boolean isIpInSubnet(String ip, String subnet) {
        if (!isValidIPv4(ip) || !StringUtils.hasText(subnet)) {
            return false;
        }
        
        try {
            String[] parts = subnet.split("/");
            if (parts.length != 2) {
                return false;
            }
            
            String networkIp = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);
            
            if (!isValidIPv4(networkIp) || prefixLength < 0 || prefixLength > 32) {
                return false;
            }
            
            long ipLong = ipToLong(ip);
            long networkLong = ipToLong(networkIp);
            long mask = (-1L << (32 - prefixLength));
            
            return (ipLong & mask) == (networkLong & mask);
            
        } catch (NumberFormatException e) {
            log.error("解析网段失败: {}", subnet, e);
            return false;
        }
    }

    /**
     * 获取IP地址的地理位置信息（使用纯真IP数据库）
     *
     * @param ip IP地址
     * @return 地理位置信息
     */
    public static String getIpLocation(String ip) {
        if (!isValidIPv4(ip)) {
            return "未知";
        }
        
        // 判断是否为内网IP
        if (isInternalIp(ip)) {
            return "内网IP";
        }
        
        try {
            QQWry qqWryInstance = getQQWry();
            if (qqWryInstance == null) {
                return "未知";
            }
            
            IPZone ipZone = qqWryInstance.findIP(ip);
            if (ipZone != null) {
                String country = ipZone.getMainInfo();
                String area = ipZone.getSubInfo();
                
                // 处理返回结果
                if (StringUtils.hasText(country) && StringUtils.hasText(area)) {
                    // 如果区域信息和国家信息相同，只返回国家信息
                    if (country.equals(area)) {
                        return country;
                    }
                    // 如果区域信息包含在国家信息中，只返回国家信息
                    if (country.contains(area) || area.contains(country)) {
                        return country.length() > area.length() ? country : area;
                    }
                    return country + " " + area;
                } else if (StringUtils.hasText(country)) {
                    return country;
                } else if (StringUtils.hasText(area)) {
                    return area;
                }
            }
        } catch (Exception e) {
            log.error("查询IP地理位置失败: {}", ip, e);
        }
        
        return "未知";
    }
    
    /**
     * 获取IP地址的详细地理位置信息（使用纯真IP数据库）
     *
     * @param ip IP地址
     * @return IPZone对象，包含详细的地理位置信息
     */
    public static IPZone getIpLocationDetail(String ip) {
        if (!isValidIPv4(ip)) {
            return null;
        }
        
        // 判断是否为内网IP
        if (isInternalIp(ip)) {
            return null;
        }
        
        try {
            QQWry qqWryInstance = getQQWry();
            if (qqWryInstance != null) {
                return qqWryInstance.findIP(ip);
            }
        } catch (Exception e) {
            log.error("查询IP详细地理位置失败: {}", ip, e);
        }
        
        return null;
    }

    /**
     * 隐藏IP地址的部分信息（用于日志记录等场景）
     *
     * @param ip IP地址
     * @return 隐藏部分信息的IP地址
     */
    public static String maskIp(String ip) {
        if (!isValidIPv4(ip)) {
            return ip;
        }
        
        String[] parts = ip.split("\\.");
        if (parts.length == 4) {
            return parts[0] + "." + parts[1] + ".***.**";
        }
        
        return ip;
    }
}