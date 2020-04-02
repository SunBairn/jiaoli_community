package com.zls.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * 获取位图里面的总1数
 */
@Repository
public class RedisServiceExtend {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static String redisCode = "utf-8";

    public Integer bitCount(final String key) {
        return new Integer(redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes())).intValue());
    }

    public Integer bitCount(String key, int start, int end) {
        return new Integer(redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes(), start, end)).intValue());
    }

    public Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        byte[][] bytes = new byte[desKey.length][];
        for (int i = 0; i < desKey.length; i++) {
            bytes[i] = desKey[i].getBytes();
        }
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
    }

}


