/** boss-core简体中文语言包 */
BCLang = {}
BCLang.common = {
	optr: "操作~",
	switchor: '选择~',
	pswd: '密码~',
	remark: '备注~',
	remark2: '备注信息~',
	busiInfo: '业务信息~',
	busiSave: '业务保存~',
	tipBusiSaveOK: '业务保存成功!~',
	tipLoadText: '正在查询，请稍等...',
	tipConfirmSave: '确定要保存业务吗?~',
	tipFormInvalid: '含有验证不通过的输入项!~',
	filterTreePanel:{//可以根据关键字过滤的treePanel
		emptyTipSearchField:'输入名称过滤...~',
		btnExpandAll:'展开所有资源~',
		btnCollapseAll:'合并所有资源~'
	},
	tipExistsSystem:'确定要退出系统~',
	queryBtn:'查询~',
	queryBtnWithBackSpace:'查  询~',
	total: '合计~',
	subTotal: '小计~',
	price: '单价~',
	count: '数量~',
	pay: '支付~',
	fee: {
		columns: ["费用项~","户数~","单价~","金额~"],
		tbar0: "业务费用~"
	},
	plsSwitch: '请选择~',
	taskTitle: '工单~',
	assignWay: '派单方式~',
	removeSelected: '移除选中~',
	expandOrCollpse: '展开或收缩~',
	optional: '可选~',
	selected: '已选~',
	save: '保存~',
	close: '关闭~',
	busido: '业务受理~'
}
//主页模块
BCLang.home = {
	topWelcome: "当前操作员~",
	searchTabs:["名称编号~","设备编号~","安装地址~","电话号码~","多条件搜索~"],
	searchTip:"客户编号|受理编号~",
	searchBtns:["搜索~","缴费~"],
	main: {
		tabs: ["客户信息~","单位信息~", "用户信息~", "账户信息~", "缴费记录~", "单据信息~", "业务流水~","指令信息~", "账单信息~"],
		//现金支付面板
		cashPay: {
			_title: '支付~',
			pay: {
				_title: '支付项目~',
				columns: ['操作~', '业务名称~', '费用名称~', '实付金额~', '订单号~', '备注~', '数量~', '操作时间~', '费用编号~', '流水号~'],
				buttons: ['保存~', '关闭~']
			},
			charge:{
				_title: '收费信息~',
				columns: ['总额USD~', '当日汇率~', '柬埔寨KHR~', '付款人~', '缴费方式~', '票据编号~', '账务日期~', '实收USD~', '实际收KHR~']
			}
		},
		// 客户面板
		cust: {
			base: {_title: "基本信息~",name: "客户名称~",busiId: "受理编号~",openDate: "开户日期~",addr: "地址~",status: "客户状态~",
				type: "客户类型~",certType: "证件类型~",certNum: "证件号码~",linkMan: "联系人~",tel: "固定电话~",barthday: "出生日期~",
				mobile: "手机号码~",areaCateory: "区域小区~",houseNetType: "服务类型~",houseManager: "小区客户经理~",
				houseOptr: "小区运维人员~",postalAddr: "邮寄地址~", remark: "备注~",
				email: "邮箱~", sex: "性别~",postcode: '邮编~',
				agentName: "代理商~", deptName: "开户部门~", developName: "发展人~", businessLicence: "营业执照~", unitNumber: "单位税号~",
				spkgSn: "协议编号~", cust_level: "客户级别~"
			},
			_form: {
				thinkCust: '意向客户~',
				switchProvince: '请选择省~',
				oldCustLevel: '原客户等级~',
				newCustLevel: '新客户等级~',
				deviceType: '设备类型~',
				deviceModel: '设备型号~',
				storeCount: '库存数量~',
				buyWay: '购买方式~',
				buyCount: '购买数量~',
				titleBatchBuyMaterial: '器材信息~',
				titleAcctRefund: '退款业务处理~',
				refundTotal: '退款金额~',
				rechargeWay: '充值方式~',
				byTicket: '凭票据~',
				rechargeCount: '充值金额~',
				tipRechargeCountMustBeGreaterThanZero: '金额需要大于0!~',
				province:'省~',
				provinceEmptyText:'请选择省~'
			},
			acct: {
				_title: "账目信息~",
				columns: ["账目名称~","卡号~","余额~","往月欠费~", "本月费用~","实时费用~","实时余额~","可转余额~","可退余额~","冻结余额~"]
			},
			change:{
				_title: '异动信息~',
				columns: ["属性~","修改前~","修改后~","修改日期~"]
			},
			acctTabs: {
				detail: {
					_title: '明细~',
					columns: ["资金类型~","余额~"]
				},
				adjust: {
					_title: '调账~',
					columns: ["调账金额~","操作时间~","调账原因~","备注~"]
				},
				changes: {
					_title: '异动~',
					columns: ["业务名称~","资金类型~","变更类型~","变更前~","变更金额~","变更后~","备注~","操作时间~"]
				}
			}
		},
		// 用户面板
		user: {
			base:{
				type: '用户类型~', name: '用户名称~', status: '状态~', statusTime: '状态时间~',
				stbId: '机顶盒~', cardId: '智能卡~', modem: 'Modem号~', createTime: '创建时间~',
				loginName: '账号~', terminal: '终端类型~', deviceModel: '设备型号~', buyWay: '购买方式~',
				stopDate: '预报停时间~', stopType: '催费类型~', protocolDate: '协议日期~', 
				str4: 'IP地址~',str6: 'IP收费数量~'
			},
			_form: {
				taskBackFill: '施工回填~',
				deviceCode: '设备编码~',
				feeName: '费用名称~',
				feeAmount: '收费金额$~',
				protocolInfo: '协议信息~',
				openAmount: '开户数量~',
				manualOpen: '手动开户~',
				addToOpenUserGrid: '添加至暂存表~',
				titleOpenUserGrid: '用户暂存库~',
				titleSwitchProd: '第一步：选择产品~',
				prodName: '产品名称~',
				prodDesc: '产品描述~',
				titleDetemineUser: '第二步：确定订购用户~',
				switchUsers: '选用户~',
				titleOrderInfo: '第三步：订购信息~',
				prodTariff: '产品资费~',
				prodOrderMonths: '订购月数~',
				prodStartDate: '开始计费日~',
				prodExpDate: '结束计费日~',
				lastOrderExpDate: '上期订购结束日~',
				titleOrderFee: '产品费~',
				shouldPay: '应收~',
				addOrderFee: '新增订购~',
				transferPay: '转移支付~',
				maxUserCount: '最大用户数~',
				titleDispatchUser: '分配用户~',
				terminalInfo: '终端信息~',
				addToSelected: '加入已选~',
				moveToOptional: '移至可选~',
				stopTime: '报停时间~',
				stopFee: '报停费用~'
			},
			list: {
				_title: '用户信息~',
				tools: ["查询~"]
			},
			prod: {
				base: {
					_title: "用户产品~",
					columns: ["订购编号~","产品名称~","所属套餐~","当前资费~","生效日期~" ,"失效日期~","状态~","状态变更时间~","订购时间~" ,"订购月数~","创建流水号~"]
				},
				pkg: {
					_title: '客户套餐~',
					columns: ["订购编号~","产品名称~","当前资费~","状态~","生效日期~" ,"失效日期~","产品类型~","订购时间~"]
				},
				tools: ["默认订单~","历史订单~"]
			},
			userDetail: {
				tabs: ["详细信息~","异动信息~"],
				detail: ["用户类型~","用户名~","设备型号~","购买方式~","状态~","状态时间~","预报停时间~","创建时间~","催费类型~","在网协议期~","账号~",
				         "终端类型~","账号~","终端类型~","IP信息~","IP收费数~"],
				change: ["业务~","属性~","修改前~","修改后~","修改日期~","操作员~"]
			},
			prodDetail: {
				tabs: ["订单金额明细~","异动信息~"],
				detail: ["编号~","资金类型~","转入产品~","转入类型~","转入金额~","转出产品~","转出类型~","转出金额~","流水号~" ]
			}
		},
		//缴费记录
		pay: {
			payfee: {
				_title: "预存费用~",
				columns: ["流水号~","业务名称~","账户类型~","账目名称~","用户类型~", "用户名~","设备编号~","状态~","金额~","缴费前预计到期日~",
				        "缴费后预计到期日~","打印状态~","付款方式~","受理日期~","账务日期~","受理人~","受理部门~","发票~","出票方式~", "发票类型~"]
			},
			busifee: {
				_title: "业务费用~",
				columns: ["流水号~","费用名称~","设备类型~","设备编号~","状态~","打印状态~",
				          "应付~","实付~","付款方式~","受理日期~","受理人~","受理部门~","发票~","出票方式~","发票类型~","购买数量~","设备型号~","备注~"]
			},
			detail: {
				_title: '支付记录~',
				columns: ["支付编号~","美元~","柬元~","汇率~","柬元抹零~","有效~","付款方式~",
				          "付款人~","业务流水号~","票据编号~","出票方式~","受理日期~","受理人~","受理部门~"]
			},
			feePayDetail: {
				_title: '费用明细~',
				columns: ['费用项目~', '金额~']
				
			}
		},
		// 单据信息
		doc: {
			invoice: {
				_title: '发票~',
				columns: ["发票号码~","发票代码~","金额~","打印时间~","出票方式~","发票类型~","使用状态~","结存状态~","操作员~","费用生成时间~"]
			},
			task: {
				_title: '施工单~',
				columns: ["工单类型~","工单状态~","预约时间~","完成时间~","创建时间~"]
			},
			busi: {
				_title: '业务受理单~',
				columns: ["操作员~","最后打印~","业务名称~"]
			}
		},
		//受理记录
		doneCode: {
			_title: '受理记录~',
			columns: ["流水号~","业务名称~","状态~","受理日期~","操作员~","受理部门~",
			         "可回退~","可忽略~","实缴金额~","扩展业务信息~","备注~"]
		},
		//指令
		cmd: {
			dtt: {
				_title: "DTT指令信息~",
				columns: ['业务流水号~','机顶盒号~','智能卡号~','控制字~','节目名称~',
				       '指令类型~','结果标记~','生成时间~','发送时间~','CA回传时间~','错误信息~','授权结束日期~']
			},
			ott: {
				_title: "OTT指令信息~",
				columns: ['指令编号~','业务流水号~','指令类型~','机顶盒号~','智能卡号~','MAC~','是否成功~','错误信息~','发送时间~']
			},
			band: {
				_title: 'BAND指令信息~',
				columns: ['指令编号~','业务流水号~','指令类型~','机顶盒号~','Modem号~','是否成功~','错误信息~','发送时间~']
			}
		},
		// 账单
		bill: {
			list: {
				_title: '账单信息~',
				columns: ['账期~','来源~','智能卡号~','流水~','出账时间~','账目名称~',
				      '资费名称~','状态~','出账金额~','欠费金额~','操作~'],
				tbar: ["智能卡~","流水号~","欠费账单~","全部账单~"]
			},
			acctitemInvalid: {
				_title: '账目作废信息~',
				columns: ["账目名称~","资金类型~","作废金额~"]
			}
			
		}
	}, // main end line...
	// 首页工具栏业务
	tools: {
		
		countySwitch:{//分公司切换
			titleSelectDept:'选择部门~',
			confirmSwitchDept:'确定切换部门吗~'
		},
		ad: {//公告
			_title: '公告信息~',
			tplPublishTme:'发布于~',
			columns: ['公告主题~','发布人~','生效时间~','失效时间~']
		},
		grxg:{//个人修改
			_title:'个人资料修改~',
			labelNewPwd:'新密码~',
            labelNewPwdConfirm:'确认新密码~',
            labelDefaultSystem:'默认登录系统~'
		},
		queryDevice:{//设备查询
			_title:'设备查询~',
			titleDevInfo:'设备信息~',
			labelDevNo:'设备序列号~',
			labelDevType:'设备类型~',
			labelDevModel:'设备型号~',
			labelModelName:'型号名称~',
			labelDevName:'设备名称~',
			labelDevCode:'设备编号~',
			labelDeptName:'所在仓库~',
			labelCustNo:'客户编号~',
			labelCustName:'客户姓名~',
			labelDevStatus:'设备状态~',
			labelDepStatus:'库存状态~',
			labelTranStatus:'流转状态~',
			labelOwnership:'产    权~',
			
			labelCardNo:'配对卡编号~',
            labelCardModel:'配对卡型号~',
            labelModemNo:'配对MODEM编号~',
            labelModemModel:'配对MODEM型号~',
            labelStbNo:'配对机顶盒编号~',
            labelStbModel:'配对机顶盒型号~',
            tipDevNotExists:'查询设备不存在!~'
			
		},
		invoiceQuery:{//发票查询
			_title:'发票查询~',
			titleInvoiceInfo:'发票信息~',
			btnShowInvoiceDetail:'费用明细~',
			btnChangeStatus:'修改状态~',
			btnChangeStatusIdel:'修改为空闲~',
			btnChangeStatusInvalid:'修改为失效~',
			btnChangeStatusUsed:'已被使用~',
			
			confirmChangeStatus:'是否将发票状态修改为~',
			tipInvoiceNotExists:'  发票不存在!~',
			tipInvoiceNotExists2:'查询发票不存在!~',
			statusInvalid:'失效~',
			statusIdel:'空闲~',
			
			cols:['客户名称~', '客户编号~', '业务名称~', '费用名称~', '实际金额~', '操作时间~', '操作员~'],
			
			labelInvoiceId:'发票号码~',
			labelInvoiceId2:'发&nbsp;票&nbsp;号&nbsp;~',
			labelInvoiceCode:'发票代码~',
			labelInvoiceType:'发票类型~',
			labelDeptName:'所在仓库~',
			labelUseStatus:'使用状态~',
			labelMoneyAmount2:'金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额~',
			labelUserOptrName:'开&nbsp;票&nbsp;员&nbsp;~',
			labelOptrName:'领&nbsp;用&nbsp;人&nbsp;~',
			labelCreateTime:'入库时间~',
			labelFinanceStatus:'结存状态~',
			labelCloseTime:'核销时间~',
			labelCheckDeptName:'结账仓库~',
			labelCheckTime:'结账时间~'
			
		},
		CustSearch:{//客户查询(多条件查询)
			_title:'客户查询~',
			tipInputAnyField:'请任填一项进行搜索!~',
			labelCustName:'客户名称~',
			labelStatus:'意向客户~',
			labelAddress:'客户地址~',
			labelLoginName:'账号~'
			
		}
	}
	
}
//各种弹出的提示信息
BCLang.msgBox = {
}
