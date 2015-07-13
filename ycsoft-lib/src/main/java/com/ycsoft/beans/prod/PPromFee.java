package com.ycsoft.beans.prod;

/**
 * PPromFee.java	2012/06/28
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.CnToSpell;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PPromFee -> P_PROM_FEE mapping 
 */
@POJO(
	tn="P_PROM_FEE",
	sn="SEQ_FEE_ID",
	pk="prom_fee_id")
public class PPromFee implements Serializable {
	
	// PPromFee all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4679053987739412363L;
	
	private String prom_fee_id ;	
	private String prom_fee_name ;	
	private Integer prom_fee ;	
	private String printitem_id ;	
	private String optr_id ;	
	private String area_id ;	
	private String county_id ;	
	private Date create_time ;	
	private Date eff_date;
	private Date exp_date;
	private Integer join_cnt;
	private String prom_type;
	private String status;
	private String remark;
	
	private Integer use_cnt;
	private String optr_name;
	private String printitem_name;
	private String prom_type_name;
	private String attr;	//用于拼音首字母过滤
	private String status_text;
	
	public String getAttr() {
		return attr;
	}


	public Integer getUse_cnt() {
		return use_cnt;
	}


	public void setUse_cnt(Integer useCnt) {
		use_cnt = useCnt;
	}


	public String getProm_type_name() {
		return prom_type_name;
	}


	public void setProm_type_name(String prom_type_name) {
		this.prom_type_name = prom_type_name;
	}


	/**
	 * default empty constructor
	 */
	public PPromFee() {}
	
	
	// prom_fee_sn getter and setter
	public String getProm_fee_id(){
		return this.prom_fee_id ;
	}
	
	public void setProm_fee_id(String prom_fee_id){
		this.prom_fee_id = prom_fee_id ;
	}
	
	// prom_fee_name getter and setter
	public String getProm_fee_name(){
		return this.prom_fee_name ;
	}
	
	public void setProm_fee_name(String prom_fee_name){
		this.prom_fee_name = prom_fee_name ;
		attr = StringHelper.append(prom_fee_name, "_", CnToSpell.getPinYin(prom_fee_name),
				"_", CnToSpell.getPinYinHeadChar(prom_fee_name));
	}
	
	// prom_fee getter and setter
	public Integer getProm_fee(){
		return this.prom_fee ;
	}
	
	public void setProm_fee(Integer prom_fee){
		this.prom_fee = prom_fee ;
		attr = StringHelper.append(attr,"_",prom_fee/100);
	}
	
	// printitem_id getter and setter
	public String getPrintitem_id(){
		return this.printitem_id ;
	}
	
	public void setPrintitem_id(String printitem_id){
		this.printitem_id = printitem_id ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
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
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}


	public Date getEff_date() {
		return eff_date;
	}


	public void setEff_date(Date effDate) {
		eff_date = effDate;
	}


	public Date getExp_date() {
		return exp_date;
	}


	public void setExp_date(Date expDate) {
		exp_date = expDate;
	}


	public String getOptr_name() {
		return optr_name;
	}


	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}


	public String getPrintitem_name() {
		return printitem_name;
	}


	public void setPrintitem_name(String printitemName) {
		printitem_name = printitemName;
	}


	public Integer getJoin_cnt() {
		return join_cnt;
	}


	public void setJoin_cnt(Integer join_cnt) {
		this.join_cnt = join_cnt;
	}


	public String getProm_type() {
		return prom_type;
	}


	public void setProm_type(String prom_type) {
		this.prom_type = prom_type;
		this.prom_type_name = MemoryDict.getDictName(DictKey.PROM_TYPE, prom_type);
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}


	public String getStatus_text() {
		return status_text;
	}


	public void setStatus_text(String statusText) {
		status_text = statusText;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}