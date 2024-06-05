package com.zhj.test.reflect;

import lombok.Data;

/**
 * Student
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/9/23 19:14
 */

public class Student {
    private String username;
    private String sex;
    private int age;

    public Student() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void printA(String a){
        System.out.println(a);
    }

    @Override
    public String toString() {
        System.out.println("Student{" +
                "username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}');
        return "Student{" +
                "username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
