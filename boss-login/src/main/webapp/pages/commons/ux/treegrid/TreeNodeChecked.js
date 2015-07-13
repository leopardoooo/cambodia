/**
 * @author zhangdaipign@vip.qq.com
 * @version 1.2.1 (1/4/2010)
 */
Ext.ns('Ext.plugin.tree');

Ext.plugin.tree.TreeNodeChecked = Ext.extend(Object, {
    // 级联选中
    cascadeCheck: true,
    
    // 级联父节点
    cascadeParent: true,
    
    // 级联子节点
    cascadeChild: true,
    
    // 连续选中
    linkedCheck: false,
    
    // 异步加载时，级联选中下级子节点
    asyncCheck: false,
    
    // 显示所有树节点checkbox，设置displayAllCheckbox==true，加载树时，如果node.checked属性为undefined，那么显示一个未选中的checkbox
    displayAllCheckbox: false,
    
    constructor: function(config) {
        config = config ||
        {};
        Ext.apply(this, config);
    },
    
    init: function(tree) {
        if (tree.cascadeCheck === false) {
            this.cascadeCheck = this.cascadeParent = this.cascadeChild = this.linkedCheck = this.asyncCheck = false;
        }
        
        Ext.apply(tree, {
            cascadeCheck: this.cascadeCheck,
            cascadeParent: this.cascadeParent,
            cascadeChild: this.cascadeChild,
            linkedCheck: this.linkedCheck,
            asyncCheck: this.asyncCheck,
            checkNode: this.checkNode,
            getLeafChecked: this.getLeafChecked
        });
        
        if (this.cascadeCheck) {
            tree.on('checkchange', this.onCheckChange);
        }
        
        if (this.cascadeCheck && this.cascadeChild && this.asyncCheck) {
            tree.on('expandnode', this.onExpandNode);
        }
        
        if (this.displayAllCheckbox) {
            var loader = tree.getLoader();
            loader.baseAttrs = loader.baseAttrs ||
            {};
            loader.baseAttrs['checked'] = false;
        }
    },
    
    // private
    onCheckChange: function(node, checked) {
        if (!this.cascadeCheck) {
            return;
        }
        
        var checkChange = function(ui) {
            ui.checkbox.checked = checked;
            // fix for IE6
            ui.checkbox.defaultChecked = checked;
            ui.node.attributes.checked = checked;
        };
        
        if (this.cascadeParent) {
            var loopParentChecked = function(parentNode) {
                var pui = parentNode.getUI();
                if (!Ext.isDefined(pui.checkbox)) {
                    return;
                }
                if (checked) {
                    checkChange.call(this, pui);
                } else {
                    var c = false;
                    Ext.each(parentNode.childNodes, function(n) {
                        if (c || n.id === node.id) {
                            return;
                        }
                        if (n.getUI().checkbox) {
                            c = n.getUI().checkbox.checked;
                        }
                    }, this);
                    if (!c) {
                        checkChange.call(this, pui);
                    }
                }
                if (Ext.isDefined(parentNode.parentNode)) {
                    loopParentChecked.call(this, parentNode.parentNode);
                }
            };
            loopParentChecked.call(this, node.parentNode);
        }
        
        if (this.cascadeChild) {
            var loopChildChecked = function(childNodes) {
                if (childNodes.length === 0) {
                    return;
                }
                Ext.each(childNodes, function(n) {
                    var nui = n.getUI();
                    if (Ext.isDefined(nui.checkbox)) {
                        checkChange(nui);
                        loopChildChecked.call(this, n.childNodes);
                    } else {
                        if (this.linkedCheck) {
                            loopChildChecked.call(this, n.childNodes);
                        }
                    }
                }, this);
            };
            loopChildChecked.call(this, node.childNodes);
        }
    },
    
    // private 
    onExpandNode: function(node) {
        if (node.asyncChecked !== true) {
            node.asyncChecked = true;
            var ui = node.getUI();
            if (Ext.isDefined(ui.checkbox)) {
                if (ui.checkbox.checked) {
                    Ext.each(node.childNodes, function(n) {
                        this.checkNode(n, true);
                    }, this);
                }
            }
        }
    },
    
    /**
     * 选中节点
     * @param {Object} node  节点ID/TreeNode/Array
     * @param {Boolean} checked  选中状态
     * @return void
     */
    checkNode: function(node, checked) {
        if (Ext.isArray(node)) {
            Ext.each(node, function(n) {
                if (Ext.isString(n)) {
                    n = this.getNodeById(n);
                }
                n.getUI().toggleCheck(checked);
            }, this);
        } else {
            if (Ext.isString(node)) {
                node = this.getNodeById(node);
            }
            node.getUI().toggleCheck(checked);
        }
    },
    
    /**
     * 获取被选中的树叶节点
     * @param {String} p  属性类别
     * @return Array/String
     */
    getLeafChecked: function(p, startNode) {
        var leafNodes = [], selNodes = this.getChecked(undefined, startNode);
        Ext.each(selNodes, function(node) {
            if (node.isLeaf()) {
                leafNodes.push(node);
            }
        });
        if (!Ext.isDefined(p)) {
            return leafNodes;
        }
        var ret = '';
        Ext.each(leafNodes, function(node) {
            if (ret.length > 0) {
                ret += ',';
            }
            ret += (p == 'id' ? node.id : node.attributes[p]);
        });
        return ret;
    }
});
