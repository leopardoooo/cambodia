/**
 * Ext 构建角色管理页面
 */
RoleForm = Ext.extend(Ext.form.FormPanel, {
    storeData: null,
    busiRuleStore: null,
    dataRightStore:null,
    ck: false,
    column:0.5,
    constructor: function (data) {
        this.busiRuleStore = new Ext.data.JsonStore({
            url: root + '/system/Index!getTruleByDataType.action',
            fields: ['rule_id', 'rule_name']
        });
        this.dataRightStore =new Ext.data.JsonStore({
			url:root+'/config/Rule!queryAllSDataType.action',
			fields:['data_right_type','is_level','type_name']
		})
        this.dataRightStore.load();
    	if(!Ext.isEmpty(data)){
        	this.storeData = data;
    	}
//		if(App.data.optr['county_id']=='4501'){
			this.column = 1;
//		}
        RoleForm.superclass.constructor.call(this, {
            id: 'RoleForm',
            layout: 'border',
            baseCls: 'x-plain',
            border: false,
            region: 'center',
            defaults: {
                bodyStyle: "background:#F9F9F9"
            },
            items: [{
                region: 'center',
                layout: 'column',
                border: false,
                bodyStyle: 'padding: 10px',
                id:'changeColumnId',
                defaults: {
                    columnWidth: this.column,
                    layout: 'form',
                    border: false,
                    labelWidth: 100
                },
                items: [{
                    items: [{
                        fieldLabel: '角色名称',
                        name: 'role_name',
                        xtype: 'textfield',
                        allowBlank: false
                    }]
                },
                {
                    items: [{
                        fieldLabel: '角色类型',
                        id: 'poleType',
                        paramName: 'ROLE_TYPE',
                        xtype: 'paramcombo',
                        allowBlank: false,
                        defaultValue: 'MENU',
                        hiddenName: 'role_type',
                        listeners: {
                            scope: this,
                            'select': function (combo, record, index) {
                                this.changeItem(combo.getValue());
                            }
                        }
                    }]
                },
                {
                    id: 'subSystemId',
                    items: [{
                        fieldLabel: '子系统',
                        paramName: 'SUB_SYSTEM',
                        xtype: 'paramcombo',
                        id: 'subSystem',
                        allowBlank: false,
                        hiddenName: 'sub_system_id',
                        listeners: {
                            scope: this,
                            'select': function (combo) {
                                Ext.getCmp('ResourceRorm').changeResource(combo.getValue());
                            }
                        }
                    }]
                },
                {
                    id: 'dataTypeId',
                    items: [{
                        fieldLabel: '数据类型',
                        paramName: 'DATA_TYPE',
                        xtype: 'paramcombo',
                        id: 'dataType',
                        allowBlank: false,
                        hiddenName: 'data_right_type',
                        listeners: {
                            scope: this,
                            'select': function (combo) {
                                this.changeItems(combo.getValue());
                            }
                        }
                    }]
                },
                {
                    id: 'ruleId',
                    items: [{
                        fieldLabel: '业务规则',
                        xtype: 'combo',
                        store: this.busiRuleStore,
                        emptyText: '请选择...',
                        mode: 'local',
                        editable: false,
                        id: 'ruleNameId',
                        valueField: 'rule_id',
                        displayField: 'rule_name',
                        hiddenName: 'rule_id'
                    }]
                },
                {
                    id: 'dataRightLevelId',
                    items: [{
                        fieldLabel: '权限级别',
                        paramName: 'SYS_LEVEL',
                        xtype: 'paramcombo',
                        id: ' dataRightLevel',
                        allowBlank: false,
                        hiddenName: 'data_right_level'
                    }]
                },
                {
                    items: [{
                        fieldLabel: '备注',
                        xtype: 'textfield',
                        name: 'role_desc'
                    }]
                },
                {
                    xtype: 'hidden',
                    name: 'role_id'
                }]
            }]

        })
    },
    initComponent: function () {
        RoleForm.superclass.initComponent.call(this);
        var comboes = this.findByType("paramcombo");
        App.form.initComboData(comboes, this.loadNext, this);
    },
    loadNext: function () {
    	Ext.getCmp('dataTypeId').hide();
        Ext.getCmp('ruleId').hide();
        Ext.getCmp('dataRightLevelId').hide();
        if (this.storeData) {
        	var type =  this.storeData.get('data_right_type');
        	this.busiRuleStore.baseParams.doneId = type;
	    	this.busiRuleStore.load();
        	this.busiRuleStore.on("load",this.loadData,this);
        }
    },
    loadData: function () {
        var Resourceitem = Ext.getCmp('ResourceRorm');
        var roleCfgwindow = Ext.getCmp('roleCfgwindow');
        var subSystemitem = Ext.getCmp('subSystemId');
        var dataTypeitem = Ext.getCmp('dataTypeId');
        var ruleitem = Ext.getCmp('ruleId');
        var dataRightLevelitem = Ext.getCmp('dataRightLevelId');
        if (this.storeData) {
            Ext.getCmp('poleType').setDisabled(true);
            var roleType = this.storeData.get('role_type');
            if (roleType == 'DATA') {
                this.changeItems(this.storeData.get('data_right_type'),'UPDATE');
                Ext.getCmp('dataType').setDisabled(true);
                subSystemitem.hide();
                Resourceitem.setVisible(false);
                Ext.getCmp('hideId').hide();
                if(App.data.optr['county_id']=='4501'){
                	roleCfgwindow.setHeight(400);
                }else{
                	roleCfgwindow.setHeight(200);
                }
                dataTypeitem.show();
            } else {
                Ext.getCmp('subSystem').setDisabled(true);
                dataTypeitem.hide();
                ruleitem.hide();
                dataRightLevelitem.hide();
                Resourceitem.setVisible(true);
                if(App.data.optr['county_id']=='4501'){
                	roleCfgwindow.setHeight(600);
                }else{
                	roleCfgwindow.setHeight(500);
                }
                Ext.getCmp('hideId').show();
           		
            }
            subSystemitem.items.each(function (item) {item.reset();});
            ruleitem.items.each(function (item) {item.reset();});
            dataTypeitem.items.each(function (item) {item.reset();});
            dataRightLevelitem.items.each(function (item) {item.reset();});
            this.loadFormData(this.storeData);
        }
    },
    changeItem: function (v) {
        var Resourceitem = Ext.getCmp('ResourceRorm');
        var roleCfgwindow = Ext.getCmp('roleCfgwindow');
        var subSystemitem = Ext.getCmp('subSystemId');
        var dataTypeitem = Ext.getCmp('dataTypeId');
        var ruleitem = Ext.getCmp('ruleId');
        var dataRightLevelitem = Ext.getCmp('dataRightLevelId');

        if (v == 'DATA') {
            Resourceitem.setVisible(false);
            Resourceitem.changeResource();
            Ext.getCmp('hideId').hide();
            if(App.data.optr['county_id']=='4501'){
        		roleCfgwindow.setHeight(400);
        	}else{
        		roleCfgwindow.setHeight(200);
        	}
            subSystemitem.hide();
            dataTypeitem.show();

        } else {
            Resourceitem.setVisible(true);
        	if(App.data.optr['county_id']=='4501'){
        		roleCfgwindow.setHeight(600);
        	}else{
        		roleCfgwindow.setHeight(500);
        	}
            subSystemitem.show();
            dataTypeitem.hide();
            ruleitem.hide();
            dataRightLevelitem.hide();
            Ext.getCmp('hideId').show();
        }

        subSystemitem.items.each(function (item) {item.reset();});
        ruleitem.items.each(function (item) {item.reset();});
        dataTypeitem.items.each(function (item) {item.reset();});
        dataRightLevelitem.items.each(function (item) {item.reset();});

    },
    changeItems: function (v,name) {
        var ruleitem = Ext.getCmp('ruleId');
        var dataRightLevelitem = Ext.getCmp('dataRightLevelId');
        var key = false;
        this.dataRightStore.each(function(record){
        	if(record.get('is_level')=='T'&& record.get('data_right_type')==v){
        		key = true;
        	}
        });
        if (key) {
            ruleitem.hide();
            dataRightLevelitem.show();
            if(v == 'ACCTDATE_EDIT'){
	            var cmb = this.find("hiddenName","data_right_level")[0];
	            var store = cmb.getStore();
				store.removeAt(store.find('item_value','AREA'));
				store.removeAt(store.find('item_value','OPTR'));
            }
            if(v == 'BUSI_OPTR'){
	            var cmb = this.find("hiddenName","data_right_level")[0];
	            var store = cmb.getStore();
				store.removeAt(store.find('item_value','AREA'));				
            }
        } else {
            ruleitem.show();
            dataRightLevelitem.hide();
        }
        ruleitem.items.each(function (item) {item.reset();});
        dataRightLevelitem.items.each(function (item) {item.reset();});
        if(Ext.isEmpty(name)){
			this.busiRuleStore.baseParams.doneId = v;
		    this.busiRuleStore.load();
        }
	    
    },
    loadFormData: function (s) {
        this.getForm().loadRecord(s);
    }
});


