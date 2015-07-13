package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDetailDim;

@Component
public class RepDetailDimDao extends BaseEntityDao<RepDetailDim> {
	

	public void delete(String rep_id) throws ReportException{
		String sql="delete from rep_detail_dim where rep_id=?";
		try {
			this.executeUpdate(sql, rep_id);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public List<RepDetailDim> queryByRepId(String rep_id) throws ReportException{
		try {
			return this.createQuery("select * from rep_detail_dim where rep_id=?", rep_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
}
