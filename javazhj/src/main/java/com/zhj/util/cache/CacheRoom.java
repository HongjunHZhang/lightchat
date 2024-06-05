package com.zhj.util.cache;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.zhj.entity.UserCount;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.UserCountMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * CacheRoom
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2023/2/3 14:43
 */
@Component
public class CacheRoom implements ApplicationContextAware {
    private static final Map<String, UserCount> USER_COUNT_CACHE = new ConcurrentHashMap<>(100);
    private static final int USER_COUNT_CACHE_MAX_SIZE = 1000;
    /**
     * 最长时间未被使用的帐户队列
     */
    private static final Deque<String> DEQUE = new LinkedBlockingDeque<>();
    private static final String USER_COUNT_CACHE_PREFIX = "user:count:detail:cache";
    private static final long USER_COUNT_CACHE_REDIS_MAX_SIZE = 10000;
    private static RedisTemplate<String,Object> redisTemplate;
    private static UserCountMapper userCountMapper;

    public static Map<String, UserCount> getUserCountCache() {
        return USER_COUNT_CACHE;
    }

    /**
     * 通过用户帐户得到账户信息
     * @param userCount 用户帐户
     * @return 帐户信息
     */
    public static UserCount getUserCountOfCount(String userCount){
        if (USER_COUNT_CACHE.containsKey(userCount)){
            return USER_COUNT_CACHE.get(userCount);
        }

        try {
            Object ret = redisTemplate.opsForHash().get(USER_COUNT_CACHE_PREFIX, userCount);
            if (ret != null){
                UserCount userCountRedis = JSON.parseObject(ret.toString(), UserCount.class);
                if (USER_COUNT_CACHE.size() < USER_COUNT_CACHE_MAX_SIZE){
                    USER_COUNT_CACHE.put(userCount,userCountRedis);
                }
                return userCountRedis;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        UserCount userCountSelect = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("user_count",userCount));
        if (userCountSelect == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EXIST);
        }
        if (USER_COUNT_CACHE.size() < USER_COUNT_CACHE_MAX_SIZE){
            USER_COUNT_CACHE.put(userCount,userCountSelect);
        }

        try {
            if (redisTemplate.opsForHash().size(USER_COUNT_CACHE_PREFIX) < USER_COUNT_CACHE_REDIS_MAX_SIZE){
                redisTemplate.opsForHash().put(USER_COUNT_CACHE_PREFIX,userCount,JSON.toJSONString(userCountSelect));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return userCountSelect;
    }

    /**
     * 通过用户帐户修改账户信息
     * @param userCount 用户账户
     */
    public static void updateUserCountOfCount(String userCount){
        USER_COUNT_CACHE.remove(userCount);
        DEQUE.remove(userCount);
        try {
            redisTemplate.opsForHash().delete(USER_COUNT_CACHE_PREFIX,userCount);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Map<String,UserCount> getUserCountOfCountList(List<String> userCountList){
        Map<String,UserCount> retMap = new HashMap<>();
        List<String> noCacheCount = new ArrayList<>();
        for (String  userCount: userCountList) {
            if (!USER_COUNT_CACHE.containsKey(userCount)){
                noCacheCount.add(userCount);
                continue;
            }
            retMap.put(userCount,USER_COUNT_CACHE.get(userCount));
        }

        if (noCacheCount.size() > 0){
            //in查询超过1000速度会变慢很多
            List<List<String>> partition = Lists.partition(noCacheCount, 500);
            List<UserCount> userCountSelectList = new ArrayList<>();
            for (List<String> list : partition) {
                userCountSelectList.addAll(userCountMapper.selectList(new QueryWrapper<UserCount>().lambda().in(true,UserCount::getUserCount, list)));
            }
            userCountSelectList.forEach(t1->{
                retMap.put(t1.getUserCount(),t1);
                if (USER_COUNT_CACHE.size() < USER_COUNT_CACHE_MAX_SIZE){
                    USER_COUNT_CACHE.put(t1.getUserCount(),t1);
                }
            });
        }
        return retMap;
    }

    public static void updateUserCountCache(String userCount){
        DEQUE.remove(userCount);
        DEQUE.offer(userCount);
    }

    public static boolean hasAdminRole(String userCount){
        return getUserCountOfCount(userCount).getRootLevel().compareTo("1") > 0;
    }

    public static boolean checkAdminRole(){
        return hasAdminRole(UserContext.getUserCount());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Object bean = applicationContext.getBean("redisTemplate");
        userCountMapper = (UserCountMapper) applicationContext.getBean("userCountMapper");
        if (bean instanceof RedisTemplate){
                redisTemplate = (RedisTemplate<String,Object>)bean;
        }else {
            throw CustomException.createCustomException("未找到redisTemplate");
        }
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
