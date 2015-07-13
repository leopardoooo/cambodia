/**
 * 【用户销户】
 */

NewAcctItemGrid = Ext.extend(AcctItemGrid,{
	singleSelect : true,
	constructor : function(){
		NewAcctItemGrid.superclass.constructor.call(this);
	},
	initEvents : function(){
		this.on("rowclick", function(grid ,index, e){
			Confirm("确定要选择该账目吗?", this ,function(){
				var record = grid.getStore().getAt(index);
				Ext.getCmp('newAcctItemId').setValue(record.get('acctitem_id'));
				Ext.getCmp('newAcctItemName').setValue(record.get('acctitem_name'));
				Ext.getCmp('acctItemSelectWin').hide();
			});
		}, this );
	}
});

/**
 * 转账面板
 * @class TransAcctPanel
 * @extends Ext.Panel
 */
TransAcctPanel = Ext.extend(Ext.Panel,{
	constructor : function(acctInfo){
		var grid = new NewAcctItemGrid();
		
		if(acctInfo.acctitems){
			var data = [];
			for(var i=0;i<acctInfo.acctitems.length;i++){
				if(acctInfo.acctitems[i].acctitem_type != 'SPEC_FEE'){//去除报停费公用账目
					data.push(acctInfo.acctitems[i])
				}
			}
			grid.getStore().loadData(data);
		}
		grid.getColumnModel().setHidden(10,true);
		grid.getColumnModel().setHidden(9,true);
		grid.getColumnModel().setHidden(8,true);
		grid.getColumnModel().setHidden(7,true);
		
		TransAcctPanel.superclass.constructor.call(this,{
			baseCls:'x-plain',
			layout:'form',
			items : [{
					xtype : 'textfield',
					id : 'newAcctItemName',
					allowBlank : false,
					editable : false,
					name : 'newAcctItemName',
					fieldLabel : '新账目',
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
									width: 520,
									height: 400,
									layout: 'fit',
									border: false,
									items: grid 
								}).show();
							}
							
						}
					}
				},{
					xtype : 'hidden',
					id : 'newAcctItemId',
					name : 'newAcctItemId'
				}]
		})
	}
});

//选中用户产品的账户信息
UserProdGrid = Ext.extend(Ext.grid.GridPanel,{
	prodStore : null,
	transBalance : null,
	totalRecord : null,//总计数据
	constructor : function(){
		var recs = App.getApp().main.infoPanel.userPanel.userGrid.getSelectionModel().getSelections();
		var prodMap = App.getApp().main.infoPanel.userPanel.prodGrid.prodMap;
		this.prodStore = new Ext.data.GroupingStore({
			reader: new Ext.data.JsonReader({},[
				{name : 'active_balance',type : 'float'},
				{name : 'real_bill',type : 'float'},
				{name : 'real_fee',type : 'float'},
				{name : 'can_trans_balance',type : 'float'},
				{name : 'can_refund_balance',type : 'float'},
				{name : 'user_name'},
				{name : 'user_type_text'},
				{name : 'stb_id'},
				{name : 'card_id'},
				{name : 'modem_mac'},
				{name : 'prod_name'},
				{name : 'user_id'}
			]),
			groupField : 'user_id'
		});
		var records = [];
		var total = {};
		total['can_trans_balance'] = 0;
		total['can_refund_balance'] = 0;
		for(var i=0;i<recs.length;i++){
			if(prodMap && prodMap[recs[i].get('user_id')]){
				var prodReocrds = prodMap[recs[i].get('user_id')];
				for(var j=0;j<prodReocrds.length;j++){
					var data = App.getAcctItemByProdId(prodReocrds[j].prod_id,prodReocrds[j].user_id);
					data["prod_name"] = prodReocrds[j].prod_name;
					data["user_id"] = prodReocrds[j].user_id;
					data["user_name"]= recs[i].get('user_name');
					data["user_type_text"] = recs[i].get('user_type_text');
					data["stb_id"] = recs[i].get('stb_id');
					data["card_id"] = recs[i].get('card_id');
					data["modem_mac"] = recs[i].get('modem_mac');
					records.push(data);
					
					total.can_trans_balance = total.can_trans_balance + data.can_trans_balance;
					total.can_refund_balance = total.can_refund_balance + data.can_refund_balance;
				}
			}
		}
		this.prodStore.loadData(records);
		this.transBalance = total.can_trans_balance > 0 ? true:false;
		this.totalRecord = total;
		
		var cm = [
			{header:'用户ID',dataIndex:'user_id',hidden : true},
			{header:'用户名',dataIndex:'user_name',hidden : true},
			{header:'用户类型',dataIndex:'user_type_text',hidden : true},
			{header:'机顶盒',dataIndex:'stb_id',hidden : true},
			{header:'智能卡',dataIndex:'card_id',hidden : true},
			{header:'modem号',dataIndex:'modem_mac',hidden : true},
			{header:'产品名称',dataIndex:'prod_name',width : 115},
			{header:'余额',dataIndex:'active_balance',width : 95,renderer : Ext.util.Format.formatFee},
			{header:'本月费用',dataIndex:'real_bill',width : 95,renderer : Ext.util.Format.formatFee},
			{header:'实时费用',dataIndex:'real_fee',width : 95,renderer : Ext.util.Format.formatFee},
			{header:'可转余额',dataIndex:'can_trans_balance',width : 95,renderer : Ext.util.Format.formatFee},
			{header:'可退余额',dataIndex:'can_refund_balance',width : 95,renderer : Ext.util.Format.formatFee}
		]
		UserProdGrid.superclass.constructor.call(this,{
			title : '用户产品账户信息',
			height : 300,
			store : this.prodStore,
			columns : cm,
			view: new Ext.grid.GroupingView({
	            groupTextTpl: '用户名：{[values.rs[0].data["user_name"]]} 用户类型：{[values.rs[0].data["user_type_text"]]} ' +
	            		'{[values.rs[0].data["stb_id"]?"机顶盒:"+[values.rs[0].data["stb_id"]]+" ":""]}' +
        				'{[values.rs[0].data["card_id"]? "智能卡："+[values.rs[0].data["card_id"]]+" ": ""]}' +
        				'{[values.rs[0].data["modem_id"]? "modem号："+[values.rs[0].data["modem_id"]]: ""]}'
        	})
		})
	}
});


