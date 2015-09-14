package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskLogDao extends BaseEntityDao<WTaskLog> {

	private static final long serialVersionUID = 8483966061975110281L;
	
	public List<WTaskLog> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select * from w_task_log where task_id=?";
		return this.createQuery(sql, taskId).list();
	}
}
