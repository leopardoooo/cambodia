Ext.ns('Department');


AdrToDeptTree = Ext.extend(Ext.ux.FilterTreePanel, {
    constructor: function (v) {
        var loader = new Ext.tree.TreeLoader({
        	url: root + "/system/Dept!queryAdrToDeptTree.action",
        	baseParams: {
       			deptId :v
        	}
        });
        AdrToDeptTree.superclass.constructor.call(this, {
            split: true,
            id: 'AdrToDeptTreeId',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            rootVisible : false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: new Ext.tree.AsyncTreeNode()
        });
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
    },openNext:function(){
		var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var i = 0; i < childarr.length; i++) {
				if (childarr[i].loaded == false) {
                    childarr[i].expand();
                    childarr[i].collapse();
                }
			}
		}
	},initComponent : function() {
		AdrToDeptTree.superclass.initComponent.call(this);
		this.getRootNode().expand();
		this.getRootNode().on('expand', function() {
			this.openNext();
		},this);
	}
});



SychBusiCodeForm = Ext.extend(Ext.form.FormPanel ,{
	url: Constant.ROOT_PATH + "/core/x/Acct!acctFree.action",
	acctRecord: null,dept_id:null,
	constructor: function(dept_id){
//		this.acctRecord = App.getApp().main.infoPanel.acctPanel.acctItemGrid.getSelectionModel().getSelected();
		this.dept_id = dept_id;
		SychBusiCodeForm.superclass.constructor.call(this,{
			border: false,
			labelWidth: 70,
			layout : 'form',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				xtype:'hidden',
				value: dept_id,
				name:'deptId'
			},{
				xtype:'hidden',
				value: 'BIND',
				name:'bindType'
			},new Ext.ux.form.ItemSelector({
				bodyStyle: 'padding-top: 20px', border: false, width: 400, autoHeight: true,
				xtype:'itemselector',name:'busiCodes',fieldLabel:'业务代码',allowBlank:false,
						imagePath:'/'+Constant.ROOT_PATH_LOGIN+'/resources/images/itemselectorImage',
						multiselects:[{
			            	legend:'待选项',
			                width: 180,
			                height: 350,
			                border: false,
			                store: new Ext.data.JsonStore({fields:['item_value','item_name']}),
			                displayField: 'item_name',
			                valueField: 'item_value',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('busiCodes').multiselects[0].store);
                                        }
                                    }
                                }
			                }]
			            },{
				           	legend:'已选项',
			                width: 180,
			                height: 350,
			                border: false,
			                store: new Ext.data.JsonStore({fields:['item_value','item_name']}),
			                displayField:'item_name',valueField:'item_value',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('busiCodes').multiselects[1].store);
                                        }
                                    }
                                }
			                }]
					}
				]
			})
			]
		})
	},
	initComponent : function(){
        DisctInfoPanel.superclass.initComponent.call(this);
//        App.form.initComboData(this.findByType("lovcombo")/*, this.doInit, this*/);
        var item = this.getForm().findField('busiCodes');
        var params = ['BUSI_CODE'];
        var deptId = this.dept_id;
		Ext.Ajax.request({
			params: {comboParamNames: params},
			url: root + "/ps.action",scope:this,
			success: function( res, ops){
				var data = Ext.decode(res.responseText );
				
				item.multiselects[0].store.loadData(data[0]);
				//过滤已经选中的
				Ext.Ajax.request({
					params: {deptId: deptId},scope:this,
					url : root + '/system/Dept!queryDeptBusiCodes.action',
					success: function( res, ops){
						var dataSelected = Ext.decode(res.responseText );
						var item = this.getForm().findField('busiCodes');
						var list = [];
						for(var index =0;index<dataSelected.length;index++){
							var rec = dataSelected[index];
							var bean = {item_value:rec.busi_code,item_name:rec.busi_name};
							list.push(bean);
						}
						var array = [];
						for( var i=0;i<list.length ;i++ ){
							var bean = list[i];
							var idx = item.fromMultiselect.store.find('item_value',bean.item_value);
							if(idx >-1){
								array.push(idx);
							}
						}
						item.fromMultiselect.view.select(array);
						item.fromTo();
					}
				});
				
			}
		});
	},
	doItemSelect: function(value,store){
        store.clearFilter();
        if (Ext.isEmpty(value)) return;
        store.filterBy(function (record){
            return record.get('item_name').indexOf(value) >= 0;
        }, this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['adjust_fee'] = Ext.util.Format.formatToFen(Ext.getCmp('free_fee').getValue()) ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
/**
 * 关联busicode.
 * @class SychBusiCodeWindow
 * @extends Ext.Window
 */
SychBusiCodeWindow = Ext.extend(Ext.Window, {
	form : null,
	deptId:null,
	constructor : function(v) {
		this.deptId = v;
		this.form = new SychBusiCodeForm(v);
		SychBusiCodeWindow.superclass.constructor.call(this, {
			title: '禁用业务',
			layout : 'fit',
			width : 560,
			height : 480,
			closeAction : 'hide',
			items : this.form,
			buttons : [{
				text : '保存',
				scope : this,
				handler :this.saveOptr
			},{
				text : '关闭',
				scope:this,
				handler:function(){
					this.hide();
				}
			}]
		});
	},
	saveOptr:function(){
//		busiCodes,deptId,bindType
		if(!this.form.getForm().isValid()){
			Alert('有必填的选项没有填');
			return ;
		}
		var values = this.form.getForm().getValues();
		var busiCodes = values.busiCodes;
		if(!Ext.isEmpty(busiCodes)){
			busiCodes = busiCodes.split(',');
		}else{
			busiCodes = [];
		}
		values.busiCodes = busiCodes;
		
		Ext.Ajax.request({
			url : root + '/system/Dept!saveBusiCodesToDept.action',
			params : values,
			scope:this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if(true === rs.success){
					Alert('操作成功!');
					this.close();
				}else{
					Alert('操作失败');
		 		}
			}
		});
	}
});

/**
 * 原先job报表使用.
 * @class AdrToDeptWindow
 * @extends Ext.Window
 */
AdrToDeptWindow = Ext.extend(Ext.Window, {
	adrToDeptTree : null,
	deptId:null,
	constructor : function(v) {
		this.deptId = v;
		this.adrToDeptTree = new AdrToDeptTree(v);
		AdrToDeptWindow.superclass.constructor.call(this, {
			layout : 'fit',
			width : 400,
			height : 500,
			closeAction : 'hide',
			items : this.adrToDeptTree,
			buttons : [{
				text : '保存',
				scope : this,
				handler :this.saveOptr
			},{
				text : '关闭',
				scope:this,
				handler:function(){
					this.hide();
				}
			}]
		});
	},
	saveOptr:function(){
		var resourceIds = [],all={};
        if (Ext.getCmp('AdrToDeptTreeId')) {
            var nodes = Ext.getCmp('AdrToDeptTreeId').getChecked();
            for (var i=0; i<nodes.length;i++) {
                if (nodes[i].attributes.others['attr']=='2') {
                    resourceIds.push(nodes[i].id);
                }
            }
        }
        if (resourceIds.length > 0) {
            all["addrIds"] = resourceIds;
        }
        all["deptId"]=this.deptId;
		Ext.Ajax.request({
			url : root + '/system/Dept!saveAddrToDept.action',
			params : all,
			scope:this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if(true === rs.success){
					Alert('操作成功!');
					Ext.getCmp('DeptTree').getRootNode().reload();
					Ext.getCmp('DeptTree').expandAll();
					this.close();
				}else{
					Alert('操作失败');
		 		}
			}
		});
	}
});

