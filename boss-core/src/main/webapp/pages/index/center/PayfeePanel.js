/**
 * 预存费用
 */
AcctFeeGrid = Ext.extend(Ext.ux.Grid, {
	border : false,
	acctFeeStore : null,
	region : 'center',
	pageSize : 10,
	constructor : function() {
		this.acctFeeStore = new Ext.data.JsonStore({
					url:Constant.ROOT_PATH+ "/commons/x/QueryCust!queryAcctPayFee.action",
					totalProperty:'totalProperty',
					root:'records',
					fields : ['acct_type_text', 'acct_type', 'fee_text','is_logoff','is_doc','is_doc_text',
							'fee_type', 'fee_type_text', {name : 'real_pay',type : 'float'},
							'create_time','acct_date','device_code','invoice_mode','invoice_book_id','finance_status',
							'invoice_code', 'invoice_id', 'pay_type_text','begin_date','prod_invalid_date',
							'pay_type', 'status_text', 'status', 'dept_id','dept_name',
							'user_id','user_name', 'user_type_text','fee_sn','optr_id','optr_name',
							'OPERATE','busi_code','busi_name','create_done_code','data_right',
							'busi_optr_id','busi_optr_name','prod_sn','invoice_fee',"doc_type","doc_type_text","invoice_mode_text","allow_done_code",'count_text']
				});
		this.acctFeeStore.on("load",this.doOperate);
		var lc = langUtils.main("pay.payfee.columns");
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:lc[0],dataIndex:'create_done_code',width:80},
				{header:lc[1],dataIndex:'busi_name',	width:150,renderer:App.qtipValue},
				{header:lc[2],dataIndex:'acct_type_text',width:105,renderer:App.qtipValue},
				{header:lc[3],dataIndex:'fee_text',width:150,renderer:App.qtipValue},
				{header:lc[4],dataIndex:'user_type_text',width:70},
				{header:lc[5],dataIndex:'user_name',width:130,renderer:App.qtipValue},
				{header:lc[6],dataIndex:'device_code',width:130,renderer:App.qtipValue},
				{header:lc[7],dataIndex:'status_text',width:70,renderer:Ext.util.Format.statusShow},
				{header:lc[8],dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},				
				{header:lc[9],dataIndex:'begin_date',width:125,renderer:Ext.util.Format.dateFormat},
				{header:lc[10],dataIndex:'prod_invalid_date',width:125,renderer:Ext.util.Format.dateFormat},
				{header:lc[11],dataIndex:'is_doc_text',width:75,renderer:Ext.util.Format.statusShow},
				{header:lc[12],dataIndex:'pay_type_text',width:75, renderer:App.qtipValue},
				{header:lc[13],dataIndex:'create_time',width:125},
				{header:lc[14],dataIndex:'acct_date',width:125},
				{header:lc[15],dataIndex:'optr_name',width:90, renderer:App.qtipValue},
				{header:lc[16],dataIndex:'dept_name',width:100, renderer:App.qtipValue},
				{header:lc[17],dataIndex:'invoice_id',width:80, renderer:App.qtipValue},
//				{header:lc[18],dataIndex:'invoice_mode_text',width:80},
				{header:lc[19],dataIndex:'doc_type_text',width:90, renderer:App.qtipValue},
				{header:lc[20],dataIndex:'busi_optr_name',width:100, renderer:App.qtipValue}
	        ]
	     });
		
		var pageTbar = new Ext.PagingToolbar({store: this.acctFeeStore ,pageSize : this.pageSize});
		AcctFeeGrid.superclass.constructor.call(this, {
					id:'P_ACCT',
					title : langUtils.main("pay.payfee._title"),
					loadMask : true,
					store : this.acctFeeStore,
					border : false,
					bbar: pageTbar,
					disableSelection:true,
					view: new Ext.ux.grid.ColumnLockBufferView(),
					sm : new Ext.grid.RowSelectionModel(),
					cm : cm,
					tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
						var comp = this.tools.search;
						if(this.acctFeeStore.getCount()>0){
							if(win)win.close();
								win = FilterWindow.addComp(this,[
									{text:'智能卡号',field:'device_code',type:'textfield'},
									{text:'状态',field:'status',showField:'status_text',
										data:[
											{'text':'所有','value':''},
											{'text':'已支付','value':'PAY'},
											{'text':'失效','value':'INVALID'}
										]
									},
									{text:'受理日期',field:'create_time',type:'datefield'},
									{text:'受理人',field:'optr_name',type:'textfield'},
									{text:'发票号',field:'invoice_id',type:'textfield'}
									],700,null,true,"queryFeeInfo");
							if(win){
								win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
								win.show();
							}
						}else{
							Alert('请先查询数据！');
						}
			    	}
				    }]
				});
	},
	initEvents :function(){
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		AcctFeeGrid.superclass.initEvents.call(this);
	},
	doOperate:function(){
		//不是当前登录人的不能进行“冲正”操作(去掉不符合条件行的“冲正”操作按钮)
		this.each(function(record){
			if(record.get('optr_name') != App.getData().optr['optr_name']){
				record.set('OPERATE','F');
			}
		});
		this.commitChanges();
	},
	remoteRefresh : function() {
		this.refresh();
	},
	localRefresh : function() {
		unitRecords = this.getSelectionModel().getSelections();
		for (var i in unitRecords) {
			this.acctFeeStore.remove(unitRecords[i]);
		}
	},
	refresh:function(){
		this.acctFeeStore.baseParams = {
			residentCustId: App.getData().custFullInfo.cust.cust_id,
			custStatus : App.data.custFullInfo.cust.status
		};
//		this.acctFeeStore.baseParams['residentCustId'] = App.getData().custFullInfo.cust.cust_id;
//		this.acctFeeStore.baseParams['custStatus'] = App.data.custFullInfo.cust.status;
//		this.acctFeeStore.load({params:{start: 0,limit: this.pageSize}});
		this.reloadCurrentPage();
	}

});

