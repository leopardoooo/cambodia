/**
 * RCard.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RCard -> R_CARD mapping
 */
@POJO(tn = "R_CARD", sn = "", pk = "DEVICE_ID")
public class RCard extends RDevice implements Serializable {

	// RCard all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3869168264872705659L;
	private String card_id;

	/**
	 * default empty constructor
	 */
	public RCard() {
	}

	// card_id getter and setter
	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

}