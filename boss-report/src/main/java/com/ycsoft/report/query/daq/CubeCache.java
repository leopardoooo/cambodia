package com.ycsoft.report.query.daq;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionLevelValueManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.daq.translate.CacheAcquisition;
import com.ycsoft.report.query.daq.translate.CacheTranslateCube;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.DataRole;
import com.ycsoft.report.query.datarole.FuncType;

/**
 * cube查询缓存运算管理
 */
public class CubeCache implements QueryExtract {

	private String query_cache_id=null;
	private AbstractDataSet dataset=null;
	private QueryResult qr=null;
	private boolean dim_check=false;
	public CubeCache(String query_cache_id,QueryResult qr,AbstractDataSet dataset){
		this.query_cache_id=query_cache_id;
		this.dataset=dataset;
		this.qr=qr;
		//具有编辑报表权限的人验证cube基础数据和维度是否匹配
		dim_check=BaseDataControl.getRole().hasFunc(FuncType.EDITREP);
	}
	/**
	 * 判读是否存在有效的缓存对象
	 * @return
	 * @throws ReportException 
	 */
	protected boolean existCache() throws ReportException{
		FileObjectOutputStream foi=null;
		try {
			//缓存键值为空
			if(StringHelper.isEmpty(query_cache_id))
				return false;
			//缓存文件不存在
			if(!new File(ReportConstants.REP_TEMP_TXT+ this.query_cache_id+ReportConstants.CACHE_BASE).exists())
				return false;
			//缓存文件不能写入，说明缓存文件正在生成不能使用
			foi=new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+ this.query_cache_id+ReportConstants.CACHE_BASE,true);
			return true;
		} catch (Exception e) {
			return false;
		}finally{
			if(foi!=null){
				try {
					foi.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * 判读缓存文件是否存在，如果不存在则创建缓存
	 */
	public String extractCache() throws ReportException {
		if(!this.existCache()){
			this.query_cache_id=this.qr.getQueryId();
			this.createCache();
		}
		return this.query_cache_id;
	}
	/**
	 * 提取cube变换后的数据体
	 */
	public DataReader getData() throws ReportException {
		CacheTranslateCube cachetrans=createCacheTranslateCube();
		DataReader dr=cachetrans.translate(this.query_cache_id, this.dataset);
		if(cachetrans.isTranslateSucess()){
			return dr;
		}else{
			return new DBAcquisition(this.dataset.getAssembleDataSet(),this.qr.getDatabase());
		}
	}
	
	protected CacheTranslateCube createCacheTranslateCube(){
		return new CacheAcquisition();
	}
		
	protected CacheOutput createCacheOutput() throws IOException, ReportException{
		return new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+ this.query_cache_id+ReportConstants.CACHE_BASE);
	}
	/**
	 * 创建基础查询缓存
	 * @param cache_id
	 * @param rep_id
	 * @param sql
	 * @param database
	 * @throws ReportException
	 */
	protected void createCache() throws ReportException{
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql=this.dataset.getDataSet();
		CacheOutput foo = null;
		try {
			foo = createCacheOutput();
			conn = ConnContainer.getConn(this.qr.getDatabase());
			stmt = conn.createStatement();
			stmt.setFetchSize(1000);
			
			LoggerHelper.debug(this.getClass(),sql);
			rs = stmt.executeQuery(sql);
			
			List<RepCube> cubeconfiglist= CubeManage.getRepCubes(qr.getRepId());
			int i=0;
			while (rs.next()) {
				i++;
				//行记录
				Map<String,Object> rowmap=new HashMap<String,Object>();
				for(RepCube vo:cubeconfiglist){
					Object value=null;
					if(vo.isIsmea())
						value=rs.getObject(vo.getColumn_code());
					else
						value=rs.getString(vo.getColumn_code());
					//基础数据为空验证
					if(value==null) throw new ReportException("base_data is null:第"+i+"行"+vo.getColumn_code()+"字段 值为空",sql);
					//具有报表编辑权限的数据完整性验证
					DataRole datarole=BaseDataControl.getRole();
					if(datarole!=null||datarole.hasFunc(FuncType.EDITREP)){
						if(!vo.isIsmea()){
							Dimension dim= DimensionManage.getDimension(vo.getColumn_define());
							if(this.dim_check){
								//具有编辑报表权限的人验证cube基础数据和维度是否匹配
								if(!DimensionLevelValueManage.getDimLevelValueMap(dim.getLevel(dim.getLevelNum())).containsKey(value)){
									throw new ReportException("base_data has id is not in dim:第"
											+i+"行"+vo.getColumn_code()+"字段值("+value+")非维度("+vo.getColumn_define()+")取值范围",sql);
								}
							}
						}
					}
					
					rowmap.put(vo.getColumn_code(), value);
				}
				foo.writeObject(rowmap);
			}
		} catch (ReportException e) {
			throw e;
		} catch (SQLException e) {
			throw new ReportException(e,sql);
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (Exception e) {
			throw new ReportException(e);
		} finally {
			try {
				if (foo != null){
					foo.close();
					foo=null;
				}
			} catch (Exception e1) {
			}
			try {
				if (rs != null){
					rs.close();
					rs=null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null){
					stmt.close();
					stmt=null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null){
					conn.close();
					conn=null;
				}
			} catch (Exception e) {
			}
		}
		
		
	}
	public String getQuery_cache_id() {
		return query_cache_id;
	}
	public AbstractDataSet getDataset() {
		return dataset;
	}
	public QueryResult getQr() {
		return qr;
	}

}
