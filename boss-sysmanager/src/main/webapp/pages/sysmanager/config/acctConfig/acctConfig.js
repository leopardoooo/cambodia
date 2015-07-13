
var acctConfigColonyStore =  new Ext.data.JsonStore({
	autoLoad : true,
	url  : root + '/config/AcctConfig!queryAllColony.action' ,
	fields : ['item_name','item_value']
});
acctConfigColonyStore.on('load',function(store){
	//插入一条空数据
	var Plant= store.recordType;
	var p = new Plant({
		'item_value': '','item_name': '客户群体...'
	});
	store.insert(0,p);
})

var acctConfigCountyStore = new Ext.data.JsonStore({
	autoLoad : true,
	url  : root + '/config/AcctConfig!queryAllCounty.action' ,
	fields : ['item_name','item_value']
});
acctConfigCountyStore.on('load',function(store){
	//插入一条空数据
	var Plant= store.recordType;
	var p = new Plant({
		'item_value': '','item_name': '分公司...'
	});
	store.insert(0,p);
})

AcctConfigGrid = Ext.extend(Ext.grid.GridPanel,{
	acctConfigStore : null,
	constructor : function(){
		this.acctConfigStore = new Ext.data.JsonStore({
			totalProperty:'totalProperty',
			root:'records',
			autoLoad : true,
			baseParams : {start:0,limit:Constant.DEFAULT_PAGE_SIZE},
			url  : root + '/config/AcctConfig!queryAcctConfig.action' ,
			fields : ['t_acct_id','county_id','county_name','optr_id','optr_name','initamount','balance',
				'cust_colony','cust_colony_text','config_year','create_time']
		});
//		this.acctConfigStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel({
			columns: [
				sm,
				{header:'分公司',dataIndex:'county_name'},
				{header:'客户群体',dataIndex:'cust_colony_text'},
				{header:'初始金额',dataIndex:'initamount',renderer:Ext.util.Format.formatFee},
				{header:'剩余金额',dataIndex:'balance',renderer:Ext.util.Format.formatFee},
				{header:'配置年份',dataIndex:'config_year',renderer:App.qtipValue},
				{header:'操作员',dataIndex:'optr_name',render:App.qtipValue},
				{header:'创建时间',dataIndex:'create_time'}
				,{header: '操作',width : 60,dataIndex: 't_acct_id',
		            renderer:function( v , md, record , i  ){
						return "<a href='#' onclick=Ext.getCmp(\'"+"acctConfigGrid"+"\').showDetail(); style='color:blue'> 使用明细</a>";
					}
				}
			]
		});
		
		AcctConfigGrid.superclass.constructor.call(this,{
			id : 'acctConfigGrid',
			title : '定额账户信息',
			border:false,
			store:this.acctConfigStore,
	        viewConfig : {
	        	forceFit : true
	        },
			cm:cm,
			sm: sm,
			bbar:new Ext.PagingToolbar({pageSize:Constant.DEFAULT_PAGE_SIZE,store:this.acctConfigStore}),
			tbar : ['查询条件',' ',new Ext.form.ComboBox({
				id : 'countySelectComboForQuery',
				store : acctConfigCountyStore,
				emptyText : '分公司...',
				valueField : 'item_value',
				displayField : 'item_name',
				mode: 'local',
				forceSelection : true,
				triggerAction : 'all',
				editable : false,
				listeners : {
					scope:this,
					select : this.selectParam
				}
			}),new Ext.form.ComboBox({
				id : 'colonySelectComboForQuery',
				store : acctConfigColonyStore,
				emptyText : '客户群体...',
				valueField : 'item_value',
				displayField : 'item_name',
				mode: 'local',
				forceSelection : true,
				triggerAction : 'all',
				editable : false,
				listeners : {
					scope:this,
					select : this.selectParam
				}
			}),'->',{
				text : '增加',
				iconCls : 'icon-add',
				scope:this,
				handler : this.addConfig
			},{
				text : '修改',
				iconCls : 'icon-modify',
				scope:this,
				handler : this.modifyConfig
			},{
				text : '删除',
				iconCls : 'icon-del',
				scope:this,
				handler : this.deleteConfig
			}]
		})
	},
	selectParam : function(){
		var countyId= Ext.getCmp('countySelectComboForQuery').getValue();
		var colony = Ext.getCmp('colonySelectComboForQuery').getValue();
		var all  = {};
		if(countyId){
			all['countyId'] = countyId;
		}
		if(colony){
			all['colony'] = colony;
		}
		this.getStore().load({
			params : all
		})
	},
	addConfig : function(){
		new AcctConfigWin().show();
	},
	modifyConfig : function(){
		var records = this.getSelectionModel().getSelections();
		new AcctConfigWin().show(records);
	},
	deleteConfig : function(){
		var records = this.getSelectionModel().getSelections();
		if(records.length == 0){
			Alert("请选择数据");
			return;
		}
		var acctIds = [];
		for(var i=0;i<records.length;i++){
			acctIds.push(records[i].get('t_acct_id'));
		}
		Confirm("确定删除吗?", this ,function(){
			mb = Show();//显示正在提交
			Ext.Ajax.request({
				scope : this,
				params : {
					acctIds : acctIds
				},
				url  : root + '/config/AcctConfig!deleteAcctConfig.action' ,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							this.getStore().reload();
						}else{
							Alert('操作失败');
						}
				}
			})
		});
	},
	showDetail : function(){
		var record= this.getSelectionModel().getSelected();
		new AcctConfigChangeWin().show(record.get('t_acct_id'));
	}
})

