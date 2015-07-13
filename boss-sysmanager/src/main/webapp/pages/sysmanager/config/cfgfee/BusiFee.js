/**
 * 费用项
 */
BusiFeeForm = Ext.extend(Ext.form.FormPanel, {
	storeData : null,
	constructor : function(data) {
		this.storeData = data;
		BusiFeeForm.superclass.constructor.call(this, {
			id : 'busiFeeFrom',
			layout : 'border',
			baseCls : 'x-plain',
			border : false,
			defaults : {
				bodyStyle : "background:#F9F9F9"
			},
			items : [{region : 'center',layout : 'column',border : false,bodyStyle : 'padding: 10px',
						defaults : {
							columnWidth : 1,layout : 'form',border : false,labelWidth : 100
						},
						items : [{
							items : [{
								fieldLabel : '费用名称',
								name : 'fee_name',
								xtype : 'textfield',
								id:'feeName',
								width : 150,
								allowBlank : false
							}]}, {
							items : [{
								fieldLabel : '费用类型',
								id : 'fee_typeId2',
								paramName : 'FEE_TYPE',
								xtype : 'paramcombo',
								allowBlank : false,
								hiddenName : 'fee_type',
								listeners:{
									scope:this,
									select:function(combo,record){
										var deviceFeeType = Ext.getCmp('device_fee_type_Itemid');
										var busiCode = Ext.getCmp('busi_code_Itemid');
										if(combo.getValue() === 'DEVICE'){//设备
											if(deviceFeeType.hidden){
												deviceFeeType.show();
											}
											if(!busiCode.hidden){
												busiCode.hide();
											}
										}else if(combo.getValue() === 'BUSI'){//服务
											if(busiCode.hidden){
												busiCode.show();
											}
											if(!deviceFeeType.hidden){
												deviceFeeType.hide();
											}
										}else{
											if(!deviceFeeType.hidden){
												deviceFeeType.hide();
											}
											if(!busiCode.hidden){
												busiCode.hide();
											}
										}
										//默认购买方式隐藏
										var buyModeComp = Ext.getCmp('buy_mode_ItemId');
										if(!buyModeComp.hidden){
											buyModeComp.hide();
										}
										deviceFeeType.items.each(function(item){
											item.reset();
										});
										busiCode.items.each(function(item){
											item.reset();
										});
									}
								}
							}]},{
							items : [{
								fieldLabel : '费用大类',
								paramName : 'FEE_BIG_TYPE',
								xtype : 'paramcombo',
								allowBlank : false,
								hiddenName : 'fee_big_type'
							}]},{
								id:'device_fee_type_Itemid',
								items:[{
									id:'deivce_fee_type_id',
									fieldLabel:'设备收费',
									hiddenName:'device_fee_type',
									paramName:'DEVICE_FEE_TYPE',
									xtype:'paramcombo',
									allowBlank:false,
									listeners:{
										scope:this,
										select:function(combo,record){
											var buyModeComp = Ext.getCmp('buy_mode_ItemId');
											//销售才有购买方式
											if(combo.getValue() === 'XS'){
												if(buyModeComp.hidden){
													buyModeComp.show();
												}
											}else{
												if(!buyModeComp.hidden){
													buyModeComp.hide();
												}
											}
											buyModeComp.items.itemAt(0).reset();
										}
									}
								}]},
								{id:'buy_mode_ItemId',items:[{
										fieldLabel:'购买方式',
										hiddenName:'buy_mode',
										xtype:'lovcombo',
										store:new Ext.data.JsonStore({
											url : root + '/config/BusiFee!queryBusiDeviceBuyMode.action',
											fields:['buy_mode','buy_mode_name']
										}),displayField:'buy_mode_name',valueField:'buy_mode',
										triggerAction:'all',mode:'local',width:320,
										allowBlank:false,editable:false,
										beforeBlur:function(){}
									}]
								},{
									id:'busi_code_Itemid',
									items:[{
										fieldLabel:'业务名称',
										xtype:'lovcombo',hideOnSelect:false,width:320,
										store:new Ext.data.JsonStore({
											url : root + '/config/BusiFee!queryBusiCodeByBusiType.action',
											fields:['busi_code','busi_name']
										}),displayField:'busi_name',valueField:'busi_code',
										hiddenName:'busi_code',triggerAction:'all',mode:'local',
										allowBlank:false,editable:false,
										beforeBlur:function(){}
									}]
								}, {
									items : [{
										fieldLabel : '押金',
										paramName : 'BOOLEAN',
										xtype : 'paramcombo',
										allowBlank : false,
										defaultValue : 'F',
										hiddenName : 'deposit'
									}]
								}, {
									items : [new PrintItemPanel({
											allowBlank : false
										})]
								}, {
									items : [{
										fieldLabel : '备注',
										xtype : 'textarea',
										width : 320,
										height : 70,
										name : 'remark'
									}]
								}, {
									id:'busiFee_status_ItemId',
									items : [{
										labelWidth : 120,
										fieldLabel : '状态',
										paramName : 'STATUS_T_BUSI_FEE',
										id : 'statusId',
										xtype : 'paramcombo',
										allowBlank : false,
										defaultValue : 'ACTIVE',
										hiddenName : 'status'
									}]
								}, {xtype : 'hidden',name : 'fee_id'}
							]
					}]

		})
	},
	initComponent : function() {
		BusiFeeForm.superclass.initComponent.call(this);
		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes, this.loadRecord, this)
	},loadRecord:function(){
		//隐藏组件
		Ext.getCmp('device_fee_type_Itemid').hide();
		Ext.getCmp('busi_code_Itemid').hide();
		Ext.getCmp('buy_mode_ItemId').hide();
		
		Ext.getCmp('buy_mode_ItemId').items.itemAt(0).getStore().load();
		Ext.getCmp('busi_code_Itemid').items.itemAt(0).getStore().load();
		Ext.getCmp('busi_code_Itemid').items.itemAt(0).getStore().on("load",this.loadData,this);
	},
	loadData : function() {
		//新增费用，删除状态框
		if (!this.storeData) {
			var item = Ext.getCmp('busiFee_status_ItemId');
			item.removeAll();
			item.doLayout();
		}
		if (this.storeData) {
			if(this.storeData.get('fee_type') === 'BUSI'){
				var busiCode = Ext.getCmp('busi_code_Itemid');
				busiCode.show();
			}else if(this.storeData.get('fee_type') === 'DEVICE'){
				var feeType = Ext.getCmp('device_fee_type_Itemid');
				feeType.show();
				//销售才会对应购买方式
				if(this.storeData.get('device_fee_type') === 'XS'){
					Ext.getCmp('buy_mode_ItemId').show();
				}
				Ext.getCmp('deivce_fee_type_id').setReadOnly(true);
			}
			Ext.getCmp('fee_typeId2').setReadOnly(true);
			this.getForm().loadRecord(this.storeData);
		}
	}
});

