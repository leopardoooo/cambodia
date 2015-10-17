/**
 * 设备状态管理
 * 
 * @class
 * @extends Ext.Panel
 */

var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var STATUS_LU = lsys('DeviceStatus');
var MSG_LU = lsys('msgBox');

var DeviceStatusGrid = Ext.extend(Ext.grid.GridPanel, {
			statusStore : null,
			detaiGrid:null,
			constructor : function() {
				this.statusStore = new Ext.data.JsonStore({
							url : 'resource/Device!queryDeviceStatusEdit',
							totalProperty : 'totalProperty',
							root : 'records',
							fields : ['device_done_code', 'depot_id',
									'edit_type', 'optr_id', 'remark',
									'create_time', 'old_value', 'new_value',
									'depot_id_text', 'old_value_text',
									'new_value_text', 'device_type',
									'device_model', 'count',
									'device_model_text', 'device_type_text']
						});
				this.statusStore.load({
							params : {
								start : 0,
								limit : Constant.DEFAULT_PAGE_SIZE
							}
						});
				var columns = [
						// {header:'修改前',dataIndex:'old_value_text',width:100},
						{
					header : '修改后',
					dataIndex : 'new_value_text',
					width : 150
				}, {
					header : DEV_COMMON_LU.labelDepot,
					dataIndex : 'depot_id_text',
					width : 150
				}, {
					header : DEV_COMMON_LU.labelOperateTime,
					dataIndex : 'create_time',
					width : 150,
					renderer:Ext.util.Format.dateFormat
				}, {
					header : DEV_COMMON_LU.labelDeviceType,
					dataIndex : 'device_type_text',
					width : 120
				}, {
					header : DEV_COMMON_LU.labelDeviceModel,
					dataIndex : 'device_model_text',
					width : 120
				}, {
					header : DEV_COMMON_LU.labelNum,
					dataIndex : 'count',
					width : 120
				}, {
					id : 'deviceStatus_remark_id',
					header : COMMON_LU.remarkTxt,
					dataIndex : 'remark',
					width : 150
				}];

				DeviceStatusGrid.superclass.constructor.call(this, {
							id : 'deviceStatusGridId',
							region : 'center',
							store : this.statusStore,
							columns : columns,
							autoExpandColumn : 'deviceStatus_remark_id',
							sm : new Ext.grid.RowSelectionModel({}),
							tbar : ['-', {
										text : DEV_COMMON_LU.labelFileOperate,
										iconCls : 'icon-excel',
										scope : this,
										handler : this.fileIn
									}, '-', {
										text : DEV_COMMON_LU.labelManualInput,
										iconCls : 'icon-hand',
										scope : this,
										handler : this.handIn
									}, '-'],
							bbar : new Ext.PagingToolbar({
										store : this.statusStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									}),
							listeners : {
								scope : this,
								rowclick : this.doClick
							}
						});
			},
			handIn : function() {
				var win = Ext.getCmp('deviceStatusHandWinId');
				if (!win) {
					win = new DeviceStatusHandWin();
				}
				win.show();
			},
			fileIn : function() {
				var win = Ext.getCmp('deviceStatusFileWinId');
				if (!win) {
					win = new DeviceStatusFileWin();
				}
				win.show();
			},
			doClick : function() {
				var record = this.getSelectionModel().getSelected();
				var deviceDoneCode = record.get('device_done_code');
				var deviceType = record.get('device_type');
				var ele = this.getEl();
				ele.mask();
				Ext.Ajax.request({
					url:root+'/resource/Device!queryEditDeviceDetail.action',
					params : {
						deviceDoneCode: record.get('device_done_code'),
						deviceType:deviceType,
						start: 0,
						limit: Constant.DEFAULT_PAGE_SIZE
					},
					scope:this,
					success:function(res,opt){
						ele.unmask();
						var data = Ext.decode(res.responseText);
						var store = this.detaiGrid.getStore();
						store.loadData(data,false);
						store.proxy = new Ext.data.HttpProxy({url:root+'/resource/Device!queryEditDeviceDetail.action'});
						store.baseParams = {
							deviceType:deviceType,
							deviceDoneCode:deviceDoneCode
						};
					},
					clearData:function(res,opt){
						ele.unmask();
					}
				});
			}
		});

		
/**
 * 设备状态管理 文件导入form
 * 
 * @class
 * @extends Ext.form.FormPanel
 */
