/**
 * common 放一些常用的，通用的.
 * 剩下的，按照功能划分,一个功能一个子对象.
 * @type 
 */
Sys = {
	common:{
		status:'状态',
			query:'查询',
			addNewOne:'新增',
			update:'修改',
			invalid:'作废',
			saveBtn:'保存',
			cancelBtn:'取消',
			forbiddenBtn:'禁用',
			enableBtn:'启用',
			doActionBtn:'操作',
			optr:'操作员',
			changeBtn:'变更',
			remarkTxt:'备注',
			exitBtn:'备注',
			to:'至',//从 xxx 至 xxxx
			
			statusList:{
			empty:'所有',
			ACTIVE:'正常',
			INVALID:'失效'
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
		  namount: "金额",
			check_time: "结账时间",
			close_time: "核销时间",
			create_time: "入库时间",
			depot_name: "仓 库",
			finance_status: "结存状态",
			invoice_code: "发票代码",
			invoice_id: "发票号码",
			invoice_type: "发票类型",
			optr_name: "所属营业员",
			status: "使用状态",
			use_time: "开票时间"
	},
	QueryInvoice:{//查询发票
			common:{
				titlaQueryInvoick:'发票查询',
				winTitleInvoiceDetail:'发票明细',
				titleInvoiceInfo:'发票信息',
				titleMoneyCountPrefix:'有效总金额',
				queryBtn:'查  询'
				
			},
			msg:{
				emptyTextSelectStore:'请选择仓库'	
			},
			labels:{//表单的label或者表格的header
					amount: "金额",
					check_time: "结账时间",
					close_time: "核销时间",
					create_time: "入库时间",
					depot_name: "仓 库",
					finance_status: "结存状态",
					invoice_code: "发票代码",
					invoice_id: "发票号码",
					invoice_type: "发票类型",
					optr_name: "所属营业员",
					status: "使用状态",
					use_time: "开票时间"
				
			}
			
	},
	InvoiceDetail:{//发票详细信息
		labels:{
			busi: "业务名称",
			create: "操作时间",
			cust: "客户编号",
			fee: "费用名称",
			optr: "操作员",
			real: "实际金额",
			status: "状态",
			invoice_id: "发票号码"
			
		}
	}
	
}