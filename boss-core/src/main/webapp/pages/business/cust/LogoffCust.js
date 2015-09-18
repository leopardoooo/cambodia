/**
 * 客户销户
 */
 
NewAcctItemGrid = Ext.extend(Ext.grid.GridPanel,{
	height : 200,
	acctItemStore : null,
	border:false,
	region : 'north',
	constructor : function(){
		this.acctItemStore = new Ext.data.JsonStore({
			fields : [
			{name : 'acct_id'},
			{name : 'acctitem_id'},
			{name : 'acctitem_name'},
			{name : 'acctitem_type'},
			{name : 'active_balance',type : 'float'},
			{name : 'real_bill',type : 'float'},
			{name : 'real_fee',type : 'float'},
			{name : 'can_trans_balance',type : 'float'},
			{name : 'can_refund_balance',type : 'float'},
			{name : 'owe_fee',type : 'float'},
			{name : 'adjust_balance',type : 'float'},
			{name : 'order_balance',type : 'float'},
			{name : 'real_balance',type : 'float'},
			{name : 'prod_id'},
			{name : 'prod_name'},
			{name : 'tariff_id'},
			{name : 'tariff_name'},
			{name : 'prod_sn'},
			{name : 'inactive_balance'},
			{name : 'user_id'},
			{name : 'next_tariff_id'},
			{name : 'next_tariff_name'},
			{name : 'prod_status'},
			{name : 'prod_status_text'}
			]
		});
		
		var publicAcctInfo;
		var acctStore = App.getApp().main.infoPanel.custPanel.acctItemGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(acctStore.getAt(i).get('acct_type') == 'PUBLIC'){
				publicAcctInfo = acctStore.getAt(i).data;
				break;
			}
		}
		
		if(publicAcctInfo){
			this.acctItemStore.loadData(publicAcctInfo);
		}
		
		var cm = [
			{header:'账目名称',dataIndex:'acctitem_name',width : 100},
			{header:'余额',dataIndex:'active_balance',renderer : Ext.util.Format.formatFee,width : 80},
			{header:'实时余额',dataIndex:'real_balance',renderer : Ext.util.Format.formatFee,width : 90},
			{header:'可转余额',dataIndex:'can_trans_balance',renderer : Ext.util.Format.formatFee,width : 90},
			{header:'可退余额',dataIndex:'can_refund_balance',renderer : Ext.util.Format.formatFee,width : 90}
		]
		
		NewAcctItemGrid.superclass.constructor.call(this,{
			title : '客户账户信息',
			store:this.acctItemStore,
			columns:cm
		});
	}
});
 
LogoffCust = new Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Cust!logoffCust.action",
	totalRecord : null,
	confirmMsg:null,
	yesBtn:null,
	isCloseBigWin:false,
	constructor: function(){
		var grid = new NewAcctItemGrid();
		var total = {
			"acctitem_name" : '总计',
			'active_balance' : 0,
			'real_balance' : 0,
			'can_trans_balance' : 0,
			'can_refund_balance' : 0
		}
		for(var i=0;i<grid.getStore().getCount();i++){
			total.active_balance = total.active_balance + grid.getStore().getAt(i).data.active_balance;
			total.real_balance = total.real_balance + grid.getStore().getAt(i).data.real_balance;
			total.can_trans_balance = total.can_trans_balance + grid.getStore().getAt(i).data.can_trans_balance;
			total.can_refund_balance = total.can_refund_balance + grid.getStore().getAt(i).data.can_refund_balance;
		}
		this.totalRecord = total;
		grid.getStore().loadData([total],true);		
		LogoffCust.superclass.constructor.call(this,{
			bodyStyle:Constant.TAB_STYLE,
            labelWidth:65,
            border:false,
            layout:'border',
            labelAlign:'right',
            items : [grid,{
            	xtype : 'panel',
            	title : '业务处理',
            	bodyStyle: Constant.TAB_STYLE,
				layout:'form',
				region:'center',
				items : [{
	            	xtype:'label',
	            	fieldLabel:'客户名称',
	            	text:App.getData().custFullInfo.cust.cust_name
	            },{
	            	xtype:'label',
	            	fieldLabel:'客户地址',
	            	text:App.getData().custFullInfo.cust.addr_id_text
	            },{
	            	xtype:'label',
	            	fieldLabel:'客户类别',
	            	text:App.getData().custFullInfo.cust.cust_type_text
	            },{
	            	xtype : 'paramcombo',
					id : 'banlanceDealType',
					fieldLabel : '余额处理',
					paramName:'ACCT_BALANCE',
					defaultValue : 'REFUND',
					listeners : {
						scope : this,
						'beforequery' : function(obj){
							var store = obj.combo.getStore();
							for(var i=0;i<store.getCount();i++){
								if(store.getAt(i).get('item_value') == "TRANS"){
									obj.combo.getStore().remove(store.getAt(i));
									break;
								}
							}
						},
						'select' : this.doSelectDealType
					}
	            },{
	            	xtype : 'numberfield',
					id : 'refundFeeValue',
					fieldLabel : '退款金额',
					readOnly: true,
					style: Constant.TEXTFIELD_STYLE,
					allowNegative : false,
					value : Ext.util.Format.formatFee(total.can_refund_balance),
					maxValue : Ext.util.Format.formatFee(total.can_refund_balance)
	            }]
			}]
		
		});
		/*var custDeviceGrid = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid;
		if(custDeviceGrid.CustDeviceArray.length > 0){
			this.confirmMsg = "确定回收自购的设备？";
			this.yesBtn = true;
			this.isCloseBigWin = true;
		}*/
	},
	doSelectDealType : function(combo){
		this.items.itemAt(1).remove(Ext.getCmp('refundFeeValue'));
		if(combo.getValue() == 'REFUND'){
			this.items.itemAt(1).add({
				xtype : 'numberfield',
				id : 'refundFeeValue',
				fieldLabel : '退款金额',
				readOnly: true,
				style: Constant.TEXTFIELD_STYLE,
				allowNegative : false,
				value : Ext.util.Format.formatFee(this.totalRecord.can_refund_balance),
				maxValue : Ext.util.Format.formatFee(this.totalRecord.can_refund_balance)
			})
		}
		this.doLayout();
	},
	getFee : function(){
		var cmp = Ext.getCmp('refundFeeValue');
		if(cmp){
			return -cmp.getValue();
		}
		return 0;
	},
	getValues : function(){
		var all = {};
		all["banlanceDealType"] = Ext.getCmp('banlanceDealType').getValue();
		return all;
	},
	success:function(){
		App.getApp().data.custFullInfo.cust.status = 'INVALID';
		App.getApp().main.infoPanel.changeDisplay();
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}

});
/**
 * 入口
 */
Ext.onReady(function(){
	var tf = new LogoffCust();
	var box = TemplateFactory.gTemplate(tf);
});