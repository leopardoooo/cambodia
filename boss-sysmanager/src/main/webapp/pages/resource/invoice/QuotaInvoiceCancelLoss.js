/*
*定额票取消挂失
*/

QuotaInvoiceCancelLoss = Ext.extend(Ext.Panel,{
	panel:null,
	constructor:function(){
		this.panel = new CommonInvoicePanel("QUOTA_CANCEL_LOSS");
		QuotaInvoiceCancelLoss.superclass.constructor.call(this,{
			id:'QuotaInvoiceCancelLoss',
			title:'定额票取消挂失',
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