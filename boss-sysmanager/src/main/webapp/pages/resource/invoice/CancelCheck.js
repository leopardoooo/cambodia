/**
 * 取消结账
 * @class CancelCheck
 * @extends Ext.Panel
 */
CancelCheck = Ext.extend(Ext.Panel,{
	panel:null,
	constructor:function(){
		this.panel = new CommonInvoicePanel("CANCELCHECK");
		CancelCheck.superclass.constructor.call(this,{
			id:'CancelCheck',
			title:lsys('CancelCheck._title'),
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