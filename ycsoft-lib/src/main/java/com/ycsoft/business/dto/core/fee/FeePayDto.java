/**
 *
 */
package com.ycsoft.business.dto.core.fee;

import com.ycsoft.beans.core.fee.CFeePay;

public class FeePayDto extends CFeePay {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9162744298643885923L;
	/**
	 *
	 */
	private String data_right;//权限级别

	public String getData_right() {
		return data_right;
	}

	public void setData_right(String data_right) {
		this.data_right = data_right;
	}



}
