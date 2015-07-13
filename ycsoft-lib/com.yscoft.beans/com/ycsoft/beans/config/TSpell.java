package com.ycsoft.beans.config;

/**
 * TSpell.java	2010/10/18
 */


import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TSpell -> T_SPELL mapping
 */
@POJO(
	tn="T_SPELL",
	sn="",
	pk="data_id")
public class TSpell implements Serializable {

	// TSpell all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5718026196870095311L;
	private String data_id ;
	private String data_type ;
	private String full_sepll ;
	private String seq_sepll ;

	/**
	 * default empty constructor
	 */
	public TSpell() {}


	// data_id getter and setter
	public String getData_id(){
		return data_id ;
	}

	public void setData_id(String data_id){
		this.data_id = data_id ;
	}

	// data_type getter and setter
	public String getData_type(){
		return data_type ;
	}

	public void setData_type(String data_type){
		this.data_type = data_type ;
	}

	// full_sepll getter and setter
	public String getFull_sepll(){
		return full_sepll ;
	}

	public void setFull_sepll(String full_sepll){
		this.full_sepll = full_sepll ;
	}

	// seq_sepll getter and setter
	public String getSeq_sepll(){
		return seq_sepll ;
	}

	public void setSeq_sepll(String seq_sepll){
		this.seq_sepll = seq_sepll ;
	}

}