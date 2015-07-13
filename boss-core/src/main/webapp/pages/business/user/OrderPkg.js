/**
 * 订购套餐表单
 * @class OrderPkgForm
 * @extends BaseForm
 */
OrderPkgForm = Ext.extend(BaseForm,{
	busiOrderProd: null,
	url: Constant.ROOT_PATH + "/core/x/User!orderPkg.action",
	constructor: function(){
		this.busiOrderProd = new BusiOrderProd(this,'CUST');
		OrderPkgForm.superclass.constructor.call(this,{
            border: false,
            layout: 'anchor',
			items:[{
				anchor:'100% 21%',
				layout:'fit',
				border : false,
				items:[this.busiOrderProd]
			},{
				anchor:'100% 10%',
				border : false,
				layout:'fit',
				items:[{
					xtype : 'panel',
					border : false,
					layout : 'form',
					labelWidth: 105,
					bodyStyle: "background:#F9F9F9",
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
				anchor:'100% 40%',
				layout:'fit',
				items:[selectableProdGrid]
			},{
				anchor:'100% 29%',
				layout:'fit',
				items:[selectedProdGrid]
			}]	
		});
	},
	changeProd:function(pkgId,pkgTarrifId){
		prodStore.baseParams.pkgId=pkgId;
		prodStore.baseParams.pkgTarrifId=pkgTarrifId;
		prodStore.baseParams.custType=App.getData().custFullInfo.cust.cust_type;
		prodStore.baseParams.custId=App.getData().custFullInfo.cust.cust_id;
		prodStore.load();
	},
	getValues : function(){
		var all = {};
		all["prodId"] = Ext.getCmp('busiOrderProd').oldProdID;
		all["tariffId"] = Ext.getCmp('prodTariffId').getValue();
		all["feeDate"] = Ext.getCmp('prodstartdate').getValue();
		all['stopType'] = Ext.getCmp('stopType').getValue();
		
		var prodSns = [];
		var prods = selectedProdGrid.getStore();
		for(i=0;i<prods.getCount();i++){
			prodSns.push(prods.getAt(i).get("prod_sn"));
		}
		all["prodSns"] = prodSns.join(",");
		return all;
	},
	doValid: function(){
		if(this.getForm().isValid()){
			var obj = {};
			var store = selectedProdGrid.getStore();
			var count = store.getCount();
			if(count == 0){
				obj['isValid'] = false;
				obj['msg'] = '至少选择一个产品';
			}
			
			for(var i=0;i<count;i++){
				var rec = store.getAt(i);
				var total = 0;//总共选择数
				for(var k=count -1;k>=0;k--){
					if(rec.get('prod_id') == store.getAt(k).get('prod_id')){
						total = total + 1;
					}
				}
				if(rec.get('max_prod_count') < total){
					obj['isValid'] = false;
					obj['msg'] = rec.get('prod_name')+'选择不能超过'+rec.get('max_prod_count');
					break;
				}
			}
			
			return obj;
		}else{
			return OrderPkgForm.superclass.doValid.call(this);
		}
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
/**
 * 入口
 */
Ext.onReady(function(){
	var opf = new OrderPkgForm();
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
