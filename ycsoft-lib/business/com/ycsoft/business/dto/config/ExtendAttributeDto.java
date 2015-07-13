/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TExtendAttribute;

/**
 * @author YC-SOFT
 *
 */
public class ExtendAttributeDto extends TExtendAttribute{

	/**
	 *
	 */
	private static final long serialVersionUID = -2238490101456282002L;
	private String busi_code ;

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
}
