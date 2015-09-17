/**工单管理*/
UserDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	userDetailStore : null,
	constructor : function() {
		this.userDetailStore = new Ext.data.JsonStore({
					fields : ['user_type', 'user_type_text','user_name', 'device_model','device_model_text','task_id',
							'device_code', 'password','device_id','band']
				});
		UserDetailGrid.superclass.constructor.call(this, {
			ds : this.userDetailStore,
			viewConfig : {
				forceFit :true
			},
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
						header : '用户类型',dataIndex : 'user_type_text',width : 100,renderer : App.qtipValue}, {
						header : '用户名',dataIndex : 'user_name',renderer : App.qtipValue}, {
						header : '密码',dataIndex : 'password',renderer : App.qtipValue}, {
						header : '设备型号',dataIndex : 'device_model_text',renderer : App.qtipValue}, {
						header : '设备号',dataIndex : 'device_id',renderer : App.qtipValue}, {
						header : '带宽',dataIndex : 'band',renderer : App.qtipValue}])
		})
	}
})

TaskDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	taskDetailStore : null,
	constructor : function() {
		this.taskDetailStore = new Ext.data.JsonStore({
					fields : ['busi_code', 'busi_name', 'optr_id','optr_name','log_time',
							'syn_status','error_remark','syn_status_text']});
		TaskDetailGrid.superclass.constructor.call(this, {
			ds : this.taskDetailStore,
			viewConfig : {
				forceFit :true
			},
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
				header : '操作时间',dataIndex : 'log_time',width : 100,renderer : Ext.util.Format.dateFormat}, {
				header : '操作类型',dataIndex : 'busi_name',renderer : App.qtipValue}, {
				header : '操作人',dataIndex : 'optr_name',renderer : App.qtipValue}, {
				header : '同步状态',dataIndex : 'syn_status_text',renderer : App.qtipValue}, {
				header : '描述',dataIndex : 'error_remark',renderer :  App.qtipValue
			}])
		})
	}
})

TaskAllInfo = Ext.extend(Ext.Panel,{
	panel : null,
	userGrid : null,
	detail : null,
	constructor : function() {
		this.userGrid = new UserDetailGrid();
		this.detail = new TaskDetailGrid();
		TaskAllInfo.superclass.constructor.call(this, {
				border : false,
				layout : 'border',
				closable : true,
				items : [{
							region : 'west',
							layout : 'fit',
							width : '55%',
							split : true,
							title : '用户信息',
							items : [this.userGrid]
						}, {
							region : 'center',
							layout : 'fit',
							title : '操作明细',
							items : [this.detail]
						}]
		})
	}
})

var TaskDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	taskDeviceGridStore:null,
	taskId:null,
	otlNoField:null,
	ponNoField:null,
	constructor:function(){
		var that = this;
		this.taskDeviceGridStore = new Ext.data.JsonStore({
			fields:['device_id','device_type','device_type_text','device_model','device_model_text',
				'device_code','user_type', 'user_type_text','user_name','task_id']
		});
		
		this.otlNoField = new Ext.form.TextField({xtype: 'textfield',width: 90});
		this.ponNoField = new Ext.form.TextField({xtype: 'textfield',width: 90});
		var columns = [{
						header : '用户类型',dataIndex : 'user_type_text',width : 80,renderer : App.qtipValue}, {
						header : '用户名',dataIndex : 'user_name',renderer : App.qtipValue},
						{header:'型号',dataIndex:'device_model_text',width:150},	
						{header:'设备编号',dataIndex:'device_code',width:150,editor:new Ext.form.TextField({
							vtype:'alphanum',
							listeners:{
								scope:this,
								change:this.queryAndAddDevice
							}
						})},
						{header:'类型',dataIndex:'device_type_text',width:70}
		];
		
		TaskDeviceGrid.superclass.constructor.call(this,{
			region:'center',
			ds:this.taskDeviceGridStore,
			clicksToEdit:1,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar: ['otlNo:',this.otlNoField,'-','ponNo:',this.ponNoField]
		});
		},
		queryAndAddDevice:function(field,newValue,oldValue){
			if(newValue && newValue!==oldValue){
				var dRecord = this.getSelectionModel().getSelected();
				Ext.Ajax.request({
					url:Constant.ROOT_PATH + "/core/x/Task!queryDeviceInfoByCodeAndModel.action",
					params:{deviceCode:newValue,deviceModel:dRecord.get('device_model')},
					scope:this,
					success:function(res,opt){
						var data = Ext.decode(res.responseText);
						dRecord.set('device_code',data['device_code']);
						dRecord.set('device_type_text',data['device_type_text']);
						dRecord.set('device_type',data['device_type']);
					},
					clearData:function(){
						dRecord.set('device_code','');
						dRecord.set('device_type_text','');
						dRecord.set('device_type','');
						//清空组件
					}
				});
			}
		},
		getValues:function(){
			var arr=[];
			var store = this.getStore();
			store.each(function(record){
				var values = {};
				values["oldDeviceCode"] = record.get('device_id');
				values["deviceModel"] =record.get('device_model');
				values["deviceCode"] = record.get('device_code');
				arr.push(values);
			},this);
			return arr;
		},
		checkDeviceCode : function(){
			this.stopEditing();//停止编辑
			var store = this.getStore();
			var count = store.getCount();//总个数
			
			var config = this.getColumnModel().config;
			
			var dataIndexes = [];
			for(var i=0;i<config.length;i++){
				dataIndexes.push(config[i].dataIndex);
			}
			
			var flag = true;
			for(var i=0;i<count;i++){
				var data = store.getAt(i).data;
				for(var k=0;k<dataIndexes.length;k++){
					var a = dataIndexes[k];
					if(Ext.isEmpty(data[a]) && a == 'device_code'){
						Alert('请输入设备号!',function(){
							this.getSelectionModel().selectRow(i);
							this.startEditing(i,k);
						});
						flag = false;
						break;
					}
				}
			}
			return flag;
	}
});

