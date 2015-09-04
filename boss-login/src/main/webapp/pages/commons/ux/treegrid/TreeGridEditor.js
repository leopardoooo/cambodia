/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditor = Ext.extend(Ext.ux.tree.TreeGrid, {
    /**
     * @cfg {String} mode 数据处理模式，取值范围'remote' 'local'，如果mode='remote'，树节点保存时将提交到服务端处理，默认'remote'
     */
    mode: 'remote',
    
    /**
     * @cfg {String} localData 静态数据，当mode='local'时有效
     */
    /**
     * @cfg {String} readMode 树节点数据读取模式，取值范围'json' 'xml'，默认'json'
     */
    readMode: 'json',
    /**
     * @cfg {String} writeMode 树节点数据输出模式，取值范围'json' 'xml'，默认'json'
     */
    writeMode: 'json',
    
    // 展开动画
    animate: false,
    
    // 列菜单
    enableHdMenu: false,
    
    // 列宽度调整
    columnResize: false,
    
    // 默认true立即设置列表滚动条，false延迟10毫秒设置
    reserveScrollOffset: true,
    
    // 排序
    enableSort: false,
    
    // vista样式
    useArrows: false,
    
    // 禁用样式 
    unstyled: false,
    
    // 最大深度（层级）
    maxDepth: 0,
    
    // 超出最大深度，提示信息
    maxDepthText: 'Maximum node depth.',
    
    // 根节点ID
    rootNodeId: 'tge-root',
    
    // 复选框
    checkbox: false,
    
    // 取值范围: 'multiple' 'single'
    checkMode: 'multiple',
    
    // mouseover事件触发显示Obar
    mouseoverShowObar: false,
    
    // 只允许同时编辑一条记录
    singleEdit: false,
    
    obarHeaderText: 'Manage',
    
    obarBtnText: {
        add: '新增下级',
        synch:'同步',
        leveladd:'新增平级',
        batchAdd: '批量新增下级',
        edit: '修改',
        remove: '删除',
        statusInvalid: '禁用',
        statusActive: '启用',
        save: 'Save',
        cancel: 'Cancel'
    },
    
    enableDD: false,
    
    enableDrag: false,
    
    enableDrop: false,
    
    // private
    initComponent: function() {
        this.autoScroll = false, this.lines = false;
        this.rootVisible = false;
        
        if (this.checkMode === 'single') {
            this.cascadeCheck = false;
        }
        
        if (!this.obarCfg) {
            this.obarCfg = {
                column: ccfg,
                btns: [{
                    id: Ext.ux.tree.TreeGridEditor.Obar.Api.add,
                    disabled: false,
                    text: this.obarBtnText.add
                }, {
                    id: Ext.ux.tree.TreeGridEditor.Obar.Api.edit,
                    disabled: false,
                    text: this.obarBtnText.edit
                }, {
                    id: Ext.ux.tree.TreeGridEditor.Obar.Api.remove,
                    disabled: false,
                    text: this.obarBtnText.remove
                }, {
                    id: Ext.ux.tree.TreeGridEditor.Obar.Api.status,
                    disabled: false,
                    text: this.obarBtnText.status
                }],
                listeners: {}
            };
        } else {
            this.obarCfg.column = this.obarCfg.column || ccfg;
            this.obarCfg.btns = this.obarCfg.btns || [];
            this.obarCfg.listeners = this.obarCfg.listeners ||
            {};
        }
        
        if (!this.obar) {
            this.obar = new Ext.ux.tree.TreeGridEditor.Obar(this, {
                listeners: this.obarCfg.listeners
            });
        }
        if (!this.obar.tge) {
            this.obar.setTge(this);
        }
        
        if (this.mode == 'local') {
            this.root = new Ext.ux.tree.TreeGridEditorNode({
                id: this.rootNodeId,
                text: 'TreeGridEditor Root'
            });
        } else {
            this.root = new Ext.ux.tree.TreeGridEditorAsyncNode({
                id: this.rootNodeId,
                text: 'TreeGridEditor Root'
            });
        }
        
        // initialize the loader
        var l = this.loader;
        if (!l) {
            l = new Ext.ux.tree.TreeGridEditorLoader({
                dataUrl: this.dataUrl,
                requestMethod: this.requestMethod
            });
        } else if (Ext.isObject(l) && !l.load) {
            l = new Ext.ux.tree.TreeGridEditorLoader(l);
        } else if (l) {
            l.processResponse = function(response, node, callback, scope) {
                return Ext.ux.tree.TreeGridEditorLoader.prototype.processResponse.apply(this, arguments);
            };
            l.createNode = function(attr) {
                return Ext.ux.tree.TreeGridEditorLoader.prototype.createNode.call(this, attr);
            };
        }
        this.loader = l;
        
        /**
         * @property nodeReader
         * @type Ext.ux.tree.TreeGridEditorNodeReader
         * 节点上下文加载
         */
        this.nodeReader = (this.readMode == 'xml' ? new Ext.ux.tree.TreeGridEditorNodeXmlReader(this) : new Ext.ux.tree.TreeGridEditorNodeJsonReader(this))
        
        /**
         * @property nodeReader
         * @type Ext.ux.tree.TreeGridEditorNodeWriter
         * 节点上下文输出
         */
        this.nodeWriter = (this.writeMode == 'xml' ? new Ext.ux.tree.TreeGridEditorNodeXmlWriter(this) : new Ext.ux.tree.TreeGridEditorNodeJsonWriter(this))
        
        Ext.ux.tree.TreeGrid.superclass.initComponent.call(this);
        
        this.initColumns();
        
        if (this.enableSort) {
            this.treeGridSorter = new Ext.ux.tree.TreeGridSorter(this, this.enableSort);
        }
        
        if (this.columnResize) {
            this.colResizer = new Ext.tree.ColumnResizer(this.columnResize);
            this.colResizer.init(this);
        }
        
        var c = this.columns;
        if (!this.internalTpl) {
            this.internalTpl = new Ext.XTemplate('<div class="x-grid3-header">', '<div class="x-treegrid-header-inner">', '<div class="x-grid3-header-offset">', '<table cellspacing="0" cellpadding="0" border="0"><colgroup><tpl for="columns"><col /></tpl></colgroup>', '<thead><tr class="x-grid3-hd-row">', '<tpl for="columns">', '<td class="x-grid3-hd x-grid3-cell x-treegrid-hd" style="text-align: {align};" id="', this.id, '-xlhd-{#}">', '<div class="x-grid3-hd-inner x-treegrid-hd-inner" unselectable="on">', this.enableHdMenu ? '<a class="x-grid3-hd-btn" href="#"></a>' : '', '{header}<img class="x-grid3-sort-icon" src="', Ext.BLANK_IMAGE_URL, '" />', '</div>', '</td></tpl>', '</tr></thead>', '</div></table>', '</div></div>', '</div>', '<div class="x-treegrid-root-node">', '<table class="x-treegrid-root-table" cellpadding="0" cellspacing="0" style="table-layout: fixed;"></table>', '</div>');
        }
        
        if (!this.colgroupTpl) {
            if (Ext.isIE6) { // 兼容IE6
                this.colgroupTpl = new Ext.XTemplate('<tr style="display:none;"><tpl for="columns"><td style="width: {width}px"/></tpl></tr>');
            } else {
                this.colgroupTpl = new Ext.XTemplate('<colgroup><tpl for="columns"><col style="width: {width}px"/></tpl></colgroup>');
            }
        }
        
        /*
         * validActions，保证Request的唯一性
         * activeNode，当前编辑的Node，singleEdit==true时使用
         */
        this.validActions = {}, this.activeNode = false;
        
        this.selModel = new Ext.ux.tree.TreeGridEditorSelectionModel(this);
    },
    
    // private
    loadLocalData: function() {
        if (this.mode == 'local' && this.localData && this.nodeReader) {
            this.nodeReader.load(this.localData);
        }
    },
    
    /**
     * 重新加载本地数据
     */
    reloadLocalData: function() {
        if (this.mode == 'local') {
            this.getRootNode().removeAll();
            this.loadLocalData();
        }
    },
    
    // private
    initEvents: function() {
        Ext.ux.tree.TreeGridEditor.superclass.initEvents.call(this);
        
        if (this.enableDD || this.enableDrag) {
            this.dragZone = new Ext.ux.tree.TreeGridEditorDragZone(this, this.dragConfig ||
            {
                ddGroup: this.ddGroup || 'TreeDD',
                scroll: this.ddScroll
            });
        }
    },
    
    // private
    afterRender: function() {
        Ext.ux.tree.TreeGridEditor.superclass.afterRender.apply(this, arguments);
        
        if (this.mode == 'local' && this.localData) {
            this.loadLocalData();
        }
        
        var bd = this.innerBody.dom;
        var hd = this.innerHd.dom;
        
        if (!bd) {
            return;
        }
        
        bd.style.height = this.body.getHeight(true) - hd.offsetHeight + 'px';
        
        var sw = Ext.num(this.scrollOffset, Ext.getScrollBarWidth());
        if (this.reserveScrollOffset || ((bd.offsetWidth - bd.clientWidth) > 10)) {
            this.setScrollOffset(sw);
        } else {
            var me = this;
            setTimeout(function() {
                me.setScrollOffset(bd.offsetWidth - bd.clientWidth > 10 ? sw : 0);
            }, 10);
        }
    },
    
    // private
    onDestroy: function() {
        Ext.destroy(this.obar);
        this.obar = this.validActions = null;
        
        if (this.nodeReader) {
            Ext.destroy(this.nodeReader);
            this.nodeReader = null;
        }
        
        if (this.nodeWriter) {
            Ext.destroy(this.nodeWriter);
            this.nodeWriter = null;
        }
        
        Ext.ux.tree.TreeGridEditor.superclass.onDestroy.call(this);
    },
    
    // private
    registerNode: function(n) {
        Ext.ux.tree.TreeGridEditor.superclass.registerNode.call(this, n);
        if (!n.uiProvider && !n.isRoot && !n.ui.isTreeGridNodeUI) {
            n.ui = new Ext.ux.tree.TreeGridEditorNodeUI(n);
        }
    },
    
    // private
    initColumns: function() {
        var cols = this.columns, columns = [], col;
        for (var i = 0, len = cols.length; i < len; i++) {
            col = cols[i];
            col.width = col.width || '';
            col.cls = col.cls || '';
            col.cls = 'x-treegrideditor-inputfield ' + col.cls;
            col.displayTip = col.displayTip || false;
            col.inputCfg = col.inputCfg ||
            {};
            col.inputCfg.inputType = col.inputCfg.inputType || 'text';
            col.inputCfg.inputType = col.inputCfg.inputType.toLowerCase();
            col.inputCfg.defaultValue = col.inputCfg.defaultValue || '';
            columns.push(col);
        }
        this.columns = columns;
        
        Ext.ux.tree.TreeGridEditor.superclass.initColumns.apply(this, arguments);
        
        this.colTpls = [];
        for (var i = 0, len = columns.length; i < len; i++) {
            col = columns[i];
            this.colTpls.push(col.tpl);
        }
        
        var self = this, c = {
            xtype: 'tgcolumn'
        }, ccfg = {
            header: this.obarHeaderText,
            dataIndex: 'id',
            width: 200
        };
        
        this.obar.btns = [];
        var btns = this.obarCfg.btns;
        for (var i = 0, len = btns.length; i < len; i++) {
            if (btns[i].id === Ext.ux.tree.TreeGridEditor.Obar.Api.update || btns[i].id === Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
                continue;
            }
            
            
            var req = btns[i].request || false;
            if (req) {
                req.method = req.method ? req.method.toUpperCase() : 'POST';
            }
            this.obar.btns[btns[i].id] = {
                disabled: btns[i].disabled || false,
                cls: btns[i].cls || '',
                text: btns[i].text || this.obarBtnText[btns[i].id],
                deepest: btns[i].deepest || 'normal',
                request: req,
                validator: btns[i].validator ||
                function() {
                    return true;
                }
            }
        }
        
        c.obarTag = true;
        c.header = this.obarCfg.column.header || this.defaultObarHeaderText;
        c.dataIndex = this.obarCfg.column.dataIndex || 'id';
        c.width = this.obarCfg.column.width || 200;
        c.autoWidth = this.obarCfg.column.autoWidth || false;
        c.align = this.obarCfg.column.align || 'left';
        c.cls = this.obarCfg.column.cls || '';
        c.tpl = new Ext.XTemplate('{' + c.dataIndex + ':this.obarHtml}', {
            obarHtml: function(v) {
                return self.obarHtml.call(self, v)
            }
        });
        c = Ext.create(c);
        c.init(this);
        this.columns.push(c);
    },
    
    // private
    onResize: function(w, h) {
        w = w || this.getWidth();
        w = (w == 'auto' ? this.getWidth() : w);
        // fix for firefox
        var ua = navigator.userAgent.toLowerCase();
        w = /firefox/.test(ua) ? w - 17 : w;
        h = h || this.getHeight();
        var tc = tw = 0;
        var cols = this.columns;
        Ext.each(cols, function(col, i, cols) {
            if (col.autoWidth) {
                tc++;
            } else {
                tw += col.width;
            }
        }, this);
        if (tc > 0) {
            var av = (w - tw - 20) / tc;
            av = av < 100 ? 100 : av;
            Ext.each(cols, function(col, i, cols) {
                if (col.autoWidth) {
                    col.width = av;
                }
            }, this);
        }
        
        Ext.ux.tree.TreeGridEditor.superclass.onResize.apply(this, arguments);
        
        var colEls = this.el.select('table > colgroup');
        colEls.each(function(c, colEls, i) {
            var t = Ext.get(c.dom.parentNode);
            t.setWidth(w - 20);
            c.remove();
            this.colgroupTpl.insertFirst(t, {
                columns: cols
            });
        }, this);
    },
    
    // private 
    getChecked: function(a, startNode) {
        if (this.checkMode === 'single') {
            var n, c = document.getElementsByName(this.id + '-node-cb');
            for (var i = 0; i < c.length; i++) {
                if (c[i].checked) {
                    n = c[i].value;
                    break;
                }
            }
            if (n) {
                n = this.getNodeById(n);
                return !a ? n : (a == 'id' ? n.id : n.attributes[a]);
            }
            return null;
        } else {
            return Ext.ux.tree.TreeGridEditor.superclass.getChecked.apply(this, arguments);
        }
    },
    
    // private
    registerInputField: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var cols = this.columns, col;
        for (var i = 0, len = cols.length - 1; i < len; i++) {
            col = cols[i];
            n.registerInputField(col.dataIndex, col.inputCfg);
        }
        var textNode = Ext.get(n.ui.textNode);
        textNode.setStyle({
            "float": "left"
        });
        textNode.insertBefore(Ext.fly(n.ui.anchor));
        return n.getInputFields();
    },
    
    // private
    unregisterInputField: function(n) {
        n.destroyInputField();
        var textNode = Ext.get(n.ui.textNode);
        textNode.setStyle({
            "float": "none"
        });
        Ext.fly(n.ui.anchor).appendChild(textNode);
    },
    
    // private
    removeColTpl: function() {
        var cols = this.columns, tpls = this.colTpls;
        for (var i = 0, len = tpls.length; i < len; i++) {
            cols[i].tpl = null;
        }
    },
    
    // private
    restoreColTpl: function() {
        var cols = this.columns, tpls = this.colTpls;
        for (var i = 0, len = tpls.length; i < len; i++) {
            cols[i].tpl = tpls[i];
        }
    },
    
    // private
    obarHtml: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var r = [];
        r.push('<div', ' style="float:left;', this.mouseoverShowObar && !n.getEditMode() ? ' display:none;' : '', '" class="x-treegrideditor-obar" unselectable="on">');
        if (n.getEditMode()) {
            r.push(this.saveBtnsHtml(n));
        } else {
            r.push(this.editBtnsHtml(n));
        }
        r.push('</div>', Ext.isIE ? '&nbsp;' : '');
        return r.join('');
    },
    
    // private 
    overShowObar: function(n) {
        var elNode = n.getUI().elNode;
        if (elNode) {
            Ext.fly(elNode).child('.x-treegrideditor-obar').setDisplayed(true);
        }
    },
    
    // private 
    onOverShowObar: function(n) {
        if (!this.mouseoverShowObar || n.getEditMode()) {
            return;
        }
        this.overShowObar(n);
    },
    
    // private 
    outShowObar: function(n) {
        var elNode = n.getUI().elNode;
        if (elNode) {
            Ext.fly(elNode).child('.x-treegrideditor-obar').setDisplayed(false);
        }
    },
    
    // private 
    onOutShowObar: function(n) {
        if (!this.mouseoverShowObar || n.getEditMode()) {
            return;
        }
        this.outShowObar(n);
    },
    
    // private
    editBtnsHtml: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var deepest = this.maxDepth > 0 && n.getDepth() >= this.maxDepth;
        var r = [], btns = this.obarCfg.btns, btn, m, mCapitalize, disableObar, hideObar;
        
        for (var i = 0, len = btns.length; i < len; i++) {
            btn = btns[i];
            if (btn.disabled) {
                continue;
            }
            
            if (deepest && btn.deepestState === 'uncreated') {
                continue;
            }
            
            m = btn.id;
            mCapitalize = Ext.util.Format.capitalize(m);
            disableObar = n.attributes['disableObar' + mCapitalize];
            hideObar = n.attributes['hideObar' + mCapitalize];
            
            r.push('<span id="', this.getBtnId(n, m), '" unselectable="on"');
            if (deepest) {
                if (btn.deepestState === 'hidden' || hideObar) {
                    r.push(' style="display: none;"');
                }
            } else {
                r.push(hideObar ? ' style="display: none;"' : '');
            }
            r.push('>');
            r.push('<a');
            if (deepest) {
                if (btn.deepestState === 'disabled' || disableObar) {
                    r.push(' disabled="true" style="color: #ACA899;"');
                }
            } else {
                r.push(disableObar ? ' disabled="true" style="color: #ACA899;"' : '');
            }
            r.push(' hidefocus="on" href="javascript:false;" class="x-treegrideditor-obar-', m);
            if (deepest) {
                if (btn.deepestState === 'disabled' || disableObar) {
                    r.push(' x-treegrideditor-obar-' + m + '-gray');
                }
            } else {
                r.push(disableObar ? ' x-treegrideditor-obar-' + m + '-gray' : '');
            }
            r.push(Ext.isEmpty(btn.cls) ? '' : ' ' + btn.cls, '" onclick="Ext.getCmp(\'', this.id, '\').callObarMethod(\'', n.id, '\',\'', m, '\');return false;">', this.obar.btns[m].text, '</a>');
            r.push('</span>');
        }
        return r.join('');
    },
    
    // private
    saveBtnsHtml: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var r = [];
        r.push('<span id="', this.getBtnId(n, Ext.ux.tree.TreeGridEditor.Obar.Api.update), '" unselectable="on">');
        r.push('<a hidefocus="on" href="#" class="x-treegrideditor-obar-yes', '" onclick="Ext.getCmp(\'', this.id, '\').updateNode(\'', n.id, '\');return false;">', this.obarBtnText.save, '</a>');
        r.push('</span>');
        r.push('<span id="', this.getBtnId(n, Ext.ux.tree.TreeGridEditor.Obar.Api.cancel), '" unselectable="on">');
        r.push('<a hidefocus="on" href="#" class="x-treegrideditor-obar-no', '" onclick="Ext.getCmp(\'', this.id, '\').cancelEdit(\'', n.id, '\');return false;">', this.obarBtnText.cancel, '</a>');
        r.push('</span>');
        return r.join('');
    },
    
    // private
    getBtnId: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        return this.id + '-' + n.id + '-obar-' + m;
    },
    
    /**
     * 调用Obar函数
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    callObarMethod: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.attributes['disableObar' + Ext.util.Format.capitalize(m)]) {
            return;
        }
        this.obar[m](n);
    },
    
    /**
     * 激活obar功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    enableObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (m == Ext.ux.tree.TreeGridEditor.Obar.Api.add && this.maxDepth > 0 && n.getDepth() >= this.maxDepth) {
            return;
        }
        
        if (m != Ext.ux.tree.TreeGridEditor.Obar.Api.update && m != Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
            n.attributes['disableObar' + Ext.util.Format.capitalize(m)] = false;
        }
        var b = Ext.get(this.getBtnId(n, m)).child('a');
        b.set({
            disabled: false
        });
        b.setStyle({
            color: ''
        });
        b.removeClass('x-treegrideditor-obar-' + m + '-gray');
        b.focus();
        n.select();
    },
    
    /**
     * 禁用obar功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    disableObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (m == Ext.ux.tree.TreeGridEditor.Obar.Api.add && this.maxDepth > 0 && n.getDepth() >= this.maxDepth) {
            return;
        }
        
        if (m != Ext.ux.tree.TreeGridEditor.Obar.Api.update && m != Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
            n.attributes['disableObar' + Ext.util.Format.capitalize(m)] = true;
        }
        var b = Ext.get(this.getBtnId(n, m)).child('a');
        b.focus();
        b.set({
            disabled: true
        });
        b.setStyle({
            color: '#ACA899'
        });
        b.addClass('x-treegrideditor-obar-' + m + '-gray');
        n.select();
    },
    
    /**
     * 自适应启用/禁用obar功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    toggleDisableObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.attributes['disableObar' + Ext.util.Format.capitalize(m)]) {
            this.enableObar(n, m);
        } else {
            this.disableObar(n, m);
        }
    },
    
    /**
     * 显示obar功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    showObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (m != Ext.ux.tree.TreeGridEditor.Obar.Api.update && m != Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
            n.attributes['hideObar' + Ext.util.Format.capitalize(m)] = false;
        }
        var b = Ext.get(this.getBtnId(n, m));
        b.setDisplayed(true);
        b.child('a').focus();
        n.select();
    },
    
    /**
     * 隐藏obar功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    hideObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (m != Ext.ux.tree.TreeGridEditor.Obar.Api.update && m != Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
            n.attributes['hideObar' + Ext.util.Format.capitalize(m)] = true;
        }
        var b = Ext.get(this.getBtnId(n, m));
        if(b){
        	b.child('a').focus();
        	b.setDisplayed(false);
        }
        n.select();
    },
    
    /**
     * 自适应显示/隐藏obar个功能按钮
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} m  obar方法id
     * @return void
     */
    toggleHideObar: function(n, m) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.attributes['hideObar' + Ext.util.Format.capitalize(m)]) {
            this.showObar(n, m);
        } else {
            this.hideObar(n, m);
        }
    },
    
    // private
    activeObar: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var deepest = this.maxDepth > 0 && n.getDepth() >= this.maxDepth;
        if (!n.getEditMode() && deepest) {
            var btns = this.obarCfg.btns, btn;
            for (var i = 0, len = btns.length; i < len; i++) {
                btn = btns[i];
                if (btn.deepestState === 'disabled') {
                    this.disableObar(n, btn.id);
                } else if (btn.deepestState === 'hidden') {
                    this.hideObar(n, btn.id);
                }
            }
        }
    },
    
    /**
     * 生成节点ID，新增下级树节点时调用
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return String
     */
    makeNodeId: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (!this.nextNewNodeNumber) {
            this.nextNewNodeNumber = 0;
        }
        this.nextNewNodeNumber++;
        return n.id + '_n_' + this.nextNewNodeNumber;
    },
    
    // private
    beginEdit: function(n, cb, args) {
        cb = cb || Ext.emptyFn, args = args || [], args.unshift(n);
        
        if (this.singleEdit && this.activeNode) {
            var handler = function(btn, text) {
                if (btn === 'yes') {
                    if (this.activeNode.validateInput()) {
                        this.updateNode(this.activeNode, function(activeNode, n, cb, args) {
                            this.outShowObar(activeNode);
                            this.activeNode = n;
                            this.overShowObar(n);
                            cb.apply(this, args);
                        }, [n, cb, args]);
                    }
                } else if (btn === 'no') {
                    this.outShowObar(this.activeNode);
                    this.cancelEdit(this.activeNode);
                    this.activeNode = n;
                    this.overShowObar(n);
                    cb.apply(this, args);
                } else {
                    // ignore
                }
            };
            
            Ext.Msg.show({
                title: this.singleEditPrompt || 'Save Changes?',
                msg: this.singleEditMsg || 'Would you like to save your changes?',
                buttons: Ext.Msg.YESNOCANCEL,
                fn: handler,
                scope: this,
                icon: this.QUESTION
            });
            
            return false;
        }
        
        this.activeNode = n;
       
        return cb.apply(this, args);
    },
    
    // private
    endEdit: function(n) {
        this.activeNode = false;
    },
    
    /**
     * 新增树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return Ext.tree.TreeNode
     */
    addNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarAdd']) {
            return;
        }
        
        if (this.maxDepth > 0 && n.getDepth() >= this.maxDepth) {
            if (Ext.isBoolean(this.maxDepthText) && !this.maxDepthText) {
                return;
            }
            Alert(this.maxDepthText);
            return;
        }
        
        for(var i=0;i<this.obarCfg.btns.length;i++){
        	if(this.obarCfg.btns[i].id == 'add'){
        		this.obarCfg.btns[i].handler(n);
        	}
        }
        
    },
    
    // private 
    onAddNode: function(nn, n) {
        if (this.obar.fireEvent('beforeaddnode', this, n) !== false) {
            var ar = function(n) {
                this.removeColTpl();
                
                try {
                    nn.editing = {};
                    nn.editMode = Ext.ux.tree.TreeGridEditor.Obar.Api.add;
                    n.leaf = false;
                    n.attributes.leaf = false;
                    n.appendChild(nn);
                    
//                    var inputFields = this.registerInputField(nn);
//                    
//                    nn.select();
//                    n.ui.interceptEvent = true;
//                    for (var p in inputFields) {
//                        inputFields[p].focus();
//                        if (Ext.isIE) {
//                            inputFields[p].focus();
//                        }
//                        break;
//                    }
                } finally {
//                    this.restoreColTpl();
                }
                
                this.obar.fireEvent('addnode', this, n, nn);
            }
            n.expand(false, false, ar, this);
        }
        return nn;
    },
    
    
    batchAddNode : function(n){
    	n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarAdd']) {
            return;
        }
        
        if (this.maxDepth > 0 && n.getDepth() >= this.maxDepth) {
            if (Ext.isBoolean(this.maxDepthText) && !this.maxDepthText) {
                return;
            }
            Alert(this.maxDepthText);
            return;
        }
        
        for(var i=0;i<this.obarCfg.btns.length;i++){
        	if(this.obarCfg.btns[i].id == 'batchAdd'){
        		this.obarCfg.btns[i].handler(n);
        	}
        }
    },
    
    /**
     * 编辑树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    editNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarEdit']) {
            return;
        }
        
        for(var i=0;i<this.obarCfg.btns.length;i++){
        	if(this.obarCfg.btns[i].id == 'edit'){
        		this.obarCfg.btns[i].handler(n);
        	}
        }
        
//        return this.beginEdit(n, this.onEditNode);
    },
    
    // private
    onEditNode: function(n) {
        if (this.obar.fireEvent('beforeeditnode', this, n) !== false) {
            n.editing = Ext.apply({}, n.attributes);
            n.editMode = Ext.ux.tree.TreeGridEditor.Obar.Api.edit;
            var inputFields = this.registerInputField(n);
            
            Ext.DomHelper.overwrite(Ext.fly(n.getUI().elNode).child('.x-treegrideditor-obar'), this.saveBtnsHtml(n));
            this.overShowObar(n);
            
            n.select();
            for (var p in inputFields) {
                inputFields[p].focus();
                if (Ext.isIE) {
                    inputFields[p].focus();
                }
                break;
            }
            this.obar.fireEvent('editnode', this, n);
        }
    },
    
    /**
     * 取消编辑树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    cancelEdit: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (!n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarNo']) {
            return;
        }
        
        if (this.obar.fireEvent('beforecanceledit', this, n) !== false) {
            if (n.getEditMode() === Ext.ux.tree.TreeGridEditor.Obar.Api.edit) {
                var cs = Ext.fly(n.getUI().elNode).query('span[id^="' + this.id + '-node-if-"], div[id^="' + this.id + '-node-if-"]');
                var cols = this.columns, col, nAttr;
                for (var i = 0, len = cols.length - 1; i < len; i++) {
                    col = cols[i], nAttr = Ext.apply({}, n.getEditing());
                    nAttr[col.dataIndex] = this.filterOutputValue(n.getInputField(col.dataIndex), nAttr[col.dataIndex]);
                    col.tpl.overwrite(cs[i], nAttr);
                }
                
                this.unregisterInputField(n);
                
                n.editing = false;
                n.editMode = false;
                
                Ext.DomHelper.overwrite(Ext.fly(n.getUI().elNode).child('.x-treegrideditor-obar'), this.editBtnsHtml(n));
                this.activeObar(n);
                n.select();
            } else {
                n.remove();
            }
            
            this.obar.fireEvent('canceledit', this, n);
            this.endEdit(n);
        }
    },
    
    /**
     * 保存树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    updateNode: function(n, cb, args) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        
        var m = n.getEditMode();
        if (!m) {
            return;
        }
        
        if (n.attributes['disableObarYes']) {
            return;
        }
        
        if (this.obar.fireEvent('beforeupdatenode', this, n, m) !== false) {
            var valid = true, flds = n.getInputFields();
            for (var p in flds) {
                if (!n.getInputField(p).validate()) {
                    valid = false;
                }
            }
            if (!valid) {
                return;
            }
            
            var gfv = function(n, sort) {
                return n.getInputValue(sort);
            };
            
            if (!this.editValidator(n, m, gfv)) {
                return;
            }
            
            this.handleRequest(n, m, gfv, function(res) {
                if (this.obar.fireEvent('updatenode', this, n, m, res) !== false) {
                    var cs = Ext.fly(n.getUI().elNode).query('span[id^="' + this.id + '-node-if-"], div[id^="' + this.id + '-node-if-"]');
                    var cols = this.columns, col, csDom, nAttr;
                    var tip = function(c, cVal, fVal) {
                        var t;
                        if (c.displayTip && cVal && fVal) {
                            t = fVal;
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
                    for (var i = 0, len = cols.length - 1; i < len; i++) {
                        col = cols[i], csDom = cs[i];
                        n.attributes[col.dataIndex] = n.getInputValue(col.dataIndex), nAttr = Ext.apply({}, n.attributes);
                        nAttr[col.dataIndex] = this.filterOutputValue(n.getInputField(col.dataIndex), nAttr[col.dataIndex]);
                        col.tpl.overwrite(csDom, nAttr);
                        if (col.displayTip) {
                            csDom.qtip = tip(col, n.attributes[col.dataIndex], nAttr[col.dataIndex]);
                        }
                    }
                    
                    this.unregisterInputField(n);
                    
                    // 如果是新增节点, 更新节点ID
                    res = res || '';
                    if (m === Ext.ux.tree.TreeGridEditor.Obar.Api.add && res.length > 0) {
                        try {
                            res = (res.charAt(0) == '{' ? Ext.decode(res).id : res);
                        } catch (e) {
                            //ignore
                        }
                        n.setId(res);
                        for (var i = 0, len = cols.length; i < len; i++) {
                            col = cols[i], csDom = cs[i];
                            csDom.setAttribute('id', this.id + '-node-if-' + col.dataIndex + '-' + res);
                        }
                    }
                    
                    n.editing = false;
                    n.editMode = false;
                    
                    Ext.DomHelper.overwrite(Ext.fly(n.getUI().elNode).child('.x-treegrideditor-obar'), this.editBtnsHtml(n))
                    this.activeObar(n);
                    this.obar.fireEvent('afterupdatenode', this, n, m);
                    
                    this.endEdit(n);
                    cb = cb || Ext.emptyFn, args = args || [], args.unshift(n);
                    cb.apply(this, args);
                }
            });
        }
    },
    
    /**
     * 移除树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    removeNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarRemove']) {
            return;
        }
        
        if (this.obar.fireEvent('beforeremovenode', this, n) !== false) {
            if (!this.editValidator(n, Ext.ux.tree.TreeGridEditor.Obar.Api.remove)) {
                return;
            }
        
	        for(var i=0;i<this.obarCfg.btns.length;i++){
	        	if(this.obarCfg.btns[i].id == 'remove'){
	        		this.obarCfg.btns[i].handler(n);
	        	}
	        }
        
        }
    },
    statusActiveNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarRemove']) {
            return;
        }
        
        if (this.obar.fireEvent('beforestatusactivenode', this, n) !== false) {
            if (!this.editValidator(n, Ext.ux.tree.TreeGridEditor.Obar.Api.statusActive)) {
                return;
            }
        
	        for(var i=0;i<this.obarCfg.btns.length;i++){
	        	if(this.obarCfg.btns[i].id == 'statusActive'){
	        		this.obarCfg.btns[i].handler(n);
	        	}
	        }
        
        }
    },
    statusInvalidNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarRemove']) {
            return;
        }
        
        if (this.obar.fireEvent('beforestatusinvalidnode', this, n) !== false) {
            if (!this.editValidator(n, Ext.ux.tree.TreeGridEditor.Obar.Api.statusInvalid)) {
                return;
            }
        
	        for(var i=0;i<this.obarCfg.btns.length;i++){
	        	if(this.obarCfg.btns[i].id == 'statusInvalid'){
	        		this.obarCfg.btns[i].handler(n);
	        	}
	        }
        
        }
    },
    synchNode: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarSynch']) {
            return;
        }
        
        if (this.obar.fireEvent('beforesynchnode', this, n) !== false) {
            if (!this.editValidator(n, Ext.ux.tree.TreeGridEditor.Obar.Api.synch)) {
                return;
            }
        
	        for(var i=0;i<this.obarCfg.btns.length;i++){
	        	if(this.obarCfg.btns[i].id == 'synch'){
	        		this.obarCfg.btns[i].handler(n);
	        	}
	        }
        
        }
    },
    leveladdNode:function(n){
     	n = Ext.isObject(n) ? n : this.getNodeById(n);
        if (n.getEditMode()) {
            return;
        }
        
        if (n.attributes['disableObarLeveladd']) {
            return;
        }
        
        if (this.obar.fireEvent('beforeleveladdnode', this, n) !== false) {
            if (!this.editValidator(n, Ext.ux.tree.TreeGridEditor.Obar.Api.leveladd)) {
                return;
            }
        
	        for(var i=0;i<this.obarCfg.btns.length;i++){
	        	if(this.obarCfg.btns[i].id == 'leveladd'){
	        		this.obarCfg.btns[i].handler(n);
	        	}
	        }
        
        }
    },
    // private
    handleRequest: function(n, m, gfv, cb) {
        n = Ext.isObject(n) ? n : this.getNodeById(n), nid = n.id;
        
        if (this.beginRequest(nid, m)) {
            return;
        }
        
        var req = this.obar.btns[m].request;
        if (req && this.mode == 'remote') {
            var rAttrs = this.getNodeAttributes(n, gfv);
            var params = req.params ? Ext.apply({}, req.params) : {};
            params.data = this.nodeWriter.write(rAttrs);
            Ext.Ajax.request({
                url: req.url,
                method: req.method,
                params: params,
                success: function(response, opts) {
                    this.endRequest(nid, m);
                    cb.call(this, response.responseText);
                },
                failure: function(response, opts) {
                    this.endRequest(nid, m);
                    Ext.Msg.show({
                        title: 'Error',
                        msg: 'Server-side failure with status code ' + response.status,
                        buttons: Ext.Msg.OK,
                        scope: this,
                        minWidth: 200
                    });
                    this.obar.fireEvent('requestfailure', this, n, m, response.status);
                },
                headers: {
                    'my-header': 'treegrideditor-request'
                },
                scope: this
            });
        } else {
            this.endRequest(nid, m);
            cb.call(this, n);
        }
    },
    
    // private
    filterInputValue: function(f, v) {
        return v ? Ext.util.Format.htmlDecode(v).replace(/&nbsp;/g, " ") : '';
    },
    
    // private
    filterOutputValue: function(f, v) {
        return v ? Ext.util.Format.htmlEncode(v).replace(/ /g, "&nbsp;") : '';
    },
    
    // private
    editValidator: function(n, m, gfv) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var a = this.obar.btns[m], v = a.validator;
        return v(this.getNodeAttributes(n, gfv));
    },
    
    // private 
    beginRequest: function(n, m) {
        if (this.validActions[n]) {
            return true;
        }
        
        this.validActions[n] = true;
        if (m === Ext.ux.tree.TreeGridEditor.Obar.Api.remove) {
            this.disableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.add);
            this.disableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.edit);
            this.disableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.remove);
        } else {
            this.disableObar(n, 'yes');
            this.disableObar(n, 'no');
        }
        
        return false;
    },
    
    // private 
    endRequest: function(n, m) {
        delete this.validActions[n];
        if (m === Ext.ux.tree.TreeGridEditor.Obar.Api.remove) {
            this.enableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.add);
            this.enableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.edit);
            this.enableObar(n, Ext.ux.tree.TreeGridEditor.Obar.Api.remove);
        } else {
            this.enableObar(n, 'yes');
            this.enableObar(n, 'no');
        }
    },
    
    
    /**
     * 获取树节点attributes，如使用gfv取值，则使用gfv返回值覆盖node.attributes
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {Function} gfv (可选)  node.attributes取值函数
     * @return Array
     */
    getNodeAttributes: function(n, gfv) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        
        var rAttrs = {
            id: n.id,
            path: this.getNodePath(n),
            loader: false,
            uiProvider: false
        }
        if (n.parentNode) {
            rAttrs.parentId = n.parentNode.id;
            rAttrs.parentPath = this.getParentNodePath(n);
        }
        if (this.checkbox) {
            var checked = false;
            if (this.checkMode === 'single') {
                var c = document.getElementsByName(this.id + '-node-cb');
                for (var i = 0; i < c.length; i++) {
                    if (c[i].value === n.id) {
                        checked = c[i].checked;
                        break;
                    }
                }
            } else {
                checked = n.attributes.checked;
            }
            rAttrs.checked = checked;
        }
        
        Ext.applyIf(rAttrs, n.attributes);
        delete rAttrs.loader;
        delete rAttrs.uiProvider;
        
        if (Ext.isDefined(gfv)) {
            var cols = this.columns;
            for (var i = 0, len = cols.length - 1; i < len; i++) {
                var col = cols[i], sort = col.dataIndex;
                rAttrs[sort] = gfv.call(this, n, sort);
            }
        }
        
        return rAttrs;
    },
    
    /**
     * 获取树节点路径，不含root节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return String
     */
    getNodePath: function(n) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var path = n.getPath();
        path = path.substring(1);
        path = path.substring(path.indexOf(this.pathSeparator));
        return path;
    },
    
    /**
     * 获取父树节点路径，不含root节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return String
     */
    getParentNodePath: function(n) {
        var path = this.getNodePath(n);
        path = path.substring(0, path.lastIndexOf(this.pathSeparator));
        return path;
    },
    
    /**
     * 获取子树节点，如果p=undefined，返回子TreeNode数组对象；如果p有值，返回指定p值的node.attributes字符串，默认由‘,’分割；
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {String} p (可选)  指定获取node.attributes值
     * @param {String} s (可选)  分隔符，默认','
     * @return Array/String
     */
    getChildNodes: function(n, p, s) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        var childs = n.childNodes || [];
        if (!Ext.isDefined(p)) {
            return childs;
        }
        var ret = '', s = s || ',';
        Ext.each(childs, function(node) {
            if (ret.length > 0) {
                ret += s;
            }
            ret += node.attributes[p];
        });
        return ret;
    },
    
    /**
     * 获取子树节点node.attributes数组
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return Array
     */
    getChildNodeAttrs: function(n) {
        var childs = this.getChildNodes(n);
        var attrs = [];
        Ext.each(childs, function(node) {
            attrs.push(this.getNodeAttributes(node));
        }, this);
        return attrs;
    },
    
    /**
     * 获得树上下文，当mode='local'时，只能通过此方法获取到树上下文
     * 根据nodeWriter模式，输入上下文内容
     * @return {String}
     */
    getTreeContext: function() {
        if (this.nodeWriter) {
            var me = this;
            var fn = function(n) {
                n.children = me.getChildNodeAttrs(n.id);
                if (n.children.length > 0) {
                    for (var i = 0; i < n.children.length; i++) {
                        fn(n.children[i]);
                    }
                }
                return n;
            }
            var data = [], root = this.getRootNode(), rc = root.childNodes;
            if (rc) {
                rc = this.getChildNodeAttrs(root);
                for (var i = 0; i < rc.length; i++) {
                    data.push(fn(rc[i]));
                }
            }
            return this.nodeWriter.write(data);
        }
        return "";
    },
    
    /**
     * 验证树节点是否存在，如果allChild=true，验证树节点的所有子节点，默认验证树节点的下一级子节点；
     * 如果single=true，单属性匹配
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @param {Object} params  验证属性
     * @param {Boolean} allChild (可选)  是否验证所有子节点，默认false
     * @param {Boolean} single (可选)  单属性匹配模式，默认false
     * @param {Object} ignore (可选)  忽略值
     * @param {Boolean} ignoreSingle (可选)  忽略值单属性匹配模式，默认false
     * @return Boolean
     */
    hasNode: function(n, params, allChild, single, ignore, ignoreSingle) {
        n = Ext.isObject(n) ? n : this.getNodeById(n);
        ignore = ignore ||
        {};
        var exist = false;
        
        var ignoreValue = function(nAttrs) {
            var ex = false, iv;
            
            if (ignoreSingle) {
                for (var p in ignore) {
                    iv = ignore[p];
                    if (Ext.isString(iv)) {
                        if (iv === nAttrs[p]) {
                            ex = true;
                        }
                    } else if (Ext.isArray(iv)) {
                        for (var i = 0; i < iv.length; i++) {
                            if (iv[i] === nAttrs[p]) {
                                ex = true;
                                break;
                            }
                        }
                    }
                    
                    if (ex) {
                        break;
                    }
                }
            } else {
                var s = [], pi = 0;
                for (var p in ignore) {
                    iv = ignore[p];
                    if (Ext.isString(iv)) {
                        if (iv === nAttrs[p]) {
                            s.push(true);
                        }
                    } else if (Ext.isArray(iv)) {
                        for (var i = 0; i < iv.length; i++) {
                            if (iv[i] === nAttrs[p]) {
                                s.push(true);
                            }
                        }
                    }
                    pi++;
                }
                
                if (pi > 0 && s.length === pi) {
                    ex = true;
                }
            }
            
            return ex;
        };
        
        var validate = function(nodes) {
            var node, nAttrs;
            var ex = false;
            for (var i = 0; i < nodes.length; i++) {
                node = nodes[i], nAttrs = node.attributes;
                
                if (ignoreValue(nAttrs)) {
                    continue;
                }
                
                if (single) {
                    for (var p in params) {
                        if (params[p] == nAttrs[p]) {
                            ex = true;
                            break;
                        }
                    }
                } else {
                    ex = true;
                    for (var p in params) {
                        if (params[p] != nAttrs[p]) {
                            ex = false;
                            break;
                        }
                    }
                }
                
                if (ex) {
                    break;
                }
            }
            return ex;
        };
        
        if (allChild) {
            var loopValidate = function(nodes) {
                var s = validate(nodes);
                if (s) {
                    return true;
                }
                var node;
                for (var i = 0; i < nodes.length; i++) {
                    node = nodes[i];
                    s = loopValidate(node.childNodes);
                    if (s) {
                        return true;
                    }
                }
                return false;
            };
            exist = loopValidate(this.getRootNode().childNodes);
        } else {
            exist = validate(n.childNodes);
        }
        
        return exist;
    },
    
    /**
     * 批量处理，如果nodes有值，则处理nodes；默认nodes=this.Checked();
     * @param {String} m  批处理类型，取值范围'update' 'cancel' 'add' 'edit' 'remove'或自定义的obar功能
     * @param {String/Array/TreeNode} nodes (可选)  树节点ID/ID数组/TreeNode对象/TreeNode数组
     * @param {Boolean} sequence (可选)  顺序处理，默认true
     * @return void
     */
    batchProcess: function(m, nodes, sequence) {
        m = m.toLowerCase();
        nodes = nodes || this.getChecked();
        if (Ext.isString(nodes)) {
            var ns = nodes.split(this.pathSeparator), nodes = [], n;
            for (var i = 0; i < ns.length; i++) {
                n = this.getNodeById(ns[i]);
                if (Ext.isDefined(n)) {
                    nodes.push(this.getNodeById(ns[i]));
                }
            }
        }
        
        sequence = sequence || true;
        if (sequence === true) {
            var desc = function(a, b) {
                var x = a.getDepth();
                var y = b.getDepth();
                if (x > y) return -1;
                if (x < y) return 1;
                return -1;
            };
            nodes.sort(desc);
        }
        
        var task, lazy = 100;
        for (var i = 0; i < nodes.length; i++) {
            if (m === Ext.ux.tree.TreeGridEditor.Obar.Api.update) {
                task = new Ext.util.DelayedTask(this.updateNode, this, [nodes[i]]);
            } else if (m === Ext.ux.tree.TreeGridEditor.Obar.Api.cancel) {
                task = new Ext.util.DelayedTask(this.cancelEdit, this, [nodes[i]]);
            } else {
                task = new Ext.util.DelayedTask(this[m + 'Node'], this, [nodes[i]]);
                if (m == Ext.ux.tree.TreeGridEditor.Obar.Api.remove) {
                    lazy = 250;
                }
            }
            task.delay(i * lazy);
        }
    }
});

