package com.zhj.test;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class SimpleTest {

    public static void main(String[] args) {



        FastAutoGenerator.create("jdbc:mysql://rm-2vcvw2m5v78k9y7wmmo.mysql.cn-chengdu.rds.aliyuncs.com/bs_zhj", "rootzhj", "Rootzhj123")
                .globalConfig(builder -> {
                    builder.author("zhj") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride()// 覆盖已生成文件
                            .outputDir("D:\\bysjzhj\\javazhj\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhj") // 设置父包名
                            //.moduleName("zhj") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\bysjzhj\\javazhj\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("log_record") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}

//
//    @Test
//    public void arrayTest(){
//        int temp[] = new int[6];
//        for (int i = 0; i < temp.length; i++) {
//            temp[i] = -1;
//        }
//        temp[5] = 1;
//        long count = Arrays.stream(temp).filter(T -> T == -1).count();
//        System.out.println(count);
//
//    }
//
//    @Test
//    public void strTrim(){
//        String str = "the sky is blue";
//        System.out.println(reverseWords(str));
//
//
//
//    }
//
//
//    public String reverseWords(String s) {
//        int length = s.length();
//        String str = "";
//        String ret = "";
//        for(int i=length-1;i>=0;i--){
//            if(s.charAt(i)!=' '){
//                str = str+s.charAt(i);
//            }else{
//                if(str.length()!=0){
//                    for(int j=str.length()-1;j>=0;j--){
//                        ret = ret +str.charAt(j);
//                    }
//                    ret = ret+" ";
//
//                }
//                str = "";
//            }
//            if (i==0){
//                if(str.length()!=0){
//                    for(int j=str.length()-1;j>=0;j--){
//                        ret = ret +str.charAt(j);
//                    }
//                    ret = ret+" ";
//
//                }
//
//            }
//
//        }
//        ret = ret.trim();
//        return ret;
//    }
//
//    public static String getFruit(){
//      boolean apple = seeApple();
//        if (!apple){
//            return "空手而归";
//        }
//      boolean grapefruit = seeGrapefruit();
//      if (!grapefruit){
//          return "一斤苹果";
//          }
//
//      return "一个苹果";
//
//    }
//
//    public static boolean seeApple(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("你是否看到了苹果");
//        String apple = scanner.nextLine();
//        if ("yes".equals(apple)){
//           return true;
//        }else {
//            return false;
//        }
//    }
//
//    public static boolean seeGrapefruit(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("你是否看到了柚子");
//        String apple = scanner.nextLine();
//        if ("yes".equals(apple)){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public static void main(String[] args) {
//        RestTemplate template = new RestTemplate();
//        Map<String,String> map = new HashMap<>();
//        map.put("name","123");
//        template.postForObject("http://localhost:8888/test?name={name}",null,String.class,map);
//
//    }


//}
