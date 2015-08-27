package com.ycsoft.commons.exception;

public enum ErrorCode {
	
	UNKNOW_EXCEPTION("未知异常，请联系管理员"),
	
	ParamIsNull("参数为空！"),
	DataNumTooMuch("数据量过大！%s"),
	CustAddressIsNull("地址为空"),
	CustDistrictIsNull("%s未配置对应的行政区域！"),
	CustProvinceIsNull("该行政区域未配置对应的省,行政区域编号=%s"),
	DeptAddrIsNull("操作员所在部门,未在部门和地区对应表s_dept_addr配置,部门编号=%s"),
	CustDataException("客户数据异常，请重新索搜客户！"),
	CustSeqIsNull("系统未正确配置客户序列号,客户前缀号=%s"),
	UnPayLock("客户被锁定,请等待%s(%s)完成支付!"),
	
	ExchangeConfigError("系统未正确配置汇率，请联系管理员"),
	
	NotCancleHasUnPay("产品含有未支付订单，不能退订"),
	NotCancelHasMoreBillingCycle("产品存在包多月优惠订单，不能退订"),
	NotCancelUserProtocol("设备协议期未结束，不能退订"),
	NotCancelIsPackageDetail("订单是套餐子产品，不能独立退订"),
	NotCancelOnlyTodayIsYou("只能取消当天自己操作的订单"),
	NotCancelStatusException("订单状态异常，不能退订"),
	
	FeeDateException("前后台金额不一致"),
	CFeeAndProdOrderIsNotOne("费用记录和订单信息不一致"),
	UnPayOrderCancelBefor("请先取消订单号=%s的订单费用"),
	UnPayOrderCancelUnsubscribe("不能取消退订费用"),
	OrderNotExists("订单不存在"),
	OrderTodayHasCancel("订单已取消，不能再次取消"),
	
	ProdNotExists("产品不存在"),
	ProdIsInvalid("产品已失效"),
	
	OrderDateCanNotUp("产品不能升级"),
	OrderDateException("订单数据异常，请联系管理员!(order_sn=%s)"),
	OrderDatePackageConfig("订单的套餐配置数据错误，请联系管理员!"),
	OrderDateUserNotCust("订单的存在异常终端数据，请联系管理员!(user_id=%s)"),
	OrderDatePackageUserLimit("订单的套餐终端选择数超过套餐定义限制"),
	OrderDateLastOrderNotCust("上期订购记录和当前客户不一致"),
	OrderDateLastOrderNotUser("上期订购记录和当前终端用户不一致"),
	OrderDateLastOrderIsLost("上期订购记录已失效，请重新打开订购界面"),
	OrderDateEffDateError("开始计费日错误"),
	OrderDateExpDateError("结束计费日错误"),
	OrderDateOrderMonthError("订购月数不能填0或订购月数必须是资费周期的倍数"),
	OrderPackageHasSingleUserParam("订购套餐时，单用户参数不能填"),
	
	OrderFeeDisagree("订单金额和明细不一致，请联系管理员！(order_sn=%s)"),
	
	
	//depot
	DeviceRepeat("设备重复"),
	DeviceNotExists("设备不存在"),
	DeviceTotalNumIsNull("设备数量不够"),
	DeviceTotalNumIsTooBig("设备入库数量超出了订货数量"),
	
	CustHasNoAcct("客户缺失公用账户，请联系管理员！"),
	;

	private ErrorCode(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		return this.desc;
	}
	
}
