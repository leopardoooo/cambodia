/**
 * 提供根据字母/中文拼音首字母进行过滤的功能，仅支持前端匹配;
 * 该组件仅支持一次性加载、节点数量不多的树
 * 
 * filter(String value,[String attr],[Node startNode]):void Filter the data by a specific attribute.
 * clear():void show all hidden nodes
 * 
 * config:
 * {ignoreFolder} if false the filter will also filter the folder node,default to true;
 * {clearAction}  the action to do for all the nodes while clearing the hidden nodes,acceptable values 
 * are 'collapse'、'expand'、undefined,default to undefined
 *
 * 1.1 改动：
 * 1.修改原控件支持数字模糊匹配，输入数字可以查找任意位置 by wang 20110216
 * 
 */
Ext.ns('Ext.ux')
Ext.ux.TreeFilter = function(tree,config){
	this.tree = tree;
	Ext.apply(this,config||{});
	this.pyCache = {};
	this.matches = [];//the nodes array which mathches the RegExp；
	this.lastQuery = '';
	this.cleared = true;//if cleared is true indecates there's no hidden nodes;
}

Ext.ux.TreeFilter.prototype = {
	ignoreFolder:true,
	clearAction:undefined,
	
	filter:function(value, attr , startNode){
		if(value == this.lastQuery){
			return;
		}
		value = value.trim();
		this.lastQuery = value;
		if(value.length == 0){
			this.clear(startNode);
			return;
		}else 
		startNode = startNode||this.tree.root;
		var fn = this.getFilterFn(value,attr);
		if(this.isForward(value)){
			this.filterMatches(fn);
		}else{
			this.filterAll(fn,startNode);
		}
		this.showPaths();
		this.cleared = false;
	},
	//make a filter function,which take node as it's argument when the node passed return true,otherwise false 
	getFilterFn:function(value, attr){
		attr = attr||'text';
		if(Ext.lib.isChinese(value)){
			var reg = new RegExp(value);
			return function(n){
				return reg.test(n.attributes[attr]);
			}	
		}else{
			return function(n){
				return Ext.lib.checkPy(value,n.attributes[attr],this.pyCache);
			}
		}
	},
	//check whether to do folter on the last mathched nodes
	isForward:function(q){
		var len = this.lastQuery.length;
		if(len == 0 || this.cleared){
			return false;
		}
		if((q.length > len)&&(q.substring(0,len) == this.lastQuery)){
			return true;
		}
		return false;
	},
	//private get the matchs and hide other nodes
	filterAll:function(fn,startNode){
		var arr = [];
		startNode = startNode||this.tree.root;				
		startNode.cascade(function(n){
			if(!n.leaf){
				n.expand(false,false);
				if (this.ignoreFolder) {
					n.ui.hide();
					return;
				}	
			}
			if(fn.call(this,n)){
				arr.push(n);	
			}
			else{
				n.ui.hide();
			}
		},this);
		this.matches = arr;
	},
	//private search match nodes from last matches while hide unmatched nodes
	filterMatches:function(fn){
		var arr = [];
		Ext.each(this.matches,function(n){
			if(fn.call(this,n)){
				arr.push(n);
			}else{
				n.bubble(function(n){
					n.ui.hide();
				});
			}
		},this);
		this.matches = arr;
	},
	//show the parentNodes of the matches
	showPaths:function(){
		Ext.each(this.matches,function(n){
			n.bubble(function(n){
				n.ui.show();
			});
		})
	},
	hasMatch:function(){
		return !Ext.isEmpty(this.matches);
	},
	clear : function(startNode){
		if(this.cleared === true){
			return;
		}
		startNode = startNode||this.tree.root;	
		startNode.cascade(function(n){
			n.ui.show();
			if (this.clearAction) {
				n[this.clearAction](true, true);
			}
		});
		this.cleared = true;
	},
	isCleared:function(){
		return this.cleared;
	},
	destroy:function(){
		Ext.destroyMembers(this,'pyCache','matches');
	}
} 
