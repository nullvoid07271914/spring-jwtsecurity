package com.src.jwtsecurity.logout;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.src.jwtsecurity.redis.RedisDao;

@Repository
public class LogoutBlockList implements RedisDao {

	public static final String HASH_KEY_NAME = "logout_tokens";

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, ?> template;

	@Override
	public void save(String username, String token, long milli) {
		HashOperations<String, String, String> operations = template.opsForHash();
		String hashKey = username + ":" + token;
		operations.put(HASH_KEY_NAME, hashKey, "logged out");
		template.expire(hashKey, milli, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean verifyExist(String username, String token) {
		HashOperations<String, String, String> operations = template.opsForHash();
		String hashKey = username + ":" + token;
		return operations.hasKey(HASH_KEY_NAME, hashKey);
	}
}
