package com.ycsoft.report.query.datarole;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.exception.ReportException;

/**
 * 权限初始化接口
 * 操作员登陆时加载权限
 */
public interface DataRoleInit {
	/**
	 * 数据权限初始化
	 * 从session中提取操作员权限
	 * @param optr
	 * @throws ReportException 
	 */
	DataRole setDataRole(SOptr optr) throws ReportException;
}
