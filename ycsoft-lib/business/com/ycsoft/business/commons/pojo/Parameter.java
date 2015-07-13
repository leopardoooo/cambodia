package com.ycsoft.business.commons.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.config.ExtAttrFormDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;


/**
 *  前台参数的统一封装基类
 *
 * @author hh
 * @date Mar 10, 2010 9:58:49 AM
 */
public class Parameter implements IParameter {

	/**
	 *
	 */
	private static final long serialVersionUID = -3717950825166657469L;
	//必须的参数
	private String busiCode ;
	private Integer doneCode ;

	private SOptr optr = new SOptr();

	//前台参数。可选
	private CFeePayDto pay ;  	  //缴费信息
	private List<FeeBusiFormDto> fees; //收费信息
	private String[] taskIds ;	//施工单编号
	private String task_books_time; //预约时间
	private String task_cust_name; //工单联系人
	private String task_mobile; // 工单联系电话
	private String task_remark;	//工单备注
	private String task_bug_cause;//故障原因
	private String[] docTypes;	//业务单据类型
	private ExtAttrFormDto extAttrForm; //表扩展属性
	private ExtCDoneCode[] busiExtAttr; //业务扩展属性
	private String remark ;

	private Map<String,Object> tempVar = new HashMap<String,Object>() ; //临时变量,用于在拦截器访问Service的变量

	
	
	public String getTask_bug_cause() {
		return task_bug_cause;
	}
	public void setTask_bug_cause(String task_bug_cause) {
		this.task_bug_cause = task_bug_cause;
	}
	public CFeePayDto getPay() {
		return pay;
	}
	public void setPay(CFeePayDto pay) {
		this.pay = pay;
	}
	public List<FeeBusiFormDto> getFees() {
		return fees;
	}
	public void setFees(List<FeeBusiFormDto> fees) {
		this.fees = fees;
	}
	public String[] getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String[] taskIds) {
		this.taskIds = taskIds;
	}
	public String[] getDocTypes() {
		return docTypes;
	}
	public void setDocTypes(String[] docTypes) {
		this.docTypes = docTypes;
	}
	public String getBusiCode() {
		return busiCode;
	}
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	public Integer getDoneCode() {
		return doneCode;
	}
	public ExtAttrFormDto getExtAttrForm() {
		return extAttrForm;
	}
	public void setExtAttrForm(ExtAttrFormDto extAttrForm) {
		this.extAttrForm = extAttrForm;
	}
	public Map<String, Object> getTempVar() {
		return tempVar;
	}
	public void setTempVar(Map<String, Object> tempVar) {
		this.tempVar = tempVar;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public ExtCDoneCode[] getBusiExtAttr() {
		return busiExtAttr;
	}
	public void setBusiExtAttr(ExtCDoneCode[] busiExtAttr) {
		this.busiExtAttr = busiExtAttr;
	}
	public void setDoneCode(Integer doneCode) {
		this.doneCode = doneCode;
	}
	/**
	 * @return the optr
	 */
	public SOptr getOptr() {
		return optr;
	}
	/**
	 * @param optr the optr to set
	 */
	public void setOptr(SOptr optr) {
		this.optr = optr;
	}
	public String getTask_books_time() {
		return task_books_time;
	}
	public void setTask_books_time(String task_books_time) {
		this.task_books_time = task_books_time;
	}
	public String getTask_cust_name() {
		return task_cust_name;
	}
	public void setTask_cust_name(String task_cust_name) {
		this.task_cust_name = task_cust_name;
	}
	public String getTask_mobile() {
		return task_mobile;
	}
	public void setTask_mobile(String task_mobile) {
		this.task_mobile = task_mobile;
	}
	public String getTask_remark() {
		return task_remark;
	}
	public void setTask_remark(String task_remark) {
		this.task_remark = task_remark;
	}
	
}
