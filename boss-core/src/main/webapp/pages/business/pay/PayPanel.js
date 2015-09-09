/**
 * 封装支付面板
 */ 
PayPanel = Ext.extend( Ext.Panel ,{
	feeStore: null ,
	payForm: null,
	feeGrid:null,
	constructor: function(){
		var payPanelThis = this;
		// 缴费信息的表单
		this.payForm = new PayForm(this);
		//实例化fee store
		this.feeStore = new Ext.data.JsonStore({
			data: [],
			fields: [
				'fee_sn','busi_name','fee_text','count_text','optr_name','create_time',
				{name: 'real_pay', type: 'int'}, 'create_done_code',
				'prod_sn', 'begin_date','prod_invalid_date','fee_type','buy_num'
			]
		});
		var lc = langUtils.main("cashPay.pay.columns");
		var bt = langUtils.main("cashPay.pay.buttons");
		//费用表格
		this.feeGrid = new Ext.grid.GridPanel({
			title: langUtils.main('cashPay.pay._title'),
			region: 'center',
			border: false,
			columns: [
			    { header: lc[0], width: 50,renderer: function(v , md, record , i  ){
					return "<DIV><a href='#' onclick='payPanelThis.deletePay();'>取消</a></DIV>";
				}},
				{ header: lc[1], dataIndex: 'busi_name', width: 80},
				{ header: lc[2], dataIndex: 'fee_text',width: 120, renderer:App.qtipValue},
				{ header: lc[3], dataIndex: 'real_pay', width: 70, xtype: 'moneycolumn'},
				{ header: lc[4], dataIndex: 'prod_sn', width: 70},
				{ header: lc[5], dataIndex: 'count_text', width:150, renderer: App.qtipValue},
				{ header: lc[6], dataIndex: 'buy_num', width: 70},
				{ header: lc[7], dataIndex: 'create_time', width: 130},
				{ header: lc[8], dataIndex: 'fee_sn', width: 80},
				{ header: lc[9], dataIndex: 'create_done_code', width: 80}
				
			],
			ds: this.feeStore
		});
		
		PayPanel.superclass.constructor.call(this,{
			border: false,
			layout: 'border',
			region: 'center',
			items: [
				{
				region: 'center',
				layout: 'border',
				items: [this.feeGrid ]
			},
				{
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
			},{
				text: bt[1],
				width: 100,
				iconCls: 'icon-close',
				scope: this,
				handler: this.doSave
			}]
		});
		this.loadBaseData();
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
					if(!flag){
						this.loadBaseData();
						App.getApp().refreshPayInfo(parent);
					}
				}
			});
			return info;
		}
		Confirm( func(true), this ,function(){
			func(false);
		});
	},
	feeData: null,
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
				var dollar = this.feeData["FEE"]/100.0;
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
				Ext.getCmp("hdExchange").setValue(this.feeData["EXC"]);
			}
		});
	},
	doSaveAndPrint: function(){
//		var params = this.getValues();
		this.doSave(params);
//		this.doSave(function(){
//			var invoiceId =this.payForm.find("name", "pay.invoice_id")[0].getValue();
//			var invoiceBookId =this.payForm.find("hiddenName", "pay.invoice_book_id")[0].getValue();
//			var menu = App.getApp().menu;
//			menu.bigWindow.toggleUrl( "打印" , {width: 700, height: 450},
//					"/pages/business/pay/Print.jsp?type=through&invoiceId="+invoiceId+"&invoiceBookId="+invoiceBookId );
//		}, params);
	},
	doSave: function(){
		if(!this.payForm.getForm().isValid()){ 
			Alert("含有验证不通过的输入项");
			return ;
		}
		Confirm("确定要保存信息吗?", this, function(){
			var params = this.getValues()
			var comomonParams = App.getValues();
			comomonParams["busiCode"] = "1207";
			params[CoreConstant.JSON_PARAMS] = Ext.encode(comomonParams);
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
						Alert('支付成功!',function(){
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
		App.getApp().menu.bigWindow.show({ text: '发票打印',  attrs: {busiCode:'1068',
					url: 'pages/business/pay/Print.jsp?type=through'}} ,{width: 710, height: 460});
	}
});

Ext.onReady(function(){
	var pay = new PayPanel();
	TemplateFactory.gViewport(pay);
});