var InvoiceDetailForm = Ext.extend(Ext.form.FormPanel, {
	parent : null,
	invoiceStore : null,
	constructor : function(p) {
		this.parent = p;
		this.invoiceStore = new Ext.data.JsonStore({
					fields : ['invoice_id', 'invoice_book_id', 'invoice_code']
				});
		InvoiceDetailForm.superclass.constructor.call(this, {
			border : false,
			layout : 'column',
			labelWidth : 120,
			bodyStyle : 'padding:5px;padding-top : 10px',
			items : [{
						style: 'margin-left: 300px',
						columnWidth : .6,
						layout : 'form',
						border : false,
						items : [{
									xtype : 'textfield',
									fieldLabel : lsys('InvoiceCommon.invoice_id'),
									allowBlank : false,
									name : 'invoice_id',
									width:150,
									vtype : 'invoiceId',
									enableKeyEvents : true,
									listeners : {
										scope : this,
										specialkey : function(field, e) {
											if (e.getKey() === Ext.EventObject.ENTER) {
												this.doQuery();
											}
										},
//										keyup : this.checkInvoice,
										change : this.checkInvoice
									}
								}]
					},/*{
						columnWidth : .3,
						layout : 'form',
						border : false,
						items : [{
									fieldLabel : lsys('InvoiceCommon.invoice_id'),
									xtype : 'combo',
									store : new Ext.data.JsonStore({
												fields : ['invoice_code']
											}),
									hiddenName : 'invoice_code',
									id : 'invoiceCodeId',
									displayField : 'invoice_code',
									valueField : 'invoice_code',
									listeners : {
										scope : this,
										select : this.changeCode
									}
								}]
					},*/ {
						columnWidth : .2,
						border : false,
						items : [{
									xtype : 'button',
									text : lsys('common').query,
									iconCls : 'icon-query',
									listeners : {
										scope : this,
										click : this.doQuery
									}
								}]
					}]
		});
	},
	checkInvoice : function(invoiceIdCmp, e) {
		var invoiceId = invoiceIdCmp.getValue();
		if (!Ext.form.VTypes.invoiceId(invoiceId))
			return;

//		var invoiceCodeCmp = this.getForm().findField('invoice_code');
		Ext.Ajax.request({
					url : 'resource/Invoice!queryInvoiceById.action',
					params : {
						invoiceId : invoiceId
					},
					scope : this,
					success : function(res, opt) {
						var data = Ext.decode(res.responseText);
						if (data.length == 0) {
							Alert(lsys('msgBox.invoiceNotExists',null,[invoiceId]), function() {
										/*invoiceCodeCmp.setReadOnly(false);
										invoiceCodeCmp.getStore().removeAll();
										invoiceCodeCmp.setValue('');*/
										invoiceIdCmp.focus(true, 10);
									});
						} else {
							this.invoiceStore.loadData(data);
							/*invoiceCodeCmp.getStore().loadData(data);
							invoiceCodeCmp.setValue(invoiceCodeCmp.getStore()
									.getAt(0).get('invoice_code'));*/
						}
					}
				});
	},
	changeCode : function(com) {
		this.invoiceStore.each(function(record) {
					var invoice_id = record.get('invoice_id');
					var invoice_code = record.get('invoice_code');
				})
	},
	doQuery : function() {
		if (!this.getForm().isValid())
			return;
		var invoiceCodeCmp = this.getForm().findField('invoice_code');
		/*var invoice_code = invoiceCodeCmp.getValue();
		if(!invoice_code){
			Alert(lsys('msgBox.selectInvoiceCode'));
			return false;
		}*/
		var invoiceIdCmp = this.getForm().findField('invoice_id');
		this.parent.queryInvoiceDetail(invoiceIdCmp.getValue(),'AAA');

		}
	});


