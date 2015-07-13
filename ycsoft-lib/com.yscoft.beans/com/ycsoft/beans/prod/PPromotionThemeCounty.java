package com.ycsoft.beans.prod;

/**
 * PPromotionThemeCounty.java	2010/10/09
 */


import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * PPromotionTheme -> P_PROMOTION_THEME_COUNTY mapping
 */
@POJO(
	tn="P_PROMOTION_THEME_COUNTY",
	sn="",
	pk="")
public class PPromotionThemeCounty implements Serializable {

	// PPromotionTheme all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 509063594083162680L;

	private String theme_id ;	
	private String county_id;
	private String county_name;

	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String countyName) {
		county_name = countyName;
	}


	public String getCounty_id() {
		return county_id;
	}


	public void setCounty_id(String countyId) {
		county_id = countyId;
		setCounty_name(MemoryDict.getDictName(DictKey.COUNTY, countyId));
	}


	/**
	 * default empty constructor
	 */
	public PPromotionThemeCounty() {}


	// theme_id getter and setter
	public String getTheme_id(){
		return theme_id ;
	}

	public void setTheme_id(String theme_id){
		this.theme_id = theme_id ;
	}	

}