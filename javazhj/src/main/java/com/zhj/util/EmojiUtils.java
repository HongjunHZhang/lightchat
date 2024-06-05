package com.zhj.util;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 789
 */
public class EmojiUtils {
    /**
     * emoji表情替换
     *
     * @param source 原字符串
     *
     * @param slipStr emoji表情替换成的字符串
     *
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source, String slipStr) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        } else {
            return source;
        }
    }

    /**
     * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
     * @param str
     *            待转换字符串
     * @return 转换后字符串
     * @throws UnsupportedEncodingException
     *             exception
     */
    public static String emojiConvert(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), "UTF-8") + "]]");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
     * @param str
     *            转换后的字符串
     * @return 转换前的字符串
     * @throws UnsupportedEncodingException
     *             exception
     */
    public static String emojiRecovery2(String str)  {
        if (str == null){
            return "";
        }
        String patternString = "\\[\\[(.*?)\\]\\]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String str = "\uD83D\uDE00  719927 \uD83E\uDD23";
        String s = emojiConvert(str);
        System.out.println(s);
        String s1 = emojiRecovery2(s);
        System.out.println(s1);



    }


}
