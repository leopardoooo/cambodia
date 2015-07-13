//【修改到期日】
InvalidChangeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!editInvalidDate.action",
	prodTariffStore : null,
	tariffId : null,
	validDate : null,
	key:true,
	constructor: function(){
		var record = null;
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		}else if(activeId == 'CUST_PANEL'){
//			record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
		}
		InvalidChangeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
				xtype : 'hidden',
				value : record.get('prod_sn'),
				id : 'prodSn'
			},{
				xtype : 'textfield',
				fieldLabel : '产品名称',
				style : Constant.TEXTFIELD_STYLE,
				value : record.get('prod_name'),
				id : 'prodName'
			},{
				xtype : 'textfield',
				fieldLabel : '原到期日',
				width : 100,
				style : Constant.TEXTFIELD_STYLE,
				value:Ext.util.Format.dateFormat(record.get('invalid_date'))
			},{
				xtype : 'datefield',
				width : 100,
				fieldLabel : '新到期日',
                format: 'Y-m-d', 	
                allowBlank:false,
                editable:false,
				minValue : Date.parseDate(record.get('invalid_date'),'Y-m-d h:i:s').add(Date.YEAR, -1),
				id : 'invalidDateId'
			}]
		})
	},
	getValues : function(){
		var params = {};
		params['prodSn'] = Ext.getCmp('prodSn').getValue();
		params['invalidDate'] = Ext.getCmp('invalidDateId').getValue().format("Y-m-d");
		return params;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
Ext.onReady(function(){
	var buy = new InvalidChangeForm();
	var box = TemplateFactory.gTemplate(buy);
});