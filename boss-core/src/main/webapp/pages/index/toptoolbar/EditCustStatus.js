/**
 * 客户状态修改为“资料隔离”
 * @class
 * @extends Ext.Window
 */
 
var CustStatusForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		CustStatusForm.superclass.constructor.call(this,{
			id:'custStatusFormId',
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			fileUpload: true,
			items:[
				{xtype:'displayfield',fieldLabel:'新客户状态',value:'<font color=blue>资料隔离</font>'},
				{xtype:'hidden',name:'custStatus',value:'DATACLOSE'},
				{id:'custNoId',xtype:'textfield',fieldLabel:'客户编号',name:'custNo'},
				{id:'custStatusFileId',xtype:'fileuploadfield',fieldLabel:'客户编号文件',name:'file',anchor:'90%',
					buttonText:'浏览...'
				}
			]
		});
	}
});

var EditCustStatusWin = Ext.extend(Ext.Window,{
	formPanel:null,
	constructor:function(){
		this.formPanel = new CustStatusForm();
		EditCustStatusWin.superclass.constructor.call(this,{
			id:'custStatusWinId',
			title:'批量修改客户状态',
			border:false,
			width:380,
			height:180,
			layout:'fit',
			items:[this.formPanel],
			closeAction:'close',
			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:this.closeWin}
			]
		});
	},
	doSave:function(){
		var form = this.formPanel.getForm();
		var custNo = Ext.getCmp('custNoId').getValue();
		var fileText = Ext.getCmp('custStatusFileId').getValue();
		if( custNo || fileText ){
			var ps={},all={};
			ps['busiCode'] = '1601';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			var msg = Show();
			form.submit({
				url:root+'/core/x/Cust!updateCustStatus.action',
				scope:this,
				params:all,
				success:function(form,action){
					var data = action.result;
					msg.hide();msg = null;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('修改成功!',function(){
								this.formPanel.getForm().reset();
								this.closeWin();
							},this);
						}
					}
				}
			});
		}else{
			Alert('请选择客户编号文件或输入客户编号');
		}
	},
	closeWin:function(){
		this.close();
	}
});