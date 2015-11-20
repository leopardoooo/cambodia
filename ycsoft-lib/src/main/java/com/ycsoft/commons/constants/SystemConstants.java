package com.ycsoft.commons.constants;

/**
 * 系统业务常量定义
 *
 * @author hh
 * @date Mar 4, 2010 4:27:26 PM
 */
public class SystemConstants {
	/**
	 * 省的统一编号
	 */
	public final static String AREA_ALL="4500";
	public final static String COUNTY_ALL="4501";
	//超管ID
	public final static  String DEFAULT_OPTR_ID = "0";
	//柬埔寨 根行政区域
	public final static  String DISTRICT_ID = "50";
	/**
	 * 分公司的统一编号
	 */
	public final static String AREA_COMPANY="5000";
	public final static String COUNTY_COMPANY="5001";
	
	//潜江地区编号
	public final static String COUNTY_9005="9005";
	//武汉分公司
	public final static String COUNTY_0101="0101";
	public final static String COUNTY_0582 = "0582";
	
	//缺省数据权限
	public final static String DEFAULT_DATA_RIGHT="1=1";
	
	//预扣费全价规则
	public final static String PRE_FEE_PRICE="PRICE";
	/**
	 * 系统Boolean型常量定义
	 */
	public final static String BOOLEAN_TRUE = "T";
	public final static String BOOLEAN_FALSE = "F";
	public final static String BOOLEAN_NO = "N";
	
	/**
	 * 预扣费处理标志
	 */
	public final static String PROCESS_FLAG_UNDO = "1";
	public final static String PROCESS_FLAG_DONE = "2";
	
	/**
	 * 模板类型常量定义
	 */
	public final static String TEMPLATE_TYPE_FEE = "FEE";
	public final static String TEMPLATE_TYPE_TASK = "TASK";
	public final static String TEMPLATE_TYPE_DOC = "DOC";
	public final static String TEMPLATE_TYPE_UPDPROP = "UPDPROP";// 修改客户
	public final static String TEMPLATE_TYPE_BILLING = "BILLING";
	public final static String TEMPLATE_TYPE_INVOICE = "INVOICE";
	public final static String TEMPLATE_TYPE_CONFIG = "CONFIG";
	public final static String TEMPLATE_TYPE_OPEN_TEMP = "OPEN_TEMP";
	public final static String TEMPLATE_TYPE_STB_FILLED = "STB_FILLED";
	public final static String TEMPLATE_TYPE_TERMINAL_AMOUNT = "TERMINAL_AMOUNT";
	public final static String TEMPLATE_TYPE_PRINT = "PRINT";


	/**
	 * 超级管理员工号
	 */
	public final static String SUPER_ADMIN = "admin";

	/**
	 * 子系统定义
	 */
	public final static String SUB_SYSTEM_BUSI = "1";
	public final static String SUB_SYSTEM_MANAGER = "2";
	public static final String SUB_SYSTEM_REPROT = "7";


	/**
	 * tempVar中定义的key
	 */
	public final static String EXTEND_ATTR_KEY_NEWADDR = "NEW_ADDR";

	/**
	 * 系统操作级别
	 */
	public final static String SYS_LEVEL_ALL="ALL";
	public final static String SYS_LEVEL_AREA="AREA";
	public final static String SYS_LEVEL_COUNTY="COUNTY";
	public final static String SYS_LEVEL_DEPT="DEPT";
	public final static String SYS_LEVEL_OPTR="OPTR";

	/**
	 * 支付方式
	 */
	public final static String PAY_TYPE_CASH = "XJ";// 现金
	public final static String PAY_TYPE_POS = "POS";// POS
	public final static String PAY_TYPE_BANK_PAY = "YHDS";// 银行代收
	public final static String PAY_TYPE_BANK_DEDU = "YHDK";// 银行代扣
	public final static String PAY_TYPE_BANK_TRAN = "YHZZ";// 银行转账
	public final static String PAY_TYPE_UNIT_TRAN = "DWZZ";// 单位转账
	public final static String PAY_TYPE_CARD = "CZK";//充值卡
	public final static String PAY_TYPE_UNITPRE = "UNITPRE";//合同支付
	public final static String PAY_TYPE_UNITPRE_CASH = "UNITPRE_XJ";//合同现金
	public final static String PAY_TYPE_UNITPRE_PRESENT = "UNITPRE_PRESENT";//合同赠送
	public final static String PAY_TYPE_PRESENT = "PRESENT";//赠送
	public final static String PAY_TYPE_SHIFT = "SHIFT";//平移转入
	public final static String PAY_TYPE_DJQ = "DJQ";//代金券
	public final static String PAY_TYPE_UNPAY = "UNPAY";//未支付
	public final static String PAY_TYPE_DEZS = "DEZS";//定额赠送
	public final static String PAY_TYPE_DELEYPAY = "DELEYPAY";//延期付款
	

