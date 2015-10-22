package com.ycsoft.beans.ott;

import java.util.Map;

public class TServerOttauthDto  {
	
	private String id;
	private String name;
	private String status;
	private Map<String,String> extension;// EXTENSION
	
	private TServerOttauthFee[] fee;

	public TServerOttauthFee[] getFee() {
		return fee;
	}

	public void setFee(TServerOttauthFee[] fee) {
		this.fee = fee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getExtension() {
		return extension;
	}

	public void setExtension(Map<String, String> extension) {
		this.extension = extension;
	}

	
}
