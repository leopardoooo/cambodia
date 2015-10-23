
AutoInvoicePanel = Ext.extend( Ext.Panel, {
	constructor: function(){
		AutoInvoicePanel.superclass.constructor.call(this, {
			trackResetOnLoad : true,
			border : false,
			layout: 'form',
			bodyStyle:Constant.TAB_STYLE,
			labelWidth : 135,
			defaultType:'textfield',
			items:[
				{id:'old_invoice_id',name:'oldInvoice.invoice_id',fieldLabel:lmain("doc._form.oldInvoiceId"),style : Constant.TEXTFIELD_STYLE},
				{id:'old_invoice_code_id',name:'oldInvoice.invoice_code',xtype:'hidden'},
				{id:'old_invoice_type_text',xtype:'displayfield',fieldLabel:lmain("doc._form.oldInvoiceType")},
				{id:'old_invoice_type',name:'oldInvoice.doc_type',xtype:'hidden'},
				{id:'old_invoice_book_id',name:'oldInvoice.invoice_book_id',xtype : 'hidden'},
				{id:'new_invoice_book_id',name : 'newInvoice.invoice_book_id',xtype : 'hidden'},
				{id:'old_doc_sn',name : 'docSn',xtype : 'hidden'},
				{id:'doc_type',name : 'doc_type',xtype : 'hidden'},
				{
					xtype : 'paramcombo',
					paramName: 'EDIT_STATUS_R_DEVICE',
					/*store : new Ext.data.SimpleStore({
								fields : ['status', 'status_text'],
								data : [['INVALID', '作废'], ['IDLE', '空闲']]
							}),
					displayField : 'status_text',
					valueField : 'status',*/
					fieldLabel : lmain("doc._form.oldStatus"),
					forceSelection : true,
					allowBlank : false,
					hiddenName : 'oldInvoice.status'
				},
				{
					id : 'new_invoice_id',
					allowBlank : false,
					name : 'newInvoice.invoice_id',
					fieldLabel : lmain("doc._form.newInvoiceId"),
					listeners : {
						change : this.checkInvoice
					}
				},{id : 'new_invoice_code',xtype:'hidden',name: 'newInvoice.invoice_code'}/*{
					id : 'new_invoice_code',
					allowBlank:false,
					xtype : 'combo',
					store : new Ext.data.JsonStore({
						fields : ['invoice_code','invoice_book_id','invoice_type','invoice_type_text'],
						listeners:{
							scope:this,
							load:function(store){
								if(store.getCount() == 1){
									var record = store.getAt(0);
									Ext.getCmp('new_invoice_code').setValue(record.get('invoice_code'));
									Ext.getCmp('new_invoice_book_id').setValue(record.get('invoice_book_id'));
								}
							}
						}
					}),
					hiddenName:'newInvoice.invoice_code',
					fieldLabel:lmain("doc._form.newInvoiceCode"),
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
							Ext.getCmp('new_invoice_code').setValue(rec.get('invoice_code'));
							Ext.getCmp('new_invoice_book_id').setValue(rec.get('invoice_book_id'));
						}
					}
				}*/				
			]
		})
		this.doInit();
	},	
	doInit:function(){
		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		Ext.getCmp('old_invoice_id').setValue(record.get('invoice_id'));
		Ext.getCmp('old_invoice_book_id').setValue(record.get('invoice_book_id'));
		Ext.getCmp('old_invoice_code_id').setValue(record.get('invoice_code'));
		Ext.getCmp('old_invoice_type').setValue(record.get('doc_type'));
		Ext.getCmp('old_invoice_type_text').setValue(record.get('doc_type_text'));
		Ext.getCmp('old_doc_sn').setValue(record.get('doc_sn'));
		Ext.getCmp('doc_type').setValue(record.get('doc_type'));
	},
	checkInvoice : function(txt){
		if(txt.isValid()){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
//				async: false,
				params:{
					invoice_id:txt.getValue(),
					doc_type:Ext.getCmp('doc_type').getValue(),
					invoice_mode:"A"
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
							obj['invoice_book_id'] = rec[i].invoice_book_id;
							obj['invoice_type'] = rec[i].invoice_type;
							obj['invoice_type_text'] = rec[i].invoice_type_text
							data.push(obj);
						}
						/*invoiceCode.setValue('');
						invoiceCode.getStore().loadData(data);*/
						if(data.length > 0)
							invoiceCode.setValue(data[0]['invoice_code']);
					}
				},
				clearData:function(){
					txt.setValue("");
					Ext.getCmp('new_invoice_code').setValue('');
				}
			});
		}
	}
});

