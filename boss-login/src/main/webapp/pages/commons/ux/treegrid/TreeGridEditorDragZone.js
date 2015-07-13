/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditorDragZone = Ext.extend(Ext.tree.TreeDragZone, {
    // private
    onBeforeDrag: function(data, e) {
        var n = data.node;
        n.getOwnerTree().outShowObar(n);
        return Ext.ux.tree.TreeGridEditorDragZone.superclass.onBeforeDrag.apply(this, arguments);
    },
    
    // private
    onEndDrag: function(data, e) {
        var n = data.node, t = n.getOwnerTree();
        if (!t.mouseoverShowObar || n.getEditMode()) {
            t.overShowObar(n);
        }
        return Ext.ux.tree.TreeGridEditorDragZone.superclass.onEndDrag.apply(this, arguments);
    }
});
