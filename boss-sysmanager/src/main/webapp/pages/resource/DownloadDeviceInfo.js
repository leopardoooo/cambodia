

DownloadDeviceInfo = Ext.extend(Ext.Panel,{
	constructor:function(){
		DownloadDeviceInfo.superclass.constructor.call(this,{
			id:'DownloadDeviceInfo',
			title:'设备盘点',
			closable: true,
			border:false,
			items:[{
				xtype:'form',id:'downLoadFormId',
				bodyStyle:'padding:10px',border:false,
				fileUpload: true,items:[
					{fieldLabel:'设备类型',xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
						hiddenName:'deviceType',allowBlank:false
					},
					{fieldLabel:'设备文件',name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'}
				],
				buttonAlign:'center',
				buttons:[
					{text:'下载',scope:this,handler:this.doDownload},
					{text:'重置',scope:this,handler:function(){
						Ext.getCmp('downLoadFormId').getForm().reset();
						Ext.getCmp('downLoadFormId').getForm().findField('files').getEl().dom.select();
						document.execCommand("delete");
					}}
				]
			}]
		});
	},
	initComponent:function(){
		DownloadDeviceInfo.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	doDownload:function(){
		var form = Ext.getCmp('downLoadFormId').getForm();
		if(!form.isValid())return;
		
		form.submit({
				url:'resource/Device!downloadDeviceInfo.action',
				scope:this,
				success:function(form,action){
					var data = action.result;
					if(data.success === true)
						Alert(data.msg);
				},  
				failure : function(form, action) {
					alert("文件下载失败!");  
				}
			});
	}
});





