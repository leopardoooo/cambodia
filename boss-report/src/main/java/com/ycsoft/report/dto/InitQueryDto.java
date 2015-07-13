package com.ycsoft.report.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.ycsoft.report.bean.RepColumn;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.bean.RepGroup;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.bean.RepTotal;
import com.ycsoft.report.pojo.Parameter;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.impl.CubeHeadCellImpl;
import com.ycsoft.report.query.key.Impl.ConKeyValue;

/**
 * 初始化查询数据
 * @author new
 */
public class InitQueryDto extends RepDefine {
	
	public InitQueryDto(){}
	public InitQueryDto(RepDefine repDefine,Parameter para){
		BeanUtils.copyProperties(repDefine,this);
		setKeyvaluelist(para.getRepkeys());
		setHeaddatacells(para.getHeaddatacells());
		setHistory_query_id(para.getHistory_query_id());
		
	}
	/**
	 * 内容缓存查询id
	 */
	private String cache_query_id;
	/**
	 * 历史查询的query_id
	 * @deprecated
	 */
	private String history_query_id;

	//seq,后台初始化
	private String query_id;
	
	//资源名称
	private String rep_name;
	
	//查询条件
	private List<ConKeyValue> keyvaluelist;
	
	//自定义报表头
	private List<RepHead> headlist;
	
	//原始查询语句
	private String sql;
	//明细报表合计项字段数组
	private String[] totals;
	
	private List<RepTotal> reptotals;
	/**
	 * 分组统计列
	 */
	private String group;
	
	private RepGroup repgroup;
	
	/**
	 * 列属性定义(分组统计，合计，维度相关)
	 * @deprecated
	 */
	private List<RepColumn> columnlist;
	/**
	 * cube点击数据获取的数据项值，用于明细报表计算
	 */
	private List<CubeHeadCellImpl> headdatacells;

	public List<RepColumn> getColumnlist() {
		return columnlist;
	}

	public void setColumnlist(List<RepColumn> columnlist) {
		this.columnlist = columnlist;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<RepHead> getHeadlist() {
		return headlist;
	}

	public void setHeadlist(List<RepHead> headlist) {
		this.headlist = headlist;
	}

	public String getQuery_id() {
		return query_id;
	}

	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}

	public String getRep_name() {
		return rep_name;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}

	public List<ConKeyValue> getKeyvaluelist() {
		return keyvaluelist;
	}

	public void setKeyvaluelist(List<ConKeyValue> keyvaluelist) {
		this.keyvaluelist = keyvaluelist;
	}

	public String getHistory_query_id() {
		return history_query_id;
	}

	public void setHistory_query_id(String history_query_id) {
		this.history_query_id = history_query_id;
	}

	public String[] getTotals() {
		return totals;
	}

	public void setTotals(String[] totals) {
		this.totals = totals;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<RepTotal> getReptotals() {
		return reptotals;
	}

	public void setReptotals(List<RepTotal> reptotals) {
		this.reptotals = reptotals;
	}

	public RepGroup getRepgroup() {
		return repgroup;
	}

	public void setRepgroup(RepGroup repgroup) {
		this.repgroup = repgroup;
	}

	public List<CubeHeadCellImpl> getHeaddatacells() {
		return headdatacells;
	}

	public void setHeaddatacells(List<CubeHeadCellImpl> headdatacells) {
		this.headdatacells = headdatacells;
	}

	public String getCache_query_id() {
		return cache_query_id;
	}

	public void setCache_query_id(String cache_query_id) {
		this.cache_query_id = cache_query_id;
	}

}
