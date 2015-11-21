/**
 * 封装支付面板
 */ 
PayPanel = Ext.extend( Ext.Panel ,{
	feeStore: null ,
	payForm: null,
	feeGrid:null,
	feeData: null,
	constructor: function(){
		// 缴费信息的表单
		this.payForm = new PayForm(this);
		//实例化fee store
		var fields = [
				'fee_sn','busi_name','fee_text','count_text','optr_name','create_time',
				{name: 'real_pay', type: 'int'}, 'create_done_code',
				'prod_sn', 'begin_date','prod_invalid_date','fee_type','buy_num', 
				'user_id','acctitem_id','user_type','busi_code'
			]
		this.feeStore = new Ext.data.JsonStore({
			data: [],
			fields: fields
		});
		var lc = langUtils.main("cashPay.pay.columns");
		var bt = langUtils.main("cashPay.pay.buttons");
		this.paySm = new Ext.grid.CheckboxSelectionModel();
		//费用表格
		var cancelText = lbc('common.cancel');
		this.feeGrid = new Ext.grid.GridPanel({
			title: langUtils.main('cashPay.pay._stayTitle'),
			region: 'center',
			border: false,
			sm: this.paySm,
			columns: [
				this.paySm,
			    { header: lc[0], width: 50,renderer: function(v , md, record , i  ){
					return "<DIV><a href='#' onclick=Ext.getCmp('PayPanelId').deletePay()>"+cancelText+"</a></DIV>";
				}},
				{ header: lc[1], dataIndex: 'busi_name', width: 80},
				{ header: lc[2], dataIndex: 'fee_text',width: 120, renderer:App.qtipValue},
				{ header: lc[3], dataIndex: 'real_pay', width: 70, xtype: 'moneycolumn'},
				{ header: lc[4], dataIndex: 'prod_sn', width: 70},
				{ header: lc[5], dataIndex: 'count_text', width:150, renderer: App.qtipValue},
				{ header: lc[6], dataIndex: 'buy_num', width: 70},
				{ header: lc[7], dataIndex: 'create_time', width: 130},
				{ header: lc[8], dataIndex: 'fee_sn', width: 80},
				{ header: lc[9], dataIndex: 'create_done_code', width: 80},
				{ header: lc[9], dataIndex: 'optr_name', width: 80}
				
			],
			ds: this.feeStore,
			listeners: {
				scope: this,
				rowdblclick: this.doRowClick
			}
		});
		
		this.realPaySm = new Ext.grid.CheckboxSelectionModel();
		this.realFeeStore = new Ext.data.JsonStore({
			fields: fields
		});
		this.realFeeGrid = new Ext.grid.GridPanel({
			title: langUtils.main('cashPay.pay._title'),
			border: false,
			sm: this.realPaySm,
			columns: [
				this.realPaySm,
				{ header: lc[1], dataIndex: 'busi_name', width: 80},
				{ header: lc[2], dataIndex: 'fee_text',width: 120, renderer:App.qtipValue},
				{ header: lc[3], dataIndex: 'real_pay', width: 70, xtype: 'moneycolumn'},
				{ header: lc[4], dataIndex: 'prod_sn', width: 70},
				{ header: lc[5], dataIndex: 'count_text', width:150, renderer: App.qtipValue},
				{ header: lc[6], dataIndex: 'buy_num', width: 70},
				{ header: lc[7], dataIndex: 'create_time', width: 130},
				{ header: lc[8], dataIndex: 'fee_sn', width: 80},
				{ header: lc[9], dataIndex: 'create_done_code', width: 80},
				{ header: lc[9], dataIndex: 'optr_name', width: 80}
				
			],
			ds: this.realFeeStore,
			listeners: {
				scope: this,
				rowdblclick: this.doRealRowClick
			}
		});
		
		PayPanel.superclass.constructor.call(this,{
			id:'PayPanelId',
			border: false,
			layout: 'border',
			items: [
			
				{
					region: 'center',
					layout: 'border',
					border: false,
					items:[{
						region: 'center',
						layout: 'border',
						items: [this.feeGrid ],
						bbar:['-',
							{text: lbc('common.confirm'), iconCls: 'icon-confirm', scope:this, handler: this.doConfirm},'-',
							{text: lbc('common.remove'), iconCls: 'icon-del', scope:this, handler: this.doDelete},'-']
					},
					{
						region: 'south',
						layout: 'fit',
						height: 200,
						items: [this.realFeeGrid ]
					}]
				},{
				region: 'east',
				split: true,
				layout: 'fit',
				width: 230,
				items: this.payForm
			}],
			buttons: [{
				text: bt[0],
				iconCls: 'icon-save',
				width: 100,
				scope: this,
				handler: this.doSave
			}]
		});
		this.loadBaseData();
	},
	loadBaseData: function(){
		// 请求后台的数据
		Ext.Ajax.request({
			scope: this,
			url: Constant.ROOT_PATH + "/core/x/Pay!queryUnPayDetail.action",
			params: {cust_id: App.getCustId()},
			success: function(res, ops){
				var data = Ext.decode(res.responseText);
				this.feeStore.loadData(data["records"]);
				this.feeData = data["simpleObj"];
				Ext.getCmp("labelExchange").setText("1USD=" + this.feeData["EXC"] + "KHR");
				/*var dollar = this.feeData["FEE"]/100.0;
				Ext.getCmp("nfDollar").setValue(dollar);
				Ext.getCmp("nfJianYuan").setValue(0);
				var substrIndex = 1, append = "";
				if(dollar < 0){
					substrIndex = 2;
					append = "-";
				} 
				Ext.getCmp("labelDollor").setText(append+ Ext.util.Format.usMoney(dollar).substr(substrIndex));
				var jianYuan = Ext.util.Format.usMoney(dollar * this.feeData["EXC"]);
				Ext.getCmp("LabelJian").setText(append+jianYuan.substr(substrIndex));
				Ext.getCmp("labelExchange").setText("1USD=" + this.feeData["EXC"] + "KHR");
				Ext.getCmp("hdExchange").setValue(this.feeData["EXC"]);*/
			}
		});
	},
	doConfirm: function(){
		var records = this.paySm.getSelections();
		if(records.length > 0){
			if(records.length == this.feeStore.getCount()){
				this.feeStore.removeAll();
				this.realFeeStore.add(records);
			}else{
				this.realFeeStore.add(records);
				this.feeStore.remove(records);
//				var recordArray = this.getSameTypeProd(this.feeStore, records);
//				this.realFeeStore.add(recordArray);
//				this.feeStore.remove(recordArray);
			}
				this.doCalFee();
		}
	},
	doDelete: function(){
		var records = this.realPaySm.getSelections();
		if(records.length > 0){
			if(records.length == this.realFeeStore.getCount()){
				this.realFeeStore.removeAll();
				this.feeStore.add(records);
			}else{
				this.realFeeStore.add(records);
				this.feeStore.remove(records);
//				var recordArray = this.getSameTypeProd(this.realFeeStore, records);
//				this.realFeeStore.remove(recordArray);
//				this.feeStore.add(recordArray);
			}
				this.doCalFee();
		}
	},
	doRowClick: function(grid, rowIndex){
		var record = this.feeStore.getAt(rowIndex);
		if(this.realFeeStore.indexOf(record) > 0)
			return;
		//取消一起拖动
		var recordArray = [];
		recordArray.push(record)			
//		var recordArray = this.getSameTypeProd(this.feeStore, record);
		this.feeStore.remove(recordArray);
		this.realFeeStore.add(recordArray);
		this.doCalFee();
	},
	doRealRowClick: function(grid, rowIndex){
		var record = this.realFeeStore.getAt(rowIndex);
		if(this.feeStore.indexOf(record) > 0)
			return;
		var recordArray = [];
		recordArray.push(record)
//		var recordArray = this.getSameTypeProd(this.realFeeStore, record);
		this.realFeeStore.remove(recordArray);
		this.feeStore.add(recordArray);
		this.doCalFee();
	},
	doCalFee: function(){
		var money = 0;
		this.realFeeStore.each(function(record){
			money += record.get('real_pay');
		}, this);
		money = money / 100.0;
		
		var arr = money.toString().split('.'), jy = 0, dollar = 0;
		if(arr.length > 1){
			dollar = parseInt(arr[0]);
			jy = parseFloat('0.'+arr[1]);
			jy = money >= 0 ? jy : jy*-1;
		}else{
			dollar = money;
		}
		
		Ext.getCmp("nfDollar").setValue(dollar);
		Ext.getCmp("nfJianYuan").setValue(Ext.util.Format.round(jy * this.feeData["EXC"], 2));

		Ext.getCmp("labelDollor").setText(dollar+jy);
		var jianYuan = Ext.util.Format.usMoney(dollar * this.feeData["EXC"]);
		Ext.getCmp("LabelJian").setText(jianYuan);
		Ext.getCmp("labelExchange").setText("1USD=" + this.feeData["EXC"] + "KHR");
		Ext.getCmp("hdExchange").setValue(this.feeData["EXC"]);
	},
	getSameTypeProd: function(store, records){
		var recordArray = [];
		Ext.each(records, function(record){
			var userType = record.get('user_type');
			var prodId = record.get('acctitem_id');
			var userId = record.get('user_id')
			var feeSn = record.get('fee_sn');
			var busiCode = record.get('busi_code');
			if(userType == 'BAND' && prodId){
				//相同用户的宽带订单必须一起支付
				store.each(function(r){
					if(userId == r.get('user_id')){
						recordArray.push(r);
					}
				}, this);
			}else if(busiCode == '1015'){
				//套餐订单，必须一起支付
				store.each(function(r){
					if(r.get('busi_code') == busiCode){
						recordArray.push(r);
					}
				}, this);
			}else if(prodId){
				//相同用户相同产品必须一起支付
				store.each(function(r){
					if(userId == r.get('user_id') && prodId == r.get('acctitem_id')){
						recordArray.push(r);
					}
				}, this);
			}else{
				recordArray.push(record);
			}
		}, this);
		
		return recordArray;
	},
	deletePay : function() {
		var rec = this.feeGrid.getSelectionModel().getSelected();
		var thiz = this;
		var func = function(flag){
			var comomonParams = App.getValues();
			comomonParams["busiCode"] = "1113";
			var params ={};
			params[CoreConstant.JSON_PARAMS] = Ext.encode(comomonParams);
			Ext.apply(params,{
				fee_sn : rec.get('fee_sn'),
				fee_type : rec.get('fee_type'),
				onlyShowInfo : flag
			});
			var info = "";
			Ext.Ajax.request({
				scope: thiz,
				url: Constant.ROOT_PATH + "/core/x/Pay!cancelUnPayFee.action",
				params: params,
				async : false,
				success: function(res, ops){
					info = Ext.decode(res.responseText);
					
					if(info['success']== false &&info['exception']){
//						info = info['exception']['content'];
					}else{
						if(!flag){
							this.loadBaseData();
							App.getApp().refreshPayInfo(parent);
							App.getApp().refreshPanel('1113');
							this.realFeeStore.each(function(record){
								if(record.get('fee_sn') == rec.get('fee_sn')){
									this.realFeeStore.remove(record);
									return false;
								}
							},this);
						}
					}
				}
			});
			return info;
		}
		
		var msg = func(true);
		if(msg['success']== false && msg['exception']){
			Alert(msg['exception']['content']);
			return false;
		}
		Confirm( msg, this ,function(){
			func(false);
		});
	},
	doSaveAndPrint: function(){
		this.doSave(params);
	},
	doSave: function(){
		if(!this.payForm.getForm().isValid()){ 
			Alert(lbc("common.tipFormInvalid"));
			return ;
		}
		if(this.realFeeStore.getCount() == 0){
			Alert(lmsg('ChooseToPayTheRecord'));
			return;
		}
		Confirm(lbc("common.confirmSaveData"), this, function(){
			var params = this.getValues()
			var comomonParams = App.getValues();
			comomonParams["busiCode"] = "1207";
			params[CoreConstant.JSON_PARAMS] = Ext.encode(comomonParams);
			
			var feeSns = [];
			this.realFeeStore.each(function(record){
				feeSns.push(record.get('fee_sn'));
			},this);
			params['feeSn'] = feeSns;
			
			var mb = Show();
			//提交
			Ext.Ajax.request({
				scope: this,
				params: params,
				url: Constant.ROOT_PATH + "/core/x/Pay!savePayNew.action",
				success: function( res, ops){
					var o = Ext.decode( res.responseText );
					mb.hide();
					if(o["exception"]){
						new DebugWindow( o , "btnBusiSave").show();
					} else {
						Alert(lmsg('paymentSuccess'),function(){
								App.getApp().refreshPayInfo(parent);
								this.success();
							},this);
					}
				}
			});
		});
	},
	getValues: function(){
		var f = this.payForm.getForm();
		var all = f.getValues();
		all["pay.khr"] = all["pay.khr"] * 100;
		all["pay.usd"] = all["pay.usd"] * 100; 
		all["pay.cust_id"] = App.getApp().data.custFullInfo.cust.cust_id;
		return all;
	},
	getFee: function(){
		return 0;
	},
	success:function(){
		App.getApp().main.infoPanel.getUserPanel().refresh();
		App.getApp().main.infoPanel.getPayfeePanel().refresh();
		App.getApp().data.currentResource = {busicode:'1068'};//打印业务编号
		App.getApp().menu.bigWindow.show({ text: lbc('common.invoicePrint'),  attrs: {busiCode:'8888',
					url: 'pages/business/pay/Print.jsp?type=through'}} ,{width: 710, height: 460});
	}
});

Ext.onReady(function(){
	var pay = new PayPanel();
	TemplateFactory.gViewport(pay);
});