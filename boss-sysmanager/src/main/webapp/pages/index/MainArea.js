MainArea = Ext.extend(Ext.TabPanel,{
	tabMenu:null,
	ctxItem:null,
	constructor: function(){
		MainArea.superclass.constructor.call(this,{
			region:'center',
			deferredRender:false,
			minTabWidth: 115,
			tabWidth:135,
			enableTabScroll:true,
			activeTab:0,
			listeners:{
				scope:this,
				tabchange:function(tab,panel){
					for(var i=0;i<App.main.nodes.length;i++){
						if(panel && App.main.nodes[i] === panel.id){
							App.menu.dataView.select(panel.id);
						}
					}
				},
				contextmenu:function(tab,panel,e){
					/**
                	 * 右键菜单定义代码
                	 */
                	if(!this.tabMenu){
	                	this.tabMenu = new Ext.menu.Menu({
					        id : 'tabMenu',
					        items : [{
					        		 text : '关闭',
					                iconCls : 'delete',
					                handler : function(){
				                		if(tab.ctxItem.closable)
				                			tab.remove(tab.ctxItem);
					                }
					        	},{
					                text : '关闭其他',
					                iconCls : 'delete',
					                handler : function(){
					                	tab.items.each(function(item){
					                		if(item.closable && item !== tab.ctxItem ){
					                			tab.remove(item);
					                		}
					                	});
					                }
					            },{
					                text : '全部关闭',
					                iconCls : 'delete',
					                handler : function(){
					                	tab.items.each(function(item){
					                		if(item.closable)
					                			tab.remove(item);
					                	});
					                }
					            }]
					    });
					}
					    e.stopEvent();
					    tab.ctxItem=panel;
					    this.tabMenu.showAt(e.getPoint());
				}
			}
		});
	}
	
});