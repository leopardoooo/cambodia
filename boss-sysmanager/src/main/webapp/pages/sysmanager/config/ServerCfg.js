/**
 * 服务器配置
 */
var ServerGrid = Ext.extend(Ext.grid.GridPanel, {
	serverStore : null,
	constructor : function() {
		this.serverStore = new Ext.data.JsonStore({
					url : root + '/config/Server!queryServer.action',
					fields : ['server_id','server_name','supplier_id','ip','supplier_name','for_osd','port',
					'supplier_id_text','for_osd_text']
				});
		this.serverStore.load();				
		var sm = new Ext.grid.CheckboxSelectionModel({});
		var columns = [{
					header : '服务器名称',
					dataIndex : 'server_name',
					renderer : App.qtipValue
				},{
					header : '服务器编号',
					dataIndex : 'server_id',
					renderer : App.qtipValue
				}, {
					header : '提供商',
					dataIndex : 'supplier_id_text'
				}, {
					id : 'bulletin_content_id',
					header : 'IP',
					dataIndex : 'ip',
					renderer : App.qtipValue
				}, {
					header : '端口',
					dataIndex : 'port'
				}, {
					header : '消息服务器',
					width:120,
					dataIndex : 'for_osd_text'
				},    {
		            header: '操作',
		            width: 170,
		            scope:this,
			        renderer:function(value,meta,record,rowIndex,columnIndex,store){
			            	var btns = this.doFilterBtns(record);
			            	return btns;
		            }
	            }];

		ServerGrid.superclass.constructor.call(this, {
					id : 'serverGridId',
					region : 'center',
					store : this.serverStore,
					sm : sm,
					columns : columns,
					tbar : [' ', App.data.optr['county_id']=='4501' ? {
								text : '添加',
								iconCls : 'icon-add',
								scope : this,
								handler : this.addRecord
							}:'']
				});
	},
	doFilterBtns : function(record){
		var btns = "";
		//如果是省公司操作员
		if(App.data.optr['county_id'] =='4501' 
//		|| record.get('optr_id') == App.data.optr['optr_id']
         ){
            btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"serverGridId"+"\').doModify(); style='color:blue'>修改</a>";
            		
		}
		return btns;
    },
	addRecord : function() { 
		var win = Ext.getCmp('serverWinId');
	 	if (!win) {
			win = new ServerWin();
		}
		win.show();
 	},
	doModify : function() {
		var grid = Ext.getCmp('serverGridId');
		var record = grid.getSelectionModel().getSelected();
		var win = Ext.getCmp('serverWinId');
	 	if (!win) {
			win = new ServerWin(record);
		}
		win.show();
	}
});

var ServerForm = Ext.extend(Ext.form.FormPanel, {
	record: null,
	constructor : function(p,record) {
		this.record = record;
		ServerForm.superclass.constructor.call(this, {
			layout: 'form',
            labelAlign: "right",
            border: false,
            bodyStyle: 'padding-top: 10px',
            labelWidth: 100,
            defaults: {
                disabled : App.data.optr['county_id']!='4501' && this.record ? true : false
            },
			items : [{
						fieldLabel : '服务器名称',
						name : 'server_name',
						xtype : 'textfield',
						allowBlank : false
					},{
						fieldLabel:'服务器编号',
						name:'server_id',
						id:'getServerId',
						xtype:'textfield',
						allowBlank:false,
						disabled : this.record ? true : false
					}, {
						fieldLabel : '提供商',
						width : 100,
						xtype : 'paramcombo',
						hiddenName : 'supplier_id',
						paramName:'CA_SUPPLIER',
						allowBlank : false
					},{
						fieldLabel:'IP',
						xtype:'textfield',
						name:'ip',
						vtype : 'IPAddress',
						allowBlank:false
					},{
						fieldLabel:'端口',
						xtype:'numberfield',
						name:'port',
						width : 100,
						allowBlank:false
					}, {
						fieldLabel : '消息服务器',
						xtype : 'paramcombo',
						width : 100,
						paramName : 'BOOLEAN',
						defaultValue : 'F',
						hiddenName : 'for_osd'
					}]
		});
	},
	initComponent : function() {
		ServerForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType("paramcombo"), this.doInit, this);
	},
	doInit: function () {
        if (this.record) {
            this.getForm().loadRecord(this.record);
        }
	}
});

