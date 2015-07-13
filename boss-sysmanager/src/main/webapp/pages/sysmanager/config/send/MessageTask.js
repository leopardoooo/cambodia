/**
 * OSD,邮件批量管理
 * @class SelectOneForm
 * @extends Ext.form.FormPanel
 */

SelectOneForm = Ext.extend(Ext.form.FormPanel, {
			unitAllStore : null,
			prodCountyStore : null,
			constructor : function(v) {
				this.unitAllStore = new Ext.data.JsonStore({
							url : root + '/config/SendMsg!getUnitAll.action',
							fields : ['cust_id', 'cust_name'],
							baseParams:v
						});
				this.prodCountyStore = new Ext.data.JsonStore({
							url : root + '/system/Prod!getProdByCounty.action',
							fields : ['prod_id', 'prod_name'],
							baseParams:v
						});
				SelectOneForm.superclass.constructor.call(this, {
							layout : "column",
							layoutConfig : {
								animate : true
							},
							border : false,
							defaults : {
								columnWidth : 1,
								layout : 'form',
								baseCls : 'x-plain',
								labelWidth : 0.5
							},
							items : [{
								bodyStyle : 'padding-top: 10px',
								items : [{
									xtype : 'itemselector',
									name : 'unit',
									id : 'selectUnitId',
									imagePath : '/'
											+ Constant.ROOT_PATH_LOGIN
											+ '/resources/images/itemselectorImage',
									multiselects : [{
										legend : '待选单位',
										width : 155,
										height : 245,
										store : new Ext.data.ArrayStore({
													fields : ['cust_id',
															'cust_name']
												}),
										displayField : 'cust_name',
										valueField : 'cust_id'
										,tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
												listeners:{
													scope:this,
													keyup:function(txt,e){
														if(e.getKey() == Ext.EventObject.ENTER){
															var value = txt.getValue();
																Ext.getCmp('selectUnitId').multiselects[0].store.filterBy(function(record){
																	if(Ext.isEmpty(value))
																		return true;
																	else
																		return record.get('cust_name').indexOf(value)>=0;
																},this);
														}
													}
												}
											}]
									}, {
										legend : '已选单位',
										width : 140,
										height : 245,
										store : new Ext.data.ArrayStore({
													fields : ['cust_id',
															'cust_name']
												}),
										displayField : 'cust_name',
										valueField : 'cust_id'
									}]
								}]
							}, {
								items : [{
									xtype : 'itemselector',
									name : 'prod_id',
									id : 'selectProdId',
									imagePath : '/'
											+ Constant.ROOT_PATH_LOGIN
											+ '/resources/images/itemselectorImage',
									multiselects : [{
										legend : '待选产品',
										width : 155,
										height : 245,
										store : new Ext.data.ArrayStore({
													fields : ['prod_id',
															'prod_name']
												}),
										displayField : 'prod_name',
										valueField : 'prod_id'
										,tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
												listeners:{
													scope:this,
													keyup:function(txt,e){
														if(e.getKey() == Ext.EventObject.ENTER){
															var value = txt.getValue();
																Ext.getCmp('selectProdId').multiselects[0].store.filterBy(function(record){
																	if(Ext.isEmpty(value))
																		return true;
																	else
																		return record.get('prod_name').indexOf(value)>=0;
																},this);
														}
													}
												}
											}]
									}, {
										legend : '已选产品',
										width : 140,
										height : 245,
										store : new Ext.data.ArrayStore({
													fields : ['prod_id',
															'prod_name']
												}),
										displayField : 'prod_name',
										valueField : 'prod_id'
									}]
								}]
							}]

						})
			},
			initComponent : function() {
				SelectOneForm.superclass.initComponent.call(this);
				this.prodCountyStore.load();
				this.unitAllStore.load();
				this.unitAllStore.on("load", this.doLoadUnitData, this);
				this.prodCountyStore.on("load", this.doLoadProdData, this);
			},
			doLoadUnitData : function() {
				var arr = [];
				this.unitAllStore.each(function(record) {
							arr.push([record.data.cust_id,
									record.data.cust_name]);
						});
				Ext.getCmp('selectUnitId').multiselects[0].store.loadData(arr);
			},
			doLoadProdData : function() {
				var arrprod = [];
				this.prodCountyStore.each(function(record) {
							arrprod.push([record.data.prod_id,
									record.data.prod_name]);
						});
				Ext.getCmp('selectProdId').multiselects[0].store
						.loadData(arrprod);
			}

		})
