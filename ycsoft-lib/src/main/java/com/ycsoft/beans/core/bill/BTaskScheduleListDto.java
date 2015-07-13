package com.ycsoft.beans.core.bill;


public class BTaskScheduleListDto extends BTaskScheduleList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729838128701589553L;
	private String schedule_time ;
	private Integer hst_day ;
	private String task_info;
	private String mail_title;

	
	public String getTask_info() {
		return task_info;
	}


	public void setTask_info(String task_info) {
		this.task_info = task_info;
	}


	public String getMail_title() {
		return mail_title;
	}


	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}


	public BTaskScheduleListDto() {}


	public String getSchedule_time() {
		return schedule_time;
	}


	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}


	public Integer getHst_day() {
		return hst_day;
	}


	public void setHst_day(Integer hst_day) {
		this.hst_day = hst_day;
	}
	
}