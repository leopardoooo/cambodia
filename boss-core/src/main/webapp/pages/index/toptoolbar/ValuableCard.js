/**
 * 【充值卡销售】
 */
var ValuableCardgrid = Ext.extend(Ext.grid.EditorGridPanel,{
			constructor:function(){
				ValuableThat = this;
				ValuableCardgrid.superclass.constructor.call(this,{
					id:'valuablegridId',
					ds : new Ext.data.JsonStore({fields : ['valuable_id', 'amount', 'remark','key']}),
					cm : new Ext.grid.ColumnModel([{
							header : '充值卡号',
							dataIndex : 'valuable_id',
							width : 200,
							sortable : true,
							editor : new Ext.form.TextField({allowBlank : false,maxLength: 13,minLength:13})
						}, {
							header : '金额',
							dataIndex : 'amount',
							sortable : true,
							width : 80,
							editor : new Ext.form.NumberField({allowNegative : false,allowBlank : false})
						},{
							header : '备注',
							dataIndex : 'remark',
							sortable : true,
							width : 80,
							renderer : App.qtipValue,
							editor : new Ext.form.TextField({})
						}, {
							header : '操作',
							width : 80,
							renderer : function(v, md, record, i) {
								var rs = Ext.encode(record.data);
								return String
										.format(
												"&nbsp;&nbsp;<a href='#' onclick='ValuableThat.removeRecord({0},{1});' style='color:blue'> 删除 </a>",
												rs, i);
							}
						}]),
					clicksToEdit : 1,	
					sm : new Ext.grid.CheckboxSelectionModel(),
					autoScroll : true,
					border : true,
					tbar : ['客户名称',{
						xtype:'textfield',
						id:'cust_nameId',
						name:'cust_name'
					},'->','插入行数',{
						xtype:'numberfield',
						id:'insertNumId',
						width:40,
						minValue:1,
						value:5
					},{
			        	text : '添加',
			        	iconCls : 'icon-add',
			        	scope : this,
			        	handler : this.onAddValuable
			        },' ']
				})
			},
	removeRecord : function() {
		var rec = Ext.getCmp('valuablegridId').getSelectionModel().getSelected();
		Ext.getCmp('valuablegridId').getStore().remove(rec);
	},onAddValuable : function() {
		var num = Ext.getCmp('insertNumId').getValue();
		var grid = Ext.getCmp('valuablegridId');
		var store = grid.getStore();
		var valuableId='';
		var amount = '';
		var remark = '';
		grid.stopEditing();
		if(store.getCount()>0){
			valuableId = store.getAt(0).data.valuable_id;
			amount =  store.getAt(0).data.amount;
			remark = store.getAt(0).data.remark;
		}
		for(var i=1;i<=num;i++){
			this.gridAdd(grid, {
					valuable_id : valuableId==''?'':parseInt(valuableId)+i,
					amount : amount,
					remark : remark,
					key:''
			});
		}
	},
	gridAdd : function(scopeThis, fieldsObj, editColumn) {
		editColumn = editColumn || 0;
		var store = scopeThis.getStore();
		var recordType = store.recordType;
		var recordData = new recordType(fieldsObj);
		scopeThis.stopEditing();
		store.insert(0, recordData);
		scopeThis.startEditing(0, editColumn);
		scopeThis.getSelectionModel().selectRow(0);
	},initEvents : function(){
		this.on('afterrender',function(){
			this.gridAdd(this, {
					valuable_id : '',
					amount : '',
					remark : '',
					key:''
			},0);
		});
		ValuableCardgrid.superclass.initEvents.call(this);
	}
		
})
			
var ValuableCardForm = Ext.extend(BaseForm,{
	url : root + '/commons/x/QueryDevice!saveValuableCard.action',
	valuablegrid:null,
	fee:null,
//	isAllowGo:false,
	constructor:function(){
		cardThat = this;
		this.valuablegrid = new ValuableCardgrid();
		ValuableCardForm.superclass.constructor.call(this,{
			border : false,
			id:'valuablecardId',
			layout : 'border',
			items:[{	region : 'center',
						layout : 'fit',
						items:[this.valuablegrid]
			}]
		})
	},doValid : function() {
		var obj = {};amount=0;
		var That = Ext.getCmp('valuablegridId');
		That.stopEditing();// 停止编辑
		var store = That.getStore();
		var count = store.getCount();// 总个数
		var config = That.getColumnModel().config;

		App.getApp().main.valuableCardGrid.custName  = Ext.getCmp('cust_nameId').getValue();
		var dataIndexes = [];
		for (var i = 0; i < config.length; i++) {
			dataIndexes.push(config[i].dataIndex);
		}
		for (var i = 0; i < count; i++) {
			var data = store.getAt(i).data;
			amount += data['amount']
			for (var k = 0; k < dataIndexes.length; k++) {
				var a = dataIndexes[k];
				if (Ext.isEmpty(data[a])) {
					if(a == 'valuable_id'||a == 'amount'){
						obj["isValid"] = false
						obj["msg"] = "请编辑完整";
						That.getSelectionModel().selectRow(i);
						That.startEditing(i, k);
						return obj;	
					}
				}
			}
			if(data['amount'] == 0){
				obj["isValid"] = false
				obj["msg"] = "第"+(i+1)+"行的金额不能为0,请重新编辑";
				return obj;	
			}
			if(count>1){	
				for(var j=i+1;j<count;j++){
					var d = store.getAt(j).data;
					if(data["valuable_id"] == d["valuable_id"]){
						obj["isValid"] = false
						obj["msg"] = "第"+(i+1)+"行和第"+(j+1)+"行充值卡相同,请重新编辑";
						return obj;	
					}
				}	
			}
		}
		this.fee = amount;
		return obj;
	},getValues:function(){
			var all = {};amount=null;
			var grid  = Ext.getCmp('valuablegridId');
			grid.stopEditing();
			var store = grid.getStore();
			var datas = [];
			var name = Ext.getCmp('cust_nameId').getValue();
			store.each(function(record) {
						datas.push({
									valuable_id : record.get('valuable_id'),
									amount : Ext.util.Format.formatToFen(record.get('amount')),
									cust_name:name,
									remark : record.get('remark')
								});
						amount += 	Ext.util.Format.formatToFen(record.get('amount'));	
								
					});
			var records = Ext.encode(datas);
			all["amount"] = amount;
			all["records"] = records;
		return all;
	},	
	success: function(form, res){
//		if(!Ext.isEmpty(res)){
//			App.getApp().menu.bigWindow.show({ text: '打印',  attrs: {busiCode:'1227',
//			url: 'pages/business/pay/Print.jsp?type=feesn&feesn='+res}} ,{width: 710, height: 460});
//		}else{
			App.getApp().main.valuableCardGrid.getStore().load();
//		}
	},getFee:function(){
		return this.fee;
	}
})

Ext.onReady(function(){
	var panel = new ValuableCardForm();
	var box = TemplateFactory.gTemplate(panel);
});