Ext.ux.tree.TreeGridEditor.nodeTypes = {};

Ext.reg('treegrideditor', Ext.ux.tree.TreeGridEditor);

Ext.ux.tree.TreeGridEditor.Obar = function(tge, config) {
    this.setTge = function(tge) {
        this.tge = tge;
    };
    this.setTge(tge);
    config = config ||
    {};
    this.initialConfig = config;
    Ext.apply(this, config);
    
    this.addEvents('beforeaddnode', 'addnode', 'beforeeditnode', 'editnode', 'beforeremovenode', 
    	'removenode', 'afterremovenode', 'beforeupdatenode', 'updatenode', 'afterupdatenode', 
    	'beforecanceledit', 'canceledit', 'requestfailure', 
    	'beforestatusactivenode', 'statusactivenode', 'beforestatusinvalidnode', 'statusinvalidnode');
    
    Ext.ux.tree.TreeGridEditor.Obar.superclass.constructor.call(this);
    
    if (this.init) {
        this.init.call(this);
    }
    
    if (this.initEvents) {
        this.initEvents.call(this);
    }
};

Ext.extend(Ext.ux.tree.TreeGridEditor.Obar, Ext.util.Observable, {
    /**
     * 新增树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    add: function(n) {
        this.tge.addNode.call(this.tge, n);
    },
    
    batchAdd : function(n){
    	this.tge.batchAddNode.call(this.tge, n);
    },
    
    /**
     * 编辑树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    edit: function(n) {
        this.tge.editNode.call(this.tge, n);
    },
    
    /**
     * 删除树节点
     * @param {String/Ext.tree.TreeNode} n  树节点ID或TreeNode对象
     * @return void
     */
    remove: function(n) {
        this.tge.removeNode.call(this.tge, n);
    },
    statusActive: function(n){
    	this.tge.statusActiveNode.call(this.tge, n);
    },
    statusInvalid: function(n){
    	this.tge.statusInvalidNode.call(this.tge, n);
    },
    synch:function(n){
    	this.tge.synchNode.call(this.tge,n);
    },
    leveladd:function(n){
    	this.tge.leveladdNode.call(this.tge,n);
    }
});

Ext.ux.tree.TreeGridEditor.Obar.Api = {
    add: 'add',
    batchAdd  : 'batchAdd',
    edit: 'edit',
    remove: 'remove',
    statusInvalid: 'statusInvalid',
    statusActive: 'statusActive',
    update: 'yes',
    cancel: 'no',
    synch:'synch',
    leveladd:'leveladd'
};
