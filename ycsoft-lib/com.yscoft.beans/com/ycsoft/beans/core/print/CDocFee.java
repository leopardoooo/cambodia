/**
 * CDocFee.java	2010/04/09
 */

package com.ycsoft.beans.core.print;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CDocFee -> C_DOC_FEE mapping
 */
@POJO(tn = "C_DOC_FEE", sn = "", pk = "")
public class CDocFee implements Serializable {

	// CDocFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -983984196386722323L;
	private String doc_sn;
	private String docitem_sn;
	private String fee_sn;

	/**
	 * default empty constructor
	 */
	public CDocFee() {
	}

	public CDocFee(String doc_sn,String docitem_sn, String fee_sn) {
		this.docitem_sn = docitem_sn;
		this.fee_sn = fee_sn;
		this.doc_sn = doc_sn;
	}

	// docitem_sn getter and setter
	public String getDocitem_sn() {
		return docitem_sn;
	}

	public void setDocitem_sn(String docitem_sn) {
		this.docitem_sn = docitem_sn;
	}

	// fee_sn getter and setter
	public String getFee_sn() {
		return fee_sn;
	}

	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}

	/**
	 * @return the doc_sn
	 */
	public String getDoc_sn() {
		return doc_sn;
	}

	/**
	 * @param doc_sn the doc_sn to set
	 */
	public void setDoc_sn(String doc_sn) {
		this.doc_sn = doc_sn;
	}

}