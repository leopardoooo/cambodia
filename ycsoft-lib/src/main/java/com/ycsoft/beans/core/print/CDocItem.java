/**
 * CDocItem.java	2010/04/09
 */

package com.ycsoft.beans.core.print;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CDocItem -> C_DOC_ITEM mapping
 */
@POJO(tn = "C_DOC_ITEM", sn = "", pk = "")
public class CDocItem implements Serializable {

	// CDocItem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7070551804634741428L;
	private String docitem_sn;
	private String doc_sn;
	private String printitem_id;
	private Integer amount;

	/**
	 * default empty constructor
	 */
	public CDocItem() {
	}

	// docitem_sn getter and setter
	public String getDocitem_sn() {
		return docitem_sn;
	}

	public void setDocitem_sn(String docitem_sn) {
		this.docitem_sn = docitem_sn;
	}

	// doc_sn getter and setter
	public String getDoc_sn() {
		return doc_sn;
	}

	public void setDoc_sn(String doc_sn) {
		this.doc_sn = doc_sn;
	}

	// printitem_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}

	// amount getter and setter
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}