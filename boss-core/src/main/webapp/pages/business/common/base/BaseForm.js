/**
 * 业务表单, 接口定义。为了统一业务表单的基类
 * @class BaseForm
 * @extends Ext.FormPanel
 */
 
BaseForm = Ext.extend(Ext.FormPanel , {
	/**
	 * 指定表单提交的URL ,默认为NULL
	 */
	url: null,
	
	initComponent: function(){
		BaseForm.superclass.initComponent.call(this);
		//初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		if(comboes.length == 0){
			this.doInit();
		}else{
			App.form.initComboData( comboes , this.doInit ,this );
		}
	},
	
	//初始化函数，当下拉框的参数值赋值后被调用，如有情况重写该函数。
	doInit: function(){},
	
	/**
	 * 在获取getValues之前。必须通过验证。
	 * 如果没有通过验证。则将不触发提交事件。
	 * 一般情况下，表单有特殊验证则需要重写该函数。
	 */
	doValid : function() {
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = "含有验证不通过的输入项";
		}
		return obj;
	},
	
	/**
	 * 重置函数。
	 */
	doReset: function(){ this.getForm().reset(); },
	
	//获取表单所有字段值，如果表单的对表单的获取值需要重新设置，可以覆盖该函数
	getValues: function(){ return this.getForm().getValues(); },
	
	/**
	 * 当业务保存，成功返回时，自动调用该函数，
	 * 可以覆盖该函数使用保存成功的后续处理。
	 * 当前的作用域属于容器，BusiPanel 或 BusiWindow
	 */
	success: function(form, resultData){},
	/**
	 * 如果业务表单中，含有费用的信息，则必须覆盖该函数。
	 * 因为在保存的时候。缴费窗口会自动调用该费用进行累加。
	 */
	getFee: function (){ return 0 ; }
});