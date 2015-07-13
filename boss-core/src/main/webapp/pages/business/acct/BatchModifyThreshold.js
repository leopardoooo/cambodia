/**
*批量阈值修改
*/

var ThresholdGrid = Ext.extend(Ext.grid.GridPanel,{
	thresholdStore: null,
	loadCount: 1,
	constructor:function(){
		this.thresholdStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Acct!queryThresholdByCustId.action",
			fields:['acct_id','acctitem_id','acctitem_name','threshold','base_threshold',
				'temp_threshold','task_code_text','task_code','status',
				'user_type_text','user_name','card_id','stb_id','modem_mac',
				'stop_type','user_class','status'],
			pruneModifiedRecords:true,
			sortInfo : {
				field : 'user_name',
				direction:'DESC'
			}
		});
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid.getSelectionModel().getSelections();
		var acctIds = [];
		Ext.each(records,function(r){
			acctIds.push(r.get('acct_id'));
		});
		this.thresholdStore.baseParams = {
			custId: App.getData().custFullInfo.cust['cust_id'],
			acctIds: acctIds
		};
		this.thresholdStore.load();
		this.thresholdStore.on('load',this.doStoreLoad,this);
		var cm = new Ext.grid.ColumnModel({
			columns:[
				{header:'账目名称',dataIndex:'acctitem_name',width:100,renderer:App.qtipValue},
				{header:'用户类型',dataIndex:'user_type_text',width:60},
				{header:'用户名',dataIndex:'user_name',width:70,renderer:App.qtipValue},
				{header:'机顶盒',dataIndex:'stb_id',width:115,renderer:App.qtipValue},
				{header:'卡号',dataIndex:'card_id',width:110,renderer:App.qtipValue},
				{header:'modem',dataIndex:'modem_mac',width:60,renderer:App.qtipValue},
				{header:'任务类别',dataIndex:'task_code_text',width:55},
				{header:'阈值',dataIndex:'threshold',width:45,renderer : Ext.util.Format.formatFee},
				{header:'基准阈值',dataIndex:'base_threshold',width:55,renderer : Ext.util.Format.formatFee},
				{header:'临时阈值',dataIndex:'temp_threshold',width:60,renderer : Ext.util.Format.formatFee}
			]
		});
		ThresholdGrid.superclass.constructor.call(this,{
			id:'thresholdGridId',
			region:'center',
			store:this.thresholdStore,
			cm:cm
		});
		
	},
	doStoreLoad: function(){
		this.getEl().unmask();
		this.getStore().each(function(record){
			var status = record.get('status');
			//客户状态资料隔离，用户状态 休眠和关模隔离 都不能修改阈值 ,待销户
			if(status == 'DATACLOSE' || status == 'DORMANCY' || status == 'ATVCLOSE' || status == 'WAITLOGOFF' ){
				this.remove(record);
			}
		},this);

		//第一次加载
		if(this.loadCount == 1){
			var arr = [];
			arr.push({
				'acctitem_id': '',
				'acctitem_name': '所有账目'
			});
			this.getStore().each(function(record){
				var flag = true;
				for(var i=0,len=arr.length;i<len;i++){
					if(arr[i]['acctitem_id'] == record.get('acctitem_id')){
						flag = false;
						break;
					}
				}
				if(flag === true){
					arr.push({
						'acctitem_id': record.get('acctitem_id'),
						'acctitem_name': record.get('acctitem_name')
					});
				}
			});
			Ext.getCmp('acctitem_id').getStore().loadData(arr);
		}
		this.loadCount++;
	},
	getValues: function(){
		var arr = [],data;
		this.store.each(function(record){
			data = record.data;
			var obj = {};
			obj['old_value'] = data['temp_threshold'];
			obj['column_name'] = 'temp_threshold';
			obj['acct_id'] = data['acct_id'];
			obj['acctitem_id'] = data['acctitem_id'];
			obj['task_code'] = data['task_code'];
			arr.push(obj);
		},this);
		return arr;
	}
});

