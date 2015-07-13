/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditorNodeWriter = function(tree) {
    this.tree = tree;
};

Ext.ux.tree.TreeGridEditorNodeWriter.prototype = {
    write: Ext.emptyFn
};

Ext.ux.tree.TreeGridEditorNodeJsonWriter = Ext.extend(Ext.ux.tree.TreeGridEditorNodeWriter, {
    write: function(data) {
        return Ext.encode(data);
    }
});

Ext.ux.tree.TreeGridEditorNodeXmlWriter = function(tree) {
    Ext.ux.tree.TreeGridEditorNodeXmlWriter.superclass.constructor.call(this, tree);
    // compile the XTemplate for rendering XML documents.
    this.tpl = (typeof(this.tpl) === 'string') ? new Ext.XTemplate(this.tpl, {
        writeNode: this.writeNode
    }) : this.tpl;
};

Ext.extend(Ext.ux.tree.TreeGridEditorNodeXmlWriter, Ext.ux.tree.TreeGridEditorNodeWriter, {
    documentRoot: 'nodes',
    
    xmlVersion: '1.0',
    
    xmlEncoding: 'UTF-8',
    
    tpl: '\<\u003fxml version="{version}" encoding="{encoding}"\u003f>\n<tpl if="documentRoot"><{documentRoot}>\n<tpl for="data">{[this.writeNode(values)]}</tpl></{documentRoot}></tpl>',
    
    write: function(data) {
        return this.tpl.applyTemplate({
            "documentRoot": this.documentRoot,
            "version": this.xmlVersion,
            "encoding": this.xmlEncoding,
            "data": data
        });
    },
    
    writeNode: function(n) {
        var d = [];
        d.push('<node id="', n.id, '">\n');
        for (var p in n) {
            if (p == 'id') {
                continue;
            } else if (p == 'children') {
                if (n[p].length > 0) {
                    d.push('<', p, '>\n');
                    for (var i = 0; i < n[p].length; i++) {
                        d.push(this.writeNode(n[p][i]));
                    }
                    d.push('</', p, '>\n');
                }
            } else {
                d.push('<', p, '>', n[p], '</', p, '>\n');
            }
        }
        d.push('</node>\n');
        return d.join('');
    }
});
