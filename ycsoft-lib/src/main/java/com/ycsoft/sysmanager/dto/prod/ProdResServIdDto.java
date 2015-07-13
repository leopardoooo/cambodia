package com.ycsoft.sysmanager.dto.prod;

import java.util.List;

import com.ycsoft.beans.prod.PProd;

public class ProdResServIdDto {
	private List<ResDto> staticResList;
	private List<ResGroupDto> dynamicResList;
	private List<ProdDto> prodList;
	public List<ResDto> getStaticResList() {
		return staticResList;
	}
	public void setStaticResList(List<ResDto> staticResList) {
		this.staticResList = staticResList;
	}
	public List<ResGroupDto> getDynamicResList() {
		return dynamicResList;
	}
	public void setDynamicResList(List<ResGroupDto> dynamicResList) {
		this.dynamicResList = dynamicResList;
	}
	/**
	 * @return the prodList
	 */
	public List<ProdDto> getProdList() {
		return prodList;
	}
	/**
	 * @param prodList the prodList to set
	 */
	public void setProdList(List<ProdDto> prodList) {
		this.prodList = prodList;
	}
}
