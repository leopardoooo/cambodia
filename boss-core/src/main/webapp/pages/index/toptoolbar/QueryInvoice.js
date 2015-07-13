//发票查询框
var QueryInvoiceWin = Ext.extend(Ext.Window,{
	invoiceTpl:null,
	constructor:function(){
		this.invoiceTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>发&nbsp;票&nbsp;号&nbsp;：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_id}</td>',
					'<td class="label" width=20%>发票代码：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_code}</td>',
				'</tr>',
				'<tr height=24>',
			      	'<td class="label" width=20%>发票类型：</td>',
					'<td class="input_bold" width=30%>&nbsp;{invoice_type_text}</td>',
					'<td class="label" width=20%>所在仓库：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{depot_name}</td>',
			
		    	'</tr>',
		    	'<tr height=24>',
		      		'<td class="label" width=20%>使用状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{status_text}</td>',
				'<td class="label" width=20%>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.amount)]}</td>',		
		    	'</tr>',		    	
		    	'<tr height=24>',
		    		'<td class="label" width=20%>开&nbsp;票&nbsp;员&nbsp;：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{user_optr_name}</td>',
		    		'<td class="label" width=20%>领&nbsp;用&nbsp;人&nbsp;：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{optr_name}</td>',
		    	'</tr>',		    	
		    	'<tr height=24>',
		     		'<td class="label" width=20%>入库时间：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{create_time}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>结存状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{finance_status_text}</td>',
		    		'<td class="label" width=20%>核销时间：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{close_time}</td>',//fm.dateFormat(

		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>结账仓库：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{check_depot_id_text}</td>',		    	
		    		'<td class="label" width=20%>结账时间：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{check_time}</td>',
		    	'</tr>',
		    	'<tr height=24>',

		    	'</tr>',
			'</table>'
		);
		QueryInvoiceWin.superclass.constructor.call(this,{
			id:'queryInvoiceWinId',
			title:'发票查询',
			region:'center',
			closeAction:'close',
			width:620,
			height:350,
			border:false,
			bodyStyle: "background:#F9F9F9",
			items:[
				{layout:'column',bodyStyle:'padding-top:10px',
					defaults:{labelAlign:'right'},labelAlign:'right',
					border:false,items:[
					{columnWidth:.4,layout:'form',border:false,items:[
						{xtype:'textfield',fieldLabel:'发票号码',allowBlank:false,
							name:'invoice_id',vtype:'invoiceId',
							enableKeyEvents :true,
							listeners:{
								scope:this,
								specialkey:function(field,e){
									if(e.getKey() === Ext.EventObject.ENTER){
										this.checkInvoice();
									}
								}
								,change:function(){
									this.checkInvoice();
								}
							}
						}
					]},
					{columnWidth:.4,layout:'form',border:false,items:[
						{fieldLabel: '发票代码',
							xtype : 'combo',
							store : new Ext.data.JsonStore({
								fields : ['invoice_code']
							}),
							hiddenName:'invoice_code',
							displayField : 'invoice_code',
							valueField : 'invoice_code',
							vtype:'invoiceCode',
							allowBlank:false,
							forceSelection : true,
							editable : true
						}
					]},
					{columnWidth:.2,border:false,items:[
						{xtype:'button',text:'查  询',iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}
					]}
				]},
				{xtype : "panel",title:'发票信息',border : false,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html : this.invoiceTpl.applyTemplate({}),
					buttonAlign : 'center',
					buttons : [{
						text : '费用明细',
						id : 'showInvoiceDetail',
						disabled : true,
						scope : this,
						handler : this.showDetailWin
					},{
						text : '修改状态',
						id : 'changeStatusBtn',
						disabled : true,
						scope : this,
						handler : this.changeStatus
					}]
				}
			]
		});
	},
	show:function(){
		QueryInvoiceWin.superclass.show.call(this);
		this.find('name','invoice_id')[0].focus(true,100);
	},
	checkInvoice : function() {
		var invoiceIdCmp = this.find('name','invoice_id')[0]
		var invoiceId = invoiceIdCmp.getValue();
		if (!Ext.form.VTypes.invoiceId(invoiceId))return;
		
		var invoiceCodeCmp = this.find('hiddenName','invoice_code')[0];
		Ext.Ajax.request({
			url:root+'/core/x/Pay!queryInvoiceById.action',
			params:{invoice_id:invoiceId},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.length == 0){
					Alert(invoiceId+' 发票不存在!',function(){
						invoiceCodeCmp.setReadOnly(false);
						invoiceCodeCmp.getStore().removeAll();
						invoiceCodeCmp.setValue('');
						invoiceIdCmp.focus(true,10);
					});
				}
				else{
					invoiceCodeCmp.getStore().loadData(data);
					invoiceCodeCmp.setValue(invoiceCodeCmp.getStore().getAt(0).get('invoice_code'));
					this.doQuery();
				}
			}
		});
	},
	doQuery:function(){
		var invoiceIdComp = this.find('name','invoice_id')[0];
		var invoiceId = invoiceIdComp.getValue();
		if (!Ext.form.VTypes.invoiceId(invoiceId))return;
		var invoiceCode = this.find('hiddenName','invoice_code')[0].getValue();
		if(Ext.isEmpty(invoiceId) || Ext.isEmpty(invoiceCode))return;
		
		Ext.getCmp('showInvoiceDetail').disable();
		Ext.getCmp('changeStatusBtn').disable();
		Ext.Ajax.request({
			url:root+'/core/x/Pay!queryInvoiceByInvoiceId.action',
			params:{invoice_id:invoiceId,invoice_code:invoiceCode},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					this.data = data;
					if(this.data.optr_id==App.data.optr['optr_id']){
						Ext.getCmp('changeStatusBtn').enable();
					}
					Ext.getCmp('showInvoiceDetail').enable();
					
					if(data.status == 'INVALID'){
						Ext.getCmp('changeStatusBtn').setText('修改为空闲');
					}else if(data.status == 'IDLE'){
						Ext.getCmp('changeStatusBtn').setText('修改为失效');
					}else if(data.status == 'USE'){
						Ext.getCmp('changeStatusBtn').disable();
						Ext.getCmp('changeStatusBtn').setText('已被使用');
					}					
					
					if(this.items.itemAt(1).getEl()){
						this.invoiceTpl.overwrite( this.items.itemAt(1).body, data);
					}
				}else{
					Alert('查询发票不存在！',function(){
						if(this.items.itemAt(1).getEl()){
							this.invoiceTpl.overwrite( this.items.itemAt(1).body, {});
						}
						invoiceIdComp.reset();
						invoiceIdComp.focus(true,100);
					},this);
				}
			}
		});
	},
	showDetailWin : function(){
		if(this.data){
			var win = Ext.getCmp('feeDetailWin');
			if(!win){
				win = new FeeDetailWin();
			}
			if(this.data.invoiceDetailList){
				win.grid.getStore().loadData(this.data.invoiceDetailList);
			}
			win.show();
		}
	},
	changeStatus:function(){
		var msg = '是否将发票状态修改为 ';
		var newStatus = 'IDLE';
		if(this.data.status == "INVALID"){
			msg += '空闲';
		}else if(this.data.status == "IDLE"){
			newStatus = 'INVALID';
			msg += '失效';
		}
		Confirm(msg, this ,function(){
			Ext.Ajax.request({
				url:root+'/core/x/Pay!editInvoiceStatus.action',
				params:{invoice_id:this.data.invoice_id,invoice_code:this.data.invoice_code,status:newStatus},
				scope:this,
				success:function(res,opt){
					this.doQuery();
				}
			});
			
		});
	}
});

