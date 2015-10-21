/**
 * common 放一些常用的，通用的. 剩下的，按照功能划分,一个功能一个子对象.
 * 
 * @type
 */
Sys = {
	common : {
		no : 'No~',
		status : 'status~',
		query : 'Query~',
		inputKeyWork : 'Enter keyword~',
		addNewOne : 'new~',
		addSuccess : "Adding success ~!",
		saveSuccess : 'Save success~!',
		saveFailed : 'Save failed~',
		updateSuccess : 'Modify success~!',
		deleteSuccess : 'deleted successfully~!',
		update : 'modify~',
		invalid : 'void~',
		remove : 'delete~',
		saveBtn : 'Save~',
		cancel : 'Close~',
		refresh : 'refresh~',
		filter : 'filter~',
		cancelBtn : 'cancel~',
		forbiddenBtn : 'disabled~',
		enableBtn : 'Enable~',
		reset : 'Reset~',
		doActionBtn : 'Manipulation~',
		optr : 'operator~',
		busiOptr : 'salesperson~',
		businessHall : 'operating room~',
		changeBtn : 'change~',
		remarkTxt : 'Remarks~',
		exitBtn : 'Remarks~',
		to : 'to~',
		pleaseSelect : 'Please select ...~',
		orderNum : 'Number~',
		detailInfo : 'For more information~',
		selectAll : 'Select~',
		confirm : 'confirm~',
		unConfirm : 'not recognized~',
		cancelConfirm : 'cancellation confirmation~',
		notForSure : 'not recognized~',
		effect : 'effective~',
		createDate : 'Creation Date~',
		createTime : 'Creation Time~',
		restore : 'restore~',
		downLoad : 'download~',
		downLoadDetail : 'Download Details~',
		labelHistory : 'History~',
		startDate : 'Start date~',
		endDate : 'End date~',
		tipTxt : 'prompt~',
		waitForUpload : 'being uploaded, please wait ...~',
		optrDetail : 'Operation Details~',
		depotText : 'where the warehouse~',
		countyText : 'where cities and counties~',
		certType : 'Document type~',
		operateType : 'operation type~',
		labelDate : 'Date~',
		busiDate : 'business date~',
		labelBusiName : 'Business name~',
		labelDoneCode : 'Serial~',
		confirmDoAction : 'confirm the operation~',
		typeSimple : 'type~',
		modelSimple : 'model~',
		labelName : 'name~',
		labelChargeFee : 'Pay~',

		statusEnum : {
			empty : 'all ~',
			ACTIVE : 'normal~',
			INVALID : 'failure~',
			IDLE : 'idle~',
			USE : 'use~'
		},
		msg : {// 各种提示消息
			actionFailed : 'The operation failed~',
			actionSuccess : 'operation was successful~',
			confirmSave : 'Confirm save~?',
			confirmDelete : 'sure you want to delete~?',
			noDataToBeprinted : 'there is no data to be printed~'
		}
	},
	ExchangeRateManage : {// 汇率配置
		title : 'Rates Configuration~',
		commons : {
			exchange_id : 'exchange rate number ~',
			effective_time : 'effective time ~',
			exchange_rate : 'exchange rate',
			status : 'status~',
			query : 'Query~',
			addNewOne : 'new~',
			update : 'modify~',
			invalid : 'void~',
			saveBtn : 'Save~',
			cancelBtn : 'cancel~',
			optr : 'operator~',
			create_time : 'Creation Time~'
		},
		status : {
			empty : 'all',
			ACTIVE : 'ACTIVE',
			INVALID : 'INVALID'
		},
		msg : {
			successMsg : 'success',
			onlyOne : 'choose and select only one record~.',
			areYouSure : 'Are you sure~?'
		}
	},
	AddressNodeManage : {// 地址节点配置
		panelTitle : 'Address Management',// 面板主标题
		msg : {// 各种提示消息
			actionFailed : 'action failed.',
			actionSuccess : 'success',
			confirmSave : 'Click OK to save these settings?',
			confirmInvalid : 'Click OK to invalid',
			confirmDelete : 'Click OK to delte',
			confirmActivate : 'Click OK to activate',
			cantDelete : 'Occupied,can\'t be deleted',
			cantBeInvalided : 'it has sub nodes,can\'t be invalided.',
			maxDepthText : 'can\'t add child node.',
			noSingleQuoteAllowed : 'single quote is forbidden.'

		},
		formWin : {
			labelSortNum : 'sort num',
			labelProvince : 'province',
			labelDistrict : 'district',
			labelNetType : 'net type',
			emptyTxtProvince : 'please select a province',
			emptyTxtBlurQuery : 'support blur query',
			btnTxtClose : 'close',
			btnTxtSave : 'save',
			labelRoadNum : 'road number',
			titleNewSaveLevelRoadNum : 'add sibling road number',
			labelStreatName : 'street name',
			titleNewSaveLevelStreet : 'add street',
			labelCityName : 'city name',
			titleNewSaveLevelCity : 'add sibling node',
			labelShowName : 'show name',
			labelParentName : 'parent address name',

			labelNewAddChild : 'add child node',
			labelNewAddBrother : 'add sibling node',
			labelAddrTree : 'Address tree'
		}
	},
	DistrictNodeManage : {//行政区配置
		panelTitle : 'Address Management',// 面板主标题
		msg : {// 各种提示消息
			actionFailed : 'action failed.',
			actionSuccess : 'success',
			confirmSave : 'Click OK to save these settings?',
			confirmInvalid : 'Click OK to invalid',
			confirmDelete : 'Click OK to delte',
			confirmActivate : 'Click OK to activate',
			cantBeInvalided : 'it has sub nodes,can\'t be invalided.',
			maxDepthText : 'can\'t add child node.',
			noSingleQuoteAllowed : 'single quote is forbidden.'

		},
		formWin : {
			labelShowName : 'show name',
			labelProvince : 'province name',
			labelCityName : 'city name',
			labelCityDistrictName : 'city Administrative Region',
			labelSubDistrictName : 'sub Administrative Region',
			labelDesc:'name description~',
			labelremark:'Remark~',
			labelAddrTree : 'district tree',
			
			labelDistrict : 'district',
			labelNetType : 'net type',
			emptyTxtProvince : 'please select a province',
			emptyTxtBlurQuery : 'support blur query',
			btnTxtClose : 'close',
			btnTxtSave : 'save',
			labelRoadNum : 'road number',
			titleNewSaveLevelRoadNum : 'add sibling road number',
			labelStreatName : 'street name',
			titleNewSaveLevelStreet : 'add street',
			labelCityName : 'city name',
			titleNewSaveLevelCity : 'add sibling node',
			labelParentName : 'parent address name',

			labelNewAddChild : 'add child node',
			labelNewAddBrother : 'add sibling node'
			
		}
	},
	WorkTask : {
		msg : {
			minTimeText : 'You can not select the day before~',
			needSelectOneRecord : 'Please select the record to be operated~',
			needSelectOnlyOneRecord : 'can only select a record operating~!',
			cantReplyWorkTask : 'Only the construction work orders to return to single~',
			workTaskInvalided : 'ticket obsolete~',
			sureToInvalidWorkTask : 'sure you want to void the selected work order you~?',
			invalidReason : 'void reason~',
			cantBeInvalided : 'allowed void~!',
			cantSendWorkTask : 'Only appointments and construction work order to send a single~!',
			printWorkTaskToolTip : 'Print Construction of single~',
			writeWorkReplyTip : 'construction is completed, fill out the information back to a single~',
			replySuccess : 'back to a single success~!',
			replyFailure : 'back to a single failure~!',
			sendWorkTaskSuccess : 'send a single success~!',
			sendWorkTaskFailure : 'send a single failure~!',
			confirmSaveReplyInfo : 'sure you want to save it back to a single information~?',
			replyInfoNotComplete : 'back to a single information Incomplete~!',
			confirmSendWorkTask : 'you sure you want to send a single you~?',
			warnSelectWorkTeam : 'Please select construction sector~!'
		},
		common : {
			replyWrokTaskTitle : 'back to a single~',
			sendWrokTaskTitle : 'send a single~',
			taskType : 'ticket type~',
			finish : 'complete~',
			failure : 'Failed~'

		},
		formLabels : {// form of labels and table column names
			replyRemark : 'back to a single remark -',
			failReason : 'Cause Failed~',
			custPrise : 'Testimonials~',
			finishFlag : 'Are completed~',
			finishOptr : 'completed staff~',
			finishDept : 'completed sector~',
			finishDate : 'completion date~',
			assignDate : 'dispatch date~',
			mobile : 'Mobile~',
			phone : 'phone~',
			contack : 'Information~',
			contactPerson : 'Contact~',
			contactPhone : 'Tel~',
			workDept : 'construction sector~',
			workTaskStatus : 'work order status~',
			createDate : 'Creation Date~',
			createTime : 'Creation Time~',
			orderDate : 'appointment~',
			servType : 'Service type~',
			createType : 'Create mode~',
			workTaskType : 'ticket type~',
			workTaskInfo : 'ticket information~',
			remark : 'Remarks~',
			faultReason : 'Cause~',
			newAddr : 'new address~',
			oldAddr : 'old address~',
			labelAddress : 'address~',
			optrDate : 'Operation Date~',
			businessName : 'Business name~',
			optr : 'operator~',
			businessHall : 'Business Office~',
			busiInfo : 'Business Information~',
			custAddr : 'Customer address~',
			custBusiName : 'Acceptance No.~',
			custName : 'Customer Name~',
			custInfo : 'customer information~',
			workRemark : 'Construction Notes~',
			createOptr : 'creators~',
			businessType : 'Business Type~'
		},
		formWin : {// some other text forms and tables, windows used
			titleChangeWorker : 'Change Construction Worker~',
			labelNewWorker : 'New Construction Worker~',
			labelFormerWorker : 'former construction workers~',
			labelNewOrderTime : 'new appointment~',
			labelFormerOrderTime : 'original appointment time~',
			labelSendWorkTask : 'to selected construction alone were to send a single~',
			labelInvalidWorkTask : 'invalid selected construction single~',
			labelWorkTaskType : 'ticket type~',
			emptyTxtWorkTeam : 'construction team~',
			emptyTxtAddress : 'address~',
			emptyTxtContactPhone : 'Mobile~',
			emptyTxtCustBusiName : 'Acceptance No.~',
			titleWorkDetailInfo : 'ticket details~',
			labelCustPrise : 'Testimonials~',
			labelWorkFinishDate : 'Construction Completion Date~',
			minTextWorkFinishDate : 'You can not select the day before~',
			labelWorker : 'Construction Worker~',
			labelSatisfy : 'satisfaction~',
			labelFinishType : 'completion type~',
			labelFailReason : 'Cause Failed~',
			titleAllWorkTask : 'All tickets~',
			titleFinishedWorkTask : 'has completed work orders~',
			titleUnFinishedWorkTask : 'Processing work orders~',
			labelWaitAcceptTas : 'pending work orders~',
			btnSearchWorkTask : 'Search Ticket~'

		}
	},
	InvoiceCommon : {

		titleInvoiceInfo : 'receipt information~',
		titleInvoiceCount : 'receipt of the amount of information: {0}~',
		titleInvoiceCountAndAmount : "Receipt Leads select {0} lines, the total amount of {1}",

		tipInvoiceCountAndAmount : "Total: {0} receipts, total: {1} dollar,",

		namount : "the amount of ...",
		check_time : "Closing Time~",
		close_time : "verification time~",
		create_time : "timespan~",
		depot_name : "depot~",
		check_depot_id : 'Checkout warehouse~',
		finance_status : "balance state~",
		invoice_code : "Receipt Code~",
		invoice_id : "Receipt number~",
		invoice_mode : 'ticket way~',
		invoice_type : "Receipt type~",
		finance_status : "balance state~",
		optr_name : "requisitioned people~",
		open_optr_name : "drawer~",
		status : "the use of state~",
		amount : 'money~',
		use_optr : 'Consuming people~',
		use_time : "Billing Time~",
		cust_name : "Customer name~",
		// Good number of functions The following table uses the same column
		commonGridColls : ['receipt number~', 'receipt of code~',
				'receipt type~', 'use of state ~', 'the amount of~',
				'for ticket~', 'Balance state~', 'warehouse~',
				'requisitioned people~']
	},
	QueryInvoice : {// Query Receipt
		common : {
			titlaQueryInvoick : 'receipts inquiry~',
			winTitleInvoiceDetail : 'Receipt Details~',
			titleMoneyCountPrefix : 'effective total amount~'
		}

	},
	InvoiceDetail : {
		InvoiceDepotDetailGridColls : ['operation type~',
				'receipt circulation~', 'operator~', 'operating time~'],
		InvoiceDetailGridColls : ['customer name~', 'customer number~',
				'business name~', 'costs name ~,', 'the actual amount of~',
				'operating time~', 'operator~', 'status~'],
		titleInvoiceBaseInfo : 'receipt basic information~',
		titleInvoiceOptrRec : 'receipt operating record~',
		titleFeeDetail : 'cost breakdown~',
		titleDetailQuery : 'detailed inquiry~'
	},
	InvoiceInput : {// receipt recipients
		startInvoiceId : 'start receipt number~',
		endInvoiceId : 'As of receipt number~',
		invoiceCount : 'receipt of {0} Zhang~',
		_title : 'receipt storage~',
		btnRecord : 'Entry~'
	},
	TransferInvoice : {// receipts allocated
		_title : 'receipts allocated~',
		_titleSimple : 'allocation~',
		transDepotId : 'allocate objects~'
	},
	ReceiveInvoice : {// receipt recipients
		optrId : 'recipients salesperson~',
		_title : 'recipients~'
	},
	CancelReceiveInvoice : {// receipt canceled recipients
		_title : 'Cancel requisitioned~'
	},
	EditStatusInvoice : {// modify the receipt state
		_title : 'Modify status~',
		editStatus : 'modify status is~'

	},
	CheckInvoice: {
		_title: 'Invoices Checkout~'
	},
	CancelCheck: {
		_title: 'Cancel Checkout~'
	},
	CloseInvoice: {
		_title: 'Invoice verification~'
	},
	CancelCloseInvoice: {
		_title: 'Cancellation verification~'
	},
	InvoiceRefunding: {
		_title:'Invoices refunding~'
	},

	DeviceCommon : {// Device Information General fields
		titleDeviceInfo : 'Device Info~',
		labelStbModel : 'set-top box models~',
		labelCardModel : 'smart card type~',
		labelFittingModel : 'equipment models~',
		labelModemModel : 'cat type~',
		labelModemModel2 : 'MODEM model~',
		labelStbCode : 'set-top box to No.',
		labelCardCode : 'smart card~',
		labelModemCode : 'MODEM No.~',
		labelDeviceType : 'device type~',
		labelDeviceTypeCode : 'device type number~',
		labelPrice : 'Price~',
		labelOrderNum : 'The quantity~',
		labelOwnerShip : 'Property~',
		labelSupplier : 'supplier~',
		labelDefinition : 'clear~',
		labelInteractiveType : 'interactive mode~',
		labelInputDate : 'storage date~',
		labelDevInputDepot : 'storage warehouse~',
		labelNum : 'Number~',
		labelTotalNum : 'Stock Number~',
		labelBatchNum : 'Lot~',
		labelPairStbCode : 'paired set-top box to No.',
		labelPairStbType : 'pairing STB models~',
		labelPairCardCode : 'card number or MAC~',
		labelPairCardType : 'pairing card model~',
		labelPairCardType2 : 'pairing card type~',
		labelPairModemType : 'pairing MODEM models~',
		labelPairModemCode : 'MAC~',
		labelDeviceModel : 'device model~',
		labelDevCode : 'device numbers~',
		labelDevCode2 : 'number (Modem_mac)~',
		labelDevStatus : 'Device Status~',
		labelInputDepotNum : 'storage quantity~',
		titleInputSimple : 'storage~',
		labelDepot : 'Warehouse~',
		labelIsNewStb : 'The new machine~',
		labelIsNewStb2 : 'whether the new machine~',
		titleModifyOrderNum : 'amendments to No.',
		labelNewOrderNo : 'New single number~',
		labelTransNo : 'requisition number~',
		labelTransStatus : 'allocation of state~',
		labelRestoreTrans : 'Recovery allocation~',
		labelTransHistory : 'History allocation~',
		titleTransInfo : 'allocation information~',
		tipSupportFuzzyQuery : 'support number fuzzy query~',
		labelDevBatchNum : 'equipment Lot~',
		labelTotalStoreNum : 'Stock Number~',
		labelMateralInfo : 'equipment information~',
		labelDevDetail : 'instrument details~',
		labelApplyInfo : 'recipients of information~',
		labelSwitchDepot : 'Switching warehouse~',
		labelVitualModemModel : 'Virtual MODEM models~',
		labelVitualModemCode : 'Virtual MODEM number~',
		labelDevFile : 'device file~',
		labelOperateTime : 'operating time~',
		labelFileOperate : 'file operations~',
		labelManualInput : 'manual entry~',
		labelSingleStb : 'set-top boxes',
		labelStbCardModemPair : 'cassette MODEM paired~',
		labelSingleCard : 'single smart card~',
		labelStbCardPair : 'Machine card pair~',
		labelSingleModem : 'MODEM',
		labelStbModemPair : 'Machine MODEM paired~',
		labelConfirmDate : 'confirmation time~',
		labelConfirmOptr : 'confirm man~',
		labelCustName : 'Customer Name~',
		labelCustStr9 : 'Development of Man',
		labelCustNo : 'Customer ID~',
		vitualDevice : 'virtual equipment~',
		isDifferent : 'Are the differences~',
		labelBackUp : 'backup machine~',
		labelModemType : 'MODEM type~',
		labelDepotStatus : 'inventory status~',
		labelRecordTime : 'timespan~',
		labelStbType : 'box type~',
		labelCardType : 'card type~',
		labelPairModemType2 : 'pairing MODEM type~',
		labelTypeName : 'type name~',
		labelBoxNo:'Box No.~',
		selectByInputNoBatchNumDeviceType:'Support input No. or batch number or device type fuzzy query~',
		filesFormatOne:'Support XLS and TXT format: first row is space, a total of 3 columns: box number, device number, card number~',
		filesFormatTwo:'Support XLS and TXT format: first row is space, a total of 3 columns: box number, device number,MAC~',
		filesFormatThree:'Support XLS and TXT format: first row is space,2 or 3 columns (2 columns, then the system default MAC= device number) <br> 2 column: box number, device number; 3 columns: box number, device number, MAC~',
		filesFormatFour:'Support XLS and TXT format: first row is space, a total of 1 columns: device number~'

	},

	DepotIndexManager : {// terminal management
		_title : 'terminal management~',
		titleExamOutPanel : 'pending transferred out~',
		columnNames : ['circulation number ~', 'the source repository~',
				'target warehouse~', 'state~', 'Created~', 'creation time~',
				'confirm man~', 'confirm the time~', 'confirmation message~',
				'Notes~'],
		titleExamInPanel : 'pending redeployment~'
	},
	EditOrder : {// modification to the order date
		_title : 'modification to the order date~'
	},
	OrderManager : {// Order Management
		_title : 'Order Management~',
		addTitle : "Add an order ...",
		titleOrderGrid : 'Order information~',
		titleOrderInputDetailGrid : 'arrival details~',
		modifyTitle : "Modify Order~",
		titleAddOrder : 'Add orders~',
		btnMakeHistory : 'History~',
		btnResumeOrder : 'restore~',
		btnResumeOrderTip : 'restore order~',
		columnsOrderGrid : ["number~", "supplier ...", "Delivery Date~",
				"Device Type~", "Type~", "Unit~", "Quantity~",
				"arrival number~", "Order Type~", "Memo~", "Operation -"],
		columnsOrderInputDetailGrid : ['device type~', 'model~',
				'arrival date~', 'Number~'],
		tipOrderStatus : 'execution order or order history~',
		orderStatus : {
			ALL : 'All orders~',
			NOW : 'Executive Order~',
			HISTORY : 'History Order~'
		},
		labelOrderNo : 'Order Number~',
		labelSupplier : 'supplier~',
		labelSupplyDate : 'Delivery Date~'

	},
	CheckIn : {// equipment storage
		_title : 'equipment storage~',
		labelInputNo : 'Receipt No.~',
		labelOrderNo : 'Order No.~',
		labelCheckBatchNum : 'storage lot~',
		titleCheckInGrid : 'storage information~',
		labelFileInput : 'file storage~',
		labelManualInput : 'manual storage~',
		labelMateralCheckIn : 'equipment storage~',
		labelInputOptr : 'warehousing people~',
		labelInputBatchNum : 'storage lot~'

	},
	CheckOut : {// equipment allocation
		_title : 'equipment allocation~',
		titleTransConfirm : 'redeployment confirm~',
		labelConfirmInfo : 'Claim your profile~!',
		labelTransType : 'allocation category~',
		labelConfirmDate : 'confirm the date~',
		labelTransIn : 'forwarding~',
		transStatus : {
			ALL : 'All allocation~',
			NOW : 'implementation of the allocation~',
			HISTORY : 'History allocation~'
		},
		tipTransStatus : 'execution or historical allocation~',
		labelFileTrans : 'file allocation~',
		labelManualTrans : 'Hand allocation~',
		labelBatchNumTrans : 'Lot allocation~',
		labelMateralTrans : 'equipment allocation~',
		labelOldTransStbNum : 'original allocation of the number of set-top boxes~',
		labelNewTransStbNum : 'The allocation of the number of set-top boxes~',
		labelOldTransCardNum : 'original allocation of the smart card number~',
		labelNewTransCardNum : 'The allocation of the smart card number~',
		labelOldTransModemNum : 'original allocation cat quantity~',
		labelNewTransModemNum : 'The amount allocated cat~',
		labelPrint : 'Print~'
	},
	AppUseDevice : {// equipment requisitioned
		_title : 'device recipients~',
		deviceCodeNeeded : 'Please enter the device number:~',
		labelProcureType : 'requisitioned type~',
		labelProcurer : 'recipients man~',
		labelProcureDept : 'Consuming sector~',
		labelProcureNo : 'Consuming a single number~',
		labelProcureTime : 'time to the recipients',
		labelProcureNo : 'Consuming a single number~',
		arrayDocType : [['sales of single~'], ['leadership batch article~']],
		labelFeeNo : 'billing numbers~'
	},
	DifferenceManage : {// difference management
		_title : 'differences Management~',
		labelDiffType : 'Differences type~',
		labelDiffTime : 'differences in time to the',
		labelManualDiff : 'Hand difference~',
		labelFileDiff : 'file differences~',
		labelConfirmDiff : 'confirm the difference~',
		labelCancelDiff : 'Cancel difference~',
		titleManualAddDiff : 'Hand increases the difference between~',
		titleFileAddDiff : 'document increases the difference between~'
	},
	DeviceStatus : {// State Management
		_title : 'state management~',
		labelSetNewOrOld : 'Let the old and new~',
		titleFileOptr : 'device state management (File operation)~',
		titleManualOptr : 'device state management (manual entry)~'
	},
	BackHouse : {// equipment refunding
		_title : 'equipment refunding~',
		titleSimple : 'refunding~',
		outPutNo : 'refunding No.~',
		outPutType : 'refunding type~',
		outPutDate : 'refunding date~',
		titleOutputInfo : 'refunding information~',
		fileOutput : 'file refunding~',
		manualOutput : 'Hand refunding~',
		materalOutPut : 'equipment refunding~'

	},
	Deploy : {// Basic Configuration
		_title : 'Configuration~',
		titleMateralCfg : 'Equipment Model Configuration~',
		titleProducerCfg : 'vendor configuration~',
		titleBuyTypeCfg : 'Buy configure~',
		labelMateralType : 'equipment type~',
		labelModelName : 'Model name~',
		stbCardPairCfg : 'machine card pairing configuration~',
		labelProducer : 'manufacturer~',
		labelSelectableCardModel : 'optional smart card model~',
		labelSelectedCardModel : 'optional smart card model~',
		isVitual : 'Are virtual~',
		caType : 'CA type~',
		labelBuyType : 'purchase type~',
		labelBuyTypeCode : 'Buy the code~',
		labelChangeOwnership : 'conversion property~',
		labelNetType : 'access type~',
		titleDevTypeCfg : 'type of device configuration~',
		titleCountyCfg : 'jurisdictions configuration~',
		titleTypeCountyCfg : 'allocation models -> {0}',
		labelAssignType : 'distribution model~',
		labelSuitableCounty : 'Application areas~'

	},
	QueryDevice : {// device queries
		_title : 'device queries~'
	},
	DeviceDetailInfo : {// Detailed Search
		_title : 'detailed inquiry~',
		titleUseRecord : 'using the recording~',
		labelTargetDepot : 'target warehouse~',
		labelSourceDepot : 'source repository~'
	},
	ReclaimDevice : {// Equipment Recycling
		_title : 'Equipment Recycling~',
		labelConfirmRecycle : 'whether recovery~',
		titleRecycleConfirm : 'Recycling confirm~',
		labelRecycleStatus : 'execution or historical recovery~',
		recycleStatusEnum : {
			ALL : 'All recovered~',
			NOW : 'recycle~',
			HISTORY : 'History recycling~'
		},
		labelRecycleReason : 'Recycling reason~'
	},
	DeviceLoss : {// equipment loss report
		_title : 'equipment to report the loss~',
		titleAddDevLoss : 'Adding devices to report the loss~',
		labelCancelLoss : 'loss cancellation~'
	},
	DownloadDeviceInfo : {// Equipment Inventory
		_title : 'equipment inventory~'
	},
	msgBox : {
		invoiceNotExists : '{0} receipt does not exist~',
		selectInvoiceCode : 'Please select a receipt Code~',
		emptyTextSelectStore : 'Please select the warehouse~',
		invoiceIdLengthNotEquals : "inconsistent start receipt number and receipt number ends length~",
		needCorrectInvoiceId : 'Please enter the correct receipt number~!',
		needInputAtLeastOneInvoice : 'Please enter a receipt number at least~',
		linkFailure : 'connection exception query failed~!',
		invoiceNotFoundTryReSearch : 'No query to the receipt information, please re-enter the receipt number for your inquiry!~',
		selectTransDepotId : 'Please select redeployment warehouse~',
		selectStatus2BeModify : 'Please select the state you want to modify~',
		confirmSaveBusiWithInvoice : '{0} sure you want to save the business you~?',
		selectARecord : 'Please select the rows to be operated!~',
		supportOrderFuzzyQuery : 'Support Order No. fuzzy query~',
		confirmConvert2HisOrder : 'Confirm Conversion historical order?~',
		confirmConvert2NowOrder : 'confirm the conversion for the implementation of the order?~',
		orderNumCantLessThanGoodsNum : 'The quantity should not be less than the number of arrival~!',
		confirmDelete : 'OK to delete it~?',
		tipSelectOneRow : 'Please selected to operate the line~',
		confirmDoAction : 'determination operation it~?',
		selectDevType : 'Please select the device type~!',
		selectModel : 'Select Model~!',
		numberShouldBiggerThan0 : 'Price Please enter a number greater than zero~!',
		orderNumShouldBigThan0 : 'Order Number Please enter number greater than zero~!',
		confirmOverideSaveDevModel : 'There are different models of equipment, whether to save~',
		errCantAssignParent4DevNo : 'Please configure the device number cell parent attribute points to form~',
		pleaseSelectSaveTypeAndModel : 'Please select the device type and device model~',
		deviceCodeExists : 'device number already exists~',
		supportDevCodeOrderNoFuzzyQuery : 'supports the storage number and order number fuzzy query~',
		tipCheckInFileInput : "File storage: [STB]: the first column: set-top box models, the second column: STB serial number, the third column: pairing smart card number (which can be empty); fourth column: Pairing MODEM number (which can be empty), [ Smart Card]: the first column: Device model, the second column: Device ID; [modem]: the first column: modem model, the second column: mac address, the third column: modem number; the last one as a lot~",
		fileUploadSuccess : 'file uploaded successfully!~',
		fileUploadFailure : "File upload failed!~",
		pleaseInputCorrectDevInfo : 'Please enter the device information is correct!~',
		transSuccess : 'redeployment confirm successful~',
		pleaseInputBatchNum : 'Please enter the lot number inquiry~',
		tipDevCheckFileFormat : 'File storage: Only one device numbers~',
		confirm2HisTrans : 'Confirm Conversion history allocate it~?',
		confirm2NowTrans : 'confirm the conversion for the implementation of the redeployment of it~?',
		warnHasSaveCode : 'There are the same number, please check!~',
		notSuitableDev4Trans : 'this batch of equipment has been allocated to the allocation does not meet the conditions of equipment~!',
		pleaseSelectDev2Trans : 'Please select redeployment of equipment~',
		tipOutOfStock : 'can not be greater than the number of stock!~',
		tipHasSameMateral : 'have the same equipment, please check!~',
		tipTransNumCantBeEmpty : 'allocation amount can not be empty!~',
		tipOutputNumCantBeEmpty : 'refunding the amount can not be empty~',
		pleaseUploadExcelFile : 'Please select a file to upload excel!~',
		pleaseUploadExcel2003 : 'Please select excel2003 file upload, file suffix .xls~!',
		needDevCode2Query : 'Enter the device number Enter inquiry~',
		confirmCancelApply : "OK Cancel requisitioned it?~",
		confirmCancelAllApply : "OK Cancel All recipients do?~",
		supportDevCodeQuery : 'Support device number inquiry~',
		tipDevHasNoDiff : 'The equipment has been different, no longer need to confirm~',
		tipDevNotDiffCantCancel : 'The device is not different, not cancel confirmed~',
		tipDevFileDiffInfo : 'file increases the difference between: the file type is Excel, a total of one, first as a difference device numbers~',
		tipConfirmDiffSuccess : 'confirm the differences success~!',
		tipCalcenDiffSuccess : 'Cancel differences success~!',
		tipSelectTragetDevStatus : 'Please select the device you want to modify the status type~!',
		fileDownloadFailed : 'File download failed~',
		confirmCancelRecycle : 'Cancel recovered it?~',
		tipDevDosNotExists : 'query device does not exist~',
		tipQueryDateBeforeDownLoad : 'Please check out the data, in order to download !!~',
		tipPleaseEditWell : 'Please edit complete~',
		duplicateRows : "The first {0} and {1} of the same row, please re-edit!"

	}
}