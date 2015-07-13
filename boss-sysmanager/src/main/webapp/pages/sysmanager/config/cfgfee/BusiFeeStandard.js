/**
 * 费用值
 */
App.pageSize = 15 ;	
/**
 * 
 * 删除选中配置
 */
current = this;
logoff = function(v,rowIndex) {
	Confirm("确定要删除该配置吗?", null, function() {				
				current.loadMoreInfo(v,rowIndex);
			})
}

var feeStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryfee.action',
			fields : ['fee_id', 'fee_name']
		});

var feevaluestore = new Ext.data.JsonStore({
			url : root + '/config/BusiFeeStandard!query.action',
			root : 'records',
			autoDestroy : true,
			totalProperty : 'totalProperty',
			fields : ['fee_id', 'fee_name', 'fee_type','value_id',
					'fee_value',"fee_type_text"]
		});

/**
 * 加载更多信息
 */
loadMoreInfo = function(v,rowIndex) {
	Ext.Ajax.request({
				scope : this,
				params : {
					"busiFeeDto.value_id":v
				},
				url : root + '/config/BusiFeeStandard!delete.action',
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);						
					if (true === rs) {					
						Alert("操作成功!",function(){
							busiFeeGrid.getStore().removeAt(rowIndex);
						});
					}
					else{
						Alert(rs);
					}
				}
			});
};
/**
 * 查询组件
 * @class SelectFeeForm
 * @extends Ext.Panel
 */
SelectFeeForm = Ext.extend(Ext.Panel, {
			constructor : function() {
				SelectFeeForm.superclass.constructor.call(this, {
							split : true,
							height : 20,
							layout : 'column',
							items : [{										
										emptyText : "选择费用类型",
										xtype : 'paramcombo',
										id:'fee_typeId',
										paramName : 'FEE_TYPE',
										hiddenName : 'fee_type',
										width:120
									}]
						})
			},
			initComponent : function() {
				SelectFeeForm.superclass.initComponent.call(this);
				// 初始化下拉框的参数
				var comboes = this.findByType("paramcombo");
				App.form.initComboData(comboes);
			}
		});

var addTitle = "【新增】费用值", addCls = "icon-add-user", editTitle = "【修改】费用值", editCls = "icon-edit-user";
/**
 * 封装表单
 * @class TemplateCountyForm
 * @extends Ext.form.FormPanel
 */
BusiFeeValueForm = Ext.extend(Ext.form.FormPanel, {

	constructor : function() {
		BusiFeeValueForm.superclass.constructor.call(this, {
					title : addTitle,
					iconCls : addCls,
					region : 'south',
					split : true,
					height : 130,
					layout : 'column',
					bodyStyle : 'padding: 5px',
					defaults : {
						labelWidth : 70,
						layout : 'form',
						baseCls : 'x-plain',
						defaultType : 'textfield'
					},
					items:[{
								xtype : 'hidden',								
								name : 'value_id'
							},{
								columnWidth : .3,
								items : [{
											fieldLabel : '费用名称',
											xtype : 'combo',
											store : feeStore,
											mode : 'local',
											displayField : 'fee_name',
											valueField : 'fee_id',
											hiddenName : 'fee_id',
											allowBlank : false,										
											width:180,
											forceSelection : true,									
											editable : true								
										}]
							}, {
								columnWidth : .3,
								items : [{
											fieldLabel : '费用值(分)',
											id:'feeValueId',
											name : 'fee_value',
											xtype:'numberfield',
											grow :true,
											growMin :120,
											growMax :200,
											allowBlank : false
										}]
							}],
					buttons : [{
						text : ' 重 置 ',
						scope: this,
						handler : function() {
							this.doReset();
						}
					}, {
						text : ' 保 存 ',
						scope: this,
						handler : function() {
							this.doSave();
						}
					}]

				})
	},
	doSave: function(){		
		if(!busiFeeForm.getForm().isValid()){return ;}	
		var old =  busiFeeForm.getForm().getValues(), newValues = {};
		for(var key in old){
			newValues["busiFeeDto." + key] = old[key];
		}		
		Confirm("确定要保存吗?", this , function(){
			Ext.Ajax.request({
				params: newValues,
				url: root + '/config/BusiFeeStandard!save.action',
				success: function(res,ops){
					var rs = Ext.decode(res.responseText);						
					if (true === rs) {					
						Alert("操作成功!",function(){
						busiFeeGrid.store.load({params: { start: 0, limit: App.pageSize }});
						busiFeeForm.getForm().reset();
						busiFeeForm.setIconClass( addCls  );
						busiFeeForm.setTitle( addTitle );
						});
					}else{
						Alert(rs);
					}
				}
			});
		});
	},
	doReset: function(){
		busiFeeForm.getForm().reset();
		busiFeeForm.setIconClass( addCls  );
		busiFeeForm.setTitle( addTitle );
	}
});

BusiFeeValueGrid = Ext.extend(Ext.grid.GridPanel, {
	selectFeeForm : null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		this.selectFeeForm = new SelectFeeForm(this);
		BusiFeeValueGrid.superclass.constructor.call(this, {
					title : '费用配置',
					region : 'center',
					ds : feevaluestore,
					clicksToEdit:1,
					cm : new Ext.grid.ColumnModel([{
								id : 'fee_name',
								header : '费用名称',
								width:150,
								dataIndex : 'fee_name',
								renderer: App.qtipValue
							}, {
								header : '费用类型',
								width:150,
								dataIndex : 'fee_type_text'
							}, {
								header:'费用值(分)',
								width:150,
								dataIndex:'fee_value'
							},{
								header : '操作',
								width:150,
								dataIndex : 'value_id',
								renderer : function(v, md, record, i) {
									return "<a href='#' onclick='logoff("+ v +"," + i
											+ ");' style='color:blue'> 删除 </a>";
								}
							}]),					
					bbar : new Ext.PagingToolbar({
								store : feevaluestore
							}),

					tbar : [' ', '查询条件:',this.selectFeeForm, {
						text : ' 查询 ',
						height : 20,
						iconCls : "icon-query",
						scope : this.selectFeeForm,
						xtype : 'button',
						store : feevaluestore,
						tooltip : '无需每个选项都选择',
						handler : function() {
							feevaluestore.load({
										params : {start: 0, limit: App.pageSize ,feetype:Ext.getCmp('fee_typeId').getValue() 											
										}
									});
						}
					}]

				})
	},
	listeners : {
		"rowdblclick" : function(g, i, e) {
			var rs = g.getStore().getAt(i);			
			busiFeeForm.getForm().loadRecord(rs);
			busiFeeForm.setTitle(editTitle);
			busiFeeForm.setIconClass(editCls);					

		}
	}

});

Ext.onReady(function() {		
	// 展现
			busiFeeGrid = new BusiFeeValueGrid();
			busiFeeForm = new BusiFeeValueForm();		
			App.form.initComboData( busiFeeForm.findByType("paramcombo"));			
			App.query = new Ext.Viewport({
						layout : 'border',
						items : [busiFeeGrid, busiFeeForm]
					});		
			feeStore.load();
			App.clearLoadImage();		
			
		});
