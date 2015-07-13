/**
 * TBusiDoc.java	2010/04/20
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TBusiDoc -> T_BUSI_DOC mapping
 */
@POJO(tn = "T_BUSI_DOC", sn = "", pk = "DOC_TYPE")
public class TBusiDoc implements Serializable {

	// TBusiDoc all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8431943663537067453L;
	private String doc_type;
	private String doc_name;
	private String is_invoice;
	private String show_county_id;


	/**
	 * default empty constructor
	 */
	public TBusiDoc() {
	}

	// doc_type getter and setter
	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	// doc_name getter and setter
	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	// is_invoice getter and setter
	public String getIs_invoice() {
		return is_invoice;
	}

	public void setIs_invoice(String is_invoice) {
		this.is_invoice = is_invoice;
	}

	public String getShow_county_id() {
		return show_county_id;
	}

	public void setShow_county_id(String show_county_id) {
		this.show_county_id = show_county_id;
	}

}