	
UserTypeForm = Ext.extend( Ext.Panel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		UserTypeForm.superclass.constructor.call(this,{
			border:false,
			labelWidth:90,
			baseCls:'x-plain',
			layout:'form',
			bodyStyle:'background:#F9F9F9;padding-top:4px',
			items:[{fieldLabel:'用户类型',
					id : 'comboForUserBaseForm',
					xtype:'paramcombo',
					width:150,
					allowBlank:false,
					hiddenName:'user_type',
					paramName:'USER_TYPE',
					listeners:{
						scope:this,
						select:this.doUserTypeSelect
				}
			}]
		
		});
	}
	//根据不同的用户类型，添加不同的面板
	,doUserTypeSelect:function(c,r,i){
		var userType = c.getValue();
		this.parent.changeSubFrom(userType);
	}
});

//数字用户面板
DtvUserForm = Ext.extend( Ext.Panel , {
	thiz:null,
	parent:null,
	constructor: function(p){
		this.parent=p;
		thiz = this;
		DtvUserForm.superclass.constructor.call(this, {
			trackResetOnLoad : true,
			border : false,
			labelWidth : 90,
			baseCls : 'x-plain',
			bodyStyle : 'background:#F9F9F9;padding-top:4px',
			layout : 'column',
			defaults : {
				layout : 'form',
				anchor : '100%',
				baseCls : 'x-plain',
				columnWidth : 0.5,
				bodyStyle : "background:#F9F9F9;padding-top:4px",
				labelWidth : 90,
				defaults : {
					xtype : 'panel',
					bodyStyle : "background:#F9F9F9;padding-top:4px",
					baseCls : 'x-plain'
				}
			},
			items:[{
				items:[{
					id:'terminal_type_id',
					fieldLabel:'终端类型',
					xtype:'paramcombo',
					width:150,
					allowBlank:false,
					hiddenName:'terminal_type',
					paramName:'TERMINAL_TYPE'
//					,listeners:{
//						afterrender:this.parent.expandTerimal
//					}
				},{
					xtype:'combo',
					id:'dtv_stb_id',hiddenName:'stb_id',
					fieldLabel:'机顶盒号',width:150,selectOnFocus:true,
					store:new Ext.data.ArrayStore({fields:['device_code']}),
					displayField:'device_code',valueField:'device_code',
					editable:true,emptyText:'请选择或输入...',enableKeyEvents:true,
					listeners:{
						scope:this,
						change:function(txt){
							//只有一个机顶盒号，初始化时延迟加载
							setTimeout(function(){
								thiz.parent.privateCheckDevice(txt,'STB',Ext.getCmp('dtv_card_id'),Ext.getCmp('modemMac'));
							},100);
						},
						select:function(txt){
							thiz.parent.privateCheckDevice(txt,'STB',Ext.getCmp('dtv_card_id'),Ext.getCmp('modemMac'));
						},
    					keyup:function(txt,e){//清空显示值时，清空缴费记录
    						var value = txt.getRawValue();
    						if(Ext.isEmpty(value)){
								var cardComp = Ext.getCmp('dtv_card_id');
								if(cardComp.getValue()){
									cardComp.reset();
									if(!cardComp.editable)
										cardComp.setEditable(true);
									if(cardComp.readOnly)
										cardComp.setReadOnly(false);
								}
							}
    					},
						afterrender:function(comp){thiz.parent.whetherData(comp);}
					}
				},{
					id:'serv_type_id',
					fieldLabel:'服务类型',
					xtype:'paramcombo',
					width:150,
					hiddenName:'serv_type',
					paramName:'DTV_SERV_TYPE',
					defaultValue:'SINGLE',
					listeners:{
						scope:this,
						afterrender:function(combo){
							var modem = Ext.getCmp('modemMac');
							modem.getStore().loadData(thiz.parent.modemArr);
							modem.reset();
							modem.disable();
							var netType = Ext.getCmp('net_type_id');
							netType.reset();
							netType.disable();//服务为"单向"时,无网络类别
							
							
						},
						select: function(combo){
							var value = combo.getValue();
							var modem = Ext.getCmp('modemMac');
							var netType = Ext.getCmp('net_type_id');
							var password = Ext.getCmp('password');
							var repassword = Ext.getCmp('repassword');
							if(value =='SINGLE'){//单向,不需要猫
								modem.reset();
								modem.disable();
								
								netType.reset();
								netType.disable();//服务为"单向"时,无网络类别
								
								password.reset();
								password.disable();
								
								repassword.reset();
								repassword.disable();
								
							}else if(value =='DOUBLE'){
								if(modem.disabled){
									modem.enable();
									modem.getStore().loadData(thiz.parent.doubleUserModem);
									if(modem.getStore().getCount()==1)
										modem.setValue(modem.getStore().getAt(0).get('device_code'));
								}
								if(netType.disabled){
									netType.enable();
									var netStore = netType.getStore();
									var index = netStore.find('net_type','CABLE');
									netType.setValue(netStore.getAt(index).get('net_type'));
									if(netStore.getAt(index).get('need_device') == 'T'){
										modem.allowBlank = false;
									}else{
										modem.allowBlank = true;
									}
								}
								
								//激活支付密码输入框
								password.reset();
								password.enable();
								
								repassword.reset();
								repassword.enable();
								
							}
							this.parent.doVodUserType(value);
						}
					}
				},{
					id:'net_type_id',
					hiddenName:'net_type',
					fieldLabel:'网络类别',
					width:150,
					xtype:'combo',
					store:new Ext.data.JsonStore({
							fields:[{name:'net_type'},{name:'net_type_name'},{name:'need_device'}],
							data:[]
						}),
					displayField:'net_type_name',
					valueField:'net_type',
					mode:'local',
					triggerAction:'all',
					hiddenField:'net_type_name',
					value:'CABLE',
					listeners:{
						scope : this,
						afterrender:function(combo){
							combo.getStore().loadData(App.getApp().findAllDtvNetType());
						},
						select : function(combo,record){
							var index = combo.getStore().find('net_type',combo.getValue());
							
							var comp = Ext.getCmp('modemMac');
							if(combo.getStore().getAt(index).get('need_device') == 'F'){
								comp.setReadOnly(false);
								comp.setValue('');
								comp.disable();
							}else{
								comp.enable();
								comp.allowBlank = false;
								if(comp.getStore().getCount() == 1){
									comp.setValue(comp.getStore().getAt(0).get('device_code'));
								}
							}
						}
					}
				}]
			},{
				items:[{
					fieldLabel:'停机类型',
					xtype:'paramcombo',
					allowBlank:false,
					width:150,
					hiddenName:'stop_type',
					paramName:'STOP_TYPE',
					defaultValue:'KCKT'
				},{
					xtype:'combo',
					id:'dtv_card_id',hiddenName:'card_id',width:150,
					store:new Ext.data.ArrayStore({fields:['device_code']}),
					displayField:'device_code',valueField:'device_code',
					allowBlank:false,editable:true,emptyText:'请选择或输入...',
					fieldLabel:'智能卡号',selectOnFocus:true,readOnly:false,
					listeners:{
						change:function(txt){thiz.parent.checkCardDevice(txt)},
						afterrender:function(comp){thiz.parent.whetherData(comp)}
					}
				},{
					xtype:'combo',
					width:150,
					id:'modemMac',hiddenName:'modem_mac',
					store:new Ext.data.JsonStore({fields:['device_code']}),
					displayField:'device_code',valueField:'device_code',
					editable:true,emptyText:'请选择或输入...',
					fieldLabel:'Modem 号',selectOnFocus:true,
					listeners:{
						change:function(txt){thiz.parent.privateCheckDevice(txt,'MODEM')},
						afterrender:function(comp){thiz.parent.whetherData(comp)}
					}
				},{
					xtype:'textfield',
					width:150,
					fieldLabel:'用户地址',
					name:'user_addr'
				}]
			}]
		})
	},
	initComponent: function(){
		DtvUserForm.superclass.initComponent.call(this);
		Ext.getCmp('dtv_stb_id').getStore().loadData(this.parent.stbArr);
		if(this.parent.stbArr.length==1){
			Ext.getCmp('dtv_stb_id').setValue(Ext.getCmp('dtv_stb_id').getStore().getAt(0).get('device_code'));
			Ext.getCmp('dtv_stb_id').fireEvent('change',Ext.getCmp('dtv_stb_id'));
		}
		
		Ext.getCmp('dtv_card_id').getStore().loadData(this.parent.cardArr);
		if(this.parent.cardArr.length==1){
			var deviceCode = Ext.getCmp('dtv_card_id').getStore().getAt(0).get('device_code');
			var deviceStore = App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
			
			var rec = deviceStore.query('pair_card_code',deviceCode);
			
			if(rec.items.length == 0){//不是配对卡号
				Ext.getCmp('dtv_card_id').setValue(deviceCode);
			}else if(rec.items.length == 1){//是配对卡号
				if(rec.items[0].get('device_code') == Ext.getCmp('dtv_stb_id').getValue()){//输入的正好是配对的机顶盒
					Ext.getCmp('dtv_card_id').setValue(deviceCode);
				}
			}
//			Ext.getCmp('dtv_card_id').fireEvent('change',Ext.getCmp('dtv_card_id'));
		}
		
		Ext.getCmp('modemMac').getStore().loadData(this.parent.doubleUserModem);
		if(this.parent.doubleUserModem.length==1){
			Ext.getCmp('modemMac').setValue(Ext.getCmp('modemMac').getStore().getAt(0).get('device_code'));
		}
	}
});

