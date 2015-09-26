/**
 * RInvoice.java	2010/11/08
 */

package com.ycsoft.beans.invoice;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * RInvoice -> R_INVOICE mapping
 */
@POJO(
	tn="R_INVOICE",
	sn="",
	pk="")
public class RInvoice implements Serializable {

	// RInvoice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4916719159313046116L;
	private String invoice_id ;
	private String invoice_code ;
	private String invoice_type ;
	private String status ;
	private Integer amount ;
	private String invoice_mode ;
	private String finance_status ;
	private String depot_id ;
	private String invoice_book_id ;
	private Date create_time;
	private Date use_time;
	private Date check_time;
	private Date close_time;
	private String check_depot_id;
	private Integer invoice_amount;
	private String optr_id;
	private String is_loss;
	private String open_optr_id;

	private String check_depot_id_text;
	private String invoice_type_text;
	private String status_text;
	private String finance_status_text;
	private String depot_name;
	private String invoice_mode_text ;
	private String optr_name;
	private String is_loss_text;
	private String open_optr_name;
	

	public Integer getInvoice_amount() {
		return invoice_amount;
	}


	public void setInvoice_amount(Integer invoice_amount) {
		this.invoice_amount = invoice_amount;
	}


	/**
	 * default empty constructor
	 */
	public RInvoice() {}


	// invoice_id getter and setter
	public String getInvoice_id(){
		return invoice_id ;
	}

	public void setInvoice_id(String invoice_id){
		this.invoice_id = invoice_id ;
	}

	// invoice_code getter and setter
	public String getInvoice_code(){
		return invoice_code ;
	}

	public void setInvoice_code(String invoice_code){
		this.invoice_code = invoice_code ;
	}

	// invoice_type getter and setter
	public String getInvoice_type(){
		return invoice_type ;
	}

	public void setInvoice_type(String invoice_type){
		this.invoice_type = invoice_type ;
		invoice_type_text = MemoryDict.getDictName(DictKey.INVOICE_TYPE, invoice_type);
	}

	// status getter and setter
	public String getStatus(){
		return status ;
	}

	public void setStatus(String status){
		this.status = status ;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// amount getter and setter
	public Integer getAmount(){
		return amount ;
	}

	public void setAmount(Integer amount){
		this.amount = amount ;
	}

	// invoice_mode getter and setter
	public String getInvoice_mode(){
		return invoice_mode ;
	}

	public void setInvoice_mode(String invoice_mode){
		invoice_mode_text = MemoryDict.getDictName(DictKey.INVOICE_MODE, invoice_mode);
		this.invoice_mode = invoice_mode ;
	}

	// finance_status getter and setter
	public String getFinance_status(){
		return finance_status ;
	}

	public void setFinance_status(String finance_status){
		this.finance_status = finance_status ;
		finance_status_text = MemoryDict.getDictName(DictKey.STATUS, finance_status);
	}

	// depot_id getter and setter
	public String getDepot_id(){
		return depot_id ;
	}

	public void setDepot_id(String depot_id){
		this.depot_id = depot_id ;
		depot_name = MemoryDict.getDictName(DictKey.DEPOT, depot_id);
	}

	// invoice_book_id getter and setter
	public String getInvoice_book_id(){
		return invoice_book_id ;
	}

	public void setInvoice_book_id(String invoice_book_id){
		this.invoice_book_id = invoice_book_id ;
	}

	public String getInvoice_type_text() {
		return invoice_type_text;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public Date getUse_time() {
		return use_time;
	}


	public void setUse_time(Date use_time) {
		this.use_time = use_time;
	}


	public Date getCheck_time() {
		return check_time;
	}


	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}


	public Date getClose_time() {
		return close_time;
	}


	public void setClose_time(Date close_time) {
		this.close_time = close_time;
	}


	public String getCheck_depot_id() {
		return check_depot_id;
	}


	public void setCheck_depot_id(String check_depot_id) {
		this.check_depot_id = check_depot_id;
		check_depot_id_text = MemoryDict.getDictName(DictKey.DEPOT, check_depot_id);
	}


	public String getStatus_text() {
		return status_text;
	}


	public String getFinance_status_text() {
		return finance_status_text;
	}


	public String getDepot_name() {
		return depot_name;
	}


	/**
	 * @return the invoice_mode_text
	 */
	public String getInvoice_mode_text() {
		return invoice_mode_text;
	}


	public String getCheck_depot_id_text() {
		return check_depot_id_text;
	}


	public void setCheck_depot_id_text(String checkDepotIdText) {
		check_depot_id_text = checkDepotIdText;
	}


	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
		setOptr_name(MemoryDict.getDictName(DictKey.OPTR, optr_id) );
	}


	public String getOptr_name() {
		return optr_name;
	}

	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}


	public String getIs_loss() {
		return is_loss;
	}


	public void setIs_loss(String is_loss) {
		this.is_loss = is_loss;
		this.is_loss_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_loss);
	}


	public String getIs_loss_text() {
		return is_loss_text;
	}
	
	public String getOpen_optr_id() {
		return open_optr_id;
	}


	public void setOpen_optr_id(String open_optr_id) {
		this.open_optr_id = open_optr_id;
		this.open_optr_name= MemoryDict.getDictName(DictKey.OPTR, open_optr_id);
	}
	
	public String getOpen_optr_name() {
		return open_optr_name;
	}


}