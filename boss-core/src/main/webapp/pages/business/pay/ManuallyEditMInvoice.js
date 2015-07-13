/**
 * 手工修改出票方式为手工的发票.
 * @class ManuallyEditMInvoice
 * @extends BaseForm
 */
ManuallyEditMInvoice = Ext.extend(BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/Pay!manuallyEditMInvoice.action",
	record : null,
	constructor : function() {
		//取合同管理选中数据
		if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.feeUnitpreGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.valuableCardGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}else{
			this.record = App.getData().currentRec;
		}
		
		ManuallyEditMInvoice.superclass.constructor.call(this, {
			border : false,
			labelWidth: 100,
			layout: 'form',
			bodyStyle: Constant.TAB_STYLE,
			defaults: {
                layout:'form',
                baseCls: 'x-plain',
                labelWidth: 100
            },
			items:[{
				id : 'oldInvoicePanel',
				items :[{
					id:'old_invoice_id',
					xtype: 'textfield',
					name:'oldInvoice.invoice_id',
					fieldLabel:'原发票号码',
					style : Constant.TEXTFIELD_STYLE,
					value : this.record.get('invoice_id')
				},{
					id:'old_invoice_code_id',
					xtype: 'textfield',
					name:'oldInvoice.invoice_code',
					fieldLabel:'原发票代码',
					style : Constant.TEXTFIELD_STYLE,
					value : this.record.get('invoice_code')
				},{
					id:'old_invoice_book_id',
					name:'oldInvoice.invoice_book_id',xtype : 'hidden',
					value : this.record.get('invoice_book_id')
					
				},{xtype:'hidden',name:'fee_sn',value:this.record.get('fee_sn')}]
			},{
				id : 'newInvoicePanel',
				items :[{
					id : 'new_invoice_id',allowBlank:false,
					name : 'newInvoice.invoice_id',
					fieldLabel : '新发票号码',xtype: 'textfield'
				},{
					id : 'new_invoice_code',
					xtype : 'textfield',allowBlank:false,
					name:'newInvoice.invoice_code',
					fieldLabel:'新发票代码'
				}
				]
			}]
		});
	},
	success:function(){
		if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			App.getApp().main.feeUnitpreGrid.getStore().reload();
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			App.getApp().main.valuableCardGrid.getStore().reload();
		}else{
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		}
	}
});

Ext.onReady(function(){
	var form = new ManuallyEditMInvoice();
	TemplateFactory.gTemplate(form);
});