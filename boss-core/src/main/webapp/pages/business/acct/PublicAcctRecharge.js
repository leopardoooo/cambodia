/**
 * 公用账目充值
 */
 
PublicRechargeForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/ProdOrder!savePublicRecharge.action",
	constructor:function(){
		var acctStore = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getStore();
		var record = acctStore.getAt(0);
		
		var acctId = record.get('acct_id');
		this.acctitemId = record.get('acctitem_id');
		PublicRechargeForm.superclass.constructor.call(this,{
			labelWidth:120,
			border: false,
			bodyStyle:'padding-top:15px',
			defaults:{layout:'form',border:false},
			items:[{
				xtype:'hidden',
				name:'acct_id',
				value:acctId
			},{
				xtype:'hidden',
				name:'acctitem_id',
				value:this.acctitemId
			},{
				xtype : 'paramcombo',
				fieldLabel:'充值方式',
				allowBlank : false,
				paramName:'PUBLIC_PAY_FEE',
				hiddenName:'pay_type',
				width:150,
				defaultValue: 'XJ',
				listeners: {
					scope: this,
					select: this.doChangePayType
				}
			},{
				fieldLabel: '凭据号',
				maxLength: 18,
				width:150,
				disabled: true,
				xtype: 'textfield',
				name: 'receipt_id'
			},{
				xtype : 'numberfield',
				id : 'rechargeFeeValue',
				fieldLabel : '充值金额',
				width : 150,
				name:'fee',
				allowNegative : false,
				allowBlank : false,
				minValue : 0
			}]
		});
	},
	doInit: function(){
		PublicRechargeForm.superclass.doInit.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	success:function(){
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	doChangePayType: function(cb, record, index){
		var b = false;
		var receiptId = this.find("name", "receipt_id")[0];
		var feeItem = this.find("name","fee")[0];
		feeItem.reset();
		receiptId.reset();
		var noreceopttype = 'XJ';
		if (noreceopttype.indexOf(record.get('item_value'))>-1 ) {
			b = true;
			receiptId.clearInvalid();
		}
		receiptId.setDisabled(b);
		receiptId.allowBlank = b;
		if (!b) {
			receiptId.focus();
		}
	},
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = "含有验证不通过的输入项";
		}
		if(this.getForm().findField('fee').getValue()<=0){
			obj["isValid"] = false;
			obj["msg"] = "金额需要大于0!";
		}
		return obj;
	},
	getValues : function(){
		var all = this.getForm().getValues();
		all['fee'] = Ext.util.Format.formatToFen(all['fee']) ;
		return all;
	}
});



Ext.onReady(function(){
	var payFees = new PublicRechargeForm();
	TemplateFactory.gTemplate(payFees);
})