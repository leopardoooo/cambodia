/**
 * JOdscntRecord.java	2012/09/25
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable;
import java.util.Date;
import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.daos.config.POJO;


/**
 * JOdscntRecord -> J_ODSCNT_RECORD mapping 
 */
@POJO(
	tn="J_BAND_COMMAND",
	sn="",
	pk="")
public class JOdscntRecord extends OptrBase implements Serializable {
	
	// JOdscntRecord all properties 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8815278770978480060L;
	private String ods_type;
	private String addr_id;
	private String ods_date;
	private Date done_date;
	
	private String addr_name;

	/**
	 * default empty constructor
	 */
	public JOdscntRecord() {}

	public String getOds_type() {
		return ods_type;
	}

	public void setOds_type(String ods_type) {
		this.ods_type = ods_type;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public String getOds_date() {
		return ods_date;
	}

	public void setOds_date(String ods_date) {
		this.ods_date = ods_date;
	}

	public Date getDone_date() {
		return done_date;
	}

	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}

	public String getAddr_name() {
		return addr_name;
	}

	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}
	
}