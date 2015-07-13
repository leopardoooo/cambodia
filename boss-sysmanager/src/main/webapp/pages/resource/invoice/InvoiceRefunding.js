
InvoiceRefunding = Ext.extend(Ext.Panel,{
	constructor: function(){
		this.refundingInvoicePanel = new CommonInvoicePanel("REFUND");
		InvoiceRefunding.superclass.constructor.call(this,{
			id:'InvoiceRefunding',
			title:'发票退库',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				this.refundingInvoicePanel
			]
		});
	}
});