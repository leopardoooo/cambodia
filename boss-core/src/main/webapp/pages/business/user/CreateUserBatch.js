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
		this.newUserRecordFields = ['user_type','buy_mode','buy_mode_text',
		                            'open_amount','fee_id','fee','sub_total',
		                            'device_type'];
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
	            {menuDisabled: true, width: 100, header: "购买方式", sortable: false, dataIndex: 'buy_mode_text'},
	            {menuDisabled: true, width: 100, header: "数量", sortable: false, dataIndex: 'open_amount'},
	            {menuDisabled: true, width: 100, header: "单价$", sortable: false, dataIndex: 'fee'},
	            {menuDisabled: true, width: 100, header: "小计", sortable: false, dataIndex: 'sub_total'}
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
				height: 68,
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
						width:120,
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
							select: this.doLoadFeeSelect
						}
					},{
						xtype: 'numberfield',
			            fieldLabel: '费用名称',
			            width : 120,
			            id: 'nfFee'
					},{
						xtype:'paramcombo',
						hiddenName:'device_model',
						hidden: true,
						paramName:'DEVICE_MODEL',
						id: 'boxDeviceCategory',
						name:'device_model_text'
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
				bodyStyle:'background:#F9F9F9;padding-top:15px;border-top-width: 0;',
				defaults: {
					layout: 'form',
					baseCls: 'x-plain',
					labelWidth:80
				},
				items: [{
					columnWidth: .70,
					items: [{
						xtype: 'radiogroup',
			            fieldLabel: '派单方式',
			            anchor: '100%',
			            id: 'radioAssignWay',
			            columns: [70, 90, 120],
			            items: [{
			            	boxLabel: 'CFOCN',
			            	name: 'assignWay',
			            	inputValue: 'CFOCN'
			            },{
			            	boxLabel: 'SUPERNET',
			            	name: 'assignWay',
			            	inputValue: 'SUPERNET'
			            },{
			            	boxLabel: 'CFOCN+SUPERNET',
			            	name: 'assignWay',
			            	inputValue: 'CFOCN+SUPERNET'
			            }]
					}]
				},{
					columnWidth: .3,
					items:[{
						xtype: 'textfield',
						id: 'tfTotal',
			            fieldLabel: '费用总额$',
			            labelWidth: 60,
			            readOnly: true,
			            width: 60
					}]
				}]
			}]
		});
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
			device_type: this.getDeviceType(userType)
		})]);
		
		// 计算费用总额
		var total = 0;
		this.newUserStore.each(function(r,i,s){
			total += r.get("sub_total"); 
		});
		Ext.getCmp("tfTotal").setValue(total);
	},
	getDeviceType: function(userType){
		// 这是后台定死的
		var FEE = {"OTT": "1", "DTT": "2", "BAND": "3" }
		var store = Ext.getCmp("boxDeviceCategory").getStore();
		
		//根据用户类型过滤出最先匹配的设备类型
		for(var i = 0; i < store.getCount(); i++){
			var r = store.getAt(i);
			if(r.get("item_idx") == FEE[userType]){
				return r.get("item_value"); 
			}
		}
		return null;
	},
	doLoadFeeSelect : function(){
		var deviceBuyModeValue = Ext.getCmp("deviceBuyMode").getValue();
		var userType = Ext.getCmp("boxUserType").getValue();
		if(!deviceBuyModeValue || !userType){ return ; }
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel: this.getDeviceType(userType),
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
		var assignWay = Ext.getCmp("radioAssignWay").getValue();
		if(!assignWay){
			return {
				isValid: false,
				msg: "请选择派单方式"
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
				device_type: r.get("device_type")
			});
		});
		return {
			openUserList: Ext.encode(userData),
			workBillAsignType: Ext.getCmp("radioAssignWay").getValue().inputValue
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
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
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