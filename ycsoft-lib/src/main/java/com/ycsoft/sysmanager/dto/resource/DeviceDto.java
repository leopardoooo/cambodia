package com.ycsoft.sysmanager.dto.resource;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceChangeDto;
import com.ycsoft.beans.device.RDeviceInput;
import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.beans.device.RDeviceUseRecords;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class DeviceDto extends RDevice{
	/**
	 *
	 */
	private static final long serialVersionUID = -5159183119216432388L;
	private String device_code;
	private String pair_device_model;
	private String pair_device_code;
	private String pair_device_modem_code;//配对猫code
	private String pair_device_modem_model;//配对猫型号
	private String pair_device_stb_code;
	private String pair_device_stb_model;
	private String pair_card_id; //配对智能卡Id
	private String pair_modem_id;//配对猫Id

	@SuppressWarnings("unused")
	private String pair_device_model_text;
	private String pair_device_modem_model_text;
	private String pair_device_stb_model_text;
	private String modem_mac;
	
	private String county_id;
	private String county_id_text;
	private String cust_id;
	private String cust_no;
	private String str9;
	private String develop_optr_name;
	private String cust_name;
	private String wrong_device_code;
	private String ca_type;
	private String area_id;
	private Date create_time;
	private RDeviceInput deviceInput;
	private List<RDeviceTransfer> deviceTransferList;
	private List<RDeviceChangeDto> deviceUseRecordsList;
	
	public RDeviceInput getDeviceInput() {
		return deviceInput;
	}

	public void setDeviceInput(RDeviceInput deviceInput) {
		this.deviceInput = deviceInput;
	}

	public List<RDeviceTransfer> getDeviceTransferList() {
		return deviceTransferList;
	}

	public void setDeviceTransferList(List<RDeviceTransfer> deviceTransferList) {
		this.deviceTransferList = deviceTransferList;
	}

	/**
	 * @return the device_code
	 */
	public String getDevice_code() {
		return device_code;
	}

	/**
	 * @param device_code
	 *            the device_code to set
	 */
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	/**
	 * @return the pair_device_model
	 */
	public String getPair_device_model() {
		return pair_device_model;
	}

	/**
	 * @param pair_device_model
	 *            the pair_device_model to set
	 */
	public void setPair_device_model(String pair_device_model) {
		this.pair_device_model = pair_device_model;
	}

	/**
	 * @return the pair_device_code
	 */
	public String getPair_device_code() {
		return pair_device_code;
	}

	/**
	 * @param pair_device_code
	 *            the pair_device_code to set
	 */
	public void setPair_device_code(String pair_device_code) {
		this.pair_device_code = pair_device_code;
	}

	public String getModem_mac() {
		return modem_mac;
	}

	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}

	/**
	 * @return the pair_device_model_text
	 */
	public String getPair_device_model_text() {
		return MemoryDict.getDictName("CARD_MODEL", pair_device_model);
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
		county_id_text = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}

	public String getCounty_id_text() {
		return county_id_text;
	}

	public String getWrong_device_code() {
		return wrong_device_code;
	}

	public void setWrong_device_code(String wrong_device_code) {
		this.wrong_device_code = wrong_device_code;
	}

	public String getCa_type() {
		return ca_type;
	}

	public void setCa_type(String caType) {
		ca_type = caType;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getPair_device_modem_code() {
		return pair_device_modem_code;
	}

	public void setPair_device_modem_code(String pair_device_modem_code) {
		this.pair_device_modem_code = pair_device_modem_code;
	}

	public String getPair_device_modem_model() {
		return pair_device_modem_model;
	}

	public void setPair_device_modem_model(String pair_device_modem_model) {
		this.pair_device_modem_model = pair_device_modem_model;
	}

	public String getPair_device_stb_code() {
		return pair_device_stb_code;
	}

	public void setPair_device_stb_code(String pair_device_stb_code) {
		this.pair_device_stb_code = pair_device_stb_code;
	}

	public String getPair_device_stb_model() {
		return pair_device_stb_model;
	}

	public void setPair_device_stb_model(String pair_device_stb_model) {
		this.pair_device_stb_model = pair_device_stb_model;
	}

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

	/**
	 * @return the pair_device_modem_model_text
	 */
	public String getPair_device_modem_model_text() {
		return MemoryDict.getDictName("MODEM_MODEL", pair_device_modem_model);
	}

	/**
	 * @return the pair_device_stb_model_text
	 */
	public String getPair_device_stb_model_text() {
		return MemoryDict.getDictName("STB_MODEL", pair_device_stb_model);
	}

	public List<RDeviceChangeDto> getDeviceUseRecordsList() {
		return deviceUseRecordsList;
	}

	public void setDeviceUseRecordsList(List<RDeviceChangeDto> deviceUseRecordsList) {
		this.deviceUseRecordsList = deviceUseRecordsList;
	}

	public String getStr9() {
		return str9;
	}

	public void setStr9(String str9) {
		this.str9 = str9;
		this.develop_optr_name = MemoryDict.getDictName(DictKey.OPTR, str9);
	}

	public void setDevelop_optr_name(String develop_optr_name) {
		this.develop_optr_name = develop_optr_name;
	}

}
