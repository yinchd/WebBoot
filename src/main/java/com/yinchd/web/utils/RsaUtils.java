package com.yinchd.web.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加密工具类
 * @author yinchd
 * @date 2019/11/15
*/
public class RsaUtils {

    private static final BASE64Encoder base64Encoder = new BASE64Encoder();
    private static final BASE64Decoder base64Decoder = new BASE64Decoder();

    /**
     * 从文件中读取公钥
     * @param pubKeyPath 公钥保存路径
     * @return 公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyPath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bytes = readFile(pubKeyPath);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取私钥
     * @param priKeyPath 私钥保存路径
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyPath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bytes = readFile(priKeyPath);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     * @param pubKeyBytes 公钥的字节形式
     * @return PublicKey
     */
    public static PublicKey getPublicKey(byte[] pubKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pubKeyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 获取私钥
     * @param priKeyBytes 私钥的字节形式
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(byte[] priKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(priKeyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文(secret)，生存RSA公钥和私钥,并写入指定文件
     * @param pubKeyPath  公钥文件路径
     * @param priKeyPath 私钥文件路径
     * @param secret     生成密钥的密文
     */
    public static void generateKey(String pubKeyPath, String priKeyPath, String secret) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(secret);
        // 获取公钥并写出
        byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(pubKeyPath, pubKeyBytes);
        // 获取私钥并写出
        byte[] priKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(priKeyPath, priKeyBytes);
    }

    /**
     * 根据密文(secret)，生存RSA公钥和私钥
     * @param secret 生成密钥的密文
     */
    public static KeyPairResult generateKey(String secret) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(secret);
        return new KeyPairResult(base64Encoder.encode(keyPair.getPublic().getEncoded()), base64Encoder.encode(keyPair.getPrivate().getEncoded()));
    }

    /**
     * 生成RSA公钥和私钥
     */
    public static KeyPairResult generateKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(null);
        return new KeyPairResult(base64Encoder.encode(keyPair.getPublic().getEncoded()), base64Encoder.encode(keyPair.getPrivate().getEncoded()));
    }

    /**
     * RSA加密，如果出现异常返回null
     * @param rawVal 待加密的原始数据
     * @param publicKey 公钥
     * @return RSA加密后的值
     */
    public static String encrypt(String rawVal, String publicKey) {
        init();
        try {
            byte[] pubKeyByte = base64Decoder.decodeBuffer(publicKey);
            PublicKey pubKey = getPublicKey(pubKeyByte);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return base64Encoder.encode(cipher.doFinal(rawVal.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA解密，如果出现异常返回null
     * @param encryptVal RSA加密后的字符串
     * @param privateKey 私钥
     * @return RSA解密后的值
     */
    public static String decrypt(String encryptVal, String privateKey) {
        init();
        try {
            byte[] encryptByte = base64Decoder.decodeBuffer(encryptVal);
            byte[] priKeyByte = base64Decoder.decodeBuffer(privateKey);
            PrivateKey priKey = getPrivateKey(priKeyByte);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(encryptByte));
        } catch (Exception e) {
            return null;
        }
    }

    //**********************私有方法区**********************//
    @Data
    @AllArgsConstructor
    protected static class KeyPairResult {
        /**
         * 公钥
         */
        private String publicKey;
        /**
         * 私钥
         */
        private String privateKey;
    }

    private static KeyPair generateKeyPair(String secret) throws NoSuchAlgorithmException, NoSuchProviderException {
        init();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if (secret != null) {
            secureRandom.setSeed(secret.getBytes());
        }
        keyPairGenerator.initialize(1024, secureRandom);
        return keyPairGenerator.genKeyPair();
    }

    private static byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }

    private static void init() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

}