/**
 *
 */
package com.ycsoft.business.dto.device;

import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RModem;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class DeviceDto extends RDevice{
	/**
	 *
	 */
	private static final long serialVersionUID = -1026219756512633039L;
	private String device_code;			//设备编号
	private String device_mac;          //设备mac地址
	private RDeviceModel deviceModel;	//设备型号
	private RCard pairCard;				//配对的智能卡
	private String isPairCard;
	private RModem pairModem;			//配对的MODEM
	private String buy_mode;			//购买方式
	private String buy_mode_text;		//购买方式
	private Integer price;				//价格
	private Integer fee;			    //实收金额
	private String isPairModem;
	private String stbMac;
	


	public String getStbMac() {
		return stbMac;
	}
	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}
	public String getIsPairModem() {
		return isPairModem;
	}
	public void setIsPairModem(String isPairModem) {
		this.isPairModem = isPairModem;
	}
	public String getBuy_mode() {
		return buy_mode;
	}
	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
		setBuy_mode_text(MemoryDict.getDictName(DictKey.BUSI_BUY_MODE, buy_mode));
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public RDeviceModel getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(RDeviceModel deviceModel) {
		this.deviceModel = deviceModel;
	}
	public RCard getPairCard() {
		return pairCard;
	}
	public void setPairCard(RCard pairCard) {
		this.pairCard = pairCard;
	}
	public String getIsPairCard() {
		return isPairCard;
	}
	public void setIsPairCard(String isPairCard) {
		this.isPairCard = isPairCard;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public RModem getPairModem() {
		return pairModem;
	}
	public void setPairModem(RModem pairModem) {
		this.pairModem = pairModem;
	}
	/**
	 * @return the buy_mode_text
	 */
	public String getBuy_mode_text() {
		return buy_mode_text;
	}
	/**
	 * @param buy_mode_text the buy_mode_text to set
	 */
	public void setBuy_mode_text(String buy_mode_text) {
		this.buy_mode_text = buy_mode_text;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}

	
	
}
