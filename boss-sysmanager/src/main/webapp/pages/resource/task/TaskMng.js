/**
* TJ: 停机
* CJ: 催缴
* CJ: 巡检
* TD: 退订
*/

/**
 * 公共编辑框
 * @class
 * @extends Ext.grid.EditorGridPanel
 */
var CommonTaskGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	areaStore:null,//地区store
	countyStore:null,//县市store
	constructor:function(config){
		config = config || {};
		CommonTaskGrid.superclass.constructor.apply(this,arguments);
	},
	initComponent:function(){
		CommonTaskGrid.superclass.initComponent.call(this);
		this.on("beforeedit",this.beforeEdit,this);
	},
	beforeEdit: function(obj){
		var field = obj.field;
		var record = obj.record;
		//失效，该条配置数据不能编辑
		if(record.get('status') == '0'){
			return false;
		}
		
		if(field == 'county_name'){
			//县市列 新增时可以编辑，一旦保存就不能修改
			if(Ext.isEmpty(record.get('isEdit'))){
				return false;
			}
		}else if(field == 'serv_type_text'){
			if(Ext.isEmpty(record.get('isEdit'))){
				return false;
			}
		}else if(field == 'isbase'){
			//停机可以编辑是否停基础产品
			if(Ext.isEmpty(record.get('isEdit')) && record.get('task_code') != 'TJ' ){
				return false;
			}
		}else if(field == 'bcnt' || field == 'xcnt'){
			//宽带不能编辑基本产品和增值产品数量列
			if( record.get('serv_type') == 'BAND' ){
				return false;
			}
		}else if(field == 'status'){
			//新增默认为有效的，不能编辑状态列
			if(record.get('isEdit') === true){	
				return false;	
			}
		}else if(field == 'max_prod_num' || field =='isbase' || field == 'eff_date' || field == 'exp_date'){
			//退订编辑无效
			if(record.get('task_code') == 'TD'){
				return false;	
			}
		}
		return true;
	},
	doRefresh: function(){
		this.getStore().reload();
	},
	isValid:function(){
		var store = this.getStore();
		var records = store.getModifiedRecords();
		if(records.length == 0 ){
			return {flag:false,msg:'没有修改过的数据!'};
		}
		var config = this.getColumnModel().config;
		
		var flag = true;
		var msg = "";
		for(var k=0;k<records.length;k++){
			var record = records[k];
			for(var i=0;i<config.length;i++){
				var cm = config[i];
				if(cm['allowBlank'] == false && Ext.isEmpty(record.get(cm['dataIndex']))){
					flag = false;
					msg = cm['header']+" 不能为空!";
					break;
				}
			}
			if(!flag){
				break;	
			}/*else{
				store.each(function(r){
					if (record.id != r.id && (!Ext.isEmpty(prop)?record.get(field) == r.get(field):1=1)
						&& record.get('task_code') == r.get('task_code')
						&& record.get('county_id') == r.get('county_id')) {
						flag = false;
						msg = record.get('county_name')+" "+record.get('serv_type_text')+" 已有配置,请确认!";
						return false;
					}
				});
			}*/
		}
		return {flag:flag,msg:msg};
	},
	getEditValues: function(){
		var obj = this.isValid();
		if(obj['flag'] === false){
			Alert(obj['msg']);
			return;
		}
		
		var records = this.getStore().getModifiedRecords();
		var arr = [];
		
		Ext.each(records,function(record){
			var data = record.data;
			for(var d in data){
				if(Ext.isEmpty(data[d]))
					delete data[d];
			}
			arr.push(record.data);
		});
		return arr;
	},
	addRecord: function(obj){
		var RecordType = this.getStore().recordType;
		var record = new RecordType(obj);
		this.stopEditing();
		this.getStore().insert(0,record);
		this.startEditing(0,0);
		this.getSelectionModel().selectFirstRow();
	},
	deleteRecord: function(rowIndex){
		Confirm('确认删除吗?',this,function(){
			this.getStore().remove(this.getSelectionModel().getSelected());
		});
	},
	rendererStopValue:function(v){
		if(v == 'Y'){
			return '停';
		}else if(v == 'N'){
			return '不停';	
		}
	},
	rendererStatusValue:function(v){
		if(v == '1'){
			return '有效';
		}else if(v == '0'){
			return '失效';	
		}
	},
	rendererNumValue:function(v){
		if(Ext.isEmpty(v)){
			return '无穷大';
		}else if(v == 0){
			return '不停';
		}
		return v;
	},
	rendererExpDateValue:function(v,meta,record,store,a){
		if(Ext.isEmpty(v)){
			return '一直生效';	
		}
		return Ext.util.Format.dateFormat(v);
	}
});

