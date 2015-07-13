CountyChooseWin = Ext.extend(Ext.Window,{
	constructor : function(v){
		CountyChooseWin.superclass.constructor.call(this,{
			title: '选择地市',
			layout: 'fit',
			id:'countyChooseWinId',
			width: 400,
			height: 400,
			closeAction:'close',
			items: [new CountyChoosePanel(v)]
		})
	}
});
/**
 * 地市选择树
 * @class CountyChoosePanel
 * @extends Ext.ux.FilterTreePanel
 */
CountyChoosePanel = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	value:null,
	constructor: function(v){
		this.value = v;
		countyChooseThis = this;
		var loader = new Ext.tree.TreeLoader({
			url: root + "/config/SendMsg!getCountyTree.action"
		});
		deptTreeThis = this;
		CountyChoosePanel.superclass.constructor.call(this, {
			width 	: 210,
			split	: true,
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        rootVisible : false,
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			loader 		: loader,
			root : new Ext.tree.AsyncTreeNode()
		});
	}
	,initEvents : function(){
		CountyChoosePanel.superclass.initEvents.call(this);
		this.on("click" , function( node , e){
			if(node.attributes.pid=='-1'){return;}//area_id不允许选择
			Confirm("确定选择该县市吗",node.text,function(){
				Ext.getCmp('countyChooseWinId').close();
				var params = {countyId:node.attributes.id,areaId:node.attributes.pid};
				if(countyChooseThis.value == "MessageWin"){
					var win = new MessageWin(params);
					win.setTitle('增加催缴配置【'+node.attributes.text+'】');
					win.setIconClass('icon-add-user');
					win.show();
				}else if(countyChooseThis.value == "StopTaskWin"){
					var win = new StopTaskWin(params);
					win.setTitle('增加停机配置【'+node.attributes.text+'】');
					win.setIconClass('icon-add-user');
					win.show();
				}
			})
		} , this);
	},openNext:function(){
		var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var j = 0; j < childarr.length; j++) {
				childarr[j].expand();
			}
		}
	},initComponent : function() {
		CountyChoosePanel.superclass.initComponent.call(this);
		this.getRootNode().expand();
		this.getRootNode().on('expand', function() {
					deptTreeThis.openNext();
				});
	}
});