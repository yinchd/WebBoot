package com.yinchd.web.utils;

import com.yinchd.web.constants.JwtConstants;
import com.yinchd.web.constants.RegexConstants;
import com.yinchd.web.dto.response.UserInfo;
import com.yinchd.web.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * JwtToken解析工具类
 *
 * @author yinchd
 * @date 2019/11/15
 */
@Slf4j
public class JwtUtils {

    /**
     * 根据用户参数生成token
     * @param user 用户对象，必须包含id和account值
     * @param privateKey JwtProperties中生成的私钥信息
     * @param expires 过期时间，如2d，代表2天、2h，代表2小时，后面的时间刻度含义：M月 d天 h小时 m分钟 s秒
     * @return jwt token
     */
    public static String generateToken(UserModel user, PrivateKey privateKey, String expires) {
        Assert.notNull(user, "user不能为空");
        Assert.notNull(privateKey, "私钥不能为空");
        if (!expires.matches(RegexConstants.TOKEN_EXPIRE_FORMAT)) {
            log.warn("expires参数不符合要求，请检查配置文件，这里将为token配置默认过期时间：30分钟");
            expires = "30m";
        }
        // 时间刻度：M月 d天 h小时 m分钟 s秒
        char timeUnit = expires.charAt(expires.length() - 1);
        int expireNum = Math.max(Integer.parseInt(expires.replaceAll(RegexConstants.EN_CHARACTER, "")), 30);
        Date expireDate = getExpireDate(timeUnit, expireNum);
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_USER_ID, user.getId())
                .claim(JwtConstants.JWT_KEY_USER_ACCOUNT, user.getAccount())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 获取token中的用户信息
     * @param token jwt token
     * @param publicKey JwtProperties中生成的公钥信息
     * @return 解析出来的用户信息，包含用户id和用户account
     */
    public static UserInfo getUserFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parseToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo()
                .setId(Integer.valueOf(body.get(JwtConstants.JWT_KEY_USER_ID).toString()))
                .setAccount(body.get(JwtConstants.JWT_KEY_USER_ACCOUNT).toString());
    }

    /**
     * 判断token是否失效
     * @param token jwt token
     * @param publicKey JwtProperties中生成的公钥信息
     * @return 是否失效
     */
    public static Boolean isTokenExpired(String token, PublicKey publicKey) {
        try {
            final Date expireDate = getExpirationDateFromToken(token, publicKey);
            return expireDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取jwt token的失效时间
     * @param token jwt token
     * @param publicKey JwtProperties中生成的公钥信息
     * @return Date类型的失效日期
     */
    public static Date getExpirationDateFromToken(String token, PublicKey publicKey) {
        return parseToken(token, publicKey).getBody().getExpiration();
    }

    /**
     * 根据公钥解析token
     * @param token jwt token
     * @param publicKey JwtProperties中生成的公钥信息
     * @return Jws<Claims>
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token);
    }

    /**
     * 根据field获取过期时间
     * @param timeUnit 时间单位 含义：M月 d天 h小时 m分钟 s秒
     * @param expireNum 过期时间
     * @return Date类型的过期时间
     */
    private static Date getExpireDate(char timeUnit, int expireNum) {
        LocalDateTime expire;
        LocalDateTime now = LocalDateTime.now();
        switch (timeUnit) {
            case 'M':
                expire = now.plusMonths(expireNum);
                break;
            case 'd':
                expire = now.plusDays(expireNum);
                break;
            case 'h':
                expire = now.plusHours(expireNum);
                break;
            case 's':
                expire = now.plusSeconds(expireNum);
                break;
            default:
                expire = now.plusMinutes(expireNum);
        }
        log.info("JwtToken过期时间为：{}", DateUtils.format(expire));
        return DateUtils.toDate(expire);
    }

}
