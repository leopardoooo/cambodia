package com.ycsoft.commons.exception;

import com.ycsoft.commons.store.MemoryDict;

public enum ErrorCode {
	
	UNKNOW_EXCEPTION("未知异常，请联系管理员"),
	SystemNoOptr("缺失操作员信息"),
	
	ParamIsNull("参数为空！"),
	DataNumTooMuch("结果集大于2000条，请增加关键字搜索！"),
	CustAddressIsNull("地址为空"),
	CustDistrictIsNull("%s未配置对应的行政区域！"),
	CustProvinceIsNull("该行政区域未配置对应的省,行政区域编号=%s"),
	CustProvinceDomainIsNull("%s未配置宽带域名后缀,请联系管理员！"),
	DeptAddrIsNull("部门(%s)未配置管辖的城市"),
	CustDataException("客户数据异常，请重新索搜客户！"),
	CustSeqIsNull("系统未正确配置客户序列号,客户前缀号=%s"),

	NoUserExistsOrBelong2CurrentCust("用户不存在,或者不在当前客户名下"),
	
	
	ExchangeConfigError("系统未正确配置汇率，请联系管理员"),
	ExchangeConfigExits("该日汇率已存在不能重复添加"),
	ExchangeConfigINvalid("汇率已作废不能再次修改"),
	
	NotCancleHasUnPay("产品含有未支付订单，不能退订"),
	NotCancelHasMoreBillingCycle("产品存在包多月优惠订单，不能退订"),
	NotCancelUserProtocol("设备协议期未结束，不能退订"),
	NotCancelIsPackageDetail("订单是套餐子产品，不能独立退订"),
	NotCancelOnlyTodayIsYou("只能取消当天自己操作的订单"),
	NotCancelStatusException("订单状态异常，不能退订"),
	NotCancleTheOrderBefor("请先退订订单号=%s的订单"),
	
	UnPayOrderCancelBefor("请先取消订单号=%s的订单费用"),
	UnPayOrderCancelUnsubscribe("不能取消退订费用"),
	UnPayFeeTypeCanNotCancel("不支持此类型费用取消"),
	UnPayFeeHasPay("费用已支付不能取消"),
	UnPayAcctIsNotPublic("非公用账目不能取消充值、退款"),
	UnPayAcctRefundFeeAndChangeIsDiffer("账户退款和资金异动明细不一致"),
	UnPayLock("客户被锁定,请等待%s(%s)完成支付!"),
	UnPayIsOld("待支付金额已失效，请重新打开待支付界面"),
	UnPayHasEdit("对应订单金额被修改，不能取消，请使用订单修改功能更正费用"),
	
	PayCancelOnlyPayOptr("只有支付操作员可以操作回退支付"),
	//PayCancelInvoiceIsCheck("发票已结账，不能回退支付"),InvoiceCheckStatusIsNotIdle
	PayHasCancel("支付已回退，不能再次回退"),
	PayFeeStatusError("支付对应的缴费记录状态异常，不能回退"),
	PayCancelInvoiceParamError("前后台发票不一致"),
	PayFeeHasCancelOrder("支付关联的订购订单费用或修改订单费用存在订单被退订情况，不能回退"),
	
	FeeDateException("前后台金额不一致"),
	CFeeAndProdOrderIsNotOne("费用记录和订单信息不一致"),
	
	OrderNotExists("订单不存在"),
	OrderTodayHasCancel("订单已退订，不能再次退订"),
	
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
	OrderDateFeeError("订单应支付金额错误"),
	OrderPackageHasSingleUserParam("订购套餐时，单用户参数不能填"),
	OrderFeeDisagree("订单金额和明细不一致，请联系管理员！(order_sn=%s)"),
	OrderTransUnPayPleaseCancel("被覆盖的订单存在未支付记录，请先取消订单号=%s的费用才能升级"),
	OrderDateCanNotUpWhyPak("产品不能升级,因为存在有效的套餐子产品"),

	OrderStatusException("订单状态异常"),
	OrderEditOnlyUnPay("只有未支付的订单才可以修改退款"),
	OrderEditNoProd("订单已支付，不能修改"),
	OrderEditIsPakDetail("订单是套餐子产品，不能修改"),
	
	OttFzdNotMoreThanTwo("OTT主终端下只能挂2个副终端"),
	//task
	TaskDeviceIsNull("设备未回填"),
	TaskDeviceModelIsNull("该工单用户的设备型号不存在"),
	TaskDeviceModelIsWrong("%s不是工单所需要的型号"),
	
	//depot
	DeviceRepeat("设备重复"),
	DeviceNotExists("设备不存在"),
	DeviceTotalNumIsNull("设备数量不够"),
	DeviceTotalNumIsTooBig("设备入库数量超出了订货数量"),
	DeviceNotInRightDepot("%s设备在%s,不在当前仓库"),
	DeviceDateException("设备数据异常，请联系管理员!(device_id=%s)"),
	DevicePairModelNotExists("设备对应的智能卡型号不存在!(device_model=%s)"),
	DeviceNotSupUserType("%s设备不支持%s用户"),
	
	AcctPublicNotExists("客户缺失公用账户，请联系管理员！"),
	AcctItemNotExists("账目不存在，请联系管理员！"),
	AcctFeeNotEnough("账户余额不足！"),
	AcctCanRefoudFeeNotEnough("账户可退余额不足！"),
	AcctBalanceError("资金余额出现负数！"),
	AcctItemAndActiveFeeDisagree("账户数据异常，账户资金和明细金额不一致,请联系管理员！"),
	AcctDebitFeeIsPositive("扣款金额不能是正数"),
	AcctAddFeeIsNotPositive("扣款金额不能是负数"),
	
