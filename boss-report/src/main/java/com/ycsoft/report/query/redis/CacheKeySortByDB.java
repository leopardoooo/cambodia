package com.ycsoft.report.query.redis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.daq.translate.CacheHeadCell;
import com.ycsoft.report.query.daq.translate.CacheKey;

/**
 * 调用存储过程创建一张临时表
 * 把CacheKey数据写入数据库
 * 调用存储过程分析新建的临时表
 * <p>
 *
 */
public class CacheKeySortByDB {

	
	
	/**
	 * 是否发生异常
	 */
	private boolean work_error=false;
	//排序用表名
	private String sorttabname=null;
	
	public enum WorkMode{
		READ,WRITE;
	}
	
	private WorkMode mode;
	
	private String writesql=null;
	private int no_crosshead_start=0;
	
	//哪个id列是带有字段
	private boolean[] field_is_sort=null;
	
	private String query_id=null;
	
	public CacheKeySortByDB(int no_crosshead_start,String query_id){
		this.no_crosshead_start=no_crosshead_start;
		this.query_id=query_id;
		
	}
	/**
	 * 打开读取工作模式
	 * @param headlist
	 * @param no_crosshead_start
	 * @param sorttab
	 * @throws ReportException
	 */
	public void openReadMode() throws ReportException{
		if(this.mode==null||WorkMode.READ.equals(this.mode)){
			throw new ReportException("cannot open read_mode");
		}	
		this.mode=WorkMode.READ;
		this.readModeInit();
	}
	/**
	 * 读取模式初始化
	 * select f0,f1,f2,f3 from r_12312 order by f0,to_number(f1),f2,f3
	 * @throws ReportException 
	 */
	private void readModeInit() throws ReportException{
		try {
			//生成读取语句
			StringBuilder selectsql=new StringBuilder();
			StringBuilder orderbysql=new StringBuilder();
			selectsql.append("select ");
			int f_idx=0;
			for(int i=0;i<no_crosshead_start;i++){
				if(i==0){
					selectsql.append(" f").append(f_idx);
				}else{
					selectsql.append(",f").append(f_idx);
				}
				f_idx++;
				//排序字段
				if(this.field_is_sort[i]){
					orderbysql.append(",to_number(f").append(f_idx).append(")");
					f_idx++;
				}
			}
			selectsql.append(" from ").append(this.sorttabname).append(" order by ").append(orderbysql.toString().substring(1, orderbysql.length()));
		
			if(this.read_conn==null){
				read_conn=ConnContainer.getConn(ReportConstants.DATABASETYPE_CUBE_SORT);
				read_conn.setAutoCommit(false);
			}
			if(this.read_ps!=null){
				throw new ReportException("PreparedStatement has open.");
			}
			String sql=selectsql.toString();
			LoggerHelper.debug(this.getClass(), sql);
			this.read_ps=read_conn.prepareStatement(sql);
			this.read_ps.setFetchSize(1000);
			this.read_rs=this.read_ps.executeQuery();
		} catch (Exception e) {
			this.work_error=true;
			throw new ReportException("数据库错误",e);
		}
	}
	
	private ResultSet read_rs=null;
	private PreparedStatement read_ps=null;
	private Connection read_conn=null;
	/**
	 * 读取一批key
	 * @throws ReportException 
	 */
	public RedisCacheKey readCacheKey() throws ReportException{
		try {
			RedisCacheKey key=null;
			if(this.read_rs.next()){
				List<Object> list=new ArrayList<Object>();
				List<Integer> sort=new ArrayList<Integer>();//伪造的排序
				for(int i=1;i<=this.no_crosshead_start;i++){
					list.add(read_rs.getString(i));
					sort.add(i);
				}
				key=new RedisCacheKey(list,sort,this.query_id);
			}
			return key;
		} catch (Exception e) {
			this.work_error=true;
			throw new ReportException(e);
		}
	}

	
	
	/**
	 * 打开写入工作模式
	 * @param key
	 * @throws ReportException
	 */
	public void openWriteMode() throws ReportException{
		if(WorkMode.WRITE.equals(this.mode)){
			return;
		}else if(WorkMode.READ.equals(this.mode)){
			throw new ReportException("is read_mode");
		}else{
			this.mode=WorkMode.WRITE;
		}
		this.writeModeInit();
	}
	
