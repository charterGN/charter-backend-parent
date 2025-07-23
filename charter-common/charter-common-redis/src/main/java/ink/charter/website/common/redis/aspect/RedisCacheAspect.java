package ink.charter.website.common.redis.aspect;

import ink.charter.website.common.redis.annotation.RedisCache;
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
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存切面
 * 处理@RedisCache注解的缓存逻辑
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedisCacheAspect {

    private final RedisService redisService;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redisCache)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        
        // 检查缓存条件
        if (!evaluateCondition(redisCache.condition(), method, args)) {
            log.debug("缓存条件不满足，直接执行方法: {}", method.getName());
            return joinPoint.proceed();
        }
        
        // 检查排除条件
        if (evaluateCondition(redisCache.unless(), method, args)) {
            log.debug("排除条件满足，直接执行方法: {}", method.getName());
            return joinPoint.proceed();
        }
        
        // 生成缓存key
        String cacheKey = generateKey(redisCache.key(), method, args);
        log.debug("生成缓存key: {}", cacheKey);
        
        // 尝试从缓存获取
        Object cachedResult = redisService.get(cacheKey);
        if (cachedResult != null) {
            log.debug("缓存命中，返回缓存结果: {}", cacheKey);
            return cachedResult;
        }
        
        // 缓存未命中，执行方法
        log.debug("缓存未命中，执行方法: {}", method.getName());
        Object result = joinPoint.proceed();
        
        // 判断是否缓存null值
        if (result == null && !redisCache.cacheNull()) {
            log.debug("方法返回null且不缓存null值，跳过缓存: {}", cacheKey);
            return null;
        }
        
        // 设置缓存
        try {
            if (redisCache.expire() > 0) {
                redisService.set(cacheKey, result, redisCache.expire(), redisCache.timeUnit());
                log.debug("设置缓存成功，key: {}, expire: {} {}", cacheKey, redisCache.expire(), redisCache.timeUnit());
            } else {
                redisService.set(cacheKey, result);
                log.debug("设置永久缓存成功，key: {}", cacheKey);
            }
        } catch (Exception e) {
            log.error("设置缓存失败，key: {}", cacheKey, e);
        }
        
        return result;
    }
    
    /**
     * 生成缓存key
     */
    private String generateKey(String keyExpression, Method method, Object[] args) {
        if (keyExpression.isEmpty()) {
            // 默认key生成策略：类名.方法名(参数类型)
            StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(method.getDeclaringClass().getSimpleName())
                     .append(".")
                     .append(method.getName())
                     .append("(");
            
            Class<?>[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) keyBuilder.append(",");
                keyBuilder.append(paramTypes[i].getSimpleName());
            }
            keyBuilder.append(")");
            
            // 添加参数值的hash
            if (args.length > 0) {
                keyBuilder.append(":").append(Math.abs(java.util.Arrays.hashCode(args)));
            }
            
            return keyBuilder.toString();
        }
        
        // 使用SpEL表达式生成key
        try {
            EvaluationContext context = createEvaluationContext(method, args);
            Expression expression = parser.parseExpression(keyExpression);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            log.error("SpEL表达式解析失败，使用默认key生成策略: {}", keyExpression, e);
            return method.getDeclaringClass().getSimpleName() + "." + method.getName() + ":" + Math.abs(java.util.Arrays.hashCode(args));
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