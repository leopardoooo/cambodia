
var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var BH_LU = lsys('BackHouse');
var MSG_LU = lsys('msgBox');

//退库
var outputNo = {fieldLabel:BH_LU.outPutNo,name:'deviceOutput.output_no',vtype:'alphanum',xtype:'textfield',allowBlank:false}
//供应商下拉框
var backSupplierCombo = {fieldLabel:DEV_COMMON_LU.labelSupplier,hiddenName:'deviceOutput.supplier_id',xtype:'combo',
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceSupplier.action',
			fields:['supplier_id','supplier_name']
		}),displayField:'supplier_name',valueField:'supplier_id',mode:'local',triggerAction:'all'
};

//设备类型
var backDeviceType = {fieldLabel:DEV_COMMON_LU.labelDeviceType,xtype:'paramcombo',
		typeAhead:false,paramName:'DEVICE_TYPE',hiddenName:'deviceType',allowBlank:false,defaultValue:'STB'
};

//退库类型
var outputType = {fieldLabel:BH_LU.outPutType,hiddenName:'deviceOutput.output_type',xtype:'paramcombo',
	paramName:'DEVICE_OUT_TYPE',allowBlank:false
};

//备注
var backRemark = {fieldLabel:COMMON_LU.remarkTxt,name:'deviceOutput.remark',xtype:'textarea',anchor:'90%',height:50};

/**
 * 退库信息
 * @class
 * @extends Ext.grid.GridPanel
 */
var BackHouseGrid = Ext.extend(Ext.grid.GridPanel,{
	backHouseGridStore: null,
	constructor:function(){
		this.backHouseGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceOutput.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_done_code','output_no','output_type','output_type_text',
				'create_time','supplier_name','supplier_id','remark','optr_id',
				'device_type','device_model','count','device_model_text','device_type_text']
		});
		this.backHouseGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:BH_LU.outPutNo,dataIndex:'output_no',width:85},
			{header:BH_LU.outPutType,dataIndex:'output_type_text',width:75},
			{header:DEV_COMMON_LU.labelSupplier,dataIndex:'supplier_name',width:75},
			{header:BH_LU.outPutDate,dataIndex:'create_time',width:90,renderer:Ext.util.Format.dateFormat},
			{header:DEV_COMMON_LU.labelDeviceType,dataIndex:'device_type_text',width:70},
			{header:COMMON_LU.modelSimple,dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelNum,dataIndex:'count',width:70},
			{id:'backHome_remark_id',header:COMMON_LU.remarkTxt,dataIndex:'remark',width:200},
			{header:COMMON_LU.doActionBtn,dataIndex:'device_done_code',renderer:function(v,meta,record){
					if(currentOptrId == record.get('optr_id')){
						return "<a href='#' onclick=Ext.getCmp('backHourseGridId').editOutputNo("+v+")>" + DEV_COMMON_LU.titleModifyOrderNum + "</a>";
					}
					return null;
				}
			}
		];
		this.editOutputNo = function(deviceDoneCode){
			var win = Ext.getCmp('outputNoWinId');
			if(!win){
				win = new OutputNoWin();
			}
			win.show(deviceDoneCode);
		}
		BackHouseGrid.superclass.constructor.call(this,{
			id:'backHourseGridId',
			title:BH_LU.titleOutputInfo,
			region:'center',
			autoExpandColumn:'backHome_remark_id',
			border:false,
			store:this.backHouseGridStore,
			columns:columns,
			sm:sm,
			tbar:['-',COMMON_LU.inputKeyWork,
				new Ext.ux.form.SearchField({  
	                store: this.backHouseGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: DEV_COMMON_LU.tipSupportFuzzyQuery
	            }),'-','->','-',
				{text:BH_LU.fileOutput,iconCls:'icon-excel',scope:this,handler:this.fileBack},'-',
				{text:BH_LU.manualOutput,iconCls:'icon-hand',scope:this,handler:this.handBack},'-',
				{text:BH_LU.materalOutPut,iconCls:'icon-hand',scope:this,handler:this.materalBack}
			],
			bbar : new Ext.PagingToolbar({
										store : this.backHouseGridStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									}),
			listeners:{
				scope:this,
				rowclick:this.doClick
			}
		});
	},
	doClick : function() {
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var deviceType = record.get('device_type');
		var deviceModel = record.get('device_model');
		var ele = this.getEl();
		ele.mask();
		Ext.Ajax.request({
			url:root+'/resource/Device!queryOutputDeviceDetail.action',
			params : {
				deviceDoneCode: deviceDoneCode,
				deviceType:deviceType,
				deviceModel:deviceModel,
				start: 0,
				limit: Constant.DEFAULT_PAGE_SIZE
			},
			scope:this,
			success:function(res,opt){
				ele.unmask();
				var data = Ext.decode(res.responseText);
				var store = this.detaiGrid.getStore();
				store.loadData(data,false);
				
				store.proxy = new Ext.data.HttpProxy({url:root+'/resource/Device!queryOutputDeviceDetail.action'});
				store.baseParams = {
					deviceType:deviceType,
					deviceDoneCode:deviceDoneCode
				};
			},
			clearData:function(res,opt){
				ele.unmask();
			}
		});
	},
	fileBack:function(){
		var fileWin = Ext.getCmp('backFileWinId');
		if(!fileWin){
			fileWin = new BackFileWin();
		}
		fileWin.show();
	},
	handBack:function(){
		var handWin = Ext.getCmp('backHandWinId');
		if(!handWin){
			handWin = new BackHandWin();
		}
		handWin.show();
	},
	materalBack:function(){
		var materalWin = Ext.getCmp('backMateralWinId');
		if(!materalWin){
			materalWin = new BackMateralWin();
		}
		materalWin.show();
	}
});

var MateralBackDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	materalStore:null,
	remoteData:null,
	constructor:function(parent){
		this.parent = parent;
		materalThat = this;
		this.materalStore = new Ext.data.JsonStore({
			url:'resource/Device!queryMateralTransferDeviceByDepotId.action',
			fields:['device_model','device_type','device_model_text',
				'device_type_text','total_num','num','device_id']
		});	
		var cm = new Ext.grid.ColumnModel([
				{id:'device_model_text_id',header:DEV_COMMON_LU.labelDeviceType,dataIndex:'device_model_text',width:300
				},
				{header:DEV_COMMON_LU.labelTotalStoreNum,dataIndex:'total_num',width:70,renderer:App.qtipValue},
				{id:'num_id',header:DEV_COMMON_LU.labelNum,dataIndex:'num',width:100,
					scope:this
					,editor: new Ext.form.NumberField({
						allowDecimals:false,//不允许输入小数 
		    			allowNegative:false,
		    			minValue:0//enableKeyEvents: true,
					})
				},
				{header:DEV_COMMON_LU.labelDevCode,dataIndex:'device_id',hidden:true},
				{header:DEV_COMMON_LU.labelDeviceTypeCode ,dataIndex:'device_type',hidden:true}
			]
		);
		cm.isCellEditable = this.cellEditable;
		MateralBackDeviceGrid.superclass.constructor.call(this,{
			title:DEV_COMMON_LU.labelMateralInfo,
			region:'center',
			id:'MateralBackDeviceGridId',
			ds:this.materalStore,
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({})
		});
	},//是否可编辑
	cellEditable:function(colIndex,rowIndex){
		var record = materalThat.getStore().getAt(rowIndex);//当前编辑行对应record
		if(colIndex == this.getIndexById('num_id')){
			if(Ext.isEmpty(record.get('device_model_text'))){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initComponent:function(){
		MateralBackDeviceGrid.superclass.initComponent.call(this);
	},
	initEvents:function(){
		MateralBackDeviceGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10});
		
		this.on("afteredit",this.afterEdit,this);
		this.on("beforeedit",this.beforeedit,this);
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
	},
	beforeedit:function(obj){
	
	},
	afterEdit:function(obj){
		var record = obj.record;
		var fieldName = obj.field;//编辑的column对应的dataIndex
		var value = obj.value; 
		if(fieldName == 'num'){
			if(value >record.get('total_num')){
				record.set('num','');
				Confirm(MSG_LU.tipOutOfStock,this,function(){
					materalThat.startEditing(obj.row,obj.column);
				});
			}
			record.set('num',parseInt(value));
		}
	}
});

var BackMateralForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		BackHandForm.superclass.constructor.call(this,{
			labelWidth: 120,
			height:155,
			region:'north',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[
				outputNo,
				backSupplierCombo,
				outputType,
				backRemark
			]
		});
	},
	initComponent:function(){
		BackMateralForm.superclass.initComponent.call(this);
		
		App.form.initComboData( this.findByType("paramcombo"));
		
		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(!(comboes[i] instanceof Ext.ux.ParamCombo)){
					comboes[i].getStore().load();	
				}
			}
			
	}
});