LogoffUserForm = Ext.extend(BaseForm,{
	oldBalanceDealTyep : 'REFUND',//现金退款
	publicAcctInfo : null,
	userProdGrid : null,
	url: Constant.ROOT_PATH + "/core/x/User!logoffUser.action",
	constructor: function(){
		this.userProdGrid = new UserProdGrid();
		this.userProdGrid["region"]='north';
		
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(acctStore.getAt(i).get('acct_type') == 'PUBLIC'){
				this.publicAcctInfo = acctStore.getAt(i).data;
				break;
			}
		}
		LogoffUserForm.superclass.constructor.call(this,{
            border: false,
            layout: 'border',
			items:[this.userProdGrid,{
            	xtype : 'panel',
            	title : '业务处理',
            	bodyStyle: Constant.TAB_STYLE,
				layout:'column',
				defaults : {
					layout : 'form',
					anchor : '100%',
					baseCls : 'x-plain',
					columnWidth : 0.5,
					labelWidth : 75
				},
				region:'center',
				items : [{
					items : [{
						fieldLabel:'余额处理',
						xtype:'paramcombo',
						id : 'banlanceDealType',
						allowBlank:false,
						defaultValue : 'REFUND',
						paramName:'ACCT_BALANCE',
						listeners : {
							scope : this,
							'select' : this.addTransAcctPanel
						}
					}]
				},{
					id : 'DealTypePanel',
					items : [{
						xtype : 'numberfield',
						id : 'refundFeeValue',
						fieldLabel : '退款金额',
						readOnly: true,
						style: Constant.TEXTFIELD_STYLE,
						allowNegative : false,
						allowBlank : false,
						value : Ext.util.Format.formatFee(this.userProdGrid.totalRecord.can_refund_balance),
						maxValue : Ext.util.Format.formatFee(this.userProdGrid.totalRecord.can_refund_balance)
					}]
				}]
            }]
		});
	},
	doInit : function(){
		var dealType = Ext.getCmp('banlanceDealType');
		var dealTypeStore = dealType.getStore();
		if(!this.userProdGrid.transBalance){
			dealTypeStore.removeAt(dealTypeStore.find('item_value','TRANS'));
		}
	},
	addTransAcctPanel : function(combo){
		//值不变，返回
		if(this.oldBalanceDealTyep == combo.getValue()){
			return;
		}
		Ext.getCmp('DealTypePanel').removeAll();
		//如果退至客户账户
		if(combo.getValue() == 'TRANS'){
			if(this.userProdGrid.transBalance){
				Ext.getCmp('DealTypePanel').add(new TransAcctPanel(this.publicAcctInfo));
			}
		}else if(combo.getValue() == 'REFUND'){
			Ext.getCmp('DealTypePanel').add({
				xtype : 'numberfield',
				id : 'refundFeeValue',
				fieldLabel : '退款金额',
				allowNegative : false,
				allowBlank : false,
				value : Ext.util.Format.formatFee(this.userProdGrid.totalRecord.can_refund_balance),
				maxValue : Ext.util.Format.formatFee(this.userProdGrid.totalRecord.can_refund_balance)
			});
		}
		
		this.oldBalanceDealTyep = combo.getValue();
		this.doLayout();
	},
	getValues : function(){
		var all = {};
		all['banlanceDealType'] = Ext.getCmp('banlanceDealType').getValue();
		if(Ext.getCmp('newAcctItemId')){
			all['transAcctId'] = this.publicAcctInfo.acct_id;
			all['transAcctItemId'] = Ext.getCmp('newAcctItemId').getValue();
		}else{
			all['transAcctId'] = "";
			all['transAcctItemId'] = "";
		}
		
		return all;
	},
	getFee : function(){
		var cmp = Ext.getCmp('refundFeeValue');
		if(cmp){
			return -cmp.getValue();
		}
		return 0;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
	
});

/**
 * 入口
 */
Ext.onReady(function(){
	var lfu = new LogoffUserForm();
	var box = TemplateFactory.gTemplate(lfu);
})