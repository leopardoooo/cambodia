package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepTask;
import com.ycsoft.report.bean.RepTaskHis;
@Component
public class RepTaskHisDao extends BaseEntityDao<RepTaskHis> {
	public RepTaskHisDao(){}
	
	public void updateKeylist(Integer taskId, String keylist) throws ReportException {
		try {
			String sql = "update rep_task_his set keylist=? where task_id=?";
			this.executeUpdate(sql, keylist, taskId);
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}	
}
