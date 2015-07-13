package com.ycsoft.report.query.treequery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepDimKey;
import com.ycsoft.report.query.key.BaseKey;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 树结构的维度key
 * @author new
 *
 */
public class DimDataTree extends BaseKey implements DimKey {
	
	private Map<String,QueryKeyValue> valuemap;
	

	public DimDataTree(RepDimKey repDimKey,List<QueryKeyValue> list,DimDataTree pdimkey) throws ReportException{
		if(repDimKey==null)
			throw new ReportException("key is null");
		if(list==null||list.size()==0)
			throw new ReportException("List<QueryKeyValue> is null");

		super.setKey(repDimKey.getKey());
		super.setDesc(repDimKey.getName());
		
		if(pdimkey!=null){
			super.setPkey(pdimkey.getKey());
			pdimkey.setSkey(repDimKey.getKey());
		}
		valuemap=new HashMap<String,QueryKeyValue>();
		for(QueryKeyValue qkv:list)
			valuemap.put(qkv.getId(), qkv);
	}
	
	/**
	 * 获得上一级的对应的值
	 * @param value
	 * @return
	 * @throws ReportException 
	 */
	public String getPid(String id) throws ReportException{
		try{
			return valuemap.get(id).getPid();
		}catch(Exception e){
			throw new ReportException(this.getKey()+" is not config id="+id,e);
		}
	}
	/**
	 * id对应的描述
	 * @throws ReportException 
	 */
	public String getName(String id) throws ReportException {
		try{
			return valuemap.get(id).getName();
		}catch(Exception e){
			throw new ReportException(this.getKey()+" is not config id="+id,e);
		}
	}
	
	public BaseKey getBaseKey(){
		BaseKey baseKey=new BaseKey();
		baseKey.setDesc(super.getDesc());
		baseKey.setKey(super.getKey());
		baseKey.setPkey(super.getPkey());
		baseKey.setSkey(super.getSkey());
		return baseKey;
	}
}
