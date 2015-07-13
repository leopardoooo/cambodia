/**
 * CVoucherProcure.java	2011/03/23
 */
 
package com.ycsoft.beans.core.voucher; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CVoucherProcure -> C_VOUCHER_PROCURE mapping 
 */
@POJO(
	tn="C_VOUCHER_PROCURE",
	sn="",
	pk="")
public class CVoucherProcure implements Serializable {
	
	// CVoucherProcure all properties 

	private Integer voucher_done_code ;	
	private String procure_no ;	
	private String procure_dept ;	
	private String procurer ;	
	private String optr_id ;
	private String county_id;
	private Integer count;
	private Date create_time ;	
	private String remark ;
	
	private String optr_name;
	private String county_id_text;
	
	/**
	 * default empty constructor
	 */
	public CVoucherProcure() {}
	
	
	// voucher_done_code getter and setter
	public Integer getVoucher_done_code(){
		return this.voucher_done_code ;
	}
	
	public void setVoucher_done_code(Integer voucher_done_code){
		this.voucher_done_code = voucher_done_code ;
	}
	
	// procure_no getter and setter
	public String getProcure_no(){
		return this.procure_no ;
	}
	
	public void setProcure_no(String procure_no){
		this.procure_no = procure_no ;
	}
	
	// procure_dept getter and setter
	public String getProcure_dept(){
		return this.procure_dept ;
	}
	
	public void setProcure_dept(String procure_dept){
		this.procure_dept = procure_dept ;
	}
	
	// procurer getter and setter
	public String getProcurer(){
		return this.procurer ;
	}
	
	public void setProcurer(String procurer){
		this.procurer = procurer ;
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
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}


	public String getCounty_id() {
		return county_id;
	}


	public void setCounty_id(String county_id) {
		this.county_id = county_id;
		this.county_id_text = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}


	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}


	public void setCounty_id_text(String county_id_text) {
		this.county_id_text = county_id_text;
	}

}