package com.ycsoft.beans.system;

import java.io.Serializable;

public class SBullentionWorkCount  implements Serializable {
	
	private Integer today_cnt;
	private Integer add_cnt;
	private Integer all_cnt;
	
	public Integer getToday_cnt() {
		return today_cnt;
	}
	public void setToday_cnt(Integer today_cnt) {
		this.today_cnt = today_cnt;
	}
	public Integer getAdd_cnt() {
		return add_cnt;
	}
	public void setAdd_cnt(Integer add_cnt) {
		this.add_cnt = add_cnt;
	}
	public Integer getAll_cnt() {
		return all_cnt;
	}
	public void setAll_cnt(Integer all_cnt) {
		this.all_cnt = all_cnt;
	}
	
}
