package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_FILEKEY_VALUE", sn = "", pk = "")
public class RepFileKeyValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4876506145857011560L;
	private String file_id;
	private String code1;
	private String code2;
	private String code3;
	private String code4;
	private String code5;
	private String code6;
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public String getCode3() {
		return code3;
	}
	public void setCode3(String code3) {
		this.code3 = code3;
	}
	public String getCode4() {
		return code4;
	}
	public void setCode4(String code4) {
		this.code4 = code4;
	}
	public String getCode5() {
		return code5;
	}
	public void setCode5(String code5) {
		this.code5 = code5;
	}
	public String getCode6() {
		return code6;
	}
	public void setCode6(String code6) {
		this.code6 = code6;
	}
	
}
