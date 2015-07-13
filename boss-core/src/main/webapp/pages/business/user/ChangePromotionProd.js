/**
*更换促销产品
*/

var OrigPromotionGrid = Ext.extend(Ext.grid.GridPanel,{
	pStore: null,
	constructor: function(){
		this.pStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH +'/commons/x/QueryUser!queryPromotionProdBySn.action',
			fields:['promotion_sn','area_id','county_id','acct_id',
				'acctitem_id','fee','months','prod_name','necessary','promotion_id','tariff_name']
		});
		var columns = [
			{header:'产品名称',dataIndex:'prod_name'},
			{header:'资费',dataIndex:'tariff_name'},
			{header:'赠送金额',dataIndex:'fee',renderer : Ext.util.Format.formatFee},
			{header:'赠送月数',dataIndex:'months'}
		];
		OrigPromotionGrid.superclass.constructor.call(this,{
			title:'原促销赠送产品信息',
			layout:'fit',
			columns:columns,
			store:this.pStore
		});
	}
	
});

var PromotionForm = Ext.extend(BaseForm,{
	origAcctPromotionGrid: null,
	acctPromotionGrid: null,
	url: Constant.ROOT_PATH + "/core/x/User!saveChangePromotion.action",
	constructor:function(){
		this.origAcctPromotionGrid = new OrigPromotionGrid();
		this.acctPromotionGrid = new AcctPromotionGrid();
		PromotionForm.superclass.constructor.call(this,{
			layout:'border',
			border:false,
            items:[{
				layout:'fit',
				region:'center',
            	items:[this.origAcctPromotionGrid]
            },{
				layout:'fit',
				region:'east',
				width:'50%',
            	items:this.acctPromotionGrid
            }]
		});
		
		var pRecord = App.getApp().main.infoPanel.getUserPanel().userDetailTab.promotionGrid
				.getSelectionModel().getSelected();
		this.origAcctPromotionGrid.getStore().load({
			params:{
				promotion_sn:pRecord.get('promotion_sn'),
				promotion_id:pRecord.get('promotion_id')
			}
		});
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH + "/core/x/User!queryPromInfo.action",
			params : {
				promotionId : pRecord.get('promotion_id'),
				custId : App.getApp().getCustId(),
				userId : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds()[0]
			},
			success : function(res,opt){
				var res = Ext.decode(res.responseText);
				if(res.success == true){
					this.acctPromotionGrid.doLoadData(res.simpleObj.acctList,Ext.util.Format.formatFee(pRecord.get('total_acct_fee')),pRecord.get('total_acct_count'));
				}
			}
		})
	},
	doValid:function(){
		var result = {};
		var msg = this.acctPromotionGrid.doValid();
		if(msg != true){
			result["isValid"] = false;
			result["msg"] = msg;
			return result;
		}
		
		var orgStore = this.origAcctPromotionGrid.getStore();	//原节目
		var acctProSel = this.acctPromotionGrid.getSelectionModel().getSelections();	//新选择节目
		var orgAcctitemIds = [],newAcctitemIds = [];
		orgStore.each(function(record){
			orgAcctitemIds.push(record.get('acctitem_id'));
		});
		Ext.each(acctProSel,function(record){
			newAcctitemIds.push(record.get('acctitem_id'));
		});
		var flag = false;
		for(var i=0,len=newAcctitemIds.length;i<len;i++){
			if( newAcctitemIds.indexOf(orgAcctitemIds[i]) == -1 ){
				flag = true;
				break;
			}
		}
		
		if(flag === false){
			result["isValid"] = false;
			result["msg"] = "更换的节目不能和原促销节目相同";
			return result;
		}
		
		return PromotionForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var record = App.getApp().main.infoPanel.getUserPanel().userDetailTab.promotionGrid
				.getSelectionModel().getSelected();
		var acctList = this.acctPromotionGrid.getSelectData();
		var all = {};
		all['acctListJson'] = Ext.encode(acctList);
		all['promotionSn'] = record.get('promotion_sn');
		all['promotionId'] = record.get('promotion_id');
		all['times'] = record.get('times') * 10;
		return all;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var cpf = new PromotionForm();
	var box = TemplateFactory.gTemplate(cpf);
});