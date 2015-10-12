/**
 * TBulletin.java	2010/11/26
 */
 
package com.ycsoft.beans.system; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * SBulletin -> S_BULLETIN mapping 
 */
@POJO(
	tn="S_BULLETIN_WORKTASK",
	sn="",
	pk="DEPT_ID")
public class SBulletinWorktask implements Serializable {
	
	// TBulletin all properties 

	private String dept_id;
	private String bulletin_id ;	
	private String bulletin_worktask_type;
	private String remark;
	
	/**
	 * default empty constructor
	 */
	public SBulletinWorktask() {}

	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getBulletin_id() {
		return bulletin_id;
	}

	public void setBulletin_id(String bulletin_id) {
		this.bulletin_id = bulletin_id;
	}

	public String getBulletin_worktask_type() {
		return bulletin_worktask_type;
	}

	public void setBulletin_worktask_type(String bulletin_worktask_type) {
		this.bulletin_worktask_type = bulletin_worktask_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}