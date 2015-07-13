package com.ycsoft.login;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ycsoft.business.dto.system.SsoDto;

public class OnlineUser {
	private final int resMaxSize=10;
	public static Map<String, OnlineUser> map = new HashMap<String, OnlineUser>();
	private Date loginTime;
	private SsoDto optr;
	private String lastServerIp;
	private String lastContextPath;
	private Map<String ,String> servers = new HashMap<String ,String>();
	private List<String> resourceList = new LinkedList<String>();
	private Date lastResTime;
	private String userIp;
	private String bwver;
	public String getBwver() {
		return bwver;
	}
	public void setBwver(String bwver) {
		this.bwver = bwver;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}
	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	/**
	 * @return the lastServerIp
	 */
	public String getLastServerIp() {
		return lastServerIp;
	}
	/**
	 * @param lastServerIp the lastServerIp to set
	 */
	public void setLastServerIp(String lastServerIp) {
		this.lastServerIp = lastServerIp;
	}
	/**
	 * @return the lastContextPath
	 */
	public String getLastContextPath() {
		return lastContextPath;
	}
	/**
	 * @param lastContextPath the lastContextPath to set
	 */
	public void setLastContextPath(String lastContextPath) {
		this.lastContextPath = lastContextPath;
	}
	/**
	 * @return the servers
	 */
	public Map<String, String> getServers() {
		return servers;
	}
	
	public void addResourceList(String resName) {
		if (resourceList.size() >= resMaxSize)
			resourceList.remove(0);
		resourceList.add(resName);
		lastResTime = new Date();
	}
	/**
	 * @return the lastResTime
	 */
	public Date getLastResTime() {
		return lastResTime;
	}
	/**
	 * @param lastResTime the lastResTime to set
	 */
	public void setLastResTime(Date lastResTime) {
		this.lastResTime = lastResTime;
	}
	/**
	 * @return the optr
	 */
	public SsoDto getOptr() {
		return optr;
	}
	/**
	 * @param optr the optr to set
	 */
	public void setOptr(SsoDto optr) {
		this.optr = optr;
	}
}
