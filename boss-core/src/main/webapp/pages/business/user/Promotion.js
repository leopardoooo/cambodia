/*
 * 促销
 */
 
/**
 * 促销详细信息面板
 * @class PromInfoPanel
 * @extends Ext.Panel
 */
/*PromInfoPanel = Ext.extend(Ext.Panel,{
	acctListStore : null,
	cardListStore : null,
	userFeeListStore : null,//费用优惠是消费过后才能参加的优惠（c_fee中有对应消费记录)
	giftListStore : null,
	promInfoData : null,
	totalAcctFee : null,
	totalAcctCount : null,//赠送产品总数
	acctPromotionGrid: null,
	constructor : function(){
		
		this.acctPromotionGrid = new AcctPromotionGrid();
		
		this.cardListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'card_type'},
				{name : 'card_value', type : 'float'},
				{name : 'promotion_id'}
			]
		});
		cardColumns = [
			{header : '有价卡类型',dataIndex : 'card_type'},
			{header : '金额',dataIndex : 'card_value',renderer : Ext.util.Format.formatFee}
		];
		
		var feeSm = new Ext.grid.CheckboxSelectionModel();
		this.userFeeListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'fee_sn'},
				{name : 'fee_name'},
				{name : 'real_pay', type : 'float'},
				{name : 'should_pay', type : 'float'}
			]
		});
		this.userFeeListStore.on('load',this.doFeeListLoad,this.userFeeListStore);
		userFeeColumns = [feeSm,
			{header : '费用名称',dataIndex : 'fee_name'},
			{header : '优惠前金额',dataIndex : 'should_pay',renderer : Ext.util.Format.formatFee},
			{header : '优惠后金额',dataIndex : 'real_pay',renderer : Ext.util.Format.formatFee}
		];
		this.giftListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'gift_type'},
				{name : 'money', type : 'float'},
				{name : 'amount'},
				{name : 'promotion_id'}
			]
		});
		giftColumns = [
			{header : '礼品类型',dataIndex : 'gift_type'},
			{header : '价值',dataIndex : 'money',renderer : Ext.util.Format.formatFee},
			{header : '数量',dataIndex : 'amount'}
		];
		PromInfoPanel.superclass.constructor.call(this,{
			title : '促销详细信息',
			region : 'south',
			height : 310,
			layout:'column',
			border : false,
			items : [{
				columnWidth : 0.5,
				style:'padding:0px 2px 0px 0px',
				defaults : {
					layout : "fit",
					height : 141
				},
				items : [{
					border : false,
					items : [this.acctPromotionGrid]
				},{
					border : false,
					items : [{
						xtype : 'grid',
						split : true,
						title : '有价卡优惠',
						store : this.cardListStore,
						columns : cardColumns,
						viewConfig:{
				        	forceFit : true
				        }
					}]
				}]
			},{
				columnWidth : 0.5,
				style:'padding:0px 0px 0px 2px',
				defaults : {
					layout : "fit",
					height : 141
				},
				items : [{
					border : false,
					items : [{
						xtype : 'grid',
						title : '营业费优惠',
						id : 'feePromotionGrid',
						store : this.userFeeListStore,
						sm: feeSm,
						columns : userFeeColumns,
						viewConfig:{
				        	forceFit : true
				        }
					}]
				},{
					border : false,
					items : [{
						xtype : 'grid',
						title : '礼品优惠',
						store : this.giftListStore,
						columns : giftColumns,
						viewConfig:{
				        	forceFit : true
				        }
					}]
				}]
			}]
		})
	},
	doFeeListLoad : function(store){
		Ext.getCmp('feePromotionGrid').getSelectionModel().selectAll();
	},
	refresh : function(promInfoData,totalAcctFee,totalAcctCount){
		//加载账目优惠数据，并作判断
		this.acctPromotionGrid.doLoadData(promInfoData.acctList,totalAcctFee,totalAcctCount);
		
		if(promInfoData.cardList){
			this.cardListStore.loadData(promInfoData.cardList);
		}
		if(promInfoData.userFeeList){
			this.userFeeListStore.loadData(promInfoData.userFeeList);
		}
		if(promInfoData.giftList){
			this.giftListStore.loadData(promInfoData.giftList);
		}
	},
	doValid : function(){
		var flag = this.acctPromotionGrid.doValid();
		return flag;
	}
});*/

