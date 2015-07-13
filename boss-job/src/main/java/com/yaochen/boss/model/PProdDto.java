package com.yaochen.boss.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.prod.PProd;

public class PProdDto extends PProd {
	private boolean hasDynRes; //是否有动态资源
	private List<String> resList = new ArrayList<String>(); //资源列表
	private Map<String,List<String>> countyResMap = new HashMap<String,List<String>>();//县区资源列表
	public boolean isHasDynRes() {
		return hasDynRes;
	}
	public void setHasDynRes(boolean hasDynRes) {
		this.hasDynRes = hasDynRes;
	}
	public List<String> getResList() {
		return resList;
	}
	public void setResList(List<String> resList) {
		this.resList = resList;
	}
	public Map<String, List<String>> getCountyResMap() {
		return countyResMap;
	}
	public void setCountyResMap(Map<String, List<String>> countyResMap) {
		this.countyResMap = countyResMap;
	}
}
