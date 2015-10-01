//预付款
var FeeUnitpreGrid = Ext.extend(Ext.grid.GridPanel,{
	feeUnitpreStore:null,
	constructor:function(){
		this.feeUnitpreStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : {
				start:0,
				limit:Constant.DEFAULT_PAGE_SIZE
			},
			totalProperty:'totalProperty',
			root:'records',
			url:Constant.ROOT_PATH+"/core/x/Acct!queryGeneralContracts.action",
			fields:['contract_id','contract_no','cust_name','fee_id','fee_type','fee_type_text','g_acct_id','contract_name','nominal_amount',
			'used_money','payed_amount','total_amount','fee_sn','used_amount','left_amount','invoice_mode','addr_district','addr_community',
			'invoice_id','invoice_code','invoice_book_id','acct_date','real_pay','finance_status','optr_id','county_id','refund_amount','create_time']
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cols = lbc('home.tools.feeUnitpre.cols');
		var columns = [
			{header:cols[0],dataIndex:'fee_type_text',width:100},
			{header:cols[1],dataIndex:'contract_no',width:100,renderer : App.qtipValue},
			{header:cols[2],dataIndex:'contract_name',width:120},
			{header:cols[3],dataIndex:'cust_name',width:120},
			{header:cols[4],dataIndex:'nominal_amount',width:100,renderer:Ext.util.Format.formatFee},
//			{header:cols,dataIndex:'payed_amount',width:80,renderer:Ext.util.Format.formatFee},
			{header:cols[5],dataIndex:'invoice_id',width:120,renderer : App.qtipValue},
//			{header:cols[6],dataIndex:'invoice_code',width:60,renderer : App.qtipValue,hidden:true},
		    {   header: cols[7],
		        dataIndex: 'contract_id',
		        width:200,
		        scope:this,
	            renderer:function(value,meta,record,rowIndex,columnIndex,store){
	            	var btns = this.doFilterBtns(record);
//	            	var btns = "<a href='#' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doQueryDetail(); style='color:blue'>明细 </a>"
//	            	+"<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doPrint()>打印 </>"
//	            	+"<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doModify()>修改 </>";
	            	return btns;
				}
			}
		];
		FeeUnitpreGrid.superclass.constructor.call(this,{
			id:'feeUnitpreGridId',
			region:'center',
			ds:this.feeUnitpreStore,
			loadMask : true,
			sm : sm,
			tbar:[
				{text: lbc('common.add'),sope:this,handler:this.doAdd}
			],
			tbar : [' ',' ',lbc('home.tools.feeUnitpre.msg.enterKeywords'), ' ',       //搜索功能
				new Ext.ux.form.SearchField({  
	                store: this.feeUnitpreStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: lbc('home.tools.feeUnitpre.msg.custNameOrContractNoQuery')
	            }),'->','-',{
	        	text : lbc('common.add'),
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.doAdd
	        },'-',' '],
			columns:columns,
			bbar:new Ext.PagingToolbar({store: this.feeUnitpreStore ,pageSize : Constant.DEFAULT_PAGE_SIZE})
		});
		
	},
	doFilterBtns : function(record){
		//record.get('optr_id') == App.getApp().data.optr['optr_id'] &&
//		var btns = "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doModify()>修改 </>";
		var btns = "";
		
		//发票打印
		/*if(!record.get('invoice_id')){
			btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doPrint()>打印 </>";
		}else{//已打印
			if(record.get('invoice_mode')== 'A'){//机打票才需要重新打印
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doRePrint()>重打 </>";
			}
			
			//已付金额小于合同金额
			if(record.get('payed_amount') < record.get('nominal_amount')){
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doPay()>付款 </>";
			}
			
		}*/
		
		//合同款显示明细
//		if(record.get('fee_type') != 'UNBUSI'){
//			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doQueryDetail(); style='color:blue'>明细 </a>";
//		}
		
		//作废
		if(record.get('nominal_amount') > 0  && record.get('fee_sn')){
			if((record.get('fee_type') == 'UNBUSI' || !record.get('used_amount')) && nowDate().format('Y-m') == record.data['create_time'].substring(0,7)){//当月，非营业费或者预收款未被使用
				btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doDelete(); style='color:blue'>作废 </a>";
			}
		}
		
		//退款
		/*if(record.get('nominal_amount') > 0  && record.get('fee_sn')&&record.get('refund_amount')>0){
			if(record.get('fee_type') == 'UNBUSI' || !record.get('used_amount') ){//非营业费或者预收款未被使用
				btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"feeUnitpreGridId"+"\').doRefund(); style='color:blue'>退款 </a>";
			}
		}*/
		
		
		return btns;
	},
	doAdd:function(){
		App.getApp().data.currentResource = {busicode:'1085',
					url: 'pages/business/pay/FeeUnitpre.js'};
		App.getApp().menu.bigWindow.show({ text: '非营业收费',  attrs: {busicode:'1085',
					url: 'pages/business/pay/FeeUnitpre.js'}} ,
						{width: 600, height:300});
	},
	doModify : function(){
		var record = this.getSelectionModel().getSelected();
		var btns = [];
		//机打票不能修改出票方式
		if('A' != record.get('invoice_mode') && App.getData().busiTask['EditInvoiceMode']){
			btns.push(App.getData().busiTask['EditInvoiceMode']);
		}
		if(record.get('finance_status')!= 'CLOSE' && App.getData().busiTask['EditAcctDate']){
			btns.push(App.getData().busiTask['EditAcctDate']);
		}
		
//		btns.push(App.getData().busiTask['EditInvoice'])
		new ModifySelectWin(btns).show();
	},
	doDelete : function(){
		var record = this.getSelectionModel().getSelected();
		
		var ps={},all = {};
		ps['busiCode'] = '1167';
		all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
		all['contractId'] = record.get('contract_id');
		
		Confirm("确定作废吗?", this , function(){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH+"/core/x/Acct!saveRemoveContract.action",
				params : all,
				success : function(res,opt){
					App.getApp().main.feeUnitpreGrid.getStore().reload();
					Alert('操作成功');
				}
			})
		});
	},
	doRefund : function(){
		App.getApp().data.currentResource = {busicode:'1168',
					url: 'pages/business/pay/EditFeeUnitpre.js'};
		App.getApp().menu.bigWindow.show({ text: '退款',  attrs: {busicode:'1168',
					url: 'pages/business/pay/RefundUnBusiFee.js'}} ,
						{width: 500, height:320});
	},
	doPay : function(){
		App.getApp().data.currentResource = {busicode:'1169',
					url: 'pages/business/pay/EditFeeUnitpre.js'};
		App.getApp().menu.bigWindow.show({ text: '合同付款',  attrs: {busicode:'116',
					url: 'pages/business/pay/PayUnBusiFee.js'}} ,
						{width: 500, height:320});
	},
