package com.ptithcm.shared.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * Ghi khóa kèm giá trị và thời gian hết hạn (Time To Live).
     *
     * @param key
     *            Khóa cần lưu
     * @param value
     *            Giá trị cần lưu
     * @param ttlSeconds
     *            Thời gian sống tính bằng giây (seconds)
     */
    public void set(String key, String value, long ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, ttlSeconds, value);
        }
    }

    /**
     * Lấy giá trị của một khóa. Trả về null nếu không tồn tại hoặc hết hạn.
     *
     * @param key
     *            Khóa cần truy vấn
     * @return Giá trị tương ứng hoặc null
     */
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Xóa một khóa khỏi Redis.
     *
     * @param key
     *            Khóa cần xóa
     */
    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }
}
