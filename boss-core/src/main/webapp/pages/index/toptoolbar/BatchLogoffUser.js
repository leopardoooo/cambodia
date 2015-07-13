BatchLogoffUserForm = Ext.extend(Ext.form.FormPanel,{
	constructor : function(){
		BatchLogoffUserForm.superclass.constructor.call(this,{
			layout : 'form',
			bodyStyle : Constant.TAB_STYLE,
			border : false,
			fileUpload: true,
			items : [{
				id : 'bathcLogoffUserId',
				xtype:'textfield',
				inputType:'file',
				fieldLabel:'用户ID文件',
				allowBlank : false,
				name:'file',
				anchor:'90%',
				buttonText:'浏览...'
			},{
				fieldLabel:'回收设备',
				hiddenName:'isReclaimDevice',
				xtype:'paramcombo',
				paramName:'BOOLEAN',
				allowBlank:false
			},{
				fieldLabel:'设备状态',
				hiddenName:'deviceStatus',
				xtype:'paramcombo',
				paramName:'DEVICE_STATUS_R_DEVICE',
				defaultValue:'ACTIVE'
			},{
				xtype : 'textarea',
				fieldLabel : '备注',
				height : 100,
				width : 300,
				name : 'remark'
			}]
		})
		App.form.initComboData([this.getForm().findField('isReclaimDevice'),this.getForm().findField('deviceStatus')]);
	},
	doValid : function(){
		var flag = true;
		if(this.getForm().isValid()){
			var fileText = Ext.getCmp('bathcLogoffUserId').getValue();
			if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
				Alert('请选择excel文件进行上传！');
				flag =  false;
			}
		}else{
			flag =  false;
		}
		return flag
	},
	getValues : function(){
		var all =  this.getForm().getValues();
		return all;
	}
})

var BatchLogoffUserWin = Ext.extend(Ext.Window,{
	form : null,
	constructor:function(){
		this.form = new BatchLogoffUserForm();
		BatchLogoffUserWin.superclass.constructor.call(this,{
			title:'批量销用户',
			border:false,
			width:500,
			height:300,
			layout:'fit',
			items:[this.form],
			closeAction:'close',
//			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:this.closeWin}
			]
		});
	},
	doSave:function(){
		if(this.form.doValid()){
			var values = this.form.getValues();
			var ps={},all={};
			ps['busiCode'] = '1913';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			all['isReclaimDevice'] = values['isReclaimDevice'];
			all['deviceStatus'] = values['deviceStatus'];
			
			var msg = Show();
			this.form.getForm().submit({
				url:root+'/core/x/User!batchLogoffUser.action',
				scope:this,
				params:all,
				success:function(form,action){
					msg.hide();msg = null;
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('修改成功!',function(){
								this.closeWin();
							},this);
						}
					}
				}
			});
		}
		
	},
	closeWin:function(){
		this.close();
	}
});