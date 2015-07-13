/**
 * 充值卡
 * @class
 * @extends Ext.grid.GridPanel
 */
var ValuableCardAllGrid = Ext.extend(Ext.grid.GridPanel,{
	valuableStore:null,
	queryItemId:'T',
	parent:null,
	custName:null,
	constructor:function(p){
		valuableAllThat = this;
		this.parent = p;
		this.valuableStore = new Ext.data.JsonStore({
			totalProperty:'totalProperty',
			root:'records',
			baseParams : {
				start:0,
				limit:Constant.DEFAULT_PAGE_SIZE
			},
			url : root + '/commons/x/QueryDevice!queryValuableAllCard.action?queryItem='+'T',
			fields:['valuable_id','amount','remark','fee_type','fee_type_text','fee_sn','invoice_mode',
			'invoice_id','invoice_code','invoice_book_id','acct_date','real_pay','finance_status','cust_name','done_code']
		});
		this.valuableStore.load();

		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			sm,
			{header:'流水号',dataIndex:'done_code',width:80,renderer : App.qtipValue},
			{header:'充值卡号',dataIndex:'valuable_id',width:90,renderer : App.qtipValue},
			{header:'金额',dataIndex:'amount',width:60,renderer:Ext.util.Format.formatFee},
			{header:'客户名称',dataIndex:'cust_name',width:60},
			{header:'发票号码',dataIndex:'invoice_id',width:70,renderer : App.qtipValue},
		    {   header: '操作',
		        width:170,
		        scope:this,
	            renderer:function(value,meta,record,rowIndex,columnIndex,store){
	            	var btns = this.doFilterBtns(record);
	            	return btns;
				}
			}
		];
		ValuableCardAllGrid.superclass.constructor.call(this,{
			id:'valuableCardAllGridId',
			region:'center',
			ds:this.valuableStore,
			loadMask : true,
			sm : sm,
			tbar : [new Ext.form.Radio({
						boxLabel: '现有记录',
						checked: true, 
						name: 'radios',
						listeners:{
						 	check : function(checkbox, checked) {
									if (checked) {
										valuableAllThat.queryItemId = 'T';
										valuableAllThat.valuableStore.proxy = new Ext.data.HttpProxy({url : root + '/commons/x/QueryDevice!queryValuableAllCard.action?queryItem='+'T'});
										valuableAllThat.valuableStore.load();
										Ext.getCmp('MergePrintGridWinId').show();
										Ext.getCmp('doAllDeleteId').show();
										Ext.getCmp('doAllAddId').show(); 
										valuableAllThat.parent.doLayout();
                                     }
							}
						}
			}) ,' ',new Ext.form.Radio({
						boxLabel: '注销记录',
						name: 'radios',
						listeners:{
						 	check : function(checkbox, checked) {
									if (checked) {
										valuableAllThat.queryItemId = 'F';
										valuableAllThat.valuableStore.proxy = new Ext.data.HttpProxy({url : root + '/commons/x/QueryDevice!queryValuableAllCard.action?queryItem='+'F'});
										valuableAllThat.valuableStore.load();
										Ext.getCmp('MergePrintGridWinId').hide();
										Ext.getCmp('doAllDeleteId').hide();
										Ext.getCmp('doAllAddId').hide();
										valuableAllThat.parent.doLayout();
                                     }
							}
						}				
			}),'-','关键字',new Ext.ux.form.SearchField({  
	                store: this.valuableStore,
	                width: 180,
	                hasSearch : true,
	                emptyText: '充值卡号,发票号,流水号模糊查询'
	            }),'->','-',{
	            	text:'加入退卡',
	            	iconCls:'icon-del',
	            	scope:this,
	            	id:'doAllDeleteId',
					handler:this.doAllDelete	            	
	            },
	            	{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	id:'doAllAddId',
	        	handler : this.doAdd
	        }],
			columns:columns,
			bbar:new Ext.PagingToolbar({store: this.valuableStore ,pageSize : Constant.DEFAULT_PAGE_SIZE})
		});
		
	},
	doFilterBtns : function(record){
		var btns = "";
			btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doModify()>修改 </>";
		if(valuableAllThat.queryItemId == "F"){
			if(!record.get('invoice_id')){
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doPrint()>打印 </>";
			}else if(record.get('invoice_mode')== 'A'){//机打票才需要重新打印
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doRePrint()>重打 </>";
			}
		}else{
			//发票打印
			if(!record.get('invoice_id')){
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doPrint()>打印 </>";
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').addPrint()>合并打印 </>";
			}else if(record.get('invoice_mode')== 'A'){//机打票才需要重新打印
				btns = btns + "<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doRePrint()>重打 </>";
			}
			//作废
			if(record.get('finance_status')!= 'CLOSE' && record.get('fee_sn')){
				btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"valuableCardAllGridId"+"\').doDelete(); style='color:blue'>退卡 </a>";
			}
		}
		return btns;
	},
	doAdd:function(){
		App.getApp().data.currentResource = {busicode:'1227',
					url: 'pages/index/toptoolbar/ValuableCard.js'};
		App.getApp().menu.bigWindow.show({ text: '充值卡销售',  attrs: {busicode:'1227',
					url: 'pages/index/toptoolbar/ValuableCard.js'}} ,
						{width: 550, height:400});
	},
	doModify : function(){
		this.showPrintGrid();
		var record = this.getSelectionModel().getSelected();
		var btns = [];
		//销售出的记录可以修改出票方式
		if(valuableAllThat.queryItemId == "T"){
			if(App.getData().busiTask['EditInvoiceMode']){
				btns.push(App.getData().busiTask['EditInvoiceMode']);
			}
		}
		if(record.get('finance_status')!= 'CLOSE' && App.getData().busiTask['EditAcctDate']){
			btns.push(App.getData().busiTask['EditAcctDate']);
		}
		new ModifyCardUpdateWin(btns).show();
	},
	doAllDelete:function(){
		var records = this.getSelectionModel().getSelections();
		var grid = Ext.getCmp('MergePrintGridId');	
		var store = grid.getStore();
		var recordType = store.recordType;flag = false;msg = null;
		if(grid.type == 'PRINT'){
			grid.type = 'DELCARD';
			store.removeAll();
			grid.setTitle('退卡列表');
			Ext.getCmp('mergeSaveId').setText('批量退卡');
		}
		for(i=0;i<records.length;i++){
			if(records.length>1){
				if(records[i].get('done_code')!=records[0].get('done_code')){
					msg = "请选择同一流水!";
					flag = true;
				}
			}
			store.each(function(record){
				if(records[i].get('done_code')!=record.get('done_code')){
					msg = "新选择的流水与退卡列表里的流水不一致!";
					flag = true;
				}
				if(records[i].get('valuable_id')==record.get('valuable_id')){
					msg = "新选择的充值卡在退卡列表里已经存在!";
					flag = true;
				}
			})
		}		
		if(flag === true){
			Alert(msg);
			return false;
		}

		for(i=0;i<records.length;i++){	
			var rec = new recordType({
					fee_sn:records[i].get('fee_sn'),valuable_id:records[i].get('valuable_id'),
					amount:records[i].get('amount'),cust_name:records[i].get('cust_name'),done_code:records[i].get('done_code')
				});
			store.add(rec);	
		}

	}
	,
	doDelete : function(){
		Ext.getCmp('MergePrintGridId').getStore().removeAll();
		App.getApp().main.valuableCardGrid.dataStore = this.getSelectionModel().getSelected();
		App.getApp().data.currentResource = {busicode:'1228',
					url: 'pages/index/toptoolbar/ValuableCheck.js'};
		App.getApp().menu.bigWindow.show({ text: '充值卡退卡',  attrs: {busicode:'1228',
				url: 'pages/index/toptoolbar/ValuableCheck.js'}} ,{width: 550, height:400});

	},
	doPrint : function(){
		this.showPrintGrid();
		var feeSn = this.getSelectionModel().getSelected().get('fee_sn');
		App.getApp()['printid'] = feeSn;
		App.getApp().data.currentResource = {busicode:'1068'};
 		App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'1068',
		url: 'pages/business/pay/Print.jsp?type=feesn&feesn='+feeSn}} ,{width: 710, height: 460});
	},
	doRePrint : function(){
		var feeSn = this.getSelectionModel().getSelected().get('fee_sn');
		App.getApp()['printid'] = feeSn;
		App.getApp().data.currentResource = {busicode:'1063'};
 		App.getApp().menu.bigWindow.show({ text: '重打',  attrs: {busiCode:'1063',
		url: 'pages/business/pay/Print.jsp?type=valuableRePrint'}} ,{width: 710, height: 460});
	},
	addPrint : function(){
		var records = this.getSelectionModel().getSelected();
		var grid = Ext.getCmp('MergePrintGridId');
		var store = grid.getStore();
		if(grid.type == 'DELCARD'){
			grid.type = 'PRINT';
			store.removeAll();
			grid.setTitle('打印合并列表');
			Ext.getCmp('mergeSaveId').setText('合并打印');
		}
		var recordType = store.recordType;flag = false;msg = null;
		store.each(function(record){
			if(record.get('cust_name')!=records.get('cust_name')){
				msg = "选择的客户名称与合并打印项里的客户名称不一致!";
				flag = true;
				return false;
			}
			if(record.get('valuable_id')==records.get('valuable_id')){
				msg = "合并打印项里已经存在该充值卡号!";
				flag = true;
				return false;
			}
			if(!Ext.isEmpty(records.get('invoice_id'))){
				msg = "充值卡【"+records.get('valuable_id')+"】已经打印过发票!发票号为:"+records.get('invoice_id');
				flag = true;
				return false;
			}
		});
		if(flag === true){
			Alert(msg);
			return false;
		}
		this.valuableStore.each(function(record){
			if(record.get('done_code') == records.get('done_code')){
				var rec = new recordType({
					fee_sn:record.get('fee_sn'),valuable_id:record.get('valuable_id'),
					amount:record.get('amount'),cust_name:record.get('cust_name'),done_code:record.get('done_code') 
				});
				store.add(rec);	
			}
		})

	},
	showPrintGrid:function(){
		var grid = Ext.getCmp('MergePrintGridId');
		var store = grid.getStore();
			grid.type = 'PRINT';
			store.removeAll();
			grid.setTitle('打印合并列表');
			Ext.getCmp('mergeSaveId').setText('合并打印');
	}
	
});

