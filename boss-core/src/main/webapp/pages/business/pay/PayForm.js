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
		this.busiOptrStore = new Ext.data.JsonStore({
			url:root+'/system/x/Index!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr'],
			autoLoad: true
		});
		var lc = langUtils.main("cashPay.charge.columns");
		PayForm.superclass.constructor.call(this, {
			border: false,
			title: langUtils.main('cashPay.charge._title'),
			bodyStyle: 'background: #F2F2F2;padding:15px 5px 5px 5px',
			labelWidth: 80,
			defaults: {width: 110},
			items:[{
				xtype: 'label',
				style: Constant.MONEY_LABEL_STYLE,
				fieldLabel: lc[0],
				text: '0.00',
				id: 'labelDollor',
				name: 'lblFee'
			},{
				xtype: 'label',
				fieldLabel: lc[1],
				id: 'labelExchange'
			},{
				xtype: 'label',
				fieldLabel: lc[2],
				text: '0.00',
				id: 'LabelJian'
			},{
				xtype: 'textfield',
				fieldLabel: lc[3],
				name: 'pay.payer',
				value: App.getApp().data.custFullInfo.cust.cust_name
			},{
				fieldLabel: lc[4],
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
				fieldLabel: lc[5],
				maxLength: 18,
				disabled: true,
				xtype: 'textfield',
				name: 'pay.receipt_id'
			}/*,{
				fieldLabel: lc[6],
				editable:false,
				xtype:'datefield',
				format:'Y-m-d',
				name: 'pay.acct_date'
			}*/,{
				fieldLabel: lc[7],
				xtype: 'numberfield',
				allowDecimals: false,
				allowNegative: false,
				id: 'nfDollar',
				name: 'pay.usd',
				listeners: {
					scope: this,
					change: this.doCalcJianYuan
				}
			},{
				fieldLabel: lc[8],
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
		all['pay.acct_date'] = nowDate();
		return all;
	}
});