	/**
	 * 出票方试
	 */
	public final static String INVOICE_MODE_AUTO = "A";//自动
	public final static String INVOICE_MODE_MANUAL = "M";//手开
	public final static String INVOICE_MODE_QUOTA = "Q";//定额发票
	

	/**
	 * 发票状态
	 */
	public final static String INVOICE_STATUS_IDLE = "IDLE";//未使用
	public final static String INVOICE_STATUS_USE = "USE";//使用
	public final static String INVOICE_STATUS_CLOSE = "CLOSE";//结账
	public final static String INVOICE_STATUS_CANCELLATION = "CANCEL";//缴销
	public final static String INVOICE_STATUS_INVALID = "INVALID";//作废

	/**
	 * 客户密码
	 */
	public final static String CUST_PASSWORD_DEFAULT = "111111";

	/**
	 * 客户类型
	 */
	public final static String CUST_TYPE_RESIDENT = "RESIDENT";
	public final static String CUST_TYPE_NONRESIDENT = "NONRES";
	public final static String CUST_TYPE_UNIT = "UNIT";

	/**
	 * 用户类型
	 */
	public final static String USER_TYPE_DTT = "DTT";// 模拟
	public final static String USER_TYPE_OTT = "OTT";// 数字
	public final static String USER_TYPE_BAND = "BAND";// 宽带
	public final static String USER_TYPE_OTT_MOBILE = "OTT_MOBILE";// 互动

	/**
	 * 用户网络类型
	 */
	public final static String USER_NET_TYPE_SATELLITE = "SATELLITE";
	public final static String USER_NET_TYPE_MIRCOWAVE = "MIRCOWAVE";
	public final static String USER_NET_TYPE_CABLE = "CABLE";
	public final static String USER_NET_TYPE_LAN = "LAN";
	
	/**
	 * 用户终端类型
	 */
	public final static String USER_TERMINAL_TYPE_FZD = "FZD";//副终端
	public final static String USER_TERMINAL_TYPE_ZZD = "ZZD";//主终端

	/**
	 * 用户服务类型
	 */
	public final static String DTV_SERV_TYPE_SINGLE = "SINGLE";// 单向
	public final static String DTV_SERV_TYPE_DOUBLE = "DOUBLE";// 双向
	public final static String ATV_SERV_TYPE_YYMN = "YYMN";// 营业模拟
	public final static String ATV_SERV_TYPE_CCT = "CCT";// 村村通
	public final static String ATV_SERV_TYPE_YBMN = "YBMN";// 一般模拟
	public final static String ATV_SERV_TYPE_BIGMN = "BIGMN";// 集团模拟用户
	public final static String ATV_SERV_TYPE_XYBIGMN = "XYBIGMN";// 集团协议用户
	

	/**
	 * 费用类型
	 */
	public final static String FEE_TYPE_BUSI = "BUSI"; // 服务费
	public final static String FEE_TYPE_DEVICE = "DEVICE"; // 设备费用
	public final static String FEE_TYPE_ACCT = "ACCT"; // 预存费
	public final static String FEE_TYPE_PROMACCT = "PROMACCT"; // 预存费
	public final static String FEE_TYPE_UNITPRE = "UNITPRE"; // 预付费
	public final static String FEE_TYPE_UNBUSI = "UNBUSI"; // 非营业收费
	public final static String FEE_TYPE_VALUABLE = "VALUABLE"; // 充值卡费
	public final static String FEE_TYPE_GENERAL_ACCT = "G_ACCT"; //分公司账户
	public final static String FEE_TYPE_CONTRACT = "CONTRACT"; //分公司账户
	
