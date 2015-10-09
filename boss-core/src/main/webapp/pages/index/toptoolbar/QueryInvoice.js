//发票查询框
var QueryInvoiceWin = Ext.extend(Ext.Window,{
	invoiceTpl:null,
	constructor:function(){
		this.LU_INVO = langUtils.bc('home.tools.invoiceQuery');
		
		this.invoiceTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>' + this.LU_INVO['labelInvoiceId2'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_id}</td>',
					'<td class="label" width=20%>' + this.LU_INVO['labelUseStatus'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{status_text}</td>',
				'</tr>',
				'<tr height=24>',
			      	'<td class="label" width=20%>' + this.LU_INVO['labelInvoiceType'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_type_text}</td>',
					'<td class="label" width=20%>' + this.LU_INVO['labelDeptName'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{depot_name}</td>',
			
		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelOpenOptrName'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{open_optr_name}</td>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelOptrName'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{optr_name}</td>',
		    	'</tr>',		    	
		    	'<tr height=24>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelMoneyAmount2'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.amount)]}</td>',
		     		'<td class="label" width=20%>' + this.LU_INVO['labelCreateTime'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{create_time}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelFinanceStatus'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{finance_status_text}</td>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelCloseTime'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{close_time}</td>',//fm.dateFormat(

		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>' + this.LU_INVO['labelCheckDeptName'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{check_depot_id_text}</td>',		    	
		    		'<td class="label" width=20%>' + this.LU_INVO['labelCheckTime'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{check_time}</td>',
		    	'</tr>',
		    	'<tr height=24>',

		    	'</tr>',
			'</table>'
		);
		QueryInvoiceWin.superclass.constructor.call(this,{
			id:'queryInvoiceWinId',
			title:this.LU_INVO['_title'],
			region:'center',
			closeAction:'close',
			width:700,
			height:500,
			border:false,
			bodyStyle: "background:#F9F9F9",
			items:[
				{layout:'column',bodyStyle:'padding:15px 0px 10px 0',
					defaults:{labelAlign:'right'},labelAlign:'right',
					border:false,items:[
					{style:'margin-left:50px',columnWidth:.5,layout:'form',border:false,items:[
						{xtype:'textfield',fieldLabel:this.LU_INVO['labelInvoiceId'],allowBlank:false,
							name:'invoice_id',vtype:'invoiceId',
							enableKeyEvents :true,
							listeners:{
								scope:this,
								specialkey:function(field,e){
									if(e.getKey() === Ext.EventObject.ENTER){
										this.checkInvoice();
									}
								}
								,change:function(){
									this.checkInvoice();
								}
							}
						}
					]},
//					{columnWidth:.4,layout:'form',border:false,items:[
//						{fieldLabel: this.LU_INVO['labelInvoiceCode'],
//							xtype : 'combo',
//							store : new Ext.data.JsonStore({
//								fields : ['invoice_code']
//							}),
//							hiddenName:'invoice_code',
//							displayField : 'invoice_code',
//							valueField : 'invoice_code',
//							vtype:'invoiceCode',
//							allowBlank:false,
//							forceSelection : true,
//							editable : true
//						}
//					]},
					{style:'margin-left:100px',columnWidth:.4,border:false,items:[
						{xtype:'button',text:langUtils.bc('common.queryBtnWithBackSpace'),iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}
					]}
				]},
				{xtype : "panel",title:this.LU_INVO['titleInvoiceInfo'],border : false,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html : this.invoiceTpl.applyTemplate({}),
					buttonAlign : 'center',
					buttons : [{
						text : this.LU_INVO['btnShowInvoiceDetail'],
						id : 'showInvoiceDetail',
						disabled : true,
						scope : this,
						handler : this.showDetailWin
					},{
						text : this.LU_INVO['btnChangeStatus'],
						id : 'changeStatusBtn',
						disabled : true,
						scope : this,
						handler : this.changeStatus
					}]
				}
			]
		});
	},
	show:function(){
		QueryInvoiceWin.superclass.show.call(this);
		this.find('name','invoice_id')[0].focus(true,100);
	},
	checkInvoice : function() {
		var invoiceIdCmp = this.find('name','invoice_id')[0]
		var invoiceId = invoiceIdCmp.getValue();
		if (!Ext.form.VTypes.invoiceId(invoiceId))return;
		
//		var invoiceCodeCmp = this.find('hiddenName','invoice_code')[0];
		Ext.Ajax.request({
			url:root+'/core/x/Pay!queryInvoiceById.action',
			params:{invoice_id:invoiceId},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.length == 0){
					Alert(invoiceId + this.LU_INVO['tipInvoiceNotExists'],function(){
						/*invoiceCodeCmp.setReadOnly(false);
						invoiceCodeCmp.getStore().removeAll();
						invoiceCodeCmp.setValue('');*/
						invoiceIdCmp.focus(true,10);
					});
				}
				else{
					/*invoiceCodeCmp.getStore().loadData(data);
					invoiceCodeCmp.setValue(invoiceCodeCmp.getStore().getAt(0).get('invoice_code'));*/
					this.doQuery();
				}
			}
		});
	},
	doQuery:function(){
		var invoiceIdComp = this.find('name','invoice_id')[0];
		var invoiceId = invoiceIdComp.getValue();
		if (!Ext.form.VTypes.invoiceId(invoiceId))return;
//		var invoiceCode = this.find('hiddenName','invoice_code')[0].getValue();
//		if(Ext.isEmpty(invoiceId) || Ext.isEmpty(invoiceCode))return;
		
		Ext.getCmp('showInvoiceDetail').disable();
		Ext.getCmp('changeStatusBtn').disable();
		Ext.Ajax.request({
			url:root+'/core/x/Pay!queryInvoiceByInvoiceId.action',
			params:{invoice_id:invoiceId,invoice_code:'AAA'},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					this.data = data;
					if(this.data.optr_id==App.data.optr['optr_id']){
						Ext.getCmp('changeStatusBtn').enable();
					}
					Ext.getCmp('showInvoiceDetail').enable();
					
					if(data.status == 'INVALID'){
						Ext.getCmp('changeStatusBtn').setText(this.LU_INVO['btnChangeStatusIdel']);
					}else if(data.status == 'IDLE'){
						Ext.getCmp('changeStatusBtn').setText(this.LU_INVO['btnChangeStatusInvalid']);
					}else if(data.status == 'USE'){
						Ext.getCmp('changeStatusBtn').disable();
						Ext.getCmp('changeStatusBtn').setText(this.LU_INVO['btnChangeStatusUsed']);
					}					
					
					if(this.items.itemAt(1).getEl()){
						this.invoiceTpl.overwrite( this.items.itemAt(1).body, data);
					}
				}else{
					Alert(this.LU_INVO['tipInvoiceNotExists2'],function(){
						if(this.items.itemAt(1).getEl()){
							this.invoiceTpl.overwrite( this.items.itemAt(1).body, {});
						}
						invoiceIdComp.reset();
						invoiceIdComp.focus(true,100);
					},this);
				}
			}
		});
	},
	showDetailWin : function(){
		if(this.data){
			var win = Ext.getCmp('feeDetailWin');
			if(!win){
				win = new FeeDetailWin();
			}
			if(this.data.invoiceDetailList){
				win.grid.getStore().loadData(this.data.invoiceDetailList);
			}
			win.show();
		}
	},
	changeStatus:function(){
		var msg = this.LU_INVO['confirmChangeStatus'];
		var newStatus = 'IDLE';
		if(this.data.status == "INVALID"){
			msg += this.LU_INVO['statusIdel'];
		}else if(this.data.status == "IDLE"){
			newStatus = 'INVALID';
			msg += this.LU_INVO['statusInvalid'];
		}
		Confirm(msg, this ,function(){
			Ext.Ajax.request({
				url:root+'/core/x/Pay!editInvoiceStatus.action',
				params:{invoice_id:this.data.invoice_id,invoice_code:'AAA',status:newStatus},
				scope:this,
				success:function(res,opt){
					this.doQuery();
				}
			});
			
		});
	}
});

