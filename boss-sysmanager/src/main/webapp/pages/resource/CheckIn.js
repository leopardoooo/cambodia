var CHECK_LU = lsys('CheckIn');
var COM_LU = lsys('common');
var CHECK_COMMON = lsys('DeviceCommon');

//入库
var inputNo = {fieldLabel:CHECK_LU.labelInputNo,name:'deviceInput.input_no',xtype:'textfield',vtype:'alphanum',allowBlank:false}

//备机
var backup = {fieldLabel:CHECK_COMMON.labelBackUp,xtype:'paramcombo',paramName:'BOOLEAN',
		hiddenName:'deviceInput.backup',defaultValue:'F'
};
//新机
var isNewStb = {fieldLabel:CHECK_COMMON.labelIsNewStb,xtype:'paramcombo',paramName:'BOOLEAN',
		hiddenName:'deviceInput.is_new_stb',defaultValue:'T'
};
//产权
var ownership = {fieldLabel:CHECK_COMMON.labelOwnerShip,xtype:'paramcombo',paramName:'DEVICE_OWNERSHIP',
		hiddenName:'deviceInput.ownership',defaultValue:'GD'
};

//设备类型
var deviceType = {fieldLabel:CHECK_COMMON.labelDeviceType,xtype:'paramcombo',
		typeAhead:false,paramName:'DEVICE_TYPE',hiddenName:'deviceType',allowBlank:false,defaultValue:'STB'
};

//供应商下拉框
var supplierCombo = {fieldLabel:CHECK_COMMON.labelSupplier,hiddenName:'deviceInput.supplier_id',xtype:'combo',
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceSupplier.action',
			fields:['supplier_id','supplier_name']
		}),displayField:'supplier_name',valueField:'supplier_id',mode:'local',triggerAction:'all'
};

//备注
var remark = {fieldLabel:lsys('common.remarkTxt'),name:'deviceInput.remark',xtype:'textarea',anchor:'95%',height:40};

/**
 * 设备类型对应的设备ID长度.
 */
var devTypeLenCfg={};

/**
 * 设备编号编辑器.
 * @class DeviceCodeField
 * @extends Ext.form.NumberField
 */
DeviceCodeField = Ext.extend(Ext.form.TextField,{
	constructor:function(cfg){
		Ext.apply(this,cfg||{});
		if(!this.parent){
			throw new Error(lsys('msgBox.errCantAssignParent4DevNo'));
		}
		DeviceCodeField.superclass.constructor.call(this,{
			listeners:{
				scope:this,
				'change':function(field,newValue,oldValue){
					var rowData = this.parent.getSelectionModel().getSelected().data;
					var devType = rowData.device_type;
					var deviceModel = rowData.device_model;
					
					if(!devType || Ext.isEmpty(devType) || !deviceModel || Ext.isEmpty(deviceModel) ){
						Alert(lsys('msgBox.pleaseSelectSaveTypeAndModel'));
						field.setValue('');
						return false;
					}
					var url = '';
					if(devType == 'STB' && this.fieldName == 'device_code'){
						url = 'resource/Device!isExistsStb.action';
					}else if( (devType == 'CARD' && this.fieldName == 'device_code') 
						|| (devType == 'STB' && this.fieldName == 'pair_device_code') ){
						url = 'resource/Device!isExistsCard.action';
					}else if(devType == 'MODEM' && this.fieldName == 'device_code'){
						url = 'resource/Device!isExistsModem.action';
					}
					if(!Ext.isEmpty(url)){
						Ext.Ajax.request({
							url:url,
							params:{deviceCode:newValue},
							scope:this,
							success:function(res){
								var data = Ext.decode(res.responseText);
								if(data.success === true){
									Alert(newValue+lsys('msgBox.deviceCodeExists'));
									this.parent.getSelectionModel().getSelected().set(this.fieldName,null);
								}
							}
						});
					}
				}
			}
		});
	}
});

