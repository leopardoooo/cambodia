var columns = [
	{header:lsys('DepotIndexManager.columnNames')[0],align:'center',dataIndex:'transfer_no',width:60},
	{header:lsys('DepotIndexManager.columnNames')[1],align:'center',dataIndex:'depot_source_text',width:90,renderer:App.qtipValue},
	{header:lsys('DepotIndexManager.columnNames')[2],align:'center',dataIndex:'depot_order_text',width:80,renderer:App.qtipValue},
	{header:lsys('DepotIndexManager.columnNames')[3],align:'center',dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
	{header:lsys('DepotIndexManager.columnNames')[4],align:'center',dataIndex:'optr_name',width:75},
	{header:lsys('DepotIndexManager.columnNames')[5],align:'center',dataIndex:'create_time',width:110},
	{header:lsys('DepotIndexManager.columnNames')[6],align:'center',dataIndex:'confirm_optr_name',width:75},
	{header:lsys('DepotIndexManager.columnNames')[7],align:'center',dataIndex:'confirm_date',width:110},
	{header:lsys('DepotIndexManager.columnNames')[8],align:'center',dataIndex:'confirm_info',width:80,renderer:App.qtipValue},
	{id:'remark_id',align:'center',header:lsys('DepotIndexManager.columnNames')[9],width:70,dataIndex:'remark',renderer:App.qtipValue}
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
			title:lsys('DepotIndexManager.titleExamInPanel'),
			autoScroll:true,
			ds:this.examInStore,
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
			title:lsys('DepotIndexManager.titleExamOutPanel'),
			autoScroll:true,
			ds:this.examOutStore,
			columns:columns
		});
	}
});

DepotIndexManager = Ext.extend(Ext.Panel,{
	examInPanel: null,
	examInTodayPanel: null,
	examOutPanel: null,
	examOutTodayPanel: null,
	constructor:function(){
		this.examInPanel = new ExamInPanel();
		this.examOutPanel = new ExamOutPanel();
		
		DepotIndexManager.superclass.constructor.call(this,{
			title:lsys('DepotIndexManager._title'),
			closable:false,
			layout:'anchor',
			items:[
				{anchor:'100% 50%',layout:'fit',border:false,items:[this.examInPanel]},
				{anchor:'100% 50%',layout:'fit',border:false,items:[this.examOutPanel]}
			]
		}
		);
	}
});