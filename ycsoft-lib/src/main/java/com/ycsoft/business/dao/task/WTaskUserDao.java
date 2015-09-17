package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.dto.config.TaskUserDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskUserDao extends BaseEntityDao<WTaskUser> {

	private static final long serialVersionUID = 8241332533236339953L;
	
	public List<WTaskUser> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select * from w_task_user where task_id=?";
		return this.createQuery(sql, taskId).list();
	}
	
	public int queryUnFillUserCount(String taskId) throws JDBCException {
		String sql = "select count(1) from w_task_user where task_id=? and device_id is null";
		return this.count(sql, taskId);
	}

	public List<TaskUserDto> queryUserDetailByTaskId(String taskId) throws JDBCException {
		String sql = "select t.*,cu.password password,cu.user_name user_name"
				+ " from w_task_user t,c_user cu  where t.task_id=? and t.user_id = cu.user_id ";
		return this.createQuery(TaskUserDto.class,sql, taskId).list();
	}
	
	public void updateTaskUserDevice(String deviceId,String userId,String taskId) throws Exception {
		String sql = "update w_task_user set device_id=? where task_id=? and user_id=?";
		this.executeUpdate(sql,deviceId,taskId,userId);
	}

}
