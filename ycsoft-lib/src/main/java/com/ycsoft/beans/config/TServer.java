/**
 * TServer.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TServer -> T_SERVER mapping
 */
@POJO(
	tn="T_SERVER",
	sn="",
	pk="server_id")
public class TServer implements Serializable {

	// TServer all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4260478065716257310L;
	private String server_id ;
	private String server_name ;
	private String supplier_id ;
	private String ip ;
	private Integer port ;
	private String user_name ;
	private String pwd ;
	private String carrier_id ;
	private String check_type ;
	private List<String> countyList;
	private String for_osd;
	private String serv_type;
	private String serv_type_text;

	private String supplier_id_text;
	private String for_osd_text;
	/**
	 * default empty constructor
	 */
	public TServer() {}


	// server_id getter and setter
	public String getServer_id(){
		return server_id ;
	}

	public void setServer_id(String server_id){
		this.server_id = server_id ;
	}

	// server_name getter and setter
	public String getServer_name(){
		return server_name ;
	}

	public void setServer_name(String server_name){
		this.server_name = server_name ;
	}

	// supplier_id getter and setter
	public String getSupplier_id(){
		return supplier_id ;
	}

	public void setSupplier_id(String supplier_id){
		this.supplier_id = supplier_id ;
		this.supplier_id_text = MemoryDict.getDictName(DictKey.CA_SUPPLIER, this.supplier_id);
	}

	// ip getter and setter
	public String getIp(){
		return ip ;
	}

	public void setIp(String ip){
		this.ip = ip ;
	}

	// port getter and setter
	public Integer getPort(){
		return port ;
	}

	public void setPort(Integer port){
		this.port = port ;
	}

	// user_name getter and setter
	public String getUser_name(){
		return user_name ;
	}

	public void setUser_name(String user_name){
		this.user_name = user_name ;
	}

	// pwd getter and setter
	public String getPwd(){
		return pwd ;
	}

	public void setPwd(String pwd){
		this.pwd = pwd ;
	}

	// carrier_id getter and setter
	public String getCarrier_id(){
		return carrier_id ;
	}

	public void setCarrier_id(String carrier_id){
		this.carrier_id = carrier_id ;
	}

	// check_type getter and setter
	public String getCheck_type(){
		return check_type ;
	}

	public void setCheck_type(String check_type){
		this.check_type = check_type ;
	}


	public List<String> getCountyList() {
		return countyList;
	}


	public void setCountyList(List<String> countyList) {
		this.countyList = countyList;
	}


	public String getFor_osd() {
		return for_osd;
	}


	public void setFor_osd(String for_osd) {
		this.for_osd = for_osd;
		for_osd_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.for_osd);
	}


	public String getServ_type() {
		return serv_type;
	}


	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
		this.serv_type_text = MemoryDict.getDictName(DictKey.SERV_ID, serv_type);
	}


	public String getServ_type_text() {
		return serv_type_text;
	}


	public String getFor_osd_text() {
		return for_osd_text;
	}


	public String getSupplier_id_text() {
		return supplier_id_text;
	}

}