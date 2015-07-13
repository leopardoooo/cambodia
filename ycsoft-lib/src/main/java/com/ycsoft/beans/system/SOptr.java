/**
 * SOptr.java	2010/03/07
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
 * SOptr -> S_OPTR mapping
 */
@POJO(tn = "S_OPTR", sn = SequenceConstants.SEQ_S_DEPT, pk = "OPTR_ID")
public class SOptr extends CountyBase implements Serializable {

	// SOptr all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3875275837430080295L;
	private String optr_id;
	private String dept_id;
	private String optr_name;
	private String login_name;
	private String password;
	private String status;
	private String login_sys_id;
	private Date create_time;
	private String creator;
	private String remark;
	private String tel;
	private String position;
	private String is_busi_optr;
	private String old_county_id;
	private String copy_optr_id;


	// 显示值
	private String status_text;
	private String creator_text;
	private String dept_name;
	private String sub_system_text;
	private String is_busi_optr_text;
	private String copy_optr_name;
	

	/**
	 * default empty constructor
	 */
	public SOptr() {
	}

	public SOptr(String login_name, String password) {
		this.login_name = login_name;
		this.password = password;
	}

	public String getIs_busi_optr() {
		return is_busi_optr;
	}

	public void setIs_busi_optr(String isBusiOptr) {
		is_busi_optr = isBusiOptr;
		is_busi_optr_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_busi_optr);
	}
	
	public String getIs_busi_optr_text() {
		return is_busi_optr_text;
	}

	public void setIs_busi_optr_text(String isBusiOptrText) {
		is_busi_optr_text = isBusiOptrText;
	}
	
	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	// dept_id getter and setter
	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
		dept_name = MemoryDict.getDictName(DictKey.DEPT, dept_id);
	}

	// optr_name getter and setter
	public String getOptr_name() {
		return optr_name;
	}

	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}

	// login_name getter and setter
	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	// password getter and setter
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// create_time getter and setter
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

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public String getCreator_text() {
		return creator_text;
	}

	public void setCreator_text(String creator_text) {
		this.creator_text = creator_text;
	}

	/**
	 * @return the dept_name
	 */
	public String getDept_name() {
		return dept_name;
	}

	/**
	 * @return the login_sys_id
	 */
	public String getLogin_sys_id() {
		return login_sys_id;
	}

	/**
	 * @param login_sys_id the login_sys_id to set
	 */
	public void setLogin_sys_id(String login_sys_id) {
		this.login_sys_id = login_sys_id;
		sub_system_text = MemoryDict.getDictName(DictKey.SUB_SYSTEM, login_sys_id);
	}

	/**
	 * @param dept_name the dept_name to set
	 */
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSub_system_text() {
		return sub_system_text;
	}

	public void setSub_system_text(String subSystemText) {
		sub_system_text = subSystemText;
	}

	public String getOld_county_id() {
		return old_county_id;
	}

	public void setOld_county_id(String old_county_id) {
		this.old_county_id = old_county_id;
	}

	public String getCopy_optr_id() {
		return copy_optr_id;
	}

	public void setCopy_optr_id(String copy_optr_id) {
		this.copy_optr_id = copy_optr_id;
		copy_optr_name = MemoryDict.getDictName(DictKey.OPTR.toString(), copy_optr_id);
	}

	public String getCopy_optr_name() {
		return copy_optr_name;
	}


}