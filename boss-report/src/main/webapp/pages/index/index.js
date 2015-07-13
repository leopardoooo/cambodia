/**
 * 首页左边导航菜单构建
 */
LeftPanel = Ext.extend(Ext.Panel, {
	constructor: function(){
		this.initializeTree();
		LeftPanel.superclass.constructor.call(this, {
			id: 'left-panel',
			region: "west",
			width:'20%',
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
				items: ['-',{
					id: 'report_search_btn_id',
					tooltip: '搜索菜单',
					hidden: true,
					enableToggle: true,
					iconCls: 'icon-search',
					scope: this,
					handler: this.doToggleSearchText
				},'-',{
					id: 'report_expan_btn_id',
					tooltip: '展开菜单',
					hidden: true,
					iconCls: 'icon-expand',
					scope: this,
					handler : this.doExpandAll
				},' ',{
					id: 'report_less_btn_id',
					tooltip: '收缩菜单',
					hidden: true,
					iconCls: 'icon-collapse',
					scope: this,
					handler : this.doCollapseAll
				},'-',{
					id: 'report_fav_btn_id',
					tooltip: '打开收藏夹',
					hidden: true,
					iconCls: 'icon-fav'
				},'-','->','-',{
					id: 'report_task_btn_id',
					tooltip: '查询任务',
					hidden: true,
					iconCls:'icon-search',
					xtype:'button',
					handler:this.doQueryTask
				},'-', {
					id: 'report_new_btn_id',
					tooltip: '新增报表',
					hidden: true,
					iconCls: 'icon-add-report',
					scope:this,
					handler:this.doAddReport
				},'-']
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
	doQueryTask: function(){
		var win = Ext.getCmp('taskQueryWinId');
		if(!win){
			win = new TaskQueryWin();
		}
		win.show();
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
				baseParams : { sub_system_id: '7' },
	            listeners:{
	            	scope:this,
	            	load: function(treeLoader, node, reponseText){
	            		//权限控制按钮
	            		node.eachChild(function (child) {
	            			var btnId = child.attributes.others['handler'];
			                if(!Ext.isEmpty(btnId)){
			                	child.ui.hide();
			                	Ext.getCmp(btnId).show();
			                }
			            });
	            	}
	            }
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
			tbar: new Ext.Toolbar({
				height: 29,
				cls: 'top-toolbar',
				items: ['-', App.LoginInfo, '-' ,'->','-',{
					tooltip: '全屏显示',
					iconCls: 'icon-full',
					enableToggle: true,
					handler: this.doFullView
				},'-']
			}),
			items: [App.left, App.page]
		});
	},
	doFullView: function(){
		if(this.pressed){
			App.top.hide();
			App.left.hide();
		}else{
			App.top.show();
			App.left.show();
		}
		App.viewport.doLayout();
	}
});

//顶部面板事件处理函数
TopHelper = function(){
	var tpl = new Ext.XTemplate(
			'<ul>',
				'<tpl for=".">',
		        	'<li class="btn fl">',
		        		'<a href="#" onclick="TopHelper.f.doToggle(\'{itemId}\', \'{text}\');">',
		        			'<img src='+ Ext.BLANK_IMAGE_URL +' class="{iconCls}" border="0"/>',
		        			'<p>{text}</p>',
		        		'</a>',
		        	'</li>',
		        '</tpl>',
	       '</ul>');
	var sss = null, ssp = null;
	function _ff(dpy){
		sss.dom.style.display = dpy;
		ssp.dom.style.display = dpy;
	}
	var F = {
		defaultSystem: 7,
		url: root + '/system/Index!queryAllSubSystem.action',
		initialize: function(){
			Ext.get("MenuSystem").on("click", _ff.createDelegate(this, ["block"]));
			Ext.get("ss-close").on("click", _ff.createDelegate(this, ["none"]));
			App.data.sysId = F.defaultSystem;
			//load data
			F.loadAndOverride(Ext.get("ss-bts"));
		},
		loadAndOverride: function(el){
			Ext.Ajax.request({
				url: F.url,
				scope: this,
				success: function(res,opt){
					var data = Ext.decode(res.responseText);
					var html = F.apply(data);
					el.dom.innerHTML = html;
				}
			});
		},
		apply: function( data ){
			var items = [];
			for(var i = 0; i< data.length; i++){
				var o = {
					text: data[i]["sub_system_name"],
					itemId: data[i]["sub_system_id"],
					url: data[i]["sub_system_url"],
					root: data[i]["sub_system_host"],
					iconCls: data[i]["iconcls"]
				};
				if(o.itemId == App.data.sysId){
					o["text"] = "<b><font color=gray>" + o["text"] + "</font></b>";
					o["disabled"] = true;
				}
				items.push( o );
			}
			// apply template
			return tpl.apply(items);
		},
		doToggle: function(itemId, text){
			if(itemId == App.data.sysId){
				return ;
			}
			Confirm("确定要切换系统至 "+ text +"", null,function(){
				App.href(regourl + '/rego?tokenId=' + token_id + '&sub_system_id=' + itemId);
			});
		}
	};
	
	var TH = {
		f: F,
		initEvents: function(){
			sss = Ext.get("ss-shadow");
			ssp = Ext.get("ss-panel"); 
			
			//切换系统
			TH.f.initialize();
			//其它按钮
			Ext.get("MenuUpdate").on("click", TH.userUpdate);
			Ext.get("MenuLogout").on("click", TH.logout);
		},
		logout: function(){
			Confirm("确定要退出系统",null,function(){
				App.href(Constant.ROOT_PATH + "/gologin");
			});
		},
		userUpdate:function(){
			var win = Ext.getCmp('optrDataWinId');
			if(!win)
				win = new OptrDataWin();
			win.show();
		}
	};
	return TH;
}();

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

TaskQueryWin = Ext.extend(Ext.Window, {
	taskStore: null,
	pageSize: 15,
	editRep: false,
	constructor: function(){
		this.taskStore = new Ext.data.JsonStore({
			url: root+"/query/Report!queryTasks.action",
			root:'page.records',
			totalProperty:'page.totalProperty',
			fields: ['task_id','task_name','rep_id','rep_name','task_type','task_type_text','task_execday','is_dowload','is_delete','keylist',
				'optr_id','optr_name','status','status_text','exec_result','exec_start_time','exec_query_id','exec_end_time','is_waitexec','create_time','remark']
		});
		
		this.grid = new Ext.grid.GridPanel({
			border:false,
			store:this.taskStore,
			columns:[
				{header:'任务ID',dataIndex:'task_id',width:50},
				{header:'任务名称',dataIndex:'task_name',width:100,renderer:App.qtipValue},
				{header:'报表名称',dataIndex:'rep_name',width:100,renderer:App.qtipValue},
				{header:'任务类型',dataIndex:'task_type_text',width:85},
				{header:'执行日',dataIndex:'task_execday',width:90,renderer:App.qtipValue},
				{header:'状态',dataIndex:'status_text',width:75,renderer:function(v,meta,record){
					if(record.get('is_waitexec') == 'T'){
						v = '待执行';
					}
					return v;
				}},
				{header:'操作员',dataIndex:'optr_name',width:90,renderer:App.qtipValue},
				{header:'操作',dataIndex:'task_id',width:75,scope:this,renderer:function(v,meta,record){
						var res = "";
						if(record.get('is_dowload') == 'T'){
							res = "<a href='#' onclick=Ext.getCmp('taskQueryWinId').doDown("+record.get('rep_id')+","+record.get('exec_query_id')+")>下载</a>&nbsp;&nbsp;";
						}
						if(record.get('is_delete') == 'T'){
							res += "<a href='#' onclick=Ext.getCmp('taskQueryWinId').doDel("+v+")>删除</a>";
						}
						return res;
					}
				},
				{header:'实际开始时间',dataIndex:'exec_start_time',width:130},
				{header:'实际结束时间',dataIndex:'exec_end_time',width:130},
				{header:'备注',dataIndex:'remark',width:100,renderer:App.qtipValue},
				{header:'查询条件',dataIndex:'keylist',width:75,renderer:App.qtipValue},
				{header:'执行标记',dataIndex:'exec_result',width:75,renderer:App.qtipValue},
				{header:'执行结果ID',dataIndex:'exec_query_id',width:75},
				{header:'创建日期',dataIndex:'create_time',width:130}
			],
			tbar: [
				'-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({
	                store: this.taskStore,
	                width: 210,
	                hasSearch : true,
	                emptyText: '支持任务名册和报表名称模糊查询'
	            }),'-'
			],
			bbar: new Ext.PagingToolbar({store: this.taskStore, pageSize: this.pageSize})
		});
		TaskQueryWin.superclass.constructor.call(this, {
			id:'taskQueryWinId',
			title:'任务配置',
			closeAction:'close',
			width:700,
			height:400,
			layout:'fit',
			items:this.grid
		});
		
		this.taskStore.load({
			params: {
				start: 0,
				limit: this.pageSize
			}
		});
		
	},
	doDown: function(repId, queryId){
		var mask = Show();//进度条
  		Ext.Ajax.request({
  			//scope : this,
			timeout:9999999999,
	    	url:root+"/query/Show!createExp.action",
	    	params:{query_id:queryId},
	    	success:function(res){
	    		mask.hide();
				mask=null;
				AlertReport("&nbsp&nbsp&nbsp&nbsp&nbsp<a href="+root+"/query/Show!downloadExp.action?query_id="
								+queryId+"&rep_id="+repId+" >点击下载</a>");
	    	}
	    });
	},
	doDel: function(taskId){
		Confirm('确定删除?', this, function(){
			Ext.Ajax.request({
				url: root+"/query/Report!deleteRepTask.action",
				params:{task_id: taskId},
				scope:this,
				success: function(res){
					var data = Ext.decode(res.responseText);
					if(data === true){
						Alert('删除成功');
						this.taskStore.load({
							start: 0,
							limit: this.pageSize
						});
					}
				}
			});			
		});
	}
});
	