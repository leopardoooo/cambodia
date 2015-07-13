//【失效时间变更】
ExpDateChangeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!changeExpDate.action",
	expDate : null,
	hiddenvalue : false,
	text : '失效日期',
	constructor: function(){
		var record = null;
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		}else if(activeId == 'CUST_PANEL'){
//			record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
		}
		var time = record.get('exp_date');
		if(Ext.isEmpty(time)){
			this.hiddenvalue = true;
		}else{
			this.expDate = Date.parseDate(time,'Y-m-d h:i:s');
			this.text = "新失效日期";
		}
		
		ExpDateChangeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 200,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			defaults: {
                layout: 'form',
                border: false,
                labelWidth: 200,
                bodyStyle: "background:#F9F9F9;"
             },
			items : [{
				xtype : 'hidden',
				value : record.get('prod_sn'),
				id : 'prodSn'
			},{items:[{
					xtype : 'textfield',
					fieldLabel : '产品名称',
					style : Constant.TEXTFIELD_STYLE,
					value : record.get('prod_name'),
					id : 'prodName'
			}]},{
			hidden:this.hiddenvalue,
			items:[{
					xtype : 'datefield',
					width : 90,
					fieldLabel : '原失效日期',
					format : 'Y-m-d',
					value : this.expDate,
					disabled : true
			}]},{items:[{
					xtype : 'datefield',
					width : 90,
					fieldLabel : this.text,
					format : 'Y-m-d',
					id : 'expdate'
			}]}]
		});
		
		//账户 store
		var acctStore = App.getApp().main.infoPanel.getAcctPanel().acctGrid.getStore();
		var prodId = record.get('prod_id');//操作的产品id
		var acctId = record.get('acct_id');//操作员产品所属账户id
		var real_balance = 0;//产品余额
		var acctItems=[];//当前用户下所有账目集合
		for(var i=0;i<acctStore.getCount();i++){
			if(acctId == acctStore.getAt(i).get('acct_id')){
				var ais = acctStore.getAt(i).get('acctitems')
				if(ais){
					acctItems = ais;
				}
				break;
			}
		}
		for(var i=0;i<acctItems.length;i++){
			var acctItem = acctItems[i];
			if(acctItem['acctitem_id'] == prodId){
				real_balance = acctItem['real_balance'];
				break;
			}
		}
/*		
		//有余额，失效日期要大于等于到期日期
		if(real_balance != 0){
			Ext.getCmp('expdate').minValue = Date.parseDate(record.get('invalid_date'), 'Y-m-d h:i:s');
		}else{
			//无余额，失效日期大于当天
			Ext.getCmp('expdate').minValue = nowDate().clearTime();
		}
		
		//已经有失效日期了，再次选择的失效日期必须大于上次日期
		if(this.hiddenvalue === false){
			var expDate = Date.parseDate(time, 'Y-m-d h:i:s');
			var minValue = Ext.getCmp('expdate').minValue;
			if (Ext.isEmpty(minValue) || minValue <= expDate){
				Ext.getCmp('expdate').minValue = expDate;
			}
		}
*/		
//		Ext.getCmp('expdate').minValue = nowDate().add(Date.DAY,1).clearTime();
	},
	getValues : function(){
		var params = {};
		params['prodSn'] = Ext.getCmp('prodSn').getValue();
		if(!Ext.isEmpty(Ext.getCmp('expdate').getValue())){
			params['expDate'] = Ext.getCmp('expdate').getValue().format("Y-m-d");
		}
		return params;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
Ext.onReady(function(){
	var buy = new ExpDateChangeForm();
	var box = TemplateFactory.gTemplate(buy);
});