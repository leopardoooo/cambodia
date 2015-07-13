/**
 * 重写过滤树，根据附加属性过滤
 * @class PinYinFilterTree
 * @extends Ext.ux.FilterTreePanel
 */
PinyinFilterTree = Ext.extend(Ext.ux.FilterTreePanel,{
	//节点过滤
	doFilterTree : function( t , e ){
		var text = t.getValue() ,filter = this.filterObj ;
		if(!text){
			filter.clear();
			return;
		}  
		this.doExpandAll();
		var re = new RegExp('^.*' + text + '.*$','i');
		
		filter.filterBy( function( n ){
			return !n.leaf || re.test( n.attributes.others.attr ) ;
		});
	}
});

/**
 * 重写下拉框树
 * @class PinyinTreeCombo
 * @extends Ext.ux.TreeCombo
 */
PinyinTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	initList: function() {
		var rootVisible = false;
		if(this.rootNodeCfg){
			rootVisible = true;
		}else{
			this.rootNodeCfg = {
				expanded: true
			};
		}
		this.list = new PinyinFilterTree({
			root: new Ext.tree.AsyncTreeNode(this.rootNodeCfg),
			loader: new Ext.tree.TreeLoader({
				dataUrl: this.treeUrl,
				baseParams: this.treeParams
			}),
			floating: true,
			height: this.treeHeight,
			autoScroll:true,
			animate:false,
			searchFieldWidth: this.treeWidth - 80,
			rootVisible: rootVisible,
			listeners: {
				click: this.onNodeClick,
				scope: this
			},
			alignTo: function(el, pos) {
				this.setPagePosition(this.el.getAlignToXY(el, pos));
			}
		});
		if(this.minChars == 0){
			this.doQuery("");
		}
	}
});