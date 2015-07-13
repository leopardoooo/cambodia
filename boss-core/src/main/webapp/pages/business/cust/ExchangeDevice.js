var StbAndCardPanel = Ext.extend(Ext.Panel,{

	constructor: function(){
		StbAndCardPanel.superclass.constructor.call(this,{
			defaults:{width:150},
			items : [
				{xtype:'textfield',fieldLabel:'机顶盒号',id:'oldStbCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'机顶盒型号',id:'oldStbModelText'},
				{xtype:'displayfield',fieldLabel:'包换期',id:'oldStbReplacoverDate'},
				{xtype:'hidden',id:'oldStbModel'},
				{xtype:'textfield',fieldLabel:'智能卡号',id:'oldCardCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'智能卡型号',id:'oldCardModelText'},	
				{xtype:'hidden',id:'oldCardModel'},
				{xtype:'hidden',id:'is_virtual_card'},
				{xtype:'textfield',fieldLabel:'Modem号',id:'oldModemCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'Modem型号',id:'oldModemModelText'},
				{xtype:'hidden',id:'oldModemModel'},
				{xtype:'hidden',id:'is_virtual_modem'},
				{xtype:'combo',fieldLabel:'新机顶盒',id:'newStbCode',
					editable:true,displayField:'device_code',valueField:'device_code',
					store : new Ext.data.ArrayStore({
						fields : ['device_code']
					}),
					listeners:{change:function(txt){
						this.ownerCt.ownerCt.checkDevice(txt,"STB",Ext.getCmp('newCardCode'),Ext.getCmp('newModemCode'),
							Ext.getCmp('newStbModel'),Ext.getCmp('newCardModel'),Ext.getCmp('newModemModel'));
					}}},
				{xtype:'displayfield',fieldLabel:'新机顶盒型号',id:'newStbModel'},
				{xtype:'hidden',fieldLabel:'新机顶盒型号清晰度',id:'newStbModelDefi'},
				{xtype:'combo',fieldLabel:'新智能卡号',id:'newCardCode',
					editable:true,displayField:'device_code',valueField:'device_code',
					store : new Ext.data.ArrayStore({
						fields : ['device_code']
					}),
					listeners:{change:function(txt){
						this.ownerCt.ownerCt.checkCardDevice(txt,Ext.getCmp('newCardModel'));
					}}},
				{xtype:'displayfield',fieldLabel:'新智能卡型号',id:'newCardModel'},
				{xtype:'combo',fieldLabel:'新Modem号',id:'newModemCode',
					editable:true,displayField:'device_code',valueField:'device_code',
					store : new Ext.data.ArrayStore({
						fields : ['device_code']
					}),
					listeners:{
						change:function(txt){
							this.ownerCt.ownerCt.checkDevice(txt,'MODEM',null,null,Ext.getCmp('newModemModel'));
						}
					}
				},
				{xtype:'displayfield',fieldLabel:'新MODEM型号',id:'newModemModel'},
				{xtype:'paramcombo',fieldLabel:'更换原因',paramName:'CHANGE_REASON', width:150,allowBlank:false,hiddenName:'change_reason'}
//				{xtype : 'paramcombo',fieldLabel : '回收设备',paramName : 'BOOLEAN',defaultValue: 'T',
//				width : 150,foreceSelection : true,id : 'reclaim',hiddenName : 'reclaimDevice'}
				/*,	//设备状态由仓库确认回收时操作
				{fieldLabel : '设备状态',xtype : 'paramcombo',paramName : 'DEVICE_STATUS_R_DEVICE',
				hiddenName : 'deviceStatus',width : 150,id : 'status',defaultValue:'ACTIVE'}*/
			]
		});
	}
});


/**
 * 购买方式面板
 * @class BuyModePanel
 * @extends Ext.Panel
 */
BuyModePanel = Ext.extend(Ext.Panel,{
	constructor : function(data){
		BuyModePanel.superclass.constructor.call(this,{
			id : 'feePanelId',
			layout : 'form',
			anchor : '100%',
			border : false,
			labelWidth:90,lableAlign:'right',
			defaults:{labelWidth:90,lableAlign:'right'},
			items : [	{xtype:'hidden',id:'fee_std_id',value:data['fee_std_id']},	{xtype:'hidden',id:'fee_id',value:data['fee_id']},{
						fieldLabel : '费用名称',
						xtype:'textfield',
						width : 150,
						style : Constant.TEXTFIELD_STYLE,
						readOnly : true,
						value:data['fee_name']
					},{
						fieldLabel : '升级收费',
						id : 'deviceFeeValue',
						width : 100,
						vtype : 'num',
						allowNegative : false,
						xtype:'numberfield',
						minValue:Ext.util.Format.formatFee(data['min_fee_value']),
						maxValue:Ext.util.Format.formatFee(data['max_fee_value']),
						value:Ext.util.Format.formatFee(data['fee_value'])
					}]
		});
	}
});

var StbZjFeePanel = Ext.extend(Ext.Panel,{
	constructor:function(){
		StbZjFeePanel.superclass.constructor.call(this,{
			id : 'stbZjFeePanel',
			layout : 'form',
			anchor : '100%',
			border : false,
			items : [{
					fieldLabel : '机顶盒折旧费',
					id : 'stbZjFeeValue',
					width : 150,
					allowNegative : false,
					xtype:'numberfield'
				}]
		})
	},
	getValues: function(){
		var obj = {};
		obj['stbZjFee'] = Ext.getCmp('stbZjFeeValue').getValue();
		return obj;
	}
});

var CardZjFeePanel = Ext.extend(Ext.Panel,{
	constructor:function(){
		CardZjFeePanel.superclass.constructor.call(this,{
			id : 'cardZjFeePanel',
			layout : 'form',
			anchor : '100%',
			border : false,
			items : [{
					fieldLabel : '智能卡折旧费',
					id : 'cardZjFeeValue',
					width : 150,
					allowNegative : false,
					xtype:'numberfield'
				}]
		})
	},
	getValues: function(){
		var obj = {};
		obj['cardZjFee'] = Ext.getCmp('cardZjFeeValue').getValue();
		return obj;
	}
});

var ModemZjFeePanel = Ext.extend(Ext.Panel,{
	constructor:function(){
		ModemZjFeePanel.superclass.constructor.call(this,{
			id : 'modemZjFeePanel',
			layout : 'form',
			anchor : '100%',
			border : false,
			items : [{
					fieldLabel : 'MODEM折旧费',
					id : 'modemZjFeeValue',
					width : 150,
					allowNegative : false,
					xtype:'numberfield'
				}]
		})
	},
	getValues: function(){
		var obj = {};
		obj['modemZjFee'] = Ext.getCmp('modemZjFeeValue').getValue();
		return obj;
	}
});

/**
 * 更换设备面板
 * @class ExchangeDevicePanel
 * @extends BaseForm
 */
ExchangeDevicePanel = Ext.extend(BaseForm,{
	deviceInfo : null,
	stbZjFee : null,
	cardZjFee: null,
	modemZjFee: null,
	deviceType:{STB:'STB',CARD:'CARD',MODEM:'MODEM'},
	needBuyStb : false,//需要购买设备stb
	needBuyCard : false,//需要购买card
	needBuyModem : false,//需要购买modem
	url : null,
	userType : null,
	userRec : null,
	url: Constant.ROOT_PATH + "/core/x/Cust!changeStbCard.action",
	constructor: function(){
		ExchangeDevicePanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			id : 'ExchangeDevicePanel',
			border : false,
			labelWidth: 90,
			layout:'column',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			defaults:{layout:'form',anchor:'100%',baseCls: 'x-plain'},
			items: []
		});
	},
	doInit: function(){
		ExchangeDevicePanel.superclass.doInit.call(this);
		this.initForm();
		
		/*if(Ext.getCmp('oldStbCode') && !Ext.getCmp('oldStbCode').getValue()){
			Ext.getCmp('newStbCode').setDisabled(true);
		}*/
		
		var oldStb = Ext.getCmp('oldStbCode');
		var oldCard = Ext.getCmp('oldCardCode');
		var oldModem = Ext.getCmp('oldModemCode');
		
		if(oldCard && oldCard.getValue() 
			&& oldStb && Ext.isEmpty(oldStb.getValue())
			&& oldModem && Ext.isEmpty(oldModem.getValue()) ){
			Ext.getCmp('newStbCode').setDisabled(true);
			Ext.getCmp('newModemCode').setDisabled(true);
		}
		if(oldModem && oldModem.getValue() 
			&& oldStb && Ext.isEmpty(oldStb.getValue())
			&& oldCard && Ext.isEmpty(oldCard.getValue()) ){
			Ext.getCmp('newStbCode').setDisabled(true);
			Ext.getCmp('newCardCode').setDisabled(true);
		}
		
		/*if(oldCard && oldCard.getValue()){
			var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
			var currRecord = null;
			userStore.each(function(record){
				if(record.get('card_id') == oldCard.getValue() && record.get('status') != 'INVALID'){
					currRecord = record;
					return false;
				}
			});
			//单向数字用户，更换设备禁用modem
			if(currRecord.get('user_type') == 'DTV' && currRecord.get('serv_type')=='SINGLE'){
				Ext.getCmp('newModemCode').setDisabled(true);
			}
		}*/
		
	},
	initForm: function(){
		var record = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected();
		this.devTobeChanged = record.data;
		var deviceCode = record.get('device_code');//设备号
		var deviceModel = record.get('device_model');
		var deviceModelText = record.get('device_model_text');
		var deviceType = record.get('device_type');//设备类型:(MODEM,STB,CARD)
		var deviceId = record.get('device_id');//设备ID
		
		//更换设备类型
		if(deviceType == this.deviceType.STB){//机顶盒
			this.add(new StbAndCardPanel());//添加界面组件
			Ext.getCmp('oldStbCode').setValue(deviceCode);
			Ext.getCmp('oldStbModel').setValue(deviceModel);
			Ext.getCmp('oldStbModelText').setValue(deviceModelText);
			if(record.get('replacover_date'))
				Ext.getCmp('oldStbReplacoverDate').setValue( Date.parseDate(record.get('replacover_date'),'Y-m-d H:i:s').format('Y年m月d日') );
			this.getStbOrCard(deviceType, deviceCode, 'oldCardCode',
					'oldCardModel', 'oldCardModelText', 'oldModemCode',
					'oldModemModel', 'oldModemModelText');// 根据机顶盒号查询获取卡号
		}else if(deviceType==this.deviceType.CARD){//卡号
			this.add(new StbAndCardPanel());
			Ext.getCmp('oldCardCode').setValue(deviceCode);
			Ext.getCmp('oldCardModel').setValue(deviceModel);
			Ext.getCmp('oldCardModelText').setValue(deviceModelText);
			this.getStbOrCard(deviceType, deviceCode, 'oldStbCode',
					'oldStbModel', 'oldStbModelText', 'oldModemCode',
					'oldModemModel', 'oldModemModelText');// 根据卡号查询获取机顶盒号
		}else if(deviceType==this.deviceType.MODEM){//猫
			this.add(new StbAndCardPanel());
			Ext.getCmp('oldModemCode').setValue(deviceCode);
			Ext.getCmp('oldModemModel').setValue(deviceModel);
			Ext.getCmp('oldModemModelText').setValue(deviceModelText);
			this.getStbOrCard(deviceType, deviceCode, 'oldStbCode',
					'oldStbModel', 'oldStbModelText', 'oldCardCode',
					'oldCardModel', 'oldCardModelText');
		}
		
		this.loadDeviceData();//加载空闲设备的数据
		this.doLayout();
		
		var comboes = this.findByType("paramcombo");
		if(comboes.length > 0){
			App.form.initComboData(comboes);
		}
	},
	//根据 机顶盒号查找卡号，或 根据卡号查找机顶盒号
	getStbOrCard:function(deviceType,deviceCode,code,deviceModel,deviceModelText,
				code2,deviceModel2,deviceModelText2){
		var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		var deviceStore = App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
		
		var field = 'stb_id';
		if(this.deviceType.CARD == deviceType){
			field = 'card_id';
		}else if(this.deviceType.MODEM==deviceType){
			field = 'modem_mac';
		}
		var recordData = userStore.query(field,deviceCode);//设备对应的用户
		var record = recordData.items[0];
		if(field == 'stb_id'){
			var cardId = record.get('card_id');
			if( !Ext.isEmpty(cardId) ){
				Ext.getCmp(code).setValue( cardId );
				var deviceRecs = deviceStore.query('device_code',cardId);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel).setValue(deviceRecord.get('device_model'));
					Ext.getCmp(deviceModelText).setValue(deviceRecord.get('device_model_text'));
					
					if(Ext.getCmp('is_virtual_card'))
						Ext.getCmp('is_virtual_card').setValue(deviceRecord.get('is_virtual_card'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code),Ext.getCmp(deviceModel),Ext.getCmp(deviceModelText),Ext.getCmp('is_virtual_card'));
				}
				
			}
			var modemCode = record.get('modem_mac');
			if( !Ext.isEmpty(modemCode) ){
				Ext.getCmp(code2).setValue(modemCode);
				var deviceRecs = deviceStore.query('device_code',modemCode);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel2).setValue(deviceRecord.get('device_model_text'));
					Ext.getCmp(deviceModelText2).setValue(deviceRecord.get('device_model_text'));
					
					if(Ext.getCmp('is_virtual_modem'))
						Ext.getCmp('is_virtual_modem').setValue(deviceRecord.get('is_virtual_modem'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code2),Ext.getCmp(deviceModel2),Ext.getCmp(deviceModelText2),Ext.getCmp('is_virtual_modem'));
				}
			}
		}else if(field == 'card_id'){
			var stbId = record.get('stb_id');
			if( !Ext.isEmpty(stbId) ){
				Ext.getCmp(code).setValue(stbId);
				var deviceRecs = deviceStore.query('device_code',stbId);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel).setValue(deviceRecord.get('device_model'));
					Ext.getCmp(deviceModelText).setValue(deviceRecord.get('device_model_text'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code),Ext.getCmp(deviceModel),Ext.getCmp(deviceModelText));
				}
			}
			
			var modemMac = record.get('modem_mac');
			if( !Ext.isEmpty(modemMac) ){
				Ext.getCmp(code2).setValue(modemMac);
				var deviceRecs = deviceStore.query('device_code',modemMac);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel2).setValue(deviceRecord.get('device_model'));
					Ext.getCmp(deviceModelText2).setValue(deviceRecord.get('device_model_text'));
					
					if(Ext.getCmp('is_virtual_modem'))
						Ext.getCmp('is_virtual_modem').setValue(deviceRecord.get('is_virtual_modem'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code2),Ext.getCmp(deviceModel2),Ext.getCmp(deviceModelText2),Ext.getCmp('is_virtual_modem'));
				}
			}
			
		}else if(field == 'modem_mac'){
			var stbId = record.get('stb_id');
			if( !Ext.isEmpty(stbId) ){
				Ext.getCmp(code).setValue(stbId);
				var deviceRecs = deviceStore.query('device_code',stbId);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel).setValue(deviceRecord.get('device_model'));
					Ext.getCmp(deviceModelText).setValue(deviceRecord.get('device_model_text'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code),Ext.getCmp(deviceModel),Ext.getCmp(deviceModelText));
				}
			}
			var cardId = record.get('card_id');
			if( !Ext.isEmpty(cardId) ){
				Ext.getCmp(code2).setValue( cardId );
				var deviceRecs = deviceStore.query('device_code',cardId);
				if( !Ext.isEmpty(deviceRecs.items[0]) ){
					var deviceRecord = deviceRecs.items[0];
					Ext.getCmp(deviceModel2).setValue(deviceRecord.get('device_model'));
					Ext.getCmp(deviceModelText2).setValue(deviceRecord.get('device_model_text'));
					
					if(Ext.getCmp('is_virtual_card'))
						Ext.getCmp('is_virtual_card').setValue(deviceRecord.get('is_virtual_card'));
				}else{
					this.getModelTextByCode(Ext.getCmp(code2),Ext.getCmp(deviceModel2),Ext.getCmp(deviceModelText2),Ext.getCmp('is_virtual_card'));
				}
			}
		}
		
		this.userType = record.get('user_type');
		this.userRec = recordData;
	},
	//加载客户下空闲设备数据
	loadDeviceData : function(){
		//查找能用的设备
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
		var device = cdStore.query('status','IDLE');
		var stbArr=[],cardArr=[],modemArr=[];
		device.each(function(item,index,length){
			var data = item.data;
			if(data['status'] == 'IDLE' && data['loss_reg'] == 'F'){
				if(data['device_type'] == 'STB'){
					stbArr.push([data['device_code']]);
					//虚拟modem可以用于双向和宽带
					if(!Ext.isEmpty(data['pair_modem_code'])){
						modemArr.push( data['pair_modem_code'] );
					}
				}else if(data['device_type'] == 'CARD'){
					cardArr.push([data['device_code']]);
				}else if(data['device_type'] == 'MODEM'){
					modemArr.push([data['device_code']]);
				}
			}
		});
		
		var userStore =  App.getApp().main.infoPanel.userPanel.userGrid.getStore();
		var bandUser = userStore.queryBy(function(record){
			return record.get('user_type') == 'BAND' && record.get('status') != 'INVALID';
		});
		var bandModems = [];//用于宽带的modem
		bandUser.each(function(item){
			if(item.get('modem_mac')){
				bandModems.push(item.get('modem_mac'));
			}
		})
		
		var doubleUser = userStore.queryBy(function(record){
			return record.get('user_type') == 'DTV' && record.get('status') == 'ACTIVE';
		});
		var doubleModems = [];//用于双向的modem
		doubleUser.each(function(item){
			if(item.get('modem_mac')){
				doubleModems.push(item.get('modem_mac'));
			}
		})
		
		var doubleUserModem = [],bandUserModem = [];
		//空闲设备
		if(modemArr.length > 0){
			doubleUserModem.push(modemArr);
			bandUserModem.push(modemArr);
		}
		
		if(this.userRec.length == 1){
			if(this.userType == 'DTV'){
				//仅用于宽带的modem
				if(doubleModems.length > 0){
					for(var i=bandModems.length -1;i>=0;i--){
						//如果宽带的modem已经用于双向，移除
						if(doubleModems.contain(bandModems[i])){
							bandModems.remove(bandModems[i]);
						}
					}
					
				}
				
				if(bandModems.length > 0){
					doubleUserModem.push(bandModems);
				}
				
			}else if(this.userType == 'BAND'){
				//仅用于双向的modem
				if(bandModems.length > 0){
					for(var i=doubleModems.length -1;i>=0;i--){
						//如果双向的modem已经用于宽带，移除
						if(bandModems.contain(doubleModems[i])){
							doubleModems.remove(doubleModems[i]);
						}
					}
					
				}
				
				if(doubleModems.length > 0){
					bandUserModem.push(doubleModems);
				}
			}
		}
		
		var modemCombo = Ext.getCmp('newModemCode')
		if(modemCombo){
			if(this.userType == 'BAND' && bandUserModem.length > 0){
				modemCombo.getStore().loadData(bandUserModem);
			}else if(this.userType == 'DTV' && doubleUserModem.length > 0){
				modemCombo.getStore().loadData(doubleUserModem);
			}
			
		}
		
		var stbCombo = Ext.getCmp('newStbCode');
		if(stbCombo && stbArr.length > 0){
			stbCombo.getStore().loadData(stbArr);
		}
		
		var cardCombo = Ext.getCmp('newCardCode');
		if(cardCombo && cardArr.length > 0){
			cardCombo.getStore().loadData(cardArr);
		}
	},
	/**
	 * 检查设备是否存在
	 * @param {} txt 新stb,card,modem组件
	 * @param {} deviceType 设备类型
	 * @param {} cardComp card 组件
	 * @param {} deviceMoedelComp 新stb,card,modem型号组件
	 * @param {} deviceMoedelComp2 新card型号组件
	 * 1、当用户输入新的stb号时，txt为新stb组件，cardComp为card组件，
	 * 		deviceMoedelComp为新stb型号组件，deviceMoedelComp2为新card型号组件
	 * 2、当用户输入新的card号时，txt为新card组件，cardComp为null，
	 * 		deviceMoedelComp为新stb型号组件，deviceMoedelComp2为新card型号组件
	 * 3、当用户输入新的modem号时，txt为新modem组件，cardComp为null，
	 * 		deviceMoedelComp为新modem型号组件，deviceMoedelComp2为null
	 */
	checkDevice:function(txt,deviceType,cardComp,modemComp,stbModelComp,cardModelComp,modemModelComp){
		var value = txt.getRawValue();
		if(Ext.isEmpty(value)){
			this.delZjPanel(deviceType);
			return;
		}
		Ext.Ajax.request({
			url : root + '/commons/x/QueryDevice!queryDeviceForExchange.action',
			async: false,
			params:{
				deviceType:deviceType,
				deviceCode:value,
				custId:App.getCustId(),
				userType : this.userType
			},
			scope:this,
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				if(data.device_type != 'STB' && deviceType != 'MODEM'){
					Alert('该设备类型为'+data.device_type_text+',不是机顶盒')
					txt.reset();
					cardComp.reset();
					cardComp.setReadOnly(false);
					return;
				}
				var deviceModel = data.deviceModel;
				/*
				//高清和普清不能互换
				if(deviceModel && deviceType == 'STB'){
					var errFlagInner = false;
					if(deviceModel.definition_type != App.getData().currentRec.data.definition_type){
						Alert('两个设备清晰类型(高清、普通)不一致,不能互换')
						errFlagInner = true;
					}
					if(App.getData().currentRec.data.interactive_type != data.deviceModel.interactive_type){
						Alert('两个设备交互类型(双向、单向)不一致,不能互换');
						errFlagInner = true;
					}
					if(errFlagInner == true){
						txt.reset();
						cardComp.reset();
						cardComp.setReadOnly(false);
						return;
					}
				}
				*/
				
				if(data.device_type != 'MODEM' && deviceType != 'STB'){
					Alert('该设备类型为'+data.device_type_text+',不是MODEM')
					txt.reset();
					cardComp.reset();
					cardComp.setReadOnly(false);
					return;
				}
				if(data.used == 'T'){
					Alert('请注意！该机顶盒不是新机！');
				}
				this.dealWithResult(data);
				
				this.initZjFeeInfo(data['device_type']);
				
				if(Ext.isEmpty(data['device_code'])){//查询的设备不正确
					txt.reset();
				}else if(value != data['device_code']){//是否更换的是同一个设备
					txt.setValue(data.device_code);
				}
				if(stbModelComp)
					stbModelComp.setValue(data['device_model_text']);
				var newStbModelDefiCmp = Ext.getCmp('newStbModelDefi');
				if(newStbModelDefiCmp){
					newStbModelDefiCmp.setValue(data.deviceModel.definition_type);
				}
				var pairCard = data['pairCard'];
				if(pairCard){
					if(cardComp && pairCard['card_id'])
						cardComp.setValue(pairCard['card_id']);
						cardComp.setReadOnly(true);
					if(cardModelComp)
							cardModelComp.setValue(pairCard['device_model_text']);
				}else{
					cardComp.setReadOnly(false);
				}
				
				var pairModem = data['pairModem'];
				if(pairModem){
					if(modemComp && pairModem['modem_mac']){
						modemComp.setValue(pairModem['modem_mac']);
						if(pairModem['is_virtual'] == 'T'){
							modemComp.setReadOnly(true);
						}else{
							modemComp.setReadOnly(false);
						}
					}
					if(modemModelComp)
							modemModelComp.setValue(pairModem['device_model_text']);
				}else{
					if(modemComp){
						modemComp.setReadOnly(false);
						modemComp.setValue('');
					}
					if(modemModelComp){
						modemModelComp.setValue('');
					}
				}
			},
			clearData:function(){
				//清空组件
				txt.reset();
				stbModelComp.reset();
				if(cardComp && cardComp.getValue()){
					cardComp.reset();
					cardComp.setReadOnly(false);
					cardModelComp.reset();
				}
				if(modemComp && modemComp.getValue()){
					modemComp.reset();
					modemComp.setReadOnly(false);
					modemModelComp.reset();
				}
				var panle = Ext.getCmp('feePanelId');
				if(panle){
					panle.removeAll();
				}
			}
		});
	},
	checkCardDevice : function(txt,cardModel,deviceType){
		var value = txt.getRawValue();
		if(txt.readOnly == true){//通过机顶盒查询获得的配对卡,直接验证通过
			return true;
		}
		if(value){
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!queryDeviceForExchange.action',
				async: false,
				params:{
					deviceCode:value,
					deviceType : 'CARD',
					custId:App.getCustId()
				},
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					
					if(data.device_type != 'CARD'){
						Alert('该设备类型存在配对'+data.device_type_text+',不能更换!')
						txt.reset();
						return;
					}
					
					if(data['device_type'] == 'STB'
							&& data['is_virtual'] == 'F'){
						if (deviceType == 'CARD' && data['pairCard']
								&& data['pairCard']['is_virtual'] == 'T'){
							Alert(value+'是虚拟卡,不能更换！');
							txt.reset();
							return;
						}else if(deviceType == 'MODEM' && data['pairModem']
								&& data['pairModem']['is_virtual'] == 'T'){
							Alert(value+'是虚拟Modem,不能更换！');
							txt.reset();
							return;
						}
					}
					
					this.dealWithResult(data);
					this.initZjFeeInfo(data['device_type']);
					if(cardModel){
						cardModel.setValue(data.deviceModel.device_model_text);
					}
				},
				clearData:function(){
					var panle = Ext.getCmp('feePanelId');
					if(panle){
						panle.removeAll();
					}
					txt.reset();
				}
			});
		}else{
			this.delZjPanel('CARD');
		}
	},
	getModelTextByCode : function(txt,deviceModel,deviceModelText,isVirtualDevice){
		var value = txt.getRawValue();
		if(value){
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!queryDeviceInfoByCode.action',
				async: false,
				params:{
					deviceCode:value 
				},
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data){
						 if(deviceModel){
							deviceModel.setValue(data['device_model']);
						}
						if(deviceModelText){
							deviceModelText.setValue(data['device_model_text']);
						}
						if(isVirtualDevice){
							isVirtualDevice.setValue(data['is_virtual']);
						}
					}
				} 
			});
		}
	},
	dealWithResult : function(data){
		if(data.depot_status == 'IDLE'){
			var deviceType = data['device_type'];
			if(deviceType == 'STB'){
				this.needBuyStb = true;
			}else if(deviceType == 'CARD'){
				this.needBuyCard = true;
			}else if(deviceType == 'MODEM'){
				this.needBuyModem = true;
			}
		}else{
			//设备已在客户名下
			if(data.device_type == 'STB'){
				this.needBuyStb = false;
			}else if(data.device_type == 'CARD'){
				this.needBuyCard = false;
			}else if(data.device_type == 'MODEM'){
				this.needBuyModem = false;
			}
		}
		
		if(this.needBuyCard || this.needBuyStb || this.needBuyModem){
			
			var	newModel =data.device_model;
			var oldModel = null;
			if(deviceType == 'STB'){
				oldModel = Ext.getCmp('oldStbModel').getValue();
			}else if(deviceType == 'CARD'){
				oldModel = Ext.getCmp('oldCardModel').getValue();
			}else if(deviceType == 'MODEM'){
				oldModel = Ext.getCmp('oldModemModel').getValue();
			}
			var panle = Ext.getCmp('feePanelId');
			if(panle){
				panle.removeAll();
			}
			//TODo 这里需要修改
			Ext.Ajax.request({
				scope : this,
				url : root + '/commons/x/QueryDevice!queryFeeByModel.action',
				params : {
					deviceType : data.device_type,oldModel:oldModel,newModel:newModel
				},
				success : function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data != null){
						var exchangeDevicePanel = Ext.getCmp('ExchangeDevicePanel');
						var devTobeChanged = exchangeDevicePanel.devTobeChanged;
						var newStbModelDefiCmp = Ext.getCmp('newStbModelDefi');
						this.add(new BuyModePanel(data));
						this.doLayout();
						//如果高清换成普通，升级费用设置为0
						if(devTobeChanged.definition_type == 'HIGH' && newStbModelDefiCmp.getValue() == 'STANDARD'){
							//设置值
							Ext.getCmp('deviceFeeValue').setValue('0');
						}
					}
				}
			});
			
		}else{
			if(Ext.getCmp('feePanelId')){
				this.remove(Ext.getCmp('feePanelId'),true);
			}
			this.deviceInfo = null;
		}
		
		this.doLayout();
	},
	delZjPanel: function(deviceType){
		if(deviceType == 'STB'){
			if(Ext.getCmp('stbZjFeePanel')){
				this.remove(Ext.getCmp('stbZjFeePanel'),true);
			}
		}else if(deviceType == 'CARD'){
			if(Ext.getCmp('cardZjFeePanel')){
				this.remove(Ext.getCmp('cardZjFeePanel'),true);
			}
		}else if(deviceType == 'MODEM'){
			if(Ext.getCmp('modemZjFeePanel')){
				this.remove(Ext.getCmp('modemZjFeePanel'),true);
			}
		}
	},
	initZjFeeInfo: function(deviceType){
		//重新选择或输入设备号，先删除折旧费
		this.delZjPanel(deviceType);
		//判断如果是配备的重新生成旧设备的折旧费
		if(this.needBuyCard || this.needBuyStb || this.needBuyModem){
			if(deviceType == 'STB'){
				if(!Ext.getCmp('stbZjFeePanel')){
					this.add(new StbZjFeePanel());
				}
			}else if(deviceType == 'CARD'){
				if(!Ext.getCmp('cardZjFeePanel')){
					this.add(new CardZjFeePanel());
				}
			}else if(deviceType == 'MODEM'){
				if(!Ext.getCmp('modemZjFeePanel')){
					this.add(new ModemZjFeePanel());
				}
			}
		}
		this.doLayout();
	},
	doValid: function(){
		
		var oldStbCode = Ext.getCmp('oldStbCode').getValue();
		var oldCardCode = Ext.getCmp('oldCardCode').getValue();
		var oldModemCode = Ext.getCmp('oldModemCode').getValue();
		
		var newStb = Ext.getCmp('newStbCode');
		var newStbCode = newStb.getValue();
		var newCard = Ext.getCmp('newCardCode');
		var newCardCode = newCard.getValue();
		var newModem = Ext.getCmp('newModemCode');
		var newModemCode = newModem.getValue();
		var newCardModel = Ext.getCmp('newCardModel');
		var newModemModel = Ext.getCmp('newModemModel');
		
		var obj = {};
		
		var record = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelected();
		var oldVirtualCard = record.get('is_virtual_card');
		var oldVirtualModem = record.get('is_virtual_modem');
		
		if(Ext.isEmpty(newStbCode) && Ext.isEmpty(newCardCode) && Ext.isEmpty(newModemCode) ){
			var obj = {};
			obj["isValid"] = false;
			obj["msg"] = "请输入新机顶盒、智能卡和Modem号!";
			return obj;
		}
		
		//更换机或卡时
		if(oldStbCode &&oldCardCode && oldModemCode){
			if(oldVirtualCard !='T' && oldVirtualModem == 'T' && Ext.isEmpty(newModemCode)){
				var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
				var servType = userStore.getAt(userStore.find('stb_id', oldStbCode)).get('serv_type');
				//单向数字电视 机MODEM一体机可以更换为单机
				if(servType == 'DOUBLE'){
					obj["isValid"] = false;
					obj["msg"] = "双向数字且旧设备是机MODEM一体虚拟机，请输入新Modem号!";
					return obj;
				}
			}else if(oldVirtualCard =='T' && oldVirtualModem != 'T' && Ext.isEmpty(newCardCode)){
				obj["isValid"] = false;
				obj["msg"] = "旧设备是机卡一体虚拟机，请输入新卡号!";
				return obj;
			}else if(oldVirtualCard =='T' && oldVirtualModem == 'T' && ( Ext.isEmpty(newCardCode) || Ext.isEmpty(newModemCode)) ){
				obj["isValid"] = false;
				obj["msg"] = "旧设备是机卡MODEM一体虚拟机，请输入新卡和新MODEM号!";
				return obj;
			}else if(Ext.isEmpty(newStbCode) && (oldVirtualModem == 'T' || oldVirtualCard == 'T')){
				obj["isValid"] = false;
				obj["msg"] = "旧机顶盒一体虚拟机，请输入新机顶盒号!";
				return obj;
			}
			
		}else if(oldStbCode && oldCardCode && Ext.isEmpty(oldModemCode)){
			if( oldVirtualCard =='T' && Ext.isEmpty(newCardCode) ){
				obj["isValid"] = false;
				obj["msg"] = "旧设备是机卡一体虚拟机，请输入新卡号!";
				return obj;
			}
		}
		
		else if(Ext.isEmpty(oldStbCode) &&oldCardCode && Ext.isEmpty(oldModemCode)){
			if( oldVirtualCard =='T' && Ext.isEmpty(newCardCode) ){
				obj["isValid"] = false;
				obj["msg"] = "旧设备是机卡一体虚拟机，请输入新卡号!";
				return obj;
			}
		}
		/*else if(oldModem && oldModem.getValue()){
			if(Ext.isEmpty(newModem.getValue())){
				obj["isValid"] = false;
				obj["msg"] = "请输入MODEM号!";
				return obj;
			}
		}else{
			if(oldStb.getValue() && Ext.isEmpty(newStb.getValue())){
				obj["isValid"] = false;
				obj["msg"] = "请输入机顶盒号!";
				return obj;
			}
			if(oldCard.getValue() && Ext.isEmpty(newCard.getValue())){
				obj["isValid"] = false;
				obj["msg"] = "请输入智能卡号!";
				return obj;
			}
		}*/
		
		if(newStb && newStb.hasFocus){
			this.checkDevice(newStb,'STB',newCard,newModem);
			if(newStb.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "机顶盒有问题，请在验证后保存";
				return obj;
			}
		}
		if(newCard && newCard.hasFocus){
			this.checkCardDevice(newCard,newCardModel,'CARD');
			if(newCard.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "智能卡有问题，请在验证后保存";
				return obj;
			}
		}
		if(newModem && newModem.hasFocus){
			this.checkDevice(newModem,'MODEM',newModemModel);
			if(newModem.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "MODEM有问题，请在验证后保存";
				return obj;
			}
		}
			
		return ExchangeDevicePanel.superclass.doValid.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		if(this.needBuyStb && this.needBuyCard && this.needBuyModem){
			all['buyFlag'] = 5;//三个都要购买
		}else if(this.needBuyStb && this.needBuyCard){
			all['buyFlag'] = 4;//两个都要购买
		}else if(this.needBuyStb && this.needBuyModem){
			all['buyFlag'] = 3;//两个都要购买
		}else if(this.needBuyModem && this.needBuyCard){
			all['buyFlag'] = 6;
		}else{
			if(this.needBuyCard){
				all['buyFlag'] = 2;//只需要买智能卡
			}else if(this.needBuyStb){
				all['buyFlag'] = 1;//只需要买机顶盒
			}else if(this.needBuyModem){
				all['buyFlag'] = 7;//只需要买MODEM
			}else{
				all['buyFlag'] = 0;//直接更换在客户名下的设备，不用购买
			}
		}
		
		var arr = [];
		var obj = {};
		if(Ext.getCmp('feePanelId')){
			obj['fee_id'] = Ext.getCmp('fee_id').getValue();
			obj['fee_std_id'] = Ext.getCmp('fee_std_id').getValue();
			var value = Ext.getCmp('deviceFeeValue').getValue();
			if(!Ext.isEmpty(value) && value != 0){
				obj['fee'] = Ext.util.Format.formatToFen(value);
				arr.push(obj);
			}
		}
		
		if(arr.length>0)
			all['feeInfo'] = Ext.encode(arr);
		else
			all['feeInfo'] = "";
			
		var stbzjFeeCmp = Ext.getCmp('stbZjFeeValue');
		if(stbzjFeeCmp){
			all['stbZjFee']= Ext.util.Format.formatToFen(stbzjFeeCmp.getValue());
		}
		var cardzjFeeCmp = Ext.getCmp('cardZjFeeValue');
		if(cardzjFeeCmp){
			all['cardZjFee']= Ext.util.Format.formatToFen(cardzjFeeCmp.getValue());
		}
		var modemzjFeeCmp = Ext.getCmp('modemZjFeeValue');
		if(modemzjFeeCmp){
			all['modemZjFee']= Ext.util.Format.formatToFen(modemzjFeeCmp.getValue());
		}
		return all;
	},
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getFee : function(){
		var fee = 0;
		var feeCmp = Ext.getCmp('deviceFeeValue');
		if(feeCmp){
			fee = feeCmp.getValue();
		}
		var stbzjFeeCmp = Ext.getCmp('stbZjFeeValue');
		if(stbzjFeeCmp){
			fee += stbzjFeeCmp.getValue();
		}
		var cardzjFeeCmp = Ext.getCmp('cardZjFeeValue');
		if(cardzjFeeCmp){
			fee += cardzjFeeCmp.getValue();
		}
		var modemzjFeeCmp = Ext.getCmp('modemZjFeeValue');
		if(modemzjFeeCmp){
			fee += modemzjFeeCmp.getValue();
		}	
		
		return fee;
	}
});

Ext.onReady(function(){
	var nuf = new ExchangeDevicePanel();
	var box = TemplateFactory.gTemplate(nuf);
});