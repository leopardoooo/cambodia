package com.ycsoft.beans.base;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class BusiBase extends OptrBase  implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2111426119786944776L;
	private String busi_code;
	private Integer done_code;
	private Date create_time;

	private String busi_name;

	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

	/**
	 * @return the busi_code
	 */
	public String getBusi_code() {
		return busi_code;
	}

	/**
	 * @param busi_code
	 *            the busi_code to set
	 */
	public void setBusi_code(String busi_code) {
		busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, busi_code);
		this.busi_code = busi_code;
	}

	/**
	 * @return the done_code
	 */
	public Integer getDone_code() {
		return done_code;
	}

	/**
	 * @param done_code
	 *            the done_code to set
	 */
	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}

	/**
	 * @return the busi_name
	 */
	public String getBusi_name() {
		return busi_name;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


}
