/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditorNodeUI = Ext.extend(Ext.ux.tree.TreeGridNodeUI, {
    renderElements: function(n, a, targetNode, bulkRender) {
        var t = n.getOwnerTree(), cols = t.columns, c = cols[0], i, buf, len;
        
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';
        
        var cVal = a[c.dataIndex] || c.text, fVal, nAttr = Ext.apply({}, c.attributes);
        var filterValue = function(c, v) {
            if (v) {
                v = Ext.util.Format.htmlEncode(v).replace(/ /g, "&nbsp;");
            }
            return v;
        };
        fVal = filterValue(c, cVal), nAttr[c.dataIndex] = fVal;
        
        var cb = false;
        if (n.getEditMode() === Ext.ux.tree.TreeGridEditor.Obar.Api.add) {
            a.checked = false;
        }
        if (t.checkbox) {
            cb = Ext.isBoolean(a.checked);
        }
        
        var tip = function(c, cVal, fVal) {
            var t;
            if (c.displayTip && cVal && fVal) {
                t = Ext.util.Format.htmlEncode(fVal).replace(/\"/g, "&quot;");
                if (Ext.isDefined(c.tipText)) {
                    if (Ext.isString(c.tipText)) {
                        t = c.tipText;
                    } else if (Ext.isFunction(c.tipText)) {
                        t = c.tipText(cVal);
                    }
                }
            }
            return t;
        };
        var cTip = tip(c, cVal, fVal);
        
        buf = ['<tbody class="x-tree-node">', '<tr ext:tree-node-id="', n.id, '" class="x-tree-node-el x-tree-node-leaf ', a.cls, '">', '<td class="x-treegrid-col ', (c.cls ? c.cls : ''), '">', '<span class="x-tree-node-indent" style="float:left;">', this.indentMarkup, "</span>", '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" style="float:left;">', '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon', (a.icon ? " x-tree-node-inline-icon" : ""), (a.iconCls ? " " + a.iconCls : ""), '" style="float:left;" unselectable="on">', (t.checkbox ? (cb ? ('<input id="' + t.id + '-node-cb" name="' + t.id + '-node-cb" class="x-tree-node-cb x-treegrideditor-node-cb" type="' +
        (t.checkMode === 'single' ? 'radio' : 'checkbox') +
        '"' +
        (a.checked ? ' checked="checked"' : '') +
        ' value="' +
        n.id +
        '"' +
        ' style="float:left;" />') : '') : ''), '<a hidefocus="on" class="x-tree-node-anchor" href="', a.href ? a.href : '#', '" tabIndex="1" ', a.hrefTarget ? ' target="' + a.hrefTarget + '"' : '', '>', '<span id="', t.id, '-node-if-', c.dataIndex, '-', n.id, '" unselectable="on"', (c.displayTip && cTip ? ' qtip="' + cTip + '"' : ''), '>', (c.tpl ? c.tpl.apply(nAttr) : fVal), '</span>', '</a>', '</td>'];
        
        for (i = 1, len = cols.length; i < len; i++) {
            c = cols[i], cVal = a[c.dataIndex];
            nAttr = Ext.apply({}, c.attributes);
            fVal = filterValue(c, cVal), nAttr[c.dataIndex] = fVal;
            cTip = tip(c, cVal, fVal);
            buf.push('<td class="x-treegrid-col ', (c.cls ? c.cls : ''), '">', '<div id="', t.id, '-node-if-', c.dataIndex, '-', n.id, '" unselectable="on" class="x-treegrid-text"', (c.align ? ' style="text-align: ' + c.align + ';"' : ''), (c.displayTip && cTip ? ' qtip="' + cTip + '"' : ''), '>', (c.tpl ? c.tpl.apply(nAttr) : fVal), '</div>', '</td>');
        }
        
        buf.push('</tr><tr class="x-tree-node-ct"><td colspan="', cols.length, '">', '<table class="x-treegrid-node-ct-table" cellpadding="0" cellspacing="0" style="table-layout: fixed; display: none; width: ', t.innerCt.getWidth(), 'px;"><colgroup>');
        for (i = 0, len = cols.length; i < len; i++) {
            buf.push('<col style="width: ', (cols[i].hidden ? 0 : cols[i].width) + 'px;"', ' />');
        }
        buf.push('</colgroup></table></td></tr></tbody>');
        
        if (bulkRender !== true && n.nextSibling && n.nextSibling.ui.getEl()) {
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", n.nextSibling.ui.getEl(), buf.join(''));
        } else {
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf.join(''));
        }
        
        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1].firstChild.firstChild;
        var cs = this.elNode.firstChild.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
        this.iconNode = cs[2];
        var index = 3;
        if (t.checkbox && cb) {
            this.checkbox = cs[3];
            // fix for IE6
            this.checkbox.defaultChecked = this.checkbox.checked;
            index++;
        }
        this.anchor = cs[index];
        this.textNode = cs[index].firstChild;
        
        t.activeObar(n);
    },
    
    // private
    updateExpandIcon: function() {
        if (this.rendered) {
            var n = this.node, c1, c2, cls = n.isLast() ? "x-tree-elbow-end" : "x-tree-elbow", hasChild = n.hasChildNodes();
            if (hasChild || n.attributes.expandable) {
                if (n.expanded) {
                    cls += "-minus";
                    c1 = "x-tree-node-collapsed";
                    c2 = "x-tree-node-expanded";
                } else {
                    cls += "-plus";
                    c1 = "x-tree-node-expanded";
                    c2 = "x-tree-node-collapsed";
                }
                if (this.wasLeaf) {
                    this.removeClass("x-tree-node-leaf");
                    this.wasLeaf = false;
                }
                if (this.c1 != c1 || this.c2 != c2) {
                    Ext.fly(this.elNode).replaceClass(c1, c2);
                    this.c1 = c1;
                    this.c2 = c2;
                }
            } else {
                if (!this.wasLeaf) {
                    Ext.fly(this.elNode).removeClass("x-tree-node-expanded");
                    Ext.fly(this.elNode).addClass('x-tree-node-leaf');
                    delete this.c1;
                    delete this.c2;
                    this.wasLeaf = true;
                }
            }
            var ecc = "x-tree-ec-icon " + cls;
            if (this.ecc != ecc) {
                this.ecNode.className = ecc;
                this.ecc = ecc;
            }
        }
    },
    
    // private
    onSelectedChange: function(state) {
        if (state) {
            //this.focus();
            this.addClass("x-tree-selected");
        } else {
            //this.blur();
            this.removeClass("x-tree-selected");
        }
    },
    
    
    // private
    onClick: function(e) {
        if (this.interceptEvent) {
            e.stopEvent();
            this.interceptEvent = false;
            return;
        }
        if (this.dropping) {
            e.stopEvent();
            return;
        }
        if (this.fireEvent("beforeclick", this.node, e) !== false) {
            var a = e.getTarget('a');
            if (!this.disabled && this.node.attributes.href && a) {
                this.fireEvent("click", this.node, e);
                return;
            } else if (a && e.ctrlKey) {
                e.stopEvent();
            }
            
            if (a) {
                if (!a.getAttribute('link')) {
                    return;
                }
            } else {
                e.preventDefault();
            }
            
            if (this.disabled) {
                return;
            }
            
            if (this.node.attributes.singleClickExpand && !this.animating && this.node.isExpandable()) {
                this.node.toggle();
            }
            
            this.fireEvent("click", this.node, e);
        } else {
            e.stopEvent();
        }
    },
    
    // private
    onDblClick: function(e) {
        e.preventDefault();
        return;
    },
    
    // private
    onOver: function(e) {
        Ext.ux.tree.TreeGridEditorNodeUI.superclass.onOver.call(this, e);
        this.node.getOwnerTree().onOverShowObar(this.node);
    },
    
    // private
    onOut: function(e) {
        Ext.ux.tree.TreeGridEditorNodeUI.superclass.onOut.call(this, e);
        this.node.getOwnerTree().onOutShowObar(this.node);
    }
});