SelectThreeForm = Ext.extend(Ext.form.FormPanel, {
	addrGrid : null,
	addressName : null,
	addressId : null,
	addrStore : null,
	setUpro : null,
	addrGrid : null,
	value:{},
	constructor : function(v) {
		if(!Ext.isEmpty(v)){
			this.value = v;
		}
		SelectThreeFormThis = this;
		this.setUprod = new Ext.grid.CheckboxSelectionModel();
		this.addrStore = new Ext.data.JsonStore({
					fields : ['addr_id', 'addr_name', 'tree_level']
				});
		this.addrGrid = new Ext.grid.EditorGridPanel({
			region : 'center',
			ds : this.addrStore,
			sm : this.setUprod,
			autoScroll : true,
			border : true,
			columns : [{
						header : '地址名称',
						dataIndex : 'addr_name',
						sortable : true,
						width : 150,
						renderer : App.qtipValue
					}, {
						header : '操作',
						width : 80,
						renderer : function(v, md, record, i) {
							var rs = Ext.encode(record.data);
							return String
									.format(
											"&nbsp;&nbsp;<a href='#' onclick='SelectThreeFormThis.deleteRecord({0},{1});' style='color:blue'> 删除 </a>",
											rs, i);
						}
					}],
			tbar : [' ', '地址查询:', {
						id : 'addrTreeComboId',
						xtype : 'treecombo',
						width : 150,
						treeWidth : 400,
						minChars : 2,
						height : 22,
						onlySelectLeaf : false,
						emptyText : '两个字搜索地址',
						blankText : '客户地址',
						treeParams : this.value,
						treeUrl : root + "/config/SendMsg!getAddrByName.action",
						hiddenName : 'addr_id',
						listeners : {
							scope : this,
							change : this.doAddressChange
						}
					}, '', '', {
						xtype : 'button',
						scale : 'small',
						width : 80,
						iconCls : 'icon-save',
						text : '保存',
						scope : this
					}]
		});

		SelectThreeForm.superclass.constructor.call(this, {
					region : 'center',
					layout : 'border',
					border : false,
					items : [this.addrGrid]
				})
	},
	doAddressChange : function() {
		this.doProdIn();
	},
	doProdIn : function() {
		if (Ext.isEmpty(Ext.getCmp('addrTreeComboId').getValue())) {
			Alert("请先选中要移入的地址!");
			return;
		}
		var data = this.addrStore;
		for (var i = 0; i < data.getCount(); i++) {
			if (data.getAt(i).get("addr_id") == Ext.getCmp('addrTreeComboId')
					.getValue()) {
				Alert(Ext.getCmp('addrTreeComboId').getRawValue() + ",该地址已经选中");
				return;
			}
		}
		var panl = this.addrStore.recordType;
		var u = new panl({
					addr_id : Ext.getCmp('addrTreeComboId').getValue(),
					addr_name : Ext.getCmp('addrTreeComboId').getRawValue(),
					tree_level : Ext.getCmp('addrTreeComboId').level
				});
		this.addrGrid.stopEditing();
		this.addrStore.insert(this.addrStore.getCount(), u);
		this.addrGrid.startEditing(this.addrStore.getCount(), 0);
		Ext.getCmp('addrTreeComboId').setValue("");
	},
	deleteRecord : function() {
		var rec = this.addrGrid.getSelectionModel().getSelected();
		this.addrStore.remove(rec);
	}
})

