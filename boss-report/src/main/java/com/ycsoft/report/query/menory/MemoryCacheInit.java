package com.ycsoft.report.query.menory;

import com.ycsoft.commons.exception.ReportException;

/***********************************************************************
 * Module:  MemoryCacheInit.java
 * Author:  new
 * Purpose: Defines the Interface MemoryCacheInit
 ***********************************************************************/


/** 内存缓存初始化接口
 * @pdOid af3a8873-9d0e-447f-8371-70c54d58bd2d */
public interface MemoryCacheInit {
   /** 内存缓存初始化接口
    * 初始化错误使用异常或者返回值判断（返回值=""表示无异常）
    * @pdOid b4639664-2b15-436f-8619-dee48c9de24c */
   public String initAll() throws ReportException;

}