package com.ycsoft.boss.remoting.ott;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class Product {
	private String id;
	private String name;
	private String state;
	private JsonObject extension;
	private List<Fee> fee;
	
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public JsonObject getExtension() {
		return extension;
	}
	public void setExtension(JsonObject extension) {
		this.extension = extension;
	}
	public List<Fee> getFee() {
		return fee;
	}
	public void setFee(List<Fee> fee) {
		this.fee = fee;
	}
	public Product(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		fee = new ArrayList<>();
		extension = new JsonObject();
		extension.addProperty("domain", "2");
		this.state="1";
	}
	
	
	
	
	

}