MergePrintGrid = Ext.extend(Ext.grid.GridPanel,{
	printStore:null,
	type:'PRINT',
	constructor : function(){
		printGridThat=this;
		this.printStore = new Ext.data.JsonStore({
			fields:['valuable_id','amount','fee_sn','cust_name','done_code']
		});
		var cm = new Ext.grid.ColumnModel([
				{header:'充值卡号',dataIndex:'valuable_id',width:90,renderer:App.qtipValue},
            	{header:'金额',dataIndex:'amount',width:50,renderer:Ext.util.Format.formatFee},
            	{header:'客户名称',dataIndex:'cust_name',width:50,renderer:App.qtipValue},
            	{header : '操作',width : 50,
					renderer : function(v, md, record, i) {
						var rs = Ext.encode(record.data);
						return String.format("&nbsp;<a href='#' onclick='printGridThat.deletePrint({0},{1});' style='color:blue'> 删除 </a>",rs, i);
					}
				}
		        ]);
		MergePrintGrid.superclass.constructor.call(this,{
			border:false,
            store:this.printStore,
            sm : new Ext.grid.CheckboxSelectionModel(),
            id:'MergePrintGridId',
            title:'打印合并列表',
            cm:cm,
			buttons : [{
					text : '合并打印',
					scope : this,
					id:'mergeSaveId',					
					iconCls : 'icon-save',
					handler : this.mergeSave
					}]            
		})
	},
	mergeSave : function() {
		var store = this.getStore();
		if(store.getCount() ==0){Alert("无数据!"); return ;}
		if(this.type == 'PRINT'){
			var feesns = [];feesn=null;num=0;
			if(store.getCount() ==1){
				feesn = store.getAt(0).get('fee_sn');
				App.getApp()['printid'] = feesn;
				App.getApp().data.currentResource = {busicode:'1068'};
				App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'1068',
				url: 'pages/business/pay/Print.jsp?type=feesn&feesn='+feesn}} ,{width: 710, height: 460});
			}else{
				store.each(function(record){
					feesns.push(record.get('fee_sn'));
				},this);
				feesn = feesns.join(",");
				App.getApp().data.currentResource = {busicode:'1068'};
		 		App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'1068',
				url: 'pages/business/pay/Print.jsp?type=feesnAll&feesn='+feesn}} ,{width: 710, height: 460});
			}	
		}else{
			records = [];
			for(var i = 0 ;i< store.getCount() ;i++){
				var record = store.getAt(i);
				records[i] = new Ext.data.Record({
					valuable_id:record.get('valuable_id'),amount:record.get('amount'),cust_name:record.get('cust_name')
				});
			}
		App.getApp().main.valuableCardGrid.dataStore = records;
		App.getApp().data.currentResource = {busicode:'1228',
					url: 'pages/index/toptoolbar/ValuableCheck.js'};
		App.getApp().menu.bigWindow.show({ text: '充值卡退卡',  attrs: {busicode:'1228',
				url: 'pages/index/toptoolbar/ValuableCheck.js'}} ,{width: 550, height:400});
		}
		store.removeAll();	
	},
	deletePrint:function(){
		var rec = this.getSelectionModel().getSelected();
		this.printStore.remove(rec);
		if(printGridThat.type == 'PRINT'){
			this.printStore.each(function(record){
				if(record.get('done_code') == rec.get('done_code')){
					printGridThat.printStore.remove(record);
				}
			})
		}
	}
	
})


