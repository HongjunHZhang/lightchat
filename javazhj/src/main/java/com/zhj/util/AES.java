package com.zhj.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AES {
    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding

    private  static  SecretKeySpec skforAES = null;
    private static String ivParameter = "zhjggadminoftheg";// 密钥默认偏移，可更改

    private static byte[] iv = ivParameter.getBytes();
    private static IvParameterSpec IV;
    static String  sKey = "zhjggadminoftheu";// key必须为16位，可更改为自己的key

    private static AES instance = null;

    public static AES getInstance() {
        if (instance == null) {
            synchronized (AES.class) {
                if (instance == null) {
                    instance = new AES();
                }
            }
        }
        return instance;
    }

    public AES() {
        byte[] skAsByteArray;
        try {
            skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        IV = new IvParameterSpec(iv);
    }

    public static String encrypt(String text)  {                              //加密
        try {
            byte[] skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        }catch (Exception e){
            e.printStackTrace();
        }
        IV = new IvParameterSpec(iv);
        byte[] plaintext = text.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
        return Base64Encoder.encode(ciphertext);
    }

    public static String decrypt(String ciphertext_base64) {               //解密
        try {
            byte[] skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        }catch (Exception e){
            return "12345678";
        }
        IV = new IvParameterSpec(iv);
        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
        return new String(Objects.requireNonNull(decrypt(CIPHERMODEPADDING, skforAES, IV,
                s)));
    }

    private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (Exception nsae) {
        }
        return null;
    }

    private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (Exception nsae) {
        }
        return null;
    }

    public static void main(String[] args) {
//        AES aes = new AES();
//        String decrypt =aes.encrypt("123675");
//        System.out.println(decrypt);
//        System.out.println(aes.decrypt(decrypt));
//        String zhj = AES.encrypt("张洪俊");
//        System.out.println(zhj);
        System.out.println(AES.decrypt("MH5t0d+NV8N7El9of24wjg=="));



    }

}
