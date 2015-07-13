package com.ycsoft.sysmanager.dto.depot;

import com.ycsoft.beans.depot.RDepotDefine;

/**
 * @author xu
 */
public class SDepotDto extends RDepotDefine {
	/**
	 *
	 */
	private static final long serialVersionUID = 6874511896332541412L;
	private String district;

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
