/**
 * Author: hh
 * Date:2009.12.25  
 * Action:
 *   采用传统模式，top-left|right的布局方式，Ext布局管理器 。
 *   top  : image 
 *   left : resource tree ,采用异步的tree。而不是一次性加载all 
 * 	 right: main body。
 * version : 3.1 
 */

/**
 * 顶部面板包括工具条、顶部标题、背景图片
 */
HDPanel = Ext.extend( Ext.Panel , {
	constructor: function( tool ){
		HDPanel.superclass.constructor.call(this, {
			region:'north',
			border : false ,
			height: 80,
			layout:'anchor',
	        cls: 'index-header',
			items:[ tool,
				{
				xtype: 'box',
				el: 'header',
				anchor: 'none 26'
			}
			]
		});
	}
});
    
Ext.onReady(function() {
	
	Ext.Ajax.request({
 		url:root+'/system/Index!queryMenus.action',
 		params: {subSystemId: sub_system_id},
 		success: function(res , ops){
 			var data = Ext.decode(res.responseText);
 			
 			App.data.resources = data;
 			
 			//tButtons:菜单按钮；cButtons:功能按钮
 			var tButtons = [],cButtons=[];
 			for(var i=0;i<data.length;i++){
 				if(data[i]['button_type'] == 'C'){
 					cButtons.push(data[i]);
 				}else{
 					tButtons.push(data[i])
 				}
 			}
 			
 			//去掉文本框验证
			Ext.apply(Ext.form.TextField.prototype,{
				vtype:''
			});
			
			App.data.optr = Ext.decode(optr);
		    App.tool = new TopToolbar( {defaultChecked: sub_system_id} );
		    if (App.data.optr['login_name']=='admin'){
				//工具菜单
		    	App.tool.insert(2,'-');
				App.tool.insert(3, {
					text:langUtils.tools("gj"),
					iconCls:'icon-t-county',
					menu: new Ext.menu.Menu({
						items:[	
							{
								itemId:'reloadConfig',
								text:langUtils.tools("czpz"),
								iconCls:'icon-t-county'
							}
						],
						listeners: {
							scope: App.tool,
							itemclick: function(item){
								if(item.itemId === 'reloadConfig')
									App.tool.reloadConfig();
							}
						}
					})
				} );
				App.tool.doLayout();
			}
		    
			App.menu = new NavigationMenu(tButtons);
			App.subMenu = cButtons;
			App.hdPanel = new HDPanel( App.tool ) ;
			App.main = new MainArea();
			App.main.nodes = [];//存放用户点击打开的资源
			
			if(sub_system_id === '5'){//仓库
				App.main.add(new DepotIndexManager());
			}else if(sub_system_id === '2' || sub_system_id === '6'){//
				if(tButtons.length > 0){
//					var handler = tButtons[0]['handler'];
//					App.menu.menuClick.defer(100,App.menu,d[handler]);
					var d = tButtons[0];
					App.menu.menuClick.defer(100,App.menu,[{res_name:d['res_name'],handler:d['handler']}]);
				}
//				if(sub_system_id === '2'){
					
//				}
			}else{//系统管理(默认)
				
			}
			
			App.tool.insert(2,{
				text: langUtils.tools("qhbm"),
				iconCls: 'icon-t-dept',
				handler : App.tool.onToggleDept
			});
			
			new Ext.Viewport({
		        layout:'border',
				border:0,
		        items:[ App.hdPanel,App.menu,App.main]
		    });
		    
		    
		    App.clearLoadImage();
		    
		    //检查操作员和服务器是否一致
			Ext.TaskMgr.start(checkOptrIsTrueTask);
 		}
 	});
	
});