SelectTwoForm = Ext.extend(Ext.form.FormPanel, {
	constructor : function() {
		SelectTwoForm.superclass.constructor.call(this, {
			layout : 'column',
			id : 'selectTwoFormId',
			border : false,
			defaults : {
				columnWidth : 1,
				layout : 'form',
				border : false,
				labelAlign : "right",
				labelWidth : 65
			},
			items : [{
				columnWidth : 0.45,
				id : 'terminalTypeItemId',
				items : [{
					fieldLabel : '终端类型',
					xtype : 'lovcombo',
					id : 'terminalTypeId',
					hideOnSelect : false,
					width : 130,
					store : new Ext.data.JsonStore({
								url : root
										+ '/config/SendMsg!getTerminalType.action',
								fields : ['item_name', 'item_value']
							}),
					displayField : 'item_name',
					valueField : 'item_value',
					hiddenName : 'terminal_type',
					triggerAction : 'all',
					mode : 'local',
					editable : false,
					beforeBlur : function() {
					}
				}]
			}, {
				columnWidth : 0.55,
				items : [{
					fieldLabel : '优惠类型',
					xtype : 'lovcombo',
					id : 'custClassId',
					hideOnSelect : false,
					width : 170,
					store : new Ext.data.JsonStore({
								url : root
										+ '/config/SendMsg!getCustClass.action',
								fields : ['item_name', 'item_value']
							}),
					displayField : 'item_name',
					valueField : 'item_value',
					hiddenName : 'cust_class',
					triggerAction : 'all',
					mode : 'local',
					editable : false,
					beforeBlur : function() {
					}
				}]
			}, {
				columnWidth : 0.45,
				items : [{
					fieldLabel : '客户类别',
					xtype : 'lovcombo',
					id : 'custTypeId',
					hideOnSelect : false,
					width : 130,
					store : new Ext.data.JsonStore({
								url : root
										+ '/config/SendMsg!getCustType.action',
								fields : ['item_name', 'item_value']
							}),
					displayField : 'item_name',
					valueField : 'item_value',
					hiddenName : 'cust_type',
					triggerAction : 'all',
					mode : 'local',
					editable : false,
					beforeBlur : function() {
					}
				}]
			}, {
				columnWidth : 0.55,
				items : [{
					fieldLabel : '客户群体',
					xtype : 'lovcombo',
					id : 'custColonyId',
					hideOnSelect : false,
					width : 170,
					store : new Ext.data.JsonStore({
								url : root
										+ '/config/SendMsg!getCustColony.action',
								fields : ['item_name', 'item_value']
							}),
					displayField : 'item_name',
					valueField : 'item_value',
					hiddenName : 'cust_colony',
					triggerAction : 'all',
					mode : 'local',
					editable : false,
					beforeBlur : function() {
					}
				}]
			}, {
				columnWidth : 0.45,
				items : [{
					fieldLabel : '停机类型',
					xtype : 'lovcombo',
					id : 'stopTypeId',
					hideOnSelect : false,
					width : 130,
					store : new Ext.data.JsonStore({
								url : root
										+ '/config/SendMsg!getStopType.action',
								fields : ['item_name', 'item_value']
							}),
					displayField : 'item_name',
					valueField : 'item_value',
					hiddenName : 'stop_type',
					triggerAction : 'all',
					mode : 'local',
					editable : false,
					beforeBlur : function() {
					}
				}]
			}, {
				items : [{
					layout : 'column',
					baseCls : 'x-plain',
					defaults : {
						baseCls : 'x-plain',
						layout : 'form',
						labelWidth : 65
					},
					items : [{
								columnWidth : 0.4,
								items : [{
											fieldLabel : '预计到期日',
											xtype : 'datefield',
											id : 'startDateId',
											editable : false,
											vtype : 'daterange',
											endDateField : 'endDateId',
											name : 'prod_invalid_bdate',
											format : 'Y-m-d',
											allowBlank : false,
											value : new Date().format('Y-m-d')
										}]
							}, {
								labelWidth : 45,
								columnWidth : 0.6,
								items : [{
											fieldLabel : '至',
											xtype : 'datefield',
											editable : false,
											vtype : 'daterange',
											id : 'endDateId',
											allowBlank : false,
											startDateField : 'startDateId',
											name : 'prod_invalid_edate',
											format : 'Y-m-d',
											minValue : new Date()
													.format('Y-m-d')
										}]
							}]

				}]
			}

			]

		})
	}

})

