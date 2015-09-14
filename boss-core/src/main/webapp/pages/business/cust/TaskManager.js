/**工单管理*/

/**
 * 修改工单
 */
TaskManager_update = function(){
	
	var rs = Ext.getCmp('taskManagerPanelId').grid.getSelectionModel().getSelected();
	if(!rs){
		Alert("请选择一条需要修改的工单");
	}
	
	var oldDate = rs.get("books_time");
	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);

	this.bugCauseCombo = new Ext.ux.ParamCombo({
		fieldLabel:'故障类型',
		xtype:'paramcombo',
		allowBlank:false,
		hiddenName:'bug_cause',
		anchor: '95%',
		paramName:'TASK_BUG_CAUSE'
	});
	
	var form = new Ext.form.FormPanel({
		labelWidth: 90,
		bodyStyle: 'padding-top: 10px;',
		items: [{
				xtype:'textfield',
				name: 'task_cust_name',
				value: rs.get("task_cust_name"),
				fieldLabel: '联系人'
			},{
				xtype:'textfield',
				name: 'tel',
				value: rs.get("tel"),
				fieldLabel: '联系电话'
			},{
				xtype : 'xdatetime',
				fieldLabel : '预约时间',
				width : 160,
				name:'books_time',
				minText : '不能选择当日之前',
				timeWidth : 60,
				timeFormat : 'H:i',
				value: rs.get("books_time"),			
				timeConfig : {
					increment : 60,
					editable:true,
					altFormats : 'H:i|H:i:s',
					minValue:'04:00',  
					value: Ext.util.Format.date(oldDate, 'H:i'),
					maxValue:'21:00' 
				},
				dateFormat : 'Y-m-d'
				,dateConfig : {
					value: Ext.util.Format.date(oldDate, 'Y-m-d'),
					altFormats : 'Y-m-d|Y-n-d'
				}
			},{
				xtype:'textarea',
				name: 'remark',
				height:40,
				value: rs.get("remark"),
				fieldLabel: '备注'
			}]
	});
	//初始化下拉框的数据 
	App.form.initComboData([this.bugCauseCombo],function(){
		if(rs.get("task_type") == 'WX'){
			form.add(this.bugCauseCombo);
			this.bugCauseCombo.setValue(rs.get("bug_cause"));
			form.doLayout();			
		}
	},this);
	var win = new Ext.Window({
		width: 320,
		height: 360,
		title: '变更',
		border: false,
		closeAction:'close',
		layout: 'fit',
		items: form,
		buttons: [{
			text: '保存',
			scope: this,
			iconCls : 'icon-save',
			handler: function(){
				var url = Constant.ROOT_PATH + "/core/x/Task!modifyTask.action";
				var o = {
					task_id : rs.get("work_id"), 
					booksTime: form.getForm().getValues()["books_time"],
					taskCustName: form.getForm().getValues()["task_cust_name"], 
					tel: form.getForm().getValues()["tel"], 
					remark: form.getForm().getValues()["remark"],
					bugCause : form.getForm().getValues()["bug_cause"]
				};
				App.sendRequest( url, o, function(res,opt){
					rs.set("books_time", o.booksTime);
					rs.set("task_cust_name", o.taskCustName);
					rs.set("tel", o.tel);
					rs.set("remark", o.remark);	
					if(o.bugCause != null){
						rs.set("bug_cause",o.bugCause);
						var index = 0;
						index = this.bugCauseCombo.getStore().find('item_value',o.bugCause);
						rs.set("bug_cause_text",this.bugCauseCombo.getStore().getAt(index).get('item_name'));
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
}


UserDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	userDetailStore : null,
	constructor : function() {
		this.userDetailStore = new Ext.data.JsonStore({
					fields : ['cust_name', 'cust_no', 'busi_name','optr_id','optr_name',
							'fee_name', 'real_pay', 'create_time', 'status', 'status_text']
				});
		UserDetailGrid.superclass.constructor.call(this, {
					ds : this.userDetailStore,
					viewConfig : {
						forceFit :true
					},
					sm : new Ext.grid.CheckboxSelectionModel(),
					cm : new Ext.grid.ColumnModel([{
								header : '客户名称',
								dataIndex : 'cust_name',
								width : 100,
								renderer : App.qtipValue
							}, {
								header : '客户编号',
								dataIndex : 'cust_no',
								renderer : App.qtipValue
							}, {
								header : '业务名称',
								dataIndex : 'busi_name',
								renderer : App.qtipValue
							}, {
								header : '费用名称',
								dataIndex : 'fee_name',
								renderer : App.qtipValue
							}, {
								header : '实际金额',
								dataIndex : 'real_pay',
								renderer : Ext.util.Format.formatFee
							}, {
								header : '操作时间',
								dataIndex : 'create_time',
								renderer : App.qtipValue
							}])
				})
	}
})

TaskDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	taskDetailStore : null,
	constructor : function() {
		this.taskDetailStore = new Ext.data.JsonStore({
					fields : ['user_type_text', 'user_type', 'user_name','password','device_model',
							'device_model_text', 'real_pay', 'create_time', 'status', 'status_text']
				});
		TaskDetailGrid.superclass.constructor.call(this, {
					ds : this.taskDetailStore,
					viewConfig : {
						forceFit :true
					},
					sm : new Ext.grid.CheckboxSelectionModel(),
					cm : new Ext.grid.ColumnModel([{
								header : '客户名称',
								dataIndex : 'cust_name',
								width : 100,
								renderer : App.qtipValue
							}, {
								header : '客户编号',
								dataIndex : 'cust_no',
								renderer : App.qtipValue
							}, {
								header : '业务名称',
								dataIndex : 'busi_name',
								renderer : App.qtipValue
							}, {
								header : '费用名称',
								dataIndex : 'fee_name',
								renderer : App.qtipValue
							}, {
								header : '实际金额',
								dataIndex : 'real_pay',
								renderer : Ext.util.Format.formatFee
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
				id:'add_btn_id',
				text: '新增工单',
				disabled:true,
				tooltip: '新增业务单,报修单',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.addTask
			},{
				id:'ivalid_btn_id',
				text: '作废',
				disabled:true,
				tooltip: '作废选择的施工单',
				height: 30,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.cancelTask
			},{
				id:'send_btn_id',
				text: '提交呼叫',
				disabled:true,
				width: 80,
				tooltip: '工单传给呼叫',
				height: 30,
				scope: this,
				handler: this.assignTask
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
			fields:['work_id','cust_id','cust_no','task_cust_name','tel','install_addr','task_type',
					'task_status','task_status_text','task_type_text','task_finish_type','books_time',{
						name: 'create_time',
						type: 'date',
						dateFormat: 'Y-m-d H:i:s'
					},{
						name: 'installer_time',
						type: 'date',
						dateFormat: 'Y-m-d H:i:s'
					},"books_optr","books_optr_text","busi_code","busi_name","bug_cause","bug_cause_text","remark"],
			root : 'records',
			totalProperty : 'totalProperty',
			autoDestroy : true
		});
		
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		var startDate = new Date();
		startDate.setDate(startDate.getDate() - 1);
//		startDate.setFullYear(startDate.getFullYear()-5);
		//create search field
		this.createStartDateField = new Ext.form.DateField({
				xtype: 'datefield',
				width: 90,
				format: 'Y-m-d',
				allowBlank: false,
				value: startDate
			});
		this.createEndDateField = new Ext.form.DateField({
				xtype: 'datefield',
				width: 90,
				format: 'Y-m-d',
				allowBlank: false,
				value: tomorrow
			});
		this.custNoField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 90,
				emptyText: '客户编号'
			});
		this.taskNoField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 90,
				emptyText: '工单编号'
			});			
		this.custNameField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 90,
				emptyText: '客户名称'
			});
		this.mobileField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 100,
				emptyText: '联系电话'
			});
		this.newaddrField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 140,
				emptyText: '地址'
			});
		this.taskStatusCombo = new Ext.ux.ParamCombo({
				xtype: 'textfield',
				width: 120,
				emptyText: '工单状态',
				paramName:'TASK_STATUS',
				allowBlankItem: true
			});		
		
		this.taskDetailTypeCombo = new Ext.ux.ParamCombo({
				xtype: 'textfield',
				width: 120,
				emptyText: '工单类型',
				paramName:'TASK_DETAIL_TYPE',
				allowBlankItem: true
		});
		
		this.taskAddrCombo = new Ext.form.ComboBox({
			typeAhead: true,
			width: 120,
		    triggerAction: 'all',
		    mode: 'remote',
		    emptyText: '地区',
		    editable : true,
		    store: new Ext.data.JsonStore({
//		    	url:root+'/config/Task!queryInstallerDept.action',
		    	autoLoad : true,
		   	 	autoDestroy:true,
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
		});
		
		this.taskTeamCombo = new Ext.form.ComboBox({
			typeAhead: true,
			width: 120,
		    triggerAction: 'all',
		    mode: 'remote',
		    emptyText: '施工队',
		    editable : true,
		    store: new Ext.data.JsonStore({
//		    	url:root+'/config/Task!queryInstallerDept.action',
		    	autoLoad : true,
		   	 	autoDestroy:true,
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
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
    	var sm = new Ext.grid.CheckboxSelectionModel();
	    this.grid = new Ext.grid.GridPanel({
	        store: this.taskStore,
	        cm: new Ext.ux.grid.LockingColumnModel({
	        	columns:[
	        	sm,
				//{header: '工单编号',		dataIndex : 'work_id', 				width: 100},
				{header: '创建时间', 		dataIndex: 'create_time', 		width: 165, renderer: Ext.util.Format.dateRenderer("Y年m月d日H点i分s秒")},
				{header: '预约时间', 		dataIndex: 'books_time', width: 140},
				{header: '客户受理号',	dataIndex : 'cust_no', 				width: 80},
				{header: '联系人', 		dataIndex: 'task_cust_name', 				width: 80},
				{header: '联系电话', 	dataIndex : 'tel', 				width: 100},
				{header: '地址', 		dataIndex : 'install_addr', 			width: 200},
				{header: '业务名称',		dataIndex : 'busi_name', 	width: 100, renderer: function(v, m ,rs){
					return "<span style='font-weight: bold;'>"+ v +"</span>";
				}},
				
				{header: '工单类型',		dataIndex : 'task_type_text', 	width: 100, renderer: function(v, m ,rs){
					return "<span style='font-weight: bold;'>"+ v +"</span>";
				}},
				{header:'故障原因',dataIndex:'bug_cause_text',width:85},
				{header: '工单状态', 		dataIndex: 'task_status', width: 100, renderer: function(v, m ,rs){
					var text = rs.get("task_status_text");
					var color = "black";
					if(v == 'INIT'){
						color = "purple";
					}else if(v == 'CREATE'){
						color = "orange";
					}else if(v == 'COMPLETE'){
						color = "green";
					}else if(v == 'CANCEL'){
						color = "gray";
					}
					return "<span style='font-weight: bold;color: "+ color +";'>"+ text +"</span>";
				}},							
				{header : "操作", menuDisabled : true, locked: true, dataIndex:'xxx', width : 80,
				 renderer:function(v,m,rs,rIndex,cIndex,store){
				 	var optrHtml = "", status = rs.get("task_status");
				 	if(status == 'CREATE'){
						optrHtml = "<a href='#' style='color: blue;font-weight: bold;' id='TaskManager_update_btn' onclick='TaskManager_update();' title='修改工单信息'>修改</a> ";
					}
					return optrHtml;
				}},
				{header:'施工备注',dataIndex:'remark',width:100,renderer:App.qtipValue}
	        ]}),
	        view: new Ext.ux.grid.ColumnLockBufferView({
	        	getRowClass: function(record,index){
		            if(Ext.util.Format.date(record.get('books_time'), 'Y-m-d') < nowDate().format('Y-m-d') && record.get('task_status')=='CREATE' ){ 
		                return 'red-row';  
	                }
	                return '';  
		        }  
	        }),
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
//					threeTbar.render(this.tbar);
//					fourTbar.render(this.tbar);
				}
			}
	    });
	},
	createStartDateField: null,
	createEndDateField: null,
	custNoField: null,
	taskStatusCombo: null,
	mobileField: null,
	newaddrField: null,
	cancelTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var taskIds = [];
		for(var i = 0; i< rs.length ; i++){
			var ts = rs[i].get("task_status");
			if(ts == 'COMPLETE' || ts == 'CANCEL' || ts == 'INIT'){
				Alert("不允许作废!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		Ext.MessageBox.prompt("备注","作废原因：",function(bu,txt){
			if(bu != 'cancel'){
	   			Confirm("确定要作废选中的工单吗?", this , function(){
					App.sendRequest(
						Constant.ROOT_PATH + "/core/x/Task!cancelTask.action",
						{task_ids : taskIds,cancelRemark:txt},
						function(res,opt){
							Alert('工单已作废!');
							for(var i = 0; i< rs.length ; i++){
								rs[i].set("task_status", "CANCEL");
								rs[i].set("task_status_text", "作废");
								rs[i].set("remark", txt);
							}
						});
				}); 
			}
		}); 
		
		
	},
	assignTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var taskIds = [];
		for(var i = 0; i< rs.length ; i++){
			if(rs[i].get("task_status") != 'CREATE'){
				Alert("只有新建的工单才能提交呼叫!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		
		var that = this;
		Confirm("确定要提交呼叫吗?", this , function(){
			App.sendRequest(
				Constant.ROOT_PATH + "/core/x/Task!assignTask.action",
				{task_ids : taskIds},
				function(res,opt){
					Alert('提交成功!', function(){
						that.close();
					});
					that.doSearchTask();					
				});
		});
	},
	addTask:function(){
		var w  = new TaskAddWin();
		w.show();
		
	},
	getSelections: function(allowMoreRows){
		var rs = this.grid.getSelectionModel().getSelections();
		if(rs.length == 0){
			Alert("请选择需要操作的记录!");
			return false;
		}else if(rs.length > 1 && allowMoreRows === false){
			Alert("仅能选择一条记录进行操作!");
			return false;
		}
		return rs;
	},
	//search task from remote
	doSearchTask: function(){
		var o = {
			"taskCond.startTime": Ext.util.Format.date(this.createStartDateField.getValue(), "Y/m/d"),
			"taskCond.EndTime": Ext.util.Format.date(this.createEndDateField.getValue(), "Y/m/d"),
			"taskCond.custNo": this.custNoField.getValue(),
			"taskCond.status": this.taskStatusCombo.getValue(),
			"taskCond.mobile": this.mobileField.getValue(),
			"taskCond.newAddr": this.newaddrField.getValue(),
			"taskCond.taskDetailType":this.taskDetailTypeCombo.getValue()
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