/**
 * 发票费用明细
 * @class FeeDetailWin
 * @extends Ext.Window
 */
FeeDetailWin = Ext.extend(Ext.Window,{
	grid : null,
	constructor : function(){
		var COLUMN_HEADERS = langUtils.bc('home.tools.invoiceQuery.cols');
		this.invoiceDetailStore = new Ext.data.GroupingStore({
            reader: new Ext.data.JsonReader({},[ 
				'cust_name', 'cust_no', 'busi_name', 'fee_name', 'real_pay', 'create_time','optr_name', 'fee_id', 'total']
			),
            groupField:'fee_name',
            listeners: {
            	scope: this,
            	load: this.doLoadData
            }
        });
		this.grid = new Ext.grid.GridPanel({
			store : this.invoiceDetailStore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			columns:[
				{ header : COLUMN_HEADERS[0], width:90, dataIndex : 'cust_name', renderer : App.qtipValue }, 
				{ header : COLUMN_HEADERS[1], width:90, dataIndex : 'cust_no', renderer : App.qtipValue }, 
				{ header : COLUMN_HEADERS[2], width:90, dataIndex : 'busi_name', renderer : App.qtipValue }, 
				{ header : COLUMN_HEADERS[3], width:150, dataIndex : 'fee_name' }, 
				{ header : COLUMN_HEADERS[4], width:70, dataIndex : 'real_pay', renderer : function(v, params, record){
	                    return Ext.util.Format.usMoney( Ext.util.Format.formatFee(record.data.real_pay) );
	                } }, 
				{ header : COLUMN_HEADERS[5], width:130, dataIndex : 'create_time', renderer : App.qtipValue }
				
			],
			view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl: '{text}  ('+lbc("common.total")+'：{[values.rs[0].data["total"]]})'
	        })
		});
		
		FeeDetailWin.superclass.constructor.call(this,{
			id : 'feeDetailWin',
			layout : 'fit',
			closeAction : 'close',
			width:700,
			height:400,
			border:false,
			items : [this.grid]
		})
	},
	doLoadData: function(){
		var feeNamesArray = this.invoiceDetailStore.collect('fee_name');
		
		for(var i=0,len=feeNamesArray.length;i<len;i++){
			var total = 0;
			this.invoiceDetailStore.each(function(record){
				if(record.get('fee_name') == feeNamesArray[i]){
					total += record.get('real_pay');
				}
			}, this);
			this.invoiceDetailStore.each(function(record){
				if(record.get('fee_name') == feeNamesArray[i]){
					record.set('total', Ext.util.Format.usMoney( Ext.util.Format.formatFee(total) ));
				}
			}, this);
		}
		
	}
})
