/**
 * 购买设备
 * @class BuyDeviceForm
 * @extends BaseForm
 */
BuyDeviceForm = Ext.extend( BaseForm , {
	buyModeStore : null,
	deviceInfo : {},
	deviceCode : null,
	feeId : null,
	cardId : null,
	modemMac:null,
	fee_std_id : null,
	isVirtualCard:'F',	//是否是虚拟卡
	isVirtualModem:'F',	//是否是虚拟MODEM
	feeInfo:[],	//费用信息,结构为Map,key为数字（0开始，几项收费项数目）,
				/*例：[
						{
							0:{fee_id:1,fee_std_id:1,fee:1000}
						},{
							1:{fee_id:2,fee_std_id:2,fee:1000}
						}
					]*/
	constructor: function(){
		this.buyModeStore = new Ext.data.JsonStore({
 				url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
 				fields : ['buy_mode','buy_mode_name']
 		});
		BuyDeviceForm.superclass.constructor.call(this, {
			id : 'buyDeviceForm',
			border: false,
			layout : 'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items: [{
					fieldLabel:'设备编号',
					xtype:'textfield',
//					vtype : 'alphanum',
					id : 'deviceCodeForSale',
					name:'device_code',
					allowBlank:false,
					width : 150,
					blankText:'请输入设备编号',
					listeners:{
						scope: this,
						'change': this.doDeviceCodeChange,
						'specialkey' : this.onEnterDown
					}
				},{
					fieldLabel : '设备类型',
					allowBlank : false,
					xtype:'textfield',
					width : 150,
					style : Constant.TEXTFIELD_STYLE,
					name:'device_type_text',
					readOnly : true
				},{
					fieldLabel : '设备型号',
//					allowBlank : false,
					width : 150,
					style : Constant.TEXTFIELD_STYLE,
					xtype:'textfield',
					name:'device_model_text',
					readOnly : true
				},{
					xtype : 'panel',
					border: false,
					baseCls: 'x-plain',
					anchor : '100%',
					id : 'pairCardPanel',
					bodyStyle: "background:#F9F9F9",
					layout : 'form',
					items : [{
						fieldLabel : '卡号',
						xtype:'textfield',
						width : 150,
						id : 'pairCardId',
						name:'pairCard.card_id',
						listeners : {
							scope: this,
							'change': this.doPairCardChange
						}
					}]
				},{
					xtype : 'panel',
					border: false,
					baseCls: 'x-plain',
					anchor : '100%',
					id : 'pairModemPanel',
					bodyStyle: "background:#F9F9F9",
					layout : 'form',
					items : [{
						fieldLabel : 'MODEM号',
						xtype:'textfield',
						width : 150,
						id : 'pairModemId',
						name:'pairModem.modem_mac',
						listeners : {
							scope: this,
							'change': this.doPairModemChange
						}
					}]
				},{
					fieldLabel : '购买方式',
					xtype:'combo',
					width : 150,
					id : 'deviceBuyMode',
					forceSelection : true,
					store : this.buyModeStore,
					triggerAction : 'all',
					mode: 'local',
					displayField : 'buy_mode_name',
					valueField : 'buy_mode',
					emptyText: '请选择',
					allowBlank : false,
					disabled : true,
					editable : false,
					listeners:{
						scope: this,
						'beforequery' : this.loadBuyModeStore,
						'select': this.doBuyModeSelect
					}
				},
				{
					xtype : 'panel',
					border: false,
					baseCls: 'x-plain',
					anchor : '100%',
					id : 'feePanelId',
					bodyStyle: "background:#F9F9F9",
					layout : 'form',
					items : []
				}
				/*,{
					fieldLabel : '费用名称',
					xtype:'textfield',
					allowBlank : false,
					width : 150,
					style : Constant.TEXTFIELD_STYLE,
					id : 'deviceFeeName',
					name:'fee_name',
					readOnly : true
				},{
					fieldLabel : '收费',
					id : 'deviceFeeValue',
					width : 150,
					vtype : 'num',
					allowNegative : false,
					xtype:'numberfield',
					name:'fee_value'
				}*/]
		})
	},
	initEvents : function(){
		this.on('afterrender',function(){
			Ext.getCmp('deviceCodeForSale').focus(true,500);
		});
		BuyDeviceForm.superclass.initEvents.call(this);
	},
	doDeviceCodeChange : function(field){//设备号发生变化
		this.cardId = null;
		if(field.getValue()){
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!queryDevice.action',
				params : {
					deviceCode : field.getValue().trim()
				},
				success : function(response,opts){
					var rs = Ext.decode(response.responseText);
					if(rs.success == true){
						Ext.getCmp('pairCardPanel').removeAll();
						Ext.getCmp('pairModemPanel').removeAll();
						
						this.deviceInfo = rs.simpleObj;
						if(rs.simpleObj.device_type == "STB"){
							Ext.getCmp('pairCardPanel').add({
								fieldLabel : '卡号',
								xtype:'textfield',
								width : 150,
								id : 'pairCardId',
								name:'pairCard.card_id',
								listeners : {
									scope: this,
									'change': this.doPairCardChange
								}
							});
							
							Ext.getCmp('pairModemPanel').add({
								fieldLabel : 'MODEM号',
								xtype:'textfield',
								width : 150,
								id : 'pairModemId',
								name:'pairModem.modem_mac',
								listeners : {
									scope: this,
									'change': this.doPairModemChange
								}
							});
						}
						this.doLayout();
						var data = rs.simpleObj;
						if(data.pairCard){//填写上虚拟卡号
							var cmp = Ext.getCmp('pairCardId');
							cmp.setValue(data.pairCard.card_id);
							cmp.setDisabled(true);
						}else{
							var cmp = Ext.getCmp('pairCardId');
							if(cmp)
								cmp.setDisabled(false);
						}
						
						if(data.pairModem){//填写上虚拟卡号
							var cmp = Ext.getCmp('pairModemId');
							cmp.setValue(data.pairModem.modem_mac);
							cmp.setDisabled(true);
						}else{
							var cmp = Ext.getCmp('pairModemId');
							if(cmp)
								cmp.setDisabled(false);
						}
						
						for(var prop in rs.simpleObj.deviceModel){
							this.deviceInfo["deviceModel."+prop] = data.deviceModel[prop];
						}
						
						var pc = data.pairCard;
						if(pc){
							this.deviceInfo["pairCard.card_id"] = pc["card_id"];
							this.isVirtualCard = pc['is_virtual'];
							
							Ext.QuickTips.register({ 
								target : Ext.getCmp('pairCardId').getEl(), 
						       	html: 'This is a test msg',
								text : pc['is_virtual']=='T'?'<font color=red>虚拟卡</font>':'' 
							});
							Ext.getCmp('pairCardPanel').items.itemAt(0).setDisabled(true);
						}else{
							this.deviceInfo["pairCard.card_id"] = "";
							var pairCard = Ext.getCmp('pairCardPanel').items;
							if(pairCard.length > 0){
								pairCard.itemAt(0).setDisabled(false);
								pairCard.itemAt(0).focus(true,100);
							}else{
								Ext.getCmp('deviceBuyMode').focus(true,100);
							}
						}
						
						var pm = data.pairModem;
						if(pm){
							this.deviceInfo["pairModem.modem_mac"] = pm["modem_mac"];
							this.isVirtualModem = pm['is_virtual'];
							Ext.QuickTips.register({ 
								target : Ext.getCmp('pairModemId').getEl(), 
								text : pm['is_virtual']=='T'?'<font color=red>虚拟MODEM</font>':'' 
							});
							Ext.getCmp('pairModemPanel').items.itemAt(0).setDisabled(true);
						}else{
							this.deviceInfo["pairModem.modem_mac"] = "";
						}
						
						
						var record = new Ext.data.Record(this.deviceInfo);
						this.getForm().reset();
						this.getForm().loadRecord(record);
						Ext.getCmp('deviceBuyMode').clearValue();
						Ext.getCmp('deviceBuyMode').setDisabled(false);
					}else{
						this.getForm().reset();
						Ext.getCmp('deviceBuyMode').clearValue();
						Ext.getCmp('deviceBuyMode').setDisabled(true);
						Alert(rs.simpleObj+",请重新输入设备号",function(){
							Ext.getCmp('deviceCodeForSale').focus(true,1000);
						});
					}
				}
			});
		}
	},
	doPairCardChange : function(field,newValue,oldValue){//手动输入配对卡号
		if(field.getValue()){
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!querySingleCard.action',
				params : {
					deviceCode : newValue
				},
				success : function(response,opt){
					var rs = Ext.decode(response.responseText);
					if(rs.success == false){
						Ext.getCmp('pairCardId').setValue(oldValue);
						Alert(rs.simpleObj+",请重新输入设备号",function(){
							Ext.getCmp('pairCardId').focus(true,1000);
						});
					}else{
						this.cardId = rs.simpleObj.device_id;
					}
				}
			});
		}
	},
	doPairModemChange : function(field,newValue,oldValue){//手动输入配对MODEM号
		if(field.getValue()){
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!querySingleModem.action',
				params : {
					deviceCode : newValue
				},
				success : function(response,opt){
					var rs = Ext.decode(response.responseText);
					if(rs.success == false){
						Ext.getCmp('pairModemId').setValue(oldValue);
						Alert(rs.simpleObj+",请重新输入设备号",function(){
							Ext.getCmp('pairModemId').focus(true,1000);
						});
					}else{
						this.modemMac = rs.simpleObj.device_id;
					}
				}
			});
		}
	},
	onEnterDown : function(field ,e){//按下回车键
		if(13 == e.getKey()){
			if(field.getValue()){
				this.doDeviceCodeChange(field);
			}
		}
	},
	loadBuyModeStore : function(){//加载设备购买方式数据
		var deviceCode = this.deviceInfo.device_code;
		if(this.deviceCode != deviceCode){
			this.buyModeStore.load();
			this.deviceCode = deviceCode;
		}
	},
	doBuyModeSelect : function(combo,record){//触发购买方式选择事件
		Ext.getCmp('feePanelId').removeAll();
		this.feeInfo = [];
		this.doLayout();
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : this.deviceInfo.deviceModel.device_model,
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
	doValid:function(){
		var obj = {};
		var buyMode = Ext.getCmp('deviceBuyMode').getValue();
		if (buyMode=='BUY' && this.getFee()==0){
			obj['isValid'] = false;
			obj['msg'] = '购买方式为自购，收费不能为0元!';
		}
		return obj;
	},
	getValues : function(){
		var o = {};
		o["deviceId"] = this.deviceInfo.device_id;
		o["buyMode"] = Ext.getCmp('deviceBuyMode').getValue();
		
		if(this.deviceInfo['device_type'] == 'STB'){
			if(this.cardId){
				o['cardId'] = this.cardId;
			}
			
			if(this.modemMac){
				o['modemMac'] = this.modemMac;
			}
		}
		if(this.feeId){
			o["feeId"] = this.feeId;
		}
		if(this.fee_std_id){
			o["feeStdId"] = this.fee_std_id;
		}
		
		if(this.isVirtualCard){
			o['virtualCard'] = this.isVirtualCard;
		}
		
		if(this.isVirtualModem){
			o['virtualModem'] = this.isVirtualModem;
		}
		
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
	url : Constant.ROOT_PATH + "/core/x/Cust!buyDevice.action",
	success: function(form, resultData){
//		//清空参数
//		App.getData().busiTaskToDo = [];
//		var buyDevice = App.getData().busiTask['BuyDevice'];
//		var newUser = App.getData().busiTask['NewUser'];
//		//跳转业务用户开户
//		App.getData().busiTaskToDo.push(buyDevice);
//		App.getData().busiTaskToDo.push(newUser);
		
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
	var buy = new BuyDeviceForm();
	var box = TemplateFactory.gTemplate(buy);
});