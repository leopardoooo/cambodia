/**
 * 用户通用显示组件，选中一个用户显示panel，选中多个用户显示grid
 * @class UserInfoPanel
 * @extends Ext.Panel
 */
UserInfoPanel = Ext.extend(Ext.Panel,{
	userStore:null,
	constructor: function(p){			
				var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();							
				var cell_items=[];
				var appheight = 250 ;
				if(!Ext.isEmpty(p)){
					appheight = p;
				}
				this.userStore = new Ext.data.JsonStore({
					root : 'records' ,
					totalProperty: 'totalProperty',
					fields: App.userRecord,
					autoLoad: false
				}); 
				this.userStore.add(App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections());
				var cm = [
					{header:'用户类型',dataIndex:'user_type_text',width:80},
					{header:'机顶盒',dataIndex:'stb_id',	width:150},
					{header:'智能卡',dataIndex:'card_id',width:100},
					{header:'Modem 号',dataIndex:'modem_mac',	width:100}
//					,{header:'停机天数',dataIndex:'status_date',width:100,renderer:Ext.util.Format.DateDiffToday}
				]				
				if(users.length>1){//多用户显示表					
					var UserSelectGrid = new Ext.grid.GridPanel({
//							title : '用户信息',
							border : false,	
							store:this.userStore,
							height : appheight,	
							columns: cm
					});
					cell_items.push(UserSelectGrid);
				}
				else{//单用户显示panel
					var UserDetailTemplate = App.getApp().main.infoPanel.getUserPanel().getUserDetailTemplate();	
					var UserSelectform = new Ext.Panel({	
							title : lmain("user.list._title"),
							height: appheight,	
							border : false,	
							autoScroll:true,
							bodyStyle : Constant.TAB_STYLE,	
							html:UserDetailTemplate[users[0].data.user_type].applyTemplate(users[0].data)							
					});
					cell_items.push(UserSelectform);						
				};
		UserInfoPanel.superclass.constructor.call(this,{
 					layout : 'form',
					border : false,					
					region:'north',	
					bodyStyle: "background:#F9F9F9",
					height: appheight,					
					items : cell_items
		});
	}
});


UserInfoBaseForm = Ext.extend(Ext.Panel,{
	userStore:null,
	constructor: function(p){			
				var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();							
				var cell_items=[];
				this.userStore = new Ext.data.JsonStore({
					root : 'records' ,
					totalProperty: 'totalProperty',
					fields: App.userRecord,
					autoLoad: false
				}); 
				this.userStore.add(App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections());
				var cm = [
					{header:'用户类型',dataIndex:'user_type_text',width:80},
					{header:'机顶盒',dataIndex:'stb_id',	width:150},
					{header:'智能卡',dataIndex:'card_id',width:100},
					{header:'Modem 号',dataIndex:'modem_mac',	width:100}
				]				
				if(users.length>1){//多用户显示表					
					var UserSelectGrid = new Ext.grid.GridPanel({
							border : false,	
							store:this.userStore,
							columns: cm
					});
					cell_items.push(UserSelectGrid);
				}
				else{//单用户显示panel
					var UserDetailTemplate = App.getApp().main.infoPanel.getUserPanel().getUserDetailTemplate();	
					var UserSelectform = new Ext.Panel({	
							border : false,	
							autoScroll:true,
							bodyStyle : Constant.TAB_STYLE,	
							html:UserDetailTemplate[users[0].data.user_type].applyTemplate(users[0].data)							
					});
					cell_items.push(UserSelectform);						
				};
		UserInfoBaseForm.superclass.constructor.call(this,{
 					layout:'fit',
					border : false,					
					items : cell_items
		});
	}
});


//选中产品信息
SelectedProdGrid = Ext.extend(Ext.grid.GridPanel,{
	prodStore : null,
	constructor : function(){
		var recs = null;
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			recs = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelections();
//		}else if(activeId == 'CUST_PANEL'){
//			recs = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelections();
		}
		this.prodStore = new Ext.data.JsonStore({
			fields : [
			{name : 'active_balance',type : 'float'},
			{name : 'real_bill',type : 'float'},
			{name : 'real_fee',type : 'float'},
			{name : 'can_trans_balance',type : 'float'},
			{name : 'can_refund_balance',type : 'float'},
			{name : 'prod_name'},
			{name : 'is_base'}
			]
		});
		var records = [];
		var total = {
			'prod_name' : "总计",
			'active_balance' : 0,
			'real_bill' : 0,
			'real_fee' : 0,
			'can_trans_balance' : 0,
			'can_refund_balance' : 0,
			'is_base' :''
		}
		for(var i=0;i<recs.length;i++){
			var data = App.getAcctItemByProdId(recs[i].get('prod_id'),recs[i].get('user_id'));
			data["prod_name"] = recs[i].get('prod_name');
			
			records.push(data);
			
			total.active_balance = total.active_balance + data.active_balance;
			total.can_refund_balance = total.can_refund_balance + data.can_refund_balance;
			total.can_trans_balance = total.can_trans_balance + data.can_trans_balance;
			total.real_bill = total.real_bill + data.real_bill;
			total.real_fee = total.real_fee + data.real_fee;
			total.is_base = data.is_base
		}
		records.push(total);
		this.prodStore.loadData(records);
		
		var cm = [
			{header:'产品名称',dataIndex:'prod_name',width : 100},
			{header:'余额',dataIndex:'active_balance',width : 80,renderer : Ext.util.Format.formatFee},
			{header:'本月费用',dataIndex:'real_bill',width : 80,renderer : Ext.util.Format.formatFee},
			{header:'实时费用',dataIndex:'real_fee',width : 80,renderer : Ext.util.Format.formatFee},
			{header:'可转余额',dataIndex:'can_trans_balance',width : 80,renderer : Ext.util.Format.formatFee},
			{header:'可退余额',dataIndex:'can_refund_balance',width : 80,renderer : Ext.util.Format.formatFee}
		]
		SelectedProdGrid.superclass.constructor.call(this,{
			title : '产品信息',
			height : 200,
			store : this.prodStore,
			columns : cm
		})
	}
});