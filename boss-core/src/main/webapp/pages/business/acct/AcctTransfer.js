/**
 * 转账
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
			'<td class="input" width=30%>&nbsp;{[Ext.util.Format.formatFee(values.can_refund_balance)]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>冻结余额：</td>',
			'<td class="input" width=30% colspan=3>&nbsp;{[Ext.util.Format.formatFee(values.inactive_balance)]}</td>',
		'</tr>',
	'</table>'
);


NewAcctItemGrid = Ext.extend(AcctItemGrid,{
	singleSelect : true,
	constructor : function(){
		Ext.apply(this,{
			header:false,
			tbar:['-',
				{xtype:'tbtext',text:'卡号: '},
				{id:'tb_card_id',xtype:'textfield'},'-',
				{text:'筛选',scope:this,handler:this.doQuery},'-'
			]
		});
		NewAcctItemGrid.superclass.constructor.call(this);
	},
	initEvents : function(){
		this.on("rowclick", function(grid ,index, e){
			Confirm("确定要选择该账目吗?", this ,function(){
				var record = grid.getStore().getAt(index);
				
				Ext.getCmp('orderAcctId').setValue(record.get('acct_id'));
				Ext.getCmp('orderAcctItemId').setValue(record.get('acctitem_id'));
				Ext.getCmp('newAcctItemNameId').setValue(record.get('acctitem_name'));
				
				Ext.getCmp('acctItemSelectWin').hide();
			});
		}, this );
	},
	doQuery: function(){
		var cardId = Ext.getCmp('tb_card_id').getValue();
		this.getStore().clearFilter();
		if(!Ext.isEmpty(cardId)){
			this.getStore().filter('card_id',cardId);
		}
	}
});

AcctTransferPanel = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Acct!saveTrans.action",
	constructor:function(){
		
		var grid = new NewAcctItemGrid();
		grid.getColumnModel().setHidden(10,true);
		grid.getColumnModel().setHidden(9,true);
		grid.getColumnModel().setHidden(8,true);
		grid.getColumnModel().setHidden(7,true);
		grid.getColumnModel().setHidden(1,false);
		
		AcctTransferPanel.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 80,
			baseCls:'x-plain',
            layout:'border',
            bodyStyle:Constant.TAB_STYLE,
			items:[{xtype:'panel',region:'north',bodyStyle: Constant.TAB_STYLE,height:180,
					width:'100%',title:'账目信息',items:[{}]},
				{xtype : 'panel',
            	title : '转账业务处理',
            	bodyStyle: Constant.TAB_STYLE,//baseCls: 'x-plain',
				layout:'form',region:'center',items:[
				{xtype:'hidden',id:'sourceAcctId'},
				{xtype:'hidden',id:'orderAcctId'},
				
				{xtype:'hidden',id:'sourceAcctItemId'},
				{xtype:'hidden',id:'orderAcctItemId'},
				
				{xtype:'hidden',id:'transBalanceId'},
				
				{xtype:'textfield',fieldLabel:'新账目',id:'newAcctItemNameId',allowBlank:false,
					listeners : {
						scope : this,
						'focus' : function(){
							if(Ext.getCmp('acctItemSelectWin')){
								Ext.getCmp('acctItemSelectWin').show();
							}else{
								new Ext.Window({
									title : '账目信息',
									id : 'acctItemSelectWin',
									closeAction: 'hide',
									maximizable:false,
									width: 615,
									height: 350,
									layout: 'fit',
									border: false,
									items: grid 
								}).show();
							}
							
						}
					}},
				{xtype:'numberfield',fieldLabel:'转账金额',allowBlank:false,
					name:'fee',id:'feeId',allowNegative:false,minValue:0}
					]}
			]
		});
		var store = grid.getStore();
		var acctItemId = Ext.getCmp('sourceAcctItemId').getValue();
		var acctID = Ext.getCmp('sourceAcctId').getValue();
		//账户store
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		//选中的账目记录
		var acctitemRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
		
		var acctType = acctitemRecord.get('acct_type');//账目对应的账户类型
		var isBase = acctitemRecord.get('is_base');//是否是基础包
		var selectAcctId = acctitemRecord.get('acct_id');
		var selectAcctItemId = acctitemRecord.get('acctitem_id');
		var selectUserId = acctitemRecord.get('user_id');
		
		acctStore.each(function(record){
			var acctItems = record.get('acctitems');
			var currentUserId = record.get('user_id');
			Ext.each(acctItems,function(acctItem){
				acctItem['card_id'] = record.get('card_id');
			});
			//休眠隔离不能转账  ,待销户
			if(record.get('status') != 'DORMANCY' && record.get('status') != 'ATVCLOSE' && record.get('status') != 'WAITLOGOFF'){
				if(acctItems){
					if(acctType == 'PUBLIC'){
						store.loadData(acctItems,true);
					}else if(acctType == 'SPEC'){//专用账户 只能转到 不是基础包、不是零资费的专用账目
						var arr = [];
						Ext.each(acctItems,function(acctItem){
							acctItem['card_id'] = record.get('card_id');
							
							//潜江 平移基本包 可以转账到 基本节目包1 中
							if(acctItem['county_id'] == '9005' && selectAcctItemId == '2728'){
								if(acctItem['acctitem_id'] == '8720' && selectUserId == currentUserId)
									arr.push(acctItem);
							}else{
								if(acctItem['is_base']!='T' && acctItem['is_zero_tariff']!='T' && acctItem['acct_type']=='SPEC'){
									arr.push(acctItem);
									//放开宽带基本包之间可以转账
								}else if(acctID==acctItem['acct_id'] && acctItem['is_base']=='T' && acctItem['serv_id']=='BAND' && acctItem['is_zero_tariff']!='T' && acctItem['acct_type']=='SPEC'){
									arr.push(acctItem);
								}
							}
						},this);
						if(arr.length>0)store.loadData(arr,true);
					}
				}
			}
		});
		
		//去掉当前选中的记录
		store.each(function(record){
			if(record.get('acct_id') == selectAcctId && record.get('acctitem_id') == selectAcctItemId){
				store.remove(record);
				return false;
			}
		});
		
		grid.getColumnModel().setHidden(10,true);
		grid.getColumnModel().setHidden(9,true);
		grid.getColumnModel().setHidden(8,true);
		grid.getColumnModel().setHidden(7,true);
	},
	initEvents:function(){
		this.on('afterrender',function(){
			this.items.itemAt(0).items.itemAt(0).getEl().dom.innerHTML = 
				AcctItemTemplate.applyTemplate(App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected().data);
		},this,{delay:10});
	},
	doInit:function(){
		var acctItemRecord = App.getApp().main.infoPanel.custPanel.acctItemGrid.getSelectionModel().getSelected();
		
		Ext.getCmp('sourceAcctId').setValue(acctItemRecord.get('acct_id'));
		Ext.getCmp('sourceAcctItemId').setValue(acctItemRecord.get('acctitem_id'));
		Ext.getCmp('transBalanceId').setValue(Ext.util.Format.formatFee(acctItemRecord.get('can_trans_balance')));
		
		AcctTransferPanel.superclass.doInit.call(this);
	},
	doValid:function(){
		var realBalance = Ext.getCmp('transBalanceId').getValue();
		var fee = Ext.getCmp('feeId').getValue();
		var obj={};
		if(fee > realBalance){
			obj["isValid"] = false;
			obj["msg"] = '转账金额应小于账目实时余额';
			return obj;
		}else if(fee == 0){
			obj["isValid"] = false;
			obj["msg"] = '转账金额不能为0';
			return obj;
		}
		
		return AcctTransferPanel.superclass.doValid.call(this);
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getValues:function(){
		var value = {};
		value['sourceAcctId'] = Ext.getCmp('sourceAcctId').getValue();
		value['orderAcctId'] = Ext.getCmp('orderAcctId').getValue();
		value['sourceAcctItemId'] = Ext.getCmp('sourceAcctItemId').getValue();
		value['orderAcctItemId'] = Ext.getCmp('orderAcctItemId').getValue();
		value['fee'] = Ext.util.Format.formatToFen(Ext.getCmp('feeId').getValue());
		return value;
	}
});

Ext.onReady(function(){
	var panel = new AcctTransferPanel();
	TemplateFactory.gTemplate(panel);
});