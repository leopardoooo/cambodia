FileForm = Ext.extend(Ext.FormPanel,{
	constructor : function(){
		FileForm.superclass.constructor.call(this,{
			layout : 'column',
			region: 'center',
			height : 50,
			fileUpload: true,
			bodyStyle : "background:#F9F9F9;",
			border : false,
			buttonAlign:'center',
			buttons:[
				{text: lbc("common.busiSave"),scope:this,handler:this.doLoad}
			],
			defaults : {
				layout : 'form',
				border : false,
				bodyStyle : "background:#F9F9F9; padding-top: 10px"
			},
			items : [{
				columnWidth : .7,
				items : [{
					id:'checkInFielId',
					fieldLabel: lbc("common.uploadFileLabel"),
					name:'files',
					xtype:'textfield',
					inputType:'file',
					allowBlank:false,
					anchor:'95%',
					emptyText:''
				}]
			},{
				columnWidth : .15,
				items:[{
					xtype : 'button',
					text : lmain("user._form.templateDown"),
					tooltip: lmain("user._form.templateDownTip"),
					scope : this,
					handler : function(){
						window.open(Constant.ROOT_PATH+'/template/batch_mod_user_name.xls');
					}
				}]
			}]
		}
		)
	},
	doLoad : function(){
		if(this.getForm().isValid()){
			var file = Ext.getCmp('checkInFielId').getValue();
			var flag = this.checkFileType(file);
			if(!flag)return;
			
			this.getForm().submit({
				url:"core/x/User!batchModifyUserName.action",scope:this,
				waitTitle:'请稍候', waitMsg: '正在提交数据...',
				params:{custId:App.getCust().cust_id},
				success:function(form,action){
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg,function(){
								App.getApp().menu.bigWindow.hide();
							},this);
						}
						App.getApp().main.infoPanel.userPanel.userGrid.store.reload();
					}
				},  
				failure : function(form, action) {  
					alert("文件上传失败!");  
				}
			});
		}
	},
	checkFileType : function(fileText){
		if(fileText.lastIndexOf('xlsx')>0 || fileText.lastIndexOf('.xlsx')==fileText.length-5){
			Alert('请选择excel2003文件进行上传,文件后缀名为.xls!');
			return false;
		}else if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
			Alert('请选择excel文件进行上传！');
			return false;
		}
		return true;
	}
});

BatchModUserNamePanel = Ext.extend(Ext.Panel,{
	form:null,
	doValid:function(){
		var file = Ext.getCmp('checkInFielId').getValue();
		var flag = this.form.checkFileType(file);
		if(!flag){
			return false;
		}
		
		
		return true;
	},
	getValues:function(){
		return this.form.getValues();
	},
	constructor:function(){
		this.form  = new FileForm();
		BatchModUserNamePanel.superclass.constructor.call(this,{
			layout: 'border',
			items:[
				this.form
			]
		})
	}
})

Ext.onReady(function() {
			var form = new BatchModUserNamePanel();
			var box = TemplateFactory.gViewport(form);
//			var box = TemplateFactory.gTemplate(form);
			//gTemplate
			
		});