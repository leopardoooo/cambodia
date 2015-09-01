/**
 * 退款
 * @class AcctReimbursePanel
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

AcctReimbursePanel = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Acct!saveRefund.action",
//	tpl : null,
	promStore : null,
	constructor:function(){
		
		this.promStore = new Ext.data.JsonStore({
 				url : Constant.ROOT_PATH + "/commons/x/QueryUser!querySelectPromFee.action",
				fields : ['prom_fee_id','prom_fee_name','prom_fee_sn','prod_sn']
 		});
		
		AcctReimbursePanel.superclass.constructor.call(this,{
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
					{xtype:'hidden',id:'userId'},
					{xtype:'hidden',id:'acctId'},
					{xtype:'hidden',id:'acctItemId'},
					{xtype:'hidden',id:'prodSn'},
					{xtype:'hidden',id:'invalidDate'},
					{xtype:'hidden',id:'realBalanceId'},
					{xtype:'hidden',id:'canRefundBalance'},
					{xtype:'numberfield',fieldLabel:'退款金额',allowBlank:false,allowNegative:false,
						name:'fee',id:'feeId',minValue:1}]}
			]
		});
	},
	initEvents:function(){
		this.on('afterrender',function(){
			this.items.itemAt(0).items.itemAt(0).getEl().dom.innerHTML = 
				AcctItemTemplate.applyTemplate(App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected().data);
			var acctItemRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
			Ext.Ajax.request({
				scope : this,
				url: Constant.ROOT_PATH + "/core/x/Acct!queryIsPromFee.action",
				params : {
					userId : acctItemRecord.get("user_id"),
					prodId : acctItemRecord.get("prod_id")
				},
				success : function(res,opt){
					var rec = Ext.decode(res.responseText);
					if(rec.length > 0){
						Alert("如果当前业务是退订"+rec[0].prom_fee_name+"，请在业务结束后联系管理员作废套餐记录!");
					}
				}
			});
		},this,{delay:10});
	},
	doInit:function(){
		var acctItemRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
		var userId = App.getApp().main.infoPanel.custPanel.acctItemGrid.userId;
		
		Ext.getCmp('userId').setValue(userId);
		Ext.getCmp('acctId').setValue(acctItemRecord.get('acct_id'));
		Ext.getCmp('acctItemId').setValue(acctItemRecord.get('acctitem_id'));
		Ext.getCmp('prodSn').setValue(acctItemRecord.get('prod_sn'));
		Ext.getCmp('realBalanceId').setValue(parseFloat(Ext.util.Format.formatFee(acctItemRecord.get('real_balance'))));
		Ext.getCmp('canRefundBalance').setValue(parseFloat(Ext.util.Format.formatFee(acctItemRecord.get('can_refund_balance'))))
		Ext.getCmp('invalidDate').setValue(acctItemRecord.get('invalid_date'));
		Ext.getCmp('feeId').setValue(Ext.util.Format.formatFee(acctItemRecord.get('can_refund_balance')));
		AcctReimbursePanel.superclass.doInit.call(this);
		
		this.promStore.baseParams = {userId:userId,prodSn: acctItemRecord.get('prod_sn')};
        this.promStore.load();
		this.promStore.on("load",this.loadData,this);
	},
	loadData : function(s){
		if(this.promStore.getCount()>0){
			this.items.itemAt(1).add({
					xtype : 'combo',
					width : 150,
					id : 'promFeeSnId',
					editable : false,
					hiddenName:'prom_fee_sn',
					fieldLabel : '所属套餐',
					store : this.promStore,
					triggerAction : 'all',
					mode: 'local',
					displayField : 'prom_fee_name',
					valueField : 'prom_fee_sn',
					emptyText: '请选择'
				})
			this.promStore.insert(0 ,new Ext.data.Record({
				prom_fee_name: '请选择...', prom_fee_id: ''
			}));
		}
		this.doLayout();
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
		return AcctReimbursePanel.superclass.doValid.call(this);
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getValues:function(){
		var value={};
		value['userId'] = Ext.getCmp('userId').getValue();
		value['acctId'] = Ext.getCmp('acctId').getValue();
		value['acctItemId'] = Ext.getCmp('acctItemId').getValue();
		value['prodSn'] = Ext.getCmp('prodSn').getValue();
		value['fee'] = Ext.util.Format.formatToFen(Ext.getCmp('feeId').getValue());
		value['invaliddate'] = Ext.getCmp('invalidDate').getValue();
		if(Ext.getCmp('promFeeSnId')){
			value['promFeeSn'] =  Ext.getCmp('promFeeSnId').getValue();
		}
		return value;
	},
	getFee:function(){
		return -parseFloat(Ext.getCmp('feeId').getValue());
	}
});

Ext.onReady(function(){
	var panel = new AcctReimbursePanel();
	TemplateFactory.gTemplate(panel);
})