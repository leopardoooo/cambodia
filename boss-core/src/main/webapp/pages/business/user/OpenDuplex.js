/**
 *【开通双向】 
 */
OpenDuolexPanle = Ext.extend(Ext.Panel,{
	parent:null,
	modemMacForm : null,
	netType : null,
	doubleUserModem: [],
	constructor: function(p){
		this.parent = p;
		this.modemMacForm = new ModemMacForm(this);
		OpenDuolexPanle.superclass.constructor.call(this,{ 
			border: false,
			labelWidth:105,
			title: '业务信息',
			bodyStyle: Constant.TAB_STYLE,
			layout:'form',
			region:'center',
			anchor:'100%',
			items:[{
				xtype:'textfield',
				inputType : 'password',
				id : 'password',
				fieldLabel:'支付密码',
				maxLength :20,
				vtype : 'num',
				name:'password',
				readOnly:true,
				listeners:{
					scope:this,
					focus:function(comp){
						GetPassword(1);	
					}
				}
			},{
				xtype:'textfield',
				inputType : 'password',
				fieldLabel:'密码确认',
				id : 'repassword',
				name:'repassword',
				vtype : 'password',
				readOnly:true,
				initialPassField : 'password',
				listeners:{
					scope:this,
					focus:function(comp){
						GetPassword(2);	
					}
				}
			},{
				xtype:'paramcombo',
				fieldLabel:'延长包换期三年',
				hiddenName:'remainReplacoverDate',
				paramName:'BOOLEAN',
				defaultValue:'F',
				allowBlank:false
			},{
				id:'nettype',
				fieldLabel:'网络类型',
				xtype:'combo',
				width : 130,
				hiddenName:'net_type',
				store:new Ext.data.JsonStore({
						reader:new Ext.data.JsonReader({},
							[{name:'net_type'},{name:'net_type_name'},{name:'need_device'}]
						),
						fields:['net_type','net_type_name','need_device'],
						listeners:{
							scope:this,
							load:this.doInitData
						}
					}),
				allowBlank:false,
				displayField:'net_type_name',
				valueField:'net_type',
				mode:'local',
				triggerAction:'all',
				blankText:'请选择',
				listeners:{
					scope:this,
					'select':this.doNetTypeSelect,
					afterrender:function(combo){
						combo.getStore().loadData(App.getApp().findAllDtvNetType());
					}

				}
			}]
		});
	},
	doInitData: function(){
		//查找能用的设备
		var record = App.getApp().main.infoPanel.userPanel.userGrid.getSelectionModel().getSelected();
		var stbId = record.get('stb_id');
		
		this.doubleUserModem = [];
		var modemMac = record.get('modem_mac');
		if(record.get('modem_mac')){
			this.doubleUserModem.push({'device_code':modemMac});
			Ext.getCmp('nettype').setValue('CABLE');
			Ext.getCmp('nettype').setReadOnly(true);
			Ext.getCmp('nettype').fireEvent('select',Ext.getCmp('nettype'));
		}else{
			var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
			cdStore.each(function(r){
				if(r.get('device_code') == stbId){
					if(r.get('device_type') == 'STB' && r.get('device_code') == stbId 
						&& !Ext.isEmpty(r.get('pair_modem_code'))
						&& r.get('status') == 'USE' && r.get('loss_reg') == 'F'){
						this.doubleUserModem.push({'device_code':r.get('pair_modem_code')});
						return false;
					}
				}
			},this);
			
			if(this.doubleUserModem.length == 0){
				var device = cdStore.query('status','IDLE');
				var modemArr=[];
				device.each(function(item,index,length){
					var data = item.data;
					if(data['status'] == 'IDLE' && data['loss_reg'] == 'F'){
						if(data['device_type'] == 'MODEM'){
							modemArr.push(data['device_code']);
						}
					}
				});
				
				var userStore =  App.getApp().main.infoPanel.userPanel.userGrid.getStore();
				var bandUser = userStore.query('user_type','BAND');
				var bandModems = [];//用于宽带的modem
				bandUser.each(function(item){
					if(item.get('modem_mac')){
						bandModems.push(item.get('modem_mac'));
					}
				})
				
				var doubleUser = userStore.query('user_type','DTV');
				var doubleModems = [];//用于双向的modem
				doubleUser.each(function(item){
					if(item.get('modem_mac')){
						doubleModems.push(item.get('modem_mac'));
					}
				})
				
				//空闲设备
				if(modemArr.length > 0){
					Ext.each(modemArr,function(data){
						this.doubleUserModem.push({'device_code':data});
					},this);
				}
				
				//仅用于宽带的modem
				if(doubleModems.length > 0){
					for(var i=bandModems.length -1;i>=0;i--){
						//如果宽带的modem已经用于双向，移除
						if(doubleModems.contain(bandModems[i])){
							bandModems.remove(bandModems[i]);
						}
					}
					
				}
				
				//用于宽带的modem，可以用于开数字双向用户
				if(bandModems.length > 0){
					Ext.each(bandModems,function(data){
						this.doubleUserModem.push({'device_code':data});
					},this);
				}
				
			}else{
				Ext.getCmp('nettype').setValue('CABLE');
				Ext.getCmp('nettype').setReadOnly(true);
				Ext.getCmp('nettype').fireEvent('select',Ext.getCmp('nettype'));
			}
		}
		this.modemMacForm.modemStore.loadData(this.doubleUserModem);
	},
	//根据网络类型 判断是否需要Modem设备
	doNetTypeSelect:function(c,r,i){
		var that = this;
		if (c.getValue()) {
			var comp = Ext.getCmp('moemMacFormId')
			if (comp) {
				that.remove(comp, true);
			}
			if (App.getApp().findDtvNetTypeById(c.getValue()).need_device == 'T') {
				if(!comp){
					comp = new ModemMacForm(this);
					comp.modemStore.loadData(this.doubleUserModem);
				}
				that.modemMacForm = comp;
				that.add(that.modemMacForm);
			}
			that.doLayout();
		}
	},
	// 检查设备是否存在
	checkDevice:function(txt,deviceType){
		//使用rawvalue,在提交时能获得值
		var value = txt.getRawValue().trim();
		
		if(value=='')return;
		Ext.Ajax.request({
			async: false,
			url:Constant.ROOT_PATH + "/core/x/Cust!queryUseableDevice.action",
			params:{
				deviceType:deviceType,
				deviceCode:txt.getValue(),
				custId:App.getCustId(),
				userType : 'DTV'
			},
			success:function(res,ops){
				var data = Ext.decode(res.responseText);
				if(!data || data.device_type!=deviceType){
					txt.reset();
				}
				txt.checked = true;
			},
			clearData:function(){
				txt.reset();
			}
		});
	}
});
/**
 * Modem选项Panel
 */
