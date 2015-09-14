Ext.ns("Ext.ux");

/**
 * 给树添加过滤的输入框，及展开和关闭的功能
 */
Ext.ux.FilterTreePanel = Ext.extend( Ext.tree.TreePanel , {
	
	//搜索框的宽度
	searchFieldWidth: 120,

	filterObj: null,
	constructor: function( cfg ){
		var FTP = langUtils.bc('common.filterTreePanel');
		Ext.apply( this , cfg || {});
		this.searchT = new Ext.form.TextField({
			width: this.searchFieldWidth,
			emptyText: FTP['emptyTipSearchField'],
			selectOnFocus: true,
			enableKeyEvents: true,
			listeners: {
				keydown: {
                    fn: this.doFilterTree,
                    buffer: 350,
                    scope: this
                }
			}
		});
		
		Ext.apply( this,{
			tbar: [ ' ', this.searchT , ' ', ' ',{
		            iconCls: 'icon-expand-all',
					tooltip: FTP['btnExpandAll'],
					scope: this,
					handler : this.doExpandAll
		         }, '-', {
			         iconCls: 'icon-collapse-all',
			         tooltip: FTP['btnCollapseAll'],
			         scope: this,
				 	 handler : this.doCollapseAll
		    }]
		});
		Ext.ux.FilterTreePanel.superclass.constructor.call(this);
	
		this.filterObj = new Ext.tree.TreeFilter( this , {
			clearBlank: true,
			autoClear : true,
			clear : function(v){
		        var t = this.tree;
		        var af = this.filtered;
		        for(var id in af){
		            if(typeof id != "function"){
		                var n = af[id];
		                if(n){
		                	if(n.ui)
		                    n.ui.show();
		                }
		            }
		        }
		        this.filtered = {}; 
		    }
		});
	},
	//展开所有节点
	doExpandAll : function(){
		this.expandAll();
	},
	//合并节点
	doCollapseAll : function(){
		this.collapseAll();
	},
	hiddenPkgs:[],//上次隐藏的节点集合
	//节点过滤
	doFilterTree : function( t , e ){
		var text = t.getValue(),filter = this.filterObj ;
		Ext.each(this.hiddenPkgs, function(n) {
					n.ui.show();
				});
		if (!text) {
			filter.clear();
			return;
		}
		this.doExpandAll();
		var re = new RegExp(Ext.escapeRe(text), 'i');
		filter.filterBy(function(n) {
					var textval = n.text;
					return !n.isLeaf() || re.test(n.text);
				});
		this.hiddenPkgs = [];
		var rootNode = this.getRootNode();
		//过滤3层及以上tree
		this.getRootNode().cascade(function(n) {
					if (!n.isLeaf() && n.ui.ctNode.offsetHeight < 3) {
						n.ui.hide();
						this.hiddenPkgs.push(n);
					}
					if (n.id != rootNode.id) {
						if (!n.isLeaf() && n.ui.ctNode.offsetHeight >= 3
								&& this.hasChild(n, re) == false) {
							n.ui.hide();
							this.hiddenPkgs.push(n);
						}
					}
				},this);
	},
	//当前节点是否有子节点
	hasChild:function(n,re){
	    var str=false;
	    n.cascade(function(n1){
	        if(n1.isLeaf() && re.test(n1.text)){
	                     str=true;
	                     return;
	             }
	         });
	     return str;
    }
});