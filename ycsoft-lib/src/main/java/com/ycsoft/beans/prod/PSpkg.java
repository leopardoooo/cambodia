/**
 * PSpkg.java	2015/09/05
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * PSpkg -> P_SPKG mapping 
 */
@POJO(
	tn="P_SPKG",
	sn="",
	pk="")
public class PSpkg implements Serializable {
	
	// PSpkg all properties 

	private String sp_id ;	
	private String spkg_sn ;	
	private String spkg_title ;	
	private String spkg_text ;	
	private Date eff_date ;	
	private Date exp_date ;	
	private String remark ;	
	private String optr_id ;	
	private Date create_time ;	
	
	/**
	 * default empty constructor
	 */
	public PSpkg() {}
	
	
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
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

}