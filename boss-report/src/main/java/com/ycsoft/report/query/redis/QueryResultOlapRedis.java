package com.ycsoft.report.query.redis;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.daq.QueryExtract;

public class QueryResultOlapRedis extends QueryResultOlap {

	private JedisDao jedisDao=null;
	
	public QueryResultOlapRedis(String query_id, RepDefine rep, String sql,String cache_query_id,JedisDao jedisDao) throws ReportException {
		super(query_id, rep, sql, cache_query_id);
		this.jedisDao=jedisDao;
	}


	/**
	 * cube计算结果提取
	 */
	@Override
	protected QueryExtract getQueryExtract() {
		return new QueryExtractRedis(this.cache_query_id,this,this,this.jedisDao);
	}

}
