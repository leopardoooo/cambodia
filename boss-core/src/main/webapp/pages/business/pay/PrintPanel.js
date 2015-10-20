/**
 * 打印组件面板封装
 */
 
 //打印方式
 PRINT_TYPE_WEB = "WEB";
 PRINT_TYPE_ACTIVE = "ACTIVEX";
 
 var LU_PI = lbc('home.tools.InvoicePrint');//打印需要国际化内容
 
 //打印的条数
 PRINT_PAGE_SIZE = 5 ;
var INVOICE_SIZE = 8;//发票长度 ,暂时不用
var oldInvoiceId= "";
var oldInvoiceCode="";
var oldInvoiceBookId="";
var printdata = null;
 PrintPanel = Ext.extend( Ext.Panel , {
 	id:"PrintPanel",
 	printStore: null,
 	printItemStore:null,
 	printGrid: null,
 	printItem: null,
 	printItemGrid:null,
 	winPreview: null,
 	winInvoice: null,
 	paytype:LU_PI.wdxj,
 	printStoreUrl:Constant.ROOT_PATH+"/core/x/Pay!queryPrintRecord.action",
 	printStoreFields: [
				'cust_id','doc_sn','done_code','doc_name',
				'is_invoice','doc_type','done_date','optr_name','doc_type_name',
				'optr_id'
			],
	printGridColumns:[
		{ header: LU_PI.printGridColumns[0], dataIndex: 'done_code'},
		{ header: LU_PI.printGridColumns[1], dataIndex: 'doc_type_name', width: 120},
		{ header: LU_PI.printGridColumns[2], dataIndex: 'done_date', width: 140}
	],
 	//构造函数
 	constructor: function(){
 		this.printGridSm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
 		//打印Store 
 		this.printStore = new Ext.data.JsonStore({
	 		url: this.printStoreUrl,
			fields: this.printStoreFields
	 	});
// 		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
 		this.printGrid = new Ext.grid.GridPanel({
 			border: false,
 			region: 'center',
 			title: LU_PI.titleDocGrid,
 			listeners:{
 				scope:this,
 				rowclick:function(grid,rowNumber){
 					var data = grid.store.getAt(rowNumber).data;
 					var doctype = data.doc_type;
 					this.printItemGrid.store.filter('doc_type',doctype);
 					var mod = this.printItemGrid.getSelectionModel();
					mod.selectAll();
 				}
 			},
 			columns: [
 				this.printGridSm
			].concat(this.printGridColumns),
			sm: this.printGridSm,
 			ds: this.printStore
 		});
		var sm1 = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
 		this.printItemStore = new Ext.data.JsonStore({
	 			autoLoad : false,
		 		url: Constant.ROOT_PATH+"/core/x/Pay!queryUnPrintItem.action",
				fields: [
					'amount','printitem_name','docitem_sn','doc_type'],
				listeners:{scope:this,
					load:function(){
//						this.printItemGrid.getSelectionModel().selectAll();
					}
				}
		 	});
		 	
		 this.printItemGrid = new Ext.grid.GridPanel({
				border : false,
				region : 'center',
				title : LU_PI.titleInvoiceGrid,
				sm: sm1,
				loadMask : true,
				columns : [sm1,
						{
							header : LU_PI.printItemGridColumns[0],
							dataIndex : 'printitem_name',
							width: 200
						}, {
							header : LU_PI.printItemGridColumns[1],
							dataIndex : 'amount',
							xtype: 'moneycolumn',
							width: 120
						}],
				ds : this.printItemStore
			});
 		
 		PrintPanel.superclass.constructor.call(this,{
 			layout: 'fit',
 			border: false,
 			items: [{
 				layout:'border',
 				items:[
 					{
		 				region: 'south',
		 				height: 200,
		 				layout: 'fit',
		 				items:this.printItemGrid
		 			},{
		 				region: 'center',
		 				layout: 'fit',
		 				items: this.printGrid
		 			}
 				]
 			}],
 			buttons:[
// 				{
// 				text: '预览',
// 				scope: this,
// 				handler: function(){
// 					this.actionProcesser("doPreview");
// 				}
// 			},
 				{
 				text: lbc('common').print,
 				scope: this,
 				width: 60,
 				iconCls:'print',
 				handler: function(){
 					this.actionProcesser("doPrint");
 				}
 			},{
				text: lbc('common').close,
				scope: this,
				iconCls:'icon-comments_remove',
				width: 60,
				handler: this.doClose
			}]
 		});
		if (App.getUrlParam('type')==""){
			try{
		 		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		 		oldInvoiceId= record.get('invoice_id');
		 		oldInvoiceBookId =record.get('invoice_book_id');
		 		oldInvoiceCode =record.get('invoice_code');
	 		}catch (e){
	 		}
		}else if(App.getUrlParam('type')=='reprint'){
			var record = App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected();
			oldInvoiceId= record.get('invoice_id');
	 		oldInvoiceBookId =record.get('invoice_book_id');
	 		oldInvoiceCode =record.get('invoice_code');
		}else if(App.getUrlParam('type')=='valuableRePrint'){
			var record = App.getApp().main.valuableCardGrid.getSelectionModel().getSelected();
			oldInvoiceId= record.get('invoice_id');
	 		oldInvoiceBookId =record.get('invoice_book_id');
	 		oldInvoiceCode =record.get('invoice_code');
		}
 		
 		 //加载数据
 		this.printStore.load({
 			params : {
						printType : App.getUrlParam('type'),
						custId : App.getCustId(),
						fee_sn : App.getUrlParam('feesn'),
						invoice_id : oldInvoiceId,
						invoice_code : oldInvoiceCode
					}
 		});
		
		this.printStore.on('load', function(s){
			this.doLoadData(s, oldInvoiceId, oldInvoiceCode)
		},this);
		
 	},
// 	initComponent:function(){
// 		
// 		PrintPanel.superclass.initComponent.call(this);
// 	},
 	doClose: function(){
		App.getApp().menu.hideBusiWin();
	},
 	doLoadData : function(store, oldInvoiceId, oldInvoiceCode){
 		if (store.getCount()==1){
 			var rs = store.getAt(0);
 			var data = rs.get("printData");
 			
 			var custType = '';
 			if(App.getApp().getCust()){
 				custType = App.getApp().getCust().cust_type;
 			}
	 		var items = new Ext.data.JsonStore({
	 			autoLoad : true,
		 		url: Constant.ROOT_PATH+"/core/x/Pay!queryPrintItem.action",
		 		baseParams : {
		 			docSn:rs.get('doc_sn'),
		 			custType : custType,
		 			invoiceId: oldInvoiceId,
		 			invoiceCode: oldInvoiceCode
		 		},
				fields: [
					'docitem_sn','amount','printitem_name','doc_type']
		 	});
		 	
		 	this.printItem = new Ext.grid.GridPanel({
				border : false,
				region : 'center',
				title : LU_PI.titlePrintDetail,
				columns : [{
							header : LU_PI.printItemGridColumns[0],
							dataIndex : 'printitem_name',
							width: 200
						}, {
							header : LU_PI.printItemGridColumns[1],
							dataIndex : 'amount',
							xtype: 'moneycolumn',
							width: 120
						}],
				ds : items
			});

		 	this.printGrid.getSelectionModel().selectRow(0,true);
		 	printdata = this.printGrid.getSelectionModel().getSelected();
			this.actionProcesser("doPrint",data);
			this.removeAll();
		 	this.add(this.printItem);
		 	this.doLayout();
 		} else if(store.getCount() > 1) {
 			//根据流水号列出所有未打印的打印项
 			this.printItemGrid.getStore().baseParams ={done_code:store.getAt(0).get("done_code")};
 			this.printItemGrid.getStore().load();
 		}
 	},
 	/**
 	 * 执行打印或者预览的处理函数
 	 * @param i Store的下标
 	 * @param t 预览(doPreview)或者打印(doPrint)
 	 */
 	actionProcesser: function( t,data ){
		printdata = this.printGrid.getSelectionModel().getSelected();
		if(printdata == null){
 			Alert(lmsg('selectInvoice2Print'));
 			return ;
		}
 		if(data){
 			this[t].call(this , printdata);
 		}else{
 			if (printdata.get("done_date") == null){
 				if (!this.printItemGrid.getSelectionModel().getSelected()){
 					Alert(lmsg('selectInvicePrintItem'));
 					return ;
 				}
 				itemRecords = this.printItemGrid.getSelectionModel().getSelections();
 				var doc_type = printdata.get("doc_type");
 				var done_code =  printdata.get("done_code");
 				var cust_id =  printdata.get("cust_id");
 				var docItem = [];
 				for (i=0;i<itemRecords.length;i++){
 					docItem[i] = itemRecords[i].get("docitem_sn");
 				};
 				Ext.Ajax.request({
		 			scope: this,
		 			async: false,
		 			url: Constant.ROOT_PATH+"/core/x/Pay!saveDocItemManual.action",
		 			params: {
		 				docType:doc_type,
		 				doneCode:done_code,
		 				custId:cust_id,
		 				docItems:docItem
		 			},
		 			success: function(res ,ops ){
		 				printdata.set("doc_sn",Ext.decode(res.responseText));
		 			}
 				});
 			}
 			this.setData( printdata , this[t]);
 		}
 	},
 	/**
 	 * 设置数据到当前行的打印记录的record中。
 	 * key为printData,为了打印或预览只查询一次数据
 	 */
 	setData: function(rs, callback){
		var all = {};
		var custId = null;
		//充值卡业务，非营业费用 业务不需要custId
		if(!App.getApp().main.valuableCardGrid || !App.getApp().main.feeUnitpreGrid){
			custId = Ext.isEmpty(rs.get('cust_id'))?App.getApp().getCustId():rs.get('cust_id');
		}
 		Ext.apply(all ,{
 			"suffix": PrintTools.isIE ? "" : "noie",
 			"custId": custId,
 			"doc.doc_sn": rs.get("doc_sn"),
			"doc.done_code": rs.get("done_code"),
			"doc.doc_type": rs.get("doc_type"),
			"invoiceId": oldInvoiceId,
			"invoiceCode": oldInvoiceCode
 		});
 		Ext.Ajax.request({
 			scope: this,
 			url: Constant.ROOT_PATH+"/core/x/Pay!initPrint.action",
 			params: all,
 			success: function(res ,ops ){
 				var map = Ext.decode( res.responseText);
 				//设置客户端的数据，
 				var feeUnitpreGrid = App.getApp().main.feeUnitpreGrid;
 				if(feeUnitpreGrid && feeUnitpreGrid.getSelectionModel().getSelected()){
 					var rec = feeUnitpreGrid.getSelectionModel().getSelected();
 					map.data['cust'] = {cust_name : rec.get('cust_name')};
 					map.data['linkman'] = {};
 				}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
					var rec = App.getApp().main.valuableCardGrid.getSelectionModel().getSelected();
 					map.data['cust'] = {cust_name : rec.get('cust_name')};
 					map.data['linkman'] = {}; 					
 				}else{
 					map.data["cust"] = App.getData().custFullInfo.cust;
 					map.data["linkman"] = App.getData().custFullInfo.linkman;
 				}
 				
 				rs.set("printData", map);
 				callback.call(this, rs);
 				
 			}
 		}); 
	},
 	//预览
// 	doPreview: function(rs){
// 		var printType = rs.get("print_type");
// 		var isInvoice = rs.get("is_invoice");
// 		var map = rs.get("printData");
// 		var xmlStr ;
// 		try{
// 			xmlStr = PrintTools.getContent( map["content"] , map["data"] );
//		}catch(e){
//			alert("模板变量替换时出错! error:" + e.message);
//			throw new Error(e);
//		}
//		if(PRINT_TYPE_WEB === printType){
//			if(!this.winPreview){
//	 			this.winPreview = new PreviewWindow();
//	 		}
//	 		this.winPreview.show(xmlStr);
//		}else{
//			alert("待续...");
//		}
// 	},
 	//打印
 	doPrint: function(rs){
 		var isInvoice = rs.get("is_invoice");
 		var map = rs.get("printData");
 		var printType = map.printType;
 		var docType= map.docType;
 		map.data.paytype=this.paytype;

		if(PRINT_TYPE_WEB === printType){
			try{
				var xmlStr = PrintTools.getContent( map["content"] , map["data"] );
				var url = root + "/pages/business/pay/PrintTemp.html";
				window.showModalDialog(url ,xmlStr);
				this.printStore.remove(rs);
			}catch(e){
					alert(lmsg('templateReplaceError',null,e.message));
					throw new Error(e);
			}
		}else{
			// 输入发票号码
			if(isInvoice === "T"){
				if(null == this.winInvoice){
					this.invoiceWindow = new InvoiceWindow();
				}
				this.invoiceWindow.show(rs ,docType,printType,this );
			}else{
				try{
					var xmlStr = PrintTools.getContent(map["content"],
									map["data"]);
				}catch(e){
					alert(lmsg('templateReplaceError',null,e.message));
					throw new Error(e);
				}
				try{
					PrintTools.print(xmlStr);
				}catch(e){
					alert(lmsg('printCmpError',null,e.message));
				}				
			}
		}

 	},
 	//发票打印，按照page count拆分数据
 	doInvoicePrint: function(invoiceInfo , record,printType){
 		var map = record.get("printData"), xmlStr;
 		var data = map["data"];
 		
 		if(printType == 'NOPRINT'){
 		
 		}else{
 		
	 		//从已经发票信息获取对应的items,并重新生成xml文档
	 		var noIePrintObj = [];
	 		for(var i=0; i< invoiceInfo.length ;i++){
	 			// 设置总金额 及打印项
	 			data["total"] = invoiceInfo[i]["amount"];
	 			data["printItems"] = invoiceInfo[i]["docitem_data"];
	 			data["invoiceCode"] = invoiceInfo[i]["invoice_code"];
	 			data["invoiceId"] = invoiceInfo[i]["invoice_id"];
	 			try{
		 			xmlStr = PrintTools.getContent( map["content"] , data );
	 			}catch(e){
	 				alert(lmsg('templateReplaceError',null,e.message));
					throw new Error(e);
				}
				if(PrintTools.isIE){
					try{
						PrintTools.print(xmlStr);
					}catch(e){
						alert(lmsg('printCmpError',null,e.message));
					}	
				}else{
					//noIePrintContent += '<div class="breakPage">' + xmlStr + "</div>";
					noIePrintObj.push(PrintTools.parsePrintXML(xmlStr));
				}
	 		}
	 		//非IE打印
	 		if(!PrintTools.isIE){
				PrintTools.appletPrint(noIePrintObj);
			}
		
 		}
		
 		this.printStore.remove(record);
 		this.printItemStore.remove(this.printItemGrid.getSelectionModel().getSelections());
 		if (this.printStore.getCount()==0 && this.printItemStore.getCount()==0){
 			App.getApp().menu.hideBusiWin();
 		}
 	}
 });
 
