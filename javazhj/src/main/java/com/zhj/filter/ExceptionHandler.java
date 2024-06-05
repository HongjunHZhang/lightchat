package com.zhj.filter;

import com.alibaba.fastjson.JSON;
import com.zhj.entity.ExceptionEntity;
import com.zhj.entity.LogRecord;
import com.zhj.entity.UserCount;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.service.IExceptionService;
import com.zhj.service.ILogRecordService;
import com.zhj.util.EmojiUtils;
import com.zhj.util.TimeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * ExceptionHandler
 *
 * @author hongjun.h.zhang@cn.pwc.com
 * @description TODO
 * @date 2022/11/4 9:29
 */
@Component
@Aspect
public class ExceptionHandler {
    @Resource
    IExceptionService exceptionService;
    @Resource
    ILogRecordService logRecordService;

    @Pointcut("execution(* com.zhj.controller..*(..))")
    public void executePointCut(){

    }

    @AfterReturning(value = "executePointCut()", returning = "keys")
    public void saveOperationLog(JoinPoint joinPoint, Object keys) {

    }

    @AfterReturning(pointcut = "executePointCut()", returning = "methodResult")
    public void saveLogRecord(JoinPoint joinPoint, Object methodResult){
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            Optional<ExcludeLogParam> annotation = Optional.ofNullable(method.getAnnotation(ExcludeLogParam.class));
            if(!annotation.isPresent() || annotation.get().ignoreLog()){
                return;
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 获取RequestAttributes
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            // 从获取RequestAttributes中获取HttpServletRequest的信息
            assert requestAttributes != null;
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            // 请求的参数
            assert request != null;
            Map<String, String> rtnMap = convertMap(request.getParameterMap());
            String mapToString = convertMapToString(rtnMap);
            Object[] objArr = joinPoint.getArgs();
            String params = "".equals(mapToString) ? convertObject(objArr): mapToString;
            LogRecord logRecord = new LogRecord();
            String userCount = UserContext.getUserCountOrDefault("");
            if ("".equals(userCount)){
                logRecord.setUserCount(getUserCount(objArr));
            }else{
                logRecord.setUserCount(UserContext.getUserCountOrDefault(""));
            }
            logRecord.setParam(EmojiUtils.emojiConvert(params));
            logRecord.setMethodName(methodName);
            logRecord.setUri(request.getRequestURI());
            logRecord.setIp(getIp(request));
            logRecord.setCreateTime(TimeUtil.getSimpleTime());
            logRecordService.save(logRecord);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    @AfterThrowing(pointcut = "executePointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            Optional<ExcludeLogParam> annotation = Optional.ofNullable(method.getAnnotation(ExcludeLogParam.class));
            if(annotation.isPresent() && annotation.get().ignoreException()){
                return;
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            assert request != null;
            Map<String, String> rtnMap = convertMap(request.getParameterMap());
            String mapToString = convertMapToString(rtnMap);
            Object[] objArr = joinPoint.getArgs();
            String params = "".equals(mapToString) ? convertObject(objArr): mapToString;
            ExceptionEntity exception = new ExceptionEntity();
            exception.setParam(params);
            exception.setMethodName(methodName);
            exception.setClassName(e.getClass().getName());
            exception.setMessage(stackTraceToString(e.getClass().getName() , e.getMessage() , e.getStackTrace()));
            exception.setUri(request.getRequestURI());
            exception.setIp(getIp(request));
            exception.setCreateTime(TimeUtil.getSimpleTime());
            exceptionService.save(exception);
        } catch (Exception ignored) {
        }
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> convertMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换post 请求参数
     *
     * @param arr post获取的参数数组
     */
    public String convertObject(Object[] arr) {
       StringJoiner sj = new StringJoiner(",","","");
        for (Object o : arr) {
            if (o instanceof HttpServletResponse || o instanceof HttpServletRequest){
               continue;
            }
            sj.add(JSON.toJSONString(o));
        }
        return sj.toString();
    }

    /**
     * 获取帐户
     * @param arr
     * @return
     */
    public String getUserCount(Object[] arr) {
        for (Object o : arr) {
           if (o instanceof UserCount){
               return ((UserCount) o).getUserCount();
           }
        }
        return "";
    }

    public String convertMapToString(Map<String, String> map){
        if (map.size() == 0){
            return "";
        }
        StringJoiner sj = new StringJoiner(",","(",")");
        for (Map.Entry<String, String> str : map.entrySet()) {
            sj.add(str.getKey() + ":" + str.getValue());
        }
        return sj.toString();
    }

    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet).append("\n");
        }
        String msg = "";
        if(strbuff.length() > 1000){
            msg = strbuff.substring(0, 1000);
        }
        return exceptionName + ":" + exceptionMessage + "\n" + msg;
    }


    private static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
