/**
 * 首页左边导航菜单构建
 */
LeftPanel = Ext.extend(Ext.Panel, {
	constructor: function(){
		this.initializeTree();
		LeftPanel.superclass.constructor.call(this, {
			id: 'left-panel',
			region: "west",
			width: 280,
			split: true,
			collapseMode: 'mini',
			layout: 'border',
			items: [this.tree, {
				region: 'north',
				height: 24,
				id: this.northId,
				titleCollapse: true,
				border: false,
				bodyStyle: 'padding: 1px 1px 2px 1px;background: #f0f0f0;',
				layout: 'fit',
				items: [{
					xtype: 'textfield',
					emptyText: '搜索菜单',
					enableKeyEvents: true,
					listeners:{
						keydown: {
	                        fn: this.doFilterTree,
	                        buffer: 350,
	                        scope: this
	                    }
					}
				}]
			}],
			tbar: new Ext.Toolbar({
				cls: '_toolbar',
				items: [' ',' ',{
					tooltip: '搜索菜单',
					enableToggle: true,
					iconCls: 'icon-search',
					scope: this,
					handler: this.doToggleSearchText
				},'-',{
					tooltip: '展开菜单',
					iconCls: 'icon-expand',
					scope: this,
					handler : this.doExpandAll
				},' ',{
					tooltip: '收缩菜单',
					iconCls: 'icon-collapse',
					scope: this,
					handler : this.doCollapseAll
				},'-',{
					tooltip: '打开收藏夹',
					iconCls: 'icon-fav'
				},'->', {
					tooltip: '新增报表',
					iconCls: 'icon-add-report',
					scope:this,
					handler:this.doAddReport
				},' ',' ',' ']
			})
		});
	},
	initEvents : function(){
		this.tree.on("click" , function( node , e){
			if(!node.isLeaf()) return;
			this.addTabPanel(node.id, node.text);
		} , this);
		LeftPanel.superclass.initEvents.call(this);
	},
	addTabPanel: function(id , text){
		if(Ext.getCmp( id + 'quieetab')){
			App.page.activate( id + 'quieetab');
		}else{
			if(!Ext.getCmp( id + 'MainPanel')){
				App.page.add({
					id : id + 'MainPanel',
					title : text,
					layout: 'fit',
					closable: true,
					items: new MainPanel( id , text)
				});
			}
			App.page.activate(id + 'MainPanel');
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
	},
	initializeTree: function(){
		this.tree = new Ext.tree.TreePanel({
			region: 'center',
			border: false,
			bodyStyle:'padding:3px',
			useArrows: true,
			autoScroll: true,
			animate: false,
			enableDD: false,
			containerScroll: true,
			border: false,
			loader: new Ext.tree.TreeLoader({
				autoLoad: true,
				url : root+"/system/Index!queryTreeNodes.action",
				baseParams : { sub_system_id: '7' }
			}),
			root: {
				id: '-1',
				iconCls: 'x-tree-root-icon',
				nodeType: 'async',
				singleClickExpand: true,
				text: '报表资源菜单',
				expanded: true
			}
		});
		this.filter = new Ext.tree.TreeFilter( this.tree , {
			clearBlank: true,
			autoClear : true
		});
	},
	doToggleSearchText: function(){
		var northPanel = this.items.get(1);
		if(northPanel.isVisible()){
			northPanel.hide();
		}else{
			northPanel.show();
		}
		this.doLayout(false, false);
	},
	doExpandAll: function(){
		this.tree.expandAll();
	},
	doCollapseAll: function(){
		this.tree.collapseAll();
	},
	doFilterTree : function( t , e ){
		var text = t.getValue(), filter = this.filter ;
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
});

// 首页工作区
RightTabPanel = Ext.extend( Ext.TabPanel, {
	welcomePanel: null,
	constructor: function(){
		this.welcomePanel = new WelcomePortal();
		RightTabPanel.superclass.constructor.call(this, {
			region:'center',
			resizeTabs: true,
			minTabWidth: 135,
			tabWidth: 135,
			maxTabWidth: 160,
			enableTabScroll: true,
			//tabPosition: 'bottom',
			activeTab: 0,
			defaults: {autoScroll: true},
			items: this.welcomePanel
		});
	}
});


//Index
ViewportCenterPanel = Ext.extend( Ext.Panel, {
	constructor: function(){
		MainPanel.superclass.constructor.call(this, {
			region : 'center',
			layout : 'border',
			style: 'padding: 0px 1px 2px 1px;',
			border: false,
			id: 'main-panel',
			items: [App.left, App.page]
		});
	}
});



//报表下载窗口
AlertReport= function( msg , fn , scope ){
	var m = Ext.Msg ;
	return m.show({
		title: m.title,
		msg: msg ,
		icon: m.INFO ,
		buttons: m.CANCEL,
		closable : false,
		fn : fn ,
		scope: scope 
	});
}; 

//滚动条处理
Show = function( anim ){
		App.currentBar = Ext.MessageBox.show({
	       msg: '正在提交数据...&nbsp;&nbsp;<a href=#>[...]</a>',
	       wait: true,
	       waitConfig: { interval: 150 },
	       icon:'icon-download',
	       animEl: anim
	   });
	   return App.currentBar;
}

// store 字段中文排序 补丁 以及使报表的合计在排序的保持位置不变化
 Ext.data.Store.prototype.applySort = function() {
	if (this.sortInfo && !this.remoteSort) {
		var s = this.sortInfo, f = s.field;
		var st = this.fields.get(f).sortType;
		var fn = function(r1, r2) {
			if(!Ext.isEmpty(r1.data['issumrow_report'])&&r1.data['issumrow_report']=='T'){
	  			return 0;
	  		}else if(!Ext.isEmpty(r2.data['issumrow_report'])&&r2.data['issumrow_report']=='T'){
	  			return 0;
	  		}else{
  			  	var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
				// 添加:修复汉字排序异常的Bug
				if (typeof(v1) == "string") { // 若为字符串，
					return v1.localeCompare(v2);// 则用 localeCompare 比较汉字字符串, Firefox
												// 与 IE 均支持
				}
				// 添加结束
				return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
	  		}
		};
		this.data.sort(s.direction, fn);
		if (this.snapshot && this.snapshot != this.data) {
			this.snapshot.sort(s.direction, fn);
		}
	}
}; 

//去掉文本框验证
Ext.apply(Ext.form.TextField.prototype,{
		vtype:''
});

/*gridpanel单元格复制*/
if  (!Ext.grid.GridView.prototype.templates) {    
	    Ext.grid.GridView.prototype.templates = {};
}
Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(    
	     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
	     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,    
	     '</td>'
);
	