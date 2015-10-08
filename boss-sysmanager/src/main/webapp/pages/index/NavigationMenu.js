/***
 * 系统导航菜单
 */
 
NavigationMenu = Ext.extend( Ext.Panel , {
	dataView:null,
	dvStore:null,
	constructor: function(data){
		var nMenuThiz = this;
		
		this.dvStore = new Ext.data.JsonStore({
	        fields:['res_id','res_name','res_pid','url','panel_name','show_name','handler','iconcls']
	    });
		this.dvStore.on('load', function(){
			this.dvStore.each(function(record){
				record.set('res_name', langUtils.resSys(record.get('res_id')));
				record.commit();
			}, this);
			this.dvStore.commitChanges();
		}, this);
		
	    this.dvStore.loadData(data);
	    
		this.dataView = new Ext.DataView({
	        store: this.dvStore,
	        tpl  : new Ext.XTemplate(
	            '<ul>',
	                '<tpl for=".">',
	                    '<li id="{handler}" class="thumb-wrap">',
							'<div style="width:75px;height:75px">',
								'<img width="48px" height="48px" src="/'+Constant.ROOT_PATH_LOGIN+'/resources/images/sysMenu/{iconcls}" />',
		                        '<span class="x-editable"><font color="#555555" style="font-family:微软雅黑;font-size:13"><b>{res_name}</b></font></span>',
	                        '</div>',
	                    '</li>',
	                '</tpl>',
					'<div class="x-clear"></div>',
	            '</ul>'
	        ),
	        selectedClass:'x-view-selected',
	        id: 'images-view',
	        itemSelector: 'li.thumb-wrap',	//必须项,值为item选择器
	        overClass   : 'images-hover',//鼠标悬停item时的类样式
	        singleSelect: true,
	        multiSelect : true,
	        autoScroll  : true,
	        style:'background-color:#dfe8f6;',
	        listeners: {
            	scope: this,
	        	click: this.doOpenResource
            }
	    });
		
		NavigationMenu.superclass.constructor.call(this, {
			region: 'west',
	        width: 210,
			split	: true,
			minSize	: 200,         
	        maxSize	: 290,
	        items : this.dataView,
	        lines:false,
	        autoScroll:true,
	        animCollapse:true,
	        animate:true,
	        collapseMode:'mini',
	        collapsible:true,
			bodyStyle:'padding:3px;background-color:#dfe8f6;'
		});
	},
	doOpenResource: function(dv, index){
		var record = this.dvStore.getAt(index);
		this.menuClick(record.data);
	},
	//资源点击事件
	menuClick:function(d){
		var panel = Ext.getCmp(d.handler);
		//不存在，则添加到tabpanel中;存在则激活它，不重新加载
		if(Ext.isEmpty(panel)){
			var obj = eval(window[d.handler]);
			
			panel = new obj();
			App.main.add(panel);
			//添加操作员正在使用的功能
			if(d.handler!='optrManage')
				App.addOnlineUser(d);
			App.main.nodes.push(d.handler);
		}
		App.main.activate(panel);
	},
	doLoadResult:function(){
		if(sub_system_id === '2' || sub_system_id === '6'){//只在系统配置中默认打开第一个页面
			if(this.dvStore.getCount()>0){
				var handler = this.dvStore.getAt(0).get('handler');
				var resName = this.dvStore.getAt(0).get('res_name');
				this.menuClick({res_name:resName,handler:handler});
			}
		}
	}
});
