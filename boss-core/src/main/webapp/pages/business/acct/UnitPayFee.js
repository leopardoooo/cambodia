PayFeesForm = Ext.extend( BaseForm , {
	url : null,
	panel:null,
	data : [],//提交数据
	constructor: function(){
		this.panel = new SpecPanel();
		PayFeesForm.superclass.constructor.call(this,{
			autoScroll:true,
			layout:'border',
            border:false,
            bodyStyle: Constant.TAB_STYLE,
            items:[{
				region:'center',
				layout : 'fit',
				items:[this.panel]
			}]
		});
	},
	getValues: function(){
		var all = {'payFeesData':Ext.encode(this.panel.getValues())};
		if(Ext.getCmp('realPay')){
			all['shouldPay'] = Ext.util.Format.formatToFen(Ext.getCmp('shouldPay').getValue()) ;
			all['realPay'] = Ext.util.Format.formatToFen(Ext.getCmp('realPay').getValue()) ;
		}else{
			all['newExpDate'] = Ext.getCmp('newExpDate').getValue().format('Y-m-d');
		}
		return all;
	},
	doValid: function(){
		var data = this.panel.getValues();
		
		if(!Ext.getCmp('realPay')){
			this.url = Constant.ROOT_PATH+"/core/x/Acct!batchEditExpDate.action";
		}else{
			this.url = Constant.ROOT_PATH+"/core/x/Acct!payBatchFees.action";
		}
		
		if(data.length==0){
			var obj = {};
			obj['msg'] = '账目不能为空';
			obj['isValid'] = false;
			return obj;
		}
		return PayFeesForm.superclass.doValid.call(this);
	},
	getFee:function(){
		if(Ext.getCmp('realPay')){
			return parseFloat(Ext.getCmp('realPay').getValue());
		}
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var payFees = new PayFeesForm();
	TemplateFactory.gTemplate(payFees);
})