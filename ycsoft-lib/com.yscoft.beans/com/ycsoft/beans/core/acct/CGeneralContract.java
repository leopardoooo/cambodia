package com.ycsoft.beans.core.acct;

/**
 * CGeneralContract.java	2011/01/24
 */
 

import java.io.Serializable ;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralContract -> C_GENERAL_CONTRACT mapping 
 */
@POJO(
	tn="C_GENERAL_CONTRACT",
	sn="SEQ_CONTRACT_ID",
	pk="contract_id")
public class CGeneralContract implements Serializable {
	
	// CGeneralContract all properties 

	private Integer contract_id ;	
	private String contract_no ;	
	private String fee_id ;	
	private String g_acct_id ;	
	private String contract_name ;	
	private String cust_name ;	
	private Integer nominal_amount ;	
	private Integer user_amount ;	
	private Integer amount ;	
	private String fee_type ;	
	private String status ;	
	private String remark ;	
	private String county_id ;
	private Integer total_amount;
	private Date create_time;
	private String fee_sn;
	private String optr_id;
	private String addr_district;
	private String addr_community;
	private Integer payed_amount;
	
	private String fee_type_text;
	private Integer used_money;
	

	/**
	 * default empty constructor
	 */
	public CGeneralContract() {}
	
	
	// contract_id getter and setter
	public Integer getContract_id(){
		return this.contract_id ;
	}
	
	public void setContract_id(Integer contract_id){
		this.contract_id = contract_id ;
	}
	
	// contract_no getter and setter
	public String getContract_no(){
		return this.contract_no ;
	}
	
	public void setContract_no(String contract_no){
		this.contract_no = contract_no ;
	}
	
	// g_acct_id getter and setter
	public String getG_acct_id(){
		return this.g_acct_id ;
	}
	
	public void setG_acct_id(String g_acct_id){
		this.g_acct_id = g_acct_id ;
	}
	
	// contract_name getter and setter
	public String getContract_name(){
		return this.contract_name ;
	}
	
	public void setContract_name(String contract_name){
		this.contract_name = contract_name ;
	}
	
	// cust_name getter and setter
	public String getCust_name(){
		return this.cust_name ;
	}
	
	public void setCust_name(String cust_name){
		this.cust_name = cust_name ;
	}
	
	// nominal_amount getter and setter
	public Integer getNominal_amount(){
		return this.nominal_amount ;
	}
	
	public void setNominal_amount(Integer nominal_amount){
		this.nominal_amount = nominal_amount ;
	}
	
	// user_amount getter and setter
	public Integer getUser_amount(){
		return this.user_amount ;
	}
	
	public void setUser_amount(Integer user_amount){
		this.user_amount = user_amount ;
	}
	
	// amount getter and setter
	public Integer getAmount(){
		return this.amount ;
	}
	
	public void setAmount(Integer amount){
		this.amount = amount ;
	}
	
	// fee_type getter and setter
	public String getFee_type(){
		return this.fee_type ;
	}
	
	public void setFee_type(String fee_type){
		this.fee_type = fee_type ;
		this.fee_type_text = MemoryDict.getDictName(DictKey.FEE_TYPE, fee_type);
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

	public void setUsed_money(Integer used_money) {
		this.used_money = used_money;
	}


	public Integer getUsed_money() {
		return used_money;
	}


	public Integer getTotal_amount() {
		return total_amount;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public void setTotal_amount(Integer total_amount) {
		this.total_amount = total_amount;
	}


	public String getFee_sn() {
		return fee_sn;
	}


	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}


	public String getFee_id() {
		return fee_id;
	}


	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	
	public String getFee_type_text() {
		return fee_type_text;
	}


	public void setFee_type_text(String fee_type_text) {
		this.fee_type_text = fee_type_text;
	}


	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}


	public String getAddr_district() {
		return addr_district;
	}


	public void setAddr_district(String addr_district) {
		this.addr_district = addr_district;
	}


	public String getAddr_community() {
		return addr_community;
	}


	public void setAddr_community(String addr_community) {
		this.addr_community = addr_community;
	}


	public Integer getPayed_amount() {
		return payed_amount;
	}


	public void setPayed_amount(Integer payedAmount) {
		payed_amount = payedAmount;
	}
	
}