/**
 * BTaskScheduleListDao.java	2011/05/27
 */
 
package com.ycsoft.business.dao.core.bill; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BTaskScheduleContent;
import com.ycsoft.beans.core.bill.BTaskScheduleList;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * BTaskScheduleListDao -> B_TASK_SCHEDULE_LIST table's operator
 */
@Component
public class BTaskScheduleListDao extends BaseEntityDao<BTaskScheduleList> {
	
	/**
	 * default empty constructor
	 */
	public BTaskScheduleListDao() {}
	
	public void updateThreeTaskStatus(String status, String taskCode,
			String isBase, String countyId) throws JDBCException {
		String sql = "";
		if(taskCode.equals(SystemConstants.TASK_TD)){
			sql = "update b_task_schedule_list set status=? where task_code=? and county_id=?";
			this.executeUpdate(sql, status, taskCode, countyId);
		}else{
			sql = "update b_task_schedule_list set status=? where task_code=? and isbase=? and county_id=?";
			this.executeUpdate(sql, status, taskCode, isBase, countyId);
		}
	}
	
	public void deleteTaskScheduleList(String taskCode,String countyId) throws JDBCException {
		String sql = "delete from b_task_schedule_list where task_code=? and county_id=?";
		this.executeUpdate(sql, taskCode, countyId);
	}
	
	public void saveTaskScheduleList(BTaskScheduleList task) throws JDBCException {
		String sql = "";
		if(task.getTask_code().equals(SystemConstants.TASK_TD)){
			sql = "delete from b_task_schedule_list where task_code=? and county_id=?";
			this.executeUpdate(sql, task.getTask_code(), task.getCounty_id());
		}else{
			sql = "delete from b_task_schedule_list where task_code=? and isbase=? and county_id=?";
			this.executeUpdate(sql, task.getTask_code(), task.getIsbase(), task.getCounty_id());
		}
		
		sql = "insert into b_task_schedule_list(task_code,status,isbase,max_prod_num," +
				"eff_date,exp_date,county_id,area_id ) values (?,?,?,?,?,?,?,?)";
		this.executeUpdate(sql, task.getTask_code(), task.getStatus(), task.getIsbase(),
				task.getMax_prod_num(), task.getEff_date(), task.getExp_date(), 
				task.getCounty_id(), task.getArea_id());
	}
	
	public void deleteTaskList(String taskCode,String countyId) throws JDBCException {
	}

}
