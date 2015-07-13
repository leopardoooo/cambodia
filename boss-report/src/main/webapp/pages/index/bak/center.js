
FirstPanel = Ext.extend(Ext.Panel,{
	xtype: 'panel',
	width: "50%",
	height: "50%",
	region: 'center',
	border: false,
	iconCls:'icon-optr',
	title: '我的报表',
	collapsible: true,
	constructor:function(){
		this.myReportGrid = new MyReportGrid();
		FirstPanel.superclass.constructor.call(this,{
			layout:"border",
			border:false,
			items:[this.myReportGrid]
		})
	}
});
SecondPanel = Ext.extend(Ext.Panel,{
	xtype: 'panel',
    width: "50%",
    height: "50%",
    region: 'center',
    border: false,
	iconCls:'icon-bulletin',
    title: '最近更新报表',
    collapsible: true,
    constructor:function(){
		this.logGrid = new LogGrid();
		SecondPanel.superclass.constructor.call(this,{
			layout:"border",
			border:false,
			items:[this.logGrid]
		})
	}
})
ThirdPanel = Ext.extend(Ext.Panel,{
	xtype: 'panel',
    width: "50%",
    height: "50%",
    region: 'center',
    border:false,
	iconCls:'icon-navigate',
    title: '营业报表',
    collapsible: true,
    constructor: function(){
    	this.busiGrid = new BusiGrid();
    	ThirdPanel.superclass.constructor.call(this,{
    		layout:"border",
			border:false,
			items:[this.busiGrid]
    	})
    }
})
FourthPanel = Ext.extend(Ext.Panel,{
	xtype: 'panel',
    width: "50%",
    height: "50%",
    region: 'center',
    border:false,
	iconCls:'icon-query',
    title: '财务报表',
    collapsible: true,
     constructor: function(){
    	this.financeGrid = new FinanceGrid();
    	ThirdPanel.superclass.constructor.call(this,{
    		layout:"border",
			border:false,
			items:[this.financeGrid]
    	})
    }
});
//我的报表grid
MyReportGrid = Ext.extend(Ext.grid.GridPanel,{
	region: 'center',
	myReportStore: null,
	isReload: true,
	constructor: function(){
		myReportGridThiz = this;
		this.myReportStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/query/RepDesign!queryMyReport.action",
			root: 'records',
			fields: ["res_id","res_name","res_type","res_status","url"],
			sortInfo:{
				field:'res_id',
				direction:'DESC'
			}
		});
		
		var cm = [
			{header:'报表ID',dataIndex:'res_id', width:50},
			{header:'报表名称',dataIndex:'res_name',width:150,renderer:function(v,metaV,record ,i){
					return "<a href='#' onclick=openRep('"+record.get("res_id")+"','"+record.get("res_name")+"') >"+v+"</a>";
				}},
			{header:'取消收藏', dataIndex: 'res_id',width:60, renderer: function(v, md, record , i ){
					return "<a href='#' onclick=cancel("+v+") style='color:blue'>取消</a>" ;
				}},
			{header:'报表类型',dataIndex:'res_type',width:60},
			{header:'所属',dataIndex:'res_status',width:60},
			{header:'数据源',dataIndex:'url'}
			
		];
		MyReportGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.myReportStore,
			columns:cm,
			bbar:  new Ext.PagingToolbar({store : this.myReportStore})
		});
		
		cancel = function(value){
		    Confirm("确认要取消吗?",this,function(){
				Ext.Ajax.request({
					url:root+"/query/RepDesign!deleteMyRep.action",
					params:{rep_id:value},
					success:function(res,opt){
						var data = Ext.decode(res.responseText);
						if(data==true) 
							myReportGridThiz.getStore().reload();
					}
				});
			});	
		},
		openRep=function(id,name){
			if(!Ext.getCmp(id+'MainPanel')){
					App.getApp().page.add({
						title : name,
						id : id+'MainPanel',
						closable: true,
						items : [new MainPanel(id,name)]
					});
			}
			App.getApp().page.activate(id+'MainPanel');

		}
	}
})
//最近更新报表grid
LogGrid = Ext.extend(Ext.grid.GridPanel,{
	LogStore: null,
	isReload: true,
	constructor: function(rep_id){
		this.LogStore = new Ext.data.JsonStore({
			root: 'records',
			url:Constant.ROOT_PATH + "/query/RepDesign!queryLog.action",
			baseParams :{rep_id:rep_id},
			fields: ["rep_id","rep_name","update_type","create_date","optr_login_name","remark"]
		});
		var cm = [
			{header:'报表名称',dataIndex:'rep_name',width:150},
			{header:'类型',dataIndex:'update_type'},
			{header:'更新时间',dataIndex:'create_date'},
			{header:'操作员',dataIndex:'optr_login_name',width:60},
			{header:'备注',dataIndex:'remark'}
		];
		LogGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.LogStore,
			columns:cm,
			bbar:  new Ext.PagingToolbar({store : this.LogStore})
		})
	},
	refresh:function(){
		this.LogStore.load();
	}
})
//营业报表Grid
BusiGrid = Ext.extend(Ext.grid.GridPanel,{
	region: 'center',
	BusiStore: null,
	isReload: true,
	constructor: function(){
		this.BusiStore = new Ext.data.JsonStore({
			root: 'records',
			url: Constant.ROOT_PATH + "/query/RepDesign!queryBusinessRep.action",
			fields: ["res_id","res_name","res_type","res_status","url"]
		});
		var cm = [
			{header:'报表ID',dataIndex:'res_id', width:80},
			{header:'报表名称',dataIndex:'res_name',width:150,renderer:function(v,metaV,record ,i){
					return "<a href='#' onclick=openRep('"+record.get("res_id")+"','"+record.get("res_name")+"') >"+v+"</a>";
				}},
			{header:'报表类型',dataIndex:'res_type',width:60},
			{header:'所属分类',dataIndex:'res_status',width:60},
			{header:'数据源',dataIndex:'url'}
		];
		BusiGrid.superclass.constructor.call(this,{
			region: 'center',
			store: this.BusiStore,
			columns: cm,
			bbar: new Ext.PagingToolbar({store: this.BusiStore})
		})
	}
})
//财务报表Grid
FinanceGrid = Ext.extend(Ext.grid.GridPanel,{
	region: 'center',
	FinanceStore: null,
	isReload: true,
	constructor: function(){
		this.FinanceStore = new Ext.data.JsonStore({
			root: 'records',
			url: Constant.ROOT_PATH + "/query/RepDesign!queryFinanceRep.action",
			fields: ["res_id","res_name","res_type","res_status","url"]
		});
		var cm = [
			{header:'报表ID',dataIndex:'res_id', width:80},
			{header:'报表名称',dataIndex:'res_name',width:150},
			{header:'报表类型',dataIndex:'res_type'},
			{header:'所属分类',dataIndex:'res_status'},
			{header:'数据源',dataIndex:'url'}
		];
		BusiGrid.superclass.constructor.call(this,{
			region: 'center',
			store: this.FinanceStore,
			columns: cm,
			bbar: new Ext.PagingToolbar({store: this.FinanceStore})
		})
	}
})
/**
 * 主面板
 * @class CenterPanel
 * @extends Ext.Panel
 */
