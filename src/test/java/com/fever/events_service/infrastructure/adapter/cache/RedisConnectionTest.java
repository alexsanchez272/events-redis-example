package com.fever.events_service.infrastructure.adapter.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        assertNotNull(redisTemplate);
        redisTemplate.opsForValue().set("testKey", "testValue");
        Object result = redisTemplate.opsForValue().get("testKey");
        assertNotNull(result);
    }
}
