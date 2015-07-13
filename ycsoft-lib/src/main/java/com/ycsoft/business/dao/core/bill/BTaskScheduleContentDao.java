/**
 * BTaskScheduleContentDao.java	2011/05/27
 */
 
package com.ycsoft.business.dao.core.bill; 

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.bill.BTaskScheduleContent;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * BTaskScheduleContentDao -> B_TASK_SCHEDULE_CONTENT table's operator
 */
@Component
public class BTaskScheduleContentDao extends BaseEntityDao<BTaskScheduleContent> {
	
	/**
	 * default empty constructor
	 */
	public BTaskScheduleContentDao() {}
	
	public void updateStopTaskStatus(String status, String taskCode,
			String servType, String countyId) throws JDBCException {
		String sql = "update b_task_schedule_content set status=? where task_code=? and serv_type=?" +
				" and county_id=?";
		this.executeUpdate(sql, status, taskCode, servType, countyId);
//		sql = "update b_task_schedule set status=? where task_code=? and county_id=?";
//		this.executeUpdate(sql, status, taskCode, countyId);
	}
	
	public void saveTaskScheduleContent(BTaskScheduleContent task) throws JDBCException {
		String sql = "insert into b_task_schedule_content(task_code,serv_type,isbase,bcnt,notbase,xcnt," +
				"base_eff_date,base_exp_date,notbase_eff_date,notbase_exp_date,is_real_time,status," +
				"county_id,area_id ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.executeUpdate(sql, task.getTask_code(), task.getServ_type(), task.getIsbase(), task.getBcnt(), 
				task.getNotbase(),task.getXcnt(), task.getBase_eff_date(), task.getBase_exp_date(), 
				task.getNotbase_eff_date(), task.getNotbase_exp_date(), task.getIs_real_time(), 
				task.getStatus(), task.getCounty_id(), task.getArea_id());
	}
	
	public void deleteTaskContent(String taskCode,String countyId,String servType) throws JDBCException {
		String sql = "delete from b_task_schedule_content where task_code=? and serv_type=? and county_id=?";
		this.executeUpdate(sql, taskCode, servType, countyId);
	}

}
