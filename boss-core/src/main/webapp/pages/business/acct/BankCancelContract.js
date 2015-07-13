/**
 * 银行解约
 * @class BankCancelForm
 * @extends Ext.form.FormPanel
 */
BankCancelForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH + "/core/x/Bank!cancelSign.action",
	acctBank : null,
	constructor:function(){
		BankCancelForm.superclass.constructor.call(this,{
			id : 'signInfo',
			layout : 'column',
			labelWidth: 95,
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			region : 'center',
			defaults:{
				layout : 'form',
           		border:false,
           		bodyStyle:'background:#F9F9F9;',
           		defaults : {
           			width : 150
           		}
           	},
			items :[{
				columnWidth:.5,
				items:[{
					fieldLabel:'银行账号',
					allowBlank : false,
					xtype:'displayfield',
					maxLength : 20,
					name:'bank_account'
				},{
					fieldLabel:'持卡人姓名',
					allowBlank : false,
					xtype:'displayfield',
					maxLength : 50,
					name:'account_name'
				}]
			},{
				columnWidth:.5,
				items:[{
					fieldLabel:'银行名称',
					xtype:'displayfield',
					name:'bank_code_text',
					allowBlank : false
				},{
					fieldLabel:'持卡人证件号',
					xtype:'displayfield',
					maxLength : 32,
					name : 'cert_num'
				}]
			}]
		});
		
	},
	doInit:function(){
		this.acctBank=App.getApp().data.custFullInfo.acctBank;
		if(this.acctBank && this.acctBank.cust_id){
			this.getForm().loadRecord(new Ext.data.Record(this.acctBank));
		}
	},
	getValues : function(){
		var all = {};
		all['acctPayType'] = 'YHDK';
		all['optionType'] = '2';
		return all;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var bank = new BankCancelForm();
	var box = TemplateFactory.gTemplate(bank);
});