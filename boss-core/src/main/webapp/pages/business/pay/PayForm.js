/**
 * 封装付费表单面板
 * @class PayBillPanel
 * @extends Ext.FormPanel
 */
PayForm = Ext.extend( Ext.form.FormPanel , {
	payTypeStore : null,
	parent: null,
	constructor: function(parent){
		this.parent = parent;
		PayForm.superclass.constructor.call(this, {
			border: false,
//			iconCls: 'icon-pay',
			title: '收费信息',
			bodyStyle: 'background: #F2F2F2;padding:15px 5px 5px 5px',
			labelWidth: 80,
			defaults: {width: 110},
			items:[{
				xtype: 'label',
				style: Constant.MONEY_LABEL_STYLE,
				fieldLabel: '总额USD',
				text: '0.00',
				id: 'labelDollor',
				name: 'lblFee'
			},{
				xtype: 'label',
				fieldLabel: '当日汇率',
				id: 'labelExchange'
			},{
				xtype: 'label',
				fieldLabel: '柬埔寨KHR',
				text: '0.00',
				id: 'LabelJian'
			},{
				xtype: 'textfield',
				fieldLabel: '付款人',
				name: 'pay.payer',
				value: App.getApp().data.custFullInfo.cust.cust_name,
				emptyText: '默认为客户名称'
			},{
				fieldLabel: '缴费方式',
				xtype: 'paramcombo',
				paramName:'CFF_PAY_FEE',
				allowBlank: false,
				hiddenName: 'pay.pay_type',
				defaultValue: 'XJ',
				listeners: {
					scope: this,
					select: this.doChangePayType
				}
			},{
				fieldLabel: '票据编号',
				maxLength: 18,
				disabled: true,
				xtype: 'textfield',
				name: 'pay.receipt_id'
			},{
				fieldLabel: '账务日期',
				editable:false,
				xtype:'datefield',
				format:'Y-m-d',
				name: 'pay.acct_date'
//			},{
//				name: 'invoice_mode',
//				fieldLabel: '出票方式',
//				maxLength: 18,
//				xtype: 'paramcombo',
//				paramName: 'INVOICE_MODE',
//				defaultValue: 'A',
//				hiddenName: 'pay.invoice_mode'
			},{
				fieldLabel: '实收USD',
				xtype: 'numberfield',
				decimalPrecision: 0,
				id: 'nfDollar',
				name: 'pay.usd',
				listeners: {
					scope: this,
					change: this.doCalcJianYuan
				}
			},{
				fieldLabel: '实收KHR',
				xtype: 'numberfield',
				name: 'pay.khr',
				decimalPrecision: 0,
				id: 'nfJianYuan'
			},{
				xtype: 'hidden',
				id: 'hdExchange',
				name: 'pay.exchange'
			}]
			
		});
		var acctdatecmp = this.find("name","pay.acct_date")[0];
		App.acctDate(acctdatecmp);
	},
	initComponent: function(){
		PayForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	doCalcJianYuan: function(field, newValue, oldValue){
		var exchange = this.parent.feeData["EXC"];
		var sumDollar = this.parent.feeData["FEE"]/100.0;
		var inputDollar = Ext.getCmp("nfDollar");
		var inputJianYuan = Ext.getCmp("nfJianYuan");
		var inputDollarValue = inputDollar.getValue() || 0;
		var inputJianYuanValue = inputJianYuan.getValue();
		// 规则，优先取美元，如果美元不足时自动生成柬埔寨瑞尔，如果超出，则瑞尔显示找零
		if(inputDollarValue > sumDollar || inputDollarValue < sumDollar){
			var v = (sumDollar - inputDollarValue) * exchange;
			// 做四舍五入
			var modV = v % 100;
			if(modV >= 50){
				v = v - modV + 100;
			}else{
				v = v - modV;
			}
			inputJianYuan.setValue( v );
		}else{
			inputJianYuan.setValue( "0");
		}
	},
	doChangePayType: function(cb, record, index){
		/*var b = false;
		var receiptId = this.find("name", "pay.receipt_id")[0];
		var noreceopttype = 'XJ,SHIFT,PRESENT';
		if (noreceopttype.indexOf(record.get('pay_type'))>-1 ) {
			b = true;
			receiptId.clearInvalid();
		}
		receiptId.setDisabled(b);
		receiptId.allowBlank = b;
		if (!b) {
			receiptId.focus();
		}*/
	},
	getValues:function(){
		var all = this.getForm().getValues();
		
		return all;
	}
});
