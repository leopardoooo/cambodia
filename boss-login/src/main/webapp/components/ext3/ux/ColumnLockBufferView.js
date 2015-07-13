
Ext.ns('Ext.ux.grid');
/**
 * 合并BufferView与LockingGridView
 * 
 * @class Ext.ux.grid.ColumnLockBufferView
 * @extends Ext.ux.grid.BufferView
 * @author yaoba
 */
Ext.ux.grid.ColumnLockBufferView = Ext.extend(Ext.ux.grid.BufferView, {});
Ext.apply(Ext.ux.grid.BufferView.prototype,
		Ext.ux.grid.LockingGridView.prototype);

Ext.apply(Ext.ux.grid.ColumnLockBufferView.prototype, {

	lockText : "锁列",
	unlockText : "解锁列",
	rowHeight: 23,
	initTemplates : function() {
		var ts = this.templates || {};
		if (!ts.master) {
			ts.master = new Ext.Template(
					'<div class="x-grid3" hidefocus="true">',
					'<div class="x-grid3-locked">',
					'<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{lstyle}">{lockedHeader}</div></div><div class="x-clear"></div></div>',
					'<div class="x-grid3-scroller"><div class="x-grid3-body" style="{lstyle}">{lockedBody}</div><div class="x-grid3-scroll-spacer"></div></div>',
					'</div>',
					'<div class="x-grid3-viewport x-grid3-unlocked">',
					'<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{ostyle}">{header}</div></div><div class="x-clear"></div></div>',
					'<div class="x-grid3-scroller"><div class="x-grid3-body" style="{bstyle}">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
					'</div>',
					'<div class="x-grid3-resize-marker">&#160;</div>',
					'<div class="x-grid3-resize-proxy">&#160;</div>', '</div>');
		}
		this.templates = ts;
		Ext.ux.grid.LockingGridView.superclass.initTemplates.call(this);

		// Ext.ux.grid.BufferView.superclass.initTemplates.call(this);
		var ts = this.templates;
		// empty div to act as a place holder for a row
		ts.rowHolder = new Ext.Template('<div class="x-grid3-row {alt}" style="{tstyle}"></div>');
		ts.rowHolder.disableFormats = true;
		ts.rowHolder.compile();

		ts.rowBody = new Ext.Template(
				'<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
				'<tbody><tr>{cells}</tr>',
				(this.enableRowBody
						? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>'
						: ''), '</tbody></table>');
		ts.rowBody.disableFormats = true;
		ts.rowBody.compile();
	},
	doRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody) {
		var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount - 1, buf = [], lbuf = [], cb, lcb, c, p = {}, rp = {}, r;

		var rb = ts.rowBody;
		var rh = this.getStyleRowHeight();
		var vr = this.getVisibleRows();
		var tstyle = 'width:' + this.getTotalWidth() + ';height:' + rh + 'px;';
		var lstyle = 'width:' + this.getLockedWidth() + ';height:' + rh + 'px;';
		rp = {
			tstyle : tstyle
		};

		for (var j = 0, len = rs.length; j < len; j++) {
			r = rs[j];
			cb = [];
			lcb = [];
			var rowIndex = (j + startRow);

			var visible = rowIndex >= vr.first && rowIndex <= vr.last;
			if (visible) {
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = (i === 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: ''))
							+ (this.cm.config[i].cellCls ? ' '
									+ this.cm.config[i].cellCls : '');
					p.attr = p.cellAttr = '';
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (Ext.isEmpty(p.value)) {
						p.value = '&#160;';
					}
					if (this.markDirty && r.dirty
							&& Ext.isDefined(r.modified[c.name])) {
						p.css += ' x-grid3-dirty-cell';
					}
					if (c.locked) {
						lcb[lcb.length] = ct.apply(p);
					} else {
						cb[cb.length] = ct.apply(p);
					}
				}
			}
			var alt = [];
			if (stripe && ((rowIndex + 1) % 2 === 0)) {
				alt[0] = 'x-grid3-row-alt';
			}
			if (r.dirty) {
				alt[1] = ' x-grid3-dirty-row';
			}
			rp.cols = colCount;
			if (this.getRowClass) {
				alt[2] = this.getRowClass(r, rowIndex, rp, ds);
			}
			rp.alt = alt.join(' ');
			rp.cells = cb.join('');
			rp.tstyle = tstyle;
			buf[buf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody
					? rb.apply(rp)
					: rt.apply(rp));
			// buf[buf.length] = rt.apply(rp);
			rp.cells = lcb.join('');
			rp.tstyle = lstyle;

			lbuf[lbuf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody
					? rb.apply(rp)
					: rt.apply(rp));
			// lbuf[lbuf.length] = rt.apply(rp);
		}
		return [buf.join(''), lbuf.join('')];
	},
	doUpdate : function() {
		if (this.getVisibleRowCount() > 0) {
			var g = this.grid, cm = g.colModel, ds = g.store;
			var cs = this.getColumnData();

			var vr = this.getVisibleRows();
			for (var i = vr.first; i <= vr.last; i++) {
				// if row is NOT rendered and is visible, render it
				if (!this.isRowRendered(i)) {
					var html = this.doRender(cs, [ds.getAt(i)], ds, i, cm
									.getColumnCount(), g.stripeRows, true);
					this.getRow(i).innerHTML = html[0];
					this.getLockedRow(i).innerHTML = html[1];
				}
			}
			this.clean();
		}
	},
	syncScroll : function(e) {
		var mb = this.scroller.dom;
		this.lockedScroller.dom.scrollTop = mb.scrollTop;
		Ext.ux.grid.LockingGridView.superclass.syncScroll.call(this, e);
		this.update();
	},
	layout : function() {
		Ext.ux.grid.BufferView.superclass.layout.call(this);
		this.update();

		if (!this.mainBody) {
			return;
		}
		var g = this.grid;
		var c = g.getGridEl();
		var csize = c.getSize(true);
		var vw = csize.width;
		if (!g.hideHeaders && (vw < 20 || csize.height < 20)) {
			return;
		}
		this.syncHeaderHeight();
		if (g.autoHeight) {
			this.scroller.dom.style.overflow = 'visible';
			this.lockedScroller.dom.style.overflow = 'visible';
			if (Ext.isWebKit) {
				this.scroller.dom.style.position = 'static';
				this.lockedScroller.dom.style.position = 'static';
			}
		} else {
			this.el.setSize(csize.width, csize.height);
			var hdHeight = this.mainHd.getHeight();
			var vh = csize.height - (hdHeight);
		}
		this.updateLockedWidth();
		if (this.forceFit) {
			if (this.lastViewWidth != vw) {
				this.fitColumns(false, false);
				this.lastViewWidth = vw;
			}
		} else {
			this.autoExpand();
			this.syncHeaderScroll();
		}
		this.onLayout(vw, vh);
	}
});