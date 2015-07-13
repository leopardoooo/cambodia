package com.ycsoft.report.query.cube.graph;

import java.io.Serializable;
import java.util.List;
/**
 * GraphData内容取值单元
 */
public class Serie implements Serializable {
	private String name;
	private List<Number> data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Number> getData() {
		return data;
	}
	public void setData(List<Number> data) {
		this.data = data;
	}
}
