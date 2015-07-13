/**
 * BBill.java	2010/11/03
 */

package com.ycsoft.beans.core.bill;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * BBill -> B_BILL mapping
 */
@POJO(
	tn="B_BILL",
	sn="SEQ_BILL_SN",
	pk="bill_sn")
public class BBill implements Serializable {

	// BBill all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8943713418494308156L;
	private String bill_sn ;
	private String cust_id ;
	private String acct_id ;
	private String acctitem_id ;
	private String user_id ;
	private String serv_id ;
	private String prod_sn ;
	private String prod_id ;
	private String package_sn ;
	private String package_id ;
	private String tariff_id ;
	private String billing_cycle_id ;
	private String come_from ;
	private String status ;
	private String fee_flag ;
	private String bill_type ;
	private String bill_done_code ;
	private Date bill_date ;
	private Date eff_date ;
	private Date exp_date ;
	private Integer rent_days_fee ;
	private Integer rent_month_fee ;
	private Integer use_fee ;
	private Integer realtime_fee ;
	private Integer final_month_fee ;
	private Integer final_use_fee ;
	private Integer bill_fee ;
	private Integer final_bill_fee ;
	private Integer owe_fee ;
	private String area_id ;
	private String county_id ;
	private String prod_type ;

	/**
	 * default empty constructor
	 */
	public BBill() {}


	// bill_sn getter and setter
	public String getBill_sn(){
		return bill_sn ;
	}

	public void setBill_sn(String bill_sn){
		this.bill_sn = bill_sn ;
	}

	// cust_id getter and setter
	public String getCust_id(){
		return cust_id ;
	}

	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}

	// acct_id getter and setter
	public String getAcct_id(){
		return acct_id ;
	}

	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return acctitem_id ;
	}

	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}

	// user_id getter and setter
	public String getUser_id(){
		return user_id ;
	}

	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}

	// serv_id getter and setter
	public String getServ_id(){
		return serv_id ;
	}

	public void setServ_id(String serv_id){
		this.serv_id = serv_id ;
	}

	// prod_sn getter and setter
	public String getProd_sn(){
		return prod_sn ;
	}

	public void setProd_sn(String prod_sn){
		this.prod_sn = prod_sn ;
	}

	// prod_id getter and setter
	public String getProd_id(){
		return prod_id ;
	}

	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}

	// package_sn getter and setter
	public String getPackage_sn(){
		return package_sn ;
	}

	public void setPackage_sn(String package_sn){
		this.package_sn = package_sn ;
	}

	// package_id getter and setter
	public String getPackage_id(){
		return package_id ;
	}

	public void setPackage_id(String package_id){
		this.package_id = package_id ;
	}

	// tariff_id getter and setter
	public String getTariff_id(){
		return tariff_id ;
	}

	public void setTariff_id(String tariff_id){
		this.tariff_id = tariff_id ;
	}

	// billing_cycle_id getter and setter
	public String getBilling_cycle_id(){
		return billing_cycle_id ;
	}

	public void setBilling_cycle_id(String billing_cycle_id){
		this.billing_cycle_id = billing_cycle_id ;
	}

	// come_from getter and setter
	public String getCome_from(){
		return come_from ;
	}

	public void setCome_from(String come_from){
		this.come_from = come_from ;
	}

	// status getter and setter
	public String getStatus(){
		return status ;
	}

	public void setStatus(String status){
		this.status = status ;
	}

	// fee_flag getter and setter
	public String getFee_flag(){
		return fee_flag ;
	}

	public void setFee_flag(String fee_flag){
		this.fee_flag = fee_flag ;
	}

	// bill_type getter and setter
	public String getBill_type(){
		return bill_type ;
	}

	public void setBill_type(String bill_type){
		this.bill_type = bill_type ;
	}

	// bill_done_code getter and setter
	public String getBill_done_code(){
		return bill_done_code ;
	}

	public void setBill_done_code(String bill_done_code){
		this.bill_done_code = bill_done_code ;
	}

	// bill_date getter and setter
	public Date getBill_date(){
		return bill_date ;
	}

	public void setBill_date(Date bill_date){
		this.bill_date = bill_date ;
	}

	// eff_date getter and setter
	public Date getEff_date(){
		return eff_date ;
	}

	public void setEff_date(Date eff_date){
		this.eff_date = eff_date ;
	}

	// exp_date getter and setter
	public Date getExp_date(){
		return exp_date ;
	}

	public void setExp_date(Date exp_date){
		this.exp_date = exp_date ;
	}

	// rent_days_fee getter and setter
	public Integer getRent_days_fee(){
		return rent_days_fee ;
	}

	public void setRent_days_fee(Integer rent_days_fee){
		this.rent_days_fee = rent_days_fee ;
	}

	// rent_month_fee getter and setter
	public Integer getRent_month_fee(){
		return rent_month_fee ;
	}

	public void setRent_month_fee(Integer rent_month_fee){
		this.rent_month_fee = rent_month_fee ;
	}

	// use_fee getter and setter
	public Integer getUse_fee(){
		return use_fee ;
	}

	public void setUse_fee(Integer use_fee){
		this.use_fee = use_fee ;
	}

	// realtime_fee getter and setter
	public Integer getRealtime_fee(){
		return realtime_fee ;
	}

	public void setRealtime_fee(Integer realtime_fee){
		this.realtime_fee = realtime_fee ;
	}

	// final_month_fee getter and setter
	public Integer getFinal_month_fee(){
		return final_month_fee ;
	}

	public void setFinal_month_fee(Integer final_month_fee){
		this.final_month_fee = final_month_fee ;
	}

	// final_use_fee getter and setter
	public Integer getFinal_use_fee(){
		return final_use_fee ;
	}

	public void setFinal_use_fee(Integer final_use_fee){
		this.final_use_fee = final_use_fee ;
	}

	// bill_fee getter and setter
	public Integer getBill_fee(){
		return bill_fee ;
	}

	public void setBill_fee(Integer bill_fee){
		this.bill_fee = bill_fee ;
	}

	// final_bill_fee getter and setter
	public Integer getFinal_bill_fee(){
		return final_bill_fee ;
	}

	public void setFinal_bill_fee(Integer final_bill_fee){
		this.final_bill_fee = final_bill_fee ;
	}

	// owe_fee getter and setter
	public Integer getOwe_fee(){
		return owe_fee ;
	}

	public void setOwe_fee(Integer owe_fee){
		this.owe_fee = owe_fee ;
	}

	// area_id getter and setter
	public String getArea_id(){
		return area_id ;
	}

	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

	// prod_type getter and setter
	public String getProd_type(){
		return prod_type ;
	}

	public void setProd_type(String prod_type){
		this.prod_type = prod_type ;
	}

}