	/**
	 * 设备销售类型
	 */
	public final static String DEVICE_FEE_TYPE_XS = "XS";
	public final static String DEVICE_FEE_TYPE_ZJ = "ZJ";
	/**
	 * 扩展信息
	 */
	public final static String EXT_TAB_INPUTTYPE_COMBO = "paramcombo";
	public final static String EXT_ATTR_TYPE_BUSI = "BUSI";
	public final static String EXDT_ATTR_TYPE_TAB = "TABLE";
	public final static String EXT_TAB_EXTENDNAME = "未知表名";
	public final static String EXT_BUSI_EXTENDNAME = "业务扩展名";
	public final static String EXT_C_DONE_CODE = "EXT_C_DONE_CODE";

	/**
	 * 套餐类型
	 */
	public final static String PACKAGE_TYPE_CUST = "CUST"; // 套餐类型
	public final static String PACKAGE_TYPE_USER = "USER"; // 用户套餐
	
	public final static String PACKAGE_MARKET_TYPE = "M";	//市场分成类型
	public final static String PACKAGE_FINANCE_TYPE = "F";	//财务分成类型

	/**
	 * 账户类型
	 */
	public final static String ACCT_TYPE_PUBLIC = "PUBLIC";// 公用账户
	public final static String ACCT_TYPE_SPEC = "SPEC";// 专用账户
	public final static String ACCT_TYPE_SPECFEE = "SPEC_FEE";// 特殊账户

	/**
	 * 账户付费方式
	 */
	public final static String ACCT_PAY_TYPE_YFF = "YFF";// 预付费
	public final static String ACCT_PAY_TYPE_HFF = "HFF";// 后付费

	/**
	 * 设备产权
	 */
	public final static String OWNERSHIP_GD = "GD";//广电
	public final static String OWNERSHIP_CUST = "CUST";//个人


	/**
	* 设备类型
	*/
	public final static String DEVICE_TYPE_STB = "STB";
	public final static String DEVICE_TYPE_CARD = "CARD";
	public final static String DEVICE_TYPE_MODEM = "MODEM";
	public static final String DEVICE_TYPE_FITTING = "FITTING";
	
	/**
	 * 设备购买模式
	 */
	public final static String BUSI_BUY_MODE_ALLOCATE = "ALLOCATE";
	public final static String BUSI_BUY_MODE_SALE = "SALE";
	public final static String BUSI_BUY_MODE_PRESENT = "PRESENT";
	public final static String BUSI_BUY_MODE_RENT = "RENT";
	public final static String BUSI_BUY_MODE_BUY = "BUY";
	public final static String BUSI_BUY_MODE_CHANGE = "CHANGE";
	public final static String BUSI_BUY_MODE_UPGRADE = "UPGRADE";
	

	/**
	 * 销售类别
	 */
	public final static String BUY_TYPE_BUSI = "BUSI";
	public final static String BUY_TYPE_DEPOT = "DEPOT";

	/**
	 * 设备调拨类型
	 */
	public final static String DEVICE_TRANS_TYPE_TRANOUT = "TRANOUT";// 调出
	public final static String DEVICE_TRANS_TYPE_TRANIN = "TRANIN";// 调入

	/**
	 * 设备差异类型
	 */
	public final static String DEVICE_DIFFENCN_TYPE_NODIFF = "NODIFF";// 无差异
	public final static String DEVICE_DIFFENCN_TYPE_UNCHECK = "UNCHECK";// 待确认
	public final static String DEVICE_DIFFENCN_TYPE_DIFF = "DIFF";// 差异


	/**
	 * 产品订购方式
	 */
	public final static String PROD_ORDER_TYPE_ORDER = "ORDER";
	public final static String PROD_ORDER_TYPE_TRY = "TRY";
	public final static String PROD_ORDER_TYPE_PRESENT = "PRESENT";
	public final static String PROD_ORDER_TYPE_TVORDER = "TVORDER";

	/**
	 * 产品服务类型
	 */
	public final static String PROD_SERV_ID_ATV = "ATV";
	public final static String PROD_SERV_ID_DTV = "DTV";
	public final static String PROD_SERV_ID_ITV = "ITV";
	public final static String PROD_SERV_ID_BAND = "BAND";
	public final static String PROD_SERV_ID_RENT = "RENT";
	public final static String PROD_SERV_ID_OTT = "OTT";
	public final static String PROD_SERV_ID_OTTMOBILE = "OTT_MOBILE";
	public final static String PROD_SERV_ID_DTT= "DTT";

