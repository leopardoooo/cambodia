package com.ycsoft.report.query.cube;

/***********************************************************************
 * Module:  Dimension.java
 * Author:  new
 * Purpose: Defines the Interface Dimension
 ***********************************************************************/

import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/** 维定义接口
 **/
public interface Dimension {
   /** 
    * 提取维度的一个层级
    * @param level
    **/
   DimensionLevel getLevel(int level);
   /**
    * 取值表
    **/
   String getTabel();
   /** 
    * 根据父级ID过滤一个层级的取值
    * 从内存中获取
	* 权限控制
	* 指定父级ID
    * @param level
    * @throws ReportException 
    **/
   List<QueryKeyValue> getLevelValues(int level,String...pids) throws ReportException ;
   /**
    * 从数据库获得一个层的取值
    * @param level
    * @return
    * @throws ReportException
    */
   List<QueryKeyValue> getLevelValuesByDatabase(int level) throws ReportException;
   /** 维度定义描述*/
   String getName();
   /** 维度定义值 */
   String getId();
   /** 
    * 获得维的层数
    **/
   int getLevelNum();
   /**
    * 维的映射键值
    * @return
    */
   String getMappingKey();
   /**
    * 明细报表对应%ID%
    * @return
    */
   String getPrefixid();
   /**
    * 获得层级权限控制map
    * @return
    * @throws ReportException
    */
   Map<DimensionLevel,List<QueryKeyValue>> getDimLevelControlMap() throws ReportException;
   /**
    * 是否日期维度
    * @return
    */
   boolean isDateDim();
   
   String getDatabase();
}