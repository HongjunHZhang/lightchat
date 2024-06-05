package com.zhj.test;

import com.zhj.entity.login.TokenBody;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDemo {
        public final static ThreadLocal<String> local = new ThreadLocal<>();

    static {
        try {
            Field trueField = Boolean.class.getDeclaredField("TRUE");
            trueField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(trueField, trueField.getModifiers() & ~Modifier.FINAL);

            trueField.set(null, false);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println(1);
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
//
//        for (int i = 0; i < 10; i++) {
//            final int k = i;
//            threadPool.submit((Runnable) () -> {
//                TestDemo.local.set("00" + k);
//                while (true) {
//                    Thread.currentThread();
//                    System.out.println(TestDemo.local.get());
//                }
//            });
//        }
//        String str2 = "abc";
//        String str1 = "asx"+"d";
//        System.out.println( "ab" + "c" == str2);
//             int a = 1;
//        System.out.println(maxNum(new int[]{1,2,7,8,8,4},7641224));
//       dd();
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> exchange =  restTemplate.exchange("https://tfkjy.cn/mini/#/pages/login/login", HttpMethod.GET,null,String.class);
//        System.out.println(exchange.getBody());

     //  \u000d // System.out.println("this is A test1");
        System.out.println("this is A test2");
        Boolean flag = true;

        if (flag){
            System.out.println("true");
        }else {
            System.out.println("false");
        }
    }

    public boolean canChange(String start, String target) {
        int index1 = 0;
        int index2 = 0;
        int n = start.length();
        while (index1 < n && index2 < n){
            int ch1 = start.charAt(index1);
            int ch2 = target.charAt(index2);
            if (ch1 != '_' && ch2 != '_'){
                if (ch1 != ch2){
                    return false;
                }else {
                    if (ch1 == 'L' && index1 < index2){
                        return false;
                    }

                    if (ch1 == 'R' && index1 > index2){
                        return false;
                    }

                    index1++;
                    index2++;
                }

            }

            if (ch1 == '_'){
                index1++;
            }

            if (ch2 == '_'){
                index2++;
            }
        }

        while (index1 < n){
            if (start.charAt(index1) != '_'){
                return false;
            }
            index1++;
        }

        while (index2 < n){
            if (target.charAt(index2) != '_'){
                return false;
            }
            index2++;
        }
         return  true;
    }

    public static void dd(){
        Scanner scanner = new Scanner(System.in);
        int day = Integer.parseInt(scanner.nextLine());
        String work = scanner.nextLine();
        String exe = scanner.nextLine();
        String[] works = work.split(" ");
        String[] exes = exe.split(" ");
        int[][] dp = new int[day+1][3];
        for(int i = 1;i <= day;i++){
            if("1".equals(works[i-1])){
                dp[i][0] = Math.max(dp[i-1][1],dp[i-1][2]) + 1;
            }
            if("1".equals(exes[i-1])){
                dp[i][1] = Math.max(dp[i-1][0],dp[i-1][2]) + 1;
            }
            dp[i][2] = Math.max(Math.max(dp[i-1][0],dp[i-1][1]),dp[i-1][2]);
        }
        System.out.println(day - Math.max(Math.max(dp[day][0],dp[day][1]),dp[day][2]));
    }

    public static int maxNum(int[] num,int target){
        if (target == 0){
            return 0;
        }
        boolean[] digits = new boolean[10];
        for (int j : num) {
            digits[j] = true;
        }
        target = target - 1;
        int way;
       while (true){
           String number = String.valueOf(target);
           way = number.length();
           for (int i = 0; i < number.length(); i++) {
               if (digits[number.charAt(i) - '0']){
                   way--;
               }else {
                   break;
               }
           }
           if (way == 0){
               break;
           }else {
               target -= (Math.pow(10,way-1));
               String ret = String.valueOf(target);
               StringBuilder retStr = new StringBuilder(ret.substring(0, number.length() + 1 - way));
               for (int i = 0; i < way - 1; i++) {
                   retStr.append("9");
               }
               target = Integer.parseInt(retStr.toString());
           }
       }
        return target;
    }


    public static String checkDigit(String str){
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

}
