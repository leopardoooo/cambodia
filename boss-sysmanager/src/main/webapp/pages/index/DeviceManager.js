var columns = [
	{header:'流转编号',dataIndex:'transfer_no',width:55},
	{header:'源仓库',dataIndex:'depot_source_text',width:70},
	{header:'目标仓库',dataIndex:'depot_order_text',width:70},
	{header:'状态',dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
	{header:'创建人',dataIndex:'optr_name',width:45},
	{header:'创建时间',dataIndex:'create_time',width:110},
	{header:'确认人',dataIndex:'confirm_optr_name',width:45},
	{header:'确认时间',dataIndex:'confirm_date',width:110},
	{header:'确认信息',dataIndex:'confirm_info',width:70},
	{id:'remark_id',header:'备注',dataIndex:'remark'}
];
//待审批调入
var ExamInPanel = Ext.extend(Ext.grid.GridPanel,{
	examInStore :null,
	constructor:function(){
		this.examInStore = new Ext.data.JsonStore({
			url:'resource/Device!queryUnCheckInput.action',
			fields:['transfer_no','depot_source','depot_order','status','optr_id','create_time',
				'confirm_optr_id','confirm_date','confirm_info','remark',
				'depot_source_text','depot_order_text','status_text','optr_name','confirm_optr_name']
		});
		this.examInStore.load();
		ExamInPanel.superclass.constructor.call(this,{
			autoScroll:true,
			ds:this.examInStore,
			autoExpandColumn :'remark_id',
			columns:columns
		});
	}
});

//今日待审批调入
var ExamInTodayPanel = Ext.extend(Ext.grid.GridPanel,{
	examInStore :null,
	constructor:function(){
		this.examInTodayStore = new Ext.data.JsonStore({
			url:'resource/Device!queryTodayCheckInput.action',
			fields:['transfer_no','depot_source','depot_order','status','optr_id','create_time',
				'confirm_optr_id','confirm_date','confirm_info','remark',
				'depot_source_text','depot_order_text','status_text','optr_name','confirm_optr_name']
		});
		this.examInTodayStore.load();
		ExamInTodayPanel.superclass.constructor.call(this,{
			autoScroll:true,
			ds:this.examInTodayStore,
			autoExpandColumn :'remark_id',
			columns:columns
		});
	}
});

//待审批调出
var ExamOutPanel = Ext.extend(Ext.grid.GridPanel,{
	examOutStore :null,
	constructor:function(){
		this.examOutStore = new Ext.data.JsonStore({
			url:'resource/Device!queryUnCheckOutput.action',
			fields:['transfer_no','depot_source','depot_order','status','optr_id','create_time',
				'confirm_optr_id','confirm_date','confirm_info','remark',
				'depot_source_text','depot_order_text','status_text','optr_name','confirm_optr_name']
		});
		this.examOutStore.load();
		ExamOutPanel.superclass.constructor.call(this,{
			autoScroll:true,
			ds:this.examOutStore,
			autoExpandColumn :'remark_id',
			columns:columns
		});
	}
});

//今日待审批调出
var ExamOutTodayPanel = Ext.extend(Ext.grid.GridPanel,{
	examOutTodayStore :null,
	constructor:function(){
		this.examOutTodayStore = new Ext.data.JsonStore({
			url:'resource/Device!queryTodayCheckOutput.action',
			fields:['transfer_no','depot_source','depot_order','status','optr_id','create_time',
				'confirm_optr_id','confirm_date','confirm_info','remark',
				'depot_source_text','depot_order_text','status_text','optr_name','confirm_optr_name']
		});
		this.examOutTodayStore.load();
		ExamOutTodayPanel.superclass.constructor.call(this,{
			autoScroll:true,
			ds:this.examOutTodayStore,
			autoExpandColumn :'remark_id',
			columns:columns
		});
	}
});

DeviceManagerPanel = Ext.extend(Ext.ux.Portal,{
	examInPanel: null,
	examInTodayPanel: null,
	examOutPanel: null,
	examOutTodayPanel: null,
	constructor:function(){
		this.examInPanel = new ExamInPanel();
		this.examInTodayPanel = new ExamInTodayPanel();
		this.examOutPanel = new ExamOutPanel();
		this.examOutTodayPanel = new ExamOutTodayPanel();
		
		DeviceManagerPanel.superclass.constructor.call(this,{
			title:'终端管理',
			closable:false,
			layout:'column',
			items:[{
					columnWidth : 0.5,
					style:'padding:10px 5px 0 10px',
					defaults : {
						layout : "fit",
						height : 240
					},
					items : [
					{title:'待审批调入',items:this.examInPanel},
					{title:'今日审批调入',items:this.examInTodayPanel}]
				},{
					columnWidth : 0.5,
					style:'padding:10px 10px 0 5px',
					defaults : {
						layout : "fit",
						height : 240
					},
					items : [
						{title:'待审批调出',items:this.examOutPanel},
						{title:'今日审批调出',items:this.examOutTodayPanel}
					]
				}]
		}
		);
	}
});