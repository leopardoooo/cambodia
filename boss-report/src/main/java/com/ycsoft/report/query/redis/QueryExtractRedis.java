package com.ycsoft.report.query.redis;

import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.daq.CubeCache;
import com.ycsoft.report.query.daq.translate.CacheTranslateCube;

public class QueryExtractRedis extends CubeCache {

	
	private JedisDao jedisDao;
	private String query_id;
	public QueryExtractRedis(String query_cache_id, QueryResult qr,
			AbstractDataSet dataset,JedisDao jedisDao) {
		super(query_cache_id, qr, dataset);
		this.jedisDao=jedisDao;
		this.query_id=qr.getQueryId();
	}
	
	
	/**
	 * 创建cube按配置计算变换后数据体对象
	 */
	@Override
	protected CacheTranslateCube createCacheTranslateCube() {
		return  new RedisAcquisition(this.jedisDao,this.query_id);
	}
	

}
