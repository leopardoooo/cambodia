package com.ycsoft.sysmanager.dto.resource.invoice;

import java.util.List;

import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class InvoiceDto extends RInvoice{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7594047219325386448L;
	
	private String start_invoice_id;//开始发票号
	private String end_invoice_id;//结束发票号
	private String start_invoice_book;//发票开始本号
	private String end_invoice_book;//发票结束本号
	private Integer invoice_book_count;//发票本数
	private Integer invoice_count;//发票张数
	private Integer invoice_use_count;//可使用的发票数
	
	private String start_input_time;//入库时间
	private String end_input_time;
	private String start_close_time;//核销时间
	private String end_close_time;
	private String start_check_time;//结账时间
	private String end_check_time;
	private String start_use_time;//开票时间
	private String end_use_time;
	
	private String optrids;
	
	private String user_optr_id;
	private String user_optr_name;
	private String cust_id;
	private String cust_name;
	private String county_id;
	private String county_id_text;
	
	private List<InvoiceDetailDto> invoiceDetailList;
	private List<InvoiceDepotDto>  invoiceDepotList;
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		county_id_text = MemoryDict.getDictName(DictKey.COUNTY, county_id);
		this.county_id = county_id;
	}
	public String getCounty_id_text() {
		return county_id_text;
	}
	public String getStart_invoice_id() {
		return start_invoice_id;
	}
	public void setStart_invoice_id(String start_invoice_id) {
		this.start_invoice_id = start_invoice_id;
	}
	public String getEnd_invoice_id() {
		return end_invoice_id;
	}
	public void setEnd_invoice_id(String end_invoice_id) {
		this.end_invoice_id = end_invoice_id;
	}
	public Integer getInvoice_book_count() {
		return invoice_book_count;
	}
	public void setInvoice_book_count(Integer invoice_book_count) {
		this.invoice_book_count = invoice_book_count;
	}
	public Integer getInvoice_count() {
		return invoice_count;
	}
	public void setInvoice_count(Integer invoice_count) {
		this.invoice_count = invoice_count;
	}
	public Integer getInvoice_use_count() {
		return invoice_use_count;
	}
	public void setInvoice_use_count(Integer invoice_use_count) {
		this.invoice_use_count = invoice_use_count;
	}

	public String getEnd_invoice_book() {
		return end_invoice_book;
	}
	public void setEnd_invoice_book(String end_invoice_book) {
		this.end_invoice_book = end_invoice_book;
	}
	public String getStart_invoice_book() {
		return start_invoice_book;
	}
	public void setStart_invoice_book(String start_invoice_book) {
		this.start_invoice_book = start_invoice_book;
	}
	public String getStart_input_time() {
		return start_input_time;
	}
	public void setStart_input_time(String start_input_time) {
		this.start_input_time = start_input_time;
	}
	public String getEnd_input_time() {
		return end_input_time;
	}
	public void setEnd_input_time(String end_input_time) {
		this.end_input_time = end_input_time;
	}
	public String getStart_close_time() {
		return start_close_time;
	}
	public void setStart_close_time(String start_close_time) {
		this.start_close_time = start_close_time;
	}
	public String getEnd_close_time() {
		return end_close_time;
	}
	public void setEnd_close_time(String end_close_time) {
		this.end_close_time = end_close_time;
	}
	public String getStart_check_time() {
		return start_check_time;
	}
	public void setStart_check_time(String start_check_time) {
		this.start_check_time = start_check_time;
	}
	public String getEnd_check_time() {
		return end_check_time;
	}
	public void setEnd_check_time(String end_check_time) {
		this.end_check_time = end_check_time;
	}
	public String getStart_use_time() {
		return start_use_time;
	}
	public void setStart_use_time(String start_use_time) {
		this.start_use_time = start_use_time;
	}
	public String getEnd_use_time() {
		return end_use_time;
	}
	public void setEnd_use_time(String end_use_time) {
		this.end_use_time = end_use_time;
	}
	public List<InvoiceDetailDto> getInvoiceDetailList() {
		return invoiceDetailList;
	}
	public void setInvoiceDetailList(List<InvoiceDetailDto> invoiceDetailList) {
		this.invoiceDetailList = invoiceDetailList;
	}
	public List<InvoiceDepotDto> getInvoiceDepotList() {
		return invoiceDepotList;
	}
	public void setInvoiceDepotList(List<InvoiceDepotDto> invoiceDepotList) {
		this.invoiceDepotList = invoiceDepotList;
	}
	public String getOptrids() {
		return optrids;
	}
	public void setOptrids(String optrids) {
		this.optrids = optrids;
	}
	/**
	 * @return the user_optr_id
	 */
	public String getUser_optr_id() {
		return user_optr_id;
	
	}
	/**
	 * @param user_optr_id the user_optr_id to set
	 */
	public void setUser_optr_id(String user_optr_id) {
		this.user_optr_id = user_optr_id;
		this.user_optr_name = MemoryDict.getDictName(DictKey.OPTR, user_optr_id);
	}
	/**
	 * @return the user_optr_name
	 */
	public String getUser_optr_name() {
		return user_optr_name;
	}
	/**
	 * @param user_optr_name the user_optr_name to set
	 */
	public void setUser_optr_name(String user_optr_name) {
		this.user_optr_name = user_optr_name;
		
	}

}
