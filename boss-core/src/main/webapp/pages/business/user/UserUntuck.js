UserUntuckForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/User!untuckUsers.action",
	constructor : function() {
		this.userInfoPanel = new UserInfoPanel();
		UserUntuckForm.superclass.constructor.call(this, {
					border : false,							
					layout : 'fit',
					items : [ this.userInfoPanel]
				}

		);
	}
});

Ext.onReady(function(){
	var nup = new UserUntuckForm();
	var box = TemplateFactory.gTemplate(nup);
});