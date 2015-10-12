/**
 * 产品退订
 */

CancelProdGrid = Ext.extend(Ext.grid.GridPanel,{
	selectProdStore: null,
	checkSm:null,
	busiCode:null,
	custId:null,
	totalAcctNum:0,
	totalFeeNum:0,
	totalFee:0,
	orderSns:null,
	constructor:function(parent){
		this.busiCode = App.getData().currentResource.busicode;
		this.custId = App.getData().custFullInfo.cust.cust_id;
		this.parent = parent;
		// 选中的产品
		this.selectProdStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + '/core/x/ProdOrder!queryCancelOrderAndFee.action' ,
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
		
		
		this.checkSm = new Ext.grid.CheckboxSelectionModel({
			checkOnly : true,
			listeners : {
				rowselect : this.doSelectedChange,
				rowdeselect : this.doSelectedChange
			}
		});
		var cms = lmain("user.prod.base.columns");
		var cm= new Ext.ux.grid.LockingColumnModel({
	        	columns:[
	        	this.checkSm,	
				{header:cms[0],dataIndex:'order_sn',width:40},
				{header:cms[1],dataIndex:'prod_name',width:120},
				{header:cms[2],dataIndex:'package_name',width:80},
				{header:cms[3],dataIndex:'tariff_name',	width:80},
				{header:lmain("user._form.orderFee"),dataIndex:'active_fee',width:80,xtype: 'moneycolumn'},
				{header:lmain("user._form.canRetrunFee"),dataIndex:'balance_cfee',width:80,xtype: 'moneycolumn'},
				{header:lmain("user._form.canTransferFee"),dataIndex:'balance_acct',width:80,xtype: 'moneycolumn'},
				{header:cms[4],dataIndex:'eff_date',width:80,renderer: Ext.util.Format.dateFormat},
				{header:cms[5],dataIndex:'exp_date',width:80,renderer: Ext.util.Format.dateFormat},
				{header:cms[6],dataIndex:'status_text',	width:60,renderer:Ext.util.Format.statusShow},				
				{header:cms[8],dataIndex:'order_time',width:80}
		        
	        ]})
		CancelProdGrid.superclass.constructor.call(this,{
			region:'center',
			id:'selectCancelProdGrid',
			store:this.selectProdStore,
			sm : this.checkSm,
			cm:cm
		});
	},
	doSelectedChange:function(sm){
		var records = sm.getSelections();
		var orderSns = [];
		var totalFeeNum = 0;
		var totalAcctNum = 0; 
		for(var i=0;i<records.length;i++){
			if(records[i].get('balance_cfee')){
				totalFeeNum = totalFeeNum + records[i].get('balance_cfee');
			}
			if(records[i].get('balance_acct')){
				totalAcctNum = totalAcctNum + records[i].get('balance_acct');
			}
			orderSns.push(records[i].get('order_sn'))
		}
		this.orderSns = orderSns;
		this.totalFeeNum = totalFeeNum;
		this.totalAcctNum = totalAcctNum;
		if(this.totalFeeNum>0){
			Ext.get("cfeeTotalAmount").update(String(this.totalFeeNum/100.0));
			Ext.getCmp('reallFeeId').maxValue = this.totalFeeNum/100.0;
			Ext.getCmp('reallFeeId').setValue(this.totalFeeNum/100.0);
		}else{
			Ext.getCmp('reallFeeId').maxValue = 0;
			Ext.getCmp('reallFeeId').setValue(0);
		}
		if(this.totalAcctNum>0){
			Ext.get("acctTotalAmount").update(String(this.totalAcctNum/100.0));
		}
		this.totalFee = this.totalFeeNum + this.totalAcctNum;
	},
	refresh:function(orderSn){
		this.selectProdStore.load({
			params:{
				busi_code: this.busiCode,
				cust_id:this.custId,
				order_sn:orderSn
			}
		});
		this.selectProdStore.on('load',this.doLoadResult,this,{delay:100});
	},
	doLoadResult:function(){
		//普通退订 需要全选，高级退订可以自由选择
//		if(this.busiCode == '1027'){
			var mod = this.getSelectionModel();
			mod.selectAll();
			mod.lock();
//		}
	}	
	
})
 
