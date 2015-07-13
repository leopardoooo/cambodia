/**
 * 字段参数配置
 */
var TabDefineGrid = Ext.extend(Ext.grid.GridPanel, {
	tabDefineStore : null,
	constructor : function() {
		tabDefineGridThiz = this;
		this.tabDefineStore = new Ext.data.JsonStore({
			url  : root + '/system/Param!queryTabDefine.action' ,
			fields : ['table_name','column_name','comments','param_name','rule_prop','data_type',
			'func_name','field_name','rule_prop_text','param_name_text','data_type_text'],
			root : 'records',
			totalProperty : 'totalProperty',
			autoDestroy : true
		});
		this.tabDefineStore.load({
				params : {
					start : 0,
					limit : Constant.DEFAULT_PAGE_SIZE
				}
			});	

		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
				{header : '表名',dataIndex : 'table_name',width : 100,renderer : App.qtipValue},
				{header : '字段名',dataIndex : 'column_name',width : 120,renderer : App.qtipValue}, 
				{header : '说明',dataIndex : 'comments',width : 120,renderer : App.qtipValue},
				{header : '参数名',dataIndex : 'param_name_text',width : 120,renderer : App.qtipValue},
				{header : '规则属性',dataIndex : 'rule_prop_text',width : 70},
				{header : '数据类型',dataIndex : 'data_type_text',width : 70},
				{header : '函数',dataIndex : 'func_name',width : 100,renderer : App.qtipValue},
				{header : '页面标签名',dataIndex : 'field_name',width : 150,renderer : App.qtipValue},
				{header : '操作',width : 100,scope:this,
					 renderer:function(value,meta,record,rowIndex,columnIndex,store){
		            	var btns = this.doFilterBtns(record);
		            	return btns;
					}
				}];

		TabDefineGrid.superclass.constructor.call(this, {
					id : 'tabDefineId',
					region : 'center',
					store : this.tabDefineStore,
					sm : sm,
					columns : columns,
					bbar:new Ext.PagingToolbar({store:this.tabDefineStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
					tbar : [' ', ' ', '输入关键字', ' ',
							new Ext.ux.form.SearchField({
										store : this.tabDefineStore,
										width : 250,
										hasSearch : true,
										emptyText : '支持表名,字段名,说明,参数名模糊查询'
									}), '->', {
								text : '添加',
								iconCls : 'icon-add',
								scope : this,
								handler : this.addRecord
							}]
				});
	},
	doFilterBtns : function(record){
		var btns = "";
			btns = btns + "&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"tabDefineId"+"\').doModify(); style='color:blue'>修改  </a>";
			btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"tabDefineId"+"\').doDelete()>删除</>";
		return btns;
	},
	addRecord : function() {
		var win = Ext.getCmp('tabDefineWinId');
		if (!win) {
			win = new tabDefineWin();
		}
		win.show.defer(100, win);
	},
	doModify : function() {
		var grid = Ext.getCmp('tabDefineId');
		var record = grid.getSelectionModel().getSelected();
		var win = Ext.getCmp('tabDefineWinId');
		if (!win) {
			win = new tabDefineWin(record);
		}
		win.show.defer(100, win);
	},
	doDelete : function(){
		var grid = Ext.getCmp('tabDefineId');
		var record = grid.getSelectionModel().getSelected();
		
		Confirm("确定删除吗?", this ,function(){
			Ext.Ajax.request({
				scope : this,
				url  : root + '/system/Param!deleteTabDefine.action' ,
				params : {
					old_table_name : record.get('table_name'),
					old_column_name : record.get('column_name')
				},
				success : function(res,opt){
					Alert('删除成功');
					this.getStore().reload();
				}
			})
		});
	}
});

var TabDefineForm = Ext.extend(Ext.form.FormPanel, {
	gridData : null,
	oldTableName :null,
	oldColumnName : null,
	constructor : function(s) {
		this.gridData = s;
		TabDefineForm.superclass.constructor.call(this, {
						id : 'tabDefineFormId',
		                layout: 'column',
		                border: false,
		                height : 350,
		                bodyStyle: 'padding: 10px',
		                defaults: {
		                	baseCls: 'x-plain',
		                    layout: 'form',
		                    columnWidth: 1,
		                    border: false,
		                    labelWidth: 120
		                },
						items : [{
								items:[{
									fieldLabel : '表名',
									xtype: 'textfield',
									name : 'table_name',
									width : 130,
									allowBlank : false
								}]},{
								items:[{
									fieldLabel : '字段名',
									xtype: 'textfield',
									name : 'column_name',
									width : 130,
									allowBlank : false
								}]},{
								items:[{
									fieldLabel : '说明',
									xtype: 'textfield',
									name : 'comments',
									width : 130,
									allowBlank : false
								}]},{
								items:[{
									fieldLabel:'参数名',hiddenName:'param_name',id:'paramNameId',
									xtype:'combo',
									store:new Ext.data.JsonStore({
										url  : root + '/config/ExtendTable!queryParams.action' ,
										fields : ['item_key','item_desc']
									}),displayField:'item_desc',valueField:'item_key'
									,selectOnFocus:true,
									editable : true,
									forceSelection : true,
									triggerAction : 'all'
								}]},{
								items:[{
									fieldLabel : '规则属性',
									xtype : 'paramcombo',
									width : 100,
									paramName : 'BOOLEAN',
									id:'rulePropId',
									defaultValue : 'F',
									hiddenName : 'rule_prop',
									listeners:{
										scope:this,
										select:function(combo,record){
											this.changeItem(combo.getValue(),null);
										}
									
									}
								}]} ,{
								id: 'dataTypeItemId',
	                    		hidden: true,
	                    		items:[{xtype:'combo',fieldLabel:'数据类型',id:'dataTypeId',
										hiddenName:'data_type',
										store:[['','请选择...'],['D','日期型'],['S','字符串型'],['F','函数型'],['N','数字型']],
										listeners:{
												scope:this,
												select:function(combo,record){
													this.changeItem(Ext.getCmp('rulePropId').getValue(),combo.getValue());
												}
											}
								}]
								},{
								id: 'funcNameItemId',
	                    		hidden: true,
	                    		items:[ {
									fieldLabel : '函数',
									xtype: 'textfield',
									id:'funcNameId',
									name : 'func_name',
									width : 130
								}]
								}, {
								items:[{
									fieldLabel : '页面标签名称',
									xtype: 'textfield',
									name : 'field_name',
									width : 130,
									allowBlank : false
								}]}]
				});
	},
	initComponent : function() {
		TabDefineForm.superclass.initComponent.call(this);
		this.getForm().findField('param_name').getStore().load();
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes, this.dintLoad, this);
	},
	dintLoad : function() {
		this.getForm().findField('param_name').getStore().load();
		this.getForm().findField('param_name').getStore().on("load",this.loadData,this);
	},
	loadData : function(){
		this.getForm().findField('param_name').getStore().insert(0 ,new Ext.data.Record({
								item_desc: '请选择...', item_key: ''
							}));
		if(this.gridData!=null){
				this.oldTableName = this.gridData.get('table_name');
				this.oldColumnName = this.gridData.get('column_name');
				this.changeItem(this.gridData.get('rule_prop'),this.gridData.get('data_type'))
				var form = Ext.getCmp('tabDefineFormId').getForm();
				form.loadRecord(this.gridData);
		}
	}
	,changeItem:function(rValue,dValue){
		var dataTypeItem = Ext.getCmp('dataTypeItemId');
		var funcNameItem = Ext.getCmp('funcNameItemId');
			if(rValue=="T"){
				dataTypeItem.show();
				if(dValue=='F'){
					funcNameItem.show();
				}else{
					funcNameItem.hide();
					funcNameItem.items.each(function (item) {item.reset();});
					Ext.getCmp('funcNameId').setValue('');
				}
			}else{
				funcNameItem.hide();
				dataTypeItem.hide();
				funcNameItem.items.each(function (item) {item.reset();});
				dataTypeItem.items.each(function (item) {item.reset();});
				Ext.getCmp('dataTypeId').setValue('');
				Ext.getCmp('funcNameId').setValue('');
			}
//		dataTypeItem.items.each(function (item) {item.reset();});
//		funcNameItem.items.each(function (item) {item.reset();});
	
	}
	
});

