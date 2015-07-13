/**
*调账
*/
AcctRefundFrom = Ext.extend(BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Acct!saveRefundInvlid.action",
	acctRecord: null,
	constructor: function(){
		this.acctRecord = App.getApp().main.infoPanel.billPanel.acctitemInvalidGrid.getSelectionModel().getSelected();
		AcctRefundFrom.superclass.constructor.call(this,{
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
				value: this.acctRecord.get("acct_id"),
				name:'acctId'
			},{
				xtype:'hidden',
				value: this.acctRecord.get("fee_type"),
				name:'fee_type'
			},{
				xtype:'hidden',
				value: this.acctRecord.get("acctitem_id"),
				name:'acctItemId'
			},{
				fieldLabel:'账目名称',
				xtype:'textfield',
				style:Constant.TEXTFIELD_STYLE,
				value: this.acctRecord.get("acctitem_name")
			},{
				fieldLabel:'退款金额',
				xtype:'numberfield',
				id : 'feeId',
				name:'fee',
				allowBlank : false,
				allowNegative : false,
				maxValue : Ext.util.Format.convertToYuan(this.acctRecord.get("invalid_fee")),
				value: Ext.util.Format.convertToYuan(this.acctRecord.get("invalid_fee"))
			}]
		})
	},
	initComponent : function(){
		AcctRefundFrom.superclass.initComponent.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['fee'] = Ext.util.Format.formatToFen(all['fee']) ;
		return all;
	},
	getFee : function(){
		return -Ext.getCmp('feeId').getValue();
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})
Ext.onReady(function(){
	var aaf = new AcctRefundFrom();
	TemplateFactory.gTemplate(aaf);
});