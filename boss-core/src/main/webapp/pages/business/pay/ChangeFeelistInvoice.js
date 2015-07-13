/**
 * 收费清单换发票
 * @class
 * @extends Ext.form.FormPanel
 */
var ChangeFeelistInvoiceForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Pay!changeFeelistInvoice.action",
	constructor: function(){
		ChangeFeelistInvoiceForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			items:[
				{xtype:'textfield',fieldLabel:'清单号',name:'feelist_id',readOnly:true},
				{xtype:'textfield',fieldLabel:'清单代码',name:'feelist_code',readOnly:true},
				{xtype:'textfield',fieldLabel:'清单本号',name:'feelist_book_id',readOnly:true},
				{xtype:'textfield',fieldLabel:'发票号码',name:'invoice_id',allowBlank:false,vtype:'invoiceId'},
				{xtype:'textfield',fieldLabel:'发票代码',name:'invoice_code',allowBlank:false},
				{xtype:'textarea',fieldLabel:'备注',name:'remark',width:350,height:60}
			]
		});
		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		var form = this.getForm();
		form.findField('feelist_id').setValue(record.get('invoice_id'));
		form.findField('feelist_code').setValue(record.get('invoice_code'));
		form.findField('feelist_book_id').setValue(record.get('invoice_book_id'));
	}
});

Ext.onReady(function(){
	var panel = new ChangeFeelistInvoiceForm();
	var box = TemplateFactory.gTemplate(panel);
});