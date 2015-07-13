
/**
 * 缴费方式 常量定义
 */
PayType = {};
//现金缴费
//TODO 配置各个busiCode需要的出票方式


/**
 * 封装付费表单面板
 * @class PayBillPanel
 * @extends Ext.FormPanel
 */
PayBillForm = Ext.extend( BaseForm , {
	//FIXME 临时性对策,一些业务默认使用手工票的出票方式,暂时配置在这个JS里.以后要修改.
	//busiCode 如果true 就是手工票 M,否则(包括没有配置的)
	busiCodeDefaultInvoceMode:{
		//设备 配件 客户开户 修改费用  杂费退款 
//		'1007':true,//购买设备 ok
//		'1108':true//配件购买	OK
	},
	payTypeStore : null,
	parent : null,
	total : null,//金额
	getDefaultInvoiceMode:function(){
		var busiCode = App.getData().currentResource.busicode;
		if(this.busiCodeDefaultInvoceMode[busiCode] == true){
			return 'M';
		}
		return 'A';
	},
	constructor: function(parent){
		
		this.parent = parent;
		this.payTypeStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryPayType.action",
			fields : ['pay_type','pay_type_name','is_print']
		});
		this.payTypeStore.load();
		this.payTypeStore.on("load",this.loadData,this);
		
		this.busiOptrStore = new Ext.data.JsonStore({
			url:root+'/system/x/Index!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr']
		});
		this.busiOptrStore.load();
		this.busiOptrStore.on("load",function(){
//			var busiOptrId = App.getData().busiOptrId;
//			if(Ext.isEmpty(busiOptrId)){	//默认选中当前登录操作员
//				busiOptrId = App.getData().optr.optr_id;
//			}
//			this.getForm().findField('busi_optr_id').setValue(busiOptrId);
		},this);
		
		PayBillForm.superclass.constructor.call(this, {
			border: false,
			labelWidth: 70,
			bodyStyle: 'background: #F2F2F2;padding: 5px',
			layout: 'column',
			defaults: {
				layout: 'form',
				border: false,
				baseCls: 'x-plain'				
			},
			items:[{
				columnWidth: .5,defaultType: 'textfield',
				defaults: {
					width: 90
				},
				items:[{
					xtype: 'hidden',
					name: 'fee'
				},{
					xtype: 'label',
					style: Constant.MONEY_LABEL_STYLE,
					fieldLabel: '需付费',
					name: 'lblFee'
				},{
					fieldLabel: '缴费方式',
					name: 'pay_type',
					xtype: 'combo',
					id:'payTypeId',
					store : this.payTypeStore,
					hiddenName: 'pay_type',
					hiddenValue : 'pay_type',
					valueField : 'pay_type',
					displayField : "pay_type_name",
					allowBlank: false
				},{
					name: 'invoice_mode',
					fieldLabel: '出票方式',
					xtype: 'combo',
					hiddenName: 'invoice_mode',
					store:[['A','机打票']],
					value:"A"
				}]
			},{
				columnWidth: .5,defaultType: 'textfield',
				defaults: {
					width: 90
				},
				items:[{
					fieldLabel: '账务日期',
					xtype:'datefield',
					format:'Y-m-d H:i:s',
					editable:false,
					allowBlank:false,
					name: 'acct_date'
				},{
					fieldLabel: '票据编号',
					maxLength: 18,
					disabled: true,
					name: 'receipt_id'
				},{
					fieldLabel: '付款人',
					name: 'payer',
					emptyText: '默认为客户名称'
				}]
			},
			{columnWidth: 1,
				items:[{
					hiddenName: 'busi_optr_id',
					fieldLabel: '业务员',
					xtype:'lovcombo',
					store:this.busiOptrStore,
					valueField:'optr_id',displayField:'optr_name',
					editable:true,forceSelection:true,
					beforeBlur:function(){},
					width:200,
					scope:this,
					listeners:{
						beforequery:function(e){
							var combo = e.combo;
							var store = combo.getStore();
				            var value = e.query;
					        if(Ext.isEmpty(value)){ 
								store.clearFilter();
					        }else{
					            combo.collapse();
					        	var re = new RegExp('^.*' + value + '.*$','i');
					            store.filterBy(function(record,id){
					                var text = record.get('attr');
					                return re.test(text);
					            });
					            combo.expand();
					            return false;
					        }
						}
					}
				}]
			}]
		});
		var acctdatecmp = this.find("name","acct_date")[0];
		App.acctDate(acctdatecmp);
	},
	doInit: function(){
		//设置付款人名字
		var custName = App.getData().custFullInfo.cust.cust_name;
		var busiCode = App.getApp().getData().currentResource.busicode;
		if (busiCode=='1001')
			custName = Ext.getCmp('cust.cust_name').getValue();
		if (busiCode=='1227'||busiCode=='1228')
			custName = App.getApp().main.valuableCardGrid.custName
		var payer = this.find("name","payer")[0];
		payer.setValue(custName);
		
		//为缴费方式添加change事件
		var cmb = this.find("name","pay_type")[0];
		cmb.on("select", this.doChangePayType, this);
		
		if(busiCode == '1048'){
			cmb.setReadOnly(true);
		}else{
			cmb.setReadOnly(false);
		}
	},
	//支付方式选择事件
	doChangePayType: function(cb, record, index){
		var b = false;
		var receiptId = this.find("name","receipt_id")[0];
		var noreceopttype = 'XJ,SHIFT,PRESENT,DK,UNPAY,DEZS,DELEYPAY';
		if (noreceopttype.indexOf(record.get('pay_type'))>-1 ) {
			b = true;
			receiptId.clearInvalid();
		}

		receiptId.setDisabled( b );
		receiptId.allowBlank = b ;
		if( !b ){
			receiptId.focus();
		}
		
		b = false;
		var invoiceMode = this.find("name","invoice_mode")[0];
		var noPrint = 'CZK,SHIFT,UNITPRE,PRESENT,DJQ,DK,UNPAY,DEZS';
		if (noPrint.indexOf(record.get('pay_type')) > -1) {
			invoiceMode.setDisabled(true);
			b = true;
		} else {
			if(App.getApp().isUseInvoice() == 'T'){			
				invoiceMode.setDisabled(false);
				if (invoiceMode.getValue()=='A'){
					b=true;
				}
			}else{
				b=true;
			}
		}
	},
	/**
	 * 将所传递的金额替换为累计需付费金额
	 * @param float total
	 */
	setTotalValue: function( total ){
		this.total = total;
		this.find("name" ,'lblFee')[0].setText( total);
		if(total<0)
			this.lockPayType();
	},
	lockPayType:function(){
		var comp = this.find("hiddenName" ,'pay_type')[0];
		comp.defaultValue='XJ';
		comp.disabled=true;
	},
	doValid: function(){
		var value = this.getForm().findField('busi_optr_id').getValue();
		var arr = value.split(',');
		if(arr.length > 1){
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '业务员最多选择1个!';
			return obj;
		}
		return PayBillForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var all= PayBillForm.superclass.getValues.call(this);
		var invoice = all['invoice_book_id'];
		if (invoice && invoice!=""){
			var invoice = invoice.split(',');
			all['invoice_book_id']= invoice[0];
			all['invoice_code'] = invoice[1];
		}
		return all;
	},
	loadData:function(store){
		Ext.getCmp('payTypeId').setValue("XJ");
		
		if(App.getApp().getData().currentResource.busicode == '1030'){
			var index = store.find('pay_type','SHIFT');
			store.removeAt(index);
			
			Confirm('本次操作收取现金【'+this.total+'】抵充模拟欠费，如果确定点“是”。如果不收请点“否”，将欠费调账后再操作！',this,function(){
			},function(){
				this.parent.close();
			})
		}
	}
});

