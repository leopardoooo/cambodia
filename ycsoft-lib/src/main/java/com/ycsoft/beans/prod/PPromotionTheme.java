package com.ycsoft.beans.prod;

/**
 * PPromotionTheme.java	2010/10/09
 */


import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;


/**
 * PPromotionTheme -> P_PROMOTION_THEME mapping
 */
@POJO(
	tn="P_PROMOTION_THEME",
	sn="",
	pk="theme_id")
public class PPromotionTheme extends BusiBase implements Serializable {

	// PPromotionTheme all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 509063594083162680L;

	private String theme_id ;
	private String theme_name ;
	private String promotion_desc ;
	private String promotion_type ;
	private String orgarize_type ;
	
	
	/**
	 * default empty constructor
	 */
	public PPromotionTheme() {}


	// theme_id getter and setter
	public String getTheme_id(){
		return theme_id ;
	}

	public void setTheme_id(String theme_id){
		this.theme_id = theme_id ;
	}

	// theme_name getter and setter
	public String getTheme_name(){
		return theme_name ;
	}

	public void setTheme_name(String theme_name){
		this.theme_name = theme_name ;
	}

	// promotion_desc getter and setter
	public String getPromotion_desc(){
		return promotion_desc ;
	}

	public void setPromotion_desc(String promotion_desc){
		this.promotion_desc = promotion_desc ;
	}

	// promotion_type getter and setter
	public String getPromotion_type(){
		return promotion_type ;
	}

	public void setPromotion_type(String promotion_type){
		this.promotion_type = promotion_type ;
	}

	// orgarize_type getter and setter
	public String getOrgarize_type(){
		return orgarize_type ;
	}

	public void setOrgarize_type(String orgarize_type){
		this.orgarize_type = orgarize_type ;
	}

}