package com.yaochen.boss.model;

import java.util.ArrayList;
import java.util.List;

public class ProdRes {
	private String prodSn;
	private List<String> resList = new ArrayList<String>();
	
	public String getProdSn() {
		return prodSn;
	}
	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}
	public List<String> getResList() {
		return resList;
	}
	public void setResList(List<String> resList) {
		this.resList = resList;
	}
	
}