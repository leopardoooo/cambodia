/**
 * 报修单
 */
NewRepairTaskForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Cust!saveBugTask.action",
	constructor: function(){
		NewRepairTaskForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 75,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
      			fieldLabel:'故障内容',
				name:'bugDetail',
				preventScrollbars : true,
				height : 120,
				width : 300,
				allowBlank:false,
				xtype:'textarea'
			}]
		})
	},
	doInit: function(){
		NewRepairTaskForm.superclass.doInit.call(this)
	},
	getValues : function(){
		var all = this.getForm().getValues();
		return all;
	},
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = lbc("common.tipFormInvalid");
		}
		return obj;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

/**
 * 入口
 */
Ext.onReady(function(){
	var form  = new NewRepairTaskForm();
	//render
//	TemplateFactory.gViewport(form);
	TemplateFactory.gTemplate(form);
});