
/**
 * 封装发票信息表单
 * @class PayBillPanel
 * @extends Ext.BaseForm
 */
InvoiceForm = Ext.extend( BaseForm , {
	constructor: function(){
		InvoiceForm.superclass.constructor.call(this, {
			border: false,
			labelWidth: 100,
			items:[{
				fieldLabel: '发票号码',
				xtype: 'textfield',
				name: 'invoice_id',
				allowBlank: false
				
			},{
				fieldLabel: '发票代码',
				xtype: 'textfield',
				allowBlank: false,
				name: 'invoice_code'
			}]
		});
	},
	//根据所传递的参数，禁用或启动发票信息输入框
	setDisabledField: function( b ){
		var id  = this.items.get(0);
		id.setDisabled( b );
		var code = this.items.get(1);
		code.setDisabled( b );
		
		id.allowBlank = b;
		code.allowBlank = b;
		if( b ){
			id.clearInvalid();
			code.clearInvalid();
		} else {
			id.focus();
		}
	}
});