/**
 * 副机用户同步主机产品
 * @class SyncZzdProdForm
 * @extends BaseForm
 */
SyncZzdProdForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!saveProdSyn.action",
	zzdProdGrid : null,
	zzdProdStore : null,
	fzdUserGrid : null,
	fzdUserStore : null,
	constructor : function(){
		this.doInitGrid();
		
		SyncZzdProdForm.superclass.constructor.call(this,{
			border:false,
			layout:'border',
			items : [{
				border : false,
				region : 'center',
				layout : 'fit',
				items : [this.zzdProdGrid]
			},{
				border : false,
				region : 'east',
				width : '55%',
				split : true,
				layout : 'fit',
				items : [this.fzdUserGrid]
			}]
		})
	},
	initEvents : function(){
//		this.on("afterrender",function(){
//			if(this.zzdProdStore.getCount() > 0){
//				this.zzdProdGrid.getSelectionModel().selectAll();
//			}
//			if(this.fzdUserStore.getCount() > 0){
//				this.fzdUserGrid.getSelectionModel().selectAll();
//			}
//		},this,{delay:2000});
		
		SyncZzdProdForm.superclass.initEvents.call(this);
	},
	doInitGrid : function(){
		this.zzdProdStore = new Ext.data.JsonStore({
			fields:['prod_name','prod_sn']
		});
		
		var zzdProdSm = new Ext.grid.CheckboxSelectionModel();
		var zzdProdColumns = [zzdProdSm,{
			header:'产品名称',width:150,dataIndex:'prod_name'
		}];
		this.zzdProdGrid = new Ext.grid.GridPanel({
			title : '主机产品信息',
			layout : 'fit',
			border : false,
			store : this.zzdProdStore,
			columns : zzdProdColumns,
			sm : zzdProdSm
		});
		
		this.fzdUserStore = new Ext.data.JsonStore({
			fields : ['user_id','user_name','card_id']
		});
		var fzdUserSm = new Ext.grid.CheckboxSelectionModel();
		var fzdUserColumns = [fzdUserSm,{
			header:'用户名',width:100,dataIndex:'user_name'
		},{
			header:'卡号',width:150,dataIndex:'card_id'
		}];
		this.fzdUserGrid = new Ext.grid.GridPanel({
			title : '副机用户',
			layout : 'fit',
			border : false,
			store : this.fzdUserStore,
			columns : fzdUserColumns,
			sm : fzdUserSm
		})
		
		this.doLoadData();
		
	},
	doLoadData : function(){
		var userPanel = App.getApp().main.infoPanel.getUserPanel();
		var record =  userPanel.userGrid.getSelections()[0];
		var prodData = userPanel.prodGrid.prodMap[record.get('user_id')];
		if(prodData.length > 0){
			this.zzdProdStore.loadData(prodData);
		}
		
		
		var userStore = userPanel.userGrid.getStore();
		var userData = [];
		userStore.each(function(record){
			if(record.get('user_type') =='DTV' && record.get('terminal_type') !='ZZD'
				&& userPanel.prodGrid.prodMap[record.get('user_id')]){
				userData.push(record.data);
			}
		})
		
		if(userData.length > 0){
			this.fzdUserStore.loadData(userData);
		}
		
	},
	doValid : function(){
		var obj = {};
		if(this.zzdProdGrid.getSelectionModel().getSelections().length == 0){
			obj["isValid"] = false;
			obj["msg"] = "主机产品至少选择一个";
			return obj;
		}
		if(this.fzdUserGrid.getSelectionModel().getSelections().length == 0){
			obj["isValid"] = false;
			obj["msg"] = "副机用户至少选择一个";
			return obj;
		}
		
		return SyncZzdProdForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var params = {};
		var prodSns = [];
		var userIds = [];
		var prods = this.zzdProdGrid.getSelectionModel().getSelections();
		var users = this.fzdUserGrid.getSelectionModel().getSelections();
		
		for(var i=0;i<prods.length;i++){
			prodSns.push(prods[i].get('prod_sn'));
		}
		for(var i=0;i<users.length;i++){
			userIds.push(users[i].get('user_id'));
		}
		params['prodSns'] = prodSns;
		params['userIds'] = userIds;
		return params;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})


Ext.onReady(function(){
	var adf = new SyncZzdProdForm();
	var box = TemplateFactory.gTemplate(adf);
});