/**
 * 产品应用地市树
 * @class ProdCountyTree
 * @extends Ext.tree.TreePanel
 */
RoleCountyTree = Ext.extend(Ext.tree.TreePanel, {
    roleId: null,
    checkchange : false,
    constructor: function (v) {
    	if (!Ext.isEmpty(v)) {
            this.roleId = v.get('role_id');
    	}
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getRoleCountyTree.action?query=" + this.roleId 
        });
        RoleCountyTree.superclass.constructor.call(this, {
        	id : 'RoleCountyTree',
        	border : false,
        	split: true,
            region : 'east',
            width: '50%',
            margins: '0 0 3 2',
            layout: 'fit',
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
                checked : false,
                text: '应用地市'
            }
        });
        this.expandAll();
    },
    initEvents: function(){
    	this.on("afterrender",function(){
    		var node = this.getRootNode();
			if(App.data.optr['county_id'] == '4501'){
    			node.eachChild(function (child) {
    				if(child.id == '4500'&&child.attributes.id == '4500'){
            			child.ui.toggleCheck(true);
	                	child.attributes.checked = true;
            		}
	            })
    		}
			//如果不是修改
    		if(!this.roleId){
    			if(App.data.optr['county_id'] != '4501'){
	        		node.attributes.checked = true;
	        		node.ui.toggleCheck(true);
	        		node.fireEvent('checkchange', node, true);
    			}
    		}
		},this,{delay:3000});
		
		this.on('checkchange',function (node, checked) {
			this.checkchange = true;
            node.expand();
            node.attributes.checked = checked;
        	node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
            
        },this);
		
		RoleCountyTree.superclass.initEvents.call(this);
	}
	,getCheckedIds : function(){
		var prodCountyIds = [];
        var nodes = this.getChecked();
        for (var i in nodes) {
            if (nodes[i].leaf) {
                prodCountyIds.push(nodes[i].id);
            }
        }
        return prodCountyIds;
	}
});

