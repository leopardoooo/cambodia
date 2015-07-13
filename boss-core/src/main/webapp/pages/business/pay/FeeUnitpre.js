/**
 * 预收费
 */
 /**
  * 预收款
  * @class FeeUnitprePanel
  * @extends Ext.Panel
  */
FeeUnitprePanel = Ext.extend(Ext.Panel,{
	parent : null,
	constructor : function(p){
		this.parent = p;
		FeeUnitprePanel.superclass.constructor.call(this,{
			id : 'FeeUnitprePanel',
			border : false,
			layout : 'form',
			anchor : '100%',
			labelWidth:95,
			baseCls:'x-plain',
			bodyStyle: "background:#F9F9F9",
			defaults : {
				width : 180
			},
			items : [{
				xtype:'numberfield',
				fieldLabel:'用户数',
				allowNegative : false,
				allowDecimals : false,
				name:'generalContract.user_amount'
			}
			,{
				xtype:'numberfield',
				fieldLabel:'序列起始号',
				allowNegative : false,
				allowDecimals : false,
				name:'credentialStartNo',
				maxValue : 99999,
				allowBlank:false,
				listeners : {
					scope : this,
					change : this.parent.doNumChange
				}
			},{
				xtype:'numberfield',
				fieldLabel:'序列截止号',
				allowNegative : false,
				allowDecimals : false,
				name:'credentialEndNo',
				maxValue : 99999,
				allowBlank:false,
				listeners : {
					scope : this,
					change : this.parent.doNumChange
				} 
			},{
				xtype:'numberfield',
				fieldLabel:'每张凭据金额',
				allowNegative : false,
				name:'credentialAmount',
				allowBlank:false,
				listeners : {
					scope : this,
					change : this.parent.doNumChange
				}  
			},{
				xtype:'displayfield',
				fieldLabel:'赠送总金额',
				name:'presentAmount',
				allowBlank:false 
			}]
		})
	}
}) 

/**
 * 打印项下拉框
 * @class PrintItemPanel
 * @extends Ext.form.CompositeField
 */
