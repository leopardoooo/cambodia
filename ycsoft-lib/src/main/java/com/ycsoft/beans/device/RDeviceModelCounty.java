/**
 * RDeviceModelCounty.java	2012/10/18
 */
 
package com.ycsoft.beans.device; 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * RDeviceModelCounty -> R_DEVICE_MODEL_COUNTY mapping 
 */
@POJO(
	tn="R_DEVICE_MODEL_COUNTY",
	sn="",
	pk="")
public class RDeviceModelCounty  implements Serializable {
	
	// RDeviceModelCounty all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -1886118034188513368L;
	private String device_type ;	
	private String device_model ;
	private String county_id;
	
	private String county_name;
	private String stb_model_src;
	private String card_model_src;
	private String modem_model_src;
	private String device_model_text;
	/**
	 * default empty constructor
	 */
	public RDeviceModelCounty() {}
	
	
	public String getDevice_model_text() {
		String deviceType = getDevice_type();
		if(!"STB".equals(deviceType) && !"CARD".equals(deviceType) && !"MODEM".equals(deviceType)){
			deviceType = "CTL";
		}
		device_model_text=MemoryDict.getDictName(deviceType+"_MODEL", device_model);
		return device_model_text;
	}


	public void setDevice_model_text(String deviceModelText) {
		device_model_text = deviceModelText;
	}


	// device_type getter and setter
	public String getDevice_type(){
		return this.device_type ;
	}
	
	public void setDevice_type(String device_type){
		this.device_type = device_type ;
	}
	
	// device_model getter and setter
	public String getDevice_model(){
		return this.device_model ;
	}
	
	public void setDevice_model(String device_model){
		this.device_model = device_model ;
	}


	public String getStb_model_src() {
		return stb_model_src;
	}


	public void setStb_model_src(String stbModelSrc) {
		stb_model_src = stbModelSrc;
	}


	public String getCard_model_src() {
		return card_model_src;
	}


	public void setCard_model_src(String cardModelSrc) {
		card_model_src = cardModelSrc;
	}


	public String getModem_model_src() {
		return modem_model_src;
	}


	public void setModem_model_src(String modemModelSrc) {
		modem_model_src = modemModelSrc;
	}


	public String getCounty_id() {
		return county_id;
	}


	public void setCounty_id(String countyId) {
		county_id = countyId;
		county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String countyName) {
		county_name = countyName;
	}



	
}