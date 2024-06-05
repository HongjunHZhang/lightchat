package com.zhj.filter;

import java.lang.annotation.*;

/**
 * RoleParam
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/22 14:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimitRequest {
    int times() default 3;
    int durationTime() default 5;
}
