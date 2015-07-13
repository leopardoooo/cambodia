package com.ycsoft.report.query.treequery;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepDimKey;
import com.ycsoft.report.query.key.BaseKey;

/**
 * 日期结构的维度key
 * 格式参照oracle_sql:to_char(sysdate,'yyyymmdd') 非此格式会出问题
 * @author new
 *
 */
public class DimSubStr extends BaseKey implements DimKey {
	

	private int str_length;
	
	private int p_str_length;
	
	public DimSubStr(RepDimKey repDimKey,DimSubStr pdimkey) throws ReportException{
		if(repDimKey==null)
			throw new ReportException("repDimKey is null");
		super.setKey(repDimKey.getKey());
		super.setDesc(repDimKey.getName());

		if(pdimkey!=null){
			super.setPkey(pdimkey.getKey());
			pdimkey.setSkey(repDimKey.getKey());
			this.p_str_length=pdimkey.getStrlength();
		}
		this.str_length=Integer.parseInt(repDimKey.getValuedefine());
		
	}
	
	private int getStrlength(){
		return this.str_length;
	}
	public String getPid(String id) {
		return id.substring(0,p_str_length);
	}

	public String getName(String id) {
		return id;
	}
	
	public BaseKey getBaseKey() {
		BaseKey baseKey=new BaseKey();
		baseKey.setDesc(super.getDesc());
		baseKey.setKey(super.getKey());
		baseKey.setPkey(super.getPkey());
		baseKey.setSkey(super.getSkey());
		return baseKey;
	}
}
