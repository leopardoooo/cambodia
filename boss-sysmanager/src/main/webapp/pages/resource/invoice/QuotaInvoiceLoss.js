/*
*定额票挂失
*/

QuotaInvoiceLoss = Ext.extend(Ext.Panel,{
	panel:null,
	constructor:function(){
		this.panel = new CommonInvoicePanel("QUOTA_LOSS");
		QuotaInvoiceLoss.superclass.constructor.call(this,{
			id:'QuotaInvoiceLoss',
			title:'定额票挂失',
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