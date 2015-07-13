/**
 * 添加合同凭据
 * @class AddCredentialForm
 * @extends BaseForm
 */
 
//TODO 记录赠送金额，计算percent

AddCredentialForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!addCredential.action",
	constructor:function(){
		this.record = App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected();
		
		AddCredentialForm.superclass.constructor.call(this,{
			labelWidth:95,
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults : {
				width : 160
			},
			items:[{
				xtype:'displayfield',
				fieldLabel:'客户名称',
				name:'cust_name',
				allowBlank:false,
				value : this.record.get('cust_name')
			},{
				xtype:'displayfield',
				fieldLabel:'合同号',
				name:'contract_no',
				allowBlank:false,
				value : this.record.get('contract_no')
			},{
				xtype:'displayfield',
				fieldLabel:'合同名称',
				name:'contract_name',
				allowBlank:false,
				value : this.record.get('contract_name')
			},{
				xtype:'numberfield',
				fieldLabel:'凭据起始号',
				allowNegative : false,
				allowDecimals : false,
				name:'credentialStartNo',
				allowBlank:false,
				maxValue : 2000000000,
				listeners : {
					scope : this,
					change : this.doNumChange
				}
			},{
				xtype:'numberfield',
				fieldLabel:'凭据截止号',
				allowNegative : false,
				allowDecimals : false,
				maxValue : 2000000000,
				name:'credentialEndNo',
				allowBlank:false,
				listeners : {
					scope : this,
					change : this.doNumChange
				} 
			},{
				xtype:'numberfield',
				fieldLabel:'每张凭据金额',
				allowNegative : false,
				name:'credentialAmount',
				allowBlank:false 
			}]
		});
	},
	doNumChange : function(){
		var startNo = this.find('name','credentialStartNo')[0].getValue();
		var endCmp = this.find('name','credentialEndNo')[0];
		var endNo = endCmp.getValue();
		if(endNo && startNo){
			if(startNo > endNo){
				Alert("凭据截止号不能小于凭据起始号")
				endCmp.reset();
			}
		}
	},
	getValues:function(){
		var result = this.getForm().getValues();
		values['credentialAmount'] = Ext.util.Format.formatToFen(values['credentialAmount']);
		result['contractId'] = this.record.get('contract_id');
		return result;
	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().load();
	}
});

Ext.onReady(function(){
	var panel = new AddCredentialForm();
	var box = TemplateFactory.gTemplate(panel);
});