ServerCountyTree = Ext.extend(Ext.tree.TreePanel, {
     serverId:null,
    constructor: function (v) {
    	 this.serverId=v;
        var loader = new Ext.tree.TreeLoader({
            url: root + "/config/Server!getCountyTree.action?query=" + this.serverId        
        });
        ServerCountyTree.superclass.constructor.call(this, {
            border : false,
            split: true,
            title: '应用地市',
            id: 'ServerCountyTreeId',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '-1',
                iconCls: 'x-tree-root-icon',
                nodeType: 'async',
                text: '地市结构'
            }
        });
       this.expandAll();
    },
 	initEvents: function(){
		this.on("afterrender",function(){
			//如果不是修改
	        if(!this.serverId){
        		var node = this.getRootNode();
        		node.attributes.checked = true;
        		node.ui.toggleCheck(true);
        		node.fireEvent('checkchange', node, true);
 	        }
		},this,{delay:1000});
		
		ServerCountyTree.superclass.initEvents.call(this);
	},
    listeners: {
        'checkchange': function (node, checked) {
            node.expand();
            node.attributes.checked = checked;
            node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
        }
     }
});

ServerWin = Ext.extend(Ext.Window, {
	serverForm : null,
	countyTree: null,
	constructor : function(s) {
		this.serverForm = new ServerForm(this,s);
		var serverId='';
     		if(s){
				if(undefined!=s.data){
					var data = s.data;
					serverId = data['server_id'];
				}
    		}
		this.countyTree = new ServerCountyTree(serverId);
		ServerWin.superclass.constructor.call(this, {
			id : 'serverWinId',
			title : '服务器配置',
			border : false,
			width : 600,
			height : 500,
			layout : 'border',
			items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : '50%',
					items : [this.serverForm]
			},{
					region : 'center',
					layout : 'fit',
					items : [this.countyTree]
			}],
				
			buttonAlign : 'center',
			closeAction : 'close',
			buttons : [{
						id:'bulletin_save_btn',
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
		if (!this.serverForm.getForm().isValid())
			return;
		var values = this.serverForm.getForm().getValues(),obj={};
		for (var v in values) {
			obj['server.' + v] = values[v];
		}
		obj['server.server_id'] = Ext.getCmp('getServerId').getValue();
		var CountyId = [];
		if (Ext.getCmp('ServerCountyTreeId')) {
		     var nodes = Ext.getCmp('ServerCountyTreeId').getChecked();
		     for (var i in nodes) {
		          if (nodes[i].leaf) {
		              CountyId.push(nodes[i].id);
		          }
		     }
		 }
		        
		obj["countyIds"] = CountyId.join(",");
		var msg = Show();
		Ext.Ajax.request({
					url : root + '/config/Server!saveServer.action',
					params : obj,
					scope : this,
					success : function(res, option) {
						msg.hide();
						msg = null;
						var data = Ext.decode(res.responseText);
						if (data.success === true) {
							Alert('保存成功', function() {
										Ext.getCmp('serverGridId').getStore().load();
										this.close();
									}, this)
						}
					}
				});
	},
	show : function() {
		ServerWin.superclass.show.call(this);
//		this.form.getForm().findField('bulletin_title').focus(true, 100);
	}
});

ServerView = Ext.extend(Ext.Panel, {
			constructor : function() {
				serverGrid = new ServerGrid();
				ServerView.superclass.constructor.call(this, {
							id : 'ServerView',
							title : '服务器配置',
							closable : true,
							border : false,
							layout : 'border',
							baseCls : "x-plain",
							items : [serverGrid]
						});
			}
		});
