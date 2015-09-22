
/**
 * 费用项Record定义
 */
BusiFeeItem = Ext.data.Record.create([
	{name: 'fee_id'},
	{name: 'fee_name'},
	{name: 'can_update'},
	{name: 'default_value', type: 'float'},
	{name: 'max_value', type: 'float'},
	{name: 'min_value', type: 'float'},
	{name: 'count', type: 'float'},
	{name: 'feeValue', type: 'float'}
]);

SERVICEPANEL = {};
SERVICEPANEL.BOTTOM_HEIGHT = 100 ;
SERVICEPANEL.TASKFORM_WIDTH = 160;

/**
 * 封装工程服务、营业服务面板
 * @class ServicePanel
 * @extends Ext.Panel
 */
ServicePanel = Ext.extend( Ext.Panel ,{
	
	serviceGrid: null,
	constructor: function( data ){
	
		this.serviceGrid = new ServiceGrid(data);
		
		ServicePanel.superclass.constructor.call(this, {
			border: false,
			layout: 'border',
			items: [{
				region: 'center',
				layout: 'border',
				items: [{ 
					region: 'center',
					layout: 'fit',
					border: false,
					items: [ this.serviceGrid ]
				}]
			}]
		});
	},
	getServiceGrid: function(){
		return this.serviceGrid;
	},
	getValues: function(){
		return this.serviceGrid.getValues();
	},
	doValid : function(){
		return true;
	},
	getFee: function(){
		this.serviceGrid.stopEditing();
		return parseFloat(Ext.getCmp('txtSumFee').getValue());
	}
});


/**
 * 费用项表格构建  
 * @class ServiceGrid
 * @extends Ext.grid.GridPanel
 */
ServiceGrid = Ext.extend( Ext.grid.EditorGridPanel, {
	
	serviceStore: null,
	totalBusiFee : 0,
	checkSm : null,
	constructor: function( data){
		var records = [];
		if(data){
			for(var i=0;i<data.length;i++){
				var rec = {};
				for(var key in data[i]){
					rec[key] = data[i][key];
				}
				rec.default_value = Ext.util.Format.formatFee(rec.default_value);
				rec.max_value = Ext.util.Format.formatFee(rec.max_value);
				rec.min_value = Ext.util.Format.formatFee(rec.min_value);
				rec.count = 1;
				rec.feeValue = rec.default_value;
				records.push(rec);
			}
		}
		
		this.serviceStore = new Ext.data.JsonStore({
			fields: BusiFeeItem
		});
		this.serviceStore.loadData(records);
		
		
		this.checkSm = new Ext.grid.CheckboxSelectionModel({
			checkOnly : true,
			listeners : {
				rowselect : this.doSelectedChange,
				rowdeselect : this.doSelectedChange
			}
		});
		var cm = langUtils.bc("common.fee.columns");
		ServiceGrid.superclass.constructor.call(this, {
			border: false,
			autoScroll: true,
			store : this.serviceStore,
			forceValidation: true,
			autoExpandColumn : 'e',
	        clicksToEdit: 1,
			sm : this.checkSm,
			viewConfig:{forceFit:true},
			cm: new Ext.grid.ColumnModel({
				defaults: { width: 55 },
				columns: [this.checkSm,
					{ id : 'e',header: cm[0], dataIndex: 'fee_name',renderer:App.qtipValue},
					{ header: cm[1], dataIndex: 'count', width: 25,
						editor : new Ext.form.NumberField({
							allowBlank : false,
							minValue : 1,
							allowNegative : false
						})},
					{ header: cm[2], dataIndex: 'default_value',width : 25,
						editor : new Ext.form.NumberField({
							allowBlank : false,
							allowNegative : false
						})},
					{ header : cm[3], dataIndex: 'feeValue',width : 25}
				]
			}),
			tbar : [langUtils.bc("common.fee.tbar0"),'->',langUtils.bc("common.total"),":",{
								id: 'txtSumFee',
								readOnly: true,
								style: Constant.MONEY_STYLE,
								xtype: 'textfield',
								width: 50,
								value: 0
							},'&nbsp;']
		});
	},
	initEvents : function(){
		this.on('beforeedit',this.beforeEdit,this);
		this.on('afteredit',this.afterEdit,this);
		ServiceGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(obj.field == 'default_value'){
			//0001175: 业务费用单价修改  取消修改属性
//			if(obj.record.get('can_update') == 'F'){
//				return false;
//			}
			if(obj.record.get('max_value') == obj.record.get('default_value') && obj.record.get('min_value') == obj.record.get('default_value')){
				return false;
			}
		}
	},
	afterEdit : function(obj){
		var flag = true;//编辑是否合法
		if(obj.field == 'default_value'){
			if(obj.record.get('max_value') != 0 && obj.value > obj.record.get('max_value')){
				Alert('单价不能大于配置的最大值'+obj.record.get('max_value'));
				obj.record.set('default_value',obj.originalValue);
				flag = false;
			}else if(obj.value < obj.record.get('min_value')){
				Alert('单价不能小于配置的最小值'+obj.record.get('min_value'));
				obj.record.set('default_value',obj.originalValue);
				flag = false;
			}
		}
		
		if(flag){
			obj.record.set('feeValue',obj.record.data.count * obj.record.data.default_value);
			
			if(this.getSelectionModel().isSelected(obj.row)){
				var cmp = Ext.getCmp('txtSumFee');
				if(obj.field == 'default_value'){
					var change = obj.record.get('count') * (obj.record.get('default_value') - obj.originalValue);
				}else if(obj.field == 'count'){
					var change = obj.record.get('default_value') * (obj.record.get('count') - obj.originalValue);
				}
				cmp.setValue(Ext.util.Format.toDecimal((parseFloat(cmp.getValue()) + change)));
			}
		}
	},
	doSelectedChange : function(sm){
		var records = sm.getSelections();
		var totalBusiFee = 0;
		for(var i=0;i<records.length;i++){
			totalBusiFee = totalBusiFee + records[i].get('count') * records[i].get('default_value');
		}
		Ext.getCmp('txtSumFee').setValue(totalBusiFee);
	}, 
	getValues: function(){
		this.stopEditing();
		var records = this.getSelectionModel().getSelections();
		var data = [];
		for(var i =0;i< records.length; i++){
			var obj = {};
			obj['fee_id'] = records[i].get("fee_id");
			obj['count'] = records[i].get("count");
			obj['real_pay'] = Ext.util.Format.formatToFen(records[i].get("feeValue"));
			obj['should_pay'] = Ext.util.Format.formatToFen(records[i].get("feeValue"));
			data.push(obj);
		}
		return data;
	}
});
