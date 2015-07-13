/**
 * 工单接口
 */
 
/***
 * 派单窗口
 */ 
TaskAssignWin = Ext.extend( Ext.Window, {
	form: null,
	parent: null,
	constructor: function(taskIds , records, parent){
		this.taskIds = taskIds;
		this.records = records;
		this.parent = parent;
		
		this.installerTeam = new Ext.form.ComboBox({
			fieldLabel: '施工部门',
			typeAhead: true,
			width: 180,
		    triggerAction: 'all',
		    mode: 'remote',
		    store: new Ext.data.JsonStore({
		    	url:root+'/config/Task!queryInstallerDept.action',
		   	 	autoDestroy:true,
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
		});
		
		this.form = new Ext.form.FormPanel({
			labelWidth: 80,
			region: 'center',
			border: false,
			defaultType: 'textfield',
			labelAlign: 'top',
			bodyStyle: 'padding-top: 10px;padding-left: 30px;',
			defaults: {
				//anchor: '90%'
			},
			items: [this.installerTeam]
		});
	
		TaskAssignWin.superclass.constructor.call(this,{
			width: 280,
			height: 150,
			title: '派单',
			id:'TaskAssignWinId',
			closeAction:'close',
			layout: 'fit',
			maximizable: false,
			items: [this.form],
			buttons: [{
				text: '保存',
				scope: this,
				handler: this.doSave
			}]
		});
	},
	doSave: function(){
		var team = this.installerTeam.getValue();
		if(team == ""){
			Alert("请选择施工部门!");
			return;
		}
		var that = this;
		Confirm("确定要派单吗?", this , function(){
			tip = Show();
			Ext.Ajax.request({
				url : root+'/config/Task!assignTask.action',
				params :{task_ids : this.taskIds,  team: team},
				success : function(res, ops) {
					tip.hide();
					tip = null;
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert('派单成功!', function() {
							that.close();
							that.parent.doSearchTask();
						});
					} else {
						Alert('派单失败');
					}
				}
			});
		});
	}
});
  
/***
 * 回单窗口
 */ 
TaskFinishWin = Ext.extend( Ext.Window, {
	form: null,
	constructor: function(parent){
		this.parent = parent;
		
		var failureCauseCombo = new Ext.ux.ParamCombo({
				width: 180,
				fieldLabel: '失败原因',
				hiddenName: 'fail_cause',
				paramName:'TASK_FAILURE_CAUSE',
				allowBlankItem: true,
				disabled: true
			});
		
		var finishTypeCombo = new Ext.ux.ParamCombo({
				fieldLabel: '完工类型',
				width: 180,
				hiddenName: 'succeed',
				allowBlank: false,
				paramName:'',
				value: 'T',
				allowBlankItem: true,
				listeners: {
					scope: this,
					change: function(f, newV , oldV){
						if(newV == "F"){
							failureCauseCombo.setDisabled(false);
						}else{
							failureCauseCombo.setDisabled(true);
							failureCauseCombo.setRawValue("");
						}
					}
				}
			});
		finishTypeCombo.getStore().loadData([{item_name: '完成', item_value: 'T'},{item_name: '失败', item_value: 'F'}]);
		
		var satisCombo = new Ext.ux.ParamCombo({
				allowBlank: true,
				fieldLabel: '满意度',
				hiddenName:'satisfaction',
				paramName:'SATISFACTION'
			});
		var completeDateComo = new Ext.ux.ParamCombo({
				allowBlank: true,
				fieldLabel: '施工完成时间',
				hiddenName: 'installer_time',
				paramName:'TASK_COMPLETE_TIME'
			});
		//初始化下拉框的数据 
		App.form.initComboData([failureCauseCombo,satisCombo,completeDateComo]);
		
		var team = this.parent.getSelectionModel().getSelected().get("assign_dept");
		this.installerCombo = new Ext.ux.form.LovCombo ({fieldLabel:'施工员',
					hiddenName:'installer_optr',allowBlank: false,
						forceSelection:true,selectOnFocus:true,editable:true,
						store:new Ext.data.JsonStore({
							url:root+'/config/Task!queryInstaller.action',
							autoDestroy:true,
					   	 	baseParams: {team: team},
					        fields: [ 'item_value', 'item_name' ]
						}),valueField: 'item_value',displayField: 'item_name',
						triggerAction:'all',mode: 'remote',width:150,listWidth:200,
						beforeBlur:function(){}
					});
					
		
		/*
		this.installerCombo = new Ext.form.ComboBox({
			fieldLabel: '施工员',
			typeAhead: true,
			width: 180,
		    triggerAction: 'all',
		    mode: 'remote',
		    allowBlank: false,
		    hiddenName: 'task.revisit_installer_id',
		    store: new Ext.data.JsonStore({
		    	url: root + '/core/x/Task!queryInstaller.action',
		   	 	autoDestroy:true,
		   	 	baseParams: {team: team},
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
		}); */
		
		this.form = new Ext.form.FormPanel({
			labelWidth: 100,
			region: 'center',
			border: false,
			defaultType: 'textfield',
			labelAlign: 'right',
			bodyStyle: 'padding-top: 10px;',
			defaults: {
				//anchor: '90%'
			},
			items: [finishTypeCombo,failureCauseCombo,this.installerCombo,satisCombo,
					{
						xtype : 'xdatetime',
						fieldLabel : '施工完成日期',
						width : 160,
						name:'installer_time',
						minText : '不能选择当日之前',
						timeWidth : 60,
						allowBlank : false,
						timeFormat : 'H:i',
						timeConfig : {
							increment : 60,
							editable:true,
							altFormats : 'H:i|H:i:s',
							minValue:'04:00',  
    						maxValue:'21:00',  
							allowBlank : false
						},
						dateFormat : 'Y-m-d'
						,dateConfig : {
							value:new Date().format('Y-m-d'),
							altFormats : 'Y-m-d|Y-n-d',
							minValue : new Date().format('Y-m-d'),
							allowBlank : false
						}
					},
			{
				fieldLabel: '客户评价',
				xtype: 'textarea',
				height: 40,
				anchor: '90%',
				name: 'satisfaction_remak'
			} ,{
				fieldLabel: '备注',
				anchor: '90%',
				xtype: 'textarea',
				height: 40,
				name: 'fail_remark'
			}]
		});
		
		TaskFinishWin.superclass.constructor.call(this,{
			width: 380,
			height: 500,
			title: '回单',
			id:'TaskFinishWinId',
			closeAction:'close',
			layout: 'border',
			items: [this.form],
			buttons: [{
				text: '保存',
				scope: this,
				handler: this.doSave
			},{
				text: '取消',
				scope: this,
				handler: function(){
					var that = this;
					that.close();
				}
			}]
		});
	},
	initEvents: function(){
		this.on("beforeclose", this.reset , this);
		TaskFinishWin.superclass.initEvents.call(this);
	},
	reset: function(){
		this.form.getForm().reset();
	},
	doSave: function(){
		var f = this.form.getForm();
		if(false === f.isValid()){
			Alert("回单信息填写不完整!");
			return ;
		}
		var all = {}; 
		var formone = f.getValues();
		for (var key in formone) {
           all["revisit." + key] = formone[key];
        }
		 
		var rs = this.parent.getSelectionModel().getSelected().data;
		all["revisit.installer_dept"] = rs["assign_dept"];
		all["revisit.work_id"] = rs["work_id"];
		all["cust_ids"] = [rs["cust_id"]];
		var that = this;
		Confirm("确定要保存回单信息吗?", this , function(){
			tip = Show();
			Ext.Ajax.request({
				url : root+'/config/Task!responseTask.action',
				params :all,
				success : function(res, ops) {
					tip.hide();
					tip = null;
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert('回单成功!', function() {
							that.close();
							that.parent.doSearchTask();
							that.form.getForm().reset();
						});
					} else {
						Alert('回单失败');
					}
				}
			});
			
		});
	}
});



