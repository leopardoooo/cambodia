/**
 * 动态资源grid
 */
var ResGroupGrid = Ext.extend(Ext.grid.GridPanel, {
	resGroupStore : null,
	constructor : function() {
		resGroupGridThiz = this;
		this.resGroupStore = new Ext.data.JsonStore({
			totalProperty:'totalProperty',
			root:'records',
			url : root + '/config/Res!queryAllRes.action',
			fields : Ext.data.Record.create([{
						name : 'group_id'
					}, {
						name : 'group_name'
					}, {
						name : 'serv_id'
					}, {
						name : 'serv_id_text'
					}, {
						name : 'group_desc'
					}, {
						name : 'create_time'
					}, {
						name : 'optr_id'
					}, {
						name : 'dept_id'
					}, {
						name : 'dept_name'
					}, {
						name : 'optr_name'
					}, {
						name : 'county_id'
					},{
						name : 'allow_update'
					}])
				});
			this.resGroupStore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});				
		var sm = new Ext.grid.CheckboxSelectionModel({});
		var columns = [{
					header : '资源组名称',
					dataIndex : 'group_name'
				}, {
					header : '服务类型',
					dataIndex : 'serv_id_text'
				}, {
					id : 'group_desc_id',
					header : '描述',
					dataIndex : 'group_desc'
				}, {
					header : '操作',
					dataIndex : 'group_id',
					renderer : function(value, cellmeta, record, i) {
						if(record.get('allow_update') == 'T' && (record.get('county_id') == App.data.optr['county_id'] || App.data.optr['county_id']=='4501')){
							return "<a href='#' style='color:blue' onclick='resGroupGridThiz.doModify();'>修改</a>&nbsp;&nbsp;&nbsp;"+"<a href='#' style='color:blue' onclick='resGroupGridThiz.doDelete();'>删除</a>&nbsp;&nbsp;";
						}else{
							return '';
						}
					}
				}];

		ResGroupGrid.superclass.constructor.call(this, {
					id : 'resGroupId',
					region : 'center',
					store : this.resGroupStore,
					autoExpandColumn : 'group_desc_id',
					sm : sm,
					columns : columns,
					bbar:new Ext.PagingToolbar({store:this.resGroupStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
					tbar : [' ', ' ', '输入关键字', ' ',
							new Ext.ux.form.SearchField({
										store : this.resGroupStore,
										width : 200,
										id : 'searchValueId',
										hasSearch : true,
										emptyText : '支持资源组名模糊查询'
									}), '->', {
								text : '添加',
								iconCls : 'icon-add',
								scope : this,
								id : 'addOptrRecord',
								handler : this.addRecord
							}]
				});
	},
	addRecord : function(s) {
		var win = Ext.getCmp('resDetailWinId');
		if (!win) {
			win = new resDetailWin(s);
		}
		win.show.defer(100, win);
	},
	doModify : function() {
		var grid = Ext.getCmp('resGroupId');
		var record = grid.getSelectionModel().getSelected();
		this.addRecord(record);
	},
	doDelete : function(){
		var grid = Ext.getCmp('resGroupId');
		var record = grid.getSelectionModel().getSelected();
		
		Confirm("确定删除吗?", this ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/config/Res!delteResGroup.action',
				params : {
					groupId : record.get('group_id')
				},
				success : function(res,opt){
					Alert('删除成功');
					this.getStore().reload();
				}
			})
		});
	},
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		ResGroupGrid.superclass.initEvents.call(this);
	},
	doClick : function(grid,rowIndex){
		var record = grid.getStore().getAt(rowIndex);
		var newValues = {};
		if(Ext.isEmpty(this.value)||this.value!=record.get('group_id')){
			this.value = record.get('group_id');
			Ext.Ajax.request({
				url : root + '/config/Res!queryResById.action?doneId=' + this.value,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					newValues["prod_name_str"]= rs;
					newValues["group_name"] = record.get('group_name');
					newValues["serv_id_text"] = record.get('serv_id_text');
					newValues["group_desc"] = record.get('group_desc');
					Ext.getCmp('groupResDetalisInfo').doint(newValues)
				}
			});
		}
	}
});

