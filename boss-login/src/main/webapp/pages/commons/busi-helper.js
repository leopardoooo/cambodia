/*********************************************************
 * 业务数据查询 
 * 目前引入到core 工程
 */
Ext.apply( App, {
	invoiceId:new Array(),
	/**
	 * 获取下一张发票
	 */
	getNextInvoice:function(docType){
		var invoiceId = App.getApp().invoiceId;
		if(!Ext.isEmpty(invoiceId)&&!Ext.isEmpty(invoiceId[docType])){
			return Ext.util.Format.lpad(parseInt(invoiceId[docType],10)+1,invoiceId[docType].length,'0');
		}
		return '';
	},
	getCurrentInvoice:function(docType){
		return App.getApp().invoiceId[docType];
	},
	/**
	 * 保存发票号
	 */	
	useInvoice:function(docType,invoiceId){
		App.getApp().invoiceId[docType]=invoiceId;
	},	
	findDtvNetTypeById: function (netType) {
	    return this.findNetTypeByUserTypeID('DTV',netType);
	},
	findAtvNetTypeById: function (netType) {
	    return this.findNetTypeByUserTypeID('ATV',netType);
	},
	findBandNetTypeById: function (netType) {
	    return this.findNetTypeByUserTypeID('BAND',netType);
	},
	findNetTypeByUserTypeID : function(usertype, netType) {
		var netTypes = App.getApp().findCfgData("NET_TYPE")[usertype];
		for (var i = 0; i < netTypes.length; i++) {
			if (netTypes[i].net_type == netType) {
				return netTypes[i];
			}
		}
		return null;
	},
	findAllDtvNetType:function (){
		return App.getApp().findCfgData("NET_TYPE")['DTV'];
	},
	findAllAtvNetType:function (){
		return App.getApp().findCfgData("NET_TYPE")['ATV'];
	},
	findAllBandNetType:function (){
		return App.getApp().findCfgData("NET_TYPE")['BAND'];
	},
	findAllNetType:function (usertype){
		return App.getApp().findCfgData("NET_TYPE")[usertype];
	},
	findAllCheckType:function(){
		return App.getApp().findCfgData("CHECK_TYPE");
	},	
	findAllTerminalAmount:function(){
		return App.getApp().findCfgData("TERMINAL_AMOUNT");
	},
	isUseInvoice : function(){
		return App.getApp().findCfgData("USE_INVOICE").config_value;
	},
	getLowestDisct : function(){
		var cfgData = App.getApp().findCfgData('LOWEST_DISCT');
		if(Ext.isEmpty(cfgData))
			return 0;
		return cfgData.config_value;;
	},
	getBandLowestDisct : function(){
		var cfgData = App.getApp().findCfgData('BAND_LOWEST_DISCT');
		if(Ext.isEmpty(cfgData))
			return 0;
		return cfgData.config_value;;
	},
	isPrintInvoice : function(){
		return App.getApp().findCfgData("PRINT_INVOICE").config_value;
	},
	/**
	 * 检查设备是否可用
	 * @param {} txt 可输入的组件
	 * @param {} deviceType 设备类型
	 * @param {} cardComp 卡号文本框
	 * @param {} ownershipComp 产权选择框(card无产权)
	 * @param {} updateOwnershipComp 添加删除产权选择框
	 * @param {} 机顶盒型号组件或modem型号组件
	 * @param {} 智能卡型号组件
	 */
	checkDevice:function(txt,deviceType,cardComp,userType){
		//使用rawvalue,在提交时能获得值
		var value = txt.getRawValue();
		
		if(value=='')return;
		Ext.Ajax.request({
			async: false,
			url:Constant.ROOT_PATH + "/core/x/Cust!queryUseableDevice.action",
			params:{
				deviceType:deviceType,
				deviceCode:txt.getValue(),
				custId:App.getCustId(),
				userType : userType
			},
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				if(Ext.isEmpty(data.device_code)){
					txt.reset();
				}
				if(data.device_code && value != data.device_code){
					txt.setValue(data.device_code);
				}
				if(cardComp){
					//机顶盒由配对卡号
					if(data.pairCard && data.pairCard.card_id){
						cardComp.setValue(data.pairCard.card_id);
						if(cardComp.edtiable)
							cardComp.setEditable(false);
						if(!cardComp.readOnly)
							cardComp.setReadOnly(true);
					}else{
						if(cardComp.store){
							cardComp.reset();
							if(!cardComp.editable)
								cardComp.setEditable(true);
							if(cardComp.readOnly)
								cardComp.setReadOnly(false);
						}else{
							cardComp.setValue(' ');
						}
					}
				}
				
				//验证通过
				txt.checked = true;
			},
			clearData:function(){
				txt.reset();
				if(cardComp && cardComp.getValue())
					cardComp.reset();
				
				//验证不通过
				txt.checked = false;
			}
		});
	},
	//检查智能卡号是否有配置的机顶盒
	checkCardDevice:function(txt,stbComp,cardModel){
		//使用rawvalue,在提交时能获得值
		var value = txt.getRawValue();
		
		if(value){
			Ext.Ajax.request({
				async: false,
				url:Constant.ROOT_PATH + "/core/x/Cust!queryUseableDevice.action",
				params:{
					deviceCode:value,
					deviceType : 'CARD',
					custId:App.getCustId()
				},
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(cardModel){
						cardModel.setValue(data.deviceModel.device_model_text);
					}
					
					//验证通过
					txt.checked = true;
				},
				clearData:function(){
					txt.reset();
					//验证不通过
					txt.checked = false;
				}
			});
		}
	},
	//根据用户ID，产品ID得到账目信息
	getAcctItemByProdId : function(prodId,userId){
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(userId == acctStore.getAt(i).get('user_id')){
				var acctItemStore = acctStore.getAt(i).get('acctitems');
				if(acctItemStore){
					for(var j=0;j<acctItemStore.length;j++){
						if(prodId == acctItemStore[j].acctitem_id){
							return acctItemStore[j];
						}
					}
				}
			}
		}
	},
	/**
	 * 封装请求
	 * @param {} url 请求url
	 * @param {} params 传送参数params(只需提供自己业务需要的，没有传null,
	 * 公共的参数包括客户信息，所选用户信息，业务代码不需要提供
	 * @param {} callback 成功后回调函数
	 */
	sendRequest : function(url,params,callback, async){
		var all = {};
		//公有的参数
		var common = App.getValues();
		all[CoreConstant.JSON_PARAMS] = Ext.encode(common);
		if(params != null){
			for(var key in params){
				all[key] = params[key];
			}
		}
		//业务提交提示框
		tip = Show();
		Ext.Ajax.request({
			scope : this,
			url : url,
			params : all,
			async: async === false ? false : true,
			success : function(res,opt){
				tip.hide();
				tip = null;
				callback(res,opt);
			}
		})
	},
	//选中真正操作的用户（资费变更，产品退订）
	selectRelativeUser : function(userIds){
		var len = userIds.length;
		var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
		userGrid.getSelectionModel().clearSelections();
		var userStore = userGrid.getStore();
		for(var i=0;i<userStore.getCount();i++){
			if(userIds.join(',').indexOf(userStore.getAt(i).get('user_id')) > -1 ){
				userGrid.getSelectionModel().selectRow(i,true);
				len = len - 1;
				if(len == 0){
					break;//选中一定数量后停止循环
				}
			}
		}
	},
	selectRelativeAcct : function(){
		var acctGrid = App.getApp().main.infoPanel.getAcctPanel().acctGrid;
		for(var i=0;i<acctGrid.getStore().getCount();i++){
			acctGrid.getSelectionModel().selectRow(i,true);
		}
	},
	selectRelativeAcctUser : function(userIds){
		var len = userIds.length;
		var acctGrid = App.getApp().main.infoPanel.getAcctPanel().acctGrid;
		//设置帐目grid不是全选
		acctGrid.allowSelectAll = false;
		if(acctGrid.getSelectionModel().lastActive != false){
			acctGrid.getSelectionModel().clearSelections();
		}
		var acctStore = acctGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(userIds.join(',').indexOf(acctStore.getAt(i).get('user_id')) > -1 ){
				acctGrid.getSelectionModel().selectRow(i,true);
				len = len - 1;
				if(len == 0){
					break;//选中一定数量后停止循环
				}
			}
		}
	},
	selectRelativeDevice : function(userRecords){
		var deviceGrid = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid;
		var GDDeviceArray = deviceGrid.GDDeviceArray;
		ok : for(var i=0;i<userRecords.length;i++){
			for(var k=0;k<GDDeviceArray.length;k++){
				var deviceRec = GDDeviceArray[i];
				if(deviceRec.get('ownership') != "USE"){//状态为空闲
					var stbId = userRecords[i].get('stb_id');
					if(stbId != null && stbId == deviceRec.get('device_code')){
						deviceGrid.getSelectionModel().selectRow(k);
						break ok;//跳出循环
					}
					var cardId = userRecords[i].get('card_id');
					if(cardId != null && cardId == deviceRec.get('device_code')){
						deviceGrid.getSelectionModel().selectRow(k);
						break ok;//跳出循环
					}
					var modemMac = userRecords[i].get('modem_mac');
					if(modemMac != null && modemMac == deviceRec.get('device_code')){
						deviceGrid.getSelectionModel().selectRow(k);
						break ok;//跳出循环
					}
				}
				
			}
		}
	},
	refreshPanel : function(busiCode){
/****************客户面板相关业务*************************/
		var panel = App.getApp().main.infoPanel;
		var unitRefresh = false;
		if (App.data.custFullInfo && App.data.custFullInfo.cust.cust_type=='UNIT')
			unitRefresh = true;
		
		if(busiCode == '2000'){			//支付
			panel.getUserPanle().prodGrid.remoteRefresh();
			panel.getPayfeePanel().refresh();
		}else if(busiCode == '1113' ){	//取消支付
			panel.getUserPanel().refresh();
			panel.getPayfeePanel().acctFeeGrid.remoteRefresh();
			panel.getPayfeePanel().busiFeeGrid.remoteRefresh();
			panel.getDoneCodePanel().doneCodeGrid.remoteRefresh();
		}else if(busiCode == '1001' || busiCode == '1002' || busiCode == '2001'){//客户开户、客户销户，刷新客户，重置其他面板isReload为true
			Ext.getDom('q').value='';
			if (unitRefresh){
				panel.getUnitPanel().refresh();
				panel.getUnitPanel().setReload(false);
			}else{
				panel.getCustPanel().refresh();
				panel.getCustPanel().setReload(false);
			}
			
//			panel.getAcctPanel().refresh();
			panel.getUserPanel().refresh();
			panel.getUserPanel().setReload(false);
//			panel.getAcctPanel().setReload(false);
		}else if(busiCode == '1004' || busiCode == '156' || busiCode =='1330' || busiCode =='1114'  ){//修改客户，刷新客户信息和客户异动信息,修改优惠类型
			if (unitRefresh){
				panel.getUnitPanel().custInfoPanel.remoteRefresh();
				panel.getUnitPanel().propChangeGrid.remoteRefresh();
			}else{
				panel.getCustPanel().custInfoPanel.remoteRefresh();
				panel.getCustPanel().refreshPropChangeGrid();
			}
		}else if(busiCode == '1003'||busiCode == '1010'){//过户、移机，刷新客户信息和客户异动信息
			if (unitRefresh){
				panel.getUnitPanel().custInfoPanel.remoteRefresh();
				panel.getUnitPanel().propChangeGrid.remoteRefresh();
			}else{
				panel.getCustPanel().custInfoPanel.remoteRefresh();
				panel.getCustPanel().refreshPropChangeGrid();
			}
			App.getCust().status = 'ACTIVE';
//			panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1005' || busiCode == '1006'
			|| busiCode == '1106' || busiCode == '1105' || busiCode == '1146'){//加入或退出单位，刷新客户基本信息,银行解约
			if (!unitRefresh){
				panel.getCustPanel().custInfoPanel.remoteRefresh();
			}else{
				panel.getUnitPanel().unitMemberGrid.remoteRefresh();
			}
				
		}else if(busiCode == '1007' || busiCode == '1201'){//购买设备终端、修改设备产权，刷新设备和费用,业务费用
//			if (!unitRefresh)
//				panel.getCustPanel().custDeviceGrid.remoteRefresh();
		}else if(busiCode == '1008' || busiCode == '1009' || busiCode == '1011' || busiCode == '1223'
		|| busiCode == '1014' || busiCode == '1013'|| busiCode == '1231' || busiCode == '2002' || busiCode == '2123'){//更换、销售或回收设备,刷新设备,修改购买方式、设备互换
			if (!unitRefresh){
//				panel.getCustPanel().custDeviceGrid.remoteRefresh();
//				panel.getCustPanel().deviceDetailTab.resetPanel();
				panel.getUserPanel().userGrid.remoteRefresh();
//				panel.getAcctPanel().acctGrid.remoteRefresh();
			}
//		}else if(busiCode == '1016' || busiCode == '1017'){//订购套餐，修改套餐
//			if (unitRefresh){
//				panel.getUnitPanel().packageGrid.remoteRefresh();
//			}else{
//				panel.getCustPanel().packageGrid.remoteRefresh();
//				panel.getUserPanel().prodGrid.remoteRefresh();
//			}
//			panel.getAcctPanel().acctGrid.remoteRefresh();
		}else if(busiCode == '1119'){//客户拆迁
			App.getCust().status = 'LYRELOCATE';
			panel.getCustPanel().custInfoPanel.remoteRefresh();
			panel.getCustPanel().refreshPropChangeGrid();
//			panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1985'){//客户迁移
			App.getCust().status = 'ACTIVE';
			panel.getCustPanel().custInfoPanel.remoteRefresh();
			panel.getCustPanel().refreshPropChangeGrid();
//			panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1220'){//恢复客户状态
			panel.getCustPanel().custInfoPanel.remoteRefresh();
			panel.getCustPanel().refreshPropChangeGrid();
//			panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1221'){//恢复用户状态
			panel.getUserPanel().userGrid.remoteRefresh();
		}
/****************用户面板相关业务*************************/		
		else if(busiCode == '1021' || busiCode == '1913' || busiCode == '111' || busiCode == '1130' || busiCode == '2262'){//销户，高级销户，加挂IP,销售设备
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getPayfeePanel().refresh();
		}else if(busiCode == '1020' || busiCode == '2020'){//用户开户、刷新用户和设备，账户
			panel.getUserPanel().userGrid.remoteRefresh();
		}else if(busiCode == '102' || busiCode == '101' || busiCode == '100' ||busiCode == '1015' ||busiCode =='1040'
			|| busiCode == '110' ||busiCode == '109' || busiCode == '1027' || busiCode == '131'){//订购，续订，升级,套餐订购,缴费,高级退订,超级退订，退订
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
			panel.getCustPanel().acctItemGrid.remoteRefresh();
		}else if(busiCode =='1224'){//拆机
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1023' || busiCode == '1018' ||busiCode == '1118' || busiCode == '1074'||busiCode == '1075'
			||busiCode == '1078'||busiCode == '1079'||busiCode == '2270'){//用户修改资料，刷新用户，修改宽带密码，修改最大连接数,取消预报停,修改最大用户数,设备回收
			panel.getUserPanel().userGrid.remoteRefresh();
		}else if(busiCode == '1024' || busiCode == '1025' 
			|| busiCode == '1222' || busiCode == '1129' || busiCode == '1031' || busiCode == '1236' || busiCode == '1224'){
				//报停、报开、开通双向或取消双向、修改接入方式，刷新用户和设备,账户
			//panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().userGrid.remoteRefresh();
			//panel.getAcctPanel().acctGrid.remoteRefresh();
		}else if(busiCode == '1500'){
			panel.getUserPanel().prodGrid.remoteRefresh();
			panel.getCustPanel().acctItemGrid.remoteRefresh();
		}else if( busiCode == '1127' || busiCode == '1034' || busiCode == '1028'|| busiCode == '1072' || busiCode == '1232' || busiCode == '1081'|| busiCode == '1983' || busiCode == '1984' || busiCode == '1036' || busiCode =='1304' ){//产品退订或套餐退订，刷新账户、产品和套餐, //资费变更、协议缴费,失效日期变更、取消变更资费,修改到期日,修改预开通时间,修改公用账目使用类型
			panel.getUserPanel().prodGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
//			if(busiCode == '1015')
//				panel.getBillPanel().billGrid.remoteRefresh();
//			if (unitRefresh)
//				panel.getUnitPanel().packageGrid.remoteRefresh();
//			else
//				panel.getCustPanel().packageGrid.remoteRefresh();
		}else if(busiCode == '1128'){	//更换动态资源
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1030'){//模拟转数
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getCustPanel().custDeviceGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
		}else if(busiCode == '1064'){//促销
			panel.getUserPanel().userGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
		}else if(busiCode == '1080' || busiCode == '1180'){//回退促销
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
//			panel.getAcctPanel().acctGrid.remoteRefresh();
		}else if(busiCode == '1071'){//排斥资源
			panel.getUserPanel().userGrid.remoteRefresh();
		}else if(busiCode == '1038' || busiCode == '1193' || busiCode == '1194'){//第二终端转副机,免费终端(管理员)，免费终端（通用）
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1123'){//充值卡充值
//			panel.getAcctPanel().acctGrid.remoteRefresh();
//			panel.getPayfeePanel().acctFeeGrid.remoteRefresh();
			panel.getDoneCodePanel().doneCodeGrid.remoteRefresh();
		}else if(busiCode == '1303'){
			if (!unitRefresh)
			panel.getUserPanel().prodGrid.remoteRefresh();
		}else if(busiCode == '1022'){
			panel.getUserPanel().userGrid.remoteRefresh();
		}
/****************账户面板相关业务*************************/
		else if(busiCode == '1110' || busiCode == '1112'){//缴费，刷新账户 、用户，费用
			panel.getCustPanel().acctItemGrid.remoteRefresh();
			if (!unitRefresh)
				panel.getUserPanel().userGrid.remoteRefresh();
		}else if(busiCode == '1041' || busiCode == '1042' || busiCode == '1088' || busiCode == '1086' || busiCode == '1166' || busiCode == '1043' || busiCode == '1073' || busiCode == '1116'){//退款、减免费用、补收费用、转账，刷新账户,欠费抹零、各种调账 
			panel.getUserPanel().prodGrid.remoteRefresh();
//			panel.getBillPanel().billGrid.remoteRefresh();
		}else if(busiCode == '1052' || busiCode == '1053'){//修改费用、修改备注
			panel.getDoneCodePanel().doneCodeGrid.remoteRefresh();
		}else if(busiCode == '1061' || busiCode == '1063' || busiCode == '1163'){
			panel.getDocPanel().invoiceGrid.remoteRefresh();
		}else if(busiCode == '1225' || busiCode == '1226' || busiCode == '1501'){
			panel.getPayfeePanel().refresh();
		}else if(busiCode == '1233'){//账目作废退款
			panel.getBillPanel().acctitemInvalidGrid.refresh();
		}else if(busiCode == '2345'){
			panel.getPayfeePanel().refresh();
		}else if(busiCode == '1153'){//调账原因修改
//			panel.getAcctPanel().acctItemDetailTab.acctItemAdjustGrid.remoteRefresh();
		}else if(busiCode == '1688'){//套餐缴费
//			panel.getAcctPanel().acctGrid.remoteRefresh();
			panel.getUserPanel().prodGrid.remoteRefresh();
			panel.getCustPanel().refreshPromFeeGrid();
//			panel.getBillPanel().billGrid.remoteRefresh();9014
		}else if(busiCode == '9014' || busiCode == '2261'){//新增故障单,工单作废
			panel.getUserPanel().userGrid.remoteRefresh();
			panel.getDocPanel().taskGrid.remoteRefresh();
		}
		
		App.getApp().refreshFeeView();
		App.getApp().refreshPayInfo();
		//无论那个业务操作完成，重置这些面板刷新标志
		panel.getPayfeePanel().setReload(true);		
		panel.getDocPanel().setReload(true);
		panel.getDoneCodePanel().setReload(true);
	},
	/**
	 * 根据业务代码判断是否禁用对应按钮，
	 * @param {} busiCode 数组
	 * @param {} bool true ： 禁用，false : 不禁用
	 */
	disableBarByBusiCode : function(tBar,busiCodes,bool){
		if(tBar){
			for(var i=tBar.items.length - 1;i>=0;i--){
				for(var k=0;k<busiCodes.length;k++){
					var item = tBar.items.itemAt(i);
					if(item.attrs && item.disabled !== bool && item.attrs.busicode == busiCodes[k]){
						tBar.items.itemAt(i).setDisabled(bool);
					}
				}
			}
		}
	},
	//判断客户是否是预开户客户
	isPreCust:function(){
		if (App.data.custFullInfo.cust && App.data.custFullInfo.cust.status=='PREOPEN'){
			return true;
		} else {
			return false;
		}
	},
	//获取账务日期
	acctDate:function(acctdatecmp,func){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH + "/commons/x/BusiCommon!acctDate.action",
			success:function(res,ops){
				var date = Date.parseDate(Ext.decode(res.responseText),"Y-m-d h:i:s");
				var date_value;
					date_value = date;
				if (nowDate() > date) {
					date_value = nowDate();
				}
				if(!Ext.isEmpty(acctdatecmp)){
					acctdatecmp.setMinValue(date);
					acctdatecmp.setValue(date_value);
				}else{
					App.baseAcctDate = date;
				}
				if(Ext.isFunction(func)){
					func.call();
				}
			}
		});
	},
	//检验发票是否可用
	checkInvoice:function(invoiceIdCmp,invoiceBookIdCmp,invoice_mode,docType){
		if (!invoice_mode)
			invoice_mode="M";
		var invoiceId = invoiceIdCmp.getValue();
		if (invoiceId.length!=8){
			return;
		}
		Ext.Ajax.request({
			url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
			async: false,
			params:{
				invoice_id:invoiceId,
				invoice_mode:invoice_mode,
				doc_type : docType
			},
			success:function(res,ops){
				var rec = Ext.decode(res.responseText);
				if(rec.length == 0){
					Alert(invoiceId+' 发票无法使用');
					invoiceIdCmp.setValue('');
					invoiceBookIdCmp.setReadOnly(false);
					invoiceBookIdCmp.getStore().removeAll();
					invoiceBookIdCmp.setValue('');
				}
//				else if(rec.length == 1){
//					invoiceBookIdCmp.setValue(rec[0].invoice_book_id);
//					invoiceBookIdCmp.setReadOnly(true);
//				}
				else{
					var data = [];
					for(var i=0;i<rec.length;i++){
						var obj = {};
						obj['invoice_book_id'] = rec[i].invoice_book_id
						obj['invoice'] = rec[i].invoice_book_id+','+rec[i].invoice_code;
						data.push(obj);
					}
					invoiceBookIdCmp.getStore().loadData(data);
					invoiceBookIdCmp.setValue(invoiceBookIdCmp.getStore().getAt(0).get('invoice_book_id'));
				}
			},
			clearData:function(){
				invoiceIdCmp.setValue('');
				if(invoiceBookIdCmp){
					invoiceBookIdCmp.getStore().loadData({});
				}
			}
		});
	},
	getUrlParam:function (param) {   
		    var r = new RegExp("\\?(?:.+&)?" + param + "=(.*?)(?:&.*)?$");   
		    var m = window.location.toString().match(r);   
		    return m ? m[1] : ""; //如果需要处理中文，可以用返回decodeURLComponent(m[1])   
	},	
	//post方式打开新窗口
	openWindowWithPost : function(url, name, keys, values) {
		var newWindow = window.open('', name);
		if (!newWindow)
			return false;

		var html = "";
		html += "<html><head></head><body><form id='formid' method='post' action='"
				+ url + "'>";
		for (var i = 0; i < keys.length; i++) {
			if (keys[i] && values[i]) {
				html += "<input type='hidden' name='" + keys[i] + "' value='"
						+ values[i] + "'/>";
			}
		}

		html += "</form><script type='text/javascript'>document.getElementById('formid').submit();";
		html += "<\/script></body></html>".toString().replace(
				/^.+?\*|\\(?=\/)|\*.+?$/gi, "");
		newWindow.document.write(html);

		return newWindow;
	},
	//增加在线用户的操作记录,只是存session
	addOnlineUser: function(t){
		if(t && t.res_name){
			Ext.Ajax.request({
				url:root + '/addSession.action',
				params:{resName : t.res_name},
				scope:this,
				success:function(res,opt){
				}
			});	
		}
	},
	/**
	 * 停机或者其他业务在执行前需要判断先关用户 基本包产品是否欠费
	 * 返回对象: array 需要缴费的产品acct_id
	 */
	getUserBaseProdOweFeeAccts:function(selectedUseridArr){
		var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
		if(!selectedUseridArr){
			selectedUseridArr = userGrid.getSelectedUserIds();
		}
		var acctGrid = App.getApp().main.infoPanel.acctPanel.acctGrid;
		var oweFeeAcctids = [];
		//遇到过零资费 欠费未停的数据,如果是正常现象,回头再根据产品资费进行处理,暂时认为是错误数据.
		var store = acctGrid.acctStore;
		for(var index =0;index<store.getCount();index++){
			var acct = store.getAt(index);
			var user_id = acct.get('user_id');
			var acctId = acct.get('acct_id');
			if(Ext.isEmpty(user_id) || !selectedUseridArr.contain(user_id) || oweFeeAcctids.contain(acctId) ){
				continue;
			}
			var items = acct.get('acctitems');
			if(items && items.length >0){
				for(var idx =0;idx<items.length;idx++){
					var item = items[idx];
					if(item.real_balance < 0 ){
						oweFeeAcctids.push(acctId);
					}
				}
			}
			
		}
		return oweFeeAcctids;
	},
	getTargetWithEvent: function(selector){
		var extEvent = new Ext.EventObjectImpl(window.event);
		return new Ext.Element(extEvent.getTarget()).parent(selector);
	},
	getFeeGridWithTargetElement: function(){
		var gridDom = App.getApp().getTargetWithEvent("DIV.x-grid-panel");
		if("P_BUSI" == gridDom.id){
			return App.main.infoPanel.getPayfeePanel().busiFeeGrid;
		}else if("P_ACCT" == gridDom.id){
			return App.main.infoPanel.getPayfeePanel().acctFeeGrid;
		}
		return null;
	},
	alertMsg:function(msg){
		Alert(msg);
	}
});