BusiFeeWin = Ext.extend(Ext.Window, {
	busiFeeForm : null,
	constructor : function(record) {
		this.busiFeeForm = new BusiFeeForm(record);
		BusiFeeWin.superclass.constructor.call(this, {
					layout : 'fit',
					height : 400,
					width : 500,
					closeAction : 'close',
					items : [this.busiFeeForm],
					buttons : [{
								text : '保存',
								scope : this,
								iconCls : 'icon-save',
								handler : this.doSave
							}, {
								text : '关闭',
								scope : this,
								handler : function() {
									this.close();
								}
							}]

				})

	},
	doSave : function() {
		var valid = true;
		//由于busi_code和device_fee_type2个组件会隐藏，修改basicForm验证方法
		this.busiFeeForm.getForm().items.each(function(f) {
			if (!f.validate()
					&& f.ownerCt.hidden === false) {
				valid = false;
			}
		});
        if(!valid)return;
		
		var old = this.busiFeeForm.getForm().getValues(), newValues = {};
		for (var key in old) {
			newValues["busiFeeDto." + key] = old[key];
		}
		//屏蔽是否允许前台修改。该字段后台数据库手工维护
//		newValues["busiFeeDto.can_update"] = 'F';

		var msg = Show();
		Ext.Ajax.request({
					params : newValues,
					url : root + '/config/BusiFee!saveBusiFee.action',
					scope:this,
					success : function(res, ops) {
						msg.hide();msg=null;
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Alert("操作成功!",function(){
								Ext.getCmp('busifeegrid').getStore().reload();
								this.close();
							},this);
						} else {
							Alert("操作失败!");
						}
					}
				});
	}
});

