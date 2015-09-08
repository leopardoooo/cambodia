package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskUserDao extends BaseEntityDao<WTaskUser> {

	private static final long serialVersionUID = 8241332533236339953L;
	
	public List<WTaskUser> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select * from w_task_user where task_id=?";
		return this.createQuery(sql, taskId).list();
	}

}
