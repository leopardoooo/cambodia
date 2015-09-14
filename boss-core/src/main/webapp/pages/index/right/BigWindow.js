/**
 * 办理业务的大窗口
 */ 
BigWindow = Ext.extend( Ext.Window , {
	maximizable : false,
	constructor: function(){
		BigWindow.superclass.constructor.call(this, {
			closeAction: 'hide',
//			closable : false,
			border: false,
			html: "<iframe id="+this.frameId+" width=100% height=100%"
						 +" frameborder=no  src=''></iframe>"
		});
	},
	initEvents : function(){
		//按Esc键关闭窗口
		var km = this.getKeyMap();
        km.on(27, this.onEsc, this);
        km.disable();
		BigWindow.superclass.initEvents.call(this);
	},
	frameId : "busiWindowPageFrame",
	show : function( t  , o ,record){
		var title;
		if(t && !t.attrs["res_id"]){
			title = t.text;
		}else{
			title = langUtils.res(t.attrs.res_id) || t.attrs["show_name"];
			if(Ext.isArray(title)){
				title = title[0];
			}
		}
		
		if(t){
			var busiCode = t.attrs.busicode;
			if(App.getData().deptBusiCode.contain(busiCode)){
				Alert('当前部门无法进行 “' + title + '” 这项业务!');
				return;
			}
		}
		App.data.ownFormSize = o;
		this.setTitle(title);
		this.setSize( o.width , o.height );
		//添加操作员正在使用的功能
		App.addOnlineUser(t.attrs);
		
		BigWindow.superclass.show.call(this);
		this.center();
		var url ;
		if (t.attrs.url.lastIndexOf('.jsp') >= 0) {
			url = t.attrs.url;
		} else {
			url = "go?url=" + t.attrs.url;
		}
		document.getElementById(this.frameId).src = root + "/" + url;
	},
	hide: function(){
		BigWindow.superclass.hide.call(this);
		document.getElementById(this.frameId).src= "";
	},
	/**
	 * 切换业务
	 * t {} 包含资源信息
	 * o {} width ,height 等属性,
	 */ 
	toggleBusi: function( t , o ){
		this.show(t,  o);
	},
	/**
	 * 更改当前的iframe url 重新加载新的JSP
	 */
	toggleUrl: function( title , o,url){
		this.setTitle(title);
		this.setSize( o.width , o.height);
		document.getElementById(this.frameId).src= root + "/" + url;
	}
});