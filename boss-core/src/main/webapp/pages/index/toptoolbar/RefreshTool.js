Ext.ns("App");

Ext.apply( App, {
	
	refrshTool : function() {
		//操作员信息
		var data = App.data;
		App.tool.insert(2,{
			text: langUtils.tools("qhbm"),
			iconCls: 'icon-t-dept',
			handler : App.tool.onToggleDept
		});
		App.tool.insert(2,{text: langUtils.tools("gg") ,iconCls:'bulletin',listeners:{
				click:function(){
					App.tool.bulletinAllWin();
				}
			}
		});
		App.tool.insert(3,'-');
		

		//操作员权限包含的其他业务按钮
		var menuItems = [], reloadMeuItems = [], resources = App.data.resources;
		for(var i=0;i<resources.length;i++){
			var res = resources[i];
			var resId = resources[i]["attrs"]["res_id"];
			var handler = App.data.resources[i].handler;
			var text = langUtils.res(resId);
			text = Ext.isArray(text)? text[0] : text;
			if(handler == 'queryDeviceId'){
				menuItems.push({
					itemId:'queryDeviceId',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'queryInvoiceId'){
				menuItems.push({
					itemId:'queryInvoiceId',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'openOTTMobile'){
				menuItems.push({
					itemId:'openOTTMobile',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'checkAccountId'){
				menuItems.push({
					itemId:'checkAccountId',
					text: text,
					iconCls:'gz',
					attrs : res
				});
			}else if(handler == 'DeviceCountCheck'){
				menuItems.push({
					itemId:'DeviceCountCheck',
					text: text,
					iconCls:'gz',
					attrs : res
				});
			}else if(handler == 'UserCountCheck'){
				menuItems.push({
					itemId:'UserCountCheck',
					text: text,
					iconCls:'gz',
					attrs : res
				});
			}else if(handler == 'feeUnitpreId'){
				menuItems.push({
					itemId:'feeUnitpreId',
					text: text,
					iconCls:'pay',
					attrs : res
				});
			}else if(handler == 'batchPayFeeId'){
				menuItems.push({
					itemId:'batchPayFeeId',
					text: text,
					iconCls:'pay',
					attrs : res
				});
			}else if(handler == 'batchEditAcctDateId'){
				menuItems.push({
					itemId:'batchEditAcctDateId',
					text: text,
					iconCls:'editCust',
					attrs : res
				});
			}else if(handler == 'BatchNewCust'){
				menuItems.push({
					itemId:'batchOpenCust',
					text: text,
					iconCls:'newCust',
					attrs : res
				});
			}else if(handler == 'BatchLogoffCust'){
				menuItems.push({
					itemId:'batchLogoffCust',
					text: text,
					iconCls:'delCust',
					attrs : res
				});
			}else if(handler == 'BatchLogoffUser'){
				menuItems.push({
					itemId:'batchLogoffUser',
					text: text,
					iconCls:'logoffUser',
					attrs : res
				});
			}else if(handler == 'BatchOrderProd'){
				menuItems.push({
					itemId:'batchOrderProd',
					text: text,
					iconCls:'orderProd',
					attrs : res
				});
			}else if(handler == 'BatchCancelProd'){
				menuItems.push({
					itemId:'batchCancelProd',
					text: text,
					iconCls:'cancelProd',
					attrs : res
				});
			}else if(handler == 'ConfigBranchCom'){
				menuItems.push({
					itemId:'ConfigBranchCom',
					text: text,
					iconCls:'newCust',
					attrs : res
				});
			}else if(handler == 'valuableCardWinId'){
				menuItems.push({
					itemId:'valuableCardWinId',
					text: text,
					iconCls:'icon_card',
					attrs : res
				});
			}else if(handler == 'updateAddressId'){
				menuItems.push({
					itemId:'updateAddressId',
					text: text,
					iconCls:'changeAddr',
					attrs : res
				});
			}else if(handler == 'queryVoucherId'){
				menuItems.push({
					itemId:'queryVoucherId',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'payRemindId'){
				Ext.Ajax.request({
						url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryImportanceCustNum.action",
						success : function(res, ops) {
							var rs = Ext.decode(res.responseText);
							var text  = "";
							var key = false;
							if(rs.simpleObj === true){
								key = true;
								text  = "续费提醒【未配置数据角色】";
							}else{
								text  = "续费提醒【"+rs.simpleObj+"】";
							}
							if(!key){
								App.tool.insert(2,{text:text,iconCls:'warning',disabled:key,listeners:{
										click:function(){								
												App.tool.remindCustWin();
										}
									}
								})
							}
							App.tool.insert(3,'-');
							App.tool.doLayout();
					    }
					});

			}else if(handler == 'editCustStatusId'){
				//将客户状态修改为"资料隔离"
				menuItems.push({
					itemId:'editCustStatusId',
					text: text,
					iconCls:'editUserStatus',
					attrs : res
				});
			}else if(handler == 'editUserStatusId'){
				//将用户状态修改为"休眠"或"关模隔离"
				menuItems.push({
					itemId:'editUserStatusId',
					text: text,
					iconCls:'editUserStatus',
					attrs : res
				});
			}else if(handler == 'checkMobileBill'){
				//未支付结账
				menuItems.push({
					itemId:'checkMobileBill',
					text: text,
					iconCls:'pay',
					attrs : res
				});
			}else if(handler == 'queryBandOnlineUser'){
				menuItems.push({
					itemId:'queryBandOnlineUser',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'queryBandUserFailedLog'){
				menuItems.push({
					itemId:'queryBandUserFailedLog',
					text: text,
					iconCls:'query',
					attrs : res
				});
			}else if(handler == 'sendCaCardId'){
				menuItems.push({
					itemId:'sendCaCardId',
					text: text,
					iconCls:'icon-busi',
					attrs : res
				});
			}else if('TaskManager' == handler){
				menuItems.push({
					itemId: 'TaskManager',
					text: text,
					iconCls: res.iconCls,
					attrs : res
				});
			}else if('AddressViewWin' == handler){
				menuItems.push({
					itemId: 'AddressViewWin',
					text: text,
					iconCls: res.iconCls,
					attrs : res
				});
			}else if('reloadConfig' == handler){
				reloadMeuItems.push({
					itemId: 'reloadConfig',
					text: langUtils.tools("czpz"),
					iconCls:'icon-t-county'
				});
			}else if('reloadPrintData' == handler){
				reloadMeuItems.push({
					itemId: 'reloadPrintData',
					text: langUtils.tools("czfpsj"),
					iconCls:'icon-t-county'
				});
			}else if('OsdSendViewWin' == handler){
				menuItems.push({
					itemId: 'OsdSendViewWin',
					text: text,
					iconCls: res.iconCls,
					attrs : res
				});
			}
		}
		
		if(menuItems.length > 0){
			//只在营业系统中显示"发票结账"
			App.tool.insert(4,{
				id:'otherBusiMenuId',
				text: langUtils.tools("qtyw"),
				iconCls:'others',
				menu: new Ext.menu.Menu({
					items:menuItems,
					listeners: {
						scope: App.tool,
						itemclick: function(item){
							var itemId = item.itemId;
							if(itemId!='batchOpenCust' || itemId!='ConfigBranchCom' ){
								//添加操作员正在使用的功能,goUrl()调用bigWindow已经存在以下方法
								if(item.attrs){
									App.addOnlineUser(item.attrs.attrs);
								}
							}
							var busiCode = item.attrs.attrs.busicode;
							if(App.getData().deptBusiCode.contain(busiCode) ){
								Alert('当前部门无法进行 “' + item.attrs.attrs.businame + '” 这项业务!');
								return;
							}
							if(itemId === 'queryDeviceId'){
								App.tool.showDeviceWin();
							}else if(itemId === 'queryInvoiceId'){
								App.tool.showInvoiceWin();
							}else if(itemId === 'openOTTMobile'){
								App.tool.showOpenOTTMobileeWin();
							}else if(itemId === 'closeInvoiceId'){
								App.tool.showCloseInvoiceWin();
							}else if(itemId === 'checkAccountId'){
								App.tool.showCheckAcctountWin();
							}else if(itemId === 'DeviceCountCheck'){
								App.tool.showDeviceCountCheckWin();
							}else if(itemId === 'UserCountCheck'){
								App.tool.showUserCountCheckWin();
							}else if(itemId === 'feeUnitpreId'){
								App.tool.showFeeUnitpreWin();
							}else if(itemId === 'batchPayFeeId'){
								App.tool.showBatchPayFeeWin();
							}else if(itemId === 'batchEditAcctDateId'){
								App.tool.showBatchEditAcctDateWin();
							}else if(itemId == 'batchOpenCust'){
								goUrl(item.attrs);
							}else if(itemId == 'batchLogoffCust'){
								App.tool.showBatchLogoffCustWin();
							}else if(itemId == 'batchLogoffUser'){
								App.tool.showBatchLogoffUserWin();
							}else if(itemId == 'batchOrderProd'){
								App.tool.showBatchOrderProdWin();
							}else if(itemId == 'batchCancelProd'){
								App.tool.showBatchCancelProdWin();
							}else if(itemId == 'ConfigBranchCom'){
								goUrl(item.attrs);
							}else if(itemId == 'valuableCardWinId'){
								App.tool.showvaluableCardWin();
							}else if(itemId == 'updateAddressId'){
								App.tool.showupdateAddressWin();
							}else if(itemId == 'queryVoucherId'){
								App.tool.showVoucherWin();
							}else if(itemId == 'editCustStatusId'){
								App.tool.showCustStatusWin();
							}else if(itemId == 'editUserStatusId'){
								App.tool.showUserStatusWin();
							}else if(itemId == 'checkMobileBill'){
								App.tool.showMobileBillWin();
							}else if(itemId == 'queryBandOnlineUser'){
								App.tool.showBandOnlineUserWin();
							}else if(itemId == 'queryBandUserFailedLog'){
								App.tool.showBandUserFailedLogWin();
							}else if(itemId == 'sendCaCardId'){
								App.tool.showCaCardWin();
							}else if(itemId == 'TaskManager'){
								App.tool.showTaskManagerWin(item);
							}else if(itemId == 'AddressViewWin'){
								App.tool.showAddressViewWin(item);
							}else if(itemId == 'OsdSendViewWin'){
								App.tool.showOsdSendViewWin(item);
							}
						}
					}
				})
			});
			
			App.tool.insert(5,'-');
	
		}
		
		if(reloadMeuItems.length > 0){
			App.tool.insert(6,{
				text: langUtils.tools("gj"),
				iconCls:'icon-t-county',
				menu: new Ext.menu.Menu({
					items:reloadMeuItems,
					listeners: {
						scope: App.tool,
						itemclick: function(item){
							if(item.itemId === 'reloadConfig')
								App.tool.reloadConfig();
							else if(item.itemId == 'reloadPrintData'){
								App.tool.reloadPrintData();
							}
						}
					}
				})
			});
			App.tool.insert(7,'-');
		}
		
		App.tool.doLayout();
	},

	refreshFeeView: function(){
		
		var width = 120,leftWidth = 0;
		var pLeft = Ext.isIE ? 'padding-left:10px':'';
		var toolstr = "<div style='width:"+width+"px;float:left;padding-left:50px' >" +
				"<div class='top_button print_big' onClick='App.openPrint()'></div>" +
				"<div style='float:left;padding-left:10px;font:11px'>"+lbc('home.tools.InvoicePrint._title')+"</div></div>";
//		toolstr +="<div style='width:"+width+"px;float:left;padding-left:"+leftWidth+"px;' >" +
//				"<div class='top_button print_big' onClick='App.openBankPayment()'>" +
//				"</div><div style='font:11px'>银行打印</div></div>";
//		toolstr +="<div style='width:"+width+"px;float:left;padding-left:"+leftWidth+"px;' >" +
//				"<div class='top_button config_big' onClick='App.openBusiPrint()'>" +
//				"</div><div style='font:11px'>受理单打印</div></div>";
		Ext.get('tool').update(toolstr);
		
 	},
 	// 刷新支付信息
 	refreshPayInfo: function(W){
 		// 请求后台的数据
 		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH + "/core/x/Pay!queryUnPaySum.action",
			params : {cust_id : App.getCustId()},
			success : function(res, opt){
				W = W || window;
				var data = W.Ext.decode(res.responseText);
				var count = data["CNT"], payfeeAmount = data["FEE"];
				// 删除当前的支付DOM
				var el = Ext.get("payAllContainer");
				
				if(el){ el.remove(); }
				if(count > 0){
					Ext.get('tool').insertHtml("beforeEnd",'<div id="payAllContainer">'
							+'<div class="amount">'+lmsg("payInfo", null, [count, payfeeAmount/100.0])+'</div>' 
							+'<div class="nowToPayBtn"><button onclick="App.openPay()">'+ lmsg("payButton")+'</button></div>'
						+'</div>');
				}
			}
		});
 	},
 	openPrint:function(){
 		App.getApp().data.currentResource = {busicode:'1068'};
		App.getApp().menu.bigWindow.show({ text: lbc('home.tools.InvoicePrint._title'),  attrs: {busiCode:'1068',
				url: 'pages/business/pay/Print.jsp?type=through'}} ,{width: 710, height: 460});
 		
 	},
 	openConfigPrint:function(){
 		App.getApp().menu.bigWindow.show({ text: '确认单打印',  attrs: {busiCode:'1069',
					url: 'pages/business/pay/ConfigPrint.jsp'}} ,{width: 710, height: 460});
 	},
 	openFeePrint:function(){
 		App.getApp().menu.bigWindow.show({ text: '账单打印',  attrs: {busiCode:'1101',
					url: 'pages/business/pay/PrintBill.jsp'}} ,{width: 660, height: 250});
 	},
 	openPay:function(){
 		App.getApp().menu.bigWindow.show({ text: langUtils.main("cashPay._title"),  attrs: {busiCode:'1027',
					url: 'pages/business/pay/PayForm.js,pages/business/pay/PayPanel.js'}} ,{width: 850, height: 600});
 	},
 	openBankPayment:function(){
		App.getApp().data.currentResource = {busicode:'2000'};
 		App.getApp().menu.bigWindow.show({ text: '银行打印',  attrs: {busiCode:'2000',
					url: 'pages/business/pay/BankPaymentPrint.jsp?type=bank'}} ,{width: 710, height: 460});
 	},
 	
	refreshBusiDocPanel:function(rePrint){
		App.printWindow.hide();
		//document.getElementById('busiDocPrintFrame').src = 'pages/business/pay/ServiceDocPrintFrame.jsp';
		if(rePrint){
			MenuHandler.RePrintBusiDoc();
		}else{
			App.openBusiPrint();
		}
	},
 	openBusiPrint:function(rePrint){
 		if(!App.getApp().data.currentResource){
	 		App.getApp().data.currentResource = {busicode:'1269'};
 		}
 		App.printWindow = new Ext.Window({
 			width: 860,
 			height: 550,
 			//maximized: true,
 			maximizable: true,
 			title: rePrint? '重打受理单' :'受理单打印',
			html: "<iframe id='busiDocPrintFrame' width='100%' height='100%'"
						 +" frameborder=no  src='pages/business/pay/ServiceDocPrintFrame.jsp'></iframe>",
			tbar: ['->','-',
				
				{
				tooltip: '重载模板文件',
				width: 50,
				height: 35,
				scope: this,
				scale: 'large',
				iconCls: 'billing_big',
				handler: function(){
					Ext.Ajax.request({
						url: "/" + Constant.ROOT_PATH_CORE + '/core/x/Print!reloadTemplate.action',
						params: {},
						success: function(res, ops){
							alert("重载模板成功!");
							App.getApp().refreshBusiDocPanel(rePrint);
						}
					});
				}
			},'-',
			{
				width: 50,
				height: 35,
				tooltip: '打印业务受理单',
				disabled:true,
				scope: this,
				id:'doc_print_btn',
				scale: 'large',
				iconCls: 'print_big',
				handler: function(){
					var btn = Ext.getCmp('doc_print_btn');
					btn.disable.defer(1050,btn);
					
					var frame = window.frames["busiDocPrintFrame"];
					var custId = frame['custId'];
					var doneCodes = frame['doneCodes'];
					var docSn2BePrint = frame['docSn2BePrint']; 
					
					frame.focus();
					frame.print();
					
					var all = {};
					var values = App.getApp().getValues();
					if(!App.getApp().getData().currentResource || App.getApp().getData().currentResource.busicode != '1163'){
						values['busiCode'] = '1269';						
					}
					Ext.apply(all,{custId:custId,doneCodes:doneCodes,docSn:docSn2BePrint});
					all[CoreConstant.JSON_PARAMS]=Ext.encode(values);
					if(!Ext.isEmpty(custId) && !Ext.isEmpty(doneCodes) && doneCodes.length>0){
						Ext.Ajax.request({
							url:'/' + Constant.ROOT_PATH_CORE + '/core/x/Print!saveDoc.action',
							params:all,
							success:function(res){
								
							}
						});
					}
					App.getApp().refreshPanel("1168");
					frame.close();
				}
			},'-',' ',' ',' ',' ',' ',' ',' ',' ',' ']
 		});
 		App.printWindow.show();
 		var shouldDisabled = false;
 		if(rePrint){//重打
 			var record = App.main.infoPanel.docPanel.busiDocGrid.getSelectionModel().getSelected();
 			var printDteStr = record.get('create_time');
	  		if(!Ext.isEmpty(printDteStr) && printDteStr.trim().length >= 10){
	  			printDteStr = printDteStr.trim().substring(0,10);
	  		}
 			var notToday = !Ext.isEmpty(printDteStr) && printDteStr != serverDate.clone().format('Y-m-d');
			var notCurrentOptr = App.getData().optr.optr_id != record.get('optr_id');
 			if (notToday || notCurrentOptr) {//不是今天的业务单,不是当前业务员打印的
	 			shouldDisabled = true;
			}
 		}
 		var btn = Ext.getCmp('doc_print_btn');
 		btn.setDisabled(shouldDisabled);
 	}
});	