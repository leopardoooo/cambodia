/**
 * BRentfee.java	2012/12/19
 */
 
package com.ycsoft.beans.core.bill; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * BRentfee -> B_RENTFEE mapping 
 */
@POJO(
	tn="B_RENTFEE",
	sn="",
	pk="")
public class BRentfee implements Serializable {
	
	// BRentfee all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723262710184503089L;
	private String billing_cycle_id ;	
	private String cust_id ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private String user_id ;	
	private String prod_sn ;	
	private String prod_id ;	
	private String serv_id ;	
	private String package_sn ;	
	private String package_id ;	
	private String tariff_id ;	
	private Date open_date ;	
	private Date eff_date ;	
	private Date exp_date ;	
	private Date done_date ;	
	private Integer day_fee ;	
	private Integer day_disct_fee ;	
	private Integer month_fee ;	
	private Integer month_disct_fee ;	
	private Integer rent_days ;	
	private Integer rent_free_days ;	
	private Integer rent_fee_days ;	
	private String status ;	
	private String fee_flag ;	
	private Date rent_date ;	
	private String area_id ;	
	private String county_id ;	
	private Integer process_flag ;	
	private Date process_time ;	
	private String free_sn ;	
	private String prod_type ;	
	private String status1 ;	
	private String done1 ;	
	private String status2 ;	
	private String done2 ;	
	private String status3 ;	
	private String done3 ;	
	private String status4 ;	
	private String done4 ;	
	private String status5 ;	
	private String done5 ;	
	private String status6 ;	
	private String done6 ;	
	private String status7 ;	
	private String done7 ;	
	private String status8 ;	
	private String done8 ;	
	private String status9 ;	
	private String done9 ;	
	private String status10 ;	
	private String done10 ;	
	private String status11 ;	
	private String done11 ;	
	private String status12 ;	
	private String done12 ;	
	private String status13 ;	
	private String done13 ;	
	private String status14 ;	
	private String done14 ;	
	private String status15 ;	
	private String done15 ;	
	private String status16 ;	
	private String done16 ;	
	private String status17 ;	
	private String done17 ;	
	private String status18 ;	
	private String done18 ;	
	private String status19 ;	
	private String done19 ;	
	private String status20 ;	
	private String done20 ;	
	private String status21 ;	
	private String done21 ;	
	private String status22 ;	
	private String done22 ;	
	private String status23 ;	
	private String done23 ;	
	private String status24 ;	
	private String done24 ;	
	private String status25 ;	
	private String done25 ;	
	private String status26 ;	
	private String done26 ;	
	private String status27 ;	
	private String done27 ;	
	private String status28 ;	
	private String done28 ;	
	private String status29 ;	
	private String done29 ;	
	private String status30 ;	
	private String done30 ;	
	private String status31 ;	
	private String done31 ;	
	private Integer zy_rent_fee_days ;	
	private Integer zy_month_fee ;	
	
	/**
	 * default empty constructor
	 */
	public BRentfee() {}
	
	
	// billing_cycle_id getter and setter
	public String getBilling_cycle_id(){
		return this.billing_cycle_id ;
	}
	
	public void setBilling_cycle_id(String billing_cycle_id){
		this.billing_cycle_id = billing_cycle_id ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return this.acctitem_id ;
	}
	
	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// prod_sn getter and setter
	public String getProd_sn(){
		return this.prod_sn ;
	}
	
	public void setProd_sn(String prod_sn){
		this.prod_sn = prod_sn ;
	}
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// serv_id getter and setter
	public String getServ_id(){
		return this.serv_id ;
	}
	
	public void setServ_id(String serv_id){
		this.serv_id = serv_id ;
	}
	
	// package_sn getter and setter
	public String getPackage_sn(){
		return this.package_sn ;
	}
	
	public void setPackage_sn(String package_sn){
		this.package_sn = package_sn ;
	}
	
	// package_id getter and setter
	public String getPackage_id(){
		return this.package_id ;
	}
	
	public void setPackage_id(String package_id){
		this.package_id = package_id ;
	}
	
	// tariff_id getter and setter
	public String getTariff_id(){
		return this.tariff_id ;
	}
	
	public void setTariff_id(String tariff_id){
		this.tariff_id = tariff_id ;
	}
	
	// open_date getter and setter
	public Date getOpen_date(){
		return this.open_date ;
	}
	
	public void setOpen_date(Date open_date){
		this.open_date = open_date ;
	}
	
	// eff_date getter and setter
	public Date getEff_date(){
		return this.eff_date ;
	}
	
	public void setEff_date(Date eff_date){
		this.eff_date = eff_date ;
	}
	
	// exp_date getter and setter
	public Date getExp_date(){
		return this.exp_date ;
	}
	
