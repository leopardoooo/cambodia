QueryInvoiceForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	fillOptrCombo:function(deptId){
		Ext.Ajax.request({
			url : "resource/Invoice!getByDeptId.action",
			params:{deptId:deptId},scope:this,
			success:function(req){
				var data = Ext.decode(req.responseText);
				var store = this.getForm().findField('invoiceDto.optrids').getStore();
				store.loadData(data||[]);
			}
		});
	},
	constructor:function(p){
		this.parent = p;
		QueryInvoiceForm.superclass.constructor.call(this,{
			border:false,
			listeners:{
				scope:this,
				render:function(thisObj){
					
					var comboArrs = [];
					var comboParamNames = [];
					var complexFields = this.findByType('compositefield');
					for(var index =0;index<complexFields.length;index++){
						var cf = complexFields[index];
						for(var idx =0;idx<cf.items.length;idx++){
							var field = cf.items[idx];
							if( (field.xtype.indexOf('combo') >= 0)  && !Ext.isEmpty(field.paramName) ){
								comboArrs.push(field);
								comboParamNames.push(field.paramName);
							}
						}
					}
					
					Ext.Ajax.request({
						params: {comboParamNames: comboParamNames},
						url: root + "/ps.action",
						success: function( res, ops){
							var data = Ext.decode(res.responseText );
							for( var i=0;i<data.length ;i++ ){
								comboArrs[i].store.loadData(data[i]);
							}
						}
					});
				}	
			},
			bodyStyle:'padding:10px',
			labelWidth:110,
			layout:'column',
			items:[
				{columnWidth:.5,layout:'form',border:false,items:[
					{xtype:'combo',fieldLabel:lsys('InvoiceCommon.depot_name'),hiddenName:'invoiceDto.depot_id',width :150,minListWidth :250,
						store:new Ext.data.JsonStore({
//							url:'resource/Invoice!queryChildInvoiceDepot.action',
//							url:'resource/Device!queryAllDept.action',
							url:'resource/Device!queryChildDept.action',
							autoLoad:true,
							fields:['dept_id','dept_name']
						}),displayField:'dept_name',valueField:'dept_id',
						triggerAction:'all',mode:'local',
						listeners : {
							scope:this,
							select:function(comp){
								this.fillOptrCombo(comp.getValue());
							}							
						}
					}
				]},
				{columnWidth:.5,layout:'form',style:'padding-buttom:5px',border:false,items:[
				
					{fieldLabel:langUtils.sys('common.optr'),width:322,height: 20,hiddenName:'invoiceDto.optrids',xtype:'paramlovcombo',
						displayField:'optr_name',valueField:'optr_id',
						editable:true,triggerAction:'all',
						store:new Ext.data.JsonStore({
							fields:['optr_id','optr_name']
						}),
						listeners:{
							scope:this,
							keyup:function(field){
								field.filter('optr_name',field.getRawValue());
							}
						}
					}
				
				]},
				{columnWidth:.5,layout:'form',border:false,items:[
					{fieldLabel:lsys('InvoiceCommon.invoice_id'),

					    xtype:'compositefield',combineErrors:false,
					    items: [
					        {xtype:'textfield',name:'invoiceDto.start_invoice_id',vtype:'invoiceId',height:22,width:150},
					        {xtype:'displayfield',value:langUtils.sys('common.to')},
					        {xtype:'textfield',name:'invoiceDto.end_invoice_id',vtype:'invoiceId',height:22,width:150}
				    	]
					},
					{fieldLabel:lsys('InvoiceCommon.create_time'),

						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'invoiceDto.start_input_time',style:'width:135px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:langUtils.sys('common.to')},
						        {xtype:'datefield',name:'invoiceDto.end_input_time',style:'width:135px;height:22px',format:'Y-m-d'}
					    	]
						},
						{fieldLabel:lsys('InvoiceCommon.close_time'),

						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'invoiceDto.start_close_time',style:'width:135px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:langUtils.sys('common.to')},
						        {xtype:'datefield',name:'invoiceDto.end_close_time',style:'width:135px;height:22px',format:'Y-m-d'}
					    	]
						}	
				]},
				/*
				{columnWidth:.5,layout:'hbox',defaults:{border:false,flex:1},border:false,items:[
				{layout:'form',flex:3.4,defaults:{border:false},items:{fieldLabel:'发票代码',xtype:'textfield',name:'invoiceDto.invoice_code',style:'width:103px;height:22px'}},
			    {layout:'form',flex:6,defaults:{border:false},items:{fieldLabel:'发票类型',hiddenName:'invoiceDto.invoice_type',xtype:'paramlovcombo',width:135,paramName:'INVOICE_TYPE'}}
				]},
				*/
				{columnWidth:.5,layout:'form',border:false,width:320,items:[
						{fieldLabel:lsys('InvoiceCommon.invoice_code'),

						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'textfield',name:'invoiceDto.invoice_code',width:133,height:22},//,style:'width:135px;height:22px'
						        {xtype:'displayfield',value:lsys('InvoiceCommon.invoice_type')},

						        {xtype:'paramlovcombo',name:'invoiceDto.invoice_type',paramName:'INVOICE_TYPE',
						        	displayField:'item_name',valueField:'item_value',
						        	store:new Ext.data.JsonStore({
						        		fields:['item_name','item_value'],data:[]
						        	}),height:22,width:133}//style:'width:135px;height:22px'
					    	]
						},
						{fieldLabel:lsys('InvoiceCommon.check_time'),

						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'invoiceDto.start_check_time',style:'width:135px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:langUtils.sys('common.to')},
						        {xtype:'datefield',name:'invoiceDto.end_check_time',style:'width:135px;height:22px',format:'Y-m-d'}
					    	]
						},
						{fieldLabel:lsys('InvoiceCommon.use_time'),

							    xtype:'compositefield',combineErrors:false,
							    items: [
							        {xtype:'datefield',name:'invoiceDto.start_use_time',style:'width:135px;height:22px',format:'Y-m-d'},
							        {xtype:'displayfield',value:langUtils.sys('common.to')},
							        {xtype:'datefield',name:'invoiceDto.end_use_time',style:'width:135px;height:22px',format:'Y-m-d'}
						    	]
						}
				]},
				
				{columnWidth:.5,layout:'form',border:false,width:150,items:[
						{fieldLabel:lsys('InvoiceCommon.finance_status'),

						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'paramlovcombo',paramName:'FINANCE_STATUS_R_INVOICE',
						        	displayField:'item_name',valueField:'item_value',
						        	store:new Ext.data.JsonStore({
						        		fields:['item_name','item_value'],data:[]
						        	}),
						        		name:'invoiceDto.finance_status',height:22,width:133},
						        {xtype:'displayfield',value:lsys('InvoiceCommon.status')},

						        {xtype:'paramlovcombo',paramName:'STATUS_R_INVOICE',
							        displayField:'item_name',valueField:'item_value',
							        	store:new Ext.data.JsonStore({
							        		fields:['item_name','item_value'],data:[]
							        	}),
						        	name:'invoiceDto.status',height:22,width:133}
					    	]
						}
				]},
				/*
				{columnWidth:.5,layout:'hbox',defaults:{border:false},border:false,items:[
				{layout:'form',flex:3.2,defaults:{border:false},
					items:{fieldLabel:'结存状态',hiddenName:'invoiceDto.finance_status',
								xtype:'paramlovcombo',paramName:'FINANCE_STATUS_R_INVOICE'}
								},
			    {layout:'form',flex:5,defaults:{border:false},
			    	items:{fieldLabel:'使用状态',hiddenName:'invoiceDto.status',
								xtype:'paramlovcombo',paramName:'STATUS_R_INVOICE'}
								}
				]},
				*/
				{columnWidth:.5,layout:'hbox',defaults:{border:false,flex:1},border:false,items:[
						{width:300,bodyStyle:'padding-left:25px',border:false,items:[
							{id:'queryInvoiceBtnId',xtype:'button',text:lsys('common.query'),iconCls:'icon-query',
								scope:this,handler:this.doQuery}
						]}
				]}
			]
		});	
        this.getForm().findField('invoiceDto.depot_id').getStore().load();
	},
	doQuery:function(){
		if(this.getForm().isValid()){
			
			Ext.getCmp('queryInvoiceBtnId').disable();//禁用查询按钮，数据加载完再激活
			
			var store = this.parent.grid.getStore();
			store.removeAll();
			this.parent.grid.setTitle(lsys('InvoiceCommon.titleInvoiceInfo'));

			
			var values = this.getForm().getValues();
			values['invoiceDto.finance_status'] = this.getForm().findField('invoiceDto.finance_status').getValue();
			values['invoiceDto.invoice_type'] = this.getForm().findField('invoiceDto.invoice_type').getValue();
			values['invoiceDto.status'] = this.getForm().findField('invoiceDto.status').getValue();
			
			var obj = {};
			for(var v in values){
				if(v.indexOf("invoiceDto.")==0){
					obj[v] = values[v];	
				}
			}
			var optr = this.getForm().findField('invoiceDto.optrids');
			
			store.baseParams = obj;
			store.load({params: { start: 0, limit: 25 }});
		}
	}
});



QueryInvoiceGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	amount:0,
	rowCount:0,
	sm : null,
	detailWin:null,
	constructor:function(){
		this.store = new Ext.data.JsonStore({
			url:'resource/Invoice!queryMulitInvoice.action',
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['invoice_id','invoice_code','invoice_type','status','status_text','amount',
				'invoice_mode','invoice_mode_text','finance_status','finance_status_text',
				'invoice_book_id','invoice_type_text','depot_id','depot_name',
				'county_id','county_id_text','create_time','use_time','check_time','close_time',
				'invoice_amount','optr_id','optr_name','is_loss','is_loss_text','open_optr_name','cust_name'
				]
		});
		this.store.on("load",function(store){
			Ext.getCmp('queryInvoiceBtnId').enable();
			var total = 0;
			store.each(function(record){
				if(record.get('status')=='USE')
					total = record.get('amount') + total;
			})
			this.setTitle(lsys('InvoiceCommon.titleMoneyCountPrefix')+Ext.util.Format.formatFee(total));
		},this);
		var columns = new Ext.grid.ColumnModel({
			defaults:{sortable:false},
			
			columns:[
				{header:lsys('InvoiceCommon.invoice_id'),dataIndex:'invoice_id',width:75,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.invoice_type'),dataIndex:'invoice_type_text',width:70,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.status'),dataIndex:'status_text',width:70,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.finance_status'),dataIndex:'finance_status_text',width:70,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.amount'),dataIndex:'amount',width:65,align:'center',renderer:Ext.util.Format.formatFee},
				{header:lsys('InvoiceCommon.optr_name'),dataIndex:'optr_name',width:90,align:'center'},
				{header:lsys('InvoiceCommon.open_optr_name'),dataIndex:'open_optr_name',width:90,align:'center'},
				{header:lsys('InvoiceCommon.depot_name'),dataIndex:'depot_name',width:120,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.create_time'),dataIndex:'create_time',width:120,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.use_time'),dataIndex:'use_time',width:120,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.check_time'),dataIndex:'check_time',width:120,align:'center',renderer:App.qtipValue},
				{header:lsys('InvoiceCommon.close_time'),dataIndex:'close_time',width:120,align:'center',renderer:App.qtipValue}
			]
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel({});
		QueryInvoiceGrid.superclass.constructor.call(this,{
			title:lsys('InvoiceCommon.titleInvoiceInfo'),

			autoScroll:true,
			border:false,
			ds:this.store,
			cm:columns,
			sm:sm,
			bbar: new Ext.PagingToolbar({store: this.store ,pageSize : 25}),
			listeners : {
				rowclick : function(grid){
					var detailWin = new DetailWin();
					detailWin.show();
					
					var record = grid.getSelectionModel().getSelected();
					var invoiceId = record.get('invoice_id');
					var invoiceBookId = record.get('invoice_book_id');
					var invoiceCode = record.get('invoice_code');
					detailWin.info.queryInvoiceDetail(invoiceId,invoiceCode);
				}
			}
		});
	}
});

DetailWin = Ext.extend(Ext.Window,{
	info:null,
	constructor : function(){
		this.info = new InvoiceAllInfo();
		DeptSelectWin.superclass.constructor.call(this,{
			title: langUtils.sys('QueryInvoice.common.titlaQueryInvoick'),

			layout: 'fit',
			width: 1000,
			height: 500,
			closeAction:'close',
			autoDestroy:true,
			items: [this.info]
		})
	}
});

QueryInvoice = Ext.extend(Ext.Panel,{
	form:null,
	grid:null,
	constructor:function(){
		this.form = new QueryInvoiceForm(this);
		this.grid = new QueryInvoiceGrid();
		QueryInvoice.superclass.constructor.call(this,{
			id:'QueryInvoice',
			title:langUtils.sys('QueryInvoice.common.titlaQueryInvoick'),

			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'anchor',
			items:[
				{anchor:'100% 35%',border:false,items:[this.form]},
				{anchor:'100% 65%',layout:'fit',border:false,items:[this.grid]}
			]
		});
	}
})