/**
 * 报停
 */
UserStopList = Ext.extend(Ext.Panel, {
			constructor : function() {
				var userRecords = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
				var items = [{
					xtype : 'datefield',
					fieldLabel : '报停时间',
					id : 'effectivedateid',
					width : 100,
					allowBlank : false,
					minText : '不能选择当日之前',
					minValue : nowDate().format('Y-m-d'),
					value : nowDate(),
					format : 'Y-m-d',
					name : 'effectiveDate',
					enableKeyEvents : true
				}];
				if(userRecords[0].get('is_rstop_fee') == 'T'){
					items.push({
						xtype : 'numberfield',
						fieldLabel : '报停费用',
						width : 100,
						id:'fee',
						value:0,
						name : 'fee'
					});
				}
				UserStopList.superclass.constructor.call(this, {
							border : false,
							labelWidth : 120,
							title : '业务受理',
							bodyStyle : Constant.TAB_STYLE,
							layout : 'form',
							items : items
						})
			}
		})

UserStop = Ext.extend(BaseForm, {
			userInfoPanel : null,
			userStopList : null,
			confirmMsg:null,
			isAllowGo:false,
			url : Constant.ROOT_PATH + "/core/x/User!stopUser.action",
			constructor : function() {
				stopThat = this ;
				this.userInfoPanel = new UserInfoPanel();
				this.userStopList = new UserStopList(this);
				UserStop.superclass.constructor.call(this, {
							border : false,							
							layout : 'border',
							items : [
							{
								region:'north',
								height:290,
								layout:'fit',
								split : true,
								items:[this.userInfoPanel]
							},{
								region:'center',
								layout:'fit',
								split : true,
								items:[this.userStopList]
							}]
						}

				);
			},doInit : function(){
				var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
				var List = [];
				for(var i=0;i<users.length;i++){
					var objs = {};
					objs['user_id'] = users[i].get('user_id');
					objs['stb_id'] = users[i].get('stb_id');
					objs['modem_mac'] = users[i].get('modem_mac');
					List.push(objs);
				}
				var records = Ext.encode(List);
				Ext.Ajax.request({
						url:Constant.ROOT_PATH + "/core/x/User!queryStopByUsers.action",
						params: {userLists: records},
						success : function(res, ops) {
							var rs = Ext.decode(res.responseText);
							if(rs.simpleObj!=null){
								stopThat.confirmMsg  = "用户已经预报停,信息为："+rs.simpleObj+"如果继续,原先预报停将作废！";
							}
								stopThat.isAllowGo = true;
					    }
					});
			}
			,success : function(form, res) {
				App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			},
			getValues:function(){
				var values = this.getForm().getValues();
				if(Ext.getCmp('fee')){
					values['specFee']=Ext.util.Format.formatToFen(Ext.getCmp('fee').getValue());
				}
				return values;
			},
			getFee : function(){
				if(Ext.getCmp('fee')){
					var fee = parseFloat(Ext.getCmp('fee').getValue());
					return fee;
				}else{
					return 0 ;
				}
			}
		});

Ext.onReady(function() {
			var tf = new UserStop();
			var box = TemplateFactory.gTemplate(tf);

		});