	public void setExp_date(Date exp_date){
		this.exp_date = exp_date ;
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// day_fee getter and setter
	public Integer getDay_fee(){
		return this.day_fee ;
	}
	
	public void setDay_fee(Integer day_fee){
		this.day_fee = day_fee ;
	}
	
	// day_disct_fee getter and setter
	public Integer getDay_disct_fee(){
		return this.day_disct_fee ;
	}
	
	public void setDay_disct_fee(Integer day_disct_fee){
		this.day_disct_fee = day_disct_fee ;
	}
	
	// month_fee getter and setter
	public Integer getMonth_fee(){
		return this.month_fee ;
	}
	
	public void setMonth_fee(Integer month_fee){
		this.month_fee = month_fee ;
	}
	
	// month_disct_fee getter and setter
	public Integer getMonth_disct_fee(){
		return this.month_disct_fee ;
	}
	
	public void setMonth_disct_fee(Integer month_disct_fee){
		this.month_disct_fee = month_disct_fee ;
	}
	
	// rent_days getter and setter
	public Integer getRent_days(){
		return this.rent_days ;
	}
	
	public void setRent_days(Integer rent_days){
		this.rent_days = rent_days ;
	}
	
	// rent_free_days getter and setter
	public Integer getRent_free_days(){
		return this.rent_free_days ;
	}
	
	public void setRent_free_days(Integer rent_free_days){
		this.rent_free_days = rent_free_days ;
	}
	
	// rent_fee_days getter and setter
	public Integer getRent_fee_days(){
		return this.rent_fee_days ;
	}
	
	public void setRent_fee_days(Integer rent_fee_days){
		this.rent_fee_days = rent_fee_days ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// fee_flag getter and setter
	public String getFee_flag(){
		return this.fee_flag ;
	}
	
	public void setFee_flag(String fee_flag){
		this.fee_flag = fee_flag ;
	}
	
	// rent_date getter and setter
	public Date getRent_date(){
		return this.rent_date ;
	}
	
	public void setRent_date(Date rent_date){
		this.rent_date = rent_date ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	
	// process_flag getter and setter
	public Integer getProcess_flag(){
		return this.process_flag ;
	}
	
	public void setProcess_flag(Integer process_flag){
		this.process_flag = process_flag ;
	}
	
	// process_time getter and setter
	public Date getProcess_time(){
		return this.process_time ;
	}
	
	public void setProcess_time(Date process_time){
		this.process_time = process_time ;
	}
	
	// free_sn getter and setter
	public String getFree_sn(){
		return this.free_sn ;
	}
	
	public void setFree_sn(String free_sn){
		this.free_sn = free_sn ;
	}
	
	// prod_type getter and setter
	public String getProd_type(){
		return this.prod_type ;
	}
	
	public void setProd_type(String prod_type){
		this.prod_type = prod_type ;
	}
	
	// status1 getter and setter
	public String getStatus1(){
		return this.status1 ;
	}
	
	public void setStatus1(String status1){
		this.status1 = status1 ;
	}
	
	// done1 getter and setter
	public String getDone1(){
		return this.done1 ;
	}
	
	public void setDone1(String done1){
		this.done1 = done1 ;
	}
	
	// status2 getter and setter
	public String getStatus2(){
		return this.status2 ;
	}
	
	public void setStatus2(String status2){
		this.status2 = status2 ;
	}
	
	// done2 getter and setter
	public String getDone2(){
		return this.done2 ;
	}
	
	public void setDone2(String done2){
		this.done2 = done2 ;
	}
	
	// status3 getter and setter
	public String getStatus3(){
		return this.status3 ;
	}
	
	public void setStatus3(String status3){
		this.status3 = status3 ;
	}
	
	// done3 getter and setter
	public String getDone3(){
		return this.done3 ;
	}
	
	public void setDone3(String done3){
		this.done3 = done3 ;
	}
	
	// status4 getter and setter
	public String getStatus4(){
		return this.status4 ;
	}
	
	public void setStatus4(String status4){
		this.status4 = status4 ;
	}
	
	// done4 getter and setter
	public String getDone4(){
		return this.done4 ;
	}
	
	public void setDone4(String done4){
		this.done4 = done4 ;
	}
	
	// status5 getter and setter
	public String getStatus5(){
		return this.status5 ;
	}
	
	public void setStatus5(String status5){
		this.status5 = status5 ;
	}
	
	// done5 getter and setter
	public String getDone5(){
		return this.done5 ;
	}
	
	public void setDone5(String done5){
		this.done5 = done5 ;
	}
	
	// status6 getter and setter
	public String getStatus6(){
		return this.status6 ;
	}
	
	public void setStatus6(String status6){
		this.status6 = status6 ;
	}
	
	// done6 getter and setter
	public String getDone6(){
		return this.done6 ;
	}
	
	public void setDone6(String done6){
		this.done6 = done6 ;
	}
	
	// status7 getter and setter
	public String getStatus7(){
		return this.status7 ;
	}
	
	public void setStatus7(String status7){
		this.status7 = status7 ;
	}
	
	// done7 getter and setter
	public String getDone7(){
		return this.done7 ;
	}
	
	public void setDone7(String done7){
		this.done7 = done7 ;
	}
	
	// status8 getter and setter
	public String getStatus8(){
		return this.status8 ;
	}
	
	public void setStatus8(String status8){
		this.status8 = status8 ;
	}
	
	// done8 getter and setter
	public String getDone8(){
		return this.done8 ;
	}
	
	public void setDone8(String done8){
		this.done8 = done8 ;
	}
	
	// status9 getter and setter
	public String getStatus9(){
		return this.status9 ;
	}
	
	public void setStatus9(String status9){
		this.status9 = status9 ;
	}
	
	// done9 getter and setter
	public String getDone9(){
		return this.done9 ;
	}
	
	public void setDone9(String done9){
		this.done9 = done9 ;
	}
	
	// status10 getter and setter
	public String getStatus10(){
		return this.status10 ;
	}
	
	public void setStatus10(String status10){
		this.status10 = status10 ;
	}
	
	// done10 getter and setter
	public String getDone10(){
		return this.done10 ;
	}
	
	public void setDone10(String done10){
		this.done10 = done10 ;
	}
	
	// status11 getter and setter
	public String getStatus11(){
		return this.status11 ;
	}
	
	public void setStatus11(String status11){
		this.status11 = status11 ;
	}
	
	// done11 getter and setter
	public String getDone11(){
		return this.done11 ;
	}
	
	public void setDone11(String done11){
		this.done11 = done11 ;
	}
	
	// status12 getter and setter
	public String getStatus12(){
		return this.status12 ;
	}
	
	public void setStatus12(String status12){
		this.status12 = status12 ;
	}
	
	// done12 getter and setter
	public String getDone12(){
		return this.done12 ;
	}
	
	public void setDone12(String done12){
		this.done12 = done12 ;
	}
	
	// status13 getter and setter
	public String getStatus13(){
		return this.status13 ;
	}
	
	public void setStatus13(String status13){
		this.status13 = status13 ;
	}
	
	// done13 getter and setter
	public String getDone13(){
		return this.done13 ;
	}
	
	public void setDone13(String done13){
		this.done13 = done13 ;
	}
	
	// status14 getter and setter
	public String getStatus14(){
		return this.status14 ;
	}
	
	public void setStatus14(String status14){
		this.status14 = status14 ;
	}
	
	// done14 getter and setter
	public String getDone14(){
		return this.done14 ;
	}
	
	public void setDone14(String done14){
		this.done14 = done14 ;
	}
	
	// status15 getter and setter
	public String getStatus15(){
		return this.status15 ;
	}
	
	public void setStatus15(String status15){
		this.status15 = status15 ;
	}
	
	// done15 getter and setter
	public String getDone15(){
		return this.done15 ;
	}
	
	public void setDone15(String done15){
		this.done15 = done15 ;
	}
	
	// status16 getter and setter
	public String getStatus16(){
		return this.status16 ;
	}
	
	public void setStatus16(String status16){
		this.status16 = status16 ;
	}
	
	// done16 getter and setter
	public String getDone16(){
		return this.done16 ;
	}
	
	public void setDone16(String done16){
		this.done16 = done16 ;
	}
	
	// status17 getter and setter
	public String getStatus17(){
		return this.status17 ;
	}
	
	public void setStatus17(String status17){
		this.status17 = status17 ;
	}
	
	// done17 getter and setter
	public String getDone17(){
		return this.done17 ;
	}
	
	public void setDone17(String done17){
		this.done17 = done17 ;
	}
	
	// status18 getter and setter
	public String getStatus18(){
		return this.status18 ;
	}
	
	public void setStatus18(String status18){
		this.status18 = status18 ;
	}
	
	// done18 getter and setter
	public String getDone18(){
		return this.done18 ;
	}
	
	public void setDone18(String done18){
		this.done18 = done18 ;
	}
	
	// status19 getter and setter
	public String getStatus19(){
		return this.status19 ;
	}
	
	public void setStatus19(String status19){
		this.status19 = status19 ;
	}
	
	// done19 getter and setter
	public String getDone19(){
		return this.done19 ;
	}
	
	public void setDone19(String done19){
		this.done19 = done19 ;
	}
	
	// status20 getter and setter
	public String getStatus20(){
		return this.status20 ;
	}
	
	public void setStatus20(String status20){
		this.status20 = status20 ;
	}
	
	// done20 getter and setter
	public String getDone20(){
		return this.done20 ;
	}
	
	public void setDone20(String done20){
		this.done20 = done20 ;
	}
	
	// status21 getter and setter
	public String getStatus21(){
		return this.status21 ;
	}
	
	public void setStatus21(String status21){
		this.status21 = status21 ;
	}
	
	// done21 getter and setter
	public String getDone21(){
		return this.done21 ;
	}
	
	public void setDone21(String done21){
		this.done21 = done21 ;
	}
	
	// status22 getter and setter
	public String getStatus22(){
		return this.status22 ;
	}
	
	public void setStatus22(String status22){
		this.status22 = status22 ;
	}
	
	// done22 getter and setter
	public String getDone22(){
		return this.done22 ;
	}
	
	public void setDone22(String done22){
		this.done22 = done22 ;
	}
	
	// status23 getter and setter
	public String getStatus23(){
		return this.status23 ;
	}
	
	public void setStatus23(String status23){
		this.status23 = status23 ;
	}
	
	// done23 getter and setter
	public String getDone23(){
		return this.done23 ;
	}
	
	public void setDone23(String done23){
		this.done23 = done23 ;
	}
	
	// status24 getter and setter
	public String getStatus24(){
		return this.status24 ;
	}
	
	public void setStatus24(String status24){
		this.status24 = status24 ;
	}
	
	// done24 getter and setter
	public String getDone24(){
		return this.done24 ;
	}
	
	public void setDone24(String done24){
		this.done24 = done24 ;
	}
	
	// status25 getter and setter
	public String getStatus25(){
		return this.status25 ;
	}
	
	public void setStatus25(String status25){
		this.status25 = status25 ;
	}
	
	// done25 getter and setter
	public String getDone25(){
		return this.done25 ;
	}
	
	public void setDone25(String done25){
		this.done25 = done25 ;
	}
	
	// status26 getter and setter
	public String getStatus26(){
		return this.status26 ;
	}
	
	public void setStatus26(String status26){
		this.status26 = status26 ;
	}
	
	// done26 getter and setter
	public String getDone26(){
		return this.done26 ;
	}
	
	public void setDone26(String done26){
		this.done26 = done26 ;
	}
	
	// status27 getter and setter
	public String getStatus27(){
		return this.status27 ;
	}
	
	public void setStatus27(String status27){
		this.status27 = status27 ;
	}
	
	// done27 getter and setter
	public String getDone27(){
		return this.done27 ;
	}
	
	public void setDone27(String done27){
		this.done27 = done27 ;
	}
	
	// status28 getter and setter
	public String getStatus28(){
		return this.status28 ;
	}
	
	public void setStatus28(String status28){
		this.status28 = status28 ;
	}
	
	// done28 getter and setter
	public String getDone28(){
		return this.done28 ;
	}
	
	public void setDone28(String done28){
		this.done28 = done28 ;
	}
	
	// status29 getter and setter
	public String getStatus29(){
		return this.status29 ;
	}
	
	public void setStatus29(String status29){
		this.status29 = status29 ;
	}
	
	// done29 getter and setter
	public String getDone29(){
		return this.done29 ;
	}
	
	public void setDone29(String done29){
		this.done29 = done29 ;
	}
	
	// status30 getter and setter
	public String getStatus30(){
		return this.status30 ;
	}
	
	public void setStatus30(String status30){
		this.status30 = status30 ;
	}
	
	// done30 getter and setter
	public String getDone30(){
		return this.done30 ;
	}
	
	public void setDone30(String done30){
		this.done30 = done30 ;
	}
	
	// status31 getter and setter
	public String getStatus31(){
		return this.status31 ;
	}
	
	public void setStatus31(String status31){
		this.status31 = status31 ;
	}
	
	// done31 getter and setter
	public String getDone31(){
		return this.done31 ;
	}
	
	public void setDone31(String done31){
		this.done31 = done31 ;
	}
	
	// zy_rent_fee_days getter and setter
	public Integer getZy_rent_fee_days(){
		return this.zy_rent_fee_days ;
	}
	
	public void setZy_rent_fee_days(Integer zy_rent_fee_days){
		this.zy_rent_fee_days = zy_rent_fee_days ;
	}
	
	// zy_month_fee getter and setter
	public Integer getZy_month_fee(){
		return this.zy_month_fee ;
	}
	
	public void setZy_month_fee(Integer zy_month_fee){
		this.zy_month_fee = zy_month_fee ;
	}

}