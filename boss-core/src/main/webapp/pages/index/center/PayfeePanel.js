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
							'busi_optr_id','busi_optr_name','prod_sn','invoice_fee',"doc_type","doc_type_text","invoice_mode_text","allow_done_code"]
				});
		this.acctFeeStore.on("load",this.doOperate);
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:'流水号',dataIndex:'create_done_code',width:80},
				{header:'业务名称',dataIndex:'busi_name',	width:70},
				{header:'账户类型',dataIndex:'acct_type_text',width:70},
				{header:'账目名称',dataIndex:'fee_text',width:130,renderer:App.qtipValue},
				{header:'用户类型',dataIndex:'user_type_text',width:70},
				{header:'用户名',dataIndex:'user_name',width:70},
				{header:'智能卡号',dataIndex:'device_code',width:130},
				{header:'状态',dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
				{header:'金额',dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},				
				{header:'缴费前预计到期日',dataIndex:'begin_date',width:125,renderer:Ext.util.Format.dateFormat},
				{header:'缴费后预计到期日',dataIndex:'prod_invalid_date',width:125,renderer:Ext.util.Format.dateFormat},
				{header:'打印状态',dataIndex:'is_doc_text',width:60,renderer:Ext.util.Format.statusShow},
				{header:'付款方式',dataIndex:'pay_type_text',width:60},
				{header:'受理日期',dataIndex:'create_time',width:125},
				{header:'账务日期',dataIndex:'acct_date',width:75,renderer:Ext.util.Format.dateFormat},
				{header:'受理人',dataIndex:'optr_name',width:60},
				{header:'受理部门',dataIndex:'dept_name',width:60},
				{header:'发票',dataIndex:'invoice_id',width:80},
				{header:'出票方式',dataIndex:'invoice_mode_text',width:80},
				{header:'发票类型',dataIndex:'doc_type_text',width:80},
				{header:'业务员',dataIndex:'busi_optr_name',width:80,renderer:App.qtipValue}
	        ]
	     });
		
		var pageTbar = new Ext.PagingToolbar({store: this.acctFeeStore ,pageSize : this.pageSize});
		AcctFeeGrid.superclass.constructor.call(this, {
					id:'P_ACCT',
					title : '预存费用',
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
							'busi_optr_id','busi_optr_name','invoice_fee',"doc_type","doc_type_text","invoice_mode_text"]
				});
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    			{header:'流水号',dataIndex:'create_done_code',width:80},
				{header:'费用名称',dataIndex:'fee_text',width:110},
				{header:'设备类型',dataIndex:'device_type_text',width:55},
				{header:'设备编号',dataIndex:'device_code',width:140},
				{header:'状态',dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
				{header:'打印状态',dataIndex:'is_doc_text',width:60,renderer:Ext.util.Format.statusShow},
				{header:'应付',dataIndex:'should_pay',width:60,renderer:Ext.util.Format.formatFee},
				{header:'实付',dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},
				{header:'付款方式',dataIndex:'pay_type_text',width:60},
				{header:'受理日期',dataIndex:'create_time',width:125},
				{header:'受理人',dataIndex:'optr_name',width:60},
				{header:'受理部门',dataIndex:'dept_name',width:80},
				{header:'发票',dataIndex:'invoice_id',width:90},
				{header:'出票方式',dataIndex:'invoice_mode_text',width:80},
				{header:'发票类型',dataIndex:'doc_type_text',width:80},
				{header:'业务员',dataIndex:'busi_optr_name',width:80,renderer:App.qtipValue}
	        ]
	      });
		
		BusiFeeGrid.superclass.constructor.call(this, {
					id:'P_BUSI',
					title : '业务费用',
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
 */
PayfeePanel = Ext.extend(BaseInfoPanel, {

			// 面板属性定义
			acctFeeGrid : null,
			busiFeeGrid : null,
			// other property
			mask : null,

			constructor : function() {
				// 子面板实例化
				this.acctFeeGrid = new AcctFeeGrid();
				this.busiFeeGrid = new BusiFeeGrid();
				PayfeePanel.superclass.constructor.call(this, {
							border : false,
							layout : 'anchor',
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
						});
			},
			refresh : function() {
				this.acctFeeGrid.remoteRefresh();
				this.busiFeeGrid.remoteRefresh();
			}
		});
Ext.reg("payfeepanel", PayfeePanel);