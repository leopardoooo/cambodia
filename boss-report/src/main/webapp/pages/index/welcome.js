/**
 * 定义welcome page需要的脚本
 */
WelcomePortal = Ext.extend(Ext.ux.Portal, {
	constructor: function(){
		WelcomePortal.superclass.constructor.call(this, {
			title: '首页',
			iconCls: 'icon-home',
			xtype: 'portal',
			defaults: {
				style:'padding:10px 0 10px 10px',
				defaultType: 'portletc'
			},
			items:[{
				width: "30%",
				items:[{
					title: '最近打开的报表',
					target: new LastOpenPortlet()
				},{
					title: '我的收藏夹',
					target: new FavPortlet()
				}]
			},{
				width: "66%",
				items:[{
					title: '图表统计',
					height: 510,
					html: '<img src="./resources/images/chart.png" width="660px" height="480px"/>'
				},{
					title: '图表统计',
					height: 510,
					html: '<img src="./resources/images/chart.png" width="660px" height="480px"/>'
				},{
					title: '图表统计',
					height: 510,
					html: '<img src="./resources/images/chart.png" width="660px" height="480px"/>'
				}]
			}]
		});
	}
});


//
PortletContainer = Ext.extend( Ext.ux.Portlet, {
	title: 'a portlet',
	height: 255,
	closeHandler: Ext.emptyFn,
	gearHandler: Ext.emptyFn,
	target: null,
	
	constructor: function(config){
		Ext.apply(this, config);
		PortletContainer.superclass.constructor.call(this, {
			tools: [{
				id: 'gear',
				scope: this,
				handler: this.gearHandler
			},{
				id: 'close',
				scope: this,
				handler: this.closeHandler
			}],
			items: this.target
		});
	}
});
Ext.reg('portletc', PortletContainer);


// 最近打开的一些报表
LastOpenPortlet = Ext.extend(Ext.Panel, {
	tpl: new Ext.XTemplate(
		'<div class="linkList">',
			'<tpl for=".">',
				'<div class="row"><a href="#" onclick=\'App.left.addTabPanel("{rep_id}","{query_id}")\' >{query_id}</a><span class="fl">{date}</span></div>',
			'</tpl>',
		'</div>'),

	constructor: function(){	
		LastOpenPortlet.superclass.constructor.call(this);
		this.doLoad();
	},
	doLoad: function(){
		Ext.Ajax.request({
			url: root + "/query/RepDesign!queryDayOpen.action",
			scope: this,
			success: function(res, ops){
				var data = Ext.decode(res.responseText);
				this.doView(data.records);
				//alert(Ext.util.Cookies.get(optr["optr_name"]+'dayopen'));
				//有汉字引起cookies异常
				/**
				if(Ext.isEmpty(data)
					||Ext.isEmpty(data.records)
					||data.records.length==0){
					this.doView(Ext.decode(Ext.util.Cookies.get(optr["optr_name"]+'dayopen')));
				}else{
					
					Ext.util.Cookies.set(optr["optr_name"]+'dayopen',Ext.encode(data.records));
				}
				**/
			}
		});
	},
	doView: function(dataArr){
		this.tpl.overwrite(this.body, dataArr);
	}
});

// 我的收藏
FavPortlet = Ext.extend( Ext.Panel, {
	tpl: new Ext.XTemplate(
		'<div class="linkList">',
			'<tpl for=".">',
				'<div class="row">',
					'<a href="#" onclick=\'App.left.addTabPanel("{res_id}","{res_name}")\' >{res_name}</a>',
					'<span class="fl">{date}</span>',
				'</div>',
			'</tpl>',
		'</div>'),
	constructor: function(){
		FavPortlet.superclass.constructor.call(this);
		this.doLoad();
	},
	doLoad: function(){
		Ext.Ajax.request({
			url: root + "/query/RepDesign!queryMyReport.action",
			scope: this,
			success: function(res, ops){
				var data = Ext.decode(res.responseText);
				this.doView(data.records);
			}
		});
	},
	doView: function(dataArr){
		this.tpl.overwrite(this.body, dataArr);
	}
});