var CheckInDetailWin = Ext.extend(Ext.Window,{
	dsStore:null,
	constructor:function(){
		this.dsStore = new Ext.data.JsonStore({
			url:'resource/Device!queryInputDeviceDetail.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_model_text','device_code','pair_device_code','box_no']
		});
		var columns = [
			{header:DEV_COMMON_LU.labelBoxNo,dataIndex:'box_no',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevCode,dataIndex:'device_code',width:150,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:200,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelPairCardCode,dataIndex:'pair_device_code',width:120,renderer:App.qtipValue}

		];
		this.grid = new Ext.grid.GridPanel({
			border:false,
			store:this.dsStore,
			columns:columns,
			bbar:new Ext.PagingToolbar({store:this.dsStore,pageSize:Constant.DEFAULT_PAGE_SIZE})
		});
		CheckInDetailWin.superclass.constructor.call(this,{
			id:'CheckInDetailInfoWinId',
			title:COMMON_LU.detailInfo,
			closeAction:'close',
			maximizable:false,
			width:850,
			height:400,
			layout:'fit',
			items:[this.grid],
			buttons:[{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.close();
				}
			}]
		});
	},
	show:function(deviceDoneCode){
		this.dsStore.removeAll();
		this.dsStore.baseParams={
			deviceDoneCode:deviceDoneCode
		};
		this.dsStore.load({
			params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}
		});
		CheckInDetailWin.superclass.show.call(this);
	}
});

/**
 * 入库信息
 * @class
 * @extends Ext.grid.GridPanel
 */
var CheckInGrid = Ext.extend(Ext.grid.GridPanel,{
	checkInGridStore: null,
	constructor:function(){
		this.checkInGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceInput.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_done_code','input_no','depot_id','batch_num',
				'supplier_id','backup','ownership','create_time','optr_id',
				'remark','supplier_name','depot_name','optr_name','ownership_text',
				'device_type','device_model','count','device_model_text','device_type_text']
		});
		this.checkInGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:CHECK_LU.labelInputNo,dataIndex:'input_no',width:80,renderer:App.qtipValue},
			{header:CHECK_COMMON.labelBatchNum,dataIndex:'batch_num',width:80,renderer:App.qtipValue},
			{header:CHECK_COMMON.labelSupplier,dataIndex:'supplier_name',width:85},
			{header:CHECK_COMMON.labelInputDate,dataIndex:'create_time',width:135},
			{header:lsys('DeviceCommon.labelDeviceType'),dataIndex:'device_type_text',width:80},
			{header:lsys('DeviceCommon.labelDeviceModel'),dataIndex:'device_model_text',width:200,renderer:App.qtipValue},
			{header:lsys('DeviceCommon.labelNum'),dataIndex:'count',width:50},
			{id:'checkIn_remark_id',header:lsys('common.remarkTxt'),dataIndex:'remark',renderer:App.qtipValue},
			{header:lsys('common.doActionBtn'),dataIndex:'device_done_code',width:120,renderer:function(v,meta,record){
					if(currentOptrId == record.get('optr_id')){
						return "<a href='#' onclick=Ext.getCmp('checkInGridId').editInputNo("+v+")>"+CHECK_COMMON.titleModifyOrderNum+"</a>";
					}
					return null;
				}
			}
		];
		this.editInputNo = function(deviceDoneCode){
			var win = Ext.getCmp('inputNoWinId');
			if(!win){
				win = new InputNoWin();
			}
			win.show(deviceDoneCode);
		}
		CheckInGrid.superclass.constructor.call(this,{
			id:'checkInGridId',
			title:CHECK_LU.titleCheckInGrid,
			region:'center',
			border:false,
			autoExpandColumn:'checkIn_remark_id',
			store:this.checkInGridStore,
			columns:columns,
			sm:sm,
			tbar:['-',COM_LU.inputKeyWork,
				new Ext.ux.form.SearchField({
	                store: this.checkInGridStore,
	                width: 280,
	                hasSearch : true,
	                emptyText: CHECK_COMMON.selectByInputNoBatchNumDeviceType
	            }),'-','->','-',
				{text:CHECK_LU.labelFileInput,iconCls:'icon-excel',scope:this,handler:this.fileCheckIn},'-',
				{text:CHECK_LU.labelManualInput,iconCls:'icon-hand',scope:this,handler:this.handCheckIn},
					{text:CHECK_LU.labelMateralCheckIn,iconCls:'icon-hand',scope:this,handler:this.materalCheckIn},'-'
			],
			bbar : new Ext.PagingToolbar({
										store : this.checkInGridStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									})
		});
	},
	initEvents:function(){
		CheckInGrid.superclass.initEvents.call(this);
		this.on("rowdblclick",this.doRecordClick,this);
	},
	doRecordClick:function(){
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var win = Ext.getCmp('CheckInDetailInfoWinId');
		if(!win){
			win = new CheckInDetailWin();
		}
		win.show(deviceDoneCode);
	},
	fileCheckIn:function(){
		var fileWin = Ext.getCmp('fileCheckInWinId');
		if(!fileWin){
			fileWin = new FileCheckInWin(this);
		}
		fileWin.show();
	},
	handCheckIn:function(){
		var handWin = Ext.getCmp('handCheckInWinId');
		if(!handWin){
			handWin = new HandCheckInWin(this);
		}
		handWin.show();
	},
	materalCheckIn:function(){
		var win = Ext.getCmp('materalCheckInWinId');
		if(!win){
			win = new MateralCheckInWin(this);
		}
		win.show();
	}
});