AcctConfigChangeWin = Ext.extend(Ext.Window,{
	detailStore : null,
	constructor : function(){
		this.detailStore = new Ext.data.JsonStore({
			totalProperty:'totalProperty',
			root:'records',
			baseParams : {start : 0,limit:Constant.DEFAULT_PAGE_SIZE},
			url  : root + '/config/AcctConfig!queryAcctDetail.action' ,
			fields : ['t_acct_id','optr_id','optr_name','done_date','change_amount','done_code']
		});
		
		var cm = new Ext.grid.ColumnModel({
			columns : [
				{header:'操作员',dataIndex:'optr_name'},
				{header:'变化金额',dataIndex:'change_amount',renderer:Ext.util.Format.formatFee},
				{header:'操作时间',dataIndex:'done_date'},
				{header:'业务流水',dataIndex:'done_code'}
			]
		})
		
		var grid = new Ext.grid.GridPanel({
			border : false,
			store : this.detailStore,
			viewConfig : {
	        	forceFit : true
	        },
			cm:cm,
			bbar:new Ext.PagingToolbar({pageSize:Constant.DEFAULT_PAGE_SIZE,store:this.detailStore})
		})
		
		AcctConfigChangeWin.superclass.constructor.call(this,{
			title : '定额账户明细',
			closeAction : 'hide',
			width : 400,
			height : 400,
			layout : 'fit'
			,items : [grid]
		})
	},
	show : function(acctId){
		this.detailStore.load({
			params : {acctId : acctId}
		});
		AcctConfigChangeWin.superclass.show.call(this);
	}
})


