/**
 * Ext 构建菜单管理页面
 */
var addTitle = "新增菜单" ,addCls = "icon-add-user",
    editTitle= "修改菜单" ,editCls= "icon-edit-user";
	subSystem = [
	    	['营业子系统','1'],
	    	['管理子系统','2'],
	    	['工单子系统','3'], 
	    	['报表子系统','7']
	];
//菜单资源列表面板
var ResourceGrid= Ext.extend(Ext.grid.GridPanel,{
	resourceStore: null ,
	roleFromWindow: null,
	constructor: function(){
		resourceGrid = this;
		this.resourceStore = new Ext.data.JsonStore({
			url  : root + '/system/Resource!queryResources.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			autoDestroy: true,
			baseParams: {limit: Constant.DEFAULT_PAGE_SIZE },
			fields : Ext.data.Record.create([
			{name: 'res_id'},
			{name: 'res_name'},
			{name: 'panel_name'},
			{name: 'show_name'},
			{name: 'res_pid'},
			{name: 'sub_system_id'},
			{name: 'obj_type'},
			{name: 'fun_name'},
			{name: 'res_type'},
			{name: 'url'},
			{name : 'handler'},
			{name: 'busicode'},
			{name: 'businame'},
			{name: 'iconcls'},
			{name: 'sort_num'},
			{name: 'remark'},
			{name: 'res_pid_text'},
			{name: 'sub_system_text'}
		])
		}) ;
		
		ResourceGrid.superclass.constructor.call(this,{
			id:'resourceGridId',
			region : "center" ,
			title  : '符合条件的菜单资源【菜单管理】',
			ds: this.resourceStore ,
			cm: new Ext.grid.ColumnModel([
				{header: '菜单名称', dataIndex: 'res_name',renderer: App.qtipValue},
				{header: '菜单URL', dataIndex: 'url',renderer: App.qtipValue},
				{header: '面板名称', dataIndex: 'panel_name',renderer: App.qtipValue},
				{header: '按钮名称', dataIndex: 'show_name',renderer: App.qtipValue},
				{header: '上级菜单', dataIndex: 'res_pid_text',renderer: App.qtipValue},
				{header: '菜单类型', dataIndex: 'obj_type'},
				{header: '排序', dataIndex: 'sort_num'},
				{header: '子系统', dataIndex: 'sub_system_text',renderer: App.qtipValue},
				{header: '业务名称', dataIndex: 'businame'},
				{header: '操作', dataIndex: 'res_id' ,width:150, renderer:function( v , md, record , i  ){
					return "<a href='#' onclick='resourceGrid.addRole("+ v +","+ i +");' style='color:blue'> 角色分配 </a>" +
						   "&nbsp; <a href='#' onclick='resourceGrid.deleteInfo("+ v +","+ i +");' style='color:red'> 删除 </a>";
				}}
			]),
			bbar: new Ext.PagingToolbar({store: this.resourceStore }),  //PagingToolbar用来提供分页控制的工具栏
			tbar: [' ',' ','输入关键字' , ' ',       //搜索功能
				new Ext.ux.form.SearchField({  
	                store: this.resourceStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持菜单名称模糊查询'
	            })
			]
		})
	},
	deleteInfo : function(v ,rowIndex){
  		Confirm("确定要删除菜单资源吗?", null ,function(){
	  		Ext.Ajax.request({
	  			url: root + '/system/Resource!delete.action',
	  			params: {res_id: v },
	  			success: function( res,ops ){
	  				var rs = Ext.decode(res.responseText);
	  				if(true === rs.success){
	  				Alert('操作成功!',function(){
	  					resourceGrid.getStore().removeAt( rowIndex );
	  				});
	  				}else{
	  				Alert('操作失败');
	  				}
		  		}
	  		});
  		})
  	},
  	addRole : function(v ,rowIndex){
  		this.roleFromWindow = new RoleFromWindow(v ,rowIndex );
		this.roleFromWindow.show();
  	},
	listeners:{      
		"rowdblclick" : function(g, i, e){
			var rs = g.getStore().getAt(i);
			var _prs = new Ext.data.Record({id: rs.get('res_pid') , text: rs.get("res_pid_text")});
			var resourceForm = Ext.getCmp('resourceFormId');
			var resComStore = resourceForm.find("hiddenName", "res_pid")[0].getStore();
			resComStore.removeAll();
			resComStore.add(_prs);
  			resourceForm.getForm().loadRecord( rs );
  			resourceForm.setTitle( editTitle );
  			resourceForm.setIconClass( editCls );
		 }
	}
})
//构造表单
var ResourceForm =  Ext.extend(Ext.form.FormPanel,{
	constructor : function() {
		ResourceForm.superclass.constructor.call(this, {
			id:'resourceFormId',
			title:  addTitle,
			iconCls: addCls,
			region: 'south',
			split: true,  //窗口可以拆分
			height: 190,
			layout: 'column',
			bodyStyle: 'padding: 5px',
			defaults: { 
				labelWidth: 70,
				layout: 'form',
				baseCls: 'x-plain',
				defaultType: 'textfield'
			}, 
			items: [{
				xtype: 'hidden', //使用xtype则是延迟创建 这样的好处是当需要渲染此组件时才创建 类似hibernate的延迟加载 都是为了提高效率！
				name: 'res_id'
			},{
				labelWidth: 80,
				columnWidth: .3,
				items: [{
					fieldLabel: '菜单名称',
					name: 'res_name',
					allowBlank: false,//不允许空白
					maxLength: 40
				}, 
				new Ext.form.ComboBox({
			   		fieldLabel: '所属系统',
			   		hiddenName: 'sub_system_id', //提交到后台的input的name 一定要写sub_system_id
			   		name:'sub_system_text',
			   		store: new Ext.data.ArrayStore({  //选择值存储的地方
						fields: ['name','value'],
			        	data : subSystem
			  		 }),
			  	 	displayField: 'name', //指定是在下拉中显示的选择值
			   		valueField: 'value',  //指定是提交给后台的值 
			   		typeAhead: true,  //相当于自动匹配的开关，就像google的查询搜索框，输入前面自动提示
			   		mode: 'local',  // 数据是在本地，所以设置了模式为local（mode: 'local'）。 
			  		triggerAction: 'all',
			  
			   		//如果不设，你选择了某个选项后，你的下拉将只会出现匹配选项值文本的选择项，其它选择项是不会再显示了，这样你就不能更改其它选项了。
			   		emptyText: this.stateEmtyText,
			   		selectOnFocus:true,
			   		allowBlank: false}),
			  	{
			  		xtype : 'paramcombo',
			  		paramName :'BUSI_CODE',
			  		fieldLabel: '业务代码',
					hiddenName: 'busicode',
					maxLength: 10
			  	},{
			  		fieldLabel : '面板名称',
			  		xtype : 'combo',
			  		store: new Ext.data.ArrayStore({  //选择值存储的地方
						fields: ['value'],
			        	data : [
			        		[''],['C_UNIT'],['C_DEVICE'],['D_BUSI,D_INVOICE,D_TASK'],['U_PROD'],['C_CUST'],['P_ACCT'],
			        		['U_USER'],['P_BUSI'],['D_DONE'],['C_PACKAGE'],['A_ACCT'],['A_ITEM'],['D_INVOICE']
			        	]
			  		 }),
			  		displayField: 'value', //指定是在下拉中显示的选择值
			   		valueField: 'value',
			   		name : 'panel_name'
			  	}]
			},{
				//相当于一列
				columnWidth: .3,
				items: [{
					fieldLabel: '菜单URL',
					name: 'url',
					maxLength: 1000
				},{
					fieldLabel: '调用JS名称',
					name: 'handler',
					maxLength: 30
				},{
					fieldLabel: 'CSS样式',
					name: 'iconcls',
					maxLength: 30
				},{
					fieldLabel : '按钮名称',
					name : 'show_name'
				}]
			},{
				columnWidth: .3,
				items: [{
					fieldLabel: '上级菜单',
					hiddenName: 'res_pid',
					allowBlank: false,
					name: 'res_pid',
					treeUrl: root + '/system/Index!loadTreeMenus.action',
					editable: false,   //该下拉列表只允许选择，不允许输入
					rootNodeCfg: {
						id: '0',
						text: '系统资源目录'
					},
					isCanClick: function(node){
		//				if(node.leaf){
		//					return false;
		//				}
						return true ;
					},
					hideTrigger: false,
					xtype: 'treecombo',//使用xtype则是延迟创建
					maxLength: 30
				},{
					xtype : 'combo',
					fieldLabel: '资源类型',
					store: new Ext.data.ArrayStore({  //选择值存储的地方
						fields: ['value'],
			        	data : [
			        		['NODE'],
			        		['MENU']
			        	]
			  		 }),
			  		displayField: 'value', //指定是在下拉中显示的选择值
			   		valueField: 'value',
			   		allowBlank : false,
					name: 'res_type',
					maxLength: 40
				},{
					fieldLabel: '排序',
					name: 'sort_num',
					xtype: 'numberfield',
					maxLength: 10
				}]
			}],
			buttons: [{
				text: ' 新 建 ',
				scope: this,
				handler:this.doReset
			},{
				text: ' 保 存 ',
				scope: this,
				handler:this.doSave
			}]
		})
	},
	initComponent : function(){
		ResourceForm.superclass.initComponent.call(this);
		var comboes = this.findByType('paramcombo');
		App.form.initComboData( comboes);
	},
	doSave: function(){
		
		if(!this.getForm().isValid()){return ;}
		var old =  this.getForm().getValues(), newValues = {};
		for(var key in old){
			newValues["resource." + key] = old[key];
		}
		Confirm("确定要保存吗?", this , function(date){   
			Ext.Ajax.request({
				params: newValues,
		  		url: root + '/system/Resource!save.action',
		  		success:function(res,ops){
		  			var rs = Ext.decode(res.responseText);
  					if(true === rs.success){
  						Alert('操作成功!',function(){
  							if(old.res_id){//刷新表格
  								Ext.getCmp('resourceGridId').getStore().reload();
  							}else{//刷新树
  								Ext.getCmp('resourceNavMenu').getRootNode().reload();
  							}
  						});
  					}else{
  						Alert('操作失败');
  					}
		  		}
		  	});
		});
	},
	doReset: function(){
		this.getForm().reset();
		this.setIconClass( addCls  );
		this.setTitle( addTitle );
	}
 });
