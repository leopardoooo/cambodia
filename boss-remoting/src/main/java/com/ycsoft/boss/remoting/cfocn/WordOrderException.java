/*
 * @(#) AAAException.java 1.0.0 2015年7月20日 下午4:58:04
 */
package com.ycsoft.boss.remoting.cfocn;

import java.rmi.RemoteException;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResultHeader;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ResultHead;

/**
 * 
 * AAA 接口请求异常类型定义
 * 
 * @author Killer
 */
@SuppressWarnings("serial")
public class WordOrderException extends RemoteException {

	private ResultHead result;
	
	public WordOrderException(ResultHead result){
		super(String.format("Error Code: %s, Desc: %s", result.getHeadCode(),
				result.getHeadMsg()));
		this.result = result;
	}
	
	public WordOrderException(Throwable e){
		super("WordOrder Service request failure.", e);
	}

	public ResultHead getResult() {
		return result;
	}

	public void setResult(ResultHead result) {
		this.result = result;
	}

	
}
