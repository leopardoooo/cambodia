/**
 * 更换设备面板
 * @class ExchangeDevicePanel
 * @extends BaseForm
 */
ExchangeDevicePanel = Ext.extend(BaseForm,{
	userData: null,
	currentFeeData: null,
	deviceInfo : null,
	stbZjFee : null,
	cardZjFee: null,
	modemZjFee: null,
	url : null,
	userType : null,
	userRec : null,
	url: Constant.ROOT_PATH + "/core/x/User!changeDevice.action",
	constructor: function(){
		this.changeReasonStore = new Ext.data.JsonStore({
			url :root + '/core/x/User!queryDeviceChangeReason.action',
			fields : ['reason_type', 'reason_text', 'is_charge', 'is_reclaim', 'is_lost'],
			autoLoad: true
		});
		ExchangeDevicePanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			id : 'ExchangeDevicePanel',
			labelWidth: 90,
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			defaults: {
				xtype:'panel',
			    layout:'form',
			    baseCls: 'x-plain',
			    labelWidth: 100,
				defaults: {
					anchor:'80%',
					bodyStyle:'background:#F9F9F9;padding-top:4px',
					border:false
				}
			},
			items: [{
			    id: 'stbPanleId',
			    items: [
			        {xtype:'textfield',fieldLabel:lmain("user._form.stdId"),id:'oldStbCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
					{xtype:'displayfield',fieldLabel:lmain("user._form.stdModel"),id:'oldStbModelText'},
					{xtype:'textfield', fieldLabel:lmain("user._form.newStdId"), id:'newStbCode', allowBlank:false,
						listeners: {
							scope: this,
							change: this.checkDevice
						}
					},
					{xtype:'displayfield',fieldLabel:lmain("user._form.newStdModel"),id:'newStbModel'},
					
			]},{
				id: 'modemPanleId',
			    items: [{xtype:'textfield',fieldLabel:lmain("user._form.modemId"),id:'oldModemCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
						{xtype:'displayfield',fieldLabel:lmain("user._form.modemModel"),id:'oldModemModelText'},
						{xtype:'textfield',fieldLabel:lmain("user._form.newModemId"),id:'newModemCode', allowBlank:false,
							listeners: {
								scope: this,
								change: this.checkDevice
							}
						},
						{xtype:'displayfield',fieldLabel:lmain("user._form.newModemModel"),id:'newModemModel'}]
			},
			{items:[
                 {xtype:'combo',fieldLabel:lmain("user._form.changeCause"),allowBlank:false,hiddenName:'change_reason', id:'reasonId',
                	 store: this.changeReasonStore, displayField:'reason_text', valueField:'reason_type',
                	 forceSelection : true, triggerAction : 'all', mode: 'local',
                	 listeners:{
                		 scope:this,
                		 select: this.doSelectReason
                	 }
                 },{
    				xtype: 'displayfield',
    			    id: 'promptInfoId',
    			    fieldLabel: lbc("common.tip")
    			}
            ]}
		]});
	},
	doSelectReason: function(combo, record){
		var comp = Ext.getCmp('promptInfoId'), str='本次更换需要';
		if(record.get('is_charge') == 'T'){
			str += ' 收自购费用';
		}
		if(record.get('is_reclaim') == 'T'){
			str += ' 回收设备';
		}
		if(record.get('is_lost') == 'T'){
			str += ' 挂失设备';
		}
		comp.setValue("<font color='red'>"+str+"</font>");
	},
	doInit: function(){
		ExchangeDevicePanel.superclass.doInit.call(this);
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.userData = record.data;
		var userType = this.userData['user_type'];//设备ID
		
		var showCmp = function(flag){
			Ext.getCmp('stbPanleId').setVisible(flag);
			Ext.getCmp('modemPanleId').setVisible(!flag);
			
			
			Ext.getCmp('newStbCode').allowBlank = !flag;
			Ext.getCmp('newModemCode').allowBlank = flag;
			
		};
		//更换设备类型
		if(userType == 'OTT' || userType == 'DTT'){		//机顶盒
			Ext.getCmp('oldStbCode').setValue(this.userData['stb_id']);
			Ext.getCmp('oldStbModelText').setValue(this.userData['device_model_text']);
			
			showCmp(true);
		}else if( userType == 'BAND' ){					//MODEM
			Ext.getCmp('oldModemCode').setValue(this.userData['modem_mac']);
			Ext.getCmp('oldModemModelText').setValue(this.userData['device_model_text']);
			
			showCmp(false);
		}
		
	},
	checkDevice:function(field){
		var value = field.getRawValue();
		if(Ext.isEmpty(value)){
			return;
		}
		Ext.Ajax.request({
			url : root + '/commons/x/QueryDevice!queryChangeDevice.action',
			params:{
				userType: this.userData['user_type'],
				deviceCode:value
			},
			scope:this,
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				
				//新更换设备型号保存，购买方式需要使用
				this.userData['new_deviec_model'] = data['device_model'];
				if(data['device_type'] == 'STB'){
					Ext.getCmp('newStbModel').setValue(data['deviceModel']['model_name']);
				}else if(data['device_type'] == 'MODEM'){
					Ext.getCmp('newModemModel').setValue(data['deviceModel']['model_name']);
				}
				
				/*Ext.getCmp('deviceBuyMode').setDisabled(false);
				Ext.getCmp('txtFeeEl').setDisabled(false);*/
			},
			clearData:function(){
				//清空组件
				field.reset();
				field.setValue('');
			}
		});
	},
	/*doBuyModeSelect: function(){
		var deviceBuyModeValue = Ext.getCmp("deviceBuyMode").getValue();
		
		if(!this.userData['new_deviec_model'] || !deviceBuyModeValue){
			return ;
		}
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : this.userData['new_deviec_model'],
				buyMode : deviceBuyModeValue
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				var dfFeeName = Ext.getCmp("dfFeeNameEl");
				var txtFee = Ext.getCmp("txtFeeEl");
				if(data && data.length > 0 ){
					data = data[0];
					dfFeeName.setValue(data["fee_name"]);
					txtFee.setValue(data["fee_value"]/100.0);
					txtFee.setMaxValue(data["max_fee_value"]/100.0);
					txtFee.setMinValue(data["min_fee_value"]/100.0);
					txtFee.clearInvalid();
					this.currentFeeData = data;
				}else{
					dfFeeName.setValue("--");
					txtFee.setValue(0.00);
					txtFee.setMaxValue(0);
					txtFee.setMinValue(0);
					this.currentFeeData = null;
				}
			}
		});
	},*/
	doValid: function(){
		
		var txtFeeEl = Ext.getCmp("txtFeeEl");
		//设备费用检查
		var fd = this.currentFeeData;
		if(fd){
			var maxfee = parseFloat(fd["min_fee_value"])/100.0;
			var minfee = parseFloat(fd["max_fee_value"])/100.0;
			var feeValue = parseFloat(txtFeeEl.getValue());
			if(feeValue < minfee || feeValue > maxfee){
				return {
					isValid: false,
					msg: "设备费用必须介于"+ minfee +"-" +maxfee +"之间"
				};
			}
		}
		
		return ExchangeDevicePanel.superclass.doValid.call(this);
	},
	getValues : function(){
		var values = {};
		values['deviceCode'] = Ext.getCmp('newStbCode').getValue() || Ext.getCmp('newModemCode').getValue();
		values['userId'] = this.userData['user_id'];
		values["reasonType"] = Ext.getCmp("reasonId").getValue();
		return values;
	},
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nuf = new ExchangeDevicePanel();
	var box = TemplateFactory.gTemplate(nuf);
});