/**
 * 构建预览窗口
 */
PreviewWindow = Ext.extend( Ext.Window, {
	
	//构造函数
	constructor: function(){
		PreviewWindow.superclass.constructor.call(this,{
			title: LU_PI.titlePrintPreview,
			width: 600,
			height: 400,
			autoScroll: true,
			html: "<div id='previewDiv' style='width: 100%;height: 100%;'></div>"
		});
	},
	show: function(xmlStr){
		PreviewWindow.superclass.show.call(this);
		Ext.get('previewDiv').update(xmlStr);
	}
});

/**
 * 输入发票窗口
 */
InvoiceWindow = Ext.extend( Ext.Window ,{
	
	//打印项记录
	record: null,
	invoiceGrid: null,
	invoiceStore: null,
	docType : null,
	printType:null,
	//发票的张数
	pageCount: 0 ,
	invoiceEditStore : null,//发票下拉框编辑数据
	//构造函数
	constructor: function(){
		this.invoiceStore = new Ext.data.JsonStore({
			fields: [
				{name: 'serialNum', type: 'number'},
				{name: 'invoiceId', type: 'number'},
				{name: 'invoice_book_id', type: 'number'},
				{name: 'invoice_code', type: 'number'},
				{name: 'open_optr_name', type: 'string'}
			]
		});
		
		this.invoiceEditStore = new Ext.data.JsonStore({
    		fields : ['invoice_book_id','invoice_code']
		})
		//实例化 发票的编辑 grid
		this.invoiceGrid = new Ext.grid.EditorGridPanel({
			clicksToEdit: 1,
			border: false,
			store: this.invoiceStore,
			columns: [{
                header: LU_PI.invoiceGridColumns[0],
                align: 'center',
                dataIndex: 'serialNum',
                width: 75
            }, {
                header: LU_PI.invoiceGridColumns[1],
                dataIndex: 'invoiceId',
                width: 150,
                align: 'center',
                editor: new Ext.form.TextField()
            }, {
                header: LU_PI.invoiceGridColumns[2],
                align: 'center',
                dataIndex: 'open_optr_name',
                width: 150
        	}],
        	listeners : {
        		scope : this,
        		afteredit : this.afterEdit
        	}
		});
		
		InvoiceWindow.superclass.constructor.call(this,{
			width: 400,
			height: 200,
			modal: true,
			layout: 'fit',
			items: this.invoiceGrid,
			buttons: [{
				text: lbc('common.save'),
				scope: this,
				iconCls: 'icon-save',
				handler: this.onSave
			},{
				text: lbc('common.cancel'),
				scope: this,
				iconCls: 'icon-cancel',
				handler: function(){
					this.close();
				}
			}]
		});
	},
	checkInvoice: function(invoiceId,successFunc,clearFunc){
		if(Ext.isEmpty(invoiceId) ){
			return;
		}
		//排除同一个发票号,不同发票代码的情况.
		if(oldInvoiceBookId && oldInvoiceCode && oldInvoiceId && oldInvoiceId == invoiceId ){
			var oldData = {invoice_book_id:oldInvoiceBookId,invoice_code:oldInvoiceCode,invoice_id:oldInvoiceId}
			successFunc.call(this,[oldData]);
			return ;
		}
		Ext.Ajax.request({
			scope : this,
			url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
			params:{
				invoice_id:invoiceId,
				invoice_mode:'A',
				doc_type:this.docType
			},
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				if(data && data.length > 0){
					if(oldInvoiceBookId && oldInvoiceCode && oldInvoiceId && oldInvoiceId == invoiceId ){
						var oldData = {invoice_book_id:oldInvoiceBookId,invoice_code:oldInvoiceCode,invoice_id:oldInvoiceId}
						data = data.concat(oldData);
					}
					successFunc.call(this,data);
				}
			},
			clearData: function(){
				clearFunc.call(this);
			}
		});
	},
	afterEdit : function(o){
		if(o.column == 1){
			var currentVal=  Ext.util.Format.lpad(parseInt(o.value , 10),o.value.length,'0');
			var invoiceLength = o.value.length;//发票长度
			if("" == currentVal) return ;
			o.record.set('invoiceId', o.value );
			var successFunc = function(data){
//				store.loadData(data);
				// 发票号码如果为1，则发票号码累加
				/*for(var i = o.row + 1,count=this.invoiceStore.getCount(); i< count;i++){
					currentVal++;
					this.invoiceStore.getAt(i).set("invoiceId", Ext.util.Format.lpad(currentVal,invoiceLength,'0'));
					this.invoiceStore.getAt(i).set("invoice_code", 'AAA');
					this.invoiceStore.getAt(i).set("invoice_book_id", 'AAA');
				}*/
				o.record.set('invoice_code','AAA');
				o.record.set('invoice_book_id','AAA');
				if (data.length==1){
					o.record.set('invoice_code',data[0]['invoice_code']);
					o.record.set('invoice_book_id',data[0]['invoice_book_id']);
					o.record.set('open_optr_name',data[0]['open_optr_name']);
				}
			};
			var clearFunc = function(){
				o.record.set('invoiceId','');
				o.record.set('invoice_code','AAA');
				o.record.set('invoice_book_id','AAA');
				o.record.set('open_optr_name','');
			};
			this.checkInvoice(o.value,successFunc,clearFunc);
		}
	},
	show: function( record , docType,printType, p ){
		this.invoiceStore.removeAll(true);
		this.printPanel = p;
		this.record = record;
		this.docType = docType;
		this.printType = printType;
		var map = this.record.get("printData");
		var pis = map.data.printItems;
		// 计算页数
		this.pageCount = Math.ceil(pis.length / PRINT_PAGE_SIZE);
		this.refelshTitle();
		InvoiceWindow.superclass.show.call(this);
		this.initPageItem();
//		this.invoiceGrid.startEditing(0, 1);
	},
	refelshTitle: function(){
		this.setTitle(lbc('home.tools.InvoicePrint.titleInvoiceWindow',null,this.pageCount)); 
	},
	initPageItem: function(){
		// 生成控件
		var invoiceId = App.getUrlParam('invoiceId');
		var t = App.getUrlParam('invoiceBookId').split(",");
		var invoiceBookId = t[0];
		var invoiceCode = t[1];
		if (oldInvoiceId != null && oldInvoiceId != "") {
			invoiceId = oldInvoiceId ;
			invoiceCode = oldInvoiceCode;
			invoiceBookId =	oldInvoiceBookId;
			this.doAddInvoice(invoiceId,invoiceCode,invoiceBookId);
		}else{
			invoiceId = App.getApp().getNextInvoice(this.docType);
			if (!Ext.isEmpty(invoiceId)){
				var that = this;
				var successFunc = function(data){
//					var store = this.invoiceGrid.getColumnModel().getColumnById('col_invoiceCode').editor.getStore();
//					store.loadData(data);
					// 发票号码如果为1，则发票号码累加
					/*for(var i = o.row + 1; i< this.invoiceStore.getCount() ;i++){
						currentVal++;
						this.invoiceStore.getAt(i).set("invoiceId", currentVal );
						this.invoiceStore.getAt(i).set("invoice_code", "AAA");
						this.invoiceStore.getAt(i).set("invoice_book_id", "AAA");
					}*/
					var record = this.invoiceGrid.getSelectionModel().selectFirstRow();
					if(record){
						record.set('invoice_code',data[0]['invoice_code']);
						record.set('invoice_book_id',data[0]['invoice_book_id']);
					}
					if (data.length==1){
						invoiceBookId = data[0]['invoice_book_id'];
						invoiceCode = data[0]['invoice_code'];
						
					}
					this.doAddInvoice(invoiceId,invoiceCode,invoiceBookId);
//					this.invoiceGrid.startEditing(1, 2);
				};
				var clearFunc = function(){
					invoiceId='';
					invoiceCode='';
					invoiceBookId='';
					that.doAddInvoice(invoiceId,invoiceCode,invoiceBookId);
				};
				this.checkInvoice(invoiceId,successFunc,clearFunc);
			}else{
				this.doAddInvoice(invoiceId,invoiceCode,invoiceBookId);
			}
		}
		

//		if (this.pageCount==1)
//			this.onSave();
	},
	doAddInvoice:function(invoiceId,invoiceCode,invoiceBookId){
		var rs = [];
		for(var i = 0 ;i< this.pageCount ;i++){
		if (i==0)
			rs[i] = new Ext.data.Record({
				serialNum: i + 1,
				invoiceId: invoiceId,
				invoice_book_id:'AAA',
				invoice_code :'AAA'
			});
		else
			rs[i] = new Ext.data.Record({
				serialNum: i + 1,
				invoiceId: invoiceId,
				invoice_book_id:'AAA',
				invoice_code:'AAA'
			});
			
		if (invoiceId!="")
			invoiceId = String.leftPad(parseInt(invoiceId,10)+1,invoiceId.length,'0')
		}
		this.invoiceStore.add(rs);
		if(invoiceId == ""){
			this.invoiceGrid.startEditing(0, 1);
		}
//		else if(invoiceCode == ""){
//			this.invoiceGrid.startEditing(1, 2);
//		}
		
	}
	,
	//验证是否已经全部输入完整
	isValid: function(){
		var obj = {};
		obj['isValid'] = true;
		this.invoiceGrid.stopEditing();
		if(this.invoiceStore.getCount() == 0){
			obj['isValid'] = false;
			obj['msg'] = LU_PI.stillEmptyInvoiceField;
			return obj;
		}
		for(var i = 0;i<this.invoiceStore.getCount();i++){
			var rs = this.invoiceStore.getAt(i);
			if(Ext.isEmpty(rs.get("invoiceId")) || Ext.isEmpty(rs.get("invoice_code"))){
				obj['isValid'] = false;
				obj['msg'] = LU_PI.stillEmptyInvoiceField;
				return obj;
				break;
			}
		}
		var flag = false;//是否有重复的发票号
		var count = this.invoiceStore.getCount();
		for(var i=0;i<count;i++){
			var invoiceId = this.invoiceStore.getAt(i).get('invoiceId');
			for(var j=i+1;j<count;j++){
				var nextInvoiceId = this.invoiceStore.getAt(j).get('invoiceId');
				if(invoiceId === nextInvoiceId){
					flag = true;
					break;
				}
			}
		}
		if(flag === true){
			obj['isValid'] = false;
			obj['msg'] = LU_PI.hasDuplcateInvoice;
		}
		return obj;
	},
	/**
	 * 按一定格式封装提交的参数值，保存发票信息。
	 */
	onSave: function(){
		var saveFunc = function(){
			var items = this.record.get("printData").data.printItems;
			var all = [] ,amount = 0, printitemData = [];
			//封装提交参数
			for(var i = 0,j = 0;i<this.invoiceStore.getCount();i++){
				var rs = this.invoiceStore.getAt(i);
				var invoiceItem = {
					invoice_id: rs.get('invoiceId'),
					invoice_code:'AAA',
					invoice_book_id:'AAA',
					doc_type: this.docType,
					doc_sn: this.record.get('doc_sn')
				}; 
				amount = 0;
				printitemData = [];
				docSnItems = [];
				while(j < items.length){
					amount += items[j].amount;
					printitemData.push(items[j]);
					docSnItems.push(items[j].docitem_sn);
					j++;
					if(j > 0 && j % PRINT_PAGE_SIZE == 0){
						break;
					}
				}
				invoiceItem["amount"] = amount;
				invoiceItem["docitem_data"] = printitemData;
				invoiceItem["docSnItems"] = docSnItems;
				all.push(invoiceItem);
			}
			
						
			// 因为需要保存的docitem_data为字符串，因此需要将其转换为字符串
			var commitValue = all ;
			for(var i =0 ;i<all.length ;i++){
				commitValue[i]["docitem_data"] = Ext.encode(all[i]["docitem_data"]);
			}
			
			// 保存生成的数据
			var baseparam = {};
			baseparam[CoreConstant.JSON_PARAMS] = Ext.encode(App.getApp().getValues());
			baseparam.invoiceInfo = Ext.encode(commitValue);
			baseparam.invoice_code  = oldInvoiceCode;
			baseparam.invoice_id = oldInvoiceId;
			
			baseparam["parameter.busiCode"] = App.getApp().data.currentResource.busicode;
			Ext.Ajax.request({
				url: Constant.ROOT_PATH+"/core/x/Pay!saveInvoiceInfo.action",
				params : baseparam,
//				async : false,
				scope: this,
				success: function(res , ops ){
					for(var i =0 ;i<all.length ;i++){
						all[i]["docitem_data"] = Ext.decode(all[i]["docitem_data"]);
					}
					//打印的收据如果不是操作人自己的，下次跳票为空
					var record = Ext.decode(res.responseText);
					var invoiceNext = '';
					if(record){
						invoiceNext = this.invoiceStore.getAt(this.invoiceStore.getCount()-1).get('invoiceId');
					}
					//保存最后一张发票
					App.getApp().useInvoice(this.docType,invoiceNext);

					//	调用打印面板的发票打印信息
					this.printPanel.doInvoicePrint(all ,this.record,this.printType);
					
					
					var feeUnitpreGrid = App.getApp().main.feeUnitpreGrid;
					if(feeUnitpreGrid && feeUnitpreGrid.getSelectionModel().getSelected()){
	 					feeUnitpreGrid.getStore().reload();
	 				}
					var valuableGrid = App.getApp().main.valuableCardGrid;
	 				if(valuableGrid && valuableGrid.getSelectionModel().getSelected()){
	 					valuableGrid.getStore().reload();
	 				}
	 				
					App.getApp().main.infoPanel.getPayfeePanel().acctFeeGrid.remoteRefresh();
					App.getApp().main.infoPanel.getPayfeePanel().busiFeeGrid.remoteRefresh();
					App.getApp().main.infoPanel.getDocPanel().invoiceGrid.remoteRefresh();
					this.close();
				}
			});
			
		};
		
		var obj = this.isValid();
		if (this.pageCount==1){
			if(obj['isValid'] === true){
				saveFunc.call(this);
			}
		}else{
			if(obj['isValid'] === true){
				Confirm(lmsg('confirmSaveInvoiceInfo') , this ,saveFunc);
			}else{
				Alert(obj['msg']);
			}
		}
	}
});
 