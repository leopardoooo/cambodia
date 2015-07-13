/**
 * 模板业务单据管理：新增，修改，删除
 */
App.pageSize=15;

AcctItemPanel = Ext.extend(Ext.grid.GridPanel,{
	acctItemStore:null,
	constructor: function(){
		this.acctItemStore = new Ext.data.JsonStore({
			url : root + '/config/BusiFee!queryAllVewAcctitem.action',
			root : 'records',
			totalProperty : 'totalProperty',
			fields:['acctitem_id','acctitem_name','acctitem_type','acctitem_type_text','printitem_id']
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		var columns = [
			sm,
			{header:'账目ID',dataIndex:'acctitem_id',width:80},
			{header:'账目名',dataIndex:'acctitem_name',width:80},
			{header:'账目类型',dataIndex:'acctitem_type_text',width:80}
		];
		
		AcctItemPanel.superclass.constructor.call(this,{
			title:'账目信息',
//			border:false,
			frame:true,
			width:'50%',
			region:'west',
			ds:this.acctItemStore,
			sm:sm,
			columns:columns,
			tbar : [' ', '输入关键字', ' ',
					new Ext.ux.form.SearchField({
//						id:'searchId',
						store : this.acctItemStore,
						width : 200,
						hasSearch : true,
						emptyText : '支持账目编号或名称'
				})],
			bbar : new Ext.PagingToolbar({store : this.acctItemStore}),
			listeners:{
				'rowdblclick':getProdByAcctItemId
			}
		});
	}
});

var getProdByAcctItemId = function(grid){
		var acctitemId = grid.getSelectionModel().getSelected().get('acctitem_id');
		Ext.Ajax.request({
			url : root + '/system/Prod!queryProdByAcctItemId.action',
			params:{acctItemId:acctitemId},
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data)
					prodPanel.getStore().loadData(data);
			}
		});
}

ProdPanel = Ext.extend(Ext.grid.GridPanel,{
	prodStore:null,
	constructor: function(){
		this.prodStore = new Ext.data.JsonStore({
			fields:['prod_id','prod_name','prod_type','prod_type_text','prod_desc']
		});
		
		var columns = [
			{header:'产品ID',dataIndex:'prod_id',width:80},
			{header:'产品名',dataIndex:'prod_name',width:80},
			{header:'产品类型',dataIndex:'prod_type_text',width:80},
			{id:'prod_desc_id',header:'产品描述',dataIndex:'prod_desc',width:80}
		];
		
		ProdPanel.superclass.constructor.call(this,{
			title:'产品信息',
			autoExpandColumn:'prod_desc_id',
//			border:false,
			frame:true,
			region : 'center',
			ds:this.prodStore,
			height:300,
			columns:columns
		});
	}
});
 			
Ext.onReady(function(){
	acctItemPanel = new AcctItemPanel();
	prodPanel = new ProdPanel();
	App.query =new Ext.Viewport({
		layout: 'border',
		items: [acctItemPanel,prodPanel]
	});
	acctItemPanel.getStore().load({params: { start: 0, limit: App.pageSize}});
//	prodPanel.getStore().load({params: { start: 0, limit: App.pageSize}});
	App.clearLoadImage();
});
  	
