/**
 * TBulletin.java	2010/11/26
 */
 
package com.ycsoft.beans.system; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * SBulletin -> S_BULLETIN mapping 
 */
@POJO(
	tn="S_BULLETIN",
	sn="SEQ_S_BULLETIN",
	pk="BULLETIN_ID")
public class SBulletin implements Serializable {
	
	// TBulletin all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511110446932005045L;
	private String bulletin_id ;	
	private String bulletin_title ;	
	private String bulletin_content ;	
	private String bulletin_publisher ;	
	private Date create_date ;
	private Date eff_date;
	private Date exp_date;
	private String status ;	
	private String optr_id ;
	
	private String status_text;
	
	
	/**
	 * default empty constructor
	 */
	public SBulletin() {}
	
	
	// bulletin_id getter and setter
	public String getBulletin_id(){
		return this.bulletin_id ;
	}
	
	public void setBulletin_id(String bulletin_id){
		this.bulletin_id = bulletin_id ;
	}
	
	// bulletin_title getter and setter
	public String getBulletin_title(){
		return this.bulletin_title ;
	}
	
	public void setBulletin_title(String bulletin_title){
		this.bulletin_title = bulletin_title ;
	}
	
	// bulletin_content getter and setter
	public String getBulletin_content(){
		return this.bulletin_content ;
	}
	
	public void setBulletin_content(String bulletin_content){
		this.bulletin_content = bulletin_content ;
	}
	
	// bulletin_publisher getter and setter
	public String getBulletin_publisher(){
		return this.bulletin_publisher ;
	}
	
	public void setBulletin_publisher(String bulletin_publisher){
		this.bulletin_publisher = bulletin_publisher ;
	}
	
	// create_date getter and setter
	public Date getCreate_date(){
		return this.create_date ;
	}
	
	public void setCreate_date(Date create_date){
		this.create_date = create_date ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}


	public String getStatus_text() {
		return status_text;
	}


	public void setStatus_text(String statusText) {
		status_text = statusText;
	}


	public Date getEff_date() {
		return eff_date;
	}


	public void setEff_date(Date effDate) {
		eff_date = effDate;
	}


	public Date getExp_date() {
		return exp_date;
	}


	public void setExp_date(Date expDate) {
		exp_date = expDate;
	}

}