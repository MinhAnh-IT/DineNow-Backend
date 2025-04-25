package com.vn.DineNow.services.common.cache;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * Save an object to Redis with a custom key.
     *
     * @param key      the custom key
     * @param object   the object to be saved
     * @param timeout  the time-to-live for the object
     * @param timeUnit the time unit for the timeout
     * @param <T>      the type of the object
     */
    <T> void saveObject(String key, T object, long timeout, TimeUnit timeUnit);

    /**
     * Get an object from Redis with a custom key.
     *
     * @param key   the custom key
     * @param clazz the class of the object
     * @param <T>   the type of the object
     * @return the object retrieved from Redis
     */
    <T> T getObject(String key, Class<T> clazz);

    /**
     * Check if an object exists in Redis with a custom key.
     *
     * @param key the custom key
     * @return true if the object exists, false otherwise
     */
    boolean objectExists(String key);

    /**
     * Delete an object from Redis with a custom key.
     *
     * @param key the custom key
     */
    void deleteObject(String key);
}
