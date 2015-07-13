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
	            dataUrl: root+"/system/Address!queryAddresses.action",
	            baseParams: {
	                method: 'load'
	            },
	            listeners:{
	            	scope:this,
	            	load: this.doLoad
	            }
	        }),
	        maxDepth: 3,
	        // 超出最大深度，提示信息
    		maxDepthText: '不能再往下添加地区',
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: '地区名称',
	            dataIndex: 'text',
	            autoWidth: true,
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
	                id: 'batchAdd',
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('batchAdd',n).show();
	                }
	            }, {
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
	            emptyText : '支持小区中文或拼音',
	            triggerTips: ['查询', '取消'],
	            listeners: {
	            	scope : this,
	                search: function(text) {
	                	this.getLoader().baseParams.queryText = text;
				        this.getRootNode().reload();
				        this.getRootNode().expand();
				        this.getRootNode().on('expand',function(){ 
				        	if (App.data.optr['count_id'] != '4501') {
					            this.expandAll();
					        } else {
					            this.getRootNode().expand();
					        }
						},this);
	                }
	            }
	        }, {
	            iconCls: 'icon-expand-all',
				tooltip: '展开所有资源',
	            listeners: {
	            	scope : this,
	                click: function() {
	                    this.expandAll();
	                }
	            }
	        }, {
	            iconCls: 'icon-collapse-all',
	            tooltip: '合并所有资源',
	            listeners: {
	            	scope : this,
	                click: function() {
	                    this.collapseAll();
	                }
	            }
	        }]
		});
		
	},
	doLoad: function(treeLoader,rootNode){
		var thiz = this;
		var handlerStatus = function(childNodes){
			if(childNodes && childNodes.length > 0){
				Ext.each(childNodes, function(node){
					var attrs = node.attributes;
					if(attrs && attrs.others){
						var status = node.attributes.others['status'];
						if(status == 'ACTIVE'){
							thiz.hideObar(node, 'statusActive');
							thiz.showObar(node, 'statusInvalid');
						}else if(status == 'INVALID'){
							thiz.hideObar(node, 'statusInvalid');
							thiz.showObar(node, 'statusActive');
							node.getUI().getTextEl().style.color = "red";
						}
					}
					handlerStatus(thiz.getChildNodes(node));
				});
			}
		}
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
						Alert('还有客户在使用，暂不能删除。')
					}else{
						if(true === rs.success){
							Alert('操作成功!');
							n.remove();
						}else{
							Alert('操作失败');
				 		}
					}
				}
			})
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
		
		this.busiOptrStore = new Ext.data.JsonStore({
			url:root + '/system/Address!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr']
		});
		this.busiOptrStore.load();
		
		this.addrStore = new Ext.data.JsonStore({
			fields : ['addr_name','addr_full_name','addr_pid',
			'tree_level','capacity','net_type','area_id','county_id','busi_optr_id','busi_optr_name','serv_optr_id','serv_optr_name']
		})
		
		var cm = [{
			header : '显示名称',
			dataIndex :'addr_name',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '完整名称',
			dataIndex : 'addr_full_name',
			editor : new Ext.form.TextField()
		},{
			header : '小区客户容量',
			dataIndex : 'capacity',
			editor : new Ext.form.NumberField({
				allowNegative : false,
				allowDecimals : false
			})
		},{
			header : '小区网络类型',
			dataIndex : 'net_type',
			editor : new Ext.form.TextField()
		},{
			header: '客户经理',
			dataIndex:'busi_optr_name',
			editor: new Ext.form.ComboBox({
				hiddenName : 'busi_optr_id',
				fieldLabel : '客户经理',
				store:this.busiOptrStore,
				valueField:'optr_name',displayField:'optr_name',
				triggerAction:'all',forceSelection:true,editable:true,
				listeners:{
					scope:this,
					beforequery:function(e){
						var combo = e.combo;
						var store = combo.getStore();
			            var value = e.query;
				        if(Ext.isEmpty(value)){ 
							store.clearFilter();
				        }else{
				            combo.collapse();
				        	var re = new RegExp('^.*' + value + '.*$','i');
				            store.filterBy(function(record,id){
				                var text = record.get('attr');
				                return re.test(text);
				            });
				            combo.expand();
				            return false;
				        }
					},
					select:function(combo,record){
						this.getSelectionModel().getSelected().set('busi_optr_id',record.get('optr_id'));
					}
				}
			})
		},{
			header: '运维人员',
			dataIndex:'serv_optr_name',
			editor: new Ext.form.ComboBox({
				hiddenName : 'serv_optr_id',
				fieldLabel : '运维人员',
				store:this.busiOptrStore,
				valueField:'optr_name',displayField:'optr_name',
				triggerAction:'all',forceSelection:true,editable:true,
				listeners:{
					scope:this,
					beforequery:function(e){
						var combo = e.combo;
						var store = combo.getStore();
			            var value = e.query;
				        if(Ext.isEmpty(value)){ 
							store.clearFilter();
				        }else{
				            combo.collapse();
				        	var re = new RegExp('^.*' + value + '.*$','i');
				            store.filterBy(function(record,id){
				                var text = record.get('attr');
				                return re.test(text);
				            });
				            combo.expand();
				            return false;
				        }
					},
					select:function(combo,record){
						this.getSelectionModel().getSelected().set('serv_optr_id',record.get('optr_id'));
					}
				}
			})
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
		})
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
			'busi_optr_id':others.busi_optr_id,
			'busi_optr_name':others.busi_optr_name,
			'serv_optr_id':others.serv_optr_id,
			'serv_optr_name':others.serv_optr_name,
			'addr_name' : '',
			'addr_full_name' : '',
			'capacity' : 0,
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
	initComponent : function(){
		AddressWin.superclass.initComponent.call(this);
	},
	constructor : function(type,node){
		this.busiOptrStore = new Ext.data.JsonStore({
			url:root + '/system/Address!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr']
		});
		this.busiOptrStore.load();
		this.busiOptrStore.on("load",function(){
			if(node){
				var optrId = node.attributes.others.busi_optr_id;
				var servOptrId = node.attributes.others.serv_optr_id;
				if(optrId){
					Ext.getCmp('busiOptrId_id').setValue(node.attributes.others.busi_optr_id);					
				}
				if(servOptrId){
					Ext.getCmp('servOptrId_id').setValue(node.attributes.others.serv_optr_id);					
				}
			}
		});
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
				fieldLabel : '显示名称'
			},{
				xtype : 'textfield',
				id : 'addrFullName',
				name : 'addr_full_name',
				allowBlank : false,
				fieldLabel : '完整名称'
			},{
				xtype : 'numberfield',
				id : 'capacity',
				name : 'capacity',
				allowNegative : false,
				allowDecimals : false,
				fieldLabel : '小区客户容量'
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
				fieldLabel : '小区网络类型'
			},{
				xtype : 'combo',
				id:'busiOptrId_id',
				hiddenName : 'busi_optr_id',
				fieldLabel : '客户经理',
				store:this.busiOptrStore,
				valueField:'optr_id',displayField:'optr_name',
				triggerAction:'all',forceSelection:true,editable:true,
				listeners:{
					beforequery:function(e){
						var combo = e.combo;
						var store = combo.getStore();
			            var value = e.query;
				        if(Ext.isEmpty(value)){ 
							store.clearFilter();
				        }else{
				            combo.collapse();
				        	var re = new RegExp('^.*' + value + '.*$','i');
				            store.filterBy(function(record,id){
				                var text = record.get('attr');
				                return re.test(text);
				            });
				            combo.expand();
				            return false;
				        }
					}
				}
			},{
				xtype : 'combo',
				id:'servOptrId_id',
				hiddenName : 'serv_optr_id',
				fieldLabel : '运维人员',
				store:this.busiOptrStore,
				valueField:'optr_id',displayField:'optr_name',
				triggerAction:'all',forceSelection:true,editable:true,
				listeners:{
					beforequery:function(e){
						var combo = e.combo;
						var store = combo.getStore();
			            var value = e.query;
				        if(Ext.isEmpty(value)){ 
							store.clearFilter();
				        }else{
				            combo.collapse();
				        	var re = new RegExp('^.*' + value + '.*$','i');
				            store.filterBy(function(record,id){
				                var text = record.get('attr');
				                return re.test(text);
				            });
				            combo.expand();
				            return false;
				        }
					}
				}
			}]
		});
		
		var width = 300;
		//操作节点
		this.node = node;
		if(type == 'add'){
			this.title = '增加地区';
			this.item = form;
			Ext.getCmp('treeLevel').setValue(parseFloat(node.attributes.others.tree_level)+1);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
		}else if(type == 'edit'){
			this.title = '修改地区';
			this.item = form;
			Ext.getCmp('addrName').setValue(node.attributes.others.fullName);
			Ext.getCmp('addrFullName').setValue(node.attributes.others.fullName);
			Ext.getCmp('treeLevel').setValue(parseFloat(node.attributes.others.tree_level));
			Ext.getCmp('capacity').setValue(node.attributes.others.capacity);
//			Ext.getCmp('netType').setValue(node.attributes.others.net_type);
			Ext.getCmp('busiOptrId_id').setValue(node.attributes.others.busi_optr_id);
			Ext.getCmp('servOptrId_id').setValue(node.attributes.others.serv_optr_id);
		}else if(type == 'batchAdd'){
			this.title = '批量添加地区';
			this.item = new BatchAddGrid(this.node);
			width = 500;
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
			height : 250,
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
		})
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
					params['addr.addr_full_name'] = Ext.getCmp('addrFullName').getValue();
					params['addr.capacity'] = Ext.getCmp('capacity').getValue();
					params['addr.net_type'] = Ext.getCmp('netType').getValue();
					params['addr.busi_optr_id'] = Ext.getCmp('busiOptrId_id').getValue();
					params['addr.serv_optr_id'] = Ext.getCmp('servOptrId_id').getValue();
					
					var url = '';
					if(this.title == '增加地区'){
						url = root + '/system/Address!saveAddress.action';
						params['addr.addr_pid'] = this.node.id;
						params['addr.tree_level'] = Ext.getCmp('treeLevel').getValue();
						params['addr.county_id'] = Ext.getCmp('countyId').getValue();
						params['addr.area_id'] = Ext.getCmp('areaId').getValue();
					}else{
						url = root + '/system/Address!editAddress.action';
						params['addr.addr_id'] = this.node.id;
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
								if(this.title == '增加地区'){
									this.addNode(rs.simpleObj);	
								}else{
									this.node.setText(Ext.getCmp('addrName').getValue());
									this.node.attributes.others.fullName = Ext.getCmp('addrFullName').getValue();
									this.node.attributes.others.net_type = Ext.getCmp('capacity').getValue();
									this.node.attributes.others.capacity = Ext.getCmp('netType').getValue();
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
				fullName : newNode.addr_full_name,
				tree_level : newNode.tree_level,
				net_type : newNode.net_type,
				capacity : newNode.capacity
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
        
        Ext.DomHelper.overwrite(Ext.fly(this.node.getUI().elNode).child('.x-treegrideditor-obar'), Ext.getCmp('AddressTree').editBtnsHtml(this.node))
        Ext.getCmp('AddressTree').activeObar(this.node);
        
        Ext.getCmp('AddressTree').hideObar(nn, 'statusActive');
		Ext.getCmp('AddressTree').showObar(nn, 'statusInvalid');
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
		})
	}
})
