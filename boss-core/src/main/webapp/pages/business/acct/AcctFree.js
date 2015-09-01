/**
*欠费抹零
*/
AcctFreeFrom = Ext.extend(BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Acct!acctFree.action",
	acctRecord: null,
	constructor: function(){
		this.acctRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
		AcctFreeFrom.superclass.constructor.call(this,{
			border: false,
			labelWidth: 200,
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
				value: this.acctRecord.get("prod_sn"),
				name:'prodSn'
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
				fieldLabel:'调账金额(元)',
				allowBlank : false,
				xtype:'numberfield',
				value:Ext.util.Format.convertToYuan(-this.acctRecord.get("owe_fee")),
				disabled : true,
				id:'free_fee',
				name:'adjust_fee'
			}]
		})
	},
	initComponent : function(){
		AcctFreeFrom.superclass.initComponent.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['adjust_fee'] = Ext.util.Format.formatToFen(Ext.getCmp('free_fee').getValue()) ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})
Ext.onReady(function(){
	var aaf = new AcctFreeFrom();
	TemplateFactory.gTemplate(aaf);
});