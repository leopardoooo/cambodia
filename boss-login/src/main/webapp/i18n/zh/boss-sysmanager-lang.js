/**
 * common 放一些常用的，通用的.
 * 剩下的，按照功能划分,一个功能一个子对象.
 * @type 
 */
Sys = {
	common:{
		
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
			confirmSave:'确认保存?'
			
			
		},
		formWin:{
			labelSortNum:'序号',
			labelProvince:'省',
			labelDistrict:'行政区域',
			labelNetType:'网络类型',
			emptyTxtProvince:'请选择省',
			
			
		},
		grid:{
			
		}
	},
	WorkTask:{//工单
		
	}
}