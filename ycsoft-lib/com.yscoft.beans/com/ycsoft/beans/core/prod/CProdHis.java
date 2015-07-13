/**
 * CProdHis.java	2010/07/19
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CProdHis -> C_PROD_HIS mapping
 */
@POJO(tn = "C_PROD_HIS", sn = "", pk = "")
public class CProdHis extends CProd implements Serializable {

	// CProdHis all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = 6612824990111243529L;
	private Integer order_done_code;

	/**
	 * default empty constructor
	 */
	public CProdHis() {
	}

	/**
	 * @return the order_done_code
	 */
	public Integer getOrder_done_code() {
		return order_done_code;
	}

	/**
	 * @param order_done_code
	 *            the order_done_code to set
	 */
	public void setOrder_done_code(Integer order_done_code) {
		this.order_done_code = order_done_code;
	}

}