/**
 * 溧阳项目中.
 * @class Adr2DeptWindow
 * @extends Ext.Window
 */
Adr2DeptWindow = Ext.extend(Ext.Window, {
	addrSelectCombo : null,
	deptId:null,
	constructor : function(attrs) {
		this.deptId = attrs.id;
		
		this.addrSelectCombo = new Ext.ux.form.ItemSelector({
				bodyStyle: 'padding: 10px 0px 10px 15px',
				xtype:'itemselector',name:'busiCodes',allowBlank:false,
						imagePath:'/'+Constant.ROOT_PATH_LOGIN+'/resources/images/itemselectorImage',
						multiselects:[{
			            	legend:'可关联',
			                width: 220,
			                height: 365,
			                store: new Ext.data.JsonStore({fields:['addr_id','addr_name']}),
			                displayField: 'addr_name',
			                valueField: 'addr_id',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('busiCodes').multiselects[0].store);
                                        }
                                    }
                                }
			                }]
			            },{
				           	legend:'已关联',
			                width: 220,
			                height: 365,
			                store: new Ext.data.JsonStore({fields:['addr_id','addr_name']}),
			                displayField: 'addr_name',valueField: 'addr_id',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('busiCodes').multiselects[1].store);
                                        }
                                    }
                                }
			                }]
					}
				]
			});
			
			this.bindedAddIdArray = (attrs.attr_src_id || '').split(',');
			var all = {deptId:this.deptId,countyId:attrs.others.countyId};
			Ext.Ajax.request({
				url : root + '/system/Dept!queryBindableAddr.action',
				params : all,
				scope:this,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					this.addrSelectCombo.fromMultiselect.store.loadData(rs);
					
					var array = [];
					for( var i=0;i<this.bindedAddIdArray.length ;i++ ){
						var addrId = this.bindedAddIdArray[i];
						var idx = this.addrSelectCombo.fromMultiselect.store.findBy(function(rec,id){
							return rec.get('addr_id') == addrId;
						});
						if(idx >-1){
							array.push(idx);
						}
					}
					this.addrSelectCombo.fromMultiselect.view.select(array);
					this.addrSelectCombo.fromTo();
					
				}
			});
		
		Adr2DeptWindow.superclass.constructor.call(this, {
			title: '关联区域',
			layout : 'fit',
			width : 500,
			height : 450,
			closeAction : 'hide',
			items : this.addrSelectCombo,
			buttons : [{
				text : '保存',
				scope : this,
				handler :this.saveOptr
			},{
				text : '关闭',
				scope:this,
				handler:function(){
					this.hide();
				}
			}]
		});
	},
	saveOptr:function(){
		var resourceIds = [],all={};
        var ids = this.addrSelectCombo.getValue();
        if(ids!=null && !Ext.isEmpty(ids)){
        	ids = ids.split(',');
        }else{
        	ids = [];
        }
        all["deptId"]=this.deptId;
        all["addrIds"]=ids;
		Ext.Ajax.request({
			url : root + '/system/Dept!saveAddr2Dept.action',
			params : all,
			scope:this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if(true === rs.success){
					Alert('操作成功!');
					Ext.getCmp('DeptTree').getRootNode().reload();
					Ext.getCmp('DeptTree').expandAll();
					this.close();
				}else{
					Alert('操作失败');
		 		}
			}
		});
	}
});

