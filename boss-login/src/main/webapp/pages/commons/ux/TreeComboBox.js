/**
 * 下拉可搜索树
 * 
 * @class Ext.TreeCombo
 * @extends Ext.form.ComboBox
 * 
 */
Ext.namespace("Ext.ux");

Ext.ux.TreeCombo = Ext.extend(Ext.form.ComboBox, {

	// extend property
	treeUrl : null,// 下拉树制定的数据来源
	treeParams : {}, // tree loader's 的参数
	rootNodeCfg : null,

	dispAttr : 'text',// 显示在下拉框中的树节点属性名称，可使用'.'操作符号，如others.fullName
	onlySelectLeaf : true,// 只能选择叶子节点
	treeWidth : 200,
	treeHeight : 300,

	// 修改原有的配置项
	editable : true,
	hideTrigger : true,
	forceSelection : true,
	minChars : 4,
	store : new Ext.data.JsonStore({
				fields : ['id', 'text']
			}),
	displayField : 'text',
	valueField : 'id',
	level :null, //传值到前台分辨区域小区
	// 为当前的store添加一个Item
	
	running :false,
	time:null,
	addOption : function(id, text) {
		var a = [];
		a[0] = new Ext.data.Record({
					id : id,
					text : text
				});
		this.store.add(a);
	},
	removeOption : function(id, text) {
		var record = new Ext.data.Record({
					id : id,
					text : text
				});
		this.store.remove(record);
	},
	setTreeParams : function(q) {
		this.list.getLoader().baseParams.comboQueryText = q;
	},
	initList : function() {
		var rootVisible = false;
		if (this.rootNodeCfg) {
			rootVisible = true;
		} else {
			this.rootNodeCfg = {
				expanded : true
			};
		}
		this.list = new Ext.ux.FilterTreePanel({
					root : new Ext.tree.AsyncTreeNode(this.rootNodeCfg),
					loader : new Ext.tree.TreeLoader({
								dataUrl : this.treeUrl,
								baseParams : this.treeParams
							}),
					floating : true,
					height : this.treeHeight,
					autoScroll : true,
					animate : false,
					searchFieldWidth : this.treeWidth - 80,
					rootVisible : rootVisible,
					listeners : {
						click : this.onNodeClick,
						checkchange : this.onCheckchange,
						scope : this
					},
					alignTo : function(el, pos) {
						this.setPagePosition(this.el.getAlignToXY(el, pos));
					}
				});
		if (this.minChars == 0) {
			this.doQuery("");
		}
	},
	expand : function() {
		var b = false;
		if (!this.list.rendered) {
			this.list.render(document.body);
			this.list.setWidth(this.el.getWidth());
			this.innerList = this.list.body;
			this.list.hide();
			b = true;
		}

		this.el.focus();
		if (this.isExpanded() || !this.hasFocus) {
			return;
		}

		if (this.title || this.pageSize) {
			this.assetHeight = 0;
			if (this.title) {
				this.assetHeight += this.header.getHeight();
			}
			if (this.pageSize) {
				this.assetHeight += this.footer.getHeight();
			}
		}

		if (this.bufferSize) {
			this.doResize(this.bufferSize);
			delete this.bufferSize;
		}
		this.list.alignTo.apply(this.list, [this.el].concat(this.listAlign));

		this.list.show();
		if (Ext.isGecko2) {
			this.innerList.setOverflow('auto'); // necessary for FF 2.0/Mac
		}
		this.mon(Ext.getDoc(), {
					scope : this,
					mousewheel : this.collapseIf,
					mousedown : this.collapseIf
				});
		this.fireEvent('expand', this);
		if (b) {
			this.list.setWidth(this.treeWidth);
		}
	},
	doQuery : function(q, forceAll) {
		if(q.length == 0 || this.isValid()){
			this.timeOutRun(function(){
				if (q.length >= this.minChars) {
					this.setTreeParams(q);
					this.list.setRootNode(new Ext.tree.AsyncTreeNode(this.rootNodeCfg));
					this.expand();
					this.list.expandAll();
				} else if (false == this.editable) {
					this.expand();
				}
				this.running = false;
			});
		}
	},
	timeOutRun:function(fn){
		if(this.running){
		  this.running = false;
		  clearTimeout(this.time)
		}
		this.running = true;
		this.time = fn.defer(800,this);
	},
	collapseIf : function(e) {
		if (this.minChars == 0) {
			if (!e.within(this.wrap) && !e.within(this.list.el)) {
				this.list.hide();
			}
		} else {
			if (!e.within(this.wrap) && !e.within(this.list.el)) {
				this.collapse();
			}
		}

	},

	onNodeClick : function(node, e) {
		if (!this.isCanClick(node)) {
			return;
		}
		var attrs = this.dispAttr.split(".");
		var temp = node.attributes;
		
		 var level = node.attributes.others.tree_level;
		if(!Ext.isEmpty(level)){
			this.level =level;
		}
		for (var i = 0; i < attrs.length; i++) {
			temp = temp[attrs[i]];
		}
		// 显示隐藏值
		this.addOption(node.id, temp);
		this.setValue(node.id);
		// 设置显示值
		this.setRawValue(temp);
		this.collapse();
		this.fireEvent('select', this, node, node.attributes);
	},
	onCheckchange : function(node, e) {
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
Ext.reg('treecombo', Ext.ux.TreeCombo);