/**
 * 停机 编辑框
 * @class
 * @extends CommonTaskGrid
 */
var StopMachGrid = Ext.extend(CommonTaskGrid,{
	constructor:function(){
		this.stopMachStore = new Ext.data.JsonStore({
			url:root+'/config/Task!queryTjTaskSchedule.action',
			pruneModifiedRecords:true,
			fields:[
				'schedule_time','status','county_id','area_id','task_code',
				'serv_type','serv_type_text','isbase','bcnt','notbase','xcnt',
				{name:"base_eff_date",type:'date',dateFormat:'Y-m-d H:i:s'},
				{name:"base_exp_date",type:'date',dateFormat:'Y-m-d H:i:s'},
				{name:"notbase_eff_date",type:'date',dateFormat:'Y-m-d H:i:s'},
				{name:"notbase_exp_date",type:'date',dateFormat:'Y-m-d H:i:s'},
				'is_real_time','county_name','area_name',
				'isEdit'//true用来编辑为新增,其他值为修改,只能一条新增有效的数据
				
			]
		});
		this.stopMachStore.load();
		
		this.countyCombo = new Ext.form.ComboBox({
			store:new Ext.data.JsonStore({
				url:root+'/config/Task!queryCounty.action',
				fields:['county_id','county_name','area_id']
			}),displayField:'county_name',valueField:'county_name',
			listeners:{
				scope:this,
				select:function(combo,r){
					var record = this.getSelectionModel().getSelected();
					record.set('area_id',r.get('area_id'));
					record.set('county_id',r.get('county_id'));
				}
			}
		});
		this.countyCombo.getStore().load();
		
		this.servTypeCombo = new Ext.ux.ParamCombo({
			paramName:'SERV_ID',valueField:'item_name',
			listeners:{
				scope:this,
				select:function(combo,r){
					var record = this.getSelectionModel().getSelected();
					record.set('serv_type',r.get('item_value'));
					record.set('serv_type_text',r.get('item_name'));
				}
			}
		});
		this.isBaseCombo = new Ext.form.ComboBox({
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[['停','Y'],['不停','N']]
			}),displayField:'text',valueField:'value'
		});
		this.statusCombo = new Ext.form.ComboBox({
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[['失效','0'],['有效','1']]
			}),displayField:'text',valueField:'value'
		});
		this.notbaseCombo = new Ext.form.ComboBox({
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[['停','Y'],['不停','N']]
			}),displayField:'text',valueField:'value'
		});
		this.isRealTimeCombo = new Ext.form.ComboBox({
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[['实时',1],['定时',0]]
			}),displayField:'text',valueField:'value'
		});
		
		StopMachGrid.superclass.constructor.call(this, {
			id:'stopMachGridId',
			border:false,
			store:this.stopMachStore,
			sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
			columns:[
				{header:'县市',dataIndex:'county_name',editor:this.countyCombo,width:75,
					allowBlank:false,renderer:App.qtipValue},
				{header:'执行时间',dataIndex:'schedule_time',width:70,allowBlank:false,
					editor:new Ext.form.TextField({
						allowDecimals:false,allowNegative:false,minLength:6,maxLength:6,
						emptyText:'请输入6位数代表时分秒',vtype:'timeHIS'
					})
				},
				{header:'服务类型',dataIndex:'serv_type_text',
					editor:this.servTypeCombo,width:60,allowBlank:false
				},
				{header:'状态',dataIndex:'status'/*,editor:this.statusCombo*/,width:45,allowBlank:false,
					renderer:this.rendererStatusValue
				},
				{header:'停基础产品',dataIndex:'isbase',editor:this.isBaseCombo,width:75,allowBlank:false,
					renderer:this.rendererStopValue
				},
				{header:'基础产品数量',dataIndex:'bcnt',width:80,editor:new Ext.form.NumberField({
						allowDecimals:false,allowNegative:false
					}),
					renderer:this.rendererNumValue
				},
				{header:'停增值产品',dataIndex:'notbase',allowBlank:false,editor:this.notbaseCombo,width:75,
					renderer:this.rendererStopValue
				},
				{header:'增值产品数量',dataIndex:'xcnt',width:80,editor:new Ext.form.NumberField({
						allowDecimals:false,allowNegative:false
					}),
					renderer:this.rendererNumValue
				},
				{header:'基础产品不停的生效时间',dataIndex:'base_eff_date',width:85,
					editor:new Ext.form.DateField({format:'Y-m-d'}),
					renderer:Ext.util.Format.dateFormat
				},
				{header:'基础产品不停的失效时间',dataIndex:'base_exp_date',width:85,
					editor:new Ext.form.DateField({format:'Y-m-d'}),
					renderer:this.rendererExpDateValue
				},
				{header:'增值产品不停的生效时间',dataIndex:'notbase_eff_date',width:85,
					editor:new Ext.form.DateField({format:'Y-m-d'}),
					renderer:Ext.util.Format.dateFormat
				},
				{header:'增值产品不停的失效时间',dataIndex:'notbase_exp_date',width:85,
					editor:new Ext.form.DateField({format:'Y-m-d'}),
					renderer:this.rendererExpDateValue
				},
				{header:'实时停',dataIndex:'is_real_time',editor:this.isRealTimeCombo,width:85,
					allowBlank:false,renderer:function(v){
						if(v == 0){
							return '定时';
						}else if(v == 1){
							return '实时';
						}
					}
				},
				{header:'操作',dataIndex:'task_code',renderer:function(v,meta,record){
						if( record.get('isEdit') == true ){
							return "<a href='#' onclick=Ext.getCmp('threeTaskGridId').deleteRecord()>删除</a>&nbsp;&nbsp;";
						}else{
							var status = record.get('status');
							if(status == '1'){
								return "<a href='#' onclick=Ext.getCmp('stopMachGridId').updateStatus('0')>失效</a>";
							}else if(status == '0'){
								return "<a href='#' onclick=Ext.getCmp('stopMachGridId').updateStatus('1')>生效</a>";
							}
						}
					}
				}
			],
			tbar:['-',
				{text:'添加',iconCls:'icon-add',scope:this,handler:this.doAdd},'-',
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},'-',
				{text:'刷新',iconCls:'icon-refresh',scope:this,handler:this.doRefresh},'-'
			]
		});
		App.form.initComboData([this.servTypeCombo]);
	},
	doSave: function(){
		this.stopEditing();
		var values = this.getEditValues();
		if(values){
			Ext.Ajax.request({
				url:root+'/config/Task!saveStopTask.action',
				params:{stopTaskList:Ext.encode(values)},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data['success'] == true){
						Alert('保存成功');
						this.getStore().load();
					}
				}
			});
		}
	},
	doAdd: function(){
		var obj = {
			task_code:'TJ',
			status:'1',
			is_real_time:1,
			isbase:'Y',
			notbase:'Y',
			isEdit:true
		};
		StopMachGrid.superclass.addRecord.call(this,obj);
	},
	updateStatus:function(status){
		Confirm('确认修改吗?',this,function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/config/Task!updateStopTaskStatus.action',
				params:{
					status:status,
					taskCode:record.get('task_code'),
					servType:record.get('serv_type'),
					countyId:record.get('county_id')
				},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					record.set('status',status);
					record.commit();
				}
			});
		});
	},
	deleteRecord:function(){
		Confirm('确认删除吗?',this,function(){
			var record = this.getSelectionModel().getSelected();
			this.getStore().remove(record);
			this.getStore().commitChanges();
		});
	}
});

