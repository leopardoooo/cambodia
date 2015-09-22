/**
 * 发票作废
 * @class InvalidInvoice
 * @extends Ext.Panel
 */

EditStatusInvoicePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		EditStatusInvoicePanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title=lsys('EditStatusInvoice._title');
		this.form.insert(0,{columnWidth:.5,layout:'form',border:false,items:[
						{xtype:'paramcombo',fieldLabel:lsys('InvoiceCommon.invoice_type'),hiddenName:'invoiceType',
							paramName:'INVOICE_TYPE',allowBlank:false,defaultValue:'2',
							listeners:{
								scope:this,
								select:function(combo){
									var value = combo.getValue();
//									var statusComp = this.form.getForm().findField('status');
//									statusComp.enable();
//									statusComp.setValue('');
//									
//									store = statusComp.getStore();
//									store.clearFilter();
//									
									//只有 定额发票 才有"使用"状态
									if(value != '5'){
										store.filterBy(function(r){
											return r.get('status') != 'USE';
										});
									}
								},
								expand:function(combo){
									var store = combo.getStore();
									store.filterBy(function(record){
										return record.get('item_value').indexOf('2')>=0;
									})
								}
							}
						}]});
		this.form.insert(1,{columnWidth:.5,layout:'form',border:false,items:[
						{xtype:'combo',fieldLabel:lsys('EditStatusInvoice.editStatus'),hiddenName:'status',
							store:new Ext.data.ArrayStore({
								fields:['status','status_text'],
								data:[['IDLE',lsys('common.statusEnum.IDLE')],['INVALID',lsys('common.statusEnum.INVALID')]]
							}),lastQuery:'',displayField:'status_text',valueField:'status',allowBlank:false
							
						}]});
		this.items.itemAt(0).anchor = '100% 25%';
		this.items.itemAt(1).anchor = '100% 75%';
		this.doLayout();
		App.form.initComboData([this.form.getForm().findField('invoiceType')],this.doInit,this);
	},
	doInit : function(){
		
	}
});

EditStatusInvoice = Ext.extend(Ext.Panel,{
	editStatusInvoicePanel:null,
	constructor:function(){
		this.editStatusInvoicePanel = new EditStatusInvoicePanel("EDITSTATUS");
		EditStatusInvoice.superclass.constructor.call(this,{
			id:'EditStatusInvoice',
			title:lsys('EditStatusInvoice._title'),
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[
				this.editStatusInvoicePanel
			]
		});
	}
});