//器材退库
BackMateralWin = Ext.extend(Ext.Window,{
	handForm:null,
	queryDeviceGrid:null,
	constructor:function(){
		this.handForm = new BackMateralForm();
		this.queryDeviceGrid = new MateralBackDeviceGrid();
		BackMateralWin.superclass.constructor.call(this,{
			id : 'backMateralWinId',
			title:DEV_COMMON_LU.materalOutPut,
			closeAction:'hide',
			maximizable:false,
			width: 600,
			height: 500,
			border: false,
			layout:'border',
			items:[this.handForm, this.queryDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.handForm.getForm().reset();
					this.queryDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	show:function(){
		BackMateralWin.superclass.show.call(this);
		this.queryDeviceGrid.materalStore.load();
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		this.queryDeviceGrid.stopEditing();
		
		var store = this.queryDeviceGrid.getStore();
		var arr = [];//只传递device_id到后台
		store.each(function(record){
			if(!Ext.isEmpty(record.get('num'))  && record.get('num')>0){
				var obj = {};
				obj['device_id'] = record.get('device_id');
				obj['device_type'] = record.get('device_type');
				obj['device_model'] = record.get('device_model');
				obj['total_num'] = record.get('num');
				arr.push(obj);
			}
		});
		
		if(arr.length === 0){
			Alert(MSG_LU.pleaseInputCorrectDevInfo);
			return;
		}
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
	
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveMateralOutput.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert(COMMON_LU.addSuccess,function(){
					this.hide();
					Ext.getCmp('backHourseGridId').getStore().reload();
				},this);
			}
		});
	}
});


/**
 * 文件退库form
 * @class
 * @extends Ext.form.FormPanel
 */
var BackFileForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		BackFileForm.superclass.constructor.call(this,{
			id:'backFileFormId',
			border:false,
			labelWidth: 120,
			layout : 'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				columnWidth:.45,layout:'form',
				items:[
					outputNo,
					backSupplierCombo
				]},
				{columnWidth:.55,layout:'form',
					items:[
						backDeviceType
						,outputType
					]
				},{columnWidth:1,layout:'form',
					items:[{
			                xtype: 'displayfield',
			                width : 400,
			                value:"<font style='font-size:14px;color:red'>"+CHECK_COMMON.filesFormatFour+"</font>"
						},
						{id:'backHouseFileId',fieldLabel:DEV_COMMON_LU.labelDevFile,name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'},//,width:367},
						backRemark
				]}
			]
		});
	},
	initComponent:function(){
		BackFileForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));

		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(!(comboes[i] instanceof Ext.ux.ParamCombo)){
					comboes[i].getStore().load();	
				}
			}
	}
});

/**
 * 文件退库弹出框
 * @class
 * @extends Ext.Window
 */
var BackFileWin = Ext.extend(Ext.Window,{
	fileForm:null,
	constructor:function(){
		this.fileForm = new BackFileForm();
		BackFileWin.superclass.constructor.call(this,{
			id : 'backFileWinId',
			title:BH_LU.fileOutput,
			closeAction:'hide',
			maximizable:false,
			width: 700,
			height: 300,
			layout: 'fit',
			border: false,
			items:[this.fileForm],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){this.hide();}}],
			listeners:{
				scope:this,
				hide:function(){
					this.fileForm.getForm().reset();
					resetFileCompContent(this.fileForm.getForm().findField('files'));//清空上传文件内容
					this.hide();
				}
			}
		});
	},
	doSave:function(){
		if(this.fileForm.getForm().isValid()){
			var file = Ext.getCmp('backHouseFileId').getValue();
			var flag = checkTxtXlsFileType(file);
			if(flag === false)return;
			var msg = Show();
			this.fileForm.getForm().submit({
				url:'resource/Device!saveDeviceOutputFile.action?fileType='+flag,
//				waitTitle:COMMON_LU.tipTxt,
//				waitMsg:COMMON_LU.waitForUpload,
				scope:this,
				success:function(form,action){
					msg.hide();
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert(MSG_LU.fileUploadSuccess,function(){
								this.hide();
								Ext.getCmp('backHourseGridId').getStore().reload();
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					alert(MSG_LU.fileUploadFailure);  
				}
			});
		}
	}
});

/**
 * 手动退库form
 * @class
 * @extends Ext.form.FormPanel
 */
var BackHandForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		BackHandForm.superclass.constructor.call(this,{
			id:'backHandFormId',
			labelWidth: 120,
			height:155,
			region:'north',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[
				outputNo,
				backSupplierCombo,
				outputType,
				backRemark
			]
		});
	},
	initComponent:function(){
		BackHandForm.superclass.initComponent.call(this);
		
		App.form.initComboData( this.findByType("paramcombo"));
		
		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(!(comboes[i] instanceof Ext.ux.ParamCombo)){
					comboes[i].getStore().load();	
				}
			}
			
	}
});

/**
 * 退库设备信息
 * @class
 * @extends QueryDeviceGrid
 */
