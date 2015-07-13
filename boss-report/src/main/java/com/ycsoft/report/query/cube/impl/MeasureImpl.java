package com.ycsoft.report.query.cube.impl;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.MeasureDataType;
import com.ycsoft.report.query.cube.MeasureGather;

public class MeasureImpl implements Measure {

	private String code;
	private String text;
	private MeasureGather calcu;
	private String mearepid;
	private MeasureDataType datatype;
	private Boolean meacustom;
	
	public MeasureImpl(RepCube repcube) throws ReportException{
		if(!repcube.getColumn_type().equals(DimensionType.measure.name()))
			throw new ReportException(" repcube type is not Measure");
		this.code=repcube.getColumn_code();
		this.text=repcube.getColumn_as();
		this.calcu=MeasureGather.valueOf(repcube.getColumn_define());
		this.mearepid=repcube.getMea_detail_id();
		this.datatype=repcube.getShow_control()==null||"".equals(repcube.getShow_control().trim())?null:MeasureDataType.valueOf(repcube.getShow_control());
		if("T".equals(repcube.getColumn_type_check())){
			this.meacustom=true;
		}
	}
	public MeasureGather getCalculation() {
		return calcu;
	}

	public String getColumnCode() {
		return code;
	}

	public String getColumnText() {
		return text;
	}
	public String getMeaRepId() {
		return this.mearepid;
	}
	public MeasureDataType getDateType() {
		return this.datatype;
	}
	public Boolean getMeaCustom() {
		return this.meacustom;
	}

}
