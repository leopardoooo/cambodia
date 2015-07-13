/**
 * TExtendScript.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TExtendScript -> T_EXTEND_SCRIPT mapping
 */
@POJO(tn = "T_EXTEND_SCRIPT", sn = "", pk = "")
public class TExtendScript implements Serializable {

	// TExtendScript all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3547371814906842694L;
	private String extentid;
	private String attribute_id;
	private String event_name;
	private String script_desc;
	private String script_text;

	/**
	 * default empty constructor
	 */
	public TExtendScript() {
	}

	// extentid getter and setter
	public String getExtentid() {
		return extentid;
	}

	public void setExtentid(String extentid) {
		this.extentid = extentid;
	}

	public String getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}

	// event_name getter and setter
	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	// script_desc getter and setter
	public String getScript_desc() {
		return script_desc;
	}

	public void setScript_desc(String script_desc) {
		this.script_desc = script_desc;
	}

	// script_text getter and setter
	public String getScript_text() {
		return script_text;
	}

	public void setScript_text(String script_text) {
		this.script_text = script_text;
	}

}