var workInfoDto={
};

var custInfoHTML =
'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
  '<tr height=20>'+
    '<td colspan=4 align="center" %><b>客户信息</b></td>' +
  '</tr>' +
  '<tr height=20>'+
    '<td class="label" width=20%>客户名称:</td>' +
    '<td class="input" width=30%>&nbsp;{cust_name}</td>'+
    '<td class="label" width=20%>受理编号:</td>' +
    '<td class="input" width=30%>&nbsp;{cust_no}</td>'+
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">客户地址:</td>' +
    '<td class="input" colspan=3>&nbsp;{install_addr}</td>'+
  '</tr>'+
  '<tr height=24>'+
    '<td colspan=4 align="center"%><b>业务信息</b></td>' +
  '</tr>' +
  '<tr height=24>'+
    '<td class="label" width=20%>营业厅:</td>' +
    '<td class="input" width=30%>&nbsp;{dept_name}</td>'+
    '<td class="label" width=20%>操作员:</td>' +
    '<td class="input" width=30%>&nbsp;{books_optr_text}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">业务名称:</td>' +
    '<td class="input">&nbsp;{busi_name}</td>'+
    '<td class="label">操作日期:</td>' +
    '<td class="input">&nbsp;{create_time}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">旧地址:</td>' +
    '<td class="input">&nbsp;{old_addr}</td>'+
    '<td class="label">新地址:</td>' +
    '<td class="input">&nbsp;{install_addr}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">故障原因:</td>' +
    '<td class="input">&nbsp;{bug_cause_text}</td>'+
    '<td class="label">备注:</td>' +
    '<td class="input">&nbsp;{remark}</td>'+
  '</tr>' +
  '<tr height=24>'+
    '<td colspan=4 align="center" %><b>工单信息</b></td>' +
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">工单类型:</td>' +
    '<td class="input">&nbsp;{task_type_text}</td>'+
    '<td class="label">创建方式:</td>' +
    '<td class="input">&nbsp;{create_type_text}</td>'+
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">服务类型:</td>' +
    '<td class="input" colspan=3>&nbsp;'+
      '<tpl for="taskServList">'+
        '{serv_name}&nbsp;'+  
          '</tpl>'+
    '</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">预约日期:</td>' +
    '<td class="input">&nbsp;{books_time}</td>'+
    '<td class="label">创建日期:</td>' +
    '<td class="input">&nbsp;{create_time}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">工单状态:</td>' +
    '<td class="input">&nbsp;{task_status_text}</td>'+  
	'<td class="label">施工部门:</td>' +
    '<td class="input">&nbsp;{assign_dept_text}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">联系人:</td>' +
    '<td class="input">&nbsp;{task_cust_name}</td>'+
    '<td class="label">联系方式:</td>' +
    '<td class="input" colspan=3>电话:{tel}&nbsp;手机:{mobile}</td>'+  	
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">分派日期:</td>' +
    '<td class="input">&nbsp;{assign_time}</td>'+
    '<td class="label">完工日期:</td>' +
    '<td class="input">&nbsp;{installer_time}</td>'+  
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">完工部门:</td>' +
    '<td class="input">&nbsp;{installer_dept_text}</td>'+
    '<td class="label">完工人员:</td>' +
    '<td class="input">&nbsp;{installer_optr_text}</td>'+
  '</tr>' +
  '<tr height=24>'+
    '<td class="label">是否完工:</td>' +
    '<td class="input">&nbsp;{succeed_text}</td>'+  
    '<td class="label">客户评价:</td>' +
    '<td class="input">&nbsp;{satisfaction_remak}</td>'+
  '</tr>'+
  '<tr height=24>'+
  	'<td class="label">失败原因:</td>' +
    '<td class="input">&nbsp;{fail_cause_text}</td>'+  
    '<td class="label">回单备注:</td>' +
    '<td class="input" colspan=3>&nbsp;{fail_remark}</td>'+
  '</tr>'+ 
