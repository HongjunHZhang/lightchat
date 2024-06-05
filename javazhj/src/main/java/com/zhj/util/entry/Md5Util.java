package com.zhj.util.entry;

import com.zhj.util.AES;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Md5Util
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2023/4/24 21:45
 */
public class Md5Util {
    private static final String PREFIX = "ZHJ";
    private static final String SUFFIX = "SMART";

    public static String getComplexStr(String password){
        return PREFIX + password + SUFFIX;
    }

    public static String createMd5ByPassword(String password){
      return DigestUtils.md5Hex(getComplexStr(password));
    }

    public static String parsePasswordToMd5FromJwt(String password){
        return createMd5ByPassword(AES.decrypt(password));
    }

    public static void main(String[] args) {
        System.out.println(getComplexStr("1234567"));
        System.out.println(createMd5ByPassword("1234567"));
    }

}
