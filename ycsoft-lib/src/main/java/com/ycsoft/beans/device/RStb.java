/**
 * RStb.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RStb -> R_STB mapping
 */
@POJO(tn = "R_STB", sn = "", pk = "DEVICE_ID")
public class RStb extends RDevice implements Serializable {

	// RStb all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3768330464494817773L;
	private String stb_id;
	private String pair_card_id;
	private String pair_modem_id;

	/**
	 * default empty constructor
	 */
	public RStb() {
	}

	// stb_id getter and setter
	public String getStb_id() {
		return stb_id;
	}

	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}

	// pair_card_id getter and setter
	public String getPair_card_id() {
		return pair_card_id;
	}

	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}

	public String getPair_modem_id() {
		return pair_modem_id;
	}

	public void setPair_modem_id(String pair_modem_id) {
		this.pair_modem_id = pair_modem_id;
	}


}