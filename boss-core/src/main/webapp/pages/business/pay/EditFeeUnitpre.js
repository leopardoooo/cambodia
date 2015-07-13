/**
 * 预收费
 */

var EditFeeUnitpreForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!editGeneralContract.action",
	record : null,
	constructor:function(){
		this.record = App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected();
		
		EditFeeUnitpreForm.superclass.constructor.call(this,{
			labelWidth:95,
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults : {
				width : 160
			},
			items:[{
				xtype:'displayfield',
				fieldLabel:'合同号',
				name:'contract_no',
				allowBlank:false,
				value : this.record.get('contract_no')
			},{
				xtype:'textfield',
				fieldLabel:'合同名称',
				name:'generalContract.contract_name',
				allowBlank:false,
				value : this.record.get('contract_name')
			},{
				xtype:'textfield',
				fieldLabel:'客户名称',
				name:'generalContract.cust_name',
				allowBlank:false,
				value : this.record.get('cust_name')
			}
//			,{
//				xtype:'numberfield',
//				fieldLabel:'用户数',
//				name:'generalContract.user_amount',
//				allowNegative : false,
//				allowDecimals : false,
//				value : this.record.get('user_amount')
//			}
			,{
				id : 'AddrDistrict',
				xtype:'combo',
				fieldLabel:'所在区域',
				store : new Ext.data.JsonStore({
					autoLoad : true,
					url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrDistrict.action",
					fields : ['addr_id','addr_name'],
					listeners : {
						scope : this,
						load : this.doLoadData
					}
				}),
				displayField : 'addr_name',
				valueField : 'addr_id',
				hiddenName:'generalContract.addr_district',
				editable : true,
				triggerAction : 'all',
				forceSelection : true
				,listeners : {
					scope : this,
					select : function(combo){
						Ext.getCmp('AddrCommunity').setDisabled(false);
						Ext.getCmp('AddrCommunity').getStore().load({
							params : {
								addrPid : combo.getValue()
							}
						})
					},
					keyup:function(){
						Ext.getCmp('AddrCommunity').reset();
						Ext.getCmp('AddrCommunity').setDisabled(true);
					}
				}
			},{
				id : 'AddrCommunity',
				xtype:'combo',
				fieldLabel:'所在小区',
				store : new Ext.data.JsonStore({
					url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrCommunity.action",
					fields : ['addr_id','addr_name']
				}),
				disabled : true,
				displayField : 'addr_name',
				hiddenName:'generalContract.addr_community',
				valueField : 'addr_id',
				editable : true,
				triggerAction : 'all',
				forceSelection : true
			}
			]
		});
	},
	doLoadData : function(){
		if(this.record.get('addr_district')){
			Ext.getCmp('AddrDistrict').setValue(this.record.get('addr_district'));
			Ext.getCmp('AddrCommunity').setDisabled(false);
			Ext.getCmp('AddrCommunity').getStore().load({
				params : {
					addrPid : this.record.get('addr_district')
				}
			})
			Ext.getCmp('AddrCommunity').getStore().on('load',function(){
				Ext.getCmp('AddrCommunity').setValue(this.record.get('addr_community'));
			},this);
			
		}
	},
	doValid : function(){
//		var newAmount = Ext.getCmp('newNominalAmount').getValue();
////		var oldAmount = parseFloat(Ext.getCmp('oldNominalAmount').getValue());
////		var usedMoney = parseFloat(Ext.getCmp('usedMoney').getValue());
//		
//		var obj = {};
//		if(newAmount == 0){
//			obj["isValid"] = false;
//			obj["msg"] = "修改合同金额不能为零";
//			return obj;
//		}
//		
////		if((oldAmount + newAmount) < usedMoney){
////			obj["isValid"] = false;
////			obj["msg"] = "新合同金额不能比已使用金额小";
////			return obj;
////		}
//		
		return EditFeeUnitpreForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var values = this.getForm().getValues();
		values['generalContract.contract_id']= this.record.get('contract_id');
		
		return values;
	},
//	getFee : function(){
//		return Ext.getCmp('newNominalAmount').getValue();
//	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().reload();
	}
});

Ext.onReady(function(){
	var panel = new EditFeeUnitpreForm();
	var box = TemplateFactory.gTemplate(panel);
});