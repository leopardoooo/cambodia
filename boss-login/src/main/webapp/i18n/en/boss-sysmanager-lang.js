/**
 * common 放一些常用的，通用的.
 * 剩下的，按照功能划分,一个功能一个子对象.
 * @type 
 */
Sys = {
	common:{
		status:'status',
			query:'query',
			addNewOne:'add',
			update:'update',
			invalid:'invalid',
			saveBtn:'save',
			cancelBtn:'cancel',
			forbiddenBtn:'forbidden',
			enableBtn:'activate',
			doActionBtn:'do action',
			optr:'optr'
	},
	ExchangeRateManage: {//汇率配置
		title:'Exchange Rate Manage',
		commons:{
			effective_time :'effective time',
			exchange_rate:'exchange rate',
			status:'status',
			query:'query',
			addNewOne:'add',
			update:'update',
			invalid:'invalid',
			saveBtn:'save',
			cancelBtn:'cancel',
			optr:'optr',
			create_time:'create time'
		},
		status:{
			empty:'all',
			ACTIVE:'ACTIVE',
			INVALID:'INVALID'
		},
		msg:{
			successMsg:'success',
			onlyOne:'you should select one record and only one.',
			areYouSure:'are you sure?'
		}
	},
	AddressNodeManage: {//地址节点配置
		panelTitle:'Address Management',//面板主标题
		msg:{//各种提示消息
			actionFailed:'action failed.',
			actionSuccess:'success',
			confirmSave:'Click OK to save these settings?',
			confirmInvalid:'Click OK to invalid',
			confirmDelete:'Click OK to delte',
			confirmActivate:'Click OK to activate',
			cantDelete:'Occupied,can\'t be deleted',
			cantBeInvalided:'it has sub nodes,can\'t be invalided.',
			maxDepthText: 'can\'t add child node.',
			noSingleQuoteAllowed:'single quote is forbidden.'
			
			
		},
		formWin:{
			labelSortNum:'sort num',
			labelProvince:'province',
			labelDistrict:'district',
			labelNetType:'net type',
			emptyTxtProvince:'please select a province',
			emptyTxtBlurQuery:'support blur query',
			btnTxtClose:'close',
			btnTxtSave:'save',
			labelRoadNum:'road number',
			titleNewSaveLevelRoadNum:'add sibling road number',
			labelStreatName:'street name',
			titleNewSaveLevelStreet:'add street',
			labelCityName:'city name',
			titleNewSaveLevelCity:'add sibling node',
			labelShowName:'show name',
			labelParentName:'parent address name',
			
			labelNewAddChild:'add child node',
			labelNewAddBrother:'add sibling node',
			labelAddrTree:'Address tree'
		}
	},
	WorkTask:{//工单
		
	}
}