/**
 * 业务费用
 */
BusiFeeGrid = Ext.extend(Ext.ux.Grid, {
	border : false,
	busiFeeStore : null,
	region : 'center',
	pageSize : 10,
	constructor : function() {
		this.busiFeeStore = new Ext.data.JsonStore({
					url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryBusiPayFee.action",
					totalProperty:'totalProperty',
					root:'records',
					fields : ['create_done_code','fee_text', 'device_type', 'device_type_text',
							'device_code', {name : 'should_pay',type : 'int'},'is_doc','is_doc_text',
							'pay_type_text', 'pay_type', 'status_text','dept_name','dept_id',
							'status', 'invoice_code', 'invoice_id','invoice_mode', 'optr_id','optr_name',
							{name : 'real_pay',type : 'int'},'fee_sn','create_time','acct_date','deposit',
							'data_right','finance_status','invoice_book_id','is_busi_fee',
							'busi_optr_id','busi_optr_name','invoice_fee',"doc_type","doc_type_text",
							"invoice_mode_text",'buy_num','device_model','device_model_name', 'count_text']
				});
		var lc = langUtils.main("pay.busifee.columns");
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:lc[0],dataIndex:'create_done_code',width:80},
				{header:lc[1],dataIndex:'fee_text',width:110, renderer:App.qtipValue},
				{header:lc[2],dataIndex:'device_type_text',width:80},
				{header:lc[16],dataIndex:'device_model_name',width:120, renderer:App.qtipValue},
				{header:lc[3],dataIndex:'device_code',width:130, renderer:App.qtipValue},
				{header:lc[4],dataIndex:'status_text',width:70,renderer:Ext.util.Format.statusShow},
				{header:lc[5],dataIndex:'is_doc_text',width:80,renderer:Ext.util.Format.statusShow},
				{header:lc[6],dataIndex:'should_pay',width:60,renderer:Ext.util.Format.formatFee},
				{header:lc[7],dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},
				{header:lc[17],dataIndex:'count_text',width:120,renderer:App.qtipValue},
				{header:lc[15],dataIndex:'buy_num',width:80},
				{header:lc[8],dataIndex:'pay_type_text',width:90, renderer:App.qtipValue},
				{header:lc[9],dataIndex:'create_time',width:125},
				{header:lc[19],dataIndex:'acct_date',width:125},
				{header:lc[10],dataIndex:'optr_name',width:90, renderer:App.qtipValue},
				{header:lc[11],dataIndex:'dept_name',width:100, renderer:App.qtipValue},
				{header:lc[12],dataIndex:'invoice_id',width:90, renderer:App.qtipValue},
//				{header:lc[13],dataIndex:'invoice_mode_text',width:80},
				{header:lc[14],dataIndex:'doc_type_text',width:90, renderer:App.qtipValue},
				{header:lc[18],dataIndex:'busi_optr_name',width:100, renderer:App.qtipValue}
	        ]
	      });
		
		BusiFeeGrid.superclass.constructor.call(this, {
					id:'P_BUSI',
					title : langUtils.main("pay.busifee._title"),
					loadMask : true,
					store : this.busiFeeStore,
					border : false,
					bbar: new Ext.PagingToolbar({store: this.busiFeeStore ,pageSize : this.pageSize}),
					sm : new Ext.grid.RowSelectionModel(),
					view: new Ext.ux.grid.ColumnLockBufferView(),
					cm : cm,
					listeners:{
						scope:this,
						delay:10,
						rowdblclick: this.doDblClickRecord,
						afterrender: this.swapViews
					},
					tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
							var comp = this.tools.search;
							if(this.busiFeeStore.getCount()>0){
								if(win)win.close();
								win = FilterWindow.addComp(this,[
									{text:'设备类型',field:'device_type',showField:'device_type_text',
										data:[
											{'text':'设备类型','value':''},
											{'text':'机顶盒','value':'STB'},
											{'text':'智能卡','value':'CARD'},
											{'text':'MODEM','value':'MODEM'}
										]
									},
									{text:'设备编号',field:'device_code',type:'textfield'},
									{text:'受理日期',field:'create_time',type:'datefield'},
									{text:'状态',field:'status',showField:'status_text',
										data:[
											{'text':'所有','value':''},
											{'text':'已支付','value':'PAY'},
											{'text':'失效','value':'INVALID'}
										]
									},
									{text:'受理人',field:'optr_name',type:'textfield'},
									{text:'发票号',field:'invoice_id',type:'textfield'}
								],800,null,true,"queryFeeInfo");
								if(win){	
									win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
									win.show();
								}
							}else{
								Alert('请先查询数据！');
							}
				    	}
				    }]
				});
	},
	doDblClickRecord : function(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
	},
	remoteRefresh : function() {
		this.refresh();
	},
	refresh:function(){
		/*Ext.Ajax.request({
			url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryBusiPayFee.action",
			scope:this,
			params:{
				residentCustId:App.getData().custFullInfo.cust.cust_id,
				custStatus : App.data.custFullInfo.cust.status
			},
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				//PagingMemoryProxy() 一次性读取数据
				this.busiFeeStore.proxy = new Ext.data.PagingMemoryProxy(data),
				//本地分页
				this.busiFeeStore.load({params:{start:0,limit:this.pageSize}});
			}
		});*/
//		this.busiFeeStore.baseParams['residentCustId'] = App.getData().custFullInfo.cust.cust_id;
		this.busiFeeStore.baseParams = {
			residentCustId: App.getData().custFullInfo.cust.cust_id
		};
//		this.busiFeeStore.load({params:{start: 0,limit: this.pageSize}});
		this.reloadCurrentPage();
	},
	localRefresh : function() {
		unitRecords = this.getSelectionModel().getSelections();
		for (var i in unitRecords) {
			this.busiFeeStore.remove(unitRecords[i]);
		}
	}

});