	/**
	 * 写入一组键值
	 * @param keys
	 * @throws SQLException
	 * @throws ReportException 
	 */
	public void writeCacheKey(List<CacheKey> keys) throws ReportException{
		if(keys==null||keys.size()==0) return;
		Connection conn=null;
		PreparedStatement ps=null;
		try{
			conn=ConnContainer.getConn(ReportConstants.DATABASETYPE_CUBE_SORT);
			ps=conn.prepareStatement(this.writesql);
			for(CacheKey key:keys){
				int field_idx=1;
				for(int i=0;i<this.no_crosshead_start;i++){
					String id=key.getKeys().get(i).toString();
					ps.setString(field_idx,id);
					field_idx++;
					if(this.field_is_sort[i]){
						ps.setString(field_idx,key.getSorts().get(i).toString());
						field_idx++;
					}
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		}catch (Exception e) {
			this.work_error=true;
			try {
				conn.rollback();
			} catch (Exception e1) {}
			throw new ReportException(e);
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
				}
				ps=null;
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
				conn=null;
			}
		}
	}
	
	

	/**
	 * 写入模式初始化
	 * 计算字段长度,cube_cache_key的临时排序表的字段数量，
	 * 一个表头对应一个字段，如果一个表头开始了自动以排序功能，则多占一个字段用于排序
	 * 如果维度的一个层启用了排序，通过在数据库建表实现排序；
	 * 调用数据库存储过程，实现建表和删表
	 * @throws ReportException 
	 */
	private void writeModeInit() throws ReportException{
		CallableStatement cs=null;
		Connection conn=null;
		try{
			//分析字段数和哪个id对应有排序
			int fields=0;
			field_is_sort=new boolean[no_crosshead_start];
			for(int i=0;i<this.no_crosshead_start;i++){
				field_is_sort[i]=true;
			}
			fields=no_crosshead_start*2;
			if(conn==null){
				conn=ConnContainer.getConn(ReportConstants.DATABASETYPE_CUBE_SORT);
				conn.setAutoCommit(false);
			}
			//调用存储过程创建表
			//参数1字段，参数2返回表名，参数3错误内容
			cs=conn.prepareCall("{call proc_repsort_create(?,?,?) }");
			cs.setInt(1,fields );
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.executeUpdate();
			conn.commit();
			
			sorttabname=cs.getString(2);
			LoggerHelper.debug(this.getClass(),sorttabname);
			String error=cs.getString(3);
			if(error!=null&&error.trim().length()>0){
				this.work_error=true;
				throw new ReportException("CUBE排序调用proc_repsort_create错误："+error);
			}
						
			// insert into r_1231(f1,f2,f3) values (?,?,?)
			//生成写入SQL语句
			StringBuilder writesql=new StringBuilder();
			writesql.append("insert into ").append(sorttabname).append("(f0");
			for(int i=1;i<fields;i++){
				writesql.append(",f").append(i);
			}
			writesql.append(")values(?");
			for(int i=1;i<fields;i++){
				writesql.append(",?");
			}
			writesql.append(")");
			this.writesql= writesql.toString();
		
		}catch(ReportException e){
			this.work_error=true;
			throw e;
			
		}catch(Exception e){
			this.work_error=true;
			try {
				conn.rollback();
			} catch (SQLException e1) {}
			throw new ReportException("CUBE排序调用proc_repsort_create错误",e);
		}finally{
			if(cs!=null){
				try {
					cs.close();
				} catch (Exception e) {}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 删除新建的排序用表
	 * @throws ReportException 
	 */
	private void dropSortTab() {
		CallableStatement cs=null;
		Connection conn=null;
		try {
			if(conn==null){
				conn=ConnContainer.getConn(ReportConstants.DATABASETYPE_CUBE_SORT);
				conn.setAutoCommit(false);
			}
			//调用存储过程创建表
			cs=conn.prepareCall("{call proc_repsort_drop(?) }");
			cs.setString(1, this.sorttabname);
			cs.executeUpdate();
			conn.commit();
			
		} catch (Exception e) {
			LoggerHelper.error(this.getClass(), "drop sort_tab error", e);
		}finally{
			if(cs!=null){
				try {
					cs.close();
				} catch (Exception e) {}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (Exception e) {}
			}
		}
	}
	
	public void close() {
		//发生异常或者读取模式结束，删除临时排序表
		if(this.work_error||WorkMode.READ.equals(this.mode)){
			this.dropSortTab();	
		}
		if(read_rs!=null){
			try {
				read_rs.close();
			} catch (SQLException e) {
			}
			read_rs=null;
		}
		if(read_ps!=null){
			try {
				read_ps.close();
			} catch (SQLException e) {
			}
			read_ps=null;
		}
		if(read_conn!=null){
			try {
				read_conn.close();
			} catch (SQLException e) {
			}
			read_conn=null;
		}
	}
}
