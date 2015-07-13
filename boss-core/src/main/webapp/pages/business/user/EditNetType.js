/**
*修改接入方式
*/

EditNetTypeForm = Ext.extend(BaseForm,{
	url:root+'/core/x/User!saveEditNetType.action',
	netType:null,//新接入方式
	constructor: function(){
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		var oldNetTypeName = record.get('net_type_text');
		
		//数字电视接入方式集合
		var dtvNetType = App.getApp().findAllDtvNetType();
		
		var newNetType = null;
		//LAN 和 CABLE 2种
		for(var i=0,len=dtvNetType.length;i<len;i++){
			if(dtvNetType[i]['net_type'] != record.get('net_type')){
				newNetType = dtvNetType[i];
				break;
			}
		}
		//接入方式显示名称
		var netNetTypeName = newNetType['net_type_name'];
		this.netType = newNetType['net_type'];
		
		//查找能用的设备
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
		var device = cdStore.query('status','IDLE');
		var modemArr=[];
		device.each(function(item,index,length){
			var data = item.data;
			if(data['status'] == 'IDLE' && data['loss_reg'] == 'F'){
				if(data['device_type'] == 'MODEM'){
					modemArr.push(data['device_code']);
				}else if(data['device_type'] == 'STB' && !Ext.isEmpty(data['pair_modem_code'])){
					modemArr.push(data['pair_modem_code']);
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
		var usableModem = [];
		if(modemArr.length > 0){
			//空闲设备
			Ext.each(modemArr,function(data){
				usableModem.push({'device_code':data});
			},this);
		}
		
		if(record.get('user_type')  == 'DTV'){
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
				Ext.each(bandModems,function(data){
					usableModem.push({'device_code':data});
				},this);
			}
		}else{
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
				Ext.each(doubleModems,function(data){
					usableModem.push({'device_code':data});
				},this);
			}
		}
		
		var modemStore = new Ext.data.JsonStore({
			fields:['device_code'],
			data : usableModem
		});
		
		EditNetTypeForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			defaults: {
                layout: 'form',
                border: false,
                labelWidth: 100,
                bodyStyle: "background:#F9F9F9;"
             },
			items:[
				{fieldLabel:'原接入方式',xtype:'displayfield',name:'oldNetType',
					readOnly:true,value:oldNetTypeName},
				{fieldLabel:'新入方式',xtype:'displayfield',name:'newNetType',
					readOnly:true,value:netNetTypeName},
					{id:'modem_ct_id',hidden:true,
						items:[{
							xtype : 'combo',
							id:'modem_mac_id',
							fieldLabel : 'MODEM号',	
							width : 130,
							name : 'modem_mac',
							editable:true,
							store : modemStore,
							displayField:'device_code',valueField:'device_code',
							listeners : {
								'change' : function(field){
									field.fireEvent('select',field);
								},
								select:function(field){App.getApp().checkDevice(field,'MODEM',null,record.get('user_type'))}
							}
						}]
					}
			]
		});	
	},
	initComponent: function(){
		EditNetTypeForm.superclass.initComponent.call(this);
		if(App.getApp().findDtvNetTypeById(this.netType).need_device == 'T'){
			var comp = Ext.getCmp('modem_ct_id');
			comp.hidden = false;
			Ext.getCmp('modem_mac_id').allowBlank = false;
		}
	},
	getValues:function(){
		var obj = {};
		obj['netType'] = this.netType;
		obj['modemMac'] = Ext.getCmp('modem_mac_id').getValue();
		return obj;
	},
	success: function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var form = new EditNetTypeForm();
	var box = TemplateFactory.gTemplate(form);
});