CenterPanel = Ext.extend(Ext.Panel,{
	firstPanel: null,
	secondPanel: null,
	thirdPanel: null,
	fourthPanel: null,
	constructor: function(){
		this.firstPanel= new FirstPanel();
		this.secondPanel= new SecondPanel();
		this.thirdPanel = new ThirdPanel();
		this.fourthPanel = new FourthPanel();
		CenterPanel.superclass.constructor.call(this,{
			layout: "anchor",
			border: false,
			region: "center",
			items:[{
				anchor: "100% 50%",
				layout:'border',
				border: false,
				items:[{
					layout:'border',
					border: false,
					region:'center',
					margins:'10 5 10 5',
					items:[this.firstPanel]
				},{
					region:'east',
					width:"50%",
					layout:'border',
					border: false,
					margins:'10 5 10 5',
					items:[this.secondPanel]
				}]
			},{
				anchor: "100% 50%",
				layout:'border',
				border: false,
				items:[{
					layout:'border',
					border: false,
					region:'center',
					margins:'10 5 10 5',
					items:[this.thirdPanel]
				}
				,{
					region:'east',
					width:"50%",
					layout:'border',
					border: false,
					margins:'10 5 10 5',
					items:[this.fourthPanel]
				}
				]
			}]
		})
		
	},
	initEvents:function(){
		CenterPanel.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.firstPanel.myReportGrid.getStore().load();
			this.secondPanel.logGrid.getStore().load();
			this.thirdPanel.busiGrid.getStore().load();
			this.fourthPanel.financeGrid.getStore().load();		
		},this,{delay:3000});
	}
})
RepTabPanel = Ext.extend(Ext.TabPanel,{
	constructor : function(){
		RepTabPanel.superclass.constructor.call(this,{
			activeTab: 0,
			region : 'center',
			style: 'padding: 0px 3px 3px 0px;',
			defaults : {
				layout : 'fit'
			},
			items : [{
				title : '首页',
				items : [new CenterPanel()]
			}]
		})
	}
})