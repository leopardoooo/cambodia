/**
 * 【用户产品订购】
 */

/**
 * 资源面板
 * @class ResPanel
 * @extends Ext.Panel
 */
ResPanel = Ext.extend(Ext.Panel,{
	dycResStore : null,
	stcResStore : null,
	parent:null,
	constructor : function(p){
		this.parent = p;
		this.stcResStore = new Ext.data.JsonStore({
			fields : ["res_desc","res_id","res_name"]
		});
		this.dycResStore = new Ext.data.GroupingStore({
			reader: new Ext.data.JsonReader({},[
				{name:'prod_id'},{name:'res_desc'},{name:'res_id'},
					{name:'res_name'},{name:'group_name'},{name : 'res_number',type : 'float'}
			]),
			groupField : 'group_name'
		});
		var sm = new Ext.grid.CheckboxSelectionModel()
		ResPanel.superclass.constructor.call(this,{
			border: false,
			baseCls: 'x-plain',
			layout:'border',
			bodyStyle: "background:#F9F9F9",
			items : [{
				region : 'west',
				border : false,
				width:"30%",
				layout : 'fit',
				items : [{
					xtype : 'grid',
					title : '静态资源',
					height : 240,
					store : this.stcResStore,
					columns : [
						{header:'资源ID',dataIndex:'res_id',hidden : true},
						{header:'资源名字',dataIndex:'res_name'}
					],
					viewConfig : {
						forceFit : true
					}
				}]
			},{
				split:true,
				region : 'center',
				layout: 'fit',
				border: false,
				items : [{
					xtype : 'grid',
					title : '动态资源选择',
					id : 'dynamicGrid',
					height : 250,
					store : this.dycResStore,
					sm : sm,
					columns : [sm,
						{header:'资源ID',dataIndex:'res_id',hidden : true},
						{header:'资源名称',dataIndex:'res_name'},
						{header:'组名',dataIndex:'group_name'}
					],
					view: new Ext.grid.GroupingView({
			            forceFit:true
			            ,groupTextTpl: '{text} (共{[values.rs.length]}项，请选择{[values.rs[0].data["res_number"]]}项)'
		        	})
				}]
			},{
				region:'east',
				width:'9%',
				layout:'fit',
				border:false,
				items:[{
					xtype:'button',
					iconCls:'icon-buy',
					listeners:{
							scope:this
							,click:this.addBuy
					}
				}]
			}]
		})
	},addBuy:function(){
		var prodId = Ext.getCmp('busiOrderProd').oldProdID;
		var tariffId = Ext.getCmp('prodTariffId').getValue();
		var store = Ext.getCmp('ProdMessagegridId').getStore();key = false;
		store.each(function(record) {
				if(prodId == record.get('prod_id')){key = true};
		});
		if(key){Alert('在暂存库中已经存在该产品!'); return;}
		var msg = this.parent.doValid()["msg"];
		if(!this.parent.doValid()["isValid"]){
			if(!Ext.isEmpty(msg)){
				Alert(this.parent.doValid()["msg"]);
			};
			return;
		};
		var prodName = Ext.getCmp('busiOrderProd').oldProdName;
		var tariffName = Ext.getCmp('prodTariffId').lastSelectionText;
		var feeDate = Ext.getCmp('prodstartdate').getValue();
		var preOpenDate = Ext.getCmp('pre_open_time').getValue();
		var expDate = Ext.getCmp('exp_date').getValue();
		var bankCom = Ext.getCmp('isBankPayId');
		var isBankPay,isBankPayText;
		if(bankCom){
			isBankPay = bankCom.getValue();
			isBankPayText = bankCom.lastSelectionText;
		}
		
		var record = [];dyresList = [];dRes = '';sRes = '';
		var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
		for(var i=0;i<records.length;i++){
			var dyres = {};
			dyres['prodId'] = Ext.getCmp('busiOrderProd').oldProdID;
			var obj = {};
			obj["res_id"] = records[i].get('res_id');
			dRes += records[i].get('res_name')+',';
			dyres["rscList"] = [obj];
			dyresList.push(dyres);
		}
		var list = Ext.encode(dyresList);
		this.stcResStore.each(function(record) {
						sRes = 	sRes.concat(record.get('res_name'), ',');				
				});
		dRes = dRes.substring(0, dRes.length-1);
		sRes = sRes.substring(0, sRes.length-1);
		
		var recordType = store.recordType;
		var record = new recordType({
			prod_id:prodId,prod_name:prodName,tariff_id:tariffId,tariff_name:tariffName,
			fee_date:feeDate,DresNames:dRes,SresNames:sRes,dynamicRscList:list,
			pre_open_time:preOpenDate,is_bank_pay:isBankPay,is_bank_pay_text:isBankPayText,
			exp_date:expDate
		});
		store.add(record);
		Ext.getCmp('ProdMessagePanel').activate(Ext.getCmp('ProdMessagepanelId'));
	}
	,
	doLoadData : function(recs){
		for(var k=0;k<recs.length;k++){
			//加载静态资源
			var res = recs[k];
			this.stcResStore.loadData(res.staticResList,true);
			
			var records = [];
			for(var i=0;i<res.dynamicResList.length;i++){
				var groupName = res.dynamicResList[i].group_name;
				var resList = res.dynamicResList[i].resList;
				for(var j=0;j<resList.length;j++){
					var record =resList[j];
					record['group_name'] = groupName;
					record['res_number'] = res.dynamicResList[i].res_number;
					record['prod_id'] = res['prod_id'];
					records.push(record);
				}
			}
			this.dycResStore.loadData(records,true);
		}
	},
	reset : function(){
		this.stcResStore.removeAll();
		this.dycResStore.removeAll();
	}
})

