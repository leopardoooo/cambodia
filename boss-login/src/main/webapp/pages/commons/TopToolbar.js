/**
 *  首页顶部显示的工具栏
 */
TopToolbar = Ext.extend(Ext.Toolbar , {
	sysmenudatas: [],
	deptmenus: [],
	deptmenu : null,
	defaultChecked: null,
	showMsg:null,
	constructor: function( cfg ){
		Ext.apply( this ,cfg || {});
		
		this.initData();
		
		TopToolbar.superclass.constructor.call(this ,{
			height: 26,
			cls: 'site-tool-bg',
		    items: ['->','-',{
				text: langUtils.tools("qhxt"),
				iconCls: 'icon-t-sys',
				menu: new Ext.menu.Menu({
					id:'subsustemMenuId',
					items:[],
					listeners: {
						scope: this,
						itemclick: this.onToggleSystem
					}
				})
			},'-',{
				text: langUtils.tools("grxg"),
				iconCls: 'icon-password',
				handler: this.showOptrDataWin
			},'-',{
				text: langUtils.tools("aqtc"),
				iconCls: 'icon-exit',
				handler: this.onSafetyExit
			},'-',{
				iconCls: 'icon-help',
				text: langUtils.tools("bz"),
				menu: [{
					text: langUtils.tools("bsbz"),
					handler: this.openHelpPage
				},{
					text: langUtils.tools("gywm"),
					handler: this.aboutUs
				}]
			},' ',' ',' ',' ']
		});
		this.refershOptrInfo();
	},
	initData: function(){
		this.defaultChecked = this.defaultChecked || "1";
		var url = "";
		if(this.defaultChecked === "1"){
			//core
			url = root + '/commons/x/BusiCommon!queryAllSubSystem.action';
		}else if(this.defaultChecked === "7"){
			//report
			url = root + '/system/Index!queryAllSubSystem.action';
		}else{
			//sysmanger
			url = root + '/system/Resource!queryAllSubSystem.action';
		}
		
		App.data.sysId = this.defaultChecked;
		
		Ext.Ajax.request({
			url:url,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				for(var i=0;i<data.length;i++){
					var obj={};
//					obj["text"]=data[i]["sub_system_name"];
					obj["itemId"]=data[i]["sub_system_id"];
					obj['text'] = langUtils.tools('subsystem')[data[i]["sub_system_id"]];
					obj["url"]=data[i]["sub_system_url"];
					obj["root"]=data[i]["sub_system_host"];
					obj["iconCls"]=data[i]["iconcls"];
					this.sysmenudatas.push(obj);
				}
				for(var i=0;i<this.sysmenudatas.length;i++){
					var item = this.sysmenudatas[i];
					if(item.itemId == this.defaultChecked ){
						item.text = "<b'>"+ item.text +"</b>";
						item.disabled = true;
					}
				}
				
				Ext.getCmp('subsustemMenuId').add(this.sysmenudatas);
			}
		});

		if (App.data&&App.data.depts){
			var depts=App.data.depts;
			for (var i=0;i<depts.length;i++){
				var item = {
					text : depts[i].dept_name,
					itemId : depts[i].dept_id
					};
				if (App.data.optr.dept_id==item.itemId)
					item.disabled = true;
				this.deptmenus[i] = item;
			}
			this.deptmenu = new Ext.menu.Menu({
							items : this.deptmenus,
							listeners : {
								scope : this,
								itemclick : this.onToggleDept
							}
						});
		}


	},	
	onToggleSystem: function(item){
		if(item.itemId == this.defaultChecked){
			return;
		}
		var isContinue = true;
		window.location.href = regourl+'/rego?sub_system_id='+item.itemId;
//		Confirm("确定要切换系统至 "+ item.text +"",null,function(){
////			window.location.href = item.root + "/rego?url=" + item.url+"&tokenId=" + token_id+"&sub_system_id="+item.itemId;
//		});
	},
	onSafetyExit: function(){
		Confirm(langUtils.bc('common.tipExistsSystem'),null,function(){
			App.href(Constant.ROOT_PATH + "/gologin");
		})
	},
	openHelpPage: function(){
		var width = screen.width - 10;
		var height = screen.height - 60;
		var property = 'resizable=no,'
					 + 'status=no,'
					 + 'menubar=no,'
					 + 'scrollbars=yes,'
					 + 'location=no,'
					 + 'toolbar=no,left=0px,top=0px,'
					 + 'width=' + width + ','
					 + 'height=' + height;
		window.location.href = "/help/Train.doc";			 
//		var win = window.open("/help/Train.doc",'', property);
	},
	aboutUs: function(){
		var win = Ext.getCmp('aboutUsId');
		if(win){
			win.show();
		}else{
			new Ext.Window({
				id:'aboutUsId',
				title:'服务器',
				width:250,
				height:110,
				maximizable:false,
				layout: 'fit',
				bodyStyle:'padding-top:15px',
				html:'<center>'+bossBasePath+'</center>',
				buttonAlign:'right',
				buttons:[{text:langUtils.bc('common.close'),handler:function(){
						Ext.getCmp('aboutUsId').close();
					}
				}]
			}).show();
		}
	},	
	onToggleDept: function(){
		var win = Ext.getCmp('deptSelectWinId');
		if(!win)
			win = new DeptSelectWin(App.data.sysId);
		win.show();
	},
	refershOptrInfo : function() {
		var optr = App.getData().optr;
		this.insert(0,langUtils.bc("home.topWelcome") + ' : ' + optr['optr_name'] + '('
						+ optr['county_name'] + '-'
						+ optr['dept_name'] + ')');
		this.doLayout();
	},
	showDeviceWin:function(){
		var win = Ext.getCmp('queryDeviceWinId');
		if(!win)
			win = new QueryDeviceWin();
		win.show();
	},
	showInvoiceWin:function(){
		var win = Ext.getCmp('queryInvoiceWinId');
		if(!win)
			win = new QueryInvoiceWin();
		win.show();
	},
	showOpenOTTMobileeWin: function(){
		var win = Ext.getCmp('showOTTMobileeWin');
		if(!win)
			win = new OpenOTTMobileWin();
		win.show();
	},
	showCloseInvoiceWin:function(){
		var win = Ext.getCmp('closeInvoiceWinId');
		if(!win)
			win = new CloseInvoiceWin();
		win.show();
	},
	showCheckAcctountWin:function(){
		var win = Ext.getCmp('checkAcctountWinId');
		if(!win)
			win = new CheckAcctountWin();
		win.show();
	},
	showDeviceCountCheckWin : function(){
		var win = Ext.getCmp('DeviceCountCheckId');
		if(!win)
			win = new DeviceCountCheckWin();
		win.show();
	},
	showUserCountCheckWin : function(){
		new UserCountCheckWin().show();
	},
	showFeeUnitpreWin:function(){
		var win = Ext.getCmp('feeUnitpreWinId');
		if(!win)
			win = new FeeUnitpreWin();
		else
			win.grid.getStore().reload();
		win.show();
	},
	showBatchPayFeeWin : function(){
		var win = Ext.getCmp('batchPayFeeId');
		if(!win)
			win = new BatchPayFeeWin();
		win.show();
	},
	showBatchEditAcctDateWin : function(){
		var win = Ext.getCmp('batchEditAcctDateId');
		if(!win)
			win = new BatchEditAcctDateWin();
		win.show();
	},
	showBatchLogoffCustWin : function(){
		new BatchLogoffCustWin().show();
	},
	showBatchLogoffUserWin : function(){
		new BatchLogoffUserWin().show();
	},
	showBatchOrderProdWin: function(){
		new BatchOrderProdWin().show();
	},
	showBatchCancelProdWin: function(){
		new BatchCancelProdWin().show();
	},
	showOptrDataWin:function(){
		var win = Ext.getCmp('optrDataWinId');
		if(!win)
			win = new OptrDataWin();
		win.show();
	},
	reloadConfig:function(){
		var url = '';
		if(Constant.ROOT_PATH == '/boss-core'){
			url = Constant.ROOT_PATH+"/system/x/Index!reloadConfig.action";
		}else{
			url = Constant.ROOT_PATH+"/system/Index!reloadConfig.action";
		}
		Ext.Ajax.request({
			url: url,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(true === data.success){
					Alert('重载成功!',this);
				}
			}
		});
	},
	reloadPrintData:function(){
		var url = Constant.ROOT_PATH+"/system/x/Index!reloadPrintData.action"
		
		Ext.Ajax.request({
			url: url,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(true === data.success){
					Alert('重载成功!',this);
				}
			}
		});
	},
	bulletinAllWin:function(){
		var win = Ext.getCmp('bulletinGridWinId');
		if(!win)
			win = new BulletinAllWin();
		win.show();
	},
	showvaluableCardWin :function(){
		var win = Ext.getCmp('valuableCardWinId');
		if(!win)
			win = new ValuableCardWin();
		win.show();
	},
	showupdateAddressWin :function(){
		var win = Ext.getCmp('UpdateAddressWinId');
		if(!win)
			win = new UpdateAddressWin();
		win.show();
	},
	showVoucherWin: function(){
		var win = Ext.getCmp('queryVoucherWinId');
		if(!win)
			win = new QueryVoucherWin();
		win.show();
	},
	showCaCardWin: function(){
		var win = Ext.getCmp('sendCaCardWinId');
		if(!win)
			win = new SendCaCardWin();
		win.show();
	},
	//批量修改客户状态为"资料隔离"
	showCustStatusWin: function(){
		var win = Ext.getCmp('custStatusWinId');
		if(!win)
			win = new EditCustStatusWin();
		win.show();
	},
	//批量修改用户状态为"休眠"或"关模隔离"
	showUserStatusWin: function(){
		var win = Ext.getCmp('userStatusWinId');
		if(!win)
			win = new EditUserStatusWin();
		win.show();
	},
	showMobileBillWin : function(){
		var win = Ext.getCmp('mobileBillWinId');
		if(!win)
			win = new MobileBillWin();
		win.show();
	},
	showBandOnlineUserWin : function(){
		var win = Ext.getCmp('bandOnlineUserWinId');
		if(!win)
			win = new BandOnlineUserWin();
		win.show();
	},
	showBandUserFailedLogWin : function(){
		var win = Ext.getCmp('bandUserFailedLogWinId');
		if(!win)
			win = new BandUserFailedLogWin();
		win.show();
	},
	remindCustWin:function(){
		var store = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryImportanceCust.action",
			root: 'records',
			totalProperty: 'totalProperty',
			fields: Ext.data.Record.create([
				"cust_id",
				"cust_no",
				"cust_name",
				"addr_id_text",
				"address",
				"status",
				"cust_type",
				"status_text",
				"cust_type_text",
				"cust_level_text",
				"net_type_text",
				"is_black_text",
				"open_time"
			])
		});
		store.load();
		win = new ChooseCustWindow(store,this);
		win.show();
	},
	showTaskManagerWin: function(){
		var win = Ext.getCmp('taskManagerWinId');
		if(!win){
			win = new Ext.Window({
				id: 'taskManagerWinId',
				title: lbc('home.tools.TaskManager._title'),
				width: 980,
				height: 580,
				border: false,
				closeAction:'hide',
				html: "<iframe id='Task_Manager_Window_Frame' width=100% height=100%"
						 +" frameborder=no  src='/"+ Constant.ROOT_PATH_CORE +"/pages/business/cust/TaskManager.jsp'></iframe>"
			});
		}
		win.show();
	},
	showAddressViewWin : function(){
		var win = Ext.getCmp('AddressViewWinId');
		if(!win)
			win = new AddressViewWin();
		win.show();
//		new AddressViewWin().show();
	},
	showOsdSendViewWin:function(){
		var win = Ext.getCmp('OsdSendViewWinId');
		if(!win)
			win = new OsdSendViewWin();
		win.show();
	}
	
});



