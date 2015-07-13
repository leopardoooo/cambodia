package com.ycsoft.report.dto;

import java.io.Serializable;

import com.ycsoft.report.commons.ReportConstants;

/**
 * 显示用数据集
 */
public class ShowQueryDto implements Serializable {
	
	private String query_id;
	private String quiee_raq;
	private String rep_id;
	private String rep_type;
	
	public ShowQueryDto(){}
	public ShowQueryDto(InitQueryDto initQueryDto){
		setQuery_id(initQueryDto.getQuery_id());
		setRep_id(initQueryDto.getRep_id());
		setRep_type(initQueryDto.getRep_type());
		if(initQueryDto.getRep_type().equals(ReportConstants.REP_TYPE_QUIEE))
			setQuiee_raq(initQueryDto.getQuiee_raq());
	}
	public String getRep_type() {
		return rep_type;
	}
	public void setRep_type(String rep_type) {
		this.rep_type = rep_type;
	}
	public String getQuery_id() {
		return query_id;
	}
	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}
	public String getQuiee_raq() {
		return quiee_raq;
	}
	public void setQuiee_raq(String quiee_raq) {
		this.quiee_raq = quiee_raq;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

}
