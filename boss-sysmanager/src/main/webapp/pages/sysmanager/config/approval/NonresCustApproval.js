
NonresCustGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor: function(){
		this.appStore = new Ext.data.JsonStore({
			url:root+'/system/CustColony!queryNonresCustApp.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['app_id','app_code','app_name','status','status_text','remark']
		});
		this.appStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var columns = [
			{header:'审批单号',dataIndex:'app_code'},
			{header:'审批单名称',dataIndex:'app_name'},
			{header:'状态',dataIndex:'status_text'},
			{header:'备注',dataIndex:'remark'},
			{header:'操作',dataIndex:'app_id',renderer:function(value,meta,record){
					if(record.get('status') == 'USE')
						return '';
					return "<a href=# onclick=Ext.getCmp('nonresCustGridId').doDel('"+value+"')>删除</a>";
				}
			}
		];
		NonresCustGrid.superclass.constructor.call(this,{
			id:'nonresCustGridId',
			region : 'center',
			border:false,
			sm:new Ext.grid.RowSelectionModel(),
			store:this.appStore,
			columns:columns,
			tbar:[
				'-','关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.appStore,
	                width: 180,
	                hasSearch : true,
	                emptyText: '支持名称或单号模糊查询'
	            }),'->','-',
	            {text:'添加',iconCls:'icon-add',scope:this,handler:this.doAdd},'-'
			],
			bbar:new Ext.PagingToolbar({store:this.appStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			listeners:{
				scope:this,
				rowdblclick:this.doDbClick
			}
		});
	},
	doAdd: function(){
		var win = Ext.getCmp('nonresCustWin');
		if(!win)
			win = new NonresCustWin();
		win.show('新增审批单');
	},
	doDbClick: function(){
		var record = this.getSelectionModel().getSelected();
		var win = Ext.getCmp('nonresCustWin');
		if(!win)
			win = new NonresCustWin();
		win.show('修改审批单',record);
	},
	doDel: function(appId){
		Confirm('确定删除吗？',this ,function(){
			Ext.Ajax.request({
				url:root+'/system/CustColony!deleteNonresCustApp.action',
				params:{
					appId: appId
				},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data['success'] === true){
						Ext.getCmp('nonresCustGridId').getStore()
										.remove(Ext.getCmp('nonresCustGridId')
												.getSelectionModel()
												.getSelected());
					}
				}
			});
		});
	}
});

var NonresCustWin = Ext.extend(Ext.Window,{
	constructor: function(){
		NonresCustWin.superclass.constructor.call(this,{
			id:'nonresCustWin',
			title:'新增审批单',
			width:300,
			height:250,
			border:false,
			layout:'fit',
			closeAction:'close',
			items:[
				{id:'nonresCustFormId',xtype:'form',labelWidth:80,labelAlign:'right',bodyStyle:'padding-top:10px',items:[
					{xtype:'hidden',name:'app_id'},{xtype:'hidden',name:'status'},
					{xtype:'textfield',fieldLabel:'审批单号',name:'app_code',width:150,allowBlank:false,
						readOnly : true,
									value : parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
											+ parseInt(Math.random() * 10) + ""
					},
					{xtype:'textfield',fieldLabel:'审批名称',name:'app_name',width:150,allowBlank:false},
					{xtype:'textarea',fieldLabel:'备注',name:'remark',width:180,height:100}
				]}
			],
			buttons:[
				{text:'保 存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'取 消',iconCls:'icon-cancel',scope:this,handler:this.doCancel}
			]
		});
	},
	show: function(title,record){
		NonresCustWin.superclass.show.call(this);
		this.setTitle(title);
		if(record){
			Ext.getCmp('nonresCustFormId').getForm().loadRecord(record);
		}
	},
	doSave: function(){
		var form = Ext.getCmp('nonresCustFormId').getForm();
		if(form.isValid()){
			var values = form.getValues();
			var obj = {};
			for(var v in values){
				obj['nonresCustApp.'+v] = values[v];
			}
			Ext.Ajax.request({
				url:root+'/system/CustColony!updateNonresCustApp.action',
				params:obj,
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data['success'] === true){
						Ext.getCmp('nonresCustGridId').getStore().load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
						this.doCancel();
					}
				}
			});
		}
	},
	doCancel: function(){
		this.close();
	}
});

NonresCustApprovalView = Ext.extend(Ext.Panel,{
	constructor: function() {
			NonresCustApprovalView.superclass.constructor.call(this, {
					id : 'NonresCustApprovalView',
					layout : 'border',
					title : '集团审批单',
					closable : true,
					border : false,
					baseCls : "x-plain",
					items : [new NonresCustGrid()]
				})
		}
});