	/**
	 * 产品打包类型
	 */
	public final static String PROD_TYPE_BASE ="BASE";//基础产品
	public final static String PROD_TYPE_SPKG ="SPKG";//协议套餐
	public final static String PROD_TYPE_CUSTPKG ="CPKG";//普通套餐
	public final static String PROD_TYPE_USERPKG = "UPKG";// 

	 /**
	  * 计费方式
	  */
	public final static String BILLING_TYPE_MONTH = "BY";
	public final static String BILLING_TYPE_JC = "JC";
	public final static String BILLING_TYPE_NUMBER = "NUMBER";
	public final static String BILLING_TYPE_MB = "MB";
	public final static String BILLING_TYPE_TIME = "TIME";
	public final static String BILLING_TYPE_DAY="DAY";
	
	public final static String BILL_FEE_FLAG_DY = "DY";	//当月 包多月账单类型

	 /**
	  * 免费类型
	  */
	 public final static String PROD_FREE_TYPE_DAY="DAY";
	 public final static String PROD_FREE_TYPE_TIMES="TIMES";
	 public final static String PROD_FREE_TYPE_KB="KB";
	 public final static String PROD_FREE_TYPE_SECOND="SECOND";

	 /**
	  * 费用优惠类型
	  */
	 public final static String FEE_DISCT_PROM="PROM"; //促销
	 /**
	  * 账户余额异动类型
	  */
	 public final static String ACCT_CHANGE_PAY = "PAY";//支付
	 public final static String ACCT_CHANGE_CFEE = "JF";//缴费
	 public final static String ACCT_CHANGE_INIT = "INIT";//初始化
	 public final static String ACCT_CHANGE_UNCFEE = "JFCZ";//缴费冲正
	 public final static String ACCT_CHANGE_REFUND = "TK";//退款
	 public final static String ACCT_CHANGE_WRITEOFF = "XZ";//销账
	 public final static String ACCT_CHANGE_UNWRITEOFF = "FXZ";//反销账
	 public final static String ACCT_CHANGE_UNFREEZE = "JD";//解冻
	 public final static String ACCT_CHANGE_TRANS = "ZZ";//转账
	 public final static String ACCT_CHANGE_PROMOTION = "CXZS";
	 public final static String ACCT_CHANGE_PROMOTION_CANCEL = "CXQX";
	 public final static String ACCT_CHANGE_ADJUST = "TZ";
	 public final static String ACCT_CHANGE_INVALID = "ZF";//作废
	 public final static String ACCT_CHANGE_CHECKMOBILE= "YDJZ";//移动结账

	 /**
	  * 账户资金类型
	  */
	 public final static String ACCT_FEETYPE_CASH="XJ"; //现金
	 public final static String ACCT_FEETYPE_CARD="CZK"; //充值卡
	 public final static String ACCT_FEETYPE_PRESENT="ZS"; //赠态
	 public final static String ACCT_FEETYPE_ADJUST="TZ"; //调账
	 public final static String ACCT_FEETYPE_ADJUST_KT="KTTZ"; //调账(可退)
	 public final static String ACCT_FEETYPE_ADJUST_EASY="JDTZ"; //小额减免
	 public final static String ACCT_FEETYPE_ZKXJYH="ZKXJYH"; //折扣现金优惠(不可退不可转)
	 public final static String ACCT_FEETYPE_UNPAY="UNPAY"; //未结账
	 public final static String ACCT_FEETYPE_OWEFEE="OWE"; //欠费

	 /**
	  *账户余额处理方式
	  */
	 public final static String ACCT_BALANCE_REFUND="REFUND"; //余额退款
	 public final static String ACCT_BALANCE_TRANS="TRANS"; //余额转账
	 public final static String ACCT_BALANCE_EXPIRE="EXPIRE"; //余额作废

	 /**
	  * 公用账目类型
	  */
	 public final static String ACCTITEM_PUBLIC="PUBLIC";	//公用
	 public final static String ACCTITEM_SPECPUBLIC="SPEC"; //专项公用

	 /**
	  * 特殊费用账目定义
	  */
	 public final static String ACCTITEM_TJ="90000000";
	 public final static String ACCTITEM_PUBLIC_ID = "10000000";
	 public final static String ACCTITEM_BASEPROD_ID = "10000001";
	 
	 /**
	  * 资源异动
	  */
	 public final static String RECORD_CHAGNE_TYPE_ADD = "ADD";
	 public final static String RECORD_CHAGNE_TYPE_EDIT = "EDIT";
	 public final static String RECORD_CHAGNE_TYPE_DELETE = "DELETE";

