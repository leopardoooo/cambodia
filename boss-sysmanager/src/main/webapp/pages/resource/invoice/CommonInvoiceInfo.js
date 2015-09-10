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
			labelWidth:90,
			items:[{columnWidth:1,layout:'form',border:false,items:[
				        {fieldLabel:'发票代码',allowBlank:false,xtype:'textfield',name:'invoiceCode',value:'AAA',readOnly:true}
				]},
				{columnWidth:.5,layout:'form',border:false,items:[
					{
					fieldLabel:'发票号码',
				    xtype:'compositefield',combineErrors:false,
				    labelWidth:120,
				    items: [
				        {xtype:'textfield',name:'startInvoiceId',vtype:'invoiceId'},
				        {xtype:'displayfield',value:'至'},
				        {xtype:'textfield',name:'endInvoiceId',vtype:'invoiceId'}
				    	]
					}
				]},
				{columnWidth:.3,layout:'form',border:false,items:[
					{id:this.queryBtnId,text:'查  询',xtype:'button',iconCls:'icon-query',
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
				Alert('请至少输入一个发票号码');
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
						Alert('未查询到发票信息，请重新输入发票号查询！');
						this.parent.grid.setTitle("发票信息");
					}else{
						this.doSuccess(data);
						this.parent.grid.setTitle("发票信息  数量："+data.length);
					}
				},
				failure:function(){
					msg.hide();
					Alert('连接异常!查询失败');
				}
			});
		}
	},
	doSuccess:function(data){//查询成功后调用函数

		this.parent.grid.doReset();
		this.parent.grid.getStore().loadData(data);
	}
});

CommonInvoiceGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	amount:0,
	rowCount:0,
	constructor:function(){
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
		var columns = new Ext.grid.ColumnModel({
			defaults:{sortable:false},
			columns:[
				sm,
				{header:'发票号码',dataIndex:'invoice_id',align:'center',width:90},
				{header:'发票代码',dataIndex:'invoice_code',align:'center',width:90},
				{header:'发票类型',dataIndex:'invoice_type_text',align:'center',width:80},
				{header:'使用状态',dataIndex:'status_text',align:'center',width:80},
				{header:'金额',dataIndex:'amount',align:'center',width:70,renderer:Ext.util.Format.formatFee},
//				{header:'定额票面额',dataIndex:'invoice_amount',align:'center',width:70,renderer:Ext.util.Format.formatFee},
				{header:'出票方式',dataIndex:'invoice_mode_text',align:'center',width:70},
				{header:'结存状态',dataIndex:'finance_status_text',align:'center',width:60},
				{header:'仓库',dataIndex:'depot_name',align:'center',width:80},
				{header:'领用人',dataIndex:'optr_name',align:'center',width:80}
			]
		});
		CommonInvoiceGrid.superclass.constructor.call(this,{
			title:'发票信息',
			border:false,
			ds:this.store,
			cm:columns,
			sm:sm
		});
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
		var str = "发票信息 共选中{0}行,金额总和为{1}";
		str = String.format(str,this.rowCount,this.amount);
		this.setTitle(str);
	},
	doReset:function(){//重置数据
		this.rowCount = 0;
		this.amount = 0;
		this.setTitle("发票信息");
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
			str ="一共:"+records.length+"张发票，总额:"+Ext.util.Format.formatFee(amount)+"元，";
		}
		return str;
	}
});

CommonInvoicePanel = Ext.extend(Ext.Panel,{
	form:null,
	grid:null,
	optrType:null,//保存对应的类型 发票(调拨,核销,结账)
	valueArr:null,
	constructor:function(type){
		this.optrType = type;
		this.form = new CommonInvoiceForm(this);
		this.grid = new CommonInvoiceGrid(this);
		CommonInvoicePanel.superclass.constructor.call(this,{
			border:false,
			region:'center',
			layout:'anchor',
			items:[
				{anchor:'100% 20%',border:false,layout:'fit',items:[this.form]},
				{anchor:'100% 80%',border:false,layout:'fit',autoScroll:true,items:[this.grid]}
			],
			buttonAlign:'center',
			buttons:[{text:'保  存',iconCls:'icon-save',scope:this,handler:this.doSave}]
		});
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
					Alert('请选择调拨仓库');
					return;
				}
			}else if(this.optrType == 'EDITSTATUS'){
				values['status'] = this.form.getForm().findField('status').getValue();
				values['invoiceType'] = this.form.getForm().findField('invoiceType').getValue();
				if(Ext.isEmpty(values['status'])){
					Alert('请选择要修改的状态');
					return;
				}
			}else if(this.optrType == 'QUOTA_ADJUST'){
				values['amount'] = -this.form.getForm().findField('amount').getValue()*100;
			}
			values['optrType'] = this.optrType;
			this.valueArr = values;
			var strArr = this.grid.getNumValues();
			if(this.optrType == 'CHECK' || this.optrType == 'CLOSE' || this.optrType == 'CANCELCHECK' || this.optrType == 'CANCELCLOSE'){
				Confirm(strArr+"确定要保存业务吗?", this , this.doToSave );
			}else{
				this.doToSave();
			}
					
		}else{
			Alert('请选择要操作的记录行！');
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
					Alert('保存成功',function(){
						this.form.getForm().reset();
						this.grid.getStore().removeAll();
						this.grid.setTitle("发票信息");
					},this);
			}
		});
	}
});





