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
			bodyStyle:'background:#F9F9F9;padding-top:15px',
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
					labelWidth: 130
				},
				items:[{
					items:[{
						fieldLabel: lmain("user.base.type"),
						xtype:'paramcombo',
						width:150,
						allowBlank:false,
						id: 'boxUserType',
						paramName:'USER_TYPE',
						listeners:{
							scope:this,
							select:this.doSelectUserType
						}
					}]
				},{
					items:[{
			            xtype: 'textarea',
			            fieldLabel: lmain("user._form.deviceCode"),
			            width : 150,
			            height: 80,
			            id: 'deviceCodeEl',
//			            disabled: true,
			            allowBlank: false,
			            listeners: {
			            	scope: this,
			            	change: this.doDeviceCodeAreaChange
			            }
					}]
				}/*,{
					items:[{
						id:'userNameId',
						fieldLabel: lmain("user.base.name"),
						xtype:'textfield',
						name:'user_name'
					}]
				}*/]
			},/*{
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
					labelWidth:100
				},
				items: [{
					items:[{
						xtype: 'checkbox',
					    labelWidth: 130,
					    fieldLabel: lmain("user._form.taskBackFill"),
					    id: "boxTaskEl",
					    checked: true,
					    listeners:{
			            	scope: this,
			            	check: this.doCheckedChangeTask
			            }
					}]
				},{
					items:[{
			            xtype: 'textfield',
			            fieldLabel: lmain("user._form.deviceCode"),
			            width : 150,
			            id: 'deviceCodeEl',
//			            disabled: true,
			            allowBlank: false,
			            listeners: {
			            	scope: this,
			            	change: this.doDeviceCodeChange
			            }
					}]
				}]
			},*/{
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
					labelWidth: 130
				},
				items:[{
					items:[{
						fieldLabel: lmain("user.base.deviceModel"),
						width : 150,
						xtype:'paramcombo',
						hiddenName:'device_model',
						paramName:'DEVICE_MODEL',
						id: 'deviceCategoryEl',
						name:'device_model_text',
						allowBlank:false,
						listeners: {
							scope: this,
							change: this.doBuyModeSelect
						}
					},{
						xtype: 'displayfield',
			            fieldLabel: lmain("user._form.feeName"),
			            width : 150,
			            id: 'dfFeeNameEl'
					}]
				},{
					items:[{
						fieldLabel: lmain("user.base.buyWay"),
						xtype:'combo',
						id : 'deviceBuyMode',
						forceSelection : true,
						store : this.buyModeStore,
						triggerAction : 'all',
						mode: 'local',
						width : 150,
						displayField : 'buy_mode_name',
						valueField : 'buy_mode',
						emptyText: lbc("common.plsSwitch"),
						editable : false,
						allowBlank:false,
						listeners: {
							scope: this,
							select: this.doBuyModeSelect
						}
					},{
						fieldLabel: lmain("user._form.feeAmount"),
						xtype:'numberfield',
						width:150,
						allowBlank:false,
						id: 'txtFeeEl'
					}]
				},{
					items:[{
						fieldLabel: lmain("user.base.stopType"),
						xtype:'paramcombo',
						allowBlank:false,
						width:150,
						id:'boxStopType',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					}]
				}]
			},{
				anchor:'96%',
				xtype: 'displayfield',
			    labelWidth: 130,
			    height: 50,
			    id: 'dfProtocolInfoEl',
			    fieldLabel: lmain("user._form.protocolInfo")
			},{
			    xtype:'fieldset',
			    width: '100%',
		        style: 'margin: 0 15px 0 30px;padding: 10px 0;',
			    layout:'column',
			    id: 'tmpFieldSet',
			    labelWidth: 80,
			    title: 'a',
			    defaults: {
			    	bodyStyle:'background:#F9F9F9;padding-top:4px',
			        layout: 'form',
			        border: false
			    },
			    items: [{
			        items :[{
			        	xtype: "textfield",
			        	id: "txtLoginName",
		                fieldLabel: lmain("user.base.loginName"),
		                allowBlank:false,
		                listeners: {
		                	change: this.validAccount
		                }
		            }]
			    },{
			    	style: 'margin-left: 80px;',
			    	items :[{
			    		xtype: 'textfield',
			    		id: "txtLoginPswd",
			    		allowBlank:false,
			    		inputType: 'password',
		                fieldLabel: lbc("common.pswd")
		            }]
			    }]
			}]
		});
		
		this.buyModeStore.on('load',function(){
			this.buyModeStore.each(function(record){
				if(record.get('buy_mode') == 'PRESENT'){
					Ext.getCmp('deviceBuyMode').setValue('PRESENT');
					return false;
				}
			}, this);
		}, this);
	},
	/*isInputTask: function(flag){
		if(Ext.getCmp('radioAssignWay'))
			Ext.getCmp('radioAssignWay').allowBlank = !flag;
	},*/
	// 施工回填
	/*doCheckedChangeTask: function(box, checked){
		if(checked){
			var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
			deviceCategoryEl.setEditable(true);
			var deviceCodeEl = Ext.getCmp("deviceCodeEl");
			deviceCodeEl.disable(true);
			deviceCodeEl.allowBlank = true;
		}else{
			var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
			deviceCategoryEl.setEditable(false);
			var deviceCodeEl = Ext.getCmp("deviceCodeEl");
			deviceCodeEl.enable(true);
			deviceCodeEl.allowBlank = false;
		}
		this.isInputTask(checked);
	},*/
	doSelectUserType: function(c,r,i){
		var type = c.getValue();
		var fs = Ext.getCmp("tmpFieldSet");
		fs.setTitle(type);
		
		var isAllowBlank = function(flag){
			Ext.getCmp('txtLoginName').allowBlank = !flag;
			Ext.getCmp('txtLoginPswd').allowBlank = !flag;
			
			Ext.getCmp('deviceCodeEl').allowBlank = flag;
			Ext.getCmp('deviceCategoryEl').allowBlank = flag;
			Ext.getCmp('deviceBuyMode').allowBlank = flag;
			Ext.getCmp('txtFeeEl').allowBlank = flag;
			Ext.getCmp('boxStopType').allowBlank = flag;
		}
		
		if(type === "BAND"){
			// 账号规则，受理号 + 两位序号，不够补零
			var users = App.getApp().data.users||[];
			var bandCount = 0;
			for(var index =0 ;index<users.length ; index++){
				if(users[index]["user_type"] === "BAND"){
					bandCount ++;
				}
			}
			bandCount ++;
			if(bandCount < 10){
				bandCount = "0" + bandCount;
			}
			
			fs.setVisible(true);
			isAllowBlank(false);
		}else if(type === "OTT_MOBILE"){
			fs.setVisible(true);
			isAllowBlank(true);
//			this.isInputTask(false);
		}else if(type === "OTT"){
			fs.setVisible(true);
			isAllowBlank(false);
		}else{
			fs.setVisible(false);
			isAllowBlank(false);
			
		}
		
		/*if(type == 'DTT'){
			//施工回填选择框默认不勾选，且禁用掉，必须输入设备号
			Ext.getCmp('boxTaskEl').setValue(false);
			Ext.getCmp('boxTaskEl').setDisabled(true);
			
		}else{
			Ext.getCmp('boxTaskEl').setValue(true);
			Ext.getCmp('boxTaskEl').setDisabled(false);
		}*/
		
		// 手机终端
		if(type === "OTT_MOBILE"){
			this.setDisplayItems(false);
		}else{
			this.setDisplayItems(true);
			// 过滤设备类型
			this.doFilterDeviceModel(type);
			this.doBuyModeSelect();
		}
		
		// 表单重置
		Ext.getCmp("deviceCodeEl").setValue("");
//		Ext.getCmp("deviceBuyMode").setRawValue("");
//		Ext.getCmp("txtFeeEl").setRawValue("");
//		Ext.getCmp("dfFeeNameEl").setRawValue("");
		Ext.getCmp('txtLoginName').setValue('');
		Ext.getCmp('txtLoginPswd').setValue(App.getCust()['password']);
		
		if(type != 'DTT'){
			Ext.Ajax.request({
				scope : this,
				url : root + '/core/x/User!generateUserName.action',
				params : { 
					custId: App.getCust()['cust_id'],
					userType: type 
				},
				success : function(response,opts){
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('txtLoginName').setValue(obj);
				}
			});
		}
		
	},
	setDisplayItems: function(bool){
		var elArr = [ /*"boxTaskEl", */"deviceCodeEl", "deviceCategoryEl", "dfFeeNameEl",
		              "deviceBuyMode", "txtFeeEl", "dfProtocolInfoEl" ];
		for(var i = 0 ; i< elArr.length; i++){
			Ext.get(elArr[i]).up('.x-form-item').setDisplayed(bool);
		}
	},
	// 后台为了简单，直接在数据字典加了个类型
	// 于是将OTT=1，DTT=2,BAND = 3
	deviceDataBak: null,
	doFilterDeviceModel: function(userType){
		var REF = {"DTT": "1", "OTT": "2", "BAND": "3" }
		var type = REF[userType] || "";
		var cmb = Ext.getCmp("deviceCategoryEl");
		/*cmb.setRawValue("");
		cmb.focus();
		cmb.expand();*/
		var store = cmb.getStore();
		// 先清除过滤
		if(!this.deviceDataBak){
			this.deviceDataBak = [];
			store.each(function(rs){
				this.deviceDataBak.push(rs.data);
			}, this);
		}
		
		var data = [];
		// 开始过滤
		for(var i = 0; i < this.deviceDataBak.length; i++){
			if(this.deviceDataBak[i]["item_idx"] == type){
				data.push(this.deviceDataBak[i]);
			}
		}
		store.removeAll();
		store.loadData(data);
		if(data.length > 0)
			cmb.setValue(data[0]['item_value']);
		Ext.getCmp("deviceBuyMode").focus();
	},/*设备文本域修改*/
	doDeviceCodeAreaChange:function(field){
		var v = field.getValue().trim();
		var currUserType = Ext.getCmp("boxUserType").getValue();
		if(!currUserType){
			Alert("请先选择用户类型!");
			currUserTypeBox.focus();
			currUserTypeBox.expand();
			field.setValue("");
			return;
		}
		if(!v)return ;
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceArea.action',
			params : { deviceCode: v,userType:currUserType},
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				if(!obj.success){
					Alert(obj.simpleObj);
					field.setRawValue("");
					return ;
				}
				var data = obj.simpleObj;
				var box = Ext.getCmp("deviceCategoryEl");
				box.getStore().loadData([{
					item_name: data["device_model_text"],
					item_value: data["device_model"]
				}]);
				box.setValue(data["device_model"]);
			}
		});
	},
	//设备号发生变化
	doDeviceCodeChange : function(field){
		var v = field.getValue().trim();
		var currUserTypeBox = Ext.getCmp("boxUserType");
		var currUserType = currUserTypeBox.getValue();
		if(!v)return ;
		if(!currUserType){
			Alert("请先选择用户类型!");
			currUserTypeBox.focus();
			currUserTypeBox.expand();
			field.setValue("");
			return;
		}
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDevice.action',
			params : { deviceCode: v },
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				if(!obj.success){
					Alert(obj.simpleObj);
					field.setRawValue("");
					return ;
				}
				var data = obj.simpleObj;
				var dtype = data["device_type"];
				if(dtype === "STB"){
					// 单双向
					var subType = data["deviceModel"]["interactive_type"]
					if(subType === "SINGLE"){
						if(currUserType !== "DTT"){
							Alert(lmsg('singleStbNotSupDTT'));
							field.setRawValue("");
							return ;
						}
					}else if(currUserType !== "OTT"){
						Alert(lmsg('doubleStbNotSupOTT'));
						field.setRawValue("");
						return ;
					}
				}else if(dtype === "MODEM" ){
					if(currUserType !== "BAND"){
						Alert(lbc('msgBox.modemNotSupUserType', null, [currUserType]));
						field.setRawValue("");
						return;
					}
				}else{
					Alert(lmsg('currDeviceNotSupUserType'));
					field.setRawValue("");
					return;
				}
				var box = Ext.getCmp("deviceCategoryEl");
				box.getStore().loadData([{
					item_name: data["device_model_text"],
					item_value: data["device_model"]
				}]);
				box.setValue(data["device_model"]);
			}
		});
	},
	// 查找设备费用
	currentFeeData: null,
	doBuyModeSelect : function(){
		var deviceModelValue = Ext.getCmp("deviceCategoryEl").getValue();
		var deviceBuyModeValue = Ext.getCmp("deviceBuyMode").getValue();
		
		if(!deviceModelValue || !deviceBuyModeValue){
			return ;
		}
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : deviceModelValue,
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
	},
	doInit:function(){
		UserBaseForm.superclass.doInit.call(this);
		Ext.getCmp("tmpFieldSet").setVisible(false);
		/* TODO 如果客户地址对应的业务信息不为空，过滤掉不能开通的业务
		 * 如果客户名下没有状态为正常的宽带业务则不能开ott用户
		 * 
		*/
		
	},
	validAccount: function(field){
		var loginName = field.getValue();
		Ext.Ajax.request({
			url: root + '/core/x/User!validAccount.action',
			params: {
				loginName: loginName
			},
			success: function(res,opt){
			}
		});
	},
	doValid : function(){
		var formValid =  UserBaseForm.superclass.doValid.call(this);
		if(formValid !== true){
			return formValid;
		}
		var boxUserType = Ext.getCmp("boxUserType");
//		var boxTask = Ext.getCmp("boxTaskEl");
//		var deviceCodeEl = Ext.getCmp("deviceCodeEl");
		var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
		var deviceBuyMode = Ext.getCmp("deviceBuyMode");
		var txtFeeEl = Ext.getCmp("txtFeeEl");
		
		// 设备回填
		/*if(boxTask.checked && !deviceCodeEl.getValue()){
			return {
				"isValid": false,
				"msg": lmsg('notChooseDeviceMustBeInput')
			}
		}*/
		
		//设备费用检查
		var fd = this.currentFeeData;
		if(fd){
			var maxfee = parseFloat(fd["min_fee_value"])/100.0;
			var minfee = parseFloat(fd["max_fee_value"])/100.0;
			var feeValue = parseFloat(txtFeeEl.getValue());
			if(feeValue < minfee || feeValue > maxfee){
				return {
					isValid: false,
					msg: lbc('msgBox.deviceFeeMustBeBetween', null, [minfee, maxfee])
				};
			}
		}
		
		return true;
	},
	getValues : function(){
		var values = {};
		// 基本参数
		values["user.user_type"] = Ext.getCmp("boxUserType").getValue();
		values["user.stop_type"] = Ext.getCmp("boxStopType").getValue();
		values["user.login_name"] = Ext.getCmp("txtLoginName").getValue();
		values["user.password"] = Ext.getCmp("txtLoginPswd").getValue();
		
		// 设备信息
		values["deviceId"] = Ext.getCmp("deviceCodeEl").getValue();
		values["deviceModel"] = Ext.getCmp("deviceCategoryEl").getValue();
		values["deviceBuyMode"] = Ext.getCmp("deviceBuyMode").getValue();
		
		// 设备费用
		var fee = this.currentFeeData;
		if(fee){
			values["deviceFee.fee_id"] = fee["fee_id"];
			values["deviceFee.fee_std_id"] = fee["fee_std_id"];
			values["deviceFee.fee"] =Ext.util.Format.formatToFen(Ext.getCmp("txtFeeEl").getValue());
		}
		return values;
	},
	getFee: function(){
		return Ext.getCmp("txtFeeEl").getValue();
	}
})

/**
 * 新建用户
 */
NewUserForm = Ext.extend(UserBaseForm , {
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
	success : function(form,res){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nup = new NewUserForm();
	var box = TemplateFactory.gTemplate(nup);
});
