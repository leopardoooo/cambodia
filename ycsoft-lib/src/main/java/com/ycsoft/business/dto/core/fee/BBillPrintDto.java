package com.ycsoft.business.dto.core.fee;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.core.bill.BillDto;

public class BBillPrintDto {
	private String billing_cycle_id ;	
	private String cust_id ;	
	private Integer month_begin_fee ;	
	private Integer month_end_fee ;	
	private Integer month_sum_fee ;	
	private Integer zs_fee ;	
	private Integer xj_fee ;	
	private Integer tk_fee ;	
	private Integer other_fee ;	
	private Integer bk_fee ;	
	private Integer prod_bill_fee;
	private Integer fee_judge;
	private String remark ;	
	private Integer str1 ;	
	private Integer str2 ;	
	private Integer str3 ;	
	private Integer str4 ;	
	private Integer str5 ;	
	private String str6 ;	
	private String str7 ;	
	private String str8 ;	
	private String str9 ;	
	private String str10 ;	
	private Date create_time ;	
	
	private List<BillDto> billList;
	
	/**
	 * default empty constructor
	 */
	public BBillPrintDto() {
	}
	
	
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
	
	// month_begin_fee getter and setter
	public Integer getMonth_begin_fee(){
		return this.month_begin_fee ;
	}
	
	public void setMonth_begin_fee(Integer month_begin_fee){
		this.month_begin_fee = month_begin_fee ;
	}
	
	// month_end_fee getter and setter
	public Integer getMonth_end_fee(){
		return this.month_end_fee ;
	}
	
	public void setMonth_end_fee(Integer month_end_fee){
		this.month_end_fee = month_end_fee ;
	}
	
	// month_sum_fee getter and setter
	public Integer getMonth_sum_fee(){
		return this.month_sum_fee ;
	}
	
	public void setMonth_sum_fee(Integer month_sum_fee){
		this.month_sum_fee = month_sum_fee ;
	}
	
	// zs_fee getter and setter
	public Integer getZs_fee(){
		return this.zs_fee ;
	}
	
	public void setZs_fee(Integer zs_fee){
		this.zs_fee = zs_fee ;
	}
	
	// xj_fee getter and setter
	public Integer getXj_fee(){
		return this.xj_fee ;
	}
	
	public void setXj_fee(Integer xj_fee){
		this.xj_fee = xj_fee ;
	}
	
	// tk_fee getter and setter
	public Integer getTk_fee(){
		return this.tk_fee ;
	}
	
	public void setTk_fee(Integer tk_fee){
		this.tk_fee = tk_fee ;
	}
	
	// other_fee getter and setter
	public Integer getOther_fee(){
		return this.other_fee ;
	}
	
	public void setOther_fee(Integer other_fee){
		this.other_fee = other_fee ;
	}
	
	// bk_fee getter and setter
	public Integer getBk_fee(){
		return this.bk_fee ;
	}
	
	public void setBk_fee(Integer bk_fee){
		this.bk_fee = bk_fee ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}
	
	// str1 getter and setter
	public Integer getStr1(){
		return this.str1 ;
	}
	
	public void setStr1(Integer str1){
		this.str1 = str1 ;
	}
	
	// str2 getter and setter
	public Integer getStr2(){
		return this.str2 ;
	}
	
	public void setStr2(Integer str2){
		this.str2 = str2 ;
	}
	
	// str3 getter and setter
	public Integer getStr3(){
		return this.str3 ;
	}
	
	public void setStr3(Integer str3){
		this.str3 = str3 ;
	}
	
	// str4 getter and setter
	public Integer getStr4(){
		return this.str4 ;
	}
	
	public void setStr4(Integer str4){
		this.str4 = str4 ;
	}
	
	// str5 getter and setter
	public Integer getStr5(){
		return this.str5 ;
	}
	
	public void setStr5(Integer str5){
		this.str5 = str5 ;
	}
	
	// str6 getter and setter
	public String getStr6(){
		return this.str6 ;
	}
	
	public void setStr6(String str6){
		this.str6 = str6 ;
	}
	
	// str7 getter and setter
	public String getStr7(){
		return this.str7 ;
	}
	
	public void setStr7(String str7){
		this.str7 = str7 ;
	}
	
	// str8 getter and setter
	public String getStr8(){
		return this.str8 ;
	}
	
	public void setStr8(String str8){
		this.str8 = str8 ;
	}
	
	// str9 getter and setter
	public String getStr9(){
		return this.str9 ;
	}
	
	public void setStr9(String str9){
		this.str9 = str9 ;
	}
	
	// str10 getter and setter
	public String getStr10(){
		return this.str10 ;
	}
	
	public void setStr10(String str10){
		this.str10 = str10 ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}


	public List<BillDto> getBillList() {
		return billList;
	}


	public void setBillList(List<BillDto> billList) {
		this.billList = billList;
	}


	public Integer getProd_bill_fee() {
		return prod_bill_fee;
	}


	public void setProd_bill_fee(Integer prod_bill_fee) {
		this.prod_bill_fee = prod_bill_fee;
	}


	public Integer getFee_judge() {
		return fee_judge;
	}


	public void setFee_judge(Integer fee_judge) {
		this.fee_judge = fee_judge;
	}
}