var NavMenu = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	
	constructor: function(){
		var loader = new Ext.tree.TreeLoader({
			url : root + '/system/Index!loadTreeMenus.action'
		});
		NavMenu.superclass.constructor.call(this, {
			id : 'resourceNavMenu',
			region: 'west',
			width 	: 210,
			split	: true,
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',  //元素上右下左和外面元素的距离都是0
	        lines		:false,  // 去掉树的线
	        autoScroll	:true,  //如果设置为true，则当显示内容超出区域时显示滚动条
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',    
			loader 		: loader,
	        root: {   //根节点
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				text: '系统资源菜单'
			}
		});
		this.getRootNode().expand();	  
	},
	initEvents : function(){
		this.on("click" , function( node , e){
			var id = node.id ;
			var url = root + '/system/Resource!queryResources.action'
			Ext.getCmp('resourceGridId').getStore().load({
				params: {pid: id },
				url: url
			});
		} , this);
		NavMenu.superclass.initEvents.call(this);
	}
});
//用于角色分配
var RoleTree = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	constructor: function(v ,rowIndex){
		var loader = new Ext.tree.TreeLoader({
			url : root+"/system/Resource!getResource2Role.action?res_id="+v
		});
		RoleTree.superclass.constructor.call(this, {
			region: 'west',
			width 	: 210,
			split	: true,
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			loader 		: loader,
	        root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				text: '系统角色'
			}
		});
		this.getRootNode().expand(true);
	},
	listeners: {  //!!!
		'checkchange' : function(node, checked) {   
        	node.expand();         
        	node.attributes.checked = checked;         
        	node.eachChild(function(child) {         
            	child.ui.toggleCheck(checked);         
            	child.attributes.checked = checked;         
            	child.fireEvent('checkchange', child, checked);         
       		});         
    	}  
	}
});
/**
 * 角色配置窗口
 * @class roleFromWindow
 * @extends Ext.Window
 */
