package com.ycsoft.beans.core.acct;

/**
 * CAcctPreFee.java	2011/03/25
 */
 
import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CAcctPreFee -> C_ACCT_PRE_FEE mapping 
 */
@POJO(
	tn="C_ACCT_PRE_FEE",
	sn="",
	pk="done_code")
public class CAcctPreFee implements Serializable {
	
	// CAcctPreFee all properties 

	private Integer done_code ;	
	private String cust_id ;	
	private String user_id ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private String prod_id ;	
	private Integer fee ;	
	private Date fee_time ;	
	private String area_id ;	
	private String county_id ;	
	private String prog_name ;	
	private String trans_id ;	
	private Integer dealtype ;	
	private String detailparams ;	
	private Date deal_time ;	
	private String status ;	
	private Date cancel_time ;	
	private Integer process_flag ;	
	private Integer ticket_sn ;	
	private String original_sn ;	
	private String prog_id ;
	private String is_valid;
	
	/**
	 * default empty constructor
	 */
	public CAcctPreFee() {}
	
	public CAcctPreFee(Integer done_code,String cust_id,String user_id,
			String acct_id,String acctitem_id,String prod_id, Integer fee,Date fee_time,
			String area_id ,String county_id,String prog_name,String trans_id,String detailparams,
			Date deal_time,String status,String prog_id){
		this.done_code = done_code;
		this.cust_id = cust_id;
		this.user_id = user_id;
		this.acct_id = acct_id;
		this.acctitem_id = acctitem_id;
		this.prod_id = prod_id;
		this.fee = fee;
		this.fee_time = fee_time;
		this.area_id = area_id;
		this.county_id = county_id;
		this.prog_name = prog_name;
		this.trans_id = trans_id;
		this.detailparams = detailparams;
		this.deal_time = deal_time;
		this.status = status;
		this.prog_id = prog_id;
	}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
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
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// fee_time getter and setter
	public Date getFee_time(){
		return this.fee_time ;
	}
	
	public void setFee_time(Date fee_time){
		this.fee_time = fee_time ;
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
	
	// prog_name getter and setter
	public String getProg_name(){
		return this.prog_name ;
	}
	
	public void setProg_name(String prog_name){
		this.prog_name = prog_name ;
	}
	
	// trans_id getter and setter
	public String getTrans_id(){
		return this.trans_id ;
	}
	
	public void setTrans_id(String trans_id){
		this.trans_id = trans_id ;
	}
	
	// dealtype getter and setter
	public Integer getDealtype(){
		return this.dealtype ;
	}
	
	public void setDealtype(Integer dealtype){
		this.dealtype = dealtype ;
	}
	
	// detailparams getter and setter
	public String getDetailparams(){
		return this.detailparams ;
	}
	
	public void setDetailparams(String detailparams){
		this.detailparams = detailparams ;
	}
	
	// deal_time getter and setter
	public Date getDeal_time(){
		return this.deal_time ;
	}
	
	public void setDeal_time(Date deal_time){
		this.deal_time = deal_time ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// cancel_time getter and setter
	public Date getCancel_time(){
		return this.cancel_time ;
	}
	
	public void setCancel_time(Date cancel_time){
		this.cancel_time = cancel_time ;
	}
	
	// process_flag getter and setter
	public Integer getProcess_flag(){
		return this.process_flag ;
	}
	
	public void setProcess_flag(Integer process_flag){
		this.process_flag = process_flag ;
	}
	
	// ticket_sn getter and setter
	public Integer getTicket_sn(){
		return this.ticket_sn ;
	}
	
	public void setTicket_sn(Integer ticket_sn){
		this.ticket_sn = ticket_sn ;
	}
	
	// original_sn getter and setter
	public String getOriginal_sn(){
		return this.original_sn ;
	}
	
	public void setOriginal_sn(String original_sn){
		this.original_sn = original_sn ;
	}
	
	// prog_id getter and setter
	public String getProg_id(){
		return this.prog_id ;
	}
	
	public void setProg_id(String prog_id){
		this.prog_id = prog_id ;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

}