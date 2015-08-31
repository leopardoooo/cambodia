/**
 * 退款
 * @class PublicAcctRefundPanel
 * @extends BaseForm
 */

AcctItemTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=20%>账目名称：</td>',
			'<td class="input_bold" width=30% colspan=3>&nbsp;{[values.acctitem_name ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>余额：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.active_balance) ]}</td>',
			'<td class="label" width=20%>欠费：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.owe_fee)]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>本月费用：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.real_bill)]}</td>',
			'<td class="label" width=20%>实时费用：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.real_fee)]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>预约：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.order_balance)]}</td>',
			'<td class="label" width=20%>实时余额：</td>',
			'<td class="input" width=30% colspan=3>&nbsp;{[Ext.util.Format.formatFee(values.real_balance)]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>可转余额：</td>',
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.can_trans_balance)]}</td>',
			'<td class="label" width=20%>可退余额：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.can_refund_balance)]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>冻结余额：</td>',
			'<td class="input" width=30% colspan=3>&nbsp;{[Ext.util.Format.formatFee(values.inactive_balance)]}</td>',
		'</tr>',
	'</table>'
);

//fm.dateFormat(

PublicAcctRefundPanel = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/ProdOrder!savePublicRefund.action",
	constructor:function(){
		PublicAcctRefundPanel.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 80,
			layout:'border',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			items:[{
					region:'north',
					bodyStyle: Constant.TAB_STYLE,
					height:180,
					title:'账目信息',items:[{}]
				},
				{xtype : 'panel',
	            	title : '退款业务处理',
	            	bodyStyle: Constant.TAB_STYLE,//baseCls: 'x-plain',
					layout:'form',region:'center',items:[
					{xtype:'hidden',id:'acctId'},
					{xtype:'hidden',id:'acctItemId'},
					{xtype:'hidden',id:'realBalanceId'},
					{xtype:'hidden',id:'canRefundBalance'},
					{xtype:'numberfield',fieldLabel:'退款金额',allowBlank:false,allowNegative:false,
						name:'fee',id:'feeId',minValue:1}]}
			]
		});
	},
	initEvents:function(){
		this.on('afterrender',function(){
		var acctStore = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getStore();
		var acctItemRecord = acctStore.getAt(0);
			this.items.itemAt(0).items.itemAt(0).getEl().dom.innerHTML = 
				AcctItemTemplate.applyTemplate(acctItemRecord.data);
		},this,{delay:10});
	},
	doInit:function(){
		var userId = App.getApp().main.infoPanel.acctPanel.acctItemGrid.userId;
		var acctStore = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getStore();
		var acctItemRecord = acctStore.getAt(0);
		
		Ext.getCmp('acctId').setValue(acctItemRecord.get('acct_id'));
		Ext.getCmp('acctItemId').setValue(acctItemRecord.get('acctitem_id'));
		Ext.getCmp('realBalanceId').setValue(parseFloat(Ext.util.Format.formatFee(acctItemRecord.get('real_balance'))));
		Ext.getCmp('canRefundBalance').setValue(parseFloat(Ext.util.Format.formatFee(acctItemRecord.get('can_refund_balance'))))
		Ext.getCmp('feeId').setValue(Ext.util.Format.formatFee(acctItemRecord.get('can_refund_balance')));
		PublicAcctRefundPanel.superclass.doInit.call(this);
		
	},
	doValid:function(){
		var canRefundBalance = Ext.getCmp('canRefundBalance').getValue();
		var fee = Ext.getCmp('feeId').getValue();
		var obj={};
		if(fee > canRefundBalance){
			obj["isValid"] = false;
			obj["msg"] = '退款金额应小于账目可退余额';
			return obj;
		}
		return PublicAcctRefundPanel.superclass.doValid.call(this);
	},
	success:function(){
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getValues:function(){
		var value={};
		value['acctId'] = Ext.getCmp('acctId').getValue();
		value['acctItemId'] = Ext.getCmp('acctItemId').getValue();
		value['fee'] = Ext.util.Format.formatToFen(Ext.getCmp('feeId').getValue());
		return value;
	},
	getFee:function(){
		return -parseFloat(Ext.getCmp('feeId').getValue());
	}
});

Ext.onReady(function(){
	var panel = new PublicAcctRefundPanel();
	TemplateFactory.gTemplate(panel);
})