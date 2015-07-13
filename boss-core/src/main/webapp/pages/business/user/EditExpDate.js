/**
 * 0资费修改到期日或缴费
 * @class TariffChangeForm
 * @extends BaseForm
 */
EditExpDateForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Acct!editExpDate.action",
	payPanel : null,
	record : null,
	constructor: function(){
		var record = null;
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		}else if(activeId == 'CUST_PANEL'){
//			record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
		}
		//最小截止日
		this.minDate = record.get('invalid_date');
		
		this.record = record;
		
		this.payPanel = {
				xtype : 'panel',
				id : 'payPanel',
				border: false,
				baseCls: 'x-plain',
				anchor : '100%',
				bodyStyle: "background:#F9F9F9",
				layout : 'form',
				labelWidth: 100,
				items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '开始计费日期',
						minValue : Date.parseDate(this.minDate,'Y-m-d h:i:s').format('Y-m-d'),
						value : Date.parseDate(this.minDate,'Y-m-d h:i:s').format('Y-m-d'),
						format : 'Y-m-d',
						name : 'beginDate',
						allowBlank : false,
						id : 'prodstartdate'
				},{
					xtype : 'numberfield',
					fieldLabel : '缴费月份数',
					allowBlank : false,
					width : 120,
					allowNegative : false,
					name : 'months',
					minValue : 1,
					maxValue : 99
				},{
					fieldLabel : '收费',
					id : 'FeeValue',
					width : 120,
					vtype : 'num',
					name : 'fee',
					xtype:'textfield',
					allowBlank : false
				}]
			};
			
		EditExpDateForm.superclass.constructor.call(this,{
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
				xtype : 'combo',
				fieldLabel : '办理业务',
				store : new Ext.data.SimpleStore({
					fields : ['text'],
			 		data : [
			 			['修改预计到期日'],
			 			['缴费']
			 		]
				}),
				width : 120,
				valueField: 'text',
				displayField: 'text',
				name : 'busi',
				editable : false,
				forceSelection : true,
				listeners : {
					scope : this,
					'select' : this.doSelectBusi
				}
			}]
		})
	},
	doInit : function(){
		if(this.record.get('user_id')){
			var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
			
			if(record.get('user_count') > 1){
				this.insert(1,{
					xtype : 'displayfield',
					fieldLabel : '终端数量',
					value : record.get('user_count')
				})
			}
		}
	},
	doSelectBusi : function(combo,rec){
		if(Ext.getCmp('newInvalidDate')){
			this.remove(Ext.getCmp('newInvalidDate'),true);
		}else{
			this.remove(Ext.getCmp('payPanel'),true);
		}
		
		if(combo.getValue() == '修改预计到期日'){
			this.add({
				xtype : 'datefield',
				id : 'newInvalidDate',
				width : 120,
				allowBlank : false,
				fieldLabel : '预计到期日',
				minValue : Date.parseDate(this.minDate,'Y-m-d h:i:s').format('Y-m-d'),
				format : 'Y-m-d',
				name : 'invalidDate'
			});
		}else{
			this.add(this.payPanel);
		}
		this.doLayout();
	},
	getValues : function(){
		var params = this.getForm().getValues();
		if(params.fee){
			params.fee = Ext.util.Format.formatToFen(params.fee);
		}
		return params;
	},
	getFee : function(){
		if(Ext.getCmp('FeeValue')){
			var fee = parseFloat(Ext.getCmp('FeeValue').getValue());
			return fee;
		}
	},
	doValid : function(){
		if(Ext.getCmp('FeeValue')){
			var fee = parseFloat(Ext.getCmp('FeeValue').getValue());
			if(fee <=0){
				var obj = {};
				obj['isValid'] = false;
				obj['msg'] = '金额必须大于0！';
				return obj;
			}
		}
		return EditExpDateForm.superclass.doValid.call(this);
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
Ext.onReady(function(){
	var buy = new EditExpDateForm();
	var box = TemplateFactory.gTemplate(buy);
});