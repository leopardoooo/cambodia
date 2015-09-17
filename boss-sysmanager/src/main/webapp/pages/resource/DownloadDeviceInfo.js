

DownloadDeviceInfo = Ext.extend(Ext.Panel,{
	constructor:function(){
		DownloadDeviceInfo.superclass.constructor.call(this,{
			id:'DownloadDeviceInfo',
			title:lsys('DownloadDeviceInfo._title'),
			closable: true,
			border:false,
			items:[{
				xtype:'form',id:'downLoadFormId',
				bodyStyle:'padding:10px',border:false,
				fileUpload: true,items:[
					{fieldLabel:lsys('DeviceCommon.labelDeviceType'),xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
						hiddenName:'deviceType',allowBlank:false
					},
					{fieldLabel:lsys('DeviceCommon.labelDevFile'),name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'}
				],
				buttonAlign:'center',
				buttons:[
					{text:lsys('common.downLoad'),scope:this,handler:this.doDownload},
					{text:lsys('common.reset'),scope:this,handler:function(){
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
					alert(lsys('msgBox.fileDownloadFailed'));  
				}
			});
	}
});





