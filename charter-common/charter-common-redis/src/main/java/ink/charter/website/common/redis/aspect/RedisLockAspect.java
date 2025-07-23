package ink.charter.website.common.redis.aspect;

import ink.charter.website.common.redis.annotation.RedisLock;
import ink.charter.website.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁切面
 * 处理@RedisLock注解的分布式锁逻辑
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedisService redisService;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        
        // 生成锁key
        String lockKey = generateLockKey(redisLock.key(), method, args);
        String requestId = UUID.randomUUID().toString();
        
        log.debug("尝试获取分布式锁: {}, requestId: {}", lockKey, requestId);
        
        // 尝试获取锁
        boolean lockAcquired = false;
        long startTime = System.currentTimeMillis();
        long waitTime = redisLock.timeUnit().toMillis(redisLock.waitTime());
        long expireTime = redisLock.timeUnit().toMillis(redisLock.expire());
        
        try {
            // 循环尝试获取锁
            while (!lockAcquired && (System.currentTimeMillis() - startTime) < waitTime) {
                lockAcquired = redisService.tryLock(lockKey, requestId, expireTime);
                
                if (!lockAcquired && waitTime > 0) {
                    // 等待一小段时间后重试
                    Thread.sleep(Math.min(100, waitTime / 10));
                }
            }
            
            if (!lockAcquired) {
                log.warn("获取分布式锁失败: {}, waitTime: {}ms", lockKey, waitTime);
                return handleLockFailure(redisLock, method, joinPoint);
            }
            
            log.debug("成功获取分布式锁: {}, requestId: {}", lockKey, requestId);
            
            // 执行目标方法
            return joinPoint.proceed();
            
        } finally {
            // 释放锁
            if (lockAcquired) {
                try {
                    boolean released = redisService.releaseLock(lockKey, requestId);
                    log.debug("释放分布式锁: {}, requestId: {}, 结果: {}", lockKey, requestId, released);
                } catch (Exception e) {
                    log.error("释放分布式锁失败: {}, requestId: {}", lockKey, requestId, e);
                }
            }
        }
    }
    
    /**
     * 处理锁获取失败的情况
     */
    private Object handleLockFailure(RedisLock redisLock, Method method, ProceedingJoinPoint joinPoint) throws Throwable {
      return switch (redisLock.failStrategy()) {
        case THROW_EXCEPTION -> {
          String message = redisLock.failMessage().isEmpty()
            ? "获取分布式锁失败: " + method.getName()
            : redisLock.failMessage();
          throw new RuntimeException(message);
        }
        case RETURN_NULL -> {
          log.debug("锁获取失败，返回null: {}", method.getName());
          yield null;
        }
        case IGNORE -> {
          log.debug("锁获取失败，忽略锁直接执行方法: {}", method.getName());
          yield joinPoint.proceed();
        }
        default -> throw new RuntimeException("未知的失败策略: " + redisLock.failStrategy());
      };
    }
    
    /**
     * 生成锁key
     */
    private String generateLockKey(String keyExpression, Method method, Object[] args) {
        if (keyExpression.isEmpty()) {
            // 默认锁key生成策略：lock:类名.方法名
            return "lock:" + method.getDeclaringClass().getSimpleName() + "." + method.getName();
        }
        
        // 使用SpEL表达式生成key
        try {
            EvaluationContext context = createEvaluationContext(method, args);
            Expression expression = parser.parseExpression(keyExpression);
            Object value = expression.getValue(context);
            String key = value != null ? value.toString() : "";
            
            // 确保锁key有前缀
            if (!key.startsWith("lock:")) {
                key = "lock:" + key;
            }
            
            return key;
        } catch (Exception e) {
            log.error("SpEL表达式解析失败，使用默认锁key生成策略: {}", keyExpression, e);
            return "lock:" + method.getDeclaringClass().getSimpleName() + "." + method.getName();
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