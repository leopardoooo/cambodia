/***
 * 常量定义
 */
Ext.ns("Constant");
Ext.apply( Constant , {
	
	//站点名称
	ROOT_PATH_CORE: "boss-core",
	ROOT_PATH_LOGIN: 'boss-login',
	ROOT_PATH_SYSMANAGER: 'boss-sysmanager',
	ROOT_PATH_REPORT: 'boss-report',
	
	ROOT_PATH: root,
	
	//默认分页时，每页显示15条记录
	DEFAULT_PAGE_SIZE: 15 ,
	/**
	 * 统一定义金额的样式。文本框
	 */
	MONEY_STYLE: "color: blue; font-weight: bold;font-size:13px;border : 0;background:#c7dff0",
	
	/**
	 * 金额的样式，Label
	 * @type String
	 */
	MONEY_LABEL_STYLE: "color: red; font-weight: bold;font-size:17px;",
	
	MONEY_FIELD_STYLE: "color: BLUE; font-weight: bold;font-size:13px;",
	
	LABEL_STYLE: "color: #666666; font-weight: bold;font-size:13px;",
	
	/**
	 * 表单的样式
	 */
	FORM_STYLE: "background:#F9F9F9; padding: 10px;padding-top: 15px;",
	
	TAB_STYLE: "background:#F9F9F9; padding: 10px",
	
	SET_STYLE: "background:#F9F9F9; padding: 0px;padding-top: 0px;",
	
	TEXTFIELD_STYLE : 'padding:3px 2px;vertical-align:middle;background:url() repeat-x scroll 0 0 #F9F9F9;border:0;',
	
	AREA_ALL : '4500',
	COUNTY_ALL:'4501',
	
	C_CUST : 'C_CUST',
	C_USER : 'C_USER',
	P_PROD : 'P_PROD',
	
	IDCARD : 'IDCARD',
	ACCT_FEETYPE_ADJUST:'KTTZ',
	ACCT_FEETYPE_ADJUST_EASY:'JDTZ',
	
	WH_INVOICE_COUNTYID : ['0101','0102'],	//武汉市发票适用地区
	WH_INVOICE_TYPE : '4'					//武汉市发票类型代码
//	REPORT_FUNC:['groupsum','dimchange','graph']//报表OLAP功能菜单配置
	,
//	SQL_INJECT_IDENTIFIER:/^\?(.*)(select |insert |delete from |count\(|drop table|update truncate |asc\(|mid\(|char\(|xp_cmdshell|exec master|net localgroup administrators|\"|:|net user|\'| or )(.*)$/gi,
	SQL_INJECT_IDENTIFIER:new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]") //格式 RegExp("[在中间定义特殊过滤字符]")
});

