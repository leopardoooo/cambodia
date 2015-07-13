package com.ycsoft.report.pojo;

import java.util.List;

import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.dto.RepDefineDto;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.cube.impl.CubeHeadCellImpl;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.key.Impl.ConKeyValue;


/**
 *  前台参数的统一封装基类
 */
public class Parameter {
	
	/**
	 * 历史查询ID
	 */
	private String history_query_id;
	

	//查询组件回传数据
	private List<ConKeyValue> repkeys;
	/**
	 * cube导航配置
	 */
	private MyCube mycube;
	/**
	 * cube定义
	 */
	private List<RepCube> repcube;
	
	/**
	 * cube点击数据获取的数据项值，用于明细报表计算
	 */
	private List<CubeHeadCellImpl>  headdatacells;
	
	private RepDefineDto repdefinedto;
	
	public RepDefineDto getRepdefinedto() {
		return repdefinedto;
	}
	public void setRepdefinedto(RepDefineDto repdefinedto) {
		this.repdefinedto = repdefinedto;
	}
	public List<CubeHeadCellImpl> getHeaddatacells() {
		return headdatacells;
	}
	public void setHeaddatacells(List<CubeHeadCellImpl> headdatacells) {
		this.headdatacells = headdatacells;
	}
	public MyCube getMycube() {
		return mycube;
	}
	public void setMycube(MyCube mycube) {
		this.mycube = mycube;
	}
	public String getHistory_query_id() {
		return history_query_id;
	}
	public void setHistory_query_id(String history_query_id) {
		this.history_query_id = history_query_id;
	}

	public List<ConKeyValue> getRepkeys() {
		return repkeys;
	}
	public void setRepkeys(List<ConKeyValue> repkeys) {
		this.repkeys = repkeys;
	}
	public List<RepCube> getRepcube() {
		return repcube;
	}
	public void setRepcube(List<RepCube> repcube) {
		this.repcube = repcube;
	}
}
