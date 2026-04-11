package com.example.cognitivetwin.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Duration ttl){
        redisTemplate.opsForValue().set(key,value,ttl);
    }

    public <T> T get(String key,Class<T> type){
        Object value = redisTemplate.opsForValue().get(key);
        if(value==null) return null;
        return type.cast(value);
    }

    public boolean setIfAbsent(String key, Object value,Duration ttl){
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                key,value,ttl
        );
        return Boolean.TRUE.equals(success);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }
}