ResourceRorm = Ext.extend(Ext.Panel, {
    IdleRoleGrid: null,
    useRoleGrid: null,
    idleRoleStroe: null,
    useRoleStroe: null,
    setProd: null,
    setUprod: null,
    store: null,
    subId: null,
    roleId: null,
    constructor: function (v) {
        this.idleRoleStroe = new Ext.data.JsonStore({
            url: root + '/system/Role!getResBySystemId.action',
            fields: ['res_id', 'res_name']
        });
        this.idleRoleStroe.setDefaultSort("res_name", "ASC");
        this.setProd = new Ext.grid.CheckboxSelectionModel();
        this.setUprod = new Ext.grid.CheckboxSelectionModel();
        this.useRoleStroe = new Ext.data.JsonStore({
            url: root + '/system/Role!findRoleResource.action',
            fields: ['res_id', 'res_name']
        });
        this.useRoleStroe.setDefaultSort("res_name", "ASC");
        if (!Ext.isEmpty(v)) {
            this.subId = v.get('sub_system_id');
            this.roleId = v.get('role_id');
            if (!Ext.isEmpty(this.subId)) {
                this.idleRoleStroe.load({
                    params: {doneId: this.subId,role_id: this.roleId}
                });
                if (!Ext.isEmpty(this.roleId)) {
                    this.useRoleStroe.load({
                        params: {doneId: this.subId,role_id: this.roleId}
                    });
                }
            }
        }
        this.IdleRoleGrid = new Ext.grid.EditorGridPanel({
            title: '未分配系统功能',
            border: true,
            autoScroll: true,
            ds: this.idleRoleStroe,
            sm: this.setProd,
            region: 'center',
            columns: [this.setProd,{header: '功能名称',width : 180,dataIndex: 'res_name'}],
			tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
							listeners:{
								scope:this,
								keyup:function(txt,e){
									if(e.getKey() == Ext.EventObject.ENTER){
										var value = txt.getValue();
											this.idleRoleStroe.filterBy(function(record){
												if(Ext.isEmpty(value))
													return true;
												else
													return record.get('res_name').indexOf(value)>=0;
											},this);
									}
								}
							}
						}]
        });

        this.useRoleGrid = new Ext.grid.EditorGridPanel({
            title: '已分配系统功能',
            region: 'center',
            ds: this.useRoleStroe,
            sm: this.setUprod,
            height: 180,
            autoScroll: true,
            border: true,
            clicksToEdit: 1,
            columns: [this.setUprod,{header: '功能名称',dataIndex: 'res_name',width : 180,sortable: true,renderer: App.qtipValue}],
			tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,id:'selectSystemId',
						listeners:{
							scope:this,
								keyup:function(txt,e){
										if(e.getKey() == Ext.EventObject.ENTER){
											var value = txt.getValue();
												this.useRoleStroe.filterBy(function(record){
													if(Ext.isEmpty(value))
														return true;
													else
														return record.get('res_name').indexOf(value)>=0;
												},this);
											}
										}
									}
							}]
        });
        ResourceRorm.superclass.constructor.call(this, {
            id: 'ResourceRorm',
            region: 'center',
            layout: 'border',
            border: false,
            items: [this.IdleRoleGrid,
            {
                region: 'east',
                border: false,
                layout: 'border',
                width: '57%',
                items: [this.useRoleGrid,
                {
                    border: false,
                    bodyStyle: 'background-color: #DFE8F6;',
                    region: 'west',
                    width: 60,
                    layout: {
                        type: 'vbox',
                        pack: 'center',
                        align: 'center'
                    },
                    items: [{
                        xtype: 'button',
                        iconCls: 'move-to-right',
                        width: 40,
                        scale: 'large',
                        tooltip: '将左边已勾选的功能分配给该角色!',
                        iconAlign: 'center',
                        scope: this,
                        handler: function () {
                            this.doProdIn(this.setProd.getSelections());
                        }
                    },
                    {
                        height: 30,
                        baseCls: 'x-plain'
                    },
                    {
                        xtype: 'button',
                        iconCls: 'move-to-left',
                        tooltip: '将右边勾选中的功能取消!',
                        width: 40,
                        scale: 'large',
                        iconAlign: 'center',
                        scope: this,
                        handler: function () {
                            this.doProdOut(this.setUprod.getSelections());
                        }
                    }]
                }]
            }]
        });
    },
    doProdIn: function (arr) {
        if (arr.length == 0) {
            Alert("请在左边的列表中选择系统功能!");
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            var panl = this.useRoleStroe.recordType;
            var u = new panl({
                res_id: arr[i].data.res_id,
                "res_name": arr[i].data.res_name
            });
            this.useRoleGrid.stopEditing();
            this.useRoleStroe.insert(this.useRoleStroe.getCount(), u);
            this.useRoleGrid.startEditing(this.useRoleStroe.getCount(), 0);
            this.idleRoleStroe.remove(arr[i]);
        }

    },
    doProdOut: function (arr) {
        if (arr.length == 0) {
            Alert("请在右边的列表中选择系统功能!");
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            var panl = this.idleRoleStroe.recordType;
            var u = new panl({
                res_id: arr[i].data.res_id,
                'res_name': arr[i].data["res_name"]
            });
            this.IdleRoleGrid.stopEditing();
            this.idleRoleStroe.insert(this.idleRoleStroe.getCount(), u);
            this.IdleRoleGrid.startEditing(this.idleRoleStroe.getCount(), 0);
            this.useRoleStroe.remove(arr[i]);
        }
        Ext.getCmp('selectSystemId').setValue("");
        this.useRoleStroe.filterBy(function(record){
				return true;
        });
        
    },
    changeResource: function (v) {
        this.idleRoleStroe.load({params: {doneId: v}});
        this.useRoleStroe.removeAll();
    },
    changerStroe: function () {
        this.idleRoleStroe.load({params: {doneId: 1}});
        this.useRoleStroe.removeAll();
    }
});