/**
 * 营业厅选择窗口
 * @class DeptSelectWin
 * @extends Ext.Window
 */
DeptSelectWin = Ext.extend(Ext.Window,{
	constructor : function(s){
		DeptSelectWin.superclass.constructor.call(this,{
			title: langUtils.bc('home.tools.countySwitch.titleSelectDept'),
			layout: 'fit',
			id:'deptSelectWinId',
			width: 400,
			height: 400,
			closeAction:'close',
			items: [new DeptTreePanel(s)]
		})
	}
});
/**
 * 营业厅选择树
 * @class DeptTreePanel
 * @extends Ext.ux.FilterTreePanel
 */
DeptTreePanel = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	constructor: function(s){
		var url = '';saveUrl = '';sysId = '';
		sysId = s;
		var optr = App.getData().optr;
		if(sysId == 1){
			url = Constant.ROOT_PATH+ '/system/x/Index!queryDeptTree.action';
			saveUrl = Constant.ROOT_PATH+ '/system/x/Index!changeDept.action';
		}else{
			url = Constant.ROOT_PATH+"/system/Index!queryDeptTree.action";
			saveUrl = Constant.ROOT_PATH+ '/system/Index!changeDept.action';
		}
		
		var loader = new Ext.tree.TreeLoader({
			url : url
			
		});
		deptTreeThis = this;
		DeptTreePanel.superclass.constructor.call(this, {
			width 	: 210,
			split	: true,
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        rootVisible : false,
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			loader 		: loader,
			root : new Ext.tree.AsyncTreeNode()
		});
	}
	,initEvents : function(){
		DeptTreePanel.superclass.initEvents.call(this);
		this.on("click" , function( node , e){
			Confirm(langUtils.bc('home.tools.countySwitch.confirmSwitchDept'),node.text,function(){
			 	Ext.Ajax.request({
			 		url:saveUrl,
					params : {
						deptId : node.id,
						countyId : node.attributes.others.countyId,
						areaId : node.attributes.others.areaId
					},
					success : function(res, ops) {
						window.location.href = root + "/rego?sub_system_id="+sysId+"&changeDept=true";
					}
				})
			})
		} , this);
	},openNext:function(){
		var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var j = 0; j < childarr.length; j++) {
				childarr[j].expand();
			}
		}
	},initComponent : function() {
		DeptTreePanel.superclass.initComponent.call(this);
		this.getRootNode().expand();
		this.getRootNode().on('expand', function() {
					deptTreeThis.openNext();
				});
	}
});
//发票结账
var CloseInvoiceGrid = Ext.extend(Ext.grid.GridPanel,{
	closeInvoiceStore:null,
	constructor:function(){
		this.closeInvoiceStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Pay!queryCloseInvoiceByCode.action",
			fields:['invoice_id','invoice_code','status','status_text','optr_id','optr_name','amount',
				'invoice_type','invoice_mode','dept_id','county_id']
		});
		this.closeInvoiceStore.on('load',function(){
			this.getSelectionModel().selectAll();
		},this);
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			sm,
			{header:'发票号码',dataIndex:'invoice_id',sortable:true,width:70},
			{header:'状态',dataIndex:'status_text',sortable:true,width:70},
			{header:'开票人',dataIndex:'optr_name',sortable:true,width:70},
			{header:'金额',dataIndex:'amount',sortable:true,width:70}
		];
		CloseInvoiceGrid.superclass.constructor.call(this,{
			id:'closeInvoiceGridId',
			title:'发票信息',
			region:'center',
			ds:this.closeInvoiceStore,
			columns:columns,
			sm:sm
		});
	}
});

