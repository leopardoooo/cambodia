package com.ycsoft.report.query.redis;

import java.io.IOException;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.QueryResultCommon;

public class QueryResultCommonRedis extends QueryResultCommon {
	
	/**
	 * 从redis读取缓存
	 * @throws ReportException 
	 */
	@Override
	protected CacheInput createCacheInput() throws IOException, ReportException {
		return new CommonRedisCache(this,jedisDao).getRedisCacheInput();
	}

	private JedisDao jedisDao;
	/**
	 * 使用redis作为缓存输出
	 * @throws ReportException 
	 */
	@Override
	protected CacheOutput createCacheOutput() throws IOException, ReportException {
		return new CommonRedisCache(this,jedisDao).getRedisCacheOutput();
	}

	public QueryResultCommonRedis(InitQueryDto qdto,JedisDao jedisDao) throws ReportException {
		super(qdto,false);
		this.jedisDao=jedisDao;
		this.execute(qdto.getSql());
	}
	
	

}
