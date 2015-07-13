/**
 * 批量手动停机管理
 * @class StopOneForm
 * @extends Ext.form.FormPanel
 */
StopOneForm = Ext.extend(Ext.form.FormPanel, {
	unitAllStore : null,
	constructor : function(v) {
		this.unitAllStore = new Ext.data.JsonStore({
					url : root + '/config/SendMsg!getUnitAll.action',
					fields : ['cust_id', 'cust_name'],
					baseParams:v
				});
		StopOneForm.superclass.constructor.call(this, {
					layout : "column",
					layoutConfig : {
						animate : true
					},
					border : false,
					defaults : {
						columnWidth : 1,
						layout : 'form',
						baseCls : 'x-plain',
						labelWidth : 1
					},
					items : [{
						items : [{
									xtype : 'hidden',
									name : 'unit'
								}, {
									xtype : 'itemselector',
									name : 'unit',
									id : 'selectUnitId',
									imagePath : '/'
											+ Constant.ROOT_PATH_LOGIN
											+ '/resources/images/itemselectorImage',
									multiselects : [{
										legend : '待选单位',
										width : 140,
										height : 300,
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
										height : 300,
										store : new Ext.data.ArrayStore({
													fields : ['cust_id',
															'cust_name']
												}),
										displayField : 'cust_name',
										valueField : 'cust_id'
									}]
								}]
					}]

				})
	},
	initComponent : function() {
		StopOneForm.superclass.initComponent.call(this);
		this.unitAllStore.load();
		this.unitAllStore.on("load", this.doLoadUnitData, this);
	},
	doLoadUnitData : function() {
		var arr = [];
		this.unitAllStore.each(function(record) {
					arr.push([record.data.cust_id, record.data.cust_name]);
				});
		Ext.getCmp('selectUnitId').multiselects[0].store.loadData(arr);
	}

})
StopThreeForm = Ext.extend(Ext.form.FormPanel, {
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
						treeParams : this.value,
						emptyText : '两个字搜索地址',
						blankText : '客户地址',
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
						text : '保存地址',
						scope : this,
						handler : function() {
							//							this.doProdIn();
						}
					}]
		});

		StopThreeForm.superclass.constructor.call(this, {
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

StopTwoForm = Ext.extend(Ext.form.FormPanel, {
	constructor : function() {
		StopTwoForm.superclass.constructor.call(this, {
			layout : 'column',
			id : 'selectTwoFormId',
			border : false,
			defaults : {
				columnWidth : 1,
				layout : 'form',
				border : false,
				labelAlign : "right",
				labelWidth : 75
			},
			items : [{
				columnWidth : 1,
				items : [{
					fieldLabel : '客户类别',
					xtype : 'lovcombo',
					id : 'custTypeId',
					hideOnSelect : false,
					width : 300,
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
				columnWidth : 1,
				items : [{
					fieldLabel : '优惠类型',
					xtype : 'lovcombo',
					id : 'custClassId',
					hideOnSelect : false,
					width : 300,
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
				columnWidth : 1,
				items : [{
					fieldLabel : '客户群体',
					xtype : 'lovcombo',
					id : 'custColonyId',
					hideOnSelect : false,
					width : 300,
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
				columnWidth : 1,
				items : [{
					fieldLabel : '停机类型',
					xtype : 'lovcombo',
					id : 'stopTypeId',
					hideOnSelect : false,
					width : 300,
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
				columnWidth : 0.45,
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
				columnWidth : 0.55,
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
							minValue : new Date().format('Y-m-d')
						}]
			}

			]

		})
	}

})

StopCfgForm = Ext.extend(Ext.form.FormPanel, {
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
					sm : new Ext.grid.CheckboxSelectionModel(),
					autoScroll : true,
					border : true
				});
		StopCfgForm.superclass.constructor.call(this, {
			layout : 'border',
			border : false,
			items : [{
						region : 'center',
						layout : 'fit',
						split : true,
						items : [this.timeGrid]
					}, {
						region : 'north',
						height : 80,
						items : [{
							layout : 'column',
							border : false,
							defaults : {
								labelAlign : "right",
								columnWidth : 1,
								layout : 'form',
								border : false,
								baseCls : "x-plain",
								labelWidth : 75
							},
							items : [{
										bodyStyle : 'padding-top: 10px',
										items : [{
													fieldLabel : '每天停机数',
													width : 100,
													id : 'limitUserCntId',
													name : 'limit_user_cnt',
													allowNegative : false,
													allowDecimals : false,
													xtype : 'numberfield'
												}]

									}, {
										columnWidth : 0.65,
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

										columnWidth : 0.35,
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
		StopCfgForm.superclass.initComponent.call(this);

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

StopTaskWin = Ext.extend(Ext.Window, {
	stopOneForm : null,
	stopTwoForm : null,
	stopCfgForm : null,
	stopThreeForm : null,
	value:{},
	constructor : function(record) {
		this.value = record;
		messageWinThis = this;
		this.stopThreeForm = new StopThreeForm(record);
		this.stopTwoForm = new StopTwoForm(record);
		this.stopOneForm = new StopOneForm(record);
		this.stopCfgForm = new StopCfgForm(record);
		StopTaskWin.superclass.constructor.call(this, {
					layout : 'border',
					height : 600,
					id : 'stopTaskWinId',
					width : 800,
					closeAction : 'close',
					border : false,
					items : [{
								region : 'center',
								layout : 'border',
								split : true,
								title : '设置条件',
								items : [{
											region : 'center',
											layout : 'border',
											split : true,
											items : [{
														region : 'center',
														layout : 'fit',
														title : '设置信息',
														items : [this.stopCfgForm]
													}, {
														region : 'north',
														layout : 'fit',
														bodyStyle : 'padding: 5px',
														split : true,
														height : 180,
														items : [this.stopTwoForm]
													}]
										}, {
											region : 'west',
											width : '43%',
											bodyStyle : 'padding: 5px',
											layout : 'border',
											items : [{
														region : 'center',
														layout : 'fit',
														split : true,
														items : [this.stopThreeForm]
													}, {
														region : 'north',
														layout : 'fit',
														bodyStyle : 'padding: 5px',
														split : true,
														height : 330,
														items : [this.stopOneForm]

													}]
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
		StopTaskWin.superclass.initComponent.call(this);
		Ext.getCmp('custTypeId').getStore().load();
		Ext.getCmp('custClassId').getStore().load();
		Ext.getCmp('custColonyId').getStore().load();
		Ext.getCmp('stopTypeId').getStore().load();

		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes);
	},
	doSave : function() {
		if (!this.stopOneForm.getForm().isValid()) {
			return;
		}
		if (!this.stopTwoForm.getForm().isValid()) {
			return;
		}
		if (!this.stopThreeForm.getForm().isValid()) {
			return;
		}
		if (!this.stopCfgForm.getForm().isValid()) {
			return;
		}
		var selectTwo = this.stopTwoForm.getForm().getValues(), selectStop = this.stopCfgForm
				.getForm().getValues(), selectOne = this.stopOneForm.getForm()
				.getValues(), newValues = {};
		var store = this.stopThreeForm.addrGrid.getStore();
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
		for (var key in selectOne) {
			newValues["sendMsg." + key] = selectOne[key];
		}
		if (!this.CheckOptrRole()) {
			return;
		}
		var store = this.stopCfgForm.timeGrid.getStore();
		var data = [], all = {};
		if (store.getCount() == 0) {
			Alert("未插入设置!");
			return;
		}
		newValues["sendMsg.task_code"] = 'TJ';
		for (var k = 0; k < store.getCount(); k++) {
			if (Ext.isEmpty(store.getAt(k).get('limit_user_cnt'))) {
				data.push({
							exec_time : store.getAt(k).get('exec_time')
						})
			} else {
				data.push(store.getAt(k).data);
			}
		}
		var records = Ext.encode(data);
		newValues['records'] = records;
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
									Ext.getCmp('stopTaskWinId').close();
									Ext.getCmp('StopTaskGridId').getStore()
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

	},
	CheckOptrRole : function() {
		this.stopCfgForm.timeGrid.stopEditing();// 停止编辑
		var store = this.stopCfgForm.timeGrid.getStore();
		var count = store.getCount();// 总个数

		var config = this.stopCfgForm.timeGrid.getColumnModel().config;

		var flag = true;
		for (var i = 0; i < count; i++) {
			var data = store.getAt(i).data;
			if (Ext.isEmpty(data.exec_time)) {
				flag = false;
			}
			if (!flag) {
				Alert('缺少发送时间，请删除!');
				break;
			}

			if (!flag)
				break;

			if (count > 1) {
				for (var j = i + 1; j < count; j++) {
					var d = store.getAt(j).data;
					if (data["exec_time"] == d["exec_time"]) {
						flag = false;
						Alert("第" + (i + 1) + "行和第" + (j + 1)
								+ "行执行时间相同,请删除一个！");
						break;
					}
				}
			}
			if (!flag)
				break;
		}
		return flag;
	}
});

StopTaskGrid = Ext.extend(Ext.grid.GridPanel, {
	osdstore : null,
	value:null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		StopTaskGridThiz = this;
		this.osdstore = new Ext.data.JsonStore({
					url : root + '/config/SendMsg!queryStop.action',
					fields : ['job_id', 'msg_type', 'exec_time', 'done_time',
							'limit_user_cnt', 'user_count', 'send_user_count',
							'task_code', 'status', "status_text"],
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
		StopTaskGrid.superclass.constructor.call(this, {
//			region : 'center',
			id : 'StopTaskGridId',
			ds : this.osdstore,
			cm : new Ext.grid.ColumnModel([{
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
						header : '操作',
						width : 60,
						dataIndex : 'status',
						renderer : function(v, md, record, i) {
							var txt = "";
							if (v == 'ACTIVE') {
								txt = "作废";
							}
							return "<a href='#' onclick='StopTaskGridThiz.deleteMessage();' style='color:blue'>"
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
								emptyText : '支持状态的模糊查询'
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
								StopTaskGridThiz.getStore().load({
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
				win = new CountyChooseWin("StopTaskWin");
			win.show();
		}else{
			var win = new StopTaskWin();
			win.setTitle('增加停机');
			win.setIconClass('icon-add-user');
			win.show();
		}
	},initEvents : function(){
		this.on('rowclick',this.doClick,this);
		StopTaskGrid.superclass.initEvents.call(this);
	},
	doClick : function(grid,rowIndex){
		var record = grid.getStore().getAt(rowIndex);
		if(Ext.isEmpty(this.value)||this.value!=record.get('job_id')){
			this.value = record.get('job_id');
			Ext.Ajax.request({
				url : root + '/config/SendMsg!queryMsgByJob.action?doneId=' + this.value,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					Ext.getCmp('stopTaskDetalisInfo').doint(rs)
				}
			});
		}
	}

});

var StopTaskHTML = '<table width="100%" border="0" cellpadding="0" cellspacing="0">'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>任务类型:</td>'
		+ '<td class="input_bold" width=10%>&nbsp;{[values.task_code_text || ""]}</td>'
		+ '<td class="label" width=10%>发送地址:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.address_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>任务状态:</td>'
		+ '<td class="input_bold">&nbsp;{[values.status_text || ""]}</td>'
		+ '<td class="label" width=10%>单位名称:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.unit_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>最大发送用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.limit_user_cnt || ""]}</td>'
		+ '<td class="label" width=10%>催停标识:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.hasten_stop_flag_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>处理用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.user_count || ""]}</td>'
		+ '<td class="label" width=10%>客户类别:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.cust_type_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>发送用户数:</td>'
		+ '<td class="input_bold">&nbsp;{[values.send_user_count || ""]}</td>'
		+ '<td class="label" width=10%>客户群体:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.cust_colony_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>完成日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.done_time) || ""]}</td>'
		+ '<td class="label" width=10%>优惠类型:</td>'
		+ '<td class="input_bold" width=60%>&nbsp;{[values.cust_class_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>创建日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.create_time) || ""]}</td>'
		+ '<td class="label" width=10%>产品预计到期区间(开始):</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.prod_invalid_bdate) || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>执行日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.exec_time) || ""]}</td>'
		+ '<td class="label" width=10%>产品预计到期区间(结束):</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.prod_invalid_edate) || ""]}</td>'
		+ '</tr>'
		+ '</tpl>' + '</table>';
StopTaskDetalisInfo = Ext.extend(Ext.Panel, {
	border : false,
	tpl : null,
	html : null,
	constructor : function(v) {
		this.tpl = new Ext.XTemplate(StopTaskHTML);
		this.tpl.compile();
		StopTaskDetalisInfo.superclass.constructor.call(this, {
					border : false,
					id : 'stopTaskDetalisInfo',
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
		var ppp = Ext.getCmp('stopTaskDetalisInfo').items.itemAt(0);
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
StopTaskView = Ext.extend(Ext.Panel, {
			constructor : function() {
				StopTaskView.superclass.constructor.call(this, {
							id : 'StopTaskView',
							layout : 'border',
							title : '批量停机',
							closable : true,
							border : false,
							baseCls : "x-plain",
							items : [{region:'center',layout:'fit',split : true,items:[new StopTaskGrid()]}, {region : 'south',height:220,split : true,layout : 'fit',items : [new StopTaskDetalisInfo()]}]
						})
			}
		});