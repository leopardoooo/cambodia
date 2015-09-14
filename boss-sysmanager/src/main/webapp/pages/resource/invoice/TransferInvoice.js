/**
 * 发票调拨
 * @class
 * @extends Ext.form.FormPanel
 */
						
TransferInvoicePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		TransferInvoicePanel.superclass.initComponent.call(this);
		this.items.itemAt(0).title=lsys('TransferInvoice._title');
		this.form.insert(0,{columnWidth:1,layout:'form',border:false,items:[
						{xtype:'combo',fieldLabel:lsys('TransferInvoice.transDepotId'),hiddenName:'transDepotId',width :150,minListWidth :250,
							store:new Ext.data.JsonStore({
								url:'resource/Device!queryDeptByOptr.action',
								fields:['dept_id','dept_name']
							}),displayField:'dept_name',valueField:'dept_id',allowBlank:false,
							triggerAction:'all',mode:'local'
//							,listeners:{
//								scope:this,
//								select:function(combo,record){
//									//获得当前登录操作员的部门ID
//									var optrDeptId = App.data.optr['dept_id'] ;
//									var deptType = record.get('dept_type');
//									var deptId = record.get('dept_id');
//									var optrComp = this.form.getForm().findField('optrId');
//									if(deptType == 'YYT' && optrDeptId ==deptId ){
//										optrComp.enable();
//										optrComp.getStore().load({
//											params:{
//												deptId : combo.getValue()
//											}
//										});
//									}else if(deptType == 'FGS' || optrDeptId !=deptId){
//										optrComp.disable();
//									}
//								}
//							}
						}]});
		/*this.form.insert(1,{columnWidth:.3,layout:'form',border:false,items:[
	     						{xtype:'combo',fieldLabel:'营业员',hiddenName:'optrId',
	     							store:new Ext.data.JsonStore({
	     								url:'resource/Invoice!getByDeptId.action',
	     								fields:['optr_id','optr_name']
	     							}),displayField:'optr_name',valueField:'optr_id',
	     							triggerAction:'all',mode:'local'
	     						}]});*/
		this.items.itemAt(0).anchor = '100% 25%';
		this.items.itemAt(1).anchor = '100% 75%';
		this.doLayout();
		this.form.getForm().findField('transDepotId').getStore().load();
	}
});

TransferInvoice = Ext.extend(Ext.Panel,{
	transferInvoicePanel:null,
	constructor:function(){
		this.transferInvoicePanel = new TransferInvoicePanel("TRANS");
		TransferInvoice.superclass.constructor.call(this,{
			id:'TransferInvoice',
			title:lsys('TransferInvoice._titleSimple'),
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[this.transferInvoicePanel]
		});
	}
});
