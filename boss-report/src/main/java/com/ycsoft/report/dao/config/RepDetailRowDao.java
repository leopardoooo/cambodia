package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDetailRow;

@Component
public class RepDetailRowDao extends BaseEntityDao<RepDetailRow> {
	
	public List<RepDetailRow> queryRowsByRepid(String rep_id) throws ReportException{
		
		String sql="select * from rep_detail_row where rep_id=?";
		try {
			return this.createQuery(sql, rep_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
		
	}
	
	public void delete(String rep_id) throws ReportException{
		String sql="delete from rep_detail_row where rep_id=?";
		try {
			this.executeUpdate(sql, rep_id);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
}
