/*
 * @(#) AAAResultUtils.java 1.0.0 2015年7月20日 下午4:47:12
 */
package com.ycsoft.boss.remoting.aaa;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResultHeader;

/**
 * 华为AAA接口的响应结果处理
 * @author Killer
 */
public class AAAResultUtils {

	private static String DEFAULT_SUCCESS_CODE = "405000000";
	
	
	/**
	 * 根据结果集状态吗判断是否业务处理成功
	 * @param result
	 * @return
	 */
	public static boolean success(ResultHeader result){
		return DEFAULT_SUCCESS_CODE.equals(result.getResultCode());
	}
	
}
