package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;
import com.ycsoft.report.query.cube.detail.custom.GridRow;
import com.ycsoft.report.query.cube.detail.custom.RowType;

@POJO(tn = "REP_DETAIL_ROW", sn = "", pk = "")
public class RepDetailRow implements Serializable  {
	
	private String rep_id;
	private int row_idx;
	//列名
	private String row_name;
	//类型
	private String row_type;
	//列定义数据格式要求
	private String dataformat;
	
	public RepDetailRow(){}
	
	public RepDetailRow(String rep_id,GridRow gridrow){
		this.rep_id=rep_id;
		this.row_idx=gridrow.getRow_idx();
		this.row_name=gridrow.getRow_name();
		this.row_type=gridrow.getRow_type().name();
		this.dataformat=gridrow.getDataformat();	
	}
	
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public int getRow_idx() {
		return row_idx;
	}
	public void setRow_idx(int row_idx) {
		this.row_idx = row_idx;
	}
	public String getRow_name() {
		return row_name;
	}
	public void setRow_name(String row_name) {
		this.row_name = row_name;
	}
	public String getRow_type() {
		return row_type;
	}
	public void setRow_type(String row_type) {
		this.row_type = row_type;
	}
	public String getDataformat() {
		return dataformat;
	}
	public void setDataformat(String dataformat) {
		this.dataformat = dataformat;
	}

}
