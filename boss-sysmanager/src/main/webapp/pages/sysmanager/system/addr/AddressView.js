Ext.ns('Address');


/**
 * 自定义文本搜索框
 * @class SearchField
 * @extends Ext.form.TwinTriggerField
 */
SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    validationEvent: false,
    
    validateOnBlur: false,
    
    trigger1Class: 'x-form-clear-trigger',
    
    trigger2Class: 'x-form-search-trigger',
    
    hideTrigger1: true,
    
    hasSearch: false,
    
    width:180,
    
    initComponent: function() {
        Ext.ux.form.SearchField.superclass.initComponent.call(this);
        this.addEvents('search');
        this.on('specialkey', function(f, e) {
            if (e.getKey() == e.ENTER) {
                this.onTrigger2Click();
            }
        }, this);
    },
    
    initTrigger: function() {
        Ext.ux.form.SearchField.superclass.initTrigger.call(this);
        if (this.triggerTips) {
            this.triggers[0].dom.qtip = this.triggerTips[1];
            this.triggers[1].dom.qtip = this.triggerTips[0];
        }
    },
    
    onTrigger1Click: function() {
        if (this.hasSearch) {
            this.el.dom.value = '';
            this.triggers[0].hide();
            this.hasSearch = false;
            this.fireEvent('search', '');
        }
    },
    
    onTrigger2Click: function() {
    	if(/.*\u0027.*/gi.test(this.getRawValue())){
        	Alert('请不要输入单引号');
			return;
        }
        if (this.getRawValue() != '') {
            this.hasSearch = true;
            this.triggers[0].show();
            this.fireEvent('search', this.getRawValue());
        }
    }
});

Ext.reg('searchfield', SearchField);

/**
 * 地址表格编辑树
 * @class AddressTree
 * @extends Ext.ux.tree.TreeGridEditor
 */
AddressTree = Ext.extend(Ext.ux.tree.TreeGridEditor,{
	constructor : function(){
		var addressThiz = this;
		AddressTree.superclass.constructor.call(this,{
			id : 'AddressTree',
			loader: new Ext.tree.TreeLoader({
	            dataUrl: root+"/system/Address!queryAddrTree.action",
//	            baseParams: {
//	                method: 'load'
//	            },
	            listeners:{
	            	scope:this,
	            	load: this.doLoad,
	            	beforeload : this.onBeforeLoad
	            }
	        }),
	        maxDepth: 4,
	        columnResize:true,
	        enableHdMenu:true,
	        // 超出最大深度，提示信息
    		maxDepthText: '不能再往下添加地区',
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: '地区名称',
	            dataIndex: 'text',
//	            autoWidth: true,
	            width:150,
	            displayTip: true
	        }],
	        // 设置Obar
	        obarCfg: {
	            column: {
	                header: '操作',
	                dataIndex: 'id',
	                width: 500
	            },
	            btns: [{
	                id: 'add',
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('add',n).show();
	                }
	            },{
	                id: 'leveladd',
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('leveladd',n).show();
	                }
	            }/*,{
	                id: 'batchAdd',
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('batchAdd',n).show();
	                }
	            }*/,{
	                id: 'edit',
	                handler : function(n){
	                	new AddressWin('edit',n).show();
	                }
	            }/*, {
	                id: 'remove',
	                handler : this.doDelete,
	                // 删除节点校验函数
	                validator: this.checkRemove
	            }*/, {
	                id: 'statusActive',
	                handler : this.doStatusActive
	            }, {
	                id: 'statusInvalid',
	                handler : this.doStatusInvalid,
	                validator: this.checkRemove
	            }]
	        }
	        ,tbar: [' ',{
	            xtype: 'searchfield',
	            emptyText : '支持模糊查询',
	            triggerTips: ['查询', '取消'],
	            listeners: {
	            	scope : this,
	                search: function(text) {
	                	Ext.Ajax.request({
							url: root+"/system/Address!queryAddrTree.action",
							scope : this,
							params : {
								queryText : text
							},
							success : function(res,opt){
								var data = Ext.decode(res.responseText);
								var root = new Ext.tree.AsyncTreeNode({
										  text:'gen',
//										  id:'root',
										  draggable:false,
										  children:data
										 });
								addressThiz.setRootNode(root);
							}
						});
	                }
	            }
	        }]
		});
		
	},
	onBeforeLoad:function(l,node){
		l.on('beforeload',function(loader,node){
  			l.baseParams.addrId=node.id; //通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
		},l);
	},
	doLoad: function(treeLoader,rootNode){
		var thiz = this;
		var handlerStatus = function(childNodes){
			if(childNodes && childNodes.length > 0){
				Ext.each(childNodes, function(node){
					var attrs = node.attributes;
					if(attrs && attrs.others){
						var status = node.attributes.others['status'];
						if(status == 'INVALID'){
//							thiz.hideObar(node, 'statusInvalid');
//							thiz.showObar(node, 'statusActive');
							node.getUI().getTextEl().style.color = "red";
						}
					}
					handlerStatus(thiz.getChildNodes(node));
				});
			}
		};
		handlerStatus(thiz.getChildNodes(rootNode));
	},
	checkRemove : function(n){
		if (!n.leaf) {
            Alert('地区下有子级别!');
            return false;
        }
        return true;
	},
	doDelete : function(n){
		Confirm("确定要删除该数据吗?", this ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!deleteAddress.action',
				params : {
					addrId : n.id
				},
				success : function(res,opt){
					var rs = Ext.decode(res.responseText);
					if(rs.simpleObj === false){
						Alert('还有客户在使用，暂不能删除。');
					}else{
						if(true === rs.success){
							Alert('操作成功!');
							n.remove();
						}else{
							Alert('操作失败');
				 		}
					}
				}
			});
		});
	},
	doStatusActive: function(node){
		Confirm("确定要激活小区吗?", Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!updateAddressStatus.action',
				params : {
					addrId: node.id,
					status: 'ACTIVE'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert('操作成功!');
						this.hideObar(node, 'statusActive');
						this.showObar(node, 'statusInvalid');
						node.getUI().getTextEl().style.color = "";
					}
				}
			});
		});
	},
	doStatusInvalid: function(node){
		Confirm("确定要禁用小区吗?", Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!updateAddressStatus.action',
				params : {
					addrId: node.id,
					status: 'INVALID'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert('操作成功!');
						this.hideObar(node, 'statusInvalid');
						this.showObar(node, 'statusActive');
						node.getUI().getTextEl().style.color = "red";
					}
				}
			});
		});
	}
});