CancelProdNewForm = Ext.extend(BaseForm,{
	cancelProdGrid: null,
	url: Constant.ROOT_PATH + "/core/x/ProdOrder!cancelProd.action",
	constructor: function(){
		this.cancelProdGrid = new CancelProdGrid(this);
		CancelProdNewForm.superclass.constructor.call(this,{
			autoScroll:true,
            border: false,
            layout:'border', 
            items:[this.cancelProdGrid,{
				region: "south",
				height: 130, 
//				bodyStyle: 'background-color: rgb(213,225,241);padding: 10px 0 10px 30px; color: red',
				buttonAlign:'center',
				 flex:1,
       			 frame:true,  
				 labelAlign:'right',  
				 layout:'column',
				 border: false,
		         items:[
		         		{ 
		         	columnWidth:.5,
		         	xtype:'fieldset',  
				    height: 100, 
				    title: lmain("user._form.retrunInfo"),
         			style:'margin-left:10px;padding: 10px 0 10px 10px; color: red',
         			layout:'column',
         			items:[{
         				columnWidth:.5,
         				items:[{
         						bodyStyle:'padding-top:4px',
		         				html: "* "+ lmain("user._form.canRetrunFee") +"$:<span id='cfeeTotalAmount'>--</span>"
			         			}]
         				},{
         				columnWidth:.5,
         				layout : 'form',
         				labelWidth:75,  
         				items:[{
								fieldLabel : lbc("common.busiWay"),
								id : 'CancelFeeTypeId',
								name:'acct_balance_type',
								allowBlank : false,
								xtype:'paramcombo',
								width: 80,
								defaultValue:'REFUND',
								paramName:'ACCT_BALANCE',
								listeners: {
									scope: this,
									'expand': function(combo){
										var store = combo.getStore();
										store.removeAt(store.find('item_value','EXPIRE'));
									}
								}
							}]
         				},{
         				columnWidth:1,
         				layout: 'form',
         				labelWidth: 120,
         				bodyStyle:'padding-top:10px;',
         				items:[{
         						id:'reallFeeId', fieldLabel: lmain("user._form.canRefundRealFee"), xtype: 'numberfield', 
         						allowNegative: false, allowBlank: false, minValue: 0
			         		}]
         				}]
		         },{ 
		         	columnWidth:.5,
		         	xtype:'fieldset',  
				    height: 100, 
				    title: lmain("user._form.transferInfo"),
         			style:'margin-left:10px;padding: 25px 0 10px 10px; color: red',
         			layout:'column',
         			items:[{
         				columnWidth:.50,
         				items:[{
         						bodyStyle:'padding-top:4px',
		         				html: "* "+ lmain("user._form.canTransferFee") +"$:<span id='acctTotalAmount'>--</span>"
			         			}]
         				},{
         				columnWidth:.50,
         				layout : 'form',
         				labelWidth:75,  
         				items:[{
								fieldLabel : lbc("common.busiWay"),
								name:'order_fee_type_id',
								allowBlank : false,
								xtype:'paramcombo',
								width: 80,
								disabled: true,
								defaultValue:'TRANS',
								paramName:'ACCT_BALANCE',
								listeners: {
									scope: this,
									'expand': function(combo){
										var store = combo.getStore();
										store.removeAt(store.find('item_value','EXPIRE'));
										store.removeAt(store.find('item_value','REFUND'));
									}
								}
							}]
         				}]
		         }]
			}]
		})
	},
	doInit:function(){
		var panelId = App.getData().currentPanelId;
		var prodGrid = null;
		// 套餐
		if(panelId === "U_CUST_PKG"){
			prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid.custPkgGrid;
		}else{
			prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid.baseProdGrid;
		}
		var record = prodGrid.selModel.getSelected().data;				
		this.cancelProdGrid.refresh(record["order_sn"]);
		
		CancelProdNewForm.superclass.doInit.call(this);
	},
	doValid : function(){
		var obj = {};
		var prodSn = this.cancelProdGrid.checkSm.orderSns;
		if(!prodSn ||prodSn.length == 0){
			obj["isValid"] = false;
			obj["msg"] = "请选择一项进行退订";
			return obj;
		}
		return CancelProdNewForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var obj = {};
		var realFee = Ext.util.Format.formatToFen(Ext.getCmp('reallFeeId').getValue());
		obj['cancelFee'] = (this.cancelProdGrid.totalAcctNum + realFee)*-1;
		obj['acctBalanceType'] = Ext.getCmp('CancelFeeTypeId').getValue();		
		obj['refundFee'] = realFee*-1;	
		obj['orderSns'] = this.cancelProdGrid.checkSm.orderSns;
		return obj;
	},
	success: function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
	
	
})
 
Ext.onReady(function(){
	var nuf = new CancelProdNewForm();
	var box = TemplateFactory.gTemplate(nuf);
});
 