BusiFeeGrid = Ext.extend(Ext.grid.GridPanel, {
	feestore : null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		this.feestore = new Ext.data.JsonStore({
			url : root + '/config/BusiFee!queryBusiFee.action',
			fields : ['fee_id', 'fee_name', 'fee_type', 'printitem_id',
					'link_user', 'can_update', 'status', 'remark',
					'deposit', "fee_type_text", "printitem_id_text",
					"link_user_text", "status_text", "can_update_text",
					"deposit_text","device_fee_type","device_fee_type_text",
					"busi_code","busi_name","buy_mode","buy_mode_name","fee_big_type","fee_big_type_text",
					"optr_id","optr_name","county_id","county_name","area_id","area_name"],
			sortInfo: {
			    field: 'fee_type',
			    direction: 'ASC'
			},listeners:{
				scope:this,
				beforeload:function(){
					var combo = Ext.getCmp('BusiFee_Query_Status_Combox');
					if(combo){
						Ext.apply(this.feestore.baseParams,{status:combo.getValue()});
					}
					return true;
				}
			}
		});
		this.statusStore = new Ext.data.JsonStore({
			fields:['value','name'],
			data:[{name:'所有',value:''},{name:'正常',value:'ACTIVE'},{name:'失效',value:'INVALID'}]
		});
		this.feestore.load();
		var currentCountyId = App.data.optr['county_id'];
		BusiFeeGrid.superclass.constructor.call(this, {
			title : '费用配置',
			region : 'center',
			id : 'busifeegrid',
			ds : this.feestore,
			autoExpandColumn:'remark_id',
			sm:new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
						id : 'fee_name',
						header : '费用名称',
						width : 110,
						dataIndex : 'fee_name',
						renderer:function(v,meta,record){
							if(record.get('status') == 'INVALID'){
								return '<div ext:qtitle="" ext:qtip="' + v + '" style="color:red">' + v +'</div>';
							}else{
								return App.qtipValue(v);
							}
						}
					}, {
						header : '费用类型',
						width : 60,
						dataIndex : 'fee_type_text',
						renderer : App.qtipValue
					},{
						header : '业务名称',
						width : 115,
						dataIndex : 'busi_name',
						renderer : App.qtipValue
					},{
						header : '费用大类',
						width : 100,
						dataIndex : 'fee_big_type_text',
						renderer : App.qtipValue
					},{
						header : '县市',
						width : 85,
						dataIndex : 'county_name',
						renderer : App.qtipValue
					},{
						header : '设备费用类型',
						width : 80,
						dataIndex : 'device_fee_type_text',
						renderer : App.qtipValue
					},{
						header : '销售方式',
						width : 115,
						dataIndex : 'buy_mode_name',
						renderer : App.qtipValue
					}, {
						header : '打印项名称',
						width : 120,
						dataIndex : 'printitem_id_text',
						renderer : App.qtipValue
					}, {
						header : '押金',
						width : 35,
						dataIndex : 'deposit_text',
						renderer : App.qtipValue
					}, {
						header : '状态',
						width : 35,
						dataIndex : 'status_text',
						renderer:Ext.util.Format.statusShow
					}, {
						id:'remark_id',
						header : '备注',
//						width : 80,
						dataIndex : 'remark',
						renderer : App.qtipValue
					}, {
						header : '操作',
						width : 80,
						dataIndex : 'fee_id',
						renderer : function(v, md, record, i) {
							var str = "";
							if(currentCountyId == '4501' || currentCountyId == record.get('county_id')
										|| currentCountyId == record.get('area_id')){
								if(record.get('status') == 'ACTIVE'){
									str = "<a href='#' onclick='Ext.getCmp(\"busifeegrid\").updateFee();' style='color:blue'> 修改 </a>"
										+ "&nbsp;&nbsp;<a href='#' onclick=Ext.getCmp(\"busifeegrid\").updateFeeStatus('"+v+"','INVALID',"+i+"); style='color:blue'> 禁用 </a>";
								}else{
									str = "<a href='#' onclick=Ext.getCmp(\"busifeegrid\").updateFeeStatus('"+v+"','ACTIVE',"+i+"); style='color:blue'> 启用 </a>";
								}
							}
							return str;
						}
					}]),
			viewConfig:{
				getRowClass : function(record,rowIndex,rowParams,store){   
	                if(record.get('status') == 'INVALID'){   //用户状态不正常
	                    return 'background-color:#FF0000;';   
	                }else{   
	                    return '';   
	                }
				}
			},
			tbar : [' ', '状态:', new Ext.form.ComboBox({
						id:'BusiFee_Query_Status_Combox',store:this.statusStore,displayField:'name',valueField:'value',
						listeners:{
						scope:this,
						select:function(){
							var sf = Ext.getCmp('BusiFee_Query_SearchField');
							if(sf){
								sf.hasSearch = true;
							}
							var q = sf.getRawValue();
							if(!Ext.isEmpty(q)){
								sf.onTrigger2Click();
							}else{
								var o = {start: 0, limit:15 };
						        sf.store.baseParams = sf.store.baseParams || {};
						        sf.store.baseParams[sf.paramName] = q;
						        sf.store.reload({params:o});
						        sf.hasSearch = true;
						        sf.triggers[0].show();
							}
							
						}
					}
					}),
					'-','查询条件:', 
				new Ext.ux.form.SearchField({
							id:'BusiFee_Query_SearchField',
								store : this.feestore,
								width : 200,
								hasSearch : true,
								emptyText : '支持模糊查询'
							}), '->', {
						text : '添加',
						iconCls : 'icon-add',
						scope : this,
						handler : this.addRecord
					}]

		})
	},
	updateFeeStatus : function(v, status, rowIndex) {
		var str = (status == 'INVALID') ? '确定要禁用该配置吗?' : '确定要启用该配置吗';
		Confirm(str, this, function() {
			Ext.Ajax.request({
				scope : this,
				params : {
					feeId : v,
					status: status
				},
				url : root + '/config/BusiFee!updateFeeStatus.action',
				success : function(res, ops) {
					Alert('操作成功!', function() {
							this.getStore().reload();
					},this);
				}
			});
		});
	},
	updateFee : function() {
		var win = new BusiFeeWin(this.getSelectionModel().getSelected());
		win.setTitle('修改费用项');
		win.setIconClass('icon-edit-user');
		win.show();
	},
	addRecord : function() {
		var win = new BusiFeeWin();
		win.setTitle('增加费用项');
		win.setIconClass('icon-add-user');
		win.show();
	}

});
BusiFeeView = Ext.extend(Ext.Panel, {
			constructor : function() {
				BusiFeeView.superclass.constructor.call(this, {
							id : 'BusiFeeView',
							layout : 'border',
							title : '费用配置管理',
							closable : true,
							border : false,
							baseCls : "x-plain",
							items : [new BusiFeeGrid()]
						})
			}
		});
