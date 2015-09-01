/**
*作废赠送
*/
CancelFreeFrom = Ext.extend(BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Acct!cancelFree.action",
	acctRecord: null,
	constructor: function(){
		this.acctRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
		CancelFreeFrom.superclass.constructor.call(this,{
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
				fieldLabel:'赠送总额',
				xtype:'textfield',
				name:'free_fee',
				style:Constant.TEXTFIELD_STYLE,
				value: Ext.util.Format.formatFee(this.acctRecord.get("can_cancel_balance"))
			},{
				fieldLabel:'作废金额',
				xtype:'numberfield',
				name:'fee',
				allowBlank : false,
				allowNegative : false,
				maxValue: Ext.util.Format.formatFee(this.acctRecord.get("can_cancel_balance"))
			}]
		})
	},
	doInit: function(){
		CancelFreeFrom.superclass.doInit.call(this);

	},doValid:function(){
		var fee = this.getForm().findField('fee').getValue();
		var balance = Ext.util.Format.formatFee(this.acctRecord.get("can_cancel_balance"));
		var obj={};
		if(fee > balance){
			obj["isValid"] = false;
			obj["msg"] = '作废金额应小于等于赠送总额!';
			return obj;
		}else if(fee == 0 || fee <0){
			obj["isValid"] = false;
			obj["msg"] = '作废金额不能为0和负数!';
			return obj;
		}
		return CancelFreeFrom.superclass.doValid.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['fee'] = Ext.util.Format.formatToFen(all['fee']) ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var aaf = new CancelFreeFrom();
	TemplateFactory.gTemplate(aaf);
});