	InvoiceIsNotYou("只有收据领用人可以操作"),
	InvoiceCheckStatusIsNotIdle("收据已结账或核销，不能操作"),
	InvoiceNotExists("收据不存在"),
	
	
	TemplateNotConfigBuseFee("该地区费用模板未配置该费用项(%s)"),
	CustUserIpAddressFeeCoinfigError("客户宽带IP收费存在多个费用项目，请联系管理员！"),
	UserLoginNameIsExists("账号名称已存在!"),
	CustStatusIsNotOpenUser("意向客户不能直接开用户，请先修改地址!"),
	OTTIsNotSingle("OTT用户不能使用单向设备!"),
	DTTIsNotDouble("DTT用户不能使用双向设备!"),
	
	UserLoginNameIsNotExistsOrIsNotOttMobile("账号不存在或不是ott_mobile用户"),
	EditFeeUnPayError("未支付中存在购买配件或批量购买配件操作，请使用取消订单功能"),
	BusiCodeCanNotCancel("该业务不能回退（%s）"),
	
	TServerOttauthProdNotConfig("控制字%s未在t_server_ottauth_prod中定义"),
	
	OttMobileProdOnlyOneControlRes("OttMobile产品(prod_id=%s)必须配置一个控制字，且只能配置一个控制字"),
	
	/**OTT接口相关错误码**/
	E40001("Mac地址认证失败（提示用户，不能进入）"),
	E40002("EPG认证失败，返回Guest帐号"),
	E40003("token认证失败，返回Guest帐号"),
	E40004("token 认证成功，用户认证成功。"),
	E40005("无产品绑定免费"),
	E40006("参数错误"),
	E40007("IP地址非法"),
	E40009("其它错误"),
	E40010("有产品绑定，且用户已经购买，当前时间有效"),
	E40011("续订/取消续订操作失败"),
	E40012("IP地址认证失败（提示用户，不能进入）针对运营商的需求限制IP"),
	E40013("设备被禁止"),
	E20000("成功"),
	E20001("用户或密码错误"),
	E20002("充值失败,充值码错误"),
	E20003("请求超时,请返回重试"),
	E20004("产品鉴权失败"),
	E20005("余额不足"),
	
	//OTT授权相关错误
	ResIsNull("控制字为空"),
	ResOttIsError("OTT控制字格式错误"),
	CmdTypeUnDefined("授权类型(%s)未定义,请联系管理员"),
	
	ReceiptNotExists("收据不存在或者不在当前库"),
	ReceiptNotRecipients("收据[%s]未领用"),
	ReceiptAlreadyCheckout("收据已结账"),
	ReceiptIsUsed("收据[%s]已使用"),
	UserIsNotExists("用户不存在"),
	UserStatusIsNotActiveNotStop("用户[%s]不是正常状态，不能报停"),
	PackageUserMustToOperation("归属套餐的用户必须同时报停"),
	BandUserStopOhterOttUserMustStop("宽带用户报停,其他OTT用户都需要报停"),
	UserStatusIsNotReqstopNotOpen("用户[%s]不是报停状态，不能报开"),
	OttUserOpenOhterBandUserMustOpen("OTT用户报开,其他宽带用户都需要报开"),
	ForecastStopUserIsNotExists("预报停用户不存在"),	
	OttUserCancelForecastStopBandUserAlso("OTT用户取消预报停,其他宽带用户都需要取消预报停"),
	
	TimePointFormatIsError("时间格式错误"),
	DataIsNullCheckFile("数据不存在,请检查文件"),
	CardHaveSpecialCharacter("该卡号[%s]含有特殊字符!"),
	DuplicateCardNumber("重复的卡号%s"),
	FileContentCommentLineNumberIsError("文件内容注释行数不对"),
	
	//======================未翻译===========================
	NoStopRecord("找不到产品报停记录，请联系管理员"),
	TaskDeviceUnFill("有未回填设备的用户，不能完工"),
	GDDEviceNotOff("supernet产权的设备不能销户，请先拆机或购买设备!"),
	UserStatusNotOff("用户状态为正常并且设备购买方式为购买，或者用户状态为拆机完成才能销户!"),
	InvoiceNotUse("收据未使用或已作废"),
	InvoiceTemplateDeptIsNull("模板缺失收据核销仓库配置，请联系管理员"),
	OttMobileNotHaveMoreRes("OTT_MOBILE产品资源只能是1个"),
	ProdPackMaxUserCntIsError("套餐产品的用户数必须大于等于1"),
	ProdPackGroupNameIsError("产品内容组名称不能为空"),
	ProdPackProdListIsError("产品内容组不能为空"),
	SpkgIsError("协议号不存在或协议未生效或已失效"),
	SpkgIsUsed("协议号已被使用"),
	SpkgHasNotCONFIRM("协议号还未通过审批"),
	SpkgIsNotTrueCust("协议号和客户不匹配"),
	SpkgIsPaiChuCustKG("请选退订客户套餐，才能订购协议套餐"),
	AddNewAddrIsFail("新增地址失败"),
	;
	
	

	private ErrorCode(String desc){
		this.desc=desc;
	}
	private String desc;
	public String getDesc(){
		String transDesc = MemoryDict.getTransData(this.desc);
		return transDesc;
	}
	/**
	 * 获得OTT接口的状态码
	 * @return
	 */
	public String getOttStatusCode(){
		String name=this.name();
		Integer statueCode=null;
		try{
			statueCode=Integer.valueOf(name.substring(1));
		}catch(Exception e){}
		if(statueCode!=null){
			return statueCode.toString();
		}else{
			return "40009";
		}
	}
}