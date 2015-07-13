package com.ycsoft.report.query.cube;

/***********************************************************************
 * Module:  DimensionLevel.java
 * Author:  new
 * Purpose: Defines the Interface DimensionLevel
 ***********************************************************************/


/** 维层级定义接口
 * 
 * @pdOid 7a0a5ee2-bd3f-4ca7-bfe1-193c692cbd73 */
public interface DimensionLevel {
   /**
    *层级
    **/
   int getLevel();
   /**
    * 层名称
    * @return
    */
   String getName();
   
   /** 
    * id 数据库取值代码 
    **/
   String getColumn_code();
   /** 
    * name 数据库取值描述
    **/
   String getColumn_text();
   /**
    * id 数据库取值对应的父级代码
    * @return
    */
   String getColumn_pid();
   /**
    * 数据库取值表
    * @return
    */
   String getColumn_table();

   /**
    * 维度层排序方式
    * @return
    */
   String getColumn_order();
   /**
    * 数据权限控制定义键值
    * @return
    */
   String getDataRoleKey();
   /**
    * 内存缓存键值
    * @return
    */
   String getMemoryCacheKey();

}