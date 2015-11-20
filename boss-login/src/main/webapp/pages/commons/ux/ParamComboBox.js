Ext.namespace("Ext.ux");

var loadParamValues = function(store){
	if(this.isFilter === true){
		var countyId = App.getData().optr.county_id;
		store.each(function(record){
			var showCountyId = record.get('show_county_id');
			if(!Ext.isEmpty(showCountyId) && showCountyId != '4501'){
				var countyArr = showCountyId.split(',');
				if(countyArr.indexOf(countyId) == -1){
					store.remove(record);
				}
			}
		});
	}
};

Ext.ux.ParamCombo = Ext.extend(Ext.form.ComboBox, {
	paramName:null,//必须指定参数名称
	defaultValue:null,
	allowBlankItem:false,
	isContrary:false,//item_name,item_value 反显示
//	isFilter:true,			//是否按县市过滤数据字典
	constructor:function(config){
		config = config || {};
		config = Ext.applyIf(config || {}, {
			editable: false,
			forceSelection: true,
			displayField: 'item_name',
			valueField: 'item_value',
			triggerAction: 'all',
			mode: 'local'
		});

		this.store = new Ext.data.JsonStore({
			fields:['item_value','item_name','show_county_id','item_idx'],
			autoDestroy:true,
			baseParams:{paramName:this.paramName}/*,
			listeners:{
				scope:this,
				load: loadParamValues
			}*/
		});
		Ext.ux.ParamCombo.superclass.constructor.apply(this, arguments);
	}
});

Ext.reg('paramcombo', Ext.ux.ParamCombo);


if(Ext.ux.form && Ext.ux.form.LovCombo){
//多选下拉框关联系统参数的下拉框
Ext.ux.ParamLovCombo = Ext.extend(Ext.ux.form.LovCombo, {
	paramName:null,//必须指定参数名称
	defaultValue:null,
	allowBlankItem:false,
	beforeBlur:function(){},
	isFilter:true,			//是否按县市过滤数据字典
	constructor:function(config){
		config = config || {};
		config = Ext.applyIf(config || {}, {
			editable: false,
			forceSelection: true,
			displayField: 'item_name',
			valueField: 'item_value',
			triggerAction: 'all',
			mode: 'local'
		});

		this.store = new Ext.data.JsonStore({
			fields:['item_value','item_name','show_county_id'],
			autoDestroy:true,
			baseParams:{paramName:this.paramName},
			listeners:{
				scope:this,
				load:loadParamValues
			}
		});
		Ext.ux.ParamCombo.superclass.constructor.apply(this, arguments);
	}
});

Ext.reg('paramlovcombo', Ext.ux.ParamLovCombo);
}