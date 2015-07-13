/**
 * 发票管理
 * CheckInInvoice.js 发票入库
 * TransferInvoice.js 发票调拨
 * CancelInvoice.js 发票核销
 * CloseInvoice.js 发票结账(在营业系统中TopToolbar上)
 * @param {} invoiceCode
 */

//发票明细renderer方法
var doDetail = function(invoiceCode){
	var win = Ext.getCmp('invoiceWinId');
	if(!win){
		win = new InvoiceWin();
	}
	win.show();
	win.invoiceGrid.getStore().load({params:{invoiceCode:invoiceCode}});
}

//发票明细grid
var InvoiceGrid = Ext.extend(Ext.grid.GridPanel,{
	invoiceStore:null,
	constructor:function(){
		this.invoiceStore = new Ext.data.JsonStore({
			url:'resource/Invoice!queryInvoiceByInvoiceCode.action',
			fields:['invoice_id','invoice_code','invoice_type','invoice_mode','status','amount','optr_id',	
					'dept_id','county_id','status_text','optr_name','invoice_type_text']
		});
		var columns = [
			{header:'发票号码',dataIndex:'invoice_id',width:75},
			{header:'发票代码',dataIndex:'invoice_code',width:75},
			{header:'发票类型',dataIndex:'invoice_type_text',width:75},
			{header:'状态',dataIndex:'status_text',width:75},
			{header:'金额',dataIndex:'amount',width:75},
			{header:'发票方式',dataIndex:'invoice_mode',width:75}
		];
		InvoiceGrid.superclass.constructor.call(this,{
			id:'invoiceGridId',
			border:false,
			columns:columns,
			ds:this.invoiceStore
		});
	}
});

//发票明细Window
var InvoiceWin = Ext.extend(Ext.Window,{
	invoiceGrid:null,
	constructor:function(){
		this.invoiceGrid = new InvoiceGrid();
		InvoiceWin.superclass.constructor.call(this,{
			id:'invoiceWinId',
			title:'发票明细',
			layout:'fit',
			width:490,
			height:350,
			items:[this.invoiceGrid]
		});
	}
});

//发票核销renderer方法
var doCancel = function(value){
	var win = Ext.getCmp('cancelInvoiceWinId');
	if(!win)
		win = new CancelInvoiceWin();
	win.show(value);
}
//所有grid store中的fields
var invoiceFields = ['start_invoice_id','end_invoice_id','invoice_code','invoice_type',
	'invoice_type_text','optr_id','optr_name','invoice_count','done_code','create_time',
	'source_county_id_text','source_dept_id_text','source_optr_id_text',
	'order_county_id_text','order_dept_id_text','order_optr_id_text'];
	
//往日结账
var ClosePanel = Ext.extend(Ext.grid.GridPanel,{
	closeStore :null,
	constructor:function(){
		
		this.closeStore = new Ext.data.JsonStore({
			url:'resource/Invoice!queryAllInvoiceClose.action',
			fields:invoiceFields
		});
		this.closeStore.load();
		var columns = [
			{header:'发票代码',dataIndex:'invoice_code',width:70},
			{header:'发票类型',dataIndex:'invoice_type_text',width:65},
			{header:'发票号段',dataIndex:'',width:100,renderer:function(valu,meta,record){
					return record.get('start_invoice_id').concat(' - ',record.get('end_invoice_id'));
				}
			},
			{header:'发票张数',dataIndex:'invoice_count',width:60},
			{header:'操作员',dataIndex:'optr_name',width:55},
			{header:'操作',dataIndex:'invoice_code',width:100,renderer:function(value){
				return "<a href='#' onclick=doDetail('"+value+"')>明细</a>&nbsp;&nbsp;" +
						"<a href='#' onclick=doCancel('"+value+"')>缴销</a>";
				}
			}];
		ClosePanel.superclass.constructor.call(this,{
			id:'closePanelId',
			autoScroll:true,
			ds:this.closeStore,
			columns:columns,
			tbar:[{text:'批量核销',scope:this,handler:function(){doCancel();}}]
		});
	}
});

//今日结账
var CloseTodayPanel = Ext.extend(Ext.grid.GridPanel,{
	closeTodayStore :null,
	constructor:function(){
		this.closeTodayStore = new Ext.data.JsonStore({
			url:'resource/Invoice!queryTodayInvoiceClose.action',
			fields:invoiceFields
		});
		this.closeTodayStore.load();
		var columns = [
			{header:'发票代码',dataIndex:'invoice_code',width:70},
			{header:'发票类型',dataIndex:'invoice_type_text',width:65},
			{header:'发票号段',dataIndex:'',width:100,renderer:function(valu,meta,record){
					return record.get('start_invoice_id').concat(' - ',record.get('end_invoice_id'));
				}
			},
			{header:'发票张数',dataIndex:'invoice_count',width:60},
			{header:'操作员',dataIndex:'optr_name',width:55},
			{header:'操作',dataIndex:'invoice_code',width:100,renderer:function(value){
				return "<a href='#' onclick=doDetail('"+value+"')>明细</a>&nbsp;&nbsp;" +
						"<a href='#' onclick=doCancel('"+value+"')>缴销</a>";
				}
			}];
		CloseTodayPanel.superclass.constructor.call(this,{
			id:'closeTodayPanelId',
			autoScroll:true,
			ds:this.closeTodayStore,
			columns:columns,
			tbar:[{text:'批量核销',scope:this,handler:function(){doCancel();}}]
		});
	}
});

