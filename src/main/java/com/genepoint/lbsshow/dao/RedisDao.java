package com.genepoint.lbsshow.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisDao {
	

	@Autowired 
	private RedisTemplate<String, String> stringRedisTemplate;
	
	
	public Map<String,String> getUsers(String key) {
		HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
		Map<String, String> users = opsForHash.entries(key);
		return users;
	}
	
	public String getUser(String user) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		String string = opsForValue.get(user);
		return string;
	}
	
	public String getUserByIndex(String user,Long index) {
		ListOperations<String, String>opsForList = stringRedisTemplate.opsForList();
		String u = opsForList.index(user, index);
		return u;
	}
	
	public String getStringByKey(String key) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		String value = opsForValue.get(key);	
		return value;
	}
	
	public Boolean setValueByKey(String key,String value,Long timeout) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(key, value);
		Boolean flag = stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		return flag;
	}
	
	public List<String> lrange(String key,Integer start,Integer end) {
		ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
		List<String> range = opsForList.range(key, start, end);
		return range;
	}
	
	public Set<String> zrevrange(String key,Integer start,Integer end){
		ZSetOperations<String, String> opsForZSet = stringRedisTemplate.opsForZSet();
		Set<String> range = opsForZSet.reverseRange(key, start, end);
		return range;
	}
	
	public Map<String,String> hgetAll(String key){
		HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
		Map<String, String> entries = opsForHash.entries(key);
		return entries;
	}
	
	public boolean setTimeout(String key, Long timeout) {
		Boolean expire = stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		return expire;
	}
}
