package com.ycsoft.report.query.cube;

import java.io.Serializable;

/**
 * cube列映射
 */
public interface ColumnMapping extends Serializable {

	/**
	 * 获得cube和维、度量的映射关系中，cube的映射键值
	 * @return
	 */
	String getCubeMappingKey();
	/**
	 * 获得cube和维、度量的映射关系中，维和度量的映射键值
	 * @return
	 */
	String getColumnMappingKey();
	
}
