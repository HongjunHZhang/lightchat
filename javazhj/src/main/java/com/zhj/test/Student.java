package com.zhj.test;

import lombok.Data;

/**
 * Student
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/9/23 19:14
 */
@Data
public class Student {
    private String username;
    private String sex;
    private int age;
    private String pass;

    @Override
    public String toString() {
        System.out.println("Student{" +
                "username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age + ",pass=" + pass +
                '}');
        return "Student{" +
                "username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
