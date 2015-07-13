/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TBusiDoc;


/**
 *
 * @author hh
 */
public class BusiDocDto extends TBusiDoc{

	/**
	 *
	 */
	private static final long serialVersionUID = 7428675340140234364L;
	private String busi_code ;

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

}
