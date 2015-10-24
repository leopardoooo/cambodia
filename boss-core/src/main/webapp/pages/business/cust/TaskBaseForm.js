/**
 * 
 */

UserDetailGrid = Ext.extend(Ext.grid.GridPanel, {
	userDetailStore : null,
	constructor : function() {
		this.userDetailStore = new Ext.data.JsonStore({
					fields : ['user_type', 'user_type_text','user_name', 'device_model','device_model_text','task_id',
							'device_code', 'password','device_id','prodname','pos_no','occ_no']
				});
		var userCols = lbc('home.tools.TaskManager.userCols');
		UserDetailGrid.superclass.constructor.call(this, {
			ds : this.userDetailStore,
			border: false,
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
						header : userCols[0],dataIndex : 'user_type_text',width : 70,renderer : App.qtipValue}, {
						header : userCols[1],dataIndex : 'user_name',width : 120,renderer : App.qtipValue}, {
						header : userCols[3],dataIndex : 'device_model_text',width : 160,renderer : App.qtipValue}, {
						header : userCols[4],dataIndex : 'device_id',width : 120,renderer : App.qtipValue}, {
						header : userCols[6],dataIndex : 'occ_no',width : 80,renderer : App.qtipValue}, {
						header : userCols[5],dataIndex : 'pos_no',width : 80,renderer : App.qtipValue}, {
						header : userCols[7],dataIndex : 'prodname',width: 120,renderer : App.qtipValue},{
						header : userCols[8],dataIndex : 'password',width: 80,renderer : App.qtipValue}])
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
				header : operateCols[1],dataIndex : 'busi_name',width:70,renderer : App.qtipValue}, {
				header : operateCols[2],dataIndex : 'optr_name',width:70,renderer : App.qtipValue}, {
				header : operateCols[3],dataIndex : 'syn_status_text',width:70,renderer : App.qtipValue}, {
				header : operateCols[5],dataIndex : 'delay_time',width:70},
				{header : operateCols[4],dataIndex : 'log_detail',width:400,renderer :  App.qtipValue}
			])
		})               
	}                    
})

TaskAllInfo = Ext.extend(Ext.TabPanel,{
	panel : null,
	userGrid : null,
	detail : null,
	constructor : function() {
		this.userGrid = new UserDetailGrid();
		this.detail = new TaskDetailGrid();
		TaskAllInfo.superclass.constructor.call(this, {
				border : false,
				activeTab: 0,
				closable : true,
				defaults : {border: false,layout : 'fit'},
				items:[{title : lbc('home.tools.TaskManager._operateTitle'),items:[this.detail]},
				{title : lbc('home.tools.TaskManager._userTitle'),items:[this.userGrid]
				}]
		})
	},
	loadBaseData:function(rs){
		this.userGrid.getStore().loadData(rs.taskUserList);
		this.detail.getStore().loadData(rs.taskLogList);
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
		'<td class="label" width=20%>'+ taskCols[2] +'：</td>',
		'<td class="input_bold" width=30%>&nbsp;{[values.task_status_text ||""]}</td>',
		'<td class="label" width=20%>'+ taskCols[3] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.team_id_text ||""]}</td>',
	'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ taskCols[5] +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.address ||""]}</td>',
			'<td class="label" width=20%>'+ taskCols[6] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.tel ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[7] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.task_create_time ||""]}</td>',	
			'<td class="label" width=20%>'+ taskCols[8] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.bug_type_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ taskCols[9] +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.bug_detail ||""]}</td>',
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
				height: 540,
				title: lmain("doc.task.winTitle"),
				border: false,
				items : [{
							region : 'north',
							layout : 'fit',
							height: 260,
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