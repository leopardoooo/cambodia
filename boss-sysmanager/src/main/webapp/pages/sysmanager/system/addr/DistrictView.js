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
        	Alert(langUtils.sys('DistrictNodeManage.msg.noSingleQuoteAllowed'));
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
 * @class DistrictTree
 * @extends Ext.ux.tree.TreeGridEditor
 */
DistrictTree = Ext.extend(Ext.ux.tree.TreeGridEditor,{
	queryText : null,
	constructor : function(){
		var DistrictThiz = this;
		DistrictTree.superclass.constructor.call(this,{
			id : 'DistrictTree',
			root : new Ext.tree.AsyncTreeNode({expanded : true,id:'-1'}),
			loader: new AddressTreeLoader({
	            dataUrl: root+"/system/Address!queryDistrictTree.action",
	            listeners:{
	            	scope:this,
	            	load: this.doLoad,
	            	beforeload : this.onBeforeLoad
	            }
	        }),
	        maxDepth: 5,
	        columnResize:true,
	        enableHdMenu:true,
	        rootNodeId: 'tge-root',
	        // 超出最大深度，提示信息
    		maxDepthText:langUtils.sys('DistrictNodeManage.msg.maxDepthText') ,
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: langUtils.sys('DistrictNodeManage.formWin.labelAddrTree'),
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
	                text:langUtils.sys('DistrictNodeManage.formWin.labelNewAddChild'),
	                deepestState: 'uncreated',
	                handler : function(n){
	                	new DistrictWin('add',n).show();
	                }
	            },{
	                id: 'edit',
	                text:langUtils.sys('common.update'),
	                handler : function(n){
	                	new DistrictWin('edit',n).show();
	                }
	            }, {
	                id: 'statusActive',
	                text:langUtils.sys('common.enableBtn'),
	                handler : this.doStatusActive
	            }, {
	                id: 'statusInvalid',
	                text:langUtils.sys('common.forbiddenBtn') ,
	                handler : this.doStatusInvalid
	            }]
	        }
	        ,tbar: [' ',{
	            xtype: 'searchfield',
	            emptyText : langUtils.sys('DistrictNodeManage.formWin.emptyTxtBlurQuery'),
	            triggerTips: [langUtils.sys('common.cancelBtn'), langUtils.sys('common.cancelBtn')],
	            listeners: {
	            	scope : this,
	                search: function(text) {
	                	DistrictThiz.queryText = text;
	                	Ext.Ajax.request({
							url: root+"/system/Address!queryDistrictTree.action",
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
								DistrictThiz.getRootNode().removeAll();									 
								DistrictThiz.setRootNode(root);
								DistrictThiz.doExpandnode();
							}
						});
	                }
	            }
	        }]
		});
		
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
	doStatusActive: function(node){
		Confirm(langUtils.sys('DistrictNodeManage.msg.confirmActivate'), Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!updateDistructStatus.action',
				params : {
					addrId: node.id,
					status: 'ACTIVE'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert(langUtils.sys('DistrictNodeManage.msg.actionSuccess'));
						node.parentNode.attributes.is_refresh = false;
						node.parentNode.reload();
					}
				}
			});
		});
	},
	doStatusInvalid: function(node){
		Confirm(langUtils.sys('DistrictNodeManage.msg.confirmInvalid'), Ext.getCmp('AddressTree') ,function(){
			Ext.Ajax.request({
				scope : this,
				url : root + '/system/Address!updateDistructStatus.action',
				params : {
					addrId: node.id,
					status: 'INVALID'
				},
				success : function(res,opt){
					var res = Ext.decode(res.responseText);
					if(res === true){
						Alert(langUtils.sys('DistrictNodeManage.msg.actionSuccess'));
						node.parentNode.attributes.is_refresh = false;
						node.parentNode.reload();
					}
				}
			});
		});
	}
});

