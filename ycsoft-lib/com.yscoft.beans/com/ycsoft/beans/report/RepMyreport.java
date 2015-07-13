/**
 * RepMyreport.java	2010/08/13
 */

package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * RepMyreport -> REP_MYREPORT mapping
 */
@POJO(
	tn="REP_MYREPORT",
	sn="",
	pk="")
public class RepMyreport implements Serializable {

	// RepMyreport all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2280645905552259176L;
	private String rep_id ;
	private String optr_id ;

	/**
	 * default empty constructor
	 */
	public RepMyreport() {}


	// rep_id getter and setter
	public String getRep_id(){
		return rep_id ;
	}

	public void setRep_id(String rep_id){
		this.rep_id = rep_id ;
	}


	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

}