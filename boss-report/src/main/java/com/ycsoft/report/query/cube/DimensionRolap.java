package com.ycsoft.report.query.cube;

import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * cube中维设置接口
 */
public interface DimensionRolap extends ColumnMapping  {

	 String getId();
	 
	 String getName();
	 
	 Dimension getDim() ;

	 boolean isUsesign() ;

	 boolean isVerticalsign() ;

	 int getLevel() ;
	 	 
	 /**
	  * 切片只能是对一个层进行
	  * @return
	  */
	 Integer getSlices_level();
	 
	 /**
	  * 切片值
	  * @return
	  */
	 String[] getSlices_value();
	 
	 /**
	  * 获得维度一个层级的取值
	  * 被切片影响
	  * @param level
	  * @return
	 * @throws ReportException 
	  */
	 List<QueryKeyValue> queryLevelValue(int level) throws ReportException;
	 /**
	  * 获得维度的一个层级是否合计
	  * @param level
	  * @return
	  */
	 Boolean isLevelTotal(int level);
	 /**
	  * 是否启用了环同指标
	  * @return
	  */
	 boolean isDate_compare();
	 
	 Map<Integer,String[]> getLevel_sort_map();
	 
}
