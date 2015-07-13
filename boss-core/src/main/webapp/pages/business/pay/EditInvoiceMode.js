EditInvocieForm = Ext.extend(BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/Pay!editInvoiceMode.action",
	record : null,
	constructor : function() {
		//取合同管理选中数据
		if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.feeUnitpreGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			var grid = App.getApp().main.valuableCardGrid;
			if(grid.getSelectionModel().getSelected()){
				this.record = grid.getSelectionModel().getSelected();
			}
		}else{
			this.record = App.getData().currentRec;
		}
		
		EditInvocieForm.superclass.constructor.call(this, {
			border : false,
			labelWidth: 100,
			layout: 'form',
			bodyStyle: Constant.TAB_STYLE,
			defaults: {
                layout:'form',
                baseCls: 'x-plain',
                labelWidth: 100
            },
			items:[{
				id : 'invoiceModeComb',
				fieldLabel: '新出票方式',
				maxLength: 18,
				xtype: 'paramcombo',
				allowBlank: false,
				paramName: 'INVOICE_MODE',
				hiddenName: 'invoice_mode',
				listeners :{
					scope : this,
					select : this.doChangeInvoiceMode
				}
			},{
				id : 'oldInvoicePanel',
				hidden : (this.record.get('invoice_id') && this.record.get('invoice_mode')== 'A')  ? false : true,
				items :[{
					id:'old_invoice_id',
					xtype: 'textfield',
					name:'oldInvoice.invoice_id',
					fieldLabel:'原发票号码',
					style : Constant.TEXTFIELD_STYLE,
					value : this.record.get('invoice_id')
				},{
					id:'old_invoice_code_id',
					xtype: 'textfield',
					name:'oldInvoice.invoice_code',
					fieldLabel:'原发票代码',
					style : Constant.TEXTFIELD_STYLE,
					value : this.record.get('invoice_code')
				},{
					id:'old_invoice_book_id',
					name:'oldInvoice.invoice_book_id',
					xtype : 'hidden',
					value : this.record.get('invoice_book_id')
					
				},{
					id:'status_id',
					xtype : 'combo',
					store : new Ext.data.SimpleStore({
								fields : ['status', 'status_text'],
								data : [['INVALID', '作废'], ['IDLE', '空闲']]
							}),
					displayField : 'status_text',
					valueField : 'status',
					fieldLabel : '原发票状态',
					forceSelection : true,
					allowBlank : (this.record.get('invoice_id') && this.record.get('invoice_mode')== 'A')  ? false : true,
					hiddenName : 'oldInvoice.status'
				}]
			},{
				id : 'newInvoicePanel',
				hidden : true,
				items :[{
					id : 'new_invoice_id',
					name : 'newInvoice.invoice_id',
					fieldLabel : '新发票号码',
					 xtype: 'textfield',
					listeners : {
						change : this.checkInvoice
					}
				},{
					id : 'new_invoice_code',
					xtype : 'combo',
					store : new Ext.data.JsonStore({
						fields : ['invoice_code','invoice_book_id']
					}),
					hiddenName:'newInvoice.invoice_code',
					fieldLabel:'新发票代码',
					displayField : 'invoice_code',
					valueField : 'invoice_code',
					forceSelection : true,
					listeners : {
						beforequery : function(){
							if(this.getStore().getCount() == 0){
								Alert('请先输入发票号码');
								return false;
							}
						},
						select : function(combo,rec){
							Ext.getCmp('new_invoice_book_id').setValue(rec.get('invoice_book_id'));
						}
					}
				},
					{id:'new_invoice_book_id',name : 'newInvoice.invoice_book_id',xtype : 'hidden'},
					{id:'feeSn',name : 'feeSn',xtype : 'hidden',value : this.record.get('fee_sn')},
					{id:'realPay',name : 'realPay',xtype : 'hidden',value : this.record.get('real_pay')
				}]
			}]
		});
	},
	doInit : function(){
		var store = Ext.getCmp('invoiceModeComb').getStore();
		if(this.record.get('invoice_mode')){
			store.removeAt(store.find('item_value',this.record.get('invoice_mode')));
		}else{
			store.removeAt(store.find('item_value','A'));
		}
		
	},
	doChangeInvoiceMode : function(combo){
		var value = combo.getValue();
		if(value == 'M'){
			/*
			Ext.getCmp('new_invoice_id').enable();
			Ext.getCmp('new_invoice_code').enable();
			Ext.getCmp('newInvoicePanel').show();
			*/
			Ext.getCmp('new_invoice_id').disable();
			Ext.getCmp('new_invoice_code').disable();
			Ext.getCmp('newInvoicePanel').hide();
		}else{
			Ext.getCmp('new_invoice_id').disable();
			Ext.getCmp('new_invoice_code').disable();
			Ext.getCmp('newInvoicePanel').hide();
		}
	},
	checkInvoice : function(txt){
		if(txt.isValid()){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
				async: false,
				params:{
					invoice_id:txt.getValue(),
					invoice_mode : 'M'
				},
				success:function(res,ops){
					var rec = Ext.decode(res.responseText);
					var invoiceCode = Ext.getCmp('new_invoice_code');
					if(rec.length == 0){
						Alert('该发票无法使用');
					}else{
						var data = [];
						for(var i=0;i<rec.length;i++){
							var obj = {};
							obj['invoice_code'] = rec[i].invoice_code;
							obj['invoice_book_id'] = rec[i].invoice_book_id
							data.push(obj);
						}
						invoiceCode.getStore().loadData(data);
					}
				},
				clearData:function(){
					txt.setValue("");
					var invoiceCode = Ext.getCmp('new_invoice_code');
					invoiceCode.getStore().removeAll();
				}
			});
		}
	},
	success:function(){
		if(App.getApp().main.feeUnitpreGrid && App.getApp().main.feeUnitpreGrid.getSelectionModel().getSelected()){
			App.getApp().main.feeUnitpreGrid.getStore().reload();
		}else if(App.getApp().main.valuableCardGrid && App.getApp().main.valuableCardGrid.getSelectionModel().getSelected()){
			App.getApp().main.valuableCardGrid.getStore().reload();
		}else{
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		}
	}
});

Ext.onReady(function(){
	var form = new EditInvocieForm();
	TemplateFactory.gTemplate(form);
});