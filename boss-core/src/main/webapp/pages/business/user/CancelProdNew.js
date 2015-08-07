/**
 * 产品退订
 */

CancelProdGrid = Ext.extend(Ext.grid.GridPanel,{
	selectProdStore: null,
	checkSm:null,
	busiCode:null,
	custId:null,
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
			         "order_type","package_group_id","remark","public_acctitem_type"],
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
		var cm= new Ext.ux.grid.LockingColumnModel({
	        	columns:[
	        	this.checkSm,	
				{header:'订购SN',dataIndex:'order_sn',width:40},
				{header:'产品名称',dataIndex:'prod_name',width:120},
				{header:'所属套餐',dataIndex:'package_name',width:80},
				{header:'当前资费',dataIndex:'tariff_name',	width:80},
				{header:'生效日期',dataIndex:'eff_date',width:80,renderer: Ext.util.Format.dateFormat},
				{header:'失效日期',dataIndex:'exp_date',width:80,renderer: Ext.util.Format.dateFormat},
				{header:'状态',dataIndex:'status_text',	width:60,renderer:Ext.util.Format.statusShow},				
				{header:'订购时间',dataIndex:'order_time',width:80},
				{header:'订单余额',dataIndex:'active_fee',width:80,xtype: 'moneycolumn'}
		        
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
		var totalBusiFee = 0; 
		for(var i=0;i<records.length;i++){
			totalBusiFee = totalBusiFee + records[i].get('active_fee');
			orderSns.push(records[i].get('order_sn'))
		}
		this.orderSns = orderSns;
		this.totalFee = totalBusiFee*-1;
		Ext.get("totalAmount").update(String(this.totalFee/100));
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
		if(this.busiCode == '1027'){
			var mod = this.getSelectionModel();
			mod.selectAll();
			mod.lock();
		}
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
				height: 40, 
				bodyStyle: 'background-color: rgb(213,225,241);padding: 10px 0 10px 30px; color: red',
				html: "* 退款总金额$:<span id='totalAmount'>--</span>"					
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
	getValues:function(){
		var obj = {};
		obj['cancelFee'] = this.cancelProdGrid.checkSm.totalFee;
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
 