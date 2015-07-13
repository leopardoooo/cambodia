/**
*小额减免
*/
AcctEasyAdjustFrom = Ext.extend(AdjustFrom ,{
	doInit: function(){
		AcctEasyAdjustFrom.superclass.doInit.call(this);
		var acctRecord = App.getApp().main.infoPanel.acctPanel.acctItemGrid.getSelectionModel().getSelected();
		var value = App.getApp().findCfgData('BASE_EASY_ADJUST_DAYS').config_value;
		var maxValue = Ext.util.Format.formatFee((acctRecord.get('tariff_rent')*value)/(acctRecord.get('billing_cycle')*30));
		this.getForm().findField('adjust_fee').setMinValue(0);
		this.getForm().findField('adjust_fee').setMaxValue(maxValue);
	},
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = "含有验证不通过的输入项";
		}
		if(this.getForm().findField('adjust_fee').getValue()==0){
			obj["isValid"] = false;
			obj["msg"] = "调账金额不能为0!";
		}
		return obj;
	},
	getValues : function(){
		var values = AcctEasyAdjustFrom.superclass.getValues.call(this);
		values['fee_type'] = Constant.ACCT_FEETYPE_ADJUST_EASY;
		return values;
	}
})

Ext.onReady(function(){
	var aaf = new AcctEasyAdjustFrom();
	TemplateFactory.gTemplate(aaf);
});