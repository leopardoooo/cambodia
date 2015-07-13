App.pageSize = 100;

/**
 * 明细查询后台回传结果集
 * @class RepResultGrid
 * @extends Ext.grid.GridPanel
 */
ResultXTable = Ext.extend(Ext.Panel, {
	query_id: null,
	rep_id: null,
	rep_info: null,
	
	containerId: null,
	xtableHeader: null,
	xtableData: null,
	
    constructor: function(query_id, rep_id, rep_info) {
    	this.query_id = query_id;
    	this.rep_id = rep_id;
    	this.rep_info = rep_info;
    	
    	this.quickTmpChangeCombo = new Ext.form.ComboBox({
    		tooltip: '模版切换',scope: this,triggerAction:'all',iconCls: 'icon-data',
			store:new Ext.data.JsonStore({
				fields:['cube_json','cube_config_text','default_show','name','optr_id','remark','rep_id','status','template_id']
			}),
			valueField:'template_id',displayField:'name',
			listeners:{
				scope:this,
				select:function(combo,record,number){
					this.showTemplate({query_id:this.query_id, template_id:record.get('template_id')});
				}
			}
    	});
    	//初始化快速切换模版的combo
    	this.initQuickChangeTmpCombo();
    
    	var tbar;
		//boss_login配置了olap报表功能菜单清单
		if(Constant.REPORT_FUNC&&Constant.REPORT_FUNC.length>0){
			tbar=[' ',' ',' ',{
			tooltip: '返回查询界面',
			scope: this,
			iconCls: 'icon-back',
			handler: this.goQueryPanel
			},'-'];
			//管理模板功能
			if(Constant.REPORT_FUNC.contain('template')){
				tbar.push(' ');
				tbar.push({
					tooltip: '管理模板',
					scope: this,
					iconCls: 'icon-switch',
					handler: this.manageTemplate
				});
			}
			//维合计功能  
			if(Constant.REPORT_FUNC.contain('groupsum')){
				tbar.push(' ');
				tbar.push({
					tooltip: '维合计',
					iconCls: 'icon-sum',
					scope: this,
					handler: this.dimCalc
				});
			}
			//维度切换功能
			if(Constant.REPORT_FUNC.contain('dimchange')){
				tbar.push(' ');
				tbar.push({
					tooltip: '维度切换',
					iconCls: 'icon-toggleTpl',
					scope: this,
					handler: this.doToggleDim
				});
			}
			//图形查看功能
			if(Constant.REPORT_FUNC.contain('graph')){
				tbar.push('-');
				tbar.push({
					tooltip: '图形选择',
					iconCls: 'icon-s-chart',
					scope: this,
					handler: this.selectGraphic
				});
				tbar.push(' ');
				tbar.push({
					tooltip: '表格显示',
					scope: this,
					iconCls: 'icon-table',
					handler: this.doTableView
				});
				tbar.push(' ');
				tbar.push({
					tooltip: '图表显示',
					scope: this,
					iconCls: 'icon-chart',
					handler: function(){this.doAddChartView();}
				});
			}
			//快速切换模板功能
			if(Constant.REPORT_FUNC.contain('template')){
				tbar.push('-');
				tbar.push(this.quickTmpChangeCombo);
			}
			tbar.push('->');
			tbar.push({
				tooltip: '下载',
				iconCls: 'icon-excel',
				scope: this,
				handler: this.doDownload
			});
			tbar.push('-');
			tbar.push({
				tooltip: '查看SQL',
				iconCls: 'icon-view-sql',
				scope: this,
				handler: this.doViewSql
			});
			tbar.push(' ');
			tbar.push(' ');
		}else{
			//未配置则启用所有功能
		tbar = [' ',' ',' ',{
					tooltip: '返回查询界面',
					scope: this,
					iconCls: 'icon-back',
					handler: this.goQueryPanel
				},'-',{
					tooltip: '管理模板',
					scope: this,
					iconCls: 'icon-switch',
					handler: this.manageTemplate
				},'-',{
					tooltip: '维合计',
					iconCls: 'icon-sum',
					scope: this,
					handler: this.dimCalc
				},' ',{
					tooltip: '维度切换',
					iconCls: 'icon-toggleTpl',
					scope: this,
					handler: this.doToggleDim
				},'-',{
					tooltip: '图形选择',
					iconCls: 'icon-s-chart',
					scope: this,
					handler: this.selectGraphic
				},' ',{
					tooltip: '表格显示',
					scope: this,
					iconCls: 'icon-table',
					handler: this.doTableView
				},' ',{
					tooltip: '图表显示',
					scope: this,
					iconCls: 'icon-chart',
					handler: function(){this.doAddChartView();}
				}
				// 新增加 简易模版切换,一个combo
				,'-',this.quickTmpChangeCombo,'->',{
					tooltip: '下载',
					iconCls: 'icon-excel',
					scope: this,
					handler: this.doDownload
				},'-',{
					tooltip: '查看SQL',
					iconCls: 'icon-view-sql',
					scope: this,
					handler: this.doViewSql
				},' ',' '];
		}
		
		var id = Ext.id();
        this.containerId = "___olapTable" + id;
        this.chartId = "__chart" + id; 
        
        // 实例化XTable
		this.xtablePanel = new XTable({
			renderTo: this.containerId,
			dataUrl: root +'/query/Show!queryTable.action',
			baseParams: {query_id: this.query_id},
			loadCallback: this.doLoad.createDelegate(this),
			listeners: {
				scope: this,
				cellclick: this.doViewDetail
			}
		});
        ResultXTable.superclass.constructor.call(this, { 
        	tbar: tbar,
        	border: false,
        	layout: 'card',
        	activeItem: 0,
        	items: [{
        		border: false,
        		layout: 'border',
        		autoScroll: true,
        		items: [{
        			region: 'center',
        			border: false,
        			html: '<div id="'+ this.containerId +'" style="width: 100%; height: 100%;"></div>'
        		},{
        			region: 'west',
    				width: 380,
    				border: false,
    				split: true,
    				layout: 'fit',
    				autoScroll: true,
    				hidden: true
        		}]
        	},{
        		border: false,
        		html: '<table style="width: 100%; height: 100%;overflow: auto;"><tr><td align="center"><div id="'+ this.chartId +'" style="width: 800px; height: 500px;"></div></td></tr></table>'
        	}]
        });
    },
    //private
    addToggleDimPanel: function(wp, main){
    	function __success(res, ops){
			var o = Ext.decode(res.responseText);
			var dims = o["dims"];
			var meas = o["meas"];
			
			
			//指标
			var avaMeas = [], edMeas = [];
			for(var i = 0; i< meas.length; i++){
				if(meas[i]["check"] === false){
					avaMeas.push(meas[i]);
				}
				if(meas[i]["check"] === true){
					edMeas.push(meas[i]);
				}
			}
			//维度
			var avaDims = [], headers = [],labels = [];  
			for(var i = 0; i< dims.length; i++){
				if(dims[i]["usesign"] === false){
					avaDims.push(dims[i]);
				}
				if(dims[i]["usesign"] === true && dims[i]["verticalsign"] === true){
					headers.push(dims[i]);
				}
				if(dims[i]["usesign"] === true && dims[i]["verticalsign"] === false){
					labels.push(dims[i]);
				}
			}
			this.avaMeasView = new DimListView("measGroup", "measGroup").loadData(avaMeas);
			this.edMeasView = new DimListView("measGroup", "measGroup").loadData(edMeas);
			this.avaDimsView = new DimListView("dimsGroup", "dimsGroup").loadData(avaDims);
			this.headersView = new DimListView("dimsGroup", "dimsGroup", {
				doDragCallback: function(ddSource){
					if(this.store.getCount() > 0 || ddSource.dragData.selections.length > 1){
						return false;
					}else{
						return DimListView.prototype.doDragCallback.call(this, ddSource);
					}
				}
			}).loadData(headers);
			this.labelsView = new DimListView("dimsGroup", "dimsGroup").loadData(labels);
			
    		this.toggleDimPanel = {
	    		border: false,
	    		layout:'column',
	    		id: 'table-cls',
	            // applied to child components
	            autoScroll: true,
	            bodyStyle:'background: #aaa;',
	            defaults: {
	            	border: false,
	            	columnWidth: .5,
	            	baseCls: 'x-plain',
	            	defaults: {
	            		height: 125, 
	            		layout: 'fit', 
	            		collapsible:true
	            	}
	            },
	            items: [{
	            	bodyStyle:'padding:5px 5px 0px 5px;',
	            	items: [{
	            		title: '可选指标',
	               	 	items: this.avaMeasView
	            	},{
	            		title: '可选维',
		            	height: 255,
		                items: this.avaDimsView
	            	}]
	            },{
	            	bodyStyle:'padding:5px 5px 0px 0px;',
	            	items: [{
	            		title: '指标',
	               		items: this.edMeasView
	            	},{
	            		title: '列头',
	                	items: this.headersView
	            	},{
	            		title: '行标签',
	                	items: this.labelsView
	            	},{
	            		xtype: 'panel',
	            		height: 40,
	            		baseCls: 'x-plain',
	            		layout: 'hbox',
	            		layoutConfig: {
                            pack:'end',
                            align:'middle'
                        },
                        collapsible: false,
                        border: false,
                        items:[{
                            xtype:'button',
                            height: 30,
	            			width: 80,
	            			text: '关闭',
	            			scope: this,
	            			handler: this.doToggleDim
                        },{
                        	width: 10,
                        	baseCls: 'x-plain'
                        },{
                            xtype:'button',
                            height: 30,
	            			width: 80,
	            			text: '刷新',
	            			scope: this,
	            			handler: this.doRefreshDimData
                        }]
	            	}]
	            }]
	    	};
	    	
	    	wp.add(this.toggleDimPanel);
	    	main.doLayout();
    	}
    	
    	Ext.Ajax.request({
    		url: root +'/query/Key!queryMeasAndDims.action',
			params: {query_id: this.query_id},
			scope: this,
			success: __success
    	});
    },
    doRefreshDimData: function(){
    	var headerId = null, labelIds = [], meaIds = [];
    	this.labelsView.store.each(function(r){
    		labelIds.push(r.get("id"));
    	});
    	if(this.headersView.store.getCount() > 0){
    		headerId = this.headersView.store.getAt(0).get("id");
    		labelIds.push(headerId);
    	}
    	this.edMeasView.store.each(function(r){
    		meaIds.push(r.get("id"));
    	});
    	var o = {
    		mycube: {
    			dimlist: labelIds,
    			vertdim: headerId,
    			mealist: meaIds
    		}
    	};
    	var mask = Show();//进度条
    	Ext.Ajax.request({
    		url: root +'/query/Show!cubeRefresh.action',
			params: {
				query_id: this.query_id, 
				jsonParams: Ext.encode(o)
			},
			scope: this,
			timeout: 9999999,
			success: function(res, ops){
				mask.hide();
				//this.doToggleDim();
				this.xtablePanel.loadAndView(0);
			}
    	});
    },
    doToggleDim: function(){
    	var main = this.items.itemAt(0); 
    	var wp = main.items.itemAt(1);
    	if(wp.isVisible()){
    		wp.hide();
    		Ext.fly(this.containerId).unmask();
    	}else{
    		wp.show();
    		Ext.fly(this.containerId).mask();
    	}
    	if(!this.toggleDimPanel){
    		this.addToggleDimPanel(wp, main);
    	}else{
    		main.doLayout();
    	}
    	
    },
    doTableView: function(){
    	this.getLayout().setActiveItem(0);
    },
    //添加图表展示
    doAddChartView: function(chartData){
    	if(!chartData && !this.chart){
    		this.selectGraphic();
    		return ;
    	}
    	this.getLayout().setActiveItem(1);
    	if(chartData){
	    	this.chart = ChartFactory.createChart(chartData);
	    	//render the chart
	    	this.chart.render(this.chartId);
    	}
    },
    doLoad: function(headerCells, data){
		this.xtableHeader = headerCells;
		this.data = data ;
		
		var tools = Ext.query("DIV.x-table-header td>DIV.tools", this.xtablePanel.renderTo.dom);
		for(var i = 0; i< tools.length; i++){
			var el = new Ext.Element(tools[i]);
			if(el.hasClass("filter")){
				el.on("click", this.doFilter, this);
			}else if(el.hasClass("in")){
				el.on("click", this.doExpand, this);
			}else if(el.hasClass("out")){
				el.on("click", this.doShrink, this);
			}else if(el.hasClass("sort")){
				el.on("click", this.doSort, this);
			}
		}
    },
    //过滤
    doFilter: function(e, t, o){
    	new DimFilterWindow(this.query_id, t.getAttribute("dim"), this).show();
    },
    //子定义排序
    doSort: function(e, t, o){
    	new DimSortWindow(this.query_id, t.getAttribute("dim"), this).show();
    },
    //展开
    doExpand: function(e, t, o){
    	var mask = Show();//进度条
    	Ext.Ajax.request({
    		url: root +'/query/Show!cubeExpand.action',
    		params: {
    			query_id: this.query_id,
    			dim: t.getAttribute("dim")
    		},
    		scope: this,
    		success: function(){
    			mask.hide();
    			this.xtablePanel.loadAndView(0);
    		}
    	});
    },
    //收缩
    doShrink: function(e, t, o){
    	var mask = Show();//进度条
    	Ext.Ajax.request({
    		timeout: 9999999,
    		url: root +'/query/Show!cubeShrink.action',
    		params: {
    			query_id: this.query_id,
    			dim: t.getAttribute("dim")
    		},
    		scope: this,
    		success: function(){
    			mask.hide();
    			this.xtablePanel.loadAndView(0);
    		}
    	});
    },
    doViewHtml:function(){
    	this.xtablePanel.loadAndView(0);
    },
    doViewDetail: function(e, target, x, y, cellData, rowData, table){
		if(target.dom.tagName !== "A"){
			return false; 
		}
		var lastRowHeader = this.xtableHeader[this.xtableHeader.length - 1];
		var rep_id = lastRowHeader[x].mea_detail_id,
			histroy_query_id = this.query_id,
			headCells = [];
		for(var i = 0; i <= x ;i ++){
			var o = lastRowHeader[i];
			if(o.dim_type == 'crosswise'){
				o["id"] = rowData[i].id;
				headCells.push(o);
			}else if(o.dim_type == 'vertical' && i == x){
				headCells.push(o);
			}
		}
		//get query_id
		Ext.Ajax.request({
			url: root + "/query/Report!initQuery.action",
			params: {
				rep_id: rep_id,
				jsonParams: Ext.encode({
					history_query_id: histroy_query_id,
					headdatacells: headCells
				})
			},
			success: function(res, ops){
				var o = Ext.decode(res.responseText),
					detail_query_id = o.query_id;
				Ext.Ajax.request({
		     			scope : this,
		     			url : root +'/query/Show!queryHeader.action',
		     			params: {query_id : detail_query_id},
		     			success : function(res,opt){
		     				var headers = Ext.decode(res.responseText).records;
		     				var repgrid = new RepResultGrid(headers, detail_query_id ,o.rep_id , null);
							var panelId = detail_query_id + 'MainPanel';
							App.getApp().page.add({
								title : "明细[" + target.dom.innerText + "]",
								id : panelId,
								layout: 'fit',
								closable: true,
								items : [repgrid]
							});
							App.getApp().page.activate(panelId);
		     			}
				});
			}
		});
		
	},
    goQueryPanel: function () {
        this.ownerCt.remove(this);
    },
    openDownloadWin: function(){
	     //打开一个对话框配置 参数  rep_id
	     var win = Ext.getCmp('colExportWinId');
	     if(!win) win = new ColExportWin();
	     win.show(this.query_id);
    },
    doDownload: function(){
	     var mask = Show(); //进度条
	     Ext.Ajax.request({
	         scope : this,
	         timeout: 9999999999,
	         url: root + "/query/Show!createExp.action",
	         params: {
	             query_id: this.query_id
	         },
	         success: function (res) {
	             mask.hide();
	             mask = null;
	             AlertReport("&nbsp&nbsp&nbsp&nbsp&nbsp<a href=" + root + "/query/Show!downloadExp.action?query_id=" + this.query_id + "&rep_id=" + this.rep_id + " >点击下载</a>");
	         }
	     });
    },
    doViewSql: function(){
	     Ext.Ajax.request({
	         url: root + '/query/RepDesign!showSql.action',
	         params: {
	             query_id: this.query_id
	         },
	         scope: this,
	         success: function (res, opt) {
	             var repdto = Ext.decode(res.responseText);
	             var win = Ext.getCmp('oneRemarkWinId');
	             if(!win) win = new OneRemarkWin();
	             win.show(repdto);
	         }
	     });
    },
    openOsdTpl: function(){
        var win = Ext.getCmp('odssqltitlewin');
        if(!win) win = new OsdSqlTitleWin();
        win.show(this.query_id);
    },
    olapNav:function(){
    	Ext.Ajax.request({
			url : root + '/query/Key!queryDims.action',scope:this,
			params : {query_id : this.query_id},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				var mycube = {dimensions:list};
				var win = Ext.getCmp('olapNavWinId');
				if( !win){
					win=new NavWindow(this,mycube,this.query_id,this.rep_id,this.rep_info);
				}else{
					win.reloadDatas(this,mycube,this.query_id,this.rep_id,this.rep_info);
				}
				win.show();
				
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
    },
    dimCalc:function(){
    	//keyaction.queryDimTotals
    	 Ext.Ajax.request({
	         url: root + '/query/Key!queryDimTotals.action',scope:this,
	         params: {
	             query_id: this.query_id
	         },
	         success: function (res, opt) {
	             var repdto = Ext.decode(res.responseText);
	             var win = Ext.getCmp('DimCalcWinId');
	             if(!win) {
	             	win = new DimCalcWin(this,repdto.records,this.query_id);
	             }else{
		             win.loadData(this,repdto.records,this.query_id);
	             }
	             win.show();
	         }
	     });
    },
	saveTemplate:function(){//存为模板
    	Ext.Ajax.request({
			url : root + '/query/RepDesign!queryMyCubeConfig.action',scope:this,
			params : {query_id : this.query_id},
			success : function(res, opt) {
				var str = res.responseText;
				var win = Ext.getCmp('SaveTemplateID');
				if( !win){
					win=new SaveTemplateWindow(this,str);
				}else{
					win.reloadDatas(this,str);
				}
				win.show();
				
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
    },
    initQuickChangeTmpCombo:function(){//初始化快速切换模版的combo
    	Ext.Ajax.request({
			url : root + '/query/RepDesign!queryMyCubes.action',scope:this,
			params : {query_id : this.query_id,rep_id:this.rep_id},
			success : function(res, opt) {
				var data = Ext.decode(res.responseText);
				this.quickTmpChangeCombo.store.loadData(data);
				for(var index =0;index<data.length;index++){
					var single = data[index];
					if(single.default_show == 'true' ){
						//var tempid = Ext.isEmpty(template_id)?single.template_id : single.template_id;
						this.quickTmpChangeCombo.setValue(single.template_id);
					}
				}
			},
			failure:function(){
				Alert('加载模版数据出错');
			}
		});
    },
    showTemplate:function(param,cmp){
		//showAction.cubeChangeMyCube()
    	if(!param){
    		Alert('未能正确获取参数!');
    		return false;
    	}
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Show!cubeChangeMyCube.action',
			scope:this,timeout:9999999999,
			params : param,
			success : function(res, opt) {
				this.xtablePanel.loadAndView(0);
				wait.hide();
				wait = null;
				if(cmp){
					cmp.hide();
				}
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('设为首选操作出错');
			}
		});
	},
    manageTemplate:function(){
    	if(!this.tmpManageWin){
    		this.tmpManageWin = new Ext.Window({
    			width:160,height:130,closeAction:'hide',resizable:false,layout:'border',
    			defaults:{border:false},
    			items:[
    			{region:'west',width:'10%'},
    			{xtype:'panel',region:'center',width:'80%',
    					items:[
    						{xtype:'button',text:'存为模板',scope:this,width:'100%',height:45,
			    			handler:function(){
			    				this.saveTemplate();
			    				this.tmpManageWin.hide();
			    			}},
			    			{xtype:'button',text:'管理模板',scope:this,width:'100%',height:45,
			    			handler:function(){
			    				this.switchTemplate();
			    				this.tmpManageWin.hide();
			    			}}
    					]
    				},
    				{region:'east',width:'10%'}
    			]
    		});
    	}
    	this.tmpManageWin.show();
    },
    switchTemplate:function(){//切换模板
    	var items = this.quickTmpChangeCombo.store.data.items;
		var datas = [];
    	if(items.length > 0){
    		Ext.each(items,function(item){
    			datas.push(item.data);
    		});
    	}
    	var win = Ext.getCmp('SwitchTemplateID');
		if( !win){
			win=new SwitchTemplateWindow(this,datas);
		}else{
			win.reloadDatas(this,datas);
		}
		win.show();
    	
    },
    selectIndex:function(){//指标选择
    	Ext.Ajax.request({
			url : root + '/query/Key!queryMeas.action',scope:this,
			params : {query_id : this.query_id},
			width:300,height:160,
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				var win = Ext.getCmp('IndexSelectID');
				if( !win){
					win=new IndexSelectWin(this,list);
				}else{
					win.reloadDatas(this,list);
				}
				win.show();
				
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
    },
    selectGraphic:function(){//选择图形
    	var win = Ext.getCmp('GraphicSelectID');
		if( !win){
			win=new GraphicSelectWin(this);
		}else{
			win.reloadDatas(this);
		}
		win.show();
    	
    }
});


//维过滤窗口
DimFilterWindow = Ext.extend(Ext.Window, {
	query_id: null,
	dim: null,
	main: null,
	
	constructor: function(query_id, dim, main){
		this.query_id = query_id;
		this.dim = dim;
		this.main = main;
		
		DimFilterWindow.superclass.constructor.call(this, {
			title: '维过滤器',
    		width: 450,
    		height: 450,
    		autoScroll: true,
    		closeAction: 'close',
    		layout: "form",
    		resizable: false,
    		labelWidth: 60,
    		maximizable: false,
    		labelAlign: 'right',
    		bodyStyle: 'padding: 5px 3px 3px 3px;background: #FFF; ',
    		defaults: {
    			border: false,
                xtype: 'itemselector',
                anchor: '99%',
               	drawUpIcon:false,
			    drawDownIcon:false,
			    drawLeftIcon:true,
			    drawRightIcon:true,
			    drawTopIcon:false,
			    drawBotIcon:false,
			    imagePath : '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage'
    		},
            items:[],
    		buttons: [{
    			text: '确定',
    			scope: this,
    			handler: this.doSave
    		},{
    			text: '关闭',
    			scope: this,
    			handler: function(){ 
    				this.close();
    			}
    		}]
		});
		
		this.__loadItems();
	},
	__loadItems: function(){
		Ext.Ajax.request({
	         url: root + '/query/Key!queryDimSlices.action',
	         params: {
	             query_id: this.query_id,
	             key: this.dim
	         },
	         scope: this,
	         success: function (res, opt) {
	             var d = Ext.decode(res.responseText);
	             this.defaultData = d;
	             var items = this.createItems(d);
	             this.add(items);
	             this.doLayout();
	         }
	     });
	},
	defaultData: null,
	doSave: function(){
		var level = null,
			data = [],
			records = null;
		for(var i = 0; i< this.items.getCount(); i++){
			var sel = this.items.get(i);
			var rs = sel.toMultiselect.store.getRange();
			if(rs.length > 0){
				level = sel.level;
				records = rs;
				break;
			}
		}
		if(level){
			for(var i = 0; i< records.length ;i ++){
				data.push(records[i].get("id"));
			}
		}else{
			var ps = this.defaultData["others"];
			var arr = null , _level = null;
			for(var key in ps){
				arr = ps[key];
				_level = key;
			}
			//如果默认中有值，那么可以肯定已删除了默认值
			if(arr){
				level = _level;
				data = [];
			}
		}
		
		//如果level为null，则无变化
		if(level){
			var ps = {
	             query_id: this.query_id,
	             dim: this.dim,
	             level: level
	         };
			if(data && data.length != 0){
	             ps["level_values"] = data;
			}
			Ext.Ajax.request({
				 url: root + '/query/Show!cubeSlices.action',
		         params: ps,
		         scope: this,
		         success: function (res, opt) {
		             var d = Ext.decode(res.responseText);
		             this.close();
		             this.main.xtablePanel.loadAndView(0);
		         }
			});
		}else{
			 this.close();
		}
	},
	createItems: function(data){
		var items = [];
		this.setTitle("维过滤器《" + data.simpleObj + "》");
		for(var i= 0 ; i< data.records.length; i++){
			var cfg = data.records[i];
			items.push(this.createItemSelector(cfg, data));
		}
		return items;
	},
	createItemSelector: function(cfg, data){
		var id = cfg["id"] + "_" + cfg["dim_level"];
		var defArrs = data["others"][cfg["dim_level"]];
		return {
			id: id,
			level: cfg["dim_level"],
			name: cfg["column_code"],
			fieldLabel: cfg["dim_level_name"],
            multiselects: [{
                width: 160,
                height: 120,
                store: new Ext.data.JsonStore({
                	autoLoad: true,
                	url: root + '/query/Key!queryLevelValues.action',
                	baseParams: {
                		key: this.dim,
                		key_value: cfg["dim_level"]
                	},
			        fields: ['id','name'],
			        listeners: {
			        	scope: this,
			        	load: function(store, rs){
			        		if(!defArrs){return;}
			        		var _tmpRs = [];
			        		for(var i = 0; i< defArrs.length; i++){
			        			for(var j = 0; j< rs.length; j++){
			        				if(defArrs[i] == rs[j].get("id")){
			        					_tmpRs.push(rs[j]);
			        					break;
			        				}	
			        			}
			        		}
			        		var sel = Ext.getCmp(id);
			        		sel.fromMultiselect.store.remove(_tmpRs);
			        		sel.toMultiselect.store.add(_tmpRs);
			        	}
			        }
			    }),
                legend: '可选',
                displayField: 'name',
                valueField: 'id',
	            tbar:[{
	            	xtype:'textfield',
	            	emptyText: '过滤..',
	            	width: 150,
	            	enableKeyEvents:true,
					listeners:{
						scope:this,
						keyup: function(txt,e){
							if(e.getKey() == Ext.EventObject.ENTER){
								var value = txt.getValue();
								var store= Ext.getCmp(id).multiselects[0].store;
								store.filterBy(function(record){
									if(Ext.isEmpty(value)){
										return true;
									}else{
										return record.get('name').indexOf(value) >= 0;
									}
								},this);
							}
						}
					}
				}]
            },{
                width: 160,
                legend: '已选',
                height: 120,
                displayField: 'name',
                valueField: 'id',
                store: new Ext.data.JsonStore({
			        fields: ['id','name']
			    })
            }],
            listeners: {
            	scope: this,
            	change: this.selChange
            }
		}
	},
	lastMutiselect: null,
	selChange: function(sel, newV, oldV){
  		if(newV.length == 0 && oldV.length == 0){
  			return ;
  		}
  		var items = this.items;
  		this.lastMutiselect = sel;
  		for(var i = 0; i< items.getCount() ; i++){
  			var _sel = items.get(i);
  			if(_sel !== sel){
  				_sel.fromMultiselect.store.add(_sel.toMultiselect.store.getRange());
  				_sel.toMultiselect.store.removeAll();
  			}
  		}
  	}
});
//维层排序框
DimSortWindow = Ext.extend(Ext.Window, {
	query_id: null,
	dim: null,
	main: null,
	
	constructor: function(query_id, dim, main){
		this.query_id = query_id;
		this.dim = dim;
		this.main = main;
		
		DimSortWindow.superclass.constructor.call(this, {
			title: '自定义排序器',
    		width: 450,
    		height: 450,
    		autoScroll: true,
    		closeAction: 'close',
    		layout: "form",
    		resizable: false,
    		labelWidth: 60,
    		maximizable: false,
    		labelAlign: 'right',
    		bodyStyle: 'padding: 5px 3px 3px 3px;background: #FFF; ',
    		defaults: {
    			border: false,
                xtype: 'itemselector',
                anchor: '99%',
               	drawUpIcon:false,
			    drawDownIcon:false,
			    drawLeftIcon:true,
			    drawRightIcon:true,
			    drawTopIcon:false,
			    drawBotIcon:false,
			    imagePath : '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage'
    		},
            items:[],
    		buttons: [{
    			text: '确定',
    			scope: this,
    			handler: this.doSave
    		},{
    			text: '关闭',
    			scope: this,
    			handler: function(){ 
    				this.close();
    			}
    		}]
		});
		
		this.__loadItems();
	},
	__loadItems: function(){
		Ext.Ajax.request({
	         url: root + '/query/Key!queryDimSort.action',
	         params: {
	             query_id: this.query_id,
	             key: this.dim
	         },
	         scope: this,
	         success: function (res, opt) {
	             var d = Ext.decode(res.responseText);
	             this.defaultData = d;
	             var items = this.createItems(d);
	             this.add(items);
	             this.doLayout();
	         }
	     });
	},
	defaultData: null,
	doSave: function(){

		//map<Integer,String[]>	
		var sort_map={};	
		for(var i = 0; i< this.items.getCount(); i++){
			var sel = this.items.get(i);
			var rs = sel.toMultiselect.store.getRange();
			
			var level = sel.level;
			var	data = [];
			for(var j = 0; j< rs.length ;j ++){
				data.push(rs[j].get("id"));
			}
			sort_map[level] = data;
			
		}
		
		var ps = {
             query_id: this.query_id,
             dim: this.dim,
             cube_sort_map:Ext.encode(sort_map)
         };
		Ext.Ajax.request({
			 url: root + '/query/Show!cubeSort.action',
	         params: ps,
	         scope: this,
	         success: function (res, opt) {
	             var d = Ext.decode(res.responseText);
	             this.close();
	             this.main.xtablePanel.loadAndView(0);
	         }
		});
	
	},
	createItems: function(data){
		var items = [];
		this.setTitle("维排序器《" + data.simpleObj + "》");
		for(var i= 0 ; i< data.records.length; i++){
			var cfg = data.records[i];
			items.push(this.createItemSelector(cfg, data));
		}
		return items;
	},
	createItemSelector: function(cfg, data){
		var id = "_sort"+cfg["id"] + "_" + cfg["dim_level"];
		var defArrs = data["others"][cfg["dim_level"]];
		return {
			id: id,
			level: cfg["dim_level"],
			name: cfg["column_code"],
			fieldLabel: cfg["dim_level_name"],
            multiselects: [{
                width: 160,
                height: 120,
                store: new Ext.data.JsonStore({
                	autoLoad: true,
                	url: root + '/query/Key!queryLevelValues.action',
                	baseParams: {
                		key: this.dim,
                		key_value: cfg["dim_level"]
                	},
			        fields: ['id','name'],
			        listeners: {
			        	scope: this,
			        	load: function(store, rs){
			        		if(!defArrs){return;}
			        		var _tmpRs = [];
			        		for(var i = 0; i< defArrs.length; i++){
			        			for(var j = 0; j< rs.length; j++){
			        				if(defArrs[i] == rs[j].get("id")){
			        					_tmpRs.push(rs[j]);
			        					break;
			        				}	
			        			}
			        		}
			        		var sel = Ext.getCmp(id);
			        		sel.fromMultiselect.store.remove(_tmpRs);
			        		sel.toMultiselect.store.add(_tmpRs);
			        	}
			        }
			    }),
                legend: '可选',
                displayField: 'name',
                valueField: 'id',
	            tbar:[{
	            	xtype:'textfield',
	            	emptyText: '过滤..',
	            	width: 150,
	            	enableKeyEvents:true,
					listeners:{
						scope:this,
						keyup: function(txt,e){
							if(e.getKey() == Ext.EventObject.ENTER){
								var value = txt.getValue();
								var store= Ext.getCmp(id).multiselects[0].store;
								store.filterBy(function(record){
									if(Ext.isEmpty(value)){
										return true;
									}else{
										return record.get('name').indexOf(value) >= 0;
									}
								},this);
							}
						}
					}
				}]
            },{
                width: 160,
                legend: '已选',
                height: 120,
                displayField: 'name',
                valueField: 'id',
                store: new Ext.data.JsonStore({
			        fields: ['id','name']
			    })
            }]
		}
	}
});