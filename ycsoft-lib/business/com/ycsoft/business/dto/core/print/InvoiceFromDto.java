
package com.ycsoft.business.dto.core.print;

import java.util.List;

import com.ycsoft.commons.helper.StringHelper;

/**
 * @author YC-SOFT
 *
 */
public class InvoiceFromDto {

	private String doc_sn ;
	private String invoice_code ;
	private String invoice_id ;
	private String invoice_book_id ;
	private String doc_type;
	private Integer amount ;
	private String status;
	private String docitem_data ;
	private int balance = 0;

	private List<String> docSnItems;

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

	/**
	 * @return the invoice_code
	 */
	public String getInvoice_code() {
		return invoice_code;
	}

	/**
	 * @param invoice_code the invoice_code to set
	 */
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	/**
	 * @return the invoice_id
	 */
	public String getInvoice_id() {
		return invoice_id;
	}

	/**
	 * @param invoice_id the invoice_id to set
	 */
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	/**
	 * @return the docitem_data
	 */
	public String getDocitem_data() {
		return docitem_data;
	}

	/**
	 * @param docitem_data the docitem_data to set
	 */
	public void setDocitem_data(String docitem_data) {
		this.docitem_data = docitem_data;
		if(StringHelper.isNotEmpty(docitem_data) && docitem_data.contains("],")){
			String[] arr = docitem_data.split("],");
			this.balance = Integer.parseInt(arr[1]);
			this.docitem_data = arr[0]+"]";
		}
	}

	/**
	 * @return the docSnItems
	 */
	public List<String> getDocSnItems() {
		return docSnItems;
	}

	/**
	 * @param docSnItems the docSnItems to set
	 */
	public void setDocSnItems(List<String> docSnItems) {
		this.docSnItems = docSnItems;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the invoice_book_id
	 */
	public String getInvoice_book_id() {
		return invoice_book_id;
	}

	/**
	 * @param invoice_book_id the invoice_book_id to set
	 */
	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}

	public int getBalance() {
		return balance;
	}

	/**
	 * @return the doc_type
	 */
	public String getDoc_type() {
		return doc_type;
	}

	/**
	 * @param doc_type the doc_type to set
	 */
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
}