var CloseInvoiceWin = Ext.extend(Ext.Window,{
	closeInvoiceGrid:null,
	constructor:function(){
		this.closeInvoiceGrid = new CloseInvoiceGrid();
		CloseInvoiceWin.superclass.constructor.call(this,{
			id:'closeInvoiceWinId',
			title:'发票结账',
			region:'center',
			layout:'border',
			width:350,
			height:300,
			items:[
				{layout:'column',region:'north',height:40,defaults:{border:false,labelWidth:70},
					bodyStyle:'padding:10px',border:false,items:[
						{layout:'form',columnWidth:.8,items:[
							{fieldLabel:"发票代码",xtype:'textfield',allowBlank:false,
								name:'invoice_code',id:'closeInvoiceCodeId'}]},
						{layout:'form',columnWidth:.2,
							items:[{text:"查  询",xtype:'button',iconCls:'icon-query',listeners:{
								scope:this,
								click:this.doQuery
							}}]}
					]
				},
				this.closeInvoiceGrid
			],
			buttonAlign:'center',
			buttons:[
				{text:'结账',scope:this,handler:this.doSave},
				{text:langUtils.bc('common.close'),scope:this,handler:function(){this.close();}}
			],
			listeners:{
				scope:this,
				hide:function(){
					Ext.getCmp('closeInvoiceCodeId').reset();
					this.closeInvoiceGrid.getStore().removeAll();
				}
			}
		});
	},
	doQuery:function(){
		var value = Ext.getCmp('closeInvoiceCodeId').getValue();
		if(!Ext.isEmpty(value))
			this.closeInvoiceGrid.getStore().load({params:{invoice_code:value}});
		else if(Ext.isEmpty(value) && this.closeInvoiceGrid.getStore().getCount()>0)
			this.closeInvoiceGrid.getStore().removeAll();
	},
	doSave:function(){
		var invoiceCodeComp = Ext.getCmp('closeInvoiceCodeId');
		var records = this.closeInvoiceGrid.getSelectionModel().getSelections();
		if(Ext.isEmpty(invoiceCodeComp.getValue()) || (records && records.length == 0)){
			Alert('请正确输入发票代码查询');
			return;
		}
		var arr = [];
		Ext.each(records,function(record){
			arr.push(record.data);
		});
		var obj = {};
		obj["invoice_code"] = invoiceCodeComp.getValue();
		obj["invoiceList"] = Ext.encode(arr);
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+"/core/x/Pay!modifyCloseInvoice.action",
			params:obj,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(true === data.success){
					Alert('结账成功!',function(){
						this.hide();
					},this);
				}
			}
		});
	}
});
//轧账
var CheckAcctountWin = Ext.extend(Ext.Window,{
	constructor:function(){
		CheckAcctountWin.superclass.constructor.call(this,{
			id:'checkAcctountWinId',
			title:'轧账',
			width:250,
			height:150,
			border:false,
			defaults:{
				labelWidth:70,
				baseCls:'x-plain'
			},
			bodyStyle:'padding:10px;',
			items:[
				{defaults:{border:false},layout:'form',border:false,items:[
					{xtype:'label',id:'checkAcctountId'},
					{xtype:'container',height:15},
					{xtype:'datefield',id:'checkAcctountDateId',name:'acctDate',allowBlank:false,
						fieldLabel:'轧账日期',format:'Y-m-d',editable:false}
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:langUtils.bc('common.save'),scope:this,handler:this.doSave},
				{text:langUtils.bc('common.close'),scope:this,handler:function(){this.hide();}}
			]
		});
	},
	initComponent:function(){
		CheckAcctountWin.superclass.initComponent.call(this);
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/commons/x/BusiCommon!queryGripAccountMode.action',
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				Ext.getCmp('checkAcctountId').setText(
					"按"+data['item_name']+"轧账"
				);
			}
		});
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/commons/x/BusiCommon!acctDate.action',
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				Ext.getCmp('checkAcctountDateId').
					setValue(Date.parseDate(data,"Y-m-d H:i:s"));
			}
		});
	},
	doSave:function(){
		var acctDate = Ext.getCmp('checkAcctountDateId').getRawValue();
		if(!Ext.isEmpty(acctDate)){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH+'/commons/x/BusiCommon!modifyAcctDate.action',
				params:{acctDate:acctDate},
				scope:this,
				success:function(res,opt){
					Alert('保存成功！',function(){
						App.acctDate();
						this.hide();
					},this);
				}
			});
		}
	}
});