var ThresholdPanel = Ext.extend(Ext.Panel,{
	constructor: function(){
		ThresholdPanel.superclass.constructor.call(this,{
			region:'south',
			height:150,
			layout:'column',
			labelWidth:75,
			bodyStyle:'padding-top:10px',
			defaults:{
				layout : 'form',
				border :false,
				bodyStyle: "background:#F9F9F9",
				columnWidth : .5
			},
			items:[
				{items:[{
						id:'acctitem_id',xtype:'combo',fieldLabel:'账目名称',
						store:new Ext.data.JsonStore({
							fields:['acctitem_id','acctitem_name']
						}),
						displayField:'acctitem_name',valueField:'acctitem_id',
						mode:'local',triggerAction:'all',forceSelection:true,editable:true,
						listeners:{
							scope:this,
							select:this.doFilter
						}
					},
					{
						id:'stop_type',xtype:'paramcombo',fieldLabel:'催停标识',paramName:'STOP_TYPE',
						forceSelection:true,editable:true,
						listeners:{
							scope:this,
							select:this.doFilter
						}
					},
					{
						id:'user_class',xtype:'paramcombo',fieldLabel:'用户等级',paramName:'USER_CLASS',
						forceSelection:true,editable:true,
						listeners:{
							scope:this,
							select:this.doFilter
						}
					}
				]},
				{items:[{
						id:'task_code',xtype:'paramcombo',fieldLabel:'任务类型',paramName:'JOB_TASK_CODE',
						forceSelection:true,editable:true,
						listeners:{
							scope:this,
							select:this.doFilter
						}
					},
					{
						id:'status',xtype:'paramcombo',fieldLabel:'用户状态',paramName:'STATUS_C_USER',
						forceSelection:true,editable:true,
						listeners:{
							scope:this,
							select:this.doFilter
						}
					},
					{
						id:'temp_threshold_id',xtype:'numberfield',fieldLabel:'新临时阈值',name:'temp_threshold'
					}
				]}
			]
		});
	},
	doFilter: function(){
		var store = Ext.getCmp('thresholdGridId').getStore();
		
		var acctitem_id = Ext.getCmp('acctitem_id').getValue();
		var stop_type = Ext.getCmp('stop_type').getValue();
		var user_class = Ext.getCmp('user_class').getValue();
		var task_code = Ext.getCmp('task_code').getValue();
		var status = Ext.getCmp('status').getValue();
		
		var obj = {};
		obj["queryAcctitemThresholdDto.acctitem_id"] = acctitem_id;
		obj["queryAcctitemThresholdDto.task_code"] = task_code;
		obj["queryAcctitemThresholdDto.user_stop_type"] = stop_type;
		obj["queryAcctitemThresholdDto.user_status"] = status;
		obj["queryAcctitemThresholdDto.user_class"] = user_class;
		
		Ext.getCmp('thresholdGridId').getEl().mask();
		store.load({
			params:	obj
		});
	},
	getValues: function(){
		return Ext.getCmp('temp_threshold_id').getValue();
	}
});

var ThresholdForm = Ext.extend(BaseForm,{
	grid:null,
	data:[],
	url:Constant.ROOT_PATH+"/core/x/Acct!updateThreshold.action",
	constructor:function(){
		this.panel = new ThresholdPanel();
		this.grid = new ThresholdGrid();
		ThresholdForm.superclass.constructor.call(this,{
			border:false,
			layout:'border',
			items:[this.grid,this.panel]
		});
	},
	doInit: function(){
		var ItemRecord = Ext.data.Record.create(['item_name','item_value']);
		var record = new ItemRecord({
				'item_name':'查询所有',
				'item_value':''
		});
		
		Ext.getCmp('stop_type').getStore().insert(0,record);
		Ext.getCmp('user_class').getStore().insert(0,record);
		Ext.getCmp('task_code').getStore().insert(0,record);
		Ext.getCmp('status').getStore().insert(0,record);
	},
	doValid: function(){
		this.grid.stopEditing();
		var count = this.grid.getStore().getCount();
		if(count == 0){
			var obj = {};
			obj['msg'] = '没有可修改的数据!';
			obj['isValid'] = false;
			return obj;
		}
		if(Ext.isEmpty(this.panel.getValues())){
			var obj = {};
			obj['msg'] = '请输入新的临时阈值!';
			obj['isValid'] = false;
			return obj;
		}
		return ThresholdForm.superclass.doValid.call(this);
	},
	getValues: function(){
		var values = this.grid.getValues();
		var tempValue = this.panel.getValues();
		Ext.each(values,function(v){
			v['new_value'] = parseInt(tempValue * 100);
		});
		
		return {'thresholdListStr':Ext.encode(values)};
	}
});

Ext.onReady(function(){
	var form = new ThresholdForm();
	TemplateFactory.gTemplate(form);
});















