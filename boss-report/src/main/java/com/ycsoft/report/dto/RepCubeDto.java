package com.ycsoft.report.dto;

import com.ycsoft.report.bean.RepCube;

public class RepCubeDto extends RepCube {
	/**
	 * 字段类型
	 * String,NUMBER
	 */
	private String attribute_type;

	public String getAttribute_type() {
		return attribute_type;
	}

	public void setAttribute_type(String attribute_type) {
		this.attribute_type = attribute_type;
	}
}
