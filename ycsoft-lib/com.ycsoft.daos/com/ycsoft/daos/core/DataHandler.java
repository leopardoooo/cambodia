/*
 * @(#)DataHandler.java 1.0.0 Jul 7, 2011 7:01:26 PM 
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.ycsoft.daos.core;

import java.util.List;


/**
 * 从游标中读取了一部分数据然后交给数据处理器进行处理，再次读取部分，
 * 重复上一步骤，直至游标数据读完
 * 
 * @see PartResultSetExtractor
 * @author allex
 */
public interface DataHandler<T> {

	
	/**
	 * 处理批量数据
	 * 
	 * @param results 结果集
	 * @param fetchCount 当前fetch的次数
	 * @throws Exception
	 */
	void fetchRows(List<T> results, int fetchCount)throws Exception;
	
}
