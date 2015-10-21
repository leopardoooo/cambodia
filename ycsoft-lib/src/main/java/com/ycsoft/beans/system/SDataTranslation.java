package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SDataTranslation -> S_DATA_TRANSLATION mapping
 */
@POJO(tn = "S_DATA_TRANSLATION", sn = "SEQ_DATA_TRANS_ID", pk = "ID")
public class SDataTranslation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990485390737034660L;
	private String id;
	private String data_cn;
	private String data_en;
	private String data_kh;
	
	public SDataTranslation(){}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getData_cn() {
		return data_cn;
	}
	public void setData_cn(String data_cn) {
		this.data_cn = data_cn;
	}
	public String getData_en() {
		return data_en;
	}
	public void setData_en(String data_en) {
		this.data_en = data_en;
	}
	public String getData_kh() {
		return data_kh;
	}
	public void setData_kh(String data_kh) {
		this.data_kh = data_kh;
	}
	
}
