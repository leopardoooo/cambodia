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
			optr:'操作员'
	},
	ExchangeRateManage: {//汇率配置
		title:'汇率配置',
		commons:{
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
	WorkTask:{//工单
		
	}
}