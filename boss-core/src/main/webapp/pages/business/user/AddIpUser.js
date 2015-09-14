/**
 * 
 */
 
AddIpUserForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!saveAddIpUser.action",
	constructor: function(){
		var record = null;
		record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();

		AddIpUserForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
				xtype : 'hidden',
				value : record.get('user_id'),
				name : 'user.user_id'
			},{
				xtype : 'hidden',
				name : 'user.str5',
				value : record.get('str5'),
				id:'userStrId'
			},{
				xtype : 'textfield',
				fieldLabel : lmain("user._form.userName"),
				style : Constant.TEXTFIELD_STYLE,
				value : record.get('user_name'),
				id : 'userName'
			},{
				fieldLabel:'IP',
				name:'user.str4',
				preventScrollbars : true,
				height : 80,
				width : 350,
				allowBlank:false,
				value : record.get('str4'),
				xtype:'textarea'
			},{
				fieldLabel: lmain("user._form.chargeNum"),
				xtype:'numberfield',
				name:'user.str6',
				value : record.get('str6'),
				allowBlank:false,
				id: 'ipCountId'
			},{
                xtype: 'displayfield',
                width : 350,
                fieldLabel: lbc("common.remark"),
                id:'feeDescId'
			}]
		})
	},
	doInit: function(){
		Ext.Ajax.request({
			scope : this,
			url: Constant.ROOT_PATH+"/core/x/User!queryUserIpFee.action",
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				Ext.getCmp('userStrId').setValue(rec.fee_id);
				Ext.getCmp('feeDescId').setValue("<font style='font-size:14px'>"+lmain('user._form.feeItem')+":<b>"+rec.fee_name+"</b>,"
					+lmain('user._form.unitPrice')+":<b>"+Ext.util.Format.convertToYuan(rec.default_value)+"</b>,<br>"
					+lmain('user._form.ipFeeDesc')+"</font>");
			}
		});
		
		
		AddIpUserForm.superclass.doInit.call(this)
	},
	getValues : function(){
		var all = this.getForm().getValues();
		return all;
	},
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = lbc("common.tipFormInvalid");
		}
		if(this.getForm().findField('user.str5').getValue()==0){
			obj["isValid"] = false;
			obj["msg"] = lmsg("noChargeNumber");
		}
		return obj;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
 
 
Ext.onReady(function(){
	var cpf = new AddIpUserForm();
	var box = TemplateFactory.gTemplate(cpf);
});