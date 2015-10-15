/**
 * 账单销账记录.
 * @class BillWriteOffWin
 * @extends Ext.Window
 */
BillWriteOffWin = Ext.extend(Ext.Window,{
	store:null,title:'账单销账记录',grid :null,layout:'fit',autoDestroy:true,
	constructor:function(data){
		this.store = new Ext.data.JsonStore({
			autoLoad:false,
			url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryCustBillWriteOff.action",
			fields : ['fee', 'writeoff_date','fee_type','fee_type_text' ,'bill_sn']
		});
		
		this.store.load({params : {query:data.bill_sn}});
		
		this.grid = new Ext.grid.GridPanel({
            		border:false,
            		store:this.store,
            		columns:[
            			{header:'资金类型',dataIndex:'fee_type_text',width:100,renderer:App.qtipValue},
            			{header:'销账时间',dataIndex:'writeoff_date',width:150,renderer:App.qtipValue},
            			{header:'销账金额',dataIndex:'fee',width:100,renderer:Ext.util.Format.formatFee}
            		]
            	});
		
		BillWriteOffWin.superclass.constructor.call(this,{
			width:370,height:220,border:false,
			items:this.grid
		});
	}
});

/**
 * 账单信息
 */
BillGrid = Ext.extend(Ext.ux.Grid, {
	border : false,
	acctFeeStore : null,
	region : 'center',
	pageSize : 10,
	preBDoneCode:null,
	showWriteOff:function(){
		if(!App.getApp().getCustId()){
			Alert('请查询客户之后再做操作.');
			return false;
		}
		var recs = this.selModel.getSelections();
		if(!recs || recs.length !=1){
			Alert('请选择且仅选择一条记录!');
			return false;
		}
		var rec = recs[0];
		
		var win = new BillWriteOffWin(rec.data);
		win.show();
	},
	constructor : function() {
		this.billStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryCustBill.action",
			totalProperty:'totalProperty',
			root:'records',
			fields : ['acctitem_name', 'owe_fee','tariff_name', 'bill_sn','cust_id',
					'acct_id', 'acctitem_id', {name : 'final_bill_fee',type : 'float'},{name : 'bill_fee',type : 'float'},
					'user_id','tariff_id','billing_cycle_id','come_from','status','fee_flag',
					'bill_type', 'bill_done_code','status', 'bill_date','come_from_text','card_id']
			,
			listeners:{
				scope:this,
				beforeload:function(store){
					var gp = Ext.getCmp('radiogroupBtn');
					store.baseParams['queryFeeInfo.oweFee'] = gp.getValue();
					var doneCmp = Ext.getCmp('doneCodeFilter'); 
					store.baseParams['queryFeeInfo.bill_done_code'] = doneCmp.getValue();
					var cardCmp = Ext.getCmp('cardFilter');
					store.baseParams['queryFeeInfo.card_id'] = cardCmp.getValue();
				}
			}
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var lc = langUtils.main("bill.list.columns");
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			sm,
    		    {header:lc[0],dataIndex:'billing_cycle_id',width:80},
    		    {header:lc[1],dataIndex:'come_from_text',width:80},
    		    {header:lc[2],dataIndex:'card_id',width:120,renderer:App.qtipValue},
    			{header:lc[3],dataIndex:'bill_done_code',width:80},
    			{header:lc[4],dataIndex:'bill_date',	width:130},
    			{header:lc[5],dataIndex:'acctitem_name',width:130,renderer:App.qtipValue},
    			{header:lc[6],dataIndex:'tariff_name',width:80},
				{header:lc[7],dataIndex:'status',width:65,renderer : this.renderStatus},
				{header:lc[8],dataIndex:'final_bill_fee',width:80,renderer:Ext.util.Format.formatFee},
				{header:lc[9],dataIndex:'owe_fee',width:80,renderer:Ext.util.Format.formatFee},
				{header:lc[10],dataIndex:'owe_fee',scope:this,width:50,renderer:function(){
					return '<div class="icon-query" style="cursor:pointer;" onclick="Ext.getCmp(\'B_BILL\').showWriteOff()"; ext:qtitle="" ext:qtip="查看销账记录">&nbsp;</div>';
				}}
	        ]
	     });
		
		var tbarText = langUtils.main("bill.list.tbar");
		BillGrid.superclass.constructor.call(this, {
					id:'B_BILL',
					title : langUtils.main("bill.list._title"),
					loadMask : true,
					store : this.billStore,
					border : false,
					disableSelection:true,
					view: new Ext.ux.grid.ColumnLockBufferView(),
					sm : sm,cm : cm,
					bbar: new Ext.PagingToolbar({store: this.billStore, pageSize : this.pageSize}),
					tbar:['-',
					{xtype:'hidden',id:'radiogroupBtn',text:'欠费账单',value:true,getValue:function(){return this.value}},
					{text : tbarText[0]} ,
					{xtype:'textfield',id:'cardFilter',scope : this},'-',
					{text : tbarText[1]} ,
					{xtype:'textfield',id:'doneCodeFilter',scope : this},'-',
					{text : tbarText[2],iconCls:'ca_3',scope : this,handler : function(){
							if(!App.getCustId()){return;}
							Ext.getCmp('radiogroupBtn').value = true;
							this.billStore.load({params:{start: 0,limit: this.pageSize}});
						}
					},'-',{text : tbarText[3],iconCls:'ca_2',scope : this,handler : function(){
							if(!App.getCustId()){return;}
							Ext.getCmp('radiogroupBtn').value = false;
							this.billStore.load({params:{start: 0,limit: this.pageSize}});
						}
					} ,'->'
//					,'-',{text : '查看销账记录',scope:this,handler:this.showWriteOff} ,'-'
					],
					tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
						var comp = this.tools.search;
						if(this.billStore.getCount()>0){
							if(win)	win.close();
							win = FilterWindow.addComp(this,[
								{text:'出账时间',field:'bill_date',format:'Ym',plugins:'monthPickerPlugin', type:'datefield'},
								{text:'账目名称',field:'acctitem_name',type:'textfield'},
								{text:'资费名称',field:'tariff_name',type:'textfield'}
							],480,null,true,"queryFeeInfo");
								
							if(win){
								win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
								win.show();
							}
						}else{
							Alert(lbc('common.emptyMsg'));
						}
			    	}
			    }],
			    listeners:{
			    	scope:this,
					rowdblclick: function(grid,rowIndex){
			    		var thiz = this;
			    		var selectRecordsFunc = function(billDoneCode){
			    			if(!Ext.isEmpty(billDoneCode)){
					    		thiz.getStore().each(function(r){
					    			if(r.get('bill_done_code') == billDoneCode){
					    				thiz.getSelectionModel().selectRow(thiz.getStore().indexOf(r), true);
					    			}
					    		},thiz);
				    		}
				    		thiz.preBDoneCode = billDoneCode;
			    		}
			    		
			    		var record = this.getSelectionModel().getSelected();
			    		var billDoneCode = record.get('bill_done_code');
			    		if(this.preBDoneCode && this.preBDoneCode == billDoneCode){
	    					this.getSelectionModel().clearSelections();
	    					this.preBDoneCode = null;
			    		}else{
				    		selectRecordsFunc(billDoneCode);
			    		}
			    	},
			    	afterrender: this.swapViews,
			    	delay:10
			    }
			});
	},
	remoteRefresh : function() {
		this.billStore.baseParams = {custId: App.getApp().getCustId()};
		this.billStore.load({
			params:{
				start: 0,
				limit: this.pageSize
			}
		});
	},
	renderStatus : function(value){
		if(value == '0'){
			return '未出账';
		}else if(value == '1'){
			return '已出账';
		}else if(value == '2'){
			return '呆账';
		}else if(value == '3'){
			return '坏账';
		}else if(value == '4'){
			return '已作废';
		}
	}
});

