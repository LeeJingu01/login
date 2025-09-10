package com.beyond.match.matching.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // Set에 값 추가 (SADD)
    public void addToSet(String key, String value) {

        redisTemplate.opsForSet().add(key, value);
    }

    // Set에서 모든 값 가져오기 (SMEMBERS)
    public Set<String> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // Set에서 값 삭제 (SREM)
    public void removeFromSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    // Set에 값이 존재하는지 확인 (SISMEMBER)
    public boolean isMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    // Set의 원소 개수 가져오기 (SCARD)
    public long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }
}
