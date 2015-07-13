/**
*定额发票调账
*/
QuatoInvoiceFeeFrom = Ext.extend(BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Pay!editInvoiceFee.action",
	record: null,
	constructor: function(){
		this.record =  App.getData().currentRec;
		QuatoInvoiceFeeFrom.superclass.constructor.call(this,{
			border: false,
			labelWidth: 85,
			layout : 'form',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				xtype:'hidden',
				value: this.record.get("fee_sn"),
				name:'fee_sn'
			},{
				fieldLabel:'原发票金额',
				xtype:'displayfield',
				value : Ext.util.Format.formatFee(this.record.get('invoice_fee'))
			},{
				fieldLabel:'新发票金额',
				allowBlank : false,
				xtype:'numberfield',
				allowNegative : false,
				name:'newInvoiceFee'
			},{
				xtype : 'textarea',
				fieldLabel : '备注',
				height : 40,
				width : 125,
				name : 'remark'
			}]
		})
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['newInvoiceFee'] = Ext.util.Format.formatToFen(all['newInvoiceFee']) ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})
Ext.onReady(function(){
	var aaf = new QuatoInvoiceFeeFrom();
	TemplateFactory.gTemplate(aaf);
});