package com.ycsoft.commons.exception;

public enum ErrorCodeConstants {
	ParamIsNull("Error:参数不能未空！"),
	CustDataException("Error:客户数据异常，请重新索搜客户！"),
	UnPayLock("Error:客户被锁定,请等待%s(%s)完成支付!"),
	NotCancleHasUnPay("Error:产品含有未支付订单，不能终止"),
	NotCancelHasMoreBillingCycle("Error:产品还有包多月优惠订单，不能终止"),
	NotCancelUserProtocol("Error:设备协议期未结束，不能终止"),
	FeeDateException("Error:前后台金额不一致"),
	;

	private ErrorCodeConstants(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		return this.desc;
	}
	
}
