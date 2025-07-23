package ink.charter.website.common.redis.service.impl;

import ink.charter.website.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    // 分布式锁释放脚本
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "    return redis.call('del', KEYS[1]) " +
        "else " +
        "    return 0 " +
        "end";

    // =============================通用操作=============================

    @Override
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long delete(Collection<String> keys) {
        try {
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            log.error("Redis批量delete操作失败, keys: {}", keys, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis hasKey操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Boolean expire(String key, long expire) {
        try {
            return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis expire操作失败, key: {}, expire: {}", key, expire, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Boolean expire(String key, long expire, TimeUnit timeUnit) {
        try {
            return redisTemplate.expire(key, expire, timeUnit);
        } catch (Exception e) {
            log.error("Redis expire操作失败, key: {}, expire: {}, timeUnit: {}", key, expire, timeUnit, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis getExpire操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================String操作=============================

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis set操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public void set(String key, Object value, long expire) {
        try {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis set操作失败, key: {}, expire: {}", key, expire, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, expire, timeUnit);
        } catch (Exception e) {
            log.error("Redis set操作失败, key: {}, expire: {}, timeUnit: {}", key, expire, timeUnit, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            throw new ClassCastException("无法将值转换为指定类型: " + clazz.getName());
        } catch (Exception e) {
            log.error("Redis get操作失败, key: {}, clazz: {}", key, clazz.getName(), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================String专用操作（使用StringRedisTemplate优化）=============================

    @Override
    public void setString(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis setString操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public void setString(String key, String value, long expire) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis setString操作失败, key: {}, expire: {}", key, expire, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public void setString(String key, String value, long expire, TimeUnit timeUnit) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
        } catch (Exception e) {
            log.error("Redis setString操作失败, key: {}, expire: {}, timeUnit: {}", key, expire, timeUnit, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public String getString(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis getString操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long increment(String key) {
        try {
            return stringRedisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("Redis increment操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long increment(String key, long delta) {
        try {
            return stringRedisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("Redis increment操作失败, key: {}, delta: {}", key, delta, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long decrement(String key) {
        try {
            return stringRedisTemplate.opsForValue().decrement(key);
        } catch (Exception e) {
            log.error("Redis decrement操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long decrement(String key, long delta) {
        try {
            return stringRedisTemplate.opsForValue().decrement(key, delta);
        } catch (Exception e) {
            log.error("Redis decrement操作失败, key: {}, delta: {}", key, delta, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================Hash操作=============================

    @Override
    public void hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            log.error("Redis hSet操作失败, key: {}, hashKey: {}", key, hashKey, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Object hGet(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error("Redis hGet操作失败, key: {}, hashKey: {}", key, hashKey, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String hashKey, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForHash().get(key, hashKey);
            if (value == null) {
                return null;
            }
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            throw new ClassCastException("无法将值转换为指定类型: " + clazz.getName());
        } catch (Exception e) {
            log.error("Redis hGet操作失败, key: {}, hashKey: {}, clazz: {}", key, hashKey, clazz.getName(), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            log.error("Redis hSetAll操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Redis hGetAll操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long hDelete(String key, Object... hashKeys) {
        try {
            return redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            log.error("Redis hDelete操作失败, key: {}, hashKeys: {}", key, Arrays.toString(hashKeys), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            log.error("Redis hHasKey操作失败, key: {}, hashKey: {}", key, hashKey, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================List操作=============================

    @Override
    public Long lLeftPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("Redis lLeftPush操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long lRightPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("Redis lRightPush操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("Redis lLeftPop操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("Redis lRightPop操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis lRange操作失败, key: {}, start: {}, end: {}", key, start, end, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("Redis lSize操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================Set操作=============================

    @Override
    public Long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Redis sAdd操作失败, key: {}, values: {}", key, Arrays.toString(values), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("Redis sRemove操作失败, key: {}, values: {}", key, Arrays.toString(values), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("Redis sIsMember操作失败, key: {}, value: {}", key, value, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis sMembers操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("Redis sSize操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================ZSet操作=============================

    @Override
    public Boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("Redis zAdd操作失败, key: {}, value: {}, score: {}", key, value, score, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long zRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error("Redis zRemove操作失败, key: {}, values: {}", key, Arrays.toString(values), e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis zRange操作失败, key: {}, start: {}, end: {}", key, start, end, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    @Override
    public Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("Redis zSize操作失败, key: {}", key, e);
            throw new RuntimeException("Redis操作失败", e);
        }
    }

    // =============================分布式锁操作=============================

    @Override
    public Boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.MILLISECONDS);
            log.debug("尝试获取分布式锁, lockKey: {}, requestId: {}, expireTime: {}, result: {}", 
                     lockKey, requestId, expireTime, result);
            return result != null && result;
        } catch (Exception e) {
            log.error("获取分布式锁失败, lockKey: {}, requestId: {}", lockKey, requestId, e);
            return false;
        }
    }

    @Override
    public Boolean releaseLock(String lockKey, String requestId) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), requestId);
            boolean success = result != null && result == 1L;
            log.debug("释放分布式锁, lockKey: {}, requestId: {}, result: {}", lockKey, requestId, success);
            return success;
        } catch (Exception e) {
            log.error("释放分布式锁失败, lockKey: {}, requestId: {}", lockKey, requestId, e);
            return false;
        }
    }
}