var tabDefineWin = Ext.extend(Ext.Window, {
	form : null,
	constructor : function(s) {
		this.form = new TabDefineForm(s);
		tabDefineWin.superclass.constructor.call(this, {
			id : 'tabDefineWinId',
			title : '字段名参数配置',
			border : false,
			width : 400,
			items : [this.form],
			buttonAlign : 'center',
			closeAction : 'close',
			buttons : [{
						text : '保存',
						scope : this,
						handler : this.doSave
					}, {
						text : '关闭',
						scope : this,
						handler : function() {
							this.close();
						}
					}]
		});
	},
	doSave : function() {
		var form = this.form.getForm();
		if (!form.isValid())
			return;
		var obj={};
		var values = form.getValues();
		for (var v in values) {
			obj['tabDefine.' + v] = values[v];
		}
		obj['old_table_name']= this.form.oldTableName;
		obj['old_column_name']=this.form.oldColumnName;
		var msg = Show();
		Ext.Ajax.request({
					url  : root + '/system/Param!saveTabDefine.action' ,
					params : obj,
					scope : this,
					success : function(res, option) {
						msg.hide();
						msg = null;
						var data = Ext.decode(res.responseText);
						if (data.success === true) {
							Alert('保存成功', function() {
										Ext.getCmp('tabDefineId').getStore().load({params : {start : 0,limit : Constant.DEFAULT_PAGE_SIZE}});
										this.close();
									}, this)
						}
					}
				});
	}
});


TabDefineView = Ext.extend(Ext.Panel, {
			constructor : function() {
				tabDefineGrid = new TabDefineGrid();
				TabDefineView.superclass.constructor.call(this, {
							id : 'TabDefineView',
							title : '字段参数配置',
							closable : true,
							border : false,
							layout : 'border',
							baseCls : "x-plain",
							items : [tabDefineGrid]
						});
			}
		});
