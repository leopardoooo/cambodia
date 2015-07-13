//套餐、产品订购基类
/**
 * 重写过滤树，根据附加属性过滤
 * @class PinYinFilterTree
 * @extends Ext.ux.FilterTreePanel
 */
PinyinFilterTree = Ext.extend(Ext.ux.FilterTreePanel,{
	//节点过滤
	doFilterTree : function( t , e ){
		var text = t.getValue() ,filter = this.filterObj ;
		if(!text){
			filter.clear();
			return;
		}  
		this.doExpandAll();
		var re = new RegExp('^.*' + text + '.*$','i');
		
		filter.filterBy( function( n ){
			return !n.leaf || re.test( n.attributes.others.attr ) ;
		});
	}
});

/**
 * 重写下拉框树
 * @class PinyinTreeCombo
 * @extends Ext.ux.TreeCombo
 */
PinyinTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	initList: function() {
		var rootVisible = false;
		if(this.rootNodeCfg){
			rootVisible = true;
		}else{
			this.rootNodeCfg = {
				expanded: true
			};
		}
		this.list = new PinyinFilterTree({
			root: new Ext.tree.AsyncTreeNode(this.rootNodeCfg),
			loader: new Ext.tree.TreeLoader({
				dataUrl: this.treeUrl,
				baseParams: this.treeParams
			}),
			floating: true,
			height: this.treeHeight,
			autoScroll:true,
			animate:false,
			searchFieldWidth: this.treeWidth - 80,
			rootVisible: rootVisible,
			listeners: {
				click: this.onNodeClick,
				scope: this
			},
			alignTo: function(el, pos) {
				this.setPagePosition(this.el.getAlignToXY(el, pos));
			}
		});
		if(this.minChars == 0){
			this.doQuery("");
		}
	}
})

/**
 * 产品资费选择面板，用于用户订购产品和客户订购套餐
 * @class BusiOrderProd
 * @extends Ext.Panel
 */