RoleWindow = Ext.extend(Ext.Window, {
    roleForm: null,
    resourceRorm: null,
    roleCountyTree:null,
    formAll:null,
    USERHEIGHT:600,
    constructor: function (v) {
        var hidder = false;
       
    	if(!Ext.isEmpty(v)){
    		var roleType = v.get('role_type');
        	if (roleType == 'DATA') {
        		hidder = true;
        		if (App.data.optr['county_id'] == '4501') {
					this.USERHEIGHT = 400;
				} else {
					this.USERHEIGHT = 200;
				}
	    	}
    	}
    	this.roleForm = new RoleForm(v)
        this.resourceRorm = new ResourceRorm(v);
        this.roleCountyTree = new RoleCountyTree(v);
        
//        if(App.data.optr['county_id']=='4501'){
			 this.formAll = [this.roleForm,this.roleCountyTree];
//		}else{
////			this.formAll = [this.roleForm];
//		}
       
        RoleWindow.superclass.constructor.call(this, {
            width: 600,
            height: this.USERHEIGHT,
            layout: 'border',
            id: 'roleCfgwindow',
            closeAction: 'close',
            items: [{
                region: 'center',
                spilt: true,
                layout: 'border',
                border : false,
                items: this.formAll
            },
            {
                region: 'south',
                spilt: true,
                id:'hideId',
                hidden:hidder,
                layout: 'fit',
                height: 300,
                items: [this.resourceRorm]
            }],
            buttons: [{
                text: '保存',
                scope: this,
                iconCls: 'icon-save',
                handler: this.doSave
            },
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.close();
                }
            }]
        });
    },
    doSave: function () {
        var valid = true;
        //由于有组件会隐藏，修改验证方法
        this.roleForm.getForm().items.each(function (f) {
            if (!f.validate() && f.ownerCt.hidden === false) {
                valid = false;
            }
        });
       	if(!valid)return;
        var old = this.roleForm.getForm().getValues(),
            newValues = {};
        for (var key in old) {
            newValues["role." + key] = old[key];
        }

        var resIds = [];
        this.resourceRorm.useRoleGrid.stopEditing();
        var resStore = this.resourceRorm.useRoleStroe;
        resStore.clearFilter(true);//清楚过滤器，返回所有数据
        for (var i = 0; i < resStore.getCount(); i++) {
            resIds.push(resStore.getAt(i).data.res_id);
        }
        if (resIds.length > 0) {
            newValues["resIds"] = resIds;
        }
        
        var roleCountyIds = [];
        if (this.roleCountyTree) {
            var nodes = this.roleCountyTree.getChecked();
            for (var i in nodes) {
                if (nodes[i].leaf) {
                    roleCountyIds.push(nodes[i].id);
                }
            }
        }
//        if(App.data.optr['county_id'] != '4501'){
//        	roleCountyIds = [];
//        	roleCountyIds.push(App.data.optr['county_id']);
//        }
        if(roleCountyIds.length == 0){
        	Alert('请选择适用地区');
        	return;
        }
        
        //产品适用地区
        newValues['countyIds'] = roleCountyIds;
        
        var that = this;
        mb = Show();// 显示正在提交
        Confirm("确定要保存吗?", this, function () {
            Ext.Ajax.request({
                params: newValues,
                url: root + '/system/Role!save.action',
                scope:this,
                success: function (res, ops) {
                	mb.hide();// 隐藏提示框
					mb = null;
                    var rs = Ext.decode(res.responseText);
                    if (true === rs.success) {
                        Alert("操作成功!", function () {
                            Ext.getCmp('roleManagePanelId').getStore().load({
                                params: {
                                    start: 0,
                                    limit: Constant.DEFAULT_PAGE_SIZE
                                }
                            });
                            that.close();
                        });
                    } else {
                        Alert("操作失败!");
                    }
                }
            });
        });
    }
});
GoToOptrTree = Ext.extend(Ext.ux.FilterTreePanel, {
    constructor: function (v) {
    	goToOptrTreeThis = this;
    	var newValues = {};
    	newValues["SRoleDto.role_id"] = v.get('role_id');
    	newValues["SRoleDto.role_type"] = v.get('role_type');
    	newValues["SRoleDto.sub_system_id"] = v.get('sub_system_id');
    	newValues["SRoleDto.data_right_type"] = v.get('data_right_type');
        var loader = new Ext.tree.TreeLoader({
        	url: root + "/system/Role!goToOptrTree.action",
        	baseParams: {
       			role_id : v.get('role_id'),
        		role_type: v.get('role_type'),
				sub_system_id : v.get('sub_system_id'), 
				data_right_type : v.get('data_right_type'),
				rule_id : v.get('rule_id'),
				data_right_level:v.get('data_right_level')
        	}
        });
        GoToOptrTree.superclass.constructor.call(this, {
            split: true,
            id: 'GoToOptrTreeId',
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
    },
    openNext:function(){
    	var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var i = 0; i < childarr.length; i++) {
				if (childarr[i].loaded == false) {
                    childarr[i].expand();
                    childarr[i].collapse();
                }
			}
		}
	},
	initComponent : function() {
		GoToOptrTree.superclass.initComponent.call(this);
		this.getRootNode().expand();
		this.getRootNode().on('expand', function() {
					goToOptrTreeThis.openNext();
				});
	}
});

