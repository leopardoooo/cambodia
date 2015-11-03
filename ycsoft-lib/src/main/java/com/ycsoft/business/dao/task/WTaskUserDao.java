package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.dto.config.TaskUserDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskUserDao extends BaseEntityDao<WTaskUser> {

	private static final long serialVersionUID = 8241332533236339953L;
	
	public List<WTaskUser> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select t.*,cu.str7 occ_no,cu.str8 pos_no,cu.login_name,cu.password,cu.status,cu.status_date from w_task_user t,c_user cu "
				+" where   cu.user_id(+)=t.user_id and t.task_id=? ";
		return this.createQuery(sql, taskId).list();
	}
	
	public int queryUnFillUserCount(String taskId) throws JDBCException {
		String sql = "select count(1) from w_task_user where task_id=? and device_id is null"; 
		return this.count(sql, taskId);
	}

	public List<TaskUserDto> queryUserDetailByTaskId(String taskId) throws JDBCException {
		String sql = "select t.*  from w_task_user t where t.task_id=? ";
		return this.createQuery(TaskUserDto.class,sql, taskId).list();
	}
	
	public WTaskUser queryBean(String taskId,String userId) throws JDBCException {
		String sql = "select t.*  from w_task_user t where t.task_id=? and t.user_id = ? ";
		return this.createQuery(sql, taskId,userId).first();
	}
	
	public void updateTaskUserDevice(String deviceId,String userId,String taskId) throws Exception {
		String sql = "update w_task_user set device_id=? where task_id=? and user_id=?";
		this.executeUpdate(sql,deviceId,taskId,userId);
	}
	public List<TaskUserDto> queryTaskWriteoffTerminal(String task_id) throws Exception {
		String sql = "select t.*  from w_task_user t where t.task_id=? and t.recycle_device = ?";
		return this.createQuery(TaskUserDto.class,sql, task_id,SystemConstants.BOOLEAN_TRUE).list();
	}
	public List<WTaskUser> queryByUserIds(String taskId, String[] userIds) throws JDBCException{
		String sql ="select * from w_task_user where user_id in ("+sqlGenerator.in(userIds)+")"
				+ " and task_id =?";
		return this.createQuery(sql, userIds,taskId).list();
	}
	
	public void updateRecycle(String taskId, String[] userIds, String recycle_result)throws JDBCException{
		String sql ="update w_task_user set recycle_result= ? where task_id =? "
				+ " and user_id in ("+sqlGenerator.in(userIds)+")";
		this.executeUpdate(sql,recycle_result, taskId);
	}


}
