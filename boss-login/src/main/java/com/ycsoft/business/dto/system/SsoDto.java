/**
 *
 */
package com.ycsoft.business.dto.system;

import java.util.Date;


/**
 * @author liujiaqi
 *
 */
public class SsoDto  {
	/**
	 *
	 */
	private static final long serialVersionUID = 9041148892466996009L;
	private String tokenId;
	
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
	
	private String area_id;
	private String county_id;
	private String old_county_id;

	private String area_name;
	private String county_name;
	
	private String dept_name;
	private String sub_system_id ;
	private String sub_system_name ;
	private String sub_system_url ;
	private String sub_system_host ;
	private String iconcls ;

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId
	 *            the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @return the login_name
	 */
	public String getLogin_name() {
		return login_name;
	}

	/**
	 * @param login_name the login_name to set
	 */
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the optr_id
	 */
	public String getOptr_id() {
		return optr_id;
	}

	/**
	 * @param optr_id the optr_id to set
	 */
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	/**
	 * @return the dept_id
	 */
	public String getDept_id() {
		return dept_id;
	}

	/**
	 * @param dept_id the dept_id to set
	 */
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}

	/**
	 * @param optr_name the optr_name to set
	 */
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
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
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the area_id
	 */
	public String getArea_id() {
		return area_id;
	}

	/**
	 * @param area_id the area_id to set
	 */
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @param county_id the county_id to set
	 */
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	/**
	 * @return the area_name
	 */
	public String getArea_name() {
		return area_name;
	}

	/**
	 * @param area_name the area_name to set
	 */
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	/**
	 * @return the county_name
	 */
	public String getCounty_name() {
		return county_name;
	}

	/**
	 * @param county_name the county_name to set
	 */
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

	/**
	 * @return the sub_system_id
	 */
	public String getSub_system_id() {
		return sub_system_id;
	}

	/**
	 * @param sub_system_id the sub_system_id to set
	 */
	public void setSub_system_id(String sub_system_id) {
		this.sub_system_id = sub_system_id;
	}

	/**
	 * @return the sub_system_name
	 */
	public String getSub_system_name() {
		return sub_system_name;
	}

	/**
	 * @param sub_system_name the sub_system_name to set
	 */
	public void setSub_system_name(String sub_system_name) {
		this.sub_system_name = sub_system_name;
	}

	/**
	 * @return the sub_system_url
	 */
	public String getSub_system_url() {
		return sub_system_url;
	}

	/**
	 * @param sub_system_url the sub_system_url to set
	 */
	public void setSub_system_url(String sub_system_url) {
		this.sub_system_url = sub_system_url;
	}

	/**
	 * @return the sub_system_host
	 */
	public String getSub_system_host() {
		return sub_system_host;
	}

	/**
	 * @param sub_system_host the sub_system_host to set
	 */
	public void setSub_system_host(String sub_system_host) {
		this.sub_system_host = sub_system_host;
	}

	/**
	 * @return the iconcls
	 */
	public String getIconcls() {
		return iconcls;
	}

	/**
	 * @param iconcls the iconcls to set
	 */
	public void setIconcls(String iconcls) {
		this.iconcls = iconcls;
	}

	/**
	 * @return the dept_name
	 */
	public String getDept_name() {
		return dept_name;
	}

	/**
	 * @param dept_name the dept_name to set
	 */
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getOld_county_id() {
		return old_county_id;
	}

	public void setOld_county_id(String old_county_id) {
		this.old_county_id = old_county_id;
	}

}