/**
 * 部门表格编辑树
 * @class AddressTree
 * @extends Ext.ux.tree.TreeGridEditor
 */
DeptTree = Ext.extend(Ext.ux.tree.TreeGridEditor,{
	constructor : function(){
		DeptTree.superclass.constructor.call(this,{
			id : 'DeptTree',
			loader: new Ext.tree.TreeLoader({
	            dataUrl: root+"/system/Dept!queryDeptByCountyId.action",
	            baseParams: {
	                method: 'load'
	            }
	        }),
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: '部门名称',
	            dataIndex: 'text',
	            autoWidth: true,
	            displayTip: true
	        },{
	        	header: '部门类别',
	            dataIndex: 'attr',
	            width:100,
	            tpl: new Ext.XTemplate('{[values.attr== "分公司" ? "<font color=black/>"+values.attr :"<font color=blue/>"+values.attr]}')
	        },{
	        	header:'代理商',
	        	dataIndex:'other_name',
	        	autoWidth: true,
	        	displayTip: true
	        },{
	        	header:'所关区域',
	        	dataIndex:'attr_src',
	        	autoWidth: true,
	        	displayTip: true
	        }],
	        // 设置Obar
	        obarCfg: {
	            column: {
	                header: '操作',
	                dataIndex: 'id',
	                width: 350
	            },
	            btns: [{
	                id: 'add',
	                deepestState: 'disabled',
	                handler : function(n){
//	                	if(n.attributes.others.dept_type != 'FGS'){
//	                		Alert('只允许在分公司下添加部门。')
//	                		return;
//	                	}
	                	new DeptWin('add',n).show();
	                }
	            },{
	                id: 'edit',
	                handler : function(n){
	                	//宜昌分公司下的部门类型为分公司的可以修改
	                	if(n.attributes.others.dept_type == 'FGS' && n.attributes.pid != '0501'){
	                		Alert('只允许修改部门。')
	                		return;
	                	}
	                	new DeptWin('edit',n).show();
	                }
				},{
	            	id:'synch',
	            	text:'关联区域',
	            	handler : function(n){
	                	new Adr2DeptWindow(n.attributes).show();
	                }
	            },{
	                id: 'remove',
	                handler : this.doDelete,
	                // 删除节点校验函数
	                validator: this.checkRemove
//	            },
//	            {
//	                id: 'statusActive',
//	                text:'禁用业务',
//	                handler : function(n){
//	                	new SychBusiCodeWindow(n.attributes.id).show();
//	                }
	            }]
	        }
		})
		
		this.expandAll();
	},
	checkRemove : function(n){
		if(n.others.dept_type == 'FGS'){
    		Alert('只允许删除部门。')
    		return false;
    	}
        return true;
	},
	doDelete : function(n){
		Confirm("确定要删除该数据吗?", this ,function(){
			var msg = Show();
			Ext.Ajax.request({
				scope : this,
				url : root+"/system/Dept!removeDept.action",
				params : {
					deptId : n.id
				},
				success : function(res,opt){
					msg.hide();msg=null;
					var rs = Ext.decode(res.responseText);
					var data = Ext.decode(rs.simpleObj);
					if(data['success'] == false){
						Alert(data['msg']);
					}else{
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('DeptTree').getRootNode().reload();
							Ext.getCmp('DeptTree').expandAll();
						}else{
							Alert('操作失败');
				 		}
					}
				}
			})
		});
	}