//	doAddCredential : function(){
//		App.getApp().data.currentResource = {busicode:'1086',
//					url: 'pages/business/pay/AddCredential.js'};
//		App.getApp().menu.bigWindow.show({ text: '添加凭据',  attrs: {busicode:'1086',
//					url: 'pages/business/pay/AddCredential.js'}} ,
//						{width: 500, height:320});
//	},
	doQueryCredential : function(){
		var contractId = this.getSelectionModel().getSelected().get('contract_id');
		new CredentialWin(contractId).show();
	},
	doQueryDetail :function(){
		var contract = this.getSelectionModel().getSelected().data;
		new ContractDetailWin(contract).show();
	},
	doPrint : function(){
		var feeSn = this.getSelectionModel().getSelected().get('fee_sn');
		App.getApp()['printid'] = feeSn;
		App.getApp().data.currentResource = {busicode:'1068'};//打印业务编号
 		App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'8888',
		url: 'pages/business/pay/Print.jsp?type=feesn&feesn='+feeSn}} ,{width: 710, height: 460});
	},
	doRePrint : function(){
		var feeSn = this.getSelectionModel().getSelected().get('fee_sn');
		App.getApp()['printid'] = feeSn;
		App.getApp().data.currentResource = {busicode:'1063'};//重打业务编号
 		App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'8888',
		url: 'pages/business/pay/Print.jsp?type=reprint'}} ,{width: 710, height: 460});
	}
});

