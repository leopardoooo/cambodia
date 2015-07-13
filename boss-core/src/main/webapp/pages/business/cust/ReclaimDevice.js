/**
 * 回收设备
 */

ReclaimDeviceFrom = Ext.extend(Ext.Panel,{
	constructor : function(){
		ReclaimDeviceFrom.superclass.constructor.call(this,{
							border : false,
							labelWidth : 75,
							title : '业务受理',
							bodyStyle : Constant.TAB_STYLE,
							layout : 'form',
							region : 'center',
							anchor : '100%',
							items : [/*{										
								fieldLabel : '设备状态',
								xtype : 'paramcombo',
								paramName : 'DEVICE_STATUS_R_DEVICE',
								hiddenName : 'deviceStatus',
								width : 100,
								defaultValue:'ACTIVE'		//设备状态由仓库回收确认时操作
							},*/
							{										
								fieldLabel : '回收原因',
								xtype : 'paramcombo',
								paramName : 'RECLAIM_REASON',
								hiddenName : 'reclaimReason',
								allowBlank : false,
								width : 100
							},{
								fieldLabel : '折旧费',
								id : 'deviceFeeValue',
								xtype : 'textfield',
								vtype : 'num',
								width : 100,
								name:'deviceFeeValue'
							}]
		})
	
	}

});

ReclaimDevice = Ext.extend(BaseForm, {
		deviceSelectForm : null,
		reclaimDeviceFrom :null,
		url : Constant.ROOT_PATH + "/core/x/Cust!reclaimDevice.action",
		constructor : function() {
			this.deviceSelectForm = new DeviceSelectForm();
			this.reclaimDeviceFrom = new  ReclaimDeviceFrom();
			this.deviceSelectForm["region"]='north',
			this.deviceSelectForm["height"]= 130,
			ReclaimDevice.superclass.constructor.call(this, {							
						border : false,
						layout : 'border',							
						items : [this.deviceSelectForm,this.reclaimDeviceFrom]
					});
		},
		getValues : function() {
			var all = {};
			all["deviceId"]=App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectedDeviceIds();
			Ext.apply(all ,	this.getForm().getValues());
			all['deviceFeeValue'] = Ext.util.Format.formatToFen(all['deviceFeeValue']);
			return all;
		},
		getFee : function(){
			return parseFloat(Ext.getCmp('deviceFeeValue').getValue());
		},
		success : function(form, res) {
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		},
		doInit: function(){
			ReclaimDevice.superclass.doInit.call(this);
			var store = this.getForm().findField('reclaimReason').getStore();
			store.removeAt(store.find('item_value','SBGH'));
		}			

	});

   
Ext.onReady(function() {
			var tf = new ReclaimDevice();
			var box = TemplateFactory.gTemplate(tf);

		});