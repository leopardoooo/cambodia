/**
 * SSignal.java
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * JSignal -> J_SIGNAL mapping
 */
@POJO(tn = "J_SIGNAL", sn = "SEQ_SIGNAL_ID", pk = "SIGNAL_ID")
public class JSignal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7830449241890586615L;
	private String signal_id;
	private String signal_date;
	private String signal_type;
	private String signal_content;

	/**
	 * @return the signal_id
	 */
	public String getSignal_id() {
		return signal_id;
	}

	/**
	 * @param signal_id
	 *            the signal_id to set
	 */
	public void setSignal_id(String signal_id) {
		this.signal_id = signal_id;
	}

	/**
	 * @return the signal_date
	 */
	public String getSignal_date() {
		return signal_date;
	}

	/**
	 * @param signal_date
	 *            the signal_date to set
	 */
	public void setSignal_date(String signal_date) {
		this.signal_date = signal_date;
	}

	/**
	 * @return the signal_type
	 */
	public String getSignal_type() {
		return signal_type;
	}

	/**
	 * @param signal_type
	 *            the signal_type to set
	 */
	public void setSignal_type(String signal_type) {
		this.signal_type = signal_type;
	}

	/**
	 * @return the signal_content
	 */
	public String getSignal_content() {
		return signal_content;
	}

	/**
	 * @param signal_content
	 *            the signal_content to set
	 */
	public void setSignal_content(String signal_content) {
		this.signal_content = signal_content;
	}

}