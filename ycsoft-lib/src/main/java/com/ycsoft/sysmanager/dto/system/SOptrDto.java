package com.ycsoft.sysmanager.dto.system;

import com.ycsoft.beans.system.SOptr;

/**
 *
 * @author sheng Mar 23, 2010 5:41:49 PM
 */
public class SOptrDto extends SOptr {

	/**
	 *
	 */
	private static final long serialVersionUID = -4595365815848716547L;
	private String confirmPwd;
	private String district;
	private String depot;
	

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the depot
	 */
	public String getDepot() {
		return depot;
	}

	/**
	 * @param depot the depot to set
	 */
	public void setDepot(String depot) {
		this.depot = depot;
	}

}
