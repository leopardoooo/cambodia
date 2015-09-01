
/**
 * 客户开户面板
 */
NewCustForm = Ext.extend( CustBaseForm , {
	url:  Constant.ROOT_PATH + "/core/x/Cust!createCust.action",
	success:function(form,res){
		var cust = res.simpleObj;
		App.getData().custFullInfo.cust = cust;
		App.getApp().main.infoPanel.changeDisplay();
		if(cust.cust_type != 'UNIT'){
			//清空参数
			App.getData().busiTaskToDo = [];
			var buyDevice = App.getData().busiTask['BuyDevice'];
			var newUser = App.getData().busiTask['NewUser'];
			//跳转业务购买设备，用户开户
			App.getData().busiTaskToDo.push(buyDevice);
			App.getData().busiTaskToDo.push(newUser);
		}
		//刷新支付
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	doValid:function(){
		if(!Ext.getCmp('tempCustAddress').getValue()){
			Ext.getCmp('tempCustAddress').setValue("");
			
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '无效的客户地址，请重新输入！';
			return obj;
		}
		if(Ext.getCmp('isCanToCustId').getValue() && Ext.isEmpty(this.custCode)){
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '意向客户请选择省！';
			return obj;
		}
		
		return NewCustForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		if(this.custLevel){
			all['cust.cust_level'] = this.custLevel;
		}
		if(this.custCode){
			all['custCode'] = this.custCode;
		}
		return all;
	}
});

/**
 * 入口
 */
Ext.onReady(function(){
	var tf = new NewCustForm();
	var box = TemplateFactory.gTemplate(tf);
});