//模拟用户面板
AtvUserForm = Ext.extend( Ext.Panel , {
	parent:null,
	constructor: function(p){
		this.parent=p;
		AtvUserForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			border: false,
			labelWidth:90,
			baseCls: 'x-plain',
			bodyStyle:'background:#F9F9F9;padding-top:4px',
			items:[{
				xtype:'panel',
				anchor:'100%',
				layout:'column',
				bodyStyle:'background:#F9F9F9;padding-top:4px',
				baseCls: 'x-plain',
				defaults: { 
					layout: 'form',
					baseCls: 'x-plain',
					columnWidth:0.5,
					anchor: '100%',
					width:130,
					labelWidth:90
				},
				items:[{
					columnWidth:0.5,
					items:[{
						fieldLabel:'停机类型',
						xtype:'paramcombo',
						width:150,
						allowBlank:false,
						hiddenName:'stop_type',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					},{
						fieldLabel : '终端数量',
						xtype : 'numberfield',
						allowNegative : false,
						width:150,
						name : 'user_count'
					}]
				},{
					columnWidth:0.5,
					items:[{
						id:'serv_type_id',
						fieldLabel:'服务类型',
						xtype:'paramcombo',
						width:150,
						allowBlank:false,
						hiddenName:'serv_type',
						paramName:'ATV_SERV_TYPE',
						defaultValue:'YBMN'
					},{
						xtype:'textfield',
						width:150,
						fieldLabel:'用户地址',
						name:'user_addr'
						
					}]
				}]
			}]
		})
	}
});