	 /**
	  * 账目任务定义
	  */
	 public final static String TASK_STOP ="TJ";//停机

	 /**
	  * 规则定义
	  */
	 public final static String RULE_TYPE_BUSI = "BUSI";//业务规则
	 public final static String RULE_TYPE_DATA = "DATA";//数据规则

	 public final static String RULE_CFG_TYPE_HAND = "HAND";//手工配置
	 public final static String RULE_CFG_TYPE_COND = "COND";//条件配置
	 public final static String RULE_CFG_TYPE_DETAIL = "DETAIL";//明细配置
	 
	 
	 //移机业务代码
	 public final static String BUSI_CODE_YIJI = "1010";
	 
	 //发票类型 
	 public static final String DOC_TYPE_CONFIG = "1";
	 public static final String DOC_TYPE_INVOICE = "3";
	 public static final String DOC_TYPE_WUHAN_INVOICE = "4";
	 public static final String DOC_TYPE_QUOTA = "5";
	 public static final String DOC_TYPE_FEELIST = "6";
	 public static final String DOC_TYPE_SERVICE = "DOC";
	
	 //t_spell表data_type字段类型ADDRESS
	 public static final String DATA_TYPE_ADDRESS = "ADDRESS";
	 

	//修改类型(r_device_edit表)
	public static final String DEVICE_EDIT_TYPE_STATUS = "DEVICE_STATUS_R_DEVICE";
	
	//授权优先级
	public static final int PRIORITY_SSSQ=10;//实时授权
	public static final int PRIORITY_DSSQ=20;//定时授权
	public static final int PRIORITY_XXTZ=30;//消息通知
	public static final int PRIORITY_XJ=40;//巡检
	
	//消息类型
	public static final String MESSAGE_TYPE_CJ = "CJ";//催缴
	public static final String MESSAGE_TYPE_CX = "CX";//促销
	public static final String MESSAGE_TYPE_GG = "GG";//公告
	//任务类型
	public static final String TASK_CODE_CJ = "CJ";//消息
	public static final String TASK_CODE_TJ = "TJ";//停机
	public static final String TASK_CODE_TD = "TD";//停机
	
	public static final String JOB_Y= "Y";
	public static final String JOB_N= "N";
	
	//客户搜索类型
	public static final String MULTIPLE = "MULTIPLE";
	
	public static final int ADDRESS_LEVEL_COUNTY = 1;
	public static final int ADDRESS_LEVEL_DISTRICT = 2;
	public static final int ADDRESS_LEVEL_SUBDISTRICT = 3;
	
	public static final String PRESENT_TYPE_TIME = "TIME";//时长
	public static final String PRESENT_TYPE_FEE = "FEE";//费用
	
	//账单来源
	public static final String BILL_COME_FROM_AUTO = "1";		//自动生成
	public static final String BILL_COME_FROM_MANUAL = "2";		//手动生成
	public static final String BILL_COME_FROM_ADJUST = "3";		//调账生成(不可退)
	public static final String BILL_COME_FROM_ADJUST_KT = "4";	//调账生成(可退)
	public static final String BILL_COME_FROM_PROM = "5";		//套餐缴费
	public static final String BILL_COME_FROM_MUCH = "6";		//包多月
	
	//账单状态
	public static final String BILL_STATUS_CANCEL="4";			//作废
	
	public static final String ADJUST_REASON_OWNFEE = "OWNFEE";	//调账原因 -- 欠费抹零
	
	//催停标志
	public static final String STOP_TYPE_KCKT = "KCKT";//可催可停
	
	//角色类型
	public static final String ROLE_TYPE_MENU = "MENU";//菜单
	public static final String ROLE_TYPE_DATA = "DATA";//数据
	
	//授权发放方式
	public static final String REFRESH_TYPE_PROD ="PROD";//产品授权
	public static final String REFRESH_TYPE_TERMINAL ="TERMINAL";//终端授权
	
	//设备变更时的产权变更模式
	public static final String OWNSHIP_BY_BUY="BUY";//根据购买方式变更
	public static final String OWNSHIP_BY_OLD_DEVICE="DEVICE";//根据原设备变更
	
