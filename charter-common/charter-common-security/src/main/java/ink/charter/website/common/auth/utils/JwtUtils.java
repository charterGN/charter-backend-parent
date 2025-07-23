package ink.charter.website.common.auth.utils;

import ink.charter.website.common.auth.config.AuthProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final AuthProperties authProperties;

    /**
     * 生成访问令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateAccessToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "access");
        
        return generateToken(claims, authProperties.getJwt().getAccessTokenExpire());
    }

    /**
     * 生成刷新令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT刷新令牌
     */
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "refresh");
        
        return generateToken(claims, authProperties.getJwt().getRefreshTokenExpire());
    }

    /**
     * 生成JWT令牌
     *
     * @param claims    声明
     * @param expireSeconds 过期时间（秒）
     * @return JWT令牌
     */
    private String generateToken(Map<String, Object> claims, Long expireSeconds) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expireSeconds * 1000);
        
        return Jwts.builder()
                .claims(claims)
                .issuer(authProperties.getJwt().getIssuer())
                .issuedAt(now)
                .expiration(expireTime)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return Claims
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
            throw new RuntimeException("令牌已过期", e);
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT令牌: {}", e.getMessage());
            throw new RuntimeException("不支持的令牌格式", e);
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", e.getMessage());
            throw new RuntimeException("令牌格式错误", e);
        } catch (SecurityException e) {
            log.warn("JWT令牌签名验证失败: {}", e.getMessage());
            throw new RuntimeException("令牌签名验证失败", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT令牌为空: {}", e.getMessage());
            throw new RuntimeException("令牌不能为空", e);
        }
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.debug("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("username");
    }

    /**
     * 从令牌中获取令牌类型
     *
     * @param token JWT令牌
     * @return 令牌类型
     */
    public String getTokenType(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("type");
    }

    /**
     * 检查令牌是否即将过期
     *
     * @param token JWT令牌
     * @param beforeMinutes 提前多少分钟算即将过期
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, int beforeMinutes) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long timeDiff = expiration.getTime() - now.getTime();
            long minutesDiff = timeDiff / (1000 * 60);
            return minutesDiff <= beforeMinutes;
        } catch (Exception e) {
            return true; // 解析失败认为即将过期
        }
    }

    /**
     * 获取令牌过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public LocalDateTime getTokenExpiration(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = authProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}