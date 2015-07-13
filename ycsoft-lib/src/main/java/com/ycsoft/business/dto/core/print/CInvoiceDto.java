/**
 *
 */
package com.ycsoft.business.dto.core.print;

import java.util.Date;

import com.ycsoft.beans.core.print.CInvoice;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 * 
 */
public class CInvoiceDto extends CInvoice {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6741877745263226703L;
	private String doc_name;
	private String print_type;
	private String is_invoice;
	private String template_filename;
	
	private String invoice_mode;
	
	private String finance_status;//发票结消状态
	private String finance_status_text;

	private String doc_type;
	
	private String fee_invoice_id;		//正式发票号码(收费清单换发票)
	private String fee_invoice_code;	//正式发票代码
	private String fee_invoice_status;	//正式发票状态
	private Integer fee_done_code;
	
	private String fee_invoice_status_text;
	private String doc_type_text;
	private String invoice_mode_text;
	private Date fee_create_time;		//费用生成时间(取最后一条收费记录)

	public String getFee_invoice_id() {
		return fee_invoice_id;
	}

	public void setFee_invoice_id(String fee_invoice_id) {
		this.fee_invoice_id = fee_invoice_id;
	}

	public String getFee_invoice_code() {
		return fee_invoice_code;
	}

	public void setFee_invoice_code(String fee_invoice_code) {
		this.fee_invoice_code = fee_invoice_code;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getPrint_type() {
		return print_type;
	}

	public void setPrint_type(String print_type) {
		this.print_type = print_type;
	}

	public String getIs_invoice() {
		return is_invoice;
	}

	public void setIs_invoice(String is_invoice) {
		this.is_invoice = is_invoice;
	}

	public String getTemplate_filename() {
		return template_filename;
	}

	public void setTemplate_filename(String template_filename) {
		this.template_filename = template_filename;
	}

	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
		this.doc_type_text = MemoryDict.getDictName(DictKey.INVOICE_TYPE, doc_type);
	}

	/**
	 * @return the finance_status
	 */
	public String getFinance_status() {
		return finance_status;
	}

	/**
	 * @param finance_status the finance_status to set
	 */
	public void setFinance_status(String finance_status) {
		this.finance_status = finance_status;
	}

	/**
	 * @return the finance_status_text
	 */
	public String getFinance_status_text() {
		return MemoryDict.getDictName(DictKey.STATUS, finance_status);
	}

	/**
	 * @return the invoice_mode
	 */
	public String getInvoice_mode() {
		return invoice_mode;
	}

	/**
	 * @param invoice_mode the invoice_mode to set
	 */
	public void setInvoice_mode(String invoice_mode) {
		this.invoice_mode = invoice_mode;
		this.invoice_mode_text = MemoryDict.getDictName(DictKey.INVOICE_MODE, invoice_mode);
	}

	public String getFee_invoice_status() {
		return fee_invoice_status;
	}

	public void setFee_invoice_status(String fee_invoice_status) {
		this.fee_invoice_status = fee_invoice_status;
		this.fee_invoice_status_text = MemoryDict.getDictName(DictKey.STATUS, fee_invoice_status);
	}

	public String getFee_invoice_status_text() {
		return fee_invoice_status_text;
	}

	public Integer getFee_done_code() {
		return fee_done_code;
	}

	public void setFee_done_code(Integer fee_done_code) {
		this.fee_done_code = fee_done_code;
	}

	public String getDoc_type_text() {
		return doc_type_text;
	}

	public String getInvoice_mode_text() {
		return invoice_mode_text;
	}

	public Date getFee_create_time() {
		return fee_create_time;
	}

	public void setFee_create_time(Date fee_create_time) {
		this.fee_create_time = fee_create_time;
	}

}
