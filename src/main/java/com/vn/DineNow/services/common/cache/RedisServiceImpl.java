package com.vn.DineNow.services.common.cache;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service implementation for interacting with Redis as a key-value store.
 * Provides generic methods to save, retrieve, check, and delete objects in Redis.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisServiceImpl implements RedisService {

    RedisTemplate<String, Object> redisTemplate;

    /**
     * Saves an object into Redis with the specified key and expiration time.
     *
     * @param key      the key to store the object under
     * @param object   the object to store
     * @param timeout  the expiration time
     * @param timeUnit the time unit for the expiration
     * @param <T>      the type of the object
     */
    @Override
    public <T> void saveObject(String key, T object, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, object, timeout, timeUnit); // Lưu object vào Redis với TTL
    }

    /**
     * Retrieves an object from Redis and casts it to the expected class type.
     *
     * @param key   the key to retrieve the object
     * @param clazz the class to cast the result to
     * @param <T>   the expected type
     * @return the object if found, otherwise null
     */
    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Object value = valueOps.get(key);

        // Safe cast the retrieved object to the expected type
        return value != null ? clazz.cast(value) : null;
    }

    /**
     * Checks if a key exists in Redis.
     *
     * @param key the key to check
     * @return true if the key exists, false otherwise
     */
    @Override
    public boolean objectExists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Deletes an object from Redis by key.
     *
     * @param key the key to delete
     */
    @Override
    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }
}