DistrictWin = Ext.extend(Ext.Window,{
	node : null,
	title : null,
	itemForm : null,
	type : null,
	level : null,
	constructor : function(type,node){
		this.type = type;
		this.node = node;
		this.itemForm = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 120,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : [{
				xtype : 'hidden',
				id : 'districtLevel',
				name : 'district_level'
			},{
				xtype : 'displayfield',
				id : 'parentName',
				name : 'parent_name',
				fieldLabel : langUtils.sys('DistrictNodeManage.formWin.labelParentName')
			},{
				xtype : 'textfield',
				id : 'districtName',
				name : 'district_name',
				allowBlank : false,
				width:250,
				fieldLabel : langUtils.sys('DistrictNodeManage.formWin.labelShowName')
			},{
				id : 'districtDesc',
				name : 'district_desc',
				height : 40,
				width : 250,
				allowBlank : false,
				xtype:'textarea',
				fieldLabel : lsys('DistrictNodeManage.formWin.labelDesc')
			},{
				id : 'districtRemark',
				name : 'remark',
				height : 80,
				width : 250,
				xtype:'textarea',
				fieldLabel : lsys('DistrictNodeManage.formWin.labelremark')
			}]
		});
		
		//操作节点
		this.node = node;
		var fieldLabel;
		this.level = parseFloat(node.attributes.others.district_level);
		
		var pcmp = Ext.getCmp('parentName');
		if(this.type == 'add'){
			if(this.level == 0){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelProvince');
			}else if(this.level == 1){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelCityName');
			}else if(this.level == 2){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelCityDistrictName');
			}else if(this.level == 3){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelSubDistrictName');
			}
			this.title = lsys('common.addNewOne');
			pcmp.hideLabel = false;
			pcmp.show();
			pcmp.setValue(node.text);
			Ext.getCmp('districtLevel').setValue(this.level+1);
		}else if(this.type == 'edit'){
			if(this.level == 0){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelCountry');
			}else if(this.level == 1){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelProvince');
			}else if(this.level == 2){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelCityName');
			}else if(this.level == 3){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelCityDistrictName');
			}else if(this.level == 4){
				fieldLabel =lsys('DistrictNodeManage.formWin.labelSubDistrictName');
			}
			this.title = lsys('common.update');
			pcmp.hideLabel = true;
			pcmp.hide();
			Ext.getCmp('districtDesc').setValue(node.attributes.others.district_desc);
			Ext.getCmp('districtRemark').setValue(node.attributes.others.remark);
			Ext.getCmp('districtName').setValue(node.text);
			Ext.getCmp('districtLevel').setValue(this.level);
		}
		Ext.getCmp('districtName').fieldLabel =fieldLabel;
		this.doLayout();
		
		DistrictWin.superclass.constructor.call(this,{
			id :'DistrictWin',
			title : this.title,
			layout : 'fit',
			height : 300,
			width : 500,
			closeAction : 'close',
			items : [this.itemForm],
			buttons : [{
				text : langUtils.sys('DistrictNodeManage.formWin.btnTxtSave'),
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : langUtils.sys('DistrictNodeManage.formWin.btnTxtClose'),
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		});
		this.doLayout();
	},
	initComponent : function(){
		DistrictWin.superclass.initComponent.call(this);
	},
	doSave : function(){
		//添加和修改
		if(this.itemForm.getForm().isValid()){
			Confirm(langUtils.sys('DistrictNodeManage.msg.confirmSave'), this ,function(){
				mb = Show();//显示正在提交
				var params = {};
				
				if(this.type == 'add'){
					params['disDto.parent_id'] = this.node.id;
					params['disDto.district_level'] = Ext.getCmp('districtLevel').getValue();
					if(this.level == 1){
						params['disDto.province_id'] = this.node.id;
					}else if(this.level == 2){
						params['disDto.province_id'] = this.node.parentNode.id;
					}else if(this.level == 3){
						params['disDto.province_id'] = this.node.parentNode.parentNode.id;
					}
				}else if(this.type == 'edit'){
					params['disDto.district_id'] = this.node.id;
				}
				params['disDto.district_desc'] = Ext.getCmp('districtDesc').getValue();
				params['disDto.remark'] = Ext.getCmp('districtRemark').getValue();
				params['disDto.district_name'] = Ext.getCmp('districtName').getValue();
				Ext.Ajax.request({
					url : root + '/system/Address!updateDistruct.action',
					params : params,
					scope : this,
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs){
							Alert(langUtils.sys('DistrictNodeManage.msg.actionSuccess'));
							if(this.type == 'add'){
								this.node.attributes.is_refresh = false;
								this.node.reload();
							}else{
								this.node.parentNode.attributes.is_refresh = false;
								this.node.parentNode.reload();
							}

							this.close();
						}else{
							Alert(langUtils.sys('DistrictNodeManage.msg.actionFailed'));
				 		}
					}
				})
			})
		}
	}
})


DistrictView = Ext.extend(Ext.Panel,{
	constructor : function(){
		DistrictView.superclass.constructor.call(this,{
			id : 'DistrictView',
			layout : 'fit',
			title : '行政区域管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new DistrictTree()]
		});
	}
});
