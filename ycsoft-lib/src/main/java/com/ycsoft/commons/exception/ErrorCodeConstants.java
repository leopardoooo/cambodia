package com.ycsoft.commons.exception;

public enum ErrorCodeConstants {
	ParamIsNull("Error:参数不能未空！"),
	CustDataException("Error:客户数据异常，请重新索搜客户！"),
	UnPayLock("Error:客户被锁定,请等待%s(%s)完成支付!");

	private ErrorCodeConstants(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		return this.desc;
	}
	
}
