package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.JSendMsg;

public class TSendMsgDto extends JSendMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -42217219672329973L;
	
	private String msg_type_str;
	private String address_str;
	private String terminal_type_str;
	private String unit_str;
	private String hasten_stop_flag_str;
	private String cust_type_str;
	private String cust_class_str;
	private String cust_colony_str;
	private String prod_id_str;
	
	public String getMsg_type_str() {
		return msg_type_str;
	}

	public void setMsg_type_str(String msgTypeStr) {
		msg_type_str = msgTypeStr;
	}

	public String getAddress_str() {
		return address_str;
	}

	public void setAddress_str(String addressStr) {
		address_str = addressStr;
	}

	public String getTerminal_type_str() {
		return terminal_type_str;
	}

	public void setTerminal_type_str(String terminalTypeStr) {
		terminal_type_str = terminalTypeStr;
	}

	public String getUnit_str() {
		return unit_str;
	}

	public void setUnit_str(String unitStr) {
		unit_str = unitStr;
	}

	public String getHasten_stop_flag_str() {
		return hasten_stop_flag_str;
	}

	public void setHasten_stop_flag_str(String hastenStopFlagStr) {
		hasten_stop_flag_str = hastenStopFlagStr;
	}

	public String getCust_type_str() {
		return cust_type_str;
	}

	public void setCust_type_str(String custTypeStr) {
		cust_type_str = custTypeStr;
	}

	public String getCust_class_str() {
		return cust_class_str;
	}

	public void setCust_class_str(String custClassStr) {
		cust_class_str = custClassStr;
	}

	public String getCust_colony_str() {
		return cust_colony_str;
	}

	public void setCust_colony_str(String custColonyStr) {
		cust_colony_str = custColonyStr;
	}

	public String getProd_id_str() {
		return prod_id_str;
	}

	public void setProd_id_str(String prodIdStr) {
		prod_id_str = prodIdStr;
	}
}