/**
 * 缴费面板
 * @class WinPayBill
 * @extends Ext.Window
 */
WinPayBill = Ext.extend( Ext.Window , {
	parent : null,
	payBillForm: null,
	invoiceForm: null,
	
	constructor: function(parent ){
		
		this.parent = parent;
		
		this.payBillForm = new PayBillForm(this);
		this.invoiceForm = new InvoiceForm();
		
		WinPayBill.superclass.constructor.call(this, {
			height: 220,
			width: 450,
			resizable: false,
			maximizable: false,
			closeAction : 'close',
			autoScroll: false,
//			iconCls: 'icon-pay',
			title: '支付信息',
			layout: 'fit',
			items: this.payBillForm,
			buttons:[
//				{
//				width: 80,
//				scope: this,
//				text: '暂不支付',
//				handler: this.doOnlySave
//			},
				{
				width: 80,
				text: '支付',
				scope: this,
				handler: this.doSaveAndPrint
			}]
		});
	},
	doOnlySave: function(){
		Confirm("确定要保存业务吗?", this , function(){
			//暂不支付模式
			this.saveModel = "1";
			this.parent.doSave();
			this.close();
		});
	},
	doSaveAndPrint: function(){
		var form = this.payBillForm.getForm();
		var msg = this.payBillForm.doValid();
		if(msg == false) {
			return;
		}else if(!msg.isValid&&msg.msg){
			Alert(msg.msg);
			return;
		};
//		Confirm("确定要保存业务吗?", this , function(){
//			var invoiceid = this.find("name", "invoice_id")[0];
//			App.getApp().useInvoice(invoiceid.getValue());
			
			App.getData().busiOptrId = form.findField('busi_optr_id').getValue();
			//支付并打印标示
			this.saveModel = "2";
			this.parent.doSave();
			this.close();
			
			
//		});
	},
	show: function( fee ){
		WinPayBill.superclass.show.call(this);
		var feeCmp = this.find('name','fee')[0];
		feeCmp.setValue(Ext.util.Format.formatToFen(fee));
		this.payBillForm.setTotalValue( fee + "");
	},
	getValues: function(){
		if(this.saveModel == "1"){
			return null;
		}else if(this.saveModel == '2'){
			return this.payBillForm.getValues();
		}
	},
	//成功后的执行函数
	success: function(form,data){
		var menu = App.getApp().menu;
		menu.hideBusiWin();
//		App.getApp().refreshFeeView();
//		if(this.saveModel == "2" && this.payBillForm.find("name","invoice_mode")[0].value=='A' ){
//			var invoiceId =this.payBillForm.find("name", "invoice_id")[0].getValue();
//			var invoiceBookId =this.payBillForm.find("hiddenName", "invoice_book_id")[0].getValue();
//			menu.bigWindow.toggleUrl( "打印" , {width: 700, height: 450},
//				"/pages/business/pay/Print.jsp?type=through&invoiceId="+invoiceId+"&invoiceBookId="+invoiceBookId );
//		}else{ 
//			menu.hideBusiWin();
//		}
	}
});


