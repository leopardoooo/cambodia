
/**
 * 领用信息
 * @class
 * @extends Ext.grid.GridPanel
 */
var AppUseGrid = Ext.extend(Ext.grid.GridPanel,{
	appUseStore: null,
	constructor:function(){
		this.appUseStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceProcure.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_done_code','procure_no','doc_no','procure_dept','procurer',
				'procure_type','create_time','procure_type_text','remark','optr_id',
				'device_type','device_model','count','device_model_text','device_type_text']
		});
		this.appUseStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:'领用单号',dataIndex:'procure_no',width:70},
			{header:'领用部门',dataIndex:'procure_dept',width:80},
			{header:'领用人',dataIndex:'procurer',width:75},
			{header:'领用类型',dataIndex:'procure_type_text',width:65},
			{header:'创建日期',dataIndex:'create_time',width:70,renderer:Ext.util.Format.dateFormat},
			{header:'设备类型',dataIndex:'device_type_text',width:65},
			{header:'型号',dataIndex:'device_model_text',width:150,renderer:App.qtipValue},
			{header:'数量',dataIndex:'count',width:60},
			{id:'appUse_remark_id',header:'备注',dataIndex:'remark',width:200},
			{header:'操作',dataIndex:'device_done_code',width:65,renderer:function(v,meta,record){
					if(currentOptrId == record.get('optr_id')){
						return "<a href='#' onclick=Ext.getCmp('appUseGridId').editProcureNo("+v+")>修改单号</a>";
					}
					return null;
				}
			}
		];
		AppUseGrid.superclass.constructor.call(this,{
			id:'appUseGridId',
			title:'领用信息',
			border:false,
			ds:this.appUseStore,
			columns:columns,
			autoExpandColumn:'appUse_remark_id',
			sm:sm,
			tbar:['-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.appUseStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持领用编号模糊查询'
	            }),'-','->','-',
				{text:'设备领用',iconCls:'icon-hand',scope:this,handler:this.procureDevice},'-'
			],
			bbar: new Ext.PagingToolbar({store:this.appUseStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			listeners:{
				scope:this,
				rowdblclick:this.doDblclick
			}
		});
	},
	procureDevice:function(){
		var procureWin = Ext.getCmp('procureWinId');
		if(!procureWin){
			procureWin = new ProcureWin();
		}
		procureWin.show();
	},
	editProcureNo:function(deviceDoneCode){
		var win = Ext.getCmp('procureNoWinId');
		if(!win){
			win = new ProcureNoWin();
		}
		win.show(deviceDoneCode);
	},
	doDblclick:function(){
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var deviceType = record.get('device_type');
		
		var win = Ext.getCmp('procureDetailInfoWinId');
		if(!win){
			win = new ProcureDetailWin();
		}
		win.show(deviceDoneCode,deviceType);
	}
});

var ProcureDetailWin = Ext.extend(Ext.Window,{
	dsStore:null,
	constructor:function(){
		this.dsStore = new Ext.data.JsonStore({
			url:'resource/Device!queryProcureDeviceDetail.action',
			fields:['device_done_code','device_id','device_code',
				'optr_id','optr_name','create_time',
				'device_model','device_model_text']
		});
		var columns = [
			{header:'设备型号',dataIndex:'device_model_text',width:150,renderer:App.qtipValue},
			{header:'设备编号',dataIndex:'device_code',width:150,renderer:App.qtipValue},
			{header:'领用时间',dataIndex:'create_time',width:120},
			{header:'领用人',dataIndex:'optr_name',width:90},
			{header:'操作',dataIndex:'device_done_code',width:80,renderer:function(value){
					return "<a href='#' onclick=Ext.getCmp('procureDetailInfoWinId').cancelProcure()>取消领用</a>";
				}
			}
		];
		this.grid = new Ext.grid.GridPanel({
			border:false,
			store:this.dsStore,
			columns:columns,
			tbar:['输入设备编号：',
				{xtype:'textfield',emptyText:'输入设备编号回车查询',scope:this,width:200,
					listeners:{
						scope:this,
						specialkey:this.doQuery
					}
				},'-'//,{xtype:'button',text:'取消所有',scope:this,handler:this.doCancelAll}
			]
		});
		ProcureDetailWin.superclass.constructor.call(this,{
			id:'procureDetailInfoWinId',
			title:'详细信息',
			closeAction:'close',
			maximizable:false,
			width:650,
			height:300,
			layout:'fit',
			items:[this.grid],
			buttons:[{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.close();
				}
			}],
			listeners:{
				scope:this,
				close:function(){
					Ext.getCmp('appUseGridId').getStore().load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
				}
			}
		});
	},
	show:function(deviceDoneCode,deviceType){
		ProcureDetailWin.superclass.show.call(this);
		this.dsStore.load({
			params:{
				deviceDoneCode:deviceDoneCode,
				deviceType:deviceType
			}
		});
	},
	doQuery:function(field,e){
		if(e.getKey() == e.ENTER){
			this.dsStore.clearFilter();
			this.dsStore.filter('device_code',field.getValue(),true);
		}
	},
	cancelProcure:function(){
		Confirm("确定取消领用吗?",this,function(){
			var record = this.grid.getSelectionModel().getSelected();
			var deviceId = record.get('device_id');
			var deviceDoneCode = record.get('device_done_code');
			Ext.Ajax.request({
				url:'resource/Device!saveCancelProcure.action',
				params:{
					deviceDoneCode:deviceDoneCode,
					deviceId:deviceId
				},
				scope:this,
				success:function(res){
					Alert('取消领用成功');
					this.dsStore.remove(record);
				}
			});
		});
	},
	doCancelAll:function(){
		Confirm("确定取消所有领用吗?",this,function(){
			var deviceDoneCode = this.dsStore.getAt(0).get('device_done_code');
			Ext.Ajax.request({
				url:'resource/Device!saveCancelProcure.action',
				params:{
					deviceDoneCode:deviceDoneCode
				},
				scope:this,
				success:function(res){
					Alert('取消领用成功');
					this.close();
				}
			});
		});
	}
});

