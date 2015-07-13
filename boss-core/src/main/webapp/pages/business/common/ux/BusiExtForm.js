/**
 * 业务扩展表单
 */
 
 BusiExtForm  = Ext.extend( BaseForm ,{
	 constructor: function( attrData){
	 	var items = this.gItems(attrData).concat([{
	 		xtype: 'textarea',
	 		name: 'remark',
	 		fieldLabel: '备注信息',
	 		anchor: '95%',
	 		height: 40
	 	}]);
	 	
//	 	var items = this.gItems(attrData);
		BusiExtForm.superclass.constructor.call(this, {
			title: '业务信息',
			labelWidth: 70,
			bodyStyle: Constant.TAB_STYLE,
			items: items
		});
	},
	initComponent: function(){
		BusiExtForm.superclass.initComponent.call(this);
		//初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		if(comboes.length == 0){
			this.doInit();
		}else{
			App.form.initComboData( comboes,function(){
				var comboes = this.findByType("paramcombo");
				for(var index =0;index<comboes.length;index++){
					var combo = comboes[index];
					var val = combo.getValue();
					combo.setValue('');
					combo.setValue(val);
				}
			},this);
		}
	},
	/**
	 * 为表单添加Items
	 */
	gItems: function( data ){
		var items = [];
		if(data){
			for(var i=0;i<data.length;i++){
				items.push(this.createField(data[i]));
			}
		}
		return items;
	},
	/** 
	 * 根据扩展属性信息创建表单控件
	 */
	createField: function(attr){
		var o = {
			xtype: attr["input_type"],
			anchor: '95%',
			fieldLabel: attr["attribute_name"],
			name: attr["attribute_id"],
			value:attr['default_value'],
            allowBlank: attr["is_null"] == 'F'? false: true,
            format: 'Y-m-d H:i:s'
		};
		if(attr["input_type"] == 'paramcombo'){
			Ext.apply( o , {
				paramName: attr["param_name"],
				hiddenName: o["name"]
			});
		}
		if(attr["input_type"] == 'textarea'){
			o["height"] = 40;
		}
		return o;
	}
 });