package com.ycsoft.report.query.cube;

import com.ycsoft.commons.exception.ReportException;

/**
 * 日期格式
 * @author new
 */
public enum DateCharacter {
	year("年","yyyy",false),half_year("半年","yyyy/2",true),
	quarter("季度","yyyy/4",true)
	,month("月","yyyymm",true),day("天","yyyymmdd",false);
	
	private String desc;//描述
	private String key;//日期格式键值
	private boolean year_on_year;//同比标志
	
	DateCharacter(String desc,String key,boolean year_on_year){
		this.desc=desc;
		this.key=key;
		this.year_on_year=year_on_year;
	}
	public String getDesc(){
		return this.desc;
	}
	
	public String getKey(){
		return this.key;
	}
	/**
	 * 是否支持同比标志
	 * @return
	 */
	public boolean isYear_on_year(){
		return this.year_on_year;
	}
	
	public DateCharacter valueOfByKey(String arg) throws ReportException{
		DateCharacter rdc=null;
		for(DateCharacter dc: DateCharacter.values()){
			if(dc.getKey().equals(arg)){
				rdc= dc;
				break;
			}
		}
		if(rdc==null)
			throw new ReportException(arg+" can not valueOf DateCharacter");
		return rdc;
	}
}