//宽带用户面板
BandUserForm = Ext.extend( Ext.Panel, {
	thiz:null,
	parent:null,
	constructor: function(p){
		this.parent = p;
		thiz=this;
		BandUserForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			border: false,
			labelWidth:90,
			baseCls: 'x-plain',
			bodyStyle:'background:#F9F9F9;padding-top:4px',
			items:[{
				xtype:'panel',
				anchor:'100%',
				layout:'column',
				bodyStyle:'background:#F9F9F9;padding-top:4px',
				baseCls: 'x-plain',
				defaults: { 
					layout: 'form',
					baseCls: 'x-plain',
					columnWidth:0.5,
					anchor: '100%',
					labelWidth:90
				},
				items:[{
					items:[{
						fieldLabel:'认证方式',
						id : 'check_type_id',
						hiddenName:'check_type',
						width:150,
						xtype:'combo',
						store:new Ext.data.JsonStore({
							reader:new Ext.data.JsonReader({},
							[{name:'check_type'},{name:'check_type_name'},{name:'is_need_login_info'}]
							),
							fields:['check_type','check_type_name','is_need_login_info']
						}),
						allowBlank:false,
						displayField:'check_type_name',
						valueField:'check_type'
						,mode:'local',
						triggerAction:'all',
						value:'RADIUS',
						listeners:{
							afterrender:function(combo){
										combo.getStore().loadData(App.getApp().findAllCheckType());
							}
							,select:function(combo){
								var value = combo.getValue();
								var store = combo.getStore();
								var ind = store.find('check_type',value);
								var login = Ext.getCmp('login_name_id');
								var password = Ext.getCmp('password_id');
								if(store.getAt(ind).get('is_need_login_info')=='F'){//是否需要登录认证
									login.reset();
									password.reset();
									login.disable();
									password.disable();
								}else{
									if(login.disabled) login.enable();
									if(password.disabled) password.enable();
								}
							}
						}
					},{
						id:'login_name_id',
						fieldLabel:'登录账号',
						width:150,
						allowBlank:false,
						vtype : 'loginName',
						xtype:'textfield',
						name:'login_name',
						listeners:{
							change:function(txt){thiz.parent.checkLoginName(txt)}
						}
					}]
				},{
					items:[{
						fieldLabel:'绑定方式',
						xtype:'paramcombo',
						width:150,
						allowBlank:false,
						hiddenName:'bind_type',
						paramName:'BAND_BIND_TYPE',
						defaultValue:'UNBIND'
					},{
						id:'password_id',
						width:150,
						fieldLabel:'密码',
						allowBlank:false,
						minLength : 6,
						maxLength : 20,
						vtype : 'alphanum',
						xtype:'textfield',
						inputType:'password',
						name:'login_password',
						value:'123456'
					}]
				},{
					items:[{
						id:'net_type_id',
						hiddenName:'net_type',
						fieldLabel:'网络类别',
						width:150,
						xtype:'combo',
						store:new Ext.data.JsonStore({
								reader:new Ext.data.JsonReader({},
									[{name:'net_type'},{name:'net_type_name'},{name:'need_device'}]
								),
								fields:['net_type','net_type_name','need_device']
							}),
						displayField:'net_type_name',
						valueField:'net_type',
						mode:'local',
						triggerAction:'all',
						allowBlank:false,
						listeners:{
							afterrender:function(combo){
								var netStore = combo.getStore();
								netStore.loadData(App.getApp().findAllBandNetType());
								var index = netStore.find('net_type','CABLE')
								combo.setValue('CABLE');
								if(netStore.getAt(index).get('need_device') == 'T'){
									Ext.getCmp('modemMac').allowBlank = false;
								}else{
									Ext.getCmp('modemMac').allowBlank = true;
								}
							},
							select:function(combo,record){//宽带的网络类型不同，决定是否需要猫
								var value = combo.getValue();
								var store = combo.getStore();
								var ind = store.find('net_type',value);
								var modem = Ext.getCmp('modemMac');
								if(store.getAt(ind).get('need_device')=='F'){//是否需要登录认证
									modem.reset();
									modem.disable();
								}else{
									if(modem.disabled) modem.enable();
									modem.allowBlank = false;
									if(modem.getStore().getCount() == 1 ){
										modem.setValue(modem.getStore().getAt(0).get('device_code'));
									}
									
								}
							}
						}
					}/*,{
						fieldLabel:'最大连接数',
						width:150,
						xtype:'numberfield',
						name:'max_connection',
						allowDecimals:false,//不允许输入小数
						allowNegative:false,//不允许输入负数
						value:1
					}*/]
				},{
					items:[{
						id:'modemMac',hiddenName:'modem_mac',
						xtype:'combo',
						width:150,
						store:new Ext.data.JsonStore({fields:['device_code']}),
						displayField:'device_code',valueField:'device_code',
						editable:true,emptyText:'请选择或输入...',
						fieldLabel:'Modem号',
						listeners:{
							change:function(txt){thiz.parent.privateCheckDevice(txt,'MODEM')},
							afterrender:function(comp){thiz.parent.whetherData(comp)}
						}
					}/*,{
						fieldLabel:'最大用户数',
						width:150,
						xtype:'numberfield',
						name:'max_user_num',
						allowDecimals:false,//不允许输入小数
						allowNegative:false,//不允许输入负数
						allowBlank:false,
						value:1
					}*/]
				}]
			}]
		})
	},
	initComponent: function(){
		BandUserForm.superclass.initComponent.call(this);
		
		var modemCombo = Ext.getCmp('modemMac');
		modemCombo.getStore().loadData(this.parent.bandUserModem);
		if(this.parent.bandUserModem.length == 1){
			modemCombo.setValue(Ext.getCmp('modemMac').getStore().getAt(0).get('device_code'));
//			modemCombo.checked = true;//验证通过的属性
		}
		var login_name = Ext.getCmp('login_name_id');
		Ext.Ajax.request({
			async: false,
			url : Constant.ROOT_PATH+"/core/x/User!createLoginName.action",
			params:{
				loginName : App.getData().custFullInfo.cust.cust_name
			},
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				login_name.setValue(data);
			}
		});
		
		
	}
});


