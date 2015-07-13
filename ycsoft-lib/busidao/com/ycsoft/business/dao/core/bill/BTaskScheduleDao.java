/**
 * BTaskScheduleDao.java	2011/05/27
 */
 
package com.ycsoft.business.dao.core.bill; 

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BTaskSchedule;
import com.ycsoft.beans.core.bill.BTaskScheduleContentDto;
import com.ycsoft.beans.core.bill.BTaskScheduleListDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * BTaskScheduleDao -> B_TASK_SCHEDULE table's operator
 */
@Component
public class BTaskScheduleDao extends BaseEntityDao<BTaskSchedule> {
	
	/**
	 * default empty constructor
	 */
	public BTaskScheduleDao() {}
	
	public void deleteTaskSchedule(String taskCode,String countyId) throws Exception {
		String sql = "delete from b_task_schedule where task_code=? and county_id=?";
		this.executeUpdate(sql, taskCode, countyId);
	}
	
	public void saveTaskSchedule(BTaskSchedule task) throws JDBCException {
		String sql = "delete from b_task_schedule where task_code=? and county_id=?";
		this.executeUpdate(sql,task.getTask_code(),task.getCounty_id());
		
		sql = "insert into b_task_schedule(task_code,schedule_time,status,county_id," +
				"area_id,create_time,optr_id,hst_day) values (?,?,?,?,?,?,?,?)";
		this.executeUpdate(sql, task.getTask_code(), task.getSchedule_time(),
				task.getStatus(), task.getCounty_id(), task.getArea_id(),
				new Date(), task.getOptr_id(), task.getHst_day());
	}
	
	public boolean checkTask(String taskCode,String countyId) throws JDBCException {
		String sql = "select count(1) from b_task_schedule where task_code=? and county_id=?";
		return this.count(sql, taskCode,countyId)>0;
	}
	
	public List<BTaskScheduleContentDto> queryTjTaskSchedule(String countyId) throws JDBCException {
		String countyCond = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			countyCond = " and ts.county_id='"+countyId+"' ";
		}
		String sql = "select * from b_task_schedule ts,b_task_schedule_content tc"+
				" where ts.task_code=tc.task_code and tc.county_id=ts.county_id" +
				" and ts.task_code=?"+countyCond;
		return this.createQuery(BTaskScheduleContentDto.class, sql, SystemConstants.TASK_TJ).list();
	}
	
	public List<BTaskScheduleListDto> queryTaskSchedule(String countyId) throws JDBCException {
		String countyCond = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			countyCond = " and ts.county_id='"+countyId+"' and l.county_id='"+countyId+"'";
		}
		String sql = "select ts.task_code,ts.schedule_time,ts.county_id,ts.area_id,ts.hst_day,l.status,l.isbase," +
				" l.max_prod_num,l.eff_date,l.exp_date"+
        		" from b_task_schedule ts,b_task_schedule_list l"+
        		" where ts.task_code=l.task_code and l.county_id=ts.county_id" + countyCond;
		return this.createQuery(BTaskScheduleListDto.class, sql).list();
	}

}
