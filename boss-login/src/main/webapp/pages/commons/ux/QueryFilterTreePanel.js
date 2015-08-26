Ext.ns("Ext.ux");

/**
 * 给树添加过滤的输入框，及展开和关闭的功能
 */
Ext.ux.QueryFilterTreePanel = Ext.extend( Ext.tree.TreePanel , {
	
	//搜索框的宽度
	searchFieldWidth: 120,
	queryValue:null,
	filterObj: null,
	constructor: function( cfg ){
		Ext.apply( this , cfg || {});
		this.searchT = new Ext.form.TextField({
			width: this.searchFieldWidth,
			emptyText:'输入名称过滤...',
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
				text : '搜索',
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
	}	
});