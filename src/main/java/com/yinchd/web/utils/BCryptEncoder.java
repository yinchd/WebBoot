package com.yinchd.web.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yinchd
 * @version 1.0
 * @description bcrypt加密工具类
 */
@Slf4j
public class BCryptEncoder {

    private final Pattern BCRYPT_PATTERN;
    private final int strength;
    private final BCryptVersion version;
    private final SecureRandom random;

    public BCryptEncoder() {
        this(-1);
    }

    public BCryptEncoder(int strength) {
        this(strength, null);
    }

    public BCryptEncoder(BCryptVersion version) {
        this(version, null);
    }

    public BCryptEncoder(BCryptVersion version, SecureRandom random) {
        this(version, -1, random);
    }

    public BCryptEncoder(int strength, SecureRandom random) {
        this(BCryptVersion.$2A, strength, random);
    }

    public BCryptEncoder(BCryptVersion version, int strength) {
        this(version, strength, null);
    }

    public BCryptEncoder(BCryptVersion version, int strength, SecureRandom random) {
        this.BCRYPT_PATTERN = Pattern.compile("\\A\\$2([ayb])?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
        if (strength == -1 || strength >= 4 && strength <= 31) {
            this.version = version;
            this.strength = strength == -1 ? 10 : strength;
            this.random = random;
        } else {
            throw new IllegalArgumentException("Bad strength");
        }
    }

    /**
     * BCrypt加密字符串
     * @param rawPassword 待加密数据
     * @return BCrypt加密后的字符串
     */
    public static String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else {
            String salt = getSalt();
            return BCrypt.hashpw(rawPassword.toString(), salt);
        }
    }

    /**
     * 获取盐值
     * @return 盐
     */
    private static String getSalt() {
        BCryptEncoder encoder = new BCryptEncoder();
        return encoder.random != null
                ? BCrypt.gensalt(encoder.version.getVersion(), encoder.strength, encoder.random)
                : BCrypt.gensalt(encoder.version.getVersion(), encoder.strength);
    }

    /**
     * 判断原始数据和加密后的数据是否匹配
     * @param rawPassword 原始数据
     * @param encodedPassword 加密后的数据
     * @return 匹配返回true，反之false
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        BCryptEncoder encoder = new BCryptEncoder();
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else if (encodedPassword != null && encodedPassword.length() != 0) {
            if (!encoder.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
                log.warn("Encoded password does not look like BCrypt");
                return false;
            } else {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        } else {
            log.warn("Empty encoded password");
            return false;
        }
    }

    public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            Matcher matcher = this.BCRYPT_PATTERN.matcher(encodedPassword);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Encoded password does not look like BCrypt: " + encodedPassword);
            } else {
                int strength = Integer.parseInt(matcher.group(2));
                return strength < this.strength;
            }
        } else {
            log.warn("Empty encoded password");
            return false;
        }
    }

    public enum BCryptVersion {
        $2A("$2a"),
        $2Y("$2y"),
        $2B("$2b");

        private final String version;

        BCryptVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return this.version;
        }
    }

    public static void main(String[] args) {

    }
}
