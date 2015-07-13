LogViewGrid = Ext.extend(Ext.grid.GridPanel,{
	logStore : null,
	constructor: function(){
		this.logStore = new Ext.data.JsonStore({
			autoLoad : true,
			url:root+'/system/Index!queryLogs.action',
			baseParams :{start:0,limit:Constant.DEFAULT_PAGE_SIZE},
			fields:['done_date','func_code','func_code_text','rec_id','rec_name','optr_id','optr_name',
				'county_id','area_id','dept_id','dept_name'],
			totalProperty:'totalProperty',
			root:'records'
		});
		var cm =[
			{header:'操作时间',dataIndex:'done_date'},
			{header:'操作员',dataIndex:'optr_name'},
			{header:'部门',dataIndex:'dept_name'},
			{header:'操作类型',dataIndex:'func_code_text',renderer:App.qtipValue},
			{header:'操作对象',dataIndex:'rec_name',renderer:App.qtipValue}
		];
		LogViewGrid.superclass.constructor.call(this, {
			border:false,
			region:'center',
			store:this.logStore,
			columns:cm,
			sm : new Ext.grid.CheckboxSelectionModel({}),
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store:this.logStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar : [' ', ' ', '输入关键字', ' ',
					new Ext.ux.form.SearchField({
								store : this.logStore,
								width : 200,
								hasSearch : true,
								emptyText : '支持操作员模糊查询'
							})]
		});
	}
})

LogView = Ext.extend(Ext.Panel,{
	logViewGrid : null,
	constructor:function(){
		this.logViewGrid = new LogViewGrid();		
		LogView.superclass.constructor.call(this,{
			id:'LogView',
			title:'操作记录',
			closable:true,
			border:false,
			layout:'border',
			items:[this.logViewGrid]
		});
	},
	initEvents : function(){
		this.on('activate',function(){
			this.logViewGrid.getStore().reload();
		},this)
		
		LogView.superclass.initEvents.call(this);
	}
});