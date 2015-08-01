/*
 * 【用户产品订购】
 */
ProdOrderForm = Ext.extend( BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/ProdOrder!saveOrderList.action",
	selectUserPanel: null,
	transferPayWindow: null,
	// 单产品订购102，升级100，续订101, 套餐订购1015
	busiCode: null,
	// 单用户
	userId: null,
	// 最近一次订购SN
	lastOrderSn: null,
	// 初始化参数，包括产品列表、资费、上一次订购记录
	baseData: null,
	// 转移支付数据明细
	transferPayData: [],
	//  套餐订购用户
	packageGroups: null,
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
				    allowBlank: false,
				    mode: 'local',
				    id: 'boxProdId',
				    store: new Ext.data.JsonStore({
						fields : ['prod_id', 'prod_name', 'prod_desc',"pkg"]
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
					    allowBlank: false,
					    valueField: 'disct_id',
					    mode: 'local',
					    emptyText: '选择产品资费..',
					    id: 'boxProdTariff',
					    store: new Ext.data.JsonStore({
							fields: ['disct_id', 'disct_name', 'billing_cycle','max_order_cycle', 'final_rent']
						}),
						listeners: {
							scope: this,
							select: this.doSelectTariff
						}
					},{
						xtype: 'spinnerfield',
			            fieldLabel: '订购月数',
			           // allowBlank: false,
			            id: 'sfOrderCycle',
			            minValue: 0,
					    listeners: {
					    	scope: this,
					    	spin: function(){
					    		this.doChangeEndDate();
					    		this.doChangeAmount();
					    	}
					    }
					},{
						xtype: 'panel',
						anchor: '100%',
						baseCls: 'x-plain',
						style: 'padding-bottom: 10px;',
						html: '<b>【上期订购结束日：<span id="lastOrderProdExtDate">--</span>】</b>'
					},{
						xtype: 'datefield',
					    fieldLabel: "开始计费日",
					    id: 'dfStartDate',
					    format: 'Y-m-d',
					    allowBlank: false,
					    readOnly: true
					},{
						xtype: 'textfield',
						id: 'dfExpDate',
						readOnly: true,
						allowBlank: false,
			            fieldLabel: '结束计费日'
					}]
				}]
			},{
				region: "south",
				height: 40, 
				bodyStyle: 'background-color: rgb(213,225,241);padding: 10px 0 10px 30px; color: red',
				html: "* 实际应收$:<span id='totalAmount'>--</span>"
					+"（新增订购:<b id='addAmount'>--</b> "
					+" - "
					+" <a id='transferHrefTag' href='#'>转移支付:<b id='transferAmount'>--</b></a>"
					+" ）"
			}]
		});
	},
	// 加载产品，资费等数据
	doLoadBaseData: function(){
		this.busiCode = App.getData().currentResource.busicode;
		// 单用户订购
		if(this.busiCode === "102"){
			var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
			this.userId = users[0].get("user_id");
		// 续订和升级
		}else if(this.busiCode === "100" || this.busiCode === "101"){
			var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
			var prodData = prodGrid.selModel.getSelected().data;
			this.lastOrderSn  = prodData["order_sn"];
		}
		
		Ext.Ajax.request({
			url :root + '/core/x/ProdOrder!loadProdList.action',
			scope : this,
			params: {
				busi_code: this.busiCode,
				cust_id: App.getCustId(),
				user_id: this.userId,
				filter_order_sn: this.lastOrderSn
			},
			success : function(response,opts){
				var responseObj = Ext.decode(response.responseText);
				this.baseData = responseObj;
				Ext.getCmp("boxProdId").getStore().loadData(this.baseData["prodList"]);
			}
		});
	},
	doInit:function(){
		this.doLoadBaseData();
		ProdOrderForm.superclass.doInit.call(this);
		//this.on("render",function(){
		var that = this;
		setTimeout(function(){
			Ext.get("transferHrefTag").on("click", function(e, t, o){
				if(!that.transferPayWindow){
					that.transferPayWindow = new TransferPayWindow();
				}
				if(that.transferPayData && that.transferPayData.length > 0){
					that.transferPayWindow.show(that.transferPayData);
				}else{
					Alert("没有转移支付项目!");
				}
			});
		}, 2000);
		//}, this);
	},
	//产品下拉框选中事件
	doSelectProd: function(box, record, index){
		// 当前选中的产品
		this.currentSelectedProd = record;
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
		// 上期结束日期
		var lastOrderProdDate = this.getLastProdEndDate(prodId) || "--";
		Ext.get("lastOrderProdExtDate").update(lastOrderProdDate);
		
		// 开始计费日期 默认为当前时间
		var startDate = nowDate(); 
		// 有上期结束日并且当前不是升级业务
		if(lastOrderProdDate && this.busiCode != "100"){
			startDate.setDate(startDate.getDate() + 1);
		}
		Ext.getCmp("dfStartDate").setValue(startDate);
		// 结束计费日期
		this.doChangeEndDate();
		
		// 加载结算金额
		if(tariffs.length == 1){
			this.doLoadTransferAmount();
		}
		// 加载终端用户
		this.doLoadUsers(record);
	},
	// 加载终端用户
	doLoadUsers: function(selectProdRecord){
		// 单产品
		if(this.isPkg()){
			this.selectUserPanel.loadPackageUsers({
				prod_id: selectProdRecord.get("prod_id"),
				last_order_sn: this.lastOrderSn
			});
		}else{
			var user = this.baseData["userDesc"];
			this.selectUserPanel.loadSingleUser(user);
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
		if(this.isProtocolPkgAndUpgradeBusiCode()){
			sfOrderCycle.setMinValue(0);
		}else{
			sfOrderCycle.setMinValue(billingCycle);
			sfOrderCycle.setValue(billingCycle);
		}
		//最大值
		if(maxOrderCycle){
			sfOrderCycle.setMaxValue(maxOrderCycle*billingCycle);
		}else{
			sfOrderCycle.setMaxValue(null);
		}
		// 结算结束计费日
		this.doChangeEndDate();
		// 计算结算金额
		this.doLoadTransferAmount();
	},
	// 上期结束计费日
	getLastProdEndDate: function(prodId){
		var lastOrderProd = this.baseData["lastOrderMap"][prodId];
		return lastOrderProd ? lastOrderProd["exp_date"].split(" ")[0] : null;
	},
	// 结束日期
	doChangeEndDate: function(){
		var startDate = Ext.getCmp("dfStartDate").getValue();
		var months = Ext.getCmp("sfOrderCycle").getValue();
		//如果是协议套餐，并且当前周期为0，则结束日为上期结束计费日
		if(this.isProtocolPkgAndUpgradeBusiCode() && months == 0){
			var lastOrderProdDate = this.getLastProdEndDate(prodId);
			Ext.getCmp("dfExpDate").setValue(lastOrderProdDate);
			return;
		}
		// 其它根据周期进行计算
		if(startDate){
			// 计算结束日
			startDate.setMonth(startDate.getMonth() + months);
			Ext.getCmp("dfExpDate").setValue(startDate.format("Y-m-d"));
		}else{ 
			Ext.getCmp("dfExpDate").setValue(null);
		}
		
	},
	doLoadTransferAmount: function(){
		var validObj = ProdOrderForm.superclass.doValid.call(this); 
		if(validObj["isValid"] !== true){
			Alert(validObj["msg"]);
			return ;
		}
//		Ext.Ajax.request({
//			url :root + '/commons/x/Order.loadTransferFee.action',
//			scope : this,
//			params: this.getTransferValues(),
//			success : function(response,opts){
//				var responseObj = Ext.decode(response.responseText);
//				alert(response.responseText);
//			}
//		});
		
		var responseData = [{
			active_fee: 10
		},{
			active_fee: 20
		},{
			active_fee: 80
		}];
		
		//修改转移金额
		this.transferPayData = responseData;
		var sumAmount = 0;
		if(responseData){
			for(var i = 0; i< responseData.length; i++){
				sumAmount += responseData[i]["active_fee"];
			}
		}
		this.transferAmount = sumAmount;
		// 修改小计信息
		this.doChangeAmount();
	},
	// 实际应付
	totalAmount: -1,
	// 转移支付
	transferAmount: -1,
	// 新增订购
	addAmount: -1,
	//当前选中的产品
	currentSelectedProd: null,
	doChangeAmount: function(){
		//计算新增金额
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var disctId = boxProdTariff.getValue();
		var index = boxProdTariff.getStore().find("disct_id", disctId);
		var tariffRecord = boxProdTariff.getStore().getAt(index);
		this.addAmount = Ext.getCmp("sfOrderCycle").getValue() * tariffRecord.get("final_rent");
		Ext.get("addAmount").update(String(this.addAmount/100));
		// 实付
		this.totalAmount = this.addAmount - this.transferAmount;
		Ext.get("totalAmount").update(String(this.totalAmount/100));
		// 转移支付
		Ext.get("transferAmount").update(String(this.transferAmount/100));
	},
	// 是否为套餐
	isPkg: function(){
		if(this.currentSelectedProd){
			var prodType = this.currentSelectedProd.get("prod_type");
			return prodType === "SPKG" || prodType === "CPKG";
		}
		return false;
	},
	// 是否为协议套餐
	isProtocolPkgAndUpgradeBusiCode: function(){
		if(this.currentSelectedProd){
			var prodType = this.currentSelectedProd.get("prod_type");
			return prodType === "SPKG" && this.busiCode === "100";
		}
		return false;
	},
	doValid : function(){
		var validObj = ProdOrderForm.superclass.doValid.call(this); 
		if(validObj["isValid"] !== true){
			Alert(validObj["msg"]);
			return false;
		}
		var orderCycle = Ext.getCmp("sfOrderCycle").getValue();
		if(String(orderCycle).trim() === ""){
			return {
				isValid: false,
				msg: "订购月数是必须的!"
			}
		}
		
		if(this.totalAmount < 0){
			return {
				isValid: false,
				msg: "实际应付不能小于0，请增加订购月数"
			}
		}
		return true;
	},
	getValues : function(){
		var values = this.getTransferValues();
		values["order_months"] = Ext.getCmp("sfOrderCycle").getValue();
		
		// 实际支付金额（小计金额）
		values["pay_fee"] = this.totalAmount;
		// 转移支付
		values["transfer_fee"] = this.transferAmount;
		// 失效日期
		values["exp_date"] = Ext.getCmp("dfExpDate").getValue();
		
		return {
			"orderProd": Ext.encode(values)
		};
	},
	getTransferValues: function(){
		var values = {};
		
		// 基础信息
		values["cust_id"] = App.getCustId();
		values["user_id"] = this.userId;
		values["last_order_sn"] = this.lastOrderSn;
		
		//计算新增金额
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var disctId = boxProdTariff.getValue();
		var index = boxProdTariff.getStore().find("disct_id", disctId);
		
		// 如果当前是套餐，则封装分组用户
		if(this.isPkg()){
			// 获得选中的用户
			var groupUserMap = {};
			this.selectUserPanel.store.each(function(rs){
				var gid = rs.get("package_group_id");
				if(!groupUserMap[gid]){
					groupUserMap[gid] = [];
				}
				groupUserMap[gid].push(rs.get("user_id"));
			}, this);
			// 封装后台数据结构
			var groupSelected = [];
			for(var key in groupUserMap){
				groupSelected.push({
					"package_group_id": key,
					"userSelectList": groupUserMap[key]
				})
			}
			values["groupSelected"] = groupSelected;
		}
		
		// 产品信息
		values["prod_id"] = Ext.getCmp("boxProdId").getValue();
		values["tariff_id"] = Ext.getCmp("boxProdTariff").getValue();
		values["eff_date"] = Ext.getCmp("dfStartDate").getValue();
		
		return values;
	},
	getFee: function(){
		return this.totalAmount/100;
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
	TemplateFactory.gTemplate(new ProdOrderForm());
});
