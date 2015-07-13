/**
 * 修改充值卡
 */

var ValuableUpdateForm = Ext.extend(BaseForm,{
	url : root + '/commons/x/QueryDevice!editValuableCard.action',
	record : null,
	constructor:function(){
		this.record = App.getApp().main.valuableCardGrid.getSelectionModel().getSelected();
		
		ValuableUpdateForm.superclass.constructor.call(this,{
			labelWidth:95,
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults : {
				width : 160
			},
			items:[{
				xtype:'hidden',
				name:'done_code',
				value : this.record.get('done_code')
			},{
				xtype:'textfield',
				fieldLabel:'客户名称',
				name:'cust_name',
				allowBlank:false,
				value : this.record.get('cust_name')
			}]
		});
	},show:function(){
		ValuableUpdateForm.superclass.show.call(this);
		this.find('name','cust_name')[0].focus(100,true);
	},
	initComponent: function(){
		ValuableUpdateForm.superclass.initComponent.call(this);
//		var custName = this.find("name", "cust_name")[0];
//		custName.focus();
	},
	getValues:function(){
		var values = this.getForm().getValues();
		return values;
	},
	success: function(form, resultData){
		App.getApp().main.valuableCardGrid.getStore().reload();
	}
});

Ext.onReady(function(){
	var panel = new ValuableUpdateForm();
	var box = TemplateFactory.gTemplate(panel);
});