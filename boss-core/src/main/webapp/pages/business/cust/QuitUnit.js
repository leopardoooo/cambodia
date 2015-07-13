/**
 * 退出单位
 */
UnitSelectGrid = Ext.extend(Ext.grid.GridPanel,{
	unitStore : null,
	height : 200,
	region : 'center',
	title: '单位信息',
	constructor:function(){
		this.unitStore = new Ext.data.JsonStore({
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields: ["cust_id","cust_name","address"],
			autoLoad: false
		}); 
		this.unitStore.add(App.getApp().main.infoPanel.getCustPanel().unitGrid.getSelectionModel().getSelections());
		var cm = [
			{header:'名称',dataIndex:'cust_name',width:100},
			{header:'地址',dataIndex:'address',	width:100}
		]
		
		UnitSelectGrid.superclass.constructor.call(this,{
			store:this.unitStore,
			columns:cm
		})
	},
	remoteRefresh:function(){
	},
	localRefresh:function(){
	}
});

QuitUnit = new Ext.extend(BaseForm,{
	unitSelectGrid: null,
	url: Constant.ROOT_PATH + "/core/x/Cust!quitUnit.action",
	constructor: function(){
		this.unitSelectGrid = new UnitSelectGrid();
		QuitUnit.superclass.constructor.call(this,{
			border: false,
            layout: 'border',
			items:[this.unitSelectGrid]	
		
		})
	},
	getValues: function(){
		var len = this.unitSelectGrid.getStore().getCount();
		var unitIds = [];
		var all = {};
		for(var i = 0; i<len ;i++){
			unitIds[i] = this.unitSelectGrid.getStore().getAt(i).get("cust_id");
		}
		all['unitIds']=unitIds;
		return all;
	},
	//覆盖该函数处理刷新的功能
	success: function(f , resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
/**
 * 入口
 */
Ext.onReady(function(){
	var tf = new QuitUnit();
	var box = TemplateFactory.gTemplate(tf);
});