//当日入库
var ExamInTodayPanel = Ext.extend(Ext.grid.GridPanel,{
	examinStore :null,
	constructor:function(){
		this.examinStore = new Ext.data.JsonStore({
			url:'resource/Invoice!queryTodayInvoiceInput.action',
			fields:invoiceFields
		});
		this.examinStore.load();
		var columns = [
			{header:'发票代码',dataIndex:'invoice_code',width:70},
			{header:'发票类型',dataIndex:'invoice_type_text',width:65},
			{header:'发票号段',dataIndex:'',width:120,renderer:function(valu,meta,record){
					return record.get('start_invoice_id').concat(' - ',record.get('end_invoice_id'));
				}
			},
			{header:'发票张数',dataIndex:'invoice_count',width:60},
			{header:'操作员',dataIndex:'optr_name',width:55},
			{header:'操作',dataIndex:'invoice_code',width:100,renderer:function(value,meta,record){
				return "<a href='#' onclick=doDetail('"+value+"')>明细</a>&nbsp;&nbsp;" +
						"<a href='#' onclick=doTransfer("+Ext.encode(record.data)+")>调拨</a>";
				}
			}];
		ExamInTodayPanel.superclass.constructor.call(this,{
			id:'examInTodayPanel',
			autoScroll:true,
			ds:this.examinStore,
			columns:columns,
			tbar:[{text:'批量入库',scope:this,handler:this.doUpdate}]
		});
		doTransfer = this.doTransfer;
	},
	doUpdate:function(){
		var win = Ext.getCmp('checkInWinId');
		if(!win){
			win = new CheckInWin();
		}
		win.show();
	},
	doTransfer:function(data){
		var win = Ext.getCmp('transferSingleWinId');
		if(!win)
			win = new TransferSingleWin();
		win.show(data['invoice_code'],data['invoice_type']);
	}
});

//调拨记录
var ExamOutPanel = Ext.extend(Ext.grid.GridPanel,{
	examOutStore :null,
	constructor:function(){
		this.examOutStore = new Ext.data.JsonStore({
			url:'resource/Invoice!queryInvoiceTransfer.action',
			fields:invoiceFields
		});
		this.examOutStore.load();
		var columns = [
			{header:'调拨时间',dataIndex:'create_time',width:110},
			{header:'源',dataIndex:'source_county_id_text',width:120,renderer:function(value,meta,record){
					value = value.concat('-',record.get('source_dept_id_text'),'-',record.get('source_optr_id_text'));
					if(Ext.isEmpty(value))return '';
					return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
				}
			},
			{header:'目标',dataIndex:'order_county_id_text',width:120,renderer:function(value,meta,record){
					if(value){
						var dept = record.get('order_dept_id_text');
						var optr = record.get('order_optr_id_text');
						if(dept)
							value = value.concat('-',dept);
						if(optr)
							value = value.concat('-',optr);
					}else return '';
					return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
				}
			},
			{header:'发票张数',dataIndex:'invoice_count',width:60},
			{header:'操作员',dataIndex:'optr_name',width:55},
			{header:'操作',dataIndex:'invoice_code',width:100,renderer:function(value){
				return "<a href='#' onclick=doDetail('"+value+"')>明细</a>";
				}
			}];
		ExamOutPanel.superclass.constructor.call(this,{
			id:'examOutPanelId',
			autoScroll:true,
			ds:this.examOutStore,
			columns:columns,
			tbar:[{text:'批量调拨',scope:this,handler:this.doUpdate}]
		});
	},
	doUpdate:function(){
		var win = Ext.getCmp('transferDoubleWinId');
		if(!win){
			win = new TransferDoubleWin();
		}
		win.show();
	}
});

InvoiceManagerPanel = Ext.extend(Ext.ux.Portal,{
	closeTodayPanel: null,
	closePanel: null,
	examInTodayPanel: null,
	examOutPanel: null,
	constructor:function(){
		this.closeTodayPanel = new CloseTodayPanel();
		this.closePanel = new ClosePanel();
		this.examInTodayPanel = new ExamInTodayPanel();
		this.examOutPanel = new ExamOutPanel();
		
		InvoiceManagerPanel.superclass.constructor.call(this,{
			title:'发票管理',
			closable:false,
			layout:'column',
			items:[{
					columnWidth : 0.5,
					style:'padding:10px 5px 0 10px',
					defaults : {
						layout : "fit",
						height : 240
					},
					items : [
					{title:'当日结账',items:this.closeTodayPanel},
					{title:'往日结账',items:this.closePanel}]
				},{
					columnWidth : 0.5,
					style:'padding:10px 10px 0 5px',
					defaults : {
						layout : "fit",
						height : 240
					},
					items : [
						{title:'当日入库',items:this.examInTodayPanel},
						{title:'调拨记录',items:this.examOutPanel}
					]
				}]
		}
		);
	}
});