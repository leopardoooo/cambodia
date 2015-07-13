package com.ycsoft.report.query.cube.impl;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.report.bean.RepDimensionLevel;
import com.ycsoft.report.query.cube.DimensionLevel;
/**
 * 维层级实现类
 */
public class DimensionLevelImpl extends RepDimensionLevel implements
		DimensionLevel {

	public DimensionLevelImpl(RepDimensionLevel level) throws ReportException{
		try {
			BeanHelper.copyProperties(this, level);
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}

	public int getLevel() {
		return super.getDim_level();
	}

	public String getName() {
		return this.getDim_level_name();
	}

	public String getColumn_order() {
		return this.getColumn_code();
	}

	public String getDataRoleKey() {
		return this.getKey_level();
	}

	public String getMemoryCacheKey() {
		return this.getMemorykey();
	}

}