ModifySelectWin = Ext.extend(Ext.Window,{
	width : null,
	constructor : function(params){
		var panel = this.createPanel(params);
		var style1 = 'background:#F9F9F9;padding : 20px';
		ModifySelectWin.superclass.constructor.call(this,{
			id : 'BusiSelectWindow',
			layout : 'fit',
			title : '请选择下一步操作',
			height : 250,
			width : 200,
			maximizable : false,
			closeAction : 'close',
			bodyStyle : style1,
			items : [panel]
		});
	},
	createPanel : function(params){
		var buttons =[];
        for(var i = 0, d; d = params[i]; i++){
        	var b = {};
        	Ext.apply(b,d);
        	b.height = 40;
        	b.width=100;
            b.handler = this.doSave;
            buttons.push(b);
        }
        buttons.push({
        	text : '修改合同',
        	height : 40,
        	width : 100,
        	handler : this.doSave
        })
		
		var Panel = new Ext.Panel({
			layout:'table',
		    defaultType: 'button',
		    baseCls: 'x-plain',
		    cls: 'btn-panel',
		    layoutConfig: { columns:1 },
		    autoScroll:true,
		    bodyStyle : Constant.TAB_STYLE,
			items : buttons
		});
		
		return Panel;
	},
	doSave : function(){
		Ext.getCmp('BusiSelectWindow').close();
		
		if(this.attrs){
			var t = this.attrs;
	    	//赋值当前的资源数据，以便在业务模块中使用。
	    	App.data.currentResource = t;
			var o = MenuHandler[t.handler].call();
	    	if ( o !== false ){
	    		if(!o.width || !o.height){
	    			Alert("校验函数"+ handler + "没有返回窗体的width、height属性!");
	    		}else{
	    			App.menu.bigWindow.show( this , o );
	    		}
	    	}
		}else{
			App.getApp().data.currentResource = {busicode:'1166',
					url: 'pages/business/pay/EditFeeUnitpre.js'};
			App.getApp().menu.bigWindow.show({ text: '修改',  attrs: {busicode:'1085',
					url: 'pages/business/pay/EditFeeUnitpre.js'}} ,
						{width: 500, height:320});
		}
	}
});

/**
 * 
 * @class CredentialGrid
 * @extends Ext.grid.GridPanel
 */
CredentialGrid = Ext.extend(Ext.grid.GridPanel,{
	credentialStore : null,
	constructor : function(contractId){
		this.credentialStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Acct!queryCredential.action",
			baseParams : {
				contractId : contractId
			},
			totalProperty:'totalProperty',
			root:'records',
			fields:['credential_no','amount','balance','percent']
		});
		this.credentialStore.load({
			params:{
				start:0,
				limit:Constant.DEFAULT_PAGE_SIZE
			}
		});
		
		var columns = [
			{header:'凭据号',dataIndex:'credential_no',renderer : App.qtipValue},
			{header:'凭据金额',dataIndex:'amount',renderer:Ext.util.Format.formatFee},
			{header:'剩余金额',dataIndex:'balance',renderer:Ext.util.Format.formatFee},
			{header:'赠送金额百分比',dataIndex:'percent'}
		];
		
		CredentialGrid.superclass.constructor.call(this,{
			border:false,
			ds : this.credentialStore,
			columns : columns,
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store: this.credentialStore ,pageSize : Constant.DEFAULT_PAGE_SIZE})
		})
	}
})

PayInfoGrid = Ext.extend(Ext.grid.GridPanel,{
	payInfoStore : null,contract:null,
	renderOperation:function(val,metadata,record,rowIndex,colIndex,store){
		var total = 0;
		that = this;
		return '<a href="#" onclick="that.modifyAcctDate();">修改账务日期</a>';
		
	},
	modifyAcctDate:function(){
		App.getData().currentResource = App.getData().busiTask['EditAcctDate'].attrs;
		var btn = App.getData().busiTask['EditAcctDate'];
		var o = {width : 540,height : 300};
		App.getApp().hirePurchaseGrid = this;
		App.menu.bigWindow.show(  btn , o );
	},
	constructor : function(contract){
		this.contract = contract;
		this.payInfoStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Acct!queryPayInfo.action",
			baseParams : {
				contractId : contract.contract_id
			},
			totalProperty:'totalProperty',
			root:'records',
			fields:['contract_id','fee','create_time','optr_name','optr_id','acct_date','fee_sn']
		});
		this.payInfoStore.load({
			params:{
				start:0,
				limit:Constant.DEFAULT_PAGE_SIZE
			}
		});
		
		var columns = [
			{header:'支付金额',dataIndex:'fee',renderer:Ext.util.Format.formatFee},
			{header:'账务日期',dataIndex:'acct_date'},
			{header:'操作时间',dataIndex:'create_time'},
			{header:'操作员',dataIndex:'optr_name'},
			{header:'操作',renderer:this.renderOperation,dataIndex:'fee_sn',scope:this}
		];
		
		PayInfoGrid.superclass.constructor.call(this,{
			border:false,
			ds : this.payInfoStore,
			columns : columns,
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store: this.payInfoStore ,pageSize : Constant.DEFAULT_PAGE_SIZE})
		})
	}
})

