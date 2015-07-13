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
				'<div class="row"><a href="#">{name}</a><span class="fl">{date}</span></div>',
			'</tpl>',
		'</div>'),

	constructor: function(){
		this.data = [
			{name: '客户分类统计', url: '', date: '2012-04-26'},
			{name: '统计平移报表', url: '', date: '2012-04-26'},
			{name: '剪线用户欠费统计', url: '', date: '2012-04-26'},
			{name: '银行扣费统计', url: '', date: '2012-04-26'},
			{name: '用户开户业务统计', url: '', date: '2012-04-26'},
			{name: '业务收费明细查询', url: '', date: '2012-04-26'},
			{name: '客户分类统计', url: '', date: '2012-04-26'}
		]
		LastOpenPortlet.superclass.constructor.call(this);
	},
	apply: function(data){
		return this.tpl.apply(data);
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