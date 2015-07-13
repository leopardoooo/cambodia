/***
 * 系统导航菜单
 */
NavigationMenu = Ext.extend( Ext.tree.TreePanel , {
	filterObj : null ,
	constructor: function(){
		var loader = new Ext.tree.TreeLoader({
			url : root+"/system/Index!queryTreeNodes.action",
			baseParams : {
				sub_system_id: '7'
			}
			
		});
		NavigationMenu.superclass.constructor.call(this, {
			region: 'west',
			width 	: 220,
			split	: true,
			minSize	: 220,
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
				text: '报表资源菜单'
			},
			tbar:[ ' ',
				new Ext.form.TextField({
					width: 90,
					emptyText:'回车过滤资源菜单...',
					selectOnFocus:true,
					listeners:{
						"specialkey" : {
							scope:this,
							fn : this.doFilterTree
						}
					}
				}), ' ', ' ',{
	                iconCls: 'icon-expand-all',
					tooltip: '展开所有资源',
					scope: this,
					handler : this.doExpandAll
	           }, '-', {
	               iconCls: 'icon-collapse-all',
	               tooltip: '合并所有资源',
	               scope: this,
				   handler : this.doCollapseAll
	           },'->','-',
				{xtype:'button',text:'增加报表',scope:this,handler:this.doAddReport}]
		});
		this.getRootNode().expand();
		this.filterObj = new Ext.tree.TreeFilter( this , {
			clearBlank: true,
			autoClear : true
		});
	},
	initEvents : function(){
		this.on("beforeclick" , function(node , e){
			return node.isLeaf();
		} ,this );
		this.on("click" , function( node , e){
			if(Ext.getCmp(node.id+'quieetab')){
				App.getApp().page.activate(node.id+'quieetab');
			}else{
				if(!Ext.getCmp(node.id+'MainPanel')){
					App.getApp().page.add({
						title : node.text,
						id : node.id+'MainPanel',
						closable: true,
//									layoutOnTabChange:true,
						items : [new MainPanel(node.id,node.text)]
					});
				}
				App.getApp().page.activate(node.id+'MainPanel');
			}
		} , this);
		NavigationMenu.superclass.initEvents.call(this);
	},
	//展开所有节点
	doExpandAll : function(){
		this.expandAll();
	},
	//合并节点
	doCollapseAll : function(){
		this.collapseAll();
	},
	//节点过滤
	doFilterTree : function( field , e ){
		if( e.getKey() == e.ENTER ){
			var text = e.target.value ,filter = this.filterObj ;
			if(!text){
				filter.clear();
				return;
			}
			this.doExpandAll();
			var re = new RegExp('^.*' + text + '.*$');
			filter.filterBy( function( n ){
				return !n.leaf || re.test( n.text ) || re.test(n.id);
			});
		}
	},
	doAddReport:function(){
		//判断该操作员是否有权限新增报表
		Ext.Ajax.request({
			url : root + '/query/RepDesign!queryRepDefine.action',
			params : {rep_id:''},
			scope : this,
			success : function(res, opt) {
				if(Ext.getCmp('deployReportId')){
  						App.getApp().page.remove(Ext.getCmp('deployReportId'));
				 }
				App.page.add({
					title : '增加新报表',
					id : 'deployReportId',
					closable: true,
					items : [new DeployReportForm()]
				});
				App.page.activate('deployReportId');
			}
		});
		
		
	}
});
