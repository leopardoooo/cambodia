package com.ycsoft.report.query.redis;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SerializeUtil;
import com.ycsoft.report.commons.StringUtil;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.query.QueryResult;

public class CommonRedisCache implements QRRedisCache {
	
	private int page_row_max=1000;
	
	private static final String KEY_BEAN="bean:";
	private static final String KEY_BASE="base:";
	
	private static final String KEY_BEAN_SQL="sql";
	private static final String KEY_BEAN_QUERYID="query_id";
	private static final String KEY_BEAN_HEAD="head";
	private static final String KEY_BEAN_ROWS="rows";
	
	/**
	 * 缓存基础键值
	 */
	private String base_cache_key="";

	private QueryResult qr=null;
	
	private JedisDao jedisDao=null;
	
	public CommonRedisCache(QueryResult qr,JedisDao jedisDao) throws ReportException{
		this(qr,ReportConstants.cache_page_row_max,jedisDao);
	}
	
	private CommonRedisCache(QueryResult qr,int page_row_max,JedisDao jedisDao) throws ReportException{
		this.jedisDao=jedisDao;
		if(this.page_row_max>0){
			this.page_row_max=page_row_max;
		}
		this.qr=qr;
		if(ReportConstants.DATABASETYPE_REALTIME
				.equals(SystemConfig.getDatabaseMap().get(qr.getDatabase()).getType())){
			this.base_cache_key=this.qr.getQueryId();
		}else{
			if(qr.getQRSHA()==null){
				throw new ReportException("历史数据表 QRSHA is null");
			}
			this.base_cache_key=qr.getQRSHA();
		}
	}

	/**
	 * 
	 */
	public void cacheQueryId(String query_id) throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		jedisDao.hset(bf.toString().getBytes(), KEY_BEAN_QUERYID.getBytes(), query_id.getBytes());
	}
	/**
	 * 提取query_id
	 */
	public String extractQueryId() throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		byte[] queryid= jedisDao.hget(bf.toString().getBytes(), KEY_BEAN_QUERYID.getBytes());
		if(queryid==null||queryid.length==0)
			return null;
		else
			return new String(queryid);
	}
	/**
	 * 行数
	 */
	public void cacheRows(Integer rows) throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		jedisDao.hset(bf.toString().getBytes(), KEY_BEAN_ROWS.getBytes(), rows.toString().getBytes());
		
	}
	
	/**
	 * 行数
	 */
	public Integer extractRows() throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		byte[] rows= jedisDao.hget(bf.toString().getBytes(), KEY_BEAN_ROWS.getBytes());
		if(rows==null||rows.length==0)
			return null;
		else
			return Integer.valueOf(new String(rows));
	}
	/**
	 * 存查询sql
	 */
	public void cacheQuerySql(String sql) throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		
		jedisDao.hset(bf.toString().getBytes(), KEY_BEAN_SQL.getBytes(), StringUtil.compressZip(sql.getBytes()));

	}
	

	/**
	 * 提取查询sql
	 */
	public String extractQuerySql() throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		
	   byte[] sql=  StringUtil.compressZip(jedisDao.hget(bf.toString().getBytes(), KEY_BEAN_SQL.getBytes()));
	   if(sql==null)
		   return null;
	   else
		   return new String(sql);
	}
	/**
	 * 缓存表头
	 */
	public void cacheHead(Serializable head) throws ReportException {
		byte[] h= SerializeUtil.serialize(head);
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		jedisDao.hset(bf.toString().getBytes(), KEY_BEAN_HEAD.getBytes(), h);
	}
	/**
	 * 提取表头
	 */
	public Serializable extractHead() throws ReportException {
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BEAN).append(this.base_cache_key);
		byte[] h= jedisDao.hget(bf.toString().getBytes(), KEY_BEAN_HEAD.getBytes());
		if(h==null||h.length==0)
			return null;
		else
			return SerializeUtil.unserialize(h);
	}
	/**
	 * 提取结果集对应键值
	 * @return
	 */
	public String getContextKey(int page_idx){
		StringBuilder bf=new StringBuilder();
		bf.append(this.qr.getRepId()).append(KEY_BASE)
			.append(this.base_cache_key).append("_").append(page_idx);
		return bf.toString();
	}
	private int i=0;
	private ArrayList<Serializable> page=null;
	private int page_index=1;
	/**
	 * row=null 表示写入结束
	 * @param row
	 * @throws ReportException
	 */
	public void cacheContext(Serializable row) throws ReportException {
		
		if(page==null){
			page=new ArrayList<Serializable>(this.page_row_max);
		}
		if(i==this.page_row_max||(row==null&&page!=null&&page.size()>0)){
			//1000行压缩后写入redis缓存
			byte[] value= StringUtil.compressZip(SerializeUtil.serialize(page));
			jedisDao.set(getContextKey(this.page_index).getBytes(), value);
			//重新计数
			i=0;
			//页码加1
			page_index++;
			if(row!=null) page.clear();
		}else{
			page.add(row);
			i++;
		}
	}
	/**
	 * 提取内容
	 * @throws ReportException 
	 */
	public Serializable extractContextPage(int page_index) throws ReportException {
		byte[] value=jedisDao.get(getContextKey(page_index).getBytes());
		if(value==null)
			return null;
		else
			return SerializeUtil.unserialize(StringUtil.uncompressZip(value));
	}

	public int getPage_index() {
		return page_index;
	}



	public CacheInput getRedisCacheInput() {
		return new RedisCacheInput();
	}
	/**
	 * 从redis缓存中读取数据
	 */
	class RedisCacheInput implements CacheInput{
		private int page_idx=0;
		private ArrayList<Serializable> page=new ArrayList<Serializable>(page_row_max);
		private int row_idx=0;
		public RedisCacheInput(){
			page_idx=0;
		}
		public void close() throws Exception {
			if(page!=null) page.clear();
		}
		
		public Object readHead() throws ReportException{
			return extractHead();
		}
		
		public Object readObject() throws ReportException {
			if(row_idx>=page.size()){
				page_idx++;
				page=(ArrayList<Serializable>) extractContextPage(page_idx);
				if(page==null||page.size()==0){
					return null;
				}
				row_idx=0;
			}
			Object row=page.get(row_idx);
			row_idx++;
			return row;
		}
	} 
	
	public CacheOutput getRedisCacheOutput() {
		return new RedisCacheOutput();
	}
	/**
	 * 输出到redis缓存中
	 */
	class RedisCacheOutput implements CacheOutput{
		public void writeHead(Object obj) throws IOException, ReportException {
			cacheHead((Serializable)obj);
		}
		public void writeObject(Object obj) throws IOException, ReportException {
			cacheContext((Serializable)obj);
		}
		public void close() throws IOException, ReportException {
			cacheContext(null);
		}
	}

}
