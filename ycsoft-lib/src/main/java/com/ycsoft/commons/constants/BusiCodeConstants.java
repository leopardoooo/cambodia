package com.ycsoft.commons.constants;

/**
 * 业务编号常量类，
 */
public final class BusiCodeConstants {
	public final static String CUST_OPEN = "1001";//客户开户
	public final static String CUST_WRITEOFF = "1002";//客户销户
	public final static String CUST_BATCH_OPEN = "1047";//批量预开户
	public final static String CUST_TRANS = "1003";//过户
	public final static String CUST_CHANGE_ADDR = "1010";//移机
	public final static String CUST_CLASS_EDIT = "1114";//修改优惠类型
	public final static String CUST_EDIT = "1004";//修改客户资料
	public final static String CUST_RELOCATE = "1119";//拆迁
	public final static String CUST_JOIN_UNIT = "1005";//加入单位
	public final static String CUST_QUIT_UNIT = "1006";//退出单位
	public final static String CUST_CHANGE_LINE = "1065";//改线
	public final static String DEVICE_BUY = "1007";//购买设备
	public final static String DEVICE_BUY_PJ = "1108";//购买配件
	public final static String DEVICE_BUY_PJ_BACTH = "1109";//批量购买配件
	public final static String DEVICE_RECLAIM = "1008";//回收设备
	public final static String DEVICE_CHANGE = "1009";//更换设备
	public final static String DEVICE_REG_LOST = "1014";//设备挂失
	public final static String DEVICE_SALE = "1013";//销售
	public final static String DEVICE_CANCEL_LOST = "1011";//取消设备挂失
	public final static String DEVICE_USER_RECLAIM = "2270";//回收设备（可批量）
	
	
	// Add business for the Cambodia
	// Modified since 2015-07-31
	public final static String BATCH_MOD_USER_NAME = "2122";//批量修改用户名
	
	public final static String PROD_PACKAGE_ORDER = "1015";//套餐订购
	public final static String PROD_SINGLE_ORDER = "102"; //单用户订购
	public final static String PROD_CONTINUE = "101";//续订
	public final static String PROD_UPGRADE = "100";//升级
	public final static String OTT_MOBILE_UPGRADE = "103";//ott_mobile升级
	
	public final static String PROD_TERMINATE = "1027";//退订
	public final static String PROD_HIGH_TERMINATE="110";//高级退订
	public final static String PROD_SUPER_TERMINATE="109";//超级退订
	
	public final static String ORDER_EDIT="131";//订单修改
	
	public final static String USER_HIGH_WRITE_OFF="111";//高级销户
	
	public final static String PROD_CHANGE_TARIFF = "1028";//资费变更
	