var BackDeviceGrid = Ext.extend(QueryDeviceGrid,{
	constructor:function(){
		BackDeviceGrid.superclass.constructor.call(this);
		var config = this.getColumnModel().config;
		//为第二列"编号" 添加编辑组件
		config[0].editor = new Ext.form.TextField({
			vtype:'alphanum',
			listeners:{
				scope:this,
				change:this.queryAndAddDevice
			}
		});
		//重新注册
		this.reconfigure(
			this.getStore(),new Ext.grid.ColumnModel(config)
		);
	},
	queryAndAddDevice:function(field,newValue,oldValue){
		if(newValue && newValue!==oldValue){
			Ext.Ajax.request({
				url:'resource/Device!queryDeviceOutputInfo.action',
				params:{deviceCode:newValue},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data.device_id){
						var record = this.getSelectionModel().getSelected();
						record.fields.each(function(field){
							record.set(field.name,data[field.name]);
						},this);
//						Confirm('需要继续添加吗?',this,function(){
							this.doAdd();
//						});
					}
				}
			});
		}
	}
});

/**
 * 手工退库弹出框
 * @class
 * @extends Ext.Window
 */
var BackHandWin = Ext.extend(Ext.Window,{
	handForm:null,
	backDeviceGrid:null,
	constructor:function(){
		this.handForm = new BackHandForm();
		this.backDeviceGrid = new BackDeviceGrid();
		BackHandWin.superclass.constructor.call(this,{
			id : 'backHandWinId',
			title:DEV_COMMON_LU.manualOutput,
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 450,
			border: false,
			layout:'border',
			items:[this.handForm,this.backDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.handForm.getForm().reset();
					this.backDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		
		this.backDeviceGrid.stopEditing();
		
		var store = this.backDeviceGrid.getStore();
		
		var arrCode = [];
		for(var i=0;i<store.getCount();i++){
			var data = store.getAt(i).data;
			if(data['device_id']){
				//过滤掉重复调拨的设备
				if(arrCode.indexOf(data['device_id']) >=0){
					Alert(MSG_LU.warnHasSaveCode);
					return ;
				}
				arrCode.push(data);
			}
		}
		var arr = [];//只传递device_id到后台
		Ext.each(arrCode,function(d){
			var obj = {};
			obj['device_id'] = d['device_id'];
			obj['device_type'] = d['device_type'];
			obj['device_code'] = d['device_code'];
			arr.push(obj);
		});
		
		if(arr.length === 0){
			Alert(MSG_LU.pleaseInputCorrectDevInfo);
			return;
		}
		
//		alert(Ext.encode(formValues));
//		alert(Ext.encode(arr));
		
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
	
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveDeviceOutput.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert(COMMON_LU.addSuccess,function(){
					this.hide();
					Ext.getCmp('backHourseGridId').getStore().reload();
				},this);
			}
		});
	}
});

var OutputNoWin = Ext.extend(Ext.Window, {
	constructor: function(){
		OutputNoWin.superclass.constructor.call(this,{
			id:'outputNoWinId',
			title:DEV_COMMON_LU.titleModifyOrderNum,
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 400,
			height: 300,
			items:[{id:'outputNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:120,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:DEV_COMMON_LU.labelNewOrderNo,width:200,name:'outputNo',vtype:'alphanum',allowBlank:false},
					{fieldLabel:COMMON_LU.remarkTxt,name:'remark',maxLength:128,xtype:'textarea',width : 210,height : 140}//128个汉字
					
				]
			}],
			buttonAlign:'right',
			buttons:[
				{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
						this.fireEvent('hide',this);
					}
				}
			],
			listeners:{
				scope:this,
				hide:function(){
					this.hide();
					Ext.getCmp('outputNoFormId').getForm().reset();
				}
			}
		});
	},
	show: function(deviceDoneCode){
		TransferNoWin.superclass.show.call(this);
		var form = Ext.getCmp('outputNoFormId').getForm();
		form.findField('deviceDoneCode').setValue(deviceDoneCode);
	},
	doSave: function(){
		var form = Ext.getCmp('outputNoFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		Ext.Ajax.request({
			url:root+'/resource/Device!editOutputNo.action',
			params:values,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					this.close();
					Ext.getCmp('backHourseGridId').getStore().reload();
				}
			}
		});
	}
});

BackHouse = Ext.extend(Ext.Panel,{
	constructor:function(){
		var backGrid = new BackHouseGrid();
		var detailGrid = new DeviceDetailGrid();
		backGrid.detaiGrid = detailGrid;
		BackHouse.superclass.constructor.call(this,{
			id:'BackHouse',
			title:BH_LU.titleSimple,
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[backGrid, detailGrid]
		});
	}
});


