/**
 * 封装付费表单面板
 * @class PayBillPanel
 * @extends Ext.FormPanel
 */
PayForm = Ext.extend( Ext.form.FormPanel , {
	payTypeStore : null,
	constructor: function(){
		this.payTypeStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryPayType.action",
			fields : ['pay_type','pay_type_name']
		});
		this.payTypeStore.load();
		
		PayForm.superclass.constructor.call(this, {
			border: false,
//			iconCls: 'icon-pay',
			title: '收费信息',
			bodyStyle: 'background: #F2F2F2;padding: 5px',
			layout: 'column',
			defaults:{
				layout: 'form',
				border: false,
				baseCls: 'x-plain'
			},
			items:[{
				columnWidth: .25,
				labelWidth: 73,
				items:[{
					xtype: 'label',
					style: Constant.MONEY_LABEL_STYLE,
					fieldLabel: '累计需付费',
					text: '0.00', 
					name: 'lblFee'
				},{
					width: 73,
					fieldLabel: '缴费方式',
					xtype: 'combo',
					store : this.payTypeStore,
					name: 'pay_type',
					hiddenValue : 'pay_type',
					valueField : 'pay_type',
					displayField : "pay_type_name",					
					allowBlank: false,
					hiddenName: 'pay.pay_type',
					defaultValue: 'XJ'
				}]
			},{
				columnWidth: .25,
				labelWidth: 60,
				items:[{
					width: 90,
					fieldLabel: '账务日期',
					editable:false,
					xtype:'datefield',
					format:'Y-m-d 00:00:00',
					name: 'pay.acct_date'
				},{
					width: 90,
					fieldLabel: '票据编号',
					maxLength: 18,
					disabled: true,
					xtype: 'textfield',
					name: 'pay.receipt_id'
				}]
			},{
				columnWidth: .25,
				labelWidth: 70,
				items:[{
					width: 90,
					name: 'invoice_mode',
					fieldLabel: '出票方式',
					maxLength: 18,
					xtype: 'paramcombo',
					paramName: 'INVOICE_MODE',
					defaultValue: 'A',
					hiddenName: 'pay.invoice_mode'
				},{
					width: 90,
					fieldLabel: '发票号',
					vtype : 'invoiceId',
					xtype: 'textfield',
					name: 'pay.invoice_id',
					listeners:{
						scope:this,
						'change':this.checkInvoice
					}
				}]
			},{
				columnWidth: .25,
				labelWidth: 60,
				items:[{
					width: 90,
					xtype: 'textfield',
					fieldLabel: '付款人',
					name: 'pay.payer',
					emptyText: '默认为客户名称'
				},{
					width: 90,
					fieldLabel: '发票代码',
					xtype : 'combo',
					store : new Ext.data.JsonStore({
						fields : ['invoice_code','invoice']
					}),
					hiddenName:'pay.invoice_code',
					displayField : 'invoice_code',
					valueField : 'invoice',
					forceSelection : true,
					editable : true
				}]
			}]
			
		});
		var acctdatecmp = this.find("name","pay.acct_date")[0];
		App.acctDate(acctdatecmp);
		this.doInit();
	},
	doInit: function(){
		App.form.initComboData( this.findByType("paramcombo"));
		var custName = App.getData().custFullInfo.cust.cust_name;
		//为缴费方式添加change事件
		var paytypecmd = this.find("name","pay_type")[0];
		paytypecmd.on("select", this.doChangePayType, this);
		
		var payer = this.find("name","pay.payer")[0];
		payer.setValue(custName);
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
	checkInvoice : function() {
		var invoiceCode = this.find("hiddenName", "pay.invoice_code")[0];
		var invoiceid = this.find("name", "pay.invoice_id")[0];
		
		if(invoiceid.isValid()){
			App.checkInvoice(invoiceid,invoiceCode)
//			if(rec.length == 0){
//				Alert('该发票无法使用');
//			}else if(rec.length == 1){
//				invoiceBookId.setValue(rec[0]);
//				invoiceBookId.setReadOnly(true);
//			}else{
//				invoiceBookId.getStore().loadData(rec);
//				invoiceBookId.setReadOnly(false);
//			}
		}
	},
	//private
	amount: null,
	/**
	 * 将所传递的金额替换为累计需付费金额
	 * @param float total
	 */
	setTotalValue: function( total ){
		this.amount += total ;
		this.find("name" ,'lblFee')[0].setText( Ext.util.Format.convertToYuan(this.amount) );
	},
	resetAmount: function(){
		this.amount = 0 ;
		this.setTotalValue( 0 );
	},
	getValues:function(){
		var all = this.getForm().getValues();
		var invoice = all['invoice_book_id'];
		if (invoice!=""){
			var invoice = invoice.split(',');
			all['invoice_book_id']= invoice[0];
			all['invoice_code'] = invoice[1];
		}
		return all;
	},
	doReset: function(){
		this.amount = 0 ;
		this.getForm().reset();
		var cm = this.find("name","pay.receipt_id")[0];
		cm.clearInvalid();
		cm.setDisabled( true );
		cm.allowBlank = true ;
		cm = this.find("name","pay.invoice_id")[0];
		cm.clearInvalid();
		cm.setDisabled( true );
		cm.allowBlank = true ;
		cm = this.find("name","pay.invoice_book_id")[0];
		cm.clearInvalid();
	}
});