'</table>';
/**
 * 工单详细信息面板
// */
DetailsPanel = Ext.extend( Ext.Panel ,{
	region:'center',
	tpl: null,
	extInfoPanel: null,
	constructor: function(){
		this.tpl = new Ext.XTemplate( custInfoHTML );
		this.tpl.compile();
		DetailsPanel.superclass.constructor.call(this, {
			border: false,
			layout: 'anchor',
			anchor: '100%',
			autoScroll:true,
			bodyStyle: "background:#F9F9F9",
			defaults: {
					bodyStyle: "background:#F9F9F9"
				},
			items : [{xtype : "panel",
						border : false,
						bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
						html : this.tpl.applyTemplate(workInfoDto)
					}]
		});
	},refresh: function(){
		 this.tpl.overwrite(this.items.itemAt(0).body, workInfoDto);
		 this.remoteRefresh();
	},remoteRefresh:function(taskId){
		 if(!Ext.isEmpty(taskId)){
			//显示数据加载提示框
			Ext.Ajax.request({
				scope : this,
				url : root+'/config/Task!queryTaskByTaskId.action',
				params: { 
					"work_id":taskId
				},
				success: function(res,ops){
					var data = Ext.decode(res.responseText);
					if(data){
						workInfoDto = data;
					}else{
						workInfoDto = {};
					}
					this.refresh();
					//隐藏数据加载提示框
	  			 }
			});
		}
	}
});


