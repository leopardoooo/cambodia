/**
 * 封装过滤的Grid组件，
 * 在grid的Top bar追加一个列名下拉框和输入框。
 */
Ext.ns("Ext.ux");

Ext.ux.FilterGrid = Ext.extend( Ext.grid.GridPanel , {
	cmbFilterColumn: null,
	
	constructor: function(cfg){
		var columns = (cfg.columns || cfg.cm.config);
		var tbar = cfg.tbar || [];
		// 判断是否需要删除第一项
		if(columns.length > 0 && Ext.isFunction(columns[0].isLocked)){
			columns = columns.slice(1);
		}
		columns = [].concat([{header: '请选择...' , dataIndex: ''}],columns);
		
		this.cmbFilterColumn = new Ext.form.ComboBox({
			width: 90,
			value: '',
			triggerAction: 'all',
            store: new Ext.data.JsonStore({
				data: columns,
		        fields: ['header','dataIndex']
			}),
			mode:'local',
            displayField:'header',
            valueField:'dataIndex'
		});
		
		var filterCmp = [' ',' ',this.cmbFilterColumn,' ',{
			xtype: 'textfield',
			width: 120,
			emptyText: '按字段名过滤，多个值逗号隔开...',
			width:200,
			selectOnFocus:true,
			listeners:{
				scope: this,
				"specialkey": this.doFilterRecord
			}
		}];
		Ext.apply(cfg , {
			tbar: tbar.concat(filterCmp)
		})
		Ext.ux.FilterGrid.superclass.constructor.call(this,cfg);
	},
	doFilterRecord: function(_txt,e){
		if(e.getKey() != e.ENTER) return ;
		var store = this.getStore(),
			column = this.cmbFilterColumn.getValue();
		if(column == "") return ;
		if(_txt.getValue() == ""){
			store.clearFilter();
		} else {
			var _vs = _txt.getValue().split(",");
			var _regex = "" ;
			if (_vs.length > 0 && _vs[0].trim() != "") {
				_regex = "(" + _vs[0].trim() + ")";
			}
			for (var i = 1; i < _vs.length; i++) {
				if (_vs[i].trim() != "") 
					_regex += "|(" + _vs[i] + ")";
			}
			if(_regex == ""){
				store.clearFilter();
				return false ;
			}
			store.filter(column, new RegExp(_regex), true, true);
		}
	}
	
});
