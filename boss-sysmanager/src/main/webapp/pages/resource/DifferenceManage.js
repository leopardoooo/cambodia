/**
 * 差异管理
 * @class
 * @extends Ext.grid.GridPanel
 */
var DifferenceGrid = Ext.extend(Ext.grid.GridPanel,{
	differenceGridStore: null,
	constructor:function(){
		this.differenceGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceDiffence.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_id','device_code','pair_device_model','pair_device_code','depot_id',
				'depot_id_text','device_type_text','device_type','device_model','device_model_text','diffence_type',
				'diffence_type_text','create_time','pair_device_modem_code','pair_device_modem_model','pair_device_modem_model_text']
		});
		this.differenceGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.CheckboxSelectionModel({});     
		var columns = [
			sm,
			{header:'设备编号',dataIndex:'device_code',width:180},
			{header:'设备类型',dataIndex:'device_type_text',width:75},
			{header:'型号',dataIndex:'device_model_text',width:120},
			{header:'差异类型',dataIndex:'diffence_type_text',width:75},
			{header:'所在仓库',dataIndex:'depot_id_text',width:120},
			{header:'差异时间',dataIndex:'create_time',width:120},
			{header:'虚拟MODEM型号',dataIndex:'pair_device_modem_model_text',width:100},
			{header:'虚拟MODEM号',dataIndex:'pair_device_modem_code',width:120},
			{header:'配对卡型号',dataIndex:'pair_device_model',width:100},
			{header:'配对卡号',dataIndex:'pair_device_code',width:120}
		];
		
		DifferenceGrid.superclass.constructor.call(this,{
			id:'differenceGridId',
			region:'center',
			border:false,
			ds:this.differenceGridStore,
			columns:columns,
			sm:sm,
			bbar:new Ext.PagingToolbar({store:this.differenceGridStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar:[
				'-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.differenceGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持设备编号查询'
	            }),'-',
	            {xtype:'treecombo',fieldLabel:'仓库',id:'diff_depot_id',
					width:150,
					treeWidth:400,
					height: 22,
					allowBlank: false,
					onlySelectLeaf:false,
					emptyText :'切换仓库',
					blankText:'请选择仓库',
					treeUrl: 'resource/Device!queryChildDepot.action',
					listeners : {
						scope:this,
						'focus' : function(combo){
							if(combo.list){
								combo.expand();
							}
						},
						select:function(tree,node,attr){
							this.differenceGridStore.baseParams['depotId'] = node.id;
							this.differenceGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
						}
					}
				},'-',
				{text:'手工差异',iconCls:'icon-add',scope:this,handler:this.addDifference},'-',
				{text:'文件差异',iconCls:'icon-add',scope:this,tooltip:'文件增加差异：文件类型为Excel，共一列，第一列为差异设备编号' ,handler:this.addFileDifference},'-',
				{text:'确认差异',iconCls:'icon-confirm',scope:this,handler:function(){this.callProcess('confirmDifference',['UNCHECK','NODIFF'],'该设备已差异，无需再确认')}},'-',
				{text:'取消差异',iconCls:'icon-cancel',scope:this,handler:function(){this.callProcess('cancelDifference',['DIFF'],'该设备未差异，不能取消确认')}},'-'
			]
		});
	},
	callProcess:function(methodName,diffenTypeArr,info){
		var records = this.getSelectionModel().getSelections();
		if(records && records.length>0){
			for(var i=0,len=records.length;i<len;i++){
				if(diffenTypeArr.indexOf(records[i].get('diffence_type'))  == -1){
					Alert(info);
					return;
				}
			}
			Confirm('确定操作吗?',this,function(){
				var deviceIds='';
				Ext.each(records,function(r){
					deviceIds = deviceIds.concat(r.get('device_id'),',');
				});
				deviceIds = deviceIds.substring(0,deviceIds.length-1);
				
				this[methodName].call(this,deviceIds);
			});
			
		}else{
			Alert('请选中要操作的行');
		}
	},
	addDifference:function(deviceIds){
		var win = Ext.getCmp('addDifferenceWinId');
		if(!win){
			win = new AddDifferenceWin(this);
		}
		win.show();
	},
	addFileDifference:function(deviceIds){
		var win = Ext.getCmp('fileDifferenceWinId');
		
		if(!win){
			win = new AddFileDifferenceWin(this);
		}
		win.show();
	},
	confirmDifference:function(deviceIds){
		Ext.Ajax.request({
			url:'resource/Device!checkDeviceDiffence.action',
			params:{deviceIds:deviceIds},
			scope:this,
			success:function(res,opt){
				Alert('确认差异成功!',function(){
					this.getStore().reload();
				},this);
			}
		});
	},
	cancelDifference:function(deviceIds){
		Ext.Ajax.request({
			url:'resource/Device!cancelDeviceDiffence.action',
			params:{deviceIds:deviceIds},
			scope:this,
			success:function(res,opt){
				Alert('取消差异成功!',function(){
					this.getStore().reload();
				},this);
			}
		});
	}
});

