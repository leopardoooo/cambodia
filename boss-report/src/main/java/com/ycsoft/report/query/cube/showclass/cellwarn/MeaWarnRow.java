package com.ycsoft.report.query.cube.showclass.cellwarn;

import java.io.Serializable;
import java.util.List;

/**
 * 指标警戒的行配置
 */
public class MeaWarnRow implements Serializable {
	//行警戒类型
	private WarnRowType warnrowtype;
	private String dimleveljson;//项目对应维度层级 WarnDimLevel=>json
	//行警戒类型 取值(id)
	private List<String> warnvaluelist;
	//行警戒取值判断方法(列头 1月+2月 求和)
	private WarnValueType warnvaluetype;
	
	//逻辑符号1和判断值
	private Operator optr1;
	private Double value1;
	//逻辑符号2和判断值
	private Operator optr2;
	private Double value2;
	//配色
	private String colour;
	
	private String text1;//文本1 条件文本
	private String text2;//文本2 逻辑判断文本
	public WarnRowType getWarnrowtype() {
		return warnrowtype;
	}
	public void setWarnrowtype(WarnRowType warnrowtype) {
		this.warnrowtype = warnrowtype;
	}
	public List<String> getWarnvaluelist() {
		return warnvaluelist;
	}
	public void setWarnvaluelist(List<String> warnvaluelist) {
		this.warnvaluelist = warnvaluelist;
	}
	public WarnValueType getWarnvaluetype() {
		return warnvaluetype;
	}
	public void setWarnvaluetype(WarnValueType warnvaluetype) {
		this.warnvaluetype = warnvaluetype;
	}
	public Operator getOptr1() {
		return optr1;
	}
	public void setOptr1(Operator optr1) {
		this.optr1 = optr1;
	}
	public Double getValue1() {
		return value1;
	}
	public void setValue1(Double value1) {
		this.value1 = value1;
	}
	public Operator getOptr2() {
		return optr2;
	}
	public void setOptr2(Operator optr2) {
		this.optr2 = optr2;
	}
	public Double getValue2() {
		return value2;
	}
	public void setValue2(Double value2) {
		this.value2 = value2;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getDimleveljson() {
		return dimleveljson;
	}
	public void setDimleveljson(String dimleveljson) {
		this.dimleveljson = dimleveljson;
	}
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	

}
