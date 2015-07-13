
/**
 * 维度切换的小模块
 */
DimListView = Ext.extend(Ext.grid.GridPanel, {

	constructor: function(myGroup, ddGroup, cfg){
		this.myGroup = myGroup;
		Ext.apply(this, cfg || {});
		this.store = new Ext.data.JsonStore({
			autoLoad: false,
      		fields: ['name', 'id']
		});
		var id = "autocol_" + Ext.id();
		DimListView.superclass.constructor.call(this, {
			store: this.store,
			hideHeaders: true,
			border: false,
		   	ddGroup: ddGroup,
		   	enableDragDrop: true,
		   	stripeRows: true,
			sm: new Ext.grid.RowSelectionModel({singleSelect:false}),
			autoExpandColumn: id,
			viewConfig: {forceFit: true},
			columns: [{
				id: id,
	            header: 'name',
	            dataIndex: 'name',
	            align: 'left'
	        }]
		});
	},
	initEvents: function(){
		DimListView.superclass.initEvents.call(this);
		var that = this;
        var dropTarget = new Ext.dd.DropTarget(this.getView().scroller.dom, {
                ddGroup: this.myGroup,
                notifyDrop: function(ddSource, e, data){
	                return that.doDragCallback(ddSource, e);
                }
        });
	},
	doDragCallback: function(ddSource, e){
        var rowIndex = false ; 
        try{
        	var t = Ext.lib.Event.getTarget(e);
        	rowIndex = this.view.findRowIndex(t);
        }catch(e){
        	//nothing
        }
		var records =  ddSource.dragData.selections;
	    Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
        if(rowIndex !== false){
        	this.store.insert(rowIndex, records);
        }else{
        	this.store.add(records);
        }
		return true;
	},
	loadData: function(data){
		this.store.loadData(data);
		return this;
	}
	
});