package com.ycsoft.business.dto.core.cust;



public class QueryTaskConditionDto {

	private String startTime;
	private String endTime ;
	private String custNo;
	private String status;
	private String linkman;
	private String mobile;
	private Integer start;
	private Integer limit;
	private String installTeam;
	private String taskDetailType;
	private String newAddr;
	
	public String getTaskDetailType() {
		return taskDetailType;
	}
	public void setTaskDetailType(String taskDetailType) {
		this.taskDetailType = taskDetailType;
	}
	public String getInstallTeam() {
		return installTeam;
	}
	public void setInstallTeam(String installTeam) {
		this.installTeam = installTeam;
	}


	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the custNo
	 */
	public String getCustNo() {
		return custNo;
	}
	/**
	 * @param custNo the custNo to set
	 */
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		this.start = start;
	}
	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNewAddr() {
		return newAddr;
	}
	public void setNewAddr(String newAddr) {
		this.newAddr = newAddr;
	}
	
	
}