MessageBaseForm = Ext.extend(Ext.form.FormPanel, {
	message : "尊敬的用户,您的余额将不足,为了不影响您的正常收看,请到附近的营业厅缴费,谢谢合作!",
	lenHis : 0,
	constructor : function() {

		MessageBaseForm.superclass.constructor.call(this, {
			layout : 'column',
			border : false,
			defaults : {
				columnWidth : 0.5,
				layout : 'form',
				border : false,
				baseCls : "x-plain",
				labelWidth : 75
			},
			items : [{
						bodyStyle : 'padding-top: 5px',
						columnWidth : 0.4,
						items : [{
									labelWidth : 120,
									fieldLabel : '消息类型',
									paramName : 'JOB_MESSAGE_TYPE',
									id : 'messageTypeId',
									xtype : 'paramcombo',
									width : 80,
									allowBlank : false,
									defaultValue : 'CJ',
									hiddenName : 'msg_type',
									listeners : {
										scope : this,
										'select' : this.changeItem
									}
								}]
					}, {
						bodyStyle : 'padding-top: 5px',
						columnWidth : 0.6,
						items : [{
									xtype : 'xdatetime',
									fieldLabel : '发送时间',
									width : 180,
									name : 'exec_time',
									timeWidth : 80,
									allowBlank : false,
									timeFormat : 'H:i:s',
									minValue : new Date().format('Y-m-d H:i:s'),
									timeConfig : {
										increment : 60,
										altFormats : 'H:i:s',
										allowBlank : false
									},
									dateFormat : 'Y-n-d',
									dateConfig : {
										altFormats : 'Y-m-d|Y-n-d',
										allowBlank : false
									}
								}]

					}, {
						columnWidth : 0.4,
						items : [{
									fieldLabel : '最大发送数',
									width : 80,
									id : 'limitUserCntId',
									name : 'limit_user_cnt',
									allowNegative : false,
									allowDecimals : false,
									xtype : 'numberfield'
								}]
					}, {
						columnWidth : 0.6,
						items : [{
							fieldLabel : '发送方式',
							xtype : 'lovcombo',
							id : 'sendTypeId',
							allowBlank : false,
							hideOnSelect : false,
							width : 180,
							store : new Ext.data.JsonStore({
										url : root
												+ '/config/SendMsg!getSendType.action',
										fields : ['item_name', 'item_value']
									}),
							displayField : 'item_name',
							valueField : 'item_value',
							hiddenName : 'send_type',
							triggerAction : 'all',
							mode : 'local',
							editable : false,
							beforeBlur : function() {
							},
							listeners : {
								scope : this,
								select:function(combo,record){
														var mailComp = Ext.getCmp('mailTitleId');
														var key = false;
														var str = combo.getValue().split(",");
														Ext.each(str,function(n,value){
															if(n ==='MAIL'){
																key = true;
															}
														})
														if (key) {
															if(mailComp.hidden){
																mailComp.show();
															}
														}else{
															if(!mailComp.hidden){
																mailComp.hide();
															}
														}
														mailComp.items.each(function(item){
																	item.reset();
																});
													}
							}
						}]
					},{
						columnWidth : 1,
						id:'mailTitleId',
						items:[{
							fieldLabel:'邮件标题',
							name:'mail_title',
							allowBlank:false,
							xtype:'textfield',
							width:200
						}]
						
					}, {
						columnWidth : 1,
						id : 'selectItemId',
						items : [{
									border : false,
									anchor:'100%',
									layout : {
										type:'hbox',
										padding : '5',
										pack:'start',
										align : 'top'
									},
									defaults : {
										margins : '0 5 0 0',
										height:20
									},
									items : [{
												xtype : 'spacer',
												width : 75
											}, {
												xtype : 'button',
												id : 'custNameId',
												text : '插入客户名',
												listeners : {
													scope : this,
													click : function() {
														this.clickName('%n')
													}
												}
											}, {
												xtype : 'button',
												id : 'prodNameId',
												text : '插入产品名',
												listeners : {
													scope : this,
													click : function() {
														this.clickName('%p')
													}
												}
											}, {
												xtype : 'button',
												id : 'stbId',
												text : '插入机顶盒号',
												listeners : {
													scope : this,
													click : function() {
														this.clickName('%s')
													}
												}
											}, {
												xtype : 'button',
												id : 'cardId',
												text : '插入智能卡号',
												listeners : {
													scope : this,
													click : function() {
														this.clickName('%c')
													}
												}
											}]
								}]

					}, {
						columnWidth : 1,
						items : [{
									fieldLabel : '信息',
									xtype : 'textarea',
									id : 'message',
									width : 400,
									allowBlank : false,
									height : 70,
									name : 'message',
									listeners : {
										scope : this,
										change : function(txt) {
											this.changeMessage(txt.getValue());
										}
									}
								}, {
									id : 'messageNumberId',
									layout : 'form',
									defaultType : 'displayfield',
									labelWidth : 75,
									border : false,
									items : [{
												id : 'invoiceBookStatisId',
												value : '还剩96个字符(48个中文)'
											}]
								}]
					}]

		})
	},
	initComponent : function() {
		MessageBaseForm.superclass.initComponent.call(this);
		Ext.getCmp('sendTypeId').getStore().load();
		Ext.getCmp('message').setValue(this.message);
		this.changeMessage(this.message);
	},
    initEvents: function () {
        this.on('afterrender', function () {
        	Ext.getCmp('mailTitleId').hide();
        	Ext.getCmp('selectItemId').hide();
        });
	    this.doLayout();
        MessageBaseForm.superclass.initEvents.call(this);
    },
	changeItem : function() {
		if (Ext.getCmp('messageTypeId').getValue() == 'CJ') {
			Ext.getCmp('message').setValue(this.message);
			Ext.getCmp('selectItemId').hide();
		} else {
			Ext.getCmp('message').setValue(null);
			Ext.getCmp('selectItemId').show();
		}
	},
	clickName : function(txt) {
		var src = Ext.getCmp('message').getValue();
		src = src + txt;
		Ext.getCmp('message').setValue(src);
	},
	changeMessage : function(txt) {
		var temp = 96 - Ext.util.Format.getByteLen(txt);
		var temp1 = temp / 2;
		if (temp % 2 == 1)
			temp1 = temp1 - 0.5;
		if (temp < 0) {
			Ext.getCmp('message').setValue(Ext.util.Format.subStringLength(txt,
					this.lenHis));
			Alert("输入已满!");
			return;
		} else {
			Ext.getCmp('invoiceBookStatisId').setValue("还剩" + temp + "个字符("
					+ temp1 + "个中文)");
			this.lenHis = Ext.util.Format.getByteLen(txt);
		}
	}
})
StopBaseForm = Ext.extend(Ext.form.FormPanel, {
	timeGrid : null,
	timeStore : null,
	cm : null,
	constructor : function() {
		StopBaseThis = this;
		this.timeStore = new Ext.data.JsonStore({
					fields : ['limit_user_cnt', 'exec_time', 'task_code']
				});
		this.cm = new Ext.grid.ColumnModel([{
					header : '最大发送用户数',
					dataIndex : 'limit_user_cnt',
					width : 100,
					renderer : App.qtipValue,
					sortable : true
				}, {
					header : '要求执行时间',
					dataIndex : 'exec_time',
					sortable : true,
					width : 150,
					renderer : App.qtipValue
				}, {
					header : '操作',
					width : 80,
					renderer : function(v, md, record, i) {
						var rs = Ext.encode(record.data);
						return String
								.format(
										"&nbsp;&nbsp;<a href='#' onclick='StopBaseThis.deleteStop({0},{1});' style='color:blue'> 删除 </a>",
										rs, i);
					}
				}]);
		this.timeGrid = new Ext.grid.EditorGridPanel({
					region : 'center',
					ds : this.timeStore,
					cm : this.cm,
					sm : new Ext.grid.RowSelectionModel({}),
					autoScroll : true,
					border : true
				});
		StopBaseForm.superclass.constructor.call(this, {
			layout : 'border',
			border : false,
			items : [{
						region : 'center',
						layout : 'fit',
						split : true,
						items : [this.timeGrid]
					}, {
						region : 'north',
						height : 50,
						items : [{
							layout : 'column',
							border : false,
							defaults : {
								labelAlign : "right",
								layout : 'form',
								border : false,
								baseCls : "x-plain",
								bodyStyle : 'padding-top: 10px',
								labelWidth : 75
							},
							items : [{
										columnWidth : 0.30,
										items : [{
													fieldLabel : '每天停机数',
													width : 80,
													id : 'limitUserCntId',
													name : 'limit_user_cnt',
													allowNegative : false,
													allowDecimals : false,
													xtype : 'numberfield'
												}]

									}, {
										columnWidth : 0.50,
										items : [{
													xtype : 'xdatetime',
													fieldLabel : '发送时间',
													width : 180,
													allowBlank : false,
													id : 'sendTimeId',
													timeWidth : 80,
													name : 'exec_time',
													timeFormat : 'H:i:s',
													timeConfig : {
														increment : 60,
														altFormats : 'H:i:s',
														allowBlank : false
													},
													dateFormat : 'Y-n-d',
													dateConfig : {
														altFormats : 'Y-m-d|Y-n-d',
														allowBlank : false
													}
												}]

									}, {
										columnWidth : 0.20,
										items : [{
													xtype : 'button',
													scale : 'small',
													width : 80,
													text : '插入设置',
													scope : this,
													handler : function() {
														this.onAddStop();
													}
												}]

									}]

						}]

					}]

		})
	},
	initComponent : function() {
		StopBaseForm.superclass.initComponent.call(this);

	},
	deleteStop : function() {
		var rec = this.timeGrid.getSelectionModel().getSelected();
		this.timeStore.remove(rec);
	},
	onAddStop : function() {
		if (!this.getForm().isValid()) {
			return;
		}
		var num = null;
		if (!Ext.isEmpty(Ext.getCmp('limitUserCntId').getValue())) {
			num = Ext.getCmp('limitUserCntId').getValue();
		}
		this.commonDoAdd(this.timeGrid, {
					limit_user_cnt : Ext.getCmp('limitUserCntId').getValue(),
					exec_time : Ext.getCmp('sendTimeId').getValue()
							.format('Y-m-d H:i:s'),
					task_code : 'TJ'
				});
	},
	commonDoAdd : function(scopeThis, fieldsObj, editColumn) {
		editColumn = editColumn || 0;
		var store = scopeThis.getStore();
		var count = store.getCount();
		var recordType = store.recordType;
		var recordData = new recordType(fieldsObj);
		scopeThis.stopEditing();
		store.insert(0, recordData);
		scopeThis.startEditing(0, editColumn);
		scopeThis.getSelectionModel().selectRow(0);
	}
})

