package com.zhj.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author 789
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public Boolean hasKey(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.hasKey(key);
    }


    public Boolean delete(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.delete(key);
    }

    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }


    public Boolean expire(String key, long timeout, TimeUnit unit) {
        if (null==key||null==unit){
            return false;
        }
        return redisTemplate.expire(key, timeout, unit);
    }


    public Set<String> keys(String pattern) {
        if (null==pattern){
            return null;
        }
        return redisTemplate.keys(pattern);
    }


    public Boolean persist(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.persist(key);
    }


    public Long getExpire(String key, TimeUnit unit) throws Exception {
        if(null==key||null==unit){
            throw new Exception("key or TomeUnit 不能为空");
        }
        return redisTemplate.getExpire(key, unit);
    }


    public void set(String key, Object value) {

        if(null==key||null==value){
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key,Object value,long time,TimeUnit unit){

        if(null==key||null==value||null==unit){
            return;
        }
        redisTemplate.opsForValue().set(key,value,time,unit);
    }

    public Boolean setifAbsen(String key,Object value,long time,TimeUnit unit) throws Exception {

        if(null==key||null==value||null==unit){
            throw new Exception("kkey、value、unit都不能为空");
        }
        return redisTemplate.opsForValue().setIfAbsent(key,value,time,unit);
    }

    public Object get(String key){

        if(null==key){
            return null;
        }
        return  redisTemplate.opsForValue().get(key);
    }

    public String getString(String key){
        if(null==key){
            return null;
        }
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null){
            return null;
        }
        return (String) o;
    }

    public boolean compareValue(String key,String value){
        String retValue = (String) redisTemplate.opsForValue().get(key);
        if (retValue == null){
            return false;
        }
        return retValue.equals(value);
    }

    public Object getSet(String key,Object value){

        if(null==key){
            return null;
        }
        return redisTemplate.opsForValue().getAndSet(key,value);
    }

    public List<Object> mget(Collection<String> keys){

        if(null==keys){
            return Collections.emptyList();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public long incrby(String key,long increment) throws Exception {
        if(null==key){
            throw new Exception("key不能为空");
        }
        return redisTemplate.opsForValue().increment(key,increment);
    }

    public Long decrby(String key,long decrement) throws Exception {
        if(null==key){
            throw new Exception("key不能为空");
        }
        return redisTemplate.opsForValue().decrement(key,decrement);
    }

    public Integer append(String key,String value) throws Exception {
        if(key==null){
            throw new Exception("key不能为空");
        }
        return redisTemplate.opsForValue().append(key,value);
    }

    public Object hget(String key, Object field) {
        if(null==key||null==field){
            return null;
        }
        return redisTemplate.opsForHash().get(key,field);
    }


    public void hset(String key, Object field, Object value) {
        if(null==key||null==field){
            return;
        }
        redisTemplate.opsForHash().put(key,field,value);
    }


    public Boolean hexists(String key, Object field) {
        if(null==key||null==field){
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key,field);
    }


    public Long hdel(String key, Object... fields) {
        if(null==key||null==fields||fields.length==0){
            return 0L;
        }
        return redisTemplate.opsForHash().delete(key,fields);
    }



    public Map<Object, Object> hgetall(String key) {
        if(key==null){
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    public void hmset(String key, Map<String, Object> hash) {

        if(null==key||null==hash){
            return;
        }
        redisTemplate.opsForHash().putAll(key,hash);
    }

    public List<Object> hmget(String key, Collection<Object> fields) {

        if(null==key||null==fields){
            return null;
        }

        return redisTemplate.opsForHash().multiGet(key,fields);
    }


    public Long hIncrBy(String key,Object field,long increment) throws Exception {
        if (null == key || null == field) {
            throw new Exception("key or field 不能为空");
        }
        return redisTemplate.opsForHash().increment(key, field, increment);

    }
    public Long lpush(String key, Object... strs) {
        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key,strs);
    }

    public Long rpush(String key, Object... strs) {
        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().rightPushAll(key,strs);
    }

    public Object lpop(String key) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().leftPop(key);
    }


    public Object rpop(String key) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().rightPop(key);
    }


    public List<Object> lrange(String key, long start, long end) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().range(key,start,end);
    }

    public void ltrim(String key, long start, long end) {
        if(null==key){
            return;
        }
        redisTemplate.opsForList().trim(key,start,end);
    }

    public Object lindex(String key, long index) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().index(key,index);
    }


    public Long llen(String key) {

        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }

    public Long sadd(String key, Object... members) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().add(key, members);

    }


    public Long scard(String key) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().size(key);

    }



    public Boolean sismember(String key, Object member) {
        if (null==key){
            return false;
        }
        return redisTemplate.opsForSet().isMember(key,member);

    }

    public Object srandmember(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().randomMember(key);

    }

    public List<Object> srandmember(String key,int count) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForSet().randomMembers(key,count);

    }


    public Object spop(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().pop(key);

    }

    public Set<Object> smembers(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().members(key);

    }

    public Long srem(String key, Object... members) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().remove(key,members);

    }

    public Boolean smove(String srckey, String dstkey, Object member) {
        if (null==srckey||null==dstkey){
            return false;
        }
        return redisTemplate.opsForSet().move(srckey,member,dstkey);

    }

    public Set<Object> sUnion(String key, String otherKeys) {
        if (null==key||otherKeys==null){
            return null;
        }
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    public Boolean zadd(String key, double score, Object member) {
        if (null==key){
            return false;
        }
        return redisTemplate.opsForZSet().add(key,member,score);

    }


    public Long zrem(String key, Object... members) {
        if(null==key||null==members){
            return 0L;
        }
        return redisTemplate.opsForZSet().remove(key,members);

    }

    public Long zcard(String key) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForZSet().size(key);
    }

    public Double zincrby(String key, double score, Object member) throws Exception {
        if (null==key){
            throw new Exception("key 不能为空");
        }
        return redisTemplate.opsForZSet().incrementScore(key,member,score);
    }


    public Long zcount(String key, double min, double max) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForZSet().count(key, min, max);

    }


    public Long zrank(String key, Object member) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().rank(key,member);

    }

    public Double zscore(String key, Object member) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().score(key,member);
    }


    public Set<Object> zrange(String key, long min, long max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().range(key, min, max);

    }

    public Set<Object> zReverseRange(String key, long start, long end) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().reverseRange(key, start, end);

    }


    public Set<Object> zrangebyscore(String key, double min, double max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);

    }



    public Set<Object> zrevrangeByScore(String key, double min, double max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    public Object execute(String script, Class resultClass, List<String> keys, String args){
        Object execute = redisTemplate.execute(new DefaultRedisScript<>(script, resultClass), keys, args);
        return execute;
    }

    public void deleteByPrex(String prex) {
        Set<String> keys = redisTemplate.keys(prex);
        redisTemplate.delete(keys);
    }
}