GoToOptrWindow = Ext.extend(Ext.Window, {
			goToOptrTree : null,
			roleId:null,
			constructor : function(v) {
				goToOptrWinThis = this;
				this.roleId = v.get('role_id');
				this.goToOptrTree = new GoToOptrTree(v);
				GoToOptrWindow.superclass.constructor.call(this, {
							layout : 'fit',
							width : 400,
							height : 500,
							closeAction : 'hide',
							items : this.goToOptrTree,
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
			},saveOptr:function(){
				var optrItemId = [],all={};
		        if (Ext.getCmp('GoToOptrTreeId')) {
		            var nodes = Ext.getCmp('GoToOptrTreeId').getChecked();
		            for (var i in nodes) {
		                if (nodes[i].leaf) {
		                    optrItemId.push(nodes[i].id);
		                }
		            }
		        }
		        if (optrItemId.length > 0) {
		            all["optrIds"] = optrItemId;
		        }
		        all["role_id"]=this.roleId;
				Ext.Ajax.request({
					url : root + '/system/Role!saveRoleToOptrs.action',
					params : all,
					success : function(res, ops) {
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Alert('操作成功!', function() {goToOptrWinThis.close();}, goToOptrWinThis);
						} else {Alert('操作失败!');}
					}
				});
			}
		});

RoleManagePanel = Ext.extend(Ext.grid.GridPanel, {
    store: null,
    constructor: function () {
        roleManagePanel = this;

        this.store = new Ext.data.JsonStore({
            url: root + '/system/Role!queryRoles.action',
            root: 'records',
            totalProperty: 'totalProperty',
            autoDestroy: true,
            fields: Ext.data.Record.create([
            {name: 'role_id'},
            {name: 'role_name'},
            {name: 'role_type'},
            {name: 'role_desc'},
            {name: 'sub_system_id'},
            {name: 'data_right_type'},
            {name: 'data_right_level'},
            {name: 'data_right_level_text'},
            {name: 'creator'},
            {name: 'rule_id'},
            {name: 'data_right_type_text'},
            {name: 'role_type_text'},
            {name: 'sub_system_text'},
            {name: 'county_id'},
            {name: 'county_name'},
            {name: 'area_id'},
            {name: 'rule_name'}])
        });
        this.store.load({
            params: {
                start: 0,
                limit: Constant.DEFAULT_PAGE_SIZE
            }
        });
        var sm = new Ext.grid.CheckboxSelectionModel();
        var columns = [
			sm,
            {
                id: 'role_name',
                header: '角色名称',
                dataIndex: 'role_name',
                width: 120,
                renderer: App.qtipValue
            },
            {
                header: '角色类型',
                dataIndex: 'role_type_text',
                width: 60
            },
            {
                header: '角色描述',
                dataIndex: 'role_desc',
                width: 120
            },
            {
                header: '子系统名称',
                dataIndex: 'sub_system_text',
                width: 80
            },
            {
                header: '数据类型',
                dataIndex: 'data_right_type_text',
                width: 80
            },
            {
                header: '权限级别',
                dataIndex: 'data_right_level_text',
                width: 80
            },
            {
                header: '业务规则',
                dataIndex: 'rule_name',
                width: 80
            },
		    {   header: '操作',
		        width:170,
		        scope:this,
	            renderer:function(value,meta,record,rowIndex,columnIndex,store){
	            	var btns = this.doFilterBtns(record);
	            	return btns;
				}
			}
		];
        
        RoleManagePanel.superclass.constructor.call(this, {
            id: 'roleManagePanelId',
            region: "center",
            ds: this.store,
            sm:sm,
            columns:columns,
            bbar: new Ext.PagingToolbar({
                store: this.store
//              ,plugins:[new Ext.ux.plugins.PageComboResizer()]//动态分页大小
            }),
            tbar: [' ', ' ', '输入关键字', ' ', new Ext.ux.form.SearchField({
                store: this.store,
                width: 200,
                hasSearch: true,
                emptyText: '支持所有模糊查询'
            }), '->',
            {
                text: '添加',
                iconCls: 'icon-add',
                scope: this,
//              hidden : App.data.optr['county_id']!='4501' ? true : false,
                handler: this.addRecord
            }]
        })
    },
	doFilterBtns : function(record){
		var btns = "";
			btns = btns + "<a href='#' onclick='roleManagePanel.goToOptr();' style='color:blue'>分配操作员</a>";
		if(App.data.optr['county_id'] == '4501'){
				btns = btns + "&nbsp;&nbsp;<a href='#' onclick='roleManagePanel.updateRecord();' style='color:blue'> 修改 </a>";
				btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick='roleManagePanel.deleteRecord();'> 删除 </a>";
		}else{
			if(record.get('county_id') == App.data.optr['county_id']){
				btns = btns + "&nbsp;&nbsp;<a href='#' onclick='roleManagePanel.updateRecord();' style='color:blue'> 修改 </a>";
				btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick='roleManagePanel.deleteRecord();'> 删除 </a>";
			}
		}
		return btns;
	},
    // 删除角色
    deleteRecord: function () {
    	var grid  = Ext.getCmp('roleManagePanelId');
    	var role_id = grid.getSelectionModel().getSelected().get('role_id');
        Confirm("确定要删除该角色吗?", null, function () {
            Ext.Ajax.request({
                url: root + '/system/Role!delete.action',
                params: {
                    role_id: role_id
                },
                success: function (res, ops) {
                    var rs = Ext.decode(res.responseText);
                    if (true === rs.success) {
                        Alert('操作成功!', function () {
                            Ext.getCmp('roleManagePanelId').getStore().load({
                                params: {
                                    start: 0,
                                    limit: Constant.DEFAULT_PAGE_SIZE
                                }
                            });
                        });
                    } else {
                        Alert('操作失败');
                    }
                }
            });
        })
    },
    // 角色分配
    addRecord: function () {
        var win = new RoleWindow();
        win.setTitle('增加角色');
        win.setIconClass('icon-add-user');
        win.show();
    },
    updateRecord: function () {
    	var grid  = Ext.getCmp('roleManagePanelId');
        var win = new RoleWindow(grid.getSelectionModel().getSelected());
        win.setTitle('修改角色');
        win.setIconClass('icon-edit-user');
        win.show();
    },goToOptr:function(v){
    	var grid  = Ext.getCmp('roleManagePanelId');
        var win = new GoToOptrWindow(grid.getSelectionModel().getSelected());
    	win.setTitle('分配角色->操作员');
 		win.setIconClass('icon-add-user');   	
 		win.show();
    }
    ,
    loadData: function (ps) {
        this.store.baseParams = ps;
        this.store.load({
            params: {
                start: 0,
                limit: Constant.DEFAULT_PAGE_SIZE
            }
        });
    }
})

roleManage = Ext.extend(Ext.Panel, {
    constructor: function () {
        var roleManagePanel = new RoleManagePanel();
        roleManage.superclass.constructor.call(this, {
            id: 'roleManage',
            title: '角色管理',
            closable: true,
            border: false,
            layout: 'border',
            baseCls: "x-plain",
            items: [roleManagePanel]
        });
    }
});