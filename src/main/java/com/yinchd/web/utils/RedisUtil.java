package com.yinchd.web.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作类
 * @author yinchd
 * @date 2019/9/20
*/
@Configuration
@Component
public class RedisUtil {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 批量删除对应的value
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			this.remove(key);
		}
	}

	/**
	 * 批量删除key
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = stringRedisTemplate.keys(pattern);
		if (keys != null && keys.size() > 0)
			stringRedisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 */
	public void remove(final String key) {
		if (exists(key)) {
			stringRedisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 */
	public Boolean exists(final String key) {
		return stringRedisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 */
	public String getString(final String key) {
		ValueOperations<String, String> operations = this.getOperations();
		return operations.get(key);
	}


	/**
	 * 读取缓存
	 */
	public Integer getInt(final String key) {
		String count = this.getString(key);
		return count == null ? null : Integer.valueOf(count);
	}

	/**
	 * 写入缓存
	 */
	public void set(final String key, String value) {
		ValueOperations<String, String> operations = this.getOperations();
		operations.set(key, value);
	}

	public ValueOperations<String, String> getOperations() {
		return stringRedisTemplate.opsForValue();
	}

	/**
	 * 存储值
	 * @param key redisKey
 	 * @param value redisValue
 	 * @param expireTime 超时时间，默认是秒
	 * @author yinchd
	 * @date 2019/11/15
	*/
	public void set(final String key, String value, Integer expireTime, TimeUnit timeUnit) {
		ValueOperations<String, String> operations = this.getOperations();
		operations.set(key, value);
		stringRedisTemplate.expire(key, expireTime, timeUnit);
	}

	public int keysCount(String keys) {
		Set<String> set = stringRedisTemplate.keys(keys);
		return set != null ? set.size() : 0;
	}

	public Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

}
