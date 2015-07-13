/**
 * RepQueryLogDao.java	2010/06/23
 */

package com.ycsoft.report.dao.config;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.key.QueryKey;

/**
 * RepQueryLogDao -> REP_QUERY_LOG table's operator
 */
@Component
public class RepQueryLogDao extends BaseEntityDao<RepQueryLog> {

	private QueryKey queryKey;
	
	public QueryKey getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(QueryKey queryKey) {
		this.queryKey = queryKey;
	}

	/**
	 * default empty constructor
	 */
	public RepQueryLogDao() {
	}

	public RepQueryLog getRepQuerLog(String query_id) throws JDBCException{
		String sql = "select * from rep_query_log where query_id=?";
		return this.findEntity(sql, query_id);
	
	}
	
	public void saveRepQuerLog(RepQueryLog o) throws JDBCException{
		String sql= StringHelper.append(
				 "insert into rep_query_log" ,
				         "     (query_id      ,rep_id       ,isvalid       ,keylist       ,querytime       ,querynum       ,optr_id,create_date,client_ip) ",
				 "  values     (?             ,?            ,?             ,?             ,?               ,?              ,?,?,?)");
				          
		this.executeUpdate(sql,o.getQuery_id(),o.getRep_id(),o.getIsvalid(),o.getKeylist(),o.getQuerytime(),o.getQuerynum(),
				o.getOptr_id(),o.getCreate_date(),o.getClient_ip());
		//this.save(o);
	}
	/**
	 * 获得有效的查询历史
	 * @param initQueryDto
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public RepQueryLog getRepQueryLog(InitQueryDto initQueryDto,String optr_id,String ip)
			throws JDBCException, ReportException {
		
		String keylist=queryKey.toString(initQueryDto.getKeyvaluelist());
		RepQueryLog repQueryLog=null;
		if(ReportConstants.DATABASETYPE_HISTROY.equals(SystemConfig.getDatabaseMap().get(initQueryDto.getDatabase()).getType())){
			if(StringHelper.isEmpty(keylist)){
				String sql = "select * from rep_query_log where rep_id=? and keylist  is null and isvalid='T' and create_date>trunc(sysdate) ";
				repQueryLog = this.findEntity(sql,initQueryDto.getRep_id());	
			}else{
				String sql = "select * from rep_query_log where rep_id=? and keylist =? and isvalid='T' and create_date>trunc(sysdate) ";
			    repQueryLog = this.findEntity(sql,initQueryDto.getRep_id(),keylist);
			}
		}
		if (repQueryLog == null) {
			repQueryLog = new RepQueryLog();
			repQueryLog.setKeylist(keylist);
			repQueryLog.setRep_id(initQueryDto.getRep_id());
			repQueryLog.setQuery_id(this.findSequence().toString());
		} else {
			initQueryDto.setCache_query_id(repQueryLog.getQuery_id());
		}
		//非实时报表缓存检测标志=T
		if (ReportConstants.DATABASETYPE_REALTIME.equals(SystemConfig.getDatabaseMap().get(initQueryDto.getDatabase()).getType())){
			repQueryLog.setIsvalid(ReportConstants.VALID_F);
		}else{
			repQueryLog.setIsvalid(ReportConstants.VALID_T);
		}
		
		// 查询ID
		initQueryDto.setQuery_id(repQueryLog.getQuery_id());
		repQueryLog.setOptr_id(optr_id);
		repQueryLog.setClient_ip(ip);
		repQueryLog.setCreate_date(new Date());
		return repQueryLog;
	}

	/**
	 * 设置查询缓存无效化
	 * @param rep_id
	 * @throws ReportException
	 */
	public void clearCacheByRepUpdate(String rep_id) throws ReportException{
		String sql="update rep_query_log set isvalid='F' where  isvalid='T' and rep_id=?";
		try {
			this.executeUpdate(sql, rep_id);
		} catch (JDBCException e) {
			throw new ReportException(e.getMessage(),e,sql);
		}
	}
	
	/**
	 * 设置查询缓存无效化（主报表和明细报表）
	 * @param rep_id
	 * @throws ReportException
	 */
	public void clearCacheRepAndDetail(String rep_id) throws ReportException{
		String sql="update rep_query_log set isvalid='F' where  isvalid='T' and (rep_id=? or rep_id =(select detail_id from rep_define where rep_id=? ))";
		try {
			this.executeUpdate(sql, rep_id,rep_id);
		} catch (JDBCException e) {
			throw new ReportException(e.getMessage(),e,sql);
		}
	}

	public void clearCache() throws ReportException{
		String sql="update rep_query_log set isvalid='F' where  isvalid='T' ";
		try {
			this.executeUpdate(sql);
		} catch (JDBCException e) {
			throw new ReportException(e.getMessage(),e,sql);
		}
	}
}
