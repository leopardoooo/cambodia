/**
 * 发票作废
 * @class InvalidInvoice
 * @extends Ext.Panel
 */

EditStatusInvoicePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		EditStatusInvoicePanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title='修改状态';
		this.form.insert(0,{columnWidth:.5,layout:'form',border:false,items:[
						{xtype:'paramcombo',fieldLabel:'发票类型',hiddenName:'invoiceType',
							paramName:'INVOICE_TYPE',allowBlank:false,
							listeners:{
								scope:this,
								select:function(combo){
									var value = combo.getValue();
									var statusComp = this.form.getForm().findField('status');
									statusComp.enable();
									statusComp.setValue('');
									
									store = statusComp.getStore();
									store.clearFilter();
									
									//只有 定额发票 才有"使用"状态
									if(value != '5'){
										store.filterBy(function(r){
											return r.get('status') != 'USE';
										});
									}
								}
							}
						}]});
		this.form.insert(1,{columnWidth:.5,layout:'form',border:false,items:[
						{xtype:'combo',fieldLabel:'使用状态',hiddenName:'status',disabled:true,
							store:new Ext.data.ArrayStore({
								fields:['status','status_text'],
								data:[['IDLE','空闲'],['INVALID','失效'],['USE','使用']]
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
			title:'修改状态',
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