/**
 * 文件入库form
 * @class
 * @extends Ext.form.FormPanel
 */
var FileForm = Ext.extend(Ext.form.FormPanel,{
	modelData:null,
	constructor:function(){
		FileForm.superclass.constructor.call(this,{
			id:'fileFormId',
			labelWidth: 120,
			layout : 'column',
			fileUpload: true,
			trackResetOnLoad:true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				columnWidth:.45,layout:'form',defaults:{anchor:'95%'},
				items:[
					inputNo,
					supplierCombo,
					ownership,
					isNewStb
				]},
				{columnWidth:.55,layout:'form',defaults:{anchor:'95%'},
					items:[{
							xtype: 'textfield',
							fieldLabel: CHECK_COMMON.labelBatchNum,
							name: 'batch_num',
							allowBlank:false
						},
						{fieldLabel:DEV_COMMON_LU.labelDeviceType,xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
							hiddenName:'deviceType',allowBlank:false,defaultValue:'STB',id:'deviceTypeInId'
							,listeners:{
								scope:this,
								select:function(combo){
									Ext.getCmp('ctlDeviceModelInId').reset();
									Ext.getCmp('filesDescId').reset();
								}
							}
						},{
							fieldLabel : CHECK_COMMON.labelDeviceModel,
							allowBlank : false,
							id : 'ctlDeviceModelInId',
							xtype:'combo',
							width:250,
							name:'device_model',
							emptyText: langUtils.bc("common.plsSwitch"),
							store: new Ext.data.JsonStore({
								fields : ['device_model', 'model_name', 'device_type','interactive_type']
							}),
							model: 'local',
							displayField: 'model_name',
							valueField: 'device_model',
							listWidth: 400
							,listeners:{
								scope:this,
								expand:function(combo){
									combo.getStore().loadData(this.modelData[Ext.get('deviceTypeInId').getValue()]);
								},
								select:function(combo,record,i){
									Ext.getCmp('deviceModelInId').setValue(combo.getValue());
									var str = "";
									if(record.get('device_type') == 'STB'){
										if(record.get('interactive_type') == 'SINGLE'){
											str = CHECK_COMMON.filesFormatOne
										}else{
											str = CHECK_COMMON.filesFormatTwo
										}
									}else{
										str = CHECK_COMMON.filesFormatThree
									}
									if(!Ext.isEmpty(str)){
										Ext.getCmp('filesInDescId').setValue("<font style='font-size:14px;color:red'>"+str+"</font>");
									}
								}
								
							}
						},backup,{name:'deviceModel',xtype:'textfield',id:'deviceModelInId',hidden:true}						
					]
				},{columnWidth:1,layout:'form',
					items:[{
			                xtype: 'displayfield',
			                width : 400,
			                id:'filesInDescId'
						},
						{id:'checkInFielId',fieldLabel:CHECK_COMMON.labelDevFile,name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%',emptyText:''}	
						,remark
				]}
			]
		});
	},
	initComponent:function(){
		FileForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		this.getForm().findField('deviceInput.supplier_id').getStore().load();
		Ext.Ajax.request({
			url:'resource/Device!queryDeviceStbModem.action',
			scope:this,
			success: function(res, ops){
				var rs = Ext.decode(res.responseText);
				this.modelData = rs;
			}
		});
	}
});

/**
 * 文件入库弹出框
 * @class
 * @extends Ext.Window
 */
