package com.ycsoft.report.query.cube.showclass.cellwarn;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeCell;

public interface MeaWarnCheck {

	boolean check(CubeCell[] cells) throws ReportException;

}
