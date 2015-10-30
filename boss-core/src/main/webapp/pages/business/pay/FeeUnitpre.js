/**
 * 预收费
 */
var FeeUnitpreForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!saveGeneralContract.action",
	constructor:function(){
		var forms = lbc('home.tools.feeUnitpre.forms');
		FeeUnitpreForm.superclass.constructor.call(this,{
			id:'feeUnitpreFormId',
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults: {
				layout: 'form',
				columnWidth: .5,
				border: false,
				labelWidth:100,
				baseCls:'x-plain',
				defaults:{
					width:120
				}
			},
			layout: 'column',
			items:[{
				xtype : 'hidden',
				id : 'feeType',
				name : 'generalContract.fee_type'
			},{
				xtype : 'hidden',
				id : 'feeId',
				name : 'generalContract.fee_id'
			},{
				items:[{
					xtype:'textfield',
					fieldLabel: forms['cust_name'],
					name:'generalContract.cust_name',
					allowBlank:false
				},{
					xtype:'textfield',
					fieldLabel: forms['contract_no'],
					vtype : 'alphanum',
					name:'generalContract.contract_no',
					allowBlank:false
				},{
					xtype:'numberfield',
					fieldLabel: forms['nominal_amount'],
					minValue : 1,
					id : 'nominalAmount',
					name:'generalContract.nominal_amount',
					allowBlank:false,
					listeners : {
						scope : this,
						change : this.doNumChange
					}  
				}]
			},{
				items:[{
					xtype:'combo',
					fieldLabel: forms['fee_name'],
					store : new Ext.data.JsonStore({
						autoLoad : true,
						url : Constant.ROOT_PATH+"/core/x/Acct!queryUnBusiFee.action",
						fields : ['fee_name','fee_type','fee_id','printitem_id']
					}),
					displayField : 'fee_name',
					valueField : 'fee_name',
					allowBlank:false,
					forceSelection : true,
					listeners:{
						scope:this,
						select: function(combo,record){
							Ext.getCmp('feeId').setValue(record.get('fee_id'));
							Ext.getCmp('feeType').setValue(record.get('fee_type'));
						}
					}
				},{
					xtype:'textfield',
					fieldLabel: forms['contract_name'],
					name:'generalContract.contract_name',
					allowBlank:false
				},{
					xtype:'combo',
					fieldLabel: forms['addr_district'],
					store : new Ext.data.JsonStore({
						autoLoad : true,
						url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrDistrict.action",
						fields : ['addr_id','addr_name']
					}),
					displayField : 'addr_name',
					valueField : 'addr_id',
					hiddenName:'generalContract.addr_district',
					editable : false,
					triggerAction : 'all',
					allowBlank:false,
					forceSelection : true
				}]
			},
			{
				columnWidth:1,
				bodyStyle:'padding-top:10px;',
				style:'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 1px solid;'
			},{
				bodyStyle: 'padding-top:10px',
				items:[{
						xtype: 'displayfield',
						style: Constant.MONEY_LABEL_STYLE,
						fieldLabel: forms['lblFee'],
						value: '0.00',
						id: 'labelDollor',
						name: 'lblFee'
					},{
						xtype: 'displayfield',
						fieldLabel: forms['LabelJian'],
						value: '0.00',
						id: 'LabelJian'
					},{
						fieldLabel: forms['nfDollar'],
						xtype: 'numberfield',
						decimalPrecision: 0,
						id: 'nfDollar',
						name: 'pay.usd',
						listeners: {
							scope: this,
							change: this.doCalcJianYuan
						}
					},{
						fieldLabel: forms['nfJianYuan'],
						xtype: 'numberfield',
						name: 'pay.khr',
						decimalPrecision: 0,
						id: 'nfJianYuan'
					}]
			},{
				bodyStyle: 'padding-top:10px',
				items:[{
					xtype: 'displayfield',
					fieldLabel: forms['labelExchange'],
					id: 'labelExchange'
				},{
					id:'pay_type_id',
					fieldLabel: forms['pay_type'],
					xtype: 'paramcombo',
					paramName:'UNBUSI_PAY_FEE',
					allowBlank: false,
					hiddenName: 'pay.pay_type',
					defaultValue: 'XJ',
					listeners: {
						scope: this,
						select: this.doChangePayType
					}
				},{
					fieldLabel: forms['receipt_id'],
					maxLength: 18,
					disabled: true,
					xtype: 'textfield',
					name: 'pay.receipt_id'
				},{
					fieldLabel: forms['invoice_id'],
					maxLength: 18,
					xtype: 'textfield',
					name: 'pay.invoice_id',
					allowBlank: false,
					listeners:{
						scope: this,
						change: this.checkInvoice
					}
					
				},{
					xtype: 'hidden',
					id: 'hdExchange',
					name: 'pay.exchange'
				}]
			}]
		});
		this.loadBaseData();
	},
	exchangeRate: null,		//汇率
	loadBaseData: function(){
		// 请求后台的数据
		Ext.Ajax.request({
			scope: this,
			url: Constant.ROOT_PATH + "/core/x/Pay!queryBaseFeeData.action",
			params: {cust_id: App.getCustId()},
			success: function(res, ops){
				this.exchangeRate = Ext.decode(res.responseText);
				Ext.getCmp("labelExchange").setValue("1USD=" + this.exchangeRate + "KHR");
				Ext.getCmp("hdExchange").setValue(this.exchangeRate);
			}
		});
	},
	doNumChange : function(field, newValue, oldValue){
		Ext.getCmp('nfDollar').setValue(newValue);
		Ext.getCmp('nfJianYuan').setValue(0);
		
		Ext.getCmp("labelDollor").setValue(Ext.util.Format.usMoney(newValue));
		Ext.getCmp("LabelJian").setValue(Ext.util.Format.usMoney(newValue * this.exchangeRate).substr(1));
	},
	doCalcJianYuan: function(field, newValue, oldValue){
		var sumDollar = Ext.getCmp('nominalAmount').getValue();
		var inputDollar = Ext.getCmp("nfDollar");
		var inputJianYuan = Ext.getCmp("nfJianYuan");
		var inputDollarValue = inputDollar.getValue() || 0;
		var inputJianYuanValue = inputJianYuan.getValue();
		// 规则，优先取美元，如果美元不足时自动生成柬埔寨瑞尔，如果超出，则瑞尔显示找零
		if(inputDollarValue > sumDollar || inputDollarValue < sumDollar){
			var v = (sumDollar - inputDollarValue) * this.exchangeRate;
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
	checkInvoice: function(txt){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
			params:{
				invoice_id:txt.getValue(),
				doc_type:'2',
				invoice_mode:"A"	
			},
			success:function(res,ops){
				var rec = Ext.decode(res.responseText);
				var invoiceCode = Ext.getCmp('new_invoice_code');
				if(rec.length == 0){
					Alert(lbc('home.tools.feeUnitpre.msg.receiptCantUsed'));
				}else{
					var data = [];
					for(var i=0;i<rec.length;i++){
						var obj = {};
						obj['invoice_code'] = rec[i].invoice_code;
						obj['invoice_book_id'] = rec[i].invoice_book_id;
						obj['invoice_type'] = rec[i].invoice_type;
						obj['invoice_type_text'] = rec[i].invoice_type_text
						data.push(obj);
					}
					invoiceCode.setValue('');
					invoiceCode.getStore().loadData(data);
				}
			},
			clearData:function(){
				txt.setValue("");
				var invoiceCode = Ext.getCmp('new_invoice_code');
				invoiceCode.setValue('');
				invoiceCode.getStore().removeAll();
			}
		});
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
		var values = this.getForm().getValues();
		values['generalContract.nominal_amount'] = Ext.util.Format.formatToFen(values['generalContract.nominal_amount']);
		
		values['credentialAmount'] = Ext.util.Format.formatToFen(values['credentialAmount']);
		values['presentAmount'] = Ext.util.Format.formatToFen(values['presentAmount']);
		
		var endCmp = Ext.getCmp('CredentialEndNo');
		if(endCmp && !endCmp.getValue()){
			values['credentialEndNo'] = 0;
		}
		values["pay.khr"] = values["pay.khr"] * 100;
		values["pay.usd"] = values["pay.usd"] * 100;
		return values;
	},
	getFee : function(){
		return Ext.getCmp('nominalAmount').getValue();
	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().load();
	}
});

Ext.onReady(function(){
	var panel = new FeeUnitpreForm();
	var box = TemplateFactory.gTemplate(panel);
});