/**
 * 催缴 巡检 退订 编辑框
 * @class
 * @extends CommonTaskGrid
 */
var ThreeTaskGrid = Ext.extend(CommonTaskGrid,{
	taskColumns:null,
	storeFields:null,
	servTypeCombo:null,
	isBaseCombo:null,
	statusCombo:null,
	currTaskCode:null,
	currTaskCodeText:null,
	parent:null,
	constructor:function(config){
		this.currTaskCode = config['taskCode'];
		this.parent = config['parent'];
		
		if(this.currTaskCode == 'XJ'){
			this.currTaskCodeText = '巡检';	
		}else if(this.currTaskCode == 'CJ'){
			this.currTaskCodeText = '催缴';
		}else if(this.currTaskCode == 'TD'){
			this.currTaskCodeText = '退订';
		}
		
		this.countyCombo = new Ext.form.ComboBox({
			store:new Ext.data.JsonStore({
				url:root+'/config/Task!queryCounty.action',
				fields:['county_id','county_name','area_id']
			}),displayField:'county_name',valueField:'county_name',
			listeners:{
				scope:this,
				select:function(combo,r){
					var record = this.getSelectionModel().getSelected();
					record.set('area_id',r.get('area_id'));
					record.set('county_id',r.get('county_id'));
				}
			}
		});
		this.countyCombo.getStore().load();
		
		this.baseText = this.currTaskCodeText+"基本产品";
		this.noBaseText = this.currTaskCodeText+"增值产品";
		this.isBaseCombo = new Ext.form.ComboBox({
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[[this.baseText,'T'],[this.noBaseText,'F']]
			}),displayField:'text',valueField:'value'
		});
		delete this.baseText;
		delete this.noBaseText;
		
//		this.statusCombo = new Ext.form.ComboBox({
//			store:new Ext.data.ArrayStore({
//				fields:['text','value'],
//				data:[['失效','0'],['有效','1']]
//			}),displayField:'text',valueField:'value'
//		});
		
		this.storeFields = [
			'schedule_time','status','county_id','county_name','area_id','task_code',
			'isbase','max_prod_num','eff_date','exp_date',
			'task_info','mail_title','isEdit','hst_day'
		];
		
		this.taskColumns = [
			{header:'县市',dataIndex:'county_name',width:90,allowBlank:false,
				editor:this.countyCombo,
				renderer:App.qtipValue
			},
			{header:'执行时间',dataIndex:'schedule_time',width:70,allowBlank:false,
				editor:new Ext.form.TextField({
					allowDecimals:false,allowNegative:false,minLength:6,maxLength:6,
					emptyText:'请输入6位数代表时分秒',vtype:'timeHIS'
				})
			},
			{header:'状态',dataIndex:'status',allowBlank:false,width:55,scope:this,
				renderer:this.rendererStatusValue
			},
			{header:'产品类别',dataIndex:'isbase',allowBlank:false,editor:this.isBaseCombo,width:90,
				scope:this,renderer:function(v,meta,r){
					if(v == 'T'){
						return this.currTaskCodeText+'基本产品';
					}else if(v == 'F'){
						return this.currTaskCodeText+'增值产品';
					}
				}
			},
			{header:'最大执行数',dataIndex:'max_prod_num',allowBlank:false,width:80,
				editor:new Ext.form.NumberField({
					allowDecimals:false,allowDecimals:false
				})
			},
			{header:'开始时间',dataIndex:'eff_date',width:85,
				editor:new Ext.form.DateField({format:'Y-m-d'}),scope:this,
				renderer:function(v){
					if(Ext.isEmpty(v)){
						return '一直'+this.currTaskCodeText;
					}
					return Ext.util.Format.dateFormat(v);
				}
			},
			{header:'失效时间',dataIndex:'exp_date',width:85,
				editor:new Ext.form.DateField({format:'Y-m-d'}),scope:this,
				renderer:function(v){
					if(Ext.isEmpty(v)){
						return '一直'+this.currTaskCodeText;
					}
					return Ext.util.Format.dateFormat(v);
				}
			},
			{header:'操作',dataIndex:'task_code',scope:this,renderer:function(v,meta,record,rowIndex){
					if(record.get('isEdit') == true){
						return "<a href='#' onclick=Ext.getCmp('threeTaskGridId').deleteRecord()>删除</a>";
					}else{
						var status = record.get('status');
						var taskCode = record.get('task_code');
						var countyId = record.get('county_id');
						var isBase = record.get('isbase');
						if(status == '1'){
							return String.format("<a href='#' onclick=Ext.getCmp('threeTaskGridId').updateStatus('0','{0}','{1}','{2}')>失效</a>",taskCode,countyId,isBase);
						}else if(status == '0'){
							return String.format("<a href='#' onclick=Ext.getCmp('threeTaskGridId').updateStatus('1','{0}','{1}','{2}')>生效</a>",taskCode,countyId,isBase);
						}
					}
				}
			}
		];
		ThreeTaskGrid.superclass.constructor.call(this,{id:'threeTaskGridId'});
		this.deleteRecord = function(){
			this.parent.deleteRecord();
		}
	},
	initComponent:function(config){
		var defConfig = {
			border:false,
            tbar:['-',
				{text:'添加',iconCls:'icon-add',scope:this,handler:this.doAdd},'-',
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},'-',
				{text:'刷新',iconCls:'icon-refresh',scope:this,handler:this.doRefresh},'-'
			]
        };
        
        Ext.apply(this,defConfig);
		ThreeTaskGrid.superclass.initComponent.call(this);
	},
	doSave:function(){
		this.stopEditing();
		var values = this.getEditValues();
		if(values){
			Ext.Ajax.request({
				url:root+'/config/Task!saveThreeTask.action',
				params:{threeTaskList:Ext.encode(values)},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data['success'] == true){
						Alert('保存成功');
						this.parent.reloadData(values[0]['task_code']);
					}
				}
			});
		}
	},
	doAdd: function(){
		//只能新增有效的数据配置
		var obj = {
			task_code:this.currTaskCode,
			status:'1',
			isbase:this.currTaskCode == 'TD' ? '':'T',
			isEdit:true
		};
		ThreeTaskGrid.superclass.addRecord.call(this,obj);
	},
	doRefresh:function(){
		this.parent.reloadData(this.currTaskCode);
	},
	updateStatus:function(status,taskCode,countyId,isBase){
		Confirm('确认修改吗?',this,function(){
			Ext.Ajax.request({
				url:root+'/config/Task!updateThreeTaskStatus.action',
				params:{
					taskCode:taskCode,
					status:status,
					countyId:countyId,
					isBase:isBase
				},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data['success'] === true){
						this.doRefresh();
					}
				}
			});
		});
	}
});

