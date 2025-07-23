package ink.charter.website.common.auth.service.impl;

import ink.charter.website.common.auth.config.AuthProperties;
import ink.charter.website.common.auth.model.LoginUser;
import ink.charter.website.common.auth.service.TokenService;
import ink.charter.website.common.auth.utils.JwtUtils;
import ink.charter.website.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Token服务实现类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtils jwtUtils;
    private final AuthProperties authProperties;
    private final RedisService redisService;
    
    private static final String TOKEN_KEY_PREFIX = "token:";
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";
    private static final String TOKEN_BLACKLIST_KEY_PREFIX = "token:blacklist:";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh:token:";

    @Override
    public String createToken(LoginUser loginUser) {
        try {
            if (loginUser == null || loginUser.getUserId() == null || !StringUtils.hasText(loginUser.getUsername())) {
                return null;
            }
            
            // 生成JWT Token
            String token = jwtUtils.generateAccessToken(loginUser.getUserId(), loginUser.getUsername());
            
            if (StringUtils.hasText(token)) {
                // 缓存Token信息
                String tokenKey = TOKEN_KEY_PREFIX + token;
                String userTokenKey = USER_TOKEN_KEY_PREFIX + loginUser.getUserId();
                
                // 存储Token和用户信息
                redisService.set(tokenKey, loginUser, authProperties.getJwt().getAccessTokenExpire(), TimeUnit.SECONDS);
                
                // 存储用户Token关联
                redisService.sAdd(userTokenKey, token);
                redisService.expire(userTokenKey, authProperties.getJwt().getAccessTokenExpire(), TimeUnit.SECONDS);
            }
            
            return token;
        } catch (Exception e) {
            log.error("创建Token失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 创建Token（内部方法）
     */
    private String createToken(Long userId, String username) {
        try {
            if (userId == null || !StringUtils.hasText(username)) {
                return null;
            }
            
            // 生成JWT Token
            String token = jwtUtils.generateAccessToken(userId, username);
            
            if (StringUtils.hasText(token)) {
                // 缓存Token信息
                String tokenKey = TOKEN_KEY_PREFIX + token;
                String userTokenKey = USER_TOKEN_KEY_PREFIX + userId;
                
                // 存储Token信息
                redisService.set(tokenKey, userId, authProperties.getJwt().getAccessTokenExpire(), TimeUnit.SECONDS);
                
                // 存储用户Token关联
                redisService.sAdd(userTokenKey, token);
                redisService.expire(userTokenKey, authProperties.getJwt().getAccessTokenExpire(), TimeUnit.SECONDS);
            }
            
            return token;
        } catch (Exception e) {
            log.error("创建Token失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String createRefreshToken(LoginUser loginUser) {
        try {
            if (loginUser == null || loginUser.getUserId() == null || !StringUtils.hasText(loginUser.getUsername())) {
                return null;
            }
            
            // 生成刷新Token
            String refreshToken = jwtUtils.generateRefreshToken(loginUser.getUserId(), loginUser.getUsername());
            
            if (StringUtils.hasText(refreshToken)) {
                // 缓存刷新Token信息
                String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + refreshToken;
                
                // 存储刷新Token信息
                redisService.set(refreshTokenKey, loginUser.getUserId(), authProperties.getJwt().getRefreshTokenExpire(), TimeUnit.SECONDS);
            }
            
            return refreshToken;
        } catch (Exception e) {
            log.error("创建刷新Token失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 创建刷新Token（内部方法）
     */
    private String createRefreshToken(Long userId, String username) {
        try {
            if (userId == null || !StringUtils.hasText(username)) {
                return null;
            }
            
            // 生成刷新Token
            String refreshToken = jwtUtils.generateRefreshToken(userId, username);
            
            if (StringUtils.hasText(refreshToken)) {
                // 缓存刷新Token信息
                String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + refreshToken;
                
                // 存储刷新Token信息
                redisService.set(refreshTokenKey, userId, authProperties.getJwt().getRefreshTokenExpire(), TimeUnit.SECONDS);
            }
            
            return refreshToken;
        } catch (Exception e) {
            log.error("创建刷新Token失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }
            
            // 检查Token是否在黑名单中
            if (isTokenBlacklisted(token)) {
                return false;
            }
            
            // 验证JWT Token
            if (!jwtUtils.validateToken(token)) {
                return false;
            }
            
            // 检查Redis中是否存在
            String tokenKey = TOKEN_KEY_PREFIX + token;
            return Boolean.TRUE.equals(redisService.hasKey(tokenKey));
            
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public LoginUser getLoginUserFromToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            
            // 验证Token
            if (!verifyToken(token)) {
                return null;
            }
            
            // 从Redis获取用户信息
            String tokenKey = TOKEN_KEY_PREFIX + token;
            Object userObj = redisService.get(tokenKey);
            
            if (userObj instanceof LoginUser) {
                return (LoginUser) userObj;
            }
            
            return null;
        } catch (Exception e) {
            log.error("从Token获取登录用户信息失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        try {
            if (!StringUtils.hasText(refreshToken)) {
                return null;
            }
            
            // 验证刷新Token
            if (!verifyToken(refreshToken)) {
                return null;
            }
            
            // 获取用户信息
            Long userId = getUserIdFromToken(refreshToken);
            String username = getUsernameFromToken(refreshToken);
            
            if (userId == null || !StringUtils.hasText(username)) {
                return null;
            }
            
            // 生成新的访问Token
            return createToken(userId, username);
            
        } catch (Exception e) {
            log.error("刷新访问Token失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void deleteToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return;
            }
            
            // 获取用户ID
            Long userId = getUserIdFromToken(token);
            
            // 删除Token缓存
            String tokenKey = TOKEN_KEY_PREFIX + token;
            redisService.delete(tokenKey);
            
            // 从用户Token集合中移除
            if (userId != null) {
                String userTokenKey = USER_TOKEN_KEY_PREFIX + userId;
                redisService.sRemove(userTokenKey, token);
            }
            
            // 添加到黑名单
            addTokenToBlacklist(token);
            
        } catch (Exception e) {
            log.error("删除Token失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserTokens(Long userId) {
        try {
            if (userId == null) {
                return;
            }
            
            String userTokenKey = USER_TOKEN_KEY_PREFIX + userId;
            
            // 获取用户所有Token
            Set<Object> tokens = redisService.sMembers(userTokenKey);
            
            if (tokens != null && !tokens.isEmpty()) {
                for (Object tokenObj : tokens) {
                    String token = (String) tokenObj;
                    
                    // 删除Token缓存
                    String tokenKey = TOKEN_KEY_PREFIX + token;
                    redisService.delete(tokenKey);
                    
                    // 添加到黑名单
                    addTokenToBlacklist(token);
                }
            }
            
            // 删除用户Token集合
            redisService.delete(userTokenKey);
            
        } catch (Exception e) {
            log.error("删除用户Token失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public LocalDateTime getTokenExpireTime(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }

            return jwtUtils.getTokenExpiration(token);
        } catch (Exception e) {
            log.error("获取Token过期时间失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void extendTokenExpire(String token, long seconds) {
        try {
            if (!StringUtils.hasText(token) || seconds <= 0) {
                return;
            }
            
            String tokenKey = TOKEN_KEY_PREFIX + token;
            if (Boolean.TRUE.equals(redisService.hasKey(tokenKey))) {
                redisService.expire(tokenKey, seconds, TimeUnit.SECONDS);
            }
            
        } catch (Exception e) {
            log.error("延长Token过期时间失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean isTokenExpiringSoon(String token, long thresholdSeconds) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }
            
            String tokenKey = TOKEN_KEY_PREFIX + token;
            Long expire = redisService.getExpire(tokenKey);
            
            return expire != null && expire > 0 && expire <= thresholdSeconds;
            
        } catch (Exception e) {
            log.error("检查Token是否即将过期失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void cleanExpiredTokens() {
        try {
            // Redis会自动清理过期的Key，这里主要清理黑名单中的过期Token
            // 可以通过定时任务实现
            log.debug("清理过期Token完成");
        } catch (Exception e) {
            log.error("清理过期Token失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }
            
            String blacklistKey = TOKEN_BLACKLIST_KEY_PREFIX + token;
            return Boolean.TRUE.equals(redisService.hasKey(blacklistKey));
            
        } catch (Exception e) {
            log.error("检查Token黑名单失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void addTokenToBlacklist(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return;
            }
            
            // 获取Token剩余过期时间
            LocalDateTime expireTime = getTokenExpireTime(token);
            if (expireTime != null && expireTime.isAfter(LocalDateTime.now())) {
                String blacklistKey = TOKEN_BLACKLIST_KEY_PREFIX + token;
                long ttl = java.time.Duration.between(LocalDateTime.now(), expireTime).getSeconds();
                redisService.set(blacklistKey, true, ttl, TimeUnit.SECONDS);
            }
            
        } catch (Exception e) {
            log.error("添加Token到黑名单失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            
            return jwtUtils.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("从Token获取用户ID失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            
            return jwtUtils.getUsernameFromToken(token);
        } catch (Exception e) {
            log.error("从Token获取用户名失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int getUserTokenCount(Long userId) {
        try {
            if (userId == null) {
                return 0;
            }
            
            String userTokenKey = USER_TOKEN_KEY_PREFIX + userId;
            Long count = redisService.sSize(userTokenKey);
            return count != null ? count.intValue() : 0;
            
        } catch (Exception e) {
            log.error("获取用户Token数量失败: {}", e.getMessage(), e);
            return 0;
        }
    }
}