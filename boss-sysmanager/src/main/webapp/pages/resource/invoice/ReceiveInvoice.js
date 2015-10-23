/**
 * 发票领用
 * @class
 * @extends Ext.form.FormPanel
 */
ReceiveInvoicePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		ReceiveInvoicePanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title=lsys('TransferInvoice._title');
		this.form.insert(0,{columnWidth:1,layout:'form', labelWidth:120,border:false,items:[
		                        {xtype: 'hidden', name:'transDepotId', value: App.data.optr['dept_id'] },
	     						{xtype:'combo',fieldLabel:lsys('ReceiveInvoice.optrId'),hiddenName:'optrId',allowBlank: false,
	     							store:new Ext.data.JsonStore({
	     								baseParams: {
	     									deptId : App.data.optr['dept_id']
	     								},
	     								autoLoad: true,
	     								url:'resource/Invoice!getByDeptId.action',
	     								fields:['optr_id','optr_name']
	     							}),displayField:'optr_name',valueField:'optr_id',
	     							triggerAction:'all',mode:'local'
	     						}]});
		this.items.itemAt(0).anchor = '100% 25%';
		this.items.itemAt(1).anchor = '100% 75%';
		this.doLayout();
	}
});

ReceiveInvoice = Ext.extend(Ext.Panel,{
	receiveInvoicePanel:null,
	constructor:function(){
		this.receiveInvoicePanel = new ReceiveInvoicePanel("RECEIVE");
		ReceiveInvoice.superclass.constructor.call(this,{
			id:'ReceiveInvoice',
			title:lsys('ReceiveInvoice._title'),
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[this.receiveInvoicePanel]
		});
	}
});
