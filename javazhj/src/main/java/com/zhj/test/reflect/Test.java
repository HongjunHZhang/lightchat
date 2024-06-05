package com.zhj.test.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Test
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/9/23 19:14
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Class<Object> student = getClassOfName("com.zhj.test.reflect.Student");
        Object o = student.newInstance();
        Field age = student.getDeclaredField("age");
        age.setAccessible(true);
        System.out.println("实例化对象的年龄为:" + age.get(o));
        Method method = student.getDeclaredMethod("toString");
        method.setAccessible(true);
        method.invoke(o);
        Student student1 = new Student();
        student1.setAge(19);
        method.invoke(student1);
        Field ageLs = student1.getClass().getDeclaredField("age");
        ageLs.setAccessible(true);
        System.out.println(ageLs.get(student1));
        System.out.println(ageLs.get(o));

    }


    public static Class<Object> getClassOfName(String className) throws ClassNotFoundException {
        return (Class<Object>) Class.forName(className);
    }

}
