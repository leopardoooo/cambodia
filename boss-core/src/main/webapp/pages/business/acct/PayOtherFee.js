/**
 * 杂费收取
 */
PayOtherFeeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/ProdOrder!savePayOtherFee.action",
	constructor: function(){
		PayOtherFeeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
                xtype: 'displayfield',
                width : 150,
                value:"<font style='font-size:16px;color:red'><b>只用于右侧收取杂费</b></font>",
                id:'feeDescId'
			}]
		})
	},
	getValues : function(){
		var all = this.getForm().getValues();
		return all;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
 
 
Ext.onReady(function(){
	var cpf = new PayOtherFeeForm();
	var box = TemplateFactory.gTemplate(cpf);
});