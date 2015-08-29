/**
 * 账户面板
 */
AcctGrid = Ext.extend(Ext.ux.Grid,{
	border:false,
	acctStore:null,
	region: 'center',
	parent : null,
	singleSelect : false,
	allowSelectAll:true, //判断是否全选
	constructor:function(p){
		this.parent = p;
		this.acctStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/core/x/Acct!queryAcctByCustId.action",
			fields: ["cust_id",'user_addr',"acct_id","acct_type","acct_type_text","user_name","user_type_text","stb_id","card_id","modem_mac","bank","user_id","status","status_text","acctitems"],
			sortInfo : {
				field : 'user_name',
				direction:'DESC'
			}
		}); 
		this.acctStore.on('load',this.doLoadResult,this);
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : this.singleSelect
		});
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
            sm,
			{header:'账户类型',dataIndex:'acct_type',hidden : true},
			{header:'账户类型',dataIndex:'acct_type_text',width:60},
			{header:'用户类型',dataIndex:'user_type_text',width:60},
			{header:'用户名',dataIndex:'user_name',width:60},
			{header:'状态',dataIndex:'status_text',width:40,renderer:Ext.util.Format.statusShow},
			{header:'机顶盒',dataIndex:'stb_id',width:130},
			{header:'卡号',dataIndex:'card_id',width:120},
			{header:'modem',dataIndex:'modem_mac',width:70},
			{header:'银行托收',dataIndex:'bank',hidden:'true'}
        ]
      });
		
		AcctGrid.superclass.constructor.call(this,{
			id:'A_ACCT',
			title:"账户信息",
			store:this.acctStore,
			sm:sm,
			cm:cm,
			sortInfo:{
				field:'acct_type',
				direction:'DESC'
			},
        	view: new Ext.ux.grid.ColumnLockBufferView({}),
        	tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
				
					var comp = this.tools.search;
					if(this.acctStore.getCount()>0){
						if(win)win.close();
							win = FilterWindow.addComp(this,[
								{text:'账户类型',field:'acct_type',showField:'acct_type_text'},
								{text:'用户地址',field:'user_addr',type:'textfield'},
								{text:'机顶盒',field:'stb_id',type:'textfield'},
								{text:'卡号',field:'card_id',type:'textfield'},
								{text:'Model',field:'modem_mac',type:'textfield'}
								],465,"1",false);
						if(win){
							win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
							win.show();
						}
					}else{
						Alert('请先查询数据！');
					}
		    	}
		    }]
		})
	},
	initEvents: function(){
		AcctGrid.superclass.initEvents.call(this);
		this.on("rowclick", this.doClickRecord, this );
		this.on("afterrender",function(){
			this.swapViews();
			
			if(this.allowSelectAll && this.getStore().getCount()>0){
				this.getSelectionModel().selectAll();
			}
			this.allowSelectAll = true;
		},this,{delay:10});
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	doLoadResult : function(_store, _rs, ops){
		//集团允许批量缴费，其他不允许
		var batchPayFees =Ext.getCmp('BatchPayFees');
		if (batchPayFees)
			if(App.getCust().cust_type=='NONRES'){
				batchPayFees.setDisabled(false);
			}else if(App.getCust().cust_type=='RESIDENT'){
				batchPayFees.setDisabled(true);
			}else{
				batchPayFees.setDisabled(true);
			}
			
		var modifyThreshold =Ext.getCmp('ModifyThreshold');
		if (modifyThreshold){
			if(App.getCust().cust_type=='NONRES'){
				modifyThreshold.setDisabled(true);
			}else{
				modifyThreshold.setDisabled(false);
			}
		}
		
		var batchModifyThreshold =Ext.getCmp('BatchModifyThreshold');
		if(batchModifyThreshold){
			if(App.getCust().cust_type=='NONRES'){
				batchModifyThreshold.setDisabled(false);
			}else{
				batchModifyThreshold.setDisabled(true);
			}
		}
		var rec = null;
		//柬埔寨
		_store.each(function(record){
			if(record.get('acct_type') == 'PUBLIC'){
				rec = record;			
			}
		});
		this.parent.acctItemGrid.doLoadAcctItem(_store,rec.get('acct_id'));
		this.parent.acctItemGrid.userId = rec.get('user_id');
		this.parent.acctItemGrid.acctType = rec.get('acct_type');
		
		
		//隐藏数据加载提示框
		App.hideTip();
		if(this.parent.acctItemGrid.acctId){
			this.parent.acctItemGrid.remoteRefresh(_store);
		}
		this.fireEvent('afterrender',this);
	},
	remoteRefresh:function(){
//		//显示数据加载提示框
//		App.showTip();
//		this.acctStore.baseParams.custId=App.getApp().getCustId();
//		this.acctStore.baseParams.custStatus=App.data.custFullInfo.cust.status;
//		this.acctStore.load();
		
//		this.parent.acctItemDetailTab.resetPanel();
//		
//			
//		//过滤tbar按钮
//		if(App.getCust().status == 'RELOCATE'){
//			App.getApp().disableBarByBusiCode(this.getTopToolbar(),['1040'],true);
//		}else if(App.getCust().status == 'DATACLOSE'){
//			App.getApp().disableBarByBusiCode(this.getTopToolbar(),['1040','1049'],true);
//		}else if(App.getCust().status == 'ACTIVE'){
//			App.getApp().disableBarByBusiCode(this.getTopToolbar(),['1040','1049'],false);
//		}
	},
	doClickRecord : function(grid,index,e){
		var rec = grid.getStore().getAt(index);
		this.parent.acctItemGrid.doLoadAcctItem(this.acctStore,rec.get('acct_id'));
		this.parent.acctItemGrid.userId = rec.get('user_id');
		this.parent.acctItemGrid.acctType = rec.get('acct_type');
	}
});

