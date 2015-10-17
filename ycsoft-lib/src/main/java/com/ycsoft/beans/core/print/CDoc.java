/**
 * CDoc.java	2010/04/09
 */

package com.ycsoft.beans.core.print;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CDoc -> C_DOC mapping
 */
@POJO(tn = "C_DOC", sn = "", pk = "DOC_SN")
public class CDoc extends BusiBase implements Serializable {

	// CDoc all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256310634360743344L;
	private String cust_id;
	private String doc_sn;
	private String doc_type;


	private String doc_type_name;

	/**
	 * default empty constructor
	 */
	public CDoc() {
	}

	public CDoc(String docSn, String custId, Integer doneCode, String busiCode,
			String docType) {
		doc_sn = docSn;
		cust_id = custId;
		setDone_code(doneCode);
		setBusi_code(busiCode);
		doc_type = docType;
		setCreate_time(DateHelper.now());
	}

	// doc_sn getter and setter
	public String getDoc_sn() {
		return doc_sn;
	}

	public void setDoc_sn(String doc_sn) {
		this.doc_sn = doc_sn;
	}

	// doc_type getter and setter
	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
		doc_type_name = MemoryDict.getDictName(DictKey.INVOICE_TYPE, this.doc_type);
	}

	/**
	 * @return the doc_type_name
	 */
	public String getDoc_type_name() {
		return doc_type_name;
	}

	/**
	 * @param doc_type_name
	 *            the doc_type_name to set
	 */
	public void setDoc_type_name(String doc_type_name) {
		this.doc_type_name = doc_type_name;
	}

	/**
	 * @return the cust_id
	 */
	public String getCust_id() {
		return cust_id;
	}

	/**
	 * @param cust_id
	 *            the cust_id to set
	 */
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
}