var ProdMessagegrid = Ext.extend(Ext.grid.GridPanel,{
			prodStore:null,
			constructor:function(){
				prodThat = this;
				this.prodStore = new Ext.data.JsonStore({
					fields : ['prod_name', 'tariff_name', 'prod_id','tariff_id','fee_date','dynamicRscList','DresNames','SresNames','pre_open_time','is_bank_pay','is_bank_pay_text']
				});
			  var cm = new Ext.grid.ColumnModel([
				{header : '产品名称',dataIndex : 'prod_name',renderer : App.qtipValue},
				{header : '资费',width : 70,dataIndex : 'tariff_name',renderer : App.qtipValue},
				{header : '开始计费日期',width : 70,dataIndex : 'fee_date',renderer:Ext.util.Format.dateFormat},
				{header : '预开通时间',width : 70,dataIndex : 'pre_open_time',renderer:Ext.util.Format.dateFormat},
				{header : '银行扣费',width:70,dataIndex:'is_bank_pay_text',renderer:App.qtipValue},
				{header : '失效日期',width : 70,dataIndex : 'exp_date',renderer:Ext.util.Format.dateFormat},
				{header : '静态资源',dataIndex : 'SresNames',renderer : App.qtipValue},
				{header : '动态资源',dataIndex : 'DresNames',renderer : App.qtipValue},
				{header : '操作',width : 80,
					renderer : function(v, md, record, i) {
						var rs = Ext.encode(record.data);
						return String.format("&nbsp;<a href='#' onclick='prodThat.deleteProd({0},{1});' style='color:blue'> 删除 </a>",rs, i);
					}
				}
		        ]);
				ProdMessagegrid.superclass.constructor.call(this,{
					id : 'ProdMessagegridId',
					store : this.prodStore,
					cm : cm,
					sm : new Ext.grid.CheckboxSelectionModel(),
					viewConfig : {forceFit : true},
					autoScroll : true,
					border : true
				})
			},
			deleteProd : function() {
				var rec = this.getSelectionModel().getSelected();
				this.prodStore.remove(rec);
			}

})

ProdMessage = Ext.extend(Ext.TabPanel,{
	prodMessagegrid:null,
	constructor:function(){
		this.prodMessagegrid = new ProdMessagegrid(this);
		ProdMessage.superclass.constructor.call(this,{
			id : 'ProdMessagePanel',
			region : 'south',
			height : 250,
			border : false,
			activeTab: 0,
			defaults: {
				border: false,
				defaults: { border: false }
			},
			items : [{
				xtype : 'panel',
				title : '产品暂存库',
				border : false,
				id:'ProdMessagepanelId',
				layout : 'fit',
				items : [this.prodMessagegrid]
				}]
		})
	
	}

})


/**
 * 订购产品表单
 * @class OrderProdForm
 * @extends BaseForm
 */
