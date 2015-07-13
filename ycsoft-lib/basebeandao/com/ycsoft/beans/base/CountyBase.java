package com.ycsoft.beans.base;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class CountyBase  implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7645529278707240216L;
	private String area_id;
	private String county_id;

	private String area_name;
	private String county_name;

	/**
	 * @return the area_id
	 */
	public String getArea_id() {
		return area_id;
	}

	/**
	 * @return the area_name
	 */
	public String getArea_name() {
		return area_name;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @return the county_name
	 */
	public String getCounty_name() {
		return county_name;
	}

	/**
	 * @param area_id
	 *            the area_id to set
	 */
	public void setArea_id(String area_id) {
		area_name = MemoryDict.getDictName(DictKey.AREA, area_id);
		this.area_id = area_id;
	}

	/**
	 * @param area_name
	 *            the area_name to set
	 */
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	/**
	 * @param county_id
	 *            the county_id to set
	 */
	public void setCounty_id(String county_id) {
		county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
		this.county_id = county_id;
	}

	/**
	 * @param county_name
	 *            the county_name to set
	 */
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

}
