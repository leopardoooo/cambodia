/**
 * 设备模糊查询
 * @class QueryDevice
 * @extends Ext.Panel
 */
QueryDeviceForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	showDownloadBtn:false,
	constructor:function(p){
		this.parent = p;
		QueryDeviceForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			layout:'column',
			labelWidth:75,
			items:[
				{columnWidth:.33,layout:'form',border:false,items:[
					{xtype:'treecombo',fieldLabel:'仓库',hiddenName:'depotId',
						width:150,
						treeWidth:400,
//						minChars:2,
						height: 22,
						allowBlank: false,
						onlySelectLeaf:false,
						emptyText :'请选择仓库',
						blankText:'请选择仓库',
						treeUrl: 'resource/Device!queryChildDepot.action',
						listeners : {
							'focus' : function(){
								if(this.list){
									this.expand();
								}
							}
						}
					},
					{fieldLabel:'设备状态',hiddenName:'status',xtype:'paramcombo',width:150,
						forceSelection:true,selectOnFocus:true,editable:true,
						paramName:'DEVICE_STATUS_R_DEVICE'
					},
					{fieldLabel:'备机',hiddenName:'backup',xtype:'paramcombo',width:150,
						forceSelection:true,selectOnFocus:true,editable:true,
						paramName:'BOOLEAN'
					}
				]},
				{columnWidth:.33,layout:'form',border:false,defaultType:'textfield',items:[
					{fieldLabel:'设备类型',hiddenName:'mode',xtype:'combo',allowBlank:false,
						store:new Ext.data.ArrayStore({
							fields:['mode','mode_name'],
							data:[['STB','单机顶盒'],['CARD','单智能卡'],
								['MODEM','单MODEM'],['STBCARD','机卡配对'],['STBMODEM','机MODEM配对']]
						}),
						displayField:'mode_name',valueField:'mode',width:150,
						listeners:{
							scope:this,
							select:this.doSelect
						}
					},
					{fieldLabel:'设备型号',hiddenName:'deviceModel',
						xtype:'lovcombo',forceSelection:true,selectOnFocus:true,editable:true,
						store:new Ext.data.JsonStore({
							url : root + '/resource/Device!queryDeviceModelByType.action',
							fields:['device_model','model_name','modem_type']
						}),displayField:'model_name',valueField:'device_model',
						triggerAction:'all',mode:'local',width:150,listWidth:150,
						beforeBlur:function(){}
					},
					{fieldLabel:'设备批号',name:'batch_num',
						xtype:'textfield',
						width: 150
					}
				]},
				{columnWidth:.33,layout:'form',border:false,items:[
					{fieldLabel:'MODEM类型',hiddenName:'modemType',xtype:'paramcombo',width:150,
						disabled : true,
						paramName:'MODEM_TYPE',
						listeners:{
							scope:this,
							select:function(combo,record){
								var deviceModel = this.getForm().findField('deviceModel');
								deviceModel.getStore().load({
									params:{deviceType:'MODEM',modemType : combo.getValue()}
								});
							}
						}
					},
					{fieldLabel:'库存状态',hiddenName:'depotStatus',xtype:'paramcombo',width:150,
//						forceSelection:true,selectOnFocus:true,editable:true,
						paramName:'DEPOT_STATUS_R_DEVICE',
						listeners:{
							scope:this,
							select:function(combo,record){
								var value = combo.getValue();
								var btn = Ext.getCmp('downloadBtnId');
								//正常，损坏 ，提供excel下载
								if(value == 'USE'){
									if(!btn.hidden)
										btn.hide();
									
								}else{
									if(btn.hidden && this.showDownloadBtn === true)
										btn.show();
								}
							}
						}
					}
				]},{columnWidth:1,layout:'form',border:false,items:[{	
						fieldLabel:'入库时间',
					    xtype:'compositefield',combineErrors:false,
					    items: [
					        {xtype:'datefield',name:'start_input_time',style:'width:135px;height:22px',format:'Y-m-d'},
					        {xtype:'displayfield',value:'至'},
					        {xtype:'datefield',name:'end_input_time',style:'width:135px;height:22px',format:'Y-m-d'}
				    	]
					}]
				}
				
			],
			buttonAlign : 'center',
			buttons : [{id:'queryDeviceBtnId',xtype:'button',text:'查  询',iconCls:'icon-query',disableSelfCtrl:true,
						scope:this,handler:this.doQuery},
					{id:'downloadBtnId',xtype:'button',hidden:true,text:'下  载',iconCls:'icon-excel',
						scope:this,handler:this.doDownload}]
		});
	},
	initComponent:function(){
		QueryDeviceForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	doSelect:function(combo,record){
		var value = combo.getValue();
		var modemType = this.getForm().findField('modemType');
		if(value == 'STBCARD' || value == 'STBMODEM'){
			value='STB';
			modemType.reset();
			modemType.disable();
		}else if (value == 'MODEM'){
			modemType.enable();
		}else{
			modemType.reset();
			modemType.disable();
		}
		
		
		var deviceModel = this.getForm().findField('deviceModel');
		deviceModel.reset();
		deviceModel.getStore().load({params:{deviceType:value}});
	},
	doQuery:function(){
		if(!this.getForm().isValid())return;
		Ext.getCmp('queryDeviceBtnId').disable();//禁用查询按钮，数据加载完再激活
		var grid = this.parent.grid;
		grid.getStore().removeAll();
		
		var values = this.getForm().getValues();
		var mode = values['mode'];
		var columns = grid.currColumns;
		if(mode == 'STB' || mode == 'STBCARD'){//单机或机卡配对
			columns = grid.stbColumns;
		}else if(mode == 'CARD'){//单卡
			columns = grid.cardColumns;
		}else if(mode == 'MODEM'){//猫
			columns = grid.modemColumns;
		}
		var store = grid.getStore();
		
		if(grid.currColumns != columns){
			grid.reconfigure(store,new Ext.grid.ColumnModel(columns));
			grid.currColumns = columns;
		}
		
		store.baseParams = values;
		grid.getEl().mask();
		store.load({params: { start: 0, limit: 25 }});
	},
	doDownload:function(){
		var count = this.parent.grid.getStore().getCount();
		if(count == 0){
			Alert('请先查询出数据，方能下载!');
			return ;
		}
		var values = Ext.Ajax.serializeForm(this.getForm().getEl().dom);
		window.open(root+'/resource/Device!downloadQueryDevice.action?'+values);
	}
});

QueryDeviceGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	
	stbColumns:null,
	cardColumns:null,
	modemColumns:null,
	currColumns:null,
	constructor:function(cfg){
		Ext.apply(this, cfg || {});
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Device!queryDeviceByMultiCriteria.action',
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['device_id','device_status','device_status_text','device_code',
				'device_model','device_model_text','modem_mac','pair_device_code',
				'pair_device_model','pair_device_model_text','depot_status_text', 'batch_num',
				'depot_id','depot_id_text','cust_id','cust_name','pair_device_modem_code','pair_device_modem_model_text']
		});
		this.store.on('load',function(){
			Ext.getCmp('queryDeviceBtnId').enable();
			this.getEl().unmask();
		},this);
		this.stbColumns = [
			{header:'机顶盒号',dataIndex:'device_code',width:130,renderer:App.qtipValue},
			{header:'盒类型',dataIndex:'device_model_text',width:100,renderer:App.qtipValue},
			{header:'智能卡号',dataIndex:'pair_device_code',width:120,renderer:App.qtipValue},
			{header:'卡类型',dataIndex:'pair_device_model_text',width:100,renderer:App.qtipValue},
			{header:'配对MODEM',dataIndex:'pair_device_modem_code',width:120,renderer:App.qtipValue},
			{header:'配对MODEM类型',dataIndex:'pair_device_modem_model_text',width:100,renderer:App.qtipValue},
			{header:'设备状态',dataIndex:'device_status_text',width:75,renderer:App.qtipValue},
			{header:'库存状态',dataIndex:'depot_status_text',width:75,renderer:App.qtipValue},
			{header:'所在仓库',dataIndex:'depot_id_text',width:100,renderer:App.qtipValue},
			{header:'客户编号',dataIndex:'cust_id',width:75,renderer:App.qtipValue},
			{header:'客户名称',dataIndex:'cust_name',width:100,renderer:App.qtipValue},
			{header:'批号',dataIndex:'batch_num',width:100,renderer:App.qtipValue}
			
		];
		this.cardColumns = [
			{header:'智能卡号',dataIndex:'device_code',width:160,renderer:App.qtipValue},
			{header:'类型',dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:'设备状态',dataIndex:'device_status_text',width:80,renderer:App.qtipValue},
			{header:'库存状态',dataIndex:'depot_status_text',width:75,renderer:App.qtipValue},
			{header:'所在仓库',dataIndex:'depot_id_text',width:130,renderer:App.qtipValue},
			{header:'客户编号',dataIndex:'cust_id',width:120,renderer:App.qtipValue},
			{header:'客户名称',dataIndex:'cust_name',width:130,renderer:App.qtipValue}
		];
		this.modemColumns = [
			{header:'MODEM号',dataIndex:'modem_mac',width:160,renderer:App.qtipValue},
			{header:'类型',dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:'设备状态',dataIndex:'device_status_text',width:80,renderer:App.qtipValue},
			{header:'库存状态',dataIndex:'depot_status_text',width:75,renderer:App.qtipValue},
			{header:'所在仓库',dataIndex:'depot_id_text',width:130,renderer:App.qtipValue},
			{header:'客户编号',dataIndex:'cust_id',width:120,renderer:App.qtipValue},
			{header:'客户名称',dataIndex:'cust_name',width:130,renderer:App.qtipValue}
		];
		this.currColumns = this.stbColumns;
		QueryDeviceGrid.superclass.constructor.call(this,{
			title:'设备信息',
			border:false,
			ds:this.store,
			columns:this.stbColumns,
			bbar: new Ext.PagingToolbar({store: this.store })
		});
	}
});

QueryDevice = Ext.extend(Ext.Panel,{
	grid:null,
	form:null,
	constructor:function(){
		this.form = new QueryDeviceForm(this);
		this.grid = new QueryDeviceGrid();
		QueryDevice.superclass.constructor.call(this,{
			id:'QueryDevice',
			title:'设备查询',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'anchor',
			items:[
				{anchor:'100% 27%',layout:'fit',border:false,items:[this.form]},
				{anchor:'100% 73%',layout:'fit',border:false,items:[this.grid]}
			]
		});
		var resources = App.subMenu;
		for(var i=0,len=resources.length;i<len;i++){
			var res = resources[i];
			if(res['sub_system_id'] == '5' && res['handler'] == 'device_download_btn_id'){
				this.form.showDownloadBtn = true;
			}
		}
	}
});

