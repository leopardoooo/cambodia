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
						id: 'boxUserType',
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
						id:'boxStopType',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					}]
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
				items: [{
					items:[{
						xtype: 'checkbox',
					    labelWidth: 120,
					    fieldLabel: "施工回填",
					    id: "boxTaskEl",
					    checked: true,
					    listeners:{
			            	scope: this,
			            	check: this.doCheckedChangeTask
			            }
					},]
				},{
					items:[{
			            xtype: 'textfield',
			            fieldLabel: '设备编码',
			            width : 150,
			            id: 'deviceCodeEl',
			            emptyText: "输入设备编码..",
			            disabled: true,
			            listeners: {
			            	scope: this,
			            	change: this.doDeviceCodeChange
			            }
					}]
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
						fieldLabel:'设备型号',
						width : 150,
						xtype:'paramcombo',
						hiddenName:'device_model',
						paramName:'DEVICE_MODEL',
						id: 'deviceCategoryEl',
						name:'device_model_text',
						listeners: {
							scope: this,
							change: this.doBuyModeSelect
						}
					},{
						xtype: 'displayfield',
			            fieldLabel: '费用名称',
			            width : 150,
			            id: 'dfFeeNameEl'
					}]
				},{
					items:[{
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
						editable : false,
						listeners: {
							scope: this,
							change: this.doBuyModeSelect
						}
					},{
						fieldLabel:'收费金额$',
						xtype:'numberfield',
						width:150,
						id: 'txtFeeEl'
					}]
				}]
			},{
				anchor:'96%',
				xtype: 'displayfield',
			    labelWidth: 120,
			    height: 50,
			    id: 'dfProtocolInfoEl',
			    fieldLabel: "协议信息"
			},{
			    xtype:'fieldset',
			    width: '100%',
		        style: 'margin: 0 22px 0 38px;padding: 10px 0;',
			    layout:'column',
			    id: 'tmpFieldSet',
			    labelWidth: 50,
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
		                fieldLabel: '账号'
		            }]
			    },{
			    	style: 'margin-left: 38px;',
			    	items :[{
			    		xtype: 'textfield',
			    		id: "txtLoginPswd",
		                fieldLabel: '密码'
		            }]
			    }]
			}]
		});
	},
	// 施工回填
	doCheckedChangeTask: function(box, checked){
		if(checked){
			var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
			deviceCategoryEl.setEditable(true);
			var deviceCodeEl = Ext.getCmp("deviceCodeEl");
			deviceCodeEl.disable(true);
		}else{
			var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
			deviceCategoryEl.setEditable(false);
			var deviceCodeEl = Ext.getCmp("deviceCodeEl");
			deviceCodeEl.enable(true);
		}
	},
	doSelectUserType: function(c,r,i){
		var type = c.getValue();
		var fs = Ext.getCmp("tmpFieldSet");
		fs.setTitle(type);
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
			
			// 密码，默认证件号后六位
			var newPswd = App.getApp().data.custFullInfo.cust.password;
			Ext.getCmp("txtLoginName").setValue(App.getApp().data.custFullInfo.cust.cust_no + bandCount);
			Ext.getCmp("txtLoginPswd").setValue(newPswd);
			
			fs.setVisible(true);
		}else if(type === "OTT_MOBILE"){
			fs.setVisible(true);
		}else{
			fs.setVisible(false);
		}
		
		// 手机终端
		if(type === "OTT_MOBILE"){
			this.setDisplayItems(false);
		}else{
			this.setDisplayItems(true);
			// 过滤设备类型
			this.doFilterDeviceModel(type);
		}
		
		// 表单重置
		Ext.getCmp("deviceCodeEl").setValue("");
		Ext.getCmp("deviceBuyMode").setRawValue("");
		Ext.getCmp("txtFeeEl").setRawValue("");
		Ext.getCmp("dfFeeNameEl").setRawValue("");
	},
	setDisplayItems: function(bool){
		var elArr = [ "boxTaskEl", "deviceCodeEl", "deviceCategoryEl", "dfFeeNameEl",
		              "deviceBuyMode", "txtFeeEl", "dfProtocolInfoEl" ];
		for(var i = 0 ; i< elArr.length; i++){
			Ext.get(elArr[i]).up('.x-form-item').setDisplayed(bool);
		}
	},
	// 后台为了简单，直接在数据字典加了个类型
	// 于是将OTT=1，DTT=2,BAND = 3
	deviceDataBak: null,
	doFilterDeviceModel: function(userType){
		var REF = {"OTT": "1", "DTT": "2", "BAND": "3" }
		var type = REF[userType] || "";
		var cmb = Ext.getCmp("deviceCategoryEl");
		cmb.setRawValue("");
		cmb.focus();
		cmb.expand();
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
		store.loadData(data);
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
							Alert("此设备为单向机顶盒，不支持当前的DTT用户类型");
							field.setRawValue("");
							return ;
						}
					}else if(currUserType !== "OTT"){
						Alert("此设备为双向机顶盒，不支持当前的OTT用户类型");
						field.setRawValue("");
						return ;
					}
				}else if(dtype === "MODEM" ){
					if(currUserType !== ""){
						Alert("设备为Modem猫，不支持所选"+ currUserType +"用户类型");
						field.setRawValue("");
						return;
					}
				}else{
					Alert("此设备不支持当前的用户类型!");
					field.setRawValue("");
					return;
				}
				var box = Ext.getCmp("deviceCategoryEl");
				box.getStore().loadData([{
					item_name: data["device_model_text"],
					item_value: data["device_model"]
				}]);
				box.setValue(data["device_model_text"]);
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
	},
	doValid : function(){
		var formValid =  UserBaseForm.superclass.doValid.call(this);
		if(formValid !== true){
			return formValid;
		}
		var boxUserType = Ext.getCmp("boxUserType");
		var boxTask = Ext.getCmp("boxTaskEl");
		var deviceCodeEl = Ext.getCmp("deviceCodeEl");
		var deviceCategoryEl = Ext.getCmp("deviceCategoryEl");
		var deviceBuyMode = Ext.getCmp("deviceBuyMode");
		var txtFeeEl = Ext.getCmp("txtFeeEl");
		var deviceCodeEl = Ext.getCmp("deviceCodeEl");
		
		// 设备回填
		if(boxTask.checked && !deviceCodeEl.getValue()){
			return {
				"isValid": false,
				"msg": "当设备没有选择施工回填时，设备编码是必须输入的"
			}
		}
		
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
		
		return true;
	},
	getValues : function(){
		var values = {};
		// 基本参数
		values["user.user_type"] = Ext.getCmp("boxUserType").getValue(),
		values["user.stop_type"] = Ext.getCmp("boxStopType").getValue(),
		values["user.login_name"] = Ext.getCmp("txtLoginName").getValue(),
		values["user.password"] = Ext.getCmp("txtLoginPswd").getValue(),
		
		// 设备信息
		values["deviceId"] = Ext.getCmp("deviceCodeEl").getValue();
		values["deviceModel"] = Ext.getCmp("deviceCategoryEl").getValue();
		values["deviceBuyMode"] = Ext.getCmp("deviceBuyMode").getValue();
		
		// 设备费用
		var fee = this.currentFeeData;
		values["deviceFee.fee_id"] = fee["fee_id"];
		values["deviceFee.fee_std_id"] = fee["fee_std_id"];
		values["deviceFee.fee"] = Ext.getCmp("txtFeeEl").getValue();
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
		var userId = res.simpleObj;
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nup = new NewUserForm();
	var box = TemplateFactory.gTemplate(nup);
});