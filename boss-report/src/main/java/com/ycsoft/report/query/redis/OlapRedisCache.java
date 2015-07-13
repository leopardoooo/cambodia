package com.ycsoft.report.query.redis;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.query.QueryContainer;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.QueryResultOlap;

/**
 * cube查询的结果集缓存
 * @author new
 *
 */
public class OlapRedisCache extends CommonRedisCache {

	private static final String KEY_CUBE="cube:";
	private QueryResultOlap olap=null;
	private String base_cache_key=null;
	public OlapRedisCache(QueryResultOlap olap, JedisDao jedisDao) throws ReportException {
		super(olap, jedisDao);
		this.olap=olap;
		
		initBaseCacheKey();
	}
	/**
	 * 初始化cube查询缓存键值
	 * @return
	 * @throws ReportException
	 */
	public String initBaseCacheKey() throws ReportException{
		QueryResult qr=QueryContainer.getRepQuery(olap.getCacheQueryID());
		if(qr==null) throw new ReportException("报表超时已销毁，请返回查询界面重新执行");
		if(ReportConstants.DATABASETYPE_REALTIME
				.equals(SystemConfig.getDatabaseMap().get(qr.getDatabase()).getType())){
			this.base_cache_key=qr.getQueryId();
		}else{
			if(qr.getQRSHA()==null){
				throw new ReportException("历史数据表 QRSHA is null");
			}
			this.base_cache_key=qr.getQRSHA();
		}
		
		
		return this.base_cache_key;
	}
	/**
	 * 提取cube结果集对应键值
	 */
	@Override
	public String getContextKey(int page_idx) {
		StringBuilder bf=new StringBuilder();
		bf.append(olap.getRepId()).append(KEY_CUBE)
			.append(this.base_cache_key).append("_").append(page_idx);
		return bf.toString();
	}
	
	

}
