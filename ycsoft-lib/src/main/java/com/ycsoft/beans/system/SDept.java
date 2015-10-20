/**
 * SDept.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * SDept -> S_DEPT mapping
 */
@POJO(tn = "S_DEPT", sn = SequenceConstants.SEQ_S_DEPT, pk = "DEPT_ID")
public class SDept extends CountyBase implements Serializable {

	// SDept all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8245605771837002645L;
	private String dept_id;
	private String dept_name;
	private String dept_type;
	private Date create_time;
	private String creator;
	private String status;
	private String dept_order_num;
	private String dept_pid;
	private String remark;
	private String agent_id;//代理商ID
	private String agent_name;

	// name info
	private String dept_type_text;

	/**
	 * default empty constructor
	 */
	public SDept() {
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}
	
	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	// dept_id getter and setter
	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	// dept_name getter and setter
	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	// dept_type getter and setter
	public String getDept_type() {
		return dept_type;
	}

	public void setDept_type(String dept_type) {
		this.dept_type = dept_type;
		dept_type_text = MemoryDict.getDictName(DictKey.DEPT_TYPE, dept_type);
	}




	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	// creator getter and setter
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDept_order_num() {
		return dept_order_num;
	}

	public void setDept_order_num(String dept_order_num) {
		this.dept_order_num = dept_order_num;
	}

	public String getDept_type_text() {
		return dept_type_text;
	}

	public String getDept_pid() {
		return dept_pid;
	}

	public void setDept_pid(String dept_pid) {
		this.dept_pid = dept_pid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}