MessageWin = Ext.extend(Ext.Window, {
	selectOneForm : null,
	selectTwoForm : null,
	messageBaseForm : null,
	selectThreeForm : null,
	value:{},
	constructor : function(record) {
		this.value = record;
		messageWinThis = this;
		this.selectThreeForm = new SelectThreeForm(record);
		this.selectTwoForm = new SelectTwoForm(record);
		this.selectOneForm = new SelectOneForm(record);
		this.messageBaseForm = new MessageBaseForm(record);
		MessageWin.superclass.constructor.call(this, {
					layout : 'border',
					height : 600,
					id : 'messageWinId',
					width : 880,
					closeAction : 'close',
					border : false,
					items : [{
								region : 'center',
								layout : 'border',
								split : true,
								items : [{
											region : 'center',
											layout : 'border',
											split : true,
											items : [{
														region : 'south',
														layout : 'fit',
														title : '设置信息',
														height : 230,
														split : true,
														items : [this.messageBaseForm]
													}, {
														region : 'center',
														layout : 'fit',
														split : true,
														items : [this.selectThreeForm]
													}, {
														region : 'north',
														layout : 'fit',
														bodyStyle : 'padding: 5px',
														split : true,
														height : 130,
														items : [this.selectTwoForm]
													}]
										}, {
											region : 'west',
											width : '39.5%',
											split : true,
											items : [this.selectOneForm]

										}]
							}],
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
	initComponent : function() {
		MessageWin.superclass.initComponent.call(this);
		Ext.getCmp('terminalTypeId').getStore().load();
		Ext.getCmp('custTypeId').getStore().load();
		Ext.getCmp('custClassId').getStore().load();
		Ext.getCmp('custColonyId').getStore().load();
		Ext.getCmp('stopTypeId').getStore().load();

		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes);
	},
	doSave : function() {
		if (!this.selectOneForm.getForm().isValid()) {
			return;
		}
		if (!this.selectTwoForm.getForm().isValid()) {
			return;
		}
		if (!this.selectThreeForm.getForm().isValid()) {
			return;
		}
		 var valid = true;
        //由于有组件会隐藏，修改验证方法
        this.messageBaseForm.getForm().items.each(function (f) {
            if (!f.validate() && f.ownerCt.hidden === false) {
                valid = false;
            }
        });
       	if(!valid)return;
		
		var selectTwo = this.selectTwoForm.getForm().getValues(), selectType = this.messageBaseForm
				.getForm().getValues(), selectOne = this.selectOneForm
				.getForm().getValues(), newValues = {};
		var store = this.selectThreeForm.addrGrid.getStore();
		var addrIds = '', data = [];
		store.each(function(record) {
					if (record.get('addr_id')) {
						addrIds = addrIds.concat(record.get('addr_id'), ',');
						data.push({
									addr_id : record.get('addr_id'),
									tree_level : record.get('tree_level')
								})
					}
				});
		var records = Ext.encode(data);
		newValues['addrRecord'] = records;
		addrIds = addrIds.substring(0, addrIds.length - 1);
		newValues["sendMsg.address"] = addrIds;
		for (var key in selectTwo) {
			newValues["sendMsg." + key] = selectTwo[key];
		}
		for (var key in selectType) {
			newValues["sendMsg." + key] = selectType[key];
		}
		for (var key in selectOne) {
			newValues["sendMsg." + key] = selectOne[key];
		}
		newValues["sendMsg.task_code"] = 'CJ';
		if(!Ext.isEmpty(messageWinThis.value)){
			newValues["countyId"]= messageWinThis.value['countyId'];
			newValues["areaId"]= messageWinThis.value['areaId'];
		}
		Confirm("确定保存吗?", this, function() {
			mb = Show();// 显示正在提交
			Ext.Ajax.request({
				url : root + '/config/SendMsg!saveMsg.action',
				params : newValues,
				success : function(res, ops) {
					mb.hide();// 隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {

						Alert('操作成功!', function() {
									Ext.getCmp('messageWinId').close();
									Ext.getCmp('MessageGridId').getStore()
											.load({
												params : {
													start : 0,
													limit : Constant.DEFAULT_PAGE_SIZE
												}
											});
								}, that);
					} else {
						Alert('操作失败!');
					}
				}
			});
		});

	}
});

MessageGrid = Ext.extend(Ext.grid.GridPanel, {
	osdstore : null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		MessageGridThiz = this;
		this.osdstore = new Ext.data.JsonStore({
					url : root + '/config/SendMsg!queryMsg.action',
					fields : ['job_id', 'msg_type', 'exec_time', 'done_time',
							'limit_user_cnt', 'user_count', 'send_user_count',
							'task_code', 'status', 'message', "status_text",
							"msg_type_text", "send_type"],
					sortInfo : {
						field : 'exec_time',
						direction : 'DESC'
					},
					root : 'records',
					totalProperty : 'totalProperty',
					autoDestroy : true
				});

		this.osdstore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});
		MessageGrid.superclass.constructor.call(this, {
			region : 'center',
			id : 'MessageGridId',
			ds : this.osdstore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			autoExpandColumn : 'message',
			cm : new Ext.grid.ColumnModel([{
						id : 'msg_type_text',
						header : '消息类型',
						width : 60,
						dataIndex : 'msg_type_text',
						renderer : App.qtipValue
					}, {
						header : '发送类型',
						width : 80,
						dataIndex : 'send_type',
						renderer : App.qtipValue
					}, {
						header : '要求执行时间',
						width : 130,
						dataIndex : 'exec_time',
						renderer : App.qtipValue
					}, {
						header : '完成时间',
						width : 130,
						dataIndex : 'done_time',
						renderer : App.qtipValue
					}, {
						header : '用户数',
						width : 80,
						dataIndex : 'user_count',
						renderer : App.qtipValue
					}, {
						header : '发送用户数',
						width : 80,
						dataIndex : 'send_user_count',
						renderer : App.qtipValue
					}, {
						header : '状态',
						width : 60,
						dataIndex : 'status_text',
						renderer:Ext.util.Format.statusShow
					}, {
						id : 'message',
						header : '信息',
						width : 80,
						dataIndex : 'message',
						renderer : App.qtipValue
					}, {
						header : '操作',
						width : 60,
						dataIndex : 'status',
						renderer : function(v, md, record, i) {
							var txt = "";
							if (v == 'ACTIVE') {
								txt = "作废";
							}
							return "<a href='#' onclick='MessageGridThiz.deleteMessage();' style='color:blue'>"
									+ txt + "</a>";
						}
					}]),
			bbar : new Ext.PagingToolbar({
						store : this.osdstore
					}),
			tbar : [' ', '查询条件:', new Ext.ux.form.SearchField({
								store : this.osdstore,
								width : 220,
								hasSearch : true,
								emptyText : '支持消息类型|发送类型|状态的模糊查询'
							}), '->', {
						text : '添加',
						iconCls : 'icon-add',
						scope : this,
						id : 'addMessageRecordId',
						handler : this.addRecord
					}]

		})
	},
	deleteMessage : function(v, rowIndex) {
		var record = this.getSelectionModel().getSelected();
		var jobId = record.get('job_id');
		Confirm("确定要作废该配置吗?", null, function() {
			Ext.Ajax.request({
				scope : this,
				params : {
					doneId : jobId
				},
				url : root + '/config/SendMsg!deleteMsg.action',
				success : function(res, ops) {
					Alert('作废成功!', function() {
								MessageGridThiz.getStore().load({
											params : {
												start : 0,
												limit : Constant.DEFAULT_PAGE_SIZE
											}
										});
							});
				}
			});
		})
	},
	addRecord : function() {		
		if (App.data.optr.county_id == '4501') {
			var win = Ext.getCmp('countyChooseWinId');
			if(!win)
				win = new CountyChooseWin("MessageWin");
			win.show();
		}else{
			var win = new MessageWin();
			win.setTitle('增加催缴配置');
			win.setIconClass('icon-add-user');
			win.show();
		}
	},initEvents : function(){
		this.on('rowclick',this.doClick,this);
		MessageGrid.superclass.initEvents.call(this);
	},
	doClick : function(grid,rowIndex){
		var record = grid.getStore().getAt(rowIndex);
		if(Ext.isEmpty(this.value)||this.value!=record.get('job_id')){
			this.value = record.get('job_id');
			Ext.Ajax.request({
				url : root + '/config/SendMsg!queryMsgByJob.action?doneId=' + this.value,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					Ext.getCmp('messageTaskDetalisInfo').doint(rs)
				}
			});
		}
	}

});

