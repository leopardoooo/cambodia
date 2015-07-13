/*
 * 
 * 
 * 
 * 
 */
Ext.grid.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};
 
 	Ext.grid.CheckColumn.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },
    onMouseDown : function(e, t){     
        if(t.id ==this.id){
            e.stopEvent();
            var v=this.grid.getView();
            var rowIndex = v.findRowIndex(t);
            var columnIndex = v.findCellIndex(t);
            var record = this.grid.store.getAt(rowIndex);
            var value= !record.data[this.dataIndex];
            record.set(this.dataIndex, value);
            this.grid.fireEvent('afteredit',this.grid,record,this.dataIndex,value,!value,rowIndex,rowIndex)
        }
    },
    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td';
        return '<div id="'+ this.id +'" class="x-grid3-check-col'+(v?'-on':'')+'">&#160;</div>';
    }
};
 	