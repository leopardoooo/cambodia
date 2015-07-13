BaseInfoPanel = Ext.extend( Ext.Panel , {
 
 	// 是否需要重新加载数据 , 当切换客户时必须设置该状态为true
 	reload:	true , 
 	mask: null ,
 	isReload: function(){
 		return this.reload;
 	},
 	setReload: function(b){
 		this.reload = b ;
 	},
	//显示Load提示
	showTip: function(){
		if(!this.mask)
			this.mask = new Ext.LoadMask(this.body, {
				msg:"正在查询，请稍等..."
			});
		this.mask.show();
	},
	//隐藏load提示
	hideTip: function(){
		if(this.mask)
			this.mask.hide();
	}
 
});