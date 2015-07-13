/**
 * 修改调账原因
 */

EditAdjustReasonForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!editAdjustReason.action",
	constructor:function(){
		var record = App.getApp().main.infoPanel.getAcctPanel().acctItemDetailTab.acctItemAdjustGrid.getSelectionModel().getSelected();
		
		var doneCode = record.get('done_code');
		this.oldReason = record.get('reason');
		var oldReasonText = record.get('reason_text');
		EditAdjustReasonForm.superclass.constructor.call(this,{
			labelWidth:65,
			bodyStyle: 'padding-top:10px',
			items:[{
				xtype:'hidden',
				name:'doneCode',
				value:doneCode
			},{
				xtype:'hidden',
				name:'oldReason',
				value:this.oldReason
			},{
				xtype : 'textfield',
				fieldLabel:'旧原因',
				value:oldReasonText,
				readOnly:true
			},{
				xtype : 'paramcombo',
				fieldLabel:'新原因',
				paramName:'ADJUST_REASON',
				hiddenName:'reason',
				width:250,height:150
			}]
		});
	},
	doInit: function(){
		EditAdjustReasonForm.superclass.doInit.call(this);
		var store = this.getForm().findField('reason').getStore();
		store.removeAt(store.find('item_value',this.oldReason));
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var panel = new EditAdjustReasonForm();
	TemplateFactory.gTemplate(panel);
});