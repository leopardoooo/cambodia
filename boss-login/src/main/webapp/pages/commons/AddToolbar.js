
AddToolbar = function(){
	
	/**
	 * private
	 * toolbar上按钮的点击事件
	 */
	function goUrl(){
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
	
	/**
	 * public 可供调用的函数集合
	 */
	var pub = {
		add :function(comp){
			var id = comp.getId();
			
			var resources = App.data.resources;
			var buttons = [];
			Ext.each(resources,function(res){
				if(res["attrs"]["panel_name"] == id && res["attrs"]["button_type"] == 'T'){
					var btn = {};
					res["handler"]=goUrl;//按钮点击事件
					Ext.apply(btn,res);
					btn["tooltip"] = res["attrs"]["res_name"];//鼠标悬浮时提示文本
					btn["text"] = "<font style='font-family:微软雅黑;font-size:13'><b>"+res["attrs"]["show_name"]+"</b></font>";//显示文本
					buttons.push(btn);
					buttons.push('-');//分隔符
				}
			},this);
			
			if(buttons.length == 0)return;
			
			
			//当panel存在tbar时，则添加，否在新建toolbar
			if(comp.getTopToolbar()){
				comp.getTopToolbar().add(buttons);
			}else{
				comp.add(new Ext.Toolbar(buttons));
			}
		}
	};
	return pub;
	
}();