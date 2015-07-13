/**
 * RDeviceHis.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RDeviceHis -> R_DEVICE_HIS mapping
 */
@POJO(tn = "R_DEVICE_HIS", sn = "", pk = "")
public class RDeviceHis extends RDevice implements Serializable {

	// RDeviceHis all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8182106968185671800L;

	private String pair_card_id;
	private String stb_id;
	private String card_id;
	private String modem_id;
	private String modem_mac;
	private String pair_modem_id;
	
	/**
	 * default empty constructor
	 */
	public RDeviceHis() {
	}

	/**
	 * @return the pair_card_id
	 */
	public String getPair_card_id() {
		return pair_card_id;
	}

	/**
	 * @param pair_card_id
	 *            the pair_card_id to set
	 */
	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}

	/**
	 * @return the stb_id
	 */
	public String getStb_id() {
		return stb_id;
	}

	/**
	 * @param stb_id
	 *            the stb_id to set
	 */
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}

	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}

	/**
	 * @param card_id
	 *            the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	/**
	 * @return the modem_id
	 */
	public String getModem_id() {
		return modem_id;
	}

	/**
	 * @param modem_id
	 *            the modem_id to set
	 */
	public void setModem_id(String modem_id) {
		this.modem_id = modem_id;
	}

	/**
	 * @return the modem_mac
	 */
	public String getModem_mac() {
		return modem_mac;
	}

	/**
	 * @param modem_mac
	 *            the modem_mac to set
	 */
	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}

	public String getPair_modem_id() {
		return pair_modem_id;
	}

	public void setPair_modem_id(String pair_modem_id) {
		this.pair_modem_id = pair_modem_id;
	}

}