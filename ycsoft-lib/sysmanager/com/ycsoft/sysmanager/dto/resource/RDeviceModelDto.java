package com.ycsoft.sysmanager.dto.resource;

import java.util.List;

import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RPairCfg;

/**
 * 
 */
public class RDeviceModelDto extends RDeviceModel{
	//机顶盒设备的智能卡配对信息,仅适用于机顶盒,其他设备请忽略
	private List<RPairCfg> stbPairCardCfgs;
	private String virtual_card_model;//虚拟卡配置
	private String virtual_modem_model;//虚拟猫配置
	
	/**
	 * @return the virtual_modem_model
	 */
	public String getVirtual_modem_model() {
		return virtual_modem_model;
	}

	/**
	 * @param virtualModemModel the virtual_modem_model to set
	 */
	public void setVirtual_modem_model(String virtualModemModel) {
		virtual_modem_model = virtualModemModel;
	}

	
	/**
	 * @return the virtual_card_model
	 */
	public String getVirtual_card_model() {
		return virtual_card_model;
	}

	/**
	 * @param virtualCardModel the virtual_card_model to set
	 */
	public void setVirtual_card_model(String virtualCardModel) {
		virtual_card_model = virtualCardModel;
	}

	/**
	 * @return the stbPairCardCfgs
	 */
	public List<RPairCfg> getStbPairCardCfgs() {
		return stbPairCardCfgs;
	}

	/**
	 * @param stbPairCardCfgs the stbPairCardCfgs to set
	 */
	public void setStbPairCardCfgs(List<RPairCfg> stbPairCardCfgs) {
		this.stbPairCardCfgs = stbPairCardCfgs;
	}
}
