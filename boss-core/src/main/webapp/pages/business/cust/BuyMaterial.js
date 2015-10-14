/**
 * 购买配件
 * @class BuyMaterialForm
 * @extends BaseForm
 */
BuyMaterialForm = Ext.extend( BaseForm , {
	buyModeStore : null,
	deviceInfo : {},
	deviceCode : null,
	feeId : null,
	cardId : null,
	fee_std_id : null,
	feeInfo:[],
	constructor: function(){
		this.buyModeStore = new Ext.data.JsonStore({
 				url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
 				fields : ['buy_mode','buy_mode_name']
 		});
 		this.deviceModelStore = new Ext.data.JsonStore({
			fields : ['device_model','device_type','model_name','total_num']
		});
 		
		BuyMaterialForm.superclass.constructor.call(this, {
			border: false,
			bodyStyle:'padding-top:15px',
			labelWidth: 150,
			defaults:{layout:'form',border:false, width:200},
			items: [{
					fieldLabel : langUtils.main("cust._form.deviceType"),
					id : 'ctlDeviceType',
					name:'device_type',
					allowBlank : false,
					xtype:'paramcombo',
					emptyText: langUtils.bc("common.plsSwitch"),
					paramName:'OTHER_DEVICE_TYPE',
					listeners: {
						scope: this
						,'select': this.filterDeviceModel,
						expand:function(combo){
							var store = combo.getStore();
							store.filterBy(function(record){
								return record.get('item_value').indexOf('CARD')<0;
							})
						}
					}
				},{
					fieldLabel : langUtils.main("cust._form.deviceModel"),
					allowBlank : false,
					id : 'ctlDeviceModel',
					xtype:'combo',
					name:'device_model',
					emptyText: langUtils.bc("common.plsSwitch"),
					store: this.deviceModelStore,
					model: 'local',
					displayField: 'model_name',
					valueField: 'device_model',
					listWidth: 250,
					listeners: {
						scope: this,
						'select': this.doBuyModeSelect
					}
				},{
					xtype : 'displayfield',
					id : 'total_num_id',
					fieldLabel : langUtils.main("cust._form.storeCount"),
					name:'total_num'
				},{
					fieldLabel : langUtils.main("cust._form.buyWay"),
					xtype:'combo',
					id : 'deviceBuyMode',
					forceSelection : true,
					store : this.buyModeStore,
					triggerAction : 'all',
					mode: 'local',
					displayField : 'buy_mode_name',
					valueField : 'buy_mode',
					emptyText: langUtils.bc("common.plsSwitch"),
					allowBlank : false,
					editable : false,
					listeners:{
						scope: this,
						'select': this.doBuyModeSelect
					}
				},{
					fieldLabel : langUtils.main("cust._form.buyCount"),
					id : 'buyNum',
					name:'buy_num',
					allowBlank:false,
					xtype: 'spinnerfield',
	            	minValue: 1,
	            	value: 1
				},{
					xtype : 'panel',
					border: false,
					baseCls: 'x-plain',
					anchor : '100%',
					id : 'feePanelId',
					layout : 'form',
					items : []
				}]
		})
	},
	remoteData: null,
	doInit:function(){
		BuyMaterialForm.superclass.doInit.call(this);
		this.buyModeStore.load();
		Ext.Ajax.request({
			scope : this,
			url :root + '/commons/x/QueryDevice!queryDeviceModel.action',
			success: function(res, ops){
				this.remoteData = Ext.decode(res.responseText);
				this.filterDeviceModel();
			}
		});
	},
	filterDeviceModel: function(){
		Ext.getCmp("ctlDeviceModel").reset();
		Ext.getCmp("total_num_id").reset();
		var arr = [];
		for(var i= 0;i < this.remoteData.length; i++){
			if(this.remoteData[i]["device_type"] == Ext.getCmp("ctlDeviceType").getValue()){
				arr.push(this.remoteData[i]);
			}
		}
		this.deviceModelStore.loadData(arr);
		this.doBuyModeSelect();
	},
	clearComps: function(){
		Ext.getCmp('feePanelId').removeAll();
		Ext.getCmp("deviceBuyMode").reset();
		this.doLayout();
	},
	doBuyModeSelect : function(combo,record){//触发购买方式选择事件
		Ext.getCmp('feePanelId').removeAll();
		this.feeInfo = [];
		this.doLayout();
		
		var deviceModel = Ext.getCmp('ctlDeviceModel').getValue(),
			deviceType = Ext.getCmp('ctlDeviceType').getValue(),
			buyMode = Ext.getCmp('deviceBuyMode').getValue();
			
		
		if(deviceType && deviceModel){
			var store = this.getForm().findField('device_model').getStore();
			store.each(function(record){
				if(record.get('device_type') == deviceType && record.get('device_model') == deviceModel){
					Ext.getCmp('total_num_id').setValue(record.get('total_num'));		
				}
			})
		}	
		
		if(!deviceType || !deviceType || !buyMode){
			return ;
		}
		
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : deviceModel,
				deviceType : deviceType,
				buyMode : buyMode
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data && data.length>0){
						for(var i=0,len=data.length;i<len;i++){
							var d = data[i];
							Ext.getCmp('feePanelId').add({
								fieldLabel : lmain('user._form.feeName'),
								xtype:'displayfield',
								width : 150,
								name:'fee_name',
								value:d['fee_name']
							});
							Ext.getCmp('feePanelId').add({
								fieldLabel : lmain('user._form.unitPrice'),
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
							obj['fee_value'] = data[i].fee_value;
							
							var obj2 = {};
							obj2[i] = obj;
							
							this.feeInfo.push(obj2);
						}
						this.doLayout();
				}
			}
		});
		
	},
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = lbc("common.tipFormInvalid");
		}
		if(this.getForm().findField('total_num').getValue()==0 || 
		this.getForm().findField('total_num').getValue()<this.getForm().findField('buy_num').getValue()){
			obj["isValid"] = false;
			obj["msg"] = lmsg('buyNumExceedStockNum');
		}
		return obj;
	},
	getValues : function(){
		var o = {};
		o["deviceType"]=this.getForm().findField('device_type').getValue();
		o["deviceModel"]=this.getForm().findField('device_model').getValue();
		o["buyMode"] = Ext.getCmp('deviceBuyMode').getValue();
		o['buyNum'] = Ext.getCmp('buyNum').getValue();
		
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
		return o;
		
	},
	url : Constant.ROOT_PATH + "/core/x/Cust!buyMaterial.action",
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var buy = new BuyMaterialForm();
	var box = TemplateFactory.gTemplate(buy);
});