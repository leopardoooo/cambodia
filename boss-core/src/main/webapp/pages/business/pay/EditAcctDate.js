EditAcctDateForm = Ext.extend(BaseForm,{
	record : null,
	url :Constant.ROOT_PATH + "/core/x/Pay!editAcctDate.action",
	constructor : function(){
		//取合同管理选中数据
		if(App.getApp().hirePurchaseGrid ){//修改分期付款明细的账目日期
			this.record = App.getApp().hirePurchaseGrid.getSelectionModel().getSelected();
			this.parent = App.getApp().hirePurchaseGrid;
			App.getApp().hirePurchaseGrid = null;//防止下次还存在
		}else if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.feeUnitpreGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.valuableCardGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}
		else{//费用面板选中数据
			this.record = App.getData().currentRec;
		}
		
		EditAcctDateForm.superclass.constructor.call(this,{
			layout : 'form',
			border : false,
			bodyStyle : Constant.TAB_STYLE,
			labelWidth : 90,
			items : [{
				fieldLabel: '原账务日期',
				xtype:'datefield',
				format:'Y-m-d',
				editable:false,
//				value : this.record.get('acct_date'),
				id : 'oldAcctDate',
				readOnly : true,
				name: 'old_acct_date'
			},{
				fieldLabel: '账务日期',
				xtype:'datefield',
				format:'Y-m-d',
				editable:false,
				id : 'acctDateCmp',
				allowBlank:false,
				name: 'acct_date'
			}]
		})
	},
	initComponent : function(){
		EditAcctDateForm.superclass.initComponent.call(this);
		//设置账务日期
		if(this.record.get('data_right') == 'ALL'){
			App.acctDate(Ext.getCmp('acctDateCmp'),function(){
				Ext.getCmp('acctDateCmp').setMinValue(Date.parseDate("2010-01-01",'Y-m-d'));
			});
		}else{
			App.acctDate(Ext.getCmp('acctDateCmp'));
		}
		
		
		Ext.getCmp('oldAcctDate').setValue(Date.parseDate(this.record.get('acct_date'),'Y-m-d h:i:s'));
		
	},
	doValid : function() {
		var obj = {};
		if (Ext.getCmp('oldAcctDate').getValue().format('Y-m-d')!=Ext.getCmp('acctDateCmp').getValue().format('Y-m-d')){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = "不能与原账务日期";
		}
		return obj;
	},
	getValues : function(){
		var all = {};
		all["feeSn"] = this.record.get('fee_sn');
		all["acctDate"] = Ext.getCmp('acctDateCmp').getValue().format('Y-m-d');
		all["oldAcctDate"] = Ext.getCmp('oldAcctDate').getValue().format('Y-m-d');
		if(!this.parent ){//避免单条修改合同款分期付费账务日期的时候把其他的也给修改
			all.contractId = this.record.data.contract_id;//合同款收费修改账目日期的时候将这个合同的contract_id作为remark传入
			all.leftAmount = this.record.data.left_amount;//剩余款一并传入
		}
		return all;
	},
	success:function(form,param){
		if(form.parent){
			form.parent.store.load();
		}else if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			App.getApp().main.feeUnitpreGrid.getStore().reload();
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			App.getApp().main.valuableCardGrid.getStore().reload();
		}else{
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		}
	}
})

Ext.onReady(function(){
	var form = new EditAcctDateForm();
	TemplateFactory.gTemplate(form);
})