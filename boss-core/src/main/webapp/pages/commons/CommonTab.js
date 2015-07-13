/**
 * 统一的TabPanel，用于客户面板，用户面板，账户面板
 * @class CommonTab
 * @extends Ext.TabPanel
 */
CommonTab = Ext.extend(Ext.TabPanel,{
	isReload : true,
	initEvents: function(){
	  	for (var i = 0; i < this.items.length; i++) {
			var p = this.items.itemAt(i);
			p.on("activate", this.refreshPanel, this );
		}
	    CommonTab.superclass.initEvents.call(this);
	},
	//必须重写该方法
	refreshPanel : function(){
		
	}
})