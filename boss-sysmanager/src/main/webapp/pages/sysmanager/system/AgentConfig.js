/**
 * 代理商配置
 */
 
 AgentGrid = Ext.extend(Ext.grid.GridPanel, {
 	agentStore: null,
 	pageSize: 20,
 	constructor: function(){
 		this.agentStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryAgent.action',
			fields: ['id', 'name', 'agent_type', 'agent_type_text', 'tel', 'cert_type', 'cert_type_text', 'cert_num', 'busi_optr_id',
				'busi_optr_name', 'taxid', 'guarantee_info', 'create_time', 'optr_id', 'optr_name', 'remark'],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.agentStore.load({params:{start:0,limit:this.pageSize}});
		var columns = [
			{header: '名称', 		dataIndex: 'name', 				width: 120, renderer: App.qtipValue},
			{header: '类型', 		dataIndex: 'agent_type_text', 	width: 80, 	renderer: App.qtipValue},
			{header: '电话', 		dataIndex: 'tel', 				width: 80, 	renderer: App.qtipValue},
			{header: '证件类型', 	dataIndex: 'cert_type_text', 	width: 70, 	renderer: App.qtipValue},
			{header: '证件号码', 	dataIndex: 'cert_num', 			width: 80, 	renderer: App.qtipValue},
			{header: '发展人 ', 		dataIndex: 'busi_optr_name', 	width: 90, 	renderer: App.qtipValue},
			{header: '税号 ', 		dataIndex: 'taxid', 			width: 80, 	renderer: App.qtipValue},
			{header: '担保信息 ', 	dataIndex: 'guarantee_info', 	width: 100, renderer: App.qtipValue},
			{header: '创建时间 ', 	dataIndex: 'create_time', 		width: 120, renderer: App.qtipValue},
			{header: '创建人 ', 		dataIndex: 'optr_name', 		width: 90, 	renderer: App.qtipValue},
			{header: '备注 ', 		dataIndex: 'remark', 			width: 120, renderer: App.qtipValue},
			{header: '操作',			dataIndex: 'id', 				width: 70,	renderer: function(value){
				return '<a href=# onclick=Ext.getCmp("agentGridId").doUpdate('+value+')>修改</a>';
			}}
		];
 		AgentGrid.superclass.constructor.call(this,{
 			id: 'agentGridId',
 			columns: columns,
 			sm: new Ext.grid.RowSelectionModel({}),
 			store: this.agentStore,
 			border: false,
 			tbar:[' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
					id: 'agentSearchFieldId',
	                store: this.agentStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持代理商名称模糊查询'
	            }),
 				'->', '-',
 				{text: '添加', iconCls: 'icon-add', scope: this, handler: this.doAdd}, '-',
 				{text: '刷新', iconCls: 'icon-refresh', scope: this, handler: this.doRefresh}, '-'
 			],
 			bbar: new Ext.PagingToolbar({store: this.agentStore, pageSize: this.pageSize})
 		});
 	},
 	doRefresh: function(){
 		this.agentStore.removeAll();
 		this.agentStore.baseParams['query'] = Ext.getCmp('agentSearchFieldId').getValue();
 		this.agentStore.load();
 	},
 	doAdd:function(){
 		var win = new AgentWin(this);
		win.setTitle('新增代理商');
		win.show();
 	},
 	doUpdate: function(id){
		var win = new AgentWin(this);
		win.setTitle('修改代理商');
		win.show(this.getSelectionModel().getSelected());
 	}
 });
 
 AgentWin = Ext.extend(Ext.Window, {
 	constructor: function(grid){
 		this.grid = grid;
 		this.form = new Ext.form.FormPanel({
 			layout: 'column',
			border: false,
			plain: true,
			baseCls: 'x-plain',
			bodyStyle: 'background:#F9F9F9;padding-top:10px',
			labelWidth: 100,
			defaults:{
				columnWidth: .5,
				border: false,
				layout: 'form',
				baseCls: 'x-plain',
				defaultType: 'textfield'		
			},
			items:[{
					xtype: 'hidden',
					name: 'id'
				},{
				items:[{
					fieldLabel: '代理商名称',
					name: 'name',
					allowBlank: false
				},{
					fieldLabel: '发展人',
					hiddenName: 'busi_optr_id',
					xtype: 'paramcombo',
					paramName: 'OPTR'
				},{
					fieldLabel: '证件类型',
					hiddenName: 'cert_type',
					xtype: 'paramcombo',
					paramName: 'CERT_TYPE'
				},{
					fieldLabel: '担保信息',
					name: 'guarantee_info'
				}]
			},{
				items:[{
					fieldLabel: '代理商类型',
					hiddenName: 'agent_type',
					xtype: 'paramcombo',
					paramName: 'CUST_TYPE',
					allowBlank: false
				},{
					fieldLabel: '电话',
					name: 'tel'
				},{
					fieldLabel: '证件号码',
					name: 'cert_num'
				},{
					fieldLabel: '税号',
					name: 'taxid'
				}]
			},{
				columnWidth: 1,
				items:[{
					xtype: 'textarea',
					anchor: '98%',
					height: 80,
					fieldLabel: '备注',
					name: 'remark'
				}]
			}]
 		});
 		
 		AgentWin.superclass.constructor.call(this, {
 			layout: 'fit',
 			width: 550,
 			height: 300,
 			items: [this.form],
 			buttonAlign: 'center',
 			buttons: [
 				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
 				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
 			]
 		});
 	},
 	show: function(record){
 		
 		App.form.initComboData(this.findByType("paramcombo"), function(){
 			if(!Ext.isEmpty(record)){
	 			this.form.getForm().loadRecord(record);
	 		}
 		}, this);
 		AgentWin.superclass.show.call(this);
 	},
 	doSave: function(){
 		var form = this.form.getForm();
 		if(form.isValid()){
 			
 			var values = form.getValues(), agent = {};
 			for(var key in values){
 				agent['agent.'+key] = values[key];
 			}
 			
 			Ext.Ajax.request({
 				url: root+'/config/Config!saveAgent.action',
 				params: agent,
 				scope: this,
 				success: function(res, opt){
 					Alert('保存成功',function(){
	 					this.close();
	 					this.grid.doRefresh();
 					}, this);
 				}
 			});
 		}
 	}
 });
 
 AgentConfig = Ext.extend(Ext.Panel, {
    constructor: function () {
        this.grid = new AgentGrid(this);
        OnlineUser.superclass.constructor.call(this, {
            id: 'AgentConfig',
            title: '代理商配置',
            closable: true,
            border: false,
            layout : 'fit',
            items: [this.grid]
        });
    }
});