/**
*充值卡充值
*/
RechargeCardForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/User!rechargeCard.action",
	constructor:function(){
		var cardId = App.getApp().main.infoPanel.getUserPanel().userGrid
						.getSelectionModel().getSelected().get('card_id');
		RechargeCardForm.superclass.constructor.call(this,{
			border:false,
			labelWidth:75,
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items:[
				{xtype:'hidden',name:'icCard',value:cardId},
				{xtype:'displayfield',fieldLabel:'智能卡号',value:cardId},
				{xtype:'textfield',fieldLabel:'密码',name:'rechargeCard',
					inputType:'password',allowBlank:false}
			]
		});
	},
	success:function(form, resultData){
		if(resultData['code'] == 100){//充值成功
			alert(resultData['codeMessage']);
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		}else{
			alert(resultData['codeMessage']);
		}
	}
});

Ext.onReady(function(){
	var form = new RechargeCardForm();
	var box = TemplateFactory.gTemplate(form);
});