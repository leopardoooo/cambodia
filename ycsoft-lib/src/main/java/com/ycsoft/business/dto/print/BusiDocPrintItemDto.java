/*
 * @(#) BusiDocDto.java 1.0.0 Aug 4, 2011 5:57:39 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.business.dto.print;

import java.io.Serializable;



/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class BusiDocPrintItemDto implements Serializable{
	
	private String info;
	private String busi_code;
	private String template_filename;
	private String done_code;
	private String tpl;
	private Integer height;
	private Integer width;
	private String protocol_desc;
	private Integer busi_code_idx;
	private String busi_code_span;
	private String group_column;
	private String user_id;
	private String condition;
	private int index;
	private boolean flag;
	private String info1="";
	private String info2="";
	private String info3="";
	private String info4="";
	private String info5="";
	private String info6="";
	private String info7="";
	private String info8="";
	private String info9="";
	private String info10="";
	public String getInfo1() {
		return null==info1?"":info1;
	}
	public void setInfo1(String info1) {
		this.info1 = info1;
	}
	public String getInfo2() {
		return null==info2?"":info2;
	}
	public void setInfo2(String info2) {
		this.info2 = info2;
	}
	public String getInfo3() {
		return null==info3?"":info3;
	}
	public void setInfo3(String info3) {
		this.info3 = info3;
	}
	public String getInfo4() {
		return null==info4?"":info4;
	}
	public void setInfo4(String info4) {
		this.info4 = info4;
	}
	public String getInfo5() {
		return null==info5?"":info5;
	}
	public void setInfo5(String info5) {
		this.info5 = info5;
	}
	public String getInfo6() {
		return null==info6?"":info6;
	}
	public void setInfo6(String info6) {
		this.info6 = info6;
	}
	public String getInfo7() {
		return null==info7?"":info7;
	}
	public void setInfo7(String info7) {
		this.info7 = info7;
	}
	public String getInfo8() {
		return null==info8?"":info8;
	}
	public void setInfo8(String info8) {
		this.info8 = info8;
	}
	public String getInfo9() {
		return null==info9?"":info9;
	}
	public void setInfo9(String info9) {
		this.info9 = info9;
	}
	public String getInfo10() {
		return null==info10?"":info10;
	}
	public void setInfo10(String info10) {
		this.info10 = info10;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBusi_code_span() {
		return busi_code_span;
	}
	public void setBusi_code_span(String busi_code_span) {
		this.busi_code_span = busi_code_span;
	}
	public Integer getBusi_code_idx() {
		return busi_code_idx;
	}
	public void setBusi_code_idx(Integer busi_code_idx) {
		this.busi_code_idx = busi_code_idx;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
	public String getTemplate_filename() {
		return template_filename;
	}
	public void setTemplate_filename(String template_filename) {
		this.template_filename = template_filename;
	}
	public String getDone_code() {
		return done_code;
	}
	public void setDone_code(String done_code) {
		this.done_code = done_code;
	}
	public String getTpl() {
		return tpl;
	}
	public void setTpl(String tpl) {
		this.tpl = tpl;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getProtocol_desc() {
		return protocol_desc;
	}
	public void setProtocol_desc(String protocol_desc) {
		this.protocol_desc = protocol_desc;
	}
	public String getGroup_column() {
		return group_column;
	}
	public void setGroup_column(String group_column) {
		this.group_column = group_column;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