AcctItemGrid = Ext.extend(Ext.ux.Grid,{
	border:false,
	acctItemStore:null,
	region: 'center',
	parent : null,
	userId :null,//当前账目对应的用户ID
	acctType:null,//当前账目对应的账户ID
	singleSelect : false,
	acctId : null,//保存当前显示的acctid
	constructor:function(p){
		this.parent = p;
		this.acctItemStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/core/x/Acct!queryPublicAcctitem.action",
			fields : [
			{name : 'acct_id'},
			{name : 'acctitem_id'},
			{name : 'acctitem_name'},
			{name : 'acctitem_type'},
			{name : 'active_balance',type : 'float'},
			{name : 'real_bill',type : 'float'},
			{name : 'real_fee',type : 'float'},
			{name : 'can_trans_balance',type : 'float'},
			{name : 'can_refund_balance',type : 'float'},
			{name : 'owe_fee',type : 'float'},
			{name : 'adjust_balance',type : 'float'},
			{name : 'order_balance',type : 'float'},
			{name : 'real_balance',type : 'float'},
			{name : 'prod_id'},
			{name : 'billing_cycle'},
			{name : 'prod_name'},
			{name : 'tariff_id'},
			{name : 'tariff_name'},
			{name : 'tariff_rent'},
			{name : 'prod_sn'},
			{name : 'invalid_date'},
			{name : 'inactive_balance'},
			{name : 'user_id'},
			{name : 'next_tariff_id'},
			{name : 'next_tariff_name'},
			{name : 'prod_status'},
			{name : 'prod_status_text'},
			{name : 'invalid_date'},
			{name:'acct_type'},
			{name:'is_base'},
			{name:'is_zero_tariff'},
			{name:'allow_adjust'},
			{name:'billing_type'},
			{name:'allow_tran'},
			{name:'status'},//账户状态
			{name:'serv_id'},
			{name:'card_id'},
			{name:'ownFeeNumber'}	//基本包欠费天数
			]
		});
		this.acctItemStore.on('load',this.doLoadResult,this);
		
		//添加列的时候，注意修改那些继承AcctItemGrid的js
    	var cm = new Ext.grid.ColumnModel({
    		columns : [
				{header:'账目名称',dataIndex:'acctitem_name'},
				{header:'卡号',dataIndex:'card_id',width:120,hidden:true},
				{header:'余额',dataIndex:'active_balance',renderer : Ext.util.Format.formatFee ,width:80},
				{header:'往月欠费',dataIndex:'owe_fee',renderer : Ext.util.Format.formatFee,width:80},
				{header:'本月费用',dataIndex:'real_bill',renderer : Ext.util.Format.formatFee,width:80},
				{header:'实时费用',dataIndex:'real_fee',renderer : Ext.util.Format.formatFee,width:80},				
				{header:'实时余额',dataIndex:'real_balance',renderer : Ext.util.Format.formatFee,width:80},
				{header:'可转余额',dataIndex:'can_trans_balance',renderer : Ext.util.Format.formatFee,width:80},
				{header:'可退余额',dataIndex:'can_refund_balance',renderer : Ext.util.Format.formatFee,width:80},
				{header:'冻结余额',dataIndex:'inactive_balance',renderer : Ext.util.Format.formatFee,width:80}
		    ]
    	});
		AcctItemGrid.superclass.constructor.call(this,{
			id:'A_ITEM',
			title: '账目信息',
			store:this.acctItemStore,
			sm:new Ext.grid.RowSelectionModel(),
			cm:cm,
			tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
					var comp = this.tools.search;
					if(this.acctItemStore.getCount()>0){
						if(win)win.close();
						win = FilterWindow.addComp(this,[
							{text:'账目名称',field:'acctitem_name',type:'textfield'}
						],145,null,false);
						
						if(win){
							win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
							win.show();
						}
					}else{
						Alert('请先查询数据！');
					}
		    	}
		    }]
		})
	},
	initEvents: function(){
		this.on("rowclick", this.doDbClickRecord, this );
		AcctItemGrid.superclass.initEvents.call(this);
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
	},
	doLoadResult : function(_store, _rs, ops){
		//隐藏数据加载提示框
		App.hideTip();
		if(this.parent){
			var acctId = this.parent.acctItemDetailTab.acctId;
			var acctItemId = this.parent.acctItemDetailTab.acctItemId;
			if(null != acctId && null != acctItemId){
				var isExist = false;//账目是否存在
				for(i=0;i<_rs.length;i++){
					if(acctItemId == _rs[i].get('acctitem_id')){
						var acctItemDetailTab = this.parent.acctItemDetailTab;
						acctItemDetailTab.resetPanel(acctId,acctItemId);
						acctItemDetailTab.refreshPanel(acctItemDetailTab.getActiveTab());
						isExist = true;
						break;
					}
				}
				if(!isExist){
					this.parent.acctItemDetailTab.resetPanel();
				}
			}
		}
	},
	remoteRefresh:function(){
		//显示数据加载提示框
		App.showTip();
		this.acctItemStore.baseParams.custId=App.getApp().getCustId();
		this.acctItemStore.load();
		this.parent.acctItemDetailTab.resetPanel();
//		this.doLoadAcctItem(acctstore,this.acctId);
	},
	doDbClickRecord : function(grid,index,e){
		if(this.parent){
			var record = grid.getStore().getAt(index).data;
			
			var acctItemDetailTab = this.parent.acctItemDetailTab;
			acctItemDetailTab.resetPanel(record["acct_id"],record["acctitem_id"]);
			acctItemDetailTab.refreshPanel(acctItemDetailTab.getActiveTab());
		}
	},
	doLoadAcctItem : function(acctstore,accId){
		this.acctItemStore.removeAll();
		
		for(var i=0;i<acctstore.getCount();i++){
			var acctRecord = acctstore.getAt(i);
			if(accId == acctRecord.get('acct_id')){
				var acctItems = acctRecord.get('acctitems');
				if(acctItems){
					for(var i=0,len=acctItems.length;i<len;i++){
						acctItems[i]['status'] = acctRecord.get('status');
						acctItems[i]['card_id'] = acctRecord.get('card_id');
						acctItems[i]['user_id'] = acctRecord.get('user_id');
					}
					this.acctItemStore.loadData(acctItems);
				}
				break;
			}
		}
		this.acctId=accId;
	},
	reset : function(){
		this.acctItemStore.removeAll();
		this.acctId = null;
	},
	getAcctItemByAcctId: function(acctId, acctitemId){
		var acctitem = null;
		this.parent.acctGrid.acctStore.findBy(function(record){
			if(record.get("acct_id") == acctId){
				var acctitems = record.get("acctitems");
				for(var i = 0;i < acctitems.length; i++){
					if(acctitems[i]["acctitem_id"] == acctitemId){
						acctitem = acctitems[i];
						break;
					}
				}
			}
			if(acctitem){
				return false;
			}
		});
		return acctitem;
	}
});

