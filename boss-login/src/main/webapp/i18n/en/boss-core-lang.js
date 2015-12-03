/** boss-core Simplified Chinese Package */
BCLang = {}
BCLang.common = {
	optr: "Operation",
	confirm: 'Confirm',
	remove: 'Remove',
	switchor: 'Select',
	pswd: 'Password',
	newPswd: 'NewPassword',
	confirmPswd: 'ConfirmPassword',
	remark: 'Remark',
	remark2: 'RemarkInfo',
	busiInfo: 'BusinessInfo',
	busiSave: 'BusinessSaved',
	tipBusiSaveOK: ' Business Saved Successfully!',
	tipLoadText: 'Being Inquiring, Please Wait...',
	tipConfirmSave: 'Are you sure to save business?',
	confirmSaveData: 'Are you sure you want to save the information?',
	tipFormInvalid: 'Containing input items cannot pass examination!',
	filterTreePanel:{
		emptyTipSearchField:'Input Name Filtration...',
		btnExpandAll:'spread all resources',
		btnCollapseAll:'merge all resources'
	},
	filterTitle: 'condition filtration',
	tipExistsSystem:'Are you sure to exist',
	queryBtn:'Inquiry',
	queryBtnWithBackSpace:' Inquiry',
	total: 'Total',
	subTotal: 'Sub-total',
	price: 'UnitPrice',
	count: 'Quantity',
	pay: 'Pay',
	fee: {
		columns: ["Fee Item","Household Quantity","Unit Price","Money Amount"],
		tbar0: "service fee"
	},
	plsSwitch: 'PleaseSelect',
	taskTitle: 'WorkOrder',
	assignWay: 'OrderArrangementMethod',
	removeSelected: 'MoveTheSelected',
	expandOrCollpse: 'Expansion or contraction',
	optional: 'Options',
	selected: 'Selected',
	optionalGroup: ' {0} options',
	selectedGroup: " {0} selected",
	save: 'Save',
	close: 'Close',
	busido: 'BusinessReceived',
	defaultGroupTpl: '{0} selected',
	uploadFileLabel: 'Document uploading',
	busiWay: 'Dealing Way',
	tipSimple: 'Tip',
    returnTxt:'Return',
	tip: 'TipInfo',
	totalRecord: ' ( Total: {0}) ',
	submit: 'Submit',
	emptyMsg: 'No Data',
	pageDisplayMsg: 'No.{0} –No. {1}, totally 2',
	alertTitle: 'Tip',
	submitingText: 'Being Submitting Data...',
	loadingText: 'Being Inquiring, please wait...',
	yes: 'YES',
	print: 'Print',
	cancel: 'Cancel',
	no: 'NO',
	update: 'Modify',
	forbiddenBtn: 'Disabled',
	enableBtn: 'Enable',
	cancelBtn: 'Cancel',
	doActionBtn: 'Manipulation',
	add: 'Add',
	correct: 'Correct',
	untreated: 'Untreated',
	wrong: 'Wrong',
	invoicePrint:'Invoice Print'
}
//home page module
BCLang.home = {
	topWelcome: "OperatorAtPresent",
	searchTabs:["NameNo.","Device|Account","InstallAddress","Phone","Multi-Condition"],
	invoicePrintTitle:'InvoicePrint',
	searchTip:"CustomerNo.|ReceptionNo.",
	searchBtns:["Search","Pay"],
	main: {
		tabs: ["Customer Information","Unit Information", "User Information", "Account Information", "Payment Record", "Reception Information", "Service Serial Order","Instruction Information", "Bill Information"],
		//cash payment page
		cashPay: {
			_title: 'Pay',
			pay: {
				_stayTitle: 'Items to be paid',
				_title: 'Payment',
				columns: ['Operate', 'Service Name', 'Fee Name', 'Paid Money Amount', 'Order No.', 'Remark', 'Quantity', 'Operate Time', 'Fee No.', 'Serial Number'],
				buttons: ['Save', 'Close']
			},
			charge:{
				_title: 'Charging Information',
				columns: ['Total USD', 'Current Exchange Rate', ' KHR', 'payer', 'Payment Way', 'Reception No.', 'Account Date', 'Received USD', ' Received KHR']
			}
		},
		// customer page
		cust: {
			base: {_title: "Basic Information",name: "CustName",busiId: "CustNo.",openDate: "Open Account Date",addr: "Address",status: "CustStatus",
				type: "CustType",certType: "ID Category",certNum: "IDNo.",linkMan: "Contact",tel: "Mobile Phone1",barthday: "Birthday",
				mobile: "Mobile Phone2",areaCateory: "Area Community",houseNetType: "ServiceType",houseManager: "Community Customer Manager",
				houseOptr: "Community Maintenance Staff",postalAddr: "PostAddress", remark: "Remark",
				email: "Email", sex: "Sex",postcode: 'Postcode', blackList: 'BlackList',
				agentName: "Agent", deptName: "Open Account Department", developName: "Developer", businessLicence: "Business License", unitNumber: "Unit Taxation Number",
				spkgSn: "AgreementNo.", cust_level: "CustLevel",switchCustTitle: 'Select Customer', languageType: 'Service Language', unitName: 'Subordinate unit'
			},
			_form: {
				thinkCust: 'Intending Customer',
				switchProvince: 'Please select province',
				oldCustLevel: 'Former Customer Level',
				newCustLevel: 'New Customer Level',
				deviceType: 'Device Type',
				deviceModel: ' Device Model',
				storeCount: 'Storage Quantity',
				buyWay: 'Purchase Way',
				buyCount: ' Purchased Quantity',
				titleBatchBuyMaterial: 'Material Information',
				titleAcctRefund: 'Refunding',
				refundTotal: 'Refunding Money Amount',
				rechargeWay: 'Recharging Method',
				byTicket: 'On Reception',
				rechargeCount: 'Recharging Amount',
				province:'Province',
				provinceEmptyText:'Please select Province',
				addrManager: 'Address Management',
				roomNumber: 'Room No.',
				roomStatus: 'Room Status',
				roomTitle: 'Room Information',
				roomTitle2: 'Administrative area:{0} service type:{1}',
				addRoom: 'increased room',
				unitName:'unit name',
				unitAddress:'unit address',
				accountName:'Account Name'
			},
			acct: {
				_title: "Account Information",
				columns: ["Account Name","Card No","Balance","Due Fee of Past Months", "Due Fee of this month","Real Time Fee","Real Time Balance","transferable Balance","Refundable Balance","Freeze Balance"]
			},
			change:{
				_title: 'Abnormal Information',
				columns: ["Feature","Before Change","After Change","Change Date"]
			},
			acctTabs: {
				detail: {
					_title: 'Details',
					columns: ["Fund Type","Balance"]
				},
				adjust: {
					_title: 'Account Adjustment',
					columns: ["Account Adjustment Money Amount","Operate Time","Account Adjustment Reason","Remark"]
				},
				changes: {
					_title: 'Abnormal',
					columns: ["Service Name","Fund Type","Change Type","Before Change","Change Money Amount","After Chang","Remark","Operate Time"]
				}
			}
		},
		// Customer Page
		user: {
			base:{
				type: 'TerminalType', name: 'TerminalName', status: 'Status', statusTime: 'StatusTime',
				stbId: 'StbNo.', cardId: 'CARDNo.', modem: 'MODEM', createTime: 'CreateTime',
				loginName: 'Account', terminal: 'TerminalType', deviceModel: 'DeviceType', buyWay: 'BuyWay',
				stopDate: 'ForecastStopTime', stopType: 'ReminderFeeType', protocolDate: 'AgreementDate', 
				str4: 'IPAdress',str6: 'IPFeeAmount',password:'Password'
			},
			_form: {
				taskBackFill: ' backfilling construction',
				deviceCode: 'DeviceCode',
				feeName: ' FeeName',
				feeAmount: 'FeeMoneyAmount $',
				protocolInfo: 'Agreement Information',
				openAmount: 'AccountNo.',
				manualOpen: 'Manual Open Account',
				addToOpenUserGrid: 'Add to Temporary<br/> Saving Table',
				titleOpenUserGrid: 'User Temporary Storage',
				titleSwitchProd: 'Step One：select Product',
				prodName: 'ProductName',
				prodDesc: 'ProductDesc',
				titleDetemineUser: ' Step Two: Sure for Purchasing User',
				switchUsers: 'Select Customer',
				titleOrderInfo: ' Step Three: Purchase Information',
				prodTariff: 'ProductFee',
				prodOrderType: 'OrderMethod',
				prodOrderMonths: 'Purchase Month Quantity',
				prodStartDate: 'Billing Starting Date',
				prodExpDate: ' Billing Ending Date',
				lastOrderExpDate: 'Ending Date of Last Purchase',
				titleOrderFee: 'ProductFee',
				shouldPay: 'Receivable',
				addOrderFee: 'New Increased Purchase',
				transferPay: 'Transfer Payment',
				maxUserCount: 'Max User Quantity',
				titleDispatchUser: 'Distribute User',
				terminalInfo: 'Terminal Information',
				addToSelected: 'AddedToSelected',
				moveToOptional: 'MoveToSelectable',
				stopTime: ' Stop Report Time',
				stopFee: 'Termination Report Fee',
				prodFeeCM: ["Product Name","Former Fee","New Fee","Former Expiry Date","Billing Date",
				          "Payment Month Quantity","New Expiry Date","Transfer Payment Money Amount","Payment Money Amount"],
				prodTitle: 'Product Information',
				prodGroupText: 'User Name:{0}  User Type:{1}',
				templateDown: ' Template Download',
				templateDownTip: 'Please do not operate when service is busy; Please do not cancel the first line of template;',
				loginId: 'Login',
				orderFee: 'Order Balance',
				canRetrunFee: 'refundable Money Amount',
				totalReturnFee: 'Total Refund',
				canTransferFee: 'transferable Money Amount',
				canRefundRealFee: 'Real refund amount',
				returnDevice: 'Recycle Device',
				retrunInfo: 'Refundable',
				transferInfo: 'transferable',
				stdId: 'StbNo.',
				stdModel: ' Stb Type',
				newStdId: 'New StbNo.',
				newStdModel: 'New Stb Type',
				modemId: 'Modem No.',
				modemModel: 'Modem Type',
				newModemId: 'New Modem No.',
				newModemModel: 'New Modem Type',
				changeCause: 'Change Reason',
				userName: 'User Name',
				chargeNum: 'Charging Quantity',
				desc: 'Description',
				feeItem: 'Fee Item',
				unitPrice: 'Unit Price',
				ipFeeDesc: 'IP Fee= Unit Price * Billed Quantity*Month Quantity（or days/30）',
				expDate: 'Expiry Date',
				payMonth: 'Month Quantity to Collect',
				totalPrice: 'Total Price',
				timeSlot: 'Period',
				titleBusiFeeGrid: 'Other Fee',
				ipFee: 'IP fee',
				
				titleTransferPayDetailGrid: 'Transfer PaymentDetails',
				transferPayCM: ['Product Name', 'Fee', 'User', ' Billing Starting Date', 'Ending Billing Date', 'Transfer Money Amount$'],
				transferDetailDate: ' Transferring Details（Billing Starting Date：{0} "）',
				labelChangeDeviceResion: 'The Replacement Need',
				labelChangeBuyDevice: ' Income from the purchase cost',
				labelChangeReclaimDevice: ' Recycling Device',
				labelChangeLossDevice: ' Report the loss of equipment',
				
				oldOrderFee: 'Original Order Amount',
				oldTransFee: 'Original Transfer Payments',
				realOrderFee: 'New Orders Amount',
				newAddFee: 'Up close to the amount of'
			},
			list: {
				_title: 'Terminal Information',
				tools: ["Inquiry"]
			},
			prod: {
				base: {
					_title: "Customer Product",
					columns: ["OrderSn","ProductName","Belonged Package","CurrentCharges","EffectiveDate" ,"ExpirationDate","Status","StatusChangeTime","OrderTime" ,"OrderMonths","SerialNo.","WhetherToPay"]
				},
				pkg: {
					_title: 'Customer Package',
					columns: ["OrderSn","ProductName","CurrentCharges","Status","EffectiveDate" ,"ExpirationDate","ProductType","OrderTime"]
				},
				tools: ["Default Order","History Oder"]
			},
			userDetail: {
				tabs: ["Details","Change Information"],
				detail: ["UserType","UserName","DeviceType","BuyWay","Status","StatusTime","ForecastStopTime","CreateTime","ReminderFeeType","InNetworkProtocolPeriod","Account",
				         "TerminalType","Account","Terminal Type","IPInfo","IPFeeAmount"],
				change: ["ServiceName","Feature","ModifyAgo","Modified","ChangeDate","Operator"]
			},
			prodDetail: {
				tabs: ["Order Amount Details","Change Information"],
				detail: ["No.","FundsType","ImportProduct","ImportType","ImportMoneyAmount","ExportProduct","ExportType","ExportMoneyAmount","SerialNo." ]
			}
		},
		//Payment Records
		pay: {
			payfee: {
				_title: "Prepaid Money",
				columns: ["SerialNo.","ServiceName","AccountTypes","AccountName","UserType", "UserName","DeviceNo.","Status","Money","Expire Date Before",
				        "Expire Date After","PrintStatus","PaymentWay","ProcessingDate","PaymentDates","Receptionist","ReceptionDept","Invoice","Invoice Issuance Way", "InvoiceType", "Developer"]
			},
			busifee: {
				_title: "Service Fee",
				columns: ["SerialNo.","FeeName","DeviceType","DeviceNo.","Status","PrintStatus",
				          "Payable","Paid","PaymentWay","ReceptionDate","Receptionist","ReceptionDept",
				          "Invoice"," Invoice Issuance Way","InvoiceType","BuyQuantity","Device Model","Remark", "Developer", "Payment Dates"]
			},
			detail: {
				_title: 'Payment Records',
				columns: ["PaymentNo.","USD","KHR","Exchange","KHR Round-off","Effective","PaymentWay",
				          "Payer","SerialNo.","ReceptionNo.","ReceptionIssuanceWay","ReceptionDate","Receptionist","ReceptionDept"]
			},
			feePayDetail: {
				_title: 'Fee Details',
				columns: ['serial number','feeitem', 'amount','invoice']
			},
			_form: {
					oldOptrName: 'Old Development',
					newOptrName: 'New development'
				}
		},
		// Reception Information
		doc: {
			invoice: {
				_title: 'Invoice',
				columns: ["InvoiceNo.","InvoiceCode","Money","PrintTime"," ReceptionIssuanceWay","InvoiceType","UsageStatus","BalanceStatus","Operator","GeneratTime"]
			},
			task: {
				_title: 'Work Order',
				columns: ["WorkOrdersNo.", "WorkOrdersType", "WorkOrderStatus", "ConstructionTeam","FaultCause", "FaultDetails", "ZTEAuthorizedState", "CreatingTime", "ReportFaultPhone"],
				userColumns: ['UserType', 'UserName', 'DeviceModel', 'DeviceNumber', 'PosNo', 'OccNo ', 'BandWidth'],
				detailColumns: ['OperatTime','OperatType', 'Operator','SyncStatus', 'ErrorDesc'],
				winTitle: 'WorkOrders Info',
				userTitle: 'User Info',
				opertaionTitle: 'Operation Info'
			},
			busi: {
				_title: 'Service Reception Order',
				columns: ["Operator"," FinalPrint","ServiceName"]
			},
			_form: {
				oldInvoiceId: 'FormerInvoiceNo.',
				oldInvoiceCode: ' FormerInvoiceCode',
				oldInvoiceType: ' FormerInvoiceType',
				oldStatus: ' FormerInvoiceStatus',
				newInvoiceId: 'NewInvoiceNo.',
				newInvoiceCode: 'NewInvoiceCode',
				faultContent:'Fault content',
				faultPhone: 'Report Fault Phone'
			}
		},
		//Reception Record
		doneCode: {
			_title: ' Reception Record',
			columns: ["Serial Number","Service Name","Status","Reception Date","Operator","ReceptionDept",
			         "Returnable","Ignorable","Paid Amount","Extended ServiceInfo","Remark"],
			editPayColumns:["Fee Item","Unit Price","Amount","Accumulated Cost","Actual Cost","The Cost","Type Name"],
			_form:{
				oldRemark: 'Old Remark',
				newRemark: 'New Remark'
			}
		},
		//instruction
		cmd: {
			dtt: {
				_title: "DTT Instruction Information",
				columns: ['SerialNo.','StbNo.','CardNo.','ControlWord','ProgramName',
				       'InstructionType','ResultMark','CreateTime','SendTime','CARecycleTime','WrongInfo','AuthEndDate']
			},
			ott: {
				_title: "OTT Instruction Information",
				columns: ['InstructionNo.','SerialNo.','InstructionType','StbNo.','CardNo.','MAC','SuccessOrNot','WrongInfo','SendTime']
			},
			band: {
				_title: 'BAND Instruction Information',
				columns: ['InstructionNo.','SerialNo.','InstructionType','StbNo.','ModemNo.','SuccessOrNot','WrongInfo','SendTime']
			}
		},
		// Bill
		bill: {
			list: {
				_title: 'Bill Information',
				columns: ['Billing Period','Source','Smart Card No.','Serial','Charge-off Time','Account Name',
				      'Fee Name','Status',' Charge-off Money Amount','Due Money Amount','Operate'],
				tbar: ["Smart Card","Serial Number","Due Bill","All Bill"]
			},
			acctitemInvalid: {
				_title: 'Account Invalid Information',
				columns: ["Account Name","Fund Type","Invalid Money Amount"]
			}
			
		}
	}, // main end line...
	// Homepage Tool  Column Service
	tools: {
		countySwitch:{//Branch Switch			titleSelectDept:'Select Department',
			confirmSwitchDept:'Sure to switch Department'
		},
		ad: {//Declaration
			_title: ' Declaration Information',
			tplPublishTme:'Published on',
			columns: [' Declaration Subject','Publisher','Effective Time','Invalid Time']
		},
		grxg:{//Personal Amendment
			_title:' Personal Information Amendment',
			labelNewPwd:'new code',
            labelNewPwdConfirm:'confirm new code',
            labelDefaultSystem:'Default Login System',
            msg: {
            	msgConfirmSave: 'You sure you want to save it?',
            	msgSuccess: 'Successfully modified!',
            	msgFail: 'Modification fails!'
            }
		},
		queryDevice:{//Device Inquiry
			_title:' Device Inquiry',
			titleDevInfo:' Device Information',
			labelDevNo:' Device Serial Number',
			labelDevType:'Device Type',
			labelDevModel:'Device Type',
			labelModelName:'Type Name',
			labelDevName:'Device Name',
			labelDevCode:'Device No.',
			labelDeptName:'Storage Warehouse',
			labelCustNo:'Customer No.',
			labelCustName:'Customer Name',
			labelDevStatus:'Device Status',
			labelDepStatus:'Storage Status',
			labelTranStatus:'Moving Status',
			labelOwnership:'Ownership',
			
			labelCardNo:'Matching Card No.',
            labelCardModel:'Martching CardType',
            labelModemNo:' Matching MODEM No.',
            labelModemModel:' Matching MODEM Type',
            labelStbNo:' Matching StbNo.',
            labelStbModel:' Matching Stb Type',
            tipDevNotExists:'Inquiry Device does not exist!'
			
		},
		invoiceQuery:{//Invoice Inquiry
			_title:'Invoice Inquiry',
			titleInvoiceInfo:'Invoice Information',
			btnShowInvoiceDetail:'Fee Details',
			btnChangeStatus:'Change Status',
			btnChangeStatusIdel:'change to unused',
			btnChangeStatusInvalid:'change to invalid',
			btnChangeStatusUsed:'Used',
			
			confirmChangeStatus:'Whether Invoice Status is changed to',
			tipInvoiceNotExists:' Invoice does not exist!',
			tipInvoiceNotExists2:'Inquiry Invoice does not exist!',
			statusInvalid:'invalid',
			statusIdel:'unused',
			
			cols:['Customer Name', ' Customer No.', 'Service Name', 'Fee Name', 'Actual Amount', 'Operation Time', 'Operator'],
			
			labelInvoiceId:'Invoice No.',
			labelInvoiceId2:'Inv&nbsp;oice&nbsp;No.&nbsp;',
			labelInvoiceCode:'Invoice Code',
			labelInvoiceType:'Invoice Type',
			labelDeptName:'Storage Warehouse',
			labelUseStatus:'Usage Status',
			labelMoneyAmount2:'Amount',
			labelOptrName:'Taker',
			labelOpenOptrName: 'Operator',
			labelCreateTime:'Storage Time',
			labelFinanceStatus:'Balance Status',
			labelCloseTime:'Write-off Time',
			labelCheckDeptName:'Settlement Warehouse',
			labelCheckTime:'Settlement Time'
			
		},
		CustSearch:{//Customer Inquiry(multi-condition inquiry)
			_title:' Customer Inquiry',
			tipInputAnyField:'Please input any item to search!',
			labelCustName:'Customer Name',
			labelStatus:'Intending Customer',
			labelAddress:'Customer Address',
			labelLoginName:'Account'
		},
		InvoicePrint:{//Invoice Print
			_title:'Invoice Print',
			titleDocGrid:'Print List',
			titleInvoiceGrid:'Invoice Item',
			titlePrintDetail:'Print Details',
			titlePrintPreview:'Print Preview',
			titleInvoiceWindow:'Print {0} Invoices totally',
			wdxj:'Shop Cash',
			printGridColumns:['Serial Number','Reception Name','Create Time'],
			invoiceGridColumns:['SerialNo.','InvoiceNo.','Drawer'],
			printItemGridColumns:['Name','Money Amount'],
			stillEmptyInvoiceField:"Still empty invoice input frame!",
			hasDuplcateInvoice:"There is repeated Invoice，Please confirm!"
		},
		AddressNodeManage : {
			panelTitle : 'Address Management',
			msg : {
				actionFailed : 'Operation failed',
				actionSuccess : 'Operation Successful',
				confirmSave : 'Confirm save?',
				confirmInvalid : 'sure you want to disable?',
				confirmDelete : 'you sure you want to delete?',
				confirmActivate : 'confirmation to activate?',
				cantDelete : 'There are customers in the use, temporarily can not be deleted.',
				cantBeInvalided : 'the presence of sub-level, can not be disabled!',
				maxDepthText : 'can not be down to add',
				noSingleQuoteAllowed : 'Do not enter single quotation marks'

			},
			formWin : {
				labelSortNum : 'Sort value',
				labelProvince : 'Province',
				labelDistrict : 'administrative area',
				labelNetType : 'Network type',
				emptyTxtProvince : 'Please select the province',
				emptyTxtBlurQuery : 'support fuzzy queries',
				btnTxtClose : 'close',
				btnTxtSave : 'save',
				labelRoadNum : 'House No.',
				titleNewSaveLevelRoadNum : 'New House No.',
				titleEditSaveLevelRoadNum:'Edit Road',
				labelStreatName : 'street name',
				titleNewSaveLevelStreet : 'New Street',
				titleEditSaveLevelStreet:'Edit Street',
				labelCityName : 'city name',
				titleNewSaveLevelCity : 'new city',
				titleEditSaveLevelCity : 'Edit city',
				labelShowName : 'show name',
				labelParentName : 'SuperiorsName',
				labelBrotherName:'brother name',
				labelOldName:'old name',
				
				labelNewAddChild : 'new subordinate',
				labelNewAddBrother : 'new flat level',
				labelAddrTree : 'address tree'
			}
		},
		TaskManager : {
			_title : 'Work Order Management',
			_operateTitle : 'Operation Details',
			_userTitle : 'User Details',
			_winTitle : 'Work Order Completion',
			_ZteWinTitle : 'ZTE Authorized',
			_fillDevTitle: 'BackfillDevice',
			_historyTitle : 'History WorkOrder',
			_custSingNoTitle: 'Modify customer signature single No.',
			forms : {
				custNo : 'CustomerNo.',
				taskNo : 'WorkOrdersNo.',
				custName : 'CustomerName',
				mobile : 'Tel',
				newaddr : 'Address',
				taskStatus : 'WorkOrdersStatus',
				taskDetailType : 'WorkOrdersType',
				taskAddr : 'Area',
				taskTeam : 'ConstructionTeam',
				taskOptr : 'WorkOrderOperator',
				faultType : 'FaultType',
				finishType : 'CompletionType',
				finishExplan : 'CompletedDesc',
				custSignNo : 'CustomerSignatureNo.',
				zteStatus	: 'ZTEStatus',
				zteRemark :'Remark',
				syncStatus:'CfocnSyncStatus',
				oldCustSignNo : 'Original SignatureNo.',
				newCustSignNo : 'New SignatureNo.'
			},
			buttons : {
				query : 'Query',
				pendingOrder : 'Pending WorkOrders',
				accptTime : 'Hours:',
				distTeam : 'Order Arrangement',
				invalidTeam : 'WorkOrders Invalid',
				backDevice : 'Backfill Device',
				finish : 'Completed',
				returning : 'Visit',
				sendAuth : 'ZTE Authorized',
				withdraw : 'CFOCN Withdraw',
				modifyCustSignNo: 'Modify SignatureNo.'
			},
			taskCols : ['WorkOrdersType', 'CustName', 'WorkOrdersStatus', 'ConstructionTeam', 'ZTEStatus', 'Address', 'Tel', 'CreateTime', 
				'FaultType', 'FaultDetails','WorkNo.','CustManager', 'CustManagerTel','CustNo.','CfocnSyncStatus','OldAddress','NewAddress',
				'ReportFaultPhone', 'FinishDescription', 'WorkOrderOperator', 'CustomerSignNo.','StatusTime','WorkOrderOperatorTel','FinishType','TaskDeviceCnt','TaskFinishTime'],
			userCols : ['UserType', 'Account number', 'Password', 'DeviceModel', 'DeviceNO.', 'PosNo.', 'OccNo.', 'BandWidth','Band password','Status','StatusTime','ProdExpDate'],
			taskDeviceCols : ['UserType', 'UserName', 'DeviceModel', 'DeviceNumber','DeviceRecycling','OldDeviceNumber'],
			operateCols : ['OperatingTime', 'OperationType', 'Operator', 'SyncStatus', 'Description','DelayTime'],
			samTaskCols : ['WorkNo.', 'WorkOrdersStatus', 'ConstructionTeam', 'CreateTime', 'FaultType', 'FaultDetails', 'SyncStatus','FinishDescription','WorkOrderOperatorTel','FinishTime','FinishType'],
			msg : {
				enterDeviceNo : 'Please enter the device number',
				noCancel : 'No cancellations!',
				sureWantSelectedWork : 'sure you want to void the selected work order you?',
				sureWantWithdrawSelectedWork : 'sure you want to withdraw the selected work order you?',
				teamCantEmpty : 'construction team can not be empty',
				faultTypeCantEmpty : 'fault type can not be empty',
				finishTypeCantEmpty : 'completion type can not be empty',
				ZteStatusCantEmpty : 'zte status can not be empty',
				selectRecord : 'Please select the records need to operate!',
				roderHaveBeenCompletedOrObsolete : 'WorkOrders have been completed or obsolete',
				teamIsnotSingleWork : 'This work is not a single construction team of the department',
				taskStatusInitAndSupernet:'Work order status must be During Construction and construction team must be supernet',
				taskStatusInitAndCfocnCanWithdraw:'Work order status must be During Construction and construction team must be cfocn',
				taskStatusInitAndSupernetCanAssignment:'Work order status is Waiting or During Construction and construction team is supernet can assignment',
				zteStatusCanSend:'status is failure  or NOT_EXEC  can be operate zte',
				endWaitCanNotUse:'completed wait Can only be used for Order completed or Order Arrangement',
				notEndCanNotModify: 'WorkOrder completion to modify customer signature single number'
			}
		},
		openOTT: {
			custNamePrefix: 'CustomerNamePrefix',
			num: 'Quantity',
			dowlnloadSuccess: 'Download Successful'
		},
		feeUnitpre : {
			_title : 'non-business TOLL information',
			cols : ['contract type', 'contract number', 'contract name', 'customer name', 'contract amount', 'invoice number', 'operator'],
			forms : {
				cust_name : 'Customer Name',
				contract_no : 'Contract No.',
				nominal_amount : 'contract amount',
				fee_name : 'expense name',
				contract_name : 'Contract name',
				addr_district : 'to your city',
				lblFee : 'total USD',
				LabelJian : 'Cambodia KHR',
				nfDollar : 'Paid USD',
				nfJianYuan : 'Paid KHR',
				labelExchange : 'the date of the exchange rate',
				pay_type : 'payment method',
				receipt_id : 'bill number',
				invoice_id : 'receipt number'
			},
			msg : {
				enterKeywords : 'keywords',
				receiptCantUsed : 'The receipt can not be used',
				custNameOrContractNoQuery : 'supports the customer name or contract number fuzzy search'
			}
		},
		osdSend:{
			_title : 'OSD calls',
			forms : {
				begin_date : 'Start time',
				end_date : 'End time',
				detail_time : 'Point of time',
				osd_call_type : 'Send type',
				send_title : 'Theme',
				template : 'Template',
				send_optr : 'Sender',
				file : 'Data file',
				message : 'message',
				sendOSD:'Send calls',
				cancleOSD:'Cancle calls'
			},
			msg : {
				actionFailed:'Send OSD failed',
				actionSuccess:'Send OSD success',
				detail_time_info : 'At the time of the time, it can be more than a comma separated, such as 9:00,12:00',
				dataMustTxt : 'Please select the txt file to upload, file suffix named.Txt!',
				confirmSendOsd:'A total of [{0}] data,[{1}] time points to send osd'
			}
		}
	}
	
}
//Various Pop-up Messages
BCLang.msgBox = {
	payInfo: 'You have<b>{0}</b> bills to pay,totally<b>{1}</b>$',
	payButton: 'Cash Payment',
	selectInvoice2Print:"Please select invoice to print!",
	selectInvicePrintItem:"Please select Invoice Item to Print",
	templateReplaceError:"Errors in template variable substitution! error:{0}",
	printCmpError:'Print Controls abnormal，Please check {0} Print Controls installed or not {0}',
	invoiceIdNeeded:'PleaseinputInvoice No.',
	confirmSaveInvoiceInfo:"Sure to save Invoice Information?",
	waitMsg:'Operating, please wait',
	needCust:'Please find Customer to Operate at first!',
	needUser:'Please select customer!',
	cancelFeeSuccess:'Successful Write-off!',
	paymentSuccess:'Payment Success!',
	confirmCancelFeeAndInvaidInvoice:'Invoice{0} will be invalid! Fee Items of this Invoice need to be printed again，Sure to cancel it?',
	confirmCancelFee:'Sure to cancel it?',
	selectRec4CancelFee:'Please select fee record to cancel!',
	confirmUnPayWithParam:'Sure to return to【Money Amount：{0} 】?',
	confirmRestoreCust:'Sure to restore customer?',
	restoreCustSuccess:'restore customer successfully!',
	restoreCustFailed:'fail to restore customer, Please contact administrator.',
	needLogOffUser:'Please cancel user under this Customer at first',
	recycleGdDevice:'Please recycle Device whose ownership is SuperNet',
	custIsRelocated:'Customer has moved',
	confirmRelocateCust:'Are you sure to move customer?',
	notAllowedJoinUnit:'The customer is unit customer, cannot join unit!',
	confirmQuitUnit:' Are you sure to exist Unit?',
	confirmBankStop:'Sure to suspend fee deduction of card?',
	confirmEnableBankPay:'Start fee deduction of bank card?',
	confirmDisableBankPay:'Forbid fee deduction of bank card?',
	confirmEditBankPay:'Confirm the Product',
	confirmBankResume:'Sure to restore fee deduction from card?',
	confirmRenewCust:'Restore Customer Status?',
	confirmCancelPayFeeWithParam:'Determined to pay rollback and void an invoice:{0}',
	confirmCancelPayFeeWithNoParam:'Determined to pay rollback?',
	cancelPayFeeSuccess:'Payment rollback Reversal Success!',
	commonSuccess: 'Operation success!',
	
	custHasUnSuitableDev:'This User cannot do Device exchange now',
	custCantExchangeDev:' This User cannot do Device exchange now',
	
	confirmRegLoss:'Are you sure to loss report?',
	regLossSuccess:'Successful loss report!',
	
	selectDev2RegLoss:'Please select to cancel lost report equipment',
	selectDevIsRegLossAlready:'Please select lost report equipment',
	unRegLossSuccess:'successful cancellation of loss report!',
	confirmUnRegLoss:'Sure to cancel lost report?',
	//Open and Close Print Mark
	statusNotPrintStatusSuccess:'Mark No-Print successfully!',
	confirmNotPrintStatus:'Sure for not Print?',
	statusPrintStatusSuccess:'open mark successfully!',
	confirmPrintStatus:'Sure to open Print Mark?',
	//Device Sales
	selectDev2Sale:'Please select Device on sales!',
	cantSaleCosOwerIsCust:'Device ownership belongs to Customer, cannot be sold',
	//Change Purchase Way
	modifyBuyType:'Change Purchase Way',
	need2SwitchCountyId:'current shop is not the shop where the Device is bought，Please switch to [{0}] and then operate！',
	//Change Ownership
	changeOwner2Cust:'Sure to change ownership to individual？',
	changeOwner2Gd:'Sure to change ownership to SuperNet？',
	cangeOwnerSuccess:'change ownership successfully!',
	//Device recycling
	selectDev2Recycle:'Please select recycled Device!',
	devCantRecycleStillInUse:"Device is in using, cannot be recycled",
	onlyResidentCanNonResiCust:'Only Resident Customer can be turned to Group Customer',
	depositUnPaySuccess:'Deposit refunded successfully!',
	unPayed:'unpaid！',
	notDeposit:'This Fee is not deposit！',
	confirmDepositUnPay:"Sure to return deposit?",
	
	userNotActive:"selected User Status is abnormal",
	userStatusActiveOrConstruction:"selected User Status must be normal or construction",
	cantLogOffZzd:'Main Terminal cannot be cancelled at first',
	cantLogOffCosBaseProdOweFee:'Overdue Basic Package Product cannot be cancelled!',
	cantLogOffCosBaseProdNotActive:' Basic Package Product Status is abnormal, cannot be cancelled!',
	
	cantPayIpUserFee:'Broadband User has no Product Please Purchase Product at first',
	
	freeUsersOver2:"Free Terminals cannot be more than 2",
	
	singleInteractiveDevCantOpenDuplex:'This User’s Stb is one-way，cannot open two-way',
	custMustHaveDuplexDev:'User must have two-way Stb',
	
	needCancelProgramFirst:'User has interactive TV Product，Please unsubscribe and then operate!',
	confirmCancelDuplex:"Sure to cancel two-way?",
	
	userHasNoBaseProd:'Customer has no basic package!',
	baseProdRechargedWait30Seconds:'Basic package is recharged already，Please wait half minute',
	usersNotOweFeeStop:'User’s Basic package Product[{0}]Status is not stopped for overdue！',
	confirmOpenTemp:"Sure to authorize?",
	openTempSuccess:'Temporary authorization is successful!',
	
	noUserSelected:'unselect any User!',
	noSelectedUserCanOpenTemp:'selected user has no conditions in line with temporary authorization!',
	userHasExtraFreeDev:'selected User has extra device, but the main device status is abnormal, cannot be authorized temporarily!',
	
	onlyOneUser: 'Please select one customer!',
	needStopUser: 'Please select User in【Termination Report】Status',
	confirmOpenUser: 'Sure to open?',
	confirmUntuckUser: 'You sure you want to disassemble?',
	searchCustTip1: 'Please input at least one key word to Inquiry Customer',
	searchCustTip2: 'Please do not input single quotation mark',
	searchNoCust: 'No Customer in line with Inquiry conditions，Please make sure and input again!',
	searchToPayOrPrint: 'Please deal with unpaid or unprinted Invoice Customer，click Sure to switch，Please Print unpaid Fee and Invoice for this Customer',
	confirmSwitchThisCust: 'Are you sure to select this customer?',
	confirmInvalidInvoice: "Sure to cancel Invoice?",
	confirmRefreshCmd: 'Sure to send New instruction?',
	confirmResendCmd: 'Sure to repeat Open Account instruction?',
	noPreStopUser: 'This User does not pre-report Termination!',
	
	phoneOrFixedPhoneMustBeEnterOne: 'You must enter a phone number!',
	invalidAddressPlsReInput: 'Useless Customer Address，Please input again!',
	intentionCustPlsChooseProvince: 'Intending Customer Please select province!',
	rechargeCountMustBeGreaterThanZero: 'Money Amount is more than 0!',
	transferModifyCustName: 'Please change Customer Name for transferring account!',
	changeModifyCustAddr: 'Please change Customer Address for moving Stb!',
	notModifyAnyInfo: 'You do not change any information!',
	buyNumExceedStockNum: 'Buy Quantity more than storage quantity!',
	enterPositiverNum: 'Please input positive integer!',
	enterBuyNum: 'Please enter purchased quantity!',
	refundAmountShouldLeeThanSumofTheAccounts: 'Refunded Money Amount shall be less than Account refundable Balance!',
	
	notChooseDeviceMustBeInput: 'When backfilling construction is not selected for equipment，the equipment code must be input!',
	noChargeNumber: 'Fee No. does not exist!',
	singleStbNotSupDTT: 'This Device is one-way Stb, Do not support current OTT Type!',
	doubleStbNotSupOTT: 'This Device is two-way Stb, Do not support current DTT Type!',
	modemNotSupUserType: 'Device Modem，Do not support selected[{0}]User Type!',
	currDeviceNotSupUserType: 'This Device do not support current User Type!',
	deviceFeeMustBeBetween:'Device Fee must be between [{0}]-[{1}]!',
	addUserToTempTable: 'Please add Users need saving to Temporary Saving Table!',
	notTransProject: 'No transfer payment item!',
	chooseTariff: 'Please choose fees at first!',
	MustBeOrderMonth: 'Purchase month number is necessary!',
	upgradeEndDateMoreThanBeginDate:'when upgrading，Ending Billing Date must be after Last Purchase Ending Date，Please adjust purchase month number!',
	realpayMustBeGreaterThanZero: 'Real Payment cannot be less than 0，Please add Purchase Month Quantity!',
	notMustBeOrderUser: 'No User need purchase!',
	custPkgChooseUserMustBeEqualToMaxUserNum: 'Customer Package is selected, Users shall equal to max user quantity of package!',
	spkgNoselectPkgCanContinueChooseUser: 'Protocol packages [{0}] does not select a user, the user can continue to choose!',
	spkgPkgCanContinueChooseUser: 'Agreement package is selected, Users less than max quantity  of package，you can continue to select User!',
	chooseInUsers: 'Please select Terminal User to join package!',
	exceedPkgMaxUserNum: 'More than max quantity limit of package!',
	completeChooseUserTerminal: 'Please fully select every User Type Terminal!',
	newOrderAmountCantBeLessThanZero: 'new orders amount can not be less than zero!',
	newOrderAmountCantBeLessThanTransAmount: 'new orders can not be less than the amount of the transfer amount',
	CancelTheAccountDismantleDevice:'Device not recovered And purchase way to configure cannot cancel',
	CancelTheAccountUserStatus:'The user state is stopped, disassemble, the construction cannot cancel',
	PleaseCancelTheOttMainTerminal:'Please cancel the main terminal OTT',
	noFeeItemCanNotContinue:'Can not continue because no cost item',
	EditPayFeeAndNumberIsWrong:'This fee is not divisible by the price, the purchase of the number as an integer',
	SelectOnlyOneData:'Please choose a record',
	ChooseToPayTheRecord:'Please choose to pay the record',
	userNotDTT:'Not DTT equipment or equipment does not exist',
	suerNotSave: 'Determined not to save it？'
	
}