/**
 工单详细信息窗口
**/
TaskDetailsWin = Ext.extend(Ext.Window,{
	detailsPanel:null,
	taskId:null,
	constructor:function(taskId){
		this.taskId=taskId;
		this.detailsPanel = new DetailsPanel();
		TaskDetailsWin.superclass.constructor.call(this,{
			width: 540,
			height: 550,
			title: '工单详细信息',
			closeAction:'hide',
			layout: 'border',
			items: [{
				region:"center",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 100%",
					layout:'fit',
					bodyStyle: 'border-top: none;border-left: none;',
					items:[this.detailsPanel]
				} ]
			}],
			buttons: [{
				text: '退出',
				scope: this,
				handler: function(){
					this.hide();
				}
			}]
		})
	},refreshInfo:function(){
		this.detailsPanel.remoteRefresh(this.taskId);
	}
});


TaskBaseForm = Ext.extend(Ext.grid.GridPanel , {
	createStartDateField: null,
	createEndDateField: null,
	custNoField: null,
	mobileField: null,
	newaddrField: null,
	taskDetailTypeCombo:null,
	taskTeamCombo:null,
	ivalidBtn:null,
	sendBtn:null,
	receiptBtn:null,
	printBtn:null,
	//初始化函数，当下拉框的参数值赋值后被调用，如有情况重写该函数。
	doInit: function(){
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		var startdate = new Date();
		startdate.setDate(startdate.getDate() -1);
		//create search field
		this.createStartDateField = new Ext.form.DateField({
				xtype: 'datefield',
				width: 90,
				format: 'Y-m-d',
				allowBlank: false,
				value: startdate
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
				width: 60,
				emptyText: '受理编号11'
			});
		this.mobileField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 80,
				emptyText: '联系电话'
			});
		this.newaddrField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 100,
				emptyText: '地址'
			});
		this.taskTeamCombo = new Ext.form.ComboBox({
			typeAhead: true,
			width: 100,
		    triggerAction: 'all',
		    mode: 'remote',
		    emptyText: '施工队',
		    editable : true,
		    store: new Ext.data.JsonStore({
		    	url:root+'/config/Task!queryInstallerDept.action',
		    	autoLoad : true,
		   	 	autoDestroy:true,
		        fields: [ 'item_value', 'item_name' ]
		    }),
		    valueField: 'item_value',
		    displayField: 'item_name'
		});
		
		this.taskDetailTypeCombo = new Ext.ux.ParamCombo({
				xtype: 'textfield',
				width: 80,
				emptyText: '工单类型',
				paramName:'TASK_TYPE',
				allowBlankItem: true
		});
		this.ivalidBtn = {
						text: '作废',
						tooltip: '作废选择的施工单',
						height: 35,
						width: 80,
						style: 'color: red;',
						scope: this,
						handler: this.cancelTask
					};
		this.sendBtn = 	{
						text: '派单',
						width: 80,
						tooltip: '对选中的施工单进行派单',
						height: 35,
						scope: this,
						handler: this.assignTask
					};
		this.receiptBtn ={
						text: '回单',
						width: 80,
						tooltip: '施工完成，填写回单信息',
						height: 35,
						scope: this,
						handler: this.responseTask
					};					
		this.printBtn ={
						width: 50,
						height: 35,
						tooltip: '打印施工单',
						scope: this,
						scale: 'large',
						iconCls: 'print_big',
						handler: this.doPrintTask
					};
	}
	,doSearchBaseTask: function(cmpId,statusValue){
		var o = {
			"taskCond.cStart": Ext.util.Format.date(this.createStartDateField.getValue(), "Y/m/d"),
			"taskCond.cEnd": Ext.util.Format.date(this.createEndDateField.getValue(), "Y/m/d"),
			"taskCond.custNo": this.custNoField.getValue(),
			"taskCond.status": statusValue,
			"taskCond.tel": this.mobileField.getValue(),
			"taskCond.newAddr": this.newaddrField.getValue(),
			"taskCond.installDept":this.taskTeamCombo.getValue(),
			"taskCond.taskType":this.taskDetailTypeCombo.getValue()
		};
		var cmp = Ext.getCmp(cmpId);
		
		cmp.getStore().baseParams = o;
		cmp.getStore().load({
			params : {
				start : 0,
				limit : Constant.DEFAULT_PAGE_SIZE
			}
		});
		this.doLayout();
	},
	doPrintBaseTask: function(cmpId){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var ps = [];
		for(var i=0;i< rs.length; i++){
			ps.push(rs[i].get("task_type")+"#" + rs[i].get("cust_id") + "#" + rs[i].get("work_id"));
		}
		Ext.Ajax.request({
			url:root+'/config/Task!queryPrintContent.action',
			params: {tasks: ps},
			success: function( res, ops){
				var data = Ext.decode(res.responseText);
				if(data.length > 0){
					Ext.getCmp(cmpId).getStore().reload();
					var html = PrintTools.getPageHTML(data[0].tpl, data);
					PrintTools.showPrintWindow(html);
				}else{
					Alert("没有需要打印的数据");
				}
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
			if(rs[i].get("task_status") != 'INIT' && rs[i].get("task_status") != 'INSTALL'){
				Alert("只有预约和施工中的工单才能派单!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		var win = Ext.getCmp('TaskAssignWinId');
		if(!win){
			win = new TaskAssignWin(taskIds , rs, this);
		}
		win.show();
	},
	cancelTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var taskIds = [];
		for(var i = 0; i< rs.length ; i++){
			var ts = rs[i].get("task_status");
			if(ts == 'END' || ts == 'CANCEL'||ts == 'WAITEND'){
				Alert("不允许作废!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		var that = this;
		Ext.MessageBox.prompt("备注","作废原因：",function(bu,txt){
			if(bu != 'cancel'){
	   			Confirm("确定要作废选中的工单吗?", this , function(){
	   				tip = Show();
					Ext.Ajax.request({
						url : root+'/config/Task!cancelTask.action',
						params :{task_ids : taskIds,cancel_remak:txt},
						success : function(res, ops) {
							tip.hide();
							tip = null;
							var rs = Ext.decode(res.responseText);
							if (true === rs.success) {
								Alert('工单已作废!', function() {
									that.close();
									that.doSearchTask();
								});
							} else {
								Alert('作废失败');
							}
						}
					});
				}); 
			}
		}); 
		
		
	},
	responseTask: function(){
		var rs = this.getSelections(false);
		if(rs === false){
			return ;
		}
		if(rs[0].get("task_status") != 'INSTALL'){
			Alert("只有施工中的工单才能回单!");
			return;
		}
		var win = Ext.getCmp('TaskFinishWinId');
		if(!win){
			win = new TaskFinishWin(this);
		}
		win.show();
	},
	getSelections: function(allowMoreRows){
		var rs = this.getSelectionModel().getSelections();
		if(rs.length == 0){
			Alert("请选择需要操作的记录!");
			return false;
		}else if(rs.length > 1 && allowMoreRows === false){
			Alert("仅能选择一条记录进行操作!");
			return false;
		}
		return rs;
	},
    doFilterBtns : function(keyId,rs){
    	var optrHtml = "", status = rs.get("task_status");
	 	if(status == 'INIT' || status == 'INSTALL'){
			optrHtml = "<a href='#' style='color: blue;font-weight: bold;' onclick=Ext.getCmp(\'"+keyId+"\').TaskManager_updatePlanTime(\'"+keyId+"\'); title='修改预约时间'>预约变更</a> ";
		}else if(status == 'END'){
			optrHtml = "<a href='#' style='color: blue;font-weight: bold;' onclick=Ext.getCmp(\'"+keyId+"\').TaskManager_updatePlanOptr(\'"+keyId+"\'); title='修改施工人员'>人员变更</a> ";
		}
			optrHtml += "<a href='#' style='color: blue;font-weight: bold;' onclick=Ext.getCmp(\'"+keyId+"\').TaskManager_QueryDetails(\'"+keyId+"\'); title='查看详细信息'>详细信息</a> ";
		return optrHtml;
    },
    TaskManager_QueryDetails : function(keyId){
		//获得选中的记录
		var rs =this.getSelectionModel().getSelected();
		//获得工单编号
		var taskId = rs.get('work_id');
		
		//根据工单查询工单基本信息
		
		//工单回单信息
		var taskDetailswin = new TaskDetailsWin(taskId);
		taskDetailswin.refreshInfo();
		taskDetailswin.show();

	},
    TaskManager_updatePlanTime : function(keyId){
		var record = Ext.getCmp(keyId).getSelectionModel().getSelected();
		var oldDate = record.get("books_time");
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
	
		var form = new Ext.form.FormPanel({
			labelWidth: 90,
			bodyStyle: 'padding-top: 10px;',
			items: [{
				xtype: 'displayfield',
				fieldLabel: '原预约时间',
				style: 'color: red;',
				value: "&nbsp;" + Ext.util.Format.date(oldDate, 'Y-m-d H:i')
			},{
				xtype : 'xdatetime',
				fieldLabel : '新预约时间',
				width : 160,
				name:'books_time',
				minText : '不能选择当日之前',
				timeWidth : 60,
				allowBlank : false,
				timeFormat : 'H:i',
				timeConfig : {
					increment : 60,
					editable:true,
					altFormats : 'H:i|H:i:s',
					minValue:'04:00',  
					maxValue:'21:00',  
					allowBlank : false
				},
				dateFormat : 'Y-m-d'
				,dateConfig : {
					altFormats : 'Y-m-d|Y-n-d',
					minValue : new Date().format('Y-m-d'),
					allowBlank : false
				}
			}]
		});
	
		var win = new Ext.Window({
			width: 280,
			height: 160,
			title: '变更',
			border: false,
			closeAction:'close',
			layout: 'fit',
			items: form,
			buttons: [{
				text: '保存',
				scope: this,
				handler: function(){
					tip = Show();
					var o = {
						work_id : record.get("work_id"),books_time: form.getForm().getValues()["books_time"]
					};
					Ext.Ajax.request({
						url : root+'/config/Task!modifyBooksTime.action',
						params :o,
						success : function(res, ops) {
							tip.hide();
							tip = null;
							var rs = Ext.decode(res.responseText);
							if (true === rs.success) {
								Alert('操作成功!', function() {
									win.close();
									Ext.getCmp(keyId).getStore().reload();
								});
							} else {
								Alert('操作失败');
							}
						}
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
	TaskManager_updatePlanOptr : function(keyId){
		var record = Ext.getCmp(keyId).getSelectionModel().getSelected();
		var taskId = record.get('work_id'); 
		var revisitInstallerOptr = record.get('installer_optr_text');	//旧施工人员
		var team = record.get("installer_dept");
		var form = new Ext.form.FormPanel({
			labelWidth: 90,
			bodyStyle: 'padding-top: 10px;',
			items: [{
				xtype: 'displayfield',
				fieldLabel: '原施工员',
				style: 'color: red;',
				value: "&nbsp;" + revisitInstallerOptr
			},{fieldLabel:'新施工员',xtype:'lovcombo',
				hiddenName:'new_revisit_installer_id',allowBlank: false,
					forceSelection:true,selectOnFocus:true,editable:true,
					store:new Ext.data.JsonStore({
						url : root+'/config/Task!queryInstaller.action',
						autoDestroy:true,
				   	 	baseParams: {team: team},
				        fields: [ 'item_value', 'item_name' ]
					}),valueField: 'item_value',displayField: 'item_name',
					triggerAction:'all',mode: 'remote',width:150,listWidth:200,
					beforeBlur:function(){}
				}]
		});
		
		var newRevisitComp = form.getForm().findField('new_revisit_installer_id');
		
		var win = new Ext.Window({
			width: 280,
			height: 160,
			title: '变更施工员',
			border: false,
			closeAction:'close',
			layout: 'fit',
			items: form,
			buttons: [{
				text: '保存',
				scope: this,
				handler: function(){
					if(form.getForm().isValid()){
						tip = Show();
						var o = {
							work_id : record.get("work_id"),newRevisitInstallerId: newRevisitComp.getValue()  
						};
						Ext.Ajax.request({
							url : root+'/config/Task!modifyPlanOptr.action',
							params :o,
							success : function(res, ops) {
								tip.hide();
								tip = null;
								var rs = Ext.decode(res.responseText);
								if (true === rs.success) {
									Alert('操作成功!', function() {
										win.close();
										Ext.getCmp(keyId).getStore().reload();
									});
								} else {
									Alert('操作失败');
								}
							}
						});						
					}
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
});