var MessageTaskInfoHTML = '<table width="100%" border="0" cellpadding="0" cellspacing="0">'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>任务类型:</td>'
		+ '<td class="input_bold" width=10%>&nbsp;{[values.task_code_text || ""]}</td>'
		+ '<td class="label" width=10%>消息内容:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.message || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >发送方式:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.send_type || ""]}</td>'
		+ '<td class="label" >发送地址:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.address_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >消息类型:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.msg_type_text || ""]}</td>'
		+ '<td class="label" >终端类型:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.terminal_type_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >任务状态:</td>'
		+ '<td class="input_bold">&nbsp;{[values.status_text || ""]}</td>'
		+ '<td class="label" >单位名称:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.unit_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >最大发送用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.limit_user_cnt || ""]}</td>'
		+ '<td class="label" >催停标识:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.hasten_stop_flag_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >处理用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.user_count || ""]}</td>'
		+ '<td class="label" >客户类别:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.cust_type_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" %>发送用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.send_user_count || ""]}</td>'
		+ '<td class="label">客户群体:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.cust_colony_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >完成日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.done_time) || ""]}</td>'
		+ '<td class="label" >产品名称:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.prod_id_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >创建日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.create_time) || ""]}</td>'
		+ '<td class="label" >预计到期区间(开始):</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.prod_invalid_bdate) || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >执行日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.exec_time) || ""]}</td>'
		+ '<td class="label" >预计到期区间(结束):</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.prod_invalid_edate) || ""]}</td>'
		+ '</tr>'
		+ '</tpl>' + '</table>';
