/**
 * common 放一些常用的，通用的.
 * 剩下的，按照功能划分,一个功能一个子对象.
 * @type 
 */
Sys = {
	common:{
		labelOldStatus:'原状态',
		labelNewStatus:'新状态',
		labelBuyMode:'购买方式',
		no:'否',
		status:'状态',
		query:'查询',
		inputKeyWork:'输入关键字',
		addNewOne:'新增',
		addSuccess:"添加成功!",
		saveSuccess:'保存成功!',
		saveFailed:'保存失败',
		updateSuccess:'修改成功!',
		deleteSuccess:'删除成功!',
		update:'修改',
		invalid:'作废',
		remove:'删除',
		saveBtn:'保存',
		cancel:'关闭',
		refresh:'刷新',
		filter:'过滤',
		cancelBtn:'取消',
		forbiddenBtn:'禁用',
		enableBtn:'启用',
		reset:'重置',
		doActionBtn:'操作',
		optr:'操作员',
		busiOptr:'营业员',
		businessHall:'营业厅',
		changeBtn:'变更',
		remarkTxt:'备注',
		exitBtn:'备注',
		to:'至',//从 xxx 至 xxxx
		pleaseSelect:'请选择...',
		orderNum:'编号',
		detailInfo:'详细信息',
		selectAll:'全选',
		confirm:'确认',
		unConfirm:'不确认',
		cancelConfirm:'取消确认',
		notForSure:'不确认',
		effect:'有效',
		createDate:'创建日期',
		createTime:'创建时间',
		restore:'恢复',
		downLoad:'下载',
		downLoadDetail:'下载明细',
		labelHistory:'历史',
		startDate:'开始日期',
		endDate:'结束日期',
		tipTxt:'提示',
		waitForUpload:'正在上传中,请稍候...',
		optrDetail:'操作明细',
		depotText:'所在仓库',
		countyText:'所在县市',
		certType:'凭证类型',
		operateType:'操作类型',
		labelDate:'日期',
		busiDate:'业务日期',
		labelBusiName:'业务名称',
		labelDoneCode:'流水号',
		confirmDoAction:'确认操作',
		typeSimple:'类型',
		modelSimple:'型号',
		labelName:'名称',
		labelChargeFee:'收费',
		
	statusEnum:{
		empty:'所有',
		ACTIVE:'正常',
		INVALID:'失效',
		IDLE:'空闲',
		USE:'使用'
	},
	msg:{//各种提示消息
		actionFailed:'操作失败',
		actionSuccess:'操作成功',
		confirmSave:'确认保存?',
		confirmDelete:'确认要删除?',
		noDataToBeprinted:'没有要打印的数据'
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
	DistrictNodeManage: {//行政区配置
		panelTitle:'行政区域管理',//面板主标题
		msg:{//各种提示消息
			actionFailed:'操作失败',
			actionSuccess:'操作成功',
			confirmSave:'确认保存?',
			confirmInvalid:'确认要禁用?',
			confirmDelete:'确认要删除?',
			confirmActivate:'确认要激活?',
			cantBeInvalided:'存在子级别,无法禁用!',
			maxDepthText: '不能再往下添加',
			noSingleQuoteAllowed:'请不要输入单引号'
		},
		formWin:{
			labelShowName:'显示名称',
			labelCountry:'国家名称',
			labelProvince:'省名称',
			labelCityName:'城市名称',
			labelCityDistrictName : '城市行政区名称',
			labelSubDistrictName : '子行政区名称',
			labelDesc:'名称描述',
			labelremark:'备注',
			labelAddrTree:'行政区域树',
			
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
			labelParentName:'上级名称',
			
			labelNewAddChild:'新增下级',
			labelNewAddBrother:'新增平级'
			
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
			emptyTxtCustBusiName:'受理编号',
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
	},//目前工单系统里的三个功能结束 .
	//收据子系统
	InvoiceCommon : {

		titleInvoiceInfo : '收据信息',
		titleInvoiceCount : '收据信息  数量：{0} ',
		titleInvoiceCountAndAmount : "收据信息 共选中{0}行,金额总和为{1}",

		tipInvoiceCountAndAmount : "一共:{0} 张收据，总额:{1} 元，",

		namount : "金额",
		check_time : "结账时间",
		close_time : "核销时间",
		create_time : "入库时间",
		depot_name : "仓&nbsp;&nbsp;&nbsp;&nbsp;库",
		check_depot_id : '结账仓库',
		finance_status : "结存状态",
		invoice_code : "收据代码",
		invoice_id : "收据号码",
		invoice_mode : '出票方式',
		invoice_type : "收据类型",
		finance_status : "结存状态",
		optr_name : "领用人",
		open_optr_name : "开票人",
		status : "使用状态",
		amount : '金&nbsp;&nbsp;额',
		use_optr : '领&nbsp;用&nbsp;人',
		use_time : "开票时间",
		cust_name : "客户名称",
		// 好多个功能下面的表格使用了同样的column
		commonGridColls : ['收据号码', '收据代码', '收据类型', '使用状态', '金额', '出票方式',
				'结存状态', '仓库', '领用人']
	},
	QueryInvoice:{//查询收据
			common:{
				titlaQueryInvoick:'收据查询',
				winTitleInvoiceDetail:'收据明细',
				titleMoneyCountPrefix:'有效总金额'				
			}
			
	},
	InvoiceDetail:{//收据详细信息
		InvoiceDepotDetailGridColls:['操作类型','收据流转','操作员','操作时间'],
		InvoiceDetailGridColls:['客户名称','客户编号','业务名称','费用名称','实际金额','操作时间','操作员','状态'],
		titleInvoiceBaseInfo:'收据基础信息',
		titleInvoiceOptrRec:'收据操作记录',
		titleFeeDetail:'费用明细',
		titleDetailQuery:'详细查询'
	},
	InvoiceInput:{//收据领用
		startInvoiceId:'开始收据号',
		endInvoiceId:'截止收据号',
		invoiceCount:'收据共 {0} 张',
		_title:'收据入库',
		btnRecord:'录入'
	},
	TransferInvoice:{//收据调拨
		_title:'收据调拨',
		_titleSimple:'调拨',
		transDepotId:'调拨对象'
	},
	ReceiveInvoice:{//收据领用
		optrId:'领用营业员',
		_title:'领用'
	},
	CancelReceiveInvoice:{//收据取消领用
		_title:'取消领用'
	},
	EditStatusInvoice:{//修改收据状态
		_title:'修改状态',
		editStatus:'修改状态为'
		
	},
	CheckInvoice: {
		_title: '发票结账'
	},
	CancelCheck: {
		_title: '取消结账'
	},
	CloseInvoice: {
		_title: '发票核销'
	},
	CancelCloseInvoice: {
		_title: '取消核销'
	},
	InvoiceRefunding: {
		_title:'发票退库'
	},
	
/** 设备管理开始 **/
	DeviceCommon:{//设备信息通用字段
		titleDeviceInfo:'设备信息',
		labelStbModel:'机顶盒型号',
		labelCardModel:'智能卡型号',
		labelFittingModel:'器材型号',
		labelModemModel:'猫类型',
		labelModemModel2:'MODEM型号',
		labelStbCode:'机顶盒号',
		labelCardCode:'智能卡号',
		labelModemCode:'MODEM号',
		labelDeviceType:'设备类型',
		labelDeviceTypeCode:'设备类型编号',
		labelPrice:'单价',
		labelOrderNum:'订购数量',
		labelOwnerShip:'产权',
		labelSupplier:'供应商',
		labelDefinition:'清晰',
		labelInteractiveType:'交互方式',
		labelInputDate:'入库日期',
		labelDevInputDepot:'入库仓库',
		labelNum:'数量',
		labelTotalNum:'库存数量',
		labelBatchNum:'批号',
		labelPairStbCode:'配对机顶盒号',
		labelPairStbType:'配对机顶盒型号',
		labelPairCardCode:'卡号或MAC',
		labelPairCardType:'配对卡型号',
		labelPairCardType2:'配对卡类型',
		labelPairModemType:'配对MODEM型号',
		labelPairModemCode:'MAC',
		labelDeviceModel:'设备型号',
		labelDevCode:'设备编号',
		labelDevCode2:'编号(Modem_mac)',
		labelDevStatus:'设备状态',
		labelInputDepotNum:'入库数量',
		titleInputSimple:'入库',
		labelDepot:'仓库',
		labelIsNewStb:'新机',
		labelIsNewStb2:'是否新机',
		titleModifyOrderNum:'修改单号',
		labelNewOrderNo:'新单号',
		labelTransNo:'调拨单号',
		labelTransStatus:'调拨状态',
		labelRestoreTrans:'恢复调拨',
		labelTransHistory:'历史调拨',
		titleTransInfo:'调拨信息',
		tipSupportFuzzyQuery:'支持编号模糊查询',
		labelDevBatchNum:'设备批号',
		labelTotalStoreNum:'库存数量',
		labelMateralInfo:'器材信息',
		labelDevDetail:'设备明细',
		labelApplyInfo:'领用信息',
		labelSwitchDepot:'切换仓库',
		labelVitualModemModel:'虚拟MODEM型号',
		labelVitualModemCode:'虚拟MODEM号',
		labelDevFile:'设备文件',
		labelOperateTime:'操作时间',
		labelFileOperate:'文件操作',
		labelManualInput:'手工录入',
		labelSingleStb:'机顶盒',
		labelStbCardModemPair:'盒卡MODEM配对',
		labelSingleCard:'单智能卡',
		labelStbCardPair:'机卡配对',
		labelSingleModem:'MODEM',
		labelStbModemPair:'机MODEM配对',
		labelConfirmDate:'确认时间',
		labelConfirmOptr:'确认人',
		labelCustName:'客户名称',
		labelCustStr9:'发展人',
		labelCustNo:'客户编号',
		vitualDevice:'虚拟设备',
		isDifferent:'是否差异',
		labelBackUp:'备机',
		labelModemType:'MODEM类型',
		labelDepotStatus:'库存状态',
		labelRecordTime:'入库时间',
		labelStbType:'盒类型',
		labelCardType:'卡类型',
		labelPairModemType2:'配对MODEM类型',
		labelTypeName:'类型名称',
		labelBoxNo:'箱号',
		selectByInputNoBatchNumDeviceType:'支持入库编号,批号,设备类型模糊查询',
		filesFormatOne:'支持xls和txt,格式为：第一行为空,共3列：箱号,设备号,卡号',
		filesFormatTwo:'支持xls和txt,格式为：第一行为空,共3列：箱号,设备号,MAC',
		filesFormatThree:'支持xls和txt,格式为：第一行为空,可以2或3列,(2列的话系统默认MAC=设备号)<br>2列:箱号,设备号;3列：箱号,设备号,MAC',
		filesFormatFour:'支持xls和txt,格式为：第一行为空,共1列：设备号'
		
		
	},
	
	DepotIndexManager:{//终端管理
		_title:'终端管理',
		titleExamOutPanel:'待审批调出',
		columnNames:['流转编号','源仓库','目标仓库','状态','创建人','创建时间','确认人','确认时间','确认信息','备注'],
		titleExamInPanel:'待审批调入'
	},
	EditOrder : {// 修改加入订单日期
		_title:'修改加入订单日期'
	},
	OrderManager : {// 订单管理
		_title:'订单管理',
		addTitle:"添加订单",
		titleOrderGrid:'订单信息',
		titleOrderInputDetailGrid:'到货明细',
		modifyTitle:"修改订单",
		titleAddOrder:'添加订单',
		btnMakeHistory:'历史',
		btnResumeOrder:'恢复',
		btnResumeOrderTip:'恢复订单',
		columnsOrderGrid:["编号", "供应商", "供货日期", "设备类型", "型号", "单价", "订购数量", "到货数量", "订单类型", "备注", "操作"],
		columnsOrderInputDetailGrid:['设备类型','型号','到货日期','数量'],
		tipOrderStatus:'执行中的订单或历史订单',
		orderStatus:{
			ALL:'所有订单',
			NOW:'执行订单',
			HISTORY:'历史订单'
		},
		labelOrderNo:'订单编号',
		labelSupplier:'供应商',
		labelSupplyDate:'供货日期'
		
	},
	CheckIn : {// 设备入库
		_title:'设备入库',
		labelInputNo:'入库单号',
		labelOrderNo:'订单号',
		labelCheckBatchNum:'入库批号',
		titleCheckInGrid:'入库信息',
		labelFileInput:'文件入库',
		labelManualInput:'手工入库',
		labelMateralCheckIn:'器材入库',
		labelInputOptr:'入库人',
		labelInputBatchNum:'入库批号'
		
		
		
	},
	CheckOut : {// 设备调拨
		_title:'设备调拨',
		titleTransConfirm:'调拨确认',
		labelConfirmInfo:'确认信息!',
		labelTransType:'调拨类别',
		labelConfirmDate:'确认日期',
		labelTransIn:'转发',
		transStatus:{
			ALL:'所有调拨',
			NOW:'执行调拨',
			HISTORY:'历史调拨'
		},
		tipTransStatus:'执行中或历史调拨',
		labelFileTrans:'文件调拨',
		labelManualTrans:'手工调拨',
		labelBatchNumTrans:'批号调拨',
		labelMateralTrans:'器材调拨',
		labelOldTransStbNum:'原调拨机顶盒数量',
		labelNewTransStbNum:'本次调拨机顶盒数量',
		labelOldTransCardNum:'原调拨智能卡数量',
		labelNewTransCardNum:'本次调拨智能卡数量',
		labelOldTransModemNum :'原调拨猫数量',
		labelNewTransModemNum:'本次调拨猫数量',
		labelPrint: '打印'
	},
	AppUseDevice : {// 设备领用
		_title:'设备领用',
		deviceCodeNeeded:'请输入设备号:',
		labelProcureType:'领用类型',
		labelProcurer:'领用人',
		labelProcureDept:'领用部门',
		labelProcureNo:'领用单号',
		labelProcureTime:'领用时间',
		labelProcureNo:'领用单号',
		arrayDocType:[['销售单'],['领导批条']],
		labelFeeNo:'缴费单号'
	},
	DifferenceManage : {// 差异管理
		_title:'差异管理',
        labelDiffType:'差异类型',
        labelDiffTime:'差异时间',
        labelManualDiff:'手工差异',
        labelFileDiff:'文件差异',
        labelConfirmDiff:'确认差异',
        labelCancelDiff:'取消差异',
        titleManualAddDiff:'手工增加差异',
        titleFileAddDiff:'文件增加差异'
    
	},
	DeviceStatus : {// 状态管理
		_title:'状态管理',
		labelSetNewOrOld:'设新旧',
		titleFileOptr:'设备状态管理(文件操作)',
		titleManualOptr:'设备状态管理(手工录入)'
	},
	BackHouse : {// 设备退库
		_title:'设备退库',
		titleSimple:'退库',
		outPutNo:'退库编号',
		outPutType:'退库类型',
		outPutDate:'退库日期',
		titleOutputInfo:'退库信息',
		fileOutput:'文件退库',
		manualOutput:'手工退库',
		materalOutPut:'器材退库'
		
	},
	Deploy : {// 基本配置
		_title:'配置',
		titleMateralCfg:'器材型号配置',
		titleProducerCfg:'供应商配置',
		titleBuyTypeCfg:'购买方式配置',
		labelMateralType:'器材类型',
		labelModelName:'型号名称',
		stbCardPairCfg:'机卡配对配置',
        labelProducer:'制造商',
        labelSelectableCardModel:'可选智能卡型号',
        labelSelectedCardModel:'可选智能卡型号',
        isVitual:'是否虚拟',
        caType:'CA类型',
		labelBuyType:'购买类型',
		labelBuyTypeCode:'购买方式代码',
		labelChangeOwnership:'转换产权',
		labelNetType:'接入类型',
		titleDevTypeCfg:'设备类型配置',
		titleCountyCfg:'适用地区配置',
		titleTypeCountyCfg:'分配型号-> {0}',
		labelAssignType:'分配型号',
		labelSuitableCounty:'适用地区'
		
	},
	QueryDevice : {// 设备查询
		_title:'设备查询'
	},
	DeviceDetailInfo : {// 详细查询
		_title:'详细查询',
		titleUseRecord:'使用记录',
		labelTargetDepot:'目标仓库',
		labelSourceDepot:'源仓库'
	},
	ReclaimDevice : {// 设备回收
		_title:'设备回收',
		labelConfirmRecycle:'是否回收',
		titleRecycleConfirm:'回收确认',
		labelRecycleStatus:'执行中或历史回收',
		recycleStatusEnum:{
			ALL:'所有回收',
			NOW:'执行回收',
			HISTORY:'历史回收'
		},
		labelRecycleReason:'回收原因'
	},
	DeviceLoss : {// 设备挂失
		_title:'设备挂失',
		titleAddDevLoss:'添加设备挂失',
		labelCancelLoss:'取消挂失'
	},
	DownloadDeviceInfo : {//设备盘点
		_title:'设备盘点'
	},



/** 设备管理结束. **/	
	//消息内容
	msgBox:{
		invoiceNotExists:'{0} 收据不存在',
		selectInvoiceCode:'请选择收据代码',
		emptyTextSelectStore:'请选择仓库',
		invoiceIdLengthNotEquals:"收据开始号和收据结束号长度不一致",
		needCorrectInvoiceId:'请正确输入收据号!',
		needInputAtLeastOneInvoice:'请至少输入一个收据号码',
		linkFailure:'连接异常!查询失败',
		invoiceNotFoundTryReSearch:'未查询到收据信息，请重新输入收据号查询！',
		selectTransDepotId:'请选择调拨仓库',
		selectStatus2BeModify:'请选择要修改的状态',
		confirmSaveBusiWithInvoice:'{0} 确定要保存业务吗?',
		selectARecord:'请选择要操作的记录行！',
		supportOrderFuzzyQuery:'支持订单编号模糊查询',
		confirmConvert2HisOrder:'确认转换为历史订单吗？',
		confirmConvert2NowOrder:'确认转换为执行中的订单吗？',
		orderNumCantLessThanGoodsNum:'订购数量不能小于到货数量!',
		confirmDelete:'确定删除吗?',
		tipSelectOneRow:'请选中要操作的行',
		confirmDoAction:'确定操作吗?',
		selectDevType:'请选择设备类型!',
		selectModel:'请选择型号!',
		numberShouldBiggerThan0:'单价请输入大于零的数字!',
		orderNumShouldBigThan0:'订购数量请输入大于零的数字!',
		confirmOverideSaveDevModel:'存在不同的型号设备，是否保存',
		errCantAssignParent4DevNo:'请为设备编号单元格配置parent属性指向表格',
		pleaseSelectSaveTypeAndModel:'请先选择设备类型和设备型号',
		deviceCodeExists:'设备号已存在',
		supportDevCodeOrderNoFuzzyQuery:'支持入库编号和订单编号模糊查询',
		tipCheckInFileInput:"文件入库：[机顶盒]：第一列：机顶盒型号,第二列：机顶盒编号,第三列：配对智能卡编号(可空);第四列：配对MODEM编号(可空),[智能卡]:第一列：设备型号,第二列：设备编号;[modem]：第一列：modem型号,第二列：mac地址,第三列：modem编号; 最后一列为批号",
		fileUploadSuccess:'文件上传成功!',
		fileUploadFailure:"文件上传失败!",
		pleaseInputCorrectDevInfo:'请正确输入设备信息！',
		transSuccess:'调拨确认成功',
		pleaseInputBatchNum:'请输入批号查询',
		tipDevCheckFileFormat:'文件入库：只有1列设备编号',
		confirm2HisTrans:'确认转换为历史调拨吗?',
		confirm2NowTrans:'确认转换为执行中的调拨吗?',
		warnHasSaveCode:'编号有相同的，请检查！',
		notSuitableDev4Trans:'本批次调拨的设备已没有符合可调拨条件的设备!',
		pleaseSelectDev2Trans:'请选择需要调拨的设备',
		tipOutOfStock:'不能大于库存数量！',
		tipHasSameMateral:'器材有相同的，请检查！',
		tipTransNumCantBeEmpty:'调拨数量不能为空！',
		tipOutputNumCantBeEmpty:'退库数量不能为空',
		pleaseUploadExcelFile:'请选择excel文件进行上传',
		pleaseUploadExcel2003:'请选择excel2003文件进行上传,文件后缀名为.xls!',
		needDevCode2Query:'输入设备编号回车查询',
		confirmCancelApply:"确定取消领用吗?",
		confirmCancelAllApply:"确定取消所有领用吗?",
		supportDevCodeQuery:'支持设备编号查询',
		tipDevHasNoDiff:'该设备已差异，无需再确认',
		tipDevNotDiffCantCancel:'该设备未差异，不能取消确认',
		tipDevFileDiffInfo:'文件增加差异：文件类型为Excel，共一列，第一列为差异设备编号',
		tipConfirmDiffSuccess:'确认差异成功!',
		tipCalcenDiffSuccess:'取消差异成功!',
		tipSelectTragetDevStatus : '请先选择要修改的设备状态类型!',
		fileDownloadFailed:'文件下载失败',
		confirmCancelRecycle:'确认取消回收吗?',
		tipDevDosNotExists:'查询的设备不存在',
		tipQueryDateBeforeDownLoad:'请先查询出数据，方能下载!!',
		tipPleaseEditWell:'请编辑完整',
		duplicateRows:"第 {0} 行和第 {1} 行相同,请重新编辑"
    
		
		
	}
	
}