var RoleFromWindow = Ext.extend(Ext.Window,{
	roleTree: null,
	constructor: function(v ,rowIndex){
		this.roleTree = new RoleTree(v ,rowIndex),
		RoleFromWindow.superclass.constructor.call(this , {
			title: '菜单资源分配给系统角色',
			layout: 'fit',
			width: 400,
			height: 400,
			closeAction:'close',  //close就是把此window destroy    ,hide是把此window隐藏
			items: this.roleTree,
			buttons: [{
				text: '保存',
				scope: this,
				handler: function(){
					this.save(v);
				}
			},{
				text: '关闭',
				scope: this,
				handler: function(){
					this.close();
				}
			}]
		});
	},
	save: function(v){
		var all = {res_id: v };
        var roleId = [];
        var nodes = this.roleTree.getChecked();
        for(var i in nodes){
        	if(nodes[i].leaf ){    //
              roleId.push(nodes[i].id);
            }
        }
	    if(roleId.length == 0){
	    	all["clear"] = true;
	    }else{
	    	all["roleIds"] = roleId;
	    }   
		Ext.Ajax.request({
          url: root + '/system/Resource!saveResource2Role.action',
          params: all,
          success: function( res,ops ){
            var rs = Ext.decode(res.responseText);
            if(true === rs.success){
            	Alert('操作成功!');
            }else{
            	Alert('操作失败!');
            }
          }
        });
	}
});

menuManage = Ext.extend(Ext.Panel,{
	constructor:function(){
		var resourceGrid = new ResourceGrid();
		var navigationMenu = new NavMenu();
		var resourceForm = new ResourceForm();
		
		menuManage.superclass.constructor.call(this,{
			id:'menuManage',
			title:'菜单管理',
			border : false ,
			closable: true,
			layout : 'border',
			baseCls: "x-plain",
			items  : [resourceGrid,navigationMenu,resourceForm ] 
		});
	}
});
