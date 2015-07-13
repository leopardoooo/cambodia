/**
 * 打印组件面板封装
 */
 
 //打印方式
 PRINT_TYPE_WEB = "WEB";
 PRINT_TYPE_ACTIVE = "ACTIVEX";
 
 //打印的条数
 PRINT_PAGE_SIZE = 3 ;
 PrintPanel = Ext.extend( Ext.Panel , {
 	
 	printStore: null,
 	printGrid: null,
 	
 	winPreview: null,
 	
 	//构造函数
 	constructor: function(){
 		
 		//打印Store 
 		this.printStore = new Ext.data.JsonStore({
	 		url: Constant.ROOT_PATH+"/core/x/Pay!queryConfigPrintRecord.action",
			fields: [
				'cust_id','done_code','doc_name','print_type',
				'is_invoice','doc_type','done_date','optr_name','busi_name',
				'optr_id','last_print'
			]
	 	});
 		
 		this.printGrid = new Ext.grid.GridPanel({
 			border: false,
 			region: 'center',
 			title: '打印列表',
 			columns: [
				{ header: '流水号', dataIndex: 'done_code'},
				{ header: '业务类型', dataIndex: 'busi_name', width: 120},
				{ header: '操作员名称', dataIndex: 'optr_name', width: 80},
				{ header: '创建时间', dataIndex: 'done_date', width: 80},
				{header:'最后打印',dataIndex:'last_print',width:80}
			],
			//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
 			ds: this.printStore,
 			buttons:[
 				{
 				text: '打印',
 				scope: this,
 				iconCls:'print',
 				width: 60,
 				handler: function(){
 					this.actionProcesser("doPrint");
 				}
 			},{
				text: '关闭',
				scope: this,
				iconCls:'icon-comments_remove',
				width: 60,
				handler: this.doClose
			}]
 		});
 	
 		PrintPanel.superclass.constructor.call(this,{
 			layout: 'border',
 			border: false,
 			items: this.printGrid
 		});
 		
 		var  donecodes = [];
 		var records = App.getApp().main.infoPanel.docPanel.busiDocGrid.getSelectionModel().getSelections();
 		if(records.length>0 ){
 			for(var i=0;i<records.length;i++){
 				var record = records[i];
				donecodes.push(record.get('done_code'));
 			}
 		}
 		//加载数据
 		this.printStore.load({
 			params: {custId: App.getCustId(),doneCodes:donecodes.join(",")}
 		});
 	},
 	doClose: function(){
		App.getApp().menu.hideBusiWin();
	},
 	/**
 	 * 执行打印或者预览的处理函数
 	 * @param i Store的下标
 	 * @param t 预览(doPreview)或者打印(doPrint)
 	 */
 	actionProcesser: function( t){
 			this.setData(  this[t]);
 	},
 	/**
 	 * 设置数据到当前行的打印记录的record中。
 	 * key为printData,为了打印或预览只查询一次数据
 	 */
 	setData: function( callback){
		var  donecodes = [];
		this.printStore.each(function(record){
			donecodes.push(record.get('done_code'));
		}) ;
		
 		Ext.Ajax.request({
 			scope: this,
 			url: Constant.ROOT_PATH+"/core/x/Pay!initConfigPrint.action",
 			params: {custId:App.getCustId(),doneCode:donecodes},
 			success: function(res ,ops ){
 				var map = Ext.decode( res.responseText);
 				//设置客户端的数据，
 				map.data["cust"] = App.getData().custFullInfo.cust;
 				callback.call(this, map);
 			}
 		}); 
	},
 	//打印
 	doPrint: function(map){
 		try{
 			var xmlStr = PrintTools.getContent( map["content"] , map["data"] );
			PrintTools.print(xmlStr);
			
			var doncCodes=[];
			this.printStore.each(function (r){
				doncCodes.push(r.get('done_code'));
			});
			
			// 保存生成的数据
			Ext.Ajax.request({
				url: Constant.ROOT_PATH+"/core/x/Pay!saveConfigPrint.action",
				params : {
					doneCode :doncCodes
				},
				scope: this,
				success: function(res , ops ){
					App.getApp().menu.hideBusiWin();
					App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
				}
			});
			
			
		}catch(e){
			alert("模板变量替换时出错! error:" + e.message);
			throw new Error(e);
		}
 	}
 });


 