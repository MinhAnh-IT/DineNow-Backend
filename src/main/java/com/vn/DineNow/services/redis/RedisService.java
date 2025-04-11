package com.vn.DineNow.services.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService implements IRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Save object to Redis with custom key
    public <T> void saveObject(String key, T object, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, object, timeout, timeUnit);
    }

    // Get object from Redis with custom key
    public <T> T getObject(String key, Class<T> clazz) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return clazz.cast(valueOps.get(key));
    }

    // Check if object exists in Redis with custom key
    public boolean objectExists(String key) {
        return redisTemplate.hasKey(key);
    }

    // Delete object from Redis with custom key
    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }
}