/**
 * CFeeBusi.java	2010/07/30
 */

package com.ycsoft.beans.core.fee;

import com.ycsoft.commons.pojo.UserTypeDto;
import com.ycsoft.daos.config.POJO;

/**
 * CFeeBusi -> C_FEE_BUSI mapping
 */
@POJO(tn = "C_FEE_BUSI", sn = "SEQ_FEE_SN", pk = "FEE_SN")
public class CFeeBusi extends CFee implements UserTypeDto {

	// CFeeBusi all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 87790166691993679L;
	private String user_type;
	private String serv_type;
	private String terminal_type;
	private String net_type;
	

	/**
	 * default empty constructor
	 */
	public CFeeBusi() {
	}

	// user_type getter and setter
	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	// serv_type getter and setter
	public String getServ_type() {
		return serv_type;
	}

	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
	}

	// terminal_type getter and setter
	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}

	// net_type getter and setter
	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

}