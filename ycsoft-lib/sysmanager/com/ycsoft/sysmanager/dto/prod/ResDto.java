package com.ycsoft.sysmanager.dto.prod;

import com.ycsoft.beans.prod.PRes;

public class ResDto extends PRes {
	/**
	 * @Description:
	 * @date Jul 28, 2010 2:54:32 PM
	 */
	private static final long serialVersionUID = 585018198102766425L;
	private String prod_id;
	private String external_res_id;
	private String boss_res_id;
	private String serverIds;
	private String resNames;
	private String cnt;
	private String key;
	
	private String server_id;
	private String server_name;
	private String isRecommend;	//是否被产品，资源组使用
	
	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

	public String getExternal_res_id() {
		return external_res_id;
	}

	public void setExternal_res_id(String externalResId) {
		external_res_id = externalResId;
	}

	public String getBoss_res_id() {
		return boss_res_id;
	}

	public void setBoss_res_id(String bossResId) {
		boss_res_id = bossResId;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getResNames() {
		return resNames;
	}

	public void setResNames(String resNames) {
		this.resNames = resNames;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
}
