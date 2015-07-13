/**
 * 预收费
 */

var PayUnBusiFeeForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!payUnBusiFee.action",
	record : null,
	constructor:function(){
		this.record = App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected();
		
		PayUnBusiFeeForm.superclass.constructor.call(this,{
			labelWidth:95,
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults : {
				width : 160
			},
			items:[{
				xtype:'displayfield',
				fieldLabel:'合同号',
				name:'contract_no',
				allowBlank:false,
				value : this.record.get('contract_no')
			},{
				xtype:'displayfield',
				fieldLabel:'合同名称',
				name:'generalContract.contract_name',
				allowBlank:false,
				value : this.record.get('contract_name')
			},{
				xtype:'displayfield',
				fieldLabel:'客户名称',
				name:'generalContract.cust_name',
				allowBlank:false,
				value : this.record.get('cust_name')
			},{
				xtype:'displayfield',
				fieldLabel:'合同金额',
				value :Ext.util.Format.formatFee( this.record.get('nominal_amount'))
			},{
				xtype:'displayfield',
				fieldLabel:'已付金额',
				value :Ext.util.Format.formatFee( this.record.get('payed_amount'))
			},{
				id : 'fee',
				xtype:'numberfield',
				fieldLabel:'本次支付金额',
				name:'fee',
				allowNegative : false,
				allowDecimals : false,
				minValue : 1,
				maxValue : Ext.util.Format.formatFee( this.record.get('nominal_amount')-this.record.get('payed_amount'))
			}]
		});
	},
	doValid : function(){
		return PayUnBusiFeeForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var values = {};
		values['contractId'] = this.record.get('contract_id');
		values['fee'] = Ext.util.Format.formatToFen(Ext.getCmp('fee').getValue());
		
		return values;
	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().reload();
	}
});

Ext.onReady(function(){
	var panel = new PayUnBusiFeeForm();
	var box = TemplateFactory.gTemplate(panel);
});