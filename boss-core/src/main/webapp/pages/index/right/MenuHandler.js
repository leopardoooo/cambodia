Ext.ns("MenuHandler");

var LU_MSG = lmsg;
var LU_FM = lbc;//带参数的

/**
 * 判断是否已经查询了客户
 */
hasCust = function() {
	if (!App.getCustId()) {
		Alert(LU_MSG('needCust'));
		return false;
	}
	return true;
}
// 冲正
UnPayBak = function(record) {
	// 回调函数
	function callback(res, opt) {
		Alert(LU_MSG('cancelFeeSuccess'));
		App.main.infoPanel.setReload(true);
		App.main.infoPanel.getPayfeePanel().refresh();
		App.main.infoPanel.getAcctPanel().refresh();
		App.main.infoPanel.getDocPanel().setReload(true);
		App.main.infoPanel.getDoneCodePanel().setReload(true);
	}

	if (record) {
		var params = {};
		params["fee_sn"] = record.get("fee_sn");

		var url = Constant.ROOT_PATH + "/core/x/Pay!saveCancelFee.action";
		if (!Ext.isEmpty(record.get('invoice_id'))
				|| Ext.isEmpty(record.get('invoice_mode') == 'A')) {
			Confirm( LU_FM('msgBox.confirmCancelFeeAndInvaidInvoice',null,[record.get('invoice_id')]),
					this, function() {
						// 调用请求函数,详细参数请看busi-helper.js
						App.sendRequest(url, params, callback);
					});
		} else {
			Confirm(LU_MSG('confirmCancelFee'), this, function() {
						// 调用请求函数,详细参数请看busi-helper.js
						App.sendRequest(url, params, callback);
					});
		}
	} else {
		Alert(LU_MSG('selectRec4CancelFee'));
		return false;
	}
	return false;
}

// UpdateFlag: 回退当天的订单
UnPay = function(record) {
	// 回调函数
	function callback(res, opt) {
		Alert(LU_MSG['cancelFeeSuccess']);
		App.main.infoPanel.setReload(true);
		App.main.infoPanel.getPayfeePanel().refresh();
		App.main.infoPanel.getAcctPanel().refresh();
		App.main.infoPanel.getDocPanel().setReload(true);
		App.main.infoPanel.getDoneCodePanel().setReload(true);
	}

	if (record) {
		var params = {};
		params["order_sn"] = record.get("prod_sn");
		params["cancelFee"] = record.get("real_pay");

		var url = Constant.ROOT_PATH + "/core/x/ProdOrder!cancelTodayOrder.action";
		Confirm( LU_FM('msgBox.confirmUnPayWithParam',null,[params["cancelFee"]/100.0]), this, function() {
			App.sendRequest(url, params, callback);
		});
	} else {
		Alert(LU_MSG('selectRec4CancelFee'));
		return false;
	}
	return false;
}

/**
 * 为MenuHandler添加处理函数
 */
