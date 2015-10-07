/**
 * 发票核销
 * @class CloseInvoice
 * @extends Ext.Panel
 */
CloseInvoice = Ext.extend(Ext.Panel,{
	cancelInvoicePanel:null,
	constructor:function(){
		this.cancelInvoicePanel = new CommonInvoicePanel("CLOSE");
		CloseInvoice.superclass.constructor.call(this,{
			id:'CloseInvoice',
			title:lsys('CloseInvoice._title'),
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				this.cancelInvoicePanel
			]
		});
	}
});