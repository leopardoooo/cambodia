/**
 * 客户状态 资料隔离状态为正常
 * @class
 * @extends BaseForm
 */
var RenewCustForm = Ext.extend(BaseForm,{
	constructor:function(){
		RenewCustForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			items:[]
		});
	}
});

Ext.onReady(function(){
	var tf = new RenewCustForm();
	var box = TemplateFactory.gTemplate(tf);
});