AcctConfigWin = Ext.extend(Ext.Window,{
	configGrid : null,
	constructor : function(){
		this.configGrid = new AcctConfigEditorGrid();
		AcctConfigWin.superclass.constructor.call(this,{
			title : '配置定额账户',
			closeAction : 'close',
			width : 500,
			height : 400,
			layout : 'fit',
			items : [this.configGrid],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	show: function(records){
		if(records){
			var dataArr = [];
			for(var i=0;i<records.length;i++){
				var obj = {};
				for(var key in records[i].data){
					obj[key]=records[i].data[key];
				}
				obj.balance = obj.balance/100;
				dataArr.push(obj);
			}
			this.configGrid.loadData(dataArr);
		}
		
		AcctConfigWin.superclass.show.call(this);
	},
	doSave: function(){
		var data = this.configGrid.getValues();
		if(data.length == 0){
			Alert('没有数据需要保存!')
			return;
		}
		Confirm("确定保存吗?", this ,function(){
			mb = Show();//显示正在提交
			Ext.Ajax.request({
				scope : this,
				params : {
					acctListStr : Ext.encode(data)
				},
				url  : root + '/config/AcctConfig!saveAcctConfig.action' ,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							this.close();
							Ext.getCmp('acctConfigGrid').getStore().reload();
						}else{
							Alert('操作失败');
						}
				}
			})
		});
	}
})

AcctConfigEditorGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	configStore : null,
	constructor : function(){
		this.configStore = new Ext.data.JsonStore({
			url  : root + '/config/AcctConfig!queryAcctConfigForAdd.action' ,
			fields : ['t_acct_id','county_id','county_name','optr_id','optr_name','initamount','balance',
				'cust_colony','cust_colony_text','config_year','create_time','change_amount']
		})
		var cm = new Ext.grid.ColumnModel({
			columns : [
				{header:'分公司',dataIndex:'county_name'},
				{header:'客户群体',dataIndex:'cust_colony_text'},
				{header:'金额',dataIndex:'balance',editor : new Ext.form.NumberField({
					minValue : 0
				})},
				{header:'年份',dataIndex:'config_year',editor : new Ext.form.NumberField({
					minValue : parseInt(new Date().format('Y')),
					allowDecimals : false
				})}
				,{header: '操作',width : 60,dataIndex: 't_acct_id',
		            renderer:function( v , md, record , i  ){
		            	if(!record.get('t_acct_id')){
		            		return "<a href='#' onclick=Ext.getCmp(\'"+"acctConfigEditorGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
		            	}
					}
				}
			]
		})
		
		AcctConfigEditorGrid.superclass.constructor.call(this,{
			id : 'acctConfigEditorGrid',
			border : false,
			store: this.configStore,
			clicksToEdit: 1,
	        viewConfig : {
	        	forceFit : true
	        },
	        sm : new Ext.grid.CheckboxSelectionModel(),
			cm:cm,
			tbar : ['查询条件',' ',new Ext.form.ComboBox({
				id : 'countySelectCombo',
				store : acctConfigCountyStore,
				emptyText : '分公司...',
				valueField : 'item_value',
				displayField : 'item_name',
				mode: 'local',
				forceSelection : true,
//				width : 100,
				triggerAction : 'all',
				editable : false,
				listeners : {
					scope:this,
					select : this.selectParam
				}
			}),new Ext.form.ComboBox({
				id : 'colonySelectCombo',
				store : acctConfigColonyStore,
				emptyText : '客户群体...',
				valueField : 'item_value',
				displayField : 'item_name',
				mode: 'local',
				forceSelection : true,
//				width : 100,
				triggerAction : 'all',
				editable : false,
				listeners : {
					scope:this,
					select : this.selectParam
				}
			})]
		})
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		this.on("beforeedit", this.beforeEdit, this);
		AcctConfigEditorGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(obj.field == 'config_year'){
			if(obj.record.get('t_acct_id')){
				return false;
			}
		}
	},
	afterEdit : function(obj){
		if(obj.field =='balance'){
			obj.record.set('change_amount',obj.originalValue-obj.value);
		}
	},
	loadData : function(records){
		this.getStore().loadData(records);
		Ext.getCmp('countySelectCombo').disable();
		Ext.getCmp('colonySelectCombo').disable();
	},
	deleteRow : function(){
		var record = this.getSelectionModel().getSelected();
		this.getStore().remove(record);
	},
	selectParam : function(){
		var countyId= Ext.getCmp('countySelectCombo').getValue();
		var colony = Ext.getCmp('colonySelectCombo').getValue();
		if(!countyId && !colony){
			return;
		}
		var all  = {};
		if(countyId){
			all['countyId'] = countyId;
		}
		if(colony){
			all['colony'] = colony;
		}
		this.configStore.load({
			params : all
		})
	},
	getValues : function(){
		var data = [];
		var records = this.getStore().getModifiedRecords();
		for(var i=0;i<records.length;i++){
			var record = records[i];
			//只有当acctId不为空（表示修改),或者金额配置大于0时，保存
			if(record.get('t_acct_id') || record.get('balance')>0){
				record.data.balance = record.data.balance* 100;
				record.data.change_amount = record.data.change_amount* 100;
				data.push(record.data);
			}
		}
			
		return data;
	}
})

AcctConfigView = Ext.extend(Ext.Panel,{
	acctConfigGrid : null,
	constructor : function(){
		this.acctConfigGrid = new AcctConfigGrid();
		
		AcctConfigView.superclass.constructor.call(this,{
			id:'AcctConfigView',
			title:'定额账户配置',
			closable:true,
			border:false,
			layout:'fit',
			items:[this.acctConfigGrid]
		});
	}
})