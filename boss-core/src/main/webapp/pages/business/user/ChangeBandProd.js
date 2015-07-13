/**
 * 宽带升级
 * @class ChangeBandProdForm
 * @extends BaseForm
 */

ChangeBandProdForm = Ext.extend(BaseForm, {
	url: Constant.ROOT_PATH + "/core/x/User!changeBandProd.action",
	prodStore: null,
	tariffStore: null,
	stcResStore: null,
	oldProd:null,		//原产品
	oldAcctItem:null,	//原产品账目
	userId:null,		//当前用户ID
	constructor: function(){
		var userPanel = App.getApp().main.infoPanel.userPanel;
		this.oldProd = userPanel.prodGrid.getSelectionModel().getSelected();
		this.userId = this.oldProd.get('user_id');
		
		this.oldAcctItem = App.getAcctItemByProdId(this.oldProd.get('prod_id'),this.oldProd.get('user_id'));
		
		this.prodStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/Prod!queryCanOrderBandProd.action",
			fields:['prod_id','prod_name','is_base']		
		});
		this.prodStore.load({
			params:{
				userId: this.userId
			}
		});
		
		var baseParams ={};
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(App.getApp()
				.getValues());
		this.tariffStore = new Ext.data.JsonStore({
			baseParams :baseParams,
			url: Constant.ROOT_PATH+"/core/x/Prod!queryProdTariff.action",
			fields:['tariff_id','tariff_name',{name:'rent',type:'float'},'billing_cycle','tariff_desc']
		});
		this.tariffStore.on('load',function(store){
			if(store.getCount()==1){
				Ext.getCmp('tariffCompId').setValue(store.getAt(0).get('tariff_id'));
				this.doTariffSelect(null,store.getAt(0));
			}
		},this);
		
		this.stcResStore = new Ext.data.JsonStore({
			fields : ["res_desc","res_id","res_name"]
		});
		
		ChangeBandProdForm.superclass.constructor.call(this,{
			bodyStyle:'padding-top:10px',
			labelLabel:85,
			layout:'column',
			items:[
				{columnWidth:.5,layout:'form',border:false,items:[
					{xtype:'combo',hiddenName:'prodId',fieldLabel:'产品名称',
						store:this.prodStore,displayField:'prod_name',valueField:'prod_id',allowBlank:false,
						listeners:{
							scope:this,
							select: this.doSelectProd
						}
					},
					{xtype:'datefield',name:'feeDate',fieldLabel:'开始计费日期',format:'Y-m-d',value:nowDate(),minValue:nowDate().format('Y-m-d'),allowBlank:false},
					{xtype:'datefield',name:'expDate',fieldLabel:'失效日期',format:'Y-m-d',disabled:true},
					{xtype:'displayfield',fieldLabel:'原产品名',value:this.oldProd.get('prod_name')},
					{xtype:'displayfield',fieldLabel:'原产品余额',id:'oldRealBalance',
						value:Ext.util.Format.formatFee(this.oldAcctItem['real_balance'] + this.oldAcctItem['inactive_balance'])},
					{id:'presentFeeId',xtype:'textfield',fieldLabel:'赠送金额',name:'present_fee',readOnly:true}
				]},
				{columnWidth:.5,layout:'form',border:false,items:[
					{id:'tariffCompId',xtype:'combo',fieldLabel:'资费',hiddenName:'tariffId',
						store:this.tariffStore,
						displayField:'tariff_name',valueField:'tariff_id',allowBlank:false,
						listeners:{
							scope:this,
							select:this.doTariffSelect
						}
					},
					{id:'tariff_desc_id',xtype:'displayfield',name:'tariff_desc',fieldLabel:'描述'},
					{id:'disct_price',xtype:'displayfield',name:'tariff_disct_price',fieldLabel:'折扣价格'},
					{xtype:'displayfield',fieldLabel:'原产品资费',value:this.oldProd.get('tariff_name')},
					{id:'is_present_id',xtype:'paramcombo',paramName:'BOOLEAN',fieldLabel:'是否赠送',hiddenName:'is_present',defaultValue:'F',
						listeners:{
							scope:this,
							select:this.doSelectPresent
						}
					}
				]},
				{columnWidth:1,layout:'fit',border:false,items:[
					{
						xtype : 'grid',
						border:false,
						title : '静态资源',
						height : 200,
						store : this.stcResStore,
						columns : [
							{header:'资源ID',dataIndex:'res_id',width:50},
							{header:'资源名字',dataIndex:'res_name',width:100}
						],
						viewConfig : {
							forceFit : true
						}
					}
				]}
				
			]
		});
		if(this.oldProd.get('billing_cycle') > 1){
			Ext.getCmp('oldRealBalance').setValue( Ext.util.Format.formatFee(this.oldAcctItem['active_balance']) );
		}
	},
	doSelectProd: function(combo,record){
		var prodId = record.get('prod_id');
		this.isBase = record.get('is_base');
		
		Ext.getCmp('tariffCompId').setValue('');
		Ext.getCmp('presentFeeId').setValue('');
		Ext.getCmp('tariff_desc_id').setValue('');
		Ext.getCmp('disct_price').setValue('');
		this.getForm().findField('expDate').disable();
		this.getForm().findField('feeDate').setValue(nowDate());
		
		this.tariffStore.load({
			params:{
				prodId: prodId,
				userId: this.userId
			}
		});
		
		//产品静态资源
		Ext.Ajax.request({
			scope: this,
			url: Constant.ROOT_PATH+"/core/x/Prod!queryProdRes.action",
			params: {
				prodId: prodId
			},
			success: function(response,opt){
				var data = Ext.decode(response.responseText);
				if(data && data.length > 0){
					for(var i=0,len=data.length;i<len;i++){
						var res = data[i];
						this.stcResStore.loadData(res.staticResList,false);
					}
				}
			}
		});
	},
	doTariffSelect: function(combo,record){
		var tariffId = record.get('tariff_id');
		var billingCycle = record.get('billing_cycle');
		var rent = parseInt(record.get('rent'));
		
		var isPresent = Ext.getCmp('is_present_id').getValue();
		if(isPresent == 'T'){
			
			//原产品为包多月资费
			if(this.oldProd.get('billing_cycle') > 1){
				
				//所有余额已出账，按到期日折算金额
				if(this.oldAcctItem['owe_fee'] != 0){
					var oldInvalidDate = Date.parseDate(this.oldProd.get('invalid_date'),'Y-m-d H:i:s');
					var today = nowDate();
					//新资费为月包
					if(billingCycle == 1){
						//新增月数
						var months = (oldInvalidDate.getFullYear() - today.getFullYear())*12 + (oldInvalidDate.getMonth() - today.getMonth());
						//新增月数后，剩下的天数
						var days = oldInvalidDate.getDate() - today.add(Date.MONTH, months).getDate();
						if(days < 0){
							months -= 1;
							days = 30 + days;
						}
						//计算赠送金额
						var presentFee = rent * months + rent*days/30 - this.oldAcctItem['owe_fee'];
						Ext.getCmp('presentFeeId').setValue( Ext.util.Format.formatFee(presentFee) );
					}else{
						//新产品单位资费
						var newTariffMonth = record.get('rent') / billingCycle;
						//原产品单位资费
						var oldTariffMonth = this.oldProd.get('tariff_rent') / this.oldProd.get('billing_cycle');
						
						//原产品 实时余额+冻结余额
						var presentFee = (this.oldAcctItem['owe_fee'] + this.oldAcctItem['inactive_balance'])
								* ( (newTariffMonth / oldTariffMonth) - 1) ;
								
						//赠送金额
						Ext.getCmp('presentFeeId').setValue( Ext.util.Format.formatFee(presentFee) );
					}
				}
			}else{
				//欠费账户
				if (this.oldProd && this.oldProd.real_balance < 0) {
					Ext.getCmp('presentFeeId').setValue( Ext.util.Format.formatFee(this.oldAcctItem['owe_fee'] * -1) );
				}else{
					//新产品单位资费
					var newTariffMonth = record.get('rent') / billingCycle;
					//原产品单位资费
					var oldTariffMonth = this.oldProd.get('tariff_rent') / this.oldProd.get('billing_cycle');
					//原产品 实时余额+冻结余额
					var presentFee = (this.oldAcctItem['real_balance'] + this.oldAcctItem['inactive_balance'])
							* ( (newTariffMonth / oldTariffMonth) - 1);
					//赠送金额
					Ext.getCmp('presentFeeId').setValue( Ext.util.Format.formatFee(presentFee) );
				}
			}
		}
		
		//设置资费描述
		Ext.getCmp('tariff_desc_id').setValue(record.get('tariff_desc'));
		
		//设置 失效日期 激活禁用状态
		var expDateComp = this.getForm().findField('expDate');
		//基本包，非包月的去失效日期
		if(this.isBase == 'T' || billingCycle > 1){
			expDateComp.disable();
		}else{
			expDateComp.enable();
		}
		
		//获取资费折扣信息
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/core/x/Prod!queryTariffByTariffIds.action',
			params:{
				custId: App.getApp().getCustId(),
				tariffIds: Ext.encode([tariffId]),
				userIds: Ext.encode([this.userId])
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
	},
	doSelectPresent: function(combo){
		var value = combo.getValue();
		if(value == 'F'){
			Ext.getCmp('presentFeeId').setValue( 0 );
		}else{
			var comp = Ext.getCmp('tariffCompId')
			var tariffId = comp.getValue();
			if(!Ext.isEmpty(tariffId)){
				var record = comp.getStore().getAt(comp.getStore().find('tariff_id',tariffId));
				comp.fireEvent('select',comp,record);
			}
		}
	},
	getValues: function(){
		var values = ChangeBandProdForm.superclass.getValues.call(this);
		values['oldProdSn'] = this.oldProd.get('prod_sn');
		values['present_fee'] = (values['is_present'] == 'T') ? Ext.util.Format.formatToFen(values['present_fee']) : 0;
		return values;
	},
	success: function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var form = new ChangeBandProdForm();
	TemplateFactory.gTemplate(form);
});