/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.2.2 (5/3/2010)
 */
Ext.override(Ext.tree.TreeEventModel, {
    delegateClick: function(e, t) {
        if (this.beforeEvent(e)) {
            if (e.getTarget('input[type=checkbox]', 1) || e.getTarget('input[type=radio]', 1)) {
                this.onCheckboxClick(e, this.getNode(e));
            } else if (e.getTarget('.x-tree-ec-icon', 1)) {
                this.onIconClick(e, this.getNode(e));
            } else if (this.getNodeTarget(e)) {
                this.onNodeClick(e, this.getNode(e));
            } else {
                this.onContainerEvent(e, 'click');
            }
        }
    },
    
    onNodeClick: function(e, node) {
        if (Ext.isObject(node)) {
            node.ui.onClick(e);
        }
    }
});
