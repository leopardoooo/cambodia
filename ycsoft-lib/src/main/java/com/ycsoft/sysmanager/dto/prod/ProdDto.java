package com.ycsoft.sysmanager.dto.prod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class ProdDto extends PProd {
	/**
	 * @Description:
	 * @date Jul 23, 2010 3:08:47 PM
	 */
	private static final long serialVersionUID = -2751415967409143149L;
	private List<ProdCountyResDto> staticResList;
	private List<ResGroupDto> dynamicResList;
	private List<PPackageProd> packList;
	private List<ProdTariffDto> prodTariffList;

	private boolean hasDynRes; //是否有动态资源
	private List<String> resList = new ArrayList<String>(); //资源列表
	private Map<String,List<String>> countyResMap = new HashMap<String,List<String>>();//县区资源列表

	private String is_exp;//是否过期
	
	private List<String> countyList = new ArrayList<String>();//产品适用地区
	private List<String> countyNameList = new ArrayList<String>();//适用地区名称
	
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
	public List<ProdCountyResDto> getStaticResList() {
		return staticResList;
	}

	public List<ResGroupDto> getDynamicResList() {
		return dynamicResList;
	}

	public void setDynamicResList(List<ResGroupDto> dynamicResList) {
		this.dynamicResList = dynamicResList;
	}

	public List<PPackageProd> getPackList() {
		return packList;
	}

	public void setPackList(List<PPackageProd> packList) {
		this.packList = packList;
	}

	public List<ProdTariffDto> getProdTariffList() {
		return prodTariffList;
	}

	public void setProdTariffList(List<ProdTariffDto> prodTariffList) {
		this.prodTariffList = prodTariffList;
	}

	public void setStaticResList(List<ProdCountyResDto> staticResList) {
		this.staticResList = staticResList;
	}
	
	public String getIs_exp() {
		return is_exp;
	}
	public void setIs_exp(String is_exp) {
		this.is_exp = is_exp;
	}
	/**
	 * @return the countyList
	 */
	public List<String> getCountyList() {
		return countyList;
	}
	/**
	 * @param countyList the countyList to set
	 */
	public void setCountyList(List<String> countyList) {
		this.countyList = countyList;
		List<String> countyNameList = new ArrayList<String>();
		for(String countyId : countyList){
			String countyName = MemoryDict.getDictName(DictKey.COUNTY, countyId);
			countyNameList.add(countyName);
		}
		this.countyNameList = countyNameList;
	}
	/**
	 * @return the countyNameList
	 */
	public List<String> getCountyNameList() {
		return countyNameList;
	}
	/**
	 * @param countyNameList the countyNameList to set
	 */
	public void setCountyNameList(List<String> countyNameList) {
		this.countyNameList = countyNameList;
	}
}