var ResDetailForm = Ext.extend(Ext.form.FormPanel, {
	gridData : null,
	constructor : function(s) {
		this.gridData = s;
		ResDetailForm.superclass.constructor.call(this, {
					id : 'resDetailFormId',
					height : 460,
					bodyStyle : 'padding: 10px',
					layout : 'form',
					defaults : {
						labelWidth : 70,
						xtype : 'textfield'
					},
					items : [{
								xtype : 'hidden',
								name : 'group_id'
							}, {
								fieldLabel : '资源组名',
								name : 'group_name',
								allowBlank : false
							}, {
								fieldLabel : '服务类型',
								xtype : 'paramcombo',
								id : 'servId',
								width : 100,
								paramName : 'SERV_ID',
								allowBlank : false,
								defaultValue : 'DTV',
								hiddenName : 'serv_id',
								listeners : {
									scope : this,
									'select' : function(combo, record, index) {
										var servId = combo.getValue();
										this.loadData(servId);
									}

								}
							}, {
								xtype : 'itemselector',
								name : 'itemselector',
								id : 'resArrId',
								fieldLabel : '资源项',
								allowBlank : false,
								imagePath : '/' + Constant.ROOT_PATH_LOGIN
										+ '/resources/images/itemselectorImage',
								multiselects : [{
											legend : '待选资源',
											width : 150,
											height : 270,
											store : new Ext.data.ArrayStore({
														fields : ['res_id',
																'res_name']
													}),
											displayField : 'res_name',
											valueField : 'res_id'
											,tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
												listeners:{
													scope:this,
													keyup:function(txt,e){
														if(e.getKey() == Ext.EventObject.ENTER){
															var value = txt.getValue();
																Ext.getCmp('resArrId').multiselects[0].store.filterBy(function(record){
																	if(Ext.isEmpty(value))
																		return true;
																	else
																		return record.get('res_name').indexOf(value)>=0;
																},this);
														}
													}
												}
											}]
										}, {
											legend : '已选资源',
											width : 150,
											height : 270,
											store : new Ext.data.ArrayStore({
														fields : ['res_id',
																'res_name']
													}),
											displayField : 'res_name',
											valueField : 'res_id'
										}]
								   		
							}, {
								xtype : 'textarea',
								fieldLabel : '描述',
								name : 'group_desc',
								width : 320,
								height : 40
							}]
				});
	},
	initComponent : function() {
		ResDetailForm.superclass.initComponent.call(this);
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes, this.dintLoad, this);
	},
	dintLoad : function() {
		var store = Ext.getCmp('servId').getStore();
		store.each(function(record){
			if(record.get('item_value') !='DTV' && record.get('item_value') !='ITV'){
				store.remove(record);
			}
		});
		
		if (this.gridData.data == null) {
			this.loadData('DTV');
		} else {
		var groupId = this.gridData.get('group_id');
		var arr = [];
		Ext.Ajax.request({
			url : root + '/config/Res!queryResByGroupId.action',
			params : {
				doneId : groupId
			},
			scope : this,
			success : function(res, opt) {
				var data = Ext.decode(res.responseText);
				if (data) {
					for (var j = 0; j < data.length; j++) {
						arr.push(data[j].res_id);
					}				
				}
				this.updateLoadRes(this.gridData,arr);
			}
		});	
		}
	},updateLoadRes:function(s,arr){
			this.loadData(this.gridData.get('serv_id'),arr);
			var form = Ext.getCmp('resDetailFormId').getForm();
			form.loadRecord(this.gridData);
	
	}
	,loadData : function(servId,arr) {
		Ext.Ajax.request({
			url : root + '/config/Res!queryResByServId.action',
			params : {
				doneId : servId
			},
			scope : this,
			success : function(res, opt) {
				var data = Ext.decode(res.responseText);
				if (data) {
					var arrs = [];
					var toArr =[];
					if(arr){
						for(var i=0;i<arr.length;i++){
							for (var j = 0; j < data.length; j++) {
								if(data[j].res_id===arr[i]){
									toArr.push([data[j].res_id, data[j].res_name]);
									data.remove(data[j]);
									break;
								}
							}
						
						}
					}
					for (var k = 0; k < data.length; k++) {
						arrs.push([data[k].res_id, data[k].res_name]);
					}
					
					Ext.getCmp('resArrId').multiselects[0].store.loadData(arrs);
					if(toArr){
						Ext.getCmp('resArrId').multiselects[1].store.loadData(toArr);
					}
				}
			}
		});
	}
});

