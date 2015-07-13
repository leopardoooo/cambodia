/**
 * SResource.java	2010/04/23
 */

package com.ycsoft.beans.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * SResource -> S_RESOURCE mapping
 */
@POJO(tn = "S_RESOURCE", sn = "", pk = "RES_ID")
public class SResource implements Serializable {
	// SResource all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7058713697643615735L;
	private String res_id;
	private String res_name;
	private String res_pid;
	private String res_type;
	private String res_status;
	private Integer sort_num;
	private String sub_system_id;
	private String url;
	private String obj_type;
	private String handler;
	private List<SResource> btnResourceList = new ArrayList<SResource>();
	private String panel_name;
	private String button_type;
	private String show_name;

	private String remark;
	private String iconcls;
	private String busicode;
	private String jsp_url;
	
	private String res_pid_text;
	private String sub_system_text;
	private String businame;

	private String show_at_index;//是否在首页显示

	/**
	 * default empty constructor
	 */
	public SResource() {
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	// res_name getter and setter
	public String getRes_name() {
		return res_name;
	}

	public void setRes_name(String res_name) {
		this.res_name = res_name;
	}

	// res_pid getter and setter
	public String getRes_pid() {
		return res_pid;
	}

	public void setRes_pid(String res_pid) {
		this.res_pid = res_pid;
	}

	// res_type getter and setter
	public String getRes_type() {
		return res_type;
	}

	public void setRes_type(String res_type) {
		this.res_type = res_type;
	}

	// res_status getter and setter
	public String getRes_status() {
		return res_status;
	}

	public void setRes_status(String res_status) {
		this.res_status = res_status;
	}

	// sort_num getter and setter
	public Integer getSort_num() {
		return sort_num;
	}

	public void setSort_num(Integer sort_num) {
		this.sort_num = sort_num;
	}

	// sub_system_id getter and setter
	public String getSub_system_id() {
		return sub_system_id;
	}

	public void setSub_system_id(String sub_system_id) {
		this.sub_system_id = sub_system_id;
	}

	// url getter and setter
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// obj_type getter and setter
	public String getObj_type() {
		return obj_type;
	}

	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRes_pid_text() {
		return res_pid_text;
	}

	public void setRes_pid_text(String res_pid_text) {
		this.res_pid_text = res_pid_text;
	}

	public String getSub_system_text() {
		return sub_system_text;
	}

	public void setSub_system_text(String sub_system_text) {
		this.sub_system_text = sub_system_text;
	}

	public String getIconcls() {
		return iconcls;
	}

	public void setIconcls(String iconcls) {
		this.iconcls = iconcls;
	}

	public String getBusicode() {
		return busicode;
	}

	public void setBusicode(String busicode) {
		this.busicode = busicode;
		businame = MemoryDict.getDictName("BUSI_CODE", busicode);
	}


	public String getPanel_name() {
		return panel_name;
	}

	public void setPanel_name(String panel_name) {
		this.panel_name = panel_name;
	}

	public String getShow_name() {
		return show_name;
	}

	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}

	/**
	 * @return the button_type
	 */
	public String getButton_type() {
		return button_type;
	}

	/**
	 * @param button_type the button_type to set
	 */
	public void setButton_type(String button_type) {
		this.button_type = button_type;
	}

	public String getBusiname() {
		return businame;
	}

	public void setBusiname(String businame) {
		this.businame = businame;
	}

	/**
	 * @return the show_at_index
	 */
	public String getShow_at_index() {
		return show_at_index;
	}

	/**
	 * @param show_at_index the show_at_index to set
	 */
	public void setShow_at_index(String show_at_index) {
		this.show_at_index = show_at_index;
	}

	public List<SResource> getBtnResourceList() {
		return btnResourceList;
	}

	public void setBtnResourceList(List<SResource> btnResourceList) {
		this.btnResourceList = btnResourceList;
	}

	/**
	 * @return the jsp_url
	 */
	public String getJsp_url() {
		return jsp_url;
	}

	/**
	 * @param jsp_url the jsp_url to set
	 */
	public void setJsp_url(String jsp_url) {
		this.jsp_url = jsp_url;
	}

}