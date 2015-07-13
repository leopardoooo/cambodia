/**
 * 取消核销
 * @class CancelCloseInvoice
 * @extends Ext.Panel
 */
CancelCloseInvoice = Ext.extend(Ext.Panel,{
	panel:null,
	constructor:function(){
		this.panel = new CommonInvoicePanel("CANCELCLOSE");
		CancelCloseInvoice.superclass.constructor.call(this,{
			id:'CancelCloseInvoice',
			title:'取消核销',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				this.panel
			]
		});
	}
});