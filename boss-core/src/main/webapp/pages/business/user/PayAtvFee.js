/**
 * 补收模拟费
 * @class PayAtvFeeForm
 * @extends BaseForm
 */
PayAtvFeeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Acct!payAtvFee.action",
	payPanel : null,
	prodTariffStore : null,
	constructor: function(){
		//产品资费
		this.prodTariffStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/core/x/Prod!queryProdTariff.action",
			fields : ['tariff_id','tariff_name','tariff_desc',
			{name : 'rent',type : 'float'}]
		});
		
		PayAtvFeeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
				xtype : 'combo',
				fieldLabel : '产品名称',
				store : new Ext.data.JsonStore({
					autoLoad : true,
					url:Constant.ROOT_PATH + "/commons/x/QueryUser!queryAtvProds.action",
					fields : ['prod_id','prod_name']
				}),
				hiddenName : 'prodId',
				displayField : 'prod_name',
				valueField : 'prod_id',
				forceSelection : true,
				id : 'prodName',
				allowBlank:false,
				listeners : {
					scope : this,
					'select' : function(combo,rec){
						this.prodTariffStore.load({
							params : {
								prodId : combo.getValue(),
								userId : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds().join(",")
							}
						});
						Ext.getCmp('prodTariffId').setDisabled(false);
						Ext.getCmp('prodTariffId').clearValue();
					}
				}
			},{
				xtype : 'combo',
				id : 'prodTariffId',
				store : this.prodTariffStore,
				fieldLabel : '产品资费',
				emptyText: '请选择',
				disabled : true,
				allowBlank : false,
				mode: 'local',
				hiddenName : 'tariffId',
				hiddenValue : 'tariff_id',
				valueField : 'tariff_id',
				displayField : "tariff_name",
				forceSelection : true,
				triggerAction : "all"
			},{
				xtype : 'datefield',
				fieldLabel : '开始计费日期',
				value : nowDate().format('Y-m-d'),
				format : 'Y-m-d',
				name : 'beginDate',
				allowBlank : false,
				id : 'beginDate',
				listeners : {
					scope : this,
					change : this.setInvalidDate
				}
			},{
				xtype : 'numberfield',
				fieldLabel : '缴费月份数',
				allowBlank : false,
				allowNegative : false,
				id : 'months',
				name : 'months',
				minValue : 1,
				maxValue : 99,
				listeners : {
					scope : this,
					change : this.setInvalidDate
				}
			},{
				xtype : 'datefield',
				name : 'invalidDate',
				id : 'invalidDate',
				format : 'Y-m-d',
				fieldLabel : '预计到期日',
				style : Constant.TEXTFIELD_STYLE,
				readOnly : true
			},{
				//可以输入负数
				fieldLabel : '收费',
				id : 'FeeValue',
				name : 'fee',
				xtype:'numberfield',
				allowBlank : false
			}]
		})
	},
	setInvalidDate : function(){
		var beginDate = Ext.getCmp('beginDate').getValue();
		var months = Ext.getCmp('months').getValue();
		if(beginDate && months){
			beginDate.setMonth(beginDate.getMonth()+months)
			Ext.getCmp('invalidDate').setValue(beginDate);
		}
	},
	getValues : function(){
		var params = this.getForm().getValues();
		if(params.fee){
			params.fee = Ext.util.Format.formatToFen(params.fee);
		}
		return params;
	},
	getFee : function(){
		if(Ext.getCmp('FeeValue')){
			var fee = parseFloat(Ext.getCmp('FeeValue').getValue());
			return fee;
		}
		return 0;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
Ext.onReady(function(){
	var buy = new PayAtvFeeForm();
	var box = TemplateFactory.gTemplate(buy);
});