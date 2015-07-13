/**
 * 发票调拨
 * @class
 * @extends Ext.form.FormPanel
 */
						
QuotaInvoiceChangePanel = Ext.extend(CommonInvoicePanel,{
	initComponent:function(){
		QuotaInvoiceChangePanel.superclass.initComponent.call(this);
		this.form.insert(0,{columnWidth:.4,layout:'form',border:false,items:[
						{xtype:'combo',fieldLabel:'调拨对象',hiddenName:'transDepotId',
							store:new Ext.data.JsonStore({
								url:'resource/Invoice!queryQuotaInvoiceTransDepot.action',
								fields:['dept_id','dept_name','dept_type']
							}),displayField:'dept_name',valueField:'dept_id',allowBlank:false,
							triggerAction:'all',mode:'local',
							listeners:{
								scope:this,
								select:function(combo,record){
									//获得当前登录操作员的部门ID
									var optrDeptId = App.data.optr['dept_id'] ;
									var deptType = record.get('dept_type');
									var deptId = record.get('dept_id');
									var optrComp = this.form.getForm().findField('optrId');
									if(deptType == 'YYT' && optrDeptId ==deptId ){
										optrComp.enable();
										optrComp.getStore().load({
											params:{
												deptId : combo.getValue()
											}
										});
									}else if(deptType == 'FGS' || optrDeptId !=deptId){
										optrComp.disable();
									}
								}
							}
						}]});
		this.form.insert(1,{columnWidth:.3,layout:'form',border:false,items:[
						{xtype:'combo',fieldLabel:'营业员',hiddenName:'optrId',
							store:new Ext.data.JsonStore({
								url:'resource/Invoice!getByDeptId.action',
								fields:['optr_id','optr_name']
							}),displayField:'optr_name',valueField:'optr_id',
							triggerAction:'all',mode:'local'
						}]});
		
		this.items.itemAt(0).anchor = '100% 25%';
		this.doLayout();
		this.form.getForm().findField('transDepotId').getStore().load();
	}
});

QuotaInvoiceChange = Ext.extend(Ext.Panel,{
	constructor:function(){
		QuotaInvoiceChange.superclass.constructor.call(this,{
			id:'QuotaInvoiceChange',
			title:'定额发票调拨',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[new QuotaInvoiceChangePanel("QUOTA_TRANS")]
		});
	}
});