TaskDeviceWin = Ext.extend(Ext.Window,{
	deviceGrid : null,
	task_id:null,
	constructor : function(){
		this.deviceGrid = new TaskDeviceGrid();
		TaskDeviceWin.superclass.constructor.call(this,{
			title : '设备回填',
			layout : 'fit',
			height : 400,
			width : 600,
			id:'TaskDeviceWinId',
			closeAction : 'close',
			items : [this.deviceGrid],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	doSave:function(){
		if(!this.deviceGrid.checkDeviceCode()){
			return false;
		}
		var data = this.deviceGrid.getValues();
		var url = Constant.ROOT_PATH + "/core/x/Task!fillTask.action";
		var o = {
			task_id:this.task_id,
			devices : Ext.encode(data), 
			otlNo: this.deviceGrid.otlNoField.getValue(),
			ponNo : this.deviceGrid.ponNoField.getValue()
		};
		var that = this;
		App.sendRequest( url, o, function(res,opt){
			Ext.getCmp('taskManagerPanelId').loadTaskData(that.task_id);
			that.close();
		});
	},
	show:function(t,data){
		this.task_id = t;
		this.deviceGrid.taskDeviceGridStore.loadData(data);
		TaskDeviceWin.superclass.show.call(this);
	}
});

/**
 * 工单管理
 */
TaskManagerPanel = Ext.extend( Ext.Panel ,{
	taskStore:null,
	grid: null,
	pageSize: 20,
	taskFinishWin: null,
	taskDetailsWin:null,
	iconCls: 'doc',
	taskAllInfo:null,
	taskUserData:null,
	taskLogData:null,
	constructor:function(item){
		this.initWidgets(item);	
		this.taskAllInfo = new TaskAllInfo();
  		TaskManagerPanel.superclass.constructor.call(this,{
			border: false,
			id: 'taskManagerPanelId',
			layout: 'border',
			items : [{
						region : 'south',
						layout : 'fit',
						height : 160,
						border: false,
						split : true,
						items : [this.taskAllInfo]
					}, {
						region : 'center',
						layout : 'fit',
						border: false,
						items : [this.grid]
					}],
			buttons: [{
				id:'team_btn_id',
				text: '分配施工队',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.doTeamTask
			},{
				id:'ivalid_btn_id',
				text: '工单作废',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.doCancelTask
			},{
				id:'device_btn_id',
				text: '回填设备',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.doDeviceTask
			},{
				id:'end_btn_id',
				text: '完工',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.doEndTask
			},{
				id:'visit_btn_id',
				text: '回访',
				height: 30,
				width: 80,
				disabled:true,
				style: 'color: red;',
				scope: this,
				handler: this.doVisitTask
			},{
				id:'send_btn_id',
				text: '发送ZTE授权',
				width: 80,
//				tooltip: '工单传给呼叫',
				disabled:true,
				height: 30,
				scope: this,
				handler: this.doSendTask
			}]
		});
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	initEvents: function(){
		this.on("afterrender",function(){
			this.swapViews.call(this.grid);
		},this,{delay:10});
		
		TaskManagerPanel.superclass.initEvents.call(this);
	},
	initWidgets: function(item){
		this.taskStore = new Ext.data.JsonStore({
			url: root + '/core/x/Task!queryTasks.action' ,
			fields:['task_id','cust_id','cust_no','cust_name','tel','old_addr','new_addr','address','task_type_id',
					'task_status','task_status_text','task_type_id_text','team_id','team_id_text','bug_type','bug_type_text'
					,'bug_detail','zte_status','zte_status_text','task_create_time'],
			root : 'records',
			totalProperty : 'totalProperty',
			autoDestroy : true
		});
		
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		var startDate = new Date();
		startDate.setDate(startDate.getDate() - 1);
		this.createStartDateField = new Ext.form.DateField({
				xtype: 'datefield',width: 90,format: 'Y-m-d',allowBlank: false,value: startDate
			});
		this.createEndDateField = new Ext.form.DateField({
				xtype: 'datefield',width: 90,format: 'Y-m-d',allowBlank: false,value: tomorrow});
		this.custNoField = new Ext.form.TextField({
				xtype: 'textfield',width: 90,emptyText: '客户编号'});
		this.taskNoField = new Ext.form.TextField({
				xtype: 'textfield',width: 90,emptyText: '工单编号'});			
		this.custNameField = new Ext.form.TextField({
				xtype: 'textfield',width: 90,emptyText: '客户名称'});
		this.mobileField = new Ext.form.TextField({
				xtype: 'textfield',width: 100,emptyText: '联系电话'});
		this.newaddrField = new Ext.form.TextField({
				xtype: 'textfield',width: 140,emptyText: '地址'});
		this.taskStatusCombo = new Ext.ux.LovCombo({
				width: 120,
				emptyText: '工单状态',
				paramName:'TASK_STATUS',
				hiddenName : 'task_status',
				typeAhead:true,editable:true,
				store:new Ext.data.JsonStore({
					fields:['item_value','item_name']
				}),displayField:'item_name',valueField:'item_value',
				triggerAction:'all',mode:'local'
			});		
		
		this.taskDetailTypeCombo = new Ext.ux.LovCombo({
				xtype: 'textfield',
				width: 120,
				emptyText: '工单类型',
				typeAhead:true,editable:true,
				paramName:'TASK_DETAIL_TYPE',
				store:new Ext.data.JsonStore({
					fields:['item_value','item_name']
				}),displayField:'item_name',valueField:'item_value',
				triggerAction:'all',mode:'local'
		});
		
		this.taskAddrCombo = new Ext.form.ComboBox({
			typeAhead: true,
			width: 120,
		    triggerAction: 'all',
		    mode: 'remote',
		    emptyText: '地区',
		    editable : true,
		    store: new Ext.data.JsonStore({
//		    	url: root + '/core/x/Task!queryTaskAddr.action' ,
//		    	autoLoad : true,
//		   	 	autoDestroy:true,
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
		});
		
		this.taskTeamCombo = new Ext.ux.LovCombo({
			typeAhead: true,
			width: 120,
		    triggerAction: 'all',
		    mode: 'remote',
		    emptyText: '施工队',
		    editable : true,
		    store: new Ext.data.JsonStore({
		    	url: root + '/core/x/Task!queryTaskTeam.action' ,
		    	autoLoad : true,
		   	 	autoDestroy:true,
		        fields: [ 'dept_id', 'dept_name' ]
		    }),
		    valueField: 'dept_id',
		    displayField: 'dept_name'
		});
		//初始化下拉框的数据 
		App.form.initComboData([this.taskStatusCombo,this.taskDetailTypeCombo]);
		
		var twoTbar = new Ext.Toolbar({
			items : [this.taskNoField,'-',this.custNoField,'-',this.mobileField,'-',this.custNameField,'-',this.newaddrField,'-',{
				text: '查询',
				pressed: true,
				scope: this,
				width: 80,
				handler: this.doSearchTask
			},'-',{
				text: '待处理工单',
				pressed: true,
				scope: this,
				width: 100
//				handler: this.doSearchTask
			}]
		});
		
    	// create the Grid
    	var sm = new Ext.grid.RowSelectionModel();
	    this.grid = new Ext.grid.GridPanel({
	        store: this.taskStore,
	        cm: new Ext.ux.grid.LockingColumnModel({
	        	columns:[
				//{header: '工单编号',		dataIndex : 'task_id', 				width: 100},
				{header: '工单类型',		dataIndex : 'task_type_id_text', 	width: 100, renderer: function(v, m ,rs){
					return "<span style='font-weight: bold;'>"+ v +"</span>";
				}},
				{header: '客户名称', 	dataIndex: 'cust_name', width: 80},
				{header: '地址', 		dataIndex : 'address', width: 200,renderer:App.qtipValue},
				{header: '联系电话', 	dataIndex : 'tel', 				width: 100},
				{header: '工单状态', 		dataIndex: 'task_status', width: 100, renderer: function(v, m ,rs){
					var text = rs.get("task_status_text");
					var color = "black";
					if(v == 'INIT'){
						color = "purple";
					}else if(v == 'END'){
						color = "green";
					}else if(v == 'CANCEL'){
						color = "gray";
					}
					return "<span style='font-weight: bold;color: "+ color +";'>"+ text +"</span>";
				}},
				{header:'施工队', dataIndex:'team_id_text',width:100,renderer:App.qtipValue},
				{header:'故障类型',dataIndex:'bug_type_text',width:85},
				{header:'故障详细信息',dataIndex:'bug_detail',width:120,renderer:App.qtipValue},
				{header: 'ZTE授权状态',dataIndex: 'zte_status_text', width: 100, renderer:App.qtipValue},
				{header: '创建时间', dataIndex: 'task_create_time', 	width: 165, renderer: Ext.util.Format.dateFormat}					
	        ]}),
	        sm: sm,
	        stripeRows: true,
			bbar: new Ext.PagingToolbar({
		        store: this.taskStore, // grid and PagingToolbar using same store
		        pageSize: this.pageSize
		    }),
			tbar: [this.taskDetailTypeCombo,'-',this.taskAddrCombo,'-','受理时间:',this.createStartDateField,' ',this.createEndDateField,'-',
			this.taskTeamCombo,'-',this.taskStatusCombo],
			listeners : {
				'render' : function() {
					twoTbar.render(this.tbar);
				}
			}
	    });
	},
	initEvents: function(){
		this.grid.on("rowclick", this.doClickRecord, this );
		
		TaskManagerPanel.superclass.initEvents.call(this);
	},
	doClickRecord:function(g, i, e){
		//选中一条时才显示
		var records = g.getSelectionModel().getSelections();
		if(records.length == 1){
			this.loadTaskData(records[0].get("task_id"));
		}
	},
	loadTaskData:function(taskId){
		Ext.Ajax.request({
			scope : this,
			url: root + '/core/x/Task!queryTaskDetail.action' ,
			params : {
				task_id : taskId
			},
			success : function(res,opt){
				var rs = Ext.decode(res.responseText);
				this.taskUserData = rs.taskUserList;
				this.taskLogData = rs.taskLogList;
				this.taskAllInfo.userGrid.getStore().loadData(rs.taskUserList);
				this.taskAllInfo.detail.getStore().loadData(rs.taskLogList);
			}
		});
	},
	createStartDateField: null,
	createEndDateField: null,
	custNoField: null,
	taskStatusCombo: null,
	mobileField: null,
	newaddrField: null,
	doCancelTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var ts = rs.get("task_status");
		if(ts == 'WAITEND' || ts == 'CANCEL' || ts == 'END'){
			Alert("不允许取消!");
			return ;
		}
		Confirm("确定要作废选中的工单吗?", this , function(){
			App.sendRequest(
				Constant.ROOT_PATH + "/core/x/Task!cancelTaskSn.action",
				{task_id : rs.get('task_id'),taskType:rs.get('task_type_id')},
				function(res,opt){
					Alert('工单已作废!');
					rs.set("task_status", "CANCEL");
					rs.set("task_status_text", "作废");
				});
		}); 
	},
	doSendTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
//		var taskIds = [];
//		for(var i = 0; i< rs.length ; i++){
//			if(rs[i].get("task_status") != 'CREATE'){
//				Alert("只有新建的工单才能提交呼叫!");
//				return ;
//			}
//			taskIds.push(rs[i].get("task_id"));
//		}
//		
//		var that = this;
//		Confirm("确定要提交呼叫吗?", this , function(){
//			App.sendRequest(
//				Constant.ROOT_PATH + "/core/x/Task!assignTask.action",
//				{task_ids : taskIds},
//				function(res,opt){
//					Alert('提交成功!', function(){
//						that.close();
//					});
//					that.doSearchTask();					
//				});
//		});
	},
	doTeamTask:function(){//分配施工队
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var arr = [];
		this.taskTeamCombo.getStore().each(function(record){
			if(record.get('dept_id') != rs.get('team_id')){
				arr.push(record.data);
			}
		});
		
		var teamCombo = new Ext.form.ComboBox({
			typeAhead: true,
			width: 120,
		    triggerAction: 'all',
		    mode: 'local',
		    fieldLabel:'施工队',
		    editable : true,
		    allowBlank:false,
		    store: new Ext.data.JsonStore({
		        fields: [ 'dept_id', 'dept_name' ]
		    }),
		    valueField: 'dept_id',
		    displayField: 'dept_name'
		});
		teamCombo.getStore().loadData(arr);
		
		var form = new Ext.form.FormPanel({
			labelWidth: 90,
			bodyStyle: 'padding-top: 10px;',
			items: [teamCombo]
		});
	
		if(rs.get('task_type_id') == '2'){
			var bugCauseCombo = new Ext.ux.ParamCombo({
				fieldLabel:'故障类型',
				xtype:'paramcombo',
				allowBlank:false,
				anchor: '95%',
				paramName:'TASK_BUG_CAUSE'
			});
			App.form.initComboData([bugCauseCombo]);
			form.add(bugCauseCombo);
		}
		
		var win = new Ext.Window({
			width: 320,
			height: 250,
			title: '施工队',
			border: false,
			closeAction:'close',
			layout: 'fit',
			items: form,
			buttons: [{
				text: '保存',
				scope: this,
				iconCls : 'icon-save',
				handler: function(){
					if(Ext.isEmpty(teamCombo.getValue())){
						Alert("施工队不能为空");return false;
					}
					if(bugCauseCombo && Ext.isEmpty(bugCauseCombo.getValue())){
						Alert("故障类型不能为空");return false;
					}
					var url = Constant.ROOT_PATH + "/core/x/Task!editTaskTeam.action";
					var o = {
						task_id : rs.get("task_id"), 
						deptId: teamCombo.getValue(),
						bugType : bugCauseCombo?bugCauseCombo.getValue():null
					};
					App.sendRequest( url, o, function(res,opt){
						rs.set("team_id",teamCombo.getValue());
						var index = 0;
						index = teamCombo.getStore().find('dept_id',teamCombo.getValue());
						rs.set("team_id_text",teamCombo.getStore().getAt(index).get('dept_name'));	
						if(bugCauseCombo){
							rs.set("bug_type",bugCauseCombo.getValue());
							index = bugCauseCombo.getStore().find('item_value',bugCauseCombo.getValue());
							rs.set("bug_type_text",bugCauseCombo.getStore().getAt(index).get('item_name'));
						}
						win.close();
					});
				}
			},{
				text: '取消',
				handler: function(){
					win.hide();
				}
			}]
		});
		win.show();
	},
	doDeviceTask:function(){//回填设备
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var win = Ext.getCmp('TaskDeviceWinId');
		if(!win)
			win = new TaskDeviceWin();
		win.show(rs.get('task_id'),this.taskUserData);
	},
	doEndTask:function(){//完成工单
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var finishCombo = new Ext.form.ComboBox({
			xtype: 'textfield',
			width: 120,
			fieldLabel:'完工类型',
			allowBlank:false,
			typeAhead:true,editable:true,
			paramName:'TASK_FINISH_TYPE',
			store:new Ext.data.JsonStore({
				fields:['item_value','item_name']
			}),displayField:'item_name',valueField:'item_value',
			triggerAction:'all',mode:'local'
		});
		App.form.initComboData([finishCombo]);
		var form = new Ext.form.FormPanel({
			labelWidth: 90,
			bodyStyle: 'padding-top: 10px;',
			items: [finishCombo]
		});
		var win = new Ext.Window({
			width: 320,
			height: 250,
			title: '工单完工',
			border: false,
			closeAction:'close',
			layout: 'fit',
			items: form,
			buttons: [{
				text: '保存',
				scope: this,
				iconCls : 'icon-save',
				handler: function(){
					if(Ext.isEmpty(finishCombo.getValue())){
						Alert("完工类型不能为空");return false;
					}
					var url = Constant.ROOT_PATH + "/core/x/Task!endTask.action";
					var o = {
						task_id : rs.get("task_id"), 
						resultType : finishCombo.getValue()
					};
					App.sendRequest( url, o, function(res,opt){
						this.grid.getStore().reload();
//						rs.set("task_status",teamCombo.getValue());
//						rs.set("task_status_text",teamCombo.getStore().getAt(index).get('dept_name'));							
						win.close();
					});
				}
			},{
				text: '取消',
				handler: function(){
					win.hide();
				}
			}]
		});
		win.show();
	},
	doVisitTask:function(){
	
	},
	getSelections: function(){
		var rs = this.grid.getSelectionModel().getSelected();
		if(rs == null || Ext.isEmpty(rs.get('task_id'))){
			Alert("请选择需要操作的记录!");
			return false;
		}
		return rs;
	},
	//search task from remote
	doSearchTask: function(){
		var o = {
			"taskCond.startTime": Ext.util.Format.date(this.createStartDateField.getValue(), "Y-m-d"),
			"taskCond.EndTime": Ext.util.Format.date(this.createEndDateField.getValue(), "Y-m-d"),
			"taskCond.custNo": this.custNoField.getValue(),
			"taskCond.status": this.taskStatusCombo.getValue(),
			"taskCond.mobile": this.mobileField.getValue(),
			"taskCond.addr": this.newaddrField.getValue(),
			"taskCond.custName":this.custNameField.getValue(),
			"taskCond.taskTeam":this.taskTeamCombo.getValue(),
			"taskCond.taskId":this.taskNoField.getValue(),
			"taskCond.taskType":this.taskDetailTypeCombo.getValue()
		};
		this.taskStore.baseParams = o;
		this.taskStore.load({
			params: {
				"start": 0, 
				"limit": this.pageSize
			}
		});
	}
});		
