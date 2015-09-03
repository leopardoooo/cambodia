/**
 * 派单用户开户表单
 */
UserBaseBatchForm = Ext.extend( BaseForm , {
	buyModeStore: null,
	constructor: function(p){
		this.buyModeStore = new Ext.data.JsonStore({
			url :root + '/commons/x/QueryDevice!queryDeviceBuyMode.action',
			fields : ['buy_mode','buy_mode_name'],
			autoLoad: true
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.newUserRecordFields = ['user_type', 'device_model','buy_mode','buy_mode_text',
		                            'open_amount','fee_id','fee','sub_total',
		                            'device_type', 'device_type_text'];
		this.newUserStore = new Ext.data.JsonStore({
			fields: this.newUserRecordFields
		});
		this.newUserGrid = new Ext.grid.GridPanel({
			region: 'center',
			title: '用户暂存表',
			store: this.newUserStore,
	        columns: [
	            sm,
	            {id: 'aecid',menuDisabled: true, header: "用户类型", sortable: false, dataIndex: 'user_type'},
	            {menuDisabled: true, header: "设备类型", sortable: false, dataIndex: 'device_type_text'},
	            {menuDisabled: true, width: 90, header: "购买方式", sortable: false, dataIndex: 'buy_mode_text'},
	            {menuDisabled: true, width: 90, header: "数量", sortable: false, dataIndex: 'open_amount'},
	            {menuDisabled: true, width: 90, header: "单价$", sortable: false, dataIndex: 'fee'},
	            {menuDisabled: true, width: 90, header: "小计", sortable: false, dataIndex: 'sub_total'}
	        ],
	        sm: sm,
	        autoExpandColumn: 'aecid',
	        tbar: [{
	        	text: '移除选中',
	        	iconCls: 'icon-del',
	        	scope: this,
	        	handler: function(){
	        		var selRecords = this.newUserGrid.getSelectionModel().getSelections();
	        		this.newUserStore.remove(selRecords);
	        	}
	        }]
		});
		
		
		UserBaseBatchForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            labelWidth:90,
			layout: 'border',
			border: false,
			items: [{
				region: 'north',
				height: 88,
				bodyStyle:'background:#F9F9F9;padding-top:4px;border-bottom-width: 0px;',
				layout: 'column',
				defaults: {
					columnWidth: .4,
					bodyStyle:'background:#F9F9F9;padding-top:4px',
					layout: 'form',
					baseCls: 'x-plain',
					labelWidth:80
				},
				items:[{
					items:[{
						fieldLabel:'用户类型',
						xtype:'combo',
						width:122,
						allowBlank:false,
						id: 'boxUserType',
						displayField : 'user_type',
						valueField : 'user_type',
						store : new Ext.data.JsonStore({
							fields: ['user_type'],
							data: [
							    {'user_type': 'OTT'},
							    {'user_type': 'DTT'},
							    {'user_type': 'BAND'}
							]
						}),
						listeners:{
							scope: this,
							select: this.doFiltDeviceModel
						}
					},{
						xtype:'paramcombo',
						hiddenName:'device_model',
						fieldLabel: '设备类型',
						paramName:'DEVICE_MODEL',
						id: 'boxDeviceCategory',
						allowBlank: false,
						name:'device_model_text',
						listeners:{
							scope: this,
							select: this.doLoadFeeSelect
						}
					},{
						xtype: 'numberfield',
			            fieldLabel: '费用名称',
			            width : 122,
			            labelWidth: 100,
			            id: 'nfFee'
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
						allowBlank: false,
						width : 120,
						displayField : 'buy_mode_name',
						valueField : 'buy_mode',
						emptyText: '请选择',
						editable : false,
						listeners: {
							scope: this,
							select: this.doLoadFeeSelect
						}
					},{
						fieldLabel:'开户数量',
						width:120,
						id: 'sfOpenAmount',
						xtype: 'spinnerfield',
						allowBlank: false,
			            minValue: 1,
			            maxValue: 5000,
			            value: 2
					}]
				},{
					columnWidth: .2,
					layout: 'anchor',
					xtype: 'button',
					height: 48,
					style: 'margin: 4px 10px 0 0;',
					text: '添加至暂存表',
					iconAlign: 'top',
					iconCls: 'icon-add-user',
					scope: this,
					handler: this.addToGrid
				}]
			},
			this.newUserGrid, {
				region: 'south',
				height: 60,
				layout: 'column',
				anchor:'100%',
				bodyStyle:'background:#F9F9F9;padding-top:15px;border-top-width: 0;',
				defaults: {
					layout: 'form',
					baseCls: 'x-plain',
					labelWidth:80
				},
				items: [{
					columnWidth: .5,
					items:[{
						fieldLabel:'催费类型',
						xtype:'paramcombo',
						allowBlank:false,
						width:150,
						id:'boxStopType',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					}]
				},{
					columnWidth: .5,
					items:[{
						xtype: 'textfield',
						id: 'tfTotal',
			            fieldLabel: '费用总额$',
			            labelWidth: 60,
			            readOnly: true,
			            width: 120
					}]
				}]
			}]
		});
	},
	doFiltDeviceModel: function(){
		var userType = Ext.getCmp("boxUserType").getValue();
		var REF = {"OTT": "1", "DTT": "2", "BAND": "3" }
		var type = REF[userType] || "";
		var cmb = Ext.getCmp("boxDeviceCategory");
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
	addToGrid: function(){
		var formValid = UserBaseBatchForm.superclass.doValid.call(this);
		if(formValid["isValid"] !== true){
			return formValid;
		}
		
		//设备费用检查
		var fd = this.currentFeeData;
		// 添加至表格
		var boxBuyMode = Ext.getCmp("deviceBuyMode");
		var amount = Ext.getCmp("sfOpenAmount").getValue();
		var fee = Ext.getCmp("nfFee").getValue();
		var userType = Ext.getCmp("boxUserType").getValue();
		
		var TMPRecord = Ext.data.Record.create(this.newUserRecordFields);
		this.newUserStore.add([new TMPRecord({
			user_type: userType,
			buy_mode: boxBuyMode.getValue(),
			buy_mode_text: App.form.getCmbDisplayValue(boxBuyMode),
			open_amount: amount,
			fee_id: (fd ? fd["fee_id"] : 0),
			fee: fee,
			sub_total: amount * (fee || 0),
			device_type: Ext.getCmp("boxDeviceCategory").getValue(),
			device_type_text: Ext.getCmp("boxDeviceCategory").getRawValue()
		})]);
		
		// 计算费用总额
		var total = 0;
		this.newUserStore.each(function(r,i,s){
			total += r.get("sub_total"); 
		});
		Ext.getCmp("tfTotal").setValue(total);
	},
	doLoadFeeSelect : function(){
		var deviceBuyModeValue = Ext.getCmp("deviceBuyMode").getValue();
		var userType = Ext.getCmp("boxUserType").getValue();
		var deviceMode = Ext.getCmp("boxDeviceCategory").getValue();
		
		if(!deviceBuyModeValue || !userType || !deviceMode){ return ; }
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel: deviceMode,
				buyMode : deviceBuyModeValue
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				var nfFee = Ext.getCmp("nfFee");
				if(data && data.length > 0 ){
					data = data[0];
					Ext.DomQuery.selectNode('label[for=nfFee]').innerHTML = data["fee_name"];
					nfFee.clearInvalid();
					nfFee.setMaxValue(data["max_fee_value"]/100.0);
					nfFee.setMinValue(data["min_fee_value"]/100.0);
					nfFee.setValue(data["fee_value"]/100.0);
					this.currentFeeData = data;
				}else{
					Ext.DomQuery.selectNode('label[for=nfFee]').innerHTML = "费用名称";
					nfFee.setMaxValue(0);
					nfFee.setMinValue(0);
					nfFee.setValue(0.00);
					this.currentFeeData = null;
				}
			}
		});
	},
	doValid : function(){
		if(this.newUserStore.getCount() == 0){
			return {
				isValid: false,
				msg: "请将需要保存的用户添加至暂存表"
			};
		}
		return true;
	},
	getValues : function(){
		var userData = [];
		this.newUserStore.each(function(r,s,i){
			userData.push({
				user_type: r.get("user_type"),
				device_buy_mode: r.get("buy_mode"),
				fee_id: r.get("fee_id"),
				fee: r.get("fee") * 100.0,
				user_count: r.get("open_amount"),
				device_model: r.get("device_type")
			});
		});
		return {
			openUserList: Ext.encode(userData),
			stopType: Ext.getCmp("boxStopType").getValue()
		};
	},
	getFee: function(){
		return Ext.getCmp("txtFeeEl").getValue();
	}
})

/**
 * 新建用户
 */
NewUserBatchForm = Ext.extend(UserBaseBatchForm , {
	url : Constant.ROOT_PATH+"/core/x/User!createUserBatch.action",
	success : function(form,res){
		var userId = res.simpleObj;
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nup = new NewUserBatchForm();
	var box = TemplateFactory.gTemplate(nup);
});