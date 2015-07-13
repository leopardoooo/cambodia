//批量加入单位
ResidentForm = Ext.extend( Ext.Panel , {
	parent:null,
	constructor: function(p){
		this.parent = p;
		ResidentForm.superclass.constructor.call(this, {
			layout : 'form',
			anchor: '100%',
			bodyStyle: "background:#F9F9F9",
			border : false,
			baseCls: 'x-plain',
			items : [{
				layout:'column',
				baseCls: 'x-plain',
				anchor: '100%',
				defaults:{
					baseCls: 'x-plain',
					bodyStyle: "background:#F9F9F9;padding-top:4px",
					layout: 'form',
					labelWidth: 75
				},
				items:[{
					columnWidth:0.33,
					items:[{
						fieldLabel:'优惠类型',
						xtype:'paramcombo',
						hiddenName:'cust_class',
						paramName:'CUST_CLASS',
						allowBlankItem:true
					}]
				},{
					columnWidth:0.33,
					items:[{
						id:'res_cust_colony_id',
						fieldLabel:'客户群体',
						xtype:'paramcombo',
						allowBlankItem:true,
						hiddenName:'cust_colony',
						paramName:'CUST_COLONY',
						listeners : {
							scope : this,
							select : function(combo){
								this.parent.custColony = combo.getValue();
							}
						}
					}]
				},{
					columnWidth:0.5,
					items:[{
						fieldLabel:'模糊地址',
						xtype:'textfield',
						width:200,
						emptyText :'输入详细点的地址',
						name:'address'
					}]
				},{
					columnWidth:0.2,
					items:[{xtype:'button',
							text:'查  询',
							iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}]
				}]
			}]
		})
	},
	doQuery : function(){
		this.parent.custGrid.custStore.baseParams = {'cust.cust_class':this.find('hiddenName','cust_class')[0].getValue(),
													 'cust.cust_colony':this.find('hiddenName','cust_colony')[0].getValue(),
													 'cust.address':this.find('name','address')[0].getValue()};
		this.parent.custGrid.custStore.load();
		this.parent.custGrid.custStore.on("load",this.parent.custGrid.countData,this.parent);
	}
});
/**
 * 客户选择框
 * @class CustGrid
 * @extends Ext.grid.GridPanel
 */
CustGrid = Ext.extend(Ext.grid.GridPanel,{
	custStore : null,
	count:null,
	constructor : function(){
		this.custStore = new Ext.data.JsonStore({
			url: root + '/commons/x/QueryCust!searchResidentCust.action',
			fields: Ext.data.Record.create([
				"cust_id","cust_no","cust_name","addr_id_text",
				"address","status","cust_type","status_text",
				"cust_type_text","cust_level_text","net_type_text",
				"is_black_text","open_time"
			])
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var cm = [
			sm,
			{header: '受理编号', dataIndex: 'cust_no'},
			{header: '客户名称', dataIndex: 'cust_name'},
			{header: '客户地址', dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: '客户状态', dataIndex: 'status_text',width: 80},
			{header: '客户类型', dataIndex: 'cust_type_text',width: 80},
			{header: '客户级别', dataIndex: 'cust_level_text',width: 80},
			{header: '黑名单', dataIndex: 'is_black_text',width: 60},
			{id: 'autoCol', header: '开户时间', dataIndex: 'open_time'}
		];
		
		CustGrid.superclass.constructor.call(this,{
			title: '选择客户【最多显示500条】',
			stripeRows: true, 
			id:'joinUnitCustId',
      		autoExpandColumn: 'autoCol',
	        store: this.custStore,
	        columns: cm,
	        sm : sm
		})
	},
	countData:function(store){
		this.count = store.getCount();
		if(this.count == 500){
			Ext.getCmp('joinUnitCustId').setTitle('选择客户【最多显示500条,查询结果:'+this.count+'】,数据过多请输入详细地址');
		}else{
			Ext.getCmp('joinUnitCustId').setTitle('选择客户【最多显示500条,查询结果:'+this.count+'】');
		}
	}
});

BatchJoinUnitForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + "/core/x/Cust!batchJoinUnit.action",
	custGrid : null,
	custform:null,
	constructor : function(){		
		this.custform = new ResidentForm(this);
		this.custGrid = new CustGrid();
		BatchJoinUnitForm.superclass.constructor.call(this,{
			bodyStyle:"background:#F9F9F9;",
			width:700,
			border : false,
			layout : 'border',
			items : [{
				region:'north',
				layout:'fit',
				height:80,
				border : false,
				items:[this.custform]
			},
			{
				region : 'center',
				layout:'fit',
				border : false,
				items:[this.custGrid]
			}]
		})
	},
	doInit:function(){
		BatchJoinUnitForm.superclass.doInit.call(this);
		var store = Ext.getCmp('res_cust_colony_id').getStore();
		store.each(function(record){
			var value = record.get('item_value');
			if(value == 'MNDKH' || value == 'XYKH'){
				store.remove(record);
			}
		});
	},
	initComponent: function(){
		BatchJoinUnitForm.superclass.initComponent.call(this);
		//初始化下拉框的参数
		var comboes = this.custform.findByType("paramcombo");
		App.form.initComboData( comboes , this ,this );
	},
	doValid : function(){
		var obj = {};
		var records = this.custGrid.getSelectionModel().getSelections();
		if(records.length == 0){
			obj['isValid'] = false;
			obj['msg'] = '请至少选择一个客户';
			return obj;
		}
		return true;
	},
	getValues : function(){
		var all = {};
		var custIds = [];
		var records = this.custGrid.getSelectionModel().getSelections();
		for(var i=0;i<records.length;i++){
			custIds.push(records[i].get('cust_id'));
		}
		
		all['custIds'] = custIds.join(",");
		return all;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})

/**
 * 入口
 */
Ext.onReady(function(){
	var tf = new BatchJoinUnitForm();
	var box = TemplateFactory.gTemplate(tf);
});