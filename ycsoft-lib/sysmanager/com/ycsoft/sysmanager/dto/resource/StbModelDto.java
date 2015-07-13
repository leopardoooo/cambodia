package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.beans.device.RStbModel;

public class StbModelDto extends RStbModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5965916257116159243L;
	private String virtual_card_model_name;
	private String virtual_modem_model_name;

	/**
	 * @return the virtual_card_model_name
	 */
	public String getVirtual_card_model_name() {
		return virtual_card_model_name;
	}

	/**
	 * @param virtual_card_model_name
	 *            the virtual_card_model_name to set
	 */
	public void setVirtual_card_model_name(String virtual_card_model_name) {
		this.virtual_card_model_name = virtual_card_model_name;
	}

	public String getVirtual_modem_model_name() {
		return virtual_modem_model_name;
	}

	public void setVirtual_modem_model_name(String virtual_modem_model_name) {
		this.virtual_modem_model_name = virtual_modem_model_name;
	}

}
