/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditorLoader = Ext.extend(Ext.tree.TreeLoader, {
    processResponse: function(response, node, callback, scope) {
        var json = response.responseText;
        try {
            var o, nodeReader = node.getOwnerTree().nodeReader;
            o = response.responseData || nodeReader.read(json);
            
            node.beginUpdate();
            for (var i = 0, len = o.length; i < len; i++) {
                var n = this.createNode(o[i]);
                if (n) {
                    node.appendChild(n);
                }
            }
            node.endUpdate();
            this.runCallback(callback, scope || node, [node]);
        } catch (e) {
            this.handleFailure(response);
        }
    },
    
    createNode: function(attr) {
        if (!attr.uiProvider) {
            attr.uiProvider = Ext.ux.tree.TreeGridEditorNodeUI;
        }
        
        // apply baseAttrs, nice idea Corey!
        if (this.baseAttrs) {
            Ext.applyIf(attr, this.baseAttrs);
        }
        if (this.applyLoader !== false && !attr.loader) {
            attr.loader = this;
        }
        if (Ext.isString(attr.uiProvider)) {
            attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
        }
        if (attr.nodeType) {
            return new Ext.ux.tree.TreeGridEditor.nodeTypes[attr.nodeType](attr);
        } else {
            return attr.leaf ? new Ext.ux.tree.TreeGridEditorNode(attr) : new Ext.ux.tree.TreeGridEditorAsyncNode(attr);
        }
    }
});
