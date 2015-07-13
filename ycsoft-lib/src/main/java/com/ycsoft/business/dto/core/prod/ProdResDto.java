
package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.beans.prod.PRes;

/**
 * @author YC-SOFT
 * 产品对应的资源DTO
 */
public class ProdResDto implements Serializable {
	private String prod_id;
	private String prod_name;

	private List<PRes> staticResList;
	private List<ResGroupDto> dynamicResList;
	private List<ResGroupDto> oldDynamicResList;
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public List<PRes> getStaticResList() {
		return staticResList;
	}
	public void setStaticResList(List<PRes> staticResList) {
		this.staticResList = staticResList;
	}
	public List<ResGroupDto> getDynamicResList() {
		return dynamicResList;
	}
	public void setDynamicResList(List<ResGroupDto> dynamicResList) {
		this.dynamicResList = dynamicResList;
	}
	public List<ResGroupDto> getOldDynamicResList() {
		return oldDynamicResList;
	}
	public void setOldDynamicResList(List<ResGroupDto> oldDynamicResList) {
		this.oldDynamicResList = oldDynamicResList;
	}
}