var PromotionTab = Ext.extend(Ext.TabPanel, {
	acctListStore : null,
	cardListStore : null,
	userFeeListStore : null,//费用优惠是消费过后才能参加的优惠（c_fee中有对应消费记录)
	giftListStore : null,
	promInfoData : null,
	totalAcctFee : null,
	totalAcctCount : null,//赠送产品总数
	acctPromotionGrid: null,
	
	acctList: null,
	totalAcctFee: null,
	totalAcctCount: null,
	cardList: null,
	userFeeList: null,
	giftList: null,
	constructor: function(){
		this.acctPromotionGrid = new AcctPromotionGrid();
		
		this.cardListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'card_type'},
				{name : 'card_value', type : 'float'},
				{name : 'promotion_id'}
			]
		});
		var cardColumns = [
			{header : '有价卡类型',dataIndex : 'card_type'},
			{header : '金额',dataIndex : 'card_value',renderer : Ext.util.Format.formatFee}
		];
		
		var feeSm = new Ext.grid.CheckboxSelectionModel();
		this.userFeeListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'fee_sn'},
				{name : 'fee_name'},
				{name : 'real_pay', type : 'float'},
				{name : 'should_pay', type : 'float'}
			]
		});
		this.userFeeListStore.on('load',this.doFeeListLoad,this.userFeeListStore,{delay:100});
		var userFeeColumns = [feeSm,
			{header : '费用名称',dataIndex : 'fee_name'},
			{header : '优惠前金额',dataIndex : 'should_pay',renderer : Ext.util.Format.formatFee},
			{header : '优惠后金额',dataIndex : 'real_pay',renderer : Ext.util.Format.formatFee}
		];
		this.giftListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'gift_type'},
				{name : 'money', type : 'float'},
				{name : 'amount'},
				{name : 'promotion_id'}
			]
		});
		var giftColumns = [
			{header : '礼品类型',dataIndex : 'gift_type'},
			{header : '价值',dataIndex : 'money',renderer : Ext.util.Format.formatFee},
			{header : '数量',dataIndex : 'amount'}
		];
		PromotionTab.superclass.constructor.call(this,{
			region:'center',
			activeTab:0,
			border:false,
			autoScroll:false,
			defaults: {
				border: false,
				defaults: { border: false,tabWidth:250 }
			},
			items:[
				this.acctPromotionGrid,
				{
					xtype : 'grid',
					split : true,
					title : '有价卡优惠',
					id:'cardPromotionGrid',
					store : this.cardListStore,
					columns : cardColumns,
					viewConfig:{
			        	forceFit : true
			        }
				},
				{
					xtype : 'grid',
					title : '营业费优惠',
					id : 'feePromotionGrid',
					store : this.userFeeListStore,
					sm: feeSm,
					columns : userFeeColumns,
					viewConfig:{
			        	forceFit : true
			        }
				},{
					xtype : 'grid',
					title : '礼品优惠',
					id:'giftPromotionGrid',
					store : this.giftListStore,
					columns : giftColumns,
					viewConfig:{
			        	forceFit : true
			        }
				}
			],
			listeners:{
				scope:this,
				tabchange: this.doTabChange
			}
		});
	},
	doFeeListLoad : function(store){
		Ext.getCmp('feePromotionGrid').getSelectionModel().selectAll();
	},
	refresh : function(promInfoData,totalAcctFee,totalAcctCount){
		this.setActiveTab(0);
		this.acctList = promInfoData.acctList;
		this.totalAcctFee = totalAcctFee;
		this.totalAcctCount = totalAcctCount;
		//加载账目优惠数据，并作判断
		this.acctPromotionGrid.doLoadData(promInfoData.acctList,totalAcctFee,totalAcctCount);
		if( !Ext.isEmpty(promInfoData.cardList) ){
			this.cardList = promInfoData.cardList;
		}
		if( !Ext.isEmpty(promInfoData.userFeeList) ){
			this.userFeeList = promInfoData.userFeeList;
		}

		if( !Ext.isEmpty(promInfoData.giftList) ){
			this.giftList = promInfoData.giftList;
		}
		this.setAutoScroll(false);
	},
	doTabChange: function(tabPanel,panel){
		var panelId = panel.id;
		if(panelId == 'acctPromotionGrid' && !Ext.isEmpty(this.userFeeList) && this.acctPromotionGrid.getStore().getCount() == 0){
			this.acctPromotionGrid.doLoadData(this.acctList,this.totalAcctFee,this.totalAcctCount);
			this.acctList = null;
		}else if(panelId == 'cardPromotionGrid' && !Ext.isEmpty(this.cardList) && this.cardListStore.getCount() == 0 ){
			this.cardListStore.loadData(this.cardList);
			this.cardList = null;
		}else if(panelId == 'feePromotionGrid' && !Ext.isEmpty(this.userFeeList) && this.userFeeListStore.getCount() == 0 ){
			this.userFeeListStore.loadData(this.userFeeList);
			this.userFeeList = null;
		}else if(panelId == 'giftPromotionGrid' && !Ext.isEmpty(this.giftList) && this.giftListStore.getCount() == 0 ){
			this.giftListStore.loadData(this.giftList);
			this.giftList = null;
		}
	},
	doValid : function(){
		var flag = this.acctPromotionGrid.doValid();
		return flag;
	}
});
 
PromotionForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH + "/core/x/User!savePromotion.action",
	promotionStore : null,
	promotionInfoPanel : null,
	promotionId : null,
	times : null,
	constructor : function(){
		this.promotionInfoPanel = new PromotionTab();
		var baseParams ={};
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(App.getApp()
				.getValues());
		
		this.promotionStore = new Ext.data.JsonStore({
			baseParams:baseParams,
			autoLoad :true,
			url : Constant.ROOT_PATH + "/core/x/User!querySelectableProm.action",
			fields : ['promotion_id','promotion_name','promotion_desc','total_acct_fee','total_acct_count','repetition_times']
		})
		this.promotionStore.on('load',this.doLoadResult,this);
		
		PromotionForm.superclass.constructor.call(this,{
			baseCls:'x-plain',
            layout:'border',
            items : [{
            	xtype : 'panel',
            	region : 'north',
            	height:65,
            	title: '业务受理',
            	layout : 'column',
            	bodyStyle : "background:#F9F9F9;",
            	border : false,
            	defaults : {
					baseCls: 'x-plain',
					bodyStyle: "padding: 5px;background:#F9F9F9;",
					columnWidth:0.5,
					labelWidth : 80,
					layout: 'form'
				},
            	items : [{
            		items : [{
		            	xtype : 'combo',
						store : this.promotionStore,
						fieldLabel : '促销名称',
						emptyText: '请选择',
						boxMinWidth : 250,
						minListWidth : 250,
						width:250,
						allowBlank : false,
						mode: 'local',
						hiddenName : 'promotion_id',
						hiddenValue : 'promotion_id',
						valueField : 'promotion_id',
						displayField : "promotion_name",
						forceSelection : true,
						triggerAction : "all",
						listeners:{
							scope : this,
							'select' : this.doSelectProm
						}
	            }]},{
            		items : [{
		            	xtype : 'textfield',
						fieldLabel : '促销描述',
						style : Constant.TEXTFIELD_STYLE,
						readOnly : true,
						id : 'promotionDesc'
            		}]
            	}]
            },this.promotionInfoPanel
            ]
		})
	},
	initComponent : function(){
		PromotionForm.superclass.initComponent.call(this);
	},
	initEvents : function(){
		PromotionForm.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			Ext.getCmp('BusiPanel').showTip();
		},this)
	},
	doSelectProm : function(combo,rec){
		if(this.promotionId == null){
			this.promotionId = rec.get('promotion_id');
			this.times = rec.get('repetition_times');
		}else{
			if(this.promotionId == rec.get('promotion_id')){
				return;
			}else{
				this.promotionId = rec.get('promotion_id');
				this.times = rec.get('repetition_times');
			}
		}
		
		if(rec.get('promotion_desc')){
			Ext.getCmp('promotionDesc').setValue(rec.get('promotion_desc'));
		}
		
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH + "/core/x/User!queryPromInfo.action",
			params : {
				promotionId : rec.get('promotion_id'),
				custId : App.getApp().getCustId(),
				userId : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds()[0]
			},
			success : function(res,opt){
				var res = Ext.decode(res.responseText);
				if(res.success == true){
					this.promotionInfoPanel.refresh(res.simpleObj,Ext.util.Format.formatFee(rec.get('total_acct_fee')),rec.get('total_acct_count'));
				}
			}
		})
		
	},
	doLoadResult : function(){
		Ext.getCmp('BusiPanel').hideTip();
	},
	doValid : function(){
		var result = {};
		var msg = this.promotionInfoPanel.doValid();
		if(msg != true){
			result["isValid"] = false;
			result["msg"] = msg;
			return result;
		}
		return PromotionForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var all  = {};
		all['promotionId'] = this.promotionId;
		all['times'] = this.times;
		var feeList = [];
		var records = Ext.getCmp('feePromotionGrid').getSelectionModel().getSelections();
		for(i=0;i<records.length;i++){
			var data = {};
			data['fee_sn'] = records[i].get('fee_sn');
			data['disct_fee'] = records[i].get('real_pay')
			feeList.push(data);
		}
		all['feeListJson'] = Ext.encode(feeList);
		
		var acctList = this.promotionInfoPanel.acctPromotionGrid.getSelectData();
		all['acctListJson'] = Ext.encode(acctList);
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