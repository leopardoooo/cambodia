/**
 * 设备模糊查询
 * @class QueryDevice
 * @extends Ext.Panel
 */
var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var QD_LU = lsys('QueryDevice');
var MSG_LU = lsys('msgBox');

QueryDeviceForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	showDownloadBtn:false,
	checkDeviceType:null,
	constructor:function(p){
		this.parent = p;
		QueryDeviceForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			layout:'column',
			labelWidth:120,
			items:[
				{columnWidth:.33,layout:'form',border:false,items:[
					{fieldLabel:DEV_COMMON_LU.labelDepot,hiddenName:'depotId',xtype:'combo',allowBlank:false,
							store:new Ext.data.JsonStore({
								url:'resource/Device!queryAllDept.action',
								autoLoad:true,
								fields:['dept_id','dept_name']
							}),displayField:'dept_name',valueField:'dept_id',triggerAction:'all',mode:'local',width :150,minListWidth :250
					},
					{fieldLabel:DEV_COMMON_LU.labelDevStatus,hiddenName:'status',xtype:'paramcombo',width:150,
						forceSelection:true,selectOnFocus:true,editable:true,
						paramName:'DEVICE_STATUS_R_DEVICE'
					},
					{fieldLabel:DEV_COMMON_LU.labelBackUp,hiddenName:'backup',xtype:'paramcombo',width:150,
						forceSelection:true,selectOnFocus:true,editable:true,
						paramName:'BOOLEAN'
					}
				]},
				{columnWidth:.33,layout:'form',border:false,defaultType:'textfield',items:[
					{fieldLabel:DEV_COMMON_LU.labelDeviceType,hiddenName:'mode',xtype:'combo',allowBlank:false,
						store:new Ext.data.ArrayStore({
							fields:['mode','mode_name'],
							data:[['STB','STB'],['FITTING','FITTING'],
//							['STBCARDMODEM',DEV_COMMON_LU.labelStbCardModemPair],['STBCARD',DEV_COMMON_LU.labelStbCardPair],['STBMODEM',DEV_COMMON_LU.labelStbModemPair],
								['MODEM','MODEM']]
						}),
						displayField:'mode_name',valueField:'mode',width:150,
						listeners:{
							scope:this,
							select:this.doSelect
						}
					},
					{fieldLabel:DEV_COMMON_LU.labelDeviceModel,hiddenName:'deviceModel',
						xtype:'lovcombo',forceSelection:true,selectOnFocus:true,editable:true,
						store:new Ext.data.JsonStore({
							url : root + '/resource/Device!queryDeviceModelByType.action',
							fields:['device_model','model_name','modem_type']
						}),displayField:'model_name',valueField:'device_model',
						triggerAction:'all',mode:'local',width:150,listWidth:150,
						beforeBlur:function(){}
					},
					{fieldLabel:DEV_COMMON_LU.labelDevBatchNum,name:'batch_num',
						xtype:'textfield',
						width: 150
					}
				]},
				{columnWidth:.33,layout:'form',border:false,items:[
					{fieldLabel:DEV_COMMON_LU.labelModemType,hiddenName:'modemType',xtype:'paramcombo',width:150,
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
					{fieldLabel:DEV_COMMON_LU.labelDepotStatus,hiddenName:'depotStatus',xtype:'paramcombo',width:150,
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
//									if(btn.hidden && this.showDownloadBtn === true)
										btn.show();
								}
							}
						}
					}
				]},{columnWidth:1,layout:'form',border:false,items:[{	
						fieldLabel:DEV_COMMON_LU.labelRecordTime,
					    xtype:'compositefield',combineErrors:false,
					    items: [
					        {xtype:'datefield',name:'start_input_time',style:'width:135px;height:22px',format:'Y-m-d'},
					        {xtype:'displayfield',value:COMMON_LU.to},
					        {xtype:'datefield',name:'end_input_time',style:'width:135px;height:22px',format:'Y-m-d'}
				    	]
					}]
				}
				
			],
			buttonAlign : 'center',
			buttons : [{id:'queryDeviceBtnId',xtype:'button',text:COMMON_LU.query,iconCls:'icon-query',disableSelfCtrl:true,
						scope:this,handler:this.doQuery},
					{id:'downloadBtnId',xtype:'button',text:COMMON_LU.downLoad,iconCls:'icon-excel',
						scope:this,handler:this.doDownload}]
		});
	},
	initComponent:function(){
		QueryDeviceForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	doSelect:function(combo,record){
		var value = combo.getValue();
		if(this.checkDeviceType == null){
			this.checkDeviceType = value;
		}else{
			if(this.checkDeviceType == value){
				return;
			}
		}
		this.checkDeviceType = value;
		if(this.checkDeviceType == 'FITTING'){
			this.getForm().items.each(function(f){
				if(f.label){
					Ext.fly(f.label.id).setStyle('color','gray');
				}
				if(f.hiddenName != 'depotId' && f.hiddenName != 'mode' ){
					f.disable();
				}
			});
		}else{
			this.getForm().items.each(function(f){
				if(f.label){
					Ext.fly(f.label.id).setStyle('color','gray');
				}
				f.enable();
			});
		}
		
		
		var modemType = this.getForm().findField('modemType');
		if (value == 'MODEM'){
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
		if(mode == 'STB'){//单机或机卡配对
			columns = grid.stbColumns;
		}else if(mode == 'FITTING'){//器材
			columns = grid.fittingColumns;
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
			Alert(MSG_LU.tipQueryDateBeforeDownLoad);
			return ;
		}
		var values = Ext.Ajax.serializeForm(this.getForm().getEl().dom);
		window.open(root+'/resource/Device!downloadQueryDeviceDetail.action?'+values);
	}
});

QueryDeviceGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	
	stbColumns:null,
	fittingColumns:null,
	modemColumns:null,
	currColumns:null,
	constructor:function(cfg){
		Ext.apply(this, cfg || {});
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Device!queryDeviceByMultiCriteria.action',
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['device_id','device_status','device_status_text','device_code',
				'device_model','device_model_text','modem_mac','pair_device_code','total_num',
				'pair_device_model','pair_device_model_text','depot_status_text', 'batch_num','develop_optr_name',
				'depot_id','depot_id_text','cust_id','cust_name','pair_device_modem_code','pair_device_modem_model_text']
		});
		this.store.on('load',function(){
			Ext.getCmp('queryDeviceBtnId').enable();
			this.getEl().unmask();
		},this);
		this.stbColumns = [
			{header:DEV_COMMON_LU.labelStbCode,dataIndex:'device_code',width:130,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelStbType,dataIndex:'device_model_text',width:100,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelPairCardCode,dataIndex:'pair_device_code',width:120,renderer:App.qtipValue},
//			{header:DEV_COMMON_LU.labelCardType,dataIndex:'pair_device_model_text',width:100,renderer:App.qtipValue},
//			{header:DEV_COMMON_LU.labelPairModemCode,dataIndex:'modem_mac',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevStatus,dataIndex:'device_status_text',width:75,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevStatus,dataIndex:'depot_status_text',width:75,renderer:App.qtipValue},
			{header:COMMON_LU.depotText,dataIndex:'depot_id_text',width:100,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustNo,dataIndex:'cust_id',width:75,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustName,dataIndex:'cust_name',width:100,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustStr9,dataIndex:'develop_optr_name',width:100,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelBatchNum,dataIndex:'batch_num',width:80,renderer:App.qtipValue}
			
		];
		this.fittingColumns = [
			{header:DEV_COMMON_LU.labelFittingModel,dataIndex:'device_model_text',width:400,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelTotalNum,dataIndex:'total_num',width:120,renderer:App.qtipValue}
		];
		this.modemColumns = [
			{header:DEV_COMMON_LU.labelModemCode,dataIndex:'modem_mac',width:160,renderer:App.qtipValue},
			{header:COMMON_LU.typeSimple,dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevStatus,dataIndex:'device_status_text',width:80,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevStatus,dataIndex:'depot_status_text',width:75,renderer:App.qtipValue},
			{header:COMMON_LU.depotText,dataIndex:'depot_id_text',width:130,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustNo,dataIndex:'cust_id',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustName,dataIndex:'cust_name',width:130,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelCustStr9,dataIndex:'develop_optr_name',width:100,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelBatchNum,dataIndex:'batch_num',width:80,renderer:App.qtipValue}
		];
		this.currColumns = this.stbColumns;
		QueryDeviceGrid.superclass.constructor.call(this,{
//			title:DEV_COMMON_LU.titleDeviceInfo,
			border:false,
			ds:this.store,
			columns:this.currColumns,
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
			title:QD_LU._title,
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