var DeviceCountCheckWin = Ext.extend(Ext.Window,{
	constructor:function(){
		DeviceCountCheckWin.superclass.constructor.call(this,{
			id:'DeviceCountCheckId',
			title:'终端数轧账',
			width:250,
			height:150,
			border:false,
			defaults:{
				labelWidth:70,
				baseCls:'x-plain'
			},
			bodyStyle:'padding:10px;',
			items:[
				{defaults:{border:false},layout:'form',border:false,items:[
					{xtype:'container',height:15},
					{xtype:'datefield',name:'acctDate',allowBlank:false,format : 'Y-m',minValue : App.data.optr.old_county_id=='4501' ? null : nowDate().format('Y-m'),
						id : 'DeviceCountCheckDate',fieldLabel:'轧账日期',editable:false}
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:langUtils.bc('common.save'),scope:this,handler:this.doSave},
				{text:langUtils.bc('common.close'),scope:this,handler:function(){this.hide();}}
			]
		});
	},
	doSave:function(){
		var acctDate = Ext.getCmp('DeviceCountCheckDate').getRawValue();
		if(!Ext.isEmpty(acctDate)){
			mb = Show();//显示正在提交
			Ext.Ajax.request({
				url:Constant.ROOT_PATH+'/commons/x/BusiCommon!checkDeviceCount.action',
				params:{acctDate:acctDate,optrId : App.data.optr.optr_id,deptId:App.data.optr.dept_id},
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					
					Alert('保存成功！',function(){
//						App.acctDate();
						this.hide();
					},this);
				}
			});
		}
	}
});

