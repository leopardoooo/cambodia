/**
*设备挂失
*/

//列出当前仓库中所有空闲设备信息
var DeviceLossGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(){
		this.lossStore = new Ext.data.JsonStore({
			url:root+'/resource/Device!queryIdleDevice.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['device_id','device_code','device_type_text',
				'device_model_text','is_loss','depot_id_text',
				'pair_device_model_text','pair_device_code','pair_device_modem_model_text','pair_device_modem_code']
		});
		this.lossStore.load({
			params:{
				start:0,limit:Constant.DEFAULT_PAGE_SIZE
			}
		});
		var columns = [
			{header:'设备编号',dataIndex:'device_code',width:120,renderer:App.qtipValue},
			{header:'设备类型',dataIndex:'device_type_text',width:80},
			{header:'设备型号',dataIndex:'device_model_text',width:100,renderer:App.qtipValue},
			{header:'所属仓库',dataIndex:'depot_id_text',width:100},
			{header:'虚拟MODEM号',dataIndex:'pair_device_modem_code',width:120},
			{header:'虚拟MODEM型号',dataIndex:'pair_device_modem_model_text',width:100,renderer:App.qtipValue},
			{header:'配对卡号',dataIndex:'pair_device_code',width:120},
			{header:'配对卡型号',dataIndex:'pair_device_model',width:100,renderer:App.qtipValue},
			{header:'操作',dataIndex:'',renderer:function(){
					return '<a href="#" onclick=Ext.getCmp("deviceLossGridId").cancelLossDevice()>取消挂失</a>';
				}
			}
		];
		DeviceLossGrid.superclass.constructor.call(this,{
			id:'deviceLossGridId',
			title:'设备挂失',
			border:false,
			store:this.lossStore,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel(),
			bbar:new Ext.PagingToolbar({store:this.lossStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar:[
				'-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.lossStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持设备编号查询'
	            }),'-','->',
	            {text:'添加',iconCls:'icon-add',scope:this,handler:this.doAdd}
			]
		});
	},
	doAdd:function(){
		var win = Ext.getCmp('deviceLossWinId');
		if(!win){
			win = new DeviceLossWin();
		}
		win.show();
	},
	cancelLossDevice:function(){
		Confirm('确定吗?',this,function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Device!updateDeviceLoss.action',
				params:{
					deviceId:record.get('device_id'),
					isLossed: 'F'
				},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						this.getStore().remove(record);
						this.getStore().commitChanges();
					}
				}
			});
		});
	}
});

var DeviceLossWin = Ext.extend(Ext.Window,{
	constructor:function(){
		DeviceLossWin.superclass.constructor.call(this,{
			id:'deviceLossWinId',
			title:'添加设备挂失',
			width:350,
			height:120,
			layout:'fit',
			items:[{
				id:'deviceLossFormId',xtype:'form',border:false,bodyStyle:'padding-top:10px',items:[
					{fieldLabel:'设备编号',name:'deviceCode',xtype:'textfield',allowBlank:false,anchor:'90%'}
				]
			}],
			buttonAlign:'center',
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
					Ext.getCmp('deviceLossFormId').getForm().reset();
				}
			}
		});
	},
	doSave:function(){
		var form = Ext.getCmp('deviceLossFormId').getForm();
		if(form.isValid()){
			var values = form.getValues();
			Ext.Ajax.request({
				url:root+'/resource/Device!saveLossDevice.action',
				params:values,
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						this.hide();
						Ext.getCmp('deviceLossGridId').getStore().reload();
					}
				}
			});
		}
	}
});

DeviceLoss = Ext.extend(Ext.Panel,{
	constructor:function(){
		var grid = new DeviceLossGrid();
		DeviceLoss.superclass.constructor.call(this,{
			id:'DeviceLoss',
			title:'设备挂失',
			closable: true,
			border : false ,
			layout:'fit',
			items:[grid]
		});
	}
});