/**
 * 账目作废信息
 */
AcctitemInvalidGrid = Ext.extend(Ext.ux.Grid, {
	border : false,
	acctitemInvalidStore : null,
	region : 'center',
	pageSize : 5,
	constructor : function() {
		this.acctitemInvalidStore = new Ext.data.JsonStore({
					fields : ['acct_id','acctitem_id','fee_type',
							 {name : 'invalid_fee',type : 'int'},'fee_type_text','acctitem_name','can_refund'
							]
				});
		var lc = langUtils.main("bill.acctitemInvalid.columns")
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:lc[0],dataIndex:'acctitem_name',width:150},
				{header:lc[1],dataIndex:'fee_type_text',width:80},
				{header:lc[2],dataIndex:'invalid_fee',width:100,renderer:Ext.util.Format.formatFee}
	        ]
	      });
		
		var pageTbar = new Ext.PagingToolbar({store: this.acctitemInvalidStore ,pageSize : this.pageSize});
		pageTbar.refresh.hide();
		AcctitemInvalidGrid.superclass.constructor.call(this, {
					id:'B_INVALID',
					title : langUtils.main("bill.acctitemInvalid._title"),
					loadMask : true,
					store : this.acctitemInvalidStore,
					border : false,
					bbar: pageTbar,
					sm : new Ext.grid.RowSelectionModel(),
					view: new Ext.ux.grid.ColumnLockBufferView(),
					cm : cm
				});
	},
	initEvents :function(){
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		AcctitemInvalidGrid.superclass.initEvents.call(this);
	},
	remoteRefresh : function() {
		this.refresh();
	},
	refresh:function(){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH + "/core/x/Acct!queryAcctitemInvalidByCustId.action",
			scope:this,
			params:{
				custId:App.getData().custFullInfo.cust.cust_id
			},
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				//PagingMemoryProxy() 一次性读取数据
				this.acctitemInvalidStore.proxy = new Ext.data.PagingMemoryProxy(data),
				//本地分页
				this.acctitemInvalidStore.load({params:{start:0,limit:this.pageSize}});
			}
		});
	}

});

/**
 */
BillPanel = Ext.extend(BaseInfoPanel, {
			// 面板属性定义
			billGrid : null,
			acctitemInvalidGrid : null,
			// other property
			mask : null,
			constructor : function() {
				// 子面板实例化
				this.billGrid = new BillGrid();
				this.acctitemInvalidGrid = new AcctitemInvalidGrid();
				BillPanel.superclass.constructor.call(this, {
							border : false,
							layout : 'anchor',
							items : [{
										layout : 'fit',
										border : false,
										anchor : "100% 60%",
										bodyStyle: 'border-bottom-width: 1px',
										defaults: {border: false},
										items : [this.billGrid]
									}, {
										layout : 'fit',
										border : false,
										anchor : "100% 40%",
										items : [this.acctitemInvalidGrid]
									}]
						});
			},
			refresh : function() {
				this.billGrid.remoteRefresh();
				this.acctitemInvalidGrid.remoteRefresh();
			}
		});
Ext.reg("billpanel", BillPanel);