BuyReplacover = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Cust!buyReplacover.action",
	deviceRecord:null,
	constructor:function(){
		this.deviceRecord = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected();
		BuyReplacover.superclass.constructor.call(this,{
			items:[
				{xtype:'displayfield',fieldLabel:'机顶盒型号',id:'device_model_text',style:Constant.TEXTFIELD_STYLE},
//				{xtype:'displayfield',fieldLabel:'清晰度',id:'definition_type_text',style:Constant.TEXTFIELD_STYLE},
				{xtype:'hidden',fieldLabel:'设备ID',id:'device_id'},
				{xtype:'textfield',fieldLabel:'机顶盒号',id:'device_code',style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'原包换期',id:'replacover_date',style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'购买之后包换期',id:'replacover_date1',style:Constant.TEXTFIELD_STYLE}
			]
		});
	},
	doInit:function(){
		var replacover_date = this.deviceRecord.get('replacover_date');
		var data = {
			device_model_text:this.deviceRecord.get('device_model_text'),
			device_id:this.deviceRecord.get('device_id'),
			definition_type_text:this.deviceRecord.get('definition_type_text'),
			device_code:this.deviceRecord.get('device_code'),
			replacover_date:this.deviceRecord.get('replacover_date')
		};
		var date = Date.parseDate(replacover_date,'Y-m-d H:i:s');
		if(!date){
			date = Date.parseDate(replacover_date,'Y-m-d');
		}
		data.replacover_date = date ? date.format('Y年m月d日') :'';
		data.replacover_date1 = ( date && date.getTime() > nowDate().getTime() ) ? date.add(Date.YEAR,1).format('Y年m月d日') : nowDate().add(Date.YEAR,1).format('Y年m月d日');
		this.getForm().loadRecord(new Ext.data.Record(data));
	},
	getValues:function(){
		var values = this.getForm().getValues();
		values.deviceId = values.device_id;
		return values;
	},
	doValid: function(){
		if(Ext.getCmp('BusiPanel').forms.feeform){
			var fees = Ext.getCmp('BusiPanel').forms.feeform.getValues();
			if(Ext.isEmpty(fees) || fees.length ==0){
				return {isValid:false,msg:'请勾选延长包换期费用'};
			}
		}else{
			return {isValid:false,msg:'还没有配置包换期费用,请联系管理员!'};
		}
		return true;
	},
	success:function(){
		App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.remoteRefresh();
	}
});

Ext.onReady(function(){
	var nuf = new BuyReplacover();
	var box = TemplateFactory.gTemplate(nuf);
});