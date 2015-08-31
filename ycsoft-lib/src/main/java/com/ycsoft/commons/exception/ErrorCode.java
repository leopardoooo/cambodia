package com.ycsoft.commons.exception;

public enum ErrorCode {
	
	UNKNOW_EXCEPTION("未知异常，请联系管理员"),
	SystemNoOptr("缺失操作员信息"),
	
	ParamIsNull("参数为空！"),
	DataNumTooMuch("结果集大于2000条，请增加关键字搜索！"),
	CustAddressIsNull("地址为空"),
	CustDistrictIsNull("%s未配置对应的行政区域！"),
	CustProvinceIsNull("该行政区域未配置对应的省,行政区域编号=%s"),
	DeptAddrIsNull("部门(%s)未配置管辖的城市"),
	CustDataException("客户数据异常，请重新索搜客户！"),
	CustSeqIsNull("系统未正确配置客户序列号,客户前缀号=%s"),

	
	
	
	ExchangeConfigError("系统未正确配置汇率，请联系管理员"),
	
	NotCancleHasUnPay("产品含有未支付订单，不能退订"),
	NotCancelHasMoreBillingCycle("产品存在包多月优惠订单，不能退订"),
	NotCancelUserProtocol("设备协议期未结束，不能退订"),
	NotCancelIsPackageDetail("订单是套餐子产品，不能独立退订"),
	NotCancelOnlyTodayIsYou("只能取消当天自己操作的订单"),
	NotCancelStatusException("订单状态异常，不能退订"),
	
	UnPayOrderCancelBefor("请先取消订单号=%s的订单费用"),
	UnPayOrderCancelUnsubscribe("不能取消退订费用"),
	UnPayFeeTypeCanNotCancel("不支持此类型费用取消"),
	UnPayFeeHasPay("费用已支付不能取消"),
	UnPayAcctIsNotPublic("非公用账目不能取消充值、退款"),
	UnPayAcctRefundFeeAndChangeIsDiffer("账户退款和资金异动明细不一致"),
	UnPayLock("客户被锁定,请等待%s(%s)完成支付!"),
	UnPayIsOld("待支付金额已失效，请重新打开待支付界面"),
	
	FeeDateException("前后台金额不一致"),
	CFeeAndProdOrderIsNotOne("费用记录和订单信息不一致"),
	
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
	OrderTransUnPayPleaseCancel("被覆盖的订单存在未支付记录，请先取消订单号=%s的费用"),

	//depot
	DeviceRepeat("设备重复"),
	DeviceNotExists("设备不存在"),
	DeviceTotalNumIsNull("设备数量不够"),
	DeviceTotalNumIsTooBig("设备入库数量超出了订货数量"),
	
	AcctPublicNotExists("客户缺失公用账户，请联系管理员！"),
	AcctItemNotExists("账目不存在，请联系管理员！"),
	AcctFeeNotEnough("账户余额不足！"),
	AcctCanRefoudFeeNotEnough("账户可退余额不足！"),
	AcctBalanceError("资金余额出现负数！"),
	AcctItemAndActiveFeeDisagree("账户数据异常，账户资金和明细金额不一致,请联系管理员！"),
	AcctDebitFeeIsPositive("扣款金额不能是正数"),
	AcctAddFeeIsNotPositive("扣款金额不能是负数"),
	
	
	TemplateNotConfigBuseFee("该地区费用模板未配置该费用项(%s)"),
	CustUserIpAddressFeeCoinfigError("客户宽带IP收费存在多个费用项目，请联系管理员！"),
	;

	private ErrorCode(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		return this.desc;
	}
	
}
