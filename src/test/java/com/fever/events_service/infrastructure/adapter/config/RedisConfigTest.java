package com.fever.events_service.infrastructure.adapter.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        String key = "testKey";
        String value = "testValue";

        redisTemplate.opsForValue().set(key, value);
        assertEquals(value, redisTemplate.opsForValue().get(key));
    }
}
