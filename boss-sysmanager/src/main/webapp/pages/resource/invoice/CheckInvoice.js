/**
 * 发票结账
 * @class CheckInvoice
 * @extends Ext.Panel
 */
CheckInvoice = Ext.extend(Ext.Panel,{
	panel:null,
	constructor:function(){
		this.panel = new CommonInvoicePanel("CHECK");
		CheckInvoice.superclass.constructor.call(this,{
			id:'CheckInvoice',
			title:lsys('CheckInvoice._title'),
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