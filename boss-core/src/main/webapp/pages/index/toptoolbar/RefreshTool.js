Ext.ns("App");

Ext.apply( App, {
	
	refrshTool : function() {
		//操作员信息
		var data = App.data;
		var deptmenu ;
			App.tool.insert(2,{
				text: '切换部门',
				iconCls: 'icon-t-dept',
				handler : App.tool.onToggleDept
			});
		if (data.optr['login_name']=='admin'){
			//工具菜单
			App.tool.insert(2, {
				text:'工具',
				iconCls:'icon-t-county',
				menu: new Ext.menu.Menu({
					items:[	
						{
							itemId:'reloadConfig',
							text:'重载配置',
							iconCls:'icon-t-county'
						},
						{
							itemId:'reloadPrintData',
							text:'重载发票数据',
							iconCls:'icon-t-county'
						}
					],
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
			} );
			App.tool.insert(3,'-');
		}
		
		App.tool.insert(2,{text:'公告',iconCls:'bulletin',listeners:{
				click:function(){
					App.tool.bulletinAllWin();
				}
			}
		});
		App.tool.insert(3,'-');
		

		//操作员权限包含的其他业务按钮
		var menuItems = [];
		for(var i=0;i<App.data.resources.length;i++){
			if(App.data.resources[i].handler == 'queryDeviceId'){
				menuItems.push({
					itemId:'queryDeviceId',
					text:'设备查询',
					iconCls:'query',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'queryInvoiceId'){
				menuItems.push({
					itemId:'queryInvoiceId',
					text:'发票查询',
					iconCls:'query',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'checkAccountId'){
				menuItems.push({
					itemId:'checkAccountId',
					text:'轧账',
					iconCls:'gz',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'DeviceCountCheck'){
				menuItems.push({
					itemId:'DeviceCountCheck',
					text:'终端数轧账',
					iconCls:'gz',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'UserCountCheck'){
				menuItems.push({
					itemId:'UserCountCheck',
					text:'用户数轧账',
					iconCls:'gz',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'feeUnitpreId'){
				menuItems.push({
					itemId:'feeUnitpreId',
					text:'非营业费收取',
					iconCls:'pay',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'batchPayFeeId'){
				menuItems.push({
					itemId:'batchPayFeeId',
					text:'批量收费',
					iconCls:'pay',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'batchEditAcctDateId'){
				menuItems.push({
					itemId:'batchEditAcctDateId',
					text:'修改账务日期',
					iconCls:'editCust',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'BatchNewCust'){
				menuItems.push({
					itemId:'batchOpenCust',
					text:'批量预开户',
					iconCls:'newCust',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'BatchLogoffCust'){
				menuItems.push({
					itemId:'batchLogoffCust',
					text:'批量销客户',
					iconCls:'delCust',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'BatchLogoffUser'){
				menuItems.push({
					itemId:'batchLogoffUser',
					text:'批量销用户',
					iconCls:'logoffUser',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'BatchOrderProd'){
				menuItems.push({
					itemId:'batchOrderProd',
					text:'批量订购产品',
					iconCls:'orderProd',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'BatchCancelProd'){
				menuItems.push({
					itemId:'batchCancelProd',
					text:'批量退订产品',
					iconCls:'cancelProd',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'ConfigBranchCom'){
				menuItems.push({
					itemId:'ConfigBranchCom',
					text:'配置分公司账户',
					iconCls:'newCust',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'valuableCardWinId'){
				menuItems.push({
					itemId:'valuableCardWinId',
					text:'充值卡销售',
					iconCls:'icon_card',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'updateAddressId'){
				menuItems.push({
					itemId:'updateAddressId',
					text:'批量修改地址',
					iconCls:'changeAddr',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'queryVoucherId'){
				menuItems.push({
					itemId:'queryVoucherId',
					text:'代金券查询',
					iconCls:'query',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'payRemindId'){
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

			}else if(App.data.resources[i].handler == 'editCustStatusId'){
				//将客户状态修改为"资料隔离"
				menuItems.push({
					itemId:'editCustStatusId',
					text:'批量修改客户状态',
					iconCls:'editUserStatus',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'editUserStatusId'){
				//将用户状态修改为"休眠"或"关模隔离"
				menuItems.push({
					itemId:'editUserStatusId',
					text:'批量修改用户状态',
					iconCls:'editUserStatus',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'checkMobileBill'){
				//未支付结账
				menuItems.push({
					itemId:'checkMobileBill',
					text:'未支付结账',
					iconCls:'pay',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'queryBandOnlineUser'){
				menuItems.push({
					itemId:'queryBandOnlineUser',
					text:'宽带在线用户查询',
					iconCls:'query',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'queryBandUserFailedLog'){
				menuItems.push({
					itemId:'queryBandUserFailedLog',
					text:'宽带认证失败日志查询',
					iconCls:'query',
					attrs : App.data.resources[i]
				});
			}else if(App.data.resources[i].handler == 'sendCaCardId'){
				menuItems.push({
					itemId:'sendCaCardId',
					text:'一体机授权',
					iconCls:'icon-busi',
					attrs : App.data.resources[i]
				});
			}else if('TaskManager' == App.data.resources[i].handler){
				menuItems.push({
					itemId: App.data.resources[i].handler,
					text: App.data.resources[i].text,
					iconCls: App.data.resources[i].iconCls,
					attrs : App.data.resources[i]
				});
			}
		}
		
		if(menuItems.length > 0){
			//只在营业系统中显示"发票结账"
			App.tool.insert(4,{
				id:'otherBusiMenuId',
				text:'其他业务',
				iconCls:'others',
				menu: new Ext.menu.Menu({
					items:menuItems,
					listeners: {
						scope: App.tool,
						itemclick: function(item){
							if(item.itemId!='batchOpenCust' || item.itemId!='ConfigBranchCom' ){
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
							if(item.itemId === 'queryDeviceId'){
								App.tool.showDeviceWin();
							}else if(item.itemId === 'queryInvoiceId'){
								App.tool.showInvoiceWin();
							}else if(item.itemId === 'closeInvoiceId'){
								App.tool.showCloseInvoiceWin();
							}else if(item.itemId === 'checkAccountId'){
								App.tool.showCheckAcctountWin();
							}else if(item.itemId === 'DeviceCountCheck'){
								App.tool.showDeviceCountCheckWin();
							}else if(item.itemId === 'UserCountCheck'){
								App.tool.showUserCountCheckWin();
							}else if(item.itemId === 'feeUnitpreId'){
								App.tool.showFeeUnitpreWin();
							}else if(item.itemId === 'batchPayFeeId'){
								App.tool.showBatchPayFeeWin();
							}else if(item.itemId === 'batchEditAcctDateId'){
								App.tool.showBatchEditAcctDateWin();
							}else if(item.itemId == 'batchOpenCust'){
								goUrl(item.attrs);
							}else if(item.itemId == 'batchLogoffCust'){
								App.tool.showBatchLogoffCustWin();
							}else if(item.itemId == 'batchLogoffUser'){
								App.tool.showBatchLogoffUserWin();
							}else if(item.itemId == 'batchOrderProd'){
								App.tool.showBatchOrderProdWin();
							}else if(item.itemId == 'batchCancelProd'){
								App.tool.showBatchCancelProdWin();
							}else if(item.itemId == 'ConfigBranchCom'){
								goUrl(item.attrs);
							}else if(item.itemId == 'valuableCardWinId'){
								App.tool.showvaluableCardWin();
							}else if(item.itemId == 'updateAddressId'){
								App.tool.showupdateAddressWin();
							}else if(item.itemId == 'queryVoucherId'){
								App.tool.showVoucherWin();
							}else if(item.itemId == 'editCustStatusId'){
								App.tool.showCustStatusWin();
							}else if(item.itemId == 'editUserStatusId'){
								App.tool.showUserStatusWin();
							}else if(item.itemId == 'checkMobileBill'){
								App.tool.showMobileBillWin();
							}else if(item.itemId == 'queryBandOnlineUser'){
								App.tool.showBandOnlineUserWin();
							}else if(item.itemId == 'queryBandUserFailedLog'){
								App.tool.showBandUserFailedLogWin();
							}else if(item.itemId == 'sendCaCardId'){
								App.tool.showCaCardWin();
							}else if(item.itemId == 'TaskManager'){
								App.tool.showTaskManagerWin(item);
							}
						}
					}
				})
			});
			
			App.tool.insert(5,'-');
	
		}
		
		App.tool.doLayout();
	},

	refreshFeeView: function(){
		
		var width = 70,leftWidth = 0;
		var pLeft = Ext.isIE ? 'padding-left:10px':'';
		var toolstr = "<div style='width:"+width+"px;float:left;"+pLeft+"' >" +
				"<div class='top_button print_big' onClick='App.openPrint()'></div>" +
				"<div style='font:11px'>" +
				"&nbsp;&nbsp;发票打印</div></div>";
		toolstr +="<div style='width:"+width+"px;float:left;padding-left:"+leftWidth+"px;' >" +
				"<div class='top_button print_big' onClick='App.openBankPayment()'>" +
				"</div><div style='font:11px'>银行打印</div></div>";
		toolstr +="<div style='width:"+width+"px;float:left;padding-left:"+leftWidth+"px;' >" +
				"<div class='top_button config_big' onClick='App.openBusiPrint()'>" +
				"</div><div style='font:11px'>受理单打印</div></div>";
				
		Ext.get('tool').update(toolstr);
		
 	},
 	openPrint:function(){
 		App.getApp().data.currentResource = {busicode:'1068'};
 		if (App.data.custFullInfo.cust.cust_type=='UNIT'){
 			App.getApp().menu.bigWindow.show({ text: '发票打印',  attrs: {busiCode:'1068',
					url: 'pages/business/pay/Print.jsp?type=unit'}} ,{width: 710, height: 460});
 		}else{
 			App.getApp().menu.bigWindow.show({ text: '发票打印',  attrs: {busiCode:'1068',
					url: 'pages/business/pay/Print.jsp?type=through'}} ,{width: 710, height: 460});
 		}
 		
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
 		App.getApp().menu.bigWindow.show({ text: '支付',  attrs: {busiCode:'1027',
					url: 'pages/business/pay/PayForm.js,pages/business/pay/PayPanel.js'}} ,{width: 790, height: 460});
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
