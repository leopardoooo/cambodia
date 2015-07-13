/**
 *
 */
package com.ycsoft.sysmanager.dto.prod;

import java.util.List;

import com.ycsoft.beans.prod.PResgroup;
import com.ycsoft.beans.prod.PResgroupRes;

/**
 * @author YC-SOFT
 *
 */
public class ResGroupDto extends PResgroup {
	/**
	 *
	 */
	private static final long serialVersionUID = -5320627259366199473L;
	private int res_number;
	private String prod_id;
	private String prod_name;
	private List<PResgroupRes> groupResList;
	
	private Integer max_count;
	private String allow_update;

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public int getRes_number() {
		return res_number;
	}

	public void setRes_number(int res_number) {
		this.res_number = res_number;
	}

	public List<PResgroupRes> getGroupResList() {
		return groupResList;
	}

	public void setGroupResList(List<PResgroupRes> groupResList) {
		this.groupResList = groupResList;
	}

	public String getAllow_update() {
		return allow_update;
	}

	public void setAllow_update(String allow_update) {
		this.allow_update = allow_update;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public Integer getMax_count() {
		return max_count;
	}

	public void setMax_count(Integer max_count) {
		this.max_count = max_count;
	}

}
