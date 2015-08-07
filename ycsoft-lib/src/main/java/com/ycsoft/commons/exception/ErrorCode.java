package com.ycsoft.commons.exception;

public enum ErrorCode {
	ParamIsNull("Error:参数不能未空！"),
	CustDataException("Error:客户数据异常，请重新索搜客户！"),
	UnPayLock("Error:客户被锁定,请等待%s(%s)完成支付!"),
	
	NotCancleHasUnPay("Error:产品含有未支付订单，不能退订"),
	NotCancelHasMoreBillingCycle("Error:产品存在包多月优惠订单，不能退订"),
	NotCancelUserProtocol("Error:设备协议期未结束，不能退订"),
	NotCancelIsPackageDetail("Error:订单是套餐子产品，不能独立退订"),
	NotCancelOnlyTodayIsYou("Error:只能取消当天自己操作的订单"),
	FeeDateException("Error:前后台金额不一致"),
	CFeeAndProdOrderIsNotOne("Error:费用记录和订单信息不一致"),
	UnPayOrderCancelBefor("请先取消订单号=%s的订单费用"),
	UnPayOrderCancelUnsubscribe("Error:不能取消退订费用"),
	OrderNotExists("Error:订单不存在"),
	OrderDateException("Error:订单数据异常，请联系厂商！");
	;

	private ErrorCode(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		return this.desc;
	}
	
}
