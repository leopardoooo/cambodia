package com.ycsoft.beans.device;

/**
 * RPairCfg.java	2010/09/13
 */

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RPairCfg -> R_PAIR_CFG mapping
 */
@POJO(tn = "R_PAIR_CFG", sn = "", pk = "")
public class RPairCfg implements Serializable {

	// RPairCfg all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6871181855293803188L;
	private String stb_model;
	private String card_model;

	/**
	 * default empty constructor
	 */
	public RPairCfg() {
	}

	// stb_model getter and setter
	public String getStb_model() {
		return stb_model;
	}

	public void setStb_model(String stb_model) {
		this.stb_model = stb_model;
	}

	// card_model getter and setter
	public String getCard_model() {
		return card_model;
	}

	public void setCard_model(String card_model) {
		this.card_model = card_model;
	}

}