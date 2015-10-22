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
        	Alert(langUtils.sys('AddressNodeManage.msg.noSingleQuoteAllowed'));
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

AddressTreeLoader = Ext.extend(Ext.tree.TreeLoader,{
	load : function(node, callback, scope){
        if(this.clearOnLoad){
            while(node.firstChild){
                node.removeChild(node.firstChild);
            }
        }
        if(this.doPreload(node)){ // preloaded json children
            this.runCallback(callback, scope || node, [node]);
        }else if(this.directFn || this.dataUrl || this.url){
            this.requestData(node, callback, scope || node);
        }
    },
    doPreload : function(node){//新增is_refresh 为了刷新父节点
        if(node.attributes.children && node.attributes.is_refresh){
            if(node.childNodes.length < 1){ // preloaded?
                var cs = node.attributes.children;
                node.beginUpdate();
                for(var i = 0, len = cs.length; i < len; i++){
                    var cn = node.appendChild(this.createNode(cs[i]));
                    if(this.preloadChildren){
                        this.doPreload(cn);
                    }
                }
                node.endUpdate();
            }
            return true;
        }
        return false;
    }


})


/**
 * 地址表格编辑树
 * @class AddressTree
 * @extends Ext.ux.tree.TreeGridEditor
 */
AddressTree = Ext.extend(Ext.ux.tree.TreeGridEditor,{
	queryText : null,
	constructor : function(){
		var addressThiz = this;
		AddressTree.superclass.constructor.call(this,{
			id : 'AddressTree',
			root : new Ext.tree.AsyncTreeNode({expanded : true,id:'-1'}),
			loader: new AddressTreeLoader({
	            dataUrl: root+"/system/Address!queryAddrTree.action",
	            listeners:{
	            	scope:this,
	            	load: this.doLoad,
	            	beforeload : this.onBeforeLoad
	            }
	        }),
	        maxDepth: 4,
	        columnResize:true,
	        enableHdMenu:true,
	        rootNodeId: 'tge-root',
	        // 超出最大深度，提示信息
    		maxDepthText:langUtils.sys('AddressNodeManage.msg.maxDepthText') ,
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: langUtils.sys('AddressNodeManage.formWin.labelAddrTree'),
	            dataIndex: 'text',
//	            autoWidth: true,
	            width:150,
	            displayTip: true
	        }],
	        // 设置Obar
	        obarCfg: {
	            column: {
	                header: langUtils.sys('common.doActionBtn'),
	                dataIndex: 'id',
	                width: 500
	            },
	            btns: [{
	                id: 'add',
	                text:langUtils.sys('AddressNodeManage.formWin.labelNewAddChild'),
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('add',n).show();
	                }
	            },{
	                id: 'leveladd',
	                deepestState: 'uncreated',
	                text:langUtils.sys('AddressNodeManage.formWin.labelNewAddBrother'),
	                handler : function(n){
	                	new AddressWin('leveladd',n).show();
	                }
	            },{
	                id: 'edit',
	                text:langUtils.sys('common.update'),
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
	                text:langUtils.sys('common.enableBtn'),
	                handler : this.doStatusActive
	            }, {
	                id: 'statusInvalid',
	                text:langUtils.sys('common.forbiddenBtn') ,
	                handler : this.doStatusInvalid,
	                validator: this.checkRemove
	            }]
	        }
	        ,tbar: [' ',{
	            xtype: 'searchfield',
	            emptyText : langUtils.sys('AddressNodeManage.formWin.emptyTxtBlurQuery'),
	            triggerTips: [langUtils.sys('common.cancelBtn'), langUtils.sys('common.cancelBtn')],
	            listeners: {
	            	scope : this,
	                search: function(text) {
	                	addressThiz.queryText = text;
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
										  id:'-1',
										  draggable:false,
										  is_refresh:true,
										  children:data
										 });
								addressThiz.getRootNode().removeAll();									 
								addressThiz.setRootNode(root);
								addressThiz.doExpandnode();
							}
						});
	                }
	            }
	        }]
		});
		
	},
	doExpandnode:function(){
		var node = this.getRootNode();
		var thiz = this;
		var handlerStatus = function(childNodes){
			if(childNodes && childNodes.length > 0){
				Ext.each(childNodes, function(node){
					var attrs = node.attributes;
					if(attrs && attrs.others){
						var status = node.attributes.others['status'];
						if(status == 'INVALID'){
							node.getUI().getTextEl().style.color = "red";
						}
					}
					handlerStatus(thiz.getChildNodes(node));
				});
			}
		};
		handlerStatus(thiz.getChildNodes(node));
	},
	onBeforeLoad:function(l,node){
		l.on('beforeload',function(loader,node){
			var id = node.id;
			if(id == 'tge-root'){
				id = "";
			}
  			l.baseParams = {addrId:id}; //通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
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
//		if (!n.leaf) {
//            Alert(langUtils.sys('AddressNodeManage.msg.cantBeInvalided'));
//            return false;
//        }
        return true;
	},
	doDelete : function(n){
		Confirm(langUtils.sys('AddressNodeManage.msg.confirmDelete'), this ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!deleteAddress.action',
				params : {
					addrId : n.id
				},
				success : function(res,opt){
					var rs = Ext.decode(res.responseText);
					if(rs.simpleObj === false){
						Alert(langUtils.sys('AddressNodeManage.msg.cantDelete'));
					}else{
						if(true === rs.success){
							Alert(langUtils.sys('AddressNodeManage.msg.actionSuccess'));
							n.remove();
						}else{
							Alert(langUtils.sys('AddressNodeManage.msg.actionFailed'));
				 		}
					}
				}
			});
		});
	},
	doStatusActive: function(node){
		Confirm(langUtils.sys('AddressNodeManage.msg.confirmActivate'), Ext.getCmp('AddressTree') ,function(){
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
						Alert(langUtils.sys('AddressNodeManage.msg.actionSuccess'));
						node.parentNode.attributes.is_refresh = false;
						node.parentNode.reload();
					}
				}
			});
		});
	},
	doStatusInvalid: function(node){
		Confirm(langUtils.sys('AddressNodeManage.msg.confirmInvalid'), Ext.getCmp('AddressTree') ,function(){
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
						Alert(langUtils.sys('AddressNodeManage.msg.actionSuccess'));
						node.parentNode.attributes.is_refresh = false;
						node.parentNode.reload();
					}
				}
			});
		});
	}
});

AddressDistrictTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	listeners:{
		expand:function(thisCmp){
			function getFocus(){
				var search = thisCmp.list.searchT;//如果第一次获取焦点,list会为空
				if(search){
//					search.setValue('输入关键字搜索');
					var dom = search.el.dom;
					dom.select();
				}
			};
			getFocus.defer(200,this);
		}
	},
	firstExpand: false,
	doQuery : function(q, forceAll) {
		if(!this.firstExpand){
			this.firstExpand = true;
			this.list.expandAll();
		}
		this.expand();
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
	itemForm : null,
	type : null,
	provinceStore:null,
	level : null,
	constructor : function(type,node){
		this.type = type;
		this.node = node;
		this.provinceStore = new Ext.data.JsonStore({
				url : root + '/system/Address!queryProvince.action',
 				fields : ['name','id']
 		});
		this.itemForm = new Ext.form.FormPanel({
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
				id : 'addressDistrictId',
				name : 'district_id'
			},{
				xtype : 'hidden',
				id : 'areaId',
				name : 'area_id'
			},{
				xtype : 'hidden',
				id : 'treeLevel',
				name : 'tree_level'
			},{
				xtype : 'displayfield',
				id : 'parentName',
				name : 'parent_name',
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelParentName')
			},{
				xtype : 'textfield',
				id : 'addrName',
				name : 'addr_name',
				allowBlank : false,
				width:250,
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelShowName')
			}]
		});
		
		var width = 400;
		//操作节点
		this.node = node;
		var fieldLabel = langUtils.sys('AddressNodeManage.formWin.labelShowName');
		this.level = parseFloat(node.attributes.others.tree_level);
		if(this.type == 'add'){
			if(this.level == 1){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelStreet');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelStreatName');
			}else{
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelRoadNum');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('treeLevel').setValue(this.level+1);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
		}else if(this.type == 'edit'){
			if(this.level == 1){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelCity');
				fieldLabel = langUtils.sys('AddressNodeManage.formWin.labelCityName');
			}else if(this.level == 2){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelStreet');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelStreatName');
			}else if(this.level == 3){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelRoadNum');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('addrName').setValue(node.text);
			Ext.getCmp('treeLevel').setValue(this.level);
		}else if(this.type == 'leveladd'){
			if(this.level == 1){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelCity');
				fieldLabel = langUtils.sys('AddressNodeManage.formWin.labelCityName');
			}else if(this.level == 2){
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelStreet');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelStreatName');
			}else{
				this.title = langUtils.sys('AddressNodeManage.formWin.titleNewSaveLevelRoadNum');
				fieldLabel =langUtils.sys('AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('treeLevel').setValue(this.level);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
		}
		Ext.getCmp('addrName').fieldLabel =fieldLabel;
		Ext.getCmp('parentName').setValue(node.text);
		
		AddressWin.superclass.constructor.call(this,{
			id :'AddressWin',
			title : this.title,
			layout : 'fit',
			height : 300,
			width : width,
			closeAction : 'close',
			items : [this.itemForm],
			buttons : [{
				text : langUtils.sys('AddressNodeManage.formWin.btnTxtSave'),
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : langUtils.sys('AddressNodeManage.formWin.btnTxtClose'),
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		});
		this.doLayout();
	},
	initComponent : function(){
		if((this.level == 2 && this.type =='add') || this.level == 3){
			this.itemForm.add({
				xtype : 'lovcombo',
				id : 'netType',
				paramName:'USER_TYPE',//SERV_ID
				hiddenName : 'net_type',
				typeAhead:true,editable:true,
				store:new Ext.data.JsonStore({
					fields:['item_value','item_name']
				}),displayField:'item_name',valueField:'item_value',
				triggerAction:'all',mode:'local',
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelNetType')
			});
			
			App.form.initComboData(this.itemForm.findByType("lovcombo"), function(){
				if(this.level == 3 && this.node && this.node.attributes && this.node.attributes.others){
					Ext.getCmp('netType').setValue(this.node.attributes.others.net_type);
				}
			}, this);
			
			var provinceId;
			if(this.level == 3){
				provinceId = this.node.parentNode.parentNode.attributes.others.district_id;
			}else{
				provinceId = this.node.parentNode.attributes.others.district_id;
			}
			this.itemForm.add(new AddressDistrictTreeCombo({
		    	width:200,
		    	id : 'addressDistrictItemId',
				treeWidth:300,
				allowBlank : false,
				minChars:2,
				height: 22,
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelDistrict'),
				treeUrl: root + '/system/Address!queryDistrictByPid.action',
				treeParams : {addrId:provinceId},
				hiddenName:'addrId',
				editable:false,
				onlySelectLeaf:false,
				listeners:{
					scope: this,
					'select': function(combo){
						Ext.getCmp('addressDistrictId').setValue(combo.getValue());
					}
				}
			}));
			
			if( this.level == 3 && this.type =='edit'){
				Ext.getCmp('addressDistrictItemId').setValue(this.node.attributes.others.district_name);
				Ext.getCmp('addressDistrictId').setValue(this.node.attributes.others.district_id);
			}
		}
		
		
		if(this.level == 1 && (this.type =='leveladd' || this.type == 'edit')){
			this.itemForm.add({
				xtype:'combo',
				id : 'provinceId',
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelProvince'),
				forceSelection : true,
				store : this.provinceStore,
				triggerAction : 'all',
				mode: 'local',
				displayField : 'name',
				valueField : 'id',
				emptyText: langUtils.sys('AddressNodeManage.formWin.emptyTxtProvince'),
				editable : false
			});
					
			this.provinceStore.on("load",this.doLoadProvince,this);
			this.provinceStore.load();						
		}else{
			var comp =  Ext.getCmp('provinceId');
			if(comp){
				this.itemForm.remove(comp,true);
			}
		}

		if(this.type == 'edit'){
			this.itemForm.add({
				xtype : 'numberfield',
				id : 'sortNum',
				name : 'sort_num',
				fieldLabel : langUtils.sys('AddressNodeManage.formWin.labelSortNum')
			});
			Ext.getCmp('sortNum').setValue(this.node.attributes.others.sort_num);
		}
		
		this.doLayout();						
		AddressWin.superclass.initComponent.call(this);
	},
	doLoadProvince:function(s){
		if(!Ext.isEmpty(this.node.attributes.others.district_id)){
			Ext.getCmp('provinceId').setValue(this.node.attributes.others.district_id);
		}
	},
	doSave : function(){
		//添加和修改
		if(this.itemForm.getForm().isValid()){
			Confirm(langUtils.sys('AddressNodeManage.msg.confirmSave'), this ,function(){
				mb = Show();//显示正在提交
				var params = {};
				
				var url = '';
				if(this.type == 'add'){
					url = root + '/system/Address!saveAddress.action';
					params['addrDto.addr_pid'] = this.node.id;
					params['addrDto.addr_last_id'] = this.node.id;
					params['addrDto.tree_level'] = Ext.getCmp('treeLevel').getValue();
					params['addrDto.county_id'] = Ext.getCmp('countyId').getValue();
					params['addrDto.area_id'] = Ext.getCmp('areaId').getValue();
				}else if(this.type == 'edit'){
					url = root + '/system/Address!editAddress.action';
					params['addrDto.addr_id'] = this.node.id;
					
				}else if(this.type == 'leveladd'){
					url = root + '/system/Address!saveAddress.action';
					var pId = this.node.parentNode.id;
					if(pId == 'tge-root'){
						pId = 1;
					}
					params['addrDto.addr_pid'] = pId;
					params['addrDto.addr_last_id'] = this.node.id;
					params['addrDto.tree_level'] = Ext.getCmp('treeLevel').getValue();
					params['addrDto.county_id'] = Ext.getCmp('countyId').getValue();
					params['addrDto.area_id'] = Ext.getCmp('areaId').getValue();
				}
				
				params['addrDto.addr_name'] = Ext.getCmp('addrName').getValue();
				if((this.level == 2 && this.type =='add') || this.level == 3){
					params['addrDto.net_type'] = Ext.getCmp('netType').getValue();
				}
				if(this.level == 1 && (this.type =='leveladd' || this.type == 'edit')){
					params['addrDto.district_id'] = Ext.getCmp('provinceId').getValue();
				}
				if((this.level == 2 && this.type =='add') || this.level == 3){
					params['addrDto.district_id'] = Ext.getCmp('addressDistrictId').getValue();
				}
				if(Ext.getCmp('sortNum')){
					params['addrDto.sort_num'] = Ext.getCmp('sortNum').getValue();
				}
				params['type'] = this.type;
				Ext.Ajax.request({
					url : url,
					params : params,
					scope : this,
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert(langUtils.sys('AddressNodeManage.msg.actionSuccess'));
							if(this.type == 'add'){
								if(this.node.leaf){
									this.node.parentNode.attributes.is_refresh = false;
									this.node.parentNode.reload();
								}else{
									this.node.attributes.is_refresh = false;
									this.node.reload();
								}
							}else{
								this.node.parentNode.attributes.is_refresh = false;
								this.node.parentNode.reload();
							}

							this.close();
						}else{
							Alert(langUtils.sys('AddressNodeManage.msg.actionFailed'));
				 		}
					}
				})
			})
		}
	}
})


AddressView = Ext.extend(Ext.Panel,{
	constructor : function(){
		AddressView.superclass.constructor.call(this,{
			id : 'AddressView',
			layout : 'fit',
			title : langUtils.sys('AddressNodeManage.panelTitle'),
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new AddressTree()]
		});
	}
});