PrintItemPanel = Ext.extend(Ext.form.CompositeField,{
	win : null,
	combo : null,
	printitemStore : null,
	constructor : function(printitem_id){
		
		this.printitemStore = new Ext.data.JsonStore({
			baseParams : {printitemId : printitem_id},
			url: Constant.ROOT_PATH+"/core/x/Pay!queryPrintitem.action",
			fields : ['printitem_id','printitem_name']
		});
		
		this.combo = new Ext.form.ComboBox({
			store : this.printitemStore,
			hiddenName : 'printitem_id',
			displayField : 'printitem_name',
			valueField : 'printitem_id',
			triggerAction : 'all',
			forceSelection : true,
			mode : 'local'
		});
		
		this.printitemStore.load();
		
		this.printitemStore.on('load',this.doLoadData,this);
		
		PrintItemPanel.superclass.constructor.call(this,{
			combineErrors: false,
			fieldLabel : '打印名称',
			items : [this.combo,{
				xtype : 'button',
				text : '修改',
				scope : this,
				handler : this.doModify
			}]
		})
	},
	doLoadData : function(store){
		if(store.getCount()>0){
			this.combo.setValue(store.getAt(0).get('printitem_id'));
		}
	},
	doModify : function(){
		this.win = new Ext.Window({
			height : 150,
			width : 300,
			title : '修改打印名称',
			layout : 'fit',
			closeAction : 'close',
			items : [{
				xtype : 'form',
				layout : 'form',
				bodyStyle : 'padding : 5px;padding-top : 10px;',
				items : [{
					xtype : 'hidden',
					name : 'printitemId',
					value : this.combo.getValue()+''
				},{
					xtype : 'textfield',
					fieldLabel : '打印项名称',
					allowBlank : false,
					name : 'printitemName',
					value : this.combo.getRawValue(),
					listeners : {
						scope : this,
						specialkey : function(txt,e){
							if(e.getKey() == 13){
								this.doSave();
							}
						}
					}
				}]
			}],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.win.close();
				}
			}]
		}).show();
	},
	doSave : function(){
		var form = this.win.items.itemAt(0);
		if(!form.getForm().isValid()){
			return;
		};
		
		var all = form.getForm().getValues();
		
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
			
			Ext.Ajax.request({
				scope : this,
				url: Constant.ROOT_PATH+"/core/x/Pay!eidtPrintitem.action",
				params : all,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						this.printitemStore.reload();
						this.win.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
});

var FeeUnitpreForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!saveGeneralContract.action",
	constructor:function(){
		
		FeeUnitpreForm.superclass.constructor.call(this,{
			id:'feeUnitpreFormId',
			labelWidth:95,
			border:false,
			baseCls:'x-plain',
			bodyStyle: Constant.FORM_STYLE,
			defaults : {
				width : 180
			},
			items:[{
				xtype:'textfield',
				fieldLabel:'客户名称',
				name:'generalContract.cust_name',
				allowBlank:false
			},{
				xtype : 'hidden',
				id : 'feeType',
				name : 'generalContract.fee_type'
			},{
				xtype : 'hidden',
				id : 'feeId',
				name : 'generalContract.fee_id'
			},{
				xtype:'combo',
				fieldLabel:'费用名称',
				store : new Ext.data.JsonStore({
					autoLoad : true,
					url : Constant.ROOT_PATH+"/core/x/Acct!queryUnBusiFee.action",
					fields : ['fee_name','fee_type','fee_id','printitem_id']
				}),
				displayField : 'fee_name',
				valueField : 'fee_name',
				allowBlank:false,
				forceSelection : true
				,listeners : {
					scope : this,
					select : this.doSelectFeeType
				}
			},{
				xtype:'textfield',
				fieldLabel:'合同号',
				vtype : 'alphanum',
				name:'generalContract.contract_no',
				allowBlank:false
			},{
				xtype:'textfield',
				fieldLabel:'合同名称',
				name:'generalContract.contract_name',
				allowBlank:false
			},{
				xtype:'numberfield',
				fieldLabel:'合同金额',
				minValue : 1,
				id : 'nominalAmount',
				name:'generalContract.nominal_amount',
				allowBlank:false
				,listeners : {
					scope : this,
					change : this.doNumChange
				}  
			},{
				xtype:'combo',
				fieldLabel:'所在区域',
				store : new Ext.data.JsonStore({
					autoLoad : true,
					url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrDistrict.action",
					fields : ['addr_id','addr_name']
				}),
				displayField : 'addr_name',
				valueField : 'addr_id',
				hiddenName:'generalContract.addr_district',
				editable : true,
				triggerAction : 'all',
				allowBlank:false,
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
				allowBlank:false,
				editable : true,
				triggerAction : 'all',
				forceSelection : true
			}]
		});
	},
	doSelectFeeType : function(combo,rec){
		this.remove(this.items.itemAt(9),true);
		var feeType = rec.get('fee_type');
		//如果是预收款
		if(feeType == 'UNITPRE'){
			this.add(new FeeUnitprePanel(this));
		}else if(feeType == 'CONTRACT'){
			this.add(new FeeUnitprePanel(this));
		}else{
			this.add(new PrintItemPanel(rec.get('printitem_id')));
		}
		Ext.getCmp('feeType').setValue(feeType);
		Ext.getCmp('feeId').setValue(rec.get('fee_id'));
		
		this.doLayout();
	},
	doNumChange : function(){
		if(this.find('name','credentialEndNo').length > 0){
			var endCmp = this.find('name','credentialEndNo')[0];
		
			var startNo = this.find('name','credentialStartNo')[0].getValue();
			var endNo = endCmp.getValue();
			
			//每张凭据金额
			var amount = this.find('name','credentialAmount')[0].getValue();
			//合同金额
			var nominalAmount = Ext.getCmp('nominalAmount').getValue();
			
			if(endNo && startNo){
				if(startNo > endNo){
					Alert("凭据截止号不能小于凭据起始号")
					endCmp.reset();
				}else{
					if(amount && nominalAmount){
						//赠送总金额
						var presentAmount = (endNo-startNo+1)*amount - nominalAmount;
						var presentCmp = this.find('name','presentAmount')[0];
						if(presentAmount < 0){
							presentAmount = 0;
						}
						presentCmp.setValue(presentAmount);
					}
				}
			}
		}
	},
	getValues:function(){
		var values = this.getForm().getValues();
		values['generalContract.nominal_amount'] = Ext.util.Format.formatToFen(values['generalContract.nominal_amount']);
		
		values['credentialAmount'] = Ext.util.Format.formatToFen(values['credentialAmount']);
		values['presentAmount'] = Ext.util.Format.formatToFen(values['presentAmount']);
		
		var endCmp = Ext.getCmp('CredentialEndNo');
		if(endCmp && !endCmp.getValue()){
			values['credentialEndNo'] = 0;
		}
		return values;
	},
	getFee : function(){
		return Ext.getCmp('nominalAmount').getValue();
	},
	success: function(form, resultData){
		App.getApp().main.feeUnitpreGrid.getStore().load();
	}
});

Ext.onReady(function(){
	var panel = new FeeUnitpreForm();
	var box = TemplateFactory.gTemplate(panel);
});