UserBaseForm = Ext.extend( BaseForm , {
	oldUsertype:'DTV',
	extAttrForm:null,
	userTypeForm:null,
	dtvUserForm:null,
	atvUserForm:null,
	bandUserForm:null,
	interactiveType : 'SINGLE',
	stbArr : [],
	cardArr : [],
	modemArr : [],
	bandUserModem : [],//宽带用户可用modem
	doubleUserModem : [],//双向用户可用modem
	/**
	 * 对象obj
	 * @property dtv : 用户存在情况;0:只有宽带；1：存在DTV;2:存在ATV
	 * @property zzd : 主终端数量
	 * @property fzd : 副终端数量
	 * @property ezd : 第二主机
	 */
	obj : {'dtv' : 0,'zzd' : 0,'fzd' : 0,'ezd': 0},
	configs : [],//系统配置终端数
	constructor: function(p){
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
		var device = cdStore.query('status','IDLE');
		device.each(function(item,index,length){
			var data = item.data;
			if(item.data.loss_reg == 'F'){//空闲且没挂失
				if(data['device_type'] == 'STB'){
					this.stbArr.push([data['device_code']]);
					//虚拟modem可以用于双向和宽带
					//if(!Ext.isEmpty(data['pair_modem_code'])){
					//	this.modemArr.push( data['pair_modem_code'] );
					//}
				}else if(data['device_type'] == 'CARD'){
					this.cardArr.push([data['device_code']]);
				}else if(data['device_type'] == 'MODEM'){
					this.modemArr.push( data['device_code'] );
				}
			}
		},this);
		
		this.setUseableModem();
		
		this.doInitAttrForm(1);
		this.userTypeForm = new UserTypeForm(this);
		
		this.dtvUserForm = new DtvUserForm(this);
		
		UserBaseForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            layout:'border',
            bodyStyle: Constant.TAB_STYLE,
           	defaults:{
				layout: 'form',
           		border:false,
           		bodyStyle:'background:#F9F9F9;'
           	},
			items:[{
				region:'north',
				height:27,
				items:[this.userTypeForm]
			},{
				region:'center',
				defaults: {
					bodyStyle: "background:#F9F9F9"
				},
				items:[this.dtvUserForm,this.extAttrForm]
			}]
		});
		
	}
	,doInit:function(){
		UserBaseForm.superclass.doInit.call(this);
		/**
		 * 初始化可以选择的用户类型和终端类型。如果客户当前用户列表中有数字电视用户，则不能选择模拟用户类型。
		 * 属于居民用户的数字电视如果是主终端且报停，则不能选择数字用户类型
		 * @param {} combo
		 */
		this.initList();
		
	},
	initList : function(){
		//用户类型下拉框
		var combo = Ext.getCmp('comboForUserBaseForm');
		var store = combo.getStore();
		//小区表里的  NET_TYPE字段要限制当前小区的可开户的客户类型
		var addrId = App.getData().custFullInfo.cust.addr_id;
		
		Ext.Ajax.request({
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!querySingleAddress.action",
			params:{addrId:addrId},scope:this,
			async:false,
			timeout:99999999999999,//12位 报异常
			success:function(res,opt){
				var add = Ext.decode(res.responseText);
				var netTypes = add.net_type;
				if(!netTypes || Ext.isEmpty(netTypes)){
					Alert('当前小区没有匹配的服务!',function(){
						App.getApp().menu.bigWindow.hide();
					});
					return;
				}
				netTypes = netTypes.split(',');
				var userTypeDatas = [];
				store.each(function(rec){
				    var item_value = rec.get('item_value');
				    if(netTypes.contain(item_value)){
				        userTypeDatas.push(rec.data);
				    }
				});
				if(userTypeDatas.length == 0){//不太可能,但是以防万一
					Alert('当前小区没有匹配的服务!',function(){
						App.getApp().menu.bigWindow.hide();
					});
					return;
				}
				store.loadData(userTypeDatas);
				
				for(var idx =0;idx<netTypes.length;idx++){
					var type = netTypes[idx];
					
				}
			}
		});
		//用户数据
		var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		
		userStore.each(function(r){
			if(r.get('user_type') == 'DTV'){
				this.obj.dtv = 1;
				store.removeAt(store.find('item_value','ATV'));
				return false;
			}
			
			if(r.get('user_type') == 'ATV'){
				this.obj.dtv = 2;
				store.removeAt(store.find('item_value','DTV'));
				return false;
			}
		},this);
		
		//客户类别
		var custType = App.getData().custFullInfo.cust.cust_type;
		
		if(this.obj.dtv == 2){
			var index = store.find('item_value','ATV');
			combo.setValue(store.getAt(index).get('item_value'));
		}else{
			var index = store.find('item_value','DTV');
			combo.setValue(store.getAt(index).get('item_value'));
		}
		combo.fireEvent('select',combo);
		
		//数字电视模拟电视基本产品状态不正常，只能开宽带
		if(this.obj.dtv == 1 || this.obj.dtv == 2){
			var user = userStore.getAt(userStore.find('terminal_type','ZZD'));
			var prodMap = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap;
			var arr = prodMap[user.get('user_id')];
			if(arr && arr.length>0){
				for(var idx =0;idx<arr.length;idx++){
					var prod = arr[idx];
					if(prod.is_base != 'T'){
						continue;
					}else{
						if(prod.status != 'ACTIVE' && prod.status != 'OUNSTOP' ){
							store.removeAt(store.find('item_value','DTV'));
							store.removeAt(store.find('item_value','ATV'));
							if(store.getAt(0)){
								combo.setValue(store.getAt(0).get('item_value'));
								combo.fireEvent('select',combo);
							}else{
								Alert("主终端基本包状态不正常,不能开户（数字电视,模拟电视）!如果是开宽带用户，确认该小区配置是否有宽带网络!");
								combo.setValue("");
							}
							return false;
						}
					}
				}
			}
		}
		
		
		if(this.obj.dtv == 1){//数字电视才需要终端
			var flag = false;
			//居民 数字电视 报停 且是 主终端 ，只能开宽带（模拟也不行，因为已经有数字）
			userStore.each(function(r){
				if(r.get('user_type') == 'DTV' && custType == 'RESIDENT' &&
					r.get('status') == 'REQSTOP' && r.get('terminal_type')=='ZZD'){//数字电视 报停 且是 主终端
					store.removeAt(store.find('item_value','DTV'));
					combo.setValue(store.getAt(0).get('item_value'));
					combo.fireEvent('select',combo);
					flag = true;
					return false;
				}
			});
			if(flag){//满足上面情况，只能开宽带，不需要考虑终端
				return;
			}
			
			this.doTerminalTypeCount();
			
			this.doConfigsData();
		}
		this.doFilterTerminal();
		//集团用户开户时，停机类型默认为不催不停。
		if(App.getCust().cust_type == "NONRES"){
			var arr = this.findByType("paramcombo");
			for(var index =0;index<arr.length;index++){
				if(arr[index].hiddenName == 'stop_type'){
					arr[index].setValue('BCBT');
					break;
				}
			}
		}
	},
	//获取终端限制数量的数据
	doConfigsData: function(){
		//客户类别
		var custType = App.getData().custFullInfo.cust.cust_type;
		var data = App.getApp().findAllTerminalAmount();
		for(var i=0;i<data.length;i++){
			if(data[i].cust_type == custType && data[i].user_type == 'DTV'){
				this.configs.push(data[i]);
			}
		}
	},
	//用户信息列表中数字电视各个终端类型的数量
	doTerminalTypeCount:function(){
		//用户数据
		var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		//统计现有各种终端类型的数量
		userStore.each(function(r){
			if(r.get('user_type') == 'DTV'){
				if(r.get('terminal_type')=='ZZD'){
					this.obj.zzd = this.obj.zzd + 1;
				}else if(r.get('terminal_type')=='FZD'){
					this.obj.fzd = this.obj.fzd + 1;
				}else if(r.get('terminal_type')=='EZD'){
					this.obj.ezd = this.obj.ezd + 1;
				}
			}
		},this);
		delete this.userStore;
	},
	doFilterTerminal : function(){//过滤、设置终端类型
		//终端类型下拉框
		var terminal = Ext.getCmp('terminal_type_id');
		if(terminal){
			var terStore = terminal.getStore();
			terStore.removeAt(terStore.find('item_value','EZD'));
			if(this.obj.dtv == 0){//只有宽带用户或没有用户
				terStore.removeAt(terStore.find('item_value','FZD'));
				terStore.removeAt(terStore.find('item_value','EZD'));
			}else{
				for(var i=0;i<this.configs.length;i++){
					if(this.configs[i].terminal_type == 'ZZD'){
						if(this.obj.zzd >= this.configs[i].amount){
							terStore.removeAt(terStore.find('item_value','ZZD'));
						}
					}else if(this.configs[i].terminal_type == 'FZD'){
						if(this.obj.fzd >= this.configs[i].amount){
							terStore.removeAt(terStore.find('item_value','FZD'));
						}
					}else if(this.configs[i].terminal_type == 'EZD'){
						if(this.obj.ezd >= this.configs[i].amount){
							terStore.removeAt(terStore.find('item_value','EZD'));
						}
					} 
				}
			}
			if(terStore.getCount() > 0){
				if(terStore.find('item_value','ZZD') > -1){
					terminal.setValue('ZZD');
				}else if(terStore.find('item_value','FZD') > -1){
					terminal.setValue('FZD');
				}else if(terStore.find('item_value','EZD') > -1){
					terminal.setValue('EZD');
				}
			}
		}
	},
	//默认为新增的扩展信息
	doInitAttrForm: function(group){
		var cfg = [{
	   	   columnWidth: .5,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 90
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
		   labelWidth: 90
	    }];
	    //扩展信息面板
		this.extAttrForm = ExtAttrFactory.gExtForm({
			tabName: Constant.C_USER,
			group: group,
			prefixName : ''
		}, cfg,this,this.doExtAttrFunc); 
	},
	doExtAttrFunc: function(){	//扩展属性添加并加载值后，触发(paramcombo)
		var servTypeComp = Ext.getCmp('serv_type_id');
		if(servTypeComp){
			this.doVodUserType(servTypeComp.getValue());
		}
	},
	doVodUserType: function(servType){
		var vodUserType = this.getForm().findField('str11');
		if(vodUserType){
			//不是用户开用，已经有值的，不重新设置
			//修改用户资料、用户等级时，有值，不重新设置
			if(App.getApp().getData().currentResource.busicode == '1020' 
				|| Ext.isEmpty(vodUserType.getValue())){
				if(servType == 'DOUBLE'){
					vodUserType.setValue('PUBLIC');
					vodUserType.enable();
					vodUserType.allowBlank = false;
				}else{
					vodUserType.setValue('');
					vodUserType.allowBlank = true;
					vodUserType.clearInvalid();
					vodUserType.disable();
				}
			}
		}
		var SingleCardType = this.getForm().findField('str1');
		if(SingleCardType){
			//不是用户开用，已经有值的，不重新设置
			//修改用户资料、用户等级时，有值，不重新设置
			if(App.getApp().getData().currentResource.busicode == '1020' 
				|| Ext.isEmpty(SingleCardType.getValue())){
				if(servType == 'DOUBLE'){
					SingleCardType.setValue('');
					SingleCardType.clearInvalid();
					SingleCardType.disable();
				}else{
					SingleCardType.enable();
				}
			}
		}
	},
	changeSubFrom:function(formtype){
		if(!this.oldUsertype){
			this.oldUsertype = formtype;
		}else if(formtype == this.oldUsertype){
			return;
		}
		var item = this.items.itemAt(1);
		item.removeAll();
		if (formtype == 'DTV') {
			this.dtvUserForm = new DtvUserForm(this);
			App.form.initComboData( this.dtvUserForm.findByType("paramcombo"),this.doFilterTerminal,this);
			item.add(this.dtvUserForm);
			this.doInitAttrForm(1);
		} else if (formtype == 'ATV') {
			this.atvUserForm = new AtvUserForm(this);
			App.form.initComboData( this.atvUserForm.findByType("paramcombo"));
			item.add(this.atvUserForm);
			this.doInitAttrForm(2);
		} else if (formtype == 'BAND') {
			this.bandUserForm = new BandUserForm(this);
			App.form.initComboData( this.bandUserForm.findByType("paramcombo"));
			item.add(this.bandUserForm);
			this.doInitAttrForm(3);
		}
		
		this.extAttrForm["region"] = "south";
		item.add(this.extAttrForm);
		item.doLayout();
		this.oldUsertype = formtype;
//		this.setUseableModem();
	},
	setUseableModem : function(){//设置可用的modem
		var userStore =  App.getApp().main.infoPanel.userPanel.userGrid.getStore();
		var bandUser = userStore.query('user_type','BAND');
		var bandModems = [];
		//已开宽带用户modem
		bandUser.each(function(item){
			if(item.get('modem_mac')){
				bandModems.push(item.get('modem_mac'));
			}
		});
		
		var doubleUser = userStore.query('user_type','DTV');
		var doubleModems = [];
		//已开数字双向用户modem
		doubleUser.each(function(item){
			if(item.get('modem_mac')){
				doubleModems.push(item.get('modem_mac'));
			}
		});
		
		this.doubleUserModem = [];	//可以用于开数字双向的modem
		this.bandUserModem = [];	//可以用于开宽带双向的modem(不能用虚拟modem开户)
		Ext.each(this.modemArr,function(data){
			this.doubleUserModem.push({'device_code':data});
			this.bandUserModem.push({'device_code':data});
		},this);
		
		
		var copyBandModems = [];	//删除前备份
		Ext.apply(copyBandModems,bandModems);
		//仅用于宽带的modem
		if(doubleModems.length > 0){
			for(var i=bandModems.length -1;i>=0;i--){
				//如果宽带的modem已经用于双向，移除
				if(doubleModems.contain(bandModems[i])){
					bandModems.remove(bandModems[i]);
				}
			}
		}
		
		//仅用于双向的modem
		if(copyBandModems.length > 0){
			for(var i=doubleModems.length -1;i>=0;i--){
				//如果双向的modem已经用于宽带，移除
				if(copyBandModems.contain(doubleModems[i])){
					doubleModems.remove(doubleModems[i]);
				}
			}
		}
		
		
		//用于宽带的modem，可以用于开数字双向用户
		if(bandModems.length > 0){
			Ext.each(bandModems,function(data){
				this.doubleUserModem.push({'device_code':data});
			},this);
		}
		
		//用于宽带的modem，可以用于开数字双向用户
		if(doubleModems.length > 0){
			Ext.each(doubleModems,function(data){
				this.bandUserModem.push({'device_code':data});
			},this);
		}
		
	},
	privateCheckDevice : function(txt,deviceType,cardComp,modemComp){
		//使用rawvalue,在提交时能获得值
		var value = txt.getRawValue();
		if(Ext.isEmpty(value))return;
		
		Ext.Ajax.request({
			async: false,
			url:Constant.ROOT_PATH + "/core/x/Cust!queryUseableDevice.action",
			params:{
				deviceType:deviceType,
				deviceCode:value,
				custId:App.getCustId(),
				userType : this.oldUsertype
			},
			scope:this,
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				if(deviceType== 'STB' && data['deviceModel']){//设备类型为机顶盒时保存交互方式 
					this.interactiveType = data.deviceModel.interactive_type;
				}
				if(Ext.isEmpty(data['device_code'])){//查询的设备不正确
					txt.reset();
				}else if(value != data['device_code']){//是否更换的是同一个设备
					txt.setValue(data.device_code);
				}
				var pairCard = data['pairCard'];
				if(cardComp){
					if(pairCard && pairCard['card_id']){
						cardComp.setValue(pairCard['card_id']);
						if(cardComp.edtiable)
							cardComp.setEditable(false);
						if(!cardComp.readOnly)
							cardComp.setReadOnly(true);
					}else{
						cardComp.reset();
						if(!cardComp.editable)
							cardComp.setEditable(true);
						if(cardComp.readOnly)
							cardComp.setReadOnly(false);
					}
				}
				var pairModem = data['pairModem'];
				if(modemComp){
					if(pairModem && pairModem['modem_mac']){
						modemComp.enable();
						modemComp.setValue(pairModem['modem_mac']);
						modemComp.setReadOnly(true);
						var servTypeComp = Ext.getCmp('serv_type_id');
						servTypeComp.setValue('DOUBLE');
						servTypeComp.setReadOnly(true);
						
						var netTypeCombo = Ext.getCmp('net_type_id');
						netTypeCombo.enable();
						netTypeCombo.setValue('CABLE');
						
						var modemStore = modemComp.getStore();
						modemStore.loadData([{'device_code':pairModem['modem_mac']}]);
						
					}else{
						var servTypeComp = Ext.getCmp('serv_type_id');
						servTypeComp.reset();
						if(servTypeComp.readOnly)servTypeComp.setReadOnly(false);
						var netTypeComp = Ext.getCmp('net_type_id');
						netTypeComp.reset();
						netTypeComp.disable();
						
						if(!modemComp.disabled)modemComp.disable();
						modemComp.reset();
						if(!modemComp.editable)
							modemComp.setEditable(true);
						if(modemComp.readOnly)
							modemComp.setReadOnly(false);
					}
					this.doVodUserType(Ext.getCmp('serv_type_id').getValue());
				}
				
				txt.checked = true;
			},
			clearData:function(){
				txt.reset();
				this.interactiveType = 'SINGLE';
				if(cardComp && cardComp.getValue())
					cardComp.reset();
			}
		});
	},
	checkCardDevice:function(txt){
		var value = txt.getRawValue();
		if(txt.readOnly == true){//这里得以正确执行的前提是如果是机卡配对的,输入机顶盒号的时候智能卡输入框会被填充并禁用，其他情况不会,跟单独做个标记(是否是通过查询机顶盒获得的智能卡号填入的)效果一样
			var stbCmp = Ext.getCmp('dtv_stb_id');
			var stb = stbCmp.getValue();
			if(!Ext.isEmpty(stb) && !Ext.isEmpty(value)){//加个保险
				txt.checked = true;
				return true;
			}
		}
		if(value){
			Ext.Ajax.request({
				async: false,
				url:Constant.ROOT_PATH + "/core/x/Cust!queryUseableDevice.action",
				params:{
					deviceCode:value,
					deviceType : 'CARD',
					custId:App.getCustId()
				},
				success:function(res,opt){
					txt.checked = true;
				},
				clearData:function(){
					txt.reset();
				}
			});
		}
	},
	checkLoginName : function(txt){
		var value = txt.getValue();
		if(value){
			Ext.Ajax.request({
				async: false,
				url : Constant.ROOT_PATH+"/core/x/User!checkLoginName.action",
				params:{
					loginName : value
				},
				success:function(res,opt){
					txt.checked = true;
				},
				clearData:function(){
					txt.reset();
				}
			});
		}
	},
	whetherData : function(comp){
		if(comp.getStore().getCount()==0){
			comp.listEmptyText='';	
		}
	},
	doValid : function(){
		var obj = {};
		if(this.oldUsertype == 'DTV'){
			if(Ext.getCmp('serv_type_id').getValue() == 'DOUBLE' 
				&& this.interactiveType != 'DOUBLE' ){//双向服务，但机顶盒类型是单向
				obj["isValid"] = false;
				obj["msg"] = "请输入交互方式为双向的机顶盒";
				return obj;
			}
			if(Ext.getCmp('serv_type_id').getValue() == 'DOUBLE' 
				&& this.getForm().findField('str11')!=null && this.getForm().findField('str11').getValue() == '' ){//双向服务，双向用户类型不能为空
				obj["isValid"] = false;
				obj["msg"] = "请选择双向用户类型";
				return obj;
			}
			if(Ext.isEmpty(Ext.getCmp('dtv_stb_id').getValue())){
				
				if (this.getForm().isValid()){
					obj["isValid"] = true;
					this.confirmMsg = "确定不输入机顶盒？";
					this.yesBtn = false;
					this.isCloseBigWin = false;
				}else{
					obj["isValid"] = false;
					obj["msg"] = "含有验证不通过的输入项";
				}
				return obj;
			}else{
				this.confirmMsg = null;
			}
		}
		
		return UserBaseForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var values = this.getForm().getValues();
		
		var userType = values.user_type;//用户类型
		var obj={};
		var type;
		if(userType == 'ATV'){
			type="userAtv";
		}else if(userType == 'DTV'){
			type="userDtv";
			values['terminal_type']=Ext.getCmp('terminal_type_id').getValue();
		}else if(userType == 'BAND'){
			type="userBroadband";
		}
		//根据不同用户类型，给需要的属性添加前缀，以供后台action中组合不同对象
		for(var i in values){
			if(i.indexOf(CoreConstant.Ext_FIELDNAME_PREFIX)==-1
				&& i.indexOf('.')==-1)//去除扩展form字段和属性中有"."的
				obj[type+'.'+i]=values[i];
		}
		return obj;
	}
})