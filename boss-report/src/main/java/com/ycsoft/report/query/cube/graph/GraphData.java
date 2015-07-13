package com.ycsoft.report.query.cube.graph;

import java.io.Serializable;
import java.util.List;

/**
title: '新增用户按优惠类型统计',
subtitle: '广西广电南宁分公司',
xtitle: '2011年',
ytitle: '用户数(户)',
categories: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
series: [{
	name: '一般用户',
	data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
}, {
	name: '低保用户',
	data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
}]
 */
public class GraphData implements Serializable {
	/**
	 * 前台图形类型
	 */
	private GraphType type;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 副标题
	 */
	private String subtitle;
	/**
	 * x轴标题
	 */
	private String xtitle;
	/**
	 * y轴标题
	 */
	private String ytitle;
	/**
	 * x轴取值
	 */
	private List<String> categories;
	/**
	 * 内容取值
	 */
	private List<Serie> series;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getXtitle() {
		return xtitle;
	}
	public void setXtitle(String xtitle) {
		this.xtitle = xtitle;
	}
	public String getYtitle() {
		return ytitle;
	}
	public void setYtitle(String ytitle) {
		this.ytitle = ytitle;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<Serie> getSeries() {
		return series;
	}
	public void setSeries(List<Serie> series) {
		this.series = series;
	}
	public GraphType getType() {
		return type;
	}
	public void setType(GraphType type) {
		this.type = type;
	}
}
