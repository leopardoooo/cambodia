Ext.ns("Ext.ux");

 /**
 * MoneyColumn 统一对Boss系统的金额封装的column控件
 * 
 * @class Ext.ux.MoneyColumn
 * @extends Ext.grid.Column
 */
Ext.ux.MoneyColumn = Ext.extend(Ext.grid.Column, {
    constructor: function(cfg){
        Ext.ux.MoneyColumn.superclass.constructor.call(this, cfg);
        this.renderer = Ext.util.Format.moneyRenderer;
    }
});

//在Ext中注册Column types
Ext.grid.Column.types.moneycolumn = Ext.ux.MoneyColumn;