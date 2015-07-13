/**
 * SItemDefine.java	2010/09/17
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * SItemDefine -> S_ITEM_DEFINE mapping
 */
@POJO(
	tn="S_ITEM_DEFINE",
	sn="",
	pk="item_key")
public class SItemDefine implements Serializable {

	// SItemDefine all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4254165222559399813L;
	private String item_key ;
	private String item_desc ;

	/**
	 * default empty constructor
	 */
	public SItemDefine() {}


	// item_key getter and setter
	public String getItem_key(){
		return item_key ;
	}

	public void setItem_key(String item_key){
		this.item_key = item_key ;
	}

	// item_desc getter and setter
	public String getItem_desc(){
		return item_desc ;
	}

	public void setItem_desc(String item_desc){
		this.item_desc = item_desc ;
	}

}