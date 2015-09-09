
Ext.ns("Ext.ux");

Ext.ux.Gpanel = Ext.extend(Ext.Panel,{
	
	attrs:[],//子面板tbar上的按钮 和 行上面的按钮的集合
	childID:null,//子面板ID

	
	constructor:function(cfg){
		this.childID = cfg.id;
		this.attrs = [];//清空上次
		Ext.ux.Gpanel.superclass.constructor.call(this,cfg);
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
				if(attr["button_type"]=='T'){//tbar上按钮
					var btn = {};
					attrs["handler"]=goUrl;//按钮点击事件
					Ext.apply(btn,attrs);
					var text = lang = langUtils.res(attr["res_id"]) || attr["show_name"];
					var tip = null;
					if(Ext.isArray(lang)){
						text = lang[0];
						tip = lang[1];
					}
					btn["tooltip"] = tip;//鼠标悬浮时提示文本
					btn["text"] = "<font style='font-family:微软雅黑;font-size:13'><b>"+text+"</b></font>";//显示文本
					tbarBtns.push(btn,'-');
				}
			}
			
			if(columnBtns.length > 0){
				this.columns.push({
					header : "操作",
							dataIndex:'OPERATE',
							width : columnBtns.length * 22+5>50?columnBtns.length * 22+5:50,
							renderer:function(value,meta,record,rowIndex,columnIndex,store){
								//可以给'OPERATE'字段赋值为'F'以去掉操作按钮
								if(value !='F'){
									return columnBtns.join(' ');
								}else
									return '';
							}
				});
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
		Ext.ux.Gpanel.superclass.initComponent.call(this);
	}
	,getChildBtns:function(){
		var resources = App.getApp().data.resources;
		for(var i=0;i<resources.length;i++){
			if(this.childID.indexOf('C_PACKAGE') >=0 ){
				if(resources[i]["attrs"]["panel_name"] && resources[i]["attrs"]["panel_name"].indexOf('C_PACKAGE')>=0){
					this.attrs.push(resources[i]);
				}
			}else if(this.childID.indexOf('U_PROD') >=0){
				if(resources[i]["attrs"]["panel_name"] && resources[i]["attrs"]["panel_name"].indexOf(this.childID)>=0){
					this.attrs.push(resources[i]);
				}
			}else{
				if(resources[i]["attrs"]["panel_name"] && this.childID.indexOf(resources[i]["attrs"]["panel_name"])>=0){
					this.attrs.push(resources[i]);
				}
			}
		}
	}
});

