package com.ycsoft.report.query.datarole;
/**
 * 功能权限
 * @author new
 *
 */
public interface FuncRole {
	/**
	 * 判断是否具有该项功能
	 * @param func
	 * @return
	 */
	public  boolean hasFunc(FuncType func);
}
