package com.ycsoft.report.dao.keycon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepFileKeyValue;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.bean.RepKeyLevel;
import com.ycsoft.report.bean.RepKeySystem;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.sql.AnalyseMemoryKey;


@Component
public class QueryKeyValueDao  {

	/**
	 * 页面组件获取初始化值
	 * @param countyid
	 * @param key
	 * @param fkey_value
	 * @param optr
	 * @param rep_role
	 * @param systemkeyvaluemap
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public List<QueryKeyValue> fineList(String countyid, String key,String fkey_value) throws ReportException{

		RepKeyCon keycon= SystemConfig.getConMap().get(key);
		if(keycon==null) throw new ReportException(key+"is not config or is null");
		if(keycon.getHtmlcode().equals(ReportConstants.htmlcode_datefield)&&StringHelper.isEmpty(keycon.getValuesql())){
			//datefield组件类型的如果取值sql为空 返回空
			return new ArrayList<QueryKeyValue>();
		}else if( StringHelper.isNotEmpty(keycon.getFkey())&&
				StringHelper.isEmpty(fkey_value)) {
			// 非datefild组件类型key存在fkey,并传入的fkey_value is null 返回空；
			return new ArrayList<QueryKeyValue>();
		}
		//当repkeycon父级为空且内存键不为空的情况下，从内存中取值
		if(StringHelper.isNotEmpty(keycon.getMemorykey())&&StringHelper.isEmpty(keycon.getFkey())){
			List<QueryKeyValue> list=AnalyseMemoryKey.getMemoryList(keycon.getMemorykey());
			if(list!=null&&list.size()>0)
				return list;
			else
				LoggerHelper.error(this.getClass(),"RepKeyCon_"+keycon.getKey()+".MemoryKey_"+ keycon.getMemorykey()+" is null or size=0.");
		}
		
		String sql=keycon.getValuesql();		
		if(StringHelper.isNotEmpty(fkey_value))
			sql=sql.replaceAll(keycon.getFkey(), fkey_value==null?"":fkey_value);
		sql=sql.replaceAll("#countyid#", countyid==null?"":countyid);
		return findList(keycon.getDatabase(),sql);
	}
	
	/**
	 * 页面组件获取初始化值
	 * @param countyid
	 * @param key
	 * @param fkey_value
	 * @param optr
	 * @param rep_role
	 * @param systemkeyvaluemap
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public List<QueryKeyValue> fineList(String countyid, String key,
			String fkey_value,Integer rep_role,Map<String,String> systemkeyvaluemap,String datarightsql) throws JDBCException, ReportException{

		RepKeyCon keycon= SystemConfig.getConMap().get(key);
		if(keycon==null) throw new ReportException(key+"is not config or is null");
		
		if(keycon.getHtmlcode().equals(ReportConstants.htmlcode_datefield)&&StringHelper.isEmpty(keycon.getValuesql())){
			//datefield组件类型的如果取值sql为空 返回空
			return new ArrayList<QueryKeyValue>();
		}else if( StringHelper.isNotEmpty(keycon.getFkey())&&
				StringHelper.isEmpty(fkey_value)) {
			// 非datefild组件类型key存在fkey,并传入的fkey_value is null 返回空；
			return new ArrayList<QueryKeyValue>();
		}
		if(rep_role==null) rep_role=4;
		String sql="";
		String database="";
		if(keycon.getLevle()==-1||keycon.getLevle()>rep_role){
			//当repkeycon父级为空且内存键不为空的情况下，从内存中取值
			if(StringHelper.isNotEmpty(keycon.getMemorykey())&&StringHelper.isEmpty(keycon.getFkey())){
				List<QueryKeyValue> list=AnalyseMemoryKey.getMemoryList(keycon.getMemorykey());
				if(list!=null&&list.size()>0)
					return list;
				else
					LoggerHelper.error(this.getClass(),"RepKeyCon_"+keycon.getKey()+".MemoryKey_"+ keycon.getMemorykey()+" is null or size=0.");
			}
			sql=keycon.getValuesql();
			database=keycon.getDatabase();
			//datarighttype系统定义的数据权限
			if(StringHelper.isNotEmpty(datarightsql)){
				sql=sql.replaceAll(ReportConstants.SESSION_REP_ROLE_DATA_KEY, datarightsql);
			}else{
				sql=sql.replaceAll(ReportConstants.SESSION_REP_ROLE_DATA_KEY, " 1=1 ");
			}
		}else{
			RepKeyLevel keylevel=SystemConfig.getLevelMap().get(keycon.getKey());
			//当repkeylevel中内存键和系统键不为空的情况下，从内存中取值
			if(StringHelper.isNotEmpty(keylevel.getMemorykey())&&StringHelper.isNotEmpty(keylevel.getSystemkey())){
				String id=systemkeyvaluemap.get(keylevel.getSystemkey());
				String name=AnalyseMemoryKey.getMemoryIDtoName(keylevel.getMemorykey(), id);
				if(name!=null){
					QueryKeyValue vo=new QueryKeyValue();
					vo.setId(id);
					vo.setName(name);
					List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
					list.add(vo);
					return list;
				}else
					LoggerHelper.error(this.getClass(),"RepKeyLevel_"+keycon.getKey()+".MemoryKey_"+ keylevel.getMemorykey()+".SystemKeyId="+id+" name is null.");
			}
			sql=SystemConfig.getLevelMap().get(keycon.getKey()).getValuesql();
		}
		
		if(keycon.getFkey()!=null && !keycon.getFkey().equals(""))
			sql=sql.replaceAll(keycon.getFkey(), fkey_value==null?"":fkey_value);
		sql=sql.replaceAll("#countyid#", countyid==null?"":countyid);
		for(RepKeySystem system_key:SystemConfig.getSystemList()){
			sql=sql.replaceAll(system_key.getKey(),systemkeyvaluemap.get(system_key.getKey()));
		}
		return findList(database,sql);
	}
	
	public List<QueryKeyValue> findList(String database,String sql) throws ReportException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn=ConnContainer.getConn(database);
			stmt=conn.createStatement();
			stmt.setFetchSize(100);
			rs=stmt.executeQuery(sql);
			LoggerHelper.debug(this.getClass(), "Query_Database:"+database);
			LoggerHelper.debug(this.getClass(), sql);
			List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
			int index=rs.getMetaData().getColumnCount();
			while(rs.next()){
				QueryKeyValue vo=new QueryKeyValue();
				if(index>=1)
					vo.setId(rs.getString(1));
				if(index>=2)
					vo.setName(rs.getString(2));
				if(index>=3)
					vo.setPid(rs.getString(3));
				list.add(vo);
			}
			return list;
		} catch (SQLException e) {
			throw new ReportException("Query_Database:"+database+e.getMessage(),e,sql);
		}finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 保存文件组件提交的数据到数据库
	 * @param file_id
	 * @param list
	 * @param database
	 * @throws ReportException
	 */
	public void saveuploadqueryfile(String file_id,List<RepFileKeyValue> list,String database) throws ReportException{
		Connection conn = null;
		PreparedStatement ps=null;
		if(list==null&&list.size()==0) return;
		try {
			conn=ConnContainer.getConn(database);
			ps= conn.prepareStatement("insert into rep_filekey_value(file_id,code1,code2,code3,code4,code5,code6,code7,code8,code9,code10,code11,code12" +
					",code13,code14,code15,code16,code17,code18,code19,code20)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?" +
					",?,?,?,?,?,?,?,?)");
			for(RepFileKeyValue vo:list){
				ps.setString(1,file_id);
				ps.setString(2,vo.getCode1());
				ps.setString(3,vo.getCode2());
				ps.setString(4,vo.getCode3());
				ps.setString(5,vo.getCode4());
				ps.setString(6,vo.getCode5());
				ps.setString(7,vo.getCode6());
				ps.setString(8,vo.getCode7());
				ps.setString(9,vo.getCode8());
				ps.setString(10,vo.getCode9());
				ps.setString(11,vo.getCode10());
				ps.setString(12,vo.getCode11());
				ps.setString(13,vo.getCode12());
				ps.setString(14,vo.getCode13());
				ps.setString(15,vo.getCode14());
				ps.setString(16,vo.getCode15());
				ps.setString(17,vo.getCode16());
				ps.setString(18,vo.getCode17());
				ps.setString(19,vo.getCode18());
				ps.setString(20,vo.getCode19());
				ps.setString(21,vo.getCode20());
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			throw new ReportException(e);
		}finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		
	}
}