var FileCheckInWin = Ext.extend(Ext.Window,{
	fileForm:null,
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.fileForm = new FileForm();
		FileCheckInWin.superclass.constructor.call(this,{
			id : 'fileCheckInWinId',
			title:CHECK_LU.labelFileInput,
			closeAction:'hide',
			maximizable:false,
			width: 600,
			height: 340,
			border: false,
			layout:'fit',
			items:[this.fileForm],
			buttonAlign:'right',
			buttons:[{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:lsys('common.cancel'),iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.fileForm.getForm().reset();
					resetFileCompContent(this.fileForm.getForm().findField('files'));//清空上传文件内容
					var suppCombo = this.fileForm.getForm().findField('deviceInput.supplier_id');
					if(suppCombo.readOnly)suppCombo.setReadOnly(false);
				}
			}
		});
	},
	show:function(){
		FileCheckInWin.superclass.show.call(this);
		this.fileForm.getForm().findField('deviceInput.input_no').focus(true,500);
	},
	doSave:function(){
		if(this.fileForm.getForm().isValid()){
			var file = Ext.getCmp('checkInFielId').getValue();
//			var flag = checkTxtFileType(file);
//			if(!flag)return;
			var flag = checkTxtXlsFileType(file);
			if(flag === false)return;
			
			var msg = Show();
			this.fileForm.getForm().submit({
				url:'resource/Device!saveDeviceInputFile.action?fileType='+flag,
//				waitTitle:'提示',
//				waitMsg:'正在上传中,请稍后...',
				scope:this,
				success:function(form,action){
					msg.hide();
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert(lsys('msgBox.fileUploadSuccess'),function(){
								this.hide();
								Ext.getCmp('checkInGridId').getStore().reload();
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					alert(lsys('msgBox.fileUploadSuccess'));  
				}
			});
		}
	}
});

/**
 * 手动入库form
 * @class
 * @extends Ext.form.FormPanel
 */
var HandForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		HandForm.superclass.constructor.call(this,{
			id:'handFormId',
			labelWidth: 120,
			height: 140,
			region: 'north',
			layout:'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[inputNo,supplierCombo,ownership]
				},
				{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[{
							xtype: 'textfield',
							fieldLabel: CHECK_COMMON.labelBatchNum,
							name: 'batch_num',
							allowBlank:false,
							width: 240
						},backup,isNewStb
					]
				},{
					columnWidth:1,
					layout:'form',
					items:[remark]
				}
			]
		});
	},
	initComponent:function(){
		HandForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		this.getForm().findField('deviceInput.supplier_id').getStore().load();
			
	}
});

var CheckInDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	checkInDeviceGridStore:null,
	deviceTypeCombo :null,
	deviceModelCombo :null,
	cardModelCombo :null,
	parent:null,
	constructor:function(p){
		this.parent = p;
		checkInDeviceGrid = this;
		this.checkInDeviceGridStore = new Ext.data.JsonStore({
			fields:['device_type','device_model','device_code','pair_device_model','pair_device_code','box_no']
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		this.deviceModelCombo = new Ext.ux.ParamCombo({typeAhead:false,
			forceSelection:true,selectOnFocus:true,editable:true,listWidth:300
		});
		this.cardModelCombo = new Ext.ux.ParamCombo({typeAhead:false,paramName:'CARD_MODEL',
			forceSelection:true,selectOnFocus:true,editable:true});
		this.deviceTypeCombo = new Ext.ux.ParamCombo({
			xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
			forceSelection:true,selectOnFocus:true,editable:true,
			listeners:{
				scope:this,
				select:function(combo){
					//当设备类型改变时，清空"型号"列
					var record = this.getSelectionModel().getSelected();
					record.set('device_model','');
					if(combo.getValue() !== 'STB'){
						record.set('pair_device_code','');
					}
				}
			}
		});
		
		var paramComboRender =function(value){
			var index = this.find('item_value',value);
			var record = this.getAt(index);
			if(!Ext.isEmpty(record)){
				return record.get('item_name');
			}
			return '';
		}
		
		doCheckInDel = function(){
			Confirm(lsys('msgBox.confirmDelete'),this,function(){
				Ext.getCmp('checkInDeviceGridId').getStore().remove(Ext.getCmp('checkInDeviceGridId').getSelectionModel().getSelected());
			});
		};
		
		var cm = new Ext.grid.ColumnModel([
			sm,
			{header:CHECK_COMMON.labelDeviceType,dataIndex:'device_type',width:75
				,editor:this.deviceTypeCombo
				,renderer:this.paramComboRender.createDelegate(this.deviceTypeCombo.getStore())
				,scope:this},
			{id:'device_model_id',header:CHECK_COMMON.labelDeviceModel,dataIndex:'device_model',width:165,editor:this.deviceModelCombo
				,renderer:this.paramComboRender.createDelegate(this.deviceModelCombo.getStore())
				,scope:this},
			{header:COM_LU.orderNum,dataIndex:'device_code',width:130,editor:new DeviceCodeField({parent:this,fieldName:'device_code',vtype:'alphanum'})},
			{id:'pair_device_code_id',header:CHECK_COMMON.labelPairCardCode,dataIndex:'pair_device_code',width:120,
				editor:new DeviceCodeField({parent:this,fieldName:'pair_device_code',vtype:'alphanum'})},
			{header:CHECK_COMMON.labelBoxNo,dataIndex:'box_no',width:120,editor:new Ext.form.TextField({vtype:'alphanum'})},
			{header:lsys('common.doActionBtn'),dataIndex:'',width:50,renderer:function(value,metavalue,record,i){
				return "<a href='#' onclick=doCheckInDel()>" + COM_LU.remove + "</a>";
			}}
		]);
		
		cm.isCellEditable = this.cellEditable;
		
		CheckInDeviceGrid.superclass.constructor.call(this,{
			id:'checkInDeviceGridId',
//			title:CHECK_COMMON.titleDeviceInfo,
			region:'center',
			ds:this.checkInDeviceGridStore,
			clicksToEdit:1,
			border: false,
			cm:cm,
			sm:sm,
			tbar:[{text:lsys('common.addNewOne'),iconCls:'icon-add',scope:this,handler:this.doAdd}]
		});
	},
	paramComboRender:function(value,combo){
		var index = this.find('item_value',value);
		var record = this.getAt(index);
		if(!Ext.isEmpty(record)){
			return record.get('item_name');
		}
		return '';
	},
	cellEditable:function(colIndex, rowIndex){
		var pdcIndex = this.getIndexById('pair_device_code_id');
//		var modemMacIndex = this.getIndexById('modem_mac_id');
		var deviceModeIndex = this.getIndexById('device_model_id');
		
		var deviceType = checkInDeviceGrid.getStore().getAt(rowIndex).get('device_type');
//		var devoceModel = checkInDeviceGrid.getStore().getAt(rowIndex).get('device_model');
//		var deviceTypeGroupedCfg = devTypeLenCfg[deviceType];
//		var devModcfg = false;
//		if(!Ext.isEmpty(deviceTypeGroupedCfg)){
//			devModcfg = deviceTypeGroupedCfg[devoceModel];//当前选中的设备的配置
//		}
		
		if(colIndex === deviceModeIndex){
			//"设备类型"列为空时，不能选择"型号"列
			if(Ext.isEmpty(deviceType))
				return false;
		}
		
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initComponent:function(){
		CheckInDeviceGrid.superclass.initComponent.call(this);
		App.form.initComboData([this.cardModelCombo,this.deviceTypeCombo]);
		Ext.Ajax.request({
			url:root + '/ps.action',
			params:{comboParamNames: ['CARD_MODEL','DEVICE_TYPE']},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				var arr = [];
				Ext.each(data[0],function(d){
					var obj = {};
					obj['item_name'] = d['item_name']+'('+d['item_value']+')';
					obj['item_value'] = d['item_value'];
					arr.push(obj);
				});
				this.cardModelCombo.getStore().loadData(arr);
				this.deviceTypeCombo.getStore().loadData(data[1]);
			}
		});
	},
	initEvents:function(){
		CheckInDeviceGrid.superclass.initEvents.call(this);
		this.on('afteredit',function(obj){
			var record = obj.record;
			var fieldName = obj.field;
			if(fieldName == 'device_code'){
				if(record.get('device_type') == 'MODEM'){
					record.set('pair_device_code',record.get('device_code'));
					
				}
			}
		});
		this.on('beforeedit',function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			
			if(fieldName == 'device_model'){
				var paramName = record.get('device_type')+'_MODEL';
				Ext.Ajax.request({
					url:root + '/ps.action',
					params:{comboParamNames: [paramName]},
					scope:this,
					success:function(res,opt){
						var data = Ext.decode(res.responseText)[0];
						var arr = [];
						Ext.each(data,function(d){
							var obj = {};
							obj['item_name'] = d['item_name']+'('+d['item_value']+')';
							obj['item_value'] = d['item_value'];
							obj['show_county_id'] = d['show_county_id'];
							arr.push(obj);
						});
						this.deviceModelCombo.getStore().loadData(arr);
					}
				});
			}else if(fieldName == 'pair_device_code'){
				
			}
		},this);
	},
	doAdd:function(){
		var count = this.getStore().getCount();
		var recordType = this.getStore().recordType;
		
		if(count >0){
			var record = this.getStore().getAt(count-1);
			var obj={};
			
			Ext.apply(obj,record.data);
			obj['device_code'] = '';
			obj['pair_device_code'] = '';
			this.getStore().add(new recordType(obj));
		}else{
			var record = new recordType({
				device_type:'',device_model:'',device_code:'',pair_device_code:'',box_no:''
			});
			this.stopEditing();
			this.getStore().add(record);
		}
		this.startEditing(count,1);
		this.getSelectionModel().selectRow(count);
		this.getStore().commitChanges();
	}
});

