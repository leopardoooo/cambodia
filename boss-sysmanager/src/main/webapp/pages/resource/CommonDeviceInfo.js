/**
 * 设备明细(入库)
 * 调用时,声明的变量必须唯一
 * @class
 * @extends Ext.grid.GridPanel
 */
var DeviceGrid = Ext.extend(Ext.grid.GridPanel,{
	deviceGridStore :null,
	constructor:function(cfg){
		Ext.apply(this,cfg);
		this.deviceGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceDoneDetail.action',
			fields:['device_done_code','device_type','device_model','count','device_model_text','device_type_text']
		});
		
		var columns = [
			{header:'设备类型',dataIndex:'device_type_text',width:120},
			{header:'型号',dataIndex:'device_model_text',width:120},
			{header:'数量',dataIndex:'count',width:120}
		];
		DeviceGrid.superclass.constructor.call(this,{
			title:'设备明细',
			autoDestory:false,
			region:'south',
			height:150,
			border:false,
			ds:this.deviceGridStore,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel()
		});
	}
});

/**
 * 通过设备编号查询设备信息
 * @class
 * @extends Ext.grid.EditorGridPanel
 */
var QueryDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	queryDeviceGridStore:null,
	deviceTypeCombo :null,
	deviceModelCombo :null,
	cardModelCombo :null,
	constructor:function(){
		var that = this;
		this.queryDeviceGridStore = new Ext.data.JsonStore({
			fields:['device_id','device_type','device_type_text','device_model','device_model_text',
				'modem_mac','device_code','pair_device_model','pair_device_model_text','pair_device_code',
				'pair_card_id','pair_modem_id','pair_device_modem_model_text','pair_device_modem_code']
		});
		
		doDel = function(){
			Confirm('确定删除吗?',this,function(){
				that.getStore().remove(that.getSelectionModel().getSelected());
			});
		};
		
		var columns = [
			{header:'编号(Modem_mac)',dataIndex:'device_code',width:150},//,editor:deviceCodeComp
			{header:'类型',dataIndex:'device_type_text',width:70},
			{header:'型号',dataIndex:'device_model_text',width:90},
			{header:'modem_mac',dataIndex:'modem_mac',width:75,hidden:true},
			{header:'配对卡型号',dataIndex:'pair_device_model_text',width:90},
			{header:'配对卡号',dataIndex:'pair_device_code',width:120},
			{header:'配对MODEM型号',dataIndex:'pair_device_modem_model_text',width:90},
			{header:'配对MODEM号',dataIndex:'pair_device_modem_code',width:120},
			{header:'操作',dataIndex:'',width:40,renderer:function(value,metavalue,record,i){
				return "<a href='#' onclick=doDel()>删除</a>";
			}}
		];
		
		QueryDeviceGrid.superclass.constructor.call(this,{
			title:'设备信息',
			region:'center',
			ds:this.queryDeviceGridStore,
			clicksToEdit:1,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[{text:'添加',iconCls:'icon-add',scope:this,handler:this.doAdd}]
		});
	},
	queryAndAddDevice:function(field,e){
		if(e.getKey() == Ext.EventObject.ENTER){
			Ext.Ajax.request({
				url:'resource/Device!queryDeviceInfo.action',
				params:{deviceCode:field.getValue()},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data.device_id){
						var record = this.getSelectionModel().getSelected();
						record.fields.each(function(field){
							record.set(field.name,data[field.name]);
						},this);
						this.doAdd();
					}
				}
			});
		}
	},
	doAdd:function(){
		var count = this.getStore().getCount();
		var recordType = this.getStore().recordType;
		var record = new recordType({
			device_id:'',device_type_text:'',device_type:'',device_model:'',
			modem_mac:'',device_code:'',pair_device_model:'',pair_device_code:''
		});
		this.stopEditing();
		this.getStore().add(record);
		this.startEditing(count,0);
		this.getSelectionModel().selectRow(count);
//		this.getStore().commitChanges();
	}
});

var DeviceDetailGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(cfg){
		Ext.apply(this,cfg);
		this.deviceDetailGridStore = new Ext.data.JsonStore({
			totalProperty:'totalProperty',
			root:'records',
			fields : ['device_done_code', 'device_type', 'device_type_text','device_code',
				'device_model','device_model_text','depot_id','depot_id_text','county_id','county_id_text']
		});
		
		var columns = [
			{header:'设备编号',dataIndex:'device_code',width:150,renderer:App.qtipValue},
			{header:'所在仓库',dataIndex:'depot_id_text',width:120},
			{header:'所在县市',dataIndex:'county_id_text',width:120}
		];
		DeviceGrid.superclass.constructor.call(this,{
			title:'操作明细',
			autoDestory:false,
			region:'south',
			height:250,
			border:false,
			store:this.deviceDetailGridStore,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel(),
			bbar:new Ext.PagingToolbar({store:this.deviceDetailGridStore,pageSize:Constant.DEFAULT_PAGE_SIZE})
		});
	}
});

//设备类型
var deviceType = {id:'device_type_id',fieldLabel:'设备类型',xtype:'paramcombo',
		typeAhead:false,paramName:'DEVICE_TYPE',hiddenName:'deviceInput.device_type'
};

var checkFileType = function(fileText){
	if(fileText.lastIndexOf('xlsx')>0 || fileText.lastIndexOf('.xlsx')==fileText.length-5){
		Alert('请选择excel2003文件进行上传,文件后缀名为.xls!');
		return false;
	}else if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
		Alert('请选择excel文件进行上传！');
		return false;
	}
	return true;
}

/**
 * 清空上传文件内容
 * @param {} fileComp 上传组件
 */
var resetFileCompContent = function(fileComp){
	fileComp.getEl().dom.select();
	document.execCommand("delete");
};