/**
 * 批量添加子级
 * @class BatchAddGrid
 * @extends Ext.grid.EditorGridPanel
 */
BatchAddGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	addrStore : null,
	node : null,
	constructor : function(node){
		this.node = node;
		
		this.addrStore = new Ext.data.JsonStore({
			fields : ['addr_name','addr_full_name','addr_pid',
			'tree_level','net_type','area_id','county_id']
		});
		
		var cm = [{
			header : '显示名称',
			dataIndex :'addr_name',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '网络类型',
			dataIndex : 'net_type',
			editor : new Ext.form.TextField()		
		},{
			header : '操作',
			dataIndex : 'addr_pid',
			renderer : function(){
				return "<a href='#' onclick=Ext.getCmp(\'"+"BatchAddGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
			}
		}];
		BatchAddGrid.superclass.constructor.call(this,{
			id : 'BatchAddGrid',
			border : false,
			store : this.addrStore,
			columns : cm,
			sm:new Ext.grid.RowSelectionModel(),
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
	        	text : '添加',
	        	scope : this,
	        	iconCls : 'icon-add',
	        	handler : this.addRecord
	        }]
		});
	},
	deleteRow : function(){//删除行
		Confirm("确定要删除该数据吗?", this ,function(){
			var record= this.getSelectionModel().getSelected();
			this.getStore().remove(record);
		});
	},
	addRecord : function(){
		var others = this.node.attributes.others;
		var obj = {
			'area_id' : others.area_id,
			'county_id' : others.county_id,
			'tree_level' : parseFloat(others.tree_level)+1,
			'addr_pid' : this.node.id,			
			'addr_name' : '',
			'net_type' : ''
		};
		var Plant= this.getStore().recordType;
		var p = new Plant(obj);
		this.stopEditing();
		this.getStore().insert(0,p);
		this.startEditing(0,0);
		this.getSelectionModel().selectRow(0);
	},
	doValid : function(){
		if(this.getStore().getCount() == 0){
			return "没有数据，无需保存";
		}
		
		for(var i=0;i<this.getStore().getCount();i++){
			var rec = this.getStore().getAt(i).data;
			for(var key in rec){
				if(Ext.isEmpty(rec[key])){
					return "请编辑完所有数据";
				}
			}
		}
		return true;
	},
	doSave : function(){
		var msg = this.doValid();
		if(msg != true){
			Alert(msg);
			return;
		}
		
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
			var all = {};
			var addrList = [];
			this.getStore().each(function(item){
				addrList.push(item.data);
			});
			all['addrListStr'] = Ext.encode(addrList);
			
			Ext.Ajax.request({
				scope : this,
				url :  root + '/system/Address!saveAddrList.action',
				params : all,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						for(var i=0;i<rs.records.length;i++){
							Ext.getCmp('AddressWin').addNode(rs.records[i]);
						}
						Ext.getCmp('AddressWin').close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
})

/**
 * 地区操作窗口
 * @class AddressWin
 * @extends Ext.Window
 */
AddressWin = Ext.extend(Ext.Window,{
	node : null,
	title : null,
	item : null,
	type : null,
	initComponent : function(){
		AddressWin.superclass.initComponent.call(this);
	},
	constructor : function(type,node){
		this.type = type;
		var form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : [{
				xtype : 'hidden',
				id : 'countyId',
				name : 'county_id'
			},{
				xtype : 'hidden',
				id : 'areaId',
				name : 'area_id'
			},{
				xtype : 'hidden',
				id : 'treeLevel',
				name : 'tree_level'
			},{
				xtype : 'textfield',
				id : 'addrName',
				name : 'addr_name',
				allowBlank : false,
				width:250,
				fieldLabel : '显示名称'
			},{
				xtype : 'lovcombo',
				id : 'netType',
				paramName:'USER_TYPE',//SERV_ID
				hiddenName : 'net_type',
				typeAhead:true,editable:true,
				store:new Ext.data.JsonStore({
					fields:['item_value','item_name']
				}),displayField:'item_name',valueField:'item_value',
				triggerAction:'all',mode:'local',
				fieldLabel : '网络类型'
			}]
		});
		
		var width = 400;
		//操作节点
		this.node = node;
		var level = parseFloat(node.attributes.others.tree_level);
		if(type == 'add'){
			if(level == 1){
				this.title = '新增街道';
			}else{
				this.title = '新增路号';
			}
			this.item = form;
			Ext.getCmp('treeLevel').setValue(level+1);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
		}else if(type == 'edit'){
			if(level == 1){
				this.title = '修改城市';
			}else if(level == 2){
				this.title = '修改街道';
			}else if(level == 3){
				this.title = '修改路号';
			}
			this.item = form;
			Ext.getCmp('addrName').setValue(node.text);
			Ext.getCmp('treeLevel').setValue(level);
			Ext.getCmp('netType').setValue(node.attributes.others.net_type);
		}else if(type == 'batchAdd'){
			this.title = '批量添加地区';
			this.item = new BatchAddGrid(this.node);
			width = 500;
		}else if(type == 'leveladd'){
			if(level == 1){
				this.title = '平级新增城市';
			}else if(level == 2){
				this.title = '平级新增街道';
			}else{
				this.title = '平级新增路号';
			}
			this.item = form;
			Ext.getCmp('treeLevel').setValue(level);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
		}
		var nextNode = this.node.nextSibling;
		if(nextNode){
			alert(nextNode.attributes.others.sort_num);
		}
		
		
		
		App.form.initComboData(form.findByType("lovcombo"), function(){
			if(type =='edit' && node && node.attributes && node.attributes.others){
				Ext.getCmp('netType').setValue(node.attributes.others.net_type);
			}
		}, this);
		
		
		AddressWin.superclass.constructor.call(this,{
			id :'AddressWin',
			title : this.title,
			layout : 'fit',
			height : 200,
			width : width,
			closeAction : 'close',
			items : [this.item],
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
	doSave : function(){
		if(this.title == '批量添加地区'){//批量添加
			this.item.doSave();
		}else{//添加和修改
			if(this.item.getForm().isValid()){
				Confirm("确定保存吗?", this ,function(){
					mb = Show();//显示正在提交
					var params = {};
					params['addr.addr_name'] = Ext.getCmp('addrName').getValue();
					params['addr.net_type'] = Ext.getCmp('netType').getValue();
					
					var url = '';
					if(this.type == 'add'){
						url = root + '/system/Address!saveAddress.action';
						params['addr.addr_pid'] = this.node.id;
						params['addr.tree_level'] = Ext.getCmp('treeLevel').getValue();
						params['addr.county_id'] = Ext.getCmp('countyId').getValue();
						params['addr.area_id'] = Ext.getCmp('areaId').getValue();
					}else if(this.type == 'edit'){
						url = root + '/system/Address!editAddress.action';
						params['addr.addr_id'] = this.node.id;
					}else if(this.type == 'leveladd'){
						url = root + '/system/Address!saveAddress.action';
						params['addr.addr_pid'] = 1;
						params['addr.tree_level'] = Ext.getCmp('treeLevel').getValue();
						params['addr.county_id'] = Ext.getCmp('countyId').getValue();
						params['addr.area_id'] = Ext.getCmp('areaId').getValue();
					}
					Ext.Ajax.request({
						url : url,
						params : params,
						scope : this,
						success : function(res,opt){
							mb.hide();//隐藏提示框
							mb = null;
							var rs = Ext.decode(res.responseText);
							if(true === rs.success){
								Alert('操作成功!');
								if(this.type == 'add'){
									this.addNode(rs.simpleObj);	
								}else if( this.type == 'leveladd'){
									this.addLevelNode(rs.simpleObj);	
								}else{
									this.node.setText(Ext.getCmp('addrName').getValue());
									this.node.attributes.others.net_type = Ext.getCmp('net_type').getValue();
									this.node.attributes.others.area_id = Ext.getCmp('areaId').getValue();
									this.node.attributes.others.county_id = Ext.getCmp('countyId').getValue();
								}
								this.close();
							}else{
								Alert('操作失败');
					 		}
						}
					})
				})
			}
		}
	},
	addNode : function(newNode){
		var nc = {
            id: newNode.addr_id,
            cls : 'file',
            text : newNode.addr_name,
			others : {
				tree_level : newNode.tree_level,
				net_type : newNode.net_type,
				county_id : newNode.county_id,
				area_id:newNode.area_id
			},
            leaf: true
        };
        var nn = new Ext.ux.tree.TreeGridEditorNode(nc);
        nn.ui = new Ext.ux.tree.TreeGridEditorNodeUI(nn);
        
        this.node.leaf = false;
		this.node.attributes.leaf = false;
        this.node.appendChild(nn);
		
		this.node.editing = false;
        this.node.editMode = false;
        
        Ext.DomHelper.overwrite(Ext.fly(this.node.getUI().elNode).child('.x-treegrideditor-obar'), Ext.getCmp('AddressTree').editBtnsHtml(this.node));
        Ext.getCmp('AddressTree').activeObar(this.node);
        
//        Ext.getCmp('AddressTree').hideObar(nn, 'statusActive');
//		Ext.getCmp('AddressTree').showObar(nn, 'statusInvalid');
	},
	addLevelNode : function(newNode){
		var nc = {
            id: newNode.addr_id,
            cls : 'file',
            text : newNode.addr_name,
			others : {
				tree_level : newNode.tree_level,
				net_type : newNode.net_type,
				county_id : newNode.county_id,
				area_id:newNode.area_id
			},
            leaf: true
        };
        var nn = new Ext.ux.tree.TreeGridEditorNode(nc);
        nn.ui = new Ext.ux.tree.TreeGridEditorNodeUI(nn);
        
        this.node.parentNode.appendChild(nn);
		
        
//        Ext.DomHelper.overwrite(Ext.fly(this.node.parentNode.getUI().elNode).child('.x-treegrideditor-obar'), Ext.getCmp('AddressTree').editBtnsHtml(this.node.parentNode));
//        Ext.getCmp('AddressTree').activeObar(this.node.parentNode);
        
//        Ext.getCmp('AddressTree').hideObar(nn, 'statusActive');
//		Ext.getCmp('AddressTree').showObar(nn, 'statusInvalid');
	}
})


AddressView = Ext.extend(Ext.Panel,{
	constructor : function(){
		AddressView.superclass.constructor.call(this,{
			id : 'AddressView',
			layout : 'fit',
			title : '地区管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new AddressTree()]
		});
	}
});