var DeviceStatusFileForm = Ext.extend(Ext.form.FormPanel, {
			constructor : function() {
				DeviceStatusFileForm.superclass.constructor.call(this, {
							id : 'deviceStatusFileFormId',
							border : false,
							labelWidth : 120,
							bodyStyle : 'padding-top:10px',
							fileUpload : true,
							defaults : {
								baseCls : 'x-plain'
							},
							items : [{
										fieldLabel : DEV_COMMON_LU.labelDeviceType,
										xtype : 'paramcombo',
										typeAhead : false,
										paramName : 'DEVICE_TYPE',
										hiddenName : 'deviceType',
										allowBlank : false
									}, {
										id : 'deviceStatusFielId',
										fieldLabel : DEV_COMMON_LU.labelDevFile,
										name : 'files',
										xtype : 'textfield',
										inputType : 'file',
										allowBlank : false,
										anchor : '95%'
									}, {
										fieldLabel : DEV_COMMON_LU.labelDevStatus,
										hiddenName : 'deviceStatus',
										allowBlank : false,
										xtype : 'paramcombo',
										paramName : 'DEVICE_STATUS_R_DEVICE'
									},  {
										fieldLabel : STATUS_LU.labelSetNewOrOld,
										hiddenName : 'isNewStb',
										allowBlank : false,
										xtype : 'paramcombo',
										paramName : 'BOOLEAN'
									}, {
										fieldLabel : COMMON_LU.remarkTxt,
										xtype : 'textarea',
										name : 'remark',
										width : 300,
										height : 100
									}]
						});
			},
			initComponent : function() {
				DeviceStatusFileForm.superclass.initComponent.call(this);
				App.form.initComboData(this.findByType("paramcombo"));
			}
		});

var DeviceStatusFileWin = Ext.extend(Ext.Window, {
			statusForm : null,
			constructor : function() {
				this.statusForm = new DeviceStatusFileForm();
				DeviceStatusFileWin.superclass.constructor.call(this, {
							id : 'deviceStatusFileWinId',
							title : STATUS_LU.titleFileOptr,
							closeAction : 'hide',
							maximizable : false,
							width : 550,
							height : 350,
							layout : 'fit',
							border : false,
							items : [this.statusForm],
							buttonAlign : 'right',
							buttons : [{
										text : COMMON_LU.saveBtn,
										iconCls : 'icon-save',
										scope : this,
										handler : this.doSave
									}, {
										text : COMMON_LU.cancel,
										iconCls : 'icon-close',
										scope : this,
										handler : function() {
											this.hide();
										}
									}],
							listeners : {
								scope : this,
								hide : function() {
									this.statusForm.getForm().reset();
									resetFileCompContent(this.statusForm
											.getForm().findField('files'));// 清空上传文件内容
								}
							}
						});
			},
			doSave : function() {
				if (this.statusForm.getForm().isValid()) {

					var file = Ext.getCmp('deviceStatusFielId').getValue();
					var flag = checkTxtXlsFileType(file);
					if(flag === false)return;
					var msg = Show();
					this.statusForm.getForm().submit({
						url : 'resource/Device!changeDeviceStatusFile.action?fileType='+flag,
//						waitTitle : '提示',
//						waitMsg : '正在上传中,请稍后...',
						scope : this,
						success : function(form, action) {
							msg.hide();
							var data = action.result;
							if (data.success == true) {
								var msgtxt = '文件上传处理成功!';
								if (data.msg) {// 错误信息
									msgtxt = data.msg;
								}
								Alert(msgtxt,function(){
										this.hide();
												Ext.getCmp('deviceStatusGridId')
														.getStore().reload();
									},this);
							}
						},
						failure : function(form, action) {
							alert("文件上传失败!");
						}
					});
				}
			}
		});

/**
 * 设备状态管理 手动导入form
 * 
 * @class
 * @extends Ext.form.FormPanel
 */
var DeviceStatusHandForm = Ext.extend(Ext.form.FormPanel, {
	parent:null,
	doValid:function(){
		var result = this.getForm().isValid();
		if(result){
			var arr = this.findByType("paramcombo");
			result = false;
			for(var idx =0;idx<arr.length;idx++){
				var val = arr[idx].getValue();
				result = result || !Ext.isEmpty(val);
			}
		}
		return result;
	},
	clearGridData:function(){
		var s = this.parent.statusGrid.getStore();
		s.removeAll();
	},
	isNewChange:function(){
		this.clearGridData();
	},
	statusChange:function(combo,newValue,oldValue){
		var isNewStbCombo = this.getForm().findField('isNewStb');
		var isNew = isNewStbCombo.getValue();
		if(newValue == 'CORRUPT'){
			isNewStbCombo.setValue('');
			isNewStbCombo.setDisabled(true);
		}else{
			isNewStbCombo.setDisabled(false);
		}
		this.clearGridData();
	},
	constructor : function(p) {
		this.parent = p;
		DeviceStatusHandForm.superclass.constructor.call(this, {
			id : 'deviceStatusHandFormId',
			region:'south',
			labelWidth : 130,
			height:150,
			bodyStyle : 'padding-top:10px',
			fileUpload : true,
			defaults : {
				baseCls : 'x-plain'
			},
			items : [{
						id : 'device_stauts_id',
						fieldLabel : DEV_COMMON_LU.labelDevStatus,
						hiddenName : 'deviceStatus',
						xtype : 'paramcombo',
						listeners:{
							scope:this,
							change:this.statusChange
						},
						paramName : 'DEVICE_STATUS_R_DEVICE'
					},  {
						fieldLabel : STATUS_LU.labelSetNewOrOld,
						hiddenName : 'isNewStb',
						id:'device_is_new_stb',
						xtype : 'paramcombo',
						listeners:{
							scope:this,
							change:this.isNewChange
						},
						paramName : 'BOOLEAN'
					},{
						fieldLabel : COMMON_LU.remarkTxt,
						xtype : 'textarea',
						name : 'remark',
						width : 350,
						height : 80
					}]
		});
	},
	initComponent : function() {
		DeviceStatusHandForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType("paramcombo"));
	}
});

