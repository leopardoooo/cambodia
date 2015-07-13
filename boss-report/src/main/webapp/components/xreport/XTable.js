if(Ext.PagingToolbar){
   //Ext.QuickTips.init(); // to display button quicktips
   Ext.apply(Ext.PagingToolbar.prototype, {
      beforePageText : "第",//update
      afterPageText  : "页,共 {0} 页",//update
      firstText      : "第一页",
      prevText       : "上一页",//update
      nextText       : "下一页",
      lastText       : "最后页",
      refreshText    : "刷新",
      displayMsg     : "显示 {0} - {1}条，共 {2} 条", // update
      emptyMsg       : '没有数据'
   });
}

/*  //表头单元格格式
	{ id: 数据ID, 
	  name: 数据显示名称, 
	  show_class: 数据显示样式, 
	  width: 宽度
	  colspan\rowspan: 单元格纵向合并值(值>1合并,值=0被合并格,值=1 正常格), 
	  cell_type: 单元格类型:维度，数据，小计，合计,
	  dim: 表头维度值, 
	  level: 表头层级,
	  level_value: 表头层级值(粒度维存在这个值), 
	  Ishidden: 表头是否隐藏, 
	  Canhidden: 表头能否隐藏 }

	//数据单元格格式
	{ id: 数据ID, 
	  name: 数据显示名称, 
	  show_class: 数据显示样式,
	  rowspan\colspan: 单元格横向合并值(值>1合并,值=0被合并格,值=1 正常格),
	  cell_type: 单元格类型:维度，数据，小计，合计 }
*/
XTable = function(){
	var DEFAULT = {
		headerWidth: 100 
	} 
	function __getWidth(colspan, width){
		var width = width || DEFAULT.headerWidth;
		return colspan > 1 ? colspan * width + colspan - 1 : width;
	}
	function isHidden(colspan ,rowspan){
		return colspan == 0 && rowspan == 0;
	}
	var HeaderTpl = new Ext.XTemplate(
		'<div class="x-table-header">',
			'<table class="" style="width: {[values.tableWidth + 16]}px;">',
				'<tpl for="headerCells">',
					'<tr>',
						'<tpl if="xindex == 1">',
							'<tpl for=".">',
								'<tpl if="!this.isHidden(colspan, rowspan)">',
									'<td class="report_th" rowspan="{rowspan}" colspan="{colspan}" ',
										'style="width: {[this.getWidth(values.colspan,values.width)]}px;{show_class}; " >',
										'<tpl if="show_slices == true">',
											'<div class="Fl tools filter icon-filter" title="过滤" style="background-position: 5px 0px; width: 21px;" dim="{dim}"></div>',
											'<div class="Fl split" style="width: 6px;">&nbsp;</div>',
										'</tpl>',
										'<tpl if="show_sort == true">',
											'<div class="Fl tools sort icon-sort" title="排序" dim="{dim}"></div>',
											'<div class="Fl split">&nbsp;</div>',
										'</tpl>',
										'<tpl if="show_expand == true">',
											'<div class="Fl tools in icon-zoom-in" title="展开" dim="{dim}"></div>',
											'<div class="Fl split">&nbsp;</div>',
										'</tpl>',
										'<tpl if="show_shrink == true">',
											'<div class="Fl tools out icon-zoom-out" title="收缩" dim="{dim}"></div>',
											'<div class="Fl split">&nbsp;</div>',
										'</tpl>',
									'</td>',
								'</tpl>',
							'</tpl>',
							'<td class="report_th" style="width: 16px;" rowspan="{[xcount]}">&nbsp;</td>',
						'</tpl>',
						'<tpl if="xindex != 1">',
							'<tpl for=".">',
								'<tpl if="!this.isHidden(colspan, rowspan)">',
									'<td class="report_th" title="{name}" rowspan="{rowspan}" colspan="{colspan}" ',
										'style="width: {[this.getWidth(values.colspan,values.width)]}px;{show_class}" >{name}</td>',
								'</tpl>',
							'</tpl>',
						'</tpl>',
					'</tr>',
				'</tpl>',
			'</table>',
		'</div>', { 
			getWidth: __getWidth,
			isHidden: isHidden
		});
	var BodyTpl = new Ext.XTemplate(
		'<div class="x-table-body" style="top: {headerHeight}px;">',
			'<tpl if="!data||data.length==0"><div style="width: {tableWidth}px;">&nbsp;</div></tpl>',
			'<table style="width: {tableWidth}px;">',
				'<tpl for="data.records">',
					'<tr rowIndex="{[this.xpindex(xindex)]}">',
						'<tpl if="xindex == 1">',
							'<tpl for=".">',
								'<td style="width: {[this.getCellWidth(xindex)]}px;{show_class}" colIndex="{[xindex-1]}" rowspan="{rowspan}" class="report_td BTop">',
									'<tpl if="this.isLink(xindex)"><a href="#" style="{show_class}">{name}</a></tpl>',
									'<tpl if="!this.isLink(xindex)">{name}</tpl>',
								'</td>',
							'</tpl>',
						'</tpl>',
						'<tpl if="xindex &gt; 1">',
							'<tpl for=".">',
								'<tpl if="!this.isHidden(colspan, rowspan)">',
									'<td style="width: {[this.getCellWidth(xindex)]}px;{show_class}" colIndex="{[xindex-1]}" rowspan="{rowspan}" class="report_td">',
										'<tpl if="this.isLink(xindex)"><a href="#" style="{show_class}">{name}</a></tpl>',
										'<tpl if="!this.isLink(xindex)">{name}</tpl>',
									'</td>',
								'</tpl>',
							'</tpl>',
						'</tpl>',
					'</tr>',
				'</tpl>',
			'</table>',
		'</div>', {
			__xpindex: 0,
			cellsWidth: [],

			isHidden: isHidden,
			xpindex: function(currentIndex){
				if(currentIndex > 0){ currentIndex --; }
				this.__xpindex = currentIndex || this.__xpindex;
				return this.__xpindex;
			},
			getCellWidth: function(i){
				return this.cellsWidth[--i] || DEFAULT.headerWidth;
			},
			isLink: function(currentIndex){
				var cell = this.headerCells[this.headerCells.length - 1][(currentIndex - 1)];
				return  !Ext.isEmpty(cell.mea_detail_id);
			}
		}
	);
	var TableTpl = new Ext.XTemplate(
		'<div class="x-table">',
			'<div class="x-d-content">{table_html}</div>',
			'<div class="x-paging"></div>',
		'</div>'
	);
	return Ext.extend(Ext.util.Observable, {
		// config
		headerCells: null, // 表头
		data: null,   //数据
		renderTo: null, //渲染的元素
		
		// private
		cellsWidth: [], // 列宽度
		tableWidth: 0,  // table宽度
		headerHeight: 0, // 表头宽度, 包含边框占用的宽度
		pagingToolbar: null,
		
		//store
		dataUrl: null,
		baseParams: {},
		loadCallback: Ext.emptyFn,
		pageSize: 50,
		start: 0,

		//
		constructor: function(config){
			Ext.apply(this, config || {});
			this.addEvents({
				"cellclick" : true,
				"rowclick": true
			});
			XTable.superclass.constructor.call(this);
		},
		render: function(){
			this.initTable();
			this.doRender();
		},
		loadAndView: function(start){
			// this.pagingToolbar = null;
			this.doLoad(start || 0);
		},
		initTable: function(){
			if(this.headerCells && this.headerCells.length > 0){
				var sumWidth = 0;
				for(var i = 0; i< this.headerCells[0].length; i++){
					var c = this.headerCells[0][i];
					sumWidth += __getWidth(c["colspan"], c["width"]);
					
					//计算单元格的宽度，需要所有的cell, 包含colspan的cell
					var colspan = c["colspan"] == 0 ? 1 : c["colspan"];
					for(var j = 0; j < colspan; j++){
						this.cellsWidth[this.cellsWidth.length] = c["width"];
					}
				}
				this.tableWidth = sumWidth;
				this.headerHeight = this.headerCells.length * 25 + this.headerCells.length + 1;
			}
		},
		//private functions
		initPagingbar: function(){
			var that = this;
			this.pagingToolbar = new Ext.PagingToolbar({
				store: new Ext.data.ArrayStore({
					autoLoad: false,
					root: 'records',
        			totalProperty: 'totalProperty',
					fields: ['x'],
					data: this.data
				}),
				displayInfo: true,
				pageSize: this.pageSize,
				doLoad: this.doLoad.createDelegate(this),
				//private 
			    onLoad : function(store, r, o){
			    	Ext.PagingToolbar.prototype.onLoad.call(this, store, r, {
			    		params: { start: that.start}
			    	});
			    }
			});
		},
		doLoad: function(start){
			this.start = start;
			var o = Ext.apply({}, this.baseParams, {
				"start": start,
				"limit": this.pageSize
			});
			Ext.Ajax.request({
	   			scope : this,
	   			url : this.dataUrl,
	   			params: o,
	   			success : function(res,opt){
	   				var data = Ext.decode(res.responseText);
	   				this.headerCells = data.records;
					this.data =  data.page;
					this.render();
					// 分页条，加载数据计算分页信息
					if(this.pagingToolbar){
				    	this.pagingToolbar.store.loadData(this.data, false);
			        }
			        this.loadCallback.call(this, this.headerCells, this.data);
	   			}
			});
		},
		doRender: function(){
			BodyTpl.cellsWidth = this.cellsWidth;
			BodyTpl.headerCells = this.headerCells;
			
			var headerHtml = HeaderTpl.apply(this);
			var bodyHtml = BodyTpl.apply(this);
			var content = headerHtml + bodyHtml;
			this.renderTo = Ext.get(this.renderTo);
			//render paging bar
			if(this.pagingToolbar){
				var contextRoot = Ext.query("DIV.x-d-content", this.renderTo.dom);
				contextRoot[0].innerHTML = content;
			}else{
				this.renderTo.dom.innerHTML = TableTpl.apply({
					table_html: content
				});
				this.initPagingbar();
				var pagingRoot = Ext.query("DIV.x-paging", this.renderTo.dom);
				this.pagingToolbar.render(pagingRoot[0]);
			}
			this.doCascadeScroll();
		},
		doCascadeScroll: function(){
			var header = this.renderTo.child(".x-table-header"),
				body = this.renderTo.child(".x-table-body");
			body.on("scroll", function(){
				header.dom.scrollLeft = body.getScroll().left;
			});
			
			body.on("click", function(e, t){
				var el = Ext.fly(t);
				var td = el.findParent("TD.report_td", 3 , true),
					tr = el.findParent("TR[rowIndex]", 6 , true);
				if(td){
					var y = tr.getAttribute("rowIndex"),
						x = td.getAttribute("colIndex");
					this.fireEvent("cellclick", e, el, x, y , this.data.records[x][y], this.data.records[y], this);
				}
				if(tr){
					this.fireEvent("rowclick", e, el, this);
				}
			}, this);
		}
	})
}();