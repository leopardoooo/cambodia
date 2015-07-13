package com.ycsoft.report.query.menory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import redis.clients.jedis.Jedis;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.db.RedisConn;
import com.ycsoft.report.query.QueryContainer;

/**
 * 清理报表的索引缓存
 * @author new
 *
 */
public class CacheManageClearIndex extends Thread {

	public long clearCache(){
		FileHelper.delAllFile(ReportConstants.REP_TEMP_TXT);
		QueryContainer.getQuerycontainer().clear();
		QueryContainer.getQueryrealtimecon().clear();
		return DateHelper.strToDate(DateHelper.getNextDay()).getTime();
	}
	/**
	 * 清理内存服务器缓存
	 * @throws ReportException 
	 */
	public void clearRedis() throws ReportException{
		Jedis jedis=null;
		try{
			jedis=RedisConn.getConn();
			jedis.flushAll();
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			if(jedis!=null){
				RedisConn.close(jedis);
			}
		}
	}
	/**
	 * 清理查询日志到备份表
	 * @throws ReportException
	 */
	public void clearQueryLog() throws ReportException{
		Connection conn=null;
		String sql="";
		Statement stmt=null;
		try {
			conn=ConnContainer.getConn(ReportConstants.DATABASE_SYSTEM);
			conn.setAutoCommit(false);
			stmt=conn.createStatement();
			sql="insert into rep_query_log_his(query_id,rep_id,isvalid,keylist,querytime,querynum,optr_id,create_date,client_ip)"
				+" select query_id,rep_id,isvalid,keylist,querytime,querynum,optr_id,create_date,client_ip "
				+" from rep_query_log ";
			stmt.execute(sql);
				//this.executeUpdate(sql);
			sql="truncate table rep_query_log ";
			stmt.execute(sql);
			conn.commit();
		} catch (Exception e) {
			LoggerHelper.error(this.getClass(), e.getMessage(), e) ;
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {}
			try {
				conn.close();
			} catch (SQLException e) {}
		}
		 
	}
	/**
	 * 清理文件组件的缓冲
	 */
	public void clearUploadFile(){
		for(RepKeyCon keycon:SystemConfig.getConList()){
			if(ReportConstants.htmlcode_fileupload.equals(keycon.getHtmlcode())
					&&StringHelper.isNotEmpty(keycon.getDatabase()))
				clearUploadFileKeyCon(keycon);
		}
	}
	/**
	 * 清理单个文件组件的缓冲
	 * @param keycon
	 * @throws ReportException
	 */
	private void clearUploadFileKeyCon(RepKeyCon keycon) {
		Connection conn=null;
		String sql="";
		Statement stmt=null;
		try {
			conn=ConnContainer.getConn(keycon.getDatabase());
			conn.setAutoCommit(false);
			stmt=conn.createStatement();
			sql="truncate table rep_filekey_value";
			stmt.execute(sql);
			conn.commit();
		} catch (Exception e) {
			LoggerHelper.error(this.getClass(), e.getMessage(), e) ;
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {}
			try {
				conn.close();
			} catch (SQLException e) {}
		}
	}
	
	public void run(){
		try {
			//System.out.println(now);
			long todaylast=DateHelper.strToDate(DateHelper.getNextDay()).getTime();
			long now=System.currentTimeMillis();
			if(todaylast>now)
				Thread.sleep(todaylast-now+1000*60*30);
			/**
			 * 每天半夜(00:30:00)时刻清理索引缓存
			 */
			while(true){
				try{
					todaylast=clearCache();
					clearQueryLog();
					clearUploadFile();
					clearRedis();
				}catch(Exception e1){
					LoggerHelper.error(this.getClass(), "Clear_Index_Cache_ERROR", e1);
				}
				Thread.sleep(todaylast-System.currentTimeMillis()+1000*60*30);
			}
			
		} catch (Exception e) {
			LoggerHelper.error(this.getClass(), "Clear_Index_Cache_ERROR", e);
		}
	}
}
