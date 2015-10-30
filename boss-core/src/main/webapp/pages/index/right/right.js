/**
 * 系统首页导航菜单
 */
NavigationMenu = Ext.extend( Ext.Panel , {
	
	//办理业务的窗口
	bigWindow: null,
	
	PAY_RESOURCE: { text: '支付',  attrs: {busiCode:'1027',
					url: 'pages/business/pay/PayForm.js,pages/business/pay/PayPanel.js'}},
	MERGE_FEE_RESOURCE: { text: '合并费用项',  attrs: {busiCode:'1026',
					url: 'pages/business/pay/MergeFeePanel.js'}},
	PRINT_RESOURCE: { text: '打印',  attrs: {busiCode:'1028',
					url: 'pages/business/pay/Print.jsp?type=through'}},
								
	feeMsg:  "<span ><label>{0}</label>笔未支付费用</span>"
						+ "<br><span>应付￥" + "<label>{1}</label></span> &nbsp;",
	payfee :0,
 	constructor: function(){
 		NavigationMenu.superclass.constructor.call(this ,{
			border: false,
//			layout: "border",
//			renderTo:Ext.get('divFeeInfo'),
//			frame:false,
//			plain:true,
//			background:'transparent',
//			opacity:0,
			width:250,
			bodyStyle:'background-color: transparent;',
			items: [{
//				region: 'center',
//				title: '导航菜单栏',
//				layout: 'border',
				iconCls: 'icon-navigate',
				items: [{
//					title: '费用信息',
					height: 20,
					border: false,
					buttonAlign:'center',
					bodyStyle: 'padding-top: 5px;background-color: #F9F9F9;',
					html: "<div id='divFeeInfo2'>" + String.format(this.feeMsg,"0","0.00") + "</div>",
					buttons: [{
						text: '打印',
						scope: this,
						handler: function(){
							if(!App.getApp().getCustId()){
								Alert('请先查询客户');
								return ;
							}
							App.getApp().menu.bigWindow.show(this.PRINT_RESOURCE ,{width: 710, height: 460});
						}
					},{
						text: '支付',
						scope: this,
						disabled: true,
						handler: function(){
							this.bigWindow.show( this.PAY_RESOURCE, {width: 710, height: 460});
						}
					}]
					
				}]
			}]
		});
		//实例化业务窗口
		this.bigWindow = new BigWindow();
 	},
 	refreshMenu: function( data ){
 		var menuPanel = this.items.itemAt(0).items.itemAt(0);
 		if(menuPanel.items && menuPanel.items.length > 0){
 			menuPanel.removeAll(true);
 		}
 		menuPanel.add(new ButtonPanel(data));
 		menuPanel.doLayout();
 	},
 	/** 
 	 * 隐藏业务窗口
 	 */
 	hideBusiWin: function(){
 		if(this.bigWindow)
 			this.bigWindow.hide();
 			
 		/*if(App.getData().busiTaskToDo.length > 0){
 			if(Ext.getCmp('BusiSelectWin')){
 				Ext.getCmp('BusiSelectWin').addPanel(App.getData().busiTaskToDo);
 				Ext.getCmp('BusiSelectWin').show();
 			}else{
 				new BusiSelectWin(App.getData().busiTaskToDo).show();
 			}
 		}*/
 			
 	},
 	getBusiWin: function(){
 		return this.bigWindow;	
 	}
});
 
/**
 * 封装一个按钮面板,按照一行两列的布局显示所给定的按钮。
 */
