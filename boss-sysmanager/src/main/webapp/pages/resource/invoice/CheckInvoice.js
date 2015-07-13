/**
 * 发票结账
 * @class CheckInvoice
 * @extends Ext.Panel
 */
CheckInvoice = Ext.extend(Ext.Panel,{
	checkInvoicePanel:null,
	constructor:function(){
		this.checkInvoicePanel = new CommonInvoicePanel("CHECK");
		CheckInvoice.superclass.constructor.call(this,{
			id:'CheckInvoice',
			title:'发票结账',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				this.checkInvoicePanel
			]
		});
	}
});