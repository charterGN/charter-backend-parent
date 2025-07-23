package ink.charter.website.common.redis.aspect;

import ink.charter.website.common.redis.annotation.RedisCacheEvict;
import ink.charter.website.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Redis缓存清除切面
 * 处理@RedisCacheEvict注解的缓存清除逻辑
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedisCacheEvictAspect {

    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redisCacheEvict)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) throws Throwable {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        
        // 检查清除条件
        if (!evaluateCondition(redisCacheEvict.condition(), method, args)) {
            log.debug("缓存清除条件不满足，直接执行方法: {}", method.getName());
            return joinPoint.proceed();
        }
        
        // 如果是方法执行前清除
        if (redisCacheEvict.beforeInvocation()) {
            evictCache(redisCacheEvict, method, args);
        }
        
        Object result = null;
        Exception methodException = null;
        
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            methodException = e;
            throw e;
        } finally {
            // 如果是方法执行后清除（默认行为）
            if (!redisCacheEvict.beforeInvocation()) {
                // 只有在方法成功执行时才清除缓存
                if (methodException == null) {
                    evictCache(redisCacheEvict, method, args);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 执行缓存清除
     */
    private void evictCache(RedisCacheEvict redisCacheEvict, Method method, Object[] args) {
        try {
            if (redisCacheEvict.allEntries()) {
                // 清除所有缓存
                evictAllEntries();
            } else if (!redisCacheEvict.keyPattern().isEmpty()) {
                // 按模式批量清除
                String pattern = generateKey(redisCacheEvict.keyPattern(), method, args);
                evictByPattern(pattern);
            } else if (!redisCacheEvict.key().isEmpty()) {
                // 清除指定key
                String key = generateKey(redisCacheEvict.key(), method, args);
                evictByKey(key);
            } else {
                log.warn("缓存清除配置无效，未指定key、keyPattern或allEntries: {}", method.getName());
            }
        } catch (Exception e) {
            log.error("缓存清除失败: {}", method.getName(), e);
        }
    }
    
    /**
     * 清除指定key的缓存
     */
    private void evictByKey(String key) {
        Boolean result = redisService.delete(key);
        log.debug("清除缓存key: {}, 结果: {}", key, result);
    }
    
    /**
     * 按模式批量清除缓存
     */
    private void evictByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisService.delete(keys);
                log.debug("按模式清除缓存: {}, 清除数量: {}", pattern, deletedCount);
            } else {
                log.debug("按模式清除缓存: {}, 未找到匹配的key", pattern);
            }
        } catch (Exception e) {
            log.error("按模式清除缓存失败: {}", pattern, e);
        }
    }
    
    /**
     * 清除所有缓存
     */
    private void evictAllEntries() {
        try {
            // 注意：这是一个危险操作，会清除Redis中的所有数据
            // 在生产环境中应该谨慎使用
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisService.delete(keys);
                log.warn("清除所有缓存，清除数量: {}", deletedCount);
            }
        } catch (Exception e) {
            log.error("清除所有缓存失败", e);
        }
    }
    
    /**
     * 生成缓存key
     */
    private String generateKey(String keyExpression, Method method, Object[] args) {
        if (keyExpression.isEmpty()) {
            return "";
        }
        
        // 使用SpEL表达式生成key
        try {
            EvaluationContext context = createEvaluationContext(method, args);
            Expression expression = parser.parseExpression(keyExpression);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            log.error("SpEL表达式解析失败: {}", keyExpression, e);
            return "";
        }
    }
    
    /**
     * 评估条件表达式
     */
    private boolean evaluateCondition(String condition, Method method, Object[] args) {
        if (condition.isEmpty()) {
            return true;
        }
        
        try {
            EvaluationContext context = createEvaluationContext(method, args);
            Expression expression = parser.parseExpression(condition);
            Boolean result = expression.getValue(context, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            log.error("条件表达式评估失败，默认返回true: {}", condition, e);
            return true;
        }
    }
    
    /**
     * 创建SpEL评估上下文
     */
    private EvaluationContext createEvaluationContext(Method method, Object[] args) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        // 设置参数名和值
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        
        // 设置参数数组
        context.setVariable("args", args);
        
        return context;
    }
}