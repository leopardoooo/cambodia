Ext.ns('Ext.invoice');

CommonInvoiceForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	queryBtnId: null,
	constructor:function(p){
		this.parent = p;
		this.queryBtnId = "commonInvoiceBtnId_" + Ext.id();
		CommonInvoiceForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding:10px',
			layout:'column',
			labelWidth:120,
			items:[/*{columnWidth:1,layout:'form',border:false,items:[
				        {fieldLabel:lsys('InvoiceCommon.invoice_code'),allowBlank:false,xtype:'textfield',name:'invoiceCode',value:'AAA',readOnly:true}
				]},*/
				{xtype: 'hidden', name: 'invoiceCode', value: 'AAA'},
				{columnWidth:.5,layout:'form',border:false,items:[
					{
					fieldLabel:lsys('InvoiceCommon.invoice_id'),
				    xtype:'compositefield',combineErrors:false,
				    labelWidth:150,
				    items: [
				        {xtype:'textfield',name:'startInvoiceId',vtype:'invoiceId'},
				        {xtype:'displayfield',value:lsys('common.to')},
				        {xtype:'textfield',name:'endInvoiceId',vtype:'invoiceId'}
				    	]
					}
				]},
				{columnWidth:.3,layout:'form',border:false,items:[
					{id:this.queryBtnId,text:lsys('common.query'),xtype:'button',iconCls:'icon-query',
						listeners:{
							scope:this,
							click:this.doQuery
						}
					}
				]}

			]
		});
	},
	doValid:function(){return this.getForm().isValid();},//formpanel验证函数(默认basicform)
	doQuery:function(){//查询按钮点击函数
		this.parent.grid.getStore().removeAll();
		if(this.doValid()){//验证通过才查询
			var values = this.getForm().getValues();
			
			var isEmpty = true;//控制至少输入一个条件
			for(var key in values){
				if(key != 'transDepotId' && key != 'status' && !Ext.isEmpty(values[key])){
					isEmpty = false;
					break;
				}
			}
			if(isEmpty){
				Alert(lsys('msgBox.needInputAtLeastOneInvoice'));
				return;
			}
			
			Ext.apply(values,{optrType:this.parent.optrType});
			
			Ext.getCmp(this.queryBtnId).disable();
			var msg = Show();
			Ext.Ajax.request({
				url:'resource/Invoice!queryInoivce.action',
				params:values,
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:function(res,opt){
					msg.hide();
					Ext.getCmp(this.queryBtnId).enable();
					var data = Ext.decode(res.responseText);
					if(Ext.isEmpty(data)){
						Alert(lsys('msgBox.invoiceNotFoundTryReSearch'));
						this.parent.grid.setTitle(lsys('InvoiceCommon.titleInvoiceInfo'));
					}else{
						this.parent.grid.doReset();
						this.parent.grid.setTitle(lsys('InvoiceCommon.titleInvoiceCount',null,[data.length]));
						this.doSuccess(data);
					}
				},
				failure:function(){
					msg.hide();
					Alert(lsys('msgBox.linkFailure'));
				}
			});
		}
	},
	doSuccess:function(data){//查询成功后调用函数
		this.parent.grid.getStore().loadData(data);
	}
});

CommonInvoiceGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	amount:0,
	rowCount:0,
	parent:null,
	constructor:function(parent){
		this.parent = parent;
		this.store = new Ext.data.JsonStore({
			fields:['invoice_id','invoice_code','invoice_type','status','status_text','amount',
				'invoice_mode','invoice_mode_text','finance_status','finance_status_text',
				'invoice_book_id','invoice_type_text','depot_id','depot_name','optr_id','optr_name',
				'is_loss','is_loss_text','invoice_amount']
		});
		this.store.on("load",this.doLoadData,this);
		var sm = new Ext.grid.CheckboxSelectionModel({
			checkOnly:true,
			onBeforeSelAllHanlder: function(){
				this.grid.doReset();
			},
			listeners:{
				scope:this,
				rowselect:this.doRowSelect,
				rowdeselect:this.doRowdeSelect
			}
		});
		commonInvoiceGridThat = this;
		
		var columns = [
				sm,
				{header:lsys('InvoiceCommon.commonGridColls')[0],dataIndex:'invoice_id',align:'center',width:90},
				{header:lsys('InvoiceCommon.commonGridColls')[1],dataIndex:'invoice_code',align:'center',width:90},
				{header:lsys('InvoiceCommon.commonGridColls')[2],dataIndex:'invoice_type_text',align:'center',width:80},
				{header:lsys('InvoiceCommon.commonGridColls')[3],dataIndex:'status_text',align:'center',width:80},
				{header:lsys('InvoiceCommon.commonGridColls')[4],dataIndex:'amount',align:'center',width:70,renderer:Ext.util.Format.formatFee},
				{header:lsys('InvoiceCommon.commonGridColls')[5],dataIndex:'invoice_mode_text',align:'center',width:70},
				{header:lsys('InvoiceCommon.commonGridColls')[6],dataIndex:'finance_status_text',align:'center',width:60},
				{header:lsys('InvoiceCommon.commonGridColls')[7],dataIndex:'depot_name',align:'center',width:80},
				{header:lsys('InvoiceCommon.commonGridColls')[8],dataIndex:'optr_name',align:'center',width:80}
			];
		
		CommonInvoiceGrid.superclass.constructor.call(this,{
			title:lsys('InvoiceCommon.titleInvoiceInfo'),
			border:false,
			ds:this.store,
			cm:new Ext.grid.ColumnModel({
				defaults:{sortable:false},
				columns:columns
			}),
			sm:sm,
			listeners:{
				scope: this,
				rowclick: this.doQueryDetail
			}
		});
	},
	doQueryDetail:function(grid, rowIndex){
		var record = grid.getStore().getAt(rowIndex);
		if(record && (this.parent.optrType == 'CHECK' || this.parent.optrType == 'CANCELCHECK') )
			this.parent.doQueryDetail(record.data);
	},
	doLoadData:function(){//store load加载完事件
		this.getSelectionModel().selectAll();
	},
	//选中行触发事件
	doRowSelect:function(cm,rowIndex,record){
		if(record.get('status') == 'USE'){
			this.setTotalValue(1,record.get('amount'));
		}
	},
	//取消选中行触发事件
	doRowdeSelect:function(cm,rowIndex,record){
		if(record.get('status') == 'USE'){
			var amount = record.get('amount')==0?0:-record.get('amount');
			this.setTotalValue(-1,amount);
		}
	},
	setTotalValue:function(rowCount,amount){
		this.rowCount += rowCount;
		if(this.rowCount == 0 ){
			this.amount = 0;
		}else{
			this.amount = Ext.util.Format.formatFee(
							this.amount * 100 + 
							Ext.util.Format.formatFee(amount) * 100);
		}
		this.setTitle(lsys('InvoiceCommon.titleInvoiceCountAndAmount',null,[this.rowCount,this.amount]));
	},
	doReset:function(){//重置数据
		this.rowCount = 0;
		this.amount = 0;
		this.setTitle(lsys('InvoiceCommon.titleInvoiceInfo'));
	},
	getSelections:function(){
		return this.getSelectionModel().getSelections();
	},
	//选中的记录
	getSelectedValues:function(){
		var records = this.getSelections();
		if(records.length > 0){
			var arr = [];
			Ext.each(records,function(record){
				var o = {};
				o['invoice_id'] = record.get('invoice_id');
				o['invoice_code'] = record.get('invoice_code');
				o['invoice_book_id'] = record.get('invoice_book_id');
				o['optr_id'] = record.get('optr_id');
				arr.push(o);
			});
			var obj = {};
			obj['invoiceList'] = Ext.encode(arr);
			return obj;
		}
		return null;
	},
	getNumValues:function(){
		var records = this.getSelections();
		var amount=0;
		var str = "";
		if(records.length > 0){
			Ext.each(records,function(record){
				if(record.get('status') != "INVALID"){
					amount = amount+record.get('amount');
				}
			});		
//			str ="一共:"+records.length+"张发票，总额:"+Ext.util.Format.formatFee(amount)+"元，";
			str = lsys('InvoiceCommon.tipInvoiceCountAndAmount',null,[records.length,Ext.util.Format.formatFee(amount)]);
		}
		return str;
	}
});


InvoiceBaseDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	invoiceDetailStore : null,
	constructor : function() {
		this.invoiceDetailStore = new Ext.data.JsonStore({
					fields : ['cust_name', 'cust_no', 'busi_name','optr_id','optr_name',
							'fee_name', 'real_pay', 'create_time', 'status', 'status_text']
				});
		InvoiceBaseDetailGrid.superclass.constructor.call(this, {
				ds : this.invoiceDetailStore,
				viewConfig : {
					forceFit :true
				},
				title:'发票明细',
				border:false,
				sm : new Ext.grid.CheckboxSelectionModel(),
				cm : new Ext.grid.ColumnModel([{
							header : '客户编号',
							dataIndex : 'cust_no',
							renderer : App.qtipValue
						},{
							header : '客户名称',
							dataIndex : 'cust_name',
							renderer : App.qtipValue
						},{
							header : '费用项',
							dataIndex : 'fee_name',
							renderer : App.qtipValue
						}, {
							header : '金额',
							dataIndex : 'real_pay',
							renderer : Ext.util.Format.formatFee
						}])
			})
	}
})


