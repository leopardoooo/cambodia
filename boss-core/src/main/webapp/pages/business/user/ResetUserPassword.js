/**
 *修改宽带密码
 */

EditPasswordForm = Ext.extend(BaseForm, {
	url : root + '/core/x/User!saveEditPwd.action',
	netType : null,//新接入方式
	constructor : function() {
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		EditPasswordForm.superclass.constructor.call(this, {
			border : false,
			bodyStyle : 'padding-top:10px',
			defaults : {
				layout : 'form',
				border : false,
				labelWidth : 150,
				bodyStyle : "background:#F9F9F9;"
			},
			items : [{
				xtype : 'textfield',
				fieldLabel : '登录账号',
				style : Constant.TEXTFIELD_STYLE,
				value : record.get('user_name')
			},{
						fieldLabel : '新密码',
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
						fieldLabel : '确认密码',
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
	getValues : function() {
		var obj = {};
		obj['login_password'] = Ext.getCmp('password').getValue();
		return obj;
	},
	success : function() {
		App.getApp()
				.refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function() {
			var form = new EditPasswordForm();
			var box = TemplateFactory.gTemplate(form);
		});