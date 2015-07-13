var StbAndCardPanel = Ext.extend(Ext.Panel,{

	constructor: function(parent){
		this.parent = parent;
		StbAndCardPanel.superclass.constructor.call(this,{
			layout:'hbox',
			defaults:{layout:'form',border:false,flex:1},
			items : [
			{
					xtype:'panel',flex:1,defaults:{width:'200'},
					items:[
				{xtype:'textfield',fieldLabel:'机顶盒号',id:'oldStbCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'机顶盒型号',id:'oldStbModelText'},
				{xtype:'hidden',id:'oldStbModel'},
				{xtype:'textfield',fieldLabel:'智能卡号',id:'oldCardCode',readOnly:true,style:Constant.TEXTFIELD_STYLE},
				{xtype:'displayfield',fieldLabel:'智能卡型号',id:'oldCardModelText'},	
				{xtype:'hidden',id:'oldCardModel'},
				{xtype:'combo',fieldLabel:'新机顶盒',id:'newStbCode',
					editable:true,displayField:'device_code',valueField:'device_code',
					store : new Ext.data.ArrayStore({fields : ['device_code']}),
					listeners:{scope:this,change:function(txt){
						this.parent.checkDevice(txt,"STB");
					}}},
				{xtype:'displayfield',fieldLabel:'新机顶盒型号',id:'newStbModel'},						
				{xtype:'combo',fieldLabel:'新智能卡号',id:'newCardCode',
					editable:true,displayField:'device_code',valueField:'device_code',
					store : new Ext.data.ArrayStore({fields : ['device_code']}),
					listeners:{scope:this,change:function(txt){
						this.parent.checkDevice(txt,"CARD");
					}}},
				{xtype:'displayfield',fieldLabel:'新智能卡型号',id:'newCardModel'}	
				]
			},
			{
					xtype:'fieldset',flex:1,labelWidth:'40',id:'fromCustInfoPanel',hidden:true,border:1,
					items:[
						{xtype:'displayfield'},
						{xtype:'displayfield',value:'<b>来源客户信息</b>'},
						{xtype:'displayfield'},
						{fieldLabel:'客户名',xtype:'displayfield',id:'custNameField'},
						{fieldLabel:'客户编号',xtype:'displayfield',id:'custNoField'},
						{fieldLabel:'客户地址',xtype:'displayfield',id:'custAddresseField'},
						{xtype:'displayfield'},
						{xtype:'displayfield'}
					]
			}
			]
		});
	}
});

/**
 * 更换设备面板
 * @class SwitchDevicePanel
 * @extends BaseForm
 */
SwitchDevicePanel = Ext.extend(BaseForm,{
	deviceInfo : null,
	deviceType:{STB:'STB',CARD:'CARD'},
	userType : null,
	userRec : null,
	url: Constant.ROOT_PATH + "/core/x/Cust!switchDevice.action",
	constructor: function(){
		SwitchDevicePanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			id : 'SwitchDevicePanel',
			border : false,
			labelWidth: 90,
			layout:'fit',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			defaults:{anchor:'100%',baseCls: 'x-plain'},
			items: [new StbAndCardPanel(this)]
		});
	},
	doInit: function(){
		SwitchDevicePanel.superclass.doInit.call(this);
		this.initForm();
	},
	initForm: function(){
		this.userGridRef = App.getApp().main.infoPanel.userPanel.userGrid;
		this.userRec = this.userGridRef.getSelectionModel().getSelected();
		this.userType = this.userRec.get('user_type');
		this.deviceGridRef = App.getApp().main.infoPanel.custPanel.custDeviceGrid;
		var record = this.deviceGridRef.store.getAt(this.deviceGridRef.store.find('device_code',this.userRec.data.stb_id));
		if(null == record){
			record = this.deviceGridRef.store.getAt(this.deviceGridRef.store.find('device_code',this.userRec.data.card_id));
		}
		
		var deviceCode = record.get('device_code');//设备号
		var deviceModel = record.get('device_model');
		var deviceModelText = record.get('device_model_text');
		var deviceType = record.get('device_type');//设备类型:(MODEM,STB,CARD)
		var deviceId = record.get('device_id');//设备ID
		
		
		
		//更换设备类型
//		this.add(new StbAndCardPanel());//添加界面组件
		Ext.getCmp('oldStbCode').setValue(deviceCode);
		Ext.getCmp('oldStbModel').setValue(deviceModel);
		Ext.getCmp('oldStbModelText').setValue(deviceModelText);
		
		this.getStbOrCard(record);
		
		this.loadDeviceData();//加载空闲设备的数据
		this.doLayout();
		
		var comboes = this.findByType("paramcombo");
		if(comboes.length > 0){
			App.form.initComboData(comboes);
		}
	},
	//根据 机顶盒号查找卡号，或 根据卡号查找机顶盒号
	getStbOrCard:function(record){
		var deviceCode = record.get('device_code');//设备号
		var deviceModel = record.get('device_model');
		var deviceModelText = record.get('device_model_text');
		var deviceType = record.get('device_type');//设备类型:(MODEM,STB,CARD)
		var deviceId = record.get('device_id');//设备ID
		
		var cardId = this.userRec.get('card_id');
		if( !Ext.isEmpty(cardId) ){
			Ext.getCmp('oldCardCode').setValue( cardId );
			var deviceRecs = this.deviceGridRef.store.query('device_code',cardId);
			if( !Ext.isEmpty(deviceRecs.items[0]) ){
				var deviceRecord = deviceRecs.items[0];
				Ext.getCmp('oldCardModel').setValue(deviceRecord.get('device_model'));
				Ext.getCmp('oldCardModelText').setValue(deviceRecord.get('device_model_text'));
				
			}else{
				this.getModelTextByCode(Ext.getCmp('oldCardCode'));//调到这里的,肯定是虚拟智能卡
			}
		}
	},
	//加载客户下已使用的设备
	loadDeviceData : function(){
		//查找能用的设备
		var cdStore =  this.deviceGridRef.getStore();//客户设备面板store
		var stbs = cdStore.query('device_type','STB');
		var cards = cdStore.query('device_type','CARD');
		var stbArr=[],cardArr=[];
		stbs.each(function(item,index,length){
			var data = item.data;
			var device_code = data['device_code'];
			//不是挂失的,并且不是当前使用的
			if(data['loss_reg'] == 'F' && data['status'] == 'USE' && this.userRec.data.stb_id != data.device_code ){
				stbArr.push([device_code]);
			}
		},this);
		
		cards.each(function(item,index,length){
			var data = item.data;
			var device_code = data['device_code'];
			//不是挂失的,并且不是当前使用的
			if(data['loss_reg'] == 'F' && data['status'] == 'USE' && this.userRec.data.card_id != data.device_code ){
				cardArr.push([device_code]);
			}
		},this);
		
		var stbCombo = Ext.getCmp('newStbCode');
		if(stbCombo && stbArr.length > 0){
			stbCombo.getStore().loadData(stbArr);
		}
		
		var cardCombo = Ext.getCmp('newCardCode');
		if(cardCombo && cardArr.length > 0){
			cardCombo.getStore().loadData(cardArr);
		}
	},
	clearTipComps:function(){
		Ext.getCmp('newStbCode').reset();
		Ext.getCmp('newCardCode').reset();
		Ext.getCmp('newStbModel').reset();
		Ext.getCmp('newCardModel').reset();
		
		Ext.getCmp('custNoField').reset();
		Ext.getCmp('custNameField').reset();
		Ext.getCmp('custAddresseField').reset();
		Ext.getCmp('fromCustInfoPanel').setVisible(false);
	},
	/**
	 * 检查设备是否存在
	 * @param {} txt 新stb,card,modem组件
	 * @param {} deviceType 设备类型
	 */
	checkDevice:function(txt,deviceType){
		Ext.getCmp('fromCustInfoPanel').setVisible(false);
		var value = txt.getRawValue();
		if(Ext.isEmpty(value)){
			this.clearTipComps();
			return;
		}
		/**
		 * 先检查当前客户设备,如果当前新的值是当前设备的,直接在当前设备里检查.
		 * 如果不是,则从后台查.
		 */
		var stbCombo = Ext.getCmp('newStbCode') ;
		var stbModelField = Ext.getCmp('newStbModel') ;
		var cardCombo = Ext.getCmp('newCardCode');
		var cardModelField = Ext.getCmp('newCardModel');
		
		var switchUser;//如果是当前客户的用户,则代表跟当前设备互换的用户的引用.
		var pairedDevice = [];//当前设备类型是机顶盒的时候，这个是对应的智能卡,反之是机顶盒.
		var pairedCombo;//如果当前查的是机顶盒,则这个是智能卡,如果查的是智能卡,这个就是机顶盒.方便后面使用.
		var deviceModelField,anotherModelField;
		if(deviceType == 'STB'){//机顶盒
			switchUser = this.userGridRef.store.query('stb_id',value);
			pairedCombo = cardCombo;
			anotherModelField = cardModelField;
			deviceModelField = stbModelField;
			if(switchUser.length ==1){
				switchUser = switchUser.get(0);
				//要被更换掉的设备
				var orginalDev = this.deviceGridRef.store.query('device_code',this.userRec.get('stb_id')).get(0);
				var newDev = this.deviceGridRef.store.query('device_code',value);
				newDev = newDev.length ==1? newDev.get(0) : {data:{}};
				var returnFlag = false;
				if(orginalDev.data.interactive_type != newDev.data.interactive_type){
					this.clearTipComps();
					returnFlag = true;
					Alert('两个设备交互类型(双向、单向)不一致,不能互换');
				}
				if(orginalDev.data.definition_type != newDev.data.definition_type){
					this.clearTipComps();
					returnFlag = true;
					Alert('两个设备清晰类型(高清、普通)不一致,不能互换');
				}
				
				if(returnFlag == true){
					return;
				}
				
				pairedDevice = this.deviceGridRef.store.query('device_code',switchUser.get('card_id'));
			}
		}else{//智能卡
			switchUser = this.userGridRef.store.query('card_id',value);
			pairedCombo = stbCombo;
			anotherModelField = stbModelField;
			deviceModelField = cardModelField; 
			if(switchUser.length ==1){
				switchUser = switchUser.get(0);
				pairedDevice = this.deviceGridRef.store.query('device_code',switchUser.get('stb_id'));
			}
		}
		
		if(pairedDevice.length ==1){
			var modemFlag = false;
			//判断是否有猫，是否开通双向、
			if(this.userRec.data.modem_mac){
				modemFlag = switchUser.data.modem_mac;
			}else{
				modemFlag = !switchUser.data.modem_mac;
			}
			if ( !modemFlag || this.userRec.data.serv_type != switchUser.data.serv_type) {//两边用户有且仅有一个用户有猫
				Alert('互换的用户的一个有Modem一个没有,或者用户一个是双向,一个是单向');
				this.clearTipComps();
				return false;
			}
			
			pairedDevice = pairedDevice.get(0);
			pairedCombo.setValue(pairedDevice.get('device_code'));
			var device = this.deviceGridRef.store.getAt(this.deviceGridRef.store.find('device_code',value));
			deviceModelField.setValue(device.get('device_model_text'));
			anotherModelField.setValue(pairedDevice.get('device_model_text'));
			return;
		}
		
		Ext.Ajax.request({
			url : root + '/commons/x/QueryDevice!queryDeviceForSwitch.action',
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
				//内含user 数据,另外设备以 device_code 为key保存在map里
				if(data[value]){
					var device = data[value]; 
					var devInfo = data[value];
					var deviceModel = devInfo.deviceModel;
					
					//系统保证了这个取这个数据不会出错,不罗嗦,直接取
					var orgiDev = this.deviceGridRef.store.query('device_code',
							this.userRec.get('stb_id')).get(0).data;
					
					var errFlag = false;
					if(orgiDev.definition_type != deviceModel.interactive_type){
						Alert('两个设备清晰类型(高清、普通)不一致,不能互换');
						errFlag = true;
					}
					if(orgiDev.definition_type != deviceModel.interactive_type){
						Alert('两个设备交互类型(双向、单向)不一致,不能互换');
						errFlag = true;
					}
					if(errFlag){
						this.clearTipComps();
						return;
					}
					deviceModelField.setValue(device.device_model_text);
				}
				
				var fromCust = data.fromCust;
				if(fromCust){//来自不同客户,需要显示客户信息
					Ext.getCmp('fromCustInfoPanel').setVisible(true);
					var custNameField = Ext.getCmp('custNameField');
					if(custNameField){
						custNameField.setValue(fromCust.cust_name);
					}
					var custNoField = Ext.getCmp('custNoField');
					if(custNoField){
						custNoField.setValue(fromCust.cust_no);
					}
					var custAddresseField = Ext.getCmp('custAddresseField');
					if(custAddresseField){
						custAddresseField.setValue(fromCust.address);
					}
				}else{
					Ext.getCmp('fromCustInfoPanel').setVisible(false);
				}
				
				var stbCode = this.userRec.get('stb_id');
				var cardCode = this.userRec.get('card_id');
				var deviceMatchedFlag = false;//判断两边设备是否对等,比如都有机卡,都只有卡
				if(!Ext.isEmpty(cardCode) && !Ext.isEmpty(stbCode) ){//机卡都有
					if(data[value] && data.anotherDevice){
						deviceMatchedFlag = true;
					}
				}else if (!Ext.isEmpty(cardCode) && Ext.isEmpty(stbCode)){//只有卡
					if(deviceType =='CARD' &&  !data.anotherDevice ){
						deviceMatchedFlag = true;
					}
				}
				var msg = '';
				//TODO 有猫的设备暂时不给换
				if(!Ext.isEmpty(data.user.modem_mac)){
					Alert('来源设备有不符合更换标准的设备');
					this.clearTipComps();
					return false;
				}
				/*
				if ((Ext.isEmpty(data.user.modem_mac) && 
						Ext.isEmpty(this.userRec.get('modem_mac'))) //都没猫
						|| (!Ext.isEmpty(data.user.modem_mac) && 
							!Ext.isEmpty(this.userRec.get('modem_mac')))) {//都有猫
					deviceMatchedFlag = deviceMatchedFlag && true;
				}else{//一边有猫，一遍没猫
					deviceMatchedFlag = false;
				}
				
				if(deviceMatchedFlag){
					if(this.userRec.data.serv_type != data.user.serv_type){
						deviceMatchedFlag = false;
						if(Ext.isEmpty(msg)){
							msg = '互换设备的两个用户一个是双向用户一个是单向,不能互换!';
						}
					}
				}
				*/
				if(!deviceMatchedFlag){
					msg = Ext.isEmpty(msg) ? '两边用户的设备数量、类型不一致,不能进行该项业务!' : msg;
					Alert(msg);
					this.clearTipComps();
					return false;
				}
				
				if(data.anotherDevice){
					pairedCombo.setValue(data.anotherDevice.device_code);
					anotherModelField.setValue(data.anotherDevice.device_model);
				}
				
				this.dealWithResult(data);
				
			},
			clearData:function(){
				//清空组件
				Ext.getCmp('newStbCode').reset();
				Ext.getCmp('newCardCode').reset();
				Ext.getCmp('newStbModel').reset();
				Ext.getCmp('newCardModel').reset();
				
				Ext.getCmp('custNoField').reset();
				Ext.getCmp('custNameField').reset();
				Ext.getCmp('custAddresseField').reset();
				Ext.getCmp('fromCustInfoPanel').setVisible(false);
			}
		});
	},
	getModelTextByCode : function(txt){
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
						 if(Ext.getCmp('oldCardModel')){
							Ext.getCmp('oldCardModel').setValue(data['device_model']);
						}
						if(Ext.getCmp('oldCardModelText')){
							Ext.getCmp('oldCardModelText').setValue(data['device_model_text']);
						}
					}
				} 
			});
		}
	},
	dealWithResult : function(data){
		this.doLayout();
	},
	doValid: function(){
		return SwitchDevicePanel.superclass.doValid.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['feeInfo'] = "";
		return all;
	},
	success: function(form, resultData){
		feeInfo = [];
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nuf = new SwitchDevicePanel();
	var box = TemplateFactory.gTemplate(nuf);
});