package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDetailData;

@Component
public class RepDetailDataDao extends BaseEntityDao<RepDetailData> {
	
	/**
	 * 根据页面坐标值，查询手工明细内容
	 * @param rep_id
	 * @param queryssql
	 * @return
	 * @throws ReportException
	 */
	public List<RepDetailData> queryDataByHeadCells(String rep_id,String queryssql) throws ReportException{		
		String sql="select * from rep_detail_data where rep_id=?  ?";
		try {
			return this.createQuery(sql, rep_id,queryssql).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	/**
	 * 更新一条手工记录
	 * @param context_id
	 * @param context_json
	 * @throws ReportException
	 */
	public void updateOneData(String context_id,String context_json) throws ReportException{
		String sql="update rep_detail_date set context_json=? where context_id=?";
		try {
			this.executeUpdate(sql, context_id,context_json);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	/**
	 * 插入一条手工记录
	 * @param rep_id
	 * @param context_json
	 * @param dimvalue
	 * @return
	 * @throws ReportException
	 */
	public String insertOneData(String rep_id,String optr_id,String context_json, RepDetailData dimvalue) throws ReportException{
		try {
			dimvalue.setRep_id(rep_id);
			dimvalue.setOptr_id(optr_id);
			dimvalue.setContext_json(context_json);
			dimvalue.setContext_id(this.findSequence("seq_rep_context").toString());
			this.save(dimvalue);
			return dimvalue.getContext_id();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
		
	}
	
}