ModemMacForm = Ext.extend( Ext.Panel , {
	parent: null,
	modemStore: null,
	doublUserModem : [],
	constructor: function(p){
		this.parent = p;
		
		this.modemStore = new Ext.data.JsonStore({
			fields:['device_code']
		});
		
		this.modemStore.on("load",function(store,records){
			if(store.getCount() == 1){
				Ext.getCmp('modemmac_id').setValue(records[0].get('device_code'));
				Ext.getCmp('modemmac_id').setReadOnly(true);
			}
		},this);
		
		ModemMacForm.superclass.constructor.call(this, {
			id:'moemMacFormId',
			trackResetOnLoad:true,
			border: false,
			labelWidth:95,
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;padding-top : 5px",
			layout : 'form',
			anchor:'100%',
			items:[{
				xtype : 'combo',
				fieldLabel : 'MODEM号',	
				id:'modemmac_id',
				width : 130,
				name : 'modem_mac',
				allowBlank : false,
				store : this.modemStore,
				displayField:'device_code',valueField:'device_code',triggerAction:'all',modem:'local',
				listeners : {
					scope:this,
					change: function(field){
						field.fireEvent('select',field);
					},
					select: function(field){this.parent.checkDevice(field,'MODEM')}
				}
			}]
		});
	}
});
/**
 * 开通双向业务窗体
 * @class OpenDuplexForm
 */
OpenDuplexForm = Ext.extend(BaseForm,{
	openDuolexPanle : null,
	userInfoPanel : null,
	url: Constant.ROOT_PATH + "/core/x/User!openDuplex.action",
	constructor: function(){
		this.userInfoPanel = new UserInfoPanel();
		this.openDuolexPanle = new OpenDuolexPanle(this);
		OpenDuplexForm.superclass.constructor.call(this,{
            layout:'border',
            border:false,
            bodyStyle: Constant.TAB_STYLE,
			items:[this.userInfoPanel,this.openDuolexPanle]	
		})
	},
	doValid : function(){
		var modemCombo = Ext.getCmp('modemmac_id');
		if(modemCombo && modemCombo.hasFocus){
			this.openDuolexPanle.checkDevice(modemCombo,'MODEM');
			if(modemCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "MODEM有问题，请在验证后保存";
				return obj;
			}
		}
		
		return OpenDuplexForm.superclass.doValid.call(this);
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
