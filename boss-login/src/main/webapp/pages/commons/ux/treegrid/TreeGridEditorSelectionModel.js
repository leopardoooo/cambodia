/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.2.2 (5/3/2010)
 */
Ext.ux.tree.TreeGridEditorSelectionModel = Ext.extend(Ext.tree.DefaultSelectionModel, {
    init: function(tree) {
        this.tree = tree;
        //tree.mon(tree.getTreeEl(), 'keydown', this.onKeyDown, this);
        tree.on('click', this.onNodeClick, this);
    }
});
