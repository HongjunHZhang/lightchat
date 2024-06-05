package com.zhj.filter;

import java.lang.annotation.*;

/**
 * @author 789
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcludeLogParam {
    //不记录入参
    boolean reqParam() default true;
    //不记录返回参数
    boolean respParam() default true;
    //忽略错误日志
    boolean ignoreException() default false;
    //忽略日常日志
    boolean ignoreLog() default true;
}
