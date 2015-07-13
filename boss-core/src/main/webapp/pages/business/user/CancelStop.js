/**
 * 取消预报停
 */
CancelStop = Ext.extend(BaseForm, {
			userInfoPanel : null,
			url : Constant.ROOT_PATH + "/core/x/User!cancelStopUser.action",
			constructor : function() {
				stopThat = this ;
				this.userInfoPanel = new UserInfoPanel(350);
				CancelStop.superclass.constructor.call(this, {
							border : false,							
							layout:'fit',
							items : [this.userInfoPanel]
						}

				);
			}
			,success : function(form, res) {
				App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			}
		});

Ext.onReady(function() {
			var tf = new CancelStop();
			var box = TemplateFactory.gTemplate(tf);

		});