BusiOrderProd = Ext.extend(Ext.Panel,{
	oldProdID : null,
	oldProdName:null,
	tariffId : null,
	node : null,//记录产品节点信息
	resNum : null,//动态资源中需多选的各个值，用于提交验证
	prodTariffStore : null,
	parent : null,
	userIds : null,
	baseProdFreeDay:{DTV:null,BANK:null,ITV:null},//先预定好,可能按照用户类型定义,比如数字电视,宽带
	orderType:null,//区分订购产品和订购套餐
	isBase:null,//是否是基本包
	initComponent:function(){
		BusiOrderProd.superclass.initComponent.call(this);
		Ext.Ajax.request({
			scope:this,
			async: false,
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryProdFreeDay.action",
			success:function(req){
				var data = Ext.decode(req.responseText);
				Ext.apply(this.baseProdFreeDay,data);
			},
			failure:function(){
				Alert('加载产品最大免费时间配置出错!');
			}
		});
	},
	resetMaxProdStartDate:function(){
		var preopenCmp = Ext.getCmp('pre_open_time');
		if(Ext.isEmpty(this.isBase)){
			Alert('请先选择产品');
			preopenCmp.setValue(null);
			return;
		}
		var now = nowDate();
		var preopenCmp = Ext.getCmp('pre_open_time');
		var prodstartdateCmp = Ext.getCmp('prodstartdate');//开始计费日期、预计到期日
		var tariffCmp = Ext.getCmp('prodTariffId');
		var tariffId = tariffCmp.getValue();
		var index = tariffCmp.store.find('tariff_id',tariffId);
		var tariffData = tariffCmp.store.getAt(index);
		
		var prodstartdate = prodstartdateCmp.getValue();
		prodstartdate = Ext.isEmpty(prodstartdate) ? now : prodstartdate;
		var preOpenDate = preopenCmp.getValue();
		preOpenDate = Ext.isEmpty(preOpenDate) ? now:preOpenDate;//如果没有设置预开通日期、就是今天
		var maxStartBillDate = preOpenDate.format('Y-m-d');//最小值
		var minStartBillDate = maxStartBillDate;//如果不是基础产品,没有选择范围,所以最大最小一样
		
		var valueShouldBe = preOpenDate;//开始计费日期、预计到期日的值
		
		
		if(this.isBase == 'T'){
			var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
			var userType = userGrid.getSelections()[0].get('user_type');
			var days = this.baseProdFreeDay[userType];
			if(!Ext.isEmpty(days)){//如果没有配置诸如宽带之类的,默认使用当天(或者已选预开通日期就是预开通日期当天)
				days = parseInt(days);
			}
			
			if(tariffData && tariffData.get('rent') == '0'){//选定了资费并且是零资费
//				minStartBillDate = preOpenDate;//0资费 预计到期日   “下限”  设置为跟预开通的时间一致
				maxStartBillDate = null;
				if(prodstartdate.getTime()> preOpenDate.getTime()){
					valueShouldBe = prodstartdate;
				}else{
					valueShouldBe = preOpenDate;
				}
			}else{//不是令资费的基础产品可以免费看 配置好了的天数
				minStartBillDate = preOpenDate.format('Y-m-d');//0资费 预计到期日   “下限”  设置为跟预开通的时间一致
				if(preOpenDate.getTime() > prodstartdate.getTime()){
					valueShouldBe = preOpenDate;
				}else{
					valueShouldBe = prodstartdate;
				}
				maxStartBillDate = preOpenDate.add(Date.DAY,days).format('Y-m-d');
				prodstartdateCmp.setMaxValue(maxStartBillDate);
			}
		}else{
			//0001582: 增值节目包 需要选择开始计费日期,现在不做限制，可以随便选择
//			if(!tariffData || !tariffData.get('rent') == '0'){
//				prodstartdateCmp.setMaxValue(maxStartBillDate);
//			}
		}
		prodstartdateCmp.setMinValue(minStartBillDate);
		prodstartdateCmp.setValue(valueShouldBe);
	},
	constructor: function(p,orderType){
		this.orderType = orderType;
		this.parent = p;
		var baseParams ={};
		That = this;
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(App.getApp()
				.getValues());
		//产品资费
		this.prodTariffStore = new Ext.data.JsonStore({
			baseParams :baseParams,
			url : Constant.ROOT_PATH+"/core/x/Prod!queryProdTariff.action",
			fields : ['tariff_id','tariff_name','tariff_desc','billing_cycle',
			{name : 'rent',type : 'float'}]
		});
		
		this.prodTariffStore.on('load',this.doLoadData,this);
		//获取选择的用户ID
		var prodUrl=null;
		var panelTitle=null;
		if (orderType=='USER'){//用户订购产品
			var userGrid = App.getApp().main.infoPanel.getUserPanel().userGrid;
			var userType = userGrid.getSelections()[0].get('user_type');
			var servType = '';
			if(userType == 'DTV'){
				var records = userGrid.getSelections();
				if(records.length > 1){//多个，判断是否含有双向用户，有则为single
					for(var i=0;i<records;i++){
						if(records[i].get('serv_type') == 'SINGLE'){
							servType = 'SINGLE';
							break;
						}
					}
					if(servType == ''){
						servType = 'DOUBLE';
					}
				}else{
					servType = records[0].get('serv_type');
				}
			}else{
				servType = userGrid.getSelections()[0].get('serv_type');
			}
			this.userIds = userGrid.getSelectedUserIds().join(",");
			panelTitle = '共选择了'+userGrid.getSelections().length+'个用户';
			prodUrl = Constant.ROOT_PATH+"/core/x/Prod!queryProdTree.action?userId="+this.userIds+"&userType="+userType+"&servType="+servType;
		} else {//客户订购套餐
			var custId = App.getApp().getCustId();
			prodUrl = Constant.ROOT_PATH+"/core/x/Prod!queryProdTree.action?custId="+custId;
			panelTitle = "客户名称："+App.getData().custFullInfo.cust.cust_name;
		}
		BusiOrderProd.superclass.constructor.call(this,{
			border: false,
			id : 'busiOrderProd',
			title:panelTitle ,
			bodyStyle: "background:#F9F9F9",
			layout : 'border',
			items:[{
				region : 'center',
				border : false,
				layout : 'column',
				defaults : {
					baseCls: 'x-plain',
					bodyStyle: "background:#F9F9F9;padding-top:4px",
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 105
				},
				items : [{
					items : [new PinyinTreeCombo({
						id : 'prodIdTreeCombo',
				    	fieldLabel: '产品名称',
				    	readOnly : true,
						treeWidth:300,
						minChars:0,
						allowBlank: false,
						treeUrl: prodUrl,
						listeners : {
							'focus' : function(){
								if(this.list){
									this.expand();
								}
								var expDateComp = Ext.getCmp('exp_date');
								if(expDateComp && !expDateComp.disabled){
									expDateComp.setValue(null);
									expDateComp.disable();
								}
							},
							'select' : function(node,obj){
								Ext.getCmp('prodIdTreeCombo').hasFocus = false;
								var pordId = obj.attributes.id.split(',');
								if(!Ext.getCmp('busiOrderProd').oldProdID){
									Ext.getCmp('busiOrderProd').oldProdID = pordId[1];
									Ext.getCmp('busiOrderProd').oldProdName = this.lastSelectionText;
									Ext.getCmp('busiOrderProd').node =  obj.attributes;
								}else{
									if(Ext.getCmp('busiOrderProd').oldProdID == pordId[1]){
										return;
									}
									Ext.getCmp('busiOrderProd').oldProdName = this.lastSelectionText;
									Ext.getCmp('busiOrderProd').oldProdID = pordId[1];
									Ext.getCmp('busiOrderProd').node =  obj.attributes;
								}
								That.isBase = obj.attributes.others['is_base'];
								Ext.getCmp('busiOrderProd').doSelectProd();
								Ext.getCmp('busiOrderProd').resetMaxProdStartDate();
							}
						}
					})
					]
				},{
					items : [{
						xtype : 'combo',
						id : 'prodTariffId',
						store : this.prodTariffStore,
						fieldLabel : '资费',
						emptyText: '请选择',
						disabled : true,
						allowBlank : false,
						mode: 'local',
						hiddenName : 'tariff_id',
						hiddenValue : 'tariff_id',
						valueField : 'tariff_id',
						displayField : "tariff_name",
						forceSelection : true,
						triggerAction : "all",
						listeners:{
							'select' : function(combo,rec){
								Ext.getCmp('busiOrderProd').doSelectProdTariff(rec);
							}
						}
					}]
				}]
			},{
				region : 'south',
				height : 45,
				border : false,
				bodyStyle: Constant.TAB_STYLE,
				layout : 'column',
				defaults : {
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form'
				},
				items : [{
					labelWidth: 95,
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '开始计费日期',
						minValue : nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						value : nowDate(),
						allowBlank : false,
						id : 'prodstartdate'
					}]
				},{
					labelWidth: 105,
					items : [{
						xtype : 'textarea',
						fieldLabel : '资费描述',
						style : Constant.TEXTFIELD_STYLE,
						disabled : true,
						grow : true,
						preventScrollbars : true,
						height : 40,
						width : 125,
						readOnly : true,
						id : 'prodTariffDesc'
					}]
				}]
			}]
		})
	},
	doLoadData : function(store,recs){
		if(recs.length == 1){
			Ext.getCmp('prodTariffId').setValue(recs[0].get('tariff_id'));
			this.doSelectProdTariff(recs[0]);
			this.resetMaxProdStartDate();
		}
	},
	doSelectProd : function(){//选中产品后的触发事件
		this.tariffId = null;
		//加载资费信息
		this.loadTariffRec(this.oldProdID);
		
		if (this.orderType =="USER"){//如果是用户订购产品
			this.parent.resPanel.reset();
			var win  = Ext.getCmp('prodChildId');
			if(win){
				Ext.getCmp('ProdMessagePanel').remove(win);
			}
				
			
			var acctBank = App.getApp().data.custFullInfo.acctBank;
			var bankPayCom = Ext.getCmp('orderProdItemId');
			if(acctBank && acctBank.status=='ACTIVE' && this.node.others.is_bank_pay == 'T'){
				bankPayCom.show();	
				Ext.getCmp('isBankPayId').setValue("T");
				Ext.getCmp('isBankPayId').disable();
			}else{
				Ext.getCmp('isBankPayId').setValue("F");
				bankPayCom.hide();
			}
			this.doLayout();
			var comboes = this.findByType("paramcombo");
			if(comboes.length > 0){
				App.form.initComboData(comboes);
			}
			
			//刷新资源面板
			Ext.Ajax.request({
				scope : this,
				url : Constant.ROOT_PATH+"/core/x/Prod!queryProdRes.action",
				params : {
					prodId : this.oldProdID
				},
				success : function(response,opt){
					var res = Ext.decode(response.responseText);
					this.parent.refreshResPanel(res);
				}
			});
			//刷新子产品面板
			this.parent.refreshSubProdPanel();
		} else {//客户订购套餐
			prodStore.removeAll();
		}
	},
	doSelectProdTariff : function(rec){//选中产品资费后的触发事件
		this.tariffId = rec.get('tariff_id');
		
		var expDateComp = Ext.getCmp('exp_date');
		var billingCycle = rec.get('billing_cycle');
		//基本包，非包月的去失效日期
		if(this.isBase == 'T' || billingCycle > 1){
			if(expDateComp && !expDateComp.disabled){
				expDateComp.disable();
			}
		}else{
			if(expDateComp && expDateComp.disabled){
				expDateComp.enable();
			}
		}
		var preOpenTime = Ext.getCmp('pre_open_time');
		if(preOpenTime){
			preOpenTime.setValue(null);
			preOpenTime.setMinValue(nowDate().add(Date.DAY,1).format('Y-m-d'));
		}
			
		this.items.itemAt(1).removeAll();
		
		// 0资费免费终端，默认30年后
		var isFree = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections()[0].get('str19');
		var defaultInvalidDate = this.node.others.invalid_date;
		if(isFree == 'T' && this.isBase == 'T' && rec.get('rent')==0){
			defaultInvalidDate = nowDate();
			defaultInvalidDate.setYear(defaultInvalidDate.getFullYear() + 30);
			defaultInvalidDate.setDate(defaultInvalidDate.getDate() - 1);
		}
		if(this.node.others.just_for_once=='T'){
			
			this.items.itemAt(1).add({
				items : [{
					xtype : 'textfield',
					width : 120,
					fieldLabel : '预计到期日',
					value : defaultInvalidDate,
					readOnly :true,
					id : 'prodstartdate'
				}]
			});
		}else{
			if(rec.get('rent') > 0){
				this.items.itemAt(1).add({
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '开始计费日期',
						minValue :nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						value : nowDate(),
						allowBlank : false,
						id : 'prodstartdate'
					}]
				});
			}else{
				this.items.itemAt(1).add({
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '预计到期日',
						minValue : nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						value: defaultInvalidDate,
						allowBlank : false,
						id : 'prodstartdate'
					}]
				})
			}
		}
		this.items.itemAt(1).add({
			items : [{
				xtype : 'textarea',
				fieldLabel : '资费描述',
				style : Constant.TEXTFIELD_STYLE,
				disabled : true,
				grow : true,
				preventScrollbars : true,
				height : 40,
				width : 125,
				readOnly : true,
				id : 'prodTariffDesc'
			}]
		})
		Ext.getCmp('prodTariffDesc').setValue(rec.get('tariff_desc'));
		if (this.orderType !="USER"){//如果是订购套餐
			this.parent.changeProd(this.oldProdID,this.tariffId);
		}
		this.doLayout();
		if(this.doShowDisct){
			this.doShowDisct(this.tariffId);
		}
	},
	loadTariffRec : function(prodId){//加载资费数据
		Ext.getCmp('prodTariffId').setDisabled(false);
		Ext.getCmp('prodTariffId').clearValue();
		Ext.getCmp('prodIdTreeCombo').setDisabled(true);
		Ext.getCmp('prodIdTreeCombo').setDisabled(false);
		this.prodTariffStore.load({
			params : {
				prodId : prodId,
				userId : this.userIds
			}
		});
	}/*,
	doShowDisct : function(tariffId){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/core/x/Prod!queryTariffByTariffIds.action',
			params:{
				custId:App.getApp().getCustId(),
				tariffIds:Ext.encode([tariffId]),
				userIds:Ext.encode(this.userIds.split(","))
			},
			scope:this,
			success:function(res,options){
				var disctData = Ext.decode(res.responseText);
				if(disctData.length > 0){
					if(disctData[0]['disctList']){
						Ext.getCmp('disct_price').setValue(Ext.util.Format.formatFee(disctData[0]['disctList'][0]['final_rent']));
					}else{
						Ext.getCmp('disct_price').setValue('');
					}
					
				}
			}
		})
	}*/
});