Ext.ns("App.data");

/**
 * 在App.data为系统的内存数据区域，包含很多信息。
 * 会随着当前的操作，给data进行赋值,这些参数主要是为具体业务的脚本所使用的。 
 * 具体data下的属性，请参数具体的属性定义及赋值情况。
 */
 
 /**
  * 当首页被加载的时候，系统将会为App.data添加如下属性。
  */
 Ext.apply( App.data , {
 	
 	/**
 	 * 属性说明： 根据查询信息的选项卡被激活，资源菜单信息也不同,
 	 *		     如：当用户信息面板被激活，则菜单均为用户对应资源菜单。
 	 * 赋值: 当首页被渲染时，该项的值默认为客户的资源菜单。
 	 * @type Array
 	 */
	resources: [],
	/**
	 * 属性说明：公告信息
	 * 赋值： 当首页渲染时，已经存在了。
	 * @type Array
	 */
	notices: [
		{title: '关于BOSS3.1系统', releaseDate: '2010-1-21', author: '黄辉', content: 'content...'},
		{title: '关于资费变更的新规定', releaseDate: '2010-1-21', author: '巫启贤', content: 'content...'},
		{title: '关于报停的新规定', releaseDate: '2010-1-21', author: 'shit', content: 'content...'}
	],
 	
 	/**
 	 * 属性说明：当前的业务资源菜单信息, 
 	 * 赋值：当点击了右边菜单，默认为NULL
 	 * @type Object
 	 */
 	currentResource: null,
 
 	/**
 	 * 属性说明：客户信息，包括客户基本信息、联系人信息、及其它信息
 	 * 赋值：当每次查询客户时赋值
 	 * @type Object
 	 */
 	custFullInfo: {
 		// 客户信息 @type Object
 		cust: {},
 		// 联系人信息 @type Object
 		linkman: {},
 		//积分信息
 		bonuspoint: {},
 		//签约信息
 		acctBank : {}
 	}, 
 	//是否缴费搜索
 	paySearch : false,
 	
 	//客户综合信息
 	custGeneralInfo : {
 		//用户信息
 		users : {
 			totalAmount : 0,
 			activeAmount : 0,
 			stopAmount : 0,
 			atvAmount : 0,
 			dtvAmount : 0,
 			bandAmount : 0
 		},
 		//账户信息
 		accts : {
 			totalBalance : 0,
 			totalOwn : 0
 		},
 		//产品信息
 		prods : {
 			totalAmount : 0,
 			activeAmount : 0,
 			ownNotStopAmount : 0,
 			ownStopAmount : 0
 		},
 		custs : {
 			balance : 0,
 			his_balance : 0
 		},
 		devices:{//设备信息,机顶盒,智能卡,猫,挂失设备,忽略虚拟设备
 			stbs:0,
			cards:0,
			modem:0,
			regLoss:0
 		}
 	},
 	
 	users:[],
 	/**
 	 * 业务配置数据,
 	 * 包含杂费等、包括杂费、业务单据、施工单、业务扩展属性,
 	 * 具体调用的函数请查看ext-hepler.js#findBusiCfgData(busiCode)
 	 */
 	busiCfgData: {},
 	/**
 	 * 配置数据
 	 */
 	cfgData: {},
 	/**
 	 * 当前地区所有部门
 	 */ 	
 	depts:{},
 	/** 
 	 * 返回当前自定义表单的宽度和高度
 	 */
 	ownFormSize: {},
 	 /** 
 	 * 当前操作员信息
 	 */
 	optr:{},
 	/**
 	 * 所有子系统集合
 	 */
 	subsystem:[],
 	
 	/**
 	 * 存放业务操作
 	 * @type 
 	 */
 	busiTask : {},
 	
 	/**
 	 * 存放后续需要操作的业务
 	 * @type 
 	 */
 	busiTaskToDo : [],
 	
 	busiTaskBtn : null,
 	
 	//是否显示数据加载框的全局变量
 	tipValue : 0,
 	
 	//是否批量缴费的标志
 	batchPayFee : false,
 	busiOptrId:null,//最新业务员id
 	baseAcctDate:null ,//账务日期
 	
 	//当前选中行
 	currentRec :{}
 });