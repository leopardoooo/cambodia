package com.ycsoft.report.query.cube.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.query.menory.MemoryCacheInit;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionLevel;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

public class DimensionLevelValueManage implements MemoryCacheInit {

	private static  Map<DimensionLevel,List<QueryKeyValue>> levelValueList=new HashMap<DimensionLevel,List<QueryKeyValue>>();
	
	private static Map<DimensionLevel,Map<String,QueryKeyValue>> levelValueMap=new HashMap<DimensionLevel,Map<String,QueryKeyValue>>();
	
	public  String initAll() {
			StringBuilder buffer=new StringBuilder();
			for(Dimension dim: DimensionManage.getDimList()){
				for(int i=1;i<=dim.getLevelNum();i++){
					try{
						levelValueList.put(dim.getLevel(i), dim.getLevelValuesByDatabase(i));
						
						Map<String,QueryKeyValue> levelMap=new HashMap<String,QueryKeyValue>();
						for(QueryKeyValue vo:levelValueList.get(dim.getLevel(i)))
							levelMap.put(vo.getId(), vo);
						levelValueMap.put(dim.getLevel(i), levelMap);
					}catch(Exception e1){
						buffer.append("rep_dimension_level(").append(dim.getId()).append(")_initlevel(").append(i).append(")_error:").append(e1.getMessage()).append(";");
						LoggerHelper.error(this.getClass(), "rep_dimenison_level_init_error", e1);
					}
				}
			}
			return buffer.toString();
	}
	
	public static List<QueryKeyValue> getDimLevelValueList(DimensionLevel level){
		return levelValueList.get(level);
	}
	
	public static Map<String,QueryKeyValue> getDimLevelValueMap(DimensionLevel level){
		return levelValueMap.get(level);
	}

}
