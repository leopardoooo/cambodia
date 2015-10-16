/**
 * 【用户销户】
 */

//选中用户产品的账户信息
UserProdGrid = Ext.extend(Ext.grid.GridPanel,{
	userProdStore : null,
	totalFee : 0,//退款总额
	constructor : function(){
		this.userProdStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + '/core/x/ProdOrder!queryLogoffUserProd.action' ,
			fields: ["tariff_name","disct_name","prod_type","prod_name","prod_type_text","serv_id",
			         "serv_id_text","is_base","is_base_text","public_acctitem_type_text","package_name",
			         "order_sn","package_sn","package_id","cust_id","user_id","prod_id","tariff_id","disct_id",
			         "status","status_text","status_date","eff_date","exp_date","active_fee","bill_fee",
			         "rent_fee","last_bill_date","next_bill_date","order_months","order_fee","order_time",
			         "order_type","package_group_id","remark","public_acctitem_type","balance_cfee","balance_acct"],
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
		var cms = lmain("user.prod.base.columns");
		var cm = [
			{header:'用户ID',dataIndex:'user_id',hidden : true},
			{header:cms[0],dataIndex:'order_sn',width:40},
			{header:cms[1],dataIndex:'prod_name',width:120},
			{header:cms[2],dataIndex:'package_name',width:80},
			{header:cms[3],dataIndex:'tariff_name',	width:80},
			{header:lmain("user._form.orderFee"),dataIndex:'active_fee',width:80,xtype: 'moneycolumn'},
			{header:lmain("user._form.canRetrunFee"),dataIndex:'balance_cfee',width:80,xtype: 'moneycolumn'},
			{header:lmain("user._form.canTransferFee"),dataIndex:'balance_acct',width:80,xtype: 'moneycolumn'},
			{header:cms[4],dataIndex:'eff_date',width:80,renderer: Ext.util.Format.dateFormat},
			{header:cms[5],dataIndex:'exp_date',width:80,renderer: Ext.util.Format.dateFormat},
			{header:cms[6],dataIndex:'status_text',	width:60,renderer:Ext.util.Format.statusShow,hidden : true},				
			{header:cms[8],dataIndex:'order_time',width:80}
		]
		UserProdGrid.superclass.constructor.call(this,{
			title : lmain("user.prod.base._title"),
			region:'center',
			store : this.userProdStore,
			columns : cm			
		})
	},
	doLoadResult:function(store){
		var fee = 0 ;
		var feeTotalNum = 0 ;
		var acctTotalNum = 0 ;
		store.each(function(record){
			fee = fee + record.get('active_fee');
			if(record.get('balance_cfee')){
				feeTotalNum = feeTotalNum + record.get('balance_cfee');
			}
			if(record.get('balance_acct')){
				acctTotalNum = acctTotalNum + record.get('balance_acct');
			}
		});
		this.refundFee = Ext.util.Format.formatFee(feeTotalNum);
		this.transFee = Ext.util.Format.formatFee(acctTotalNum);
		Ext.getCmp('refundFeeValue').setValue(Ext.util.Format.formatFee(fee));
		Ext.getCmp('cfeeTotalAmountId').setValue(this.refundFee);
		Ext.getCmp('acctTotalAmountId').setValue(this.transFee);
		
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
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.userId = record.get('user_id');
		LogoffUserForm.superclass.constructor.call(this,{
            border: false,
            layout: 'border',
			items:[this.userProdGrid,{
            	xtype : 'panel',
            	title : lbc("common.busido"),
            	region: "south",
            	height : 200,
            	bodyStyle: Constant.TAB_STYLE,
				layout:'column',
				defaults : {
					layout : 'form',
					anchor : '100%',
					baseCls : 'x-plain',
					columnWidth : 0.5,
					labelWidth : 85
				},
				items : [{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.name"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('user_name')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.type"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('user_type')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.status"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('status_text')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.stbId"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('stb_id')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.cardId"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('card_id')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user.base.modem"),style:Constant.TEXTFIELD_STYLE,
					value:record.get('modem_mac')}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user._form.canRetrunFee"),style:Constant.TEXTFIELD_STYLE,id:'cfeeTotalAmountId'}]
				},{
					items:[{xtype:'displayfield',fieldLabel:lmain("user._form.canTransferFee"),style:Constant.TEXTFIELD_STYLE,id:'acctTotalAmountId'}]
				},{
					items : [{
						fieldLabel: lbc("common.busiWay"),
						xtype:'paramcombo',
						id : 'banlanceDealType',
						allowBlank:false,
						defaultValue : 'REFUND',
						paramName:'ACCT_BALANCE',
						listeners : {
							scope : this,
							expand: function(combo){
								var store = combo.getStore();
								store.removeAt(store.find('item_value','EXPIRE'));
							},
							select: function(combo){
								var value = combo.getValue();
								if(value == 'TRANS'){
									Ext.getCmp('cfeeTotalAmountId').setValue(0);
									Ext.getCmp('acctTotalAmountId').setValue( this.userProdGrid.refundFee + this.userProdGrid.transFee );
								}else if(value == 'REFUND'){
									Ext.getCmp('cfeeTotalAmountId').setValue( this.userProdGrid.refundFee );
									Ext.getCmp('acctTotalAmountId').setValue( this.userProdGrid.transFee );
								}
							}
						}
					}]
				},{
					width : 100,
					id : 'DealTypePanel',
					items : [{
						xtype : 'numberfield',
						id : 'refundFeeValue',
						fieldLabel : lmain("user._form.totalReturnFee"),
						width : 100,
						readOnly: true,
						style: Constant.TEXTFIELD_STYLE,
						allowNegative : false,
						allowBlank : false
					}]
				}]
            }]
		});
	},
	getValues : function(){
		var all = {};
		all['banlanceDealType'] = Ext.getCmp('banlanceDealType').getValue();
		all['userId'] = this.userId;
		all['cancelFee'] = -1*Ext.util.Format.formatToFen(Ext.getCmp('refundFeeValue').getValue());
		all['refundFee'] =-1*Ext.util.Format.formatToFen(Ext.getCmp('cfeeTotalAmountId').getValue());
		return all;
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