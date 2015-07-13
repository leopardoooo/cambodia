/**
 * 租赁费用
 */

LeaseFeeForm = Ext.extend(BaseForm, {
			feeId:null,
			url : Constant.ROOT_PATH + "/core/x/User!saveLeaseFee.action",
			constructor : function() {
				leaseThat = this ;
				LeaseFeeForm.superclass.constructor.call(this, {
							border : false,							
							layout : 'form',
							baseCls : 'x-plain',
							labelWidth: 185,
							bodyStyle: Constant.TAB_STYLE,
							items : [{
								xtype:'textfield',
								fieldLabel:'费用名称',								
								name:'fee_name',
								style : Constant.TEXTFIELD_STYLE							
							},{
								xtype : 'numberfield',
								fieldLabel : '金额',
								width : 100,
								id:'fee',
								allowBlank : false,
								name : 'fee'
							}]
						}

				);
			},doInit : function(){
				Ext.Ajax.request({
						url:Constant.ROOT_PATH + "/core/x/User!queryZlFeeById.action",
						success : function(res, ops) {
							var rs = Ext.decode(res.responseText);
							if(rs.simpleObj!=null){
								leaseThat.find('name','fee_name')[0].setValue(rs.simpleObj.fee_name);
								if(!Ext.isEmpty(rs.simpleObj.fee_id)){
									leaseThat.feeId = rs.simpleObj.fee_id;
								}else{
									Alert("未配置费用项!");
								}
							}								
					    }
					});
			}
			,success : function(form, res) {
				App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			},
			getValues:function(){
				var values = this.getForm().getValues();
					values['amount']=Ext.util.Format.formatToFen(Ext.getCmp('fee').getValue());
					values['fee_id']=leaseThat.feeId;
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
			var tf = new LeaseFeeForm();
			var box = TemplateFactory.gTemplate(tf);

		});