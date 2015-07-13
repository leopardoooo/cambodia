package com.ycsoft.report.dto;

import com.ycsoft.report.bean.RepOptrExport;

public class RepOptrExportDto extends RepOptrExport {
	
	private boolean ischeck;
	private String col_name;
	public String getCol_name() {
		return col_name;
	}

	public void setCol_name(String col_name) {
		this.col_name = col_name;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
}
