package com.ycsoft.report.query.redis;

import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.daq.translate.CacheKey;

/**
 * redis使用的CacheKey 
 * 多了一个rediskey 用于redis缓存提取
 * 要重新设计排序的键值保存
 * @author new
 *
 */
public class RedisCacheKey extends CacheKey {

	private String rediskey=null;
	private String query_id=null;
	
	public RedisCacheKey(List<Object> keys,List<Integer> sorts,String query_id) throws ReportException {
		super(keys,sorts);
		this.query_id=query_id;
		
	}
	/**
	 * redis缓存键值
	 * 根据keys的值列表增加一定的格式，生成值列表
	 * cube结果内部计算时，缓存到redis用的键值
	 * @return
	 */
	public String getRediskey() {
		if(this.rediskey==null){
			StringBuilder bf=new StringBuilder();
			bf.append(this.query_id).append(":");
			for(Object obj:getKeys()){
				bf.append(obj).append(',');
			}
			this.rediskey=bf.toString();
		}
		return rediskey;
	}
}
