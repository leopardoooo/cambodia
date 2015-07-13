/**
 * 发票领用
 * @class
 * @extends Ext.form.FormPanel
 */
CancelReceiveInvoicePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		CancelReceiveInvoicePanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title='发票调拨';
		this.form.insert(0,{columnWidth:1,layout:'form',border:false,items:[
		                        {xtype: 'hidden', name:'transDepotId', value: App.data.optr['dept_id'] },
	     						{xtype:'combo',fieldLabel:'领用营业员',hiddenName:'optrId',allowBlank: false,
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

CancelReceiveInvoice = Ext.extend(Ext.Panel,{
	CancelReceiveInvoicePanel:null,
	constructor:function(){
		this.cancelReceiveInvoicePanel = new CancelReceiveInvoicePanel("CANCEL_RECEIVE");
		CancelReceiveInvoice.superclass.constructor.call(this,{
			id:'CancelReceiveInvoice',
			title:'取消领用',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[this.cancelReceiveInvoicePanel]
		});
	}
});
