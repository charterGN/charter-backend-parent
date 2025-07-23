package ink.charter.website.common.redis.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务接口
 * 定义Redis常用操作方法
 *
 * @author charter
 * @create 2025/07/19
 */
public interface RedisService {

    // =============================通用操作=============================

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    Boolean delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    Long delete(Collection<String> keys);

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    Boolean hasKey(String key);

    /**
     * 设置过期时间
     *
     * @param key    键
     * @param expire 过期时间（秒）
     * @return 是否设置成功
     */
    Boolean expire(String key, long expire);

    /**
     * 设置过期时间
     *
     * @param key      键
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    Boolean expire(String key, long expire, TimeUnit timeUnit);

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间（秒）
     */
    Long getExpire(String key);

    // =============================String操作=============================

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置缓存并指定过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间（秒）
     */
    void set(String key, Object value, long expire);

    /**
     * 设置缓存并指定过期时间和时间单位
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    void set(String key, Object value, long expire, TimeUnit timeUnit);

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 获取缓存并转换为指定类型
     *
     * @param key   键
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 值
     */
    <T> T get(String key, Class<T> clazz);

    // =============================String专用操作（使用StringRedisTemplate优化）=============================

    /**
     * 设置字符串缓存
     *
     * @param key   键
     * @param value 字符串值
     */
    void setString(String key, String value);

    /**
     * 设置字符串缓存并指定过期时间
     *
     * @param key    键
     * @param value  字符串值
     * @param expire 过期时间（秒）
     */
    void setString(String key, String value, long expire);

    /**
     * 设置字符串缓存并指定过期时间和时间单位
     *
     * @param key      键
     * @param value    字符串值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    void setString(String key, String value, long expire, TimeUnit timeUnit);

    /**
     * 获取字符串缓存
     *
     * @param key 键
     * @return 字符串值
     */
    String getString(String key);

    /**
     * 字符串值递增
     *
     * @param key 键
     * @return 递增后的值
     */
    Long increment(String key);

    /**
     * 字符串值递增指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递增后的值
     */
    Long increment(String key, long delta);

    /**
     * 字符串值递减
     *
     * @param key 键
     * @return 递减后的值
     */
    Long decrement(String key);

    /**
     * 字符串值递减指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递减后的值
     */
    Long decrement(String key, long delta);

    // =============================Hash操作=============================

    /**
     * 设置Hash缓存
     *
     * @param key     键
     * @param hashKey Hash键
     * @param value   值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 获取Hash缓存
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 值
     */
    Object hGet(String key, String hashKey);

    /**
     * 获取Hash缓存并转换为指定类型
     *
     * @param key     键
     * @param hashKey Hash键
     * @param clazz   目标类型
     * @param <T>     泛型
     * @return 值
     */
    <T> T hGet(String key, String hashKey, Class<T> clazz);

    /**
     * 批量设置Hash缓存
     *
     * @param key 键
     * @param map 键值对
     */
    void hSetAll(String key, Map<String, Object> map);

    /**
     * 获取所有Hash缓存
     *
     * @param key 键
     * @return 键值对
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 删除Hash缓存
     *
     * @param key      键
     * @param hashKeys Hash键
     * @return 删除的数量
     */
    Long hDelete(String key, Object... hashKeys);

    /**
     * 判断Hash键是否存在
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 是否存在
     */
    Boolean hHasKey(String key, String hashKey);

    // =============================List操作=============================

    /**
     * 从左侧推入List
     *
     * @param key   键
     * @param value 值
     * @return List长度
     */
    Long lLeftPush(String key, Object value);

    /**
     * 从右侧推入List
     *
     * @param key   键
     * @param value 值
     * @return List长度
     */
    Long lRightPush(String key, Object value);

    /**
     * 从左侧弹出List
     *
     * @param key 键
     * @return 值
     */
    Object lLeftPop(String key);

    /**
     * 从右侧弹出List
     *
     * @param key 键
     * @return 值
     */
    Object lRightPop(String key);

    /**
     * 获取List范围内的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素列表
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List长度
     *
     * @param key 键
     * @return 长度
     */
    Long lSize(String key);

    // =============================Set操作=============================

    /**
     * 添加Set元素
     *
     * @param key    键
     * @param values 值
     * @return 添加的数量
     */
    Long sAdd(String key, Object... values);

    /**
     * 移除Set元素
     *
     * @param key    键
     * @param values 值
     * @return 移除的数量
     */
    Long sRemove(String key, Object... values);

    /**
     * 判断Set是否包含元素
     *
     * @param key   键
     * @param value 值
     * @return 是否包含
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set所有元素
     *
     * @param key 键
     * @return 元素集合
     */
    Set<Object> sMembers(String key);

    /**
     * 获取Set大小
     *
     * @param key 键
     * @return 大小
     */
    Long sSize(String key);

    // =============================ZSet操作=============================

    /**
     * 添加ZSet元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    Boolean zAdd(String key, Object value, double score);

    /**
     * 移除ZSet元素
     *
     * @param key    键
     * @param values 值
     * @return 移除的数量
     */
    Long zRemove(String key, Object... values);

    /**
     * 获取ZSet范围内的元素（按分数排序）
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素集合
     */
    Set<Object> zRange(String key, long start, long end);

    /**
     * 获取ZSet大小
     *
     * @param key 键
     * @return 大小
     */
    Long zSize(String key);

    // =============================分布式锁操作=============================

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁键
     * @param requestId  请求ID
     * @param expireTime 过期时间（毫秒）
     * @return 是否获取成功
     */
    Boolean tryLock(String lockKey, String requestId, long expireTime);

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁键
     * @param requestId 请求ID
     * @return 是否释放成功
     */
    Boolean releaseLock(String lockKey, String requestId);
}