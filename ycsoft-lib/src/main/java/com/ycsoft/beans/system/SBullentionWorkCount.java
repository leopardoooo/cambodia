package com.ycsoft.beans.system;

import java.io.Serializable;
import java.util.Date;

public class SBullentionWorkCount  implements Serializable {
	
	private Date query_date;//当前时间
	private String dept_id;
	
	private Integer  create_total; //'总的待派单',
	private Integer  create_supernet_total;// '工程部待派单',
	private Integer  create_supernet_new ;//'工程部有无新增待派单',
	private Integer  cfocn_failure_total;//'cfocn派单同步失败',
	private Integer  cfocn_failure_new;// '新增cfocn派单同步失败',
	private Integer  init_total;// '派单待完工',
	private Integer  init_new;// '新增派单待完工',
	private Integer  init_supernet_total;// '工程部的派单待完工',
	private Integer  zte_total; //'ZTE授权数',
	private Integer  zte_new;// '新增ZTE授权数',
	private Integer  endwait_total;// '完工等待',
	private Integer  endwait_new;// '新增完工等待',
	private Integer  end_today_total;//'今天完工待回访',
	private Integer  end_today_new;// '新增今日完工待回访'
	
	
	public Date getQuery_date() {
		return query_date;
	}
	public void setQuery_date(Date query_date) {
		this.query_date = query_date;
	}
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public Integer getCreate_total() {
		return create_total;
	}
	public void setCreate_total(Integer create_total) {
		this.create_total = create_total;
	}
	public Integer getCreate_supernet_total() {
		return create_supernet_total;
	}
	public void setCreate_supernet_total(Integer create_supernet_total) {
		this.create_supernet_total = create_supernet_total;
	}
	public Integer getCreate_supernet_new() {
		return create_supernet_new;
	}
	public void setCreate_supernet_new(Integer create_supernet_new) {
		this.create_supernet_new = create_supernet_new;
	}
	public Integer getCfocn_failure_total() {
		return cfocn_failure_total;
	}
	public void setCfocn_failure_total(Integer cfocn_failure_total) {
		this.cfocn_failure_total = cfocn_failure_total;
	}
	public Integer getCfocn_failure_new() {
		return cfocn_failure_new;
	}
	public void setCfocn_failure_new(Integer cfocn_failure_new) {
		this.cfocn_failure_new = cfocn_failure_new;
	}
	public Integer getInit_total() {
		return init_total;
	}
	public void setInit_total(Integer init_total) {
		this.init_total = init_total;
	}
	public Integer getInit_new() {
		return init_new;
	}
	public void setInit_new(Integer init_new) {
		this.init_new = init_new;
	}
	public Integer getInit_supernet_total() {
		return init_supernet_total;
	}
	public void setInit_supernet_total(Integer init_supernet_total) {
		this.init_supernet_total = init_supernet_total;
	}
	public Integer getZte_total() {
		return zte_total;
	}
	public void setZte_total(Integer zte_total) {
		this.zte_total = zte_total;
	}
	public Integer getZte_new() {
		return zte_new;
	}
	public void setZte_new(Integer zte_new) {
		this.zte_new = zte_new;
	}
	public Integer getEndwait_total() {
		return endwait_total;
	}
	public void setEndwait_total(Integer endwait_total) {
		this.endwait_total = endwait_total;
	}
	public Integer getEndwait_new() {
		return endwait_new;
	}
	public void setEndwait_new(Integer endwait_new) {
		this.endwait_new = endwait_new;
	}
	public Integer getEnd_today_total() {
		return end_today_total;
	}
	public void setEnd_today_total(Integer end_today_total) {
		this.end_today_total = end_today_total;
	}
	public Integer getEnd_today_new() {
		return end_today_new;
	}
	public void setEnd_today_new(Integer end_today_new) {
		this.end_today_new = end_today_new;
	}
	
}
