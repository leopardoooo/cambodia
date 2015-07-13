var QueryFeeForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		QueryFeeForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:75,
			layout:'column',
			items:[
				{xtype:'hidden',name:'countyId'},
				{columnWidth:.37,border:false,layout:'form',items:[
					{fieldLabel:'操作时间',xtype:'compositefield',allowBlank:false,
						combineErrors:false,items:[
						{xtype:'datefield',name:'beginOptrateDate',format:'Y-m-d',allowBlank:false},
				        {xtype:'displayfield',value:'至'},
				        {xtype:'datefield',name:'endOptrateDate',format:'Y-m-d',allowBlank:false}
					]},
					{fieldLabel:'账务日期',xtype:'compositefield',
						combineErrors:false,items:[
						{xtype:'datefield',name:'beginAcctDate',format:'Y-m-d'},
				        {xtype:'displayfield',value:'至'},
				        {xtype:'datefield',name:'endAcctDate',format:'Y-m-d'}
					]}
				]},
				{columnWidth:.35,border:false,layout:'form',items:[
					{fieldLabel:'营业厅',xtype:'treecombo',hiddenName:'deptId',
						allowBlank:false,
						expanded:false,
						minChars:0,
						treeUrl:root + '/system/x/Index!queryOtherDeptTree.action',
						width:180,
						treeWidth:400,
						height: 22,
						emptyText :'请选择分公司或营业厅',
						blankText:'请选择分公司或营业厅',
						listeners : {
							scope:this,
							focus: function(combo){
								if(combo.list){
									combo.expand();
								}
							},
							select: this.doSelectByDept,
							blur: function(combo){
								if(Ext.isEmpty(combo.getRawValue())){
									var optrComp = this.getForm().findField('optrId');
									optrComp.getStore().removeAll();
									optrComp.clearValue();
								}
							}
						}
					},
					{fieldLabel:'费用类型',xtype:'combo',hiddenName:'feeType',
						store: new Ext.data.ArrayStore({
							fields:['text','value'],
							data:[['全部','ALL'],['营业费','ACCT'],['杂费','BUSI'],['非营业收费','UNITPRE']]
						}),displayField:'text',valueField:'value',allowBlank:false,value:'ALL',
						listeners:{
							scope:this,
							select:this.doFeeTypeSelect
						}	
					}
				]},
				{columnWidth:.28,border:false,layout:'form',items:[
					{fieldLabel:'操作员',xtype:'combo',hiddenName:'optrId',
						store:new Ext.data.JsonStore({
							url:Constant.ROOT_PATH + '/core/x/User!getByDeptId.action',
							fields:['optr_id','optr_name']
						}),displayField:'optr_name',valueField:'optr_id',editable:true,forceSelection:true
					},{xtype:'textfield',fieldLabel:'客户编号',name:'cust_no'}
				]},
				{columnWidth:.50,border:false,layout:'form',items:[{fieldLabel:'发票号段',xtype:'compositefield',
						combineErrors:false,items:[
						{xtype:'textfield',name:'beginInvoice'},
				        {xtype:'displayfield',value:'至'},
				        {xtype:'textfield',name:'endInvoice'}
					]}]},
				{columnWidth:.28,bodyStyle:'padding-left:32px',border:false,layout:'form',items:[
					{id:'batchEdit_query_btn',xtype:'button',text:'查  询',iconCls:'icon-query',scope:this,handler:this.doQuery}
				]}
			]
		});
	},
	doSelectByDept:function(combo,obj){
		combo.hasFocus = false;
		var value = combo.getValue();
		var store = this.getForm().findField('optrId').getStore();
		store.removeAll();
		
		//分公司查询数据太多，取消，只按各个营业厅查询
		
//		var deptType = obj.attributes.others.dept_type;
//		if(deptType == 'FGS'){
//			this.getForm().findField('countyId').setValue(value);
//			this.getForm().findField('optrId').disable();
//		}else if(deptType == 'YYT'){
//			this.getForm().findField('countyId').setValue(null);
//			this.getForm().findField('optrId').enable();
			store.load({params:{deptId:value}});
//		}
	},
	doFeeTypeSelect: function(combo){
		var value = combo.getValue();
		var grid = this.parent.grid;
		var busi = this.parent.busi;
		var feeType = grid.currFeeType;
		if(feeType =='BUSI'){
			if(value == 'ACCT'){
				grid.busiFeeStore.removeAll();
				grid.reconfigure(grid.acctFeeStore,grid.acctCm);
				grid.setTitle('营业费信息');
				Ext.getCmp('BusiId').hide();
			}else if(value == 'ALL'){
				grid.busiFeeStore.removeAll();
				grid.reconfigure(grid.acctFeeStore,grid.acctCm);
				grid.setTitle('营业费信息');
				Ext.getCmp('BusiId').show();
			}
		}else if(feeType =='ACCT'){
			if(value == 'BUSI'){
				grid.acctFeeStore.removeAll();
				grid.reconfigure(grid.busiFeeStore,grid.busiCm);
				grid.setTitle('杂费信息');
			}else if(value == 'ALL'){
				Ext.getCmp('BusiId').show();
			}
			
		}else if(feeType =='ALL'){
			if(value == 'BUSI'){
				grid.acctFeeStore.removeAll();
				grid.reconfigure(grid.busiFeeStore,grid.busiCm);
				grid.setTitle('杂费信息');
				busi.busiFeeStore.removeAll();	
				Ext.getCmp('BusiId').hide();
			}else if(value == 'ACCT'){
				grid.acctFeeStore.removeAll();
				busi.busiFeeStore.removeAll();				
				Ext.getCmp('BusiId').hide();
			}
		}
		
		this.parent.doLayout();
		this.parent.grid.currFeeType = value;
	},
	doQuery:function(){
		var form = this.getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		
		if(!Ext.isEmpty(values.beginAcctDate) && Ext.isEmpty(values.endAcctDate)){
			Alert('账务日期开始时间和结束时间必须都为空或者都填写!');
			this.form.findField('endAcctDate').markInvalid('不能为空');
			return false;
		}else if(!Ext.isEmpty(values.endAcctDate) && Ext.isEmpty(values.beginAcctDate)){
			Alert('账务日期开始时间和结束时间必须都为空或者都填写!');
			this.form.findField('beginAcctDate').markInvalid('不能为空');
			return false;
		}else{
			this.form.findField('beginAcctDate').clearInvalid();
			this.form.findField('endAcctDate').clearInvalid();
		}
		
		values.countyId = App.getData().optr.county_id;
		var acctDateMinValue = "2010-01-01";
		try{
			var grid = this.parent.grid;
			var feeType = grid.currFeeType;
			grid.getEl().mask('查询中，请稍后','x-mask-loading');
			grid.getStore().load({
				params:values,
				callback:function(){
					grid.getEl().unmask();
					grid.getSelectionModel().selectAll();
					if(grid.getStore().getCount()>0){
						//最小值为2009-12-31
						App.acctDate(Ext.getCmp('acctDateId'),function(){
							Ext.getCmp('acctDateId').setMinValue(Date.parseDate(acctDateMinValue,'Y-m-d'));
						});
					}
				}
			});	
			if(feeType == 'ALL'){
				var busi = this.parent.busi;
				busi.getEl().mask('查询中，请稍后','x-mask-loading');
				busi.getStore().load({
					params:values,
					callback:function(){
						busi.getEl().unmask();
						busi.getSelectionModel().selectAll();
						if(busi.getStore().getCount()>0){
							App.acctDate(Ext.getCmp('acctDateId'),function(){
								Ext.getCmp('acctDateId').setMinValue(Date.parseDate(acctDateMinValue,'Y-m-d'));
							});
						}
					}
				});	
			}
		}catch(e){
		}finally{
			Ext.getCmp('batchEdit_query_btn').getEl().unmask();
		}
	}
});

