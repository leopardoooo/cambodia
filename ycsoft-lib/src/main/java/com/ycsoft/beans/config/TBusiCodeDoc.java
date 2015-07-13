/**
 * TBusiCodeDoc.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TBusiCodeDoc -> T_BUSI_CODE_DOC mapping
 */
@POJO(tn = "T_BUSI_CODE_DOC", sn = "", pk = "")
public class TBusiCodeDoc implements Serializable {

	// TBusiCodeDoc all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6860906916743212195L;
	private String template_id;
	private String busi_code;
	private String doc_type;


	private String busi_name;
	private String doc_name;

	/**
	 * default empty constructor
	 */
	public TBusiCodeDoc() {
	}

	// template_id getter and setter
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	// busi_code getter and setter
	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	// doc_type getter and setter
	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public String getBusi_name() {
		return busi_name;
	}

	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

}