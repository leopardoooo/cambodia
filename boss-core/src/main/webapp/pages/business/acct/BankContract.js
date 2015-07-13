/**
 * 银行签约
 * @class BankContractForm
 * @extends Ext.form.FormPanel
 */
BankContractForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Acct!signBank.action",
	constructor:function(){
		BankContractForm.superclass.constructor.call(this,{
			border:false,
			labelWidth: 85,
			layout : 'column',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				layout:'form',
				columnWidth:.5,
				items:[{
					fieldLabel:'银行代码',
					xtype:'paramcombo',
					emptyText: '请选择',
					allowBlank : false,
					paramName:'BANK_CODE',
					hiddenName:'bank_code'
				},{
					fieldLabel:'银行账号',
					allowBlank : false,
					xtype:'textfield',
					id:'bank_account'
				},{
					fieldLabel:'银行账户名称',
					allowBlank : false,
					xtype:'textfield',
					id:'account_name'
				}]
			},{
				layout:'form',
				columnWidth:.5,
				items:[{
					fieldLabel:'签约证件号',
					allowBlank : false,
					xtype:'textfield',
					id:'cert_num'
				},{
					fieldLabel:'签约日期',
					editable:false,
					allowBlank : false,
					xtype:'datefield',
					format:'Y-m-d',
					width:125,
					id:'sign_time'
				}]
			}]
		});
	}
});

Ext.onReady(function(){
	var bank = new BankContractForm();
	var box = TemplateFactory.gTemplate(bank);
});