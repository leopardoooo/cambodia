Ext.ns("Ext.ux");

/**
 * 给树添加过滤的输入框，及展开和关闭的功能
 */
Ext.ux.QueryFilterTreePanel = Ext.extend( Ext.tree.TreePanel , {
	
	//搜索框的宽度
	searchFieldWidth: 120,
	queryValue:null,
	filterObj: null,
	onlySelectLeaf : true,// 只能选择叶子节点
	constructor: function( cfg ){
		Ext.apply( this , cfg || {});
		this.searchT = new Ext.form.TextField({
			width: this.searchFieldWidth,
			emptyText: lbc("common.filterTreePanel.emptyTipSearchField"),
			selectOnFocus: true,
			enableKeyEvents: true,
			listeners: {
				scope: this,
				specialKey: function(field, e){
        			if (e.getKey() == e.ENTER) {
        				this.doSearch();
        	        }
        		}
			}
		});
		
		Ext.apply( this,{
			tbar: [ ' ', this.searchT , ' ', ' ',{
				text :  lbc("common.queryBtn"),
				scope : this,
				iconCls : 'query',
				handler : this.doSearch
			}]
		});
		Ext.ux.QueryFilterTreePanel.superclass.constructor.call(this);	
	},	
	doSearch:function(){
		this.queryValue = this.searchT.getValue();
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
    },
    onBeforeLoad:function(loader,node){
	},
	// 检查当前的节点是否能够被点击
	isCanClick : function(node) {
		if (this.onlySelectLeaf) {
			if (!node.leaf || node.attributes['is_leaf'] == 'F')
				return false;
		}
		return true;
	}
});