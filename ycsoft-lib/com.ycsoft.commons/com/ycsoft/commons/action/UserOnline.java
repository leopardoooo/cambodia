/*
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ycsoft.commons.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.system.SOptr;

public class UserOnline extends SOptr implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date loginTime;
	private String jsonOptr;
	private String lastServerIp;
	private String lastContextPath;
	private Map<String ,String> servers = new HashMap<String ,String>();
	private List<String> resourceList = new ArrayList<String>();
	private String userIp;
	private String bwver;
	private Date lastResTime;
	private String resourceSrc;
	
	
	public String getResourceSrc() {
		return resourceSrc;
	}
	public void setResourceSrc(String resourceSrc) {
		this.resourceSrc = resourceSrc;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getJsonOptr() {
		return jsonOptr;
	}
	public void setJsonOptr(String jsonOptr) {
		this.jsonOptr = jsonOptr;
	}
	public String getLastServerIp() {
		return lastServerIp;
	}
	public void setLastServerIp(String lastServerIp) {
		this.lastServerIp = lastServerIp;
	}
	public String getLastContextPath() {
		return lastContextPath;
	}
	public void setLastContextPath(String lastContextPath) {
		this.lastContextPath = lastContextPath;
	}
	public Map<String, String> getServers() {
		return servers;
	}
	public void setServers(Map<String, String> servers) {
		this.servers = servers;
	}
	public List<String> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getBwver() {
		return bwver;
	}
	public void setBwver(String bwver) {
		this.bwver = bwver;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getLastResTime() {
		return lastResTime;
	}
	public void setLastResTime(Date lastResTime) {
		this.lastResTime = lastResTime;
	}
	

}
