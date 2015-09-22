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
		typeAhead:false,paramName:'DEVICE_TYPE',hiddenName:'deviceType',allowBlank:false,defaultValue:'STB',
		listeners:{
			scope:this,
			expand:function(combo){
				var store = combo.getStore();
				store.filterBy(function(record){
					return record.get('item_value').indexOf('CARD')<0;
				})
			}
		}
};

//供应商下拉框
var supplierCombo = {fieldLabel:CHECK_COMMON.labelSupplier,hiddenName:'deviceInput.supplier_id',xtype:'combo',
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceSupplier.action',
			fields:['supplier_id','supplier_name']
		}),displayField:'supplier_name',valueField:'supplier_id',mode:'local',triggerAction:'all'
};

//备注
var remark = {fieldLabel:lsys('common.remarkTxt'),name:'deviceInput.remark',xtype:'textarea',anchor:'95%',height:50};

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
					}else if((devType == 'MODEM' || devType == 'STB') && this.fieldName == 'modem_mac'){
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
			fields:['device_done_code','input_no','depot_id','order_done_code','order_no','batch_num',
				'supplier_id','backup','ownership','create_time','optr_id',
				'remark','supplier_name','depot_name','optr_name','ownership_text',
				'device_type','device_model','count','device_model_text','device_type_text']
		});
		this.checkInGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:CHECK_LU.labelInputNo,dataIndex:'input_no',width:80,renderer:App.qtipValue},
			{header:CHECK_LU.labelOrderNo,dataIndex:'order_no',width:80,renderer:App.qtipValue},
			{header:CHECK_LU.labelInputNo,dataIndex:'batch_num',width:80,renderer:App.qtipValue},
			{header:CHECK_COMMON.labelSupplier,dataIndex:'supplier_name',width:85},
			{header:CHECK_COMMON.labelInputDate,dataIndex:'create_time',width:135},
			{header:lsys('DeviceCommon.labelDeviceType'),dataIndex:'device_type_text',width:80},
			{header:lsys('DeviceCommon.labelDeviceModel'),dataIndex:'device_model_text',width:150,renderer:App.qtipValue},
			{header:lsys('DeviceCommon.labelNum'),dataIndex:'count',width:50},
			{id:'checkIn_remark_id',header:lsys('common.remarkTxt'),dataIndex:'remark',renderer:App.qtipValue},
			{header:lsys('common.doActionBtn'),dataIndex:'device_done_code',renderer:function(v,meta,record){
					if(currentOptrId == record.get('optr_id')){
						return "<a href='#' onclick=Ext.getCmp('checkInGridId').editInputNo("+v+")>修改单号</a>";
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
	                width: 210,
	                hasSearch : true,
	                emptyText: lsys('msgBox.supportDevCodeOrderNoFuzzyQuery')
	            }),'-','->','-',
				{text:CHECK_LU.labelFileInput,iconCls:'icon-excel',scope:this,handler:this.fileCheckIn,tooltip:lsys('msgBox.tipCheckInFileInput')},'-',
				{text:CHECK_LU.labelManualInput,iconCls:'icon-hand',scope:this,handler:this.handCheckIn},
					{text:CHECK_LU.labelMateralCheckIn,iconCls:'icon-hand',scope:this,handler:this.materalCheckIn},'-'
			],
			bbar : new Ext.PagingToolbar({
										store : this.checkInGridStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									})
		});
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
	
	},
	loadOrderInfo: function(store,key){
		Ext.Ajax.request({
			url:'resource/Device!queryDeviceOrder.action',
			success:function(res,opt){
				var result = Ext.decode(res.responseText).records;
				if(result && result.length>0){
					var doneCodeArr=[],data=[];
					Ext.each(result,function(d){
						if(key){
							if(d['device_type'] == 'STB' || d['device_type'] == 'CARD' || d['device_type'] == 'MODEM' ){
								//去掉重复的记录
								if(doneCodeArr.indexOf(d['device_done_code']) == -1 ){
									data.push(d);
									doneCodeArr.push(d['device_done_code']);
								}
							}
						}else{
							if(d['device_type'] != 'STB' && d['device_type'] != 'CARD' && d['device_type'] != 'MODEM' ){
								//去掉重复的记录
								if(doneCodeArr.indexOf(d['device_done_code']) == -1 ){
									data.push(d);
									doneCodeArr.push(d['device_done_code']);
								}
							}
							
						}
					});
					store.loadData(data);
				}
			}
		});
	}
});

/**
 * 文件入库form
 * @class
 * @extends Ext.form.FormPanel
 */
var FileForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		FileForm.superclass.constructor.call(this,{
			id:'fileFormId',
			labelWidth: 80,
			layout : 'column',
			fileUpload: true,
			trackResetOnLoad:true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
				items:[
					inputNo,
					supplierCombo,
					ownership,
					isNewStb
				]},
				{columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[
						{fieldLabel:lsys('OrderManager.labelOrderNo'),hiddenName:'deviceInput.order_done_code',xtype:'combo',
							store:new Ext.data.JsonStore({
								fields:['order_no','device_done_code','supplier_id','supplier_name','order_info']
							}),displayField:'order_info',valueField:'device_done_code',
							mode:'local',triggerAction:'all',forceSelection:true,editable:true,
							listeners:{
								select:function(combo,record){
									var fileForm = Ext.getCmp('fileFormId');
									var supCombo= fileForm.getForm().findField('deviceInput.supplier_id');
									supCombo.setRawValue(record.get('supplier_name'));
									supCombo.setValue(record.get('supplier_id'));
									supCombo.setReadOnly(true);
								}
							}
					},
						backup,
						deviceType
					]
				},{columnWidth:1,layout:'form',
					items:[
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
			width: 550,
			height: 300,
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
		//订单管理模块中随时会添加订单，故show window时查询订单
		var store = this.fileForm.getForm().findField('deviceInput.order_done_code').getStore();
		this.parent.loadOrderInfo(store,true);
		this.fileForm.getForm().findField('deviceInput.input_no').focus(true,500);
	},
	doSave:function(){
		if(this.fileForm.getForm().isValid()){
			var file = Ext.getCmp('checkInFielId').getValue();
			var flag = checkFileType(file);
			if(!flag)return;
			var msg = Show();
			this.fileForm.getForm().submit({
				url:'resource/Device!saveDeviceInputFile.action',
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
			labelWidth: 80,
			height: 170,
			region: 'north',
			layout:'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[
						inputNo,
						supplierCombo,
						ownership
					]
				},
				{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[
						{fieldLabel:lsys('OrderManager.labelOrderNo'),hiddenName:'deviceInput.order_done_code',xtype:'combo',
								store:new Ext.data.JsonStore({
									fields:['order_no','device_done_code','supplier_id','supplier_name','order_info']
								}),displayField:'order_info',valueField:'device_done_code',
								mode:'local',triggerAction:'all',forceSelection:true,editable:true,
								listeners:{
									scope:this,
									select:function(combo,record){
										var handForm = Ext.getCmp('handFormId');
										var supCombo= handForm.getForm().findField('deviceInput.supplier_id');
										supCombo.setRawValue(record.get('supplier_name'));
										supCombo.setValue(record.get('supplier_id'));
										supCombo.setReadOnly(true);
										
										var checkInStore = this.parent.checkInDeviceGrid.getStore();
										if(checkInStore.getCount()>0)
											checkInStore.removeAll();
										
										this.parent.deviceTypeArr = [];
										this.parent.deviceModelArr = [];
										
										var doneCode = combo.getValue();
										Ext.Ajax.request({
											url:'resource/Device!queryDeviceOrderDetail.action',
											params:{deviceDoneCode:doneCode},
											scope:this,
											success:function(res,opt){
												var data = Ext.decode(res.responseText);
												if(data && data.length>0){
													Ext.each(data,function(d){
														if(this.parent.deviceTypeArr.indexOf(d['device_type'])==-1)
															this.parent.deviceTypeArr.push(d['device_type']);
														if(this.parent.deviceModelArr.indexOf(d['device_model'])==-1)
															this.parent.deviceModelArr.push(d['device_model']);
													},this);
												}
											}
										});
										
									}
								}
						},
						backup,
						isNewStb
//						deviceType
					]
				},{
					columnWidth:1,
					layout:'form',
					items:[{
						xtype: 'textfield',
						fieldLabel: CHECK_COMMON.labelBatchNum,
						name: 'batch_num',
						width: 240
					}]
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
		HandForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		this.getForm().findField('deviceInput.supplier_id').getStore().load();
			
	}
});

var CheckInDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	checkInDeviceGridStore:null,
	
	deviceTypeCombo :null,deviceTypeData:[],//设备类型所有数据 
	deviceModelCombo :null,deviceModelData:[],//设备型号所有数据
	cardModelCombo :null,
	parent:null,
	constructor:function(p){
		this.parent = p;
		checkInDeviceGrid = this;
		this.checkInDeviceGridStore = new Ext.data.JsonStore({
			fields:['device_type','device_model','modem_mac','device_code','pair_device_model','pair_device_code']
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		this.deviceModelCombo = new Ext.ux.ParamCombo({typeAhead:false,
			forceSelection:true,selectOnFocus:true,editable:true,listWidth:200
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
						record.set('pair_device_model','');
						record.set('pair_device_code','');
						record.set('modem_mac','');
					}
				},
				afterrender:function(combo){
					this.deviceTypeData = [];
					var store = combo.getStore();
					store.each(function(record){
						this.deviceTypeData.push(record.data);
					},this);
				}
				,
				expand:function(combo){
					var store = combo.getStore();
					if(this.parent.deviceTypeArr.length>0){//如果选择了订单
						store.removeAll();//下拉时清空数据 
						Ext.each(this.deviceTypeData,function(deviceType){
							Ext.each(this.parent.deviceTypeArr,function(data){
								if(deviceType['item_value'] == data){
									store.loadData([deviceType],true);
								}
							},this);
						},this);
					}else{
						store.filterBy(function(record){
							return record.get('item_value').indexOf('CARD')<0;
						})
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
			{id:'modem_mac_id',header:'modem_mac',dataIndex:'modem_mac',width:120
				,editor:new DeviceCodeField({parent:this,fieldName:'modem_mac',vtype:'alphanum'})},
			/*{id:'pair_device_model_id',header:'配对卡型号',dataIndex:'pair_device_model',width:110,
				editor:this.cardModelCombo
				,renderer:this.paramComboRender.createDelegate(this.cardModelCombo.getStore())
				,scope:this},*/
			{id:'pair_device_code_id',header:CHECK_COMMON.labelPairCardCode,dataIndex:'pair_device_code',width:120,
				editor:new DeviceCodeField({parent:this,fieldName:'pair_device_code',vtype:'alphanum'})},
			{header:lsys('common.doActionBtn'),dataIndex:'',width:50,renderer:function(value,metavalue,record,i){
				return "<a href='#' onclick=doCheckInDel()>" + COM_LU.remove + "</a>";
			}}
		]);
		
		cm.isCellEditable = this.cellEditable;
		
		CheckInDeviceGrid.superclass.constructor.call(this,{
			id:'checkInDeviceGridId',
			title:CHECK_COMMON.titleDeviceInfo,
			region:'center',
			ds:this.checkInDeviceGridStore,
			clicksToEdit:1,
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
		var modemMacIndex = this.getIndexById('modem_mac_id');
		var deviceModeIndex = this.getIndexById('device_model_id');
		
		var deviceType = checkInDeviceGrid.getStore().getAt(rowIndex).get('device_type');
		
		var deviceType = checkInDeviceGrid.getStore().getAt(rowIndex).get('device_type');
		var devoceModel = checkInDeviceGrid.getStore().getAt(rowIndex).get('device_model');
		var deviceTypeGroupedCfg = devTypeLenCfg[deviceType];
		var devModcfg = false;
		if(!Ext.isEmpty(deviceTypeGroupedCfg)){
			devModcfg = deviceTypeGroupedCfg[devoceModel];//当前选中的设备的配置
		}
		
		if(colIndex === pdcIndex){
			//只有设备类型为机顶盒时，才能输入卡号
			if(deviceType !== 'STB'){
				return false;
			}
		}else if(colIndex === modemMacIndex){
//			if(deviceType !== 'STB'){
//				return false;
//			}
			
			//"modem_mac"列只能在设备类型为MODEM或者机猫一体机时，才能编辑
//			if(deviceType !== 'MODEM'){
//				if(deviceType == 'STB')&& devModcfg && devModcfg.virtual_modem_model){
//					return true;
//				}else{
//					return false;
//				}
//			}
		}else if(colIndex === deviceModeIndex){
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
					record.set('modem_mac',record.get('device_code'));
				}
			}
		});
		
		
		this.on('beforeedit',function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			
			if(fieldName == 'device_model'){
				/*this.deviceModelCombo.paramName = record.get('device_type')+'_MODEL';
				App.form.initComboData( [this.deviceModelCombo],function(){
					if(this.parent.deviceModelArr.length>0){
						this.deviceModelData = [];
						var store = this.deviceModelCombo.getStore();
						
						if(store.getCount()>0){
							store.each(function(record){
								this.deviceModelData.push(record.data);
							},this);
						}
						
						store.removeAll();//下拉时清空数据 
						Ext.each(this.deviceModelData,function(deviceModel){
							Ext.each(this.parent.deviceModelArr,function(data){
								if(deviceModel['item_value'] == data){
									store.loadData([deviceModel],true);
								}
							},this);
						},this);
					}
					
				},this);*/
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
						
						if(this.parent.deviceModelArr.length>0){
							this.deviceModelData = [];
							var store = this.deviceModelCombo.getStore();
							
							if(store.getCount()>0){
								store.each(function(record){
									this.deviceModelData.push(record.data);
								},this);
							}
							
							store.removeAll();//下拉时清空数据 
							Ext.each(this.deviceModelData,function(deviceModel){
								Ext.each(this.parent.deviceModelArr,function(data){
									if(deviceModel['item_value'] == data){
										store.loadData([deviceModel],true);
									}
								},this);
							},this);
						}
					}
				});
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
			obj['modem_mac'] = '';
			this.getStore().add(new recordType(obj));
		}else{
			var record = new recordType({
				device_type:'',device_model:'',modem_mac:'',device_code:'',pair_device_model:'',pair_device_code:''
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
			height: 500,
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
		//订单管理模块中随时会添加订单，故show window时查询订单
		var store = this.handForm.getForm().findField('deviceInput.order_done_code').getStore();
//		Ext.Ajax.request({
//			url:'resource/Device!queryDeviceOrder.action',
//			success:function(res,opt){
//				var result = Ext.decode(res.responseText).records;
//				var doneCodeArr=[],data=[];
//				Ext.each(result,function(d){
//					//去掉重复的记录
//					if(doneCodeArr.indexOf(d['device_done_code']) == -1){
//						data.push(d);
//						doneCodeArr.push(d['device_done_code']);
//					}
//				});
//				store.loadData(data);
//			}
//		});
		this.parent.loadOrderInfo(store,true);
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
			var deviceType = record.get('device_type');
			var deviceModel = record.get('device_model');
			var deviceCode = record.get('device_code');
			var modemMac = record.get('modem_mac');
			if(deviceModel){
				if((deviceType == 'STB' || deviceType == 'CARD') && deviceCode){
					arr.push(record.data);	
				}else if(deviceType == 'MODEM' && deviceCode){
					arr.push(record.data);	
				}
			}
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
	deviceModelData:[],
	constructor:function(p){
		this.parent = p;
		MateralHandForm.superclass.constructor.call(this,{
			id:'materalHandFormId',
			labelWidth: 80,
			height: 170,
			region:'center',
			layout:'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[
						inputNo,supplierCombo
					]
				},
				{
					columnWidth:.5,layout:'form',defaults:{anchor:'95%'},
					items:[
						{fieldLabel:lsys('OrderManager.labelOrderNo'),hiddenName:'deviceInput.order_done_code',xtype:'combo',
								store:new Ext.data.JsonStore({
									fields:['order_no','device_done_code','supplier_id','supplier_name','order_info']
								}),displayField:'order_info',valueField:'device_done_code',
								mode:'local',triggerAction:'all',forceSelection:true,editable:true,
								listeners:{
									scope:this,
									select:function(combo,record){
										var handForm = Ext.getCmp('materalHandFormId');
										var supCombo= handForm.getForm().findField('deviceInput.supplier_id');
										supCombo.setRawValue(record.get('supplier_name'));
										supCombo.setValue(record.get('supplier_id'));
										supCombo.setReadOnly(true);									
										
										Ext.getCmp("materalDeviceModelId").reset();
										this.parent.deviceModelArr = [];
										
										var doneCode = combo.getValue();
										Ext.Ajax.request({
											url:'resource/Device!queryDeviceOrderDetail.action',
											params:{deviceDoneCode:doneCode},
											scope:this,
											success:function(res,opt){
												var data = Ext.decode(res.responseText);
												if(data && data.length>0){
													Ext.each(data,function(d){
														if(this.parent.deviceModelArr.indexOf(d['device_model'])==-1)
															this.parent.deviceModelArr.push(d['device_model']);
													},this);
												}
											}
										});
										
									}
								}
						},{
							fieldLabel : CHECK_COMMON.labelDeviceModel,
							allowBlank : false,
							id : 'materalDeviceModelId',
							xtype:'paramcombo',
							paramName:'FITTING_MODEL',
							hiddenName : 'device_model',
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
			height: 400,
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
		//订单管理模块中随时会添加订单，故show window时查询订单
		var store = this.handForm.getForm().findField('deviceInput.order_done_code').getStore();
		this.parent.loadOrderInfo(store,false);
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
			width: 330,
			height: 250,
			items:[{id:'inputNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:65,items:[
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


