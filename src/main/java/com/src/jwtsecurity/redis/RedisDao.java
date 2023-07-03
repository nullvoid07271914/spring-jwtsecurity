package com.src.jwtsecurity.redis;

public interface RedisDao {

	public void save(String username, String token, long milli);

	public boolean verifyExist(String username, String token);
}
