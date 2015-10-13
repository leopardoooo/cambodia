/**
 * 补收设备费用
 */
SaleDeviceFee = Ext.extend(BaseForm, {
	url : Constant.ROOT_PATH + "/core/x/User!saveSaleDevice.action",
	currentFeeData:null,
	constructor : function() {
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		
		SaleDeviceFee.superclass.constructor.call(this, {
				border : false,							
				bodyStyle: Constant.TAB_STYLE,
	            labelWidth:150,
				baseCls: 'x-plain',
				items : [{
						xtype : 'hidden',
						value : record.get('user_id'),
						id:'saleUserId',
						name : 'user_id'
					},{
						xtype : 'hidden',
						value : record.get('device_model'),
						id: 'deviceCategoryEl',
						name : 'device_model'
					},{
						fieldLabel: lmain("user.base.deviceModel"),
						width : 150,
						xtype:'displayfield',
						name:'device_model_text',
						value:record.get('device_model_text')
					},{
						xtype : 'paramcombo',
						fieldLabel: lmain("user.base.buyWay"),
						allowBlank : false,
						paramName:'DEVICE_SALE_MODE',
						hiddenName:'buy_mode',
						id : 'deviceBuyMode',
						width:150,
						defaultValue: 'BUY',
						listeners: {
							scope: this,
							select: this.doBuyModeSelect
						}
					},{
						xtype: 'displayfield',
			            fieldLabel: lmain("user._form.feeName"),
			            width : 150,
			            id: 'dfFeeNameEl'
					},{
						fieldLabel: lmain("user._form.feeAmount"),
						xtype:'numberfield',
						width:150,
						allowBlank:false,
						id: 'txtFeeEl'
					}]
			}

		);
	},doInit : function(){
		this.doBuyModeSelect();
		SaleDeviceFee.superclass.doInit.call(this);
	},
	doBuyModeSelect : function(){
		var deviceModelValue = Ext.getCmp("deviceCategoryEl").getValue();
		var deviceBuyModeValue = Ext.getCmp("deviceBuyMode").getValue();
		
		if(!deviceModelValue || !deviceBuyModeValue){
			return ;
		}
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/QueryDevice!queryDeviceFee.action',
			params : {
				deviceModel : deviceModelValue,
				buyMode : deviceBuyModeValue
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				var dfFeeName = Ext.getCmp("dfFeeNameEl");
				var txtFee = Ext.getCmp("txtFeeEl");
				if(data && data.length > 0 ){
					data = data[0];
					dfFeeName.setValue(data["fee_name"]);
					txtFee.setValue(data["fee_value"]/100.0);
					txtFee.setMaxValue(data["max_fee_value"]/100.0);
					txtFee.setMinValue(data["min_fee_value"]/100.0);
					txtFee.clearInvalid();
					this.currentFeeData = data;
				}else{
					dfFeeName.setValue("--");
					txtFee.setValue(0.00);
					txtFee.setMaxValue(0);
					txtFee.setMinValue(0);
					this.currentFeeData = null;
				}
			}
		});
	}
	,success : function(form, res) {
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	doValid : function(){
		var formValid =  SaleDeviceFee.superclass.doValid.call(this);
		if(formValid !== true){
			return formValid;
		}
		var txtFeeEl = Ext.getCmp("txtFeeEl");
		//设备费用检查
		var fd = this.currentFeeData;
		if(fd){
			var maxfee = parseFloat(fd["min_fee_value"])/100.0;
			var minfee = parseFloat(fd["max_fee_value"])/100.0;
			var feeValue = parseFloat(txtFeeEl.getValue());
			if(feeValue < minfee || feeValue > maxfee){
				return {
					isValid: false,
					msg: lbc('msgBox.deviceFeeMustBeBetween', null, [minfee, maxfee])
				};
			}
		}
		
		return true;
	},
	getValues:function(){
		var values = {};
		// 设备信息
		values["userId"] = Ext.getCmp("saleUserId").getValue();
		values["deviceModel"] = Ext.getCmp("deviceCategoryEl").getValue();
		values["deviceBuyMode"] = Ext.getCmp("deviceBuyMode").getValue();
		
		// 设备费用
		var fee = this.currentFeeData;
		if(fee){
			values["deviceFee.fee_id"] = fee["fee_id"];
			values["deviceFee.fee_std_id"] = fee["fee_std_id"];
			values["deviceFee.fee"] =Ext.util.Format.formatToFen(Ext.getCmp("txtFeeEl").getValue());
		}
		return values;
	}
});

Ext.onReady(function() {
	var tf = new SaleDeviceFee();
	var box = TemplateFactory.gTemplate(tf);

});