/**
 * 首页的核心部分,包含信息面板、业务办理面板
 */

/**
 * 封装显示客户信息的面板
 * @class InfoPanel
 * @extends Ext.Panel
 */
InfoPanel = Ext.extend( CommonTab , {

	custPanel: null,
	unitPanel: null,
	userPanel: null,
	acctPanel: null,
	payfeePanel: null,
	billPanel : null,
	docPanel: null,
	doneCodePanel: null,
	commandInfoPanel: null,
	//默认激活的面板id
	DEFAULT_PANEL: 'CUST_PANEL',
	
	constructor: function(){
		this.unitPanel = new UnitPanel();
		this.custPanel = new CustPanel();
		
		this.userPanel = new UserPanel();
		
//		this.acctPanel = new AcctPanel();
		this.payfeePanel = new PayfeePanel();
		this.billPanel = new BillPanel();
		this.docPanel = new DocPanel();
		this.doneCodePanel = new DoneCodePanel();
		this.commandInfoPanel = new CommandInfoPanel();
		InfoPanel.superclass.constructor.call(this, {
			border: false,
			defaults: {
				border: false,
				defaults: { border: false }
			},
			items: [{
				id: 'CUST_PANEL',
				iconCls:'cust',
				title: langUtils.bc("home.main.tabs.[0]"),
				layout: 'fit',
				border: false,
				items: [this.custPanel]
			},{
				id: 'UNIT_PANEL',
				iconCls:'cust',
				title: langUtils.bc("home.main.tabs.[1]"),
				hidden:true,
				layout: 'fit',
				border: false,
				items: [this.unitPanel]
			},{
				id: 'USER_PANEL',
				iconCls:'user',
				title: langUtils.bc("home.main.tabs.[2]"),
				layout: 'fit',
				border: false,
				items: [this.userPanel]
			}/*,{
				id: 'ACCT_PANEL',
				iconCls:'acct',
				title: langUtils.bc("home.main.tabs.[3]"),
				layout: 'fit',
				border: false,
				items: [this.acctPanel]
			}*/,{
				id: 'PAY_PANEL',
				iconCls:'pay_fee',
				title: langUtils.bc("home.main.tabs.[4]"),
				layout: 'fit',
				border: false,
				items: [this.payfeePanel]
			},{
				id: 'DOC_PANEL',
				iconCls:'doc',
				title: langUtils.bc("home.main.tabs.[5]"),
				layout: 'fit',
				border: false,
				items: [this.docPanel]
			},{
				id: 'DONECODE_PANEL',
				iconCls:'done_code',
				title: langUtils.bc("home.main.tabs.[6]"),
				layout: 'fit',
				border: false,
				items: [this.doneCodePanel]
			},{
				id: 'COMMAND_PANEL',
				iconCls:'resendCmd',
				title: langUtils.bc("home.main.tabs.[7]"),
				layout: 'fit',
				border: false,
				items:[this.commandInfoPanel],
				html:''
			},{
				id: 'BILL_PANEL',
				iconCls:'bill',
				title: langUtils.bc("home.main.tabs.[8]"),
				layout: 'fit',
				border: false,
				items: [this.billPanel]
			}]
		});
	},
	initEvents:function(){
		InfoPanel.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.changeDisplay();
		},this,{delay : 10});
	},
	refreshPanel : function(p){
		this.toggleMenu(p);
		var content = p.items.itemAt(0);
		//如果这里报错，则说明信息面板没有继承BaseInfoPanel，具体请查看CustPanel，UserPanel
		if(App.getCustId() && content.isReload()){
			content.refresh();
			content.setReload(false);
		}
		
		var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
		if(p.id == 'USER_PANEL'){
			//只有一个用户时，默认选中
			if(userGrid.getStore().getCount() == 1){
				userGrid.getSelectionModel().selectFirstRow();
			}else{
				//当有多个用户时，切换回用户面板，删除所属产品，不然操作产品相关功能，后台getBusiParam()取不到选中user_id
				if(userGrid.getStore().getCount() > 1){
					App.getApp().main.infoPanel.getUserPanel().prodGrid.userProdStore.removeAll();
				}
			}
		}else{
			//不是用户面板的功能操作，去掉选中用户，后台getBusiParam中选中user_id则为空
			if(userGrid.getSelectionModel().getSelections().length != 0){
				userGrid.getSelectionModel().clearSelections();
			}
		}
		win=Ext.getCmp('filterWinID');
		if(win && win.isVisible()){
			win.hide();
		}
		
	},
	changeDisplay: function(){
		
		if(App.data.custFullInfo.cust.cust_type){
			var custTBar = null;
			var packageTbar = null;
			var userTBar = Ext.getCmp('U_USER').getTopToolbar();
//			var acctTBar = Ext.getCmp('A_ACCT').getTopToolbar();
//			var deviceTBar = Ext.getCmp('C_DEVICE').getTopToolbar();
//			var busiTBar = Ext.getCmp('D_BUSI').getTopToolbar();
			
			if (App.data.custFullInfo.cust.cust_type=='UNIT'){
				this.hideTabStripItem('CUST_PANEL');
				this.hideTabStripItem('USER_PANEL');
				this.hideTabStripItem('COMMAND_PANEL');
				this.hideTabStripItem('ACCT_PANEL');
				this.hideTabStripItem('BILL_PANEL');
				this.hideTabStripItem('PAY_PANEL');
				this.hideTabStripItem('DOC_PANEL');
				this.unhideTabStripItem('UNIT_PANEL');
				this.DEFAULT_PANEL='UNIT_PANEL';
				
				custTBar = Ext.getCmp('C_CUST_UNIT').getTopToolbar();
				//packageTbar = Ext.getCmp('C_PACKAGE_UNIT').getTopToolbar();
			}else{
				this.hideTabStripItem('UNIT_PANEL');
				this.unhideTabStripItem('CUST_PANEL');
				this.unhideTabStripItem('USER_PANEL');
				this.unhideTabStripItem('ACCT_PANEL');
				this.unhideTabStripItem('BILL_PANEL');
				this.unhideTabStripItem('PAY_PANEL');
				this.unhideTabStripItem('DOC_PANEL');
				this.unhideTabStripItem('COMMAND_PANEL');
				this.DEFAULT_PANEL='CUST_PANEL';
				
				custTBar = Ext.getCmp('C_CUST').getTopToolbar();
				//packageTbar = Ext.getCmp('C_PACKAGE').getTopToolbar();
			}
			
			if (App.data.custFullInfo.cust.status=='INVALID' || App.data.custFullInfo.cust.status=='LOGOFF' ){
				if(!Ext.isEmpty(custTBar)){
					custTBar.show();
				}
				if(!Ext.isEmpty(userTBar)){
					userTBar.hide();
				}
//				if(!Ext.isEmpty(acctTBar)){
//					acctTBar.hide();
//				}
				//if(!Ext.isEmpty(packageTbar)){
				//	packageTbar.hide();
				//}
//				if(!Ext.isEmpty(deviceTBar)){
//					deviceTBar.hide();
//				}
//				busiTBar.hide();
			}else{
				if(!Ext.isEmpty(custTBar)){
					custTBar.show();
				}
				if(!Ext.isEmpty(userTBar)){
					userTBar.show();
				}
//				if(!Ext.isEmpty(acctTBar)){
//					acctTBar.show();
//				}
				//if(!Ext.isEmpty(packageTbar)){
				//	packageTbar.show();
				//}
//				if(!Ext.isEmpty(deviceTBar)){
//					deviceTBar.show();
//				}
//				if(!Ext.isEmpty(busiTBar)){
//					busiTBar.show();
//				}
			}
		}else{
			this.hideTabStripItem('UNIT_PANEL');
			this.unhideTabStripItem('CUST_PANEL');
			this.unhideTabStripItem('USER_PANEL');
			this.unhideTabStripItem('COMMAND_PANEL');
			this.DEFAULT_PANEL='CUST_PANEL';
		}
		this.activeDefaultTab();
	},
	getCustPanel: function(){
		return this.custPanel; 
	},
	getUnitPanel: function(){
		return this.unitPanel; 
	},	
	getUserPanel: function(){
		return this.userPanel; 
	},
	getAcctPanel : function(){
		return this.acctPanel;
	},
	getPayfeePanel: function(){
		return this.payfeePanel; 
	},
	getDocPanel: function(){
		return this.docPanel; 
	},
	getDoneCodePanel: function(){
		return this.doneCodePanel; 
	},
	getCommandInfoPanel: function(){
		return this.commandInfoPanel; 
	},
	getBillPanel : function(){
		return this.billPanel;
	},	
	setReload:function(reload){
		this.getDocPanel().setReload(reload);
		this.getPayfeePanel().setReload(reload);
		this.getUserPanel().setReload(reload);
//		this.getAcctPanel().setReload(reload);
		this.getBillPanel().setReload(reload);
		this.getCommandInfoPanel().setReload(reload);
	},
	//切换菜单面板
	toggleMenu: function(p){
/*		var ids = p.id.split("_");
		var buttons = App.data.resources;
		var newbuts = new Array();
    	for(var i = 0, t; t = buttons[i]; i++){
   			if(ids[0] == t.attrs.obj_type){
           		newbuts.push(t);
           	}
        }
		App.menu.refreshMenu(newbuts);*/
	},
	//设置默认激活的tab页
	activeDefaultTab: function( ){
		this.activate(this.DEFAULT_PANEL);
		this.doLayout();
	}
});

/**
 * Main 构建首页的body部分。
 * @class Main
 * @extends Ext.Panel
 */
Center = Ext.extend( Ext.Panel , {

	infoPanel: null,
	
	constructor: function(){
		// 实例化需要组装的面板
		this.infoPanel = new InfoPanel();
		
		Center.superclass.constructor.call(this, {
		    border: false,
			layout: 'fit',
		    items: this.infoPanel
		});
	}
});
