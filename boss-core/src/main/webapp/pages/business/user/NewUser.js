/**
 * 新建用户
 * @class NewUserForm
 * @extends UserBaseForm
 */
NewUserForm = Ext.extend(UserBaseForm , {
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
	doValid : function(){
		var stbCombo = Ext.getCmp('dtv_stb_id');
		var cardCombo = Ext.getCmp('dtv_card_id');
		var modemCombo = Ext.getCmp('modemMac');
		if(stbCombo && stbCombo.hasFocus){
			this.checkDevice(stbCombo,'STB',cardCombo);
			if(stbCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "机顶盒有问题，请在验证后保存";
				return obj;
			}
		}
		if(cardCombo && cardCombo.hasFocus){
			this.checkCardDevice(cardCombo);
			if(cardCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "智能卡有问题，请在验证后保存";
				return obj;
			}
		}
		if(modemCombo && modemCombo.hasFocus){
			this.checkDevice(modemCombo,'MODEM');
			if(modemCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "MODEM有问题，请在验证后保存";
				return obj;
			}
		}
		
		/*if(this.oldUsertype=='BAND'){
			var loginNames = [];
			var userStore =  App.getApp().main.infoPanel.userPanel.userGrid.getStore();
			var bandUser = userStore.query('user_type','BAND');
			bandUser.each(function(item){
				loginNames.push(item.get('login_name'));
			})
			if(loginNames.contain(Ext.getCmp('login_name_id').getValue())){
				obj["isValid"] = false;
				obj["msg"] = "登录账号不能重复";
				return obj;
			}
		}*/
		
		return NewUserForm.superclass.doValid.call(this);
	},
	success : function(form,res){
		var userId = res.simpleObj;
		//清空参数
//		App.getData().busiTaskToDo = [];
		var orderProd = App.getData().busiTask['OrderProd'];
		orderProd['callback'] = {
			fn : App.getApp().selectRelativeUser,
			params : [userId]
		};
//		
//		var newUser = App.getData().busiTask['NewUser'];
//		
//		//跳转业务订购产品
//		App.getData().busiTaskToDo.push(newUser);
//		App.getData().busiTaskToDo.push(orderProd);
		
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		
	}
});

//Ext.onReady(function(){
//	var nup = new NewUserForm();
//	var box = TemplateFactory.gTemplate(nup);
//});

