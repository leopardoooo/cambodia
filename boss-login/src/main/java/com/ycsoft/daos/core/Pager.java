package com.ycsoft.daos.core;

import java.io.Serializable;
import java.util.List;

/**
 * <p> 分页控制类，包含分页的相关信息，如总页数 </p>
 * @author hh
 * @param <T> 结果集的类型
 */
public class Pager<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6432245873739957544L;
	//查询参数
	private T params ;
	//开始位置，
	private Integer start ;
	//显示的条数
	private Integer limit ;
	//符合条件的结果集
	private List<T> records;
	//总条数
	private Integer totalProperty ;

	public Pager(T params, Integer start, Integer limit) {
		super();
		this.params = params;
		this.start = start;
		this.limit = limit;
	}

	public Pager(){
	}

	/**
	 * 构造函数传入总记录数及当前的结果集
	 */
	public Pager(List<T> records, Integer totalProperty) {
		super();
		this.records = records;
		this.totalProperty = totalProperty;
	}

	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}
	public Integer getTotalProperty() {
		return totalProperty;
	}
	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}
	public T getParams() {
		return params;
	}
	public void setParams(T params) {
		this.params = params;
	}
}
