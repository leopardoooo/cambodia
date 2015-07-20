/*
 * @(#) AAAException.java 1.0.0 2015年7月20日 下午4:58:04
 */
package com.ycsoft.boss.remoting.aaa;

import java.rmi.RemoteException;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResultHeader;

/**
 * 
 * AAA 接口请求异常类型定义
 * 
 * @author Killer
 */
@SuppressWarnings("serial")
public class AAAException extends RemoteException {

	private ResultHeader result;
	
	public AAAException(ResultHeader result){
		super(String.format("Error Code: %s, Desc: %s", result.getResultCode(),
				result.getResultDesc()));
		this.result = result;
	}
	
	public AAAException(Throwable e){
		super("AAA Service request failure.", e);
	}

	public ResultHeader getResult() {
		return result;
	}

	public void setResult(ResultHeader result) {
		this.result = result;
	}
}