var resDetailWin = Ext.extend(Ext.Window, {
	form : null,
	constructor : function(s) {
		this.form = new ResDetailForm(s);
		resDetailWin.superclass.constructor.call(this, {
			id : 'resDetailWinId',
			title : '资源组配置',
			border : false,
			width : 500,
			height : 500,
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
		var values = form.getValues();
		var store = form.findField('itemselector').toMultiselect.view.store;
		if (store.getCount() === 0) {
			Alert('请选择数据项!');
			return;
		}
		
		if (store.getCount() == 1){
			Alert('不能只配置1个资源!');
			return;
		}
		
		var arr = [];
		store.each(function(record) {
					arr.push(record.get('res_id'));
				});
		var resRecords = arr.join(",");
		var obj = {};
		for (var v in values) {
			if (v !== 'itemselector')
				obj['resGroupDto.' + v] = values[v];
		}
		obj['records']=resRecords;
		var msg = Show();
		Ext.Ajax.request({
					url : root + '/config/Res!saveResGroup.action',
					params : obj,
					scope : this,
					success : function(res, option) {
						msg.hide();
						msg = null;
						var data = Ext.decode(res.responseText);
						if (data.success === true) {
							Alert('保存成功', function() {
										Ext.getCmp('resGroupId').getStore().load();
										this.close();
									}, this)
						}
					}
				});
	},
	show : function() {
		resDetailWin.superclass.show.call(this);
		this.form.getForm().findField('group_name').focus(true, 100);
	}
});
var GroupResTaskInfoHTML = '<table width="100%" border="0" cellpadding="0" cellspacing="0">'
		+ '<tr height=24>'
		+ '<td class="label" width=10%>资源组名称:</td>'
		+ '<td class="input_bold" width=90%>&nbsp;{[values.group_name || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >服务类型:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.serv_id_text || ""]}</td>'	
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >资源:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.prod_name_str || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >描述:</td>'
		+ '<td class="input_bold">&nbsp;{[values.group_desc || ""]}</td>'			
		+ '</tr>'
		+ '</tpl>' + '</table>';
GroupResTaskInfo = Ext.extend(Ext.Panel, {
	border : false,
	tpl : null,
	html : null,
	constructor : function(v) {
		this.tpl = new Ext.XTemplate(GroupResTaskInfoHTML);
		this.tpl.compile();
		GroupResTaskInfo.superclass.constructor.call(this, {
					border : false,
					id : 'groupResDetalisInfo',
					title : "<font style='font-family:微软雅黑;font-size:12'>详细信息</font>",
					layout : 'anchor',
					anchor : '100%',
					collapsible : true,
					autoScroll : true,
					bodyStyle : "background:#F9F9F9",
					defaults : {
						bodyStyle : "background:#F9F9F9"
					},
					items : [{
								xtype : "panel",
								region : 'center',
								border : false
							}]
				});
	},
	doint : function(v) {
		var ppp = Ext.getCmp('groupResDetalisInfo').items.itemAt(0);
		var panel = {
			xtype : "panel",
			border : false,
			bodyStyle : "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
			html : this.tpl.applyTemplate(v)
		};
		ppp.removeAll();
		ppp.add(panel);
		ppp.doLayout();
		this.tpl.overwrite(this.items.itemAt(0).body, v);
	}
});


ResGroupView = Ext.extend(Ext.Panel, {
			constructor : function() {
				resGroupGrid = new ResGroupGrid();
				ResGroupView.superclass.constructor.call(this, {
							id : 'ResGroupView',
							title : '动态资源配置',
							closable : true,
							border : false,
							layout : 'border',
							baseCls : "x-plain",
							items : [{region:'center',layout:'fit',split : true,items:[resGroupGrid]}, {region : 'south',height:220,split : true,layout : 'fit',items : [new GroupResTaskInfo()]}]
//							items : [resGroupGrid]
						});
			}
		});