ButtonPanel = Ext.extend(Ext.Panel, {
    layout:'table',
    defaultType: 'button',
    baseCls: 'x-plain',
    cls: 'btn-panel',
    layoutConfig: { columns:2 },
    autoScroll:true,
    constructor: function(datas){
    	//给按钮添加默认值
    	var buttons =[];
        for(var i = 0, d; d = datas[i]; i++){
        	var b = {};
        	Ext.apply(b,d);
            b.width =60;
            b.handler = this.goUrl;
            b.scale = 'medium';
            b.iconAlign = 'top';
            buttons.push(b);
        }
        ButtonPanel.superclass.constructor.call(this, {
			items: buttons
		});
    },
    /**
     * 单击菜单按钮的回调函数
     */
    goUrl:function(){
    	var t = this.attrs;
    	//赋值当前的资源数据，以便在业务模块中使用。
    	App.data.currentResource = t;
    	if( Ext.isEmpty(t.url )&& Ext.isEmpty(t.handler)){
    		Alert("请为该资源配置脚本文件!");
    		return ;
    	}
    	//获取文件名不包含后缀
    	var handler = t.handler;
    	if (!Ext.isEmpty(t.url)){
    		var s = t.url.lastIndexOf('/'),e = t.url.lastIndexOf('.');
    		handler = t.url.substr( s+1 , e - s -1 );
    	}
    	if(!Ext.isFunction(MenuHandler[handler])){
    		Alert("在MenuHandler.js中找不到对应的校验函数"+ handler + "");
    		return;
    	}
    	var o = MenuHandler[handler].call();
    	if ( o !== false ){
    		if(!o.width || !o.height){
    			Alert("校验函数"+ handler + "没有返回窗体的width、height属性!");
    		}else{
    			App.menu.bigWindow.show(  this , o );
    		}
    	}
    }
});


/**
 * 
 * @class BusiSelectWin
 * @extends Ext.Window
 */
BusiSelectWin = Ext.extend(Ext.Window,{
	width : null,
	constructor : function(params){
		BusiSelectWin.superclass.constructor.call(this,{
			id : 'BusiSelectWin',
			layout : 'fit',
			title : '请选择下一步操作',
			height : 180,
			width : this.width,
			maximizable : false,
			closeAction : 'hide'
//			items : [panel],
//			buttons : [
////				{
////				text : '取消',
////				scope : this,
////				handler : function() {
////					App.getData().busiTaskToDo = [];
////					this.close();
////				}
////			}
//			]
		});
		
		this.addPanel(params);
	},
	addPanel : function(params){
		this.removeAll();
		
		//清空数据
		App.getData().busiTaskToDo = [];
		
		if(params.length/3 >= 1){
			this.width = 350;
		}else{
			this.width = 250;
		}
		
		var buttons =[];
        for(var i = 0, d; d = params[i]; i++){
        	var b = {};
        	Ext.apply(b,d);
            b.width =80;
            b.handler = this.doSave;
            b.scale = 'medium';
            b.iconAlign = 'top';
            buttons.push(b);
        }
        
//        var items = [{
//            xtype: 'box',
//            autoEl: {tag: 'h3', html :'请选择下一步操作',  style:"padding:15px 0 3px;"},
//            colspan: 3
//        }].concat(buttons);
        
        var style1 = 'background:#F9F9F9;padding : 30px 50px 20px 60px';
		var style2 = 'background:#F9F9F9;padding : 30px 10px 20px 20px';
		
		var panel = new Ext.Panel({
			layout:'table',
		    defaultType: 'button',
		    baseCls: 'x-plain',
		    cls: 'btn-panel',
		    layoutConfig: { columns:3 },
		    autoScroll:true,
		    bodyStyle : params.length >=2 ? style2 : style1,
			items : buttons
		});
		
		this.add(panel);
		this.doLayout();
	},
	doSave : function(){
		Ext.getCmp('BusiSelectWin').hide();
		App.getData().busiTaskToDo = [];
		if(this.attrs.panel_name.substring(0,1) == 'A'){
			App.main.infoPanel.activate('ACCT_PANEL');
		}else if(this.attrs.panel_name.substring(0,1) == 'U'){
			App.main.infoPanel.activate('USER_PANEL');
		}else if(this.attrs.panel_name.substring(0,1) == 'C'){
			App.main.infoPanel.activate('CUST_PANEL');
		}
		
		if(this.callback){
			this.callback.fn(this.callback.params);
		}
		
		var t = this.attrs;
    	//赋值当前的资源数据，以便在业务模块中使用。
    	App.data.currentResource = t;
		var o = MenuHandler[t.handler].call();
    	if ( o !== false ){
    		if(!o.width || !o.height){
    			Alert("校验函数"+ handler + "没有返回窗体的width、height属性!");
    		}else{
    			App.menu.bigWindow.show( this , o );
    		}
    	}
	}
});