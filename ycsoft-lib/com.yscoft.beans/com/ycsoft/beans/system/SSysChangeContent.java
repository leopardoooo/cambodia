package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn="S_SYS_CHANGE_CONTENT",sn="",pk="")
public class SSysChangeContent implements Serializable{
	private String change_id;
	private Integer idx;
	private String content;
	public String getChange_id() {
		return change_id;
	}
	public void setChange_id(String change_id) {
		this.change_id = change_id;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
