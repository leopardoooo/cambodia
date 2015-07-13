package com.ycsoft.report.dao.redis;

import java.util.List;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.db.RedisConn;

@Component
public class JedisDao {
	
	public List<byte[]> mget(byte[]...keys) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			return jedis.mget(keys);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	
	public void del(byte[]...keys) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.del(keys);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public void mset(byte[]...keysvalues) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.mset(keysvalues);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public void set(String key,String value) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.set(key, value);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public String get(String key) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			return jedis.get(key);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public void set(byte[] key,byte[] value) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.set(key, value);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public byte[] get(byte[] key) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			return jedis.get(key);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public byte[] hget(byte[] key,byte[] field) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			return jedis.hget(key, field);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public void hset(byte[] key,byte[] field,byte[] value) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.hset(key, field, value);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	
	public void rpush(byte[] key,byte[]...strings) throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.rpush(key, strings);
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
}