ModifyCardUpdateWin = Ext.extend(Ext.Window,{
	width : null,
	constructor : function(params){
		var panel = this.createPanel(params);
		var style1 = 'background:#F9F9F9;padding : 20px';
		ModifyCardUpdateWin.superclass.constructor.call(this,{
			id : 'BusiUpdateWindowId',
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
        if(Ext.getCmp('valuableCardAllGridId').queryItemId == "T"){
	        buttons.push({
	        	text : '修改充值卡',
	        	height : 40,
	        	width : 100,
	        	handler : this.doSave
	        })
        }
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
		Ext.getCmp('BusiUpdateWindowId').close();
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
			App.getApp().data.currentResource = {busicode:'1235',
					url: 'pages/index/toptoolbar/ValuableUpdate.js'};
			App.getApp().menu.bigWindow.show({ text: '修改',  attrs: {busicode:'1235',
					url: 'pages/index/toptoolbar/ValuableUpdate.js'}} ,
						{width: 500, height:320});
		}
	}
});


var ValuableCardWin = Ext.extend(Ext.Window,{
	grid:null,
	printGrid:null,
	constructor:function(){
		this.grid = new ValuableCardAllGrid(this);
		this.printGrid = new MergePrintGrid();
		App.getApp().main.valuableCardGrid = this.grid;
		ValuableCardWin.superclass.constructor.call(this,{
			id:'valuableCardWinId',
			closeAction : 'close',
			title:'充值卡信息',
			width:800,
			height : 450,
			border:false,
			layout : 'border',
			items:[{region : 'center',
					layout : 'fit',
					border:false,
					items:[this.grid]
			},{
				region:'east',
				layout:'fit',
				width :220,
				id:'MergePrintGridWinId',
				border:false,
				items:[this.printGrid]
			}]
		});
	}
});
