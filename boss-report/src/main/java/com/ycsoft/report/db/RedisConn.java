package com.ycsoft.report.db;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConn {
	
	private static JedisPool pool;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(bundle
				.getString("redis.pool.maxActive")));
		
		config.setMaxIdle(Integer.valueOf(bundle
				.getString("redis.pool.maxIdle")));
		config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(bundle
				.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle
				.getString("redis.pool.testOnReturn")));
		pool = new JedisPool(config, bundle.getString("redis.ip"),
				Integer.valueOf(bundle.getString("redis.port")));
	}
	
	public static Jedis getConn(){
		return pool.getResource();
	}
	public static void  close(Jedis jedis){
		pool.returnResource(jedis);
	}
	
	public static void main(String[] args) {
		// 从池中获取一个Jedis对象
		Jedis jedis = pool.getResource();
		String keys = "name";
		// 删数据
		jedis.del(keys);
		// 存数据
		jedis.set(keys, "snowolf");
		// 取数据
		String value = jedis.get(keys);

		System.out.println(value);

		// 释放对象池
		pool.returnResource(jedis);
		
		
	}

}
