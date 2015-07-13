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
	sm: null,
	constructor: function(){
		
		// 缴费信息的表单
		this.payForm = new PayForm();
		
		//实例化fee store
		this.feeStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH+"/core/x/Pay!queryUnPay.action",
			fields: [
				'fee_sn','fee_text','fee_type','fee_type_text','busi_name','optr_name',
				{name: 'real_pay', type: 'int'},
				'optr_id', 'create_time'
			]
		});
		p = this.payForm ;
		this.sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect: false,
			checkOnly: true,
			//该函数是为了解决 ext CheckboxSelectionModel的一个BUG
			onBeforeSelAllHanlder: function(){
				p.resetAmount();
			}
		});
		current = this;
		//处理更多详细数据的函数
		more = function(rowIndex ){
			var r = current.feeStore.getAt(rowIndex);
			current.loadMoreInfo(r);
		}
		//费用表格
		var feeGrid = new Ext.grid.GridPanel({
			title: '选择支付的费用项',
			region: 'center',
			border: false,
			autoExpandColumn: 'e',
			columns: [
				this.sm,
				{ id: 'e', header: '费用名称', dataIndex: 'fee_text'},
				{ header: '费用类型', dataIndex: 'fee_type_text', width: 100},
				{ header: '业务名称', dataIndex: 'busi_name', width: 100},
				{ header: '操作员', dataIndex: 'optr_name', width: 100},
				{ header: '实付金额', dataIndex: 'real_pay', width: 100, xtype: 'moneycolumn'},
				{ header: '费用时间', dataIndex: 'create_time', width: 140}
//				,
//				{ header: '查看详细' , dataIndex: '' , width: 60, renderer: function(v , md, record , i  ){
//					return "<DIV><a href='#' onclick='more("+ i +");'>更多...</a></DIV>";
//				}}
			],
			sm: this.sm,
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
		
		//加载数据
		this.feeStore.load({
			params: {custId: App.getData().custFullInfo.cust.cust_id }
		});
		
		PayPanel.superclass.constructor.call(this,{
			border: false,
			layout: 'border',
			region: 'center',
			items: [
				{
				border: false,
				region: 'center',
				layout: 'border',
				items: [feeGrid ]
			},
				{
				region: 'south',
				border: false,
				layout: 'fit',
				height: 98,
				items: this.payForm
			}],
			buttons: [{
				text: '重置信息',
				width: 100,
				scope: this,
				handler: this.doReset
			},
//			{
//				text: '保存后合并',
//				width: 100,
//				scope: this,
//				handler: function(){ 
//					Confirm("确定要保存信息吗?",this,this.doSaveAndMerge);
//				}
//			},
			{
				text: '保存后打印',
				width: 100,
				scope: this,
				handler: function(){ 
					Confirm("确定要保存信息吗?",this, this.doSaveAndPrint);
				}
			}]
		});
	},
	initEvents: function(){
		this.sm.on("rowselect", function(sm , rowIndex , record ){
			this.payForm.setTotalValue( record.get("real_pay") );
		}, this );
		this.sm.on("rowdeselect", function(sm , rowIndex , record ){
			this.payForm.setTotalValue( -record.get("real_pay") );
		}, this );
		PayPanel.superclass.initEvents.call(this);
	},
	/**
	 * 加载更多信息
	 */ 
	loadMoreInfo: function(r){
		var feeSn = r.get('fee_sn');
		var feeType = r.get('fee_type');
		//提交 
		Ext.Ajax.request({
			scope: this,
			params: {feeSn: feeSn , feeType: feeType},
			url: Constant.ROOT_PATH+"/core/x/Pay!queryFeeInfo.action",
			success: function( res, ops){
				var o = Ext.decode( res.responseText );
				if(o["exception"]){
					new DebugWindow( o , "btnBusiSave").show();
				} else {
					o["fee_name"] = r.get('fee_text');
					MoreInfoTemplate[feeType].overwrite( this.infoPanel.body, o );
				}
			}
		});
	},
	doReset: function(){ 
		this.payForm.doReset();
		this.sm.selectAll();
	},
	doSaveAndMerge: function(){
		var params = this.getValues();
		params['notMerge']= true;
		this.doSave(function(){
			var menu = App.getApp().menu;
			menu.bigWindow.toggleBusi( menu.MERGE_FEE_RESOURCE , {width: 700, height: 450} );
		},params);
	},
	doSaveAndPrint: function(){
		var params = this.getValues();
		this.doSave(params);
//		this.doSave(function(){
//			var invoiceId =this.payForm.find("name", "pay.invoice_id")[0].getValue();
//			var invoiceBookId =this.payForm.find("hiddenName", "pay.invoice_book_id")[0].getValue();
//			var menu = App.getApp().menu;
//			menu.bigWindow.toggleUrl( "打印" , {width: 700, height: 450},
//					"/pages/business/pay/Print.jsp?type=through&invoiceId="+invoiceId+"&invoiceBookId="+invoiceBookId );
//		}, params);
	},
	doSave: function(params,callback){
		if(!this.doValid()) return ;
		var comomonParams = App.getValues();
		comomonParams["busiCode"] = "1207";
		params[CoreConstant.JSON_PARAMS] = Ext.encode(comomonParams);
		var mb = Show();
		//提交
		Ext.Ajax.request({
			scope: this,
			params: params,
			url: Constant.ROOT_PATH+"/core/x/Pay!savePay.action",
			success: function( res, ops){
				var o = Ext.decode( res.responseText );
				mb.hide();
				if(o["exception"]){
					new DebugWindow( o , "btnBusiSave").show();
				} else {
					Alert('支付成功!',callback ,this);
					//App.getApp().refreshFeeView();
					var panel = App.getApp().main.infoPanel;
					panel.getUserPanel().prodGrid.remoteRefresh();
					panel.getAcctPanel().acctGrid.remoteRefresh();
				}
			}
		});
	},
	doValid: function(){
		var f = this.payForm.getForm();
		if(!f.isValid())
			return false;
		if(this.sm.getSelections().length == 0 ){
			Alert("没有可以支付的费用项!");
			return false;
		}
		return true;
	},
	getValues: function(){
		var f = this.payForm.getForm();
		var all = f.getValues();
		//获得选中的记录
		var feeSn = [];
		for(var i =0,rsArr = this.sm.getSelections();i< rsArr.length ;i++){
			feeSn[i] = rsArr[i].get("fee_sn");
		}
		all["feeSn"] = feeSn;
		return all;
	},
	getFee: function(){
		return 0;
	}
});

Ext.onReady(function(){
	var pay = new PayPanel();
	TemplateFactory.gViewport(pay);
});