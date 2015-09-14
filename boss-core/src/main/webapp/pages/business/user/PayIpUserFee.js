/**
 * 
 */
 
PayIpUserFeeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!savePayIpFee.action",
	record:null,
	busiFee:null,
	busiFeeAmount:0,
	busiFeeDate:null,
	constructor: function(){
		this.record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		PayIpUserFeeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
				xtype : 'hidden',
				value : this.record.get('user_id'),
				name : 'user_id'
			},{
				xtype : 'textfield',
				fieldLabel : lmain("user._form.userName"),
				style : Constant.TEXTFIELD_STYLE,
				value : this.record.get('user_name')
			},{
				xtype : 'textarea',
				fieldLabel : 'IP',
				width:300,
				height:40,
				disabled:true,
				value : this.record.get('str4')
			},{
				xtype : 'textfield',
				fieldLabel : lmain("expDate"),
				style : Constant.TEXTFIELD_STYLE,
				id:'expDateId'
			},{
				xtype : 'numberfield',
				fieldLabel : lmain("user._form.payMonth"),
				allowDecimals:false,//不允许输入小数 
			    allowNegative:false,
				id:'payMonth',
				allowBlank:false,
				width:100,
				enableKeyEvents:true,
				listeners:{
					scope:this,
					keyup:this.doPayMonth,
					change:this.doPayMonth
				}
			},{
                xtype: 'displayfield',
                width : 350,
                fieldLabel:lbc("common.remark"),
                id:'feeDescId'
			}]
		})
	},
	doInit: function(){
		Ext.Ajax.request({
			scope : this,
			url: Constant.ROOT_PATH+"/core/x/User!queryIpFeeLoad.action",
			params : {
				userId : this.record.get("user_id")
			},
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				Ext.getCmp('expDateId').setValue(Ext.util.Format.dateFormat(rec.CProdOrder.exp_date));
				if(rec.tBusiFee){
					this.busiFee = rec.tBusiFee;
					Ext.getCmp('feeDescId').setValue("<font style='font-size:14px'>"+lmain('user._form.feeItem')+":<b>"+rec.tBusiFee.fee_name+"</b>,"
						+lmain('user._form.unitPrice')+":<b>"+Ext.util.Format.convertToYuan(rec.tBusiFee.default_value)+"</b>,"+lmain('user._form.chargeNum')+":<b>"+this.record.get('str6')+"</b>"
						+"</font>"+"<br>"+lmain('user._form.totalPrice')+":<font style='font-size:16px;color:red'><b><span id='busiFeeAmount'>--</span></b></font>,"
						+lmain('user._form.timeSlot')+":<font style='font-size:16px;color:red'><b><span id='busiFeeDate'>--</span></b></font>");
						
				}
			}
		});
		
		PayIpUserFeeForm.superclass.doInit.call(this)
	},
	doPayMonth:function(comp){
		var endDate = Date.parseDate(Ext.getCmp('expDateId').getValue(),"Y-m-d");
		endDate.setDate(endDate.getDate()+1);
		var startDate = Date.parseDate(Ext.util.Format.addMoth(endDate.format("Y-m-d"),-1*comp.getValue()), "Y-m-d");
		
		this.busiFeeTime =  startDate.format("Ymd")+"-"+Date.parseDate(Ext.getCmp('expDateId').getValue(),"Y-m-d").format("Ymd");
		this.busiFeeAmount = comp.getValue()*this.busiFee.default_value*this.record.get('str6');
		Ext.get('busiFeeAmount').update(Ext.util.Format.convertToYuan(this.busiFeeAmount));
		Ext.get('busiFeeDate').update(this.busiFeeTime);
	},
	getValues : function(){
		var all = {};
		var data = [];
		var obj = {};
		obj['fee_id'] = this.busiFee.fee_id;
		obj['fee_std_id'] = this.busiFee.fee_std_id;
		obj['count'] = this.record.get('str6');
		obj['should_pay'] = this.busiFeeAmount;
		obj['real_pay'] = this.busiFeeAmount;
		obj['disct_info'] = this.busiFeeTime;
		data.push(obj);		
		//其他杂费busiFees 专用
		all["busiFees"] = data;
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
		if(this.busiFeeAmount == 0){
			obj["isValid"] = false;
			obj["msg"] = lmain("tipNoChargeNumber");
		}
		return obj;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
 
 
Ext.onReady(function(){
	var cpf = new PayIpUserFeeForm();
	var box = TemplateFactory.gTemplate(cpf);
});