InvoiceDepotDetailGrid = Ext.extend(Ext.grid.GridPanel, {
			invoiceDepotStore : null,
			constructor : function() {
				this.invoiceDepotStore = new Ext.data.JsonStore({
							fields : ['optr_type', 'optr_name', 'depot_name',
									'create_time']
						});
				InvoiceDepotDetailGrid.superclass.constructor.call(this, {
							ds : this.invoiceDepotStore,
							viewConfig : {
								forceFit :true
							},
							sm : new Ext.grid.CheckboxSelectionModel(),
							cm : new Ext.grid.ColumnModel([{
										header : lsys('InvoiceDetail.InvoiceDepotDetailGridColls')[0],
										dataIndex : 'optr_type',
										width : 70,
										renderer: App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDepotDetailGridColls')[1],
										dataIndex : 'depot_name',
										width: 120,
										renderer: App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDepotDetailGridColls')[2],
										dataIndex : 'optr_name',
										width: 80,
										renderer: App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDepotDetailGridColls')[3],
										dataIndex : 'create_time',
										width: 130,
										renderer: App.qtipValue
									}])
						})
			}
		});

 InvoiceDetailGrid = Ext.extend(Ext.grid.GridPanel, {
			invoiceDetailStore : null,
			constructor : function() {
				this.invoiceDetailStore = new Ext.data.JsonStore({
							fields : ['cust_name', 'cust_no', 'busi_name','optr_id','optr_name',
									'fee_name', 'real_pay', 'create_time', 'status', 'status_text']
						});
				InvoiceDetailGrid.superclass.constructor.call(this, {
							ds : this.invoiceDetailStore,
							viewConfig : {
								forceFit :true
							},
							sm : new Ext.grid.CheckboxSelectionModel(),
							cm : new Ext.grid.ColumnModel([{
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[0],
										dataIndex : 'cust_name',
										width : 80,
										renderer : App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[1],
										dataIndex : 'cust_no',
										width: 80,
										renderer : App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[2],
										dataIndex : 'busi_name',
										width: 80,
										renderer : App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[3],
										dataIndex : 'fee_name',
										width: 120,
										renderer : App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[4],
										dataIndex : 'real_pay',
										width: 80,
										renderer : Ext.util.Format.formatFee
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[5],
										dataIndex : 'create_time',
										width: 130,
										renderer : App.qtipValue
									},{
										header:lsys('InvoiceDetail.InvoiceDetailGridColls')[6],
										dataIndex:'optr_name',
										width: 80,
										renderer : App.qtipValue
									}, {
										header : lsys('InvoiceDetail.InvoiceDetailGridColls')[7],
										dataIndex : 'status_text',
										width: 70,
										renderer : Ext.util.Format.statusShow
									}])
						})
			}
		})

