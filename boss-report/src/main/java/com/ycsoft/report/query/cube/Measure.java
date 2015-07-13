package com.ycsoft.report.query.cube;

/***********************************************************************
 * Module:  Measure.java
 * Author:  new
 * Purpose: Defines the Interface Measure
 ***********************************************************************/


/** 
 * 指标接口
 */
public interface Measure {
   /** 
    * 指标对应数据体列ID
    */
   String getColumnCode();
   /** 
    * 指标说明
    */
   String getColumnText();
   /** 
    * 指标聚集计算方法
    */
   MeasureGather getCalculation();
   /**
    * 指标明细报表
    * @return
    */
   String getMeaRepId();
   /**
    * 指标数值显示格式
    * @return
    */
   MeasureDataType getDateType();
   /**
    * 是否可编辑指标
    * @return
    */
   Boolean	getMeaCustom();

}