MessageTaskInfo = Ext.extend(Ext.Panel, {
	border : false,
	tpl : null,
	html : null,
	constructor : function(v) {
		this.tpl = new Ext.XTemplate(MessageTaskInfoHTML);
		this.tpl.compile();
		MessageTaskInfo.superclass.constructor.call(this, {
					border : false,
					id : 'messageTaskDetalisInfo',
					title : "<font style='font-family:微软雅黑;font-size:12'>详细信息</font>",
					layout : 'anchor',
					anchor : '100%',
					collapsible : true,
					autoScroll : true,
					bodyStyle : "background:#F9F9F9",
					defaults : {
						bodyStyle : "background:#F9F9F9"
					},
					items : [{
								xtype : "panel",
								region : 'center',
								border : false
							}]
				});
	},
	doint : function(v) {
		var ppp = Ext.getCmp('messageTaskDetalisInfo').items.itemAt(0);
		var panel = {
			xtype : "panel",
			border : false,
			bodyStyle : "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
			html : this.tpl.applyTemplate(v)
		};
		ppp.removeAll();
		ppp.add(panel);
		ppp.doLayout();
		this.tpl.overwrite(this.items.itemAt(0).body, v);
	}
});

MessageView = Ext.extend(Ext.Panel, {
			constructor : function() {
				MessageView.superclass.constructor.call(this, {
							id : 'MessageView',
							layout : 'border',
							title : '批量操作',
							closable : true,
							border : false,
							baseCls : "x-plain",
							items : [{region:'center',layout:'fit',split : true,items:[new MessageGrid()]}, {region : 'south',height:220,split : true,layout : 'fit',items : [new MessageTaskInfo()]}]
						})
			}
		});