/**
 * 凭据查看窗口
 * @class CredentialWin
 * @extends Ext.Window
 */
CredentialWin = Ext.extend(Ext.Window, {
	grid : null,
	constructor : function(contractId){
		this.grid = new CredentialGrid(contractId);
		CredentialWin.superclass.constructor.call(this,{
			width: 600, 
			height:320,
			border:false,
			closeAction : 'close',
			layout : 'fit',
			defaults:{
				baseCls:'x-plain'
			},
			items:[this.grid]
		})
	}
})

/**
 * 合同明细
 * @class ContractDetailWin
 * @extends Ext.Window
 */
ContractDetailWin = Ext.extend(Ext.Window, {
	panel : null,
	payInfoGrid : null,
	credentialGrid : null,
	tabPanel : null,
	constructor : function(contract){
		if((contract.total_amount - contract.nominal_amount) < 0){
			contract.total_amount = contract.nominal_amount;
		}
		
		//付款明细
		this.payInfoGrid = new PayInfoGrid(contract);
			
		var itemArr = [];
		if(contract.fee_type == 'UNBUSI'){
			itemArr= [{
				title:'付款信息',
				region : 'center',
				layout: 'fit',
				border:false,
				items:[this.payInfoGrid]
			}]
		}else{
			this.contractTpl = new Ext.XTemplate(
				'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
					'<tr height=24>',
						'<td class="label" width=20%>合同号：</td>',
						'<td class="input_bold" width=30%>&nbsp;{contract_no}</td>',
						'<td class="label" width=20%>合同名称：</td>',
						'<td class="input_bold" width=30%>&nbsp;{contract_name}</td>',
					'</tr>',
					'<tr height=24>',
						'<td class="label" width=20%>合同总金额：</td>',
						'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.total_amount)]}</td>',
				      	'<td class="label" width=20%>赠送金额：</td>',
						'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee((values.total_amount - values.nominal_amount))]}</td>',
			    	'</tr>',
			    	'<tr height=24>',
			      		'<td class="label" width=20%>已使用总金额：</td>',
				      	'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.used_amount)]}</td>',	
			    		'<td class="label" width=20%>已支付金额：</td>',
			      		'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.refund_amount)]}</td>',
			    	'</tr>',
			    	'<tr height=24>',
			    		'<td class="label" width=20%>剩余总金额：</td>',
			      		'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.left_amount)]}</td>',
			    	'</tr>',
				'</table>'
			);
			this.contractTpl.compile();
			
			this.panel = new Ext.Panel({
				border : false,
				region : 'north',
				height : 150,
				bodyStyle: "background:#F9F9F9; padding: 10px;",
				html : this.contractTpl.applyTemplate(contract)
			})
			
			this.credentialGrid = new CredentialGrid(contract.contract_id);
			
			
			
			this.tabPanel = new Ext.TabPanel({
				region : 'center',
				activeTab: 0,
				border: false,
				defaults: {
					layout: 'fit',
					border:false
				},
				items : [{
					title:'凭据信息',
					items:[this.credentialGrid]
				},{
					title:'付款信息',
					items:[this.payInfoGrid]
				}]
			})
			itemArr = [this.panel,this.tabPanel];
		}
		
		
		ContractDetailWin.superclass.constructor.call(this,{
			title:'合同信息',
			width: 600, 
			height:450,
			border:false,
			closeAction : 'close',
			layout : 'border',
			items:itemArr
		})
	}
})

var FeeUnitpreWin = Ext.extend(Ext.Window,{
	grid:null,
	constructor:function(){
		this.grid = new FeeUnitpreGrid();
		App.getApp().main.feeUnitpreGrid = this.grid;
		FeeUnitpreWin.superclass.constructor.call(this,{
			id:'feeUnitpreWinId',
			closeAction : 'close',
			title: lbc('home.tools.feeUnitpre._title'),
			width:850,
			height : 450,
			border:false,
			layout : 'fit',
			defaults:{
				baseCls:'x-plain'
			},
			items:[this.grid]
		});
	}
});

