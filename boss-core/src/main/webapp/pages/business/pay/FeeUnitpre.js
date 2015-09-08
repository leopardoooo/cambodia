/**
 * 预收费
 */
var FeeUnitpreForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!saveGeneralContract.action",
	constructor:function(){
		
		this.payTypeStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryPayType.action",
			fields : ['pay_type','pay_type_name'],
			autoLoad: true
		});
		
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
					fieldLabel:'客户名称',
					name:'generalContract.cust_name',
					allowBlank:false
				},{
					xtype:'textfield',
					fieldLabel:'合同号',
					vtype : 'alphanum',
					name:'generalContract.contract_no',
					allowBlank:false
				},{
					xtype:'numberfield',
					fieldLabel:'合同金额',
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
					fieldLabel:'费用名称',
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
					fieldLabel:'合同名称',
					name:'generalContract.contract_name',
					allowBlank:false
				},{
					xtype:'combo',
					fieldLabel:'所属城市',
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
						fieldLabel: '总额USD',
						value: '0.00',
						id: 'labelDollor',
						name: 'lblFee'
					},{
						xtype: 'displayfield',
						fieldLabel: '柬埔寨KHR',
						value: '0.00',
						id: 'LabelJian'
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
					}]
			},{
				bodyStyle: 'padding-top:10px',
				items:[{
					xtype: 'displayfield',
					fieldLabel: '当日汇率',
					id: 'labelExchange'
				},{
					id:'pay_type_id',
					fieldLabel: '缴费方式',
					xtype: 'combo',
					store : this.payTypeStore,
					hiddenValue : 'pay_type',
					valueField : 'pay_type',
					displayField : "pay_type_name",					
					allowBlank: false,
					hiddenName: 'pay.pay_type',
					triggerAction: 'all',
					mode: 'local',
					listeners: {
						scope: this,
						select: this.doChangePayType,
						'expand': function(combo){
							var store = combo.getStore();
							store.filterBy(function(record){
								return record.get('pay_type') == 'XJ';
							})
						}
					}
				},{
					fieldLabel: '票据编号',
					maxLength: 18,
					disabled: true,
					xtype: 'textfield',
					name: 'pay.receipt_id'
				},{
					fieldLabel: '发票号',
					maxLength: 18,
					xtype: 'textfield',
					name: 'pay.invoice_id',
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
		this.payTypeStore.on("load",function(){
			Ext.getCmp('pay_type_id').setValue("XJ");		
		},this);
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
					Alert('该发票无法使用');
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
		var b = false;
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
		}
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
		console.log(values);
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