/**
 * 发票费用明细
 * @class FeeDetailWin
 * @extends Ext.Window
 */
FeeDetailWin = Ext.extend(Ext.Window,{
	grid : null,
	constructor : function(){
		var invoiceDetailStore = new Ext.data.JsonStore({
			fields : ['cust_name', 'cust_no', 'busi_name',
					'fee_name', 'real_pay', 'create_time','optr_name']
		});
		
		this.grid = new Ext.grid.GridPanel({
			ds : invoiceDetailStore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
				header : '客户名称',
				dataIndex : 'cust_name',
				renderer : App.qtipValue
			}, {
				header : '客户编号',
				dataIndex : 'cust_no',
				renderer : App.qtipValue
			}, {
				header : '业务名称',
				dataIndex : 'busi_name',
				renderer : App.qtipValue
			}, {
				header : '费用名称',
				dataIndex : 'fee_name'
			}, {
				header : '实际金额',
				dataIndex : 'real_pay',
				renderer : Ext.util.Format.formatFee
			}, {
				header : '操作时间',
				dataIndex : 'create_time',
				renderer : App.qtipValue
			}, {
				header : '操作员',
				dataIndex : 'optr_name',
				renderer : App.qtipValue
			}]),
			viewConfig : {
				forceFit : true
			}
		});
		
		FeeDetailWin.superclass.constructor.call(this,{
			id : 'feeDetailWin',
			layout : 'fit',
			closeAction : 'hide',
			width:600,
			height:300,
			border:false,
			items : [this.grid]
		})
	}
})