/**
 * 手工入库弹出框
 * @class
 * @extends Ext.Window
 */
var HandCheckInWin = Ext.extend(Ext.Window,{
	handForm:null,
	checkInDeviceGrid:null,
	deviceTypeArr:[],//选择订单编号，订单中规定只能输入的设备类型
	deviceModelArr:[],//以及 设备型号
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.handForm = new HandForm(this);
		this.checkInDeviceGrid = new CheckInDeviceGrid(this);
		HandCheckInWin.superclass.constructor.call(this,{
			id : 'handCheckInWinId',
			title:CHECK_LU.labelManualInput,
			closeAction:'close',
			maximizable:false,
			width: 700,
			height: 460,
			border: false,
			layout:'border',
			items:[this.handForm,this.checkInDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:lsys('common.cancel'),iconCls:'icon-close',scope:this,handler:function(){
					this.close();
				}}],
			listeners:{
				scope:this,
				close:function(){
					this.handForm.getForm().reset();
					var suppCombo = this.handForm.getForm().findField('deviceInput.supplier_id');
					if(suppCombo.readOnly)suppCombo.setReadOnly(false);
					this.checkInDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	show:function(){
		HandCheckInWin.superclass.show.call(this);
		this.handForm.getForm().findField('deviceInput.input_no').focus(true,500);
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		
		this.checkInDeviceGrid.stopEditing();

		var store = this.checkInDeviceGrid.getStore();
		
		var arr = [];
		store.each(function(record){
			arr.push(record.data);	
		},this);
		
		if(arr.length === 0){
			Alert(lsys('msgBox.pleaseInputCorrectDevInfo'));
			return;
		}
		
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
		
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveDeviceInput.action',
			params:obj,
			scope:this,
			timeout:999999999,
			success:function(res,opt){
				msg.hide();
				msg=null;
				Alert(COM_LU.addSuccess,function(){
					this.close();
					Ext.getCmp('checkInGridId').getStore().reload();
				},this);
			}
		});
	}
});


var MateralHandForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		MateralHandForm.superclass.constructor.call(this,{
			id:'materalHandFormId',
			labelWidth: 120,
			region:'center',
			layout:'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.45,layout:'form',defaults:{anchor:'95%'},
					items:[
						inputNo,supplierCombo
					]
				},
				{
					columnWidth:.55,layout:'form',defaults:{anchor:'95%'},
					items:[
						{
							fieldLabel : CHECK_COMMON.labelDeviceModel,
							allowBlank : false,
							id : 'materalDeviceModelId',
							xtype:'paramcombo',
							paramName:'FITTING_MODEL',
							hiddenName : 'device_model',
							listWidth: 300,
							emptyText: COM_LU.pleaseSelect,
							listeners:{
								scope:this,
								expand:function(combo){
									var store = combo.getStore();
									var that = this;
									if(this.parent.deviceModelArr.length>0){//如果选择了订单
										store.filterBy(function(record){
											var key = false;
											Ext.each(that.parent.deviceModelArr,function(data){
												if(record.get('item_value').indexOf(data) > -1){
													key = true;
												};
											},this);
											return key;
										});
									}
								}
							}
						},{
							fieldLabel : CHECK_COMMON.labelInputDepotNum,
							id : 'total_num',
							name:'total_num',
							allowBlank:false,
							allowDecimals:false, //不允许输入小数 
			    			allowNegative:false, //不允许输入负数
							xtype: 'numberfield',
			            	minValue: 1,
			            	width: 122,
			            	value: 1
						}
					]				
				},{
					columnWidth:1,
					layout:'form',
					items:[
						remark
					]
				}
			]
		});
	},
	initComponent:function(){
		MateralHandForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType("paramcombo"));
		
		this.getForm().findField('deviceInput.supplier_id').getStore().load();
			
	}
});


