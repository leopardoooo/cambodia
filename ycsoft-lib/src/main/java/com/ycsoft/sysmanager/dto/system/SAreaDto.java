package com.ycsoft.sysmanager.dto.system;

import java.util.List;

import com.ycsoft.beans.system.SArea;

public class SAreaDto extends SArea {
	private static final long serialVersionUID = 3406711140088787230L;
	private List<SCountyDto> countyList;

	public List<SCountyDto> getCountyList() {
		return countyList;
	}
	public void setCountyList(List<SCountyDto> countyList) {
		this.countyList = countyList;
	}



}
