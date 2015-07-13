package com.ycsoft.beans.core.bill;

import com.ycsoft.commons.constants.SystemConstants;


public class BTaskScheduleContentDto extends BTaskScheduleContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729838128701589553L;
	private String schedule_time ;

	
	public BTaskScheduleContentDto() {}
	
	public BTaskScheduleContentDto(String serv_type,String county_id) {
		setServ_type(serv_type);
		setCounty_id(county_id);
		setTask_code(SystemConstants.TASK_TJ);
	}


	public String getSchedule_time() {
		return schedule_time;
	}


	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}
	
}