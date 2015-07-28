/**
 * 用户开户表单
 */
UserBaseForm = Ext.extend( BaseForm , {
	buyModeStore: null,
	constructor: function(p){
		this.buyModeStore = new Ext.data.JsonStore({
			url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
			fields : ['buy_mode','buy_mode_name'],
			autoLoad: true
		});
		
		UserBaseForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            bodyStyle: Constant.TAB_STYLE,
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
						fieldLabel:'用户类型',
						xtype:'paramcombo',
						width:150,
						allowBlank:false,
						hiddenName:'user_type',
						paramName:'USER_TYPE',
						listeners:{
							scope:this,
							select:this.doSelectUserType
						}
					},]
				},{
					items:[{
						fieldLabel:'停机类型',
						xtype:'paramcombo',
						allowBlank:false,
						width:150,
						hiddenName:'stop_type',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					}]
				}]
			},{
				anchor:'100%',
				xtype: 'compositefield',
			    labelWidth: 120,
			    fieldLabel: "设备编号",
			    items: [{
		            xtype: 'textfield',
		            emptyText:'请输入设备编号..',
		            disabled: true,
		            flex: 1
		        },{
		            xtype: 'checkbox',
		            boxLabel: "施工回填",
		            checked: true,
		            width: 84,
		            listeners:{
		            	scope: this,
		            	check: function(box, checked){
		            		alert(checked);
		            	}
		            }
			    }]
			},{
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
						fieldLabel:'设备类型',
						xtype:'textfield',
						disabled : true,
						width:150
					},{
						fieldLabel:'购买方式',
						xtype:'combo',
						id : 'deviceBuyMode',
						forceSelection : true,
						store : this.buyModeStore,
						triggerAction : 'all',
						mode: 'local',
						width : 150,
						displayField : 'buy_mode_name',
						valueField : 'buy_mode',
						emptyText: '请选择',
						allowBlank : false,
						editable : false,
						listeners:{
							scope: this,
							'beforequery' : this.loadBuyModeStore
						}
					}]
				},{
					items:[{
						fieldLabel:'设备型号',
						width : 150,
						//style : Constant.TEXTFIELD_STYLE,
						xtype:'textfield',
						name:'device_model_text',
						readOnly : true
					},{
						fieldLabel:'收费金额',
						xtype:'textfield',
						width:150,
						allowBlank:false
					}]
				}]
			},{
				anchor:'96%',
				xtype: 'textarea',
			    labelWidth: 120,
			    height: 50,
			    disabled: true,
			    fieldLabel: "协议信息",
			    emptyText: "空空如也.."
			},{
			    xtype:'fieldset',
		        title: 'OTT_MOBILE',
		        width: '100%',
		        style: 'margin: 0 22px 0 38px;padding: 10px 0;',
			    layout:'column',
			    id: 'bandfieldSet',
			    labelWidth: 50,
			    defaults: {
			    	bodyStyle:'background:#F9F9F9;padding-top:4px',
			        layout: 'form',
			        border: false,
			        defaultType: 'textfield'
			    },
			    items: [{
			        items :[{
		                fieldLabel: '账号'
		            }]
			    },{
			    	defaultType: 'textfield',
			    	style: 'margin-left: 78px;',
			    	items :[{
		                fieldLabel: '密码'
		            }]
			    }]
			}]
		});
		
	},
	loadBuyModeStore : function(){//加载设备购买方式数据
		var deviceCode = this.deviceInfo.device_code;
		if(this.deviceCode != deviceCode){
			this.buyModeStore.load();
			this.deviceCode = deviceCode;
		}
	},
	doSelectUserType: function(c,r,i){
		var formtype = c.getValue();
		var bfs = Ext.getCmp("bandfieldSet");
		bfs.setTitle(formtype);
		if(formtype === "BAND" || formtype === "OTT_MOBILE"){
			bfs.show();
			bfs.enable();
		}else{
			bfs.hide();
			bfs.disable();
		}
	},
	doInit:function(){
		UserBaseForm.superclass.doInit.call(this);
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

/**
 * 新建用户
 * @class NewUserForm
 * @extends UserBaseForm
 */
NewUserForm = Ext.extend(UserBaseForm , {
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
	doValid : function(){
		var stbCombo = Ext.getCmp('dtv_stb_id');
		var cardCombo = Ext.getCmp('dtv_card_id');
		var modemCombo = Ext.getCmp('modemMac');
		if(stbCombo && stbCombo.hasFocus){
			this.checkDevice(stbCombo,'STB',cardCombo);
			if(stbCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "机顶盒有问题，请在验证后保存";
				return obj;
			}
		}
		if(cardCombo && cardCombo.hasFocus){
			this.checkCardDevice(cardCombo);
			if(cardCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "智能卡有问题，请在验证后保存";
				return obj;
			}
		}
		if(modemCombo && modemCombo.hasFocus){
			this.checkDevice(modemCombo,'MODEM');
			if(modemCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "MODEM有问题，请在验证后保存";
				return obj;
			}
		}
		
		return NewUserForm.superclass.doValid.call(this);
	},
	success : function(form,res){
		var userId = res.simpleObj;
		//清空参数
//		App.getData().busiTaskToDo = [];
		var orderProd = App.getData().busiTask['OrderProd'];
		orderProd['callback'] = {
			fn : App.getApp().selectRelativeUser,
			params : [userId]
		};
//		
//		var newUser = App.getData().busiTask['NewUser'];
//		
//		//跳转业务订购产品
//		App.getData().busiTaskToDo.push(newUser);
//		App.getData().busiTaskToDo.push(orderProd);
		
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		
	}
});

Ext.onReady(function(){
	var nup = new NewUserForm();
	var box = TemplateFactory.gTemplate(nup);
});