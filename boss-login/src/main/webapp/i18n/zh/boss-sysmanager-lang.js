/**
 * common 放一些常用的，通用的.
 * 剩下的，按照功能划分,一个功能一个子对象.
 * @type 
 */
Sys = {
	common:{
		status:'状态~',
		query:'查询~',
		addNewOne:'新增~',
		update:'修改~',
		invalid:'作废~',
		saveBtn:'保存~',
		cancelBtn:'取消~',
		forbiddenBtn:'禁用~',
		enableBtn:'启用~',
		doActionBtn:'操作~',
		optr:'操作员~',
		changeBtn:'变更~',
		remarkTxt:'备注~',
		exitBtn:'备注~',
		to:'至~',//从 xxx 至 xxxx
		
	statusEnum:{
		empty:'所有~',
		ACTIVE:'正常~',
		INVALID:'失效~',
		IDLE:'空闲~',
		USE:'使用~'
	},
	msg:{//各种提示消息
		actionFailed:'操作失败~',
		actionSuccess:'操作成功~',
		confirmSave:'确认保存?~',
		confirmDelete:'确认要删除?~',
		noDataToBeprinted:'没有要打印的数据~'
			
		}
	},
	ExchangeRateManage: {//汇率配置
		title:'汇率配置',
		commons:{
			exchange_id: '汇率编号',
			effective_time :'生效时间',
			exchange_rate:'汇率',
			status:'状态',
			query:'查询',
			addNewOne:'新增',
			update:'修改',
			invalid:'作废',
			saveBtn:'保存',
			cancelBtn:'取消',
			optr:'操作员',
			create_time:'创建时间'
		},
		status:{
			empty:'所有',
			ACTIVE:'正常',
			INVALID:'失效'
		},
		msg:{
			successMsg:'操作成功.',
			onlyOne:'选择且仅选择一条记录.',
			areYouSure:'是否确认?'
		}
	},
	AddressNodeManage: {//地址节点配置
		panelTitle:'地址管理',//面板主标题
		msg:{//各种提示消息
			actionFailed:'操作失败',
			actionSuccess:'操作成功',
			confirmSave:'确认保存?',
			confirmInvalid:'确认要禁用?',
			confirmDelete:'确认要删除?',
			confirmActivate:'确认要激活?',
			cantDelete:'还有客户在使用，暂不能删除。',
			cantBeInvalided:'存在子级别,无法禁用!',
			maxDepthText: '不能再往下添加',
			noSingleQuoteAllowed:'请不要输入单引号'
			
		},
		formWin:{
			labelSortNum:'序号',
			labelProvince:'省',
			labelDistrict:'行政区域',
			labelNetType:'网络类型',
			emptyTxtProvince:'请选择省',
			emptyTxtBlurQuery:'支持模糊查询',
			btnTxtClose:'关闭',
			btnTxtSave:'保存',
			labelRoadNum:'路号名称',
			titleNewSaveLevelRoadNum:'平级新增路号',
			labelStreatName:'街道名称',
			titleNewSaveLevelStreet:'平级新增街道',
			labelCityName:'城市名称',
			titleNewSaveLevelCity:'平级新增城市',
			labelShowName:'显示名称',
			labelParentName:'上级名称',
			
			labelNewAddChild:'新增下级',
			labelNewAddBrother:'新增平级',
			labelAddrTree:'地址树'
		}
	},
	WorkTask:{//工单系统目前三个功能，所有资源都是一样的.
		msg:{//提示信息
			minTimeText:'不能选择当日之前',
			needSelectOneRecord:'请选择需要操作的记录',
			needSelectOnlyOneRecord:'仅能选择一条记录进行操作!',
			cantReplyWorkTask:'只有施工中的工单才能回单',
			workTaskInvalided:'工单已作废',
			sureToInvalidWorkTask:'确定要作废选中的工单吗?',
			invalidReason:'作废原因',
			cantBeInvalided:'不允许作废!',
			cantSendWorkTask:'只有预约和施工中的工单才能派单!',
			printWorkTaskToolTip:'打印施工单',
			writeWorkReplyTip:'施工完成，填写回单信息',
			replySuccess:'回单成功!',
			replyFailure:'回单失败!',
			sendWorkTaskSuccess:'派单成功!',
			sendWorkTaskFailure:'派单失败!',
			confirmSaveReplyInfo:'确定要保存回单信息吗?',
			replyInfoNotComplete:'回单信息填写不完整!',
			confirmSendWorkTask:'确定要派单吗?',
			warnSelectWorkTeam:'请选择施工部门!'
		},
		common:{
			replyWrokTaskTitle:'回单',
			sendWrokTaskTitle:'派单',
			taskType:'工单类型',
			finish:'完成',
			failure:'失败'
			
		},
		formLabels:{//表单的标签和表格的列名
				replyRemark:'回单备注',
				failReason:'失败原因',
				custPrise:'客户评价',
				finishFlag:'是否完工',
				finishOptr:'完工人员',
				finishDept:'完工部门',
				finishDate:'完工日期',
				assignDate:'分派日期',
				mobile:'手机',
				phone:'电话',
				contack:'联系方式',
				contactPerson :'联系人',
				contactPhone :'联系电话',
				workDept:'施工部门',
				workTaskStatus:'工单状态',
				createDate:'创建日期',
				createTime:'创建时间',
				orderDate:'预约时间',
				servType:'服务类型',
				createType:'创建方式',
				workTaskType:'工单类型',
				workTaskInfo:'工单信息',
				remark:'备注',
				faultReason:'故障原因',
				newAddr:'新地址',
				oldAddr:'旧地址',
				labelAddress:'地址',
				optrDate:'操作日期',
				businessName:'业务名称',
				optr:'操作员',
				businessHall:'营业厅',
				busiInfo:'业务信息',
				custAddr:'客户地址',
				custBusiName:'受理编号',
				custName:'客户名称',
				custInfo:'客户信息',
				workRemark:'施工备注',
				createOptr:'创建人员',
				businessType:'业务类型'
		},
		formWin:{//表单和表格，窗口用到的其他的一些文字
			titleChangeWorker:'变更施工员',
			labelNewWorker:'新施工员',
			labelFormerWorker:'原施工员',
			labelNewOrderTime:'新预约时间',
			labelFormerOrderTime:'原预约时间',
			labelSendWorkTask:'对选中的施工单进行派单',
			labelInvalidWorkTask:'作废选择的施工单',
			labelWorkTaskType:'工单类型',
			emptyTxtWorkTeam:'施工队',
			emptyTxtAddress:'地址',
			emptyTxtContactPhone:'手机',
			emptyTxtCustBusiName:'受理编号11',//后面的11是原来的代码里有的,照抄过来.
			titleWorkDetailInfo:'工单详细信息',
			labelCustPrise:'客户评价',
			labelWorkFinishDate:'施工完成日期',
			minTextWorkFinishDate:'不能选择当日之前',
			labelWorker:'施工员',
			labelSatisfy:'满意度',
			labelFinishType:'完工类型',
			labelFailReason:'失败原因',
			titleAllWorkTask:'所有工单',
			titleFinishedWorkTask:'已完成工单',
			titleUnFinishedWorkTask:'处理中工单',
			labelWaitAcceptTas:'待处理工单',
			btnSearchWorkTask:'搜索工单'
			
		}
	},//目前工单系统里的三个功能结束 ~.
	//发票子系统
	InvoiceCommon:{
		  titleInvoiceInfo:'发票信息~',
			titleInvoiceCount:'发票信息  数量：{0} ~',
			titleInvoiceCountAndAmount:"发票信息 共选中{0}行,金额总和为{1}",
			
			tipInvoiceCountAndAmount:"一共:{0} 张发票，总额:{1} 元，",
			
		  namount: "金额~",
			check_time: "结账时间~",
			close_time: "核销时间~",
			create_time: "入库时间~",
			depot_name: "仓&nbsp;&nbsp;&nbsp;&nbsp;库~",
			check_depot_id:'结账仓库~',
			finance_status: "结存状态~",
			invoice_code: "发票代码~",
			invoice_id: "发票号码~",
			invoice_mode:'出票方式~',
			invoice_type: "发票类型~",
			finance_status: "结存状态~",
			optr_name: "所属营业员~",
			status: "使用状态~",
			amount:'金&nbsp;&nbsp;额~',
			use_optr:'领&nbsp;用&nbsp;人~',
			use_time: "开票时间~",
			//好多个功能下面的表格使用了同样的column
			commonGridColls:['发票号码~','发票代码~','发票类型~','使用状态~','金额~','出票方式~','结存状态~','仓库~','领用人~']
	},
	QueryInvoice:{//查询发票
			common:{
				titlaQueryInvoick:'发票查询~',
				winTitleInvoiceDetail:'发票明细~',
				titleMoneyCountPrefix:'有效总金额~'				
			}
			
	},
	InvoiceDetail:{//发票详细信息
		InvoiceDepotDetailGridColls:['操作类型~','发票流转~','操作员~','操作时间~'],
		InvoiceDetailGridColls:['客户名称~','客户编号~','业务名称~','费用名称~','实际金额~','操作时间~','操作员~','状态~'],
		titleInvoiceBaseInfo:'发票基础信息~',
		titleInvoiceOptrRec:'发票操作记录~',
		titleFeeDetail:'费用明细~',
		titleDetailQuery:'详细查询~'
	},
	InvoiceInput:{//发票领用
		startInvoiceId:'开始发票号~',
		endInvoiceId:'开始发票号~',
		invoiceCount:'发票共 {0} 张~',
		_title:'发票入库~',
		btnRecord:'录入~'
	},
	TransferInvoice:{//发票调拨
		_title:'发票调拨~',
		_titleSimple:'调拨~',
		transDepotId:'调拨对象~'
	},
	ReceiveInvoice:{//发票领用
		optrId:'领用营业员~',
		_title:'领用~'
	},
	CancelReceiveInvoice:{//发票取消领用
		_title:'取消领用~'
	},
	EditStatusInvoice:{//修改发票状态
		_title:'修改状态~'
	},
	
/** 设备管理开始 **/
	DeviceCommon:{//设备信息通用字段
		
	},
	EditOrder : {// 修改加入订单日期
		_title:'修改加入订单日期~'
	},
	OrderManager : {// 订单管理
		_title:'订单管理~'
	},
	CheckIn : {// 设备入库
		_title:'设备入库~'
	},
	CheckOut : {// 设备调拨
		_title:'设备调拨~'
	},
	AppUseDevice : {// 设备领用
		_title:'设备领用~'
	},
	DifferenceManage : {// 差异管理
		_title:'差异管理~'
	},
	DeviceStatus : {// 状态管理
		_title:'状态管理~'
	},
	BackHouse : {// 设备退库
		_title:'设备退库~'
	},
	Deploy : {// 基本配置
		_title:'基本配置~'
	},
	QueryDevice : {// 设备查询
		_title:'设备查询~'
	},
	DeviceDetailInfo : {// 详细查询
		_title:'详细查询~'
	},
	ReclaimDevice : {// 设备回收
		_title:'设备回收~'
	},
	DeviceLoss : {// 设备挂失
		_title:'设备挂失~'
	},
	DownloadDeviceInfo : {//设备盘点
		_title:'设备盘点~'
	},



/** 设备管理结束~.~ **/	
	//消息内容
	msgBox:{
		invoiceNotExists:'{0} 发票不存在~',
		selectInvoiceCode:'请选择发票代码~',
		emptyTextSelectStore:'请选择仓库~',
		invoiceIdLengthNotEquals:"发票开始号和发票结束号长度不一致~",
		needCorrectInvoiceId:'请正确输入发票号!~',
		needInputAtLeastOneInvoice:'请至少输入一个发票号码~',
		linkFailure:'连接异常!查询失败~',
		invoiceNotFoundTryReSearch:'未查询到发票信息，请重新输入发票号查询！~',
		selectTransDepotId:'请选择调拨仓库~',
		selectStatus2BeModify:'请选择要修改的状态~',
		confirmSaveBusiWithInvoice:'{0} 确定要保存业务吗?~',
		selectARecord:'请选择要操作的记录行！~'
			
	}
	
}