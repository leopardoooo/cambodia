package com.ycsoft.business.dto.config;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.beans.config.TBusiFeeStd;
import com.ycsoft.beans.config.TTemplateColumn;

public class TemplateUseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 139606969794051124L;
	private List<TTemplateColumn> columnList;
	private List<TBusiFeeStd> feeStbList;
	private String is_include;
	
	public List<TTemplateColumn> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<TTemplateColumn> columnList) {
		this.columnList = columnList;
	}
	public List<TBusiFeeStd> getFeeStbList() {
		return feeStbList;
	}
	public void setFeeStbList(List<TBusiFeeStd> feeStbList) {
		this.feeStbList = feeStbList;
	}
	public String getIs_include() {
		return is_include;
	}
	public void setIs_include(String is_include) {
		this.is_include = is_include;
	}
}
