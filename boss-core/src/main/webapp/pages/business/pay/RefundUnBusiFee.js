/**
 * 预收费
 */

var RefundUnBusiFeeForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!refundUnBusiFee.action",
	record : null,
	constructor:function(){
		this.record = App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected();
		
		RefundUnBusiFeeForm.superclass.constructor.call(this,{
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
				xtype:'displayfield',
				fieldLabel:'合同名称',
				name:'generalContract.contract_name',
				allowBlank:false,
				value : this.record.get('contract_name')
			},{
				xtype:'displayfield',
				fieldLabel:'客户名称',
				name:'generalContract.cust_name',
				allowBlank:false,
				value : this.record.get('cust_name')
			},{
				xtype:'displayfield',
				fieldLabel:'可退金额',
				value :Ext.util.Format.formatFee( this.record.get('refund_amount'))
			}
			,{
				id : 'refundAmount',
				xtype:'numberfield',
				fieldLabel:'退款金额',
				name:'generalContract.refund_amount',
				allowNegative : false,
				allowDecimals : false,
				maxValue : Ext.util.Format.formatFee(this.record.get('refund_amount')),
				readOnly : this.record.get('fee_type') == 'UNBUSI' ? false : true,
				value : Ext.util.Format.formatFee(this.record.get('refund_amount'))
			},{
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
		return RefundUnBusiFeeForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var values = {},data = this.record.data;
		for(var key in data){
			values['generalContract.'+key] = data[key];
		}
		values['generalContract.nominal_amount'] = Ext.util.Format.formatToFen(-Ext.getCmp('refundAmount').getValue());
		
		return values;
	},
	getFee : function(){
		return -Ext.getCmp('refundAmount').getValue();
	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().reload();
	}
});

Ext.onReady(function(){
	var panel = new RefundUnBusiFeeForm();
	var box = TemplateFactory.gTemplate(panel);
});