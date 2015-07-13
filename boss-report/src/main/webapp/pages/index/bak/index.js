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
			height : 80 ,
			layout:'anchor',
	        //cls: 'index-header',
	        split: true,
	        collapseMode:'mini',
			items:[ tool,{
				xtype: 'box',
				el: 'header',
				anchor: 'none 26'
			}]
		});
	}
});

/**
 * 显示自定义页面的面板，该面板包含一个iframe,并提供loadPage函数。
 * 用于重新加载一个指定url的页面。
 */
Main = Ext.extend( Ext.Panel ,{
	frameId : "loadMainPageFrame",
	
	constructor: function(){
		Main.superclass.constructor.call(this, {
			region : 'center',
			border :false,
			margins: '0 0 3 0',
			html   : "<iframe id="+this.frameId+" width=100% height=100%"
					 +" frameborder=no src=''></iframe>"
		});
	},
	loadPage : function( url ){
		document.getElementById(this.frameId).src= root + "/" + url;
	}
});

// store 字段中文排序 补丁 以及使报表的合计在排序的保持位置不变化
 Ext.data.Store.prototype.applySort = function() {
	if (this.sortInfo && !this.remoteSort) {
		var s = this.sortInfo, f = s.field;
		var st = this.fields.get(f).sortType;
		var fn = function(r1, r2) {
			if(!Ext.isEmpty(r1.data['issumrow_report'])&&r1.data['issumrow_report']=='T'){
	  			return 0;
	  		}else if(!Ext.isEmpty(r2.data['issumrow_report'])&&r2.data['issumrow_report']=='T'){
	  			return 0;
	  		}else{
  			  	var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
				// 添加:修复汉字排序异常的Bug
				if (typeof(v1) == "string") { // 若为字符串，
					return v1.localeCompare(v2);// 则用 localeCompare 比较汉字字符串, Firefox
												// 与 IE 均支持
				}
				// 添加结束
				return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
	  		}
		};
		this.data.sort(s.direction, fn);
		if (this.snapshot && this.snapshot != this.data) {
			this.snapshot.sort(s.direction, fn);
		}
	}
}; 

AlertReport= function( msg , fn , scope ){
	var m = Ext.Msg ;
	return m.show({
		title: m.title,
		msg: msg ,
		icon: m.INFO ,
		buttons: m.CANCEL,
		closable : false,
		fn : fn ,
		scope: scope 
	});
}; 

var siglerep_sign=false;
Ext.onReady(function(){
	App.data.optr = Ext.decode(optr);
	App.tool = new TopToolbar( {defaultChecked: sub_system_id} );
	
	//去掉文本框验证
	Ext.apply(Ext.form.TextField.prototype,{
		vtype:''
	});
	
	/*gridpanel单元格复制*/
	if  (!Ext.grid.GridView.prototype.templates) {    
	    Ext.grid.GridView.prototype.templates = {};
	}
	Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(    
	     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
	     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,    
	     '</td>'
	);

	
	App.menu = new NavigationMenu();
//	App.menu = new ReportMenu();
	App.hdPanel = new HDPanel( App.tool ) ;
	//App.main = new Main();
	App.page = new RepTabPanel();
	
	new Ext.Viewport({
        layout:'border',
		border:0,
        items:[ App.hdPanel, App.menu , App.page]
    });
	 
	App.clearLoadImage();
	
	//App.main.loadPage( "pages/index/welcome.html" );
});

