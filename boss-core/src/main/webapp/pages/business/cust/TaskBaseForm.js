/**
 * 
 */

UserDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	userDetailStore : null,
	constructor : function() {
		this.userDetailStore = new Ext.data.JsonStore({
					fields : ['user_type', 'user_type_text','login_name', 'device_model','device_model_text','task_id',
							'device_code', 'password','device_id','bandwidth','pos_no','occ_no','status_text','status_date','exp_date']
				});
		var userCols = lbc('home.tools.TaskManager.userCols');
		UserDetailGrid.superclass.constructor.call(this, {
			ds : this.userDetailStore,
			border: false,
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
						header : userCols[0],dataIndex : 'user_type_text',width : 70,renderer : App.qtipValue}, {
						header : userCols[3],dataIndex : 'device_model_text',width : 160,renderer : App.qtipValue}, {
						header : userCols[4],dataIndex : 'device_id',width : 120,renderer : App.qtipValue}, {
						header : userCols[7],dataIndex : 'bandwidth',width: 40,renderer : App.qtipValue},{
						header : userCols[1],dataIndex : 'login_name',width : 110,renderer : App.qtipValue}, {
						header : userCols[8],dataIndex : 'password',width: 60,renderer : App.qtipValue}, {
						header : userCols[9],dataIndex : 'status_text',width : 60,renderer : App.qtipValue}, {
						header : userCols[10],dataIndex : 'status_date',width : 110,renderer : App.qtipValue}, {
						header : userCols[11],dataIndex : 'exp_date',width : 100,renderer : App.qtipValue},{
						header : userCols[6],dataIndex : 'occ_no',width : 80,renderer : App.qtipValue}, {
						header : userCols[5],dataIndex : 'pos_no',width : 80,renderer : App.qtipValue}])
		})
	}
})

TaskDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	taskDetailStore : null,
	constructor : function() {
		this.taskDetailStore = new Ext.data.JsonStore({
					fields : ['busi_code', 'busi_name', 'optr_id','optr_name','log_time',
							'syn_status','error_remark','syn_status_text','delay_time','log_detail']});
		var operateCols = lbc('home.tools.TaskManager.operateCols');							
		TaskDetailGrid.superclass.constructor.call(this, {
			ds : this.taskDetailStore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			border: false,
			cm : new Ext.grid.ColumnModel([{
				header : operateCols[0],dataIndex : 'log_time',width : 130}, {
				header : operateCols[1],dataIndex : 'busi_name',width:180,renderer : App.qtipValue}, {
				header : operateCols[2],dataIndex : 'optr_name',width:70,renderer : App.qtipValue}, {
				header : operateCols[3],dataIndex : 'syn_status_text',width:120,renderer : App.qtipValue}, {
				header : operateCols[5],dataIndex : 'delay_time',width:70},
				{header : operateCols[4],dataIndex : 'log_detail',width:400,renderer :  App.qtipValue}
			])
		})               
	}                    
})

//相同客户 相同工单类型，除当前选中工单行以外的数据
TaskSameGrid = Ext.extend(Ext.grid.GridPanel, {
	taskSameStore : null,
	constructor : function() {
		this.taskSameStore = new Ext.data.JsonStore({
					fields : ['task_id','task_create_time','bug_type_text', 'bug_detail', 'task_finish_desc', 'team_id_text','installer_id_text',
						'task_status', 'task_status_text','task_finish_time','task_finish_type_text']});
		var cols = lbc('home.tools.TaskManager.samTaskCols');							
		TaskSameGrid.superclass.constructor.call(this, {
			ds : this.taskSameStore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			border: false,
			cm : new Ext.grid.ColumnModel([
				{header : cols[0],dataIndex : 'task_id',width : 70, renderer:function(value,metaData,record){
						if(value != ''){
							return '<div style="text-decoration:underline;font-weight:bold"    ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}else{
							return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}
					}
				},
				{header : cols[1],dataIndex : 'task_status',width:120, renderer: function(v, m ,rs){
					var text = rs.get("task_status_text");
					var color = "black";
					if(v == 'INIT'){
						color = "purple";
					}else if(v == 'END'){
						color = "green";
					}else if(v == 'CANCEL'){
						color = "gray";
					}
					return '<div  style="font-weight: bold;color: '+ color +';" ext:qtitle="" ext:qtip="' + text + '">' + text +'</div>';
				}},
				{header : cols[10],dataIndex : 'task_finish_type_text',width:120,renderer : App.qtipValue},
				{header : cols[7],dataIndex : 'task_finish_desc',width:150,renderer :  App.qtipValue},				
				{header : cols[4],dataIndex : 'bug_type_text',width:150,renderer :  App.qtipValue},
				{header : cols[5],dataIndex : 'bug_detail',width:150,renderer :  App.qtipValue},
				{header : cols[3],dataIndex : 'task_create_time',width:120,renderer : App.qtipValue},	
				{header : cols[9],dataIndex : 'task_finish_time',width:150,renderer:App.qtipValue},
				{header : cols[2],dataIndex : 'team_id_text',width:120,renderer : App.qtipValue},
				{header : cols[8],dataIndex : 'installer_id_text',width:120,renderer : App.qtipValue}
			]),
			listeners: {
				scope: this,
				cellclick: this.doCellClick
			}
		})               
	},
	doCellClick:function(grid, rowIndex, columnIndex, e) {
	    var record = grid.getStore().getAt(rowIndex);  // 返回Record对象 Get the Record
	    var fieldName = grid.getColumnModel().getDataIndex(columnIndex); // 返回字段名称 Get field name
	    if(fieldName == 'task_id'){
		    var data = record.get(fieldName);
		    if(!this.taskWin){
				this.taskWin = new TaskDetailWindow();
			}
			this.taskWin.show(data);
	    }
	}
});