/**
 * 巡检
 * @class
 * @extends Ext.grid.GridPanel
 */
var RoundCheckGrid = Ext.extend(ThreeTaskGrid,{
	constructor:function(p){
		RoundCheckGrid.superclass.constructor.call(this,{taskCode:'XJ',parent:p,id:'roundCheckGridId'});
	},
	initComponent:function(){
		var defConfig = {
			store:new Ext.data.JsonStore({fields:this.storeFields,pruneModifiedRecords:true}),
			columns:this.taskColumns,
			sm:new Ext.grid.RowSelectionModel({})
        };
        
        Ext.apply(this,defConfig);
		RoundCheckGrid.superclass.initComponent.call(this);
	}
});


/**
 * 催缴
 * @class
 * @extends Ext.grid.GridPanel
 */
var PreePayGrid = Ext.extend(ThreeTaskGrid,{
	constructor:function(p){
		PreePayGrid.superclass.constructor.call(this,{taskCode:'CJ',parent:p,id:'preePayGridId'});
	},
	initComponent:function(){
		var columns = [];
		Ext.each(this.taskColumns,function(c){
			columns.push(c);
		});
		columns.push({id:'hst_day_id',header:'催缴周期',dataIndex:'hst_day',editor:new Ext.form.NumberField({
				allowDecimals:false,allowNegative:false
			})},
			{id:'task_info_id',header:'催缴内容',dataIndex:'task_info',width:200,
				editor:new Ext.form.TextArea({maxLength:2000,height:150})
			},
			{id:'mail_title_id',header:'标题',dataIndex:'mail_title',
				editor:new Ext.form.TextField({})
			});
		var defConfig = {
			store:new Ext.data.JsonStore({fields:this.storeFields,pruneModifiedRecords:true}),
			columns:columns,
			sm:new Ext.grid.RowSelectionModel({})
        };
        
        Ext.apply(this,defConfig);
		PreePayGrid.superclass.initComponent.call(this,{children:this});
	}
});