var DifferenceQueryDeviceGrid = Ext.extend(QueryDeviceGrid,{
	constructor:function(){
		DifferenceQueryDeviceGrid.superclass.constructor.call(this);
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
				url:'resource/Device!queryDeviceDiffecnceInfo.action',
				params:{
					deviceCode:newValue,
					depotId:Ext.getCmp('diff_depot_id').getValue()
				},
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
 * 文件差异
 */
var DiffFileForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		DiffFileForm.superclass.constructor.call(this,{
			border:false,
			region:"center",
			labelWidth: 70,
			layout : 'column',
			fileUpload: true,
			trackResetOnLoad:true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[ {columnWidth:1,layout:'form',
					items:[
						{id:'differenceManageFielId',fieldLabel:'设备文件',name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'80%',emptyText:''},
						 {fieldLabel:'备注',name:'remark',xtype:'textarea',anchor:'80%',height:50},
						{id:'depotId',name:'depotId',xtype:'hidden'}
						 
				]}
			]
		});
	},
	initComponent:function(){
		DiffFileForm.superclass.initComponent.call(this);
	}
});

/**
 * 调拨确认Window
 * @class
 * @extends Ext.Window
 */
var AddDifferenceWin = Ext.extend(Ext.Window,{
	parent:null,
	grid :null,
 	constructor:function(p){
 		this.parent = p ,
		this.grid = new DifferenceQueryDeviceGrid();
 		AddDifferenceWin.superclass.constructor.call(this,{
			id : 'addDifferenceWinId',
			title:'手工增加差异',
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 350,
			layout:'border',
			items:[this.grid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.grid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var store = this.grid.getStore();
		var deviceIds='';
		store.each(function(record){
			if(!Ext.isEmpty(record.get('device_id')))
				deviceIds = deviceIds.concat(record.get('device_id'),',')
		});
		if(deviceIds.length === 0){
			Alert('请正确输入设备信息！');
			return;
		}
		deviceIds = deviceIds.substring(0,deviceIds.length-1);
		
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!addDeviceDiffence.action',
			params:{deviceIds:deviceIds},
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert('添加成功',function(){
					this.hide();
					Ext.getCmp('differenceGridId').getStore().reload();
				},this);
			}
		});
	}
});


/**
 * 文件差异
 * @class
 * @extends Ext.Window
 */
var AddFileDifferenceWin = Ext.extend(Ext.Window,{
	fileForm :null,
 	constructor:function(){
		this.fileForm = new DiffFileForm();
 		AddFileDifferenceWin.superclass.constructor.call(this,{
			id : 'fileDifferenceWinId',
			title:'文件增加差异',
			closeAction:'hide',
			maximizable:false,
			width: 650,
			height: 350,
			layout:'border',
			items:[this.fileForm],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					
 				}
			}
		});
	},
	doSave:function(){
		
		 if(this.fileForm.getForm().isValid()){
			var file = Ext.getCmp('differenceManageFielId').getValue();
			var flag = checkFileType(file);
			if(!flag)return;
 			 Ext.getCmp('depotId').setValue(Ext.getCmp('diff_depot_id').getValue());
 			var msg = Show();
			this.fileForm.getForm().submit({
				url:'resource/Device!addFileDeviceDiffence.action',
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
							Alert('差异文件上传成功!',function(){
								this.hide();
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					alert("差异文件上传失败!");  
				}
			});
		}
	}
});







DifferenceManage = Ext.extend(Ext.Panel,{
	constructor:function(){
		var differenceGrid = new DifferenceGrid();
		queryDeviceGrid = new QueryDeviceGrid();
		DifferenceManage.superclass.constructor.call(this,{
			id:'DifferenceManage',
			title:'差异管理',
			closable: true,
			border : false ,
			layout:'border',
			baseCls: "x-plain",
			items:[differenceGrid]
		});
	}
});