	public final static String PKG_ORDER = "1016";//订购套餐
	public final static String PKG_EDIT = "1017";//修改套餐
	public final static String USER_EDIT_LEVEL = "1018";//修改用户等级
	public final static String USER_OPEN = "1020";//用户开户
	public final static String USER_OPEN_BATCH = "2020";//批量用户开户
	public final static String USER_WRITE_OFF = "1021";//用户销户
	public final static String USER_TRANS = "1022";//带账移机
	public final static String USER_EDIT = "1023";//用户修改资料
	public final static String USER_REQUIRE_STOP = "1024";//报停
	public final static String USER_PRE_REQUIRE_STOP = "1124";//预报停
	public final static String USER_REQUIRE_OPEN = "1025";//报开
	public final static String USER_RESEND_CA = "1029";//指令重发
	public final static String USER_ATOD = "1030";//模拟转数
	public final static String USER_DTOI = "1031";//开通双向
	public final static String USER_PROMOTION = "1064";//促销
	public final static String USER_OPEN_TEMP = "1033";//临时授权
	public final static String USER_SINGLE_CARD = "1223";//一体机转换
	public final static String ACCT_PAY_ZERO = "1034";//零资费缴费
	public final static String Unit_ACCT_PAY = "1039";//单位批量缴费
	public final static String ACCT_PAY = "1040";//缴费
	public final static String ACCT_PAY_ATV="1048";//补收模拟费用
	public final static String ACCT_REFUND = "1041";//退款
	public final static String ACCT_ADJUST = "1042";//调账
	public final static String ACCT_TRANS = "1043";//转账
	public final static String ACCT_BIND_BANK = "1044";//取消托收
	public final static String ACCT_UNBIND_BANK = "1045";//银行托收
	public final static String ACCT_BANK_AGREE = "1145";//银行签约
	public final static String ACCT_BANK_CANCEL_AGREE = "1146";//cancel签约
	public final static String ACCT_BANK_PAY = "1147";//pay from bank
	public final static String FEE_PAY = "1207";//费用支付
	public final static String FEE_CANCEL = "1051";//冲正
	public final static String FEE_EDIT = "1052";//修改费用
	public final static String FEE_REFUND_YJ = "1055";//退押金
	public final static String PRINT_EDIT_INVOICE = "1061";//修改发票
	public final static String PRINT = "1068";//打印
	public final static String PRINT_REPEAT = "1068";//票据重打
	public final static String BUSI_CANCEL = "1032";//业务回退
	public final static String ACCT_UNFREEZE = "9001";//资金解冻
	public final static String ACCT_WRITEOFF = "9002";//销帐
	public final static String PROMOTION_AUTO= "9003";//自动促销
	public final static String PROMOTION_CANCEL= "9006";//促销取消
	public final static String EDIT_ACCT_DATE = "1226";//修改账务日期
	public final static String EDIT_INVALID_DATE = "1081";//修改季包,半年包,年包产品到期日期
	public final static String VALUABLE_CARD_ADD = "1227";//充值卡销售
	public final static String VALUABLE_CARD_REMOVE = "1228";//充值卡退卡
	public final static String JOB_PROD_STOP = "9007";//自动退订
	public final static String VOD_PRE_FEE = "8000";//VOD预扣款
	public final static String CANCEL_VOD_PRE_FEE = "8001";//VOD取消预扣款
	public final static String ADDRESS_UPDATE_ALL = "1229";//批量地址修改
	public final static String ADDRESS_UPDATE_SOME = "1230";//明细地址修改
	public final static String ADDRESS_CHANGE_ADDR = "1300";//小区挂载
	public final static String CREDIT_CAL = "9901";//信控
	public final static String ATV_CUSTLINE = "1133";//模拟剪线
	public final static String ATV_ACTIVE = "1134";//模拟恢复
	public final static String PROD_CHANGE_PUBACCTTYPE = "9111";//修改产品公用账目使用类型
	public final static String DEVICE_LOSS = "2207";//仓库系统 设备挂失
	public final static String CARD_CA_SEND = "1302";//单卡指令发送
	public final static String PF_ACCT_PAY = "9103";//支付平台缴费
	public final static String OWN_LONG = "1332";//长期欠费
	public final static String CHANGE_DEVICE_BUY_TYPE = "1231";//修改设备购买方式
	public final static String CHANGE_OWNERSHIP = "1201";//修改设备产权
	public final static String CHECK_MOBILE_BILL = "1607";//移动结账
	public final static String CANCEL_PROCURE_DEVICE = "2111";//取消设备领用
	public final static String CANCEL_B_BILL="1234"; //作废账单
	public final static String CONFIRM_RECLAIM_DEVICE="2118"; //确认回收设备
	public final static String CANCEL_RECLAIM_DEVICE="2228"; //取消回收设备
	public final static String BATCH_ACCT_PAY="1240"; //批量缴费
	public final static String BATCH_FILE_ACCT_PAY="1241"; //批量收费(文件)
	public final static String PROM_ACCT_PAY = "1688";//套餐缴费
	public final static String BAND_CHANG_PROD = "1127";//宽带升级
	public final static String BATCH_PROD_ORDER = "1914";//批量订购产品
	public final static String BATCH_PROD_CANCEL = "1915";//批量退订产品
	public final static String CHANGE_PROD_DYN_RES = "1128";//更换产品动态资源
	public final static String TEMP_PROD_PAUSE = "1324";	//暂停产品
	public final static String CHANGE_PROMOTION = "1180";	//更换促销
	public final static String SYNC_ZZD_PROD = "1500";		//主机产品同步
	
	public final static String PROD_PRE_OPEN = "1983";//修改产品预开通日期
	public final static String PROD_PUB_ACCTITEM_TYPE = "1984";//修改产品公用账目适用类型
	
	public final static String DEVICE_CANCEL_CONFIRM = "2112";//取消调拨确认
	public final static String DEVICE_CONFIRM = "2113";//调拨确认
	public final static String DEVICE_TRANSFER= "2114";//调拨
	public final static String DEVICE_PROCURE= "2115";//领用
	public final static String DEVICE_INPUT = "2116";//入库
	public final static String DEVICE_OUT = "2117";//退库
	public final static String DEVICE_STB_FILLED = "2119";//灌装
	public final static String DEVICE_CARDSTBFILLED = "2121";//机卡解绑
	
	public final static String SERVICE_PRINT = "1269";//受理单打印
	public final static String CA_SINGLE_CARD = "1722";//一体机预授权
	/**
	 * 设备购买包换期.
	 */
	public final static String DEVICE_BUY_REPLACOVER= "1091";//领用
	public final static String JOB_REQSTOP = "1060"; //job处理报停
	public final static String TASK_FINISH = "2256";	//工单完工
	public final static String TASK_INIT = "2257";	//新建工单
	public final static String TASK_ASSIGN = "2258";	//分配施工员
	public final static String TASK_FILL = "2259";	//工单回填
	public final static String TASK_ZTE_OPEN = "2269";	//开通zte授权
	public final static String TASK_CANCEL = "2261";	//工单作废
	public final static String TASK_Withdraw = "2268";	//工单撤回
	public final static String TASK_CHANGE_DEVICE = "2262";	//工单更换回填的智能卡
	public final static String CANCEL_PAY = "2263";	//支付回退
	public final static String SUPER_CANCEL_PAY = "2264";//超级支付回退
	public final static String OTHER_FEE = "2265";//杂费收取
	public final static String TASK_MODIFY_CUSTSIGNNO = "3258";	//修改客户前单号
	
	public final static String BATCH_USER_WRITE_OFF = "1913";	//批量销用户
	public final static String BATCH_OPEN_USER_OTT_MOBILE = "2500";	//批量手机开户
	public final static String CARD_OSD_SEND = "5001";//OSD催缴
}
