/**
 *
 */
package com.ycsoft.business.dto.config;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.task.WTaskServ;
import com.ycsoft.beans.task.WWork;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class TaskQueryWorkDto extends WWork{

	/**
	 *
	 */
	private static final long serialVersionUID = -468240709825328581L;
	private String task_cust_name;
	private String cust_name ;
	private String cust_id;
	private String cust_no;
	private String user_id;
	private String install_addr;
	private String tel;
	private String old_addr;
	private Date installer_time ;	
	private String net_type;
	private String busi_code;
	private String mobile ;	
	private String dept_id;
	private String dept_name;
	private String busi_name;
	private String satisfaction ;
	private String satisfaction_text ;
	private String satisfaction_remak ;
	private String fail_cause;
	private String fail_cause_text;
	private String succeed;
	private String succeed_text;
	private String installer_dept ;	
	private String installer_optr ;
	private String installer_dept_text ;	
	private String installer_optr_text ;
	private String addr_pid;
	
	private String task_type_id;
	
	public String getTask_type_id() {
		return task_type_id;
	}
	public void setTask_type_id(String task_type_id) {
		this.task_type_id = task_type_id;
	}
	private List<Object> remarkList=null;
	private List<WTaskServ> taskServList=null;
	
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getInstall_addr() {
		return install_addr;
	}
	public void setInstall_addr(String install_addr) {
		this.install_addr = install_addr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getOld_addr() {
		return old_addr;
	}
	public void setOld_addr(String old_addr) {
		this.old_addr = old_addr;
	}
	
	public String getNet_type() {
		return net_type;
	}
	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
		busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, busi_code);
	}
	public String getBusi_name() {
		return busi_name;
	}
	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public List<Object> getRemarkList() {
		return remarkList;
	}
	public void setRemarkList(List<Object> remarkList) {
		this.remarkList = remarkList;
	}
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
		dept_name = MemoryDict.getDictName(DictKey.DEPT, dept_id);
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTask_cust_name() {
		return task_cust_name;
	}
	public void setTask_cust_name(String task_cust_name) {
		this.task_cust_name = task_cust_name;
	}
	public String getSucceed() {
		return succeed;
	}
	public void setSucceed(String succeed) {
		this.succeed = succeed;
		succeed_text = MemoryDict.getDictName(DictKey.BOOLEAN,succeed);
	}
	public String getSucceed_text() {
		return succeed_text;
	}
	public void setSucceed_text(String succeed_text) {
		this.succeed_text = succeed_text;
	}
	public String getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
		satisfaction_text = MemoryDict.getDictName(DictKey.SATISFACTION,satisfaction);
	}
	public String getSatisfaction_text() {
		return satisfaction_text;
	}
	public void setSatisfaction_text(String satisfaction_text) {
		this.satisfaction_text = satisfaction_text;
	}
	public String getSatisfaction_remak() {
		return satisfaction_remak;
	}
	public void setSatisfaction_remak(String satisfaction_remak) {
		this.satisfaction_remak = satisfaction_remak;
	}
	public String getFail_cause() {
		return fail_cause;
	}
	public void setFail_cause(String fail_cause) {
		this.fail_cause = fail_cause;
		fail_cause_text = MemoryDict.getDictName(DictKey.TASK_FAILURE_CAUSE,fail_cause);
	}
	public String getFail_cause_text() {
		return fail_cause_text;
	}
	public void setFail_cause_text(String fail_cause_text) {
		this.fail_cause_text = fail_cause_text;
	}
	public Date getInstaller_time() {
		return installer_time;
	}
	public void setInstaller_time(Date installer_time) {
		this.installer_time = installer_time;
	}
	public String getInstaller_dept() {
		return installer_dept;
	}
	public void setInstaller_dept(String installer_dept) {
		this.installer_dept = installer_dept;
		installer_dept_text = MemoryDict.getDictName(DictKey.DEPT, installer_dept);
	}
	public String getInstaller_optr() {
		return this.installer_optr ;
	}
	public void setInstaller_optr(String installer_optr) {
		this.installer_optr = installer_optr;
	}
	public String getInstaller_dept_text() {
		return installer_dept_text;
	}
	public void setInstaller_dept_text(String installer_dept_text) {
		this.installer_dept_text = installer_dept_text;
	}
	public String getInstaller_optr_text() {
		if(null!=this.getInstaller_optr()){
			String[] installers = this.getInstaller_optr().split(",");
			if(null!=installers){
				for(String installer : installers){
					String src = MemoryDict.getDictName(DictKey.OPTR, installer)+",";
					if(null != installer_optr_text){
						installer_optr_text += src;
					}else{
						installer_optr_text = src;
					}
				}
				installer_optr_text  = installer_optr_text.substring(0,installer_optr_text.length()-1);
			}
		}
		return installer_optr_text;
	}
	public void setInstaller_optr_text(String installer_optr_text) {
		this.installer_optr_text = installer_optr_text;
	}
	public List<WTaskServ> getTaskServList() {
		return taskServList;
	}
	public void setTaskServList(List<WTaskServ> taskServList) {
		this.taskServList = taskServList;
	}
	/**
	 * @return the addr_pid
	 */
	public String getAddr_pid() {
		return addr_pid;
	}
	/**
	 * @param addr_pid the addr_pid to set
	 */
	public void setAddr_pid(String addr_pid) {
		this.addr_pid = addr_pid;
	}
	
}
