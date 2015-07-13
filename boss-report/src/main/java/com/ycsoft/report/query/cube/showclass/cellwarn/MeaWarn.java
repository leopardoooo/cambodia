package com.ycsoft.report.query.cube.showclass.cellwarn;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeCell;

/**
 * 指标警戒bean
 */
public class MeaWarn implements Serializable {
	
	private String mea;
	private List<MeaWarnRow> rowlist;
	public String getMea() {
		return mea;
	}
	public void setMea(String mea) {
		this.mea = mea;
	}
	public List<MeaWarnRow> getRowlist() {
		return rowlist;
	}
	public void setRowlist(List<MeaWarnRow> rowlist) {
		this.rowlist = rowlist;
	}
}
