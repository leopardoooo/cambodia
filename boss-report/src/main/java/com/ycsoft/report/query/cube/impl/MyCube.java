package com.ycsoft.report.query.cube.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MyCube implements Serializable {

	// 已选维清单
	private List<String> dimlist;
	// 纵向维
	private String vertdim;
	// 维合计配置 Map<维，层数组>
	private Map<String, Integer[]> dimtotalmap;
	// 有效指标
	private List<String> mealist;

	// 维 层级
	private Map<String, Integer> dimlevelmap;
	// 维 切片层
	private Map<String, Map<Integer, String[]>> dimslicesmap;
	//维 自定义排序
	private Map<String, Map<Integer, String[]>> dimsortmap;
	
	public List<String> getMealist() {
		return mealist;
	}

	public void setMealist(List<String> mealist) {
		this.mealist = mealist;
	}

	public Map<String, Integer> getDimlevelmap() {
		return dimlevelmap;
	}

	public void setDimlevelmap(Map<String, Integer> dimlevelmap) {
		this.dimlevelmap = dimlevelmap;
	}

	public Map<String, Map<Integer, String[]>> getDimslicesmap() {
		return dimslicesmap;
	}

	public void setDimslicesmap(Map<String, Map<Integer, String[]>> dimslicesmap) {
		this.dimslicesmap = dimslicesmap;
	}

	public List<String> getDimlist() {
		return dimlist;
	}

	public void setDimlist(List<String> dimlist) {
		this.dimlist = dimlist;
	}

	public String getVertdim() {
		return vertdim;
	}

	public void setVertdim(String vertdim) {
		this.vertdim = vertdim;
	}

	public Map<String, Integer[]> getDimtotalmap() {
		return dimtotalmap;
	}

	public void setDimtotalmap(Map<String, Integer[]> dimtotalmap) {
		this.dimtotalmap = dimtotalmap;
	}

	public Map<String, Map<Integer, String[]>> getDimsortmap() {
		return dimsortmap;
	}

	public void setDimsortmap(Map<String, Map<Integer, String[]>> dimsortmap) {
		this.dimsortmap = dimsortmap;
	}

}