//公告
var BulletinAllGrid = Ext.extend(Ext.grid.GridPanel,{
	bulletinStore:null,loadMask: true,
	constructor:function(){
		var LU_AD = langUtils.bc('home.tools.ad');
		this.bulletinStore = new Ext.data.JsonStore({
			url:root + '/system/x/Index!queryBulletin.action' ,
			fields:['bulletin_title','shouldExpand','bulletin_publisher','bulletin_content','status_text','create_date','bulletin_id','status','eff_date','exp_date','check_date'],
			sortInfo : {
				field : 'status',
				direction : 'DESC'
			},
			root : 'records',
			totalProperty : 'totalProperty',
			listeners:{
				load:function(store,records){
					for(var index =0;index<records.length;index++){
						var rec = records[index];
						rec.set('shouldExpand',true);
					}
				}
			},
			autoDestroy : true
		});
		this.bulletinStore.load({params : {start : 0,limit : Constant.DEFAULT_PAGE_SIZE}});
		var columns = [
			{header : LU_AD['columns'][0],dataIndex : 'bulletin_title',width:120,renderer : App.qtipValue},
			{header : LU_AD['columns'][1],dataIndex : 'bulletin_publisher',width:80},
			{header : LU_AD['columns'][2],width:75,dataIndex : 'eff_date',renderer:Ext.util.Format.dateFormat},
			{header : LU_AD['columns'][3],width:75,dataIndex : 'exp_date',renderer:Ext.util.Format.dateFormat}
		];
		BulletinAllGrid.superclass.constructor.call(this,{
			id:'bulletinGridId',
			height:450,
			region:'center',
			sm : new Ext.grid.CheckboxSelectionModel(),
			ds:this.bulletinStore,
			columns:columns,
			listeners:{
				scope:this,
				rowclick:function(grid,rowNum){
					var view = grid.getView();
					view.ignoreToolBarStatus = false;
					var rec = grid.store.getAt(rowNum);
					rec.set('shouldExpand',!rec.get('shouldExpand'));
		            view.refresh();
				}
			},
			viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:true,
	            getRowClass : function(record, rowIndex, p, store){
                	if(record.get('shouldExpand') != true){
                		return 'x-grid3-row-collapsed';
                	}
                	p.body = '<div><div style="font-size:14px;font-family:KaiTi;color:#337FE5;">'
                    + '&nbsp&nbsp' +record.data.bulletin_content + '</br></div>'
                    + '<div style="width:690px;height:1;"/>'
                    + '<div style="text-align:right;float:right;"> ' + 
                    String.format('{0}' + LU_AD['tplPublishTme'] + ' {1}', record.get('bulletin_publisher'),record.get('create_date')) +
                    '</div></div>'
                    ;
                    return 'x-grid3-row-expanded';
	            }
	        },
			bbar : new Ext.PagingToolbar({
						store : this.bulletinStore
					})
		});
	}
});

