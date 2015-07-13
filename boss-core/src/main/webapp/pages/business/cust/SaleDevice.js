/**
 * 销售设备
 * @class BuyDeviceForm
 * @extends BaseForm
 */
SaleDeviceForm = Ext.extend( BaseForm , {
	buyModeStore : null,
	deviceInfo : {},
	device_id : null,
	feeId : null,
	fee_std_id : null,
	feeInfo:[],
	changeOwnship : null,
	constructor: function(){
		this.buyModeStore = new Ext.data.JsonStore({
 				url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
 				fields : ['item_name','item_value']
 		});
		SaleDeviceForm.superclass.constructor.call(this, {
			id : 'saleDeviceFormId',
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
					fieldLabel : '购买方式',
					xtype:'combo',
					width : 150,
					id : 'deviceBuyMode',
					emptyText: '请选择',
					allowBlank : false,
					store:new Ext.data.JsonStore({
						url:root+'/commons/x/QueryDevice!queryDeviceCanFee.action',
						fields:['buy_mode','buy_mode_name','change_ownship']
					}),displayField:'buy_mode_name',valueField:'buy_mode',triggerAction:'all',mode:'local',
					editable : false,
					listeners:{
						scope: this,
						'select': this.doBuyModeSelect
					}
				},{
					xtype : 'panel',
					border: false,
					baseCls: 'x-plain',
					anchor : '100%',
					id : 'feePanelId',
					bodyStyle: "background:#F9F9F9",
					layout : 'form',
					items : []
				}]
		});
	},	
	initComponent:function(){
		SaleDeviceForm.superclass.initComponent.call(this);
		this.findByType('combo')[0].getStore().load();
		
		var data = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected().data;
		var form = this.getForm();
		var deviceCode = data['device_code'];
		
		this.device_id = data['device_id'];
		form.findField('device_code').setValue(deviceCode);
		form.findField('device_type').setValue(data['device_type_text']);
		form.findField('pairCard_id').setValue(data['pair_card_code']);
		
		this.doDeviceCodeChange(data['device_id'],data['device_type']);
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
					this.deviceInfo = rs;
					this.deviceInfo.device_model = rs['device_model'];
					this.getForm().findField('model_name').setValue(rs['model_name'])
				}
			}
		});
	},
	doBuyModeSelect : function(combo,record){//触发购买方式选择事件
		Ext.getCmp('feePanelId').removeAll();
		this.feeInfo = [];
		this.doLayout();
		this.changeOwnship = record.get('change_ownship');
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : this.deviceInfo.device_model,
				deviceType : this.deviceInfo.device_type,
				buyMode : combo.getValue()
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data && data.length>0){
						for(var i=0,len=data.length;i<len;i++){
							var d = data[i];
							Ext.getCmp('feePanelId').add({
								fieldLabel : '费用名称',
								xtype:'textfield',
								width : 150,
								style : Constant.TEXTFIELD_STYLE,
								name:'fee_name',
								readOnly : true,
								value:d['fee_name']
							});
							Ext.getCmp('feePanelId').add({
								fieldLabel : '收费',
								id : 'deviceFeeValue'+i,
								width : 150,
								vtype : 'num',
								allowNegative : false,
								xtype:'numberfield',
								name:'fee_value',
								minValue:Ext.util.Format.formatFee(d['min_fee_value']),
								maxValue:Ext.util.Format.formatFee(d['max_fee_value']),
								value:Ext.util.Format.formatFee(d['fee_value'])
							});
							var obj = {};
							obj['fee_std_id'] = data[i].fee_std_id;
							obj['fee_id'] = data[i].fee_id;
							
							var obj2 = {};
							obj2[i] = obj;
							
							this.feeInfo.push(obj2);
						}
						this.doLayout();
				}
			}
		});
	},
	getValues : function(){
		var o = {};
		o["deviceId"] = this.device_id;
		
		var arr = [];
		for(var i=0,len=this.feeInfo.length;i<len;i++){
			var obj = {};
			obj['fee_id'] = this.feeInfo[i][i]['fee_id'];
			obj['fee_std_id'] = this.feeInfo[i][i]['fee_std_id'];
			obj['fee'] = Ext.util.Format.formatToFen(Ext.getCmp('deviceFeeValue'+i).getValue());
			arr.push(obj);
		}
		
		if(arr.length>0)
			o['feeInfo'] = Ext.encode(arr);
		else
			o['feeInfo'] = "";
			
		o["buyMode"] = Ext.getCmp('deviceBuyMode').getValue();
		o["changeOwnship"] = this.changeOwnship;
		return o;
	},
	url : Constant.ROOT_PATH + "/core/x/Cust!saveSaleDevice.action",
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getFee : function(){
		var fee = 0;
		for(var i=0,len=this.feeInfo.length;i<len;i++){
			fee += Ext.getCmp('deviceFeeValue'+i).getValue();	
		}
		return fee;
	}
});

Ext.onReady(function(){
	var saleForm = new SaleDeviceForm();
	var box = TemplateFactory.gTemplate(saleForm);
});