var MateralCheckInWin = Ext.extend(Ext.Window,{
	handForm:null,
	checkInDeviceGrid:null,
	deviceTypeArr:[],//选择订单编号，订单中规定只能输入的设备类型
	deviceModelArr:[],//以及 设备型号
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.handForm = new MateralHandForm(this);
		MateralCheckInWin.superclass.constructor.call(this,{
			id : 'materalCheckInWinId',
			title:CHECK_LU.labelMateralCheckIn,
			closeAction:'close',
			maximizable:false,
			width: 600,
			height: 250,
			border: false,
			layout:'border',
			items:[this.handForm],
			buttonAlign:'right',
			buttons:[{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:lsys('common.cancel'),iconCls:'icon-close',scope:this,handler:function(){
					this.close();
				}}],
			listeners:{
				scope:this,
				close:function(){
					this.handForm.getForm().reset();
					var suppCombo = this.handForm.getForm().findField('deviceInput.supplier_id');
					if(suppCombo.readOnly)suppCombo.setReadOnly(false);
				}
			}
		});
	},
	show:function(){
		MateralCheckInWin.superclass.show.call(this);
		this.handForm.getForm().findField('deviceInput.input_no').focus(true,500);
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		var obj={};
		Ext.apply(obj,formValues);
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveMateralDeviceInput.action',
			params:obj,
			scope:this,
			timeout:999999999,
			success:function(res,opt){
				msg.hide();
				msg=null;
				Alert(COM_LU.addSuccess,function(){
					this.close();
					Ext.getCmp('checkInGridId').getStore().reload();
				},this);
			}
		});
	}
});

/**
 * 修改入库单号
 * @class
 * @extends Ext.Window
 */
var InputNoWin = Ext.extend(Ext.Window, {
	constructor: function(){
		InputNoWin.superclass.constructor.call(this,{
			id:'inputNoWinId',
			title:CHECK_COMMON.titleModifyOrderNum,
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 450,
			height: 260,
			items:[{id:'inputNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:120,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:CHECK_COMMON.labelNewOrderNo,width:200,name:'inputNo',vtype:'alphanum',allowBlank:false},
					{fieldLabel:lsys('common.remarkTxt'),name:'remark',maxLength:128,xtype:'textarea',width : 210,height : 140}//128个汉字
				]
				
			}],
			buttonAlign:'right',
			buttons:[
				{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:lsys('common.cancel'),iconCls:'icon-close',scope:this,handler:function(){
						this.fireEvent('hide',this);
					}
				}
			],
			listeners:{
				scope:this,
				hide:function(){
					this.hide();
					Ext.getCmp('inputNoFormId').getForm().reset();
				}
			}
		});
	},
	show: function(deviceDoneCode){
		TransferNoWin.superclass.show.call(this);
		var form = Ext.getCmp('inputNoFormId').getForm();
		form.findField('deviceDoneCode').setValue(deviceDoneCode);
	},
	doSave: function(){
		var form = Ext.getCmp('inputNoFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		Ext.Ajax.request({
			url:root+'/resource/Device!editInputNo.action',
			params:values,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data === true){
					var record = Ext.getCmp('checkInGridId').getSelectionModel().getSelected();
					record.set('input_no',values['inputNo']);
					record.set('remark',values['remark']);
					record.commit();
					this.fireEvent('hide',this);
				}
			}
		});
	}
});

CheckIn = Ext.extend(Ext.Panel,{
	constructor:function(){
		var checkInGrid = new CheckInGrid();
		CheckIn.superclass.constructor.call(this,{
			id:'CheckIn',
			title:CHECK_COMMON.titleInputSimple,
			closable: true,
			border : false ,
			layout:'fit',
			items:[checkInGrid],
			listeners:{
				scope:this,
				'show':function(){
					Ext.Ajax.request({
						url:root+'/resource/Device!listDeviceLenCfg.action',
						scope:this,
						success:function(res){
							var data = Ext.decode(res.responseText);
							devTypeLenCfg = data;
						}
					});
				}
			}
		});
	}
});