var InvoiceDetailPanel = Ext.extend(Ext.Panel, {
	invoiceTpl : null,
	constructor : function() {
		this.invoiceTpl = new Ext.XTemplate(
				'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.invoice_id')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_id}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.invoice_type')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_type_text}</td>',
				'</tr>',
				/*'<tr height=24>',
				'<td class="label" width=20%>'+lsys('InvoiceCommon.invoice_code')+'：</td>',
				'<td class="input_bold" width=30%>&nbsp;{invoice_code}</td>',
				'<td class="label" width=20%>'+lsys('InvoiceCommon.invoice_mode')+'：</td>',
				'<td class="input_bold" width=30%>&nbsp;{invoice_mode_text}</td>',
				'</tr>',*/
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.status')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{status_text}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.finance_status')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{finance_status_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.depot_name')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{depot_name}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.check_depot_id')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{check_depot_id_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.create_time')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{create_time}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.close_time')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.close_time|| ""]}</td>',// fm.dateFormat(
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.check_time')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.check_time|| ""]}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.use_time')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.use_time|| ""]}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.amount')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[this.checkAmount(values)]}</td>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.use_optr')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.optr_name||""]}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>'+lsys('InvoiceCommon.open_optr_name')+'：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.open_optr_name||""]}</td>',
				'</tr>',
				'</table>', {
					checkAmount : function(values) {
						if (Ext.isEmpty(values.amount))
							return '';
						else
							return values.amount / 100;
					},
					checkInvoiceAmount : function(values) {
						if (Ext.isEmpty(values.invoice_amount))
							return '';
						else
							return values.invoice_amount / 100;
					}
				});
		InvoiceDetailPanel.superclass.constructor.call(this, {
			border : false,
			autoScroll:true,
			bodyStyle : "background:#F9F9F9",
			items : [{
				xtype : "panel",
				border : false,
				bodyStyle : "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
				html : this.invoiceTpl.applyTemplate({})
			}]
		});
	}
});
InvoiceAllInfo = Ext.extend(Ext.Panel,{
	panel : null,
	depotGrid : null,
	detail : null,
	constructor : function() {
				this.panel = new InvoiceDetailPanel();
				this.depotGrid = new InvoiceDepotDetailGrid();
				 this.detail = new InvoiceDetailGrid();
	InvoiceAllInfo.superclass.constructor.call(this, {
			border : false,
			layout : 'border',
			closable : true,
			items : [{
								region : 'north',
								height : 200,
								split : true,
								autoScroll:true,
								title : lsys('InvoiceDetail.titleInvoiceBaseInfo'),
								items : [this.panel]
							}, {
								region : 'center',
								layout : 'border',
								items : [{
											region : 'west',
											layout : 'fit',
											width : '40%',
											split : true,
											title : lsys('InvoiceDetail.titleInvoiceOptrRec'),
											items : [this.depotGrid]
										}, {
											region : 'center',
											layout : 'fit',
											title : lsys('InvoiceDetail.titleFeeDetail'),
											items : [this.detail]
										}]
							}]
	})
	},
	queryInvoiceDetail:function (invoiceId,invoiceCode){
		Ext.Ajax.request({
				url : 'resource/Invoice!queryInvoiceByInvoiceId.action',
				params : {
					invoiceId : invoiceId,
					invoiceCode : invoiceCode
				},
				scope : this,
				success : function(res, opt) {
					var data = Ext.decode(res.responseText);
					var item = this.panel.items.itemAt(0);
					var tpl = this.panel.invoiceTpl;
					if (data) {
						var invoiceDepotDetailGrid = this.depotGrid;
						var depot = data.invoiceDepotList;
						invoiceDepotDetailGrid.getStore().removeAll();
						if (depot && depot.length > 0) {
							var depotdata = [];
							for (var i = 0; i < depot.length; i++) {
								var obj = {};
								obj['optr_type'] = depot[i].optr_type;
								obj['optr_name'] = depot[i].optr_name;
								obj['depot_name'] = depot[i].depot_name;
								obj['create_time'] = depot[i].create_time;
								depotdata.push(obj);
							}
							invoiceDepotDetailGrid.getStore().loadData(depotdata);
						}
						
						var invoiceDetailGrid = this.detail;
						var detail = data.invoiceDetailList;
						invoiceDetailGrid.getStore().removeAll();
						if(detail && detail.length > 0){
							 var detaildata = [];
							 for(var i=0;i<detail.length;i++){
								 var obj = {};
								 obj['cust_name'] = detail[i].cust_name;
								 obj['cust_no'] = detail[i].cust_no;
								 obj['busi_name'] = detail[i].busi_name;
								 obj['fee_name'] = detail[i].fee_name;
								 obj['real_pay'] = detail[i].real_pay;
								 obj['create_time'] = detail[i].create_time;
								 obj['status'] = detail[i].status;
								 obj['status_text'] = detail[i].status_text;
								 obj['optr_id'] = detail[i].optr_id;
								 obj['optr_name'] = detail[i].optr_name;
								 detaildata.push(obj);
							 }
							 invoiceDetailGrid.getStore().loadData(detaildata);
						 }

						if (item.getEl()) {
							tpl.overwrite(item.body, data);
						}
					}
				}
			});
	}
})



InvoiceDetailInfo = Ext.extend(Ext.Panel, {
			form : null,
			info:null,
			queryInvoiceDetail:function(invoiceId,invoiceCode){
				this.info.queryInvoiceDetail(invoiceId,invoiceCode);
			},
			constructor : function() {
				
				this.form = new InvoiceDetailForm(this);
				this.info = new InvoiceAllInfo();
				InvoiceDetailInfo.superclass.constructor.call(this, {
					id : 'InvoiceDetailInfo',
					title : lsys('InvoiceDetail.titleDetailQuery'),
					border : false,
					layout : 'border',
					closable : true,
					items : [{
								region : 'north',
								height : 50,
								layout : 'fit',
								split : true,
								items : [this.form]
							}, {
								region : 'center',
								layout : 'fit',
								items : [this.info]
							}]
					});
				 this.form.getForm().findField('invoice_id').focus(true, 100);
			}
		});