AcctDetailTab = Ext.extend(Ext.TabPanel,{
	region:"center",
	constructor:function(){
		AcctDetailTab.superclass.constructor.call(this, {
			activeTab: 0,
			border: false,
			items:[{
				title:'详细信息'
			},{
				title:'账单信息'
			}]
		});
	}
});

/**
 * 账目明细
 */
AcctItemActiveGrid = Ext.extend(Ext.ux.Grid,{
	border: false,
	itemStore: null,
	isReload : false,
	constructor: function(){
		this.itemActiveStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemActive.action",
			fields: ["acct_id","acctitem_id","fee_type_text","fee_type","balance"],
			sortInfo:{
				field:'fee_type',
				direction:'DESC'
			}
		});
		
		var cm = new Ext.grid.ColumnModel({
    		columns : [
				{header:'资金类型',dataIndex:'fee_type_text',width:80},
				{header:'余额',dataIndex:'balance',renderer:Ext.util.Format.formatFee}
		    ]
    	});
		AcctItemActiveGrid.superclass.constructor.call(this,{
			id:'ACCTITEM_ACTIVE',
			width : 600,
			store:this.itemActiveStore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm:cm,
			viewConfig : {
				forceFit : true
			}
		})
	},
	remoteRefresh:function(acctId,acctItemId){
		this.itemActiveStore.baseParams.acctId= acctId;
		this.itemActiveStore.baseParams.acctItemId= acctItemId;
		this.itemActiveStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 账目异动信息
 */
AcctItemPropChangeGrid = Ext.extend(Ext.grid.GridPanel,{
	border: false,
	changeStore: null,
	isReload : false,
	constructor: function(){
		this.changeStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemChange.action",
			fields: ["acctitem_id","acctitem_name","busi_name","fee_type_text","change_type_text","fee_type","change_type","change_fee","fee","pre_fee"
					,"done_date","start_acctitem","end_acctitem"],
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20},
			sortInfo:{
				field:'done_date',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'业务',dataIndex:'busi_name',width:40,renderer : App.qtipValue},
			{header:'资金类型',dataIndex:'fee_type_text',width:60,renderer : App.qtipValue},
			{header:'变更类型',dataIndex:'change_type_text',	width:60,renderer : App.qtipValue},
			{header:'变更前',dataIndex:'pre_fee',renderer:Ext.util.Format.formatFee,width:50},
			{header:'变更金额',dataIndex:'change_fee',renderer:Ext.util.Format.formatFee,width:60},
			{header:'变更后',dataIndex:'fee',renderer:Ext.util.Format.formatFee,	width:50},
			{header:'销账账目',dataIndex:'acctitem_name',width:60,renderer : App.qtipValue},
			{header:'操作时间',dataIndex:'done_date',width:120},
			{header:'来源账目',dataIndex:'start_acctitem',width:80,renderer : App.qtipValue},
			{header:'目标账目',dataIndex:'end_acctitem',width:80,renderer : App.qtipValue}
		];
		AcctItemPropChangeGrid.superclass.constructor.call(this,{
			width : 600,
			store:this.changeStore,
			columns:cm,
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.changeStore
			})
		})
	},
	remoteRefresh:function(acctId,acctItemId){
		this.changeStore.baseParams.acctId= acctId;
		this.changeStore.baseParams.acctItemId= acctItemId;
		this.changeStore.baseParams.start = 0;
		this.changeStore.baseParams.limit = 20;
		this.changeStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 账目调账信息
 */
AcctItemAdjustGrid = Ext.extend(Ext.ux.Grid,{
	adjustStore: null,
	isReload : false,
	constructor: function(){
		this.adjustStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemAdjust.action",
			fields: ["done_code","acct_id","acctitem_id","ajust_fee","create_time","remark","reason","reason_text"],
			sortInfo:{
				field:'create_time',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'调账金额',dataIndex:'ajust_fee',width:65,renderer:Ext.util.Format.formatFee},
			{header:'操作时间',dataIndex:'create_time',width:120},
			{header:'调账原因',dataIndex:'reason_text',width:120},
			{header:'备注',dataIndex:'remark'}
		];
		AcctItemAdjustGrid.superclass.constructor.call(this,{
			id:'A_ADJUST',
			store:this.adjustStore,
			border: false,
			columns:cm,
			sm:new Ext.grid.RowSelectionModel(),
			width : 200,
			viewConfig : {
				forceFit : true
			}
		})
	},
	remoteRefresh:function(acctId,acctItemId){
		this.adjustStore.baseParams.acctId= acctId;
		this.adjustStore.baseParams.acctItemId= acctItemId;
		this.adjustStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 账目阈值信息
 */
AcctitemThresholdGrid = Ext.extend(Ext.grid.GridPanel,{
	border: false,
	thresholdStore: null,
	isReload : false,
	constructor: function(){
		this.thresholdStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemThreshold.action",
			fields: ["acct_id","acctitem_id","task_code","threshold","base_threshold","temp_threshold","area_id",'task_code_text'],
			sortInfo:{
				field:'threshold',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'任务类别',dataIndex:'task_code_text'},
			{header:'阈值',dataIndex:'threshold',renderer : Ext.util.Format.formatFee},
			{header:'基准阈值',dataIndex:'base_threshold',renderer : Ext.util.Format.formatFee},
			{header:'临时阈值',dataIndex:'temp_threshold',renderer : Ext.util.Format.formatFee}
		];
		AcctitemThresholdGrid.superclass.constructor.call(this,{
			store:this.thresholdStore,
			columns:cm,
			viewConfig:{
        		forceFit : true
        	}
		})
	},
	
	remoteRefresh:function(acctId,acctItemId){
		this.thresholdStore.baseParams.acctId= acctId;
		this.thresholdStore.baseParams.acctItemId= acctItemId;
		this.thresholdStore.load({
			params: { start: 0, limit: App.pageSize}
		});
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});
AcctitemThresholdPropGrid = Ext.extend(Ext.grid.GridPanel,{
	border: false,
	thresholdPropStore: null,
	isReload : false,
	constructor: function(){
		this.thresholdPropStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemThresholdProp.action",
			fields: ["acct_id","acctitem_id","task_code","old_value","new_value","change_time","area_id","task_code_text","column_name_text","optr_name"],
			sortInfo:{
				field:'change_time',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'任务类别',dataIndex:'task_code_text'},
			{header:'阈值类型',dataIndex:'column_name_text'},
			{header:'修改前阈值',dataIndex:'old_value' ,renderer : Ext.util.Format.formatFee},
			{header:'修改后阈值',dataIndex:'new_value',renderer : Ext.util.Format.formatFee},
			{header:'修改时间',dataIndex:'change_time'},
			{header:'操作员',dataIndex:'optr_name'}
		];
		AcctitemThresholdPropGrid.superclass.constructor.call(this,{
			store:this.thresholdPropStore,
			columns:cm,
			viewConfig:{
        		forceFit : true
        	}
		})
	},
	
	remoteRefresh:function(acctId,acctItemId){
		this.thresholdPropStore.baseParams.acctId= acctId;
		this.thresholdPropStore.baseParams.acctItemId= acctItemId;
		this.thresholdPropStore.load({
			params: { start: 0, limit: App.pageSize}
		});
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 账目冻结信息
 */
AcctitemInactiveGrid = Ext.extend(Ext.ux.Grid,{
	border: false,
	inactiveStore: null,
	isReload : false,
	constructor: function(){
		this.inactiveStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemInactive.action",
			fields: ["promotion_sn","init_amount","acctitem_id","use_amount","balance","cycle","create_time","active_amount",
					"last_active_time","area_id","next_active_time","acct_id","fee_sn"],
			sortInfo:{
				field:'acctitem_id',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'初始金额',dataIndex:'init_amount',width:60,renderer:Ext.util.Format.formatFee},
			{header:'已返还金额',dataIndex:'use_amount',width:70,renderer:Ext.util.Format.formatFee},
			{header:'余额',dataIndex:'balance',width:60,renderer:Ext.util.Format.formatFee},
			{header:'周期',dataIndex:'cycle',width:40},
			{header:'激活金额',dataIndex:'active_amount',width:60,renderer:Ext.util.Format.formatFee},
			{header:'下次执行时间',dataIndex:'next_active_time',renderer:Ext.util.Format.dateFormat}
		];
		AcctitemInactiveGrid.superclass.constructor.call(this,{
			id:'ACCTITEM_INACTIVE',
			store:this.inactiveStore,
			sm:new Ext.grid.RowSelectionModel(),
			columns:cm,
			width : 600
		})
	},
	
	remoteRefresh:function(acctId,acctItemId){
		this.inactiveStore.baseParams.acctId= acctId;
		this.inactiveStore.baseParams.acctItemId= acctItemId;
		this.inactiveStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 账目预约信息
 */
AcctItemOrderGrid = Ext.extend(Ext.grid.GridPanel,{
	border: false,
	orderStore: null,
	isReload : false,
	constructor: function(){
		this.orderStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAllAcctitemOrder.action",
			fields: ["acctitem_id","acctitem_name","src_acctitem_name","amount","order_time"],
			sortInfo:{
				field:'order_time',
				direction:'DESC'
			}
		});
		var cm = [
			{header:'预约账目',dataIndex:'acctitem_name',width:80},
			{header:'被预约账目',dataIndex:'src_acctitem_name',width:80},
			{header:'预约金额',dataIndex:'amount',	renderer:Ext.util.Format.formatFee,width:60},
			{header:'预约日期',dataIndex:'order_time',renderer:Ext.util.Format.dateFormat} 
		];
		AcctItemOrderGrid.superclass.constructor.call(this,{
			width : 600,
			store:this.orderStore,
			columns:cm
		})
	},
	remoteRefresh:function(acctId,acctItemId){
		this.orderStore.baseParams.acctId= acctId;
		this.orderStore.baseParams.acctItemId= acctItemId;
		this.orderStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

AcctItemDetailTab = Ext.extend(CommonTab,{
	acctItemActiveGrid:null,
	acctItemPropChangeGrid : null,
	acctitemThresholdGrid : null,
	acctitemInactiveGrid : null,
	acctItemAdjustGrid : null,
	acctItemOrderGrid: null,
	acctitemThresholdPropGrid: null,
	acctItemId : null,
	acctId : null,
	constructor:function(){
		this.acctItemAdjustGrid = new AcctItemAdjustGrid();
		this.acctItemActiveGrid = new AcctItemActiveGrid();
		this.acctItemPropChangeGrid = new AcctItemPropChangeGrid();
//		this.acctitemInactiveGrid = new AcctitemInactiveGrid();
//		this.acctitemThresholdGrid = new AcctitemThresholdGrid();
//		this.acctItemOrderGrid = new AcctItemOrderGrid();
//		this.acctitemThresholdPropGrid = new AcctitemThresholdPropGrid();
		AcctItemDetailTab.superclass.constructor.call(this, {
			activeTab: 0,
			border: false,
			defaults : {
				border: false,
				layout : 'fit'
			},
			items:[{
				title:'明细',
				items : [this.acctItemActiveGrid]
			}/*,{
				title:'冻结',
				items : [this.acctitemInactiveGrid]
			}*/,{
				title:'调账',
				items : [this.acctItemAdjustGrid]
			}/*,{
				title:'阈值',
				items : [this.acctitemThresholdGrid]
			}*/,{
				title:'异动',
				items : [this.acctItemPropChangeGrid]
			}/*,{
				title:'预约',
				items : [this.acctItemOrderGrid]
			},{
				title:'阈值异动',
				items : [this.acctitemThresholdPropGrid]
			}*/]
		});
	},
	refreshPanel : function(p){//重写父类CommonTab的方法，必须
		var content = p.items.itemAt(0);
		if(content && content.isReload && this.acctId && this.acctItemId){
			content.remoteRefresh(this.acctId,this.acctItemId);
			content.isReload = false;
		}
	},
	resetPanel : function(acctId,acctItemId){
		this.acctItemActiveGrid.reset();
		this.acctItemPropChangeGrid.reset();
//		this.acctitemInactiveGrid.reset();
		this.acctItemAdjustGrid.reset();
//		this.acctitemThresholdGrid.reset();
//		this.acctItemOrderGrid.reset();
//		this.acctitemThresholdPropGrid.reset();
		
		if(acctId){
			this.acctId = acctId;
		}else{
			this.acctId = null;
		}
		
		if(acctItemId){
			this.acctItemId = acctItemId;
		}else{
			this.acctItemId = null;
		}
		
		this.isReload = true;
	},
	refreshAcctItemInfo : function(acctId,acctItemId){
		that = this;
		Ext.Ajax.request({
			scope : this,
			url: Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemInactive.action",
			params : {
				acctId : acctId,
				acctItemId : acctItemId
			},
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				if(rec.length > 0){
					this.items.itemAt(0).getEl().dom.innerHTML = AcctItemInfoTpl.applyTemplate(rec[0]);
				}
			}
		});
	}
});
 
/**
 * 账户信息面板
 */
AcctPanel = Ext.extend(BaseInfoPanel,{
		// 面板属性定义
	acctGrid: null,
	acctItemGrid: null,
	acctItemDetailTab:null,
	constructor: function(){
		this.acctGrid = new AcctGrid(this);
		this.acctItemGrid = new AcctItemGrid(this);
		this.acctItemDetailTab = new AcctItemDetailTab();
		AcctPanel.superclass.constructor.call(this, {
			id : 'acctPanel',
			layout:"anchor",
			border:false,
			items:[{
				anchor:"100% 62.3%",
				layout:'fit',
				layout:"fit",
				border: true,
				bodyStyle: 'border-top-width: 0;border-right-width: 0;',
				items:[this.acctItemGrid]
			},{
				anchor:"100% 38.1%",
				layout:'fit',
				height: 200,
				border: false,
				bodyStyle: 'border-left-width: 1px;',
				items:[this.acctItemDetailTab]
			}]
		});
	},
	refresh:function(){
//		this.acctItemGrid.reset();
		this.acctItemDetailTab.resetPanel();
		this.acctItemGrid.remoteRefresh();
	}
});
Ext.reg( "acctPanel" , AcctPanel );