	public static final String J_CA_COMMAND = "J_CA_COMMAND";//CA业务指令
	public static final String J_CA_COMMAND_OUT = "J_CA_COMMAND_OUT";//CA优化指令
	public static final String J_CA_COMMAND_HIS = "J_CA_COMMAND_HIS";//CA历史指令
	public static final String J_CA_COMMAND_DAY = "J_CA_COMMAND_DAY";//CA历史指令
	public static final String J_CA_COMMAND_OUT_BAK = "J_CA_COMMAND_OUT_BAK";//CA历史指令
	
	//通用账户类型
	public static final String General_ACCT_TYPE_COMPANY = "COMPANY";//网络公司
	public static final String General_ACCT_TYPE_CONTRACT = "CONTRACT";//合同
	
	//部门类型
	public static final String DEPT_TYPE_FGS = "FGS";//分公司
	public static final String DEPT_TYPE_KFB = "KFB";//客服部
	public static final String DEPT_TYPE_YYT = "YYT";//营业厅
	public static final String DEPT_TYPE_CK = "CK";//仓库
	
	//公用账目可用类型
	public static final String PUBLIC_ACCTITEM_TYPE_ALL = "ALL";//都可以用
	public static final String PUBLIC_ACCTITEM_TYPE_NONE = "NONE";//都不能用
	public static final String PUBLIC_ACCTITEM_TYPE_SPEC_ONLY = "SPEC_ONLY";//只有专项公用账目可以
	public static final String PUBLIC_ACCTITEM_TYPE_PUBLIC_ONLY = "PUBLIC_ONLY";//只有公用类型公用账目可以
	
	//VOD免费重复点播时间
	public static final Integer VOD_TIME_FORFREE = 24;//24小时
	
	public static final String DEFAULT_PAY_PASSWORD = "111111";//双向支付默认密码
	
	//任务执行类别
	public static final String TASK_TJ = "TJ";//停机
	public static final String TASK_XJ = "XJ";//巡检
	public static final String TASK_TD = "TD";//退订
	public static final String TASK_CJ = "CJ";//催缴
	//租赁费FEE_ID
	public static final String LEASE_FEE_ID="17";//租赁费用
	
	public static final String WH_AREA_ID = "0100";	//武汉地区
	public static final String WH_COUNTY_ID = "0101";	//武汉县市
	public static final String ZS_COUNTY_ID = "0102";	//直属县市
	
	/**
	 * 客户群体(模拟大客户)
	 */
	public static final String CUST_COLONY_MNDKH = "MNDKH";
	public static final String CUST_COLONY_XYKH = "XYKH";
	
	public static final String CUST_CLASS_YBKH = "YBKH";
	
	public static final String CUST_LEVEL_YBKH="YBKH";
	public static final String CUST_LEVEL_VIP="VIPKH";
	/**
	 * 充值卡充值用户名和密码
	 */
	public static final String CARD_USER_NAME = "recharge_intf";
	public static final String CARD_RECHARGE_CARD = "bjdv_recharge";
	
	 public final static String DEVICE_CFG_TYPE_HAND = "HAND";//设备手工导入
	 public final static String DEVICE_CFG_TYPE_FILE = "FILE";//设备文件导入
	
	/**
	 * 宽带供应商
	 */
	 
	 public final static String BAND_SUPPLYIER_ZX = "ZX"; //中兴
	 public final static String BAND_SUPPLYIER_YX = "YX"; //亚信
	 
	public static final String RECHARFE_CARD_URL = "http://192.100.1.32:8088/recharge_interface/services/RechargeService";
	public static final String ZX_BAND_URL = "http://192.168.1.100/ZXISAM3NorthSVR/UserAcctService";
	
	public final static String BAND_ZX_SREVER_ID = "ZX0500";
	
	
	//工单操作类型
	public static String WORK_CREATE = "WORK_CREATE";//新建工单
	public static String WORK_ASSIGN = "WORK_ASSIGN";//派单
	public static String WORK_REVISIT = "WORK_REVISIT";//回单
	public static String WORK_CHANGE_TIME = "WORK_CHANGE_TIME";//变更预约
	public static String WORK_VISIT = "WORK_VISIT";//回访
	public static String WORK_CHANGE_OPTR = "WORK_CHANGE_OPTR";//变更人员
	public static String WORK_CANCEL = "WORK_CANCEL";//作废工单
	
	//设备回收原因
	public static String RECLAIM_REASON_SBGH = "SBGH";//设备更换
	public static String RECLAIM_REASON_XHTH = "XHTH";//销户退还
	public static String RECLAIM_REASON_YWQX = "YWQX";//业务取消
	
