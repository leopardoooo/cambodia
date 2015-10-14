/**
 *修改宽带密码
 */

EditPasswordForm = Ext.extend(BaseForm, {
	url : root + '/core/x/User!saveEditPwd.action',
	constructor : function() {
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.oldLoginName = record.get('login_name');
		EditPasswordForm.superclass.constructor.call(this, {
			border : false,
			bodyStyle : 'padding-top:10px',
			labelWidth : 150,
			defaults : {
				layout : 'form',
				border : false,
				bodyStyle : "background:#F9F9F9;"
			},
			items : [{
				xtype : 'textfield',
				fieldLabel : lmain("user._form.loginId"),
				name: 'login_name',
				id: 'loginNameId',
				allowBlank: false,
				value : record.get('login_name'),
                listeners: {
                	scope: this,
                	change: this.validAccount
                }
			},{
				fieldLabel : lbc("common.newPswd"),
				inputType : 'password',
				xtype : 'textfield',
				name : 'password',
				id : 'password',
				allowBlank : false,
				listeners : {
					scope : this,
					change : function(txt) {
						if (!Ext.isEmpty(txt.getValue())) {
							Ext.getCmp('confirmPwd').allowBlank = false;
						} else {
							Ext.getCmp('confirmPwd').allowBlank = true;
						}
					}

				}
			}, {
				fieldLabel : lbc("common.confirmPswd"),
				inputType : 'password',
				xtype : 'textfield',
				name : 'confirmPwd',
				id : 'confirmPwd',
				allowBlank : false,
				vtype : 'password',
				initialPassField : 'password'
			}]
		});
	},
	validAccount: function(field){
		var loginName = field.getValue();
		if(loginName != this.oldLoginName){
			Ext.Ajax.request({
				url: root + '/core/x/User!validAccount.action',
				params: {
					loginName: loginName
				},
				success: function(res,opt){
					field.focus();
				}
			});
		}
	},
	getValues : function() {
		var obj = {};
		obj['login_name'] = Ext.getCmp('loginNameId').getValue();
		obj['login_password'] = Ext.getCmp('password').getValue();
		return obj;
	},
	success : function() {
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function() {
			var form = new EditPasswordForm();
			var box = TemplateFactory.gTemplate(form);
		});