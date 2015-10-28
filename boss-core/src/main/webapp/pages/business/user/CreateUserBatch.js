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
		                            'device_type', 'device_type_text', 'fee_name'];
		this.newUserStore = new Ext.data.JsonStore({
			fields: this.newUserRecordFields
		});
		this.newUserGrid = new Ext.grid.GridPanel({
			region: 'center',
			title: lmain("user._form.titleOpenUserGrid"),
			store: this.newUserStore,
	        columns: [
	            sm,
	            {id: 'aecid',menuDisabled: true, header: lmain("user.base.type"), width:85, sortable: false, dataIndex: 'user_type'},
	            {menuDisabled: true, header: lmain("cust._form.deviceType"), width:120, sortable: false, dataIndex: 'device_type_text', renderer:App.qtipValue},
	            {menuDisabled: true, width: 80, header: lmain("user.base.buyWay"), sortable: false, dataIndex: 'buy_mode_text', renderer:App.qtipValue},
	            {menuDisabled: true, width: 50, header: lbc("common.count"), sortable: false, dataIndex: 'open_amount'},
	            {menuDisabled: true, width: 100, header: lmain("user._form.feeName"), sortable: false, dataIndex: 'fee_name', renderer:App.qtipValue},
	            {menuDisabled: true, width: 50, header: lbc("common.price"), sortable: false, dataIndex: 'fee'},
	            {menuDisabled: true, width: 50, header: lbc("common.subTotal"), sortable: false, dataIndex: 'sub_total'}
	        ],
	        sm: sm,
	        autoExpandColumn: 'aecid', 
	        autoScroll:true,
	        width:'100%',
	        forceFit: false,
	        tbar: [{
	        	id: 'removeDataId',
	        	text: lbc("common.removeSelected"),
	        	iconCls: 'icon-del',
	        	scope: this,
	        	handler: function(){
	        		var selRecords = this.newUserGrid.getSelectionModel().getSelections();
	        		this.newUserStore.remove(selRecords);
	        	}
	        }]
		});
		
		this.busiFeeStore = new Ext.data.JsonStore({
			fields: ['fee_name', 'fee']
		});
		this.busiFeeGrid = new Ext.grid.GridPanel({
			region: 'south',
			title: lmain("user._form.titleBusiFeeGrid"),
			store: this.busiFeeStore,
			width:'100%',
			autoExpandColumn: 'aecid',
	        columns: [
	            {id:'aecid', menuDisabled: true, header: lmain('user._form.feeName'), sortable: false, dataIndex: 'fee_name'},
	            {menuDisabled: true, header: lmain('user._form.feeAmount'), sortable: false, dataIndex: 'fee', width:300, renderer:Ext.util.Format.formatFee}
	        ]
		});
		
		UserBaseBatchForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            labelWidth:100,
			layout: 'border',
			border: false,
			items: [{
				region: 'north',
				height: 115,
				bodyStyle:'background:#F9F9F9;padding-top:4px;border-bottom-width: 0px;',
				layout: 'column',
				defaults: {
					columnWidth: .4,
					bodyStyle:'background:#F9F9F9;padding-top:4px',
					layout: 'form',
					baseCls: 'x-plain',
					labelWidth:100
				},
				items:[{
					items:[{
						fieldLabel: lmain("user.base.type"),
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
						fieldLabel: lmain("cust._form.deviceType"),
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
			            fieldLabel: lmain("user._form.feeName"),
			            width : 122,
			            labelWidth: 100,
			            id: 'nfFee'
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
						allowBlank: false,
						width : 120,
						displayField : 'buy_mode_name',
						valueField : 'buy_mode',
						emptyText: lbc("common.plsSwitch"),
						editable : false,
						listeners: {
							scope: this,
							select: this.doLoadFeeSelect
						}
					},{
						fieldLabel: lmain("user._form.openAmount"),
						width:120,
						id: 'sfOpenAmount',
						xtype: 'spinnerfield',
						allowBlank: false,
			            minValue: 1,
			            maxValue: 5000,
			            value: 1
					},{
						layout: 'form',
						baseCls: 'x-plain',
						hidden: Ext.isEmpty(App.getCust()['spkg_sn']) ? true : false,
						border: false,
						items:[{
							xtype:'checkbox',
						    fieldLabel: lmain("user._form.manualOpen"),
						    style: 'padding: 0 0 -20px 0;',
						    id: 'handOpenId',
						    checked: true,
						    listeners:{
				            	scope: this,
				            	check: this.doCheckedChangeOpenType
				            }
						}]
					}]
				},{
					id:'addUserToGridBtnId',
					columnWidth: .2,
					layout: 'anchor',
					xtype: 'button',
					height: 48,
					style: 'margin: 4px 10px 0 0;',
					text: lmain("user._form.addToOpenUserGrid"),
					iconAlign: 'top',
					iconCls: 'icon-add-user',
					scope: this,
					handler: this.addToGrid
				}]
			},{
				region: 'center',
				layout:{
					type: 'vbox',
					pack: 'start',
					align: 'stretch'
				},
				border: false,
				defaults:{baseCls: 'x-plain', border:false},
				autoScroll: true,
				items:[
				       {id:'newUserPanelId', layout:'fit',flex:1, autoScroll:true, items:[this.newUserGrid]},
				       {id:'busiFeePanelId', layout:'fit',flex:1, items:[this.busiFeeGrid]}
				]
			},{
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
						fieldLabel: lmain("user.base.stopType"),
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
			            fieldLabel: lbc("common.total"),
			            labelWidth: 60,
			            readOnly: true,
			            width: 120
					}]
				}]
			}]
		});
	},
	doInit:function(){
		UserBaseBatchForm.superclass.doInit.call(this);
		Ext.getCmp('busiFeePanelId').hide();
		this.doLayout();
	},
	doCheckedChangeOpenType: function(box, checked){
		var flag = true;
		if(checked){
			flag = false;
			Ext.getCmp('busiFeePanelId').hide();
			Ext.getCmp('removeDataId').setDisabled(false);
			this.newUserGrid.getStore().removeAll();
			Ext.getCmp("tfTotal").setValue(0);
			Ext.getCmp('boxStopType').reset();
		}else{
			Ext.getCmp('busiFeePanelId').show();
			Ext.getCmp('removeDataId').setDisabled(true);
			
			if(!Ext.isEmpty(App.getCust()['spkg_sn'])){
				Ext.Ajax.request({
					url: root + '/core/x/User!querySpkgUserInfo.action',
					params: {
						spkgSn: App.getCust()['spkg_sn'],
						custId: App.getCust()['cust_id']
					},
					scope: this,
					success: function(res, opt){
						var data = Ext.decode(res.responseText);
						var spkgUsers = data['spkgUser'];
						var array = [], total = 0;
						for(var i=0; i<spkgUsers.length; i++){
							var obj = {
									user_type: spkgUsers[i]['user_type'],
									device_type_text: spkgUsers[i]['device_model_text'],
									buy_mode_text: spkgUsers[i]['buy_mode_name'],
									open_amount: spkgUsers[i]['open_num'],
									fee: 0,
									sub_total: Ext.util.Format.formatFee( spkgUsers[i]['fee'] ),
									fee_name: spkgUsers[i]['fee_name']
							};
							total += obj['sub_total'];
							array.push(obj);
						}
						this.newUserGrid.getStore().removeAll();
						this.newUserGrid.getStore().loadData(array);
						
						this.busiFeeGrid.getStore().removeAll();
						this.busiFeeGrid.getStore().loadData(data['busiFee']);
						
						for(var i=0; i<data['busiFee'].length; i++){
							total += Ext.util.Format.formatFee( data['busiFee'][i]['fee'] );
						}
						Ext.getCmp("tfTotal").setValue(total);
					}
				});
			}
		}
		Ext.getCmp('boxUserType').setDisabled(flag);
		Ext.getCmp('boxDeviceCategory').setDisabled(flag);
		Ext.getCmp('nfFee').setDisabled(flag);
		Ext.getCmp('deviceBuyMode').setDisabled(flag);
		Ext.getCmp('sfOpenAmount').setDisabled(flag);
		Ext.getCmp('addUserToGridBtnId').setDisabled(flag);
	},
	doFiltDeviceModel: function(){
		var userType = Ext.getCmp("boxUserType").getValue();
		var REF = {"DTT": "1", "OTT": "2", "BAND": "3" }
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
		
		var flag = true;
		this.newUserStore.each(function(record){
			if(record.get('user_type') == userType && record.get('device_type') == Ext.getCmp("boxDeviceCategory").getValue()
				&& record.get('buy_mode') == boxBuyMode.getValue() && record.get('fee') == fee ){
				record.set('open_amount', record.get('open_amount') + amount);
				record.set('sub_total', record.get('sub_total') + amount * (fee || 0));
				record.commit();
				flag = false;
				return false;
			}
		},this);
		if(flag){
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
				device_type_text: Ext.getCmp("boxDeviceCategory").getRawValue(),
				fee_name: fd ? fd['fee_name'] : ''
			})]);
		}
		
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
					Ext.DomQuery.selectNode('label[for=nfFee]').innerHTML = lmain("user._form.feeName");
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
				msg: lmsg('addUserToTempTable')
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
			isHand: Ext.getCmp('handOpenId').checked ? 'T' : 'F',
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
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nup = new NewUserBatchForm();
	var box = TemplateFactory.gTemplate(nup);
});
