DEZSRefundForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Acct!dezsRefund.action",
	record : null,
	constructor : function(){
		this.record = App.getApp().main.infoPanel.acctPanel.acctItemDetailTab.acctItemActiveGrid.getSelectionModel().getSelected();
		DEZSRefundForm.superclass.constructor.call(this,{
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
				value: this.record.get("acct_id"),
				name:'acctId'
			},{
				xtype:'hidden',
				value: this.record.get("fee_type"),
				name:'feeType'
			},{
				xtype:'hidden',
				value: this.record.get("acctitem_id"),
				name:'acctItemId'
			},{
				fieldLabel:'可退金额',
				xtype:'numberfield',
				style:Constant.TEXTFIELD_STYLE,
				readOnly : true,
				value: Ext.util.Format.convertToYuan(this.record.get("balance"))
			},{
				fieldLabel:'退款金额',
				xtype:'numberfield',
				name:'fee',
				allowBlank : false,
				allowNegative : false,
				maxValue : Ext.util.Format.convertToYuan(this.record.get("balance"))
			}]
		})
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['fee'] = Ext.util.Format.formatToFen(all['fee']) ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})

Ext.onReady(function(){
	var aaf = new DEZSRefundForm();
	TemplateFactory.gTemplate(aaf);
});