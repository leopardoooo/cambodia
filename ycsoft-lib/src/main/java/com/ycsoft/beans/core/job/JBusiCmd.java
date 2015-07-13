/**
 * JBusiCmd.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JBusiCmd -> J_BUSI_CMD mapping
 */
@POJO(tn = "J_BUSI_CMD", sn = "", pk = "")
public class JBusiCmd extends BusiBase implements Serializable {

	// JBusiCmd all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = 8820895038770330721L;
	private Integer job_id;
	private String busi_cmd_type;
	private String cust_id;
	private String user_id;
	private String net_type;
	private String stb_id;
	private String card_id;
	private String modem_mac;
	private String prod_sn;
	private String prod_id;
	private String detail_params;
	private int priority;
	
	private String supplier_id;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @param job_id
	 *            the job_id to set
	 */
	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	/**
	 * @return the detail_param
	 */
	
	/**
	 * default empty constructor
	 */
	public JBusiCmd() {
	}

	// job_id getter and setter
	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	// busi_cmd_type getter and setter
	public String getBusi_cmd_type() {
		return busi_cmd_type;
	}

	public void setBusi_cmd_type(String busi_cmd_type) {
		this.busi_cmd_type = busi_cmd_type;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	// net_type getter and setter
	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	// stb_id getter and setter
	public String getStb_id() {
		return stb_id;
	}

	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}

	// card_id getter and setter
	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	// modem_id getter and setter
	public String getModem_mac() {
		return modem_mac;
	}

	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}

	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getDetail_params() {
		return detail_params;
	}

	public void setDetail_params(String detail_params) {
		this.detail_params = detail_params;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

}