BatchAcctGrid = Ext.extend(Ext.grid.GridPanel, {
	border : false,
	currFeeType: 'ALL',//当前费用类型
	acctFeeStore: null,//预存费store
	busiFeeStore: null,//业务费store
	acctCm: null,//预存费column
	busiCm: null,//业务费column
	parent:null,
	constructor : function(p) {
		this.parent = p;
		this.acctFeeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + '/commons/x/QueryCust!queryBatchAcctPayFee.action',
			fields : ['acct_type_text', 'acct_type', 'fee_text','is_logoff','cust_id',
					'dept_id','county_id','area_id',
					'fee_type', 'fee_type_text', {name : 'real_pay',type : 'float'},
					'create_time','acct_date','device_code','invoice_mode',
					'invoice_code', 'invoice_id', 'pay_type_text','begin_date','prod_invalid_date',
					'pay_type', 'status_text', 'status', 'dept_id','dept_name',
					'user_id','user_name', 'user_type_text','fee_sn','optr_id','optr_name','OPERATE','busi_name','create_done_code']
		});
		this.busiFeeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + '/commons/x/QueryCust!queryBatchBusiPayFee.action',
			fields : ['create_done_code','fee_text', 'device_type', 'device_type_text',
					'device_code', {name : 'should_pay',type : 'int'},
					'pay_type_text', 'pay_type', 'status_text','dept_name','dept_id',
					'status', 'invoice_code', 'invoice_id','invoice_mode', 'optr_id','optr_name',
					{name : 'real_pay',type : 'int'},'fee_sn','create_time','acct_date','deposit',
					'data_right','finance_status','invoice_book_id','is_busi_fee'
					]
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.acctCm = new Ext.ux.grid.LockingColumnModel({
				columns:[
					sm,
	    			{header:'流水号',dataIndex:'create_done_code',width:70},
					{header:'业务名称',dataIndex:'busi_name',	width:70},
					{header:'账户类型',dataIndex:'acct_type_text',width:70},
					{header:'账目名称',dataIndex:'fee_text',width:130,renderer:App.qtipValue},
					{header:'用户类型',dataIndex:'user_type_text',width:70},
					{header:'智能卡号',dataIndex:'device_code',width:130,renderer:App.qtipValue},
					{header:'状态',dataIndex:'status_text',width:55,renderer:Ext.util.Format.statusShow},
					{header:'金额',dataIndex:'real_pay',width:55,renderer:Ext.util.Format.formatFee},				
					{header:'缴费前预计到期日',dataIndex:'begin_date',width:85,renderer:Ext.util.Format.dateFormat},
					{header:'缴费后预计到期日',dataIndex:'prod_invalid_date',width:85,renderer:Ext.util.Format.dateFormat},
					{header:'付款方式',dataIndex:'pay_type_text',width:60},
					{header:'受理日期',dataIndex:'create_time',width:120},
					{header:'账务日期',dataIndex:'acct_date',width:70,renderer:Ext.util.Format.dateFormat},
					{header:'受理人',dataIndex:'optr_name',width:60},
					{header:'受理部门',dataIndex:'dept_name',width:80},
					{header:'发票',dataIndex:'invoice_id',width:70}
		        ]
		});
		
		this.busiCm = new Ext.ux.grid.LockingColumnModel({
				columns:[
					sm,
	    			{header:'流水号',dataIndex:'create_done_code',width:80},
					{header:'费用名称',dataIndex:'fee_text',width:110},
					{header:'设备类型',dataIndex:'device_type_text',width:55},
					{header:'设备编号',dataIndex:'device_code',width:140},
					{header:'状态',dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
					{header:'应付',dataIndex:'should_pay',width:60,renderer:Ext.util.Format.formatFee},
					{header:'实付',dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},
					{header:'付款方式',dataIndex:'pay_type_text',width:60},
					{header:'受理日期',dataIndex:'create_time',width:125},
					{header:'账务日期',dataIndex:'acct_date',width:75,renderer:Ext.util.Format.dateFormat},
					{header:'受理人',dataIndex:'optr_name',width:60},
					{header:'受理部门',dataIndex:'dept_name',width:80},
					{header:'发票',dataIndex:'invoice_id',width:90}
		        ]
		});
		
		BatchAcctGrid.superclass.constructor.call(this, {
			title : '营业费信息',
			width:'100%',
			height:280,
			autoScroll:true,
			store : this.acctFeeStore,
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView(),
			sm : sm,
			cm:this.acctCm
		});
	}
});



