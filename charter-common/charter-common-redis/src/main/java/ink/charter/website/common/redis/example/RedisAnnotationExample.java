package ink.charter.website.common.redis.example;

import ink.charter.website.common.redis.annotation.RedisCache;
import ink.charter.website.common.redis.annotation.RedisCacheEvict;
import ink.charter.website.common.redis.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis注解使用示例
 * 展示如何使用@RedisCache、@RedisCacheEvict、@RedisLock注解
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
//@Service
public class RedisAnnotationExample {

    /**
     * 缓存示例 - 使用默认配置
     * 缓存key会自动生成，过期时间1小时
     */
    @RedisCache
    public String getUserById(Long userId) {
        log.info("从数据库查询用户: {}", userId);
        // 模拟数据库查询
        return "User-" + userId;
    }

    /**
     * 缓存示例 - 自定义key和过期时间
     * 使用SpEL表达式生成key，缓存30分钟
     */
    @RedisCache(key = "'user:profile:' + #userId", expire = 30, timeUnit = TimeUnit.MINUTES)
    public String getUserProfile(Long userId) {
        log.info("从数据库查询用户资料: {}", userId);
        return "UserProfile-" + userId;
    }

    /**
     * 条件缓存示例
     * 只有当userId大于0时才缓存
     */
    @RedisCache(key = "'user:info:' + #userId", condition = "#userId > 0")
    public String getUserInfo(Long userId) {
        log.info("查询用户信息: {}", userId);
        if (userId <= 0) {
            return null;
        }
        return "UserInfo-" + userId;
    }

    /**
     * 排除缓存示例
     * 当返回结果包含"admin"时不缓存
     */
    @RedisCache(key = "'user:role:' + #userId", unless = "#result != null and #result.contains('admin')")
    public String getUserRole(Long userId) {
        log.info("查询用户角色: {}", userId);
        return userId == 1L ? "admin" : "user";
    }

    /**
     * 缓存清除示例 - 清除指定key
     */
    @RedisCacheEvict(key = "'user:profile:' + #userId")
    public void updateUserProfile(Long userId, String profile) {
        log.info("更新用户资料: {}, {}", userId, profile);
        // 模拟更新数据库
    }

    /**
     * 批量缓存清除示例 - 使用模式匹配
     */
    @RedisCacheEvict(keyPattern = "'user:*:' + #userId")
    public void updateUser(Long userId) {
        log.info("更新用户信息: {}", userId);
        // 这会清除所有匹配 user:*:userId 模式的缓存
    }

    /**
     * 方法执行前清除缓存
     */
    @RedisCacheEvict(key = "'user:cache:' + #userId", beforeInvocation = true)
    public void deleteUser(Long userId) {
        log.info("删除用户: {}", userId);
        // 即使方法执行失败，缓存也会被清除
    }

    /**
     * 条件清除缓存
     */
    @RedisCacheEvict(key = "'user:temp:' + #userId", condition = "#force == true")
    public void clearUserCache(Long userId, boolean force) {
        log.info("清除用户缓存: {}, force: {}", userId, force);
    }

    /**
     * 分布式锁示例 - 默认配置
     * 锁key自动生成，过期时间30秒，获取失败抛异常
     */
    @RedisLock(key = "'order:lock:' + #orderId")
    public String processOrder(String orderId) {
        log.info("处理订单: {}", orderId);
        // 模拟业务处理
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Order-" + orderId + "-processed";
    }

    /**
     * 分布式锁示例 - 自定义key和等待时间
     * 使用SpEL表达式生成锁key，等待5秒
     */
    @RedisLock(key = "'order:lock:' + #orderId", waitTime = 5, timeUnit = TimeUnit.SECONDS)
    public String processOrderWithWait(String orderId) {
        log.info("处理订单（带等待）: {}", orderId);
        return "Order-" + orderId + "-processed-with-wait";
    }

    /**
     * 分布式锁示例 - 获取失败返回null
     */
    @RedisLock(
        key = "'payment:lock:' + #paymentId", 
        failStrategy = RedisLock.LockFailStrategy.RETURN_NULL,
        failMessage = "支付处理中，请稍后重试"
    )
    public String processPayment(String paymentId) {
        log.info("处理支付: {}", paymentId);
        return "Payment-" + paymentId + "-processed";
    }

    /**
     * 分布式锁示例 - 获取失败忽略锁
     */
    @RedisLock(
        key = "'log:lock:' + #logId", 
        failStrategy = RedisLock.LockFailStrategy.IGNORE
    )
    public void writeLog(String logId, String content) {
        log.info("写入日志: {}, {}", logId, content);
        // 即使获取锁失败，也会执行方法
    }

    /**
     * 组合使用示例 - 缓存 + 分布式锁
     */
    @RedisCache(key = "'expensive:calc:' + #input", expire = 1, timeUnit = TimeUnit.HOURS)
    @RedisLock(key = "'calc:lock:' + #input", waitTime = 10, timeUnit = TimeUnit.SECONDS)
    public String expensiveCalculation(String input) {
        log.info("执行复杂计算: {}", input);
        // 模拟复杂计算
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result-" + input + "-" + System.currentTimeMillis();
    }
}