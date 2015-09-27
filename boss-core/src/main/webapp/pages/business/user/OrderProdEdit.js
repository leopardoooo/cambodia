/**
 * 订单修改
 */
OrderProdEditForm = Ext.extend(BaseForm, {
	url: Constant.ROOT_PATH+"/core/x/ProdOrder!saveOrderEdit.action",
	baseData: null,
	constructor: function(){
		this.prodStore = new Ext.data.JsonStore({
			fields: ['prod_id', 'prod_name']
		});
		this.tariffStore = new Ext.data.JsonStore({
			fields: ['tariff_id', 'disct_name', 'disct_rent', 'billing_cycle']
		});
		OrderProdEditForm.superclass.constructor.call(this, {
			border: false,
			bodyStyle: 'padding-top:10px',
			labelWidth: 100,
			items: [{
				id: 'cmProdId',
				fieldLabel: '产品',
				xtype: 'combo',
				store: this.prodStore,
				displayField: 'prod_name', valueField: 'prod_id',
				allowBlank: false, listWidth: 200,
				listeners: {
					scope: this,
					select: this.doSelectedProd
				}
			},{
				id: 'cmTariffId',
				fieldLabel: '资费',
				xtype: 'combo',
				store: this.tariffStore,
				displayField: 'disct_name', valueField: 'tariff_id',
				allowBlank: false
			},{
				id: 'nfMonthId',
				fieldLabel: '订购月数',
				xtype: 'numberfield',
				allowNegative: false,
				allowDecimals: false,
				allowBlank: false,
				minValue: 1,
				value: 1,
				listeners: {
					scope: this,
					change: this.doChangeOrderMonth
				}
			},{
				id: 'dfStartDateId',
				fieldLabel: '开始计费日期',
				xtype: 'displayfield'
			},{
				id: 'dfEndDateId',
				fieldLabel: '结束计费日期',
				xtype: 'datefield',
				format: 'Y-m-d',
				editable: false,
				allowBlank: false,
				listeners: {
					scope: this,
					select: this.doSelectEndDate
				}
			},{
				id: 'oldOrderFeeId',
				fieldLabel: '原订单金额',
				xtype: 'textfield',
				readOnly: true
			},{
				id: 'oldTransFeeId',
				fieldLabel: '原转移支付',
				xtype: 'textfield',
				readOnly: true
			},{
				id: 'newOrderFeeId',
				fieldLabel: '新订单金额',
				xtype: 'hidden',
				readOnly: true
			},{
				id: 'dfDiffFeeId',
				fieldLabel: '补收费用',
				xtype: 'numberfield',
				allowBlank: false,
				listeners: {
					scope: this,
					change: this.doChangeDiffFee
				}
			},{
				id: 'realOrderFeeId',
				fieldLabel: '新订单金额',
				xtype: 'textfield',
				readOnly: true,
				allowBlank: false
			}]
		});
	},
	doInit:function(){
		this.doLoadBaseData();
		OrderProdEditForm.superclass.doInit.call(this);
	},
	doLoadBaseData: function(){
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelections()[0];
		Ext.Ajax.request({
			url :root + '/core/x/ProdOrder!queryOrderToEdit.action',
			scope : this,
			params: {
				order_sn: record.get('order_sn')
			},
			success : function(res,opts){
				var data = Ext.decode(res.responseText);
				this.baseData = data;
				var prodList = data['prodList'];
				var tariffList = data['tariffMap'];
				
				Ext.getCmp('cmProdId').getStore().loadData(prodList);
				Ext.getCmp('cmProdId').setValue(data['prod_id']);
				this.doSelectedProd();
			}
		});
	},
	doSelectedProd: function(combo, record, index){
		var prodId = Ext.getCmp('cmProdId').getValue();
		var tariffCmp = Ext.getCmp('cmTariffId');
		this.tariffStore.loadData(this.baseData['tariffMap'][prodId]);
		
		if(this.tariffStore.find('tariff_id', this.baseData['tariff_id']) >= 0){
			tariffCmp.setValue( this.baseData['tariff_id'] );
		}else{
			tariffCmp.setValue('');
		}
		
		Ext.getCmp('oldOrderFeeId').setValue( this.baseData['old_order_fee']/100.0 );
		Ext.getCmp('oldTransFeeId').setValue( this.baseData['old_transfer_fee']/100.0 );
		
		this.baseData['eff_date'] = this.baseData['eff_date'].substring(0, 10);
		Ext.getCmp('dfStartDateId').setValue( this.baseData['eff_date'] );
		
		var minEndDate = Ext.util.Format.getDays(this.baseData['eff_date'], nowDate().format('Y-m-d')) > 0 ? nowDate().format('Y-m-d') : this.baseData['eff_date'];
		Ext.getCmp('dfEndDateId').minValue = Date.parseDate(minEndDate, 'Y-m-d');
		
		Ext.getCmp('dfEndDateId').setValue( Date.parseDate(this.baseData['exp_date'], 'Y-m-d H:i:s') );
	},
	doSelectEndDate: function(df, endDate){
		//选择结束计费日期，计算包月数
		//下个月的前一天算整月(开始日期2015-09-27，结束日期2015-10-26算一个月)
		var endDateTom = endDate.add(Date.DAY, 1);
		var months = Ext.util.Format.getMonths( this.baseData['eff_date'], endDateTom.format('Y-m-d') );
		var days = Ext.util.Format.getDays( this.baseData['eff_date'], endDateTom.add(Date.MONTH, months*-1).format('Y-m-d') );
		
		var orderMonths = Ext.util.Format.round((days/30+months),2);
		Ext.getCmp('nfMonthId').setValue( orderMonths );
		this.calRealOrderFee();
	},
	doChangeOrderMonth: function(filed, newValue, oldValue){
		//根据输入整月数，计算计费结束日期
		var date = Date.parseDate( this.baseData['eff_date'], 'Y-m-d' );
		Ext.getCmp('dfEndDateId').setValue( date.add(Date.MONTH, newValue).add(Date.DAY, -1).format('Y-m-d') );
		this.calRealOrderFee();
	},
	calRealOrderFee: function(){
		//计算新订单金额
		var tariffComb = Ext.getCmp('cmTariffId');
		var tariffRecord = this.tariffStore.getAt( this.tariffStore.find('tariff_id', tariffComb.getValue()) );
		var orderMonths = Ext.getCmp('nfMonthId').getValue();
		var realOrderFee = Ext.util.Format.round(orderMonths*tariffRecord.get('disct_rent')/tariffRecord.get('billing_cycle')/100.0,2);
		Ext.getCmp('newOrderFeeId').setValue( realOrderFee );
		
		//新、旧订单差额; 差额为负表示退款，为正表示补收费用
		Ext.getCmp('dfDiffFeeId').setValue( realOrderFee - Ext.getCmp('oldOrderFeeId').getValue()*1.0 );
		Ext.getCmp('realOrderFeeId').setValue( Ext.getCmp('dfDiffFeeId').getValue()*1.0 + Ext.getCmp('oldOrderFeeId').getValue()*1.0 );
	},
	doChangeDiffFee: function(field, newValue){
		Ext.getCmp('realOrderFeeId').setValue('');
		var realOrderFee = newValue*1.0 + Ext.getCmp('oldOrderFeeId').getValue()*1.0;
		if(realOrderFee < 0){
			Alert('新订单金额不能小于零!');
			return;
		}
		if(realOrderFee < Ext.getCmp('oldTransFeeId').getValue()*1.0){
			Alert('新订单金额不能小于转移金额!');
			return;
		}
		Ext.getCmp('realOrderFeeId').setValue( realOrderFee );
	},
	doValid: function(){
		var formValid =  OrderProdEditForm.superclass.doValid.call(this);
		if(formValid !== true){
			return formValid;
		}
		/*return {
				"isValid": false,
				"msg": lmsg('notChooseDeviceMustBeInput')
		}*/
		return true;
	},
	getValues: function(){
		this.baseData['prod_id'] = Ext.getCmp('cmProdId').getValue();
		this.baseData['tariff_id'] = Ext.getCmp('cmTariffId').getValue();
		this.baseData['pay_fee'] = Ext.getCmp('dfDiffFeeId').getValue()*100;
		this.baseData['order_months'] = Ext.getCmp('nfMonthId').getValue();
		this.baseData['eff_date'] = this.baseData['eff_date']+' 00:00:00';	//JSON解析日期格式 年月日时分秒
		this.baseData['exp_date'] = Ext.getCmp('dfEndDateId').getValue().format("Y-m-d H:i:s");
		return {
			orderProd: Ext.encode(this.baseData)
		}
	},
	success: function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	TemplateFactory.gTemplate(new OrderProdEditForm());
});