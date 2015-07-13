package com.ycsoft.business.dto.config;



/**
 * 
 * @author allex
 */
public class TaskQueryConditionDto {

	private String cStart;
	private String cEnd ;
	private String custNo;
	private String status;
	private String custName;
	private String tel;
	private Integer start;
	private Integer limit;
	private String installDept;
	private String taskType;
	private String newAddr;
	

	/**
	 * @return the cStart
	 */
	public String getCStart() {
		return cStart;
	}
	/**
	 * @param start the cStart to set
	 */
	public void setCStart(String start) {
		cStart = start;
	}
	/**
	 * @return the cEnd
	 */
	public String getCEnd() {
		return cEnd;
	}
	/**
	 * @param end the cEnd to set
	 */
	public void setCEnd(String end) {
		cEnd = end;
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
	public String getNewAddr() {
		return newAddr;
	}
	public void setNewAddr(String newAddr) {
		this.newAddr = newAddr;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getInstallDept() {
		return installDept;
	}
	public void setInstallDept(String installDept) {
		this.installDept = installDept;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	
}