var ProcureNoWin = Ext.extend(Ext.Window, {
	constructor: function(){
		ProcureNoWin.superclass.constructor.call(this,{
			id:'procureNoWinId',
			title:'修改单号',
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 230,
			height: 100,
			items:[{id:'procureNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:65,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:'新单号',name:'procureNo',vtype:'alphanum',allowBlank:false}
				]
			}],
			buttonAlign:'right',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
						this.hide();
					}
				}
			],
			listeners:{
				scope:this,
				hide:function(){
					this.hide();
					Ext.getCmp('procureNoFormId').getForm().reset();
				}
			}
		});
	},
	show: function(deviceDoneCode){
		ProcureNoWin.superclass.show.call(this);
		var form = Ext.getCmp('procureNoFormId').getForm();
		form.findField('deviceDoneCode').setValue(deviceDoneCode);
	},
	doSave: function(){
		var form = Ext.getCmp('procureNoFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		Ext.Ajax.request({
			url:root+'/resource/Device!editProcureNo.action',
			params:values,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data['success'] === true){
					var record = Ext.getCmp('appUseGridId').getSelectionModel().getSelected();
					record.set('procure_no',values['procureNo']);
					record.commit();
					this.hide();
				}
			}
		});
	}
});

/**
 * 设备领用form
 * @class
 * @extends Ext.form.FormPanel
 */
var ProcureForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		ProcureForm.superclass.constructor.call(this,{
			id:'procureFormId',
			region:'north',
			height:170,
			labelWidth: 80,
			layout : 'column',
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.5,layout:'form',defaults:{xtype:'textfield'},
					items:[
						{fieldLabel:'领用单号',name:'deviceProcure.procure_no',vtype:'alphanum',allowBlank:false},
						{fieldLabel:'领用部门',name:'deviceProcure.procure_dept',allowBlank:false},
						{fieldLabel:'领用人',name:'deviceProcure.procurer',allowBlank:false}
					]
				},
				{
					columnWidth:.5,layout:'form',
					items:[
						{fieldLabel:'领用类型',hiddenName:'deviceProcure.procure_type',xtype:'paramcombo'
							,paramName:'DEPOT_BUY_MODE',allowBlank:false},
						{fieldLabel:'凭证类型',hiddenName:'deviceProcure.doc_type',xtype:'combo',allowBlank:false,
							store:new Ext.data.ArrayStore({
								fields:['doc_type'],data:[['销售单'],['领导批条']]
							}),displayField:'doc_type',valueField:'doc_type',triggerAction:'all',mode:'local'},
						{fieldLabel:'缴费单号',name:'deviceProcure.doc_no',xtype:'textfield',vtype:'alphanum'}
					]
				},{columnWidth:1,layout:'form',
					items:[
						{fieldLabel:'备注',name:'deviceProcure.remark',xtype:'textarea',height:65,anchor:'90%'}
					]}
			]
		});
	},
	initComponent:function(){
		ProcureForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
	}
});

/**
 * 设备信息
 * @class
 * @extends QueryDeviceGrid
 */
var ProcureQueryDeviceGrid = Ext.extend(QueryDeviceGrid,{
	constructor:function(){
		ProcureQueryDeviceGrid.superclass.constructor.call(this);
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
		if(newValue && newValue!==oldValue)
			Ext.Ajax.request({
				url:'resource/Device!queryDeviceProcureInfo.action',
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
});

/**
 * 设备领用
 * @class
 * @extends Ext.Window
 */
var ProcureWin = Ext.extend(Ext.Window,{
	procureForm:null,
	procureGrid:null,
	constructor:function(){
		this.procureForm = new ProcureForm();
		this.procureGrid = new ProcureQueryDeviceGrid();
		ProcureWin.superclass.constructor.call(this,{
			id : 'procureWinId',
			title:'设备领用',
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 450,
			border: false,
			layout:'border',
			items:[this.procureForm,this.procureGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.procureForm.getForm().reset();
					this.procureGrid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var form = this.procureForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		
		this.procureGrid.stopEditing();
		
		var store = this.procureGrid.getStore();
		
		var arr = [];
		store.each(function(record){
			if(record.get('device_id'))
				arr.push(record.data);
		},this);
		
		if(arr.length === 0 || Ext.isEmpty(arr[0]['device_code']) ){
			Alert('请正确输入设备信息！');
			return;
		}
		
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
		var mb = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveDeviceProcure.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				mb.hide();// 隐藏提示框
				mb = null;
				Alert('添加成功',function(){
					this.hide();
					Ext.getCmp('appUseGridId').getStore().reload();
				},this);
			}
		});
	}
});

AppUseDevice = Ext.extend(Ext.Panel,{
	constructor:function(){
		var appUseGrid = new AppUseGrid();
//		appUseDeviceGrid = new DeviceGrid();
		CheckIn.superclass.constructor.call(this,{
			id:'AppUseDevice',
			title:'设备领用',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'fit',
			items:[appUseGrid/*,appUseDeviceGrid*/]
		});
	}
});