//	,doLoad: function(treeLoader,rootNode){
//		var thiz = this;
//		var handlerStatus = function(childNodes){
//			if(childNodes && childNodes.length > 0){
//				Ext.each(childNodes, function(node){
//					if(node.attributes.others.dept_type != 'FGS'){
//					//根节点隐藏修改复制
//							thiz.hideObar(node, 'add');
//					}
//					if(node.attributes.others.dept_type == 'FGS' && node.attributes.pid != '0501'){
//						thiz.hideObar(node, 'edit');
//					}
//					handlerStatus(thiz.getChildNodes(node));
//				});
//			}
//		}
//		handlerStatus(thiz.getChildNodes(rootNode));
//	}
});

/**
 * 部门操作窗口
 * @class AddressWin
 * @extends Ext.Window
 */
DeptWin = Ext.extend(Ext.Window,{
	node : null,
	title : null,
	record : null,
	form : null,
	constructor : function(type,node){
		this.record = {};
		//操作节点
		this.node = node;
		if(type == 'add'){
			this.title = '增加部门';
			this.record.dept_pid = node.attributes.id;
		}else if(type == 'edit'){
			this.title = '修改部门';
			this.record.dept_id = node.attributes.id;
			this.record.dept_pid = node.attributes.pid;
			this.record.dept_name = node.attributes.text;
			this.record.dept_type = node.attributes.others.dept_type;
			this.record.agent_id = node.attributes.other_id;
		}
		
		this.agentStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryAllAgent.action',
			fields: ['id', 'name'],
			autoLoad: true
		});
		
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : [{
				xtype : 'hidden',
				name : 'dept_id'
			},{
				xtype : 'hidden',
				name : 'dept_pid'
			},{
				xtype : 'textfield',
				name : 'dept_name',
				allowBlank : false,
				fieldLabel : '部门名称'
			},{
				xtype : 'paramcombo',
				id : 'deptTypeCombo',
				hiddenName : 'dept_type',
				paramName : 'DEPT_TYPE',
				defaultValue : 'YYT',
				allowBlank : false,
				fieldLabel : '部门类型'/*,
				listeners : {
					afterrender : function(comp){
						var store = comp.getStore();
						store.removeAt(store.find('item_value','FGS'));
					}
				}*/
			},{
				fieldLabel: '代理商',
				xtype: 'combo',
				store: this.agentStore,
				displayField: 'name', valueField: 'id',
				hiddenName: 'agent_id',
				editable: true
			}]
		});
		App.form.initComboData( this.form.findByType("paramcombo"),this.doInit,this);
		
		DeptWin.superclass.constructor.call(this,{
			title : this.title,
			layout : 'fit',
			height : 250,
			width : 300,
			closeAction : 'close',
			items : [this.form],
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
		});
		
	},
	doInit : function(){
		var store = this.form.findByType("paramcombo")[0].getStore();
		store.removeAt(store.find('item_value','FGS'));
		
		this.agentStore.on('load', function(){
			this.form.getForm().loadRecord(new Ext.data.Record(this.record));
		}, this);
	},
	doSave : function(){
		//添加和修改
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				mb = Show();//显示正在提交
				var oldValues = this.form.getForm().getValues(),newValues = {};
				
				for(var key in oldValues){
					newValues['dept.'+key] = oldValues[key];
				}
				
				Ext.Ajax.request({
					url : root+"/system/Dept!saveDept.action",
					params : newValues,
					scope : this,
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('DeptTree').getRootNode().reload();
							Ext.getCmp('DeptTree').expandAll();
							
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
//	addNode : function(newNode){
//		var nc = {
//            id: newNode.addr_id,
//            cls : 'file',
//            text : newNode.addr_name,
//			others : {
//				fullName : newNode.addr_full_name
//			},
//            leaf: true
//        };
//        var nn = new Ext.ux.tree.TreeGridEditorNode(nc);
//        nn.ui = new Ext.ux.tree.TreeGridEditorNodeUI(nn);
//        
//        this.node.leaf = false;
//		this.node.attributes.leaf = false;
//        this.node.appendChild(nn);
//		
//		this.node.editing = false;
//        this.node.editMode = false;
//        
//        Ext.DomHelper.overwrite(Ext.fly(this.node.getUI().elNode).child('.x-treegrideditor-obar'), Ext.getCmp('AddressTree').editBtnsHtml(this.node))
//        Ext.getCmp('AddressTree').activeObar(this.node);
//	}
})


DeptView = Ext.extend(Ext.Panel,{
	constructor : function(){
		DeptView.superclass.constructor.call(this,{
			id : 'DeptView',
			layout : 'fit',
			title : '机构管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new DeptTree()]
		})
	}
})