	//协议扩展字段编号，因为业务单据需要，所以编号必须硬编码
	public static final String EXT_ATTRID_PROTOCOL = "100"; 
	
	
	public static final String TEAM_TYPE_SUPERNET="SUPERNET";//工程运维部
	public static final String TEAM_TYPE_CFOCN="CFOCN"; //柬光施工
	
	public static final String TASK_ASSIGN_SUPPERNET="SUPERNET";
	public static final String TASK_ASSIGN_CFOCN="CFOCN";
	public static final String TASK_ASSIGN_BOTH="CFOCN+SUPERNET";
	
	public static final String TASK_TYPE_INSTALL="1";//新装工单
	public static final String TASK_TYPE_FAULT="2";//故障工单
	public static final String TASK_TYPE_MOVE="3";//迁移工单
	public static final String TASK_TYPE_CHANGE="5";//变动工单
	public static final String TASK_TYPE_WRITEOFF_LINE="7";//拆除工单
	public static final String TASK_TYPE_PROJECT="8";//工程工单
	public static final String TASK_TYPE_WRITEOFF_TERMINAL="9";//销终端工单
	
	public static final String TASK_FINISH_TYPE_SUCCESS="QC";//正常完工
	public static final String TASK_FINISH_TYPE_FAILURE="QD";//施工失败
	
	public static final String ADDR_TREE_LEVEL_ONE = "1";
	public static final String ADDR_TREE_LEVEL_TWO = "2";
	public static final String ADDR_TREE_LEVEL_THREE = "3";
	public static final String ADDR_TREE_LEVEL_FOUR = "4";
	//地址结点的根结点ID
	public static final String ADDRESS_ROOT_ID="1";
	//服务渠道
	public static final String SERVICE_CHANNEL_YYT="YYT";//营业厅
	public static final String SERVICE_CHANNEL_HJZX="HJZX";//呼叫中心
	public static final String SERVICE_CHANNEL_DSYYT="DSYYT";//电视营业厅
	public static final String SERVICE_CHANNEL_MOBILE="MOBILE";//移动
	public static final String SERVICE_CHANNEL_BANK="BANK";//银行
	
	//ORDER_FEE_TYPE订单费用转入（转出）类型
	public static final String ORDER_FEE_TYPE_TRANSFEE="TRANSFEE";//覆盖转移
	public static final String ORDER_FEE_TYPE_CFEE="CFEE";//缴费
	public static final String ORDER_FEE_TYPE_ACCT="ACCT";//公用账目支付
	
	public static final String UNPAY_CANCEL_PROMPT_FEEANDBUSI="FeeAndBusi";//回退业务并取消费用
	public static final String UNPAY_CANCEL_PROMPT_ONLYFEE="OnlyFee";//业务正常，仅取消费用
	public static final String UNPAY_CANCEL_PROMPT_ONLYACCT="OnlyAcct";//业务正常，仅退款转账户
	
	public static final int USER_TYPE_OTT_NUM = 61;		//ott用户账户名生成规则数字
	public static final int USER_TYPE_BAND_NUM = 1;		//band用户账户名生成规则数字
	
	public static final String OTT_MOBILE_CUSTCODE="90";//移动端客户cust_no取值
	public static final String OTT_MOBILE_ADDR_ID="625";   //移动端固定的地址码
	public static final String OTT_MOBILE_ADDRESS="Room #, No.Register, St.Mobile,Special";   //移动端固定的开户地址
	
	public static final String USER_IP_FEE_ID = "5051";//用户IP外挂费用编号
	
	public static final String LANGUAGE_ZH = "zh";		//中文
	public static final String LANGUAGE_KH = "kh";		//柬文
	public static final String LANGUAGE_EN = "en";		//英文
	
	public static final String PROMOTION_TYPE_OTT_MOBILE_EXTERNAL="ott_mobile_external";
	
	public static final String BASE_INVOICE_CODE="AAA";//发票固定代码
	
	public static final String Bulletin_worktask_YYT="YYT";//营业厅工单提醒
	public static final String Bulletin_worktask_CALL="CALL";//呼叫中心工单提醒
	public static final String Bulletin_worktask_WORK="WORK";//工程部门工单提醒
	
	public static final String ExtOperateObj="operateObj";//操作对象键值
	public static final int Osd_Job_Id=-10;//osd催缴job_id
 }