var BulletinAllWin = Ext.extend(Ext.Window,{
	grid:null,
	constructor:function(){
		this.grid = new BulletinAllGrid();
		BulletinAllWin.superclass.constructor.call(this,{
			id:'bulletinGridWinId',
			title:langUtils.bc('home.tools.ad._title'),
			width:800,
			border:false,
			defaults:{
				baseCls:'x-plain'
			},
			closeAction : 'close',
			layout:'fit',
			items:[this.grid],
			buttons : [{
						text : langUtils.bc('common.close'),
						scope : this,
						handler : function() {
							this.close();
						}
					}]
		});
	}
});

OptrDataWin = Ext.extend(Ext.Window, {//个人修改面板
	systemStore : null,
	sysUrl:null,
	constructor : function() {
		if(Constant.ROOT_PATH=='/boss-core'){
			this.sysUrl = Constant.ROOT_PATH+'/system/x/Index!getSubSystemByOptrId.action';
		}else if(Constant.ROOT_PATH == '/boss_report'){
			this.sysUrl= Constant.ROOT_PATH+ '/system/Index!getSubSystemByOptrId.action';
		}else{
			this.sysUrl= Constant.ROOT_PATH+ '/system/Optr!getSubSystemByOptrId.action';
		}
		this.systemStore = new Ext.data.JsonStore({
					url : this.sysUrl,
					fields : ['sub_system_id', 'sub_system_text']
				});
				
		var LU = langUtils.bc('home.tools.grxg');
		
		OptrDataWin.superclass.constructor.call(this, {
			title : LU['_title'],
			region : 'center',
			id:'optrDataWinId',
			layout : 'border',
			width : 350,
			height : 190,
			closeAction:'close', 
			items : [{
						layout : 'column',
						region : 'center',
						xtype:'form',
						id:'OptrDataFormId',
						defaults : {
							border : false,
							labelWidth : 100,
							baseCls : 'x-plain',
							columnWidth : 1,
							defaultType : 'textfield',
							layout : 'form'
						},
						bodyStyle : 'padding:10px',
						border : false,
						items : [{								
									labelAlign : 'right',
									items : [{
												fieldLabel : LU['labelNewPwd'],
												inputType : 'password',
												name : 'password',
												id : 'password',						
												listeners :{
													scope : this,
													change:function(txt){
														if(!Ext.isEmpty(txt.getValue())){
															Ext.getCmp('confirmPwd').allowBlank = false;											 
														}else{
															Ext.getCmp('confirmPwd').allowBlank = true;	
														}
													}
												
												}
											}, {
												fieldLabel : LU['labelNewPwdConfirm'],
												inputType : 'password',
												name : 'confirmPwd',
												id : 'confirmPwd',
												vtype : 'password',
												initialPassField : 'password'
											}, {
												xtype : 'combo',
												id : 'optrIoginSysId',
												store : this.systemStore,
												displayField : 'sub_system_text',
												valueField : 'sub_system_id',
												fieldLabel : LU['labelDefaultSystem'],
												hiddenName : 'sub_system_id',
												allowBlank : false,
												width: 150
											}]
								}]
					}],
			buttonAlign : 'center',
			buttons : [{
						text : langUtils.bc('common.save'),
						scope : this,
						handler : this.doSave
					}, {
						text : langUtils.bc('common.close'),
						scope : this,
						handler : function() {
							this.close();
						}
					}]
		});
	},initComponent:function(){
		OptrDataWin.superclass.initComponent.call(this);
		this.systemStore.load();
		this.systemStore.on("load", function(s) {
			Ext.getCmp('optrIoginSysId').setValue(App.getData().optr.login_sys_id);
		});
		
	},
	doSave : function() {
		if (!Ext.getCmp('OptrDataFormId').getForm().isValid()) {
			return;
		};
		var obj = {};saveUrl=null;
		
		obj["query"] = Ext.getCmp('optrIoginSysId').getValue();
		if(Constant.ROOT_PATH == "/boss-core" ){
			saveUrl = root+"/system/x/Index!updateOptrData.action" ;
		}else if(Constant.ROOT_PATH == '/boss_report'){
			saveUrl = root+ '/system/Index!updateOptrData.action';
		}else{
			saveUrl = root+"/system/Optr!updateOptrData.action" ;
		}
		if(!Ext.isEmpty(Ext.getCmp('password').getValue())){
			obj["pwd"] = Ext.getCmp('password').getValue();
		}
		var LBC = lbc('home.tools.grxg.msg');
		Confirm(LBC['msgConfirmSave'], this, function() {
		Ext.Ajax.request({
					url : saveUrl,
					params : obj,
					scope : this,
					success : function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (true === data.success) {
							Alert(LBC['msgSuccess'], function() {
								App.getData().optr.login_sys_id = obj["query"];
								this.close();
							}, this);
						}else{
							Alert(LBC['msgFail']);
						}
					}
				});
		})
	}
});


var checkOptrIsTrueTime = 30*1000;//公告任务循环执行时间(单位为毫秒)
//var winHideTime = 10000;//公告框显示多少时间后隐藏(单位为毫秒)

var checkOptrIsTrueTask = {
		run:function(){checkOptrIsTrue();},
		interval:checkOptrIsTrueTime
	};

var checkOptrIsTrue = function(){
	Ext.Ajax.request({
		url:root + '/querySessionOptr' ,
		scope:this,
		method: 'POST',
		success:function(res,opt){
			var optr = Ext.decode(res.responseText);
			if(!Ext.isEmpty(optr)){
				if(optr['optr_id'] != App.getData()['optr']['optr_id']){
					Alert('操作员异常，请重新登陆！optr exception,Please Relogin!',function(){
						window.location.href = '/boss-login/login.jsp';
					});
				}
			}else{
				Alert('操作员丢失，请重新登录！ optr loss,Please Relogin!',function(){
					window.location.href = '/boss-login/login.jsp';
				});
			}
		}
	});
};
