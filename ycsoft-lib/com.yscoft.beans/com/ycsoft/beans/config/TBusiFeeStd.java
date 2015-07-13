/**
 * TBusiFeeStd.java	2010/10/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TBusiFeeStd -> T_BUSI_FEE_STD mapping
 */
@POJO(
	tn="T_BUSI_FEE_STD",
	sn="SEQ_BUSI_FEE_STD",
	pk="fee_std_id")
public class TBusiFeeStd extends TBusiFee implements Serializable {
	// TBusiFeeStd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2004087406026421192L;
	private String template_id ;
	private String fee_std_id ;
	private Integer min_value ;
	private Integer max_value ;
	private Integer default_value ;
	private Integer sum_fee;
	private String optr_id;
	private String optr_name;

	private String device_buy_mode;
	private String device_type;
	private List<String> deviceModelList;


	private String device_buy_mode_text;
	private String device_type_text;
	private String device_model;
	private String device_model_text;

	/**
	 * default empty constructor
	 */
	public TBusiFeeStd() {}


	// template_id getter and setter
	public String getTemplate_id(){
		return template_id ;
	}

	public void setTemplate_id(String template_id){
		this.template_id = template_id ;
	}



	// fee_std_id getter and setter
	public String getFee_std_id(){
		return fee_std_id ;
	}

	public void setFee_std_id(String fee_std_id){
		this.fee_std_id = fee_std_id ;
	}

	// min_value getter and setter
	public Integer getMin_value(){
		return min_value ;
	}

	public void setMin_value(Integer min_value){
		this.min_value = min_value ;
	}

	// max_value getter and setter
	public Integer getMax_value(){
		return max_value ;
	}

	public void setMax_value(Integer max_value){
		this.max_value = max_value ;
	}

	// default_value getter and setter
	public Integer getDefault_value(){
		return default_value ;
	}

	public void setDefault_value(Integer default_value){
		this.default_value = default_value ;
	}


	public String getDevice_buy_mode() {
		return device_buy_mode;
	}


	public void setDevice_buy_mode(String device_buy_mode) {
		this.device_buy_mode = device_buy_mode;
		device_buy_mode_text =  MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE, this.device_buy_mode);
	}


	public String getDevice_type() {
		return device_type;
	}


	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE, this.device_type);
	}


	public List<String> getDeviceModelList() {
		return deviceModelList;
	}


	public void setDeviceModelList(List<String> deviceModelList) {
		this.deviceModelList = deviceModelList;
	}

	public String getDevice_buy_mode_text() {
		return device_buy_mode_text;
	}


	public void setDevice_buy_mode_text(String device_buy_mode_text) {
		this.device_buy_mode_text = device_buy_mode_text;
	}


	public String getDevice_type_text() {
		return device_type_text;
	}


	public void setDevice_type_text(String device_type_text) {
		this.device_type_text = device_type_text;
	}


	public String getDevice_model_text() {
		return device_model_text;
	}


	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}


	public Integer getSum_fee() {
		return sum_fee;
	}


	public String getDevice_model() {
		return device_model;
	}


	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}


	public void setSum_fee(Integer sum_fee) {
		this.sum_fee = sum_fee;
	}


	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}


	public String getOptr_name() {
		return optr_name;
	}


}