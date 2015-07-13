package com.ycsoft.business.dao.system;

/**
 * SLogDao.java	2011/06/15
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SLog;
import com.ycsoft.commons.constants.FuncCode;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * SLogDao -> S_LOG table's operator
 */
@Component
public class SLogDao extends BaseEntityDao<SLog> {
	
	/**
	 * default empty constructor
	 */
	public SLogDao() {}

	public Pager<SLog> queryLogs(String query, String countyId, Integer start,
			Integer limit) throws JDBCException {
		String sql = "select s.* from s_log s,s_optr p where s.optr_id=p.optr_id and func_code <> ? ";
		
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = StringHelper.append(sql," and s.county_id = '"+countyId+"'");
		}
		if(StringHelper.isNotEmpty(query)){
			sql = StringHelper.append(sql," and p.optr_name like '%",query,"%'");
		}
		sql = StringHelper.append(sql," order by s.done_date desc");
		
		return createQuery(SLog.class, sql, FuncCode.STB_FILLED.toString()).setStart(start).setLimit(limit).page();
	}
	
	public List<SLog> queryCurrDateLog(String funcCode, String cardId) throws JDBCException {
		String sql = "select * from s_log t" +
				" where  t.func_code=? and t.rec_name=?" +
				" order by t.done_date desc";
		return this.createQuery(sql, funcCode, cardId).list();
	}

}
