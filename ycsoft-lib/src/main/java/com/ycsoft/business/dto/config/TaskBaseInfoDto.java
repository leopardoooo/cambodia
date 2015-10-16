/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.commons.helper.StringHelper;

/**
 * @author YC-SOFT
 *
 */
public class TaskBaseInfoDto extends WTaskBaseInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7637497351437559729L;
	/**
	 *
	 */
	private String address;
	private String team_type;
	private String linkman_name;
	private String linkman_tel;
	private String cust_no;
	
	
	
	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public String getLinkman_name() {
		return linkman_name;
	}


	public void setLinkman_name(String linkman_name) {
		this.linkman_name = linkman_name;
	}

	public String getLinkman_tel() {
		return linkman_tel;
	}


	public void setLinkman_tel(String linkman_tel) {
		this.linkman_tel = linkman_tel;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getTeam_type() {
		return team_type;
	}


	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}


	public String getAddress() {
		if(StringHelper.isNotEmpty(getOld_addr())){
			address = "old:"+getOld_addr()+",<br>new:"+getNew_addr();
		}else{
			address = getNew_addr();
		}
		return address;
	}
	
}
