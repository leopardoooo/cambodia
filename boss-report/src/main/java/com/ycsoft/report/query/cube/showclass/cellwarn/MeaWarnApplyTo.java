package com.ycsoft.report.query.cube.showclass.cellwarn;

import com.ycsoft.report.query.cube.CellType;

public interface MeaWarnApplyTo extends  MeaWarnCheck{
	
	boolean applyTo(CellType celltype);
}