/**
 * 退订
 * @class
 * @extends Ext.grid.GridPanel
 */
var CancelOrderGrid = Ext.extend(ThreeTaskGrid,{
	constructor:function(p){
		CancelOrderGrid.superclass.constructor.call(this,{taskCode:'TD',parent:p,id:'cancelOrderGridId'});
	},
	initComponent:function(){
		var defConfig = {
			store:new Ext.data.JsonStore({fields:this.storeFields,pruneModifiedRecords:true}),
			columns:this.taskColumns,
			sm:new Ext.grid.RowSelectionModel({})
        };
        
        Ext.apply(this,defConfig);
		CancelOrderGrid.superclass.initComponent.call(this);
        var colModel = this.getColumnModel();
        Ext.each(colModel.config,function(column){
        	if(column.allowBlank == false && column.dataIndex != 'schedule_time'){
        		column.allowBlank = true;
        	}
        });
	}
});

var TaskTabPanel = Ext.extend(Ext.TabPanel,{
	stopMachGrid:null,
	preePayGrid:null,
	roundCheckGrid:null,
	cancelOrderGrid:null,
	constructor:function(){
		this.stopMachGrid = new StopMachGrid();
		this.preePayGrid = new PreePayGrid(this);
		this.roundCheckGrid = new RoundCheckGrid(this);
		this.cancelOrderGrid = new CancelOrderGrid(this);
		TaskTabPanel.superclass.constructor.call(this,{
			border:false,
			activeTab:0,
			items:[
				{title:'停机',layout:'fit',items:this.stopMachGrid},
				{title:'催缴',layout:'fit',items:this.preePayGrid},
				{title:'巡检',layout:'fit',items:this.roundCheckGrid},
				{title:'退订',layout:'fit',items:this.cancelOrderGrid}
			]
		});
	},
	initComponent:function(){
		TaskTabPanel.superclass.initComponent.call(this);
		this.loadData();
	},
	loadData:function(){
		Ext.Ajax.request({
			url:root+'/config/Task!queryTaskSachedule.action',
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data['CJ']){
					this.preePayGrid.getStore().loadData(data['CJ'],false);
				}else{
					this.preePayGrid.getStore().removeAll();
				}
				if(data['XJ']){
					this.roundCheckGrid.getStore().loadData(data['XJ'],false);
				}else{
					this.roundCheckGrid.getStore().removeAll();
				}
				if(data['TD']){
					this.cancelOrderGrid.getStore().loadData(data['TD'],false);
				}else{
					this.cancelOrderGrid.getStore().removeAll();
				}
			}
		});
	},
	reloadData:function(taskCode){
		this.loadData();
	},
	deleteRecord:function(){
		Confirm('确认删除吗？',this,function(){
			var activeGrid = this.getActiveTab().items.items[0];
			var record = activeGrid.getSelectionModel().getSelected();
			if(record.get('isEdit') !== true){
				Ext.Ajax.request({
					url:root+'/config/Task!deleteThreeTask.action',
					params:{
						taskCode : record.get('task_code'),
						countyId : record.get('county_id')
					},
					scope:this,
					success:function(res,opt){
						var data = Ext.decode(res.responseText);
						if(data['success'] == true){
							activeGrid.getStore().remove(record);
							activeGrid.getStore().commitChanges();
						}
					}
				});
			}else{
				activeGrid.getStore().remove(record);
				activeGrid.getStore().commitChanges();
			}
		});
	}
});

TaskMng = Ext.extend(Ext.Panel,{
	constructor:function(){
		TaskMng.superclass.constructor.call(this,{
			id:'TaskMng',
			title:'任务配置',
			closable: true,
			border : false ,
			layout:'fit',
			items:[new TaskTabPanel()]
		});
	}
});



















