//【用户产品退订】

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
		var data = [];
		for(var i=0;i<acctInfo.acctitems.length;i++){
			if(acctInfo.acctitems[i].acctitem_type != 'SPEC_FEE'){//去除报停费公用账目
				data.push(acctInfo.acctitems[i])
			}
		}
		grid.getStore().loadData(data);
		grid.getColumnModel().setHidden(10,true);
		grid.getColumnModel().setHidden(9,true);
		grid.getColumnModel().setHidden(8,true);
		grid.getColumnModel().setHidden(7,true);
		TransAcctPanel.superclass.constructor.call(this,{
			baseCls:'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			layout:'form',
			labelWidth : 90,
			items : [{
					xtype : 'hidden',
					id : 'newAcctItemId',
					name : 'newAcctItemId'
				},{
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
				}]
		})
	}
});

/**
 * 取消产品表单
 * @class CancelProdForm
 * @extends BaseForm
 */
CancelProdForm = Ext.extend(BaseForm,{
	
	confirmMsg : null,
	
	publicAcctInfo : null,
	oldAcctBalance : 'TRANS',
	selectedProdGrid: null,
	totalRecord : null,//总计数据
	url: Constant.ROOT_PATH + "/core/x/User!cancelProd.action",
	promStore : null,
	constructor: function(){
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(acctStore.getAt(i).get('acct_type') == 'PUBLIC'){
				this.publicAcctInfo = acctStore.getAt(i).data;
				break;
			}
		}
		this.selectedProdGrid = new SelectedProdGrid();
		this.selectedProdGrid["region"]='north';
		
		this.promStore = new Ext.data.JsonStore({
 				url : Constant.ROOT_PATH + "/commons/x/QueryUser!querySelectPromFee.action",
				fields : ['prom_fee_id','prom_fee_name','prom_fee_sn','prod_sn']
 		});
		
		CancelProdForm.superclass.constructor.call(this,{
			id : 'cancelProdForm',
			baseCls:'x-plain',
            layout:'border',
            bodyStyle:Constant.TAB_STYLE,
            items:[this.selectedProdGrid,{
            	xtype : 'panel',
            	title : '业务处理',
            	bodyStyle: Constant.TAB_STYLE,
				layout:'form',
				region:'center',
				items : [{
					xtype : 'paramcombo',
					id : 'banlanceDealType',
					fieldLabel : '余额处理',
					paramName:'ACCT_BALANCE',
					allowBlank : false,
					defaultValue : 'TRANS',
					hiddenName : 'item_value',
					listeners : {
						scope : this,
						'select' : this.addTransAcctPanel
					}
				},new TransAcctPanel(this.publicAcctInfo)]
            }]
		})
	},
	doInit : function(){
		var store = this.selectedProdGrid.getStore();
		var record = store.getAt(store.getCount() - 1);
		this.totalRecord = record;

		var dealType = Ext.getCmp('banlanceDealType');
		var dealTypeStore = dealType.getStore();
		if(record.get('can_trans_balance') == 0||record.get('is_base') =='F'){
			dealTypeStore.removeAt(dealTypeStore.find('item_value','TRANS'));
		}
		if(record.get('can_refund_balance') == 0){
			dealTypeStore.removeAt(dealTypeStore.find('item_value','REFUND'));
		}
		if(dealTypeStore.find('item_value','TRANS') == -1){
			dealType.setValue(dealTypeStore.getAt(0).get('item_value'));
			dealType.fireEvent('select',dealType);
		}
		if (record.get('can_trans_balance') > 0 && record.get('is_base') == 'F') {
			this.confirmMsg = "该增值产品可转余额为:"
					+ Ext.util.Format.convertToYuan(record
							.get('can_trans_balance')) + "元,余额将作废!";
		}
		
		var prodRecord = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelections()[0];
		this.promStore.baseParams = {userId:prodRecord.get('user_id'),prodSn: prodRecord.get('prod_sn')};
        this.promStore.load();
		this.promStore.on("load",this.loadData,this);
		
		Ext.Ajax.request({
			scope : this,
			url: Constant.ROOT_PATH + "/core/x/Acct!queryIsPromFee.action",
			params : {
				userId : prodRecord.get("user_id"),
				prodId : prodRecord.get("prod_id")
			},
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				if(rec.length > 0){
					Alert("如果当前业务是退订"+rec[0].prom_fee_name+"，请在业务结束后联系管理员作废套餐记录!");
				}
			}
		});
	},
	loadData : function(){
		this.promStore.insert(0 ,new Ext.data.Record({
			prom_fee_name: '请选择...', prom_fee_id: ''
		}));
	},
	addTransAcctPanel : function(combo){
		//值不变，返回
		if(this.oldAcctBalance == combo.getValue()){
			return;
		}
		if(this.items.itemAt(1).items.length == 2){
			this.items.itemAt(1).remove(this.items.itemAt(1).items.itemAt(1),true);
		}
		
		if(this.items.itemAt(1).items.length == 3){
			this.items.itemAt(1).remove(this.items.itemAt(1).items.itemAt(2),true);
			this.items.itemAt(1).remove(this.items.itemAt(1).items.itemAt(1),true);
		}
		//如果转账
		if(combo.getValue() == 'TRANS'){
			this.items.itemAt(1).add(new TransAcctPanel(this.publicAcctInfo));
		}else{
			if(combo.getValue() == 'REFUND'){
				this.items.itemAt(1).add({
					xtype : 'numberfield',
					id : 'refundFeeValue',
					fieldLabel : '退款金额',
					readOnly: true,
					style: Constant.TEXTFIELD_STYLE,
					allowNegative : false,
					allowBlank : false,
					value : Ext.util.Format.formatFee(this.totalRecord.get('can_refund_balance')),
					maxValue : Ext.util.Format.formatFee(this.totalRecord.get('can_refund_balance'))
				},{
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
				
			}
		}
		this.oldAcctBalance = combo.getValue();
		this.doLayout();
	},
	getValues : function(){
		var all = {};
		var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
		
		if (prodGrid.getSelectedProdSns().length>0){
			all['prodSns'] = prodGrid.getSelectedProdSns().join(",");
//		}else{
//			if(App.getApp().getCust().cust_type == 'UNIT'){
//				all['prodSns'] = App.getApp().main.infoPanel.getUnitPanel().packageGrid.getSelectedProdSns().join(",");
//			}else{
//				all['prodSns'] = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectedProdSns().join(",");
//			}
		}
		
		if(Ext.getCmp('promFeeSnId')){
			all['promFeeSn'] =  Ext.getCmp('promFeeSnId').getValue();
		}
		all['banlanceDealType'] = Ext.getCmp('banlanceDealType').getValue();
		all['transAcctId'] = this.publicAcctInfo.acct_id;
		if(Ext.getCmp('newAcctItemId')){
			all['transAcctItemId'] = Ext.getCmp('newAcctItemId').getValue();
		}
		if(Ext.getCmp('refundFeeValue')){
			all['refundFeeValue'] = Ext.util.Format.formatToFen(Ext.getCmp('refundFeeValue').getValue());
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

Ext.onReady(function(){
	var cpf = new CancelProdForm();
	var box = TemplateFactory.gTemplate(cpf);
});