EditInvocieForm = Ext.extend(BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/Pay!saveChangeInvoice.action",
	invoiceEditStore : null,//发票下拉框编辑数据
	editItem: null,
	constructor : function() {
		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		var invoice_mode = record.get('invoice_mode');
		if (invoice_mode=='A'){
			this.editItem = new AutoInvoicePanel();
		}else{
			this.invoiceEditStore = new Ext.data.JsonStore({
	    		fields : ['new_invoice_book_id','new_invoice_code']
			});
			
			var itemStore = new Ext.data.JsonStore({
				url : Constant.ROOT_PATH + "/core/x/Pay!queryInvoice.action",
				baseParams: {invoice_id:record.get('invoice_id'), invoice_code:record.get('invoice_code'),custId:App.getApp().getCust().cust_id},
				fields : ['invoice_code', 'invoice_id', 'invoice_book_id', 'fee_sn','real_pay','fee_name']
			});
			itemStore.load();
			
			this.editItem =  new Ext.grid.EditorGridPanel({
				clicksToEdit: 1,
				border: false,
				store: itemStore,
				width: 10,
				height: 210,
				columns: [{
	                header: '费用名称',
	                align: 'center',
	                dataIndex: 'fee_name',
	                width: 100
	            },{
	                header: '金额',
	                align: 'center',
	                dataIndex: 'real_pay',
	                width: 60,
	                renderer:Ext.util.Format.formatFee
	            },{
	                header: '原发票号',
	                align: 'center',
	                dataIndex: 'invoice_id',
	                width: 80
	            },{
	                header: '原发票本号',
	                hidden :true,
	                dataIndex: 'invoice_book_id'
	                
	            }, {
	                header: '原发票代码',align: 'center',
	                dataIndex: 'invoice_code',
	                width: 80
				}, {
	                header: '新发票号',
	                width: 80,
	                align: 'center',
	                dataIndex: 'new_invoice_id',
	                editor: new Ext.form.TextField({
	                	vtype : 'invoiceId'
	                })
				}, {
	                header: '新发票代码',
	                width: 80,
	                align: 'center',
	                id : 'col_invoiceCode',
	                dataIndex: 'new_invoice_code',
	                editor: new Ext.form.ComboBox({
	                	store : this.invoiceEditStore,
	                	displayField : 'new_invoice_code',
	                	valueField : 'new_invoice_code',
	                	allowBlank : false,
						mode: 'local',
						triggerAction : "all"
	                })
				}],
	        	listeners : {
	        		scope : this,
	        		beforeedit : this.beforeEdit,
	        		afteredit : this.afterEdit
	        	}
			});
		}
		
		EditInvocieForm.superclass.constructor.call(this, {
			border : false,
			labelWidth : 80,
//			bodyStyle : Constant.TAB_STYLE,
			layout: 'fit',
			items:this.editItem
		});
	},
	beforeEdit : function(obj){
		if(obj.field == 'new_invoice_book_id'){
			if(!obj.record.get('new_invoice_id')){
				Alert('请输入发票号码');
				return false;
			}
		}
	},
	afterEdit : function(o){
		if(o.field == 'new_invoice_id'){
			var currentVal = parseInt(o.value , 10);
			if("" == currentVal) return ;
			if (currentVal==o.record.get('invoice_id')){
				Alert('修改发票不能与原发票相同');
				o.record.set('new_invoice_id','');
				return;
			}
			var fm = Ext.util.Format;
			o.record.set('new_invoice_id', o.value);
			Ext.Ajax.request({
				scope : this,
				url:Constant.ROOT_PATH + "/core/x/Pay!checkInvoice.action",
				async: false,
				params:{
					invoice_id:o.value,
					invoice_mode:'M'
				},
				success:function(res,ops){
					var rec = Ext.decode(res.responseText);
					if(rec.length == 0){
						o.record.set('new_invoice_id','');
						o.record.set('new_invoice_book_id','');
						o.record.set('new_invoice_code','');
					}else{
						var store = this.editItem.getColumnModel().getColumnById('col_invoiceCode').editor.getStore();
						var data = [];
						for(var i=0;i<rec.length;i++){
							var obj = {};
							obj['new_invoice_book_id'] = rec[i].invoice_book_id
							obj['new_invoice_code'] = rec[i].invoice_code;
							data.push(obj);
						}
						store.loadData(data);
						o.record.set('new_invoice_book_id',data[0]['new_invoice_book_id']);
						o.record.set('new_invoice_code',data[0]['new_invoice_code']);
//						this.stopEditing();
//						this.editItem.startEditing(1, 3);
					}
				},
				clearData:function(){
					//清空组件
					o.record.set('new_invoice_id','');
					o.record.set('new_invoice_book_id','');
					o.record.set('new_invoice_code','');
				}
			});
		}else if(o.field == 'new_invoice_book_id'){
			var store = this.editItem.getColumnModel().getColumnById('col_invoiceCode').editor.getStore();
			var index = store.find('new_invoice_code',o.value);
			o.record.set('new_invoice_book_id',store.getAt(index).get('new_invoice_book_id'));
		}
	},
	doValid : function(){
		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		if(record.get('invoice_mode')=='M'){
			this.editItem.stopEditing();
			var store = this.editItem.getStore();
			var hasEdit = false;
			for (var k = 0; k < store.getCount(); k++) {
				var rs = store.getAt(k);
				if (!rs.get('new_invoice_id')){
					var obj = {};
					obj['isValid'] = false;
					obj['msg'] = '新发票号不能为空';
					return obj;
				}
				
				if(rs.get('new_invoice_book_id')){
					hasEdit = true;
				}
			}
			
			if(!hasEdit){
				var obj = {};
				obj['isValid'] = false;
				obj['msg'] = '请输入新的发票号';
				return obj;
			}
		}
		return EditInvocieForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var record = App.getApp().main.infoPanel.docPanel.invoiceGrid.getSelectionModel().getSelected();
		if(record.get('invoice_mode')=='M'){
			var store = this.editItem.getStore();
			var fees = [];
			for (var k = 0; k < store.getCount(); k++) {
				var rs = store.getAt(k);
				if (rs.get('new_invoice_id')!=null){
					fees.push(rs.data);
				}
			}
			return {'feedata':Ext.encode(fees)};
		}else{
			return this.getForm().getValues();
		}

	},
	doInit:function(){
		EditInvocieForm.superclass.doInit.call(this);
	},
	success:function(){
		App.getApp().main.infoPanel.docPanel.invoiceGrid.remoteRefresh();
		App.getApp().main.infoPanel.payfeePanel.refresh();
	}
});
Ext.onReady(function(){
	var form = new EditInvocieForm();
	TemplateFactory.gTemplate(form);
});