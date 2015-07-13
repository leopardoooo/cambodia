/**
 * PDictProd.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PDictProd -> P_DICT_PROD mapping
 */
@POJO(tn = "P_DICT_PROD", sn = "", pk = "")
public class PDictProd implements Serializable {

	// PDictProd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8235269203469860540L;
	private String node_id;
	private String prod_id;
	private String node_pid;
	private String is_leaf;

	private String prod_name;
	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prodName) {
		prod_name = prodName;
	}

	/**
	 * default empty constructor
	 */
	public PDictProd() {
	}

	// node_id getter and setter
	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// node_pid getter and setter
	public String getNode_pid() {
		return node_pid;
	}

	public void setNode_pid(String node_pid) {
		this.node_pid = node_pid;
	}

	// is_leaf getter and setter
	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

}