/**
 * 设备信息
 * 
 * @class
 * @extends QueryDeviceGrid
 */
var StatusQueryDeviceGrid = Ext.extend(QueryDeviceGrid, {
	constructor : function() {
		StatusQueryDeviceGrid.superclass.constructor.call(this);
		var config = this.getColumnModel().config;
		// 为第二列"编号" 添加编辑组件
		config[0].editor = new Ext.form.TextField({
					vtype : 'alphanum',
					listeners : {
						scope : this,
						change : this.queryAndAddDevice
					}
				});
		// 重新注册
		this.reconfigure(this.getStore(), new Ext.grid.ColumnModel(config));
	},
	queryAndAddDevice : function(field, newValue, oldValue) {
		if (newValue && newValue !== oldValue) {
			var deviceStatus = Ext.getCmp('device_stauts_id').getValue();
			var isNewStb = Ext.getCmp('device_is_new_stb').getValue();
			if (Ext.isEmpty(deviceStatus)) {
				Alert(MSG_LU.tipSelectTragetDevStatus);
				return;
			}
			Ext.Ajax.request({
						url : 'resource/Device!queryDeviceStatusChangeInfo.action',
						params : {
							'device.device_id' : newValue,
							'device.is_new_stb' : isNewStb,
							'device.device_status' : deviceStatus
						},
						scope : this,
						success : function(res, opt) {
							var data = Ext.decode(res.responseText);
							if (data.device_id) {
								var record = this.getSelectionModel()
										.getSelected();
								record.fields.each(function(field) {
											record.set(field.name,
													data[field.name]);
										}, this);
								// Confirm('需要继续添加吗?',this,function(){
								this.doAdd();
								// });
							}
						}
					});
		}
	}
});

var DeviceStatusHandWin = Ext.extend(Ext.Window, {
			procureForm : null,
			procureGrid : null,
			constructor : function() {
				this.statusForm = new DeviceStatusHandForm(this);
				this.statusGrid = new StatusQueryDeviceGrid();
				ProcureWin.superclass.constructor.call(this, {
							id : 'deviceStatusHandWinId',
							title : STATUS_LU.titleManualOptr,
							closeAction : 'hide',
							maximizable : false,
							width : 800,
							height : 450,
							border : false,
							layout:'border',
							items : [this.statusForm, this.statusGrid],
							buttonAlign : 'right',
							buttons : [{
										text : COMMON_LU.saveBtn,
										iconCls : 'icon-save',
										scope : this,
										handler : this.doSave
									}, {
										text : COMMON_LU.cancel,
										iconCls : 'icon-close',
										scope : this,
										handler : function() {
											this.hide();
										}
									}],
							listeners : {
								scope : this,
								hide : function() {
									this.statusForm.getForm().reset();
									this.statusGrid.getStore().removeAll();
								}
							}
						});
			},
			doSave : function() {
				var form = this.statusForm.getForm();
				if (!this.statusForm.doValid()){
					Alert(MSG_LU.tipSbztSbxjZstxyg);
					return;
				}
					
				var formValues = form.getValues();

				this.statusGrid.stopEditing();

				var store = this.statusGrid.getStore();

				var deviceIds = '';
				store.each(function(record) {
							if (record.get('device_id'))
								deviceIds = deviceIds.concat(record
												.get('device_id'), ',');
						});
				deviceIds = deviceIds.substring(0, deviceIds.length - 1);
				if (deviceIds.length === 0) {
					Alert(MSG_LU.pleaseInputCorrectDevInfo);
					return;
				}

				var obj = {};
				Ext.apply(obj, formValues);
				obj['deviceIds'] = deviceIds;

				var msg = Show();
				Ext.Ajax.request({
							url : 'resource/Device!changeDeviceStatus.action',
							params : obj,
							scope : this,
							success : function(res, opt) {
								msg.hide();
								msg = null;
								Alert(COMMON_LU.addSuccess, function() {
											this.hide();
											Ext.getCmp('deviceStatusGridId')
													.getStore().reload();
										}, this);
							}
						});
			}
		});

DeviceStatus = Ext.extend(Ext.Panel, {
	constructor : function() {
		var detailGrid = new DeviceDetailGrid();
		var grid = new DeviceStatusGrid();
		grid.detaiGrid = detailGrid;
		DeviceStatus.superclass.constructor.call(this, {
					id : 'DeviceStatus',
					title : STATUS_LU._title,
					closable : true,
					border : false,
					layout : 'border',
					items : [grid ,detailGrid]
				});
	}
});