/**
 * 定额发票调账
 * @class InvalidInvoice
 * @extends Ext.Panel
 */

QuotaAdjustPanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		QuotaAdjustPanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title='定额票调账';
		this.form.insert(0,{columnWidth:1,layout:'form',border:false,items:[
						{xtype:'numberfield',fieldLabel:'调账金额',name:'amount',allowBlank:false
						
						}]});
		this.items.itemAt(0).anchor = '100% 25%';
		this.items.itemAt(1).anchor = '100% 75%';
		this.doLayout();
	}
});

QuotaInvoiceAdjust = Ext.extend(Ext.Panel,{
	constructor:function(){
		QuotaInvoiceAdjust.superclass.constructor.call(this,{
			id:'QuotaInvoiceAdjust',
			title:'定额票调账',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				new QuotaAdjustPanel("QUOTA_ADJUST")
			]
		});
	}
});