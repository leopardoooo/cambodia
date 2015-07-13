/**
 * 修改购买方式
 * @class BuyDeviceForm
 * @extends BaseForm
 */
ChangeBuyTypeForm = Ext.extend( BaseForm , {
	device_id : null,
	buyModeId:null,
	constructor: function(){
		this.buyModeStore = new Ext.data.JsonStore({
 				url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
 				fields : ['buy_mode','buy_mode_name']
 		});
 		this.buyModeStore.load();
 		this.buyModeStore.on("load",this.doFilterData,this);
		ChangeBuyTypeForm.superclass.constructor.call(this, {
			border: false,
			layout : 'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items: [{
					fieldLabel:'设备编号',
					xtype:'textfield',
					name:'device_code',
					style : Constant.TEXTFIELD_STYLE,
					allowBlank:false,
					width : 150
				},{
					fieldLabel : '设备类型',
					allowBlank : false,
					xtype:'textfield',
					width : 150,
					style : Constant.TEXTFIELD_STYLE,
					name:'device_type',
					readOnly : true
				},{
					fieldLabel : '设备型号',
					allowBlank : false,
					width : 150,
					id : 'deviceModel',
					style : Constant.TEXTFIELD_STYLE,
					xtype:'textfield',
					name:'model_name',
					readOnly : true
				},{
					fieldLabel : '配对卡号',
					xtype:'textfield',
					width : 150,
					style : Constant.TEXTFIELD_STYLE,
					name:'pairCard_id',
					readOnly : true
				},
				{
					xtype : 'combo',
					width : 150,
					editable : false,
					id : 'deviceBuyMode',
					allowBlank : false,
					hiddenName:'buy_mode',
					fieldLabel : '购买方式',
					store : this.buyModeStore,
					triggerAction : 'all',
					mode: 'local',
					displayField : 'buy_mode_name',
					valueField : 'buy_mode',
					emptyText: '请选择'
				}]
		});
	},
	initComponent:function(){
		ChangeBuyTypeForm.superclass.initComponent.call(this);
		
		var data = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected().data;
		var form = this.getForm();
		var deviceCode = data['device_code'];
		this.buyModeId = data['buy_mode'];
		this.device_id = data['device_id'];
		form.findField('device_code').setValue(deviceCode);
		form.findField('device_type').setValue(data['device_type_text']);
		form.findField('pairCard_id').setValue(data['pair_card_code']);
		
		this.doDeviceCodeChange(data['device_id'],data['device_type']);
	},
	doFilterData : function(){
		var value = this.buyModeId;
		var combo = Ext.getCmp('deviceBuyMode');
		var store = combo.getStore();
		store.each(function(record) {
			if (record.get('buy_mode')==value) {
				store.removeAt(store.find('buy_mode',value));
			}
		},this);
	},
	doDeviceCodeChange : function(deviceId,deviceType){//设备号发生变化
		Ext.Ajax.request({
			url : root + '/commons/x/QueryDevice!queryDeviceModelByDeviceType.action',
			params : {
				deviceId : deviceId,
				deviceType: deviceType
			},
			scope : this,
			success : function(response, opts) {
				var rs = Ext.decode(response.responseText);
				if(rs){
					this.getForm().findField('model_name').setValue(rs['model_name'])
				}
			}
		});
	},
	getValues : function(){
		var o = {};
		o["deviceId"] = this.device_id;		
		o["buyMode"] = Ext.getCmp('deviceBuyMode').getValue();
		return o;
	},
	url : Constant.ROOT_PATH + "/core/x/Cust!saveChangeDeviceType.action",
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var saleForm = new ChangeBuyTypeForm();
	var box = TemplateFactory.gTemplate(saleForm);
});