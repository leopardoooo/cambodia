/**
 * TAddress.java	2010/03/11
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TAddress -> T_ADDRESS mapping
 */
@POJO(tn = "T_ADDRESS", sn = "", pk = "addr_id")
public class TAddress extends OptrBase implements Serializable {

	// TAddress all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3765296477676040618L;
	private String addr_id;
	private String addr_pid;
	private String addr_name;
	private String addr_full_name;
	private String is_leaf;
	private Integer tree_level;
	private Integer capacity;
	private String net_type;
	private String busi_optr_id;
	private String status;
	
	private String busi_optr_name;
	
	private String serv_optr_id;//运维人员
	private String serv_optr_name;//运维人员

	/**
	 * default empty constructor
	 */
	public TAddress() {
	}

	// addr_id getter and setter
	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	// addr_pid getter and setter
	public String getAddr_pid() {
		return addr_pid;
	}

	public void setAddr_pid(String addr_pid) {
		this.addr_pid = addr_pid;
	}

	// addr_name getter and setter
	public String getAddr_name() {
		return addr_name;
	}

	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}

	// addr_full_name getter and setter
	public String getAddr_full_name() {
		return addr_full_name;
	}

	public void setAddr_full_name(String addr_full_name) {
		this.addr_full_name = addr_full_name;
	}

	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

	public Integer getTree_level() {
		return tree_level;
	}

	public void setTree_level(Integer tree_level) {
		this.tree_level = tree_level;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	public String getBusi_optr_id() {
		return busi_optr_id;
	}

	public void setBusi_optr_id(String busi_optr_id) {
		this.busi_optr_id = busi_optr_id;
		this.busi_optr_name = MemoryDict.getDictName(DictKey.OPTR, busi_optr_id);
	}

	public String getBusi_optr_name() {
		return busi_optr_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the serv_optr_id
	 */
	public String getServ_optr_id() {
		return serv_optr_id;
	}

	/**
	 * @param serv_optr_id the serv_optr_id to set
	 */
	public void setServ_optr_id(String serv_optr_id) {
		this.serv_optr_id = serv_optr_id;
		this.serv_optr_name = MemoryDict.getDictName(DictKey.OPTR, serv_optr_id);
	}

	/**
	 * @return the serv_optr_name
	 */
	public String getServ_optr_name() {
		return serv_optr_name;
	}

}