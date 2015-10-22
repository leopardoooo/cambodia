/**
 * 修改地址
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
        	Alert(langUtils.bc('home.tools.AddressNodeManage.msg.noSingleQuoteAllowed'));
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
	constructor : function(){
		var addressThiz = this;
		AddressTree.superclass.constructor.call(this,{
			id : 'AddressTree',
			root : new Ext.tree.AsyncTreeNode({expanded : true,id:'tge-root'}),
			loader: new AddressTreeLoader({
	            dataUrl: Constant.ROOT_PATH + "/commons/x/QueryParam!queryAddressTree.action",
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
    		maxDepthText:langUtils.bc('home.tools.AddressNodeManage.msg.maxDepthText') ,
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: langUtils.bc('home.tools.AddressNodeManage.formWin.labelAddrTree'),
	            dataIndex: 'text',
	            width:160,
	            displayTip: true
	        }],
	        // 设置Obar
	        obarCfg: {
	            column: {
	                header: langUtils.bc('common.doActionBtn'),
	                dataIndex: 'id',
	                width:250
	            },
	            btns: [{
	                id: 'add',
	                text:langUtils.bc('home.tools.AddressNodeManage.formWin.labelNewAddChild'),
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new AddressWin('add',n).show();
	                }
	            },{
	                id: 'leveladd',
	                deepestState: 'uncreated',
	                text:langUtils.bc('home.tools.AddressNodeManage.formWin.labelNewAddBrother'),
	                handler : function(n){
	                	new AddressWin('leveladd',n).show();
	                }
	            },{
	                id: 'edit',
	                text: lbc('common.update'),
	                handler : function(n){
	                	new AddressWin('edit',n).show();
	                }
	            }, {
	                id: 'statusActive',
	                text: lbc('common.enableBtn'),
	                handler : this.doStatusActive
	            }, {
	                id: 'statusInvalid',
	                text: lbc('common.forbiddenBtn') ,
	                handler : this.doStatusInvalid,
	                validator: this.checkRemove
	            }]
	        }
	        ,tbar: [' ',{
	            xtype: 'searchfield',
	            emptyText : langUtils.bc('home.tools.AddressNodeManage.formWin.emptyTxtBlurQuery'),
	            triggerTips: [langUtils.bc('common.cancelBtn'), langUtils.bc('common.cancelBtn')],
	            listeners: {
	            	scope : this,
	                search: function(text) {
	                	Ext.Ajax.request({
							url:  Constant.ROOT_PATH + "/commons/x/QueryParam!queryAddressTree.action",
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
//            Alert(langUtils.bc('home.tools.AddressNodeManage.msg.cantBeInvalided'));
//            return false;
//        }
        return true;
	},
	doDelete : function(n){
		Confirm(langUtils.bc('home.tools.AddressNodeManage.msg.confirmDelete'), this ,function(){
			Ext.Ajax.request({
				scope : this,
				url :  Constant.ROOT_PATH + "/commons/x/QueryParam!deleteAddress.action",
				params : {
					addrId : n.id
				},
				success : function(res,opt){
					var rs = Ext.decode(res.responseText);
					if(rs.simpleObj === false){
						Alert(langUtils.bc('home.tools.AddressNodeManage.msg.cantDelete'));
					}else{
						if(true === rs.success){
							Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionSuccess'));
							n.remove();
						}else{
							Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionFailed'));
				 		}
					}
				}
			});
		});
	},
	doStatusActive: function(node){
		Confirm(langUtils.bc('home.tools.AddressNodeManage.msg.confirmActivate'), Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url :  Constant.ROOT_PATH + "/commons/x/QueryParam!updateAddressStatus.action",
				params : {
					addrId: node.id,
					status: 'ACTIVE'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionSuccess'));
						node.parentNode.attributes.is_refresh = false;
						node.parentNode.reload();
					}
				}
			});
		});
	},
	doStatusInvalid: function(node){
		Confirm(langUtils.bc('home.tools.AddressNodeManage.msg.confirmInvalid'), Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url :  Constant.ROOT_PATH + "/commons/x/QueryParam!updateAddressStatus.action",
				params : {
					addrId: node.id,
					status: 'INVALID'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionSuccess'));
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
				url :  Constant.ROOT_PATH + "/commons/x/QueryParam!queryProvince.action",
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
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelParentName')
			},{
				xtype : 'textfield',
				id : 'addrName',
				name : 'addr_name',
				allowBlank : false,
				width:250,
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelShowName')
			}]
		});
		
		var width = 400;
		//操作节点
		this.node = node;
		var fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelShowName');
		this.level = parseFloat(node.attributes.others.tree_level);
		if(this.type == 'add'){
			if(this.level == 1){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleNewSaveLevelStreet');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelStreatName');
			}else{
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleNewSaveLevelRoadNum');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('treeLevel').setValue(this.level+1);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
			Ext.getCmp('parentName').fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelParentName');
		}else if(this.type == 'edit'){
			if(this.level == 1){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleEditSaveLevelCity');
				fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelCityName');
			}else if(this.level == 2){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleEditSaveLevelStreet');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelStreatName');
			}else if(this.level == 3){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleEditSaveLevelRoadNum');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('addrName').setValue(node.text);
			Ext.getCmp('treeLevel').setValue(this.level);
			Ext.getCmp('parentName').fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelOldName');
		}else if(this.type == 'leveladd'){
			if(this.level == 1){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleNewSaveLevelCity');
				fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelCityName');
			}else if(this.level == 2){
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleNewSaveLevelStreet');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelStreatName');
			}else{
				this.title = langUtils.bc('home.tools.AddressNodeManage.formWin.titleNewSaveLevelRoadNum');
				fieldLabel =langUtils.bc('home.tools.AddressNodeManage.formWin.labelRoadNum');
			}
			Ext.getCmp('treeLevel').setValue(this.level);
			Ext.getCmp('areaId').setValue(node.attributes.others.area_id);
			Ext.getCmp('countyId').setValue(node.attributes.others.county_id);
			Ext.getCmp('parentName').fieldLabel = langUtils.bc('home.tools.AddressNodeManage.formWin.labelBrotherName');
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
				text : langUtils.bc('home.tools.AddressNodeManage.formWin.btnTxtSave'),
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : langUtils.bc('home.tools.AddressNodeManage.formWin.btnTxtClose'),
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
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelNetType')
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
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelDistrict'),
				treeUrl:  Constant.ROOT_PATH + "/commons/x/QueryParam!queryDistrictByPid.action",
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
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelProvince'),
				forceSelection : true,
				store : this.provinceStore,
				triggerAction : 'all',
				mode: 'local',
				displayField : 'name',
				valueField : 'id',
				emptyText: langUtils.bc('home.tools.AddressNodeManage.formWin.emptyTxtProvince'),
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
				fieldLabel : langUtils.bc('home.tools.AddressNodeManage.formWin.labelSortNum')
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
			Confirm(langUtils.bc('home.tools.AddressNodeManage.msg.confirmSave'), this ,function(){
				mb = Show();//显示正在提交
				var params = {};
				
				var url = '';
				if(this.type == 'add'){
					url =  Constant.ROOT_PATH + "/commons/x/QueryParam!saveAddress.action";
					params['addrDto.addr_pid'] = this.node.id;
					params['addrDto.addr_last_id'] = this.node.id;
					params['addrDto.tree_level'] = Ext.getCmp('treeLevel').getValue();
					params['addrDto.county_id'] = Ext.getCmp('countyId').getValue();
					params['addrDto.area_id'] = Ext.getCmp('areaId').getValue();
				}else if(this.type == 'edit'){
					url =  Constant.ROOT_PATH + "/commons/x/QueryParam!editAddress.action";
					params['addrDto.addr_id'] = this.node.id;
					
				}else if(this.type == 'leveladd'){
					url =  Constant.ROOT_PATH + "/commons/x/QueryParam!saveAddress.action";
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
							Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionSuccess'));
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
							Alert(langUtils.bc('home.tools.AddressNodeManage.msg.actionFailed'));
				 		}
					}
				})
			})
		}
	}
})

AddressViewWin = Ext.extend(Ext.Window,{
	addrTree:null,
	constructor:function(){
		this.addrTree = new AddressTree();
		AddressViewWin.superclass.constructor.call(this,{
			id:'AddressViewWinId',
			layout : 'fit',
			title : lbc('home.tools.AddressNodeManage.panelTitle'),
			border : false ,
			closeAction : 'hide',
			width:800,
			height : 550,
			items : [this.addrTree],
			buttons : [{text : lbc('common.close'),scope : this,handler : function() {this.hide();}}]
		});
	}
});
