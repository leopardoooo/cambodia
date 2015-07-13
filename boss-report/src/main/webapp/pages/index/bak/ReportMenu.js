/***
 * 系统导航菜单
 */

MenuPanel = Ext.extend(Ext.Panel,{
	firstItem : null,
	buts : [],
	constructor : function(firstItem){
		this.firstItem = null;
		this.buts = [];
		this.firstItem = firstItem;
			for(i=0;i<this.firstItem.children.length;i++){
				var menuItems = [];
				for(j=0;j<this.firstItem.children[i].children.length;j++){
					menuItems.push({
						text : this.firstItem.children[i].children[j].text,//八类优惠客户统计
						id : this.firstItem.children[i].children[j].id,
						handler : function(obj){
							
							if(Ext.getCmp(obj.id+'quieetab')){
								App.getApp().page.activate(obj.id+'quieetab');
							}else{
								if(!Ext.getCmp(obj.id+'MainPanel')){
									App.getApp().page.add({
										title : obj.text,
										id : obj.id+'MainPanel',
										closable: true,
	//									layoutOnTabChange:true,
										items : [new MainPanel(obj.id,obj.text)]
									});
								}
								App.getApp().page.activate(obj.id+'MainPanel');
							}
						}
					})
				}
				this.buts.push({
					text : "<font style='font-family:微软雅黑;font-size:14'>"+this.firstItem.children[i].text+"</font>",
					anchor : '100%',
					menuAlign : 'tl-tr?',
		            menu: new Ext.menu.Menu({
		                items: menuItems
		            }),
		            listeners: {
	                	'mouseover': function(bt){
	                		bt.showMenu();
	                	}
	//                	'mouseout': function(bt){
	//                		bt.hideMenu();
	//                	}
	                }
				})
			}
		MenuPanel.superclass.constructor.call(this,{
			title : "<font style='font-family:微软雅黑;font-size:16'>"+this.firstItem.text+"</font>",
			//iconCls:'icon-query',
			layout : 'anchor',
        	defaultType: 'button',
        	items : this.buts
		})
	}
})

ReportMenu =Ext.extend( Ext.Panel ,{
	panels : [],
	constructor:function(){
		var menuDate = new Ext.data.JsonStore({
			fields : ['checked','children']
		})
		Ext.Ajax.request({
			url: root+"/system/Index!queryTreeNodes.action",
			async: false, 
			success : function(res){
				var rec = Ext.decode(res.responseText);
				menuDate.loadData(rec);
			}
		})
		thiz = this;
		for(var i=0;i<menuDate.getCount();i++){
			for(var j=0; j<menuDate.getAt(i).data.children.length;j++){
				thiz.panels.push(new MenuPanel(menuDate.getAt(i).data.children[j]));
			}
		}
		ReportMenu.superclass.constructor.call(this,{
			region: 'west',
			width 	: 200,
			split	: true,
			minSize	: 200,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        animCollapse:true,
	        animate		:false,
	        layout: 'accordion',
	        collapseMode:'mini', 
//			bodyStyle	:'padding:3px',
			tbar:[ '  ',
				{
					xtype : 'treecombo',
			    	id : 'resTreeCombo',
			    	emptyText:'点击搜索报表',
			    	minChars:0,
					height: 40,
					treeWidth:300,
					allowBlank: false,
			    	readOnly : true,
					treeUrl: root+"/system/Index!queryIDTreeNodes.action",
					listeners : {
						'focus' : function(){
							if(this.list){
								this.doQuery("");
							}
						},
						'select' : function(node,obj){
							if(Ext.getCmp(obj.id+'quieetab')){
								App.getApp().page.activate(obj.id+'quieetab');
							}else{
								if(!Ext.getCmp(obj.id+'MainPanel')){
									App.getApp().page.add({
										title : obj.text,
										id : obj.id+'MainPanel',
										closable: true,
	//									layoutOnTabChange:true,
										items : [new MainPanel(obj.id,obj.text)]
									});
								}
								App.getApp().page.activate(obj.id+'MainPanel');
							}
							Ext.getCmp('resTreeCombo').hasFocus = false;
						}
					}
				},'->','-',
				{xtype:'button',text:'增加报表',scope:this,handler:this.doAddReport}
			],
	        defaults: {
	            border: false
	        },
	        layoutConfig:{     
//              animate:true
//              hideCollapseTool:true  
            }, 
	        items : this.panels
		});
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
				return !n.leaf || re.test( n.text ) ;
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
					title : '配置报表',
					id : 'deployReportId',
					closable: true,
					items : [new DeployReportForm()]
				});
				App.page.activate('deployReportId');
			}
		});
		
		
	}
});
/**
//var data = [
//	{title : '客户用户',secondItem : [
//		{text : '客户统计分析',thirdItem : [
//			{text : '八类优惠客户统计',rep_id : '2050'},
//			{text : '黑名单客户数统计',rep_id : '2012'},
//			{text : '客户开户数统计',rep_id : '2013'},
//			{text : '客户综合查询【客户】',rep_id : ''}
//		]},
//		{text : '用户统计分析',thirdItem : [
//			{text : '用户统计',rep_id : ''},
//			{text : '用户更数统计',rep_id : ''},
//			{text : '开户数统计',rep_id : ''}
//		]}
//	]},
//	{title : '产品信息',secondItem : [
//		{text : '产品统计分析',thirdItem : [
//			{text : '产品清单',rep_id : ''}
//		]}
//	]},
//	{title : '财务报表',secondItem : [
//		{text : '用户统计分析',thirdItem : [
//			{text : '八类优惠客户统计',rep_id : ''},
//			{text : '黑名单客户数统计',rep_id : ''},
//			{text : '自动停机报表',rep_id : ''},
//			{text : '客户综合查询【客户】',rep_id : ''}
//		]}
//	]}
//];
//var data = [
//	{"text":"系统根目录","children":[
//		{"text":"客户用户","children":[
//			{"text":"客户统计分析","children":[
//												{"text":"八类优惠客户统计","id":"2050","children":[]},
//												{"text":"黑名单客户数统计","id":"2012","children":[]},
//												{"text":"自动停机报表","id":"2052","children":[]},
//												{"text":"客户综合查询【客户】","id":"2053","children":null}
//											]
//			},
//			{"text":"用户统计分析","id":"2011","children":[]},
//			{"text":"客户明细信息","id":"2012","children":[]},
//			{"text":"用户明细信息","id":"2013","children":[]}
//		]},
//		{"text":"客户用户","children":[
//			{"text":"客户统计分析","children":[
//												{"text":"八类优惠客户统计","id":"","children":[]},
//												{"text":"黑名单客户数统计","id":"","children":[]},
//												{"text":"自动停机报表","id":"","children":[]},
//												{"text":"客户综合查询【客户】","id":"","children":null}
//											]
//			},
//			{"text":"用户统计分析","id":"","children":[]},
//			{"text":"客户明细信息","id":"","children":[]},
//			{"text":"用户明细信息","id":"","children":[]}
//		]}
//	]}
//];
 */
