package com.yinchd.web.config;

import com.yinchd.web.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/** 
 * jwt配置属性类
 * @author yinchd
 * @date 2019/11/15
*/ 
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "web.jwt")
public class JwtProperties {
    /**
     * request header中的token名称
     */
    private String name;
    /**
     * 密钥
     */
    private String secret;
    /**
     * accessToken过期时间
     */
    private String accessExpire;
    /**
     * refreshToken过期时间
     */
    private String refreshExpire;
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 初始化publicKey，privateKey
     * @author yinchd
     * @date 2019/11/15
    */
    @PostConstruct
    public void init() {
        try {
            String dir = System.getProperty("user.home") + File.separator + "config" + File.separator;
            String pubKeyPath = dir + "rsa.pub",
                priKeyPath = dir + "rsa.pri";
            File pubKey = new File(pubKeyPath),
                priKey = new File(priKeyPath);
            if (pubKey.exists() && priKey.exists()) {
                boolean r1 = pubKey.delete();
                boolean r2 = priKey.delete();
                log.info("Deleting history pubKey and priKey...[pubKey：{}，priKey：{}]", r1, r2);
            }
            // 生成公钥和私钥
            RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
            log.info("Initializing pubKey and priKey success...");
        } catch (Exception e) {
            log.error("Initializing pubKey and priKey failure...", e);
            throw new RuntimeException();
        }
    }
}