Ext.apply(MenuHandler, {
	// 配置分公司账户
	ConfigBranchCom : function() {
		return {
			width : 500,
			height : 470
		};
	},
	//支付回退  
	CancelPayFee : function(){
		record = App.main.infoPanel.getPayfeePanel().feePayGrid.getSelectionModel().getSelected();
		// 回调函数
		function callback(res, opt) {
			Alert(LU_MSG('cancelPayFeeSuccess'));
			App.main.infoPanel.setReload(true);
			App.main.infoPanel.getPayfeePanel().refresh();
			App.main.infoPanel.getDocPanel().setReload(true);
			App.main.infoPanel.getDoneCodePanel().setReload(true);
			App.getApp().refreshPayInfo();
		}
		
		
		function callbackSave(res, opt) {
			var rec = Ext.decode(res.responseText);
			var saveParams = {invoiceIds:rec,paySn:record.get("pay_sn")};
			var txt = LU_FM('msgBox.confirmCancelPayFeeWithNoParam');
			if(rec.length>0){
				txt = LU_FM('msgBox.confirmCancelPayFeeWithParam',null,[saveParams["invoiceIds"]]);
			}
			Confirm( txt, this, function() {
				App.sendRequest(Constant.ROOT_PATH + "/core/x/Pay!canclePay.action", saveParams, callback);
			});
		}
	
		if (record) {
			var params = {};
			params["paySn"] = record.get("pay_sn");
			var url = Constant.ROOT_PATH + "/core/x/Pay!queryPayToCancel.action";
			App.sendRequest(url, params, callbackSave);
		} else {
			Alert(LU_MSG('selectRecCancelPayFee'));
			return false;
		}
		return false;
	},
	// --------------------客户信息------------------------------------------------
	// 开户
	NewCust : function() {
		return {
			width : 700,
			height : 510
		};
	},
	/**
	 * 返销户.
	 */
	restoeCust:function(){
		if(!hasCust()){
			return false;
		}
		Confirm(lmsg('confirmRestoreCust'),this,function(){
			Ext.getBody().mask(lmsg('waitMsg'));
			
			var all = {custId:App.getApp().getCustId()};
			//公有的参数
			var common = App.getValues();
			all[CoreConstant.JSON_PARAMS] = Ext.encode(common);
			
			//业务提交提示框
			tip = Show();
			Ext.Ajax.request({
				scope : this,
				url : Constant.ROOT_PATH + "/core/x/Cust!restoeCust.action",
				params : all,
				success : function(res,opt){
					tip.hide();
					tip = null;
					var result = Ext.decode(res.responseText);
					if (result.success == true) {
						Alert(lmsg('restoreCustSuccess'));
						Ext.getBody().unmask();
						App.search.doRefreshCust(result.custInfo.cust);
					}else{
						Alert(lmsg('restoreCustFailed'));
					}
				}
			})
			
		});
		return false;
	},
	// 客户销户
	LogoffCust : function() {
		if (!hasCust())
			return false;
		var count = App.main.infoPanel.getUserPanel().userGrid.getStore()
				.getCount();
		if (count != 0) {
			Alert(lmsg('needLogOffUser'));
			return false;
		}
//		var custDeviceGrid = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid;
//		if (custDeviceGrid.GDDeviceArray.length > 0) {
//			Alert(lmsg('recycleGdDevice'));
//			return false;
//		}
		// alert(custDeviceGrid.CustDeviceArray.length);
		// if(custDeviceGrid.CustDeviceArray.length > 0){
		// var o = {};
		// Confirm("是否回收自购的设备？",null,function(){
		// App.getApp().menu.hideBusiWin();
		// },function(){
		// o = {width: 500 , height: 450};
		// });
		// return o;
		// }else{
		// }
		return {
			width : 500,
			height : 450
		};
	},
	// 过户
	TransferCust : function() {
		if (!hasCust())
			return false;
		return {
			width : 500,
			height : 470
		};
	},
	CustTransfer : function() {//客户迁移
		if (!hasCust()){
			return false;
		}
		//必须在迁入客户所属区域营业厅办理 
//		var addr_id = App.getData().custFullInfo.cust.addr_id;
//		var arrArray = App.getData().deptAddress;
		/*
		if(!(App.getData().custFullInfo.cust.addr_id in App.getData().deptAddress)){
			Alert('当前客户无法在本营业厅执行客户迁移操作!');  //</br>请到客户开户营业厅办理!
			return false;
		}
		*/
		return {
			width : 500,
			height : 470
		};
	},
	// 客户拆迁
	RelocateCust : function() {
		if (!hasCust())
			return false;

		if (App.data.custFullInfo.cust.status == 'RELOCATE') {
			Alert(lmsg('custIsRelocated'))
			return false;
		}

		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().refreshPanel('1119');// 1119：客户拆迁
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/Cust!relocateCust.action";

		var params = {};
		params['custId'] = App.getApp().getCustId();
		Confirm(lmsg('confirmRelocateCust'), this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, params, callback);
				});

		return false;
	},
	// 加入单位
	JoinUnit : function() {
		if (!hasCust())
			return false;
		if (App.data.custFullInfo.cust.cust_type == "UNIT") {
			Alert(lmsg('notAllowedJoinUnit'));
			return false;
		}
		return {
			width : 520,
			height : 400
		};
	},

	// 退出单位
	QuitUnit : function() {
		if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().refreshPanel('1006');// 1006：退出单位
			}
		}

		var url = Constant.ROOT_PATH + "/core/x/Cust!quitUnit.action";
		var params = {};
		params['custId'] = App.getApp().getCustId();
		params['unitId'] = App.getCust().unit_id;

		Confirm(lmsg('confirmQuitUnit'), this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, params, callback);
				});

		return false;
	},
	BankStop: function(){
		if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().main.infoPanel.getCustPanel().custInfoPanel.remoteRefresh();
				App.getApp().main.infoPanel.getCustPanel().custDetailTab.propChangeGrid.remoteRefresh();
			}
		}

		var url = Constant.ROOT_PATH + "/core/x/Bank!bankStop.action";
		var params = {};
		params['custId'] = App.getApp().getCustId();
		Confirm(lmsg('confirmBankStop'), this, function() {
			// 调用请求函数,详细参数请看busi-helper.js
			App.sendRequest(url, params, callback);
		});

		return false;
	},
	EditBankPay:function(){
		if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
			}
		}

		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
		if (record) {
			App.getApp().selectRelativeUser([record.get('user_id')]);
		}
		var url = Constant.ROOT_PATH + "/core/x/User!changeCprodBank.action";
		var params = {};
		params['prodSn'] = record.get('prod_sn');
		
		var txt = lmsg('confirmEnableBankPay');
		if(record.get('is_bank_pay') == 'T'){
			txt = lmsg('confirmDisableBankPay');
		}
		Confirm(lmsg('confirmEditBankPay')+txt, this, function() {
			// 调用请求函数,详细参数请看busi-helper.js
			App.sendRequest(url, params, callback);
		});

		return false;
	},
	BankResume: function(){
		if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().main.infoPanel.getCustPanel().custInfoPanel.remoteRefresh();
				App.getApp().main.infoPanel.getCustPanel().custDetailTab.propChangeGrid.remoteRefresh();
			}
		}

		var url = Constant.ROOT_PATH + "/core/x/Bank!bankResume.action";
		var params = {};
		params['custId'] = App.getApp().getCustId();
		Confirm(lmsg('confirmBankResume'), this, function() {
			// 调用请求函数,详细参数请看busi-helper.js
			App.sendRequest(url, params, callback);
		});

		return false;
	},
	// 修改客户信息处理函数
	EditCust : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 700,
			height : 510
		};
	},
	EditCustClass: function(){//修改优惠类型
		if (!hasCust()) {
			return false;
		}
		return {
			width : 590,
			height : 370
		};
	},
	BatchModifyUserName:function(){//批量修改用户名
		if (!hasCust()) {
			return false;
		}
		return {
			width : 590,
			height : 180
		};
	},
	ResetUserPassword:function(){//重置密码
		if (!hasCust()) {
			return false;
		}
		return {
			width : 590,
			height : 370
		};
	},
	BatchNewCust : function() {
		return {
			width : 540,
			height : 300
		};
	},
	RenewCust : function() {
		if(!hasCust())return false;
		var url = Constant.ROOT_PATH + "/core/x/Cust!renewCust.action";
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getCust().status = result.simple_obj;
				App.getApp().refreshPanel('1220');
			}
		}
		Confirm(lmsg('confirmRenewCust'), this, function() {
					App.sendRequest(url, null, callback);
				});
		return false;
	},
	SwitchDevice:function(){
		if (!hasCust()){
			return false;
		}
		var continueFlag = true;
		var custDeviceGrid = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
		var userGrid = App.getApp().main.infoPanel.userPanel.userGrid;
		var record = userGrid.getSelectionModel().getSelected();
		if(record.get('modem_mac')){//TODO 如果有猫,暂时不给换.
			Alert(lmsg('custHasUnSuitableDev'));
			return false;
		}
		// 关闭过滤窗口
		if (Ext.getCmp('filterWinID')) {
			Ext.getCmp('filterWinID').close();
			var index = userGrid.getStore().find('device_code',
					record.get('device_code'));
			userGrid.getSelectionModel().selectRow(index);
		}
		if(continueFlag){
			return {width : 650,height : 310};
		}else{
			Alert(lmsg('custCantExchangeDev'));
			return false;
		}
	},
	// 更换设备
	ExchangeDevice : function() {
		if (!hasCust())
			return false;
		/*var custDeviceGrid = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
		var record = custDeviceGrid.getSelectionModel().getSelected();
		
		 * if(records.length == 0){ Alert('请选择更换的设备'); return false; }else
		 * if(records.length>1){ Alert('只能对一个设备进行更换，请选择一个设备'); return false; }
		 * if(records[0].get('status')!='USE'){
		 * Alert('选择的设备状态为未使用的设备，请选择使用的设备'); return false; }
		 * if(records[0].get('loss_reg') === 'T'){ Alert('挂失的设备不能更换！'); return
		 * false; }
		 

		// 关闭过滤窗口
		if (Ext.getCmp('filterWinID')) {
			Ext.getCmp('filterWinID').close();
			var index = custDeviceGrid.getStore().find('device_code',
					record.get('device_code'));
			custDeviceGrid.getSelectionModel().selectRow(index);
		}*/

		return {
			width : 550,
			height : 550
		};
	},
	// 购买终端
	BuyDevice : function() {
		if (!hasCust())
			return false
		return {
			width : 640,
			height : 500
		};
	},
	//购买保修
	BuyReplacover : function() {
		if (!hasCust())
			return false
		return {
			width : 530,
			height : 260
		};
	},
	// 购买配件
	BuyMaterial : function() {
		if (!hasCust())
			return false
		return {
			width : 500,
			height : 350
		};
	},
	//批量购买配件
	BacthBuyMaterial:function(){
		if (!hasCust())
			return false
		return {
			width : 640,
			height : 500
		};
	},
	// 挂失
	RegLoss : function() {
		if (!hasCust())
			return false;
		var record = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
				.getSelectionModel().getSelected();

		// if(Ext.isEmpty(record)){
		// Alert('请选择要挂失的设备！');
		// return false;
		// }
		// if(record.get('loss_reg') === 'T'){
		// Alert('请选择没有挂失的设备！');
		// return false;
		// }
		// if(record.get('status') !== 'IDLE' && record.get('status') !==
		// 'REQSTOP'){
		// Alert('请选择状态为报停或空闲的设备！');
		// return false;
		// }
		var url = Constant.ROOT_PATH + "/core/x/Cust!saveRegLossDevcie.action";
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert(lmsg('regLossSuccess'), function() {
							App.main.infoPanel.getCustPanel().custDeviceGrid
									.remoteRefresh();
						}, this);
			}
		}
		var params = {};
		params['deviceId'] = record.get('device_id');
		Confirm(lmsg('confirmRegLoss'), this, function() {
					App.sendRequest(url, params, callback);
				});
		return false;
	},
	// 取消挂失
	CancelLoss : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
				.getSelectionModel().getSelections();

		if (records.length === 0) {
			Alert(lmsg('selectDev2RegLoss'));
			return false;
		}
		if (records[0].get('loss_reg') === 'F') {
			Alert(lmsg('selectDevIsRegLossAlready'));
			return false;
		}
		var url = Constant.ROOT_PATH
				+ "/core/x/Cust!saveCancelLossDevcie.action";
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert(lmsg('unRegLossSuccess'), function() {
							App.main.infoPanel.getCustPanel().custDeviceGrid
									.remoteRefresh();
						}, this);
			}
		}
		var params = {};
		params['deviceId'] = records[0].get('device_id');
		Confirm(lmsg('confirmUnRegLoss'), this, function() {
					App.sendRequest(url, params, callback);
				});
		return false;
	},
	// 打印标记
	PrintStatus : function() {
		if (!hasCust())
			return false;
		var grid = App.getFeeGridWithTargetElement();	
		var record = grid.getSelectionModel().getSelected();
		var url = Constant.ROOT_PATH + "/core/x/Pay!savePrintStatus.action";
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert(lmsg('statusNotPrintStatusSuccess'), function() {
					grid.remoteRefresh();
				}, this);
			}
		}
		var params = {};
		params['fee_sn'] = record.get('fee_sn');
		Confirm(lmsg('confirmNotPrintStatus'), this, function() {
			App.sendRequest(url, params, callback);
		});
		return false;
	},
	// 取消打印标记
	CancelPrintStatus : function() {
		if (!hasCust())
			return false;
		var grid = App.getFeeGridWithTargetElement();	
		var record = grid.getSelectionModel().getSelected();
		var url = Constant.ROOT_PATH + "/core/x/Pay!saveCancelPrintStatus.action";
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert(lmsg('statusPrintStatusSuccess'), function() {
							grid.remoteRefresh();
						}, this);
			}
		}
		var params = {};
		params['fee_sn'] = record.get('fee_sn');
		Confirm(lmsg('confirmPrintStatus'), this, function() {
					App.sendRequest(url, params, callback);
				});
		return false;
	},
	// 销售设备
	SaleDevice : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
				.getSelectionModel().getSelections();
		if (records.length === 0) {
			Alert(lmsg('selectDev2Sale'));
			return false;
		}
		if (records[0].get('ownership') == 'CUST') {
			Alert(lmsg('cantSaleCosOwerIsCust'));
			return false;
		}
		return {
			width : 540,
			height : 330
		};
	},
	// 修改购买方式
	ChangeDeviceType : function() {
		var record = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected();
		var btn = {text:lmsg('modifyBuyType'),attrs:App.data.currentResource};
		var cfg = {width : 560,height : 460}; 
		if(record.get('depot_id') == App.data.optr['dept_id']){
			App.menu.bigWindow.show(btn,cfg);
		}else{
			var msg = LU_FM('msgBox.need2SwitchCountyId',null,[record.get('depot_name')]);
			Ext.Msg.show({
                title : langUtils.bc('common.tipSimple'),
                msg : msg,
                width:300,
                buttons: {cancel : "<font size='2'><b>" + langUtils.bc('common.returnTxt') + "</b></font>"},
                scope : this
            });
            
			return false;
		}
//		return {
//			width : 540,
//			height : 330
//		};
	},
	ChangeOwnership : function() {
		if (!hasCust())
			return false;

		var record = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
				.getSelectionModel().getSelected();

		var ownership = record.get('ownership'), msg;
		if (ownership == CoreConstant.OWNERSHIP_GD) {
			msg = lmsg('changeOwner2Cust');
		} else {
			msg = lmsg('changeOwner2Gd');
		}

		Confirm(msg, this, function() {
			App.sendRequest(Constant.ROOT_PATH
							+ "/core/x/Cust!changeOwnership.action", {
						deviceId : record.get('device_id')
					}, function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (data['success'] == true) {
							Alert(lmsg('cangeOwnerSuccess'));
							App
									.getApp()
									.refreshPanel(App.getApp().getData().currentResource.busicode);
						}
					});
		});

		return false;
	},
	UserDeviceReclaim:function(){
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid.getSelections();
		if (userRecords.length == 0) {
			Alert(lmsg("needUser"));
			return false;
		}
		//宽带也需要回收，取消限制
		for (i = 0; i < userRecords.length; i++) {
			if (Ext.isEmpty(userRecords[i].get("card_id")) && Ext.isEmpty(userRecords[i].get("stb_id")) 
			&& Ext.isEmpty(userRecords[i].get("modem_mac"))) {
				Alert(lmsg("selectDev2Recycle"));
				return false;
			}
		}	
		return {
			width: 600,
			height: 400
		};	
	
	},
	// 回收设备
	ReclaimDevice : function() {
		if (!hasCust())
			return false;
		var deviceids = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid
				.getSelectionModel().getSelections();
		if (deviceids.length == 0) {
			Alert(lmsg('selectDev2Recycle'));
			return false;
		}
		if (deviceids[0].get("status") == "USE") {
			Alert(lmsg('devCantRecycleStillInUse'));
			return false;
		};
		return {
			width : 540,
			height : 330
		};
	},
	ChangeNonresCust: function(){
		if(!hasCust()) return false;
		if (App.data.custFullInfo.cust.cust_type != 'RESIDENT') {
			Alert(lmsg('onlyResidentCanNonResiCust'))
			return false;
		}
//		if(App.main.infoPanel.getCustPanel().packageGrid.getStore().getCount() > 0){
//			Alert('请退订客户套餐后再转集团')
//			return false;
//		}
		return {
			width:520,
			height:500
		};
	},
	// 预存费用冲正
	DestineUnPay : function() {
		var record = App.main.infoPanel.getPayfeePanel().acctFeeGrid
				.getSelectionModel().getSelected();
		return UnPay(record);
	},
	// 业务费用冲正
	BusinessUnPay : function() {
		var record = App.main.infoPanel.getPayfeePanel().busiFeeGrid
				.getSelectionModel().getSelected();
		return UnPay(record);
	},
	// 退押金
	DepositUnPay : function() {
		if (!hasCust())
			return false;
		var records = App.main.infoPanel.getPayfeePanel().busiFeeGrid
				.getSelectionModel().getSelected();
		var url = Constant.ROOT_PATH + "/core/x/Pay!saveDepositUnPay.action";
		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert(lmsg('depositUnPaySuccess'), function() {
							App.main.infoPanel.getPayfeePanel().busiFeeGrid
									.remoteRefresh();
						}, this);
			}
		}
		if (records.get('status') !== 'PAY') {
			Alert(lmsg('unPayed'));
			return false;
		}
		if (records.get('deposit') == 'F') {
			Alert(lmsg('notDeposit'));
			return false;
		}
		var params = {};
		params['feeSn'] = records.get('fee_sn');
		Confirm(lmsg('confirmDepositUnPay'), this, function() {
					App.sendRequest(url, params, callback);
				});
		return false;
	},
	BatchJoinUnit : function() {
		if (!hasCust())
			return false;
		return {
			width : 700,
			height : 450
		};
	},
	EditPay : function() {
		return {
			width : 750,
			height : 400
		};
	},
	EditDoneRemark: function(){
		return {
			width : 500,
			height : 400
		};
	},
	EditAdjustReason: function(){
		return {
			width : 400,
			height : 300
		};
	},
	// ----------------------用户信息---------------------------------------------------
	// 用户开户
	NewUser : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 620,
			height : 480
		};
	},
	//派单开户
	NewUserBatch: function(){
		if (!hasCust()) {
			return false;
		}
		return {
			width: 650,
			height: 550
		}
	},
	// 用户销户
	SingleLogoffUser : function() {
		if (!hasCust())
			return false;
		var userGrid = App.main.infoPanel.getUserPanel().userGrid;
		var userRecords = userGrid.getSelections();
		if (userRecords.length == 0) {
			Alert(lmsg('needUser'));
			return false;
		}
		if(userRecords.length>1){
			Alert(lmsg('needOneUser'));
			return false;
		}
		
		if(userRecords[0].get("status") == 'UNTUCK' || userRecords[0].get("status") == 'REQSTOP' 
				|| userRecords[0].get("status") == 'INSTALL' ){
			Alert(lmsg('CancelTheAccountUserStatus'));
			return false;
		}
		if(userRecords[0].get("str10")== 'PRESENT' && 
			(!Ext.isEmpty(userRecords[0].get("stb_id"))||!Ext.isEmpty(userRecords[0].get("card_id"))
					|| !Ext.isEmpty(userRecords[0].get("modem_mac")))){
			Alert(lmsg('CancelTheAccountDismantleDevice'));
			return false;
		}
		
		return {
				width : 650,
				height : 550
			};
	},
	BatchLogoffUser: function(){
		if(!hasCust()) return false;
		return {
			width:440,
			height:450
		}
	},
	// 用户销户
	LogoffUser : function() {
		if (!hasCust())
			return false;
		var userGrid = App.main.infoPanel.getUserPanel().userGrid;
		var userRecords = userGrid.getSelections();

		var len = userRecords.length;// 选中记录

		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		} else {
			var dtv = false;// 如果选中的用户中不存在数字电视
			for (i = 0; i < len; i++) {
				if (userRecords[i].get("status") != "ACTIVE" && userRecords[i].get("status") != "INSTALL" ) {
					Alert(lmsg('userNotActive'));
					return false;
				}
				if (userRecords[i].get("user_type") == 'OTT') {
					dtv = true;
				}
			}
			if (dtv) {// 存在数字电视
				var userIds = userGrid.getSelectedUserIds().join(',') + ',';
				var store = userGrid.getStore();
				// 选中用户数不等于总数
				if (len != store.getCount()) {
					var flag = true;// 没选中的用户中没有主终端
					var flag2 = true;// 选中的用户没有主终端
					var dtvAmount = 0;
					for (var i = 0; i < store.getCount(); i++) {
						var record = store.getAt(i);
						if (record.get('user_type') != 'BAND' && record.get('user_type') != 'DTT') {
							if (record.get('terminal_type') == 'ZZD') {
								// 不在选中的用户中
								if (userIds
										.indexOf(record.get('user_id') + ",") == -1) {
									flag = false;// 没选中的用户中有主终端
									break;
								} else {
									flag2 = false;// 选中的用户中有主终端
								}
							}

							// 数字电视数
							dtvAmount = dtvAmount + 1;
						}
					}
					// 选中的用户中有主终端,没选中的用户中没有主终端,有未选中的数字用户
					if (!flag2 && flag && dtvAmount != len) {
						Alert(lmsg('cantLogOffZzd'));
						return false;
					}
					
				}
			}
			
			var prodMap = App.main.infoPanel.getUserPanel().prodGrid.prodMap;
			var acctGrid = App.getApp().main.infoPanel.getAcctPanel().acctGrid;
			var acctMap = {};
			for(var loopFlag =0;loopFlag<acctGrid.store.getCount();loopFlag++){
				var acctData = acctGrid.store.getAt(loopFlag).data;
				var uid = acctData.user_id;
				if(!uid || !acctData.acctitems){
					continue;
				}
				for(var idx = 0;idx<acctData.acctitems.length;idx++){
					var item = acctData.acctitems[idx];
					if(item.is_base != 'T'){
						continue;
					}
					acctMap[uid] = item;
				}
			}
			
			for(var ii = 0;ii<userRecords.length;ii++){
				var uid = userRecords[ii].get('user_id');
				
				var item = acctMap[uid];
				if(item && item.real_balance <0){
					Alert(lmsg('cantLogOffCosBaseProdOweFee'));
					return false;
				}
				
				var array = prodMap[uid];
				if(!array || array.length ==0){
					continue;
				}
				for(var idx = 0;idx<array.length;idx++){
					var prod = array[idx];
					if(prod.is_base == 'T' && prod.status != 'ACTIVE'){
						Alert(lmsg('cantLogOffCosBaseProdNotActive'));
						return false;
					}
				}
			}

			return {
				width : 650,
				height : 450
			};

		}
	},
	AddIpUser : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 550,
			height : 360
		};
	},
	PayIpUserFee : function() {
		if (!hasCust()) {
			return false;
		}	
		var record = App.main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		var prodMap = App.main.infoPanel.getUserPanel().prodGrid.prodMap;
		var prid = prodMap[record.get('user_id')];
		if(null==prid || prid.length==0){
			Alert(lmsg('cantPayIpUserFee'));
			return false;
		}
		return {
			width : 550,
			height : 360
		};
	},
	ChangeCust : function(){
	  	if(!hasCust()) return false;
	  	
	  	return {width: 540 , height: 600};
  	},
	//免费终端（管理员）
	FreeUserDevice : function(){
		return {
			width : 570,
			height : 400
		};
	},
	//免费终端通用
	FreeUserGeneral : function(){
		var store = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		
		var num = 0;
		store.each(function(item){
			if(item.get("terminal_type") == "FZD" && item.get("str19") == "T"){
				num++;
			}
		});
		
		if(num >= 2 && record.get('str19') != 'T'){
			Alert(lmsg('freeUsersOver2'));
			return false;
		}
		return {
			width : 570,
			height : 400
		};
	},
	// 排斥资源
	RejectRes : function() {
		return {
			width : 540,
			height : 400
		};
	},
	// 模拟转数字
	AtvToDtv : function() {
		return {
			width : 540,
			height : 500
		};
	},
	// 开通双向
	OpenDuplex : function() {
		if (!hasCust())
			return false;
		var records = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();

		var flag = true;
		if (records[0].get('stb_id')) {
			Ext.Ajax.request({
						url : root
								+ '/commons/x/QueryDevice!queryStbModel.action',
						async : false,// 同步请求
						params : {
							stbId : records[0].get('stb_id')
						},
						success : function(res, opt) {
							var rec = Ext.decode(res.responseText);
							if (rec.interactive_type == 'SINGLE') {
								Alert(lmsg('singleInteractiveDevCantOpenDuplex'));
								flag = false;
							}
						}
					})
		} else {
			Alert(lmsg('custMustHaveDuplexDev'));
			return false;
		}
		if (!flag) {
			return false;
		}

		return {
			width : 540,
			height : 530
		};
	},
	// 取消双向
	CancelDuplex : function() {
		if (!hasCust())
			return false;
		var userPanel = App.main.infoPanel.getUserPanel();
		var userRecord = userPanel.userGrid.getSelectionModel().getSelected();
		var prodStore = userPanel.prodGrid.getStore();
		var flag = false;// 是否有ITV的产品
		prodStore.each(function(record) {
					if (record.get('serv_id') == 'ITV') {
						flag = true;
						return false;
					}
				});
		if (flag === true) {
			Alert(lmsg('needCancelProgramFirst'));
			return false;
		}

		Confirm(lmsg('confirmCancelDuplex'), this, function() {
			App.sendRequest(Constant.ROOT_PATH
							+ "/core/x/User!saveCancelOpenInteractive.action",
					null, function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (data['success'] === true) {
							Alert(lmsg('confirmCancelDuplex'));
							App
									.getApp()
									.refreshPanel(App.getApp().getData().currentResource.busicode);
						}
					});
		});
		return false;
	},
	// 修改接入方式
	EditNetType : function() {
		return {
			width : 520,
			height : 200
		};
	},
	// 修改宽带密码
	EditNetPassword : function() {
		return {
			width : 550,
			height : 200
		};
	},
	// 临时授权
	OpenTemp : function() {
		var prodStore = App.main.infoPanel.getUserPanel().prodGrid.getStore();
		if (prodStore.getCount() == 0) {
			Alert(lmsg('userHasNoBaseProd'));
			return false;
		}
		for (var i = 0; i < prodStore.getCount(); i++) {
			if (prodStore.getAt(i).get('prod_type') == 'BASE'
					&& prodStore.getAt(i).get('is_base') == 'T') {
				if (prodStore.getAt(i).get('status') == 'OWESTOP') {
					var acctId = prodStore.getAt(i).get('acct_id');
					var prodId = prodStore.getAt(i).get('prod_id');

					var acctStore = App.main.infoPanel.getAcctPanel().acctGrid
							.getStore();

					for (var k = 0; k < acctStore.getCount(); k++) {
						var rec = acctStore.getAt(k);
						if (rec.get('acct_id') == acctId) {
							var acctItems = rec.get('acctitems');
							for (var j = 0; j < acctItems.length; j++) {
								if (acctItems[j]['acctitem_id'] == prodId) {
									if (acctItems[j]['real_balance'] > 1) {
										Alert(lmsg('baseProdRechargedWait30Seconds'));
										return false;
									}
									break;
								}
							}
						}
					}
				} else {
					Alert(LU_FM('msgBox.usersNotOweFeeStop'),null,[prodStore.getAt(i).get('prod_name')]);
					return false;
				}
				
			}
		}
		
		Confirm(lmsg('confirmOpenTemp'), this, function() {
					App.sendRequest(Constant.ROOT_PATH
									+ "/core/x/User!saveOpenTemp.action", null,
							function(res, opt) {
								var data = Ext.decode(res.responseText);
								if (data == true) {
									Alert(lmsg('openTempSuccess'));
									App.getApp().main.infoPanel.getUserPanel().userGrid
											.remoteRefresh();
								}
							});
				});
		return false;
	},
	/**
	 * 批量临时授权.
	 */
	OpenTempBatch:function(){
		var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
		var users = userGrid.getSelections();
		if(users.length==0){
			Alert(lmsg('noUserSelected'));
			return false;
		}
		//验证过滤用户类型
		var flag = false;
		for(var index =0;index<users.length;index++){
			var data = users[index].data;
			var userId = data.user_id;
			if(data['user_type'] == 'ATV' || (data['user_type'] == 'DTV' && data['terminal_type'] !='ZZD' && Ext.isEmpty(data['card_id'])) ){
				userGrid.selModel.deselectRow(userGrid.store.find('user_id',userId));
			}
		}
		
		users = userGrid.getSelections();
		if(users.length==0){
			Alert(lmsg('noSelectedUserCanOpenTemp'));
			return false;
		}
		
		var store = userGrid.getStore();
		var flag = false;
		store.each(function(record){
			//主机非正常状态,超额不能临时授权
			if(record.get('user_type') == 'DTV' && record.get('terminal_type') == 'ZZD' && record.get('status') != 'ACTIVE' && record.get('status') != 'OWESTOP' && record.get('status') != 'OUNSTOP'){
				flag = true;
				return false;
			}
		},store);
		for(var index =0;index<users.length;index++){
			var data = users[index].data;
			var userId = data.user_id;
			if(data['user_type'] == 'DTV' && data['terminal_type'] !='ZZD'  && data['str19'] != 'T' && flag){//超额副机
				userGrid.selModel.deselectRow(userGrid.store.find('user_id',userId));
			}
		}
		users = userGrid.getSelections();
		if(users.length==0){
			Alert(lmsg('userHasExtraFreeDev'));
			return false;
		}
		
		
		var prodMap = App.main.infoPanel.getUserPanel().prodGrid.prodMap;
		//遍历验证是否已经停机,
		var acctStore = App.main.infoPanel.getAcctPanel().acctGrid.getStore();
		var acctDatas = {};//keyByAcctId
		acctStore.each(function(rec){
			var data = rec.data;
			acctDatas[data.acct_id] = data;
		});
		
		
		for(var index =0;index<users.length;index++){
			var user = users[index];
			var userId = user.get('user_id');
			var prods = prodMap[userId];
			for(idx =0;idx<prods.length;idx++){
				var prod = prods[idx];
				if(prod.prod_type == 'BASE' && prod.is_base == 'T'){
					if(prod.status =='OWESTOP'){
						var acctId = prod.acct_id;
						var prodId = prod.prod_id;
						var acctData = acctDatas[acctId];
						var acctItems = acctData.acctitems;
						for (var j = 0; j < acctItems.length; j++) {
							if (acctItems[j]['acctitem_id'] == prodId) {
								if (acctItems[j]['real_balance'] > 1) {
									userGrid.selModel.deselectRow(userGrid.store.find('user_id',userId));
								}
								break;
							}
						}
					
					}else{
						userGrid.selModel.deselectRow(userGrid.store.find('user_id',userId));
					}
				}
			}
		}
		
		users = userGrid.getSelections();
		if(users.length==0){
			Alert('选中的用户没有符合可临时授权的条件,或者已经充值!');
			return false;
		}
		
		Confirm("确定授权吗?", this, function() {
					App.sendRequest(Constant.ROOT_PATH + "/core/x/User!saveOpenTempBatch.action", null,
							function(res, opt) {
								var data = Ext.decode(res.responseText);
								if (data == true) {
									Alert('临时授权成功!');
									App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
								}
							});
				});
		return false;
	},
	// 用户修改资料
	EditUser : function() {
		if (!hasCust()) {
			return false;
		}
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		if (userRecords.length != 1) {
			Alert(lmsg("onlyOneUser"));
			return false;
		} else if (userRecords[0].get("status") != "ACTIVE" && userRecords[0].get("status") != "OWELONG") {
			Alert(lmsg("needStopUser"));
			return false;
		} else {
			return {
				width : 550,
				height : 400
			};
		}

	},
	EditStb : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 450,
			height : 300
		};
	},
	EditsSingleCard : function() {
		if (!hasCust()) {
			return false;
		}
		var selectUser = App.main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		//双向数字用户
		if(selectUser.get('user_type') == 'DTV' && selectUser.get('serv_type') == 'DOUBLE'){
			var userStore = App.main.infoPanel.getUserPanel().prodGrid.getStore();
			
			var flag = false;// 是否有ITV的产品
			userStore.each(function(record) {
					if (record.get('serv_id') == 'ITV') {
						flag = true;
						return false;
					}
				});
			if (flag === true) {
				Alert('用户下有互动电视产品，请退订后再操作!');
				return false;
			}
			
			
			var items = userStore.query('user_type','BAND');
			//该客户下是否有宽带用户
			if(items.getCount() > 0){
				var stbId = selectUser.get('stb_id'), modemMac = selectUser.get('modem_mac');
				var isShare = false;		//当前双向用户是否和宽带共用一个MODEM
				items.each(function(record){
					if(record.get('modem_mac') == modemMac){
						isShare = true;
						return false;
					}
				});
				
				//如果双向用户是虚拟机猫一体，且和宽带共用MODEM，则不能转换一体机
				if(isShare === true){
					var deviceStore = App.main.infoPanel.getCustPanel().custDeviceGrid.getStore();
					var isVirtual = 'F';
					deviceStore.each(function(record){
						if(record.get('device_code') == stbId && record.get('pair_modem_code') == modemMac){
							isVirtual = record.get('is_virtual_modem');
							return false;
						}
					});
					
					if(isVirtual == 'T'){
						Alert('当前双向用户使用机MODEM虚拟一体机，且有宽带共用MODEM，不能转换一体机!');
						return false;
					}
					
				}
			}
		}
		
		return {
			width : 540,
			height : 350
		};
	},
	// 修改支付密码
	EditUserPassword : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 540,
			height : 400
		};
	},
	EzdToFzd : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 540,
			height : 500
		};
	},
	UserUntuck: function(){
		if (!hasCust()) {
			return false;
		}
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var typecout = 0;
		if (userRecords.length == 0) {
			Alert(lmsg("needUser"));
			return false;
		}
		var userIds = [];
		for (i = 0; i < userRecords.length; i++) {
			if (userRecords[i].get("status") != "ACTIVE") {
				Alert(lmsg("userNotActive"));
				return false;
			}
			userIds.push(userRecords[i].get("user_id"));
		}
		
		/*var url = Constant.ROOT_PATH + "/core/x/User!untuckUsers.action";
		Confirm(lmsg("confirmUntuckUser"), this, function() {
			App.sendRequest(url, {userIds: userIds}, function(res, opt){
				var data = Ext.decode(res.responseText);
				if (data == true) {
					App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
				}
			});
		});*/
		return {
			width: 450,
			height: 400
		};
	},
	// 报停
	UserStop : function() {
		if (!hasCust()) return false;
		var userGrid = App.main.infoPanel.getUserPanel().userGrid;
		var userRecords = userGrid.getSelections();
		if (userRecords.length == 0) {
			Alert(lmsg('needUser'));
			return false;
		}
		var userIds = [];
		for(var i = 0; i< userRecords.length; i++){
			userIds.push(userRecords[i].get("user_id"));
		}
		
		// 同步请求
		var flag = false;
		Ext.Ajax.request({
			url: Constant.ROOT_PATH + "/core/x/User!checkStop.action",
			params: {
				custId: App.getCustId(),
				userIds: userIds
			},
			async: false,
			success: function(response, options){
				var data = Ext.decode(response.responseText);
				if(data !== true){
					Ext.Ajax.handleResponse(response, options);
					flag = (data && data["success"] === true);
				}else{
					flag = true;
				}
			}
		});
		
		if(flag){
			return {
				width : 540,
				height : 460
			}
		}
		return flag;
	},
	// 报开
	UserOpen:function() {
		if (!hasCust()) {
			return false;
		}
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var typecout = 0;
		if (userRecords.length == 0) {
			Alert(lmsg("needUser"));
			return false;
		}
		for (i = 0; i < userRecords.length; i++) {
			if (userRecords[i].get("status") != "REQSTOP") {
				Alert(lmsg("needStopUser"));
				return false;
			}
		}
		
		var url = Constant.ROOT_PATH + "/core/x/User!openUser.action";
		Confirm(lmsg("confirmOpenUser"), this, function() {
			App.sendRequest(url, null, function(res, opt){
				var data = Ext.decode(res.responseText);
				if (data == true) {
					App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
				}
			});
		});
		return false;
	},
	UserInvalid : function(){
		if (!hasCust())
			return false;
			
		//判断用户下是否为数字电视用户
		var countNum = 0;
		var store = App.main.infoPanel.getUserPanel().userGrid.getStore();
		for (i = 0; i < store.getCount(); i++) {
			if (store.getAt(i).data.user_type == 'DTV') {
				countNum++;
			}
		}
		if(countNum<=0){
			Alert('该客户下无数字用户，无法进行重算到期日期!');
			return false;
		}
			
			// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('添加到期日重算任务成功，请重新搜索客户查看!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!userInvalid.action";

		Confirm("确定添加到期日重算任务吗?", this, function() {
					App.sendRequest(url, null, callback);
				});
		return false;
	},

	// 续报停
	EditUserStop : function() {
		if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('续报停成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!editUserStop.action";

		Confirm("确定续报停吗?", this, function() {
					App.sendRequest(url, null, callback);
				});
		return false;
	},
	//取消授权
	CancelAuth : function() {
	 if (!hasCust())
			return false;
		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert('取消授权成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!cancelCaAuth.action";

		Confirm("确定取消授权码？取消授权后该用户将无法收看任何节目。", this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, null, callback);
				});

		return false;
		 
	},
	PayAtvFee : function() {
		if (!hasCust())
			return false

		return {
			width : 540,
			height : 350
		};
	},
	CancelPromotion : function() {
		if (!hasCust())
			return false;

		var record = App.main.infoPanel.getUserPanel().userDetailTab.promotionGrid
				.getSelectionModel().getSelected();

		var promotion_sn = record.get('promotion_sn');

		Confirm("确定回退促销?", this, function() {
			App.sendRequest(Constant.ROOT_PATH
							+ "/core/x/User!saveCancelPromotion.action", {
						promotionSn : promotion_sn
					}, function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (data === true) {
							Alert('回退促销成功!');
							App
									.getApp()
									.refreshPanel(App.getApp().getData().currentResource.busicode);
						}
					});
		});

		return false;
	},
	//变更促销
	ChangePromotionProd: function(){
		if(!hasCust())return false;
		var record = App.getApp().main.infoPanel.getUserPanel().userDetailTab.promotionGrid
				.getSelectionModel().getSelected();
		var userId = record.get('user_id');
		// 选中真正操作的用户
		if (userId != null)
			App.getApp().selectRelativeUser([userId]);
				
		return {width: 850 , height: 450};
	},
	//主机产品同步
	SyncZzdProd : function(){
		if(!hasCust())return false
		return {width: 650 , height: 400};
	},
	//修改预开通日期
	ChangeProdPreOpenTime : function(){
		return {
			width : 400,
			height : 240
		};
	},
	//修改公用账目使用类型
	ChangePublicAcctItemType : function(){
		return {
			width : 360,
			height : 200
		};
	},
	//订购
	OrderProd: function(){
		if(!hasCust())return false;
		var windowSize = {
			width : 650,
			height : 520
		};
		//套餐订购
		var busiCode = App.data.currentResource.busicode;
		if(busiCode === "1015"){
			return windowSize;
		}
		// 如果是套餐续订或升级，不用判断用户
		// 单产品订购102，升级100，续订101, 套餐订购1015
		if(App.data.currentPanelId === "U_CUST_PKG" && (busiCode === "100" || busiCode === "101")){
			return windowSize;
		}
		
		var userRecords =  App.main.infoPanel.getUserPanel().userGrid.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}
		for (var i = 0; i < len; i++) {
			if (userRecords[i].get("status") != "ACTIVE" && userRecords[i].get("status") != "INSTALL" ) {
				Alert(lmsg('userStatusActiveOrConstruction'));
				return false;
			}
			for (var j = i + 1; j < len; j++) {
				if (userRecords[i].get('user_type') != userRecords[j]
						.get('user_type')) {

					Alert("用户的类型必须一致");
					return false;
				}
			}
		}
		return windowSize;
	},
	OrderProdEdit: function(){
		if(!hasCust())	return false;
		var prodRecord = App.main.infoPanel.getUserPanel().prodGrid.getSelections();
		if(prodRecord[0].get('prod_type') == 'BASE'){
			var userRecords =  App.main.infoPanel.getUserPanel().userGrid.getSelections();
			var len = userRecords.length;
			if (len == 0) {
				Alert(lmsg('needUser'));
				return false;
			}
		}
		for (var i = 0; i < len; i++) {
			if (userRecords[i].get("status") != "ACTIVE" && userRecords[i].get("status") != "INSTALL" ) {
				Alert(lmsg('userStatusActiveOrConstruction'));
				return false;
			}
			for (var j = i + 1; j < len; j++) {
				if (userRecords[i].get('user_type') != userRecords[j]
						.get('user_type')) {

					Alert("用户的类型必须一致");
					return false;
				}
			}
		}
		return {
			width: 650,
			height: 520
		};
	},
	// 取消套餐
	CancelPromFee : function() {
		if(!hasCust())return false;
		var url = Constant.ROOT_PATH+"/core/x/Acct!cancelPromFee.action";
		var record = App.getApp().main.infoPanel.custPanel.custDetailTab.promFeeGrid.selModel.getSelected();
		if(!record || Ext.isEmpty(record.get('done_code'))){
			Alert('未能正确获取数据！');return false;
		}
		Ext.Ajax.request({
			url : Constant.ROOT_PATH+"/core/x/Acct!queryPromAcctItemInactive.action",
			params:{doneCode:record.get('done_code'),custId:App.getCustId(),query:'F'},//query标记是否从历史表取
			scope:this,
			timeout:99999999999999,//12位 报异常
			success:function(res,opt){
				var datas = Ext.decode(res.responseText);
				var msg = '是否确认失效套餐缴费记录?';
				var actInfo = '';
				var prodMap = App.getApp().main.infoPanel.userPanel.prodGrid.prodMap;
				if(!datas || datas.length <=0){
					datas = [];
				}
				for(var index =0;index<datas.length;index++){
					for(var userid in prodMap){
						var arr = prodMap[userid];
						if(arr && arr.length>0){
							if(datas[index].acct_id != arr[0].acct_id){
								continue;
							}
							for(var idx =0;idx<arr.length;idx++){
								var item = arr[idx];
								if(datas[index].acct_id == item.acct_id && datas[index].acctitem_id == item.prod_id){
									actInfo += '' + item.prod_name + ' 冻结金额  '  + (datas[index].init_amount/100) + ' 元,(其中已解冻 ' + (datas[index].use_amount /100) + ' 元)</br>';
								}
							}
						}
					}
				}
				Ext.Msg.prompt('提示', '<font color="red">' + (Ext.isEmpty(actInfo)?actInfo:'以下账目冻结余额将被取消，请按实际情况调账处理</br>' + actInfo ) + msg + '</font>', function(value,remark){
				if(value == 'cancel'){
					return;
				}
				App.sendRequest(url, {promFeeSn:record.get('prom_fee_sn'),reason:remark}, function(){
					record.set('status','INVALID');
					Ext.getCmp('C_PROMFEE').remoteRefresh();
				});
			}, this, true, '备注信息')//
			}
		})
		return false;
	},
	OrderPkg : function() {
		if (!hasCust())
			return false
		return {
			width : 700,
			height : 550
		};
	},
	// 修改套餐
	EditPkg : function() {
		if (!hasCust())
			return false
		return {
			width : 700,
			height : 550
		};
	},
	//重算到期日
	ReCalcInvalidDate:function(){
		if(!hasCust())return false
		var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
		var prodData = prodGrid.selModel.getSelected().data;
		
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.store;
		var acctData = acctStore.getAt(acctStore.find('acct_id',prodData.acct_id));
		var acctItems = acctData.data.acctitems;
		var item = null;
		for(var index = 0;index < acctItems.length;index++ ){
			item = acctItems[index];
			if(item.acctitem_id == prodData.prod_id){
				break;
			}
			item = null;
		}
		if( ( item.real_balance - item.real_fee ) <=0 ){
			Alert('产品余额不足，不能重算到期日!');
			return false;
		}
		
		var url = Constant.ROOT_PATH+"/core/x/Prod!reCalcInvalidDate.action";
		function callback(res, ops) {
			var data = Ext.decode(res.responseText);
			Alert('操作成功,新的到日期为：' + data.invalidDate ,function(){
				App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			});
		}
		Confirm("确定要重算到期日么?", this, function() {
					App.sendRequest(url,  {prodSn: prodData.prod_sn}, callback);
				});
		
		return false;
	},
	// 退订
	CancelProdNew: function(){
		//柬埔寨
		return {
			width:750,
			height:450
		};
	},
	
	// 退订
	CancelProd: function(){
		
		if(!hasCust())return false
		var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
		var record = null;
		var prodId = null;
		var userId = null;
		
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			record = prodGrid.getSelectionModel().getSelections()[0];
//		}else if(activeId == 'CUST_PANEL'){
//			record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelections()[0];
		}
		prodId = record.get('prod_id');
		userId = record.get('user_id');
		
		if (activeId == 'USER_PANEL'){
			//剩余产品必须有基本产品
			prodId = record.get('prod_id');
			userId = record.get('user_id');
			//用户状态不能为报停
			var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
			for(var i=0;i<userStore.getCount();i++){
				if(userId == userStore.getAt(i).get('user_id')){
					if(userStore.getAt(i).get("status") != "ACTIVE"){
						Alert(lmsg('userNotActive'));
						return false;
					}
				}
			}
		}
		//账目实际余额大于0
		var acctItemData = App.getAcctItemByProdId(record.get('prod_id'),userId);
		if(acctItemData){
			if(acctItemData.real_balance < 0 && acctItemData.order_balance != -acctItemData.real_balance){
				Alert("产品账目实际余额必须大于等于0");
				return false;
			}
		}else{
			Alert("该用户对应的账户或者账目数据有问题,请核对");
			return false;
		}
			
		//选中真正操作的用户
		if (userId != null){
			App.getApp().selectRelativeUser([userId]);
		}
		
		
		var url = root+'/commons/x/QueryUser!queryPromotionCanCancel.action';
			Ext.Ajax.request({
				url :url,
				params:{userId:userId,prod_id:prodId,userId:userId},
				success:function(req){
					array = Ext.decode(req.responseText);
					Ext.getBody().unmask();
					var btn = {text:'退订',attrs:App.data.currentResource};
					var cfg = {width : 560,height : 460}; 
					
					if(array.length ==0){
						App.menu.bigWindow.show(btn,cfg);
					}else{
						var msg = '该产品存在可回退促销[<font color="red">{0}</font>],该促销包含:</br>';
						var promotionName = '';
						for(var index =0;index<array.length;index++){
							var obj = array[index];
							promotionName = obj.promotion_name;
							msg += '[<font color="red">' + obj.cust_id +'</font>] ';
						}
						msg += ' </br>如有必要,请先执行促销回退';
						
						msg = String.format(msg,promotionName);
						Ext.Msg.show({
			                title : '提示',
			                msg : msg,
			                width:300,
			                buttons: {ok : "<font size='2'><b>忽略</b></font>",cancel : "<font size='2'><b>返回</b></font>"},
			                fn: function(text){
			                	if(text =='ok'){
									App.menu.bigWindow.show(btn,cfg);
			                	}
							},
			                scope : this
			            });
			            
						return false;
					}
					
				}
			});
			
			return false;
	},
	//宽带升级
	ChangeBandProd: function(){
		
		return {
			width:650,
			height:450
		};
	},
	//更换动态资源
	ChangeProdDynRes: function(){
		
		return {
			width:650,
			height:450
		};
	},
	// 恢复用户状态
	RenewUser : function() {
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid
				.getSelectionModel().getSelected();
		var userId = record.get('user_id');
		var url = Constant.ROOT_PATH + "/core/x/User!renewUser.action";
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert(lmsg('commonSuccess'));
				App.getCust().status = result.simple_obj;
				App.getApp().refreshPanel('1221');
			}
		}
		Confirm("确定恢复用户状态吗?", this, function() {
					App.sendRequest(url, {userId:userId}, callback);
				});
		return false;
	},
	// 资费变更
	ChangeTariff : function() {
		if (!hasCust())
			return false
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid
				.getSelectionModel().getSelected();
		if (record) {
			App.getApp().selectRelativeUser([record.get('user_id')]);
		}
		return {
			width : 540,
			height : 300
		};
	},	
	//修改到期日
	EditInvalidDate : function() {
		if (!hasCust())
			return false
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid
				.getSelectionModel().getSelected();
		if (record) {
			App.getApp().selectRelativeUser([record.get('user_id')]);
		}
		return {
			width : 540,
			height : 300
		};
	},
	// 取消资费变更
	CancelChangeTariff : function() {
		if (!hasCust())
			return false;
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid
				.getSelectionModel().getSelected();
		var prodSn = record.get('prod_sn');
		var tariffId = record.get('next_tariff_id');
		Confirm("确定取消资费变更吗?", this, function() {
			App.sendRequest(Constant.ROOT_PATH
							+ "/core/x/User!removeByProdSn.action", {
						prodSn : prodSn,
						tariffId : tariffId
					}, function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (data === true) {
							Alert('取消资费变更成功!');
							App
									.getApp()
									.refreshPanel(App.getApp().getData().currentResource.busicode);
						}
					});
		});
		return false;
	},
	// 失效日期变更
	ChangeExpDate : function() {
		if (!hasCust())
			return false
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid
				.getSelectionModel().getSelected();
		if (record) {
			App.getApp().selectRelativeUser([record.get('user_id')]);
		}
		return {
			width : 540,
			height : 250
		};
	},
	// 协议缴费
	EditExpDate : function() {
		if (!hasCust())
			return false
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid
				.getSelectionModel().getSelected();
		if (record) {
			App.getApp().selectRelativeUser([record.get('user_id')]);
		}

		return {
			width : 540,
			height : 350
		};
	},
	BatchEditExpDate : function() {
		if (!hasCust())
			return false;

		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}

		for (var i = 0; i < len; i++) {
			var status = userRecords[i].get("status");
			if ( status != "ACTIVE" && status != "OWELONG" ) {
				Alert("所选用户的状态必须是正常");
				return false;
			}
		}

		return {
			width : 750,
			height : 550
		};
	},
	// 指令重发
	ResendCmd : function() {
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}

		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert(lmsg("commonSuccess"));
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!ResendCmd.action";

		Confirm(lmsg("confirmResendCmd"), this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, null, callback);
				});

		return false;
	},
	ResendVodCmd : function() {
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}

		for (var i = 0; i < len; i++) {
			var status = userRecords[i].get("status");
			if ( status != "ACTIVE" && status != "OWELONG" ) {
				Alert(lmsg("userNotActive"));
				return false;
			}
		}

		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert(lmsg("commonSuccess"));
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!ResendVodCmd.action";

		Confirm(lmsg("confirmResendCmd"), this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, null, callback);
				});

		return false;
	},
	// 刷新指令
	RefreshCmd : function() {
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}

		 //回调函数
		 function callback(res,opt){
			 if (res.responseText=='true'){
				 Alert(lmsg("commonSuccess"));
				 App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
			 }
		 }
		 var url = Constant.ROOT_PATH + "/core/x/User!RefreshCmd.action";
				
		 Confirm(lmsg("confirmRefreshCmd"), this , function(){
			 //调用请求函数,详细参数请看busi-helper.js
			 App.sendRequest(url,null,callback);
		 } );

		return false;
	},
	// 促销
	Promotion : function() {
		if (!hasCust())
			return false;
		return {
			width : 750,
			height : 500
		};
	},
	// 宽带指令强制下线
	BandOfflineCmd : function() {
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid
				.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}
		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert('强制下线成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!saveOfflineCmd.action";

		Confirm("确定强制下线吗?", this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, null, callback);
				});

		return false;
	},
	// 宽带指令清除绑定
	BandClearCmd : function() {
		if (!hasCust())
			return false;
		var userRecords = App.main.infoPanel.getUserPanel().userGrid.getSelections();
		var len = userRecords.length;
		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		}
		//判断用户名下是否有产品
		var prodData = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap[userRecords[0].get('user_id')];
 		if(null==prodData || prodData.length==0){
			Alert('宽带用户下无产品请先订购产品');
			return false;
		}
		// 回调函数
		function callback(res, opt) {
			if (res.responseText == 'true') {
				Alert('清除绑定成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!saveClearBind.action";

		Confirm("确定清除绑定吗?", this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, null, callback);
				});
		return false;
	},
	CancelStop : function() {
		if (!hasCust()) {
			return false;
		}
		var userGrid = App.main.infoPanel.getUserPanel().userGrid;
		var userRecords = userGrid.getSelections();

		var len = userRecords.length;// 选中记录

		if (len == 0) {
			Alert(lmsg('needUser'));
			return false;
		} else {
			for (i = 0; i < len; i++) {
				var status = userRecords[i].get("status");
				if ( status != "ACTIVE" && status != "INSTALL" && status != "OWELONG" ) {
					Alert(lmsg('userNotActive'));
					return false;
				}
				if (Ext.isEmpty(userRecords[i].get("stop_date"))) {
					Alert(lmsg("noPreStopUser"));
					return false;
				}
			}
			return {
				width : 540,
				height : 400
			};

		}
	},
	LeaseFee : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	// 模拟剪线
	AtvCutLine : function() {
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('模拟剪线成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
				App.getApp().main.infoPanel.getUserPanel().prodGrid
						.remoteRefresh();
				App.getApp().main.infoPanel.getAcctPanel().acctGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!saveAtvCustLine.action";

		Confirm("确定剪线吗?", this, function() {
					App.sendRequest(url, null, callback);
				});
		return false;
	},
	// 模拟恢复
	AtvActive : function() {
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('模拟恢复成功!');
				App.getApp().main.infoPanel.getUserPanel().userGrid
						.remoteRefresh();
				App.getApp().main.infoPanel.getUserPanel().prodGrid
						.remoteRefresh();
				App.getApp().main.infoPanel.getAcctPanel().acctGrid
						.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!saveAtvActive.action";

		Confirm("确定恢复吗?", this, function() {
					App.sendRequest(url, null, callback);
				});
		return false;
	},
	// 产品暂停
	PauseProd : function() {
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('产品暂停成功!');
				App.getApp().main.infoPanel.getUserPanel().prodGrid.remoteRefresh();
//				App.getApp().main.infoPanel.getCustPanel().packageGrid.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!pauseProd.action";

		Confirm("确定暂停产品吗?", this, function() {
					var record = null;
					var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
					if(activeId == 'USER_PANEL'){
						record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//					}else if(activeId == 'CUST_PANEL'){
//						record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
					}
					var prodSn = record.get('prod_sn');
					var userId = record.get('user_id');
					App.sendRequest(url, {
								'prodSn' : prodSn,
								'userId' : userId
							}, callback);
				});
		return false;
	},
	// 产品恢复
	ResumeProd : function() {

		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('产品恢复成功!');
				App.getApp().main.infoPanel.getUserPanel().prodGrid.remoteRefresh();
//				App.getApp().main.infoPanel.getCustPanel().packageGrid.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/User!resumeProd.action";

		Confirm("确定恢复产品吗?", this, function() {
					var record = null;
					var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
					if(activeId == 'USER_PANEL'){
						record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//					}else if(activeId == 'CUST_PANEL'){
//						record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
					}
					var prodSn = record.get('prod_sn');
					var userId = record.get('user_id');
					App.sendRequest(url, {
								'prodSn' : prodSn,
								'userId' : userId
							}, callback);
				});
		return false;
	},
	RechargeCard : function() {
		if (!hasCust()) {
			return false;
		}
		return {
			width : 520,
			height : 150
		};
	},

	// --------------------账户信息------------------------------------------------
	//套餐缴费
	PromPayFee : function(){
		if (!hasCust()) {
			return false;
		}
		return {
			width : 700,
			height : 500
		};
	},
	// 调账
	AcctAdjust : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	// 作废账目退款
	AcctitemRefund : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	// 调账可退
	AcctKtAdjust : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	//小额减免
	AcctEasyAdjust : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
		//作废赠送
	AcctCancelFree : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	DEZSRefund : function(){
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	//作废冻结金额
	ClearInactiveAmount: function(){
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('作废剩余冻结金额成功!');
				App.getApp().main.infoPanel.getAcctPanel().acctGrid.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/Acct!clearInactiveAmount.action";
		
		var record = App.getApp().main.infoPanel.getAcctPanel().acctItemDetailTab.acctitemInactiveGrid.getSelectionModel().getSelected();
		Ext.MessageBox.show({
	        title: "确定作废剩余冻结金额吗?",
	        msg: "作废冻结金额：",
	        modal: true,
	        prompt: true,
	        value: Ext.util.Format.formatFee(record.get('balance')),
	        fn: function (id, msg) {
	        	if(id == 'ok'){
	        		if(Ext.isEmpty(msg)){
	        			Alert("请输入金额!");
	        			return;
	        		}
	        		if(!/^([1-9]\d*|[1-9]\d*\.\d+|0\.\d*[1-9]\d*|0?\.0+|0)$/.test(msg)){
			        	Alert("请输入正整数或小数!");	
			        	return;
	        		}
	        		var amount = Ext.util.Format.formatToFen(msg);
	        		if(amount > record.get('balance') || amount<0){
	        			Alert("请输入:0~"+Ext.util.Format.formatFee(record.get('balance'))+"!");	
	        			return;
	        		}
	        		var feeSn = record.get('fee_sn');
	        		if(Ext.isEmpty(feeSn)){
	        			if(Ext.isEmpty(record.get('promotion_sn'))){
		        			Alert("数据有问题,请联系管理员!");
		        			return;
	        			}else{
		        			feeSn = record.get('promotion_sn');
	        			}
	        		}
	        		App.sendRequest(url,{'promFeeSn':feeSn,'acctId': record.get('acct_id'),
								'acctItemId': record.get('acctitem_id'),'fee':amount,'realPay':record.get('balance')},callback);
	        	}
	        },
	        buttons: Ext.Msg.OKCANCEL,
	        icon: Ext.Msg.QUEATION
	    });
		return false;
	},
	// 欠费抹零
	AcctFree : function() {
		if (!hasCust())
			return false;
		return {
			width : 540,
			height : 280
		};
	},
	// 银行签约
	BankContract : function() {
		if (!hasCust())
			return false
		return {
			width : 500,
			height : 180
		};
	},
	// 银行解约
	BankCancelContract : function() {
		if (!hasCust())
			return false
		return {
			width : 500,
			height : 180
		};
	},
	// 缴费
	PayFees : function() {
		if (!hasCust())
			return false;
			
			return {
				width : 750,
				height : 550
			};
			
			
//		var records = App.getApp().main.infoPanel.acctPanel.acctGrid
//				.getSelectionModel().getSelections();
//
//		if (records.length == 0) {
//			Alert("请选择要缴费的账户！");
//			return false;
//		} else {
//			var flag = false;
//			for (var i = 0; i < records.length; i++) {
//				if ((records[i].get('acct_type') == 'PUBLIC' || (records[i]
//						.get('acct_type') == 'SPEC' && (records[i].get('status') == 'ACTIVE' 
//						|| records[i].get('status') == 'OWELONG' || records[i].get('status') == 'ATVCLOSE')))
//						&& records[i].get('acctitems')) {
//					flag = true;
//				}
//			}
//			if (flag) {
//				// 批量缴费的标志位
//				App.getApp().getData().batchPayFee = false;
//				return {
//					width : 750,
//					height : 550
//				};
//			} else {
//				Alert("选中账户下无任何账目或者用户账户状态不正常！");
//				return false;
//			}
//		}
	},
	ModifyThreshold : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid
				.getSelectionModel().getSelections();
		if (records.length == 0) {
			Alert("请选择要修改阀值的账户！");
			return false;
		}
		return {
			width : 750,
			height : 400
		};
	},
	BatchModifyThreshold : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid.getSelectionModel().getSelections();
		if(records.length == 0){
			Alert("请选择要修改阀值的账户！");
			return false;
		}
		return {
			width : 750,
			height : 550
		};
	},
	// 批量缴费
	BatchPayFees : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid
				.getSelectionModel().getSelections();

		if (records.length == 0) {
			Alert("请选择要缴费的账户！");
			return false;
		} else {
			var flag = false;
			for (var i = 0; i < records.length; i++) {
				if ((records[i].get('acct_type') == 'PUBLIC' || (records[i]
						.get('acct_type') == 'SPEC' && records[i].get('status') == 'ACTIVE'))
						&& records[i].get('acctitems')) {
					flag = true;
				}
			}
			if (flag) {
				// 批量缴费的标志位
				App.getApp().getData().batchPayFee = true;
				return {
					width : 750,
					height : 550
				};
			} else {
				Alert("选中账户下无任何账目或者用户账户状态不正常！");
				return false;
			}
		}
	},
	// 单位客户缴费
	UnitPayFee : function() {
		if (!hasCust())
			return false;

		var records = App.getApp().main.infoPanel.unitPanel.unitMemberGrid
				.getSelectionModel().getSelections();
		if (records.length == 0) {
			Alert('至少选择一个客户');
			return false;
		}

		return {
			width : 750,
			height : 550
		};
	},
	BatchChangeTariff : function(){
		if (!hasCust())
			return false;
		if(App.getCust().cust_type == "UNIT"){
			var records = App.getApp().main.infoPanel.unitPanel.unitMemberGrid
					.getSelectionModel().getSelections();
			if (records.length == 0) {
				Alert('至少选择一个客户');
				return false;
			}
		}else{ 
			var userGrid = App.main.infoPanel.getUserPanel().userGrid;
			var userRecords = userGrid.getSelections();
			if (userRecords.length == 0) {
				Alert('至少选择一个用户');
				return false;
			}
		}
		return {
			width : 750,
			height : 550
		};
	}
	,
	// 退款
	AcctReimburse : function() {
		if (!hasCust())
			return false;

		var record = App.getApp().main.infoPanel.custPanel.acctItemGrid
				.getSelectionModel().getSelected();
		if (record.get('can_refund_balance') === 0) {
			Alert("账目退款余额不足！");
			return false;
		}

		// var acctType =
		// App.getApp().main.infoPanel.acctPanel.acctItemGrid.acctType;
		// if(acctType == 'PUBLIC'){
		// Alert("对不起,公共账户不允许退款！请选择用户账户进行退款");
		// return false;
		// }
		return {
			width : 550,
			height : 350
		};
	},
	// 转账
	AcctTransfer : function() {
		if (!hasCust())
			return false;
		var record = App.getApp().main.infoPanel.custPanel.acctItemGrid
				.getSelectionModel().getSelected();
		if (record.get('can_trans_balance') === 0) {
			Alert('账目可转金额不足！');
			return false;
		}
		return {
			width : 650,
			height : 400
		};
	},
	EditQuatoInvoice : function(){
		if (!hasCust())
			return false;
			
		return {
			width : 540,
			height : 280
		};
	},
	// 修改出票方式
	EditInvoiceMode : function() {
		return {
			width : 540,
			height : 300
		};
	},
	//手工修改手工票的发票号
	ManuallyEditMInvoice : function() {
		return {
			width : 540,
			height : 300
		};
	},
	// 修改账务日期
	EditAcctDate : function() {
		// if(!hasCust()) return false;

		return {
			width : 540,
			height : 300
		};
	},
	// 修改业务员
	ModifyBusiOptr : function() {
		return {
			width : 555,
			height : 300
		};
	},
	//收费清单更换发票
	ChangeFeelistInvoice: function(){
		return {
			width : 555,
			height : 300
		};
	},
	InvalidInvoice:function(){
		if (!hasCust()) return false;
		var records = App.getApp().main.infoPanel.docPanel.invoiceGrid
				.getSelectionModel().getSelections();
		if (records.length == 0 || records.length != 1) {
			Alert("请选择要作废的发票！");
			return false;
		}
		var invoice = records[0];
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert(lmsg("commonSuccess"));
				App.getApp().main.infoPanel.getDocPanel().invoiceGrid.remoteRefresh();
				App.getApp().main.infoPanel.getPayfeePanel().refresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/Pay!invalidInvoice.action";

		Confirm(lmsg("confirmInvalidInvoice"), this, function() {
					App.sendRequest(url, {
								'invoice_id': invoice.get('invoice_id'),
								'invoice_code': invoice.get('invoice_code'),
								'invoice_book_id': invoice.get('invoice_book_id')
							}, callback);
				});
		return false;
	},
	//正式发票作废
	InvalidFeelistInvoice: function(){
		// 回调函数
		function callback(res, opt) {
			var data = Ext.decode(res.responseText);
			if (data['success'] == true) {
				Alert('作废成功!');
				App.getApp().main.infoPanel.getDocPanel().invoiceGrid.remoteRefresh();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/Pay!invalidFeeListInvoice.action";

		Confirm("确定作废正式发票吗?", this, function() {
					var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected()
					App.sendRequest(url, {
								'donecode': record.get('fee_done_code')
							}, callback);
				});
		return false;
	},

	// --------------------缴费记录------------------------------------------------
	// --------------------单据信息------------------------------------------------

	// 发票打印
	DocPrint : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.docPanel.invoiceGrid
				.getSelectionModel().getSelections();
		if (records.length == 0 || records.length != 1) {
			Alert("请选择一个要打印的发票！");
			return false;
		}
		return {
			width : 710,
			height : 460
		};
	},
	// 确认单打印
	ConfigPrint : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.docPanel.busiDocGrid
				.getSelectionModel().getSelections();
		if (records.length == 0) {
			Alert("请选择要打印的确认单！");
			return false;
		}
		return {
			width : 550,
			height : 300
		};
	},
	//新增保障单
	NewRepairTask: function(){
	    if(!hasCust()) return false;
	    return {width: 450 , height: 350};
	  },
	PrintRepairTask: function(){
	  	var record = App.getApp().main.infoPanel.getDocPanel().taskGrid.getSelectionModel().getSelected();
		var ps = [];
		ps.push(record.get("task_type")+"#" + record.get("cust_id") + "#" + record.get("work_id"));
		Ext.Ajax.request({
			url: Constant.ROOT_PATH + "/core/x/Task!queryPrintContent.action",
		params: {tasks: ps},
		success: function( res, ops){
			var data = Ext.decode(res.responseText);
			if(data.length > 0){
				var html = PrintTools.getPageHTML(data[0].tpl, data);
				PrintTools.showPrintWindow(html);
			}else{
				Alert("没有需要打印的数据");
					}
				}
			});
		  	return false;
	},
	// 修改发票
	EditInvoice : function() {
		if (!hasCust())
			return false;
		var records = App.getApp().main.infoPanel.docPanel.invoiceGrid
				.getSelectionModel().getSelections();
		if (records.length == 0 || records.length != 1) {
			Alert("请选择要修改的发票！");
			return false;
		}
		return {
			width : 540,
			height : 300
		};
	},
	MergeFeePanel : function() {
		return {
			width : 740,
			height : 470
		};
	},
	Print : function() {
		return {
			width : 540,
			height : 470
		};
	},

	// --------------------受理记录------------------------------------------------
	CanDoneCode : function() {
		if (!hasCust())
			return false;
		var records = App.main.infoPanel.getDoneCodePanel().doneCodeGrid
				.getSelectionModel().getSelections();
		if (records.length != 1) {
			Alert('请选择一条记录');
			return false;
		}
		var record = records[0];
		var params = {};
		params['doneCode'] = record.get('done_code');
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result.success == true) {
				Alert('业务回退成功!');
				App.getApp().main.infoPanel.getDoneCodePanel().doneCodeGrid
						.remoteRefresh();
				App.getApp().refreshPanel(record.get('busi_code'));
			} else {
				Alert(result.simpleObj);
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/DoneCode!cancelDoneCode.action";

		Confirm("确定业务回退吗?", this, function() {
					// 调用请求函数,详细参数请看busi-helper.js
					App.sendRequest(url, params, callback);
				});

		return false;
	},
	// --------------------账单面板------------------------------------------------
	CancelBill : function() {
		if (!hasCust())
			return false;
		var confirmMsg = '';
		var thisMonthBillingCycle = parseInt(nowDate().format('Ym') );
		var records = App.main.infoPanel.getBillPanel().billGrid.getSelectionModel().getSelections();
		var billSns = [];
		var custStatus = App.getApp().getData().custFullInfo.cust.status;
		if(custStatus == 'INVALID'){
			Ext.each(records,function(record){
				var billCycle = parseInt(record.get('billing_cycle_id'));
				if(thisMonthBillingCycle > billCycle){
					confirmMsg = Ext.isEmpty(confirmMsg) ? '本次有不能取消的账单，将自动过滤掉' :confirmMsg;
				}else{
					if(record.get('status') == '1'){
						billSns.push(record.get('bill_sn'));
					}
				}
			});
		}else{
			Ext.each(records,function(record){
				var billCycle = parseInt(record.get('billing_cycle_id'));
				if(thisMonthBillingCycle > billCycle){
					confirmMsg = Ext.isEmpty(confirmMsg) ? '本次有不能取消的账单，将自动过滤掉' :confirmMsg;
				}else{
					if(record.get('come_from') != '4'){
						//套餐
						if( (record.get('status')=='0' && record.get('come_from')=='5') 
							|| (record.get('status') == '1' || record.get('status')=='0') ){
							billSns.push(record.get('bill_sn'));
						}
					}
				}
				
			});
		}

		if(billSns.length >0){
			// 回调函数
			function callback(res, opt) {
				var result = Ext.decode(res.responseText);
				if (result.success == true) {
					Alert('作废账单成功!');
					App.getApp().main.infoPanel.getBillPanel().billGrid
							.remoteRefresh();
					App.getApp().main.infoPanel.getBillPanel().acctitemInvalidGrid
							.remoteRefresh();
				    App.getApp().main.infoPanel.getAcctPanel().setReload(true);
							
				} else {
					Alert(result.simpleObj);
				}
			}
			var url = Constant.ROOT_PATH + "/core/x/Acct!saveCancelBill.action";
	
			Confirm(confirmMsg + "确定作废账单吗?", this, function() {
						// 调用请求函数,详细参数请看busi-helper.js
						App.sendRequest(url, {billSns: billSns}, callback);
					});

		} else {
			Alert('没有符合条件的账单可作废');
		}
		return false;
	},
	RePrintBusiDoc:function(){//重打受理单
		var record = App.main.infoPanel.docPanel.busiDocGrid.getSelectionModel().getSelected();
	  	if(record){
	  		App.getApp().data['docSn'] = record.get('doc_sn');
	  		var printDteStr = record.get('create_time');
	  		if(!Ext.isEmpty(printDteStr) && printDteStr.trim().length >= 10){
	  			App.getApp().data['busiDocPrintDate'] = printDteStr.trim().substring(0,10);
	  		}
	  	}else{
	  		Alert('请选择一条记录!');
	  		return false;
	  	}
		App.getApp().openBusiPrint(true);
	  	return false;
	},
	PublicAcctRecharge:function(){
		if (!hasCust())
			return false;
		var acctStore = App.getApp().main.infoPanel.getCustPanel().acctItemGrid.getStore();
		if (acctStore.getCount() > 1) {
			Alert("目前只支持单个公用账目！");
			return false;
		}
		return {
			width : 450,
			height : 300
		};
	},
	PublicAcctRefund:function(){
		if (!hasCust())
			return false;
		var acctStore = App.getApp().main.infoPanel.getCustPanel().acctItemGrid.getStore();
		if (acctStore.getCount() > 1) {
			Alert("目前只支持单个公用账目！");
			return false;
		}
		return {
			width : 580,
			height : 400
		};
	},
	removeTaskBtnId:function(){
		var record = App.main.infoPanel.docPanel.taskGrid.getSelectionModel().getSelected();
		// 回调函数
		function callback(res, opt) {
			var result = Ext.decode(res.responseText);
			if (result == true) {
				Alert(lmsg('commonSuccess'));
				App.getApp().main.infoPanel.getUserPanel().userGrid.remoteRefresh();
				App.getApp().main.infoPanel.docPanel.taskGrid.remoteRefresh();
				App.getApp().refreshPayInfo();
			}
		}
		var url = Constant.ROOT_PATH + "/core/x/Task!cancelTaskSn.action";

		Confirm(lbc('home.tools.TaskManager.msg.sureWantSelectedWork'), this, function() {
			// 调用请求函数,详细参数请看busi-helper.js
			App.sendRequest(url, {task_id : record.get('task_id'),taskType:record.get('task_type_id')}, callback);
		});
	},
	SaleDeviceFee:function(){
		if (!hasCust())
			return false;
		var record = App.main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		return {
			width : 450,
			height : 400
		};
		
	},
	PayOtherFee:function(){
		if (!hasCust())
			return false;
		return {
			width : 450,
			height : 400
		};
	}
	
});
