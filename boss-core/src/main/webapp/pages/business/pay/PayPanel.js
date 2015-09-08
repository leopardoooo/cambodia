BusiTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=13%>数量：</td>',
			'<td class="input_bold" width=23%>&nbsp;{count}</td>',
			'<td class="label" width=13%>实付：</td>',
			'<td class="input_bold" width=23%>&nbsp;{real_pay}</td>',			
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=13%>优惠类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{disct_type}</td>',
			'<td class="label" width=13%>优惠信息：</td>',
			'<td class="input_bold" width=23%>&nbsp;{disct_info}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=13%>部门：</td>',
			'<td class="input_bold" width=23%>&nbsp;{dept_id}</td>',
			'<td class="label" width=13%>操作员：</td>',
			'<td class="input_bold" width=23%>&nbsp;{optr_id}</td>',
			'<td class="label" width=13%>创建时间：</td>',
			'<td class="input_bold" width=23%>&nbsp;{create_time}</td>',
		'</tr>',
	'</table>'
);
DeviceTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=13%>设备类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{device_type}</td>',
			'<td class="label" width=13%>设备型号：</td>',
			'<td class="input_bold" width=23%>&nbsp;{device_model}</td>',
			'<td class="label" width=13%>应付：</td>',
			'<td class="input_bold" width=23%>&nbsp;{should_pay}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=13%>部门：</td>',
			'<td class="input_bold" width=23%>&nbsp;{dept_id}</td>',
			'<td class="label" width=13%>操作员：</td>',
			'<td class="input_bold" width=23%>&nbsp;{optr_id}</td>',
			'<td class="label" width=13%>创建时间：</td>',
			'<td class="input_bold" width=23%>&nbsp;{create_time}</td>',
		'</tr>',
	'</table>'
);
AcctTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=13%>用户类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{user_type}</td>',
			'<td class="label" width=13%>业务类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{serv_type}</td>',
			'<td class="label" width=13%>终端类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{terminal_type}</td>',
		'</tr>',		
		'<tr height=24>',
			'<td class="label" width=13%>实付：</td>',
			'<td class="input_bold" width=23%>&nbsp;{real_pay}</td>',
			'<td class="label" width=13%>优惠类型：</td>',
			'<td class="input_bold" width=23%>&nbsp;{disct_type}</td>',
			'<td class="label" width=13%>优惠信息：</td>',
			'<td class="input_bold" width=23%>&nbsp;{disct_info}</td>',
		'</tr>',		
		'<tr height=24>',
			'<td class="label" width=13%>部门：</td>',
			'<td class="input_bold" width=23%>&nbsp;{dept_id}</td>',		
			'<td class="label" width=13%>操作员：</td>',
			'<td class="input_bold" width=23%>&nbsp;{optr_id}</td>',
			'<td class="label" width=13%>创建时间：</td>',
			'<td class="input_bold" width=23%>&nbsp;{create_time}</td>',
		'</tr>',
	'</table>'
);


MoreInfoTemplate = {
	"BUSI": BusiTemplate,
	"DEVICE": DeviceTemplate,
	"ACCT": AcctTemplate
}

/**
 * 封装支付面板
 */ 
PayPanel = Ext.extend( Ext.Panel ,{
	feeStore: null ,
	payForm: null,
	feeGrid:null,
	constructor: function(){
		payPanelThis = this;
		// 缴费信息的表单
		this.payForm = new PayForm(this);
		//实例化fee store
		this.feeStore = new Ext.data.JsonStore({
			data: [],
			fields: [
				'fee_sn','busi_name','fee_text','count_text','optr_name','create_time',
				{name: 'real_pay', type: 'int'},
				'prod_sn', 'begin_date','prod_invalid_date','fee_type'
			]
		});
		p = this.payForm ;
		//费用表格
		this.feeGrid = new Ext.grid.GridPanel({
			title: '支付项目',
			region: 'center',
			border: false,
			autoExpandColumn: 'e',
			columns: [
			    { header: '操作', width: 50,renderer: function(v , md, record , i  ){
					return "<DIV><a href='#' onclick='payPanelThis.deletePay();'>取消</a></DIV>";
				}},
				{ header: '业务名称', dataIndex: 'busi_name', width: 60},
				{ id: 'e', header: '费用名称', dataIndex: 'fee_text',width: 100},
				{ header: '实付金额', dataIndex: 'real_pay', width: 60, xtype: 'moneycolumn'},
				{ header: '订单号', dataIndex: 'prod_sn', width: 60},
				{ header: '操作时间', dataIndex: 'create_time', width: 80},
				{ header: '数量', dataIndex: 'count_text'}
				
			],
			ds: this.feeStore
		});
		this.infoPanel = new Ext.Panel({
			region: 'south',
			height: 130,
			border: false,
			split: true,
			bodyStyle: 'padding:8px;padding-bottom:0px;',
			autoScroll: true,
			html: '请点击<更多...>查看更多费用信息!'
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
				text: '保存',
				width: 100,
				scope: this,
				handler: this.doSave
			}]
		});
		this.loadBaseData();
	},
	deletePay : function() {
		var rec = this.feeGrid.getSelectionModel().getSelected();
		Confirm("确定要取消吗?", this ,function(){
			var comomonParams = App.getValues();
			comomonParams["busiCode"] = "1113";
			var params ={};
			params[CoreConstant.JSON_PARAMS] = Ext.encode(comomonParams);
			Ext.apply(params,{
				fee_sn : rec.get('fee_sn'),
				fee_type : rec.get('fee_type'),
				onlyShowInfo : false
			});
					// 请求后台的数据
			Ext.Ajax.request({
				scope: this,
				url: Constant.ROOT_PATH + "/core/x/Pay!cancelUnPayFee.action",
				params: params,
				success: function(res, ops){
					var data = Ext.decode(res.responseText);
					this.loadBaseData();
					Alert(data);
					App.getApp().refreshPayInfo(parent);
				}
			});
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