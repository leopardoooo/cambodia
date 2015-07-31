/**aaaa
 * 【用户产品订购】
 */
ProdOrderForm = Ext.extend( BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
	selectUserPanel: null,
	transferPayWindow: null,
	// 当前业务代码,单产品订购102，升级100，续订101
	busiCode: null,
	// 客户编号
	custId: null,
	// 当前选中的用户编号
	userId: null,
	// 订单sn（仅在续订和升级才有效）
	filterOrderSn: null,
	// 初始化参数，包括产品列表、资费、上一次订购记录
	baseData: null,
	constructor: function(p){
		this.selectUserPanel = new SelectUserPanel();
		ProdOrderForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            border: false,
            layout: 'border',
			items:[{
				bodyStyle:'background:#F9F9F9;padding-top:10px; border-bottom: 0px;',
				region: "north",
				layout: 'form',
				title: '第一步：选择产品',
				height: 94,
				items: [{
					xtype: 'combo',
				    fieldLabel: "产品名称",
				    width: 200,
				    displayField: 'prod_name',
				    valueField: 'prod_id',
				    mode: 'local',
				    id: 'boxProdId',
				    store: new Ext.data.JsonStore({
						fields : ['prod_id', 'prod_name', 'prod_desc']
					}),
					listeners: {
						scope: this,
						select: this.doSelectProd
					}
				},{
					anchor: '90%',
		            xtype: 'displayfield',
		            fieldLabel: '产品描述',
		            id: 'dfProdDesc',
		            value: 'balabalabala..'
				}]
			},{
				region: 'center',
				layout: 'border',
				border: false,
				items: [{
					region: 'west',
					width: 280,
					title: "第二步：确定订购用户",
					split: true,
			        minSize: 150,
			        maxSize: 350,
					layout: 'fit',
					bodyStyle: "border-bottom: none;",
					items: [this.selectUserPanel]
				},{
					region: 'center',
					layout: 'form',
					labelAlign: 'top',
					labelPad: 20,
					defaults: {anchor: "90%"},
					title: '第三步：订购信息',
					bodyStyle: 'padding: 15px 0 0 20px;border-bottom: none;',
					items: [{
					    xtype: 'combo',
					    fieldLabel: "产品资费",
					    displayField: 'disct_name',
					    valueField: 'disct_id',
					    mode: 'local',
					    emptyText: '选择产品资费..',
					    id: 'boxProdTariff',
					    store: new Ext.data.JsonStore({
							fields: ['disct_id', 'disct_name', 'billing_cycle','max_order_cycle']
						}),
						listeners: {
							scope: this,
							select: this.doSelectTariff
						}
					},{
						xtype: 'spinnerfield',
			            fieldLabel: '订购月数',
			            value: 0,
			            id: 'sfOrderCycle',
			            minValue: 0
					},{
						xtype: 'panel',
						anchor: '100%',
						baseCls: 'x-plain',
						style: 'padding-bottom: 10px;',
						html: '<b>【上期订购结束日：2015-07-30】</b>'
					},{
						xtype: 'datefield',
					    fieldLabel: "开始计费日",
					    minValue: new Date()
					},{
						xtype: 'datefield',
			            fieldLabel: '结束计费日',
			            minValue: new Date()
					}]
				}]
			},{
				region: "south",
				height: 40,
				bodyStyle: 'background-color: rgb(213,225,241);padding: 10px 0 10px 30px; color: red',
				html: "<b> * 共计:$100.00（新增订购:<b>58.00</b> + <a id='transferHrefTag' href='#'>转移支付:<b>42.00</b></a>）</b>"
			}]
		});
	},
	// 加载产品，资费等数据
	doLoadBaseData: function(){
		var requestObj = {};
		requestObj["busiCode"] = App.getData().currentResource.busicode;
		requestObj["custId"] = App.getCustId();
		// 单用户订购
		if(requestObj.busiCode === "102"){
			var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
			requestObj["userId"] = users[0].get("user_id");
		// 续订和升级
		}else if(requestObj.busiCode === "100" || requestObj.busiCode === "101"){
			var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
			var prodData = prodGrid.selModel.getSelected().data;
			requestObj["filter_order_sn"] = prodData["order_sn"];
		}
		
		this.baseData = {
			"userDesc": "DTT_12312312,OTT_122",
			"prodList": [
			     {prod_id: 100001,prod_name: 'OTT基本包', prod_desc: '没什么好说的，爱定不定'},
			     {prod_id: 100002,prod_name: '宽带1M',prod_desc: '蜗牛一样的速度，你还敢定吗'},
			     {prod_id: 100003,prod_name: '普通套餐：金包',prod_desc: '大资生活'},
			     {prod_id: 100004,prod_name: '普通套餐：银包',prod_desc: '小资生活'},
			     {prod_id: 100005,prod_name: '协议套餐：110',prod_desc: '高干子弟，惹不起。'}
			 ],
			 "tariffMap": {
				 "100001": [
				     {disct_id: 101,disct_name: "10元/月",billing_cycle: 1,max_order_cycle: null},
				     {disct_id: 102,disct_name: "50元半年",billing_cycle: 6,max_order_cycle: 4}
				 ],
				 "100002": [
				     {disct_id: 103,disct_name: "10元/月",billing_cycle: 1,max_order_cycle: null}
				 ]
			 },
			 "lastOrderMap":null
		}
		
		Ext.getCmp("boxProdId").getStore().loadData(this.baseData["prodList"]);
		
//		Ext.Ajax.request({
//			// url :root + '/commons/x/Order.loadProdList.action',
//			scope : this,
//			params: requestObj,
//			success : function(response,opts){
//				var responseObj = Ext.decode(response.responseText);
//				// 'userDesc', 'prodList', 'tariffMap','lastOrderMap'
//				this.baseData = responseObj;
//			}
//		});
	},
	doInit:function(){
		this.doLoadBaseData();
		ProdOrderForm.superclass.doInit.call(this);
//		Ext.get("transferHrefTag").on("click", function(e, t, o){
//			if(!this.transferPayWindow){
//				this.transferPayWindow = new TransferPayWindow();
//			}
//			this.transferPayWindow.show();
//		}, this);
	},
	//产品下拉框选中事件
	doSelectProd: function(box, record, index){
		var prodId = record.get("prod_id");
		Ext.getCmp("dfProdDesc").setValue(record.get("prod_desc"));
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var tariffs = this.baseData["tariffMap"][prodId] || [];
		boxProdTariff.getStore().loadData(tariffs);
		// 如果只有一个默认选中
		if(tariffs.length == 1){
			boxProdTariff.setValue(tariffs[0]["disct_id"]);
		}else{
			boxProdTariff.setRawValue("");
		}
	},
	// 资费下拉框选中事件
	doSelectTariff: function(box, record, index){
		var billingCycle = record.get("billing_cycle");
		var maxOrderCycle = record.get("max_order_cycle");
		var sfOrderCycle = Ext.getCmp("sfOrderCycle");
		// 递增值
		sfOrderCycle.spinner.incrementValue = billingCycle;
		//最小值
		sfOrderCycle.setMinValue(billingCycle);
		// 最小周期
		sfOrderCycle.setValue(billingCycle);
		//最大值
		if(maxOrderCycle){
			sfOrderCycle.setMaxValue(maxOrderCycle*billingCycle);
		}else{
			sfOrderCycle.setMaxValue(null);
		}
	},
	// 上期订购结束日
	loadLastOrderExpDate: function(){
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/Order.loadLastOrderExpDate.action',
			params : { /*cust_id: prod_id: user_id:prod_id是套餐则填空，否则填用户栏选中的user_id */},
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				alert(response.responseText);
			}
		});
	},
	// 开始计费日
	loadLastOrderExpDate: function(){
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/Order.loadOrderEffDate.action',
			params : { /*cust_id: prod_id: user_id: prod_id是套餐则填空，否则填用户栏选中的user_id last_order_sn:上期订购结束日获得的order_sn order_months: 订购月数  */},
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				alert("date: " + response.responseText);
			}
		});
	},
	//加入产品列表
	doAddProdList: function(){
		alert("加入产品列表..");
	},
	doValid : function(){
		return ProdOrderForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var values = {};
		return values;
	},
	getFee: function(){
		return 0;
	},
	success : function(form,res){
		var userId = res.simpleObj;
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

// 产品临时列表
SelectedProdGrid = Ext.extend(Ext.grid.GridPanel, {
	store: new Ext.data.JsonStore({
		fields: ["cust_id","user_id","user_name","user_selected","prod_id", "prod_name","tariff_id","tariff_name","last_order_sn","last_exp_date"]
	}),
	constructor: function(){
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var columns = [sm,
    	    {header: "产品名称", width: 100,sortable:true, dataIndex: 'prod_name'},
    	    {header: "资费", width: 80, sortable:true, dataIndex: 'traff_name'},
    	    {header: "用户", width: 100, sortable:true, dataIndex: 'user_ids'},
    	    {header: "订购月数", width: 60, sortable:true, dataIndex: 'month_count'},
    	    {header: "金额", width: 60, sortable:true, dataIndex: 'fee'},
    	    {header: "开始计费日", width: 80, sortable:true, dataIndex: 'start_date'},
    	    {header: "结束计费日", width: 80, sortable:true, dataIndex: 'start_date'},
    	    {header: "上期结束日", width: 80, sortable:true, dataIndex: 'last_order_end_date'}
    	];
		
		return SelectedProdGrid.superclass.constructor.call(this, {
			stripeRows: true,
			region: "center",
			layotu: "fit",
			title: "已定产品列表",
			columns: columns,
	        stateful: true,
	        sm: sm,
	        bbar: [{
	        	text: '移除选中',
	        	iconCls: 'icon-del'
	        }]
		});
	}
});

/** 入口 */
Ext.onReady(function(){
//	var main = new Ext.Panel({
//		layout: "border",
//		defaults: {border: false},
//		items:[{
//			region: "north",
//			height: 200,
//			layout: "fit",
//			items: new ProdOrderForm()
//		}, new SelectedProdGrid()]
//	});
	var box = TemplateFactory.gTemplate(new ProdOrderForm());
});
