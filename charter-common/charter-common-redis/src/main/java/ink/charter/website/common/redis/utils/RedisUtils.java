package ink.charter.website.common.redis.utils;

import ink.charter.website.common.redis.service.RedisService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 提供静态方法调用Redis服务
 *
 * @author charter
 * @create 2025/07/19
 */
@Component
public class RedisUtils implements ApplicationContextAware {

    private static RedisService redisService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisService = applicationContext.getBean(RedisService.class);
    }

    // =============================通用操作=============================

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    public static Boolean delete(String key) {
        return redisService.delete(key);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    public static Long delete(Collection<String> keys) {
        return redisService.delete(keys);
    }

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public static Boolean hasKey(String key) {
        return redisService.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key    键
     * @param expire 过期时间（秒）
     * @return 是否设置成功
     */
    public static Boolean expire(String key, long expire) {
        return redisService.expire(key, expire);
    }

    /**
     * 设置过期时间
     *
     * @param key      键
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public static Boolean expire(String key, long expire, TimeUnit timeUnit) {
        return redisService.expire(key, expire, timeUnit);
    }

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间（秒）
     */
    public static Long getExpire(String key) {
        return redisService.getExpire(key);
    }

    // =============================String操作=============================

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        redisService.set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间（秒）
     */
    public static void set(String key, Object value, long expire) {
        redisService.set(key, value, expire);
    }

    /**
     * 设置缓存并指定过期时间和时间单位
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public static void set(String key, Object value, long expire, TimeUnit timeUnit) {
        redisService.set(key, value, expire, timeUnit);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return redisService.get(key);
    }

    /**
     * 获取缓存并转换为指定类型
     *
     * @param key   键
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 值
     */
    public static <T> T get(String key, Class<T> clazz) {
        return redisService.get(key, clazz);
    }

    // =============================String专用操作（使用StringRedisTemplate优化）=============================

    /**
     * 设置字符串缓存
     *
     * @param key   键
     * @param value 字符串值
     */
    public static void setString(String key, String value) {
        redisService.setString(key, value);
    }

    /**
     * 设置字符串缓存并指定过期时间
     *
     * @param key    键
     * @param value  字符串值
     * @param expire 过期时间（秒）
     */
    public static void setString(String key, String value, long expire) {
        redisService.setString(key, value, expire);
    }

    /**
     * 设置字符串缓存并指定过期时间和时间单位
     *
     * @param key      键
     * @param value    字符串值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public static void setString(String key, String value, long expire, TimeUnit timeUnit) {
        redisService.setString(key, value, expire, timeUnit);
    }

    /**
     * 获取字符串缓存
     *
     * @param key 键
     * @return 字符串值
     */
    public static String getString(String key) {
        return redisService.getString(key);
    }

    /**
     * 字符串值递增
     *
     * @param key 键
     * @return 递增后的值
     */
    public static Long increment(String key) {
        return redisService.increment(key);
    }

    /**
     * 字符串值递增指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递增后的值
     */
    public static Long increment(String key, long delta) {
        return redisService.increment(key, delta);
    }

    /**
     * 字符串值递减
     *
     * @param key 键
     * @return 递减后的值
     */
    public static Long decrement(String key) {
        return redisService.decrement(key);
    }

    /**
     * 字符串值递减指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递减后的值
     */
    public static Long decrement(String key, long delta) {
        return redisService.decrement(key, delta);
    }

    // =============================Hash操作=============================

    /**
     * 设置Hash缓存
     *
     * @param key     键
     * @param hashKey Hash键
     * @param value   值
     */
    public static void hSet(String key, String hashKey, Object value) {
        redisService.hSet(key, hashKey, value);
    }

    /**
     * 获取Hash缓存
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 值
     */
    public static Object hGet(String key, String hashKey) {
        return redisService.hGet(key, hashKey);
    }

    /**
     * 获取Hash缓存并转换为指定类型
     *
     * @param key     键
     * @param hashKey Hash键
     * @param clazz   目标类型
     * @param <T>     泛型
     * @return 值
     */
    public static <T> T hGet(String key, String hashKey, Class<T> clazz) {
        return redisService.hGet(key, hashKey, clazz);
    }

    /**
     * 批量设置Hash缓存
     *
     * @param key 键
     * @param map 键值对
     */
    public static void hSetAll(String key, Map<String, Object> map) {
        redisService.hSetAll(key, map);
    }

    /**
     * 获取所有Hash缓存
     *
     * @param key 键
     * @return 键值对
     */
    public static Map<Object, Object> hGetAll(String key) {
        return redisService.hGetAll(key);
    }

    /**
     * 删除Hash缓存
     *
     * @param key      键
     * @param hashKeys Hash键
     * @return 删除的数量
     */
    public static Long hDelete(String key, Object... hashKeys) {
        return redisService.hDelete(key, hashKeys);
    }

    /**
     * 判断Hash键是否存在
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 是否存在
     */
    public static Boolean hHasKey(String key, String hashKey) {
        return redisService.hHasKey(key, hashKey);
    }

    // =============================List操作=============================

    /**
     * 从左侧推入List
     *
     * @param key   键
     * @param value 值
     * @return List长度
     */
    public static Long lLeftPush(String key, Object value) {
        return redisService.lLeftPush(key, value);
    }

    /**
     * 从右侧推入List
     *
     * @param key   键
     * @param value 值
     * @return List长度
     */
    public static Long lRightPush(String key, Object value) {
        return redisService.lRightPush(key, value);
    }

    /**
     * 从左侧弹出List
     *
     * @param key 键
     * @return 值
     */
    public static Object lLeftPop(String key) {
        return redisService.lLeftPop(key);
    }

    /**
     * 从右侧弹出List
     *
     * @param key 键
     * @return 值
     */
    public static Object lRightPop(String key) {
        return redisService.lRightPop(key);
    }

    /**
     * 获取List范围内的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素列表
     */
    public static List<Object> lRange(String key, long start, long end) {
        return redisService.lRange(key, start, end);
    }

    /**
     * 获取List长度
     *
     * @param key 键
     * @return 长度
     */
    public static Long lSize(String key) {
        return redisService.lSize(key);
    }

    // =============================Set操作=============================

    /**
     * 添加Set元素
     *
     * @param key    键
     * @param values 值
     * @return 添加的数量
     */
    public static Long sAdd(String key, Object... values) {
        return redisService.sAdd(key, values);
    }

    /**
     * 移除Set元素
     *
     * @param key    键
     * @param values 值
     * @return 移除的数量
     */
    public static Long sRemove(String key, Object... values) {
        return redisService.sRemove(key, values);
    }

    /**
     * 判断Set是否包含元素
     *
     * @param key   键
     * @param value 值
     * @return 是否包含
     */
    public static Boolean sIsMember(String key, Object value) {
        return redisService.sIsMember(key, value);
    }

    /**
     * 获取Set所有元素
     *
     * @param key 键
     * @return 元素集合
     */
    public static Set<Object> sMembers(String key) {
        return redisService.sMembers(key);
    }

    /**
     * 获取Set大小
     *
     * @param key 键
     * @return 大小
     */
    public static Long sSize(String key) {
        return redisService.sSize(key);
    }

    // =============================ZSet操作=============================

    /**
     * 添加ZSet元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    public static Boolean zAdd(String key, Object value, double score) {
        return redisService.zAdd(key, value, score);
    }

    /**
     * 移除ZSet元素
     *
     * @param key    键
     * @param values 值
     * @return 移除的数量
     */
    public static Long zRemove(String key, Object... values) {
        return redisService.zRemove(key, values);
    }

    /**
     * 获取ZSet范围内的元素（按分数排序）
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素集合
     */
    public static Set<Object> zRange(String key, long start, long end) {
        return redisService.zRange(key, start, end);
    }

    /**
     * 获取ZSet大小
     *
     * @param key 键
     * @return 大小
     */
    public static Long zSize(String key) {
        return redisService.zSize(key);
    }

    // =============================分布式锁操作=============================

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁键
     * @param requestId  请求ID
     * @param expireTime 过期时间（毫秒）
     * @return 是否获取成功
     */
    public static Boolean tryLock(String lockKey, String requestId, long expireTime) {
        return redisService.tryLock(lockKey, requestId, expireTime);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁键
     * @param requestId 请求ID
     * @return 是否释放成功
     */
    public static Boolean releaseLock(String lockKey, String requestId) {
        return redisService.releaseLock(lockKey, requestId);
    }
}