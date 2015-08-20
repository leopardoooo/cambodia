//退库
var outputNo = {fieldLabel:'退库编号',name:'deviceOutput.output_no',vtype:'alphanum',xtype:'textfield',allowBlank:false}
//供应商下拉框
var backSupplierCombo = {fieldLabel:'供应商',hiddenName:'deviceOutput.supplier_id',xtype:'combo',
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceSupplier.action',
			fields:['supplier_id','supplier_name']
		}),displayField:'supplier_name',valueField:'supplier_id',mode:'local',triggerAction:'all'
};

//设备类型
var backDeviceType = {fieldLabel:'设备类型',xtype:'paramcombo',
		typeAhead:false,paramName:'DEVICE_TYPE',hiddenName:'deviceType',allowBlank:false,defaultValue:'STB'
};

//退库类型
var outputType = {fieldLabel:'退库类型',hiddenName:'deviceOutput.output_type',xtype:'paramcombo',
	paramName:'DEVICE_OUT_TYPE',allowBlank:false
};

//备注
var backRemark = {fieldLabel:'备注',name:'deviceOutput.remark',xtype:'textarea',anchor:'90%',height:50};

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
			{header:'退库单号',dataIndex:'output_no',width:85},
			{header:'退库类别',dataIndex:'output_type_text',width:75},
			{header:'供应商',dataIndex:'supplier_name',width:75},
			{header:'退库日期',dataIndex:'create_time',width:90,renderer:Ext.util.Format.dateFormat},
			{header:'设备类型',dataIndex:'device_type_text',width:70},
			{header:'型号',dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:'数量',dataIndex:'count',width:70},
			{id:'backHome_remark_id',header:'备注',dataIndex:'remark',width:200},
			{header:'操作',dataIndex:'device_done_code',renderer:function(v,meta,record){
					if(currentOptrId == record.get('optr_id')){
						return "<a href='#' onclick=Ext.getCmp('backHourseGridId').editOutputNo("+v+")>修改单号</a>";
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
			title:'退库信息',
			region:'center',
			autoExpandColumn:'backHome_remark_id',
			border:false,
			store:this.backHouseGridStore,
			columns:columns,
			sm:sm,
			tbar:['-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.backHouseGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持退库编号模糊查询'
	            }),'-','->','-',
				{text:'文件退库',iconCls:'icon-excel',scope:this,handler:this.fileBack},'-',
				{text:'手工退库',iconCls:'icon-hand',scope:this,handler:this.handBack},'-',
				{text:'器材退库',iconCls:'icon-batch-number',scope:this,handler:this.materalBack}
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

//器材调拨
BackMateralWin = Ext.extend(Ext.Window,{
	handForm:null,
	queryDeviceGrid:null,
	constructor:function(){
		this.handForm = new BackHandForm();
		this.queryDeviceGrid = new MateralTransferDeviceGrid();
		BackMateralWin.superclass.constructor.call(this,{
			id : 'backMateralWinId',
			title:'器材退库',
			closeAction:'hide',
			maximizable:false,
			width: 600,
			height: 500,
			border: false,
			layout:'border',
			items:[this.handForm, this.queryDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
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
		var that = this;
		Ext.Ajax.request({
			url:'resource/Device!queryMateralTransferDeviceByDepotId.action',
			success: function(res, ops){
				that.queryDeviceGrid.remoteData = Ext.decode(res.responseText);
			}
		});
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		this.queryDeviceGrid.stopEditing();
		
		var store = this.queryDeviceGrid.getStore();
		
		var arrCode = [];
		for(var i=0;i<store.getCount();i++){
			var data = store.getAt(i).data;
			if(data['device_id']){
				//过滤掉重复调拨的设备
				if(arrCode.indexOf(data['device_id']) >=0){
					Alert('器材有相同的，请检查！');
					return ;
				}
				if(Ext.isEmpty(data['num'])){
					Alert('退库数量不能为空！');
					return;
				}
				arrCode.push(data);
			}
		}
		var arr = [];//只传递device_id到后台
		Ext.each(arrCode,function(d){
			var obj = {};
			obj['device_id'] = d['device_id'];
			obj['device_type'] = d['device_type'];
			obj['device_model'] = d['device_model'];
			obj['total_num'] = d['num'];
			arr.push(obj);
		});
		
		if(arr.length === 0){
			Alert('请正确输入设备信息！');
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
				Alert('添加成功',function(){
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
			labelWidth: 70,
			layout : 'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				columnWidth:.5,layout:'form',
				items:[
					outputNo,
					backSupplierCombo
				]},
				{columnWidth:.5,layout:'form',
					items:[
						backDeviceType,
						outputType
					]
				},{columnWidth:1,layout:'form',
					items:[
						{id:'backHouseFileId',fieldLabel:'设备文件',name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'},//,width:367},
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
			title:'文件退库',
			closeAction:'hide',
			maximizable:false,
			width: 450,
			height: 230,
			layout: 'fit',
			border: false,
			items:[this.fileForm],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){this.hide();}}],
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
			var flag = checkFileType(file);
			if(!flag)return;
			
			this.fileForm.getForm().submit({
				url:'resource/Device!saveDeviceOutputFile.action',
				waitTitle:'提示',
				waitMsg:'正在上传中,请稍后...',
				scope:this,
				success:function(form,action){
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('文件上传成功!',function(){
								this.hide();
								Ext.getCmp('backHourseGridId').getStore().reload();
							},this);
						}
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
 * 手动退库form
 * @class
 * @extends Ext.form.FormPanel
 */
var BackHandForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		BackHandForm.superclass.constructor.call(this,{
			id:'backHandFormId',
			labelWidth: 80,
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
			title:'手工退库',
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 450,
			border: false,
			layout:'border',
			items:[this.handForm,this.backDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
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
					Alert('编号有相同的，请检查！');
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
			Alert('请正确输入设备信息！');
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
				Alert('添加成功',function(){
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
			title:'修改单号',
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 330,
			height: 250,
			items:[{id:'outputNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:65,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:'新单号',width:200,name:'outputNo',vtype:'alphanum',allowBlank:false},
					{fieldLabel:'备注',name:'remark',maxLength:128,xtype:'textarea',width : 210,height : 140}//128个汉字
					
				]
			}],
			buttonAlign:'right',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
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
			title:'退库',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[backGrid, detailGrid]
		});
	}
});