/**
 * 订单金额明细
 */
CfeePayWindow = Ext.extend(Ext.Window, {
	feeStore:null,
	constructor: function(){
		this.feeStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryFeePayDetail.action",
			fields: ["real_pay","fee_text","invoice_id","create_done_code"]
		})
		var lc = langUtils.main("pay.feePayDetail.columns");
		var columns = [
					   {header: lc[0], width: 80,sortable:true, dataIndex: 'create_done_code',renderer:App.qtipValue},
		               {header: lc[1],sortable:true, dataIndex: 'fee_text',renderer:App.qtipValue},
		               {header: lc[2], width: 70, sortable:true, dataIndex: 'real_pay',renderer:Ext.util.Format.formatFee},
					   {header: lc[3], width: 80,sortable:true, dataIndex: 'invoice_id',renderer:App.qtipValue}
           ];
		return CfeePayWindow.superclass.constructor.call(this, {
			layout:"fit",
			title: langUtils.main("pay.feePayDetail._title"),
			width: 600,
			height: 300,
			resizable: false,
			maximizable: false,
			closeAction: 'hide',
			minimizable: false,
			items: [{
				xtype: 'grid',
				stripeRows: true,
				border: false,
				store: this.feeStore,
				columns: columns,
				viewConfig:{forceFit:true},
		        stateful: true
			}]
		});
	},
	show: function(sn){
		this.feeStore.baseParams = {
			paySn: sn
		};
		this.feeStore.load();
		return CfeePayWindow.superclass.show.call(this);
	}
});


