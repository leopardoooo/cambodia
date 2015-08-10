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
	userProdStore : null,
	totalFee : 0,//退款总额
	constructor : function(){
		userProdThis = this;
		this.userProdStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + '/core/x/ProdOrder!queryLogoffUserProd.action' ,
			fields: ["tariff_name","disct_name","prod_type","prod_name","prod_type_text","serv_id",
			         "serv_id_text","is_base","is_base_text","public_acctitem_type_text","package_name",
			         "order_sn","package_sn","package_id","cust_id","user_id","prod_id","tariff_id","disct_id",
			         "status","status_text","status_date","eff_date","exp_date","active_fee","bill_fee",
			         "rent_fee","last_bill_date","next_bill_date","order_months","order_fee","order_time",
			         "order_type","package_group_id","remark","public_acctitem_type"],
			sortInfo : {
				field : 'order_time',
				direction:'DESC'
			}
		});		
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.userProdStore.load({
			params:{
				busi_code: App.getData().currentResource.busicode,
				user_id:record.get('user_id')
			}
		});
		this.userProdStore.on('load',this.doLoadResult,this,{delay:100});
		
		var cm = [
			{header:'用户ID',dataIndex:'user_id',hidden : true},
			{header:'订购SN',dataIndex:'order_sn',width:40},
			{header:'产品名称',dataIndex:'prod_name',width:120},
			{header:'所属套餐',dataIndex:'package_name',width:80},
			{header:'当前资费',dataIndex:'tariff_name',	width:80},
			{header:'订单余额',dataIndex:'active_fee',width:80,xtype: 'moneycolumn'},
			{header:'生效日期',dataIndex:'eff_date',width:80,renderer: Ext.util.Format.dateFormat},
			{header:'失效日期',dataIndex:'exp_date',width:80,renderer: Ext.util.Format.dateFormat},
			{header:'状态',dataIndex:'status_text',	width:60,renderer:Ext.util.Format.statusShow,hidden : true},				
			{header:'订购时间',dataIndex:'order_time',width:80}
		]
		UserProdGrid.superclass.constructor.call(this,{
			title : '用户产品信息',
			height : 300,
			store : this.userProdStore,
			columns : cm			
		})
	},
	initComponent:function(){
		UserProdGrid.superclass.initComponent.call(this);
		
	},
	doLoadResult:function(store){
		var fee = 0 ;
		store.each(function(record){
			fee = fee + record.get('active_fee');
		})
		userProdThis.totalFee = fee;
		if(Ext.getCmp('refundFeeValue')){
			Ext.getCmp('refundFeeValue').setValue(Ext.util.Format.formatFee(userProdThis.totalFee));
			Ext.getCmp('refundFeeValue').maxValue = Ext.util.Format.formatFee(userProdThis.totalFee);
		}
	}
});


LogoffUserForm = Ext.extend(BaseForm,{
	oldBalanceDealTyep : 'REFUND',//现金退款
	publicAcctInfo : null,
	userProdGrid : null,
	url: Constant.ROOT_PATH + "/core/x/User!logoffUser.action",
	userId:null,
	constructor: function(){
		this.userProdGrid = new UserProdGrid();
		this.userProdGrid["region"]='north';
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.userId = record.get('user_id');
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
					labelWidth : 85
				},
				region:'center',
				items : [{
					items:[{xtype:'displayfield',fieldLabel:'用户名',style:Constant.TEXTFIELD_STYLE,
					value:record.get('user_name')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:'用户类型',style:Constant.TEXTFIELD_STYLE,
					value:record.get('user_type')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:'状态',style:Constant.TEXTFIELD_STYLE,
					value:record.get('status_text')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:'机顶盒号',style:Constant.TEXTFIELD_STYLE,
					value:record.get('stb_id')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:'智能卡号',style:Constant.TEXTFIELD_STYLE,
					value:record.get('card_id')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:'MODEM号',style:Constant.TEXTFIELD_STYLE,
					value:record.get('modem_mac')}]
				},{
					width : 60,
					columnWidth : 1,
					items:[{xtype : 'paramcombo',fieldLabel : '回收设备',paramName : 'BOOLEAN',defaultValue: 'T',
				width : 60,foreceSelection : true,id : 'reclaim',hiddenName : 'reclaimDevice'}]
				}
				,{
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
					width : 100,
					id : 'DealTypePanel',
					items : [{
						xtype : 'numberfield',
						id : 'refundFeeValue',
						fieldLabel : '退款金额',
						width : 100,
						readOnly: true,
						style: Constant.TEXTFIELD_STYLE,
						allowNegative : false,
						allowBlank : false
//						value : Ext.util.Format.formatFee(this.userProdGrid.totalFee),
//						maxValue : Ext.util.Format.formatFee(this.userProdGrid.totalFee)
					}]
				}]
            }]
		});
	},
	doInit : function(){
		var dealType = Ext.getCmp('banlanceDealType');
//			dealTypeStore.removeAt(dealTypeStore.find('item_value','TRANS'));
	},
	addTransAcctPanel : function(combo){
		//值不变，返回
		if(this.oldBalanceDealTyep == combo.getValue()){
			return;
		}
		Ext.getCmp('DealTypePanel').removeAll();
		//如果退至客户账户
		if(combo.getValue() == 'TRANS'){
			alert(this.userProdGrid.totalFee);
			if(this.userProdGrid.totalFee>0 && this.publicAcctInfo != null){
				Ext.getCmp('DealTypePanel').add(new TransAcctPanel(this.publicAcctInfo));
			}
		}else if(combo.getValue() == 'REFUND'){
			Ext.getCmp('DealTypePanel').add({
				xtype : 'numberfield',
				id : 'refundFeeValue',
				fieldLabel : '退款金额',
				allowNegative : false,
				allowBlank : false,
				value : Ext.util.Format.formatFee(this.userProdGrid.totalFee),
				maxValue : Ext.util.Format.formatFee(this.userProdGrid.totalFee)
			});
		}
		
		this.oldBalanceDealTyep = combo.getValue();
		this.doLayout();
	},
	getValues : function(){
		var all = {};
		all['banlanceDealType'] = Ext.getCmp('banlanceDealType').getValue();
		all['reclaim'] = Ext.getCmp('reclaim').getValue();
		all['userId'] = this.userId;
		var cmp = Ext.getCmp('refundFeeValue');
		if(cmp){
			all['cancelFee'] = -Ext.util.Format.formatToFen(cmp.getValue());
		}
		if(Ext.getCmp('newAcctItemId')){
			all['transAcctId'] = this.publicAcctInfo.acct_id;
			all['transAcctItemId'] = Ext.getCmp('newAcctItemId').getValue();
		}else{
			all['transAcctId'] = "";
			all['transAcctItemId'] = "";
		}
		
		return all;
	},
	success : function(){
		//刷新支付
		App.getApp().refreshPayInfo(parent);
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