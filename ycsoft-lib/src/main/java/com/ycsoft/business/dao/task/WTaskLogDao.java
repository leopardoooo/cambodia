package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskLogDao extends BaseEntityDao<WTaskLog> {

	private static final long serialVersionUID = 8483966061975110281L;
	
	public List<WTaskLog> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select * from w_task_log where task_id=? order by log_time desc";
		return this.createQuery(sql, taskId).list();
	}
	
	public List<WTaskLog> querySynLog() throws JDBCException {
		String sql = "select * from w_task_log t where "
				+" syn_status =? "+
				" and  (t.delay_time is  null or t.delay_time<=0 or (t.delay_time>0 and t.log_time <sysdate-t.delay_time/(24*60))) "
				+" order by t.log_sn";
		return this.createQuery(sql, StatusConstants.NOT_EXEC).list();
	}
}
