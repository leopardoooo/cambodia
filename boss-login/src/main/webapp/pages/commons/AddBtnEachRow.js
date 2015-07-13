
AddBtnEachRow = function(){
	var cmFormat = "<div title='{0}' class='{1}' style='float:left;width:20;height:16;background-repeat:no-repeat;cursor:pointer;'  onclick='AddBtnEachRow.goUrl({2})'></div>";	
	
	var pub = {
		add: function(comp){
			var id = comp.getId();
			if(Ext.isEmpty(id))return;
			
			var resources = App.data.resources;
			
			var buttons = [];
			Ext.each(resources,function(res){
				if(res["attrs"]["panel_name"] && res["attrs"]["panel_name"].indexOf(id)>=0 && res["attrs"]["button_type"] == 'C'){
					var attrs = res["attrs"];
					buttons.push(String.format(cmFormat,attrs["res_name"],attrs["iconcls"],
						Ext.encode(res),attrs["show_name"])
					);
				}
			},this);
			
			
			if(buttons.length == 0)return;

			comp.getColumnModel().config.push({
//						header : "操作",
						dataIndex:'OPERATE',
						width : buttons.length * 22+5>50?buttons.length * 22+5:50,
						sortable:false,
						menuDisabled:true, 
						renderer:function(value,meta,record,rowIndex,columnIndex,store){
							//可以给'OPERATE'字段赋值为'F'以去掉操作按钮
							if(value !='F')
								return buttons.join(' ');
							else
								return '';
						}
				});
			comp.getColumnModel().setColumnHeader(comp.getColumnModel().getColumnCount()-1,'操&nbsp;&nbsp;作');
			
		},
		/**
		 * private
		 * toolbar上按钮的点击事件
		 */
		goUrl :function(){
			var resource = arguments[0];
			t = resource['attrs'];
			
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
	    			App.menu.bigWindow.show(  resource , o);
	    		}
	    	}
		}
	};
	return pub;
	
}();