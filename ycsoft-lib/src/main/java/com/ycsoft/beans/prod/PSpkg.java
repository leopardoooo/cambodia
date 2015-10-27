/**
 * PSpkg.java	2015/09/05
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PSpkg -> P_SPKG mapping 
 */
@POJO(
	tn="P_SPKG",
	sn="SEQ_SP_ID",
	pk="SP_ID")
public class PSpkg implements Serializable {
	
	// PSpkg all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224509151465955453L;
	private String sp_id ;	
	private String spkg_sn ;	
	private String spkg_title ;	
	private String spkg_text ;	
	private Date eff_date ;	
	private Date exp_date ;	
	private String remark ;	
	private String optr_id ;	
	private Date create_time ;	
	private String status;
	private String confirm_optr_id;
	private Date confirm_date;
	private String apply_optr_id;
	private Date apply_date;
	
	private String status_text;
	private String optr_name;
	private String confirm_optr_name;
	private String apply_optr_name;
	
	private String prod_name;
	private String cust_no;
	private String cust_name;
	
	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	/**
	 * default empty constructor
	 */
	public PSpkg() {}
	
	public String getStatus_text() {
		return status_text;
	}
	
	public String getOptr_name() {
		return optr_name;
	}

	public String getConfirm_optr_name() {
		return confirm_optr_name;
	}

	public String getApply_optr_name() {
		return apply_optr_name;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	public String getConfirm_optr_id() {
		return confirm_optr_id;
	}

	public void setConfirm_optr_id(String confirm_optr_id) {
		this.confirm_optr_id = confirm_optr_id;
		this.confirm_optr_name = MemoryDict.getDictName(DictKey.OPTR, confirm_optr_id);
	}

	public Date getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}

	public String getApply_optr_id() {
		return apply_optr_id;
	}

	public void setApply_optr_id(String apply_optr_id) {
		this.apply_optr_id = apply_optr_id;
		this.apply_optr_name = MemoryDict.getDictName(DictKey.OPTR, apply_optr_id);
	}

	public Date getApply_date() {
		return apply_date;
	}

	public void setApply_date(Date apply_date) {
		this.apply_date = apply_date;
	}

	// sp_id getter and setter
	public String getSp_id(){
		return this.sp_id ;
	}
	
	public void setSp_id(String sp_id){
		this.sp_id = sp_id ;
	}
	
	// spkg_sn getter and setter
	public String getSpkg_sn(){
		return this.spkg_sn ;
	}
	
	public void setSpkg_sn(String spkg_sn){
		this.spkg_sn = spkg_sn ;
	}
	
	// spkg_title getter and setter
	public String getSpkg_title(){
		return this.spkg_title ;
	}
	
	public void setSpkg_title(String spkg_title){
		this.spkg_title = spkg_title ;
	}
	
	// spkg_text getter and setter
	public String getSpkg_text(){
		return this.spkg_text ;
	}
	
	public void setSpkg_text(String spkg_text){
		this.spkg_text = spkg_text ;
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
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

}