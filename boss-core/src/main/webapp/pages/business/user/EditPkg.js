/**
 * 修改套餐表单
 * @class EditPkgForm
 * @extends BaseForm
 */
EditPkgForm = Ext.extend(BaseForm,{
	busiOrderProd: null,
	url: Constant.ROOT_PATH + "/core/x/User!editPkg.action",
	pkgSn : null,
	record : null,
	constructor: function(){
		var packageGrid = App.getApp().main.infoPanel.getCustPanel().packageGrid;
		if (App.getApp().data.custFullInfo && App.getApp().data.custFullInfo.cust.cust_type=='UNIT'){
			packageGrid = App.getApp().main.infoPanel.getUnitPanel().packageGrid;
		}
		
		this.record = packageGrid.getSelectionModel().getSelected();
		this.loadData(this.record);
		
		EditPkgForm.superclass.constructor.call(this,{
            border: false,
            layout: 'anchor',
			items:[{
				anchor:'100% 10%',
				border : false,
				layout:'fit',
				items:[{
					xtype : 'panel',
					border : false,
					layout : 'form',
					labelWidth: 105,
					bodyStyle: Constant.TAB_STYLE,
					items : [{
						fieldLabel:'停机类型',
						xtype:'paramcombo',
						allowBlank:false,
						id : 'stopType',
						hiddenName:'stop_type',
						paramName:'STOP_TYPE',
						defaultValue:'KCKT'
					}]
				}]
			},{
				anchor:'100% 50%',
				layout:'fit',
				items:[selectableProdGrid]
			},{
				anchor:'100% 40%',
				layout:'fit',
				items:[selectedProdGrid]
			}],
			tbar : ['  ','请直接拖到产品进行修改']
		});
	},
	doInit : function(){
		Ext.getCmp('stopType').setValue(this.record.get('stop_type'));
	},
	loadData:function(record){
		//可选产品数据
		prodStore.baseParams.pkgId=record.get('prod_id');
		prodStore.baseParams.pkgTarrifId=record.get('tariff_id');
		prodStore.baseParams.custType=App.getData().custFullInfo.cust.cust_type;
		prodStore.baseParams.custId=App.getData().custFullInfo.cust.cust_id;
		prodStore.load();
		
		//已选产品数据
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH+"/core/x/Cust!queryProdsOfPkg.action",
			params : {
				custId :App.getData().custFullInfo.cust.cust_id,
				pkgId : record.get('prod_id')
			},
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				if(rec.length > 0){
					this.pkgSn = rec[0].package_sn;
				}else{
					this.pkgSn = record.get('prod_sn');
				}
				selectedProdGrid.getStore().loadData(rec);
			}
		})
	},
	getValues : function(){
		var all = {};
		
		var prods = selectedProdGrid.getStore();
		var prodSns = [];
		for(i=0;i<prods.getCount();i++){
			prodSns.push(prods.getAt(i).get("prod_sn"));
		}
		all["prodSns"] = prodSns.join(",");
		all['pkgSn'] = this.pkgSn;
		all['stopType'] = Ext.getCmp('stopType').getValue();
		return all;
	},
	doValid: function(){
		var obj = {};
		if(selectedProdGrid.getStore().getCount() == 0){
			obj['isValid'] = false;
			obj['msg'] = '至少选择一个产品';
		}else{
			obj['isValid'] = true;
		}
		return obj;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
/**
 * 入口
 */
Ext.onReady(function(){
	var opf = new EditPkgForm();
	var box = TemplateFactory.gTemplate(opf);
	
	// This will make sure we only drop to the  view scroller element
	
	var blankRecord =  Ext.data.Record.create(fds);
	var firstGridDropTargetEl =  selectableProdGrid.getView().scroller.dom;
	var firstGridDropTarget = new Ext.dd.DropTarget(firstGridDropTargetEl, {
	        ddGroup    : 'firstGridDDGroup',
	        notifyDrop : function(ddSource, e, data){
	                var records =  ddSource.dragData.selections;
	                Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
	                selectableProdGrid.store.add(records);
	                selectedProdGrid.setTitle("已选"+selectedProdGrid.store.getCount()+"个产品");
	                return true
	        }
	});
	
	
	// This will make sure we only drop to the view scroller element
	var secondGridDropTargetEl = selectedProdGrid.getView().scroller.dom;
	var secondGridDropTarget = new Ext.dd.DropTarget(secondGridDropTargetEl, {
	        ddGroup    : 'secondGridDDGroup',
	        notifyDrop : function(ddSource, e, data){
	                var records =  ddSource.dragData.selections;
	                Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
	                selectedProdGrid.store.add(records);
	                selectedProdGrid.setTitle("已选"+selectedProdGrid.store.getCount()+"个产品");
	                return true
	        }
	});

});
