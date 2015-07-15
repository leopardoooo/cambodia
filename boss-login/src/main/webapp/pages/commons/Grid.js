
Ext.ns("Ext.ux");

var cmFormat = "<div title='{0}' class='{1} toolbtn' onclick='goUrl({2})'></div>";	
var lockCmFormat = "<a onclick='goUrl({0},{1})'><img title='{2}' class='toolbtn' src='/boss-login/resources/images/menu2/{3}' /></a>";
Ext.ux.Grid = Ext.extend(Ext.grid.GridPanel,{
	
	attrs:[],//子面板tbar上的按钮 和 行上面的按钮的集合
	childID:null,//子面板ID
	constructor:function(cfg){
		this.childID = cfg.id;
		this.attrs = [];//清空上次
		Ext.ux.Grid.superclass.constructor.call(this,cfg);
	},
	initEvents : function(){
		Ext.ux.Grid.superclass.initEvents.call(this);
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	/**
	 * 部分grid里有业务操作,客户希望操作完之后,还在当前页面(当前分页).
	 */
	reloadCurrentPage:function(){
		var pageBar = this.getBottomToolbar();
		var start = 0;
		var limit = this.pageSize;
		if(pageBar){
			start = pageBar.cursor;//当前分页信息的start
		}
		this.getStore().load({
			params:{
				start:start,
				limit:limit
			}
		});	
	}
	,initComponent:function(){
		//获取子类需要添加到行的按钮集合
		this.getChildBtns();
		if(this.attrs.length > 0){
			var columnBtns = [];//行按钮集合
			var tbarBtns = [];//tbar按钮集合
			for(var i=0;i<this.attrs.length;i++){
				var attrs = this.attrs[i];
				var attr = attrs["attrs"];
				if(attr["button_type"]=='C'){//行按钮
//					attrs["attrs"]["gridId"] = this.childID;
					columnBtns.push(attrs);
				}else if(attr["button_type"]=='T'){//tbar上按钮
					var btn = {};
					attrs["handler"]=goUrl;//按钮点击事件
					
					Ext.apply(btn,attrs);
					//批量缴费
					if(this.childID == 'A_ACCT'){
						btn["id"] = attrs.attrs.handler;
					}
					btn["tooltip"] = attr["res_name"];//鼠标悬浮时提示文本
					btn["text"] = "<font style='font-family:微软雅黑;font-size:13'><b>"+attr["show_name"]+"</b></font>";//显示文本
					tbarBtns.push(btn,'-');
				}
			}
			
			if(columnBtns.length > 0){
				var columnBtnsLength = columnBtns.length > 13 ? 13 : columnBtns.length;
				if(this.columns){//普通表格
					this.columns.push(new Ext.grid.Column({
						header : "操作",
						dataIndex:'OPERATE',
						menuDisabled : true,
						width : columnBtnsLength * 22>45?columnBtnsLength * 22+8:53,
						scope:this,
						renderer:function(value,meta,record,rowIndex,columnIndex,store){
							var arr = [];
							Ext.each(columnBtns,function(btn){
								if(App.func.FilterOperteBtn(record.data,btn['attrs']['busicode'],btn['attrs']['panel_name']) )
									arr.push(String.format(cmFormat,btn['attrs']["res_name"],btn['attrs']["iconcls"],Ext.encode(btn)));
							},this);
							return arr.join('');
						}
					}));
				}else{//列锁定表格
					var gridId = this.childID;
					this.cm.columns.push(new Ext.grid.Column({
						header : "操作",
						locked : true,
						menuDisabled : true,
						dataIndex:'OPERATE',
						width : columnBtnsLength * 22>45?columnBtnsLength * 22+40:63,
						scope:this,
						renderer:function(value,meta,record,rowIndex,columnIndex,store){
							var arr = [];
							Ext.each(columnBtns,function(btn){
								if(App.func.FilterOperteBtn(record.data,btn['attrs']['busicode'],btn['attrs']['panel_name']) )
									arr.push(String.format(lockCmFormat,Ext.encode(btn),gridId,btn['attrs']["res_name"],btn['attrs']["iconcls"]))
							},this);
							return arr.join('');
						}
					}));
				}
				
			}
			if(tbarBtns.length > 0 ){
				var tbar = this.tbar || [];
				Ext.apply(this , {
					tbar: new Ext.Toolbar({
						enableOverflow: true,
						items : tbar.concat(tbarBtns)
					})
				})
			}
		}
		Ext.ux.Grid.superclass.initComponent.call(this);
	}
	,getChildBtns:function(){
		var resources = App.getApp().data.resources;
		for(var i=0;i<resources.length;i++){
			if(this.childID.indexOf('C_PACKAGE') >=0 ){//C_PACKAGE,C_PACKAGE_UNIT
				if(resources[i]["attrs"]["panel_name"] && resources[i]["attrs"]["panel_name"].indexOf('C_PACKAGE')>=0){
					this.attrs.push(resources[i]);
				}
			}else if(this.childID.indexOf('C_CUST') >=0){//C_CUST,C_CUST_UNIT
				if(resources[i]["attrs"]["panel_name"] && resources[i]["attrs"]["panel_name"].indexOf('C_CUST')>=0){
					this.attrs.push(resources[i]);
				}
			}else{
				if(resources[i]["attrs"]["panel_name"] && resources[i]["attrs"]["panel_name"].indexOf(this.childID)>=0){
					this.attrs.push(resources[i]);
				}
			}
		}
	}
});

/**
 * private
 * toolbar上按钮的点击事件
 */
var goUrl = function(btn,div){
	
	//设置当前选中行
	if(div && div.id){
		App.getData().currentRec = Ext.getCmp(div.id).getSelectionModel().getSelected();
	}
	
	
	var t,resource;
	if(this.attrs)
		t = this.attrs;
	else{
		resource = arguments[0];
		t = resource["attrs"];
	}
	
	//赋值当前的资源数据，以便在业务模块中使用。
	App.data.currentResource = t;
	
	if( Ext.isEmpty(t.url )&& Ext.isEmpty(t.handler)){
		Alert("请为该资源配置脚本文件!");
		return ;
	}
	//获取文件名不包含后缀
	var handler = t.handler;
	if (!handler && !Ext.isEmpty(t.url)){
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
			if(resource)
				App.menu.bigWindow.show(  resource , o);
			else
				App.menu.bigWindow.show(  this , o);
		}
	}
}