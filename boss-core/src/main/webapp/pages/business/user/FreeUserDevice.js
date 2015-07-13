/**
 * 免费终端
 */
FreeUserDeviceForm = Ext.extend( BaseForm , {
	url:  Constant.ROOT_PATH + "/core/x/User!editFreeUser.action",
	zzdUser: null,
	zzdBaseProd: null,
	fzdUser: null,
	baseProdTariffStore:null,
	fzdBaseProd:null,
	nextMonthFirstDate:null,
	constructor: function(){
		var _nowDate = nowDate();
		this.nextMonthFirstDate = _nowDate.clone();
		this.nextMonthFirstDate.setMonth(this.nextMonthFirstDate.getMonth() + 1);
		this.nextMonthFirstDate.setDate(1);
		
		
		this.baseProdTariffStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH + "/core/x/Prod!queryFreeTariff.action",
			fields: ['tariff_id','tariff_name','tariff_desc','billing_cycle',{name:'rent', type: 'float'}]
		});
		
		FreeUserDeviceForm.superclass.constructor.call(this, {
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			items:[{
					xtype:'combo',fieldLabel:'终端分类',hiddenName:'type',id:'typeId',
						store:new Ext.data.ArrayStore({
							fields:['type','type_text'],
							data:[['OUT','超额终端'],['FREE','免费终端']]
						}),lastQuery:'',displayField:'type_text',valueField:'type',allowBlank:false
				},{
				id: 'dynamicForm',
				xtype: 'panel',
				layout: 'form',
				width: '100%',
				baseCls: 'x-plain'
			}]
		});
	},
	initComponent: function(){
		FreeUserDeviceForm.superclass.initComponent.call(this);
		
		var store = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		var that = this;
		store.each(function(item){
			if(item.get("terminal_type") == "ZZD"){
				that.zzdUser = item.data;
				return false;
			}
		});
		this.fzdUser = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		
		var form = this.getForm().findField('type');
		if(this.fzdUser.get('str19') == 'T'){
			form.setValue('OUT');
		}else{
			form.setValue('FREE');
		}
		form.setDisabled(true);
		
		var prodMap = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap;
		if(this.zzdUser){
			var zzdUserProdData = prodMap[this.zzdUser["user_id"]];
			if(zzdUserProdData){
				for(var i = 0; i < zzdUserProdData.length ; i++){
					if(zzdUserProdData[i]["is_base"] == "T"){
						this.zzdBaseProd = zzdUserProdData[i];
						break;
					}
				}
			}
		}
		
		var dyform = Ext.getCmp("dynamicForm");
		if(dyform){
			dyform.removeAll(true);
		}
		var fzdUserProdData = prodMap[this.fzdUser.get('user_id')];
		if(fzdUserProdData){
			for(var i = 0; i < fzdUserProdData.length ; i++){
				if(fzdUserProdData[i]["prod_id"] == this.zzdBaseProd["prod_id"]){
					this.fzdBaseProd = fzdUserProdData[i];
					break;
				}
			}
			dyform.add({
				fieldLabel:'基本包资费',
				xtype : 'combo',
				id : 'prodTariffId',
				store : this.baseProdTariffStore,
				emptyText: '请选择',
				allowBlank : false,
				mode: 'local',
				hiddenName : 'tariff_id',
				hiddenValue : 'tariff_id',
				valueField : 'tariff_id',
				displayField : "tariff_name",
				forceSelection : true,
				triggerAction : "all",
				width: 130
			},{
				xtype : 'datefield',
				fieldLabel : '生效日期',
				format : 'Y-m-d',
				readOnly: true,
				value : this.nextMonthFirstDate,
				allowBlank : false,
				width: 130,
				name: 'tariffStartDate',
				id : 'prodstartdate'
			});
//			this.doLayout();
			
		}else{
			
		}		
		
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		var atvAcctInfo = [];
		for(var i=0;i<acctStore.getCount();i++){
			atvAcctInfo.push(acctStore.getAt(i).data);
		}
		var active_balance = 0;//余额
		var payFee = 0;//往月欠费
		var real_bill = 0;//本月费用
		for(var i=0;i<atvAcctInfo.length;i++){
			if(atvAcctInfo[i].acctitems){
				if(atvAcctInfo[i].user_id == this.fzdUser.get('user_id')){
					if(atvAcctInfo[i].acctitems.length > 0){
						payFee = atvAcctInfo[i].acctitems[0].owe_fee + payFee + atvAcctInfo[i].acctitems[0].real_bill;
						active_balance = active_balance + atvAcctInfo[i].acctitems[0].active_balance;
					}
				}else if(atvAcctInfo[i].acct_type == 'PUBLIC' && atvAcctInfo[i].acctitems.length > 0){
					for(var k=0;k<atvAcctInfo[i].acctitems.length;k++){
						if(atvAcctInfo[i].acctitems[k]['acctitem_id'] == '10000000'){
							active_balance = atvAcctInfo[i].acctitems[k].active_balance + active_balance;
						}
					}
				}
			}
		}
		payFee = payFee - active_balance;
		this.payFee = Ext.util.Format.formatFee(payFee);
		if(this.fzdUser.get('str19') != 'T' && payFee>0){
			dyform.add({
				xtype : 'numberfield',
				fieldLabel : '欠费金额',
				width : 100,
				id:'payFee',
				value:this.payFee,
				name : 'payFee',
				readOnly : true
			});
		}
		
		this.doLayout();
		var baseParams ={};
		var _all = App.getApp().getValues();
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(_all);
		this.baseProdTariffStore.baseParams = baseParams;
		this.baseProdTariffStore.load({
			params:{
				prodId:this.zzdBaseProd["prod_id"],
				userId:this.fzdUser.get('user_id'),
				userType : form.getValue(),
				tariffId:''
			}
		});
		
	},
	doValid:function(){
		return FreeUserDeviceForm.superclass.doValid.call(this);
	},
	getValues: function(){
		var ps = FreeUserDeviceForm.superclass.getValues.call(this);
		ps["user_id"] = this.fzdUser.get('user_id');
		ps["prod_sn"] = this.fzdBaseProd==null?'':this.fzdBaseProd['prod_sn'];
		ps["type"] = Ext.getCmp("typeId").getValue();
		if(Ext.getCmp('payFee')){
			ps['payFee']=Ext.util.Format.formatToFen(Ext.getCmp('payFee').getValue());
		}
		return ps;
	},
	getFee : function(){
		var fee =0 ;
		if (Ext.getCmp('payFee')){
			fee = fee + parseFloat(Ext.getCmp('payFee').getValue());
		} 
		return fee;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});


/**
 * 入口
 */
Ext.onReady(function(){
	var ecf = new FreeUserDeviceForm();
	var box = TemplateFactory.gTemplate(ecf);
});
