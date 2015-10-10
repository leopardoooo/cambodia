/** boss-core Simplified Chinese Package */
BCLang = {}
BCLang.common = {
	optr: "Operation~",
	confirm: 'Confirm~',
	remove: 'Remove~',
	switchor: 'Select~',
	pswd: 'Password~',
	newPswd: 'NewPassword~',
	confirmPswd: 'ConfirmPassword~',
	remark: 'Remark~',
	remark2: 'RemarkInfo~',
	busiInfo: 'BusinessInfo~',
	busiSave: 'BusinessSaved~',
	tipBusiSaveOK: ' Business Saved Successfully!~',
	tipLoadText: 'Being Inquiring, Please Wait...',
	tipConfirmSave: 'Are you sure to save business?~',
	tipFormInvalid: 'Containing input items cannot pass examination!~',
	filterTreePanel:{
		emptyTipSearchField:'Input Name Filtration...~',
		btnExpandAll:'spread all resources~',
		btnCollapseAll:'merge all resources~'
	},
	filterTitle: 'condition filtration~',
	tipExistsSystem:'Are you sure to exist~',
	queryBtn:'Inquiry~',
	queryBtnWithBackSpace:' Inquiry~',
	total: 'Total~',
	subTotal: 'Sub-total~',
	price: 'UnitPrice~',
	count: 'Quantity~',
	pay: 'Pay~',
	fee: {
		columns: ["Fee Item~","Household Quantity~","Unit Price~","Money Amount~"],
		tbar0: "service fee~"
	},
	plsSwitch: 'PleaseSelect~',
	taskTitle: 'WorkOrder~',
	assignWay: 'OrderArrangementMethod~',
	removeSelected: 'MoveTheSelected~',
	expandOrCollpse: 'Expansion or contraction~',
	optional: 'Options~',
	selected: 'Selected~',
	optionalGroup: ' {0} options',
	selectedGroup: " {0} selected",
	save: 'Save~',
	close: 'Close~',
	busido: 'BusinessReceived~',
	defaultGroupTpl: '{0} selected',
	uploadFileLabel: 'Document uploading~',
	busiWay: 'Dealing Way~',
	tipSimple: 'Tip~',
    returnTxt:'Return~',
	tip: 'TipInfo~',
	totalRecord: ' ( Total: {0}) ',
	submit: 'Submit~',
	emptyMsg: 'No Data~',
	pageDisplayMsg: 'No.{0} –No. {1}, totally 2~',
	alertTitle: 'Tip~',
	submitingText: 'Being Submitting Data...~',
	loadingText: 'Being Inquiring, please wait...~',
	yes: 'YES~',
	print: 'Print~',
	cancel: 'Cancel~',
	no: 'NO~',
	update: 'Modify~',
	forbiddenBtn: 'Disabled~',
	enableBtn: 'Enable~',
	cancelBtn: 'Cancel~',
	doActionBtn: 'Manipulation~',
	add: 'Add~'
}
//home page module
BCLang.home = {
	topWelcome: "OperatorAtPresent~",
	searchTabs:["NameNo.","Device|Account","InstallAddress","Phone","Multi-Condition"],
	invoicePrintTitle:'InvoicePrint~',
	searchTip:"CustomerNo.|ReceptionNo.~",
	searchBtns:["Search~","Pay~"],
	main: {
		tabs: ["Customer Information~","Unit Information~", "User Information~", "Account Information~", "Payment Record~", "Reception Information~", "Service Serial Order~","Instruction Information~", "Bill Information~"],
		//cash payment page
		cashPay: {
			_title: 'Pay~',
			pay: {
				_stayTitle: 'Items to be paid~',
				_title: 'Payment~',
				columns: ['Operate~', 'Service Name~', 'Fee Name~', 'Paid Money Amount~', 'Order No.~', 'Remark~', 'Quantity~', 'Operate Time~', 'Fee No.~', 'serial number~'],
				buttons: ['Save~', 'Close~']
			},
			charge:{
				_title: 'Charging Information~',
				columns: ['Total USD~', 'Current Exchange Rate~', ' KHR~', 'payer~', 'Payment Way~', 'Reception No.~', 'Account Date~', 'Received USD~', ' Received KHR~']
			}
		},
		// customer page
		cust: {
			base: {_title: "Basic Information~",name: "Customer Name~",busiId: "Customer No.~",openDate: "Open Account Date~",addr: "Address~",status: "Customer Status~",
				type: "Customer Type",certType: "ID Type~",certNum: "ID No.~",linkMan: "Contact~",tel: "mobile phone number1~",barthday: "Birthday~",
				mobile: "mobile phone number2~",areaCateory: "Area Community~",houseNetType: "service Type~",houseManager: "Community Customer Manager~",
				houseOptr: "Community Maintenance Staff~",postalAddr: "PostAddress", remark: "Remark~",
				email: "email~", sex: "sex~",postcode: 'postcode~', blackList: 'black list~',
				agentName: "Agent~", deptName: "Open Account Department~", developName: "Developer", BusinessLicence: "Business License~", unitNumber: "Unit Taxation Number~",
				spkgSn: "Agreement No.~", cust_level: "Customer Level~",switchCustTitle: 'Select Customer~', languageType: 'Service language~', unitName: 'Subordinate unit~'
			},
			_form: {
				thinkCust: 'Intending Customer~',
				switchProvince: 'Please select province~',
				oldCustLevel: 'Former Customer Level~',
				newCustLevel: 'New Customer Level~',
				deviceType: 'Equipment Type~',
				deviceModel: ' Device Model~',
				storeCount: 'Storage Quantity~',
				buyWay: 'Purchase Way~',
				buyCount: ' Purchased Quantity~',
				titleBatchBuyMaterial: 'Material Information~',
				titleAcctRefund: 'Refunding~',
				refundTotal: 'Refunding Money Amount~',
				rechargeWay: 'Recharging Method~',
				byTicket: 'On Reception~',
				rechargeCount: 'Recharging Amount~',
				province:'Province~',
				provinceEmptyText:'Please select Province~',
				addrManager: 'Address Management~',
				roomNumber: 'Room No.~',
				roomStatus: 'Room Status~',
				roomTitle: 'Room Information~',
				roomTitle2: 'Administrative area:{0} service type:{1}',
				addRoom: 'increased room~',
				unitName:'unit name~',
				unitAddress:'unit address~'
			},
			acct: {
				_title: "Account Information~",
				columns: ["Account Name~","Card No","Balance~","Due Fee of Past Months~", "Due Fee of this month~","Real Time Fee~","Real Time Balance~","transferable Balance~","Refundable Balance~","Freeze Balance~"]
			},
			change:{
				_title: 'Abnormal Information~',
				columns: ["Feature~","Before Change~","After Change~","Change Date~"]
			},
			acctTabs: {
				detail: {
					_title: 'Details~',
					columns: ["Fund Type~","Balance~"]
				},
				adjust: {
					_title: 'Account Adjustment~',
					columns: ["Account Adjustment Money Amount~","Operate Time~","Account Adjustment Reason~","Remark~"]
				},
				changes: {
					_title: 'Abnormal~',
					columns: ["Service Name~","Fund Type~","Change Type~","Before Change~","Change Money Amount~","After Chang~","Remark~","Operate Time~"]
				}
			}
		},
		// Customer Page
		user: {
			base:{
				type: 'Service Type', name: ' Terminal Name', status: 'Status~', statusTime: ' Status Time~',
				stbId: 'Set-Top Box~', cardId: 'Smart Card~', modem: 'Modem No.~', createTime: 'Create Time~',
				loginName: 'Account~', terminal: 'Terminal Type~', deviceModel: 'Equipment Type~', buyWay: 'Buy Way~',
				stopDate: 'Termination Pre-Report Time~', stopType: 'Calling Fee Type~', protocolDate: 'Agreement Date~', 
				str4: 'IP Adress~',str6: 'IP Fee Amount~'
			},
			_form: {
				taskBackFill: ' backfilling construction~',
				deviceCode: 'Equipment Code~',
				feeName: ' Fee Name~',
				feeAmount: 'Fee Money Amount $~',
				protocolInfo: 'Agreement Information~',
				openAmount: 'Account Number~',
				manualOpen: 'Manual Open Account~',
				addToOpenUserGrid: 'Add to Temporary<br/> Saving Table~',
				titleOpenUserGrid: 'User Temporary Storage~',
				titleSwitchProd: 'Step One：select Product~',
				prodName: 'Product Name~',
				prodDesc: 'Product Description~',
				titleDetemineUser: ' Step Two: Sure for Purchasing User~',
				switchUsers: 'Select Customer~',
				titleOrderInfo: ' Step Three: Purchase Information~',
				prodTariff: 'Product Fee~',
				prodOrderType: 'order method~',
				prodOrderMonths: 'Purchase Month Quantity~',
				prodStartDate: 'Billing Starting Date~',
				prodExpDate: ' Billing Ending Date~',
				lastOrderExpDate: 'Ending Date of Last Purchase~',
				titleOrderFee: 'Product Fee~',
				shouldPay: 'Receivable~',
				addOrderFee: 'New Increased Purchase~',
				transferPay: 'transfer payment~',
				maxUserCount: 'Max User Quantity~',
				titleDispatchUser: 'Distribute User~',
				terminalInfo: 'Terminal Information~',
				addToSelected: 'Added to the selected~',
				moveToOptional: 'Move to the Selectable~',
				stopTime: ' Stop Report Time~',
				stopFee: 'Termination Report Fee~',
				prodFeeCM: ["Product Name~","Former Fee~","New Fee~","Former Expiry Date~","Billing Date~",
				          "Payment Month Quantity~","New Expiry Date~","Transfer Payment Money Amount~","Payment Money Amount~"],
				prodTitle: 'Product Information~',
				prodGroupText: 'User Name:{0}  User Type:{1}~',
				templateDown: ' Template Download~',
				templateDownTip: 'Please do not operate when service is busy; Please do not cancel the first line of template;~',
				loginId: 'Login~',
				orderFee: 'Order Balance~',
				canRetrunFee: 'refundable Money Amount~',
				canTransferFee: 'transferable Money Amount~',
				canRefundRealFee: 'Real refund amount~',
				returnDevice: 'Recycle Equipment~',
				retrunInfo: 'Refundable~',
				transferInfo: 'transferable~',
				stdId: 'STP No.~',
				stdModel: ' STP Type~',
				newStdId: 'New Set-Top Box No.~',
				newStdModel: 'New Set-Top Box Type~',
				modemId: 'Modem No.~',
				modemModel: 'Modem Type~',
				newModemId: 'New Modem No.~',
				newModemModel: 'New Modem Type~',
				changeCause: 'Change Reason~',
				userName: 'User Name~',
				chargeNum: 'Charging Quantity~',
				desc: 'Description~',
				feeItem: 'Fee Item~',
				unitPrice: 'Unit Price~',
				ipFeeDesc: 'IP Fee= Unit Price * Billed Quantity*Month Quantity（or days/30）~',
				expDate: 'Expiry Date~',
				payMonth: 'Month Quantity to Collect~',
				totalPrice: 'Total Price~',
				timeSlot: 'Period~',
				titleBusiFeeGrid: 'Other Fee~',
				ipFee: 'IP fee~',
				
				titleTransferPayDetailGrid: 'transfer paymentDetails~',
				transferPayCM: ['Product Name~', 'Fee~', 'User~', ' Billing Starting Date~', 'Ending Billing Date~', 'Transfer Money Amount$~'],
				transferDetailDate: ' Transferring Details（Billing Starting Date：{0} "）~',
				labelChangeDeviceResion: 'The replacement need~',
				labelChangeBuyDevice: ' Income from the purchase cost~',
				labelChangeReclaimDevice: ' Recycling Equipment~',
				labelChangeLossDevice: ' Report the loss of equipment~',
				
				oldOrderFee: 'original order amount~',
				oldTransFee: 'original transfer payments~',
				realOrderFee: 'new orders amount~',
				newAddFee: 'up close to the amount of~'
			},
			list: {
				_title: 'User Information~',
				tools: ["Inquiry~"]
			},
			prod: {
				base: {
					_title: " Customer Product~",
					columns: ["Order Sn","Product Name~","Belonged Package~","Current Fee~","Effective Date~" ,"Invalid Date~","Status~","Status Change Time~","Purchase Time~" ,"Purchase Month Quantity~","Create serial number~"]
				},
				pkg: {
					_title: 'Customer Package~',
					columns: ["Order Sn","Product Name~","Current Fee~","Status~"," Effective Date~" ,"Invalid Date~","Product Type~","Purchase Time~"]
				},
				tools: ["Default Order~","History Oder~"]
			},
			userDetail: {
				tabs: ["Details~","Abnormal Information~"],
				detail: ["User Type~","User Name~","Equipment Type~","Buy Way~","Status~","Status Time~","Pre-termination Report Time~","Creation Time~","Calling Fee Type~","During Agreement Period~","Account~",
				         "Terminal Type~","Account~","Terminal Type~","IP Information~","IP Fee Amount~"],
				change: ["Service~","Feature~","Prior to Change~","After Change~","Change Date~","Operator~"]
			},
			prodDetail: {
				tabs: ["Order Amount in Details~","Abnormal Information~"],
				detail: ["No.~","Fund Type~"," Import Product~"," Import Type~"," Import Money Amount~"," Export Product~","Export Type~","Import Money Amount~","serial number~" ]
			}
		},
		//Payment Records
		pay: {
			payfee: {
				_title: "prepaid money~",
				columns: ["serial number~","Service Name~","Account Type~","Account Name~","User Type~", "User Name~","Equipment No.~","Status~","Money Amount~","Estimated Expiry Date before Payment~",
				        "Expiry Date after Payment~","Print Status~","Payment Way~","Reception Date~","Account Date~","Receptionist~","Reception Department~","Invoice~","Invoice Issuance Way~", "Invoice Type~", "Development of Human ~"]
			},
			busifee: {
				_title: "Service Fee~",
				columns: ["serial number~","Fee Name~","Equipment Type~","Equipment No.~","Status~","Print Status~",
				          "Payable~","Paid~","Payment Way~","ReceptionDate~","Receptionist~","ReceptionDepartment~","Invoice~"," Invoice Issuance Way~","Invoice Type~","Buy Quantity~","Equipment Type~","Remark~"]
			},
			detail: {
				_title: 'payment record~',
				columns: ["Payment No.~","USD~","KHR~","Exchange~","KHR Round-off~","Effective~","Payment Way~",
				          "Payer~","Service serial number~","Reception No.~","Reception Issuance Way~","Reception Date~","Receptionist~","Reception Department~"]
			},
			feePayDetail: {
				_title: 'Fee Details~',
				columns: ['serial number','feeitem', 'amount','invoice']
			},
			_form: {
					oldOptrName: 'Old Development~',
					newOptrName: 'New development~'
				}
		},
		// Reception Information
		doc: {
			invoice: {
				_title: 'Invoice~',
				columns: ["InvoiceNo.~","InvoiceCode~","Money~","PrintTime~"," ReceptionIssuanceWay~","InvoiceType~","UsageStatus~","BalanceStatus~","Operator~","GeneratTime~"]
			},
			task: {
				_title: 'Work Order~',
				columns: ["TicketNo.~", "TicketType~", "WorkOrderStatus~", "ConstructionTeam~","Failure~", "FaultDetails~", "ZTEAuthorizedState~", "CreatingTime~"],
				userColumns: ['UserType~', 'UserName~', 'DeviceModel~', 'DeviceNumber~', 'PosNo~', 'OccNo ~', 'BandWidth~'],
				detailColumns: ['OperatTime~','OperatType~', 'Operator~','SyncStatus~', 'ErrorDesc~'],
				winTitle: 'Ticket Info~',
				userTitle: 'User Info~',
				opertaionTitle: 'Operation Info~'
			},
			busi: {
				_title: 'Service Reception Order~',
				columns: ["Operator~"," FinalPrint~","ServiceName~"]
			},
			_form: {
				oldInvoiceId: 'FormerInvoiceNo.~',
				oldInvoiceCode: ' FormerInvoiceCode~',
				oldInvoiceType: ' FormerInvoiceType~',
				oldStatus: ' FormerInvoiceStatus~',
				newInvoiceId: 'NewInvoiceNo.~',
				newInvoiceCode: 'NewInvoiceCode~'
			}
		},
		//Reception Record
		doneCode: {
			_title: ' Reception Record~',
			columns: ["serial number~","Service Name~","Status~","Reception Date~","Operator~","Reception Department~",
			         "Returnable~","Ignorable~","Paid Money Amount~"," Service Expansion Information~","Remark~"]
		},
		//instruction
		cmd: {
			dtt: {
				_title: "DTT Instruction Information~",
				columns: ['Service serial number~','Set-Top Box No.~','Smart Card No.~','Control Word~','Program Name~',
				       'Instruction Type~','Result Mark~','Create Time~','Send Time~','CA Recycle Time~','Wrong Information~','Authorization Ending Date~']
			},
			ott: {
				_title: "OTT Instruction Information~",
				columns: ['Instruction No.~','Service Serial Number~','Instruction Type~','Set-Top Box No.~','Smart Card No.~','MAC~','Success or not~','Wrong Information~','Send Time~']
			},
			band: {
				_title: 'BAND Instruction Information~',
				columns: ['Instruction No.~','Service Serial Number~','Instruction  Type~','Set-Top Box No.~','Modem No.~','Success or Not~','Wrong Information~','Sending Time~']
			}
		},
		// Bill
		bill: {
			list: {
				_title: 'Bill Information~',
				columns: ['Billing Period~','Source~','Smart Card No.~','Serial~','Charge-off Time~','Account Name~',
				      'Fee Name~','Status~',' Charge-off Money Amount~','Due Money Amount~','Operate~'],
				tbar: ["Smart Card~","serial number~","Due Bill~","All Bill~"]
			},
			acctitemInvalid: {
				_title: 'Account Invalid Information~',
				columns: ["Account Name~","Fund Type~","Invalid Money Amount~"]
			}
			
		}
	}, // main end line...
	// Homepage Tool  Column Service
	tools: {
		countySwitch:{//Branch Switch			titleSelectDept:'Select Department~',
			confirmSwitchDept:'Sure to switch Department~'
		},
		ad: {//Declaration
			_title: ' Declaration Information~',
			tplPublishTme:'Published on~',
			columns: [' Declaration Subject~','Publisher~','Effective Time~','Invalid Time~']
		},
		grxg:{//Personal Amendment
			_title:' Personal Information Amendment~',
			labelNewPwd:'new code~',
            labelNewPwdConfirm:'confirm new code~',
            labelDefaultSystem:'Default Login System~'
		},
		queryDevice:{//Equipment Inquiry
			_title:' Equipment Inquiry~',
			titleDevInfo:' Equipment Information~',
			labelDevNo:' Equipment Serial Number~',
			labelDevType:'Equipment Type~',
			labelDevModel:'Equipment Type~',
			labelModelName:'Type Name~',
			labelDevName:'Equipment Name~',
			labelDevCode:'Equipment No.~',
			labelDeptName:'Storage Warehouse~',
			labelCustNo:'Customer No.~',
			labelCustName:'Customer Name~',
			labelDevStatus:'Equipment Status~',
			labelDepStatus:'Storage Status~',
			labelTranStatus:'Moving Status~',
			labelOwnership:'Ownership~',
			
			labelCardNo:'Matching Card No.~',
            labelCardModel:'Martching CardType~',
            labelModemNo:' Matching MODEM No.~',
            labelModemModel:' Matching MODEM Type~',
            labelStbNo:' Matching Set-Top Box No.~',
            labelStbModel:' Matching Set-Top Box Type~',
            tipDevNotExists:'Inquiry Equipment does not exist!~'
			
		},
		invoiceQuery:{//Invoice Inquiry
			_title:'Invoice Inquiry~',
			titleInvoiceInfo:'Invoice Information~',
			btnShowInvoiceDetail:'Fee Details~',
			btnChangeStatus:'Change Status~',
			btnChangeStatusIdel:'change to unused~',
			btnChangeStatusInvalid:'change to invalid~',
			btnChangeStatusUsed:'Used~',
			
			confirmChangeStatus:'Whether Invoice Status is changed to~',
			tipInvoiceNotExists:' Invoice does not exist!~',
			tipInvoiceNotExists2:'Inquiry Invoice does not exist!~',
			statusInvalid:'invalid~',
			statusIdel:'unused~',
			
			cols:['Customer Name~', ' Customer No.~', 'Service Name~', 'Fee Name~', 'Actual Amount~', 'Operation Time~', 'Operator~'],
			
			labelInvoiceId:'Invoice No.~',
			labelInvoiceId2:'Inv&nbsp;oice&nbsp;No.&nbsp;~',
			labelInvoiceCode:'Invoice Code~',
			labelInvoiceType:'Invoice Type~',
			labelDeptName:'Storage Warehouse~',
			labelUseStatus:'Usage Status~',
			labelMoneyAmount2:'Amount~',
			labelOptrName:'Taker~',
			labelOpenOptrName: 'Operator~',
			labelCreateTime:'Storage Time~',
			labelFinanceStatus:'Balance Status~',
			labelCloseTime:'Write-off Time~',
			labelCheckDeptName:'Settlement Warehouse~',
			labelCheckTime:'Settlement Time~'
			
		},
		CustSearch:{//Customer Inquiry(multi-condition inquiry)
			_title:' Customer Inquiry~',
			tipInputAnyField:'Please input any item to search!~',
			labelCustName:'Customer Name~',
			labelStatus:'Intending Customer~',
			labelAddress:'Customer Address~',
			labelLoginName:'Account~'
		},
		InvoicePrint:{//Invoice Print
			_title:'Invoice Print~',
			titleDocGrid:'Print List~',
			titleInvoiceGrid:'Invoice Item~',
			titlePrintDetail:'Print Details~',
			titlePrintPreview:'Print Preview~',
			titleInvoiceWindow:'Print {0} Invoices totally~',
			wdxj:'Shop Cash~',
			printGridColumns:['serial number~','Reception Name~','Create Time~'],
			invoiceGridColumns:['Serial Number~','Invoice No.~','Invoice Code~'],
			printItemGridColumns:['Name~','Money Amount~'],
			stillEmptyInvoiceField:"Still empty invoice input frame!~",
			hasDuplcateInvoice:"There is repeated Invoice，Please confirm!~"
		},
		AddressNodeManage : {
			panelTitle : 'Address Management~',
			msg : {
				actionFailed : 'Operation failed~',
				actionSuccess : 'Operation Successful~',
				confirmSave : 'Confirm save?~',
				confirmInvalid : 'sure you want to disable?~',
				confirmDelete : 'you sure you want to delete?~',
				confirmActivate : 'confirmation to activate?~',
				cantDelete : 'There are customers in the use, temporarily can not be deleted.~',
				cantBeInvalided : 'the presence of sub-level, can not be disabled!~',
				maxDepthText : 'can not be down to add~',
				noSingleQuoteAllowed : 'Do not enter single quotation marks~'

			},
			formWin : {
				labelSortNum : 'Sort value',
				labelProvince : 'Province~',
				labelDistrict : 'administrative area~',
				labelNetType : 'Network type~',
				emptyTxtProvince : 'Please select the province~',
				emptyTxtBlurQuery : 'support fuzzy queries~',
				btnTxtClose : 'close~',
				btnTxtSave : 'save~',
				labelRoadNum : 'House No.',
				titleNewSaveLevelRoadNum : 'New House No.',
				titleEditSaveLevelRoadNum:'Edit Road~',
				labelStreatName : 'street name~',
				titleNewSaveLevelStreet : 'New Street~',
				titleEditSaveLevelStreet:'Edit Street~',
				labelCityName : 'city name~',
				titleNewSaveLevelCity : 'new city~',
				titleEditSaveLevelCity : 'Edit city~',
				labelShowName : 'show name~',
				labelParentName : 'SuperiorsName',
				labelBrotherName:'brother name~',
				labelOldName:'old name~',
				
				labelNewAddChild : 'new subordinate~',
				labelNewAddBrother : 'new flat level~',
				labelAddrTree : 'address tree~'
			}
		},
		TaskManager : {
			_title : 'Work Order Management~',
			_operateTitle : 'Operation Details~',
			_userTitle : 'User Details~',
			_winTitle : 'Work Order completion~',
			_ZteWinTitle : 'ZTE authorized~',
			forms : {
				custNo : 'Customer ID~',
				taskNo : 'ticket number~',
				custName : 'Customer Name~',
				mobile : 'Tel~',
				newaddr : 'address~',
				taskStatus : 'work order status~',
				taskDetailType : 'ticket type~',
				taskAddr : 'area~',
				taskTeam : 'construction team~',
				faultType : 'fault type~',
				finishType : 'completion type~',
				finishExplan : 'completed Description~',
				zteStatus	: 'ZTE status~',
				zteRemark :'remark~'
			},
			buttons : {
				query : 'Query~',
				pendingOrder : 'pending work orders~',
				accptTime : 'Hours:~',
				distTeam : 'Order Arrangement~',
				invalidTeam : 'ticket invalid~',
				backDevice : 'backfill equipment~',
				finish : 'completed~',
				returning : 'visit~',
				sendAuth : 'ZTE authorized~',
				withdraw : 'cfocn withdraw~'
			},
			taskCols : ['ticket type~', 'customer name~', 'work order status~', 'construction team~',
					'ZTE state~', 'address~', 'Tel~', 'creation time~ ', ' fault type~ ', ' fault details~ ','work No.~','Customer Manager~','Customer Manager tel~'],
			userCols : ['user type~', '~ username', 'password~', 'device model~', 'device number~', 'posNo~', 'occNo~', 'Bandwidth~'],
			operateCols : ['operating time~', 'operation type~', 'operator~,', 'synchronization status~', 'description~','delay time~'],
			taskDeviceCols : ['user type~', '~ username', 'model~', 'Device number~', 'equipment recycling~','old device number~'],
			msg : {
				enterDeviceNo : 'Please enter the device number~',
				noCancel : 'No cancellations~!',
				sureWantSelectedWork : 'sure you want to void the selected work order you~?',
				sureWantWithdrawSelectedWork : 'sure you want to withdraw the selected work order you~?',
				teamCantEmpty : 'construction team can not be empty~',
				faultTypeCantEmpty : 'fault type can not be empty~',
				finishTypeCantEmpty : 'completion type can not be empty~',
				ZteStatusCantEmpty : 'zte status can not be empty~',
				selectRecord : 'Please select the records need to operate!~',
				roderHaveBeenCompletedOrObsolete : 'ticket have been completed or obsolete~',
				teamIsnotSingleWork : 'This work is not a single construction team of the department~',
				taskStatusInitAndSupernet:'Work order status must be During Construction and construction team must be supernet~',
				taskStatusInitAndCfocnCanWithdraw:'Work order status must be During Construction and construction team must be cfocn~',
				taskStatusInitAndSupernetCanAssignment:'Work order status is Waiting or During Construction and construction team is supernet can assignment~',
				zteStatusCanSend:'status is failure  or NOT_EXEC  can be operate zte~'
			}
		},
		feeUnitpre : {
			_title : 'non-business TOLL information~',
			cols : ['contract type~', 'contract number~', 'contract name~', 'customer name~', 'contract amount~', 'invoice number~', 'operator~'],
			forms : {
				cust_name : 'Customer Name~',
				contract_no : 'Contract No.~',
				nominal_amount : 'contract amount~',
				fee_name : 'expense name~',
				contract_name : 'Contract name~',
				addr_district : 'to your city~',
				lblFee : 'total USD~',
				LabelJian : 'Cambodia KHR~',
				nfDollar : 'Paid USD~',
				nfJianYuan : 'Paid KHR~',
				labelExchange : 'the date of the exchange rate~',
				pay_type : 'payment method~',
				receipt_id : 'bill number~',
				invoice_id : 'receipt number~'
			},
			msg : {
				enterKeywords : 'keywords~',
				receiptCantUsed : 'The receipt can not be used~',
				custNameOrContractNoQuery : 'supports the customer name or contract number fuzzy search~'
			}
		}
	}
	
}
//Various Pop-up Messages
BCLang.msgBox = {
	payInfo: 'You have<b>{0}</b> bills to pay,totally<b>{1}</b>$',
	payButton: 'Cash Payment',
	selectInvoice2Print:"Please select invoice to print!~",
	selectInvicePrintItem:"Please select Invoice Item to Print~",
	templateReplaceError:"Errors in template variable substitution! error:{0}~",
	printCmpError:'Print Controls abnormal，Please check {0} Print Controls installed or not {0}~',
	invoiceIdNeeded:'PleaseinputInvoice No.~',
	confirmSaveInvoiceInfo:"Sure to save Invoice Information?~",
	waitMsg:'Operating, please wait~',
	needCust:'Please find Customer to Operate at first!~',
	needUser:'Please select customer!~',
	cancelFeeSuccess:'Successful Write-off!~',
	confirmCancelFeeAndInvaidInvoice:'Invoice{0} will be invalid! Fee Items of this Invoice need to be printed again，Sure to cancel it?~',
	confirmCancelFee:'Sure to cancel it?~',
	selectRec4CancelFee:'Please select fee record to cancel!~',
	confirmUnPayWithParam:'Sure to return to【Money Amount：{0} 】?~',
	confirmRestoreCust:'Sure to restore customer?~',
	restoreCustSuccess:'restore customer successfully!~',
	restoreCustFailed:'fail to restore customer, Please contact administrator.~',
	needLogOffUser:'Please cancel user under this Customer at first~',
	recycleGdDevice:'Please recycle Equipment whose ownership is SuperNet~',
	custIsRelocated:'Customer has moved~',
	confirmRelocateCust:'Are you sure to move customer?~',
	notAllowedJoinUnit:'The customer is unit customer, cannot join unit!~',
	confirmQuitUnit:' Are you sure to exist Unit?~',
	confirmBankStop:'Sure to suspend fee deduction of card?~',
	confirmEnableBankPay:'Start fee deduction of bank card?~',
	confirmDisableBankPay:'Forbid fee deduction of bank card?~',
	confirmEditBankPay:'Confirm the Product',
	confirmBankResume:'Sure to restore fee deduction from card?~',
	confirmRenewCust:'Restore Customer Status?~',
	confirmCancelPayFeeWithParam:'Determined to pay rollback and void an invoice:{0}',
	confirmCancelPayFeeWithNoParam:'Determined to pay rollback?',
	cancelPayFeeSuccess:'Payment rollback Reversal Success!',
	
	custHasUnSuitableDev:'This User cannot do Equipment exchange now~',
	custCantExchangeDev:' This User cannot do Equipment exchange now~',
	
	confirmRegLoss:'Are you sure to loss report?~',
	regLossSuccess:'Successful loss report!~',
	
	selectDev2RegLoss:'Please select to cancel lost report equipment~',
	selectDevIsRegLossAlready:'Please select lost report equipment~',
	unRegLossSuccess:'successful cancellation of loss report!~',
	confirmUnRegLoss:'Sure to cancel lost report?~',
	//Open and Close Print Mark
	statusNotPrintStatusSuccess:'Mark No-Print successfully!~',
	confirmNotPrintStatus:'Sure for not Print?~',
	statusPrintStatusSuccess:'open mark successfully!~',
	confirmPrintStatus:'Sure to open Print Mark?~',
	//Equipment Sales
	selectDev2Sale:'Please select Equipment on sales!~',
	cantSaleCosOwerIsCust:'Equipment ownership belongs to Customer, cannot be sold~',
	//Change Purchase Way
	modifyBuyType:'Change Purchase Way~',
	need2SwitchCountyId:'current shop is not the shop where the Equipment is bought，Please switch to [{0}] and then operate！~',
	//Change Ownership
	changeOwner2Cust:'Sure to change ownership to individual？~',
	changeOwner2Gd:'Sure to change ownership to SuperNet？~',
	cangeOwnerSuccess:'change ownership successfully!~',
	//Equipment recycling
	selectDev2Recycle:'Please select recycled Equipment!~',
	devCantRecycleStillInUse:"Equipment is in using, cannot be recycled~",
	onlyResidentCanNonResiCust:'Only Resident Customer can be turned to Group Customer~',
	depositUnPaySuccess:'Deposit refunded successfully!~',
	unPayed:'unpaid！~',
	notDeposit:'This Fee is not deposit！~',
	confirmDepositUnPay:"Sure to return deposit?~",
	
	userNotActive:"selected User Status is abnormal~",
	cantLogOffZzd:'Main Terminal cannot be cancelled at first~',
	cantLogOffCosBaseProdOweFee:'Overdue Basic Package Product cannot be cancelled!~',
	cantLogOffCosBaseProdNotActive:' Basic Package Product Status is abnormal, cannot be cancelled!~',
	
	cantPayIpUserFee:'Broadband User has no Product Please Purchase Product at first~',
	
	freeUsersOver2:"Free Terminals cannot be more than 2~",
	
	singleInteractiveDevCantOpenDuplex:'This User’s Set-Top Box is one-way，cannot open two-way~',
	custMustHaveDuplexDev:'User must have two-way Set-Top Box~',
	
	needCancelProgramFirst:'User has interactive TV Product，Please unsubscribe and then operate!~',
	confirmCancelDuplex:"Sure to cancel two-way?~",
	
	userHasNoBaseProd:'Customer has no basic package!~',
	baseProdRechargedWait30Seconds:'Basic package is recharged already，Please wait half minute~',
	usersNotOweFeeStop:'User’s Basic package Product[{0}]Status is not stopped for overdue！~',
	confirmOpenTemp:"Sure to authorize?~",
	openTempSuccess:'Temporary authorization is successful!~',
	
	noUserSelected:'unselect any User!~',
	noSelectedUserCanOpenTemp:'selected user has no conditions in line with temporary authorization!~',
	userHasExtraFreeDev:'selected User has extra device, but the main device status is abnormal, cannot be authorized temporarily!~',
	
	onlyOneUser: 'Please select one customer!~',
	needStopUser: 'Please select User in【Termination Report】Status~',
	confirmOpenUser: 'Sure to open?~',
	confirmUntuckUser: 'You sure you want to disassemble?~',
	searchCustTip1: 'Please input at least one key word to Inquiry Customer~',
	searchCustTip2: 'Please do not input single quotation mark~',
	searchNoCust: 'No Customer in line with Inquiry conditions，Please make sure and input again!~',
	searchToPayOrPrint: 'Please deal with unpaid or unprinted Invoice Customer，click Sure to switch，Please Print unpaid Fee and Invoice for this Customer~',
	confirmSwitchThisCust: 'Are you sure to select this customer?~',
	confirmInvalidInvoice: "Sure to cancel Invoice?~",
	confirmRefreshCmd: 'Sure to send New instruction?~',
	confirmResendCmd: 'Sure to repeat Open Account instruction?~',
	noPreStopUser: 'This User does not pre-report Termination!~',
	
	phoneOrFixedPhoneMustBeEnterOne: 'You must enter a phone number!~',
	invalidAddressPlsReInput: 'Useless Customer Address，Please input again!~',
	intentionCustPlsChooseProvince: 'Intending Customer Please select province!~',
	rechargeCountMustBeGreaterThanZero: 'Money Amount is more than 0!~',
	transferModifyCustName: 'Please change Customer Name for transferring account!~',
	changeModifyCustAddr: 'Please change Customer Address for moving STB!~',
	notModifyAnyInfo: 'You do not change any information!~',
	buyNumExceedStockNum: 'Buy Quantity more than storage quantity!~',
	enterPositiverNum: 'Please input positive integer!~',
	enterBuyNum: 'Please enter purchased quantity!~',
	refundAmountShouldLeeThanSumofTheAccounts: 'Refunded Money Amount shall be less than Account refundable Balance!~',
	
	notChooseDeviceMustBeInput: 'When backfilling construction is not selected for equipment，the equipment code must be input!~',
	noChargeNumber: 'Fee No. does not exist!~',
	singleStbNotSupDTT: 'This Equipment is one-way Set-Top Box，Do not support current OTT Type!~',
	doubleStbNotSupOTT: 'This Equipment is two-way Set-Top Box, do not support current DTT Type!~',
	modemNotSupUserType: 'Equipment为Modem，Do not support selected[{0}]User Type!~',
	currDeviceNotSupUserType: 'This Equipment do not support current User Type!~',
	deviceFeeMustBeBetween:'Equipment Fee must be between [{0}]-[{1}]!~',
	addUserToTempTable: 'Please add Users need saving to Temporary Saving Table!~',
	notTransProject: 'No transfer payment item!~',
	chooseTariff: 'Please choose fees at first!~',
	MustBeOrderMonth: 'Purchase month number is necessary!~',
	upgradeEndDateMoreThanBeginDate:'when upgrading，Ending Billing Date must be after Last Purchase Ending Date，Please adjust purchase month number!~',
	realpayMustBeGreaterThanZero: 'Real Payment cannot be less than 0，Please add Purchase Month Quantity!~',
	notMustBeOrderUser: 'No User need purchase!~',
	custPkgChooseUserMustBeEqualToMaxUserNum: 'Customer Package is selected, Users shall equal to max user quantity of package!~',
	spkgPkgCanContinueChooseUser: 'Agreement package is selected, Users less than max quantity  of package，you can continue to select User!~',
	chooseInUsers: 'Please select Terminal User to join package!~',
	exceedPkgMaxUserNum: 'More than max quantity limit of package!~',
	completeChooseUserTerminal: 'Please fully select every User Type Terminal!~',
	newOrderAmountCantBeLessThanZero: 'new orders amount can not be less than zero ~!',
	newOrderAmountCantBeLessThanTransAmount: 'new orders can not be less than the amount of the transfer amount ~'
}