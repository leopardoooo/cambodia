/**
 * SAgent.java	2015/09/02
 */
 
package com.ycsoft.beans.system; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * SAgent -> S_AGENT mapping 
 */
@POJO(
	tn="S_AGENT",
	sn="SEQ_AGENT_ID",
	pk="ID")
public class SAgent implements Serializable {
	
	// SAgent all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -8437077888132146145L;
	private String id ;	
	private String name ;	
	private String agent_type ;	
	private String tel ;	
	private String cert_type ;	
	private String cert_num ;	
	private String busi_optr_id ;	
	private String taxid ;	
	private String guarantee_info ;	
	private Date create_time ;	
	private String optr_id ;	
	private String remark ;	
	
	private String agent_type_text;
	private String cert_type_text;
	private String busi_optr_name;
	private String optr_name;

	/**
	 * default empty constructor
	 */
	public SAgent() {}
	
	// id getter and setter
	public String getId(){
		return this.id ;
	}
	
	public void setId(String id){
		this.id = id ;
	}
	
	// name getter and setter
	public String getName(){
		return this.name ;
	}
	
	public void setName(String name){
		this.name = name ;
	}
	
	// agent_type getter and setter
	public String getAgent_type(){
		return this.agent_type ;
	}
	
	public void setAgent_type(String agent_type){
		this.agent_type = agent_type ;
		this.agent_type_text = MemoryDict.getDictName(DictKey.CUST_TYPE, agent_type);
	}
	
	// tel getter and setter
	public String getTel(){
		return this.tel ;
	}
	
	public void setTel(String tel){
		this.tel = tel ;
	}
	
	// cert_type getter and setter
	public String getCert_type(){
		return this.cert_type ;
	}
	
	public void setCert_type(String cert_type){
		this.cert_type = cert_type ;
		this.cert_type_text = MemoryDict.getDictName(DictKey.CERT_TYPE, cert_type);
	}
	
	// cert_num getter and setter
	public String getCert_num(){
		return this.cert_num ;
	}
	
	public void setCert_num(String cert_num){
		this.cert_num = cert_num ;
	}
	
	// busi_optr_id getter and setter
	public String getBusi_optr_id(){
		return this.busi_optr_id ;
	}
	
	public void setBusi_optr_id(String busi_optr_id){
		this.busi_optr_id = busi_optr_id ;
		this.busi_optr_name = MemoryDict.getDictName(DictKey.OPTR, busi_optr_id);
	}
	
	// taxid getter and setter
	public String getTaxid(){
		return this.taxid ;
	}
	
	public void setTaxid(String taxid){
		this.taxid = taxid ;
	}
	
	// guarantee_info getter and setter
	public String getGuarantee_info(){
		return this.guarantee_info ;
	}
	
	public void setGuarantee_info(String guarantee_info){
		this.guarantee_info = guarantee_info ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}

	
	public String getAgent_type_text() {
		return agent_type_text;
	}


	public String getCert_type_text() {
		return cert_type_text;
	}


	public String getBusi_optr_name() {
		return busi_optr_name;
	}


	public String getOptr_name() {
		return optr_name;
	}

}