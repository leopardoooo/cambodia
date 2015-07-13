/**
*调账可退
*/
AcctKtAdjustFrom = Ext.extend(AdjustFrom ,{
	doInit: function(){
		AcctKtAdjustFrom.superclass.doInit.call(this);
		this.getForm().findField('adjust_fee').setMinValue(0);
	},
	getValues : function(){
		var values = AcctKtAdjustFrom.superclass.getValues.call(this);
		values['fee_type'] = Constant.ACCT_FEETYPE_ADJUST;
		return values;
	}
})

Ext.onReady(function(){
	var aaf = new AcctKtAdjustFrom();
	TemplateFactory.gTemplate(aaf);
});