TaskAllInfo = Ext.extend(Ext.TabPanel,{
	panel : null,
	userGrid : null,
	detail : null,
	constructor : function() {
		this.userGrid = new UserDetailGrid();
		this.detail = new TaskDetailGrid();
		this.sameTask = new TaskSameGrid();
		TaskAllInfo.superclass.constructor.call(this, {
				activeTab: 0,
				closable : true,
				defaults : {border: false,layout : 'fit'},
				items:[
					{title : lbc('home.tools.TaskManager._operateTitle'),items:[this.detail]},
					{title : lbc('home.tools.TaskManager._userTitle'),items:[this.userGrid]},
					{title : lbc('home.tools.TaskManager._historyTitle'),items:[this.sameTask]}
				]
		})
	},
	loadBaseData:function(rs){
		this.userGrid.getStore().loadData(rs.taskUserList);
		this.detail.getStore().loadData(rs.taskLogList);
		this.sameTask.getStore().loadData(rs.sameTaskList);
	}
})
var taskCols = lbc('home.tools.TaskManager.taskCols');

TaskBaseTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[10] +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.task_id ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[0] +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.task_type_id_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+taskCols[13]+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.cust_no ||""]}</td>',
			'<td class="label" width=20%>'+taskCols[1]+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.cust_name ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ taskCols[6] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.cust_tel ||""]}</td>',
		'<td class="label" width=20%>'+ taskCols[7] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.task_create_time ||""]}</td>',	
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[2] +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.task_status_text ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[21] +'：</td>',
			'<td class="input" width=30% colspan=3>&nbsp;{[values.task_status_date ||""]}</td>',
			
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[3] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.team_id_text ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[19] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.installer_id_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ taskCols[17] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.bug_phone ||""]}</td>',
		'<td class="label" width=20%>'+ taskCols[22] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.installer_id_tel ||""]}</td>',
	'</tr>',
		
		'<tpl if="values.old_addr">',
			'<tr height=24>',
				'<td class="label" width=20%>'+ taskCols[15] +'：</td>',
				'<td class="input" width=30% colspan=3>&nbsp;{[values.old_addr ||""]}</td>',
			'</tr>',
		'</tpl>',
		'<tr height=24>',
			'<tpl if="Ext.isEmpty(values.old_addr)">',
				'<td class="label" width=20%>'+ taskCols[5] +'：</td>',
			'</tpl>',
			'<tpl if="!Ext.isEmpty(values.old_addr)">',
				'<td class="label" width=20%>'+ taskCols[16] +'：</td>',
			'</tpl>',
			'<td class="input" width=30% colspan=3>&nbsp;{[values.new_addr ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ taskCols[24] +'：</td>',
		'<td class="input" width=30% colspan=3>&nbsp;{[values.remark ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[8] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.bug_type_text ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[20] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.cust_sign_no ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ taskCols[9] +'：</td>',
		'<td class="input" width=30% colspan=3>&nbsp;{[values.bug_detail ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[18] +'：</td>',
			'<td class="input" width=30% colspan=3>&nbsp;({[values.task_finish_type_text ||""]})&nbsp;{[values.task_finish_desc ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[11] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.linkman_name ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[12] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.linkman_tel ||""]}</td>',
		'</tr>',
	'</table>'
);

TaskDetailForm = Ext.extend(Ext.Panel,{
	tpl: null,
	constructor: function(){
		this.tpl = TaskBaseTemplate;
		this.tpl.compile();
		TaskDetailForm.superclass.constructor.call(this, {
			layout: 'anchor',
			anchor: '100%',
			autoScroll:true,
			bodyStyle: "background:#F9F9F9",
			defaults: {
					bodyStyle: "background:#F9F9F9"
				},
			items : [{xtype : "panel",border: false,
						bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
						html : this.tpl.applyTemplate({})
					}]
		});
	},
	reset:function(){//重置详细信息
		if(this.items.itemAt(0).getEl()){
			this.tpl.overwrite( this.items.itemAt(0).body, {});
		}
	},
	refresh:function(record){
		this.tpl.overwrite( this.items.itemAt(0).body, record);
	}
});

TaskDetailWindow = Ext.extend(Ext.Window, {
	taskDetailForm : null,
	taskAllInfo : null,
	constructor: function(){
		this.taskDetailForm = new TaskDetailForm();
		this.taskAllInfo = new TaskAllInfo();
		TaskDetailWindow.superclass.constructor.call(this, {
				layout : 'border',
				closable : true,
				width: 860,
				height: 550,
				title: lmain("doc.task.winTitle"),
				border: false,
				items : [{
							region : 'north',
							layout : 'fit',
							height: 310,
							border: false,
							items : [this.taskDetailForm]
						}, {
							region : 'center',
							layout : 'fit',
							split : true,
							border: false,
							items : [this.taskAllInfo]
						}]
		});
	},
	show: function(taskId){
		Ext.Ajax.request({
			scope : this,
			url: root + '/core/x/Task!queryAllTaskDetail.action' ,
			params : {
				task_id : taskId
			},
			success : function(res,opt){
				var rs = Ext.decode(res.responseText);
				this.taskAllInfo.loadBaseData(rs);
				this.taskDetailForm.refresh(rs.taskBaseInfo);
			}
		});
		TaskDetailWindow.superclass.show.call(this);
	}
});