FeePayGrid = Ext.extend(Ext.ux.Grid, {
	feePayStore : null,	
	pageSize : 20,
	cfeePayWindow:null,
	feePayData: [],
	constructor : function() {
		this.feePayStore = new Ext.data.JsonStore({
					url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryFeePay.action",
					totalProperty:'totalProperty',
					root:'records',
					fields : ['pay_sn','reverse_done_code', 'pay_type', 'pay_type_text',
							'done_code', {name : 'usd',type : 'int'},'receipt_id','is_valid_text',
							'is_valid', 'payer', 'acct_date','dept_name','dept_id',
							'invoice_mode', 'invoice_mode_text', 'remark',{name : 'exchange',type : 'int'},
								{name : 'cos',type : 'int'}, 'optr_id','optr_name',
							{name : 'khr',type : 'int'},'create_time','data_right']
				});
		var lc = langUtils.main("pay.detail.columns");
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:lc[0],dataIndex:'pay_sn',width:85,renderer:function(value,metaData,record){
						that = this;
						if(value != ''){
							return '<div style="text-decoration:underline;font-weight:bold"  onclick="Ext.getCmp(\'P_FEE_PAY\').doTransferFeeShow();"  ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}else{
							return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}
					}},
				{header:lc[1],dataIndex:'usd',width:50,renderer:Ext.util.Format.formatFee},
				{header:lc[5],dataIndex:'is_valid_text',width:70,renderer:Ext.util.Format.statusShow},
				{header:lc[2],dataIndex:'khr',width:50,renderer:Ext.util.Format.formatFee},
				{header:lc[3],dataIndex:'exchange',width:70},
				{header:lc[4],dataIndex:'cos',width:100,renderer:Ext.util.Format.formatFee},
				{header:lc[6],dataIndex:'pay_type_text',width:100},
				{header:lc[7],dataIndex:'payer',width:70},
				{header:lc[8],dataIndex:'done_code',width:80},
				{header:lc[9],dataIndex:'receipt_id',width:100},
//				{header:lc[10],dataIndex:'invoice_mode_text',width:80},
				{header:lc[11],dataIndex:'create_time',width:125},
				{header:lc[12],dataIndex:'optr_name',width:100},
				{header:lc[13],dataIndex:'dept_name',width:100}
	        ]
	      });
		
		FeePayGrid.superclass.constructor.call(this, {
					id:'P_FEE_PAY',
					title : langUtils.main("pay.detail._title"),
					region:"east",
					width:"30%",
					split:true,
					loadMask : true,
					store : this.feePayStore,
//					border : false,
					bbar: new Ext.PagingToolbar({store: this.feePayStore ,pageSize : this.pageSize}),
					sm : new Ext.grid.RowSelectionModel(),
					view: new Ext.ux.grid.ColumnLockBufferView(),
					cm : cm,
					listeners:{
						scope:this,
						delay:10,
						rowdblclick: this.doDblClickRecord,
						afterrender: this.swapViews
					},
					tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
							var comp = this.tools.search;
							if(this.feePayStore.getCount()>0){
								if(win)win.close();
								win = FilterWindow.addComp(this,[									
									{text:'状态',field:'status',showField:'is_valid_text',
										data:[
											{'text':'所有','value':''},
											{'text':'有效','value':'T'},
											{'text':'失效','value':'F'}
										]
									},
									{text:'受理日期',field:'create_time',type:'datefield'},
									{text:'受理人',field:'optr_name',type:'textfield'},
									{text:'发票号',field:'invoice_id',type:'textfield'}
								],580,null,true,"queryFeeInfo");
								if(win){	
									win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
									win.show();
								}
							}else{
								Alert('请先查询数据！');
							}
				    	}
				    }]					
				});
	},
	doDblClickRecord : function(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
	},
	remoteRefresh : function() {
		this.refresh();
	},
	refresh:function(){
		this.feePayStore.baseParams = {
			residentCustId: App.getData().custFullInfo.cust.cust_id
		};
//		this.busiFeeStore.load({params:{start: 0,limit: this.pageSize}});
		this.reloadCurrentPage();
	},
	localRefresh : function() {
		unitRecords = this.getSelectionModel().getSelections();
		for (var i in unitRecords) {
			this.feePayStore.remove(unitRecords[i]);
		}
	},
	doTransferFeeShow:function(){
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
		
		if(!this.cfeePayWindow){
			this.cfeePayWindow = new CfeePayWindow();
		}
		this.cfeePayWindow.show(rec.get('pay_sn'));
		
	}

});

/**
 */
PayfeePanel = Ext.extend(BaseInfoPanel, {
	// 面板属性定义
	acctFeeGrid : null,
	busiFeeGrid : null,
	feePayGrid	: null,
	// other property
	mask : null,
	constructor : function() {
		// 子面板实例化
		this.acctFeeGrid = new AcctFeeGrid();
		this.busiFeeGrid = new BusiFeeGrid();
		this.feePayGrid = new FeePayGrid();
		PayfeePanel.superclass.constructor.call(this, {
					layout:"border",
					border:false,
					items:[{
						region:"center",
						layout:"anchor",
//						border: false,
						items : [{
								layout : 'fit',
								border : false,
								anchor : "100% 60%",
								bodyStyle: 'border-bottom-width: 1px;',
								items : [this.acctFeeGrid]
							}, {
								layout : 'fit',
								border : false,
								anchor : "100% 40%",
								items : [this.busiFeeGrid]
							}]
							
						},this.feePayGrid
//						{
//							region:"west",
//							split:true,
//							width:"30%",
//							border: false,
//							items:[this.feePayGrid]
//						}
						]
				});
			},
			refresh : function() {
				this.acctFeeGrid.remoteRefresh();
				this.busiFeeGrid.remoteRefresh();
				this.feePayGrid.remoteRefresh();
			}
		});
Ext.reg("payfeepanel", PayfeePanel);