OrderProdForm = Ext.extend(BaseForm,{
	busiOrderProd: null,
	resPanel : null,
	prodMessage:null,
	orderProdId: null,
	url: Constant.ROOT_PATH + "/core/x/User!orderProd.action",
	constructor : function() {
		this.busiOrderProd = new BusiOrderProd(this, "USER");
		this.resPanel = new ResPanel(this);
		this.prodMessage = new ProdMessage(this);
		var tomorrow = nowDate().add(Date.DAY,1);
		
		var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
		var userType = userGrid.getSelections()[0].get('user_type');
		var preOpenTimeDisabled = userType == 'ATV';//模拟没有预开通的概念
		
		OrderProdForm.superclass.constructor.call(this, {
					border : false,
					layout : 'border',
					items : [
					{layout: 'anchor',
						region : 'north',
						height : 150,
						items:[{
							anchor:'100% 65%',
							layout:'fit',
							border : false,
							items:[this.busiOrderProd]
						},{
							hidden: true,
							anchor:'100% 20%',
							border : false,
							bodyStyle: "background:#F9F9F9",
							layout:'column',
							defaults : {
								baseCls: 'x-plain',
								bodyStyle: "background:#F9F9F9;",
								columnWidth:0.5,
								layout: 'form',
								border : false,
								labelWidth: 105
							},
							items:[{
								items : [{
									xtype:'datefield',
									fieldLabel: '失效日期',
									disabled:true,
									width:120,
									id:'exp_date',
									format:'Y-m-d'
								}]
							}
							]
						},
						{
							
							anchor:'100% 15%',
							border : false,
							bodyStyle: "background:#F9F9F9",
							layout:'column',
							defaults : {
								baseCls: 'x-plain',
								bodyStyle: "background:#F9F9F9;",
								columnWidth:0.5,
								layout: 'form',
								border : false,
								labelWidth: 105
							},
							items:[
								{
								items : [{
									xtype:'datefield',
									fieldLabel: '预开通日期',
									width:120,
									disabled:preOpenTimeDisabled,
									minValue:tomorrow.format('Y-m-d'),
									id:'pre_open_time',
									listeners:{
										scope:this,
										select:function(field,value){
											var prodstartdate = this.getForm().findField('prodstartdate');
											prodstartdate.setMinValue(value.format('Y-m-d'));
											this.busiOrderProd.resetMaxProdStartDate();
											var fee_date = prodstartdate.getValue();
											if(!Ext.isDate(fee_date) || (fee_date.getTime() - value.getTime()<0 ) ){
												prodstartdate.setValue(value);
											}
										}
									},
									format:'Y-m-d'
								}]
							},{
								id:'orderProdItemId',
								items:[{
									fieldLabel : '银行扣费',
									xtype : 'paramcombo',
									width : 100,
									id:'isBankPayId',
									paramName : 'BOOLEAN',
									defaultValue : 'T',
									hiddenName : 'is_bank_pay'
								}]
							}
							]
						}
						
						]
					},
								
									/*{
								region : 'north',
								layout : 'fit',
								height : 100,
								items : [this.busiOrderProd]
							},*/ {
								region : 'center',
								layout : 'fit',
								items : [this.resPanel]
							}, {
								layout : 'fit',
								region : 'south',
								height : 120,
								items : [this.prodMessage]
							}]
				});
	},doInit : function(){		
		Ext.getCmp('orderProdItemId').hide();
		Ext.getCmp('isBankPayId').setValue("F");
	},
	refreshResPanel : function(data){
		this.resPanel.doLoadData(data);
	},
	refreshSubProdPanel : function(){
		that = this;
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH+"/core/x/Prod!querySubProds.action",
			params : {
				prodId : Ext.getCmp('busiOrderProd').oldProdID
			},
			success : function(response,opt){
				var rec = Ext.decode(response.responseText);
				if(rec.length > 0){
					that.createSubProdPanel(rec);
				}
			}
		});
	},
	createSubProdPanel : function(rec){
		var subProdStore = new Ext.data.JsonStore({
			fields : ["prod_name","prod_type","prod_type_text","is_base_text","prod_desc"]
		});
		subProdStore.loadData(rec);
		var grid = new Ext.grid.GridPanel({
			title : '子产品信息',
			height : 100,
			region : 'south',
			id:'prodChildId',
			layout : 'fit',
			store : subProdStore,
			columns : [
				{header : '产品名称',dataIndex : 'prod_name'},
				{header : '产品描述',dataIndex : 'prod_desc'},
				{header : '基本产品',dataIndex : 'is_base_text'}
			],
			viewConfig:{
	        	forceFit : true
	        }
		})
		Ext.getCmp('ProdMessagePanel').add(grid);
		Ext.getCmp('ProdMessagePanel').activate(grid);
	},
	getValues : function(){
		var all = {};
		//wang  批量订购产品
		var store = Ext.getCmp('ProdMessagegridId').getStore();
		var List = [];
		if(store.getCount()>0){
			for(var i=0;i<store.getCount();i++){
				var record = store.getAt(i);
				var objs = {};
				objs['prodId'] = record.get('prod_id');
				objs['tariffId'] = record.get('tariff_id');
				objs['feeDate'] = record.get('fee_date').format('Y-m-d H:i:s');
				var preOpenDate = record.get('pre_open_time');
				if(preOpenDate && Ext.isDate(preOpenDate)){
					objs['preOpenTime'] = preOpenDate.format('Y-m-d H:i:s');
				}
				var expDate = record.get('exp_date');
				if(expDate && Ext.isDate(expDate)){
					objs['expDate'] = expDate.format('Y-m-d H:i:s');
				}
				objs['isBankPay'] = record.get('is_bank_pay');
				objs['dynamicRscList'] = record.get('dynamicRscList');
				List.push(objs);
			}
			all["pordLists"] = Ext.encode(List);
			this.orderProdId = List[0]['prodId'];
		}else {
			this.orderProdId = all["prodId"] = Ext.getCmp('busiOrderProd').oldProdID;
			all["tariffId"] = Ext.getCmp('prodTariffId').getValue();
			all["feeDate"] = Ext.getCmp('prodstartdate').getValue();
			all["preOpenTime"] = Ext.getCmp('pre_open_time').getValue();
			all["expDate"] = Ext.getCmp('exp_date').getValue();
			if( Ext.getCmp('isBankPayId')){
				all["isBankPay"] =  Ext.getCmp('isBankPayId').getValue();
			}
			var dyresList = [];
			var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
			for(var i=0;i<records.length;i++){
				var dyres = {};
				dyres['prodId'] = records[i].get('prod_id');
				var obj = {};
				obj["res_id"] = records[i].get('res_id');
				dyres["rscList"] = [obj];
				dyresList.push(dyres);
			}
			all["dynamicRscList"] = Ext.encode(dyresList);
		}
		return all;
	},
	doValid: function(){		
		var result = {};
		var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
		var store = Ext.getCmp('dynamicGrid').getStore();
		
		if(store.getCount() > 0){
			var firstRec = store.getAt(0);
			var firstObj = {};
			firstObj['group_name'] = firstRec.get('group_name');
			firstObj['res_number'] = firstRec.get('res_number');
			var array = [];
			array.push(firstObj);
			var groupName = firstRec.get('group_name');
			store.each(function(rec){
				if(rec.get('group_name') != groupName){
					groupName = rec.get('group_name');
					var obj = {};
					obj['group_name'] = rec.get('group_name');
					obj['res_number'] =  rec.get('res_number');
					array.push(obj);
				}
			})
			if(records.length == 0){
				for(var i=0;i<array.length;i++){
					if(array[i].res_number > 0){
						result["isValid"] = false
						result["msg"] = array[i].group_name+"请按要求选择项目";
						return result;
					}
				}
			}else{
				for(var i=0;i<array.length;i++){
					var total = 0;
					for(var j=0;j<records.length;j++){
						if(array[i].group_name == records[j].get('group_name')){
							total = total + 1;
						}
					}
					if(array[i].res_number != total){
						result["isValid"] = false
						result["msg"] = array[i].group_name+"请按要求选择项目";
						return result;
					}
				}
			}
		}
		
		if(result.isValid == false){
			return result;
		}else{
			result["isValid"] = this.getForm().isValid();
			return result;
		}
	},
	success : function(form,resultData){
		var orderProd = App.getData().busiTask['OrderProd'];
		orderProd['callback'] = {
			fn : App.getApp().selectRelativeUser,
			params : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds()
		};
		
		var payFees = App.getData().busiTask['PayFees'];
		payFees['callback'] = {
			fn : App.getApp().selectRelativeAcct
		};
		
		if(App.getData().custFullInfo.cust['county_id'] == '0102'){
			if(form.orderProdId == '8100'){
				alert('请参加 直属主或第二终端基本包满288元，6选2满12元 促销');
			}else if(form.orderProdId == '8120'){
				alert('请参加 直属主或第二终端基本包满288，12选6满120 促销');
			}
		}
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
/**
 * 入口
 */
Ext.onReady(function(){
	var opf = new OrderProdForm();
	
	var box = TemplateFactory.gTemplate(opf);

});