CommonInvoicePanel = Ext.extend(Ext.Panel,{
	form:null,
	grid:null,
	optrType:null,//保存对应的类型 发票(调拨,核销,结账)
	valueArr:null,
	detail:null,
	constructor:function(type){
		this.optrType = type;
		this.form = new CommonInvoiceForm(this);
		this.grid = new CommonInvoiceGrid(this);
		var items= [];
		if(this.optrType == 'CHECK' || this.optrType == 'CANCELCHECK'){
			this.detail = new InvoiceBaseDetailGrid(this);
			items = [{region : 'center',layout : 'fit',items : [this.grid]},
				{region : 'east',width : '30%',split : true,layout : 'fit',items : [this.detail]}];
		}else{
			items = [{region : 'center',layout : 'fit',items : [this.grid]}];
		}
		
		CommonInvoicePanel.superclass.constructor.call(this,{
			border:false,
			region:'center',
			layout:'anchor',
			items:[
				{anchor:'100% 20%',border:false,layout:'fit',items:[this.form]},
				{anchor:'100% 80%',border:false,layout:'border',autoScroll:true,items:items}
			],
			buttonAlign:'center',
			buttons:[{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave}]
		});
	},
	doQueryDetail:function(rs){
		var panelId = Ext.getCmp('sysMainTabPanelId').getActiveTab().getId();
		if(panelId == 'CheckInvoice' || panelId == 'CancelCheck'){
			deatilThat = this;
			Ext.Ajax.request({
				url : 'resource/Invoice!queryInvoiceDetailByInvoiceId.action',
				params : {
					invoiceId : rs['invoice_id'],
					invoiceCode : rs['invoice_code']
				},
				scope : deatilThat,
				success : function(res, opt) {
					var data = Ext.decode(res.responseText);
					if (data) {
						var invoiceDetailGrid = Ext.getCmp(panelId).panel.detail;
						var detail = data.invoiceDetailList;
						invoiceDetailGrid.getStore().removeAll();
						if(detail && detail.length > 0){
							 var detaildata = [];
							 for(var i=0;i<detail.length;i++){
								 var obj = {};
								 obj['cust_name'] = detail[i].cust_name;
								 obj['cust_no'] = detail[i].cust_no;
								 obj['busi_name'] = detail[i].busi_name;
								 obj['fee_name'] = detail[i].fee_name;
								 obj['real_pay'] = detail[i].real_pay;
								 obj['create_time'] = detail[i].create_time;
								 obj['status'] = detail[i].status;
								 obj['status_text'] = detail[i].status_text;
								 obj['optr_id'] = detail[i].optr_id;
								 obj['optr_name'] = detail[i].optr_name;
								 detaildata.push(obj);
							 }
							 invoiceDetailGrid.getStore().loadData(detaildata);
						 }

						
					}
				}
			});
		}
	},
	doSave:function(){
		var values = this.grid.getSelectedValues();
		if(values){
			if(this.optrType == 'TRANS' || this.optrType == 'CANCEL_RECEIVE' || this.optrType == 'QUOTA_TRANS' || this.optrType=='RECEIVE'){//发票调拨或定额发票下发,回收时,调拨的仓库对象
				values['transDepotId'] = this.form.getForm().findField('transDepotId').getValue();
				if(this.optrType == 'QUOTA_TRANS' || this.optrType=='RECEIVE' || this.optrType=='CANCEL_RECEIVE'){
					values['optrId'] = this.form.getForm().findField('optrId').getValue();
				}
				if(Ext.isEmpty(values['transDepotId'])){
					Alert(lsys('msgBox.selectTransDepotId'));
					return;
				}
			}else if(this.optrType == 'EDITSTATUS'){
				values['status'] = this.form.getForm().findField('status').getValue();
				values['invoiceType'] = this.form.getForm().findField('invoiceType').getValue();
				if(Ext.isEmpty(values['status'])){
					Alert(lsys('msgBox.selectStatus2BeModify'));
					return;
				}
			}else if(this.optrType == 'QUOTA_ADJUST'){
				values['amount'] = -this.form.getForm().findField('amount').getValue()*100;
			}
			values['optrType'] = this.optrType;
			this.valueArr = values;
			var strArr = this.grid.getNumValues();
			if(this.optrType == 'CHECK' || this.optrType == 'CLOSE' || this.optrType == 'CANCELCHECK' || this.optrType == 'CANCELCLOSE'){
				Confirm(lsys('msgBox.confirmSaveBusiWithInvoice',null,[strArr]), this , this.doToSave );
			}else{
				this.doToSave();
			}
					
		}else{
			Alert(lsys('msgBox.selectARecord'));
		}
	},
	doToSave:function(){
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Invoice!saveInoivceOptr.action',
			params:this.valueArr,
			scope:this,
			timeout:999999,
			success:function(res,opt){
				msg.hide();
				var data = Ext.decode(res.responseText);
				if(data['success'] === true)
					Alert(lsys('common.msg.actionSuccess'),function(){
						this.form.getForm().reset();
						this.grid.getStore().removeAll();
						this.grid.setTitle(lsys('InvoiceCommon.titleInvoiceInfo'));
					},this);
			}
		});
	}
});