BatchBusiGrid = Ext.extend(Ext.grid.GridPanel, {
	border : false,
	currFeeType: 'BUSI',//当前费用类型
	busiFeeStore: null,//业务费store
	busiCm: null,//业务费column
	parent:null,
	constructor : function(p) {
		this.parent = p;
		this.busiFeeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + '/commons/x/QueryCust!queryBatchBusiPayFee.action',
			fields : ['create_done_code','fee_text', 'device_type', 'device_type_text',
					'cust_id','dept_id','county_id','area_id',
					'device_code', {name : 'should_pay',type : 'int'},
					'pay_type_text', 'pay_type', 'status_text','dept_name','dept_id',
					'status', 'invoice_code', 'invoice_id','invoice_mode', 'optr_id','optr_name',
					{name : 'real_pay',type : 'int'},'fee_sn','create_time','acct_date','deposit',
					'data_right','finance_status','invoice_book_id','is_busi_fee'
					]
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		this.busiCm = new Ext.ux.grid.LockingColumnModel({
				columns:[
					sm,
	    			{header:'流水号',dataIndex:'create_done_code',width:80},
					{header:'费用名称',dataIndex:'fee_text',width:110},
					{header:'设备类型',dataIndex:'device_type_text',width:55},
					{header:'设备编号',dataIndex:'device_code',width:140},
					{header:'状态',dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
					{header:'应付',dataIndex:'should_pay',width:60,renderer:Ext.util.Format.formatFee},
					{header:'实付',dataIndex:'real_pay',width:60,renderer:Ext.util.Format.formatFee},
					{header:'付款方式',dataIndex:'pay_type_text',width:60},
					{header:'受理日期',dataIndex:'create_time',width:125},
					{header:'账务日期',dataIndex:'acct_date',width:75,renderer:Ext.util.Format.dateFormat},
					{header:'受理人',dataIndex:'optr_name',width:60},
					{header:'受理部门',dataIndex:'dept_name',width:80},
					{header:'发票',dataIndex:'invoice_id',width:90}
		        ]
		});
		
		BatchBusiGrid.superclass.constructor.call(this, {
			title : '杂费信息',
			width:'100%',
			height:280,
			autoScroll:true,
			store : this.busiFeeStore,
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView(),
			sm : sm,
			cm:this.busiCm
		});
	}
});


BatchEditAcctDateWin = Ext.extend(Ext.Window,{
	form:null,
	grid:null,
	busi:null,
	gridAnchor:'100% 38%',
	busiAnchor:'100% 30%',
	constructor:function(){
		this.form = new QueryFeeForm(this);
		this.grid = new BatchAcctGrid(this);
		this.busi = new BatchBusiGrid(this);
		BatchEditAcctDateWin.superclass.constructor.call(this,{
			id:'batchEditAcctDateId',
			title : '批量修改账务日期',
			closeAction:'close',
			height:550,
			width:800,
			layout:'border',
			items:[
				{region:'north',border:false,height:100,layout:'fit',items:[this.form]},
				{region:'center',border:false,layout:'border',items:[{region:'center',border:false,layout:'fit',items:[this.grid]},
					{region:'south',border:false,height:150,id:'BusiId',layout:'fit',items:[this.busi]}]},
				{region:'south',border:false,height:40,layout:'form',labelWidth:100,labelAlign:'right',
					bodyStyle:'padding-top:10px',items:[
					{fieldLabel:'账务日期',xtype:'datefield',format:'Y-m-d',
						editable:false,
						id : 'acctDateId',
						allowBlank:false,
						name: 'acct_date'
					}
				]}
			],
			
//			layout:'anchor',
//			items:[
//				{anchor:'100% 22%',border:false,layout:'fit',items:[this.form]},
//				{anchor:this.gridAnchor,border:false,layout:'fit',id:'GridId',items:[this.grid]},
//				{anchor:this.busiAnchor,border:false,layout:'fit',id:'BusiId',items:[this.busi]},
//				{anchor:'100% 10%',border:false,layout:'form',labelWidth:100,labelAlign:'right',
//					bodyStyle:'padding-top:10px',items:[
//					{fieldLabel:'账务日期',xtype:'datefield',format:'Y-m-d',
//						editable:false,
//						id : 'acctDateId',
//						allowBlank:false,
//						name: 'acct_date'
//					}
//				]}
//			],
			buttons:[
				{text:'保  存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关  闭',iconCls:'icon-exit',scope:this,handler:function(){this.close();}}
			]
		});
	},
	doSave:function(){
		var records = this.grid.getSelectionModel().getSelections();
		var busiRecords = this.busi.getSelectionModel().getSelections();
		if(records.length>0||busiRecords.length>0){
			var acctDate = Ext.getCmp('acctDateId').getValue().format('Y-m-d');
			var arr = [];
			Ext.each(records,function(record){
				//原账务日期不能和新日期相同
				if(acctDate != Ext.util.Format.dateFormat(record.get('acct_date')))
					arr.push({
						'cust_id':record.get('cust_id'),
						'fee_sn':record.get('fee_sn'),
						'acct_date':record.get('acct_date'),
						'dept_id':record.get('dept_id'),
						'county_id':record.get('county_id'),
						'area_id':record.get('area_id')
					});
			});
			Ext.each(busiRecords,function(record){
				//原账务日期不能和新日期相同
				if(acctDate != Ext.util.Format.dateFormat(record.get('acct_date')))
					arr.push({
						'cust_id':record.get('cust_id'),
						'fee_sn':record.get('fee_sn'),
						'acct_date':record.get('acct_date'),
						'dept_id':record.get('dept_id'),
						'county_id':record.get('county_id'),
						'area_id':record.get('area_id')
					});
			});
			if(arr.length==0){
				Alert('不能与原账务日期相同！');
				return;
			}
			var obj = {};
			obj['feeList'] = Ext.encode(arr);
			obj['acctDate'] = Ext.getCmp('acctDateId').getValue().format('Y-m-d');
			var msg = Show();
			Ext.Ajax.request({
				url:root+'/core/x/Pay!batchEditAcctDate.action',
				params:obj,
				scope:this,
				success:function(res,opt){
					msg.hide();
					msg = null;
					var res = Ext.decode(res.responseText);
					if(res.success == true){
						Alert('修改账务日期成功！');
//						this.form.getForm().reset();
						this.form.getForm().findField('optrId').getStore().removeAll();
						this.grid.getStore().removeAll();
						this.busi.getStore().removeAll();
						Ext.getCmp('acctDateId').setValue(null);
					}
				}
			});
		}else{
			Alert('请选择要修改的记录！');
		}
	}
});

