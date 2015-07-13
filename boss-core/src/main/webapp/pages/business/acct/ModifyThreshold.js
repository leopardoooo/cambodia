/**
*阈值修改
*/

var ThresholdGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	thresholdStore: null,
	constructor:function(){
		this.thresholdStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Acct!queryThresholdByAcctId.action",
			fields:['acct_id','acctitem_id','acctitem_name','threshold','base_threshold',
				'temp_threshold','task_code_text','task_code','status',
				'user_type_text','user_name','card_id','stb_id','modem_mac'],
			pruneModifiedRecords:true,
			sortInfo : {
				field : 'user_name',
				direction:'DESC'
			}
		});
		this.thresholdStore.on('load',function(){
			this.each(function(record){
				var status = record.get('status');
				//客户状态资料隔离，用户状态 休眠和关模隔离 都不能修改阈值  ,待销户
				if(status == 'DATACLOSE' || status == 'DORMANCY' || status == 'ATVCLOSE' || status == 'WAITLOGOFF'){
					this.remove(record);
				}
				record.set('temp_threshold',Ext.util.Format.formatFee(record.get('temp_threshold')));
				record.commit();
			},this);
		},this.thresholdStore);
		var cm = new Ext.grid.ColumnModel({
			columns:[
				{header:'账目名称',dataIndex:'acctitem_name',width:90,renderer:App.qtipValue},
				{header:'用户类型',dataIndex:'user_type_text',width:60},
				{header:'用户名',dataIndex:'user_name',width:50},
				{header:'机顶盒',dataIndex:'stb_id',width:115,renderer:App.qtipValue},
				{header:'卡号',dataIndex:'card_id',width:110,renderer:App.qtipValue},
				{header:'modem',dataIndex:'modem_mac',width:60},
				{header:'任务类别',dataIndex:'task_code_text',width:55},
				{header:'阈值',dataIndex:'threshold',width:55,renderer : Ext.util.Format.formatFee},
				{header:'基准阈值',dataIndex:'base_threshold',width:55,renderer : Ext.util.Format.formatFee},
				{header:'临时阈值',dataIndex:'temp_threshold',width:70,
					editor:new Ext.form.NumberField({})
				}
			]
		});
		ThresholdGrid.superclass.constructor.call(this,{
			border:false,
			store:this.thresholdStore,
			cm:cm, 
			clicksToEdit:1
		});
		var acctIds = [];
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid.getSelectionModel().getSelections();
		Ext.each(records,function(record){
			acctIds.push(record.get('acct_id'));
		});
		this.thresholdStore.load({
			params:{
				custId: App.getData().custFullInfo.cust['cust_id'],
				acctIds:acctIds
			}
		});
	}
});

var ThresholdForm = Ext.extend(BaseForm,{
	grid:null,
	data:[],
	url:Constant.ROOT_PATH+"/core/x/Acct!updateThreshold.action",
	constructor:function(){
		this.grid = new ThresholdGrid();
		ThresholdForm.superclass.constructor.call(this,{
			border:false,
			layout:'fit',
			items:[this.grid]
		});
	},
	doValid: function(){
		this.grid.stopEditing();
		var records = this.grid.getStore().getModifiedRecords();
		if(records.length == 0){
			var obj = {};
			obj['msg'] = '没有数据被修改!';
			obj['isValid'] = false;
			return obj;
		}else{
			Ext.each(records,function(record){
				var obj = {};
//				var d = record.data;
//				d['temp_threshold'] = d['temp_threshold']*100;
//				this.data.push(d);
				obj['old_value'] = record.modified['temp_threshold']*100;
				obj['new_value'] = record.get('temp_threshold')*100;
				obj['column_name'] = 'temp_threshold';
				obj['acct_id'] = record.get('acct_id');
				obj['acctitem_id'] = record.get('acctitem_id');
				obj['task_code'] = record.get('task_code');
				this.data.push(obj);
			},this);
		}
		return ThresholdForm.superclass.doValid.call(this);
	},
	getValues: function(){
		return {'thresholdListStr':Ext.encode(this.data)};
	}
});

Ext.onReady(function(){
	var form = new ThresholdForm();
	TemplateFactory.gTemplate(form);
});















