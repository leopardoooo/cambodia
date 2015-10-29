Ext.ns("App");

ViewPortPanel = Ext.extend( Ext.Panel , {
	constructor: function(){
		ViewPortPanel.superclass.constructor.call( this,{
			layout:'border',
			id : 'ViewPortPanel',
			border: false,
	        items:[{
	      		region:'north',
				border: false,
				height: 95,
				layout: 'anchor',
				items:[App.tool , {
					xtype : 'box',
		            el	  : 'header',
		            border: false
				}]
	        },{
	        	region: 'center',
	        	layout: 'border',
	        	border: false,
	        	cls: 'content',
	        	items: [{
					region: 'center',
					layout: 'fit',
					items: App.main
				}]
	        }]
		});
	},
	//显示Load提示
	showTip: function(){
		App.data.tipValue = App.data.tipValue + 1;
		if(!this.mask)
			this.mask = new Ext.LoadMask(this.body, {
				msg:"正在查询，请稍等..."
			});
		this.mask.show();
	},
	//隐藏load提示
	hideTip: function(){
		if(App.data.tipValue > 0){
			App.data.tipValue = App.data.tipValue - 1;
		}
		if(App.data.tipValue == 0){
			if(this.mask)
				this.mask.hide();
			//刷新客户综合信息
//			Ext.getCmp('GeneralPanel').remoteRefresh();
			//缴费按钮
			if(App.data.paySearch && App.getCust().status!='INVALID' && App.getCust().cust_type!='UNIT'){
				App.main.infoPanel.activate('USER_PANEL');
				var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
				var store = userGrid.getStore();
				if(store.getCount() >0){
					if(App.getCustId() == store.getAt(0).get('cust_id')){
						var button = userGrid.getTopToolbar().items.itemAt(0);
						button.handler.call(button);
						App.data.paySearch = false;
					}
				}
				
			}else{
				if(App.getApp().getCust().cust_type != 'UNIT'){
					if(App.getApp().data.busiTaskBtn){
						this.goUrl(App.getApp().data.busiTaskBtn);
					}
				}
				
				App.getApp().data.busiTaskBtn = null;
			}
		}
		
	},
	removeTip : function(){
		App.data.tipValue = 0;
		if(this.mask){
			this.mask.hide();
		}
		App.data.paySearch = false;
	},
	goUrl : function(btn){
 		var t= btn.attrs;
		if(t.panel_name.substring(0,1) == 'A'){
			App.getApp().main.infoPanel.activate('ACCT_PANEL');
		}else if(t.panel_name.substring(0,1) == 'U'){
			App.getApp().main.infoPanel.activate('USER_PANEL');
		}else if(t.panel_name.substring(0,1) == 'C'){
			App.getApp().main.infoPanel.activate('CUST_PANEL');
		}
		
		if(btn.attrs.handler == 'OrderProd'){
			App.getData().busiTask['OrderProd'].callback.fn(App.getData().busiTask['OrderProd'].callback.params);
		}else if(btn.attrs.handler == 'PayFees'){
			App.getData().busiTask['PayFees'].callback.fn(App.getData().busiTask['PayFees'].callback.params);
		}
		
    	//赋值当前的资源数据，以便在业务模块中使用。
    	App.getApp().data.currentResource = t;
		var o = MenuHandler[t.handler].call();
    	if ( o !== false ){
    		if(!o.width || !o.height){
    			Alert("校验函数"+ handler + "没有返回窗体的width、height属性!");
    		}else{
    			App.getApp().menu.bigWindow.show( btn , o );
    		}
    	}
 	}
});


/**
 * 程序的入口函数
 */
Ext.onReady(function(){
	//加载首页初始化数据
 	Ext.Ajax.request({
 		url  : root + '/system/x/Index!indexInit.action' ,
 		params: {},
 		success: function(res , ops){
 			var data = Ext.decode(res.responseText);
 			App.data.resources = data.resources;
 			App.data.busiCfgData = data.busiCfgData;
 			App.data.cfgData = data.cfgData;
 			App.data.optr = data.optr;
 			App.data.depts = data.depts;
 			
 			App.data.deptBusiCode = [];//当前营业厅禁用的功能.
 			if (data.deptBusiCode)
	 			for(var idx =0;idx<data.deptBusiCode.length;idx++){
	 				App.data.deptBusiCode.push(data.deptBusiCode[idx].busi_code);
	 			}
			 			
 			App.data.deptAddress = [];//当前营业厅关联的区域.
 			if(data.deptAddress)
	 			for(var idx =0;idx<data.deptAddress.length;idx++){
	 				App.data.deptAddress.push(data.deptAddress[idx].addr_id);
	 			}
 			
 			for(var key in data.busiTask){
 				App.data.busiTask[key] = data.busiTask[key];
 			}
 			App.tool = new TopToolbar();
			App.search = new TopSearch();
			App.menu = new NavigationMenu();
			App.main = new Center();
		
			App.search.init();
			new Ext.Viewport({
				layout: 'fit',
				items: new ViewPortPanel()
			});
			
			//删除load提示
			setTimeout(function(){
				Ext.get('loading').remove();
		    }, 250 );
			
		    //显示整个页面
		    Ext.get('header').setVisible(true);
		    
			//首先激活默认的选项卡
			App.main.infoPanel.activeDefaultTab();
			//Ext.getDom('q').focus();
			Ext.getDom('q').value=tiptext;
 			
 			//刷新顶部按钮
			App.refrshTool();
			
			//开始轮询公告任务
			Ext.TaskMgr.start(bulletinTask);
			//检查操作员和服务器是否一致
			Ext.TaskMgr.start(checkOptrIsTrueTask);
 		}
 	});
 	App.acctDate();
});

