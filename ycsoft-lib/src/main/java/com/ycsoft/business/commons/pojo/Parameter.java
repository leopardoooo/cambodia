package com.ycsoft.business.commons.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.config.ExtAttrFormDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.commons.constants.SystemConstants;


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
	private String[] docTypes;	//业务单据类型
	private ExtAttrFormDto extAttrForm; //表扩展属性
	private ExtCDoneCode[] busiExtAttr; //业务扩展属性
	private String remark ;
	
	private String workBillAsignType;
	private String optr_id;

	private Map<String,Object> tempVar = new HashMap<String,Object>() ; //临时变量,用于在拦截器访问Service的变量

	
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
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
	//设置业务的操作对象信息
	public void setOperateObj(String optrInfo){
		ExtCDoneCode ext=new ExtCDoneCode();
		ext.setAttribute_id(SystemConstants.ExtOperateObj);
		ext.setAttribute_value(optrInfo);
		List<ExtCDoneCode> list=new ArrayList<>();
		if(this.busiExtAttr!=null){
			for(ExtCDoneCode e:this.busiExtAttr){
				list.add(e);
			}
		}
		list.add(ext);
		this.busiExtAttr=list.toArray(new ExtCDoneCode[list.size()]);
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
	public String getWorkBillAsignType() {
		return workBillAsignType;
	}
	public void setWorkBillAsignType(String workBillAsignType) {
		this.workBillAsignType = workBillAsignType;
	}
	
	
	
	
}
