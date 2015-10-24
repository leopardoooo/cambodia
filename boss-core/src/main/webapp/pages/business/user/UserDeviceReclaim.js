/**
 * 设备回收
 */
UserDeviceReclaim = Ext.extend(BaseForm, {
		userInfoPanel : null,
		url : Constant.ROOT_PATH + "/core/x/User!saveDeviceReclaim.action",
		constructor : function() {
			this.userInfoPanel = new UserInfoBaseForm();
			UserDeviceReclaim.superclass.constructor.call(this, {							
						border : false,
						layout:'fit',
						items : [this.userInfoPanel]
					});
		},
		success : function(form, res) {
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		},
		doInit: function(){
			UserDeviceReclaim.superclass.doInit.call(this);
		}			

	});

   
Ext.onReady(function() {
	var tf = new UserDeviceReclaim();
	var box = TemplateFactory.gTemplate(tf);

});