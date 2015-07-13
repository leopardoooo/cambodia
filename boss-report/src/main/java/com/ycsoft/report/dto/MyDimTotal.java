package com.ycsoft.report.dto;

import java.util.List;

import com.ycsoft.report.query.key.Impl.ConKeyCheck;

public class MyDimTotal {
	private String dim;
	private String  dim_name;
	private String  total_name;
	private List<ConKeyCheck> totallist;
	
	public String getDim() {
		return dim;
	}
	public void setDim(String dim) {
		this.dim = dim;
	}
	public String getDim_name() {
		return dim_name;
	}
	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}
	public String getTotal_name() {
		return total_name;
	}
	public void setTotal_name(String total_name) {
		this.total_name = total_name;
	}
	public List<ConKeyCheck> getTotallist() {
		return totallist;
	}
	